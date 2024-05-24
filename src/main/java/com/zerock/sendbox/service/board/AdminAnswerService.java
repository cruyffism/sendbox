package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.AdminAnswerDTO;
import com.zerock.sendbox.entity.AdminAnswer;
import com.zerock.sendbox.entity.Board;

import java.util.List;

public interface AdminAnswerService {
    Integer register(AdminAnswerDTO adminAnswerDTO);

    List<AdminAnswerDTO> getLIst(Integer boardNo);

    void modify(AdminAnswerDTO adminAnswerDTO);

    void remove(Integer answerNo);

    default AdminAnswer dtoToEntity(AdminAnswerDTO adminAnswerDTO) {
        Board board = Board.builder().boardNo(adminAnswerDTO.getBoardNo()).build();

        AdminAnswer adminAnswer = AdminAnswer.builder()
                .answerNo(adminAnswerDTO.getAnswerNo())
                .content(adminAnswerDTO.getContent())
                .replyer(adminAnswerDTO.getReplyer())
                .board(board)
                .build();
        return adminAnswer;
    }

    default AdminAnswerDTO entityToDTO(AdminAnswer adminAnswer) {
        AdminAnswerDTO dto = AdminAnswerDTO.builder()
                .answerNo(adminAnswer.getAnswerNo())
                .content(adminAnswer.getContent())
                .replyer(adminAnswer.getReplyer())
                .regDate(adminAnswer.getRegDate())
                .modDate(adminAnswer.getModDate())
                .build();
        return dto;
    }

}
