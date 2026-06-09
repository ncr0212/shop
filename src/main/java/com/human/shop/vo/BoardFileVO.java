package com.human.shop.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardFileVO {
    private Long id;
    private Long boardId;
    private String originalName;
    private String savedName;
    private String filePath;
    private Long fileSize;
}
