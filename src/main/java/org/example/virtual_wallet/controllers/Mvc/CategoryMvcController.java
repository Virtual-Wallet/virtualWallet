package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jdk.jfr.Category;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.SpendingCategoryMapper;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.models.dtos.CategoryUpdateDto;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.SpendingCategoryService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryMvcController {
    private final SpendingCategoryService service;
    private final SpendingCategoryMapper mapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public CategoryMvcController(SpendingCategoryService service, SpendingCategoryMapper mapper, AuthenticationHelper authenticationHelper, UserService userService) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("categories")
    public List<SpendingCategory> categories() {
        return new ArrayList<>();
    }
    @ModelAttribute("categoryDto")
    public CategoryDto categoryDto() {
        return new CategoryDto();
    }

    @GetMapping
    public String showCategoriesPage(Model model, HttpSession session) {

        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);

        try {
            List<SpendingCategory> categories = service.getAllUserCategories(user);
            model.addAttribute("categories", categories);
            model.addAttribute("currentUser", user);
            model.addAttribute("categoryDto", new CategoryDto());
            return "categoriesView";
        } catch (EntityNotFoundException e) {
            return "categoriesView";
        }
    }

    @PostMapping("/new")
    public String createCategory(@ModelAttribute("categoryDto") CategoryDto categoryDto, HttpSession session,
                                 Model model) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);

        try {
            SpendingCategory category = mapper.fromDTO(categoryDto, user);
            service.create(category, user);
            return "redirect:/categories";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }catch (EntityDuplicateException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }


    }

    @GetMapping("/{name}/update")
    public String showEditCategoryPage(@PathVariable String name, Model model,
                                       HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            SpendingCategory category = service.getByCategoryAndUser(name, user);
            CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();
            categoryUpdateDto.setName(category.getName());
            categoryUpdateDto.setId(category.getSpendingCategoryId());
            model.addAttribute("category", categoryUpdateDto);
            return "CategoryUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{name}/update")
    public String updateCategory(@PathVariable String name,
                                 @Valid @ModelAttribute("category") CategoryUpdateDto categoryUpdateDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        if (bindingResult.hasErrors()) {
            return "CategoryUpdateView";
        }
        try {
            SpendingCategory category = service.getByCategoryAndUser(name, user);
            category.setName(categoryUpdateDto.getName().toUpperCase());
            service.update(category, user);
            return "redirect:/categories";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }


    @GetMapping("/{name}/delete")
    public String deleteCategory(@PathVariable String name, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            service.delete(service.getByCategoryAndUser(name, user).getSpendingCategoryId(), user);
            return "redirect:/categories";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (InvalidOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }
    }

}
