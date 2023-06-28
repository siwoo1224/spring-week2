package com.sparta.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest
class BoardApplicationTests {

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    @Test
    void contextLoads() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        String coverted = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(coverted);

        String str = "항해9915기최시우입니다.";
        byte[] bytes23 = Base64.getEncoder().encode(str.getBytes());
        String coverted2 = new String(bytes23, StandardCharsets.UTF_8);
        System.out.println(coverted2);
    }

}
