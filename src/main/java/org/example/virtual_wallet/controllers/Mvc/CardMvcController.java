package org.example.virtual_wallet.controllers.Mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.CardMapper;
import org.example.virtual_wallet.models.Card;
import org.example.virtual_wallet.models.SpendingCategory;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.CardDto;
import org.example.virtual_wallet.models.dtos.CategoryDto;
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
//        try{
//            authenticationHelper.tryGetCurrentUser(session);
//        }catch (AuthorizationException e){
//            model.addAttribute("error", e.getMessage());
//            return "redirect:/auth/login";
//        }
//
//        User user = authenticationHelper.tryGetCurrentUser(session);

        User user = userService.getById(1);

//        try {
            List<Card> cards = service.getUserCards(user);
            model.addAttribute("cards", cards);
            model.addAttribute("currentUser", user);
//            model.addAttribute("cardDto", new CardDto());
            return "cardsView";
//        } catch (EntityNotFoundException e) {
//            return "NotFoundView";
//        }
    }

    @GetMapping("/update/{cardId}")
    public String showEditCardPage(@PathVariable int cardId,
                                   @Valid Model model,
                                   HttpSession session) {
//        try {
//            authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e){
//            model.addAttribute("error", e.getMessage());
//            return "UnauthorizedView";
//        }
        //        User user = authenticationHelper.tryGetCurrentUser(session);

        User user = userService.getById(1);


//        try {
            Card card = service.getById(cardId);
            CardDto cardDto = cardMapper.toCardDto(card);
            model.addAttribute("user", user);
            model.addAttribute("cardDto", cardDto);
            return "CardUpdateView";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "NotFoundView";
//        }
    }

    @PostMapping("/update/{cardId}")
    public String editCard(@PathVariable int cardId,
                           @Valid @ModelAttribute("cardDto") CardDto cardDto,
                           BindingResult errors,
                           Model model) {

//        try {
//            authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e){
//            model.addAttribute("error", e.getMessage());
//            return "UnauthorizedView";
//        }
            //        User user = authenticationHelper.tryGetCurrentUser(session);

            User user = userService.getById(1);
        if (errors.hasErrors()) {
            return "CardUpdateView";
        }

//        try {
            Card card = cardMapper.dtoCardUpdate(cardDto, cardId);
            service.update(card,user);
            return "redirect:/cards";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "NotFoundView";
//        } catch (UnauthorizedOperationException e) {
//            model.addAttribute("error", e.getMessage());
//            return "UnauthorizedView";
//        }
    }

}
