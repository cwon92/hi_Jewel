package com.demo.jewel.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Users {
    private int no;
    private String userId;
    private String userPw;
    private String name;
    private String email;
    private Date regDate;
    private Date updDate;
    private int enabled;

    List<UserAuth> authList;

    public Users(){

    }

    public Users(Users user){
        this.no = user.getNo();
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.name = user.getName();
        this.email = user.getEmail();
        this.authList = user.getAuthList();
    }
}
