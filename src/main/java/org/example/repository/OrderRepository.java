package org.example.repository;

import org.example.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll();
    List<Order> findAllByIsPaidFalse();
    Optional<Order> findByIdAndIsPaidFalse(Long id);
}
