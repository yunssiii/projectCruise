
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
var reg = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

//-- 비밀번호 빨간 안내 js ------------------------------------

function chkUserPwd(event) {

    var inputValue = event.target.value; //addEventListener 요소의 값

    if (reg.test(inputValue) && inputValue != null) {

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


// -- 새 비밀번호 빨간 안내 js ----------------------------------

function NewPwd(event) {
    
    var inputValue = event.target.value; //addEventListener 요소의 값

    //정규식에 맞고, 이전 비밀번호와 다를 때
    if (reg.test(inputValue) && inputValue !== pwdInput[0].value) {

        document.getElementById("resultNewPwd").innerText = "";

        newPwdInput[0].style.border = "1px solid #8b8b8b";
        newPwdInput[0].style.outline = "1px solid #8b8b8b";

    }
    else if (!reg.test(inputValue)){ //정규식에 맞지 않을 때

        document.getElementById("resultNewPwd").innerText = text2;

        newPwdInput[0].style.border = "1px solid red";
        newPwdInput[0].style.outline = "1px solid red";

        newPwdInput[0].value = "";

    }else if (inputValue === pwdInput[0].value) { //이전 비밀번호와 같을 때

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

    //형식에 맞으면서 새비밀번호와 동일할 때
    if (reg.test(inputValue) && inputValue == newPwdInput[0].value) {

        document.getElementById("resultNewPwd").innerText = "";
        document.getElementById("resultNewPwd-chk").innerText = "";

        chkNewPwdInput[0].style.border = "1px solid #8b8b8b";
        chkNewPwdInput[0].style.outline = "1px solid #8b8b8b";

    }
    else {
        document.getElementById("resultNewPwd-chk").innerText = text;

        chkNewPwdInput[0].style.border = "1px solid red";
        chkNewPwdInput[0].style.outline = "1px solid red";

    }
}

chkNewPwdInput[0].addEventListener("change",chkNewPwd);

// -- 비밀번호 변경하겠습니까? 버튼 누를 때 모달 열기
function changePwd(){

    $('#userPwdmodalId').removeClass('hidden');
    $('#userPwdmodalId').addClass('visible');

}

// -- '비밀번호 변경' 버튼 누를 때 모달 닫기
function updatePwdBtn(){

    //새 비밀번호와 확인 비밀번호가 같지 않으면 모달 유지
    if($('.newPwd').val() !== $('.chk-newPwd').val){

        $('#userPwdmodalId').removeClass('hidden');
        $('#userPwdmodalId').addClass('visible');

    }else {

        $('#userPwdmodalId').addClass('hidden');
        $('#userPwdmodalId').removeClass('visible');

    }
}

// x 버튼 누를 때 모달 닫기
function xcloseModal() {

    $('#userPwdmodalId').addClass('hidden');
    $('#userPwdmodalId').removeClass('visible');

    $('.password').val(''); //닫을 때 값 지우기
    $('.newPwd').val('');
    $('.chk-newPwd').val('');

    $('.user-cover4').css('display','none'); //새 비밀번호 창 닫기

    document.getElementById("resultUserPwd").innerText = ""; //경고 지우기
    pwdInput[0].style.border = "1px solid #8b8b8b"; //테두리 색 변화
    pwdInput[0].style.outline = "1px solid #8b8b8b";

}

// -- 수정 form검사 함수 ----------------------------------------
function updateSubmit() {

    updateForm = document.updateForm;
    tel = updateForm.tel.value;
    address = updateForm.address.value;
    detailAddress = updateForm.detailAddress.value;

    if(!tel){
        alert('전화번호를 입력하세요.');
        updateForm.tel.focus();
        return;
    }

    if(!address){
        alert('주소를 입력하세요.');
        return;
    }

    if(!detailAddress){
        alert('상세주소를 입력하세요.');
        updateForm.detailAddress.focus();
        return;
    }

    updateForm.action = '/mypage/mypage_myInfo';
    updateForm.submit();



}

// 새비밀번호 reg체크, 빈칸인지 아닌지 확인
function newChkPwdForm() {

    newChkForm = document.newChkForm;
    newPwd = newChkForm.newPwd.value;
    chkNewPwd = newChkForm.chkNewPwd.value;

    //새 비밀번호 값이 없고, 형식에 맞지 않을 때
    if(!newPwd && !reg.test(newPwd)){

        document.getElementById("resultNewPwd").innerText = text2; //안내

        newPwdInput[0].style.border = "1px solid red"; //색변화
        newPwdInput[0].style.outline = "1px solid red";

        newPwdInput[0].value = "";//값 지우기
        newChkForm.newPwd.focus();//포커스

        //alert("새 비밀번호 정확히..!");
        return;
    }

    //새 비밀번호와 이전 비밀번호가 같을 때
    if(newPwd === pwdInput[0].value){
        document.getElementById("resultNewPwd").innerText = text3; //안내

        newPwdInput[0].style.border = "1px solid red"; //빨간
        newPwdInput[0].style.outline = "1px solid red";

        newPwdInput[0].value = ""; //값 지우기
        newChkForm.newPwd.focus();//포커스

        //alert("새 비밀번호랑 이전이랑 같다.!");
        return;
    }

    //새 비밀번호 확인값이 없고, 형식에 맞지 않을 때
    if(!chkNewPwd && !reg.test(chkNewPwd)){

        //alert("새 비밀번호 확인 정확히..!");
        document.getElementById("resultNewPwd-chk").innerText = text;

        chkNewPwdInput[0].style.border = "1px solid red";
        chkNewPwdInput[0].style.outline = "1px solid red";
        newChkForm.chkNewPwd.focus();

        return;
    }

    //새 비밀번호와 확인 값이 다를 떼
    if(newPwd !== chkNewPwd){
        //alert("둘이 다르자나!!");

        document.getElementById("resultNewPwd-chk").innerText = text;

        chkNewPwdInput[0].style.border = "1px solid red";
        chkNewPwdInput[0].style.outline = "1px solid red";
        newChkForm.chkNewPwd.focus();
        return;

    }

    newChkForm.action = '/mypage/mypage_myInfo_pwd';
    newChkForm.submit();
}


//-- 비밀번호 엔터 시 submit 막기
$('input[type="password"]').keydown(function() {
  if (event.keyCode === 13) {
    event.preventDefault();
  };
});


