// 안써도됨
// let page = 0; // 페이지 번호를 저장하는 변수
//
// function getSearchList() {
//     // 검색어를 가져옴
//     let storeName = document.getElementById("search").value;
//   //콘솔창에 받아온 검색어 출력
//     console.log("검색어 : " + storeName);
//
//     fetch(`/store/searchList?storeName=${storeName}`)
//         .then(response => response.text()) // 응답을 텍스트로 변환
//         .then(data => {
//             let searchResults = document.getElementById("search_result");
//             searchResults.innerHTML = data; // HTML을 그대로 삽입
//
//             data.forEach(store => {
//                 // 각 매장에 대한 정보 반복
//                 let storeItem = document.createElement("div");
//                 storeItem.classList.add("store_item");
//
//                 // 상세 페이지로 넘어가는 링크
//                 let detailLink = document.createElement("a");
//                 detailLink.classList.add("goto_detail");
//                 detailLink.href = "/store/detail?id=" + store.storeName; //상세 페이지 URL을 지정
//                 // detailLink.textContent = "상세 보기";
//                 //썸네일을 링크 영역에 추가
//                 detailLink.appendChild(thumbnailDiv);
//
//                 //썸네일 생성
//                 let thumbnailDiv = document.createElement("div");
//                 thumbnailDiv.classList.add("search_thumbnail");
//                 let thumbnailImg = document.createElement("img");
//                 thumbnailImg.src = store.thumbnail;
//                 thumbnailImg.alt = "썸네일";
//                 thumbnailDiv.appendChild(thumbnailImg);
//
//                 //매장정보 생성
//                 let infoDiv = document.createElement("div");
//                 infoDiv.classList.add("search_info");
//                 let infoNameDiv = document.createElement("div");
//                 infoNameDiv.classList.add("search_infoName");
//                 let nameParagraph = document.createElement("p");
//                 nameParagraph.textContent = store.storeName;
//                 infoNameDiv.appendChild(nameParagraph);
//                 infoDiv.appendChild(infoNameDiv);
//
//                 // 각 매장에 대한 price값
//                 store.rooms.forEach(room => {
//                     let roomInfoDiv = document.createElement("div");
//                     roomInfoDiv.classList.add("room_info");
//                     let roomPriceParagraph = document.createElement("p");
//                     roomPriceParagraph.textContent = room.price;
//                     roomInfoDiv.appendChild(roomPriceParagraph);
//                     infoDiv.appendChild(roomInfoDiv);
//                 });
//
//                 //썸네일과 매장정보를 storeItem에 추가
//                 storeItem.appendChild(thumbnailDiv);
//                 storeItem.appendChild(infoDiv);
//
//                 //검색 결과 목록에 storeItem 추가
//                 searchResults.appendChild(storeItem);
//             });
//             // 페이지 번호 증가
//             page++;
//         })
//         .catch(error => console.error('Error:', error)); // 오류 처리
// }
//
