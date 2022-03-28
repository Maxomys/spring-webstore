package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.CategoryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

//    @Mock
//    ImageService imageService;
//
//    @Mock
//    PermissionService permissionService;

    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, categoryRepository, null, null);
    }

    @Test
    void getLatestProducts() {
        Product product = Product.builder().build();
        when(productRepository.findAll()).thenReturn(List.of(product, product, product));

        List<Product> products = productService.getLatestProducts(2);

        assertEquals(2, products.size());
    }

    @Test
    void findById() {
        Product product = Product.builder().id(1L).build();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(1L);

        assertEquals(1, foundProduct.getId());
    }

    @Test
    void findByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void updateProduct() {
        Product foundProduct = Product.builder().id(2L).build();
        Product newProduct = Product.builder()
                .id(2L)
                .name("new")
                .price(33)
                .amountInStock(13)
                .build();
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(foundProduct));

        productService.updateProduct(newProduct);

        verify(productRepository).save(productCaptor.capture());
        assertEquals("new", productCaptor.getValue().getName());
    }

    @Test
    void addRequestToProduct() {
        Product product = Product.builder().id(1L).uniqueAddresses(new HashSet<>()).build();
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productService.addRequestToProduct(product, "test");

        verify(productRepository).save(productCaptor.capture());

        assertEquals("test", productCaptor.getValue().getUniqueAddresses().stream().findFirst().get());
    }

}
