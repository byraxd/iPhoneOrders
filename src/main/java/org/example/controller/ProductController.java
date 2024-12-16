package org.example.controller;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAllByIsAvailableTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProductById(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProductById(id);

        return ResponseEntity.ok("Operation was done successfully");
    }
}
