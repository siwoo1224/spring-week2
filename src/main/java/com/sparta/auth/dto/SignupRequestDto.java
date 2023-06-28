package com.sparta.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Size(min = 4, max = 10, message = "이름(아이디) : 4자 이상, 10자 이하로 작성 부탁함")
    @Pattern(regexp = "^[a-z0-9]+$", message = "이름(아이디) : 소문자(a~z), 숫자(0~9)로 구성해야함")
    private String username; // 사용자 이름

    @Size(min = 8, max = 15, message = "비밀번호 : 최소 8자 이상, 15자 이하 작성 부탁함")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "비밀번호 : 대소문자(a~z, A~Z), 숫자(0~9) 구성해야함")
    @NotBlank
    private String password; //사용자 비밀번호
}