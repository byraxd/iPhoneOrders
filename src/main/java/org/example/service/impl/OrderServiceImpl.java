package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.weaver.ast.Or;
import org.example.model.Order;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order createOrder(Order order) {
        if(order == null) throw new NullPointerException("order is null");


        return orderRepository.save(
                Order
                        .builder()
                        .user(order.getUser())
                        .products(order.getProducts())
                        .isPaid(false)
                        .build()
        );
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        if(id == null || order == null) throw new NullPointerException("id or order is null");

        try {
            Order orderForUpdate = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("order not found by following id"));

            orderForUpdate.setUser(order.getUser());
            orderForUpdate.setProducts(order.getProducts());
            orderForUpdate.setIsPaid(order.getIsPaid());

            return orderRepository.save(orderForUpdate);
        }catch (OptimisticLockException e){
            throw new OptimisticLockException("Another user updated this record already", e);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        if(id == null) throw new NullPointerException("id is null");
        if(!orderRepository.existsById(id)) throw new NoSuchElementException("order not found by following id");

        orderRepository.deleteById(id);
    }
}
