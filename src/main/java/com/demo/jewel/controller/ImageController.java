package com.demo.jewel.controller;

import com.demo.jewel.dto.ImageResDto;
import com.demo.jewel.upload.ImageUploadAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageUploadAdapter imageUploadAdapter;

    @PostMapping("/images")
    public ResponseEntity postImage(@RequestParam MultipartFile file){
        ImageResDto imageResDto = imageUploadAdapter.uploadImage(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageResDto);
    }
}
