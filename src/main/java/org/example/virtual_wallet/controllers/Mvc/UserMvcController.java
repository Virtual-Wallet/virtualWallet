package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EmailDuplicateException;
import org.example.virtual_wallet.exceptions.EntityDuplicateException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.UserMapper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.UserService;
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

    @GetMapping("/{userId}")
    public String showUserPage(@PathVariable int userId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        try {
            User user = userService.getById(userId);
            model.addAttribute("user", user);
            return "ProfileView";
        } catch (EntityNotFoundException e) {
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

}
