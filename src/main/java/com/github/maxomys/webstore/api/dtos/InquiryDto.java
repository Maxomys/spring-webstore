package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class InquiryDto {
    
    private Long id;
    private String email;
    private Date createdOn;
    private String messageBody;
    private Long productId;
    private String productName;

}
