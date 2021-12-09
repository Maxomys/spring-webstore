package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.NewTransactionDto;
import com.github.maxomys.webstore.api.dtos.TransactionDto;
import com.github.maxomys.webstore.api.mappers.TransactionMapper;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.domain.Transaction;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.exceptions.NotInStockException;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.ProductRepository;
import com.github.maxomys.webstore.repositories.TransactionRepository;
import com.github.maxomys.webstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Page<TransactionDto> getTransactionsPage(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionPage.map(transactionMapper::transactionToTransactionDto);
    }

    @Override
    public List<TransactionDto> getTransactionsForUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found for id: " + userId));

        return Stream.concat(
                user.getBought()
                    .stream()
                    .map(transactionMapper::transactionToTransactionDto),
                user.getSold()
                    .stream()
                    .map(transactionMapper::transactionToTransactionDto)
        ).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getTransactionsForBuyer(Long userId) {
        return null;
    }

    @Override
    public List<TransactionDto> getTransactionsForSeller(Long userId) {
        return null;
    }

    @Override
    @Transactional
    public TransactionDto saveTransaction(NewTransactionDto dto) {
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(NotInStockException::new);
        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + dto.getBuyerId()));
        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + dto.getSellerId()));

        if (buyer.equals(seller)) {
            throw new RuntimeException("Buyer is the same as seller");
        }

        if (product.getAmountInStock() < 1) {
            throw new NotInStockException();
        }
        product.decreaseStockBy(1);

        Transaction transaction = Transaction.builder()
                .time(new Date())
                .product(product)
                .buyer(buyer)
                .seller(seller)
                .build();

        transactionRepository.save(transaction);

        return transactionMapper.transactionToTransactionDto(transaction);
    }

}
