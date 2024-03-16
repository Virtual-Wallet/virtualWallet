package org.example.virtual_wallet.services;

import org.example.virtual_wallet.filters.OrderFilterOptions;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.OrderFilterDto;
import org.example.virtual_wallet.repositories.contracts.OrderSearchDao;
import org.example.virtual_wallet.services.contracts.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderSearchDao orderSearchDao;

    public OrderServiceImpl(OrderSearchDao orderSearchDao) {
        this.orderSearchDao = orderSearchDao;
    }

    @Override
    public List<OrderFilterDto> getFiltered(OrderFilterOptions orderFilterOptions, User user) {
        return orderSearchDao.getFiltered(orderFilterOptions, user);
    }
}
