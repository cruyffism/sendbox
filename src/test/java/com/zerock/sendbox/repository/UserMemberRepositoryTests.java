package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.UserMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class UserMemberRepositoryTests {

    @Autowired
    private UserMemberRepository userMemberRepository;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            UserMember userMember = UserMember.builder()
                    .userNo(i)
                    .userId("user"+ i)
                    .gender("1")
                    .phone("01011112222")
                    .mail("user" + i + "@aaa.com")
                    .password("1111")
                    .name("USER" + i)
                    .build();
            userMemberRepository.save(userMember);
        });
    }
}
