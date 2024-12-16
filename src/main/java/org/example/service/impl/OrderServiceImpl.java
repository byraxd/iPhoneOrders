package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.dto.OrderDto;
import org.example.dto.PayRequestDto;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(OrderDto orderDto) {
        if (orderDto == null) throw new NullPointerException("order is null");

        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new NoSuchElementException("user not found"));

        return orderRepository.save(
                Order
                        .builder()
                        .user(user)
                        .products(convertIdsIntoProducts(orderDto.getProductsIds()))
                        .isPaid(false)
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @Override
    public Order updateOrder(Long id, OrderDto orderDto) {
        if (id == null || orderDto == null) throw new NullPointerException("id or order is null");

        try {
            Order orderForUpdate = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("order not found by following id"));

            User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new NoSuchElementException("user not found"));

            orderForUpdate.setUser(user);
            orderForUpdate.setProducts(convertIdsIntoProducts(orderDto.getProductsIds()));
            orderForUpdate.setIsPaid(orderDto.getIsPaid());

            orderForUpdate.setUpdatedAt(Instant.now());

            return orderRepository.save(orderForUpdate);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Another user updated this record already", e);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        if (id == null) throw new NullPointerException("id is null");
        if (!orderRepository.existsById(id)) throw new NoSuchElementException("order not found by following id");

        orderRepository.deleteById(id);
    }

    @Override
    @Scheduled(fixedRate = 120000)
    public void deleteExpiredOrders() {
        List<Order> orders = orderRepository.findAllByIsPaidFalse();

        for (Order order : orders) {
            if (Duration.between(order.getCreatedAt(), Instant.now()).toMinutes() >= 10)
                orderRepository.delete(order);
        }
    }

    @Override
    public Order payForOrder(PayRequestDto payRequestDto) {
        if (payRequestDto == null) throw new NullPointerException("payRequest is null");

        User user = userRepository.findById(payRequestDto.getUserId()).orElseThrow(() -> new NoSuchElementException("user not found"));
        Order order = orderRepository.findByIdAndIsPaidFalse(payRequestDto.getOrderId()).orElseThrow(() -> new NoSuchElementException("order not found by following id or order already paid"));

        if (user.getWalletBalance() < getPriceOfAllProducts(order))
            throw new IllegalArgumentException("User haven't enough money for order");

        user.setWalletBalance(user.getWalletBalance() - getPriceOfAllProducts(order));

        order.setIsPaid(true);
        order.setUpdatedAt(Instant.now());

        userRepository.save(user);

        return orderRepository.save(order);
    }

    private Double getPriceOfAllProducts(Order order) {
        Double price = 0.0;
        for (Product product : order.getProducts()) {
            price += product.getPrice();
        }
        return price;
    }

    private List<Product> convertIdsIntoProducts(List<Long> ids) {
        return ids.stream().map(id -> productRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("product not found by following id"))
        ).collect(Collectors.toList());
    }


}
