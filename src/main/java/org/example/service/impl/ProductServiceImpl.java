package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Product getProductById(Long id) {
        if (id == null) throw new NullPointerException("id is null");

        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found by following id"));
    }

    @Override
    public Product addProduct(Product product) {
        if (product == null) throw new NullPointerException("product is null");

        return productRepository.save(
                Product
                        .builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .isAvailable(product.getIsAvailable())
                        .quantity(product.getQuantity())
                        .build()
        );
    }

    @Override
    public Product updateProductById(Long id, Product product) {
        if (id == null || product == null) throw new NullPointerException("id or product is null");

        try {
            Product productForUpdate = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found by following id"));

            productForUpdate.setName(product.getName());
            productForUpdate.setPrice(product.getPrice());
            productForUpdate.setIsAvailable(product.getIsAvailable());
            productForUpdate.setQuantity(product.getQuantity());

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
