package com.demo.jewel.service;

import com.demo.jewel.dto.ImageResDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public ImageResDto uploadImg(MultipartFile file);
}
