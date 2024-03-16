package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderDto;
import org.example.virtual_wallet.services.contracts.OrderService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<OrderDto> getAll() {

        User user = userService.getById(1);
        return orderService.getAll(user);
    }
}
