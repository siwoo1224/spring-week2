package com.sparta.auth.jwt;

import com.sparta.auth.jwt.JwtUtil;
import com.sparta.auth.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// API에 전달되는 JWT 유효성 검증 및 인가 처리
// 인가 필터 ( 허가 )
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService; // 사용자가 있는지 없는지 체크하려고

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // OncePerRequestFilter 상속받으면 HttpServletRequest,HttpServletResponse 사용할수 있음

        String tokenValue = jwtUtil.getTokenFromRequest(req); //쿠키가져와서 전부다 체크 후에 값 가져옴

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        log.info("인증 처리");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        log.info("인증 객체 생성");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}