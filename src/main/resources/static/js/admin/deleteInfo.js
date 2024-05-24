//회원 탈퇴


function deleteInfo() {
    let form = document.getElementById('modify_account_form');
    let form2 = document.getElementById("dropAccountForm");
    form2.password.value = form.password.value;
    if (form.password.value === "") {
        alert("비밀번호를 입력해주세요.");
        form.password.focus();
    } else if (form.passwordAgain.value === "") {
        alert("비밀번호를 다시 입력해주세요.");
        form.passwordAgain.focus();
    } else if (form.password.value !== form.passwordAgain.value) {
        alert("비밀번호가 일치하지 않습니다.");
        form.passwordAgain.focus();
    } else {
        var confirmResult = confirm("정말로 회원 탈퇴하시겠습니까?");
        if (confirmResult) {
            // 확인을 선택한 경우, 회원 탈퇴를 위한 form2를 자동으로 제출
            form2.submit();
        }
    }
}

// function deleteInfo() {
//     // 확인 창을 표시
//     var confirmation = confirm("정말로 회원 탈퇴하시겠습니까?");
//
//     // 사용자가 확인을 눌렀을 때만 회원 탈퇴 요청을 보냄
//     if (confirmation) {
//         // AJAX를 사용하여 회원 탈퇴 요청을 보냄
//         var xhr = new XMLHttpRequest();
//         xhr.open("GET", "/deleteInfo", true);
//         xhr.onreadystatechange = function() {
//             if (xhr.readyState === 4) {
//                 if (xhr.status === 200) {
//                     // 요청이 성공적으로 처리되었을 때
//                     var result = xhr.responseText;
//                     if (result === "1") {
//                         // 회원 탈퇴가 성공했을 경우
//                         alert("회원 탈퇴가 완료되었습니다.");
//                         window.location.href = "/user/home"; // 홈 페이지로 이동
//                     } else {
//                         // 회원 탈퇴가 실패했을 경우
//                         alert("회원 탈퇴에 실패했습니다.");
//                     }
//                 } else {
//                     // 요청이 실패했을 때
//                     alert("요청을 처리하는 중 오류가 발생했습니다.");
//                 }
//             }
//         };
//         xhr.send();
//     } else {
//         // 사용자가 취소를 눌렀을 때
//         alert("회원 탈퇴가 취소되었습니다.");
//     }
// }
