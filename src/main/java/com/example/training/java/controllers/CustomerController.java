package com.example.training.java.controllers;

import com.example.training.java.dto.CustomerRequest;
import com.example.training.java.entitys.CustomerEntity;
import com.example.training.java.repositorys.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Operation(summary = "Get all customer", description = "Get all customer")
    @GetMapping("/customer/select")
    public List<CustomerEntity> getCustomer() {
        return customerRepository.findAll();
    }

    @Operation(summary = "Create customer", description = "Return new customer")
    @PostMapping("/customer/create")
    public CustomerEntity createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .username(customerRequest.getUsername())
                .password("{noop}" + customerRequest.getPassword())
                .role(customerRequest.getRole())
                .build();
        return customerRepository.save(customerEntity);
    }

    @Operation(summary = "Update customer by id", description = "Return customer after update")
    @PutMapping("/customer/update/{id}")
    public CustomerEntity createCustomer(@PathVariable(value = "id") Long id, @RequestBody CustomerRequest customerEntityRequest) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customerEntity.setUsername(customerEntityRequest.getUsername());
        customerEntity.setPassword(customerEntityRequest.getPassword());
        return customerRepository.save(customerEntity);
    }

    @Operation(summary = "Delete customer by id", description = "Return HTTPStatus 200")
    @DeleteMapping("/customer/delete/{id}")
    public void createCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }
}
