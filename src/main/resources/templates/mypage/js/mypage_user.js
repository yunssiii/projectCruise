
var resultDivs = document.getElementsByClassName("resultDiv-pwd");
var pwdInput = document.getElementsByClassName("password");

var text = "비밀번호를 정확히 입력해주세요.";
var text2 = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.";
var text3 = "기존 비밀번호로 변경할 수 없습니다.";

var pwdAuthBtn = document.getElementsByClassName("passwordBtn");
var newPwds = document.getElementsByClassName("user-cover4");

var newPwdInput = document.getElementsByClassName("newPwd");
var chkNewPwdInput = document.getElementsByClassName("chk-newPwd");

var updateBtns = document.getElementsByClassName("updateBtn");

// 8~16자 사이의 대/소문자, 숫자, 특수문자 필수 포함 정규식
var reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

//-- 비밀번호 빨간 안내 js ------------------------------------

function chkUserPwd(event) {

    var inputValue = event.target.value; //addEventListener 요소의 값

    if (reg.test(inputValue)) {

        document.getElementById("resultUserPwd").innerText = "";

        pwdInput[0].style.border = "1px solid #8b8b8b";
        pwdInput[0].style.outline = "1px solid #8b8b8b";

    }else { 

        document.getElementById("resultUserPwd").innerText = text;

        pwdInput[0].style.border = "1px solid red";
        pwdInput[0].style.outline = "1px solid red";

    }
}

pwdInput[0].addEventListener("change",chkUserPwd);

// -- "확인" 버튼 -> 비밀번호 인증 ------------------------------
//입력된 비밀번호 복호화

function pwdAuth (event) {

    if(pwdInput[0].value == "dbsgk12!"){
        alert("인증성공");

        document.getElementById("resultUserPwd").innerText = "";
        pwdInput[0].style.border = "1px solid #8b8b8b";
        pwdInput[0].style.outline = "none";

        newPwds[0].style.display = "flex";
        newPwdInput[0].focus();
        
    }else {

        document.getElementById("resultUserPwd").innerText = text;

        pwdInput[0].style.border = "1px solid red";
        pwdInput[0].style.outline = "1px solid red";
    }
}

//"확인" 버튼 엔터키 이벤트 발생 js
function enterkey(event) {
	if (event.keyCode == 13) {

    	if(pwdInput[0].value == "dbsgk12!"){
            alert("인증성공");
    
            document.getElementById("resultUserPwd").innerText = "";
            pwdInput[0].style.border = "1px solid #8b8b8b";
            pwdInput[0].style.outline = "none";
    
            newPwds[0].style.display = "flex";
            newPwdInput[0].focus();
            
        }else {
            document.getElementById("resultUserPwd").innerText = text;

            pwdInput[0].style.border = "1px solid red";
            pwdInput[0].style.outline = "1px solid red";
        }
    }
}

pwdAuthBtn[0].addEventListener("click",pwdAuth);

// -- 새 비밀번호 빨간 안내 js ----------------------------------

function NewPwd(event) {
    
    var inputValue = event.target.value; //addEventListener 요소의 값

    if (reg.test(inputValue) && inputValue !== pwdInput[0].value) {

        document.getElementById("resultNewPwd").innerText = "";

        newPwdInput[0].style.border = "1px solid #8b8b8b";
        newPwdInput[0].style.outline = "1px solid #8b8b8b";

    }else if (!reg.test(inputValue)){ 

        document.getElementById("resultNewPwd").innerText = text2;

        newPwdInput[0].style.border = "1px solid red";
        newPwdInput[0].style.outline = "1px solid red";

        newPwdInput[0].value = "";

    }else if (inputValue === pwdInput[0].value) {
        
        document.getElementById("resultNewPwd").innerText = text3;
        
        newPwdInput[0].style.border = "1px solid red";
        newPwdInput[0].style.outline = "1px solid red";

        newPwdInput[0].value = "";
    }
}

newPwdInput[0].addEventListener("change",NewPwd);

// -- 새 비밀번호 확인 빨간 안내 js ----------------------------------

function chkNewPwd(event) {

    var text = "비밀번호를 정확히 입력해주세요.";
    var inputValue = event.target.value; //addEventListener 요소의 값

    if (inputValue === newPwdInput[0].value) {

        document.getElementById("resultNewPwd-chk").innerText = "";

        chkNewPwdInput[0].style.border = "1px solid #8b8b8b";
        chkNewPwdInput[0].style.outline = "1px solid #8b8b8b";

    }else { 

        document.getElementById("resultNewPwd-chk").innerText = text;

        chkNewPwdInput[0].style.border = "1px solid red";
        chkNewPwdInput[0].style.outline = "1px solid red";

    }
}

chkNewPwdInput[0].addEventListener("change",chkNewPwd);

// -- 회원정보 수정 버튼 관련 js -------------------------------------- 

//모달창 클릭, 모달창
var updateBtns = document.getElementsByClassName("updateBtn");
var userUpdateDiv = document.getElementsByClassName("userUpdatemodal-div");

var yesBtns = document.getElementsByClassName("yesBtn");
var noBtns = document.getElementsByClassName("noBtn");

// 수정버튼 누를 때

updateBtns[0].onclick = function() {

    if(window.getComputedStyle(newPwds[0]).display === "flex") {
        // alert("flex임! - 수정불가");

        if(reg.test(newPwdInput[0].value) && newPwdInput[0].value !== pwdInput[0].value && chkNewPwdInput[0].value === newPwdInput[0].value) {
            
            userUpdateDiv[0].style.display = "block";
            document.body.style.overflow = "hidden";

            pwdInput[0].value = "";
        }else {
            alert("새 비밀번호를 입력하세요.");
            userUpdateDiv[0].style.display = "none";
            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";

            newPwdInput[0].value = "";
            chkNewPwdInput[0].value = "";
            
        }
    }else {

        userUpdateDiv[0].style.display = "block";
        document.body.style.overflow = "hidden";

        pwdInput[0].value = "";
    }   
    
}

//수정 모달 예/아니오

yesBtns[0].onclick = function() {
    // "예" 버튼 누를 때
    // db에 전체 update
    userUpdateDiv[0].style.display = "none";
    newPwds[0].style.display = "none";
    document.body.style.overflow = "auto";
    document.body.style.overflowX = "hidden";
    
}

noBtns[0].onclick = function() {
    userUpdateDiv[0].style.display = "none";
    newPwds[0].style.display = "none";
    document.body.style.overflow = "auto";
    document.body.style.overflowX = "hidden";
}

//"수정" 버튼 엔터키 이벤트 발생 js

function updateEnterkey(event) {
	if (event.keyCode == 13) {

    	if(window.getComputedStyle(newPwds[0]).display === "flex") {
    
            if(reg.test(newPwdInput[0].value) && newPwdInput[0].value !== pwdInput[0].value && chkNewPwdInput[0].value === newPwdInput[0].value) {//수정됨
                
                userUpdateDiv[0].style.display = "block";
                document.body.style.overflow = "hidden";

                pwdInput[0].value = "";
            }else {//수정안됨
                alert("새 비밀번호를 입력하세요.");
                userUpdateDiv[0].style.display = "none";
                document.body.style.overflow = "auto";
                document.body.style.overflowX = "hidden";

                newPwdInput[0].value = "";
                chkNewPwdInput[0].value = "";
                
            }
        }else {//수정됨
    
            userUpdateDiv[0].style.display = "block";
            document.body.style.overflow = "hidden";

            pwdInput[0].value = "";
        } 
    }
}




