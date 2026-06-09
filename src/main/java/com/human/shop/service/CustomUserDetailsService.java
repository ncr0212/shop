package com.human.shop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.human.shop.mapper.MemberMapper;
import com.human.shop.security.CustomUserDetails;
import com.human.shop.vo.MemberVO;

import lombok.RequiredArgsConstructor;
// 사용자 정보를 데이터베이스부터 가져오는 역할
// 가져온 데이터베이스 정보와 클라이언트가 보낸 비밀번호 일치를 판단한다. 
// 이런 역할을 하는 스프링에서 제공하는 UserDetailsService 객체를
// 구현받아서 작업한다.
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // 기능을 구현받고, 우리 프로젝트에게 설정한 데이터베이스 객체를  가져온다.
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //3layer를 거치지 않고 바로 조회
        MemberVO member = memberMapper.findByLoginId(username);
        if (member==null)
            throw new UsernameNotFoundException("사용자없음");
        //member라는 객체가 현재 로그인을 시도하는 사람
        //스프링은 이 객체의 타입을 모른다. 
        //이 객체로 스프링이 이해할 수 있는 userDetails라는 객체로 감싼다
        //감싸는 작업을 랩핑이라고 한다. 

        System.out.println("아이디와 비번 검증 성공");
        return new CustomUserDetails(member);
    }

}
