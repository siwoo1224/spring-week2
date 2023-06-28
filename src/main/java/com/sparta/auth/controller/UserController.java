package com.sparta.auth.controller;

import com.sparta.auth.dto.SignupRequestDto;
import com.sparta.auth.entity.User;
import com.sparta.auth.security.UserDetailsImpl;
import com.sparta.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 페이지
     * @return
     */
    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    /**
     * 회원가입 페이지
     * @return
     */
    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    /**
     * 회원가입 처리
     * @param requestDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }
}