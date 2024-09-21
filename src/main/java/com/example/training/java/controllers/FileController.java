package com.example.training.java.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class FileController {

    @GetMapping("/file/read")
    public void readFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ResponseEntity.badRequest().body("File request empty");
        }
        // Lấy tên file gốc
        String fileName = file.getOriginalFilename();
        try {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get("./test");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file vào thư mục đã chỉ định
            Path filePath = uploadPath.resolve(fileName);

            // Sử dụng Files.copy để lưu tệp, thay thế file nếu đã tồn tại
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            ResponseEntity.ok().body("File success " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            ResponseEntity.ok().body("Fail read file " + fileName);
        }
    }
}
