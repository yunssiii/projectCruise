// 캘린더 렌더링 함수
function calendarLoad(crewNum) {
    $(function () {
        var request = $.ajax({
            url: "/crew/setting/loadCrewSchedule",
            method: "POST",
            data: {
                crewNum: crewNum
            }
        })

        request.done(function (data) {
            var calendarEl = document.getElementById('calendar');
            // 렌더링
            var calendar = new FullCalendar.Calendar(calendarEl, {
                headerToolbar: { // 툴바 설정부분.
                    left: 'dayGridMonth,listMonth',
                    center: 'title',
                    right: 'prev,next today'
                },
                height: 460,
                // 한글달력 일자에 '일' 부분 빼기
                dayCellContent: function (info) {
                    var number = document.createElement("a");
                    number.classList.add("fc-daygrid-day-number");
                    number.innerHTML = info.dayNumberText.replace("일", '');
                    if (info.view.type === "dayGridMonth") {
                        return {
                            html: number.outerHTML
                        };
                    }
                    return {
                        domNodes: []
                    };
                },
                dateClick: function(info) { // 날짜 클릭했을 때
                    fullCalDateClick(info);
                },
                eventClick: function(info) { // 이벤트 클릭 했을 때
                    fullCalEventClick(info);
                },
                views: {
                    dayGridMonth: {
                        type: 'dayGrid',
                        duration: {months: 1},
                        monthMode: true,
                        fixedWeekCount: false, // 다음달의 첫 주가 마지막줄에 추가되는 것을 없애기 위함
                    }
                },
                // initialDate: '2023-01-12', // 디폴트 데이트. 창을 처음 켰을 때 포커싱할 날짜를 설정한다. 이 설정을 없애면 오늘 날짜로 포커싱된다.
                navLinks: true, // can click day/week names to navigate views - 요일이나 날짜 클릭시, 일이나 주 단위 보여주는 화면으로 넘어간다.
                businessHours: true, // display business hours
                editable: true, // 드래그해서 수정이 가능한지에 대한 여부. 길게 확장도 가능하다.
                selectable: true, // more 표시 전 최대 이밴트 갯수. true는 셀 높이에 의해 결정된다.
                locale: "ko", // 이 설정을 추가하면 한글로 출력이 된다.
                events: data // 이 부분에 데이터가 들어옴.
            });

            calendar.render();

            /* 날짜 검색용 */
            $('#searchDate').on('change',function() {
                calendar.gotoDate($(this).val());
                btnNameCustom();
            })

            var monthViewBtn = document.querySelectorAll('button[title="month view"]');
            var listViewBtn = document.querySelectorAll('button[title="list view"]');
            var PrevMonthBtn = document.querySelectorAll('button[title="Previous month"]');
            var NextMonthBtn = document.querySelectorAll('button[title="Next month"]');
            var ThisMonthBtn = document.querySelectorAll('button[title="This month"]');
            var calendarTagA = calendarEl.querySelectorAll('a');

            var clickEvent = new Event("click");

            if($('#scheduleSet').hasClass('visibleSettings')){

                // 버튼 커스텀하기
                // 버튼 이름 커스텀
                // 1. Modal이 띄워졌을 때
                btnNameCustom();

                // 2. 달력보기, 리스트보기를 눌렀을 때
                // 달력 버튼들 누르니까 기존 text가 다시 추가되길래.. 방지용
                btnNameCustomAddEvent(monthViewBtn[0]);
                btnNameCustomAddEvent(listViewBtn[0]);
                btnNameCustomAddEvent(PrevMonthBtn[0]);
                btnNameCustomAddEvent(NextMonthBtn[0]);
                btnNameCustomAddEvent(ThisMonthBtn[0]);
                calendarTagA.forEach(function () {
                    btnNameCustomAddEvent(this);
                })
            }

            function btnNameCustom() {
                monthViewBtn[0].textContent = '달력보기';
                listViewBtn[0].textContent = '리스트보기';
                ThisMonthBtn[0].textContent = '오늘';
            }

            function btnNameCustomAddEvent(item) {
                item.addEventListener('click',function(){ btnNameCustom() })
            }

        });
    })
}

// 캘린더 일정 추가
function addScheOKBtnClick(crewNum) {
    $(function () {
        var scheForm = document.scheForm;

        // 넘겨야할 데이터들
        var scheTitle = scheForm.sche_title.value;
        var scheAssort = scheForm.sche_assort.value;
        var scheAllDayTF = scheForm.sche_alldayTF.checked;
        var scheStart = scheForm.sche_start.value;
        var scheEnd = scheForm.sche_end.value;

        var request = $.ajax({
            url: "/crew/setting/addCrewSche",
            method: "POST",
            data: {
                crewNum: crewNum,
                scheTitle: scheTitle,
                scheAssort: scheAssort,
                scheAllDayTF: scheAllDayTF,
                scheStart:scheStart,
                scheEnd:scheEnd
            }
        })

        request.done(function (resp) {
            // 폼 리셋하기
            scheForm.reset();
            $($('.assortColors')[0]).addClass('selectedColor');
            $('.assortColors').not($($('.assortColors')[0])).removeClass('selectedColor');
            $('#selectedColor').removeClass();
            $('#selectedColor').addClass($($('.assortColors')[0]).attr('id'));
            $('#scheAssort').val($($('.assortColors')[0]).attr('id'));

            // 추가 폼 닫기
            $('#scheModalBg').addClass('hiddenModal');
            $('#addScheModal').addClass('hiddenModal');

            // 캘린더 다시 렌더링
            calendarLoad(crewNum);
        })
    })
}



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







