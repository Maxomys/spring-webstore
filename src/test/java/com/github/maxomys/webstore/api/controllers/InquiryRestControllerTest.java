package com.github.maxomys.webstore.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.configuration.PasswordConfig;
import com.github.maxomys.webstore.services.InquiryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryRestController.class)
@Import(PasswordConfig.class)
class InquiryRestControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    InquiryService inquiryService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    InquiryDto inquiryDto;

    @BeforeEach
    void setUp() {
        inquiryDto = InquiryDto.builder()
                .id(1L)
                .messageBody("test")
                .build();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(inquiryService);
    }

    @Test
    void getAllInquiries() throws Exception {
        when(inquiryService.getInquiries()).thenReturn(List.of(inquiryDto));

        mockMvc.perform(get("/api/inquiry/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].messageBody", is("test")));
    }

    @Test
    void getInquiryById() throws Exception {
        when(inquiryService.getInquiryById(anyLong())).thenReturn(inquiryDto);

        mockMvc.perform(get("/api/inquiry/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messageBody", is("test")));
    }

    @Test
    void getInquiryByIdEvaluateParam() throws Exception {
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        mockMvc.perform(get("/api/inquiry/1"))
                .andExpect(status().isOk());

        verify(inquiryService).getInquiryById(argumentCaptor.capture());

        assertEquals(1, argumentCaptor.getValue());
    }

    @Test
    void createInquiry() throws Exception {
        ArgumentCaptor<InquiryDto> inquiryDtoArgumentCaptor = ArgumentCaptor.forClass(InquiryDto.class);

        mockMvc.perform(post("/api/inquiry")
                .content("{\"email\": \"test@test.pl\", \"messageBody\": \"test\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(inquiryService).saveInquiry(inquiryDtoArgumentCaptor.capture());
        InquiryDto capturedDto = inquiryDtoArgumentCaptor.getValue();

        assertEquals("test@test.pl", capturedDto.getEmail());
        assertEquals("test", capturedDto.getMessageBody());
    }

}
