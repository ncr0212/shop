package com.human.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @PostMapping("/login")
    public String postMethodName(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password
    ) {
        //RequestParam 클라이언트가 보낸 변수를 저장한다.
        //@RequestParam("password") 클라이언트가 보낸 변수
        // String password  저장할 변수명
        System.out.println(loginId + password);
        return "main/index";  //뷰이름이 마땅치가 않아서 아무거나
    }

    @GetMapping("/login")
    public String getMethodName() {
        return "login/login";
    }
    


}
