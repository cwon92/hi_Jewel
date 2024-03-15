package com.demo.jewel.controller;

import com.demo.jewel.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile image) {
        String imageUrl = imageUploadService.uploadImage(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageUrl);
    }
}
