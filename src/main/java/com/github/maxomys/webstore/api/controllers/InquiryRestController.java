package com.github.maxomys.webstore.api.controllers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.InquiryDto;
import com.github.maxomys.webstore.api.mappers.InquiryMapper;
import com.github.maxomys.webstore.services.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryRestController {

    private final InquiryService inquiryService;

    private final InquiryMapper inquiryMapper;

    @GetMapping("/all")
    public List<InquiryDto> getAllInquiries() {
        return inquiryService.getInquiries();
    }

    @GetMapping("/{inquiryId}")
    public InquiryDto getInquiryById(@PathVariable Long inquiryId) {
        return inquiryService.getInquiryById(inquiryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInquiry(@RequestBody InquiryDto inquiryDto) {
        inquiryService.saveInquiry(inquiryDto);
    }

    @DeleteMapping("/{inquiryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@PathVariable Long inquiryId) {
        inquiryService.deleteInquiryById(inquiryId);
    }

}
