package com.human.shop.controller;

import org.springframework.stereotype.Controller;

import com.human.shop.service.MemberService;
import com.human.shop.vo.MemberVO;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public String join(@ModelAttribute MemberVO memberVO) {
        memberService.join(memberVO);
        
        return "redirect:/";
    }
    
}
