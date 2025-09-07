package com.example.studyrediswithjwt.controller;

import com.example.studyrediswithjwt.service.PasswordTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    PasswordTestService passwordTestService;

    public AuthController(PasswordTestService passwordTestService) {
        this.passwordTestService = passwordTestService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "expired", required = false) String expired,
                       Model model) {

        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (logout != null) {
            model.addAttribute("message", "로그아웃되었습니다.");
        }

        if (expired != null) {
            model.addAttribute("expired", "세션이 만료되었습니다. 다시 로그인해주세요.");
        }

        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/test-password")
    @ResponseBody
    public String testPassword() {
        passwordTestService.testPassword();
        return "비밀번호 테스트 완료 - 콘솔 로그를 확인하세요";
    }







}
