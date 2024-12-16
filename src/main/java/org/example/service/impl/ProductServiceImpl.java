package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllByIsAvailableTrue() {
        return productRepository.findAllByIsAvailableTrue();
    }

    @Override
    public Product getProductById(Long id) {
        if (id == null) throw new NullPointerException("id is null");

        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found by following id"));
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        if (productDto == null) throw new NullPointerException("product is null");

        return productRepository.save(
                Product
                        .builder()
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .isAvailable(productDto.getIsAvailable())
                        .quantity(productDto.getQuantity())
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @Override
    public Product updateProductById(Long id, ProductDto productDto) {
        if (id == null || productDto == null) throw new NullPointerException("id or product is null");

        try {
            Product productForUpdate = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found by following id"));

            productForUpdate.setName(productDto.getName());
            productForUpdate.setPrice(productDto.getPrice());
            productForUpdate.setIsAvailable(productDto.getIsAvailable());
            productForUpdate.setQuantity(productDto.getQuantity());

            productForUpdate.setUpdatedAt(Instant.now());

            return productRepository.save(productForUpdate);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Another user updated this record already", e);
        }
    }

    @Override
    public void deleteProductById(Long id) {
        if (id == null) throw new NullPointerException("id is null");
        if (!productRepository.existsById(id)) throw new NoSuchElementException("Product not found by following id");

        productRepository.deleteById(id);
    }
}
