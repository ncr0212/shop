package com.human.shop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.human.shop.vo.BoardVO;
import com.human.shop.vo.PageVO;

public interface BoardService {

    void inserBoard(BoardVO boardvo, MultipartFile[] files);
    //mapper 인터페이스는 구현체가 없어 된다
    //mybatis가 알아서 처리합니다.
    //서비스 단은 인터페이스와 구현체가 짝꿍이 되어야 함
    //구현체를 하나 만든다. 

    List<BoardVO> getBoardList(PageVO pageVO);

    //삭제에 대한 서비스를 정의.
    void deleteBoard(Long id);

    //글자세히 보기 서비스. 서비스를 생각하며 네이밍
    BoardVO detailBoard(Long id);

    //글수정서비스
    void updateBoard(BoardVO boardvo);

    //전체카운트
    int getCount();



}
