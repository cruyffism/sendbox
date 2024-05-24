// create_account_form.html의 input이 빈칸일 경우 if문으로 경고창을 띄우는 함수

function registerStore() {

    let form = document.getElementById("registerStoreForm");

    if(form.storeName.value === "") {
        alert("업체명을 입력하세요.");
        form.storeName.focus();
        return false;
    } else if(form.notice.value === "") {
        alert("공지사항을 입력하세요.");
        form.notice.focus();
        return false;
    } else if (form.region.value === "") {
        alert("지역을 입력하세요.");
        form.region.focus();
        return false;
    } else if (form.address.value === "") {
        alert("주소를 입력하세요.");
        form.address.focus();
        return false;
    } else if (form.brn.value === "") {
        alert("사업자번호를 입력하세요.");
        form.brn.focus();
        return false;
    } else if (form.phone.value === "") {
        alert("전화번호를 입력하세요.");
        form.phone.focus();
        return false;
    } else if (form.thumbnail.value === "") {
        alert("대표 사진을 선택하세요.");
        form.thumbnail.focus();
        return false;
    } else if (form.infoPhoto.value === "") {
        alert("상세 사진을 입력하세요.");
        form.infoPhoto.focus();
        return false;
    } else if (form.price1.value === "") {
        alert("가격을 입력하세요.");
        form.price1.focus();
        return false;
    } else if (form.remain1.value === "") {
        alert("수량을 입력하세요.");
        form.remain1.focus();
        return false;
    } else if (form.price2.value === "") {
        alert("가격을 입력하세요.");
        form.price2.focus();
        return false;
    } else if (form.remain2.value === "") {
        alert("수량을 입력하세요.");
        form.remain2.focus();
        return false;
    } else if (form.price3.value === "") {
        alert("가격을 입력하세요.");
        form.price3.focus();
        return false;
    } else if (form.remain3.value === "") {
        alert("수량을 입력하세요.");
        form.remain3.focus();
        return false;
    } else {
        form.submit();
    }

}