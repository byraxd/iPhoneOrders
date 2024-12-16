package org.example.service;

import org.example.dto.OrderDto;
import org.example.dto.PayRequestDto;
import org.example.model.Order;

public interface OrderService {

    Order createOrder(OrderDto orderDto);

    Order updateOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);

    void deleteExpiredOrders();

    Order payForOrder(PayRequestDto payRequestDto);

}
