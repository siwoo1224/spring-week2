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
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    //@PostConstruct는 의존성 주입이 이루어진 후 초기화를 수행하는 메서드이다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    // 토큰 생성
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
//                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // 압축? String 으로 바꿔줌
    }

    // JWT Cookie 에 저장
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

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        //StringUtils.hasText 공백 + null 확인 해줌! (str != null && !str.isEmpty() && containsText(str))
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // |Bearer |   => 6글자 + 공백
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            //토큰의 클레임을 검증하고 파싱하는 JWT 파서를 생성한 다음, 해당 파서를 사용하여 토큰의 클레임을 파싱하고 유효성을 검증하는 작업을 수행합니다.
            /*
            Jwts.parserBuilder(): 이 메서드는 JWT 파서의 빌더 객체를 생성합니다.
            setSigningKey(key): 이 메서드는 JWT의 서명 키를 설정합니다. 서명 키는 JWT를 생성할 때 사용된 비밀 키와 일치해야 합니다. 이 서명 키를 사용하여 JWT의 유효성을 검증합니다.
            build(): 이 메서드는 설정된 옵션에 따라 JWT 파서를 빌드하여 반환합니다.
            parseClaimsJws(token): 이 메서드는 주어진 토큰을 파싱하고, 서명을 검증하여 유효한 JWT인 경우 클레임 (claims)을 추출합니다. 이때 추출된 클레임은 Claims 객체로 반환됩니다.
            */
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

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
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
}