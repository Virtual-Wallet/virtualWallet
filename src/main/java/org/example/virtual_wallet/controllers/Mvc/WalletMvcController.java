package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.WalletMapper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.Wallet;
import org.example.virtual_wallet.models.dtos.WalletDto;
import org.example.virtual_wallet.services.CurrencyServiceImpl;
import org.example.virtual_wallet.services.contracts.CurrencyService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.example.virtual_wallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wallets")
public class WalletMvcController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final WalletMapper walletMapper;
    private final CurrencyService currencyService;

    @Autowired
    public WalletMvcController(WalletService walletService,
                               AuthenticationHelper authenticationHelper,
                               UserService userService, WalletMapper walletMapper, CurrencyService currencyService) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.walletMapper = walletMapper;
        this.currencyService = currencyService;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/new")
    public String showCreateWalletPage(@Valid Model model,
                                       HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
        User user = authenticationHelper.tryGetCurrentUser(session);

        try {
            model.addAttribute("user", user);
            model.addAttribute("wallet", new WalletDto());
            model.addAttribute("currencies",currencyService.getAll());
            return "WalletCreateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/new")
    public String createWallet(@Valid @ModelAttribute("wallet") WalletDto walletDto,
                               BindingResult errors,
                               Model model,
                               HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
        User user = authenticationHelper.tryGetCurrentUser(session);
        if (errors.hasErrors()) {
            return "WalletCreateView";
        }

        try {
            model.addAttribute("currencies",currencyService.getAll());
            Wallet wallet = walletMapper.dtoWalletCreate(walletDto,user);
            walletService.create(wallet, user);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

}
