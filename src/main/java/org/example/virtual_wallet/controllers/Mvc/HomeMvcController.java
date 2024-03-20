package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.Currency;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;
    private final CurrencyService currencyService;
    private AuthenticationHelper authenticationHelper;
    private TransactionsInternalService transactionsInternalServices;

    public HomeMvcController(UserService userService, CurrencyService currencyService,
                             AuthenticationHelper authenticationHelper,
                             TransactionsInternalService transactionsInternalServices) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.authenticationHelper=authenticationHelper;
        this.transactionsInternalServices=transactionsInternalServices;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @ModelAttribute("allCurrencies")
    public int allCurrencies() {
        return currencyService.getAll().size();
    }
    @ModelAttribute("transactionsIncoming")
    public List<TransactionsInternal> transactionsIncoming() {
        return new ArrayList<>();
    }
    @ModelAttribute("transactionsOutgoing")
    public List<TransactionsInternal> transactionsOutgoing() {
        return new ArrayList<>();
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {


//        User user;
//
//        try {
//            user = authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/authentication/login";
//        }

        if (populateIsAuthenticated(session)) {
            User user = authenticationHelper.tryGetCurrentUser(session);
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByUsername(currentUsername));
            List<TransactionsInternal> transactionsIncoming = transactionsInternalServices.getIncoming(user);
            List<TransactionsInternal> transactionsOutgoing = transactionsInternalServices.getOutgoing(user);
            model.addAttribute("transactionsIncoming", transactionsIncoming);
            model.addAttribute("transactionsOutgoing", transactionsOutgoing);
            model.addAttribute("currentUser", user);
            model.addAttribute("transactionDto", new TransactionsInternal());

            return "HomePageView";
        } else {
            return "index";
        }


    }



}
