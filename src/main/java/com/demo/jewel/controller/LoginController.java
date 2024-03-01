package com.demo.jewel.controller;

import com.demo.jewel.constants.SecurityConstants;
import com.demo.jewel.domain.AuthenticationRequest;
import com.demo.jewel.prop.JwtProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class LoginController{
    @Autowired
    private JwtProp jwtProp;

    //login -username - password
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){

        String username = request.getUsername();
        String password = request.getPassword();

        log.info("username: " + username);
        log.info("password: " + password);

        //권한
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        //시크릿키 바이트 변환
        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        //토근생성
        String jwt = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey),Jwts.SIG.HS512)//시그니처 비밀키, 알고리즘 설정
                .header()                                   //헤더
                .add("typ", SecurityConstants.TOKEN_TYPE) //typ: jwt
                .and()
                .expiration(new Date( System.currentTimeMillis() + 1000*60*60*24 ))//토큰만료시간(1일)
                .claim("uid", username)//payload username
                .claim("rol", roles)//payload 권한
                .compact();//토큰생성

        log.info("jwt: " + jwt);

        return new ResponseEntity<String>(jwt, HttpStatus.OK);
    }

    //토큰해석
    @GetMapping("/user/info")
    public ResponseEntity<?> userInfo(@RequestHeader(name = "Authorization") String header) {

        log.info("============ header ===========");
        log.info("Authorization : " + header);

        // Authorization : Bearer ${jwt}
        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");
        byte[] signingKey = jwtProp.getSecretKey().getBytes();
        //토큰 해석
        Jws<Claims> parsedToken = Jwts.parser()
                .verifyWith( Keys.hmacShaKeyFor(signingKey) )
                .build()
                .parseSignedClaims(jwt); //decoding

        log.info("parsedToken : " + parsedToken);
        // uid: user
        String username = parsedToken.getPayload().get("uid").toString();
        log.info("username : " + username);

        // rol:
        Claims claims = parsedToken.getPayload();
        Object roles = claims.get("rol");
        log.info("roles : " + roles);

        return new ResponseEntity<String>(parsedToken.toString(), HttpStatus.OK);
    }

}
