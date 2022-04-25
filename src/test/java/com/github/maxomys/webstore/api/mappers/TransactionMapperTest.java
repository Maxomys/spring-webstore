package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.TransactionDto;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.Transaction;
import com.github.maxomys.webstore.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TransactionMapperImpl.class})
class TransactionMapperTest {

    @Autowired
    TransactionMapper transactionMapper;

    @Test
    void transactionToTransactionDto() {
        User user = User.builder()
                .id(1L)
                .username("Joe")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("Ed")
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Product")
                .user(user)
                .build();
        Transaction transaction = Transaction.builder()
                .id(1L)
                .seller(user)
                .buyer(user2)
                .product(product)
                .build();

        TransactionDto transactionDto = transactionMapper.transactionToTransactionDto(transaction);

        assertEquals(1L, transactionDto.getSellerId());
        assertNull(transactionDto.getTransactionAmount());

    }

}
