package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.InquiryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final ProductRepository productRepository;
    private final InquiryRepository inquiryRepository;
    private final EmailService emailService;

    public InquiryServiceImpl(ProductRepository productRepository, InquiryRepository inquiryRepository, EmailService emailService) {
        this.productRepository = productRepository;
        this.inquiryRepository = inquiryRepository;
        this.emailService = emailService;
    }

//    @Override
//    public void saveInquiry(Inquiry inquiry) {
//        Calendar calendar = Calendar.getInstance();
//        inquiry.setCreatedOn(calendar.getTime());
//
//        Product product = inquiry.getProduct();
//
//        product.getInquiries().add(inquiry);
//
//        productRepository.save(product);
//
//        emailService.sendEmail(inquiry);
//    }

    @Override
    @Transactional
    public void saveInquiry(Inquiry inquiry) {
        inquiry.setCreatedOn(new Date());
        Long productId = inquiry.getProduct().getId();

        Product product = productRepository.findById(productId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Product not found for id: " + productId);
        });

        product.getInquiries().add(inquiry);

        emailService.sendEmail(inquiry);
    }

    @Override
    public Inquiry getInquiryById(Long inquiryId) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(inquiryId);

        if (!inquiryOptional.isPresent()) {
            throw new RuntimeException("Inquiry not Found!");
        }

        return inquiryOptional.get();
    }

    @Override
    public List<Inquiry> getInquiries() {
        return inquiryRepository.findAll();
    }

    @Override
    public void deleteInquiryById(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

}
