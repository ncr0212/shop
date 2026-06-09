package com.human.shop.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.human.shop.mapper.BoardFileMapper;
import com.human.shop.mapper.BoardMapper;
import com.human.shop.util.FileUploadUtil;
import com.human.shop.vo.BoardFileVO;
import com.human.shop.vo.BoardVO;
import com.human.shop.vo.PageVO;

import lombok.AllArgsConstructor;

//고객의 요청을 처리해 주는 게 역할
//메서드 네이밍시.. 고객의 요청의 냄새가 나게.. 
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{
   
   // 컨트롤러 부터 요청을 받으면 mapper에게 디비작업 콜
   // boardmapper를 의존합니다.
   // 그래서 boardmapper객체의 주소가 필요
               //BoardMapper 타입의 객체의 주소를 
//    @Autowired  //컨테이너로 부터 주입해 주는 기능
   private final BoardMapper boardmapper;
   private final BoardFileMapper boardfilemapper;
   
    @Override
    public void inserBoard(BoardVO boardvo, MultipartFile[] files) {
        //글 저장기능
        //하루에 3개만 작성하게 하자..서비스 추가
        //글에 욕설이 있는지 인공지능 필터링
        //아래 코드가 실행되면 게시글이 board 테이블에 저장
        //저장되기 전에는 id값이 없지만 저장 후에는 id 값이 생김. 
        System.out.println("전 : "+boardvo.getId());
        boardmapper.insertBoard(boardvo);
        System.out.println("후: "+boardvo.getId());
        //방금 저장된 글 번호를 알 수 있다...
        // 여기서 저장된 글 번호와 첨부파일 정보를 조합하여 board_file 테이블에
        //첨부파일이 어떤 글의 파일인지의 정보를 저장하면 된다. 
        //이전에 해야 할 일은 임시 공간에 있는 파일을 로컬 지정된 위치에 저장
        Long boardId = boardvo.getId();
        String uploadPath = "c:/upload"; //파일을 저장할 위치  
        List<BoardFileVO> fileList = FileUploadUtil.saveFiles(files, uploadPath);
        for(BoardFileVO f: fileList){
            f.setBoardId(boardId); // 첨부파일이 어떤 글의 파일인지 정보

            boardfilemapper.insertFile(f);
        }
               
    }

    @Override
    public List<BoardVO> getBoardList(PageVO pagevo) {
        //컨트롤러로 부터 리스트달라고 서비스 요청받음
        //데이터베이스 책임이 없다.
        //mapper에게 다시 요청..
        List<BoardVO> boardlist = boardmapper.selectBoardList(pagevo);
        return boardlist;
    }

    @Override
    public void deleteBoard(Long id) {
        System.out.println("서비스단에서 삭제요청 처리");
        //컨트롤러요청 처리, 매퍼에게 요청, 컨트롤러에게 응답(리턴)
        //컨셉을 데이터베이스에 삭제하기 전에 암호를 입력했는가? 체크
        //수업에서는 바로 데이터베이스에 삭제하는 방법으로 진행

        // 매퍼에게 요청
        boardmapper.deleteById(id);
        // 매퍼요청까지 끝나면 .서비스에게 리턴해야 하나? 
        // 안해도 된다고 결정하자. 그래서 리턴타입  void
    }

    @Override
    public BoardVO detailBoard(Long id) {        
       BoardVO boardVO = boardmapper.getBoardById(id);
    //    첨부파일도 가져와서 추가 
    List<BoardFileVO> flist = boardfilemapper.findByBoardId(id);
    // flist를 BoardVO에 추가
    // BoardVO에 변수가 없으므로 변수 추가 
        boardVO.setFlist(flist);
        return boardVO;
    }

    @Override
    public void updateBoard(BoardVO boardvo) {
        boardmapper.updateById(boardvo);
    }

    @Override
    public int getCount() {
        return boardmapper.getCount();
    }


}
