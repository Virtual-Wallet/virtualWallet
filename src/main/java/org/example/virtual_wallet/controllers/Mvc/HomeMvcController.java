package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;
    private final CurrencyService currencyService;

    public HomeMvcController(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

//    @ModelAttribute("allUsers")
//    public int allUsers() {
//        return userService.getAll().size();
//    }

    @ModelAttribute("allCurrencies")
    public int allCurrencies() {
        return currencyService.getAll().size();
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
//        model.addAttribute("usersCount", userService.getAll(new FilterOptionsUser()).size());
//        model.addAttribute("currenciesCount", currencyRepository.getAll().size());
//        model.addAttribute("transactionsCount", transactionService.get(
//                        new FilterOptionsTransaction(null, null, null, null, null, null, null, null))
//                .size());

        if (populateIsAuthenticated(session)) {
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByUsername(currentUsername));
            return "HomePageView";
        } else {
            return "index";
        }


    }



}
