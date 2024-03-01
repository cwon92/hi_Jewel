package com.demo.jewel.security.jwt.filter;

import com.demo.jewel.security.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtRequestFilter( JwtTokenProvider jwtTokenProvider ){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // jwt 요청 필터
    // request > header > Authorization
    // JWT 토큰 유효성 검사
    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {



    }
}
