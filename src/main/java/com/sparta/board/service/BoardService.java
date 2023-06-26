package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시판 글 생성
     * @param requestDto
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);
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
        Board board = findBoard(id);
        if(board.getPasswd().equals(requestDto.getPasswd())){
            board.update(requestDto);
        }else{
            throw new IllegalArgumentException("선택한 글이 존재하지 않습니다.");
        }

        return id;
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @param requestDto
     * @return
     */
    @Transactional
    public Long deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);
        if(board.getPasswd().equals(requestDto.getPasswd())){
            boardRepository.delete(board);
        }else{
            throw  new IllegalArgumentException("선택한 글이 존재하지 않습니다.");
        }

        return id;
    }

    private Board findBoard(Long id){
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 글이 존재하지 않습니다.")
        );
    }
}
