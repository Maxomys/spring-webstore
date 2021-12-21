package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.NewTransactionDto;
import com.github.maxomys.webstore.api.dtos.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Page<TransactionDto> getTransactionsPage(Pageable pageable);

    List<TransactionDto> getTransactionsForUserId(Long userId);

    List<TransactionDto> getTransactionsForBuyer(Long userId);

    List<TransactionDto> getTransactionsForSeller(Long userId);

    TransactionDto saveTransaction(NewTransactionDto dto);

}
