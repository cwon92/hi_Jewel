package com.demo.jewel.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserAuth {
    private int authNo;
    private String userId;
    private String auth;

    public UserAuth() {
    }

    public UserAuth(String userId, String auth){
        this.userId = userId;
        this.auth = auth;
    }
}
