package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TransactionDto {

    private Long id;
    private Date time;
    private Long productId;
    private String productName;
    private Long buyerId;
    private Long sellerId;
    private Integer transactionAmount;

}
