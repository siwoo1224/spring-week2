package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.Message;
import com.sparta.board.dto.StatusEnum;
import com.sparta.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     *
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/board/{id}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long id) {
        Long aLong = boardService.deleteBoard(id);
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        message.setStatus(StatusEnum.OK2);
        message.setMessage(aLong + "게시글 삭제완료");
        message.setData(aLong);
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
