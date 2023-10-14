

// 자세히 보기 누르면 캘린더 모달 띄우기
$('#detail_href').on('click',function(){
    if($('#modalBg').hasClass('hiddenModalBg')){
        $('#modalBg').removeClass('hiddenModalBg');
    }
})

// X 누르면 모달 닫히게 하기
$('#closeXBtn').on('click',function(){
    if(!$('#modalBg').hasClass('hiddenModalBg')){
        $('#modalBg').addClass('hiddenModalBg');
    }
})