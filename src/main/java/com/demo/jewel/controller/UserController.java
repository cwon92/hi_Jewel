package com.demo.jewel.controller;

import com.demo.jewel.dto.CustomUser;
import com.demo.jewel.dto.Users;
import com.demo.jewel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //회원정보
    @Secured("USER")
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUser customUser){
        log.info("=======customUser========");
        log.info("customUser : " + customUser);

        Users user = customUser.getUser();
        log.info("user : " + user);

        //인증된 사용자 정보
        if( user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        //인증 되지 않음
        return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
    }

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody Users user) throws Exception{
        log.info("[POST] /users");
        int result = userService.insert(user);

        if (result > 0){
            log.info("회원가입 성공");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("회원가입 실패");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    //회원정보수정
    @Secured("USER")
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody Users user) throws Exception{
        log.info("[PUT] /users");
        int result = userService.update(user);

        if(result > 0){
            log.info("회원정보 수정 성공");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("회원정보 수정 실패");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    //회원 탈퇴
    @Secured("USER")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId) throws Exception{
        log.info("[DELETE] /users/{userId}");

        int result = userService.delete(userId);

        if(result > 0){
            log.info("회원탈퇴 성공");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("회원탈퇴 실패");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }


}
