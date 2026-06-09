package com.human.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.human.shop.vo.BoardFileVO;

@Mapper
public interface BoardFileMapper {
    void insertFile(BoardFileVO boardFileVO);
    List<BoardFileVO> findByBoardId(Long BoardId);
}
