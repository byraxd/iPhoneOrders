package org.example.service.impl;

import org.example.dto.OrderDto;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    void test_createOrder_shouldReturnCreatedOrder_WhenOrderDtoIsNotNull() {

        Long userId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        OrderDto orderDto = OrderDto
                .builder()
                .userId(userId)
                .productsIds(productIds)
                .build();

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);

        Mockito.when(userRepository.findById(orderDto.getUserId())).thenReturn(Optional.of(user));

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(product2.getId())).thenReturn(Optional.of(product2));


        Order order = Order
                .builder()
                .user(user)
                .products(List.of(product, product2))
                .build();

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Order result = service.createOrder(orderDto);

        Mockito.verify(orderRepository).save(Mockito.any(Order.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getUser().getId());
        Assertions.assertEquals(product.getId(), result.getProducts().get(0).getId());
        Assertions.assertEquals(product2.getId(), result.getProducts().get(1).getId());
    }

    @Test
    void test_updateOrder_shouldReturnUpdatedOrder_WhenOrderDtoAndIdIsNotNull() {
        Long userId = 1L;
        List<Long> productIds = List.of(1L, 2L);

        OrderDto orderDto = OrderDto
                .builder()
                .userId(userId)
                .productsIds(productIds)
                .build();

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);

        Mockito.when(userRepository.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(product2.getId())).thenReturn(Optional.of(product2));

        Order order = Order
                .builder()
                .user(user)
                .products(List.of(product, product2))
                .build();

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Assertions.assertNotNull(service.createOrder(orderDto));

        Mockito.verify(orderRepository).save(Mockito.any(Order.class));

        Assertions.assertNotNull(order);
        Assertions.assertEquals(userId, order.getUser().getId());
        Assertions.assertEquals(product.getId(), order.getProducts().get(0).getId());
        Assertions.assertEquals(product2.getId(), order.getProducts().get(1).getId());

    }

    @Test
    void test_deleteOrder_shouldDeleteOrder_WhenOrderIsExist() {
        Long id = 1L;

        Mockito.when(orderRepository.existsById(id)).thenReturn(true);

        service.deleteOrder(id);

        Mockito.verify(orderRepository).existsById(id);
        Mockito.verify(orderRepository).deleteById(id);
    }

    @Test
    void test_payForOrder_ShouldReturnPayedOrder_WhenOrderIsExistAndUserHaveEnoughMoney() {
        Long id = 1L;

        User user = new User();
        user.setId(1L);
        user.setWalletBalance(160.00);

        // Creating products with prices
        List<Product> products = List.of(
                Product.builder().id(1L).price(120.00).build(),
                Product.builder().id(2L).price(30.00).build()
        );

        Order order = Order
                .builder()
                .id(id)
                .user(user)
                .products(products)
                .isPaid(false)
                .build();

        Mockito.when(orderRepository.findByIdAndIsPaidFalse(id)).thenReturn(Optional.of(order));  // Mock the order fetch
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));  // Mock the user fetch

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Order result = service.payForOrder(id);

        // Verifying the repository calls
        Mockito.verify(orderRepository).findByIdAndIsPaidFalse(id);
        Mockito.verify(userRepository).findById(user.getId());

        // Asserting that the result is not null
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getIsPaid());  // Ensure the order is marked as paid
        Assertions.assertEquals(user.getId(), result.getUser().getId());
        Assertions.assertEquals(products.get(0).getId(), result.getProducts().get(0).getId());
        Assertions.assertEquals(products.get(1).getId(), result.getProducts().get(1).getId());
    }
}
