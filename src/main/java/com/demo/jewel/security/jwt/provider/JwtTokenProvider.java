package com.demo.jewel.security.jwt.provider;

import com.demo.jewel.dto.CustomUser;
import com.demo.jewel.dto.UserAuth;
import com.demo.jewel.dto.Users;
import com.demo.jewel.mapper.UserMapper;
import com.demo.jewel.prop.JwtProp;
import com.demo.jewel.security.jwt.constants.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProp jwtProp;

    @Autowired
    private UserMapper userMapper;

    public String createToken(int userNo, String userId, List<String> roles) {
        //byte[] signingKey = getSigningKey(); 아래에 메서드로 만들어놔서 필요없음

        //토큰 생성
        String jwt = Jwts.builder()
                .signWith( getShaKey(), Jwts.SIG.HS512) //Keys.hmac~ 아래에 메서드로 만들어놓음 (getShaKey())
                .header()
                .add("typ", JwtConstants.TOKEN_TYPE)
                .and()
                .expiration( new Date( System.currentTimeMillis() + 864000000 ) )
                .claim("uno", "" + userNo)
                .claim("uid", userId)
                .claim("rol", roles)
                .compact();

        log.info("jwt : " + jwt);

        return jwt;
    }

    //토큰 해석
    // Authorization : Bearer + {jwt} (authHeader) -> jwt 추출 -> UsernamePasswordAuthenticationToken
    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader){
        if(authHeader == null || authHeader.length() == 0){
            return null;
        }
        try {
            //jwt 추출
            String jwt = authHeader.replace(JwtConstants.TOKEN_PREFIX, ""); //"Bearer "

            //parsing
            Jws<Claims> parsedToken = Jwts.parser()
                                            .verifyWith(getShaKey())
                                            .build()
                                            .parseSignedClaims(jwt);

            log.info("parsedToken : " + parsedToken);

            //인증된 사용자 번호
            String userNo = parsedToken.getPayload().get("uno").toString();
            int no = ( userNo == null ? 0 : Integer.parseInt(userNo));
            log.info("userNo : " + userNo);

            //인증된 사용자 아이디
            String userId = parsedToken.getPayload().get("uid").toString();
            log.info("userId : " + userId);

            //인증된 사용자 권한
            Claims claims = parsedToken.getPayload();
            Object roles = claims.get("rol");
            log.info("roles : " + roles);

            //토큰에 userId 있는지 확인
            if( userId == null || userId.length() == 0){
                return null;
            }

            //유저 정보 세팅
            Users user = new Users();
            user.setNo(no);
            user.setUserId(userId);

            //권한 Users 객체에 담기
            List<UserAuth> authList = ( (List<?>) roles )
                                            .stream()
                                            .map( (auth) -> new UserAuth(userId, auth.toString()) )
                                            .collect(Collectors.toList());

            user.setAuthList(authList);

            //CustomUser 에 권한 담기
            List<SimpleGrantedAuthority> authorities = ( (List<?>) roles)
                                                            .stream()
                                                            .map( auth -> new SimpleGrantedAuthority( (String) auth ) )
                                                            .collect(Collectors.toList());

            //토큰이 유효하면 name,email 담기
            try {
                Users userInfo = userMapper.select(no);
                if(userInfo != null){
                    user.setName(userInfo.getName());
                    user.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생");
            }

            UserDetails userDetails = new CustomUser(user);

            //new UsernamePasswordAuthenticationToken(사용자 정보 객체, 비밀번호, 사용자의 권한 (목록))
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception){
            log.warn("Request to parse unsupported JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (MalformedJwtException exception){
            log.warn("Request to parse invalid JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception){
            log.warn("Request to parse empty or null JWT : {} failed : {}", authHeader, exception.getMessage());
        }

        return null;
    }

    //토큰 유효성 검사
    public boolean validateToken(String jwt){

        try {
            //parsing
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("토큰 만료기간 =============" + parsedToken.getPayload().getExpiration());

            Date exp = parsedToken.getPayload().getExpiration();

            //만료시간과 현재시간 비교
            //만료 : true --> false 로 변환해서 리턴.
            //유효 : false --> true 로 변환해서 리턴.
            return !exp.before(new Date());

        } catch (ExpiredJwtException exception) {
            log.info("Token expired");//만료
            return false;
        } catch (JwtException exception) {
            log.info("Token Tampered");//손상
            return false;
        } catch (NullPointerException exception) {
            log.info("Token is null");//없음
            return false;
        } catch (Exception exception) {
            return false;
        }


    }

    //secretKey -> signingKey
    private byte[] getSigningKey(){
        return jwtProp.getSecretKey().getBytes();
    }

    //secretKey -> (HMAC-SHA algorithms) -> signingKey
    private SecretKey getShaKey(){
        return Keys.hmacShaKeyFor(getSigningKey());
    }


}
