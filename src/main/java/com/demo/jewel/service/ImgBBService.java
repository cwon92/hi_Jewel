package com.demo.jewel.service;

import com.demo.jewel.dto.ImgBBResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImgBBService {

    private final WebClient webClient;
    private final String apiKey;

    public ImgBBService(@Value("${imgbb.api-key}") String apiKey) {
        this.webClient = WebClient.create("https://api.imgbb.com/1/");
        this.apiKey = apiKey;
    }

    public Mono<String> uploadImage(Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> {
            Path tempFile;
            try {
                tempFile = Files.createTempFile(UUID.randomUUID().toString(), "");
                filePart.transferTo(tempFile.toFile()).block();
            } catch (Exception e) {
                return Mono.error(e);
            }

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("key", apiKey);
            parts.add("image", BodyInserters.fromResource(new FileSystemResource(tempFile.toFile())));

            System.out.println(parts);

            return webClient.post()
                    .uri("/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(parts))
                    .retrieve()
                    .bodyToMono(ImgBBResponse.class)
                    .map(response -> response.getData().getUrl());
        });
    }
}
