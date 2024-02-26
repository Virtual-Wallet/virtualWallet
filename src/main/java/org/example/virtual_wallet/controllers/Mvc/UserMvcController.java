package org.example.virtual_wallet.controllers.Mvc;

import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserMvcController {

    private final UserService userService;
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }
}
