package com.sparta.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.auth.dto.LoginRequestDto;
import com.sparta.auth.jwt.JwtUtil;
import com.sparta.auth.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // UsernamePasswordAuthenticationFilter 역할
    // 사용자가 username이랑 passwd를 보내면 그걸
    // 인증객체 즉, UsernamePasswordAuthenticationToken을 만든 다음에
    // AuthenticationManager를 통해서 확인까지 하는 작업

    // UsernamePasswordAuthenticationFilter 그대로 사용하면 세션방식
    // 나는 토큰으로 로그인 처리하기때문에 커스텀할거

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login"); // 내가 설계한 post 로그인 URI
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            // JSON -> 오브젝트로 바꾸는거 LoginRequestDto
            // request.getInputStream() 해당 데이터 가져오기
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = jwtUtil.createToken(username);
        jwtUtil.addJwtToCookie(token, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}