package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.CardMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.models.dtos.CategoryDto;
import org.example.virtual_wallet.models.dtos.CategoryUpdateDto;
import org.example.virtual_wallet.services.contracts.CardService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardMvcController {
    private final CardService service;
    private final UserService userService;
    private final CardMapper cardMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CardMvcController(CardService cardService,
                             UserService userService,
                             CardMapper cardMapper,
                             AuthenticationHelper authenticationHelper) {
        this.service = cardService;
        this.userService = userService;
        this.cardMapper = cardMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
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

}
