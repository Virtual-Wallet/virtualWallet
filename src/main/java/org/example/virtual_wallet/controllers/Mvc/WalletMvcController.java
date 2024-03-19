package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.exceptions.InsufficientAmountException;
import org.example.virtual_wallet.exceptions.LargeTransactionDetectedException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsInternalMapper;
import org.example.virtual_wallet.helpers.mappers.WalletMapper;
import org.example.virtual_wallet.models.*;
import org.example.virtual_wallet.models.dtos.TransactionDto;
import org.example.virtual_wallet.models.dtos.TransferDto;
import org.example.virtual_wallet.models.dtos.WalletDto;
import org.example.virtual_wallet.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/wallets")
public class WalletMvcController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final WalletMapper walletMapper;
    private final CurrencyService currencyService;
    private final SpendingCategoryService categoryService;
    private final TransactionsInternalService transactionsInternalService;
    private final TransactionsInternalMapper transactionsInternalMapper;

    @Autowired
    public WalletMvcController(WalletService walletService,
                               AuthenticationHelper authenticationHelper,
                               UserService userService,
                               WalletMapper walletMapper,
                               CurrencyService currencyService,
                               SpendingCategoryService categoryService,
                               TransactionsInternalService transactionsInternalService,
                               TransactionsInternalMapper transactionsInternalMapper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.walletMapper = walletMapper;
        this.currencyService = currencyService;
        this.categoryService = categoryService;
        this.transactionsInternalService = transactionsInternalService;
        this.transactionsInternalMapper = transactionsInternalMapper;
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
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
        User user = authenticationHelper.tryGetCurrentUser(session);

        try {
            model.addAttribute("user", user);
            model.addAttribute("wallet", new WalletDto());
            model.addAttribute("currencies", currencyService.getAll());
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
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
        User user = authenticationHelper.tryGetCurrentUser(session);
        if (errors.hasErrors()) {
            return "WalletCreateView";
        }

        try {
            model.addAttribute("currencies", currencyService.getAll());
            Wallet wallet = walletMapper.dtoWalletCreate(walletDto, user);
            walletService.create(wallet, user);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/transactions")
    public String showTransactionPage(
            @Valid Model model,
            HttpSession session) {

        User user = new User();

        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        try {
            model.addAttribute("user", user);
            model.addAttribute("transactionDto", new TransactionDto());
            model.addAttribute("currency", currencyService.getAll());
            model.addAttribute("category", categoryService.getAll());
            return "TransactionView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/transactions")
    public String withdraw(
            @Valid @ModelAttribute("transactionDto") TransactionDto transactionDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {
        User user = new User();

        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/authentication/login";
        }

        if (bindingResult.hasErrors()) {
            return "TransactionView";
        }

        try {
            TransactionsInternal transaction = transactionsInternalMapper.createDto(user, user, transactionDto);
            transactionsInternalService.create(transaction);

            return "SuccessfulTransactionView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (InsufficientAmountException e) {
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
