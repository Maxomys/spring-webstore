package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.domain.Inquiry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InquiryMapper {
    
    public InquiryDto inquiryToInquiryDto(Inquiry inquiry) {
        return InquiryDto.builder()
            .id(inquiry.getId())
            .email(inquiry.getEmail())
            .createdOn(inquiry.getCreatedOn())
            .messageBody(inquiry.getMessageBody())
            .productId(inquiry.getProduct().getId())
            .productName(inquiry.getProduct().getName())
            .build();
    }

    public Inquiry inquiryDtoToInquiry(InquiryDto dto) {
        return Inquiry.builder()
            .email(dto.getEmail())
            .messageBody(dto.getMessageBody())
            .build();
    }

}
