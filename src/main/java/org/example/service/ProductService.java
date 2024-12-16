package org.example.service;

import org.example.dto.ProductDto;
import org.example.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getAllByIsAvailableTrue();
    Product getProductById(Long id);

    Product addProduct(ProductDto productDto);

    Product updateProductById(Long id, ProductDto productDto);

    void deleteProductById(Long id);
}
