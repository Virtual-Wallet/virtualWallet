package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserFilterDto;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminMvc {
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public AdminMvc(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping
    public String showAdminView(
            @ModelAttribute("filter") UserFilterDto userFilterDto,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model,
            HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterDto.getUsername(),
                userFilterDto.getPhoneNumber(),
                userFilterDto.getEmail(),
                userFilterDto.getSortBy(),
                userFilterDto.getSortOrder());


        try {
            List<User> filteredList = userService.getAllFiltered(userFilterOptions, user);
            Page<User> usersPage = userService.findPage(filteredList, PageRequest.of(currentPage - 1, pageSize));
            int totalPages = usersPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.iterate(totalPages, i -> i - 1)
                        .limit(totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("filterOptions", userFilterDto);
            model.addAttribute("users", usersPage);

            return "AdminView";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/{userToBan}/ban")
    public String handleBanUser(@PathVariable int userToBan, Model model, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        try {
            User userToBlock = userService.getById(userToBan);
            userService.blockUserByAdmin(userToBlock, admin);
            model.addAttribute("userToBlock", userToBlock);
            model.addAttribute("adminStatus", admin.getRoleType());
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            return "redirect:/admin";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/{userToActive}/active")
    public String handleUserActivation(@PathVariable int userToActive, Model model, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        try {
            User userToUnBan = userService.getById(userToActive);
            userService.unblockUserByAdmin(userToUnBan, admin);
            model.addAttribute("adminStatus", admin.getRoleType());
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            return "redirect:/admin";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/{userToPromote}/promote")
    public String handleUserPromotion(@PathVariable int userToPromote, Model model, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }
        try {
            User userForPromotion = userService.getById(userToPromote);
            userService.promoteUserToAdmin(userForPromotion, admin);
            model.addAttribute("adminStatus", admin.getRoleType());
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            return "redirect:/admin";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/authentication/login";
        }
    }
}
