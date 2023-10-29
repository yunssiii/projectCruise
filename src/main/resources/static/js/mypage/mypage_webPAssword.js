
var reg= /^[0-9]{6}$/;

var webPwdInput = document.getElementsByClassName("webPWd-input");
var webChkPwdInput = document.getElementsByClassName("webPWd-chk-input");
var insertWebBtn = document.getElementsByClassName("webPwdBtn");

// ! 등록 & 변경 !
// -- 비밀번호 정규화 검사 js ----------------------------------
function webPwd(event) {

    var text = "비밀번호를 정확히 입력해주세요.";
    var inputValue = event.target.value; //addEventListener 요소의 값

    if (reg.test(inputValue)) {

        document.getElementById("resultWebPwd").innerText = "";

        webPwdInput[0].style.border = "1px solid #8b8b8b";
        webPwdInput[0].style.outline = "1px solid #8b8b8b";

    }else { 

        document.getElementById("resultWebPwd").innerText = text;

        webPwdInput[0].style.border = "1px solid red";
        webPwdInput[0].style.outline = "1px solid red";

    }
}

webPwdInput[0].addEventListener("change",webPwd);

// -- 비밀번호 확인 정규화 검사 js ----------------------------------
function chkWebPwd(event) {

    var text = "비밀번호를 정확히 입력해주세요.";
    var inputValue = event.target.value; //addEventListener 요소의 값

    if (inputValue === webPwdInput[0].value) {

        document.getElementById("resultWebPwd-chk").innerText = "";

        webChkPwdInput[0].style.border = "1px solid #8b8b8b";
        webChkPwdInput[0].style.outline = "1px solid #8b8b8b";

    }else { 

        document.getElementById("resultWebPwd-chk").innerText = text;

        webChkPwdInput[0].style.border = "1px solid red";
        webChkPwdInput[0].style.outline = "1px solid red";

    }
}

webChkPwdInput[0].addEventListener("change",chkWebPwd);

// -- "비밀번호 등록" 버튼 누를 때 js
insertWebBtn[0].onclick = function() {

    if(webPwdInput[0].value === webChkPwdInput[0].value && reg.test(webPwdInput[0].value)){

        alert("정상적으로 처리되었습니다.");
        updateSubmit();

    }else {
        alert("비밀번호를 정확히 입력해주세요.")
    }
}

// -- 비밀번호 등록 시 서버로 보내는 데이터 -------
function updateSubmit() {

    var PwdUpdateForm = document.updatePwdForm;

    PwdUpdateForm.submit();

}
