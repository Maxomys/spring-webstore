package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.NewTransactionDto;
import com.github.maxomys.webstore.api.dtos.TransactionDto;
import com.github.maxomys.webstore.domain.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {

    @Mapping(target = "buyerId", source = "buyer.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "transactionAmount", source = "product.price")
    TransactionDto transactionToTransactionDto(Transaction transaction);

}
