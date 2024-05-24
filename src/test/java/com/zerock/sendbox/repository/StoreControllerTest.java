package com.zerock.sendbox.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void searchListTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/store/searchList")
                        .param("storeName", "검색어"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // 검색 결과가 포함되어 있는지 확인하는 로직을 추가할 수 있음
        // 예: .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("예상 결과")));

    }
}
