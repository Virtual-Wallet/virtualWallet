package org.example.virtual_wallet.repositories.contracts;

import org.example.virtual_wallet.filters.OrderFilterOptions;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderFilterDto;

import java.util.List;

public interface OrderSearchDao {
    List<OrderFilterDto> getFiltered(OrderFilterOptions orderFilterOptions, User user);
}
