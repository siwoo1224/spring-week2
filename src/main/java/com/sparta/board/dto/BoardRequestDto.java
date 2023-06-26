package com.sparta.board.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class BoardRequestDto {
    private String title;       //제목
    private String name;        //이름
    private String contents;    //내용
    private String passwd;      //비밀번호
}
