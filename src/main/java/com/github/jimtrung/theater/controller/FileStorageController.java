package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.UploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileStorageController {
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody UploadRequest request) {
        return ResponseEntity.status(200).body("Image uploaded successfully");
    }
}
