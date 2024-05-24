package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Board;
import com.zerock.sendbox.repository.search.SearchBoardRepository;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{

    @Query("select b, w from Board b left join b.adminMember w where b.boardNo =:boardNo")
    Object getBoardWithWriter(@Param("boardNo") Integer boardNo);


    @Query("select b, r from Board b left join AdminAnswer  r on r.board = b where b.boardNo =:boardNo")
    List<Object[]> getBoardWithAdminAnswer(@Param("boardNo") Integer boardNo);

    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.adminMember w " +
            " LEFT JOIN AdminAnswer r ON r.board = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithAdminAnswerCount(Pageable pageable);

    @Query("SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.adminMember w " +
            " LEFT OUTER JOIN AdminAnswer r ON r.board = b" +
            " WHERE b.boardNo = :boardNo")
    Object getBoardByBoardNo(@Param("boardNo") Integer boardNo);
}
