package com.zerock.sendbox.controller.admin;

import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.service.admin.AdminStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminStoreController {
    @Autowired
    AdminStoreService adminStoreService;
    

    //빈곽 스토어 리스트 조회
    @GetMapping("/storeList")
    public String storeList() {
        return "admin/member/storeList";
    }

    // admin의 모든 업체 리스트 조회
    @GetMapping("/storeListAjax")
    public String storeListAjax(Model model, @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Store> storeList = adminStoreService.findStore();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), storeList.size());

        List<Store> pageContent = storeList.subList(start, end);
        Page<Store> store = new PageImpl<>(pageContent, pageable, storeList.size());

        model.addAttribute("storeList", store);
        return "admin/member/storeListAjax";
    }
}
