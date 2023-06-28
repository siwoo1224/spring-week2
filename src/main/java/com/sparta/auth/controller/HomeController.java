package com.sparta.auth.controller;

import com.sparta.auth.entity.User;
import com.sparta.auth.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    /**
     * 인덱스
     *
     * @return
     */
    @GetMapping("/")
    @ResponseBody
    public User index(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails.getUser();
    }
}
