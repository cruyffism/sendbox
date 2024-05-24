//js를 통해 리스트와 아작스를 연결하는 역할을 한다

//유저 리스트 조회
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    userListAjax(1); // 들어가서 바로 1페이지가 보임, 아래 function의 이름
});

function userListAjax(page) {  // 위에서 보낸 매개변수 1을 받아줌
    const innerHtml = $("#userListForm") //userListAjax.html 소스를 붙일 파일 위치 지정
    const f = document.getElementById("form1"); //userList.html의 form1과 연결
    f.page.value = page;

    $.ajax({ // $부분이 jquery
        url: "/admin/userListAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(), // 이걸 통해 input 애들 값을 백엔드 경로로 보내줌 그러면 백엔드가 받는다.
        dataType: "html",
        async: false,
        //성공 시에 userList.html의 userListForm태그 위치에 백엔드 경로(userListAjax)에 연결 된 리턴 값인 프론트 userListAjax.html을 넣어서 보여줌
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


//유저 단건 삭제 기능
function deleteUser() {
    const innerHtml = $("#userListForm") //userListAjax.html 소스를 붙일 파일 위치 지정
    const userNo = document.getElementById('userNo').value; //id가 userNo인 실제 값을 가져옴

    console.log(userNo)
    $.ajax({
        url: "/admin/deleteUser/" + userNo, //백엔드 경로
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