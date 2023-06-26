package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시판 글 생성
     * @param requestDto
     * @return
     */
    @PostMapping("/boardCreate")
    public BoardResponseDto boardCreate(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    /**
     * 게시판 글 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/boardDetail/{id}")
    public BoardResponseDto board(@PathVariable Long id) {
        // Map To List
//        return new BoardResponseDto(boardList.get(id));
        return boardService.getBoard(id);
    }

    /**
     * 게시판 글 리스트
     * @return
     */
    @GetMapping("/boardList")
    public List<BoardResponseDto> boardList() {
        // Map To List
//        return boardList.values().stream().map(BoardResponseDto::new).toList();
        return boardService.getBoardlist();
    }

    /**
     * 게시판 글 업데이트
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/boardUpdate/{id}")
    public Long boardUpdate(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @param requestDto
     * @return
     */
    @DeleteMapping("/boardDelete/{id}")
    public Long boardDelete(@PathVariable Long id, @RequestBody  BoardRequestDto requestDto) {

        return boardService.deleteBoard(id, requestDto);
    }
}
