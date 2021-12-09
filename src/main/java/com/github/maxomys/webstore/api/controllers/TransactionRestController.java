package com.github.maxomys.webstore.api.controllers;

import com.github.maxomys.webstore.api.dtos.NewTransactionDto;
import com.github.maxomys.webstore.api.dtos.TransactionDto;
import com.github.maxomys.webstore.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public Page<TransactionDto> getTransactionsPage(@SortDefault(sort = "time") Pageable pageable) {
        return transactionService.getTransactionsPage(pageable);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionDto> getTransactionForUser(@PathVariable Long userId) {
        return transactionService.getTransactionsForUserId(userId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveNewTransaction(@RequestBody NewTransactionDto transactionDto) {
        transactionService.saveTransaction(transactionDto);
    }

}
