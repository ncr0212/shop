package com.human.shop.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVO {
    private String loginId;
    private String name;
    private String password;
    private String address;
    private String phone;
    private String email;
    private String role;
    // 필드명하고 데이터베이스 컬럼명하고 일치하지 않으면 수동으로 매핑을 해야합니다.
    // 단 표기법을 생각해서 자동 매핑이 되도록 설정할 수 있습니다. 
    // 변수명이 완전 다르면 수동 매핑해야 합니다. 
}
