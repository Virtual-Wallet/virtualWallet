package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.models.TransactionsInternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.services.contracts.TransactionsInternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"api/transactions"})
public class TransactionsInternalRestController {

    private final TransactionsInternalService service;
    private final UserService userService;


    // GV TODO: Add mapper perhaps?
    public TransactionsInternalRestController(TransactionsInternalService service,
                                              UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<TransactionsInternal> getAll(/*@RequestHeader HttpHeaders headers*/) {
        /*User user = authenticationHelper.tryGetUser(headers);*/
        // GV TODO: To be finished after verification implemented
        return service.getAll();
    }

    @GetMapping("/outgoing")
    public List<TransactionsInternal> getOutgoing() {

        //GV TODO Replace this one with current user;
        User user = userService.getById(3);
        return service.getOutgoing(user);
    }

    @GetMapping("/incoming")
    public List<TransactionsInternal> getIncoming() {

        //GV TODO Replace this one with current user;
        User user = userService.getById(3);
        return service.getIncoming(user);
    }
}
