package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.services.contracts.TransactionsExternalService;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TransactionsInternalService transactionsService;
    private final TransactionsExternalService transfersService;

    @Autowired
    public HomeMvcController(AuthenticationHelper authenticationHelper, UserService userService, TransactionsInternalService transactionsService, TransactionsExternalService transfersService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.transactionsService = transactionsService;
        this.transfersService = transfersService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

//    @GetMapping
//    public String showHomePage(@ModelAttribute("TransactionsFilterDto") TransactionsFilterDto transactionsFilterDto,


}
