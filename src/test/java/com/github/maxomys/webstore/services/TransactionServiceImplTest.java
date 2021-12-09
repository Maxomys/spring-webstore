package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.NewTransactionDto;
import com.github.maxomys.webstore.api.dtos.TransactionDto;
import com.github.maxomys.webstore.api.mappers.TransactionMapper;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.Transaction;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.exceptions.NotInStockException;
import com.github.maxomys.webstore.repositories.ProductRepository;
import com.github.maxomys.webstore.repositories.TransactionRepository;
import com.github.maxomys.webstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    TransactionMapper transactionMapper;

    TransactionServiceImpl transactionService;

    @BeforeEach
    void setup() {
        transactionMapper = Mappers.getMapper(TransactionMapper.class);
        transactionService = new TransactionServiceImpl(transactionRepository, productRepository, userRepository, transactionMapper);
    }

    @Test
    void getTransactionsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        when(transactionRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new Transaction(), new Transaction())));

        Page<TransactionDto> transactionPage = transactionService.getTransactionsPage(pageable);

        verify(transactionRepository).findAll(any(Pageable.class));

        assertEquals(TransactionDto.class, transactionPage.getContent().get(0).getClass());
    }

    @Test
    void getTransactionsForUserId() {
        Transaction transactionB = Transaction.builder()
                .id(1L)
                .build();
        Transaction transactionS = Transaction.builder()
                .id(2L)
                .build();

        User user = User.builder()
                .id(1L)
                .username("user")
                .bought(new HashSet<Transaction>(Collections.singleton(transactionB)))
                .sold(new HashSet<Transaction>(Collections.singleton(transactionS)))
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<TransactionDto> transactionDtos = transactionService.getTransactionsForUserId(1L);

        assertEquals(2, transactionDtos.size());
    }

    @Test
    void getTransactionsForBuyer() {
    }

    @Test
    void getTransactionsForSeller() {
    }

    @Test
    void saveTransaction() {
        NewTransactionDto newDto = NewTransactionDto.builder()
                .productId(1L)
                .buyerId(1L)
                .sellerId(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .amountInStock(1)
                .build();

        User user = User.builder()
                .id(1L)
                .username("user")
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        TransactionDto savedTransaction = transactionService.saveTransaction(newDto);
        assertEquals(1L, savedTransaction.getProductId());
    }

    @Test
    void saveTransactionNotInStock() {
        NewTransactionDto newDto = NewTransactionDto.builder()
                .productId(1L)
                .buyerId(1L)
                .sellerId(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .amountInStock(0)
                .build();

        User user = User.builder()
                .id(1L)
                .username("user")
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(NotInStockException.class, () -> transactionService.saveTransaction(newDto));
    }

}
