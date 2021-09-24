package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Inquiry;

import java.util.List;

public interface InquiryService {

    void saveInquiry(Inquiry inquiry);

    Inquiry getInquiryById(Long inquiryId);

    List<Inquiry> getInquiries();

    void deleteInquiryById(Long inquiryId);

}
