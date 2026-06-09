package com.human.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ErrorController {
    @GetMapping("/access-denied")
    public String getMethodName() {
        return "error/accessDenied";
    }
    
}
