package com.example.training.java.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final Environment environment;

//    @GetMapping("/getenv")
//    @ResponseBody
//    public String[] getEnv() {
//        return environment.getActiveProfiles();
//    }

    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/manager")
    public String getManager(HttpServletRequest httpServletRequest) {
        log.info("========= vao manager roi...");
        log.info("========= vao manager roi...{}", httpServletRequest.getRemoteAddr());
        return "/manager";
    }

//    @GetMapping("/login_fail")
//    public String loginFail() {
//        return "/login_fail";
//    }
}

