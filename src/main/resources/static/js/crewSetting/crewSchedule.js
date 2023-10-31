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
                height: '100%',
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
                    fullCalDateClick(info,crewNum);
                },
                eventClick: function(info) { // 이벤트 클릭 했을 때
                    fullCalEventClick(info, crewNum);
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
                dayMaxEvents: true, // 칸 보다 이벤트가 많으면 more 제공
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

// 모달 공통세팅
function sameModalSetting(crewNum) {

    // X 누르면 모달 닫기
    $('#addScheModalcloseX').on('click',function() {
        $('#scheModalBg').addClass('hiddenModal')
        $('#addScheModal').addClass('hiddenModal')
        $('#colorSelectorContainer').addClass('hiddenSpeechBubble');

        $('#updateScheOK').off('click');
        $('#delSche').off('click');
        $('#delScheOK').off('click');
        $('#delScheCancel').off('click');
        $('#selectedColorCont').off('click');
        $('#allDayChk').off('change');
        $('#startDate').off('change');
        $('#startTime').off('change');
        $('#endDate').off('change');
        $('#endTime').off('change');

        var scheForm = document.scheForm;
        scheForm.reset();
    })

    // 하루종일에 체크되면 date input 없애기
    $('#allDayChk').on('change',function (){
        var startDateValue = $('#startDate').val();
        var startTimeValue = $('#startTime').val();
        var endDateValue = $('#endDate').val();
        var endTimeValue = $('#endTime').val();

        if(this.checked) {
            $('#startTime').addClass('inputHidden');
            $('#endTime').addClass('inputHidden');
            $('#scheStart').val(startDateValue + ' 00:00:00');
            $('#scheEnd').val(endDateValue + ' 00:00:00');
        } else {
            $('#startTime').removeClass('inputHidden');
            $('#endTime').removeClass('inputHidden');
            $('#scheStart').val(startDateValue + ' ' + startTimeValue);
            $('#scheEnd').val(endDateValue + ' ' + endTimeValue);
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
        // 1. 모달 띄워졌을 때 날짜시간값 채워두기
        var startDateValue = $('#startDate').val();
        var startTimeValue = $('#startTime').val();
        $('#scheStart').val(startDateValue + ' ' + startTimeValue);

        var endDateValue = $('#endDate').val();
        var endTimeValue = $('#endTime').val();
        $('#scheEnd').val(endDateValue + ' ' + endTimeValue);

        // 2. 모달 띄워졌을 때 값을 복원데이터로 저장해두기
        $('#scheStart').data('prevDateValue', $('#startDate').val());
        $('#scheStart').data('prevTimeValue', $('#startTime').val());
        $('#scheEnd').data('prevDateValue', $('#endDate').val());
        $('#scheEnd').data('prevTimeValue', $('#endTime').val());

        // 3. 각 input 요소마다 이벤트리스너 달아주기
        $('#startDate').on('change',function() {
            
            // 1. 요소가 변경된 후의 FullDate들을 가져와주고, 
            var startDateValue = $('#startDate').val();
            var startTimeValue = $('#startTime').val();
            $('#scheStart').val(startDateValue + ' ' + startTimeValue);

            var scheStartDateObj = new Date($('#scheStart').val());
            var scheEndDateObj = new Date($('#scheEnd').val());

            // 2. 변경된 후의 값을 비교한 후,
                // 조건이 true면 전 값으로 돌리기
                // 조건이 false면 변경된 값 유지
            if(scheStartDateObj>scheEndDateObj) {
                alert("시작일정은 마감일정보다 이전이거나 같아야 합니다.");
                $('#startDate').val($('#scheStart').data('prevDateValue'));
                startDateValue = $('#startDate').val();
                $('#scheStart').val(startDateValue + ' ' + startTimeValue);
                return;
            }
            
            // 3. 변경된 후의 값을 다시 복원데이터로 저장
            $('#scheStart').data('prevDateValue', $('#startDate').val());
                // if문을 실행하지 않고 변경되었다면, 변경된 값을 다시 백업데이터로 저장.

        })

        $('#startTime').on('change',function() {

            var startDateValue = $('#startDate').val();
            var startTimeValue = $('#startTime').val();
            $('#scheStart').val(startDateValue + ' ' + startTimeValue);

            var scheStartDateObj = new Date($('#scheStart').val());
            var scheEndDateObj = new Date($('#scheEnd').val());

            if(scheStartDateObj>scheEndDateObj) {
                alert("시작일정은 마감일정보다 이전이거나 같아야 합니다.");
                $('#startTime').val($('#scheStart').data('prevTimeValue'));
                startTimeValue = $('#startTime').val();
                $('#scheStart').val(startDateValue + ' ' + startTimeValue);
                return;
            }
            $('#scheStart').data('prevTimeValue', $('#startTime').val());
                // if문을 실행하지 않고 변경되었다면, 변경된 값을 다시 백업데이터로 저장.

        })

        $('#endDate').on('change',function() {

            var endDateValue = $('#endDate').val();
            var endTimeValue = $('#endTime').val();
            $('#scheEnd').val(endDateValue + ' ' + endTimeValue);

            var scheStartDateObj = new Date($('#scheStart').val());
            var scheEndDateObj = new Date($('#scheEnd').val());

            if(scheStartDateObj>scheEndDateObj) {
                alert("시작일정은 마감일정보다 이전이거나 같아야 합니다.");
                $('#endDate').val($('#scheEnd').data('prevDateValue'));
                endDateValue = $('#endDate').val();
                $('#scheEnd').val(endDateValue + ' ' + endTimeValue);
                return;
            }
            $('#scheEnd').data('prevDateValue', $('#endDate').val());

        })

        $('#endTime').on('change',function() {
            var endDateValue = $('#endDate').val();
            var endTimeValue = $('#endTime').val();
            $('#scheEnd').val(endDateValue + ' ' + endTimeValue);

            var scheStartDateObj = new Date($('#scheStart').val());
            var scheEndDateObj = new Date($('#scheEnd').val());

            if(scheStartDateObj>scheEndDateObj) {
                alert("시작일정은 마감일정보다 이전이거나 같아야 합니다.");
                $('#endTime').val($('#scheEnd').data('prevTimeValue'));
                endTimeValue = $('#endTime').val();
                $('#scheEnd').val(endDateValue + ' ' + endTimeValue);
                return;
            }
            $('#scheEnd').data('prevTimeValue', $('#endTime').val());
        })
}

// 수정 시 색 selectedColor 클래스 적용
function updateModalColorClassSet(className) {
    $('#selectedColor').removeClass();
    $('#selectedColor').addClass(className);
    $('#' + className).addClass('selectedColor');
    $('.assortColors:not(#' +className + ')').removeClass('selectedColor');
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


// 날짜 칸 클릭했을 때 실행할 함수 -----------------------------------------------

    // 추가 모달 처음 세팅
    function fullCalDateClick(info, crewNum){

        // 색깔 선택 컨테이너 설정 초기화하기
        $('#selectedColor').removeClass();
        $('#selectedColor').addClass('redSche');
        $('#redSche').addClass('selectedColor');
        $('.assortColors:not(#redSche)').removeClass('selectedColor');
        $('#scheTitle').val('');

        $('#scheModalBg').removeClass('hiddenModal')
        $('#addScheModal').removeClass('hiddenModal')
        $('#allDayChk').prop('checked',false);

        // 수정, 삭제 버튼 없애고 추가버튼 보이기
        $('#addScheOK').removeClass('hiddenBtn');
        $('#updateScheOK').addClass('hiddenBtn');
        $('#delSche').addClass('hiddenBtn');

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
        $('#startTime').val('08:00:00');
        $('#endTime').val('09:00:00');

        var startDateValue = $('#startDate').val();
        var startTimeValue = $('#startTime').val();
        var endDateValue = $('#endDate').val();
        var endTimeValue = $('#endTime').val();

        $('#startTime').removeClass('inputHidden');
        $('#endTime').removeClass('inputHidden');
        $('#scheStart').val(startDateValue + ' ' + startTimeValue);
        $('#scheEnd').val(endDateValue + ' ' + endTimeValue);

        sameModalSetting(crewNum); // 공통적인 부분 함수로 따로 빼기
    }

// ---------------------------------------------------------------------------


// 일정 자세히 보기 -----------------------------------------------------------


    function fullCalEventClick(info, crewNum) {
        // 일정 자세히보기 모달 세팅
        // 모달 켜기
        $('#scheModalBg').removeClass('hiddenModal');
        $('#addScheModal').removeClass('hiddenModal');
        $('#delScheModal_YN').addClass('hiddenModal');

        // 추가버튼 없애고 수정,삭제 버튼 보이기
        $('#addScheOK').addClass('hiddenBtn');
        $('#updateScheOK').removeClass('hiddenBtn');
        $('#delSche').removeClass('hiddenBtn');

        // read 제목 설정
        $('#addScheTitle').text("[일정] " + info.event.title);
        $('#scheTitle').val(info.event.title);

        // read 시작일 설정
        var dateObj = new Date(info.event.start);
        var year = dateObj.getFullYear();
        var month = (dateObj.getMonth() + 1);
        var date = dateObj.getDate();
        var hours = dateObj.getHours();
        var minutes = dateObj.getMinutes();

        if (date < 10) {
            date = "0" + date;
        }
        if (month < 10) {
            month = "0" + month;
        }
        if (hours < 10) {
            hours = "0" + hours;
        }
        if (minutes < 10) {
            minutes = "0" + minutes;
        }

        var startDate = year + "-" + month + "-" + date;
        var startTime = hours + ":" + minutes + ":00"

        $('#startDate').val(startDate)
        $('#startTime').val(startTime)

        // 종료일 설정

        var dateObj = new Date(info.event.end);
        var year = dateObj.getFullYear();
        var month = (dateObj.getMonth() + 1);
        var date = dateObj.getDate();
        var hours = dateObj.getHours();
        var minutes = dateObj.getMinutes();


        if (info.event.allDay) { // allDay일 경우 숨기기
            $('#startTime').addClass('inputHidden');
            $('#endTime').addClass('inputHidden');
            date = date - 1;
        } else {
            $('#startTime').removeClass('inputHidden');
            $('#endTime').removeClass('inputHidden');
        }

        if (date < 10) {
            date = "0" + date;
        }
        if (month < 10) {
            month = "0" + month;
        }
        if (hours < 10) {
            hours = "0" + hours;
        }
        if (minutes < 10) {
            minutes = "0" + minutes;
        }

        var endDate = year + "-" + month + "-" + date;
        var endTime = hours + ":" + minutes + ":00"

        $('#endDate').val(endDate)
        $('#endTime').val(endTime)

        // 체크여부 설정
        if (info.event.allDay == true) {
            $('#allDayChk').prop('checked', true)
        } else {
            $('#allDayChk').prop('checked', false)
        }


        //색 설정하기
        switch (info.event.backgroundColor) {
            default:
            case "#FF8383":
                updateModalColorClassSet("redSche");
                break;
            case "#22B14C":
                updateModalColorClassSet("greenSche");
                break;
            case "#FFC90E":
                updateModalColorClassSet("yellowSche");
                break;
            case "#00A5ED":
                updateModalColorClassSet("blueSche");
                break;
            case "#A1A1A1":
                updateModalColorClassSet("graySche");
                break;
        }

        sameModalSetting(crewNum); // 공통적인 부분 함수로 빼기

        //update 누르면 해당 데이터 수정하기
        $('#updateScheOK').on('click', function () {

            var scheForm = document.scheForm;
            // 넘겨야할 데이터들
            var scheTitle = scheForm.sche_title.value;
            var scheAssort = scheForm.sche_assort.value;
            var scheAllDayTF = "";

            if (scheForm.sche_alldayTF.checked) {
                scheAllDayTF = "true";
            } else {
                scheAllDayTF = "false";
            }
            ;
            var scheStart = scheForm.sche_start.value;
            var scheEnd = scheForm.sche_end.value;

            var scheNum = info.event.id;
            var request = $.ajax({
                url: "/crew/setting/updateCrewSche",
                method: "POST",
                data: {
                    scheNum: scheNum,
                    scheTitle: scheTitle,
                    scheAssort: scheAssort,
                    scheAllDayTF: scheAllDayTF,
                    scheStart: scheStart,
                    scheEnd: scheEnd
                }
            })

            request.done(function (resp) {
                // 수정 폼 닫기
                $('#scheModalBg').addClass('hiddenModal');
                $('#addScheModal').addClass('hiddenModal');
                var scheForm = document.scheForm;
                // 캘린더 다시 렌더링
                calendarLoad(crewNum);
            })
        })

        // 삭제
        $('#delSche').on('click', function () {

            $('#addScheModal').addClass('hiddenModal');
            $('#delScheModal_YN').removeClass('hiddenModal');

            $('#deleteMsg').text('[' + info.event.title + '] 일정을 삭제합니다.')

        })

        $('#delScheOK').on('click', function () {
            var scheNum = info.event.id;
            var request = $.ajax({
                url: "/crew/setting/deleteCrewSche",
                method: "POST",
                data: {
                    scheNum: scheNum,
                }
            })

            request.done(function (resp) {
                // 삭제 폼 닫기
                $('#scheModalBg').addClass('hiddenModal');
                $('#delScheModal_YN').addClass('hiddenModal');
                // 캘린더 다시 렌더링
                calendarLoad(crewNum);
                var scheForm = document.scheForm;
                scheForm.reset();
            })
        })

        $('#delScheCancel').on('click',function(){
            $('#scheModalBg').addClass('hiddenModal');
            $('#delScheModal_YN').addClass('hiddenModal');
            var scheForm = document.scheForm;
            scheForm.reset();
            calendarLoad(crewNum);
            $('#delScheOK').off('click');
        })
    }



