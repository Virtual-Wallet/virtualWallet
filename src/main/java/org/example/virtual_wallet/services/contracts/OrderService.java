package org.example.virtual_wallet.services.contracts;

import org.example.virtual_wallet.filters.OrderFilterOptions;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderFilterDto;

import java.util.List;

public interface OrderService {
    List<OrderFilterDto> getFiltered(OrderFilterOptions orderFilterOptions, User user);
}
