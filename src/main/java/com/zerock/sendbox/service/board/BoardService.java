package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.BoardDTO;
import com.zerock.sendbox.dto.board.PageRequestDTO;
import com.zerock.sendbox.dto.board.PageResultDTO;
import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.Board;

public interface BoardService {

    Integer register(BoardDTO dto);

    BoardDTO get(Integer boardNo);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    void removeWithAdminAnswer(Integer boardNo);

    void modify(BoardDTO boardDTO);

    Board dtoToEntity(BoardDTO dto);

    BoardDTO entityToDTO(Board board, AdminMember adminMember, Long AdminAnswerCount);
}
