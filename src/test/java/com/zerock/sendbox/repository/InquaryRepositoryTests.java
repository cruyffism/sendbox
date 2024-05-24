package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.Inquary;
import com.zerock.sendbox.entity.UserMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class InquaryRepositoryTests {

    @Autowired
    private  InquaryRepository inquaryRepository;

    @Test
    public void insertInquary() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            UserMember userMember = UserMember.builder().mail("user" + i + "@aaa.com").build();

            Inquary inquary = Inquary.builder()
                    .inquaryNo(i)
//                    .userNo(i)
                    .title("Title...." + i)
                    .content("Content...." + i)
//                    .member(userMember)
                    .build();

            inquaryRepository.save(inquary);
        });
    }
}
