package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAll(User user);
}
