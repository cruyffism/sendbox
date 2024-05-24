package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.ReplyDTO;
import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.Reply;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.InquaryRepository;
import com.zerock.sendbox.repository.ReplyRepository;
import com.zerock.sendbox.repository.UserMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    private final InquaryRepository inquaryRepository;

    @Override
    public Integer register(ReplyDTO replyDTO) {
        // ReplyDTO에서 Reply 엔티티로 변환
        Reply reply = dtoToEntity(replyDTO);

        // ReplyDTO에서 조회 번호를 가져옴
        Integer inquaryNo = replyDTO.getInquaryNo();

        // 데이터베이스에서 실제 Inquary 엔티티를 가져옴
        Inquary inquary = inquaryRepository.findById(inquaryNo)
                .orElseThrow(() -> new IllegalArgumentException("Id에 해당하는 Inquary를 찾을 수 없습니다: " + inquaryNo));

        // Reply 엔티티에 조회 엔티티를 설정
        reply.setInquary(inquary);

        // Reply 엔티티를 저장
        replyRepository.save(reply);

        // 저장된 Reply 엔티티의 댓글 번호 반환
        return reply.getReplyNo();
    }



    @Override
    public List<ReplyDTO> getList(Integer inquaryNo) {
        List<Reply> result = replyRepository
                .getRepliesByInquaryOrderByReplyNo(Inquary.builder().inquaryNo(inquaryNo).build());
        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);
    }

    @Override
    public void remove(Integer replyNo) {
        replyRepository.deleteById(replyNo);
    }
}
