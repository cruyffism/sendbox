package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.ReplyDTO;
import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.Reply;

import java.util.List;

public interface ReplyService {

    Integer register(ReplyDTO replyDTO);

    List<ReplyDTO> getList(Integer inquaryNo);

    void modify(ReplyDTO replyDTO);

    void remove(Integer replyNo);

    default Reply dtoToEntity(ReplyDTO replyDTO) {
        Inquary inquary = Inquary.builder().inquaryNo(replyDTO.getReplyNo()).build();

        Reply reply = Reply.builder()
                .content(replyDTO.getContent())
                .replyer(replyDTO.getReplyer())
                .build();
        return reply;
    }

    default ReplyDTO entityToDTO(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder()

                .content(reply.getContent())
                .replyer(reply.getReplyer())

                .build();
        return dto;
    }
}
