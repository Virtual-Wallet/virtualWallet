package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.CardMapper;
import org.example.virtual_wallet.helpers.mappers.TransactionsExternalMapper;
import org.example.virtual_wallet.models.*;
import org.example.virtual_wallet.models.dtos.*;
import org.example.virtual_wallet.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardMvcController {
    private final CardService service;
    private final UserService userService;
    private final CardMapper cardMapper;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsExternalMapper transactionsExternalMapper;
    private final TransactionsExternalService transactionsExternalService;
    private final CurrencyService currencyService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private static final String DUMMY_END_POINT = "http://localhost:8080/api/dummy";
    private final static String INSUFFICIENT_AVAILABILITY = "Insufficient availability. Try with different amount.";
    private static final String CURRENT_USER = "currentUser";



    @Autowired
    public CardMvcController(CardService cardService,
                             UserService userService,
                             CardMapper cardMapper,
                             AuthenticationHelper authenticationHelper, TransactionsExternalMapper transactionsExternalMapper, TransactionsExternalService transactionsExternalService, CurrencyService currencyService, TokenService tokenService, EmailService emailService) {
        this.service = cardService;
        this.userService = userService;
        this.cardMapper = cardMapper;
        this.authenticationHelper = authenticationHelper;
        this.transactionsExternalMapper = transactionsExternalMapper;
        this.transactionsExternalService = transactionsExternalService;
        this.currencyService = currencyService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("allCurrencies")
    public List<Currency>allCurrencies(){
        return currencyService.getAll();
    }
    @ModelAttribute("token")
    public Token token() {
        return new Token();
    }
    @GetMapping
    public String showCardsPage(Model model, HttpSession session) {
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            List<Card> cards = service.getUserCards(user);
            model.addAttribute("cards", cards);
            model.addAttribute("currentUser", user);
//            model.addAttribute("cardDto", new CardDto());
            return "cardsView";
        } catch (EntityNotFoundException e) {
            return "NotFoundView";
        }
    }

    @GetMapping("/update/{cardId}")
    public String showEditCardPage(@PathVariable int cardId,
                                   @Valid Model model,
                                   HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
                User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            Card card = service.getById(cardId);
            CardDto cardDto = cardMapper.toCardDto(card);
            model.addAttribute("user", user);
            model.addAttribute("cardDto", cardDto);
            return "CardUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/update/{cardId}")
    public String editCard(@PathVariable int cardId,
                           @Valid @ModelAttribute("cardDto") CardDto cardDto,
                           BindingResult errors,
                           Model model, HttpSession session) {

        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e){
            model.addAttribute("error", e.getMessage());
            return "redirect:/authentication/login";
        }
        User user = authenticationHelper.tryGetCurrentUser(session);

        if (errors.hasErrors()) {
            return "CardUpdateView";
        }

        try {
            Card card = cardMapper.dtoCardUpdate(cardDto, cardId);
            service.update(card,user);
            return "redirect:/cards";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable int id, Model model, HttpSession session){
                try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
                    return "redirect:/authentication/login";
                }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            service.delete(id,user);
            return "redirect:/cards";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }catch (InvalidOperationException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }
    }
    @GetMapping("/new")
    public String showCreateCardPage(@Valid Model model, HttpSession session){
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            model.addAttribute("user", user);
            model.addAttribute("card", new CardDto());
            return "CardNewView";
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/new")
    public String createCard(@Valid @ModelAttribute("card") CardDto cardDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session) {
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        if (bindingResult.hasErrors()){
            return "CardNewView";
        }

        try {
            Card card = cardMapper.dtoCardCreate(cardDto,user);
            service.create(card, user);
            return "redirect:/cards";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }catch (EntityDuplicateException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        }

    }

    @GetMapping("/deposit")
    public String showDepositPage(
            @Valid Model model, HttpSession session){

        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            model.addAttribute("user", user);
            model.addAttribute("depositDto", new TransferDto());
            model.addAttribute("cards", user.getCards());
            return "DepositView";
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/deposit")
    public String deposit(
                          @Valid @ModelAttribute("depositDto") TransferDto transferDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        if (bindingResult.hasErrors()){
            return "DepositView";
        }

        try {
            Card card = service.getByCardNumber(transferDto.getCardNumber());
            TransactionsExternal transfer = transactionsExternalMapper.depositDto(user, transferDto);
            String depositUrl = DUMMY_END_POINT + "/deposit";
            RestTemplate template = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<DummyDto> entity = new HttpEntity<>(transactionsExternalMapper
                    .transferToDummyDto(transfer), headers);
            ResponseEntity<String> response = template.exchange(depositUrl, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INSUFFICIENT_AVAILABILITY);
            }

             transactionsExternalService.createDeposit(transfer);

            return "SuccessfulTransactionView";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (RestClientException e){
            model.addAttribute("error", "Unsuccessful Transaction. Please check with your provider!");
            return "UnsuccessfulBankOperationView";
        }

    }
    @GetMapping("/withdraw")
    public String showWithdrawPage(
            @Valid Model model, HttpSession session){

        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        try {
            model.addAttribute("user", user);
            model.addAttribute("withdrawDto", new TransferDto());
            model.addAttribute("cards", user.getCards());
            return "WithdrawView";
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @PostMapping("/withdraw")
    public String withdraw(
            @Valid @ModelAttribute("withdrawDto") TransferDto transferDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {
        try{
            authenticationHelper.tryGetCurrentUser(session);
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }

        User user = authenticationHelper.tryGetCurrentUser(session);


        if (bindingResult.hasErrors()){
            return "WithdrawView";
        }

        try {
            TransactionsExternal transfer = transactionsExternalMapper.withdrawalDto(user, transferDto);
            transactionsExternalService.createWithdrawal(transfer);

            return "SuccessfulTransactionView";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (InsufficientAmountException e){
            model.addAttribute("error", e.getMessage());
            return "UnsuccessfulBankOperationView";
        } catch (LargeTransactionDetectedException e){
            Token token = tokenService.create(user);
            emailService.sendTransactionEmail(user.getEmail(), token.getCode());
            return "TransactionVerify";
        }

    }

    @GetMapping("/verify")
    public String showTokenVerificationPage(Model model) {
        model.addAttribute("token", new Token());
        return "TransactionVerify";
    }

    @PostMapping("/verify")
    public String handleTokenVerification(@Valid @ModelAttribute("token") Token token,
                                          BindingResult bindingResult,
                                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "TransactionVerify";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            token = tokenService.getUserToken(user.getId());
            tokenService.validateCorrectToken(token, user);
            session.setAttribute(CURRENT_USER, user);

            return "SuccessfulTransactionView";
        } catch (InvalidTokenException | EntityNotFoundException e) {
            bindingResult.rejectValue("code", "code_error", e.getMessage());
            return "TransactionVerify";
        }catch (AuthorizationException e){
            return "redirect:/authentication/login";
        }
    }

    @GetMapping("/resend")
    public String handleResendToken(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Token token = tokenService.create(user);
            emailService.sendTransactionEmail(user.getEmail(), token.getCode());
            redirectAttributes.addFlashAttribute("message", "Token has been resent.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while trying to resend the token.");
        }
        return "redirect:/cards/verify";
    }




}
