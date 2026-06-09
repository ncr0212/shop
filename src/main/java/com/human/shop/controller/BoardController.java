package com.human.shop.controller;

import com.human.shop.service.BoardServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.shop.service.BoardService;
import com.human.shop.vo.BoardVO;
import com.human.shop.vo.PageVO;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@AllArgsConstructor //실무 스타일 
public class BoardController {
 
    //Boardservice에게 의존
    // @Autowired  //컨테이너로 부터 주소 주입 받겠다.
    private final BoardService boardService;

    //클라이언트가 글의 목록을 요청할 때 처리
    @GetMapping("/boards")
    public String getMethodName(
        @ModelAttribute PageVO pageVO, Model model) {
        System.out.println("page: "+pageVO.getPage());
        System.out.println("offset: "+pageVO.getOffset());
        //데이터베이스로 부터 데이틀 가져오는 작업.
        //컨트롤러는 데이터베이스에서 자료를 가져오는 책임이 없다.
        int totalCount=boardService.getCount();
        pageVO.pageInfo(totalCount);
        System.out.println(pageVO.toString());


        //서비스 레이어에게 요청을 한다.
        List<BoardVO> boardlist = boardService.getBoardList(pageVO);
        //컨트롤러가 서비스에 요청하고 서비스는 매퍼layer에 요청
        //컨트롤러가 그 데이터를 받았다.
        //컨트롤러는 클라이언트 요청 매핑 > 서비스단 > 뷰를 지정
        //즉, boardlist를 뷰에게 넘긴다.  필요한 객체 Model
        model.addAttribute("boards", boardlist);
        model.addAttribute("pageVO", pageVO);
        return "board/list";
    }


    @PostMapping("/boards")
    public String postMethodName(@ModelAttribute BoardVO boardvo,
        @RequestParam("files") MultipartFile[] files 
    ) {
        //@RequestParam  변수 한 개씩 받을 때 사용
        //@ModelAttribute vo객체로 받는다.. 파라미티변수명과
        // Multipartfile 첨부파일의 메타데이터 정보를 저장하는 객체
        for (MultipartFile f : files){
            System.out.println(f.getOriginalFilename());
            System.out.println(f.getSize());
        }
        // vo의 변수명이 같으면 자동으로 매핑해 준다.
        // 글쓰기 화면에서 writer, title, content가 입력되면
        // 세 개의 정보를 boardvo변수로 받겠다.. 예전  DTO 개념
        // System.out.println(boardvo.toString());
        boardService.inserBoard(boardvo, files);
        return "redirect:/boards";
    }
    
    @GetMapping("/boards/write")  //클라이언트 요청 자원을 매핑해서 처리
    public String boardWrite() {
        //@RequestParam 어노테이션은 클라이언트가 보낸 파마미터를 받는다
        //여기서는 필요없어서 삭제
        return "board/write";  //뷰이름
    }

    //삭제
    //만약 클라이언트가  /boards/delete?id=30 보냈다면  querystring방식 -> @RequestParam받음
    @GetMapping("/boards/delete/{id}")  //{} 파라미터 받는다. 1개.. Pathvariable방식
    public String deleteBoard(@PathVariable("id") Long id) {
        // 아래 처럼 고민하는 이유가 3Layers 설계 했기 때문에..
        // 고민 : 서비스단에 요청해야 하는가?
        // 극단적으로 말하면 컨트롤러는 매퍼가 있는지도 몰라도 됩니다.
        // 클라이언트는 글 삭제를 요청 .. 데이테베이스 삭제 역한 mapper
        // 결론:서비스 단에 요청해야 한다.
        // 기술적으로 요청은 어떻게 하는가? 의존관계, 메서드 콜과 리턴
        boardService.deleteBoard(id);
        return "redirect:/boards";  //다시 리쿼스트 요청, 컨트롤러로 요청, 뷰이름이 아님
        //고급적으로 표현하자면 뷰리졸브, 뷰리졸브는 뷰이 이름으로 매핑 + 다시요청하능 기능
        // 1. 리스트로 화면 으로 바꾸려고요.. 틀린답은 아닙니다.. 임팩트 그런 답은 아니죠
        // 2. 역할을 추가 설명, deleteboard의 역할을 삭제의 역할이고
        //   추후 보여 줄 화면은 boards요청이 당담하고 있어서. redirect로 설정했어요
        //  + 뷰리졸브는 redirect 를 보고 다시 request를 요청하는 것으로 알고 있어요.
    }

    @GetMapping("/boards/{id}")
    public String getMethodName(@PathVariable("id") Long id,
                                    Model model) {
        // 클라이언트가 id를 보내야 하는 이유는
        // 서비스가 글 자세히 보기..  쿼리문이 select * from board where deleted=0 and id=#{id}
        // 자세히 보려는 글을 테이블에서 찾아야 하기 때문에, 테이블에서
        // 유니크한 속성인  id값이 제격이다. 
        //boardService.detailBoard(id);
        model.addAttribute("board", 
                            boardService.detailBoard(id));

        // 1. 첨부파일만 별도로 가져와서 뷰에게 넘긴다
        // model.addAttribute("files",데이터베이스로부터 가져온 리스트)
        // 2. 어차피 boardService.detailBoard(id)를 넘기니까
        // BoardVO에 첨부파일 저장하는 편수 하나 추가해서 
        // 서비스단에서 추가해 달라고 하고.. 컨트롤러는 수정 X
        // 서비스단에 하는 것이 역할을 생각했을 때 더 낫다라는 판단.. 

        return "board/detail";
    }

    @GetMapping("/boards/edit/{id}")
    public String getBoardEdit(@PathVariable("id") Long id,
                                    Model model) {
        //클라이언트가 보낸 아이디 값으로 데이터베이스 튜플 가져와야 함
        model.addAttribute("board", 
                            boardService.detailBoard(id));
        return "board/edit";
    }
    @PostMapping("/boards/update")
    public String boardUpdate(@ModelAttribute BoardVO boardvo) {
        // 수정한 글의 아이디가 넘어 오는지 확인 하기 위한 코드
        //System.out.println(boardvo.toString());
        boardService.updateBoard(boardvo);
        return "redirect:/boards";
    }

    // 이론추가 : 컨트롤러는 어떻게 응답을 하느냐... view를 이용해서. 우리 같은 경우는 타임리프
    // 첨부파일은 파일 자체로 응답을 해야한다.
    @ResponseBody 
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> getMethodName(@PathVariable("filename") String filename) {
        String uploadDir = "C:\\upload";
        File file = new File(uploadDir, filename);

        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
    
    
    
    
    
}
