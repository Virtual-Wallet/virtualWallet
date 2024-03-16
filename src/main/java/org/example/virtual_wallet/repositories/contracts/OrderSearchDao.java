package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderDto;

import java.util.List;

public interface OrderSearchDao {
    List<OrderDto> getAll(User user);
}
