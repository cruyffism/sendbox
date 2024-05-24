package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.BoardDTO;
import com.zerock.sendbox.dto.board.PageRequestDTO;
import com.zerock.sendbox.dto.board.PageResultDTO;
import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.Board;
import com.zerock.sendbox.repository.AdminAnswerRepository;
import com.zerock.sendbox.repository.AdminMemberRepository;
import com.zerock.sendbox.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AdminAnswerRepository adminAnswerRepository;
    private final AdminMemberRepository adminMemberRepository;

    @Override
    public Integer register(BoardDTO dto) {
        log.info(dto);

        // BoardDTO를 이용하여 Board 엔티티 생성
        Board board = dtoToEntity(dto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminname = authentication.getName();

        AdminMember adminMember = adminMemberRepository.findByAdminId(adminname);

        // Board 엔티티에 AdminMember를 설정
        board.setAdminMember(adminMember);

        // Board 엔티티 저장
        boardRepository.save(board);

        return board.getBoardNo();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (AdminMember) en[1], (Long) en[2]));

        Page<Object[]> result = boardRepository.getBoardWithAdminAnswerCount(
                pageRequestDTO.getPageable(Sort.by("boardNo").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Integer boardNo) {
        Object result = boardRepository.getBoardByBoardNo(boardNo);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0], (AdminMember) arr[1], (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithAdminAnswer(Integer boardNo) {
//        adminAnswerRepository.deleteByBoardNo(boardNo);
        boardRepository.deleteById(boardNo);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.getOne(boardDTO.getBoardNo());

        if (board != null) {
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            if(boardDTO.getThumbnail() != null){
                board.setThumbnail(boardDTO.getThumbnail());
            }
            boardRepository.save(board);
        }
    }

    @Override
    public Board dtoToEntity(BoardDTO dto) {

        Board board = Board.builder()
                .boardNo(dto.getBoardNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .thumbnail(dto.getThumbnail())
                .build();
        return board;
    }

    @Override
    public BoardDTO entityToDTO(Board board, AdminMember adminMember, Long AdminAnswerCount) {

        if (board == null) {
            return null;
        }

        BoardDTO boardDTO = BoardDTO.builder()

                .boardNo(board.getBoardNo())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerName(adminMember.getName())
                .AdminAnswerCount(AdminAnswerCount.intValue())
                .adminNo(adminMember.getAdminNo())
                .thumbnail(board.getThumbnail())
                .build();
        return boardDTO;
    }
}
