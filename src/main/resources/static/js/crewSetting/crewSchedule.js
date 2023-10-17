

// 날짜 클릭했을 때 실행할 함수
function fullCalDateClick(info){
    $('#scheModalBg').removeClass('hiddenModal')
    $('#addScheModal').removeClass('hiddenModal')

    // 선택된 날짜 div에 표시하기
    var dateObj = new Date(info.date);
    var year = dateObj.getFullYear();
    var month = (dateObj.getMonth()+1);
    var date = dateObj.getDate();
    if(date<10) {date = "0" + date;}

    if(month<10) {month = "0" + month;}

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

// 이벤트 클릭했을 때 실행할 함수
function fullCalEventClick(info) {
    alert(info.event.title + "")
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
    // 선택된 색상에만 selectedColor 적용하기
    $(this).addClass('selectedColor');
    $('.assortColors').not(this).removeClass('selectedColor')
    var selectColor = $(this).attr('id')

    // hidden input에 값 넢어주기
    $('#scheAssort').val(selectColor);

    // 제목 옆 #selectedColor 에 선택된 클래스 적용해주기
    $('#selectedColor').removeClass();
    $('#selectedColor').addClass($(this).attr('id'));

    //말풍선 닫기
    $('#colorSelectorContainer').removeClass('visibleSpeechBubble');
    $('#colorSelectorContainer').addClass('hiddenSpeechBubble');
})

// 날짜, 시간 합쳐서 채워넣기

$(document).ready(function() {

})

$('#startDate').on('change',function() {
    var startDateValue = $('#startDate').val();
    var startTimeValue = $('#startTime').val();
    $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
})

$('#startTime').on('change',function() {
    var startDateValue = $('#startDate').val();
    var startTimeValue = $('#startTime').val();
    $('#scheStart').val(startDateValue + ' ' + startTimeValue + ':00');
})

$('#endDate').on('change',function() {
    var endDateValue = $('#endDate').val();
    var endTimeValue = $('#endTime').val();
    $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
})

$('#endTime').on('change',function() {
    var endDateValue = $('#endDate').val();
    var endTimeValue = $('#endTime').val();
    $('#scheEnd').val(endDateValue + ' ' + endTimeValue + ':00');
})







