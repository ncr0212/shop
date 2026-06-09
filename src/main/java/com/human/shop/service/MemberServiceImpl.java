package com.human.shop.service;

import com.human.shop.controller.BoardController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.human.shop.mapper.MemberMapper;
import com.human.shop.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //final로 지정된 변수 주소 주입 (롬복 기능)
public class MemberServiceImpl implements MemberService {

    private final BoardController boardController;
    private final MemberMapper memberMapper;
    // 시큐리티 컨피그에서 등록된 객체의 주소를 주입 
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void join(MemberVO memberVO) {
        // 사용자가 입력한 평문을 암호화 시킨다. 
        String encodedPassword = passwordEncoder.encode(memberVO.getPassword());
        memberVO.setPassword(encodedPassword);
        // 컨셉을 추가한다면 클라이언트로부터 일반사용자, vip, 관리자 등을 
        // 체크하게 하고 체크를 확인하여 USER, VIP, ADMIN으로 등급을 나눈다.
        memberVO.setRole("USER");
        memberMapper.insertMember(memberVO);
    }

}
