package com.demo.jewel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgBBData {
    @JsonProperty("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
