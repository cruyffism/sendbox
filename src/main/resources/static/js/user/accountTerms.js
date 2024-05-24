function showTerm(term) {
    let termPopup = document.getElementById('popup' + term);
    termPopup.style.display = 'block';
}

function closePopup(popupId) {
    // 팝업 창 닫기
    let popup = document.getElementById(popupId);
    popup.style.display = 'none';
}

function toggleAllTerms() {
    // 전체 동의 체크박스가 체크되었을 때 모든 약관에 대해 동의 처리
    let allAgreeCheckbox = document.querySelector('.all-agree');
    let termCheckboxes = document.querySelectorAll('.term-agree');

    termCheckboxes.forEach(function(checkbox) {
        checkbox.checked = allAgreeCheckbox.checked;
    });
}

function proceedIfAllAgreed() {
    // 모든 약관에 동의했는지 확인
    let termCheckboxes = document.querySelectorAll('.term-agree');
    let allTermsAgreed = true;

    termCheckboxes.forEach(function(checkbox) {
        if (!checkbox.checked) {
            allTermsAgreed = false;
        }
    });

    if (allTermsAgreed) {
        window.location.href = "/user/create_account_form";
    } else {
        alert("모든 약관에 동의해야 가입이 가능합니다.");
    }
}