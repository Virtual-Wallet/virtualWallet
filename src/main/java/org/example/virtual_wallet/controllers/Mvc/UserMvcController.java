package org.example.virtual_wallet.controllers.Mvc;

import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.services.contracts.EmailService;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserMvcController {
    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;
    public UserMvcController(EmailService emailService, UserService userService, TokenService tokenService) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
    }



}
