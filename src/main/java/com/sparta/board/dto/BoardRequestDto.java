package com.sparta.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String title;       //제목
    private String name;        //이름
    private String contents;    //내용
}
