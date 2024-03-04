package com.demo.jewel.upload;

import com.demo.jewel.dto.ImageResDto;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ImgBbUploadAPI implements ImageUploadAdapter{

    private final String apiUrl = "https://api.imgbb.com/1/upload";
    private final WebClient webClient;
    @Value("${imgbb.secret}")
    private String secret;

    @Override
    public ImageResDto uploadImage(MultipartFile file) {
        // 요청 바디 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", secret);
        try {
            body.add("image", new NamedByteArrayResource(file.getBytes(), file.getName()));
        } catch (IOException e) {
            throw new BusinessLogicException(CommonExceptionCode.BAD_REQUEST);
        }

        // POST 요청 보내서 ImgBBData 객체에 받아오기
        ImgBBData imgBBData = webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ImgBBInfo.class)
                .blockOptional()
                .map(ImgBBInfo::getData)
                .orElseThrow(() -> new BusinessLogicException(CommonExceptionCode.SERVICE_UNAVAILABLE));

        // 필요한 정보만 파싱하여 ImageResponseDto 에 담아 반환
        return ImageResponseDto
                .builder()
                .image(imgBBData.getImage().getUrl())
                .thumb(imgBBData.getThumb().getUrl())
                .build();
    }

    private static class NamedByteArrayResource extends ByteArrayResource {
        private final String filename;

        public NamedByteArrayResource(byte[] byteArray, String filename) {
            super(byteArray);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }
}
