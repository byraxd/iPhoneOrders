package org.example.service;

import org.example.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product addProduct(Product product);

    Product updateProductById(Long id, Product product);

    void deleteProductById(Long id);
}
