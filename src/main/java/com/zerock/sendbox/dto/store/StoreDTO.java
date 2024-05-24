package com.zerock.sendbox.dto.store;

import com.zerock.sendbox.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreDTO {
    private String storeName;
    private String thumbnail;
    private String address;
    private String phone;
    private String info_photo;
    private String notice;

    public static StoreDTO fromEntity(Store store) {
        return StoreDTO.builder()
                .storeName(store.getStoreName())
                .thumbnail(store.getThumbnail())
                .address(store.getAddress())
                .phone(store.getPhone())
                .info_photo(store.getInfoPhoto())
                .notice(store.getNotice())
                .build();
    }

}
