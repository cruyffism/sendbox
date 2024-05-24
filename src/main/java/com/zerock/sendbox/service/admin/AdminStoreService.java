package com.zerock.sendbox.service.admin;

import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStoreService {
    @Autowired
    StoreRepository storeRepository;

    // admin의 모든 업체 리스트 조회
    public List<Store> findStore() {
        return storeRepository.findStore();
    }
}
