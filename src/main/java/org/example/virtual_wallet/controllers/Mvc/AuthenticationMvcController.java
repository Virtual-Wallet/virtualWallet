package org.example.virtual_wallet.controllers.Mvc;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.identity.VerificationSession;
import com.stripe.net.Webhook;
import com.stripe.param.identity.VerificationSessionCreateParams;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.enums.AccountStatus;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.Sessions;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.LogInDto;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.repositories.SessionsRepository;
import org.example.virtual_wallet.services.contracts.EmailService;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/authentication")
public class AuthenticationMvcController {
    private static final String EMAIL_SUBJECT = "Verification code from Flex Pay";
    private static final String CURRENT_USER = "currentUser";
    private static String verifiedUser = "";
    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    private final SessionsRepository sessionsRepository;

    public AuthenticationMvcController(EmailService emailService,
                                       UserService userService,
                                       TokenService tokenService,
                                       UserMapper userMapper,
                                       AuthenticationHelper authenticationHelper,
                                       SessionsRepository sessionsRepository) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.sessionsRepository = sessionsRepository;
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

    @GetMapping("/id-verification")
    public String verifyUserIdentity(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }

        return "identity-verification";
    }

    @GetMapping("/submit-session")
    public String getConfirmation(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }

        return "submitted";
    }

    @PostMapping("/id-verification")
    public ResponseEntity<Map<String, String>>
    createVerificationObject() throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        VerificationSessionCreateParams params =
                VerificationSessionCreateParams.builder()
                        .setType(VerificationSessionCreateParams.Type.DOCUMENT)
                        .setOptions(
                                VerificationSessionCreateParams.Options.builder()
                                        .setDocument(
                                                VerificationSessionCreateParams.Options.Document.builder()
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();


        VerificationSession verificationSession = VerificationSession.create(params);

        Map<String, String> responseBody = new HashMap<>();

        Sessions secret = new Sessions();
        secret.setSecret(verificationSession.getClientSecret());

        sessionsRepository.create(secret);

        responseBody.put("client_secret", verificationSession.getClientSecret());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader,
                                                Model model,
                                                HttpSession session) throws StripeException {
        Stripe.apiKey = "sk_test_51OnmGhDmJUkFXVmIja9A5hf1B0czvxYcdSZeHWQF9dLKYKv3BDs4KnqHXwqkHWSz5pyUOVzYLaEU8Wz69aUP10A900QPBpu8mk";

        String endpointSecret = "whsec_321b6046a4d78a5327e1c9a97c113cdd4833a1871aa4ed09c0b61694c82f2f5d";

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (JsonSyntaxException | SignatureVerificationException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            System.out.println("Webhook error while parsing basic request.");
            return ResponseEntity.badRequest().build();
        }

        VerificationSession verificationSession = null;
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        switch (event.getType()) {
            case "identity.verification_session.verified":
                // All the verification checks passed
                verificationSession = (VerificationSession) dataObjectDeserializer.getObject().get();
                if (dataObjectDeserializer.getObject().isPresent()) {
                    if (sessionsRepository.getSecret(verificationSession.getClientSecret()) != null) System.out.println("yes");;
                } else {
                    // Deserialization failed, probably due to an API version mismatch.
                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                    // instructions on how to handle this case, or return an error here.
                }
                break;
            case "identity.verification_session.requires_input":
                // At least one of the verification checks failed
                if (dataObjectDeserializer.getObject().isPresent()) {
                    verificationSession = (VerificationSession) dataObjectDeserializer.getObject().get();
                    switch (verificationSession.getLastError().getCode()) {
                        case "document_unverified_other":
                            // the document was invalid
                            break;
                        case "document_expired":
                            // the document was expired
                            break;
                        case "document_type_not_supported":
                            // document type not supported
                            break;
                        default:
                            // ...
                            break;
                    }
                } else {
                    // Deserialization failed, probably due to an API version mismatch.
                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                    // instructions on how to handle this case, or return an error here.
                }
                break;
            default:
                // other event type
                break;
        }
        // Response status 200:
        return ResponseEntity.ok().build();
    }
}
