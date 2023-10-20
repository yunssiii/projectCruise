
//-- 모임 탈퇴 js------------------------------------------------------------
/*
    탈퇴 버튼 클릭 시 모달
    예/아니오 -> 모달
*/

$('.user-out').on('click', showDelInfo(crewNum));
$('.nBtn').on('click', closeNo);
$('.no').on('click', closeContinue);
$('.yes').on('click', closeExit);
$('.e-yes').on('click', closeNone);


// '탈퇴하기' 버튼 누를 때 실행함수
function showDelInfo(crewNum) {

    $('.crewDelmodal-div'+crewNum).removeClass('hidden');
    $('.crewDelmodal-div').addClass('visible');

    $('body').css('overflow', 'hidden');

}

// 첫 모달에서 '예' 버튼 누를 때 실행함수 - ok
function closeOk() {

    $('.crewDelmodal-div').addClass('hidden'); //info창 닫기

    $('.y-div').removeClass('hidden');//탈퇴됨 창 열기
    $('.y-div').addClass('visible');

}

// 첫 모달에서 '예' 버튼 누를 때 실행함수 - none
function closeDelNone() {

    $('.crewDelmodal-div').addClass('hidden'); //info창 닫기

    $('.e-div').removeClass('hidden');//선장 창 열기
    $('.e-div').addClass('visible');

}

// 첫 모달에서 '아니오' 버튼 누를 때 실행함수
function closeNo() {

    $('.crewDelmodal-div').addClass('hidden'); //info창 닫기

    $('.n-div').removeClass('hidden');//계속 창 열기
    $('.n-div').addClass('visible');

}

// 계속 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeContinue() {

    $('.n-div').addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}

// 탈퇴 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeExit() {

    $('.y-div').addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}

// 선장 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeNone() {

    $('.e-yes').addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}




