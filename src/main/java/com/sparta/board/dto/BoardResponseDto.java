package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

import java.util.Date;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String name;
    private String contents;
    private Date date;
    private String passwd;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.name = board.getName();
        this.contents = board.getContents();
        this.date = board.getDate();
        this.passwd = board.getPasswd();
    }
}
