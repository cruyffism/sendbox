package com.zerock.sendbox.service.store;

import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;

    // 검색 결과 조회
    public List<Store> findAllByKeyword(String storeName) {

        return storeRepository.findAllByStoreNameContaining(storeName);
    }

    //상세페이지
    public Store getStoreDetail(Integer storeNo) {
        return storeRepository.findByStoreNo(storeNo);
    }
    public List<Store> getStoreThumbnail(String storeName) {
        return storeRepository.findAllByStoreNameContaining(storeName);
    }

    //퀵메뉴
    public List<Store> getStoresByRegion(String region) {
        return storeRepository.findByRegion(region);
    }

}
