package com.zerock.sendbox.repository;

import com.zerock.sendbox.entity.AdminMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class AdminMemberRepositoryTests {

    @Autowired
    private AdminMemberRepository adminMemberRepository;

    @Test
    public void insertAdminMembers() {
        IntStream.rangeClosed(1,100).forEach(i -> {

            AdminMember adminMember = AdminMember.builder()
                    .adminNo(i)
                    .adminId("user" + i + 10)
                    .approval(i + 1)
                    .gender("0")
                    .phone("1111111")
                    .part("abc")
                    .mail("user" + i + "@aaa.com")
                    .password("1111")
                    .name("USER" + i)
                    .build();
            adminMemberRepository.save(adminMember);
        });
    }
}
