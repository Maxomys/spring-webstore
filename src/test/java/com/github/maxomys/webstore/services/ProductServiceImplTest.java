package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.api.mappers.ProductMapperImpl;
import com.github.maxomys.webstore.domain.Category;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.CategoryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import com.github.maxomys.webstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ProductMapperImpl.class})
class ProductServiceImplTest {

    @Autowired
    ProductMapperImpl productMapper;

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PermissionService permissionService;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

//    @Mock
//    ImageService imageService;

    ProductServiceImpl productService;

    ProductDto validProductDto;
    Product validProduct;
    User validUser;
    Category validCategory;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, categoryRepository, userRepository, null, permissionService, productMapper);
        SecurityContextHolder.setContext(securityContext);

        validCategory = Category.builder()
                .description("category")
                .id(1L)
                .build();
        validProductDto = ProductDto.builder()
                .id(1L)
                .name("productDto")
                .price(1)
                .amountInStock(1)
                .categoryId(1L)
                .build();
        validProduct = Product.builder()
                .id(1L)
                .name("product")
                .price(1)
                .category(validCategory)
                .amountInStock(1)
                .build();
        validUser = User.builder()
                .username("username")
                .build();
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
        Product product = Product.builder()
                .id(1L)
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDto foundProduct = productService.findById(1L);

        assertEquals(1, foundProduct.getId());
    }

    @Test
    void saveProductDto() {
        when(userRepository.findByUsername(anyString())).thenReturn(validUser);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(validCategory));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("username");
        when(productRepository.save(any())).thenReturn(validProduct);

        ProductDto savedProductDto = productService.saveProduct(validProductDto);
        verify(permissionService, times(2)).addPermissionForCurrentUser(any(), any(), any());

        assertEquals(1L, savedProductDto.getCategoryId());
    }

    @Test
    void saveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);

        Product savedProduct = productService.saveProduct(validProduct);

        verify(permissionService, times(2)).addPermissionForCurrentUser(any(), any(), any());
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getCreatedAt());
    }

    @Test
    void findByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void updateProduct() {
        Product foundProduct = Product.builder().id(2L).build();
        ProductDto newProduct = ProductDto.builder()
                .id(2L)
                .name("new")
                .price(33)
                .amountInStock(13)
                .categoryId(1L)
                .build();
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(foundProduct));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any(Product.class))).thenReturn(foundProduct);

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

    @Test
    void searchProductsByName() {
        productService.searchProductsByName("product");

        verify(productRepository).findAllByNameContainingIgnoreCase(anyString());
    }

}
