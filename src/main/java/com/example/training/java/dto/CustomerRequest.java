package com.example.training.java.dto;

import lombok.Data;

@Data
public class CustomerRequest {

    private String username;
    private String password;
    private String role;
}
