package com.zerock.sendbox.service.board;

import com.zerock.sendbox.dto.board.*;
import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.UserMember;
import com.zerock.sendbox.repository.InquaryRepository;
import com.zerock.sendbox.repository.ReplyRepository;
import com.zerock.sendbox.repository.UserMemberRepository;
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
public class InquaryServiceImpl implements InquaryService{

    private final InquaryRepository inquaryRepository;

    private final ReplyRepository replyRepository;

    private final UserMemberRepository userMemberRepository;

    @Override
    public Integer register(InquaryDTO dto) {
        log.info(dto);

        Inquary inquary = dtoToEntity(dto);

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 여기서 사용자 유저네임이 반환되는 것으로 가정합니다.

        // 가져온 사용자 정보를 이용하여 필요한 작업 수행
        UserMember userMember = userMemberRepository.findByUserId(username);

        inquary.setUserMember(userMember);

        inquaryRepository.save(inquary);

        return inquary.getInquaryNo();
    }


    @Override
    public PageResultDTO2<InquaryDTO, Object[]> getList(PageRequestDTO2 pageRequestDTO2) {

        log.info(pageRequestDTO2);

        Function<Object[], InquaryDTO> fn = (en -> entityToDTO((Inquary)en[0], (UserMember)en[1], (Long) en[2]));

        Page<Object[]> result = inquaryRepository.getInquaryWithReplyCount(
                pageRequestDTO2.getPageable(Sort.by("inquaryNo").descending()));

        return new PageResultDTO2<>(result, fn);

    }

    @Override
    public InquaryDTO get(Integer inquaryNo) {
        Object result = inquaryRepository.getInquaryByInquaryNo(inquaryNo);

        Object[] arr = (Object[])result;

        return entityToDTO((Inquary)arr[0], (UserMember)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Integer inquaryNo) {

        // replyRepository.deleteByInquaryNo(inquaryNo);

        inquaryRepository.deleteById(inquaryNo);
    }

    @Transactional
    @Override
    public void modify(InquaryDTO inquaryDTO) {
        Inquary inquary = inquaryRepository.getOne(inquaryDTO.getInquaryNo());

        if(inquary != null) {
            inquary.changeTitle(inquaryDTO.getTitle());
            inquary.changeContent(inquaryDTO.getContent());

            inquaryRepository.save(inquary);
        }
    }

    @Override
    public Inquary dtoToEntity(InquaryDTO dto) {

        Inquary inquary = Inquary.builder()
                .inquaryNo(dto.getInquaryNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return inquary;
    }

    @Override
    public InquaryDTO entityToDTO(Inquary inquary, UserMember userMember, Long replyCount) {

        if (inquary == null) {
            return null;
        }

        InquaryDTO inquaryDTO = InquaryDTO.builder()

                .inquaryNo(inquary.getInquaryNo())
                .title(inquary.getTitle())
                .content(inquary.getContent())
                .regDate(inquary.getRegDate())
                .modDate(inquary.getModDate())
                .userId(userMember.getUserId())
                .memberName(userMember.getName())
                .replyCount(replyCount.intValue())
                .build();
        return inquaryDTO;
    }

}
