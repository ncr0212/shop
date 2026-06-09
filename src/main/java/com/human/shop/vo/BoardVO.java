package com.human.shop.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@Builder //객체 만들때
@NoArgsConstructor // 객체 만들때 빈 생성자
@AllArgsConstructor // 객체 만들 때 생성자에 모든 변수가 있다. 
public class BoardVO {

    // 게시글 번호
    private Long id;
    private String writer;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private List<BoardFileVO> flist;
}
