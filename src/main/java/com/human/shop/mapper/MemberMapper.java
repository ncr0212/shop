package com.human.shop.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.human.shop.vo.MemberVO;

@Mapper
public interface MemberMapper {
    void insertMember(MemberVO memberVO);
    MemberVO findByLoginId(String loginId);
    
} 
