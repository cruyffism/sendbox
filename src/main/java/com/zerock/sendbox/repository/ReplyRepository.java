package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Modifying
    @Query("delete from Reply r where r.inquary.inquaryNo =:inquaryNo")
    void deleteByInquaryNo(Integer inquaryNo);

    List<Reply> getRepliesByInquaryOrderByReplyNo(Inquary inquary);

}
