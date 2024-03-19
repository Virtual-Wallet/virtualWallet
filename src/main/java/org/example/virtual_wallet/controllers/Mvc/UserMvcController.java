package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private static final String CURRENT_USER = "currentUser";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute(CURRENT_USER) != null;
    }

    @GetMapping("/MyProfile")
    public String showUserPage(UserDto userDto,
                               Model model,
                               HttpSession session) {

        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        try {
            UserDto userDetails = userMapper.userToDto(currentUser);
            model.addAttribute("currentUser", userDetails);
            return "ProfileView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/edit")
    public String showUserEditView(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            UserDto userDetails = userMapper.userToDto(user);
            model.addAttribute("dto", userDetails);
            return "UserEditView";
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/edit")
    public String handleUserEdit(@Valid @ModelAttribute("dto") UserDto userDto,
                                 HttpSession session,
                                 BindingResult bindingResult,
                                 Model model) {
        User user;
        if (bindingResult.hasErrors()) {
            model.addAttribute(CURRENT_USER, userDto);
            return "UserEditView";
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        try {
            User toUpdate = userMapper.updateUser(user.getId(), userDto);
            userService.update(toUpdate);
            return "redirect:/";
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("phoneNumber", "phone-exist", "Phone number already exists");
            return "UserEditView";
        } catch (EmailDuplicateException e) {
            bindingResult.rejectValue("email", "email-exist", "Email already exists");
            return "UserEditView";
        }
    }

    @GetMapping("/change-password")
    public String showUserEditPasswordView(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            UserDto passwordDetails = userMapper.userToDto(user);
            model.addAttribute("passwordDto", passwordDetails);
            return "PasswordChangeView";
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/change-password")
    public String handleUserEditPassword(@Valid @ModelAttribute("passwordDto") UserDto userDto,
                                         HttpSession session,
                                         BindingResult bindingResult,
                                         Model model) {
        User user;

        if (bindingResult.hasErrors()) {
            return "PasswordChangeView";
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        User toUpdatePassword = userMapper.updatePassword(user.getId(), userDto);
        if (!toUpdatePassword.getPassword().equals(userDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "missmatchPassword", "Confirm password doesn't match password");
            return "PasswordChangeView";
        }
        userService.update(toUpdatePassword);
        return "redirect:/authentication/login";
    }

    @GetMapping("/remove/{userId}")
    public String removeFromContactList (@PathVariable int userId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            userService.removeUserFromContactList(user, userService.getById(userId));
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/add/{userId}")
    public String addToContactList (@PathVariable int userId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            userService.addUserToContactList(user, userService.getById(userId));
            return "HomePageView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
}
