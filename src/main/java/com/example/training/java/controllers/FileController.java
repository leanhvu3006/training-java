package com.example.training.java.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @GetMapping("/file/read")
    public void readFile(@RequestParam("file") MultipartFile file) {

    }
}
