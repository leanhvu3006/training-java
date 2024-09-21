package com.example.training.java.controllers;

import com.example.training.java.entitys.Customer;
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
    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    @Operation(summary = "Create customer", description = "Return new customer")
    @PostMapping("/customer/create")
    public Customer createCustomer(@RequestBody Customer customerRequest) {
        return customerRepository.save(customerRequest);
    }

    @Operation(summary = "Update customer by id", description = "Return customer after update")
    @PutMapping("/customer/update/{id}")
    public Customer createCustomer(@PathVariable(value = "id") Long id, @RequestBody Customer customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setUsername(customerRequest.getUsername());
        customer.setPassword(customerRequest.getPassword());
        return customerRepository.save(customer);
    }

    @Operation(summary = "Delete customer by id", description = "Return HTTPStatus 200")
    @DeleteMapping("/customer/delete/{id}")
    public void createCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }
}
