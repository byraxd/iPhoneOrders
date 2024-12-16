package org.example.service;

import org.example.model.Order;

public interface OrderService {

    Order createOrder(Order order);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);

}
