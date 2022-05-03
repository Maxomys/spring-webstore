package com.github.maxomys.webstore.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.configuration.PasswordConfig;
import com.github.maxomys.webstore.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ProductRestController.class)
@Import(PasswordConfig.class)
class ProductRestControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = ProductDto.builder()
                .id(1L)
                .name("Product")
                .build();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(productService);
    }

    @Test
    void getAllProducts() throws Exception {
        when(productService.getProducts()).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/product/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void getAllProductsPaginatedEvaluateParameter() throws Exception {
        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);

        mockMvc.perform(get("/api/product/page")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name")
                .param("sortDir", "asc"))
                .andExpect(status().isOk());

        verify(productService).getProductDtoPage(pageableCaptor.capture());
        PageRequest pageRequest = pageableCaptor.getValue();

        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(Sort.Direction.ASC, pageRequest.getSort().getOrderFor("name").getDirection());
    }

    @Test
    void getAllProductsPaginatedEvaluateDefault() throws Exception {
        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);

        mockMvc.perform(get("/api/product/page"))
                .andExpect(status().isOk());

        verify(productService).getProductDtoPage(pageableCaptor.capture());
        PageRequest pageRequest = pageableCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(6, pageRequest.getPageSize());
        assertEquals(Sort.Direction.DESC, pageRequest.getSort().getOrderFor("createdAt").getDirection());
    }

    @Test
    void getProductsInCategoryPaginatedEvaluateParameter() throws Exception {
        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);

        mockMvc.perform(get("/api/product/category/1/page")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name")
                .param("sortDir", "asc"))
                .andExpect(status().isOk());

        verify(productService).getProductDtosByCategoryIdPaginated(anyLong(), pageableCaptor.capture());
        PageRequest pageRequest = pageableCaptor.getValue();

        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(Sort.Direction.ASC, pageRequest.getSort().getOrderFor("name").getDirection());
    }

    @Test
    void getProductsInCategoryPaginatedEvaluateDefault() throws Exception {
        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);

        mockMvc.perform(get("/api/product/category/1/page"))
                .andExpect(status().isOk());

        verify(productService).getProductDtosByCategoryIdPaginated(anyLong(), pageableCaptor.capture());
        PageRequest pageRequest = pageableCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(6, pageRequest.getPageSize());
        assertEquals(Sort.Direction.DESC, pageRequest.getSort().getOrderFor("createdAt").getDirection());
    }

    @Test
    void getAllProductsForCurrentUser() throws Exception {
        when(productService.getAllProductsForCurrentUser()).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/product/my"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void getProductById() throws Exception {
        when(productService.findById(anyLong())).thenReturn(productDto);

        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createProduct() throws Exception {
        ArgumentCaptor<ProductDto> productDtoCaptor = ArgumentCaptor.forClass(ProductDto.class);

        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Product\"}"))
                .andExpect(status().isCreated());

        verify(productService).saveProduct(productDtoCaptor.capture());
        ProductDto capturedProductDto = productDtoCaptor.getValue();

        assertEquals("Product", capturedProductDto.getName());
    }

}
