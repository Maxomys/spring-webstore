package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.api.mappers.InquiryMapper;
import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.domain.Product;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.repositories.InquiryRepository;
import com.github.maxomys.webstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final ProductRepository productRepository;
    private final InquiryRepository inquiryRepository;
    private final EmailService emailService;
    private final InquiryMapper inquiryMapper;


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
    public void saveInquiry(InquiryDto dto) {
        Inquiry inquiry = inquiryMapper.inquiryDtoToInquiry(dto);

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id: " + dto.getProductId()));

        inquiry.setProduct(product);
        inquiry.setCreatedOn(new Date());

        product.getInquiries().add(inquiry);
    }

    @Override
    public InquiryDto getInquiryById(Long inquiryId) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(inquiryId);

        if (!inquiryOptional.isPresent()) {
            throw new RuntimeException("Inquiry not Found!");
        }

        return inquiryMapper.inquiryToInquiryDto(inquiryOptional.get());
    }

    @Override
    public List<InquiryDto> getInquiries() {
        return inquiryRepository.findAll()
                .stream()
                .map(inquiryMapper::inquiryToInquiryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInquiryById(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

}
