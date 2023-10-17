

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
    $('#startTime').val('08:00');
    $('#endTime').val('09:00');

    var startDateValue = $('#startDate').val();
    var startTimeValue = $('#startTime').val();
    var endDateValue = $('#endDate').val();
    var endTimeValue = $('#endTime').val();

    $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
    $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
}

// X 눌렀을 때 모달 닫기
$('#addScheModalcloseX').on('click',function() {
    $('#scheModalBg').addClass('hiddenModal')
})

// 하루종일에 체크되면 date input 없애기
$('#allDayChk').on('change',function (){
    var startDateValue = $('#startDate').val();
    var startTimeValue = $('#startTime').val();
    var endDateValue = $('#endDate').val();
    var endTimeValue = $('#endTime').val();

    if(this.checked) {
        $('#startTime').addClass('inputHidden', 500, 'easeOutBounce');
        $('#endTime').addClass('inputHidden', 500, 'easeOutBounce');
        $('#scheStart').val(startDateValue + ' 00:00:00');
        $('#scheEnd').val(endDateValue + ' 00:00:00');
    } else {
        $('#startTime').removeClass('inputHidden', 500, 'easeOutBounce');
        $('#endTime').removeClass('inputHidden', 500, 'easeOutBounce');
        $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
        $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
    }
})

// 컬러 선택하기
$('#selectedColorCont').on('click',function(){
    if($('#colorSelectorContainer').hasClass('hiddenSpeechBubble')){
        $('#colorSelectorContainer').removeClass('hiddenSpeechBubble');
        $('#colorSelectorContainer').addClass('visibleSpeechBubble');
    }
})
$('.assortColors').click(function() {
    $(this).addClass('selectedColor');
    var selectColor = $(this).attr('id')
    $('#scheAssort').val('#' + selectColor);
    $('.assortColors').not(this).removeClass('selectedColor')
})

// 날짜, 시간 합쳐서 채워넣기

$(document).ready(function() {

})


$('#startDate').on('change',function() {
    startDateValue = $(this).val();
    $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
})

$('#startTime').on('change',function() {
    startTimeValue = $(this).val()
    $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
})

$('#endDate').on('change',function() {
    endDateValue = $(this).val()
    $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
})

$('#endTime').on('change',function() {
    endTimeValue = $(this).val()
    $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
})



// TODO - insert 쿼리문 작성하고, insert 계속 진행




