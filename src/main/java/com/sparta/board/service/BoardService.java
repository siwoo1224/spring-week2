package com.sparta.board.service;

import com.sparta.auth.entity.User;
import com.sparta.auth.jwt.JwtUtil;
import com.sparta.auth.security.UserDetailsImpl;
import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    private JwtUtil jwtUtil;

    private HttpServletRequest req;

    public BoardService(BoardRepository boardRepository,JwtUtil jwtUtil, HttpServletRequest req) {
        this.boardRepository = boardRepository;
        this.jwtUtil = jwtUtil;
        this.req = req;
    }

    /**
     * 게시판 글생성
     * @param requestDto
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Claims info = getClaims(req);

        // RequestDto -> Entity
        Board board = new Board(requestDto);
        board.setName(info.getSubject());
        // DB 저장
        Board saveBoard = boardRepository.save(board);
        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);
        return boardResponseDto;
    }

    /**
     * 게시판 글 상세보기
     * @param id
     * @return
     */
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }

    /**
     * 게시판 글 리스트
     *
     * @return
     */
    public List<BoardResponseDto> getBoardlist() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    /**
     * 게시판 글 수정
     * @param id
     * @param requestDto
     * @return
     */
    @Transactional
    public Long updateBoard(Long id, BoardRequestDto requestDto) {
        Claims info = getClaims(req);

        // 글 존재 유무
        Board board = findBoard(id);
        if (info.getSubject().equals(board.getName())) {
            board.update(requestDto);
        }else{
            throw new IllegalArgumentException("작성자만 수정할 수 있다");
        }

        return id;
    }

    /**
     *
     * @param id
     * @return
     */
    public Long deleteBoard(Long id) {
        Claims info = getClaims(req);

        // 글 존재 유무
        Board board = findBoard(id);
        if (info.getSubject().equals(board.getName())) {
            boardRepository.delete(board);
        }else{
            throw new IllegalArgumentException("작성자만 수정할 수 있다");
        }
        return id;
    }

    /**
     * 글 존재 유무
     * @param id
     * @return
     */
    private Board findBoard(Long id){
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 글이 존재하지 않습니다.")
        );
    }

    /**
     * 토큰 유효성 검증
     * @return
     */
    private Claims getClaims(HttpServletRequest req) {
        String tokenFromRequest = jwtUtil.getTokenFromRequest2(req);
        tokenFromRequest = jwtUtil.substringToken(tokenFromRequest);
        // 토큰 검증
        if (!jwtUtil.validateToken(tokenFromRequest)) {
            throw new IllegalArgumentException("Token Error");
        }
        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(tokenFromRequest);
        return info;
    }
}
