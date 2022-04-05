package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.domain.Inquiry;

import java.util.List;

public interface InquiryService {

    void saveInquiry(InquiryDto inquiry);

    InquiryDto getInquiryById(Long inquiryId);

    List<InquiryDto> getInquiries();

    void deleteInquiryById(Long inquiryId);

}
