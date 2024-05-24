package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.AdminAnswerDTO;
import com.zerock.sendbox.entity.AdminAnswer;
import com.zerock.sendbox.entity.Board;
import com.zerock.sendbox.repository.AdminAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAnswerServiceImpl implements AdminAnswerService{

    private final AdminAnswerRepository adminAnswerRepository;

    @Override
    public Integer register(AdminAnswerDTO adminAnswerDTO) {
        AdminAnswer adminAnswer = dtoToEntity(adminAnswerDTO);

        adminAnswerRepository.save(adminAnswer);

        return adminAnswer.getAnswerNo();
    }

    @Override
    public List<AdminAnswerDTO> getLIst(Integer boardNo) {
        List<AdminAnswer> result = adminAnswerRepository
                .getAdminAnswersByBoardOrderByAnswerNo(Board.builder().boardNo(boardNo).build());
        return result.stream().map(adminAnswer -> entityToDTO(adminAnswer)).collect(Collectors.toList());
    }

    @Override
    public void modify(AdminAnswerDTO adminAnswerDTO) {
        AdminAnswer adminAnswer = dtoToEntity(adminAnswerDTO);

        adminAnswerRepository.save(adminAnswer);
    }

    @Override
    public void remove(Integer answerNo) {
        adminAnswerRepository.deleteById(answerNo);
    }
}
