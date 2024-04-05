package com.polarbookshop.catalogservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String getGreeting() {
        return "커널 라이브러리에 오신 것을 환영합니다!!";
    }
}
