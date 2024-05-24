
function modifyAccount() {
    console.log("modifyAccount()");

    /* modify_account_form.html form 태그 아이디를 가져옴 */
    let form = document.getElementById("modify_account_form");

    if (form.password.value === "") {
        alert("비밀번호를 입력해주세요.");
        form.password.focus();
        return false;
    } else if (form.password_again.value === "") {
        alert("비밀번호를 다시 입력해주세요.");
        form.password_again.focus();
        return false;
    } else if (form.password.value !== form.password_again.value) {
        alert("비밀번호가 일치하지 않습니다.");
        form.password_again.focus();
        return false;
    } else if (form.name.value === "") {
        alert("이름을 입력해주세요.");
        form.name.focus();
        return false;
    } else if (form.mail.value === "") {
        alert("이메일을 입력해주세요.");
        form.mail.focus();
        return false;
    } else if (form.phone.value === "") {
        alert("전화번호를 입력해주세요.");
        form.phone.focus();
        return false;
    } else {
        form.submit();
    }
}