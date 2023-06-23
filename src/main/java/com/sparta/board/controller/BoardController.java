package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final Map<Long, Board> boardList = new HashMap<>();

    @PostMapping("/boardCreate")
    public BoardResponseDto boardCreate(@RequestBody BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);

        //MAX ID Check
        Long maxId = boardList.size() > 0 ? Collections.max(boardList.keySet()) + 1 : 1;
        board.setId(maxId);

        // 메모리 디비 저장
        boardList.put(board.getId(), board);

        // Entity -> BoardResponseDto
        return new BoardResponseDto(board);
    }

    @GetMapping("/boardDetail/{id}")
    public BoardResponseDto board(@PathVariable Long id) {
        // Map To List
        return new BoardResponseDto(boardList.get(id));
    }

    @GetMapping("/boardList")
    public List<BoardResponseDto> boardList() {
        // Map To List
        return boardList.values().stream().map(BoardResponseDto::new).toList();
    }

    @PutMapping("/boardUpdate/{id}")
    public Long boardUpdate(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        if(boardList.containsKey(id)) {
            if (requestDto.getPasswd().equals(boardList.get(id).getPasswd())) {
                // 해당 메모 가져오기
                Board board = boardList.get(id);
                // memo 수정
                board.update(requestDto);
                return board.getId();
            }else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
            }
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/boardDelete/{id}")
    public Long boardDelete(@PathVariable Long id, @RequestBody  BoardRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        System.out.println(requestDto.equals(boardList.get(id).getPasswd()));
        System.out.println(requestDto);
        System.out.println(boardList.get(id).getPasswd());
        if(boardList.containsKey(id)) {
            if (requestDto.getPasswd().equals(boardList.get(id).getPasswd())) {
                // 해당 메모 삭제하기
                boardList.remove(id);
                return id;
            }else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }
}
