package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.enums.AccountStatus;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.LogInDto;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.EmailService;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authentication")
public class AuthenticationMvcController {
    private static final String EMAIL_SUBJECT = "Verification code from Flex Pay";
    private static final String CURRENT_USER = "currentUser";

    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    public AuthenticationMvcController(EmailService emailService,
                                       UserService userService,
                                       TokenService tokenService,
                                       UserMapper userMapper,
                                       AuthenticationHelper authenticationHelper) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute(CURRENT_USER) != null;
    }


    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LogInDto());
        return "LogInView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LogInDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "LogInView";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());

            if (user.getAccountStatus() == AccountStatus.PENDING_EMAIL) {
                return "redirect:/authentication/verify";
            }
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LogInView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute(CURRENT_USER);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") UserDto register,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }
        if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Passwords doesn't match");
        }
        try {
            User user = userMapper.dtoUserCreate(register);
            userService.create(user);
            Token token = tokenService.create(user);
            emailService.sendEmail(user.getEmail(), EMAIL_SUBJECT, token.getCode());
            return "redirect:/authentication/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("phoneNumber", "phone_error", e.getMessage());
            return "RegisterView";
        } catch (UsernameDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "RegisterView";
        } catch (EmailDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "RegisterView";
        }
    }

    @GetMapping("/verify")
    public String showTokenVerificationPage(Model model) {
        model.addAttribute("token", new Token());
        return "TokenVerificationView";
    }

    @PostMapping("/verify")
    public String handleTokenVerification(@Valid @ModelAttribute("token") Token token,
                                          BindingResult bindingResult,
                                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "TokenVerificationView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            token = tokenService.getUserToken(user.getId());
            tokenService.validateCorrectToken(token, user);
            session.setAttribute(CURRENT_USER, user);
            userService.advanceAccountStatus(user);

//            return "redirect:/id-verification";
            return "redirect:/";
        } catch (InvalidTokenException | EntityNotFoundException e) {
            bindingResult.rejectValue("code", "code_error", e.getMessage());
            return "TokenVerificationView";
        }
    }

    @GetMapping("/resend")
    public String handleResendToken(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Token token = tokenService.create(user);
            emailService.sendEmail(user.getEmail(), EMAIL_SUBJECT, token.getCode());
            redirectAttributes.addFlashAttribute("message", "Token has been resent.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while trying to resend the token.");
        }
        return "redirect:/authentication/verify";
    }


}
