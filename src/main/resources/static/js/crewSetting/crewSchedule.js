

// 날짜 클릭했을 때 실행할 함수
function fullCalDateClick(info){
    $('#scheModalBg').removeClass('hiddenModal')

    // 선택된 날짜 div에 표시하기
    var dateObj = new Date(info.date);
    var year = dateObj.getFullYear();
    var month = (dateObj.getMonth()+1);
    var date = dateObj.getDate();
    if(date<10) {
        date = "0" + date;
    }

    var dateStr =  year + "년 " + month + "월 " + date + "일 "
    $('#addScheTitle').text(dateStr + " 일정 추가하기");

    var formattedDate = year + "-" + month + "-" + date;
    $('#startDate').val(formattedDate);
    $('#endDate').val(formattedDate);
    $('#startDateTime').val('08:00');
    $('#endDateTime').val('09:00');
}

// X 눌렀을 때 모달 닫기
$('#addScheModalcloseX').on('click',function() {
    $('#scheModalBg').addClass('hiddenModal')
})

// 하루종일에 체크되면 date input 없애기
$('#allDayChk').on('change',function (){
    if(this.checked) {
        $('#startDateTime').addClass('inputHidden', 500, 'easeOutBounce');
        $('#endDateTime').addClass('inputHidden', 500, 'easeOutBounce');
    } else {
        $('#startDateTime').removeClass('inputHidden', 500, 'easeOutBounce');
        $('#endDateTime').removeClass('inputHidden', 500, 'easeOutBounce');
    }
})