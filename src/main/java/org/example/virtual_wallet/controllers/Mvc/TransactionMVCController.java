package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsInternalMapper;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("transactions")
public class TransactionMVCController {

    private final TransactionsInternalService service;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsInternalMapper transactionsInternalMapper;

    @Autowired
    public TransactionMVCController(TransactionsInternalService service, UserService userService, AuthenticationHelper authenticationHelper, TransactionsInternalMapper transactionsInternalMapper) {
        this.service = service;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionsInternalMapper = transactionsInternalMapper;
    }

//    @GetMapping("/verify")
//    public String showTransactionVerification(Model model, HttpSession httpSession){
//
//    }

}
