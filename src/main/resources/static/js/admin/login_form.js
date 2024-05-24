

function adminLoginForm() {
    console.log("loginForm() 호출");

    let form = document.getElementById("login_form");

    if(form.adminId.value === "") {
        alert("아이디를 입력하세요.");
        form.adminId.focus();
        return false;
    } else if(form.password.value === "") {
        alert("비밀번호를 입력하세요.");
        form.password.focus();
        return false;
    } else {
        form.submit();
    }
}