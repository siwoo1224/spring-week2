package com.sparta.auth.controller;

import com.sparta.auth.dto.LoginRequestDto;
import com.sparta.auth.dto.SignupRequestDto;
import com.sparta.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    /**
     * 회원가입 처리
     * @param requestDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/signup")
    @ResponseBody
    public Map<String, Object> signup(@Valid @RequestBody SignupRequestDto requestDto,
                            BindingResult bindingResult,
                            HttpServletResponse res) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return status("회원가입 실패", res.SC_BAD_REQUEST);
        }
        userService.signup(requestDto);
        return status("회원가입 성공", 200);
    }

    @PostMapping("/user/login")
    @ResponseBody
    public Map<String, Object> login(@Valid @RequestBody LoginRequestDto requestDto,
                        BindingResult bindingResult,
                        HttpServletResponse res){
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return status("로그인 실패", res.SC_BAD_REQUEST);
        }
        userService.login(requestDto, res);
        return status("로그인 성공", 200);
    }

    public Map<String, Object> status(String msg, int status) {

        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("status", status);

        return map;
    }
}