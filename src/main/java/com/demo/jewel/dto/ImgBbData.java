package com.demo.jewel.dto;

import lombok.Data;

@Data
public class ImgBbData {
    private String id;
    private String title;
    private String urlViewer;
    private String url;
    private String displayUrl;
    private int width;
    private int height;
    private int size;
    private long time;
    private int expiration;
    private ImgBbImage image;
    private ImgBbImage thumb;
    private ImgBbImage medium;
    private String deleteUrl;
}
