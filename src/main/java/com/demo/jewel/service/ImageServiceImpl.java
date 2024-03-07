package com.demo.jewel.service;

import com.demo.jewel.dto.ImageResDto;
import com.demo.jewel.upload.NamedByteArrayResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    @Autowired
    private final WebClient webClient;
    private final String imgbbUrl = "https://api.imgbb.com/1/upload";
    private String secret = "98de50943f5634dd479c1cdf0604760d";

    @Override
    public Mono<ImageResDto> uploadImg(MultipartFile file) {

        ImageResDto imageResDto = new ImageResDto();

        return webClient.post()
                .uri(imgbbUrl)
                .body(Mono.just(imageResDto), ImageResDto.class)
                .retrieve()
                .bodyToMono(ImageResDto.class);





        /*
        // 요청 바디 설정

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", secret);
        try {
            body.add("image", new NamedByteArrayResource(file.getBytes(), file.getName()));
        } catch (java.io.IOException e) {
            log.info("FAIL");
        }

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl("http://localhost:8080")
                .build();

        String response = webClient.post()
                .uri( uriBuilder -> uriBuilder.queryParam("name", file.getOriginalFilename()).build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("============" + response + "============");

        return new ImageResDto();
        */
    }
}
