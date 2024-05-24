

function ownerLoginForm() {
    console.log("loginForm() 호출");

    let form = document.getElementById("login_form");

    if (form.ownerId.value === "") {
        alert("아이디를 입력하세요.");
        form.ownerId.focus();
        return false;
    } else if (form.password.value === "") {
        alert("비밀번호를 입력하세요.");
        form.password.focus();
        return false;
    }
}