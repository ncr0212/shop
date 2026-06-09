package com.human.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.human.shop.vo.BoardVO;
import com.human.shop.vo.PageVO;

//데이터베이스 작업을 하는게 역할
@Mapper
public interface BoardMapper {
    public void insertBoard(BoardVO boardvo);
    //selectBoardList 이름만 보면 보드에검색해서리스트로 가졌오겠다.
    List<BoardVO> selectBoardList(PageVO pageVO);
    //삭제요청 - 삭제하겠다. id를 조건으로  
    void deleteById(Long id);
    //매퍼(DAO)의 역할 : 서비스의 요청 처리, 데이터베이스 작업 실행, 서비스에게 응답(리턴)

    //id로 튜플하나 가져오자
    BoardVO getBoardById(Long id);

    void updateById(BoardVO boardvo);

    int getCount(); // 페이지 수?

} 
