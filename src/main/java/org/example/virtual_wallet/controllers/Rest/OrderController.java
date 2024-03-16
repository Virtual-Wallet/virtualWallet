package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.exceptions.UnauthorizedOperationException;
import org.example.virtual_wallet.filters.OrderFilterOptions;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderFilterDto;
import org.example.virtual_wallet.services.contracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public OrderController(OrderService orderService,
                           AuthenticationHelper authenticationHelper) {
        this.orderService = orderService;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping
    public List<OrderFilterDto> getFiltered(@RequestHeader HttpHeaders headers,
                                            @RequestParam(required = false) String type,
                                            @RequestParam(required = false) int category,
                                            @RequestParam(required = false) int contractor,
                                            @RequestParam(required = false) Timestamp date,
                                            @RequestParam(required = false) int currency,
                                            @RequestParam(required = false) double amount,
                                            @RequestParam(required = false) String sortBy,
                                            @RequestParam(required = false) String sortOrder,
                                            @RequestHeader(name = "Credentials") String credentials) {

        OrderFilterOptions orderFilterOptions = new OrderFilterOptions(
                type,
                category,
                contractor,
                date,
                currency,
                amount,
                sortBy,
                sortOrder);

        try {
            User user = authenticationHelper.tryGetUser(headers);
            return orderService.getFiltered(orderFilterOptions, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
