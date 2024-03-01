package com.demo.jewel.security.jwt.filter;

import com.demo.jewel.dto.CustomUser;
import com.demo.jewel.dto.UserAuth;
import com.demo.jewel.security.jwt.constants.JwtConstants;
import com.demo.jewel.security.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    //생성자
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

        //필터 URL 경로 설정 : /login
        setFilterProcessesUrl("/login");
    }

    //인증 시도 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("username : " + username);
        log.info("password : " + password);

        //사용자 인증 정보 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        // 사용자 인증 (로그인)
        authentication = authenticationManager.authenticate(authentication);

        log.info("인증 여부 : " + authentication.isAuthenticated());

        // 인증 실패 (username, password 불일치)
        if(!authentication.isAuthenticated()){
            log.info("아이디 또는 비밀번호가 일치하지 않습니다.");
            response.setStatus(401); // 401 (unAuthorized) 반환
        }

        return authentication;
    }

    //인증 성공 후 작업 메서드
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {

        log.info("인증 성공=====================");

        CustomUser user =(CustomUser) authentication.getPrincipal();
        int userNo = user.getUser().getNo();
        String userId = user.getUser().getUserId();
        List<String> roles = user.getUser().getAuthList().stream()
                                                        .map( (auth) -> auth.getAuth() )
                                                        .collect( Collectors.toList() );

        //JWTProvider
        String jwt = jwtTokenProvider.createToken(userNo, userId, roles);

        //{ Authorization : Bearer + {jwt} }
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + jwt);
        response.setStatus(200);

    }




















}
