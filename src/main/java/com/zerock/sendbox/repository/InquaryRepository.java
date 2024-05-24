package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Inquary;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquaryRepository extends JpaRepository<Inquary, Integer>{

    @Query("select b, w from Inquary b left  join b.userMember w where b.inquaryNo =:inquaryNo")
    Object getInquaryWithMember(@Param("inquaryNo") Integer inquaryNo);

    @Query("select b, r from Inquary b left join Reply r on r.inquary = b where b.inquaryNo =:inquaryNo")
    List<Object[]> getInquaryWithReply(@Param("inquaryNo") Integer inquaryNo);

    @Query(value = "select b, w, count(r) " +
            " from Inquary b " +
            " left join b.userMember w " +
            " left join Reply r on r.inquary = b " +
            " group by b ",
            countQuery = "select count(b) from Inquary b")
    Page<Object[]> getInquaryWithReplyCount(Pageable pageable);

    @Query("select b, w, count(r) " +
            " from Inquary b left join b.userMember w " +
            " left outer join Reply r on r.inquary = b" +
            " where b.inquaryNo =:inquaryNo")
    Object getInquaryByInquaryNo(@Param("inquaryNo") Integer inquaryNo);
}
