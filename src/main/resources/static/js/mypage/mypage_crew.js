
//-- 모임 탈퇴 js------------------------------------------------------------
/*
    탈퇴 버튼 클릭 시 모달
    예/아니오 -> 모달
*/



// '탈퇴하기' 버튼 누를 때 실행함수
function showDelInfo(crewNum) {

    $('#crewDelmodal-div'+crewNum).removeClass('hidden');
    $('#crewDelmodal-div'+crewNum).addClass('visible');

    $('body').css('overflow', 'hidden');

}

// 첫 모달에서 '예' 버튼 누를 때 실행함수 - ok
function closeOk(crewNum) {

    $('#crewDelmodal-div'+crewNum).addClass('hidden'); //info창 닫기

    $('#y-div'+crewNum).removeClass('hidden');//탈퇴됨 창 열기
    $('#y-div'+crewNum).addClass('visible');

}

// 첫 모달에서 '예' 버튼 누를 때 실행함수 - none
function closeDelNone(crewNum) {

    $('#crewDelmodal-div'+crewNum).addClass('hidden'); //info창 닫기

    $('#e-div'+crewNum).removeClass('hidden');//선장 창 열기
    $('#e-div'+crewNum).addClass('visible');

}

// 첫 모달에서 '아니오' 버튼 누를 때 실행함수
function closeNo(crewNum) {

    $('#crewDelmodal-div'+crewNum).addClass('hidden'); //info창 닫기

    $('#n-div'+crewNum).removeClass('hidden');//계속 창 열기
    $('#n-div'+crewNum).addClass('visible');

}

// 계속 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeContinue(crewNum) {

    $('#n-div'+crewNum).addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}

// 탈퇴 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeExit(crewNum) {

    $('#y-div'+crewNum).addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}

// 선장 창에서 '확인' 버튼 누를 때 실행함수 - 창 닫힘
function closeNone(crewNum) {

    $('#e-yes'+crewNum).addClass('hidden'); //계속 창 닫기

    $('body').css('overflow','auto');
    $('body').css('overflowX','hidden');

    location.reload(); // 페이지 리로드

}




