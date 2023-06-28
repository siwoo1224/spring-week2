package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService, HttpServletRequest req) {
        this.boardService = boardService;
    }

    /**
     * 게시판 글 생성
     * @param requestDto
     * @return
     */
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }
    /**
     * 게시판 글 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }
    /**
     * 게시판 글 리스트
     *
     * @return
     */
    @GetMapping("/board")
    public List<BoardResponseDto> getBoardlist() {
        return boardService.getBoardlist();
    }
    /**
     * 게시판 글 업데이트
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/board/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/board/{id}")
    public Long deleteBoard(@PathVariable Long id) {
        return boardService.deleteBoard(id);
    }
}
