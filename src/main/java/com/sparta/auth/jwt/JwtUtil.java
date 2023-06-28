package com.sparta.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간 60분
    private final long TOKEN_TIME = 60 * 60 * 1000L;
    // Base64 Encode 한 SecretKey
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct  //@PostConstruct는 의존성 주입이 이루어진 후 초기화를 수행하는 메서드이다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    /**
     * 토큰 생성
     * @param username
     * @return
     */
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // 압축? String 으로 바꿔줌
    }
    /**
     * JWT Cookie 에 저장
     * @param token
     * @param res
     */
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            // 자바 URLEncoding
            // 쿠키와 같이 한글을 표현하지 못하는 경우 한글을 ASCII값으로 인코딩해주야 합니다.
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    /**
     * JWT 토큰 substring
     * @param tokenValue
     * @return
     */
    public String substringToken(String tokenValue) {
        //StringUtils.hasText 공백 + null 확인 해줌! (str != null && !str.isEmpty() && containsText(str))
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // |Bearer |   => 6글자 + 공백
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }
    /**
     * 토큰 검증
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // return type Claims
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    /**
     * 토큰에서 사용자 정보 가져오기
     * @param token
     * @return
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    /**
     * HttpServletRequest 에서 Cookie Value : JWT 가져오기
     * @param req
     * @return
     */
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) { // 가져온 쿠키 전부다 검사
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param token
     * @param res
     */
    public void addJwtToHeader(String token, HttpServletResponse res) {
        try {
            // 자바 URLEncoding
            // 쿠키와 같이 한글을 표현하지 못하는 경우 한글을 ASCII값으로 인코딩해주야 합니다.
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            res.setHeader(AUTHORIZATION_HEADER, token);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    /**
     *
     * @param req
     * @return
     */
    public String getTokenFromRequest2(HttpServletRequest req) {
        String token = req.getHeader(AUTHORIZATION_HEADER);
        if(token != null) {
            try {
                return URLDecoder.decode(token, "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }
}