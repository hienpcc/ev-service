package com.customer.controller;

import com.customer.dto.CustomerProfileDto;
import com.customer.service.CustomerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    public CustomerProfileController(CustomerProfileService customerProfileService) {
        this.customerProfileService = customerProfileService;
    }

    // Đây là API tổng hợp hồ sơ
    @GetMapping("/{id}/profile")
    public ResponseEntity<CustomerProfileDto> getCustomerProfile(@PathVariable Long id) {
        return ResponseEntity.ok(customerProfileService.getCustomerProfile(id));
    }
}
