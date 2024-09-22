package com.example.training.java.security;

import com.example.training.java.entitys.CustomerEntity;
import com.example.training.java.repositorys.CustomerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedData {

    private final CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        insertData();
    }

    private void insertData() {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findByUsername("admin");
        if(customerEntityOptional.isEmpty()) {
            log.info("Initialize data for testing");
            customerRepository.save(CustomerEntity.builder()
                            .username("admin")
                            .password("{noop}admin")
                            .role("ADMIN")
                    .build());
        }
    }
}
