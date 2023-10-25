
var withdrawalBtns = document.getElementsByClassName("withdrawal");
var userOutDiv = document.getElementsByClassName("userOutmodal-div");
var yBtn = document.getElementsByClassName("yBtn");
var nBtn = document.getElementsByClassName("nBtn");

var yDivs = document.getElementsByClassName("y-div");
var nDivs = document.getElementsByClassName("n-div");

var okBtn1 = document.getElementsByClassName("yes");
var okBtn2 = document.getElementsByClassName("no");

// 회원 탈퇴 js

//'화원 탈퇴' 누르면 모달
withdrawalBtns[0].onclick = function() {

    userOutDiv[0].style.display = "block";
    document.body.style.overflow = "hidden";

}

// '예' 누르면 탈퇴 처리
function deleteUser(email){

    $.ajax({
        type : "POST",
        url : "/mypage/deleteUser",
        data : {
        email : email, // 로그인한 이메일
        },
        success : function(result){
            console.log("탈퇴성공..!");

            //모달 열기
            $('.userOutmodal-div').css('display','none');
            $('.y-div').css('display','block');

        },
        error:function(xhr,status,error) {
            // 모임통장 계좌번호와 같으면 에러로 넘어옴
            console.log("탈퇴에러..!" + error);

            //모달 열기
            $('.userOutmodal-div').css('display','none');
            $('.n-div').css('display','block');

        }
    });
};

// '아니오' 누르면 모달 닫기
function closeModal() {

    $('.userOutmodal-div').css('display','none');

    $('body').css('overflow', 'auto');
    $('body').css('overflow-x', 'hidden');

}

// 탈퇴완료의 '확인' 함수
function okMain() {

    $('.y-div').css('display','none');

    $('body').css('overflow', 'auto');
    $('body').css('overflow-x', 'hidden');

    //location.replace("/logout"); //전 페이지로 돌아갈 수 없음

    localStorage.removeItem('accessToken');
    window.location.href = '/logout';
}

// 선장의 '확인' 함수
function okMypage() {

    $('.n-div').css('display','none');

    $('body').css('overflow', 'auto');
    $('body').css('overflow-x', 'hidden');

    location.reload();
}

