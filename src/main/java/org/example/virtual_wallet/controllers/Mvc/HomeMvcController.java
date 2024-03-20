package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
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
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(UserService userService, CurrencyService currencyService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("AdminRole")
    public RoleType populateIsAdmin() {
      return RoleType.ADMIN;
    }

//    @ModelAttribute("isAdmin")
//    public boolean populateIsAdmin(HttpSession session) {
//        User user = authenticationHelper.tryGetCurrentUser(session);
//        return user.getRoleType().equals(RoleType.ADMIN);
//    }

//    @ModelAttribute("allUsers")
//    public int allUsers() {
//        return userService.getAll().size();
//    }


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
