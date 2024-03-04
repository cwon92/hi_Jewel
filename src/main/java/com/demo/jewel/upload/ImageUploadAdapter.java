package com.demo.jewel.upload;

import com.demo.jewel.dto.ImageResDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadAdapter {
    ImageResDto uploadImage (MultipartFile file);
}
