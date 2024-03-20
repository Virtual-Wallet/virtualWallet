package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsInternalMapper;
import org.example.virtual_wallet.helpers.mappers.WalletMapper;
import org.example.virtual_wallet.models.*;
import org.example.virtual_wallet.models.dtos.TransactionDto;
import org.example.virtual_wallet.models.dtos.TransactionDtoIn;
import org.example.virtual_wallet.models.dtos.TransferDto;
import org.example.virtual_wallet.models.dtos.WalletDto;
import org.example.virtual_wallet.services.CurrencyServiceImpl;
import org.example.virtual_wallet.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/wallets")
public class WalletMvcController {
    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final WalletMapper walletMapper;
    private final CurrencyService currencyService;
    private final TransactionsInternalService transactionsInternalService;
    private final TransactionsInternalMapper transactionsInternalMapper;
    private final SpendingCategoryService categoryService;

    @Autowired
    public WalletMvcController(WalletService walletService,
                               AuthenticationHelper authenticationHelper,
                               UserService userService, WalletMapper walletMapper, CurrencyService currencyService, TransactionsInternalService transactionsInternalService, TransactionsInternalMapper transactionsInternalMapper, SpendingCategoryService categoryService) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.walletMapper = walletMapper;
        this.currencyService = currencyService;
        this.transactionsInternalService = transactionsInternalService;
        this.transactionsInternalMapper = transactionsInternalMapper;
        this.categoryService = categoryService;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("categories")
    public List<SpendingCategory> categories() {
        return new ArrayList<>();
    }
    @ModelAttribute("allCurrencies")
    public List<Currency>allCurrencies(){
        return currencyService.getAll();
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
    @GetMapping("/transfer")
    public String showTransferPage(
            @Valid Model model, HttpSession session){

        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);

        if (user.getWallet() == null){
            return "redirect:/";
        }
        try {
            model.addAttribute("user", user);
            model.addAttribute("transactionDto", new TransactionDtoIn());
            model.addAttribute("categories", categoryService.getAllUserCategories(user));
            return "TransferView";
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/categories";
        }
    }
    @PostMapping("/transfer")
    public String transfer(
            @Valid @ModelAttribute("transactionDto") TransactionDtoIn transactionDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);
        if (user.getWallet() == null){
            throw new InvalidOperationException("You do not have wallet created yet!");
        }

        if (bindingResult.hasErrors()){
            return "TransferView";
        }

        try {
            TransactionsInternal transactionsInternal = transactionsInternalMapper.createDto(user, transactionDto);
            transactionsInternalService.create(transactionsInternal);

            return "SuccessfulTransactionView";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }catch (InsufficientAmountException | InvalidOperationException | IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/transfer/{recipientId}")
    public String showContactTransferPage(@PathVariable int recipientId,
            @Valid Model model, HttpSession session){

        User user;

        try{
            user = authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        if (user.getWallet() == null){
            return "redirect:/";
        }
        try {
            User recipient = userService.getById(recipientId);
            TransactionDtoIn transactionDtoIn = new TransactionDtoIn();
            transactionDtoIn.setTargetUserIdentity(recipient.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("recipient", recipient);
            model.addAttribute("transactionDto", transactionDtoIn);
            model.addAttribute("categories", categoryService.getAllUserCategories(user));
            return "TransferView";
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/categories";
        }
    }
    @PostMapping("/transfer/{recipientId}")
    public String transfer(@PathVariable int recipientId,
            @Valid @ModelAttribute("transactionDto") TransactionDtoIn transactionDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        User user;

        try{
            user = authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        if (user.getWallet() == null){
            throw new InvalidOperationException("You do not have wallet created yet!");
        }

        if (bindingResult.hasErrors()){
            return "TransferView";
        }

        try {
            TransactionsInternal transactionsInternal = transactionsInternalMapper.createDto(user, transactionDto);
            transactionsInternalService.create(transactionsInternal);

            return "SuccessfulTransactionView";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }catch (InsufficientAmountException | InvalidOperationException | IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
