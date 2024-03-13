package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.services.contracts.EmailService;
import org.example.virtual_wallet.services.contracts.TokenService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailRestController {

    private static final String EMAIL_SUBJECT = "Verification code from Flex Pay";
    private static final String LARGE_TRANSACTION_SUBJECT = "Verification code for large transaction from Flex Pay";
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public EmailRestController(EmailService emailService, TokenService tokenService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/send")
    public ResponseEntity sendEmail(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        Token token = tokenService.create(user);
        emailService.sendEmail(user.getEmail(), EMAIL_SUBJECT, token.getCode());
        return ResponseEntity.ok("EMAIL SEND!");
    }
}
