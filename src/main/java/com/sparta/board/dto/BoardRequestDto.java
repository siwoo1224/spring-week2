package com.sparta.board.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class BoardRequestDto {
    private String title;
    private String name;
    private String contents;
    private Date date;
    private String passwd;
}
