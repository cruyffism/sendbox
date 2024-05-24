package com.zerock.sendbox.controller.store;

import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.service.store.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/store")
@Slf4j
public class StoreController {

    @Autowired
    private StoreService storeService;

    //검색 결과
    @GetMapping("/searchList")
    public String searchList(@RequestParam(value = "storeName", required = false, defaultValue="") String storeName,
                             @PageableDefault(size = 20) Pageable pageable, Model model) {

        // 검색어가 비어있던 안비어있던 알아서 조회 됨
        List<Store> storeList = storeService.findAllByKeyword(storeName);
        // 매장 정보와 방 정보를 모두 모델에 저장
        model.addAttribute("storeList", storeList);
        return "store/search_store";
    }



    //상세페이지
    @GetMapping("/detail/{storeNo}")
    public String getStoreDetail(@PathVariable("storeNo") Integer storeNo, Model model) {
        Store store = storeService.getStoreDetail(storeNo);
        model.addAttribute("store", store);
        return "store/store_detail";
    }

    //퀵메뉴
    @GetMapping
    public String getStoresByRegion(@RequestParam("region") String region, Model model) {
        List<Store> storeList = storeService.getStoresByRegion(region);
        model.addAttribute("storeList", storeList);
        return "store/search_store";
    }

}
