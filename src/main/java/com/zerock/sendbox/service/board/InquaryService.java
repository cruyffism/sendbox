package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.InquaryDTO;
import com.zerock.sendbox.dto.board.PageRequestDTO2;
import com.zerock.sendbox.dto.board.PageResultDTO2;
import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.UserMember;

public interface InquaryService {

    Integer register(InquaryDTO dto);

    InquaryDTO get(Integer inquaryNo);

    PageResultDTO2<InquaryDTO, Object[]> getList(PageRequestDTO2 pageRequestDTO2);

    void removeWithReplies(Integer inquaryNo);

    void modify(InquaryDTO inquaryDTO);


    Inquary dtoToEntity(InquaryDTO dto);

    InquaryDTO entityToDTO(Inquary inquary, UserMember userMember, Long replyCount);
}
