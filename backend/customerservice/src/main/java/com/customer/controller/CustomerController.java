package com.customer.controller;

import com.customer.dto.CustomerDto;
import com.customer.service.CustomerManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerManagementService customerService;

    public CustomerController(CustomerManagementService customerService) {
        this.customerService = customerService;
    }

    // Lấy thông tin cơ bản của khách hàng
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerDetails(id));
    }

    // (Giả sử việc tạo Customer được xử lý bởi AccountService,
    // ở đây chúng ta chỉ cập nhật thông tin)
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomerDetails(id, customerDto));
    }
}
