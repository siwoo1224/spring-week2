package com.sparta.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.repository.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board") // 매핑할 테이블의 이름을 지정
public class Board  extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.name = requestDto.getName();
        this.contents = requestDto.getContents();
    }

    public void update(BoardRequestDto requestDto, String subject) {
        this.title = requestDto.getTitle();
        this.name = subject;
        this.contents = requestDto.getContents();
    }

}
