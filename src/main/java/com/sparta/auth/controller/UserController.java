package com.sparta.auth.controller;

import com.sparta.auth.dto.LoginRequestDto;
import com.sparta.auth.dto.SignupRequestDto;
import com.sparta.auth.service.UserService;
import com.sparta.board.dto.Message;
import com.sparta.board.dto.StatusEnum;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
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
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto requestDto,
                                            BindingResult bindingResult,
                                            HttpServletResponse res){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        message.setStatus(StatusEnum.OK2);
        message.setMessage("회원가입 성공");
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("회원가입 실패");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        userService.signup(requestDto);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    @ResponseBody
    public ResponseEntity<Message> login(@Valid @RequestBody LoginRequestDto requestDto,
                        BindingResult bindingResult,
                        HttpServletResponse res){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        message.setStatus(StatusEnum.OK2);
        message.setMessage("로그인 성공");
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("로그인 실패");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        userService.login(requestDto, res);
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

}