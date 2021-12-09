package com.github.maxomys.webstore.controllers;

import lombok.extern.slf4j.Slf4j;
import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.services.InquiryService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@Profile("mvc")
public class InquiryController {

    private InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/inquiries")
    public String saveInquiry(@ModelAttribute Inquiry inquiry, BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
        }

        inquiryService.saveInquiry(inquiry);

        return "redirect:/index";
    }

    @GetMapping("/admin/inquiries/{inquiryId}")
    public String getInquiryById(@PathVariable String inquiryId , Model model) {
        model.addAttribute("inquiry", inquiryService.getInquiryById(Long.valueOf(inquiryId)));

        return "/admin/inquiry";
    }

    @PostMapping("/admin/inquiries/{inquiryId}")
    public String deleteInquiryById(@PathVariable String inquiryId) {
        inquiryService.deleteInquiryById(Long.valueOf(inquiryId));

        return "redirect:/admin/index";
    }

}
