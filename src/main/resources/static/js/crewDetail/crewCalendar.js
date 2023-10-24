
// X 누르면 모달 닫히게 하기
$('#closeXBtn').on('click',function(){
    if(!$('#modalBg').hasClass('hiddenModalBg')){
        $('#modalBg').addClass('hiddenModalBg');
    }

    if(!$('#calendarModal').hasClass('calendarHidden')){
        $('#calendarModal').addClass('calendarHidden');
    }
})






