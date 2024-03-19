package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.models.dtos.TransactionDto;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
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
    private final TransactionsInternalService transactionsInternalService;
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.transactionsInternalService = transactionsInternalService;
        this.authenticationHelper = authenticationHelper;
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

        User user;

        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        try {
            List<TransactionsInternal> transactionsIncoming = transactionsInternalService.getIncoming(user);
            List<TransactionsInternal> transactionsOutgoing = transactionsInternalService.getOutgoing(user);
            model.addAttribute("transactionsIncoming", transactionsIncoming);
            model.addAttribute("transactionsOutgoing", transactionsOutgoing);
            model.addAttribute("currentUser", user);
            model.addAttribute("transactionDto", new TransactionsInternal());
            return "HomePageView";
        } catch (EntityNotFoundException e) {
            return "NotFoundView";
        }
    }

}
