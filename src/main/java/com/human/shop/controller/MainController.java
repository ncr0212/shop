package com.human.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    //클라이언트 요청
    //서비스명 : 메인화면 요청
    //URL  http://localhost:8080/
    //요청자원 : /
    //메서드 : get
    @GetMapping("/")
    public String getMethodName(Model model) {
        // MVC 에서 M은 model 뷰에게 데이터를 전달하는 역할
        // 아래 코드는 main/index 뷰에게 message변수에 환영이라는 데이터 전송
        model.addAttribute("message", "환영");
        
        String[] fruits = {"바나나","사과","포도","딸기"};
        // 이번에는 클라이언트에게 배열을 넘겨 본다
        model.addAttribute("fruits", fruits);
        
        return "main/index"; //뷰 이름 지정
        //문자열을 지정된 뷰이름을
        //뷰리졸브가 자동으로 해당되는 뷰를
        //찾아서 html로 변환하여 응답한다.
    }    
    
}
