$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    document.getElementById('startDate').value = new Date().toISOString().substring(0, 10);
    document.getElementById('endDate').value = new Date().toISOString().substring(0, 10);
    document.getElementById('totalAmount').value = 1;


    const f = document.getElementById("order_form"); //cartList.html의 form1과 ㅇ녀결
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    const price = val2[1];
    f.roomNo.value = no;
    f.onePrice.value = price;
    document.getElementById('totalPrice').value = document.getElementById('onePrice').value;

});

//장바구니 추가 기능 및 옵션 미선택 시 알럿창
function addCart() { /*cartList.html에 form태그와 연결*/
    const f = document.getElementById("order_form"); //cartList.html의 form1과 ㅇ녀결
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    f.roomNo.value = no;

    if (f.startDate.value === "") {
        alert("시작 날짜를 선택해주세요.");
        f.startDate.focus();
    } else if (f.endDate.value === "") {
        alert("종료 날짜를 선택해주세요.");
        f.endDate.focus();
    } else if (f.totalAmount.value === "") {
        alert("개수를 선택해주세요.");
        f.totalAmount.focus();
    } else {
        // alert("로그인이 필요합니다.");
        f.submit();
    }

}

//결제하기
function paymentGo() {
    const j = document.getElementById("order_form"); //cartList.html의 form1과 ㅇ녀결
    if (j.startDate.value === "") {
        alert("시작 날짜를 선택해주세요.");
        j.startDate.focus();
    } else if (j.endDate.value === "") {
        alert("종료 날짜를 선택해주세요.");
        j.endDate.focus();
    } else if (j.totalAmount.value === "") {
        alert("개수를 선택해주세요.");
        j.totalAmount.focus();
    } else {
        // alert("로그인이 필요합니다.");
        fetch('http://localhost:8080/user/payment', {
            method: 'GET',
            cache: 'no-cache',
        }).then((response) => {
            if (response.url === 'http://localhost:8080/user/login') {  // 로그인 필요 시 url에서 로그인 페이지로 이동 
                location.href = response.url;
            } else {                                                    // 그렇지 않으면 결제를 처리하는 함수인 paymentAjax()를 호출
                paymentAjax();
            }
        }).then((data) => {
            console.log(data);
        });

    }

}

function paymentAjax() {
    const f = document.getElementById("order_form");
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    f.roomNo.value = no;

    $.ajax({
        url: "/user/payment", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#order_form').serialize(),
        async: false,
        success: function (data) {   //성공시에 pc창에 카카오페이지 창 띄우기
            location.href = data.next_redirect_pc_url;
            setTimeout(function () {
            }, 1000)
        },
        error: function (jqXHR, error) { // 요청이 실패했을 때의 콜백 함수
            console.log("status : ", jqXHR.status)
        }
    })

}


//가격 계산
function updateTotalPrice() {
    const f = document.getElementById("order_form"); //cartList.html의 form1과 ㅇ녀결
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    const price = val2[1];
    f.roomNo.value = no;
    f.onePrice.value = price;

    // 단가가격
    const onePrice = document.getElementById('onePrice').value;
    // 수량
    const amount = document.getElementById('totalAmount').value;
    // 날짜차이 계산
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const da1 = new Date(startDate);
    const da2 = new Date(endDate);
    const dif = da2 - da1;
    const diffDay = dif/ (24 * 60 * 60 * 1000) +1;


    // 보여주는 값
    document.getElementById('totalPrice').value = onePrice * amount * diffDay;
}