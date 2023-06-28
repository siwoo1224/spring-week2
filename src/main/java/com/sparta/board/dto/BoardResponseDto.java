package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;                    //seq
    private String title;               //제목
    private String name;                //이름
    private String contents;            //내용
    private LocalDateTime createdAt;    //생성날짜
    private LocalDateTime modifiedAt;   //수정날짜

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.name = board.getName();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
