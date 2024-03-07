package com.demo.jewel.controller;

import com.demo.jewel.dto.ImageResDto;
import com.demo.jewel.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class ImageController {



    private ImageService imageService;

    @PostMapping("/images")
    public ResponseEntity<?> postImage(@RequestParam MultipartFile file){



        ImageResDto imageResDto = imageService.uploadImg(file);
        return new ResponseEntity<>("ok", HttpStatus.OK);


    }
}
