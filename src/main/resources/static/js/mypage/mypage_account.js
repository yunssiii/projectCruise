
//-- 계좌 등록 ----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

//모달 이동 버튼
var funs = []; //모달 이동 함수를 저장하기 위한 변수

var addDiv = document.getElementsByClassName("add-accountBtn");
var modalDiv1 = document.getElementsByClassName("modal-div1");
var xButton1 = document.getElementsByClassName("closeBtn1");

var nextBtn1 = document.getElementsByClassName("nextBtn1");
var modalDiv2 = document.getElementsByClassName("modal-div2");
var xButton2 = document.getElementsByClassName("closeBtn2");

var nextBtn2 = document.getElementsByClassName("nextBtn2");
var modalDiv3 = document.getElementsByClassName("modal-div3");
var xButton3 = document.getElementsByClassName("closeBtn3");
var addBtn = document.getElementsByClassName("addBtn");

//모달1 약관 동의
var agreeAll = document.getElementById("agree-all");

//모달2 계좌 선택
var bankBtns = document.querySelectorAll(".bankBtn");

//모달3 계좌 유효성 검사, 인증 버튼
var accounNums = document.getElementsByClassName("account-input");
var accounPwds = document.getElementsByClassName("accountPwd-input");
var authBtns = document.getElementsByClassName("account-authBtn");

//-- 모달 이동 함수 ---------------------------------------------
//nextBtn1 -> 전체 동의했는지 확인
//nextBtn2 -> 계좌 선택했는지 확인
function AddModal(num) {

    return function() {

        addDiv[num].onclick = function() {
            modalDiv1[num].style.display = "block";
            document.body.style.overflow = "hidden";
        }

        //-- 모달1 -> 모달2, 전체 동의했는지도 같이 확인------------
        nextBtn1[0].onclick = function() {

            for (var i = 0; i < nextBtn1.length; i++) {

                if (!agreeAll.checked) {
                    modalDiv1[num].style.display = "block";
                    modalDiv2[num].style.display = "none";

                }else {
                    modalDiv1[num].style.display = "none";
                    modalDiv2[num].style.display = "block";
                }
            }
        }

        xButton1[num].onclick = function() {
            modalDiv1[num].style.display = "none";
            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";

            $('#agree-all').prop('checked', false); //동의 지우기
            $("input[name=agreeChk]").prop("checked",false);

            nextBtn1[num].style.backgroundColor = "#bebebe"; //버튼 회색 유지
            nextBtn1[num].style.cursor = "default";
            nextBtn2[num].style.backgroundColor = "#bebebe";
            nextBtn2[num].style.cursor = "default";
            addBtn[num].style.backgroundColor = "#bebebe";
            addBtn[num].style.cursor = "default";

            bankBtns.forEach((e) => { //은행 클릭 지우기
                e.classList.remove("click");
            });
        }

        //-- 모달2 -> 모달3, 계좌 선택했는지도 확인----------
        nextBtn2[0].onclick = function() {

            //모달2에서 bankBtn 클릭 경우
            var bankBtnClick = document.querySelector(".bankBtn.click");

            if(bankBtnClick === null) {//선택 안됨
                modalDiv2[num].style.display = "block";
                modalDiv3[num].style.display = "none";
            }else {//선택됨
                modalDiv2[num].style.display = "none";
                modalDiv3[num].style.display = "block";
            }
        }

        xButton2[num].onclick = function() {
            modalDiv2[num].style.display = "none";
            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";

            $('#agree-all').prop('checked', false); //동의 지우기
            $("input[name=agreeChk]").prop("checked",false);

            nextBtn1[num].style.backgroundColor = "#bebebe"; //버튼 회색 유지
            nextBtn1[num].style.cursor = "default";
            nextBtn2[num].style.backgroundColor = "#bebebe";
            nextBtn2[num].style.cursor = "default";
            addBtn[num].style.backgroundColor = "#bebebe";
            addBtn[num].style.cursor = "default";

            bankBtns.forEach((e) => { //은행 클릭 지우기
                e.classList.remove("click");
            });

        }

        xButton3[num].onclick = function() {
            modalDiv3[num].style.display = "none";
            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";

            $('#agree-all').prop('checked', false);
            $("input[name=agreeChk]").prop("checked",false);

            nextBtn1[num].style.backgroundColor = "#bebebe"; //버튼 회색 유지
            nextBtn1[num].style.cursor = "default";
            nextBtn2[num].style.backgroundColor = "#bebebe";
            nextBtn2[num].style.cursor = "default";
            addBtn[num].style.backgroundColor = "#bebebe";
            addBtn[num].style.cursor = "default";

            bankBtns.forEach((e) => { //은행 클릭 지우기
                e.classList.remove("click");
            });

            accounNums[1].value = ''; //계좌, 비밀번호 값 지우기
            accounPwds[0].value = '';
        }
    };
}

funs[0] = AddModal(0);

funs[0]();

//-- 모달1 약관 숨기기--------------------------------------------

$("#agree-show").click(function() {

    if($("#agree-hide").css('display')==='none'){
        $("#agree-hide").css('display','block');
    }else if($("#agree-hide").css('display')==='block'){
        $("#agree-hide").css('display','none');
    }
});

$("#agree-show2").click(function() {

    if($("#agree-hide2").css('display')==='none'){
        $("#agree-hide2").css('display','block');
    }else if($("#agree-hide2").css('display')==='block'){
        $("#agree-hide2").css('display','none');
    }
});

$("#agree-show3").click(function() {

    if($("#agree-hide3").css('display')==='none'){
        $("#agree-hide3").css('display','block');
    }else if($("#agree-hide3").css('display')==='block'){
        $("#agree-hide3").css('display','none');
    }
});

//-- 모달1 약관 동의 checkbox ------------------------------------------
/*
    전체 체크박스 체크시
        -> "확인" 버튼 색 변함
        -> 개별 체크박스 체크됨
    개별 체크박스 체크시
        -> 전부 다 체크되면 전체, "확인" 버튼 변화
        -> 하나만 체크되면 변화 없음
        -> name.length로 구하기

*/
$(document).ready(function(){

    // 전체 선택
    $("#agree-all").click(function() {

        if($("#agree-all").is(":checked")){
            $("input[name=agreeChk]").prop("checked",true);
            $(".nextBtn1").css("background-color","#0c0ccad0");
            $(".nextBtn1").css("cursor","pointer");
        }else {
            $("input[name=agreeChk]").prop("checked",false);
            $(".nextBtn1").css("background-color","#bebebe");
            $(".nextBtn1").css("cursor","default");
        }
    })

    // 개별 선택
    $("input[name=agreeChk]").click(function() {

        var total = $("input[name=agreeChk]").length;
        var checked = $("input[name=agreeChk]:checked").length;

        if(total == checked){
            $("#agree-all").prop("checked",true);
            $(".nextBtn1").css("background-color","#0c0ccad0");
            $(".nextBtn1").css("cursor","pointer");
        }else {
            $("#agree-all").prop("checked",false);
            $(".nextBtn1").css("background-color","#bebebe");
            $(".nextBtn1").css("cursor","default");
        }
    });

});


//-- 모달2 계좌에 click 클래스를 더하는 함수------------------------------
/*
    클릭한 div만 주황선 css 입히기
    "확인" 버튼 색 변화
    "확인" 버튼 누를 때의 js는 모달 이동 함수에 포함되어 있음
*/

function bankClick(event) {

    bankBtns.forEach((e) => {
        e.classList.remove("click");
    });

    event.target.classList.add("click");
    colorchange();
}

function colorchange() {
    nextBtn2[0].style.backgroundColor = "#0c0ccad0";
    nextBtn2[0].style.cursor = "pointer";

}

bankBtns.forEach((e) => {
    e.addEventListener("click", bankClick);
});


//-- 모달3 계좌번호 검사 함수 -----------------------------------------
/*
    테두리 빨강, 알림문구 띄우기
    숫자만 입력 가능, 14자리
    입력값 14보다 크,작 -> "계좌번호를 정확히 입력해주세요"
    input 박스가 변할 때랑 등록 버튼이 눌릴 때 둘 다 나와야함
*/

var text = "정확히 입력해주세요.";

function chkAccountNum(event) {

    var inputValue = event.target.value; //addEventListener 요소의 값

    if (inputValue.length >= 10 && inputValue.length <= 14 ) {

        document.getElementById("resultNum").innerText = "";

        accounNums[1].style.border = "1px solid black";
        accounNums[1].style.outline = "1px solid black";

    }else if(inputValue.length < 10) {

        document.getElementById("resultNum").innerText = text;

        accounNums[1].style.border = "1px solid red";
        accounNums[1].style.outline = "1px solid red";

    }else if(inputValue.length > 14) {

        document.getElementById("resultNum").innerText = text;

        accounNums[1].style.border = "1px solid red";
        accounNums[1].style.outline = "1px solid red";

    }
}

//-- 계좌 비밀번호 검사 함수 -----------------------------------------
/*
    테두리 빨강, 알림문구 띄우기
    숫자만 입력 가능, 4자리
    입력값 4보다 크,작 -> "계좌번호를 정확히 입력해주세요"
    input 박스가 변할 때랑 등록 버튼이 눌릴 때 둘 다 나와야함
*/

function chkAccountPwd(event) {

    var inputValue = event.target.value;

    if (inputValue.length >= 4 && inputValue.length <= 6) {

        document.getElementById("resultPwd").innerText = "";
        accounPwds[0].style.border = "1px solid black";
        accounPwds[0].style.outline = "1px solid black";

    }else if(inputValue.length < 4) {

        document.getElementById("resultPwd").innerText = text;

        accounPwds[0].style.border = "1px solid red";
        accounPwds[0].style.outline = "1px solid red";

    }else if(inputValue.length > 6) {

        document.getElementById("resultPwd").innerText = text;

        accounPwds[0].style.border = "1px solid red";
        accounPwds[0].style.outline = "1px solid red";
    }
}

accounNums[1].addEventListener("change",chkAccountNum);
accounPwds[0].addEventListener("change",chkAccountPwd);

//-- 계좌번호 인증 함수 --------------------------------------------------
/*
    인증 버튼 누르면 함수 실행
    if(가상계좌 비밀번호=입력된 비밀번호 && 불러와진 이름의 가상계좌번호 = 입력된 계좌번호) ⇒ 인증 성공 / 등록 버튼 색변화/ 창 닫기/ insert
    if(번호!=비밀번호) ⇒인증 실패 / 인증 실패 알림/ 내용 지우고 cursor 계좌번호로
*/

function accountAuth (event) {

    //인증 실패 시 insert도 안되고, 모달도 유지해야 함

    //서버에서 넘긴 비밀번호,계좌번호는 html에서 받음
    var accountPwdsValue =  accounPwds[0].value;
    var accountNumsValue =  accounNums[1].value;

    console.log("인증전-번호"+accountNumsValue);

    for(var i=0;i<myaccountList.length;i++){
        //등록된 계좌와 일치하면 alert
        if(accountNumsValue == myaccountList[i].myaccount_anum){

            alert("이미 등록된 계좌입니다.");

            addBtn[num].style.backgroundColor = "#bebebe"; //버튼 회색
            addBtn[num].style.cursor = "default";
            modalDiv3[0].style.display = "block"; //모달 유지

            accounNums[1].value = ''; //값 지우기
            accounPwds[0].value = '';

        }else {

            for(var i=0;i<openAccPwd.length;i++){

                if(accountPwdsValue == openAccPwd[i].open_password && accountNumsValue != "" && accountNumsValue == openAccPwd[i].open_account){
                    console.log("인증성공");

                    console.log("인증성공-번호"+accounNums[1].value);

                    addBtn[0].style.backgroundColor = "#0c0ccad0"; //버튼 파란색으로 변화
                    addBtn[0].style.cursor = "pointer";

                    accounNums[1].value = accountNumsValue; //값 남기기
                    accounPwds[0].value = accountPwdsValue;

                    addBtn[0].onclick = function() { //버튼 클릭하면 창 닫기

                        modalDiv3[0].style.display = "none";
                        document.body.style.overflow = "auto";
                        document.body.style.overflowX = "hidden";

                    }
                } else {
                    console.log('인증실패')
                    //alert('인증 실패하였습니다.');

                    document.getElementById("resultNum").innerText = ""; //경고문구 지우기
                    document.getElementById("resultPwd").innerText = "";

                    accounNums[1].style.border = "1px solid black";
                    accounNums[1].style.outline = "1px solid black";
                    accounPwds[0].style.border = "none";
                    accounPwds[0].style.outline = "1px solid black";

                    accounNums[1].value = accountNumsValue; //값 남기기
                    accounPwds[0].value = accountPwdsValue;

                    //modalDiv3[0].style.display = "block";

                }

            }
        }
    }
}

authBtns[0].addEventListener("click",accountAuth);


// -- 계좌 상세 보기 ----------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

/*
    계좌 상세 모달
    계좌 별칭 수정
    계좌 삭제
*/

// 계좌 카드 누를 때 함수
function showAccount(myaccountNum) {

    $('#account-modal'+myaccountNum).removeClass('hidden');
    $('#account-modal'+myaccountNum).addClass('visible');

    $('body').css('overflow', 'hidden');

}

// x 버튼 누를 때 함수
function closeAccount(myaccountNum) {

    $('#account-modal'+myaccountNum).addClass('hidden');
    $('#account-modal'+myaccountNum).removeClass('visible');

    $('body').css('overflow', 'auto');
    $('body').css('overflowX', 'hidden');

    $('.custom-select-option').eq(0).click(); //selectbox 1개월로 지정
    $('#search' + myaccountNum).val(''); //검색창 값 지우기

}

//// Modal 영역 밖을 클릭하면 Modal 닫음
//window.onclick = function(event) {
//    if (event.target.className == "account-modal") {
//        event.target.style.display = "none";
//        document.body.style.overflow = "auto";
//        document.body.style.overflowX = "hidden";
//    }
//};

// 계좌명 수정
function updateBtn(myaccountNum){

    $('#account-alias'+myaccountNum).addClass('hidden');
    $('#aname'+myaccountNum).removeClass('hidden');

    $('#account-update'+myaccountNum).addClass('hidden');
    $('#account-update-ok'+myaccountNum).removeClass('hidden');

}





// -- 계좌 삭제 예/아니오---------------------------------------------------------------
var accountFunc = []; //함수 저장 변수

//모달창 클릭, 모달창
var accDelBtn = document.getElementsByClassName("account-delete");
var accountdelDiv = document.getElementsByClassName("accountDelmodal-div");

//예스버튼 클릭
var yesBtns = document.getElementsByClassName("yesBtn");
var noBtns = document.getElementsByClassName("noBtn");

function accountDelModal(num) {
    return function() {

        accDelBtn[num].onclick = function() {
            accountdelDiv[num].style.display = "block";
            document.body.style.overflow = "hidden";
        }

//        yesBtns[num].onclick = function() {
//            accountdelDiv[num].style.display = "none";
//            document.body.style.overflow = "auto";
//            document.body.style.overflowX = "hidden";
//        }

        noBtns[num].onclick = function() {
            accountdelDiv[num].style.display = "none";
            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";
        }
    }
}

for(var i = 0; i < accDelBtn.length; i++) {
    accountFunc[i] = accountDelModal(i); //함수 담기
}

for(var j = 0; j < accDelBtn.length; j++) {
    accountFunc[j](); //함수 호출
}

// -- 계좌 내역 조회 ---------------------------------------------------------------


