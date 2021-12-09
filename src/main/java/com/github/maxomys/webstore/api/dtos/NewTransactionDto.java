package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewTransactionDto {

    private Long productId;
    private Long buyerId;
    private Long sellerId;

}
