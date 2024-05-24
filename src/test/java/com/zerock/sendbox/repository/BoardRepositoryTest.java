package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.AdminMember;
import com.zerock.sendbox.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AdminMemberRepository adminMemberRepository;

    @Test
    public void insertBoard() {

        IntStream.range(0, 10).forEach(i -> {
            AdminMember adminMember = AdminMember.builder().name("admin" + i).build();
            adminMemberRepository.save(adminMember);

            Board board = Board.builder()
                    .title("title" + i)
                    .content("Content.." + i)
                    .boardNo(i)
                    .boardType("1")
                    .thumbnail("tumbnail" + i)
                    .build();
            boardRepository.save(board);
        });

    }
}
