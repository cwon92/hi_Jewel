package com.demo.jewel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

    @Value("${imgbb.api-key}")
    private String apiKey;

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";
    @Autowired
    private final RestTemplate restTemplate;

    public ImageUploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadImage(MultipartFile image) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());

        body.add("key", apiKey);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String imageUrl = restTemplate.exchange(IMGBB_API_URL, HttpMethod.POST, requestEntity, String.class).getBody();
        System.out.println("imageUrl : " + imageUrl);

        return imageUrl;
    }
}
