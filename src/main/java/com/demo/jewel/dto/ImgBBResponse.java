package com.demo.jewel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgBBResponse {
    @JsonProperty("data")
    private ImgBBData data;

    public ImgBBData getData() {
        return data;
    }

    public void setData(ImgBBData data) {
        this.data = data;
    }
}


