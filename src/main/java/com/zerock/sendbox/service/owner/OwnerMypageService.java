package com.zerock.sendbox.service.owner;

import com.zerock.sendbox.entity.Orders;
import com.zerock.sendbox.entity.OwnerMember;
import com.zerock.sendbox.entity.Room;
import com.zerock.sendbox.entity.Store;
import com.zerock.sendbox.repository.OrdersRepository;
import com.zerock.sendbox.repository.OwnerMemberRepository;
import com.zerock.sendbox.repository.RoomRepository;
import com.zerock.sendbox.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class OwnerMypageService {
    @Autowired
    OwnerMemberRepository ownerMemberRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    RoomRepository roomRepository;


    //오너 정보 수정폼
    public OwnerMember findByOwnerId(String ownerId) {
        return ownerMemberRepository.findByOwnerId(ownerId);
    }

    //오너 정보 수정
    public OwnerMember updateInfo(OwnerMember ownerMember) {
        return ownerMemberRepository.save(ownerMember);
    }

    //오너 정보 조회

    public OwnerMember findByOwnerNo(Integer ownerNo) {
        return ownerMemberRepository.findByOwnerNo(ownerNo);
    }

    //오너 정보 탈퇴
    @Transactional // 탈퇴 성공 시 commit 실패 시 rollback으로 바로 저장하게 유도
    public Integer deleteInfo(Integer ownerNo) {
        return ownerMemberRepository.deleteInfo(ownerNo);
    }

    //사업자의 예약 내역 리스트 조회
    public List<Orders> findAllUserReservation(Integer ownerNo) {
        return ordersRepository.findAllUserReservation(ownerNo);
    }

    //매장 정보 수정폼 조회
    public Store findByInfoOwnerNo(Integer ownerNo) {
        return storeRepository.findByInfoOwnerNo(ownerNo);
    }

    //매장 정보 수정파트 >> 기존 정보 조회
    public Store findByStoreNo(Integer storeNo) {
        return storeRepository.findByStoreNo(storeNo);
    }

    //매장 정보 수정파트 >> 새로운 정보로 수정
    public Store save(Store storeUpdateInfo) {
        return storeRepository.save(storeUpdateInfo);
    }

    // roomList 저장
    public List<Room> saveAll(List<Room> roomList) { // 리스트 저장은 saveAll
        return roomRepository.saveAll(roomList);
    }

    public String uploadFile(MultipartFile file){
        boolean result = false;

        //파일저장
        String fileOriName = file.getOriginalFilename(); //관리자가 업로드한 원본 파일의 이름을 가져옴
        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length()); //관리자가 업로드한 원본 파일의 확장자를 가져옴
        String uploadDir = "C:\\upload\\"; //서버에서 파일이 저장되는 위치

        UUID uuid = UUID.randomUUID();
        String uniqueName = uuid.toString().replace("-", ""); // 하이픈 제거

        File Folder = new File(uploadDir);

        // 해당 디렉토리가 없다면 디렉토리를 생성.
        if (!Folder.exists()) {
            Folder.mkdir();
        }

        File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);

        if(!saveFile.exists()){
            saveFile.mkdirs();
        }

        try {
            file.transferTo(saveFile);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result) {
            System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!");
            return uniqueName + fileExtension;

        } else {
            System.out.println("[UploadFileService] FILE UPLOAD FAIL!");
            return null;
        }

    }

    public OwnerMember ownerSave(OwnerMember byOwnerNo) {
        return ownerMemberRepository.save(byOwnerNo);
    }
}
