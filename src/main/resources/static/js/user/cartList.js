//js를 통해 리스트와 아작스를 연결
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    cartListAjax(1); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
});

function cartListAjax(page) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#cartListForm") //cartListAjax.html 소스를 붙일 파일 위치 지정
    const f = document.getElementById("form1"); //cartList.html의 form1과 ㅇ녀결
    f.page.value = page;


    $.ajax({
        url: "/user/cartListAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,
        //성공 시에 cartList.html의 cartListForm태그 위치에 백엔드 경로(cartListAjax)에 연결 된 리턴 값인 프론트 cartListAjax.html을 넣어서 보여줌
        success: function (data) {
            $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })

}

//장바구니 결제
function payment() {
    $.ajax({
        url: "/user/payment", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        async: false,
        success: function (data) {   //성공시에 pc창에 카카오페이지 창 띄우기
            location.href = data.next_redirect_pc_url;
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
        }
    })

}


//장바구니 옵션 수정 기능 
function updateCart() { /*cartList.html에 form태그와 연결*/
    const f = document.getElementById("form1"); //cartList.html의 form1과 ㅇ녀결
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    f.roomNo.value = no;
    f.submit();

}

//장바구니 단건 삭제 기능
function deleteCart() {
    const innerHtml = $("#cartListForm") //cartListAjax.html 소스를 붙일 파일 위치 지정
    const orderNo = document.getElementById('orderNo').value; //id가 orderNo인 실제 값을 가져옴

    console.log(orderNo)
    $.ajax({
        url: "/user/deleteCart/"+ orderNo, //백엔드 경로
        type: 'POST',
        cache: false,
        dataType: "html",
        async: false,
        success: function (data) {
             $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
             $(innerHtml).html("")
        }
    })

}

//장바구니 전체 삭제 기능
function deleteAllCart() {
    const innerHtml = $("#cartListForm") //cartListAjax.html 소스를 붙일 파일 위치 지정
    const userNo = document.getElementById('userNo').value; //id가 userNo인 실제 값을 가져옴

    $.ajax({
        url: "/user/deleteAllCart/"+ userNo, //백엔드 경로
        type: 'POST',
        cache: false,
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })

}

//가격 계산
function updateTotalPrice() {
    const f = document.getElementById("form1"); //cartList.html의 form1과 ㅇ녀결
    const select_value = document.getElementById('sizeType'); // 다큐먼트(즉 html 문서)안에 id 설정 해놓은 것 중에 sizeType 이거와 맞는 소스를 가져옴!
    const val1 = select_value.options[select_value.selectedIndex].value; // 옵션 중에서 셀렉트 된 것만 가져온다. 여기선 cartListAjax의 roomNo
    const val2 = val1.split(",");
    const no = val2[0];
    const price = val2[1];
    f.roomNo.value = Number(no);
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



