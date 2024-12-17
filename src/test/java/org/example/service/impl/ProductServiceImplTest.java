package org.example.service.impl;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.repository.ProductRepository;
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
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void test_getAllProducts_shouldReturnListOfAllProducts() {
        Product product = new Product();
        product.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);


        Mockito.when(productRepository.findAll()).thenReturn(List.of(product, product2));

        List<Product> products = productService.getAllProducts();

        Assertions.assertEquals(List.of(product, product2), products);

        Mockito.verify(productRepository).findAll();
        productService.getAllByIsAvailableTrue();
    }

    @Test
    void test_getAllByIsAvailableTrue_shouldReturnListOfAllProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setIsAvailable(true);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setIsAvailable(true);

        Mockito.when(productRepository.findAllByIsAvailableTrue()).thenReturn(List.of(product, product2));

        List<Product> products = productService.getAllByIsAvailableTrue();

        Assertions.assertEquals(List.of(product, product2), products);
        Assertions.assertEquals(product.getIsAvailable(), products.get(0).getIsAvailable());

        Mockito.verify(productRepository).findAllByIsAvailableTrue();

    }

    @Test
    void test_getProductById_shouldReturnProductById_WhenProductExists() {
        Long id = 1L;

        Product product = new Product();
        product.setId(id);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Assertions.assertEquals(product, productService.getProductById(id));

        Mockito.verify(productRepository).findById(product.getId());
    }

    @Test
    void test_addProduct_shouldAddProduct_WhenProductDtoNotNull() {
        ProductDto productDto = ProductDto
                .builder()
                .name("productDto name")
                .price(15.00)
                .isAvailable(true)
                .quantity(15L)
                .build();

        Product expectedProduct = Product
                .builder()
                .name("productDto name")
                .price(15.00)
                .isAvailable(true)
                .quantity(15L)
                .build();


        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(expectedProduct);

        Product result = productService.addProduct(productDto);

        Mockito.verify(productRepository).save(Mockito.any(Product.class));

        Assertions.assertEquals(expectedProduct.getName(), result.getName());
        Assertions.assertEquals(expectedProduct.getIsAvailable(), result.getIsAvailable());
        Assertions.assertEquals(expectedProduct.getPrice(), result.getPrice());
        Assertions.assertEquals(expectedProduct.getQuantity(), result.getQuantity());
    }

    @Test
    void test_updateProductById_ShouldUpdateProduct_WhenProductExistsById() {
        Long id = 1L;

        ProductDto productDto = ProductDto
                .builder()
                .name("productDto name")
                .price(15.00)
                .isAvailable(true)
                .quantity(15L)
                .build();

        Product expectedProduct = Product
                .builder()
                .id(id)
                .name("productDto name")
                .price(15.00)
                .isAvailable(true)
                .quantity(15L)
                .build();

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(expectedProduct);

        Product result = productService.updateProductById(id, productDto);

        Mockito.verify(productRepository).save(Mockito.any(Product.class));

        Assertions.assertEquals(expectedProduct.getName(), result.getName());
        Assertions.assertEquals(expectedProduct.getIsAvailable(), result.getIsAvailable());
        Assertions.assertEquals(expectedProduct.getPrice(), result.getPrice());
        Assertions.assertEquals(expectedProduct.getQuantity(), result.getQuantity());
    }

    @Test
    void test_deleteUserById_shouldDeleteUserById_WhenUserExists() {
        Long id = 1L;

        Mockito.when(productRepository.existsById(id)).thenReturn(true);

        productService.deleteProductById(id);

        Mockito.verify(productRepository).deleteById(id);
    }
}
