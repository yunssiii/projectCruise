
//-- 캘린더 기능 --------------------------------------------------
function myCalendarLoad(email){

    $(function () {

        var request = $.ajax({
            url: "/mypage/mypage_all_sche",
            method: "POST",
            data: {
            email: email
            }
        })

        request.done(function (data) {
            var calendarEl = document.getElementById('calendar');
            // 렌더링
            var calendar = new FullCalendar.Calendar(calendarEl, {
                aspectRatio: 1.35,
                headerToolbar: { // 툴바 설정부분.
                    left: 'dayGridMonth,listMonth',
                    center: 'title',
                    right: 'prev,next today'
                },
                eventDataTransform: function(event) {
                  if(event.allDay) {
                    event.end = moment(event.end).add(1, 'days').format('YYYY-MM-DD HH:mm:SS')

                  }
                  return event;
                },
                height: 490,
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
                    oneDayClick(info,email);
                    $('.oneday').children().remove();
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
            $('#searchDate').on('change', function () {
                calendar.gotoDate($(this).val());
                btnNameCustom();
            })

            var monthViewBtn = document.querySelectorAll('button[title="month view"]');
            var listViewBtn = document.querySelectorAll('button[title="list view"]');
            var PrevMonthBtn = document.querySelectorAll('button[title="Previous month"]');
            var NextMonthBtn = document.querySelectorAll('button[title="Next month"]');
            var ThisMonthBtn = document.querySelectorAll('button[title="This month"]');
            var calendarTagA = calendarEl.querySelectorAll('a');

            var clickEvent = new MouseEvent("click", {
                bubbles: true,
                cancelable: true,
                view: window
            });

            if ($('#modalBg').hasClass('hiddenModalBg')) {
                $('#modalBg').removeClass('hiddenModalBg');

                // 왜인지 모르겠는데 처음으로 Modal을 켜면 달력이 깨진채로 나와서...
                // month 버튼을 눌러주니 제자리를 찾아가길래, modal이 띄워지면 month버튼이 같이 눌러지도록 설정해주었다.

                monthViewBtn[0].dispatchEvent(clickEvent);

                // 버튼 커스텀하기
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
               // listViewBtn[0].textContent = '리스트보기';
                ThisMonthBtn[0].textContent = '오늘';
            }

            function btnNameCustomAddEvent(item) {
                item.addEventListener('click', function () {
                    btnNameCustom()
                })
            }
        });
    });
};



//-- 하루 이벤트 조회 --------------------------------------------------
function oneDayClick(info,email){

    //선택한 날짜 가져오기
    var sDateObj = new Date(info.date);
    var sYear = sDateObj.getFullYear();
    var sMonth = (sDateObj.getMonth() + 1);
    var sDate = sDateObj.getDate();

    if (sDate < 10) {
        sDate = "0" + sDate;
    }
    if (sMonth < 10) {
        sMonth = "0" + sMonth;
    }

    var clickDate = sYear + "-" + sMonth + "-" + sDate; //넘기는 값
    var divDate = sMonth + "." + sDate; //뷰에 표시하는 값

    console.log('로그인한 이메일>>>>>' + email);
    console.log('시작일>>>>>' + clickDate);

    $.ajax({
            type : "POST",
            url : "/mypage/mypage_onedaySche",
            data : {
                email : email,
                clickDate : clickDate
            },
            success : function(result){
                console.log('일정조회 성공..!>>>>>'+result);

                //모달 부분 보여짐
                $('.oneday').removeClass('hidden');
                $('.oneday').addClass('visible');

                $('.sche-date').text('📌 '+ divDate); //클릭한 날짜 html 표시

                var str = '';
                var message = '등록된 일정이 없습니다.';

                if(result.length === 0){

                    console.log('결과 길이>>>>>' + result.length)

                        str += '<div class="oneDay-modal">'
                            str += '<div class="oneDay-div1">'
                                str += '<div class="sche-assort" style="background-color:white"></div>'
                                str += '<div class="sche-title-box">'
                                    str += '<p class="sche-title">'+ message +'</p>'
                                    str += '<p class="sche-crewName"></p>'
                                str += '</div>'
                            str += '</div>'
                            str += '<div class="oneDay-div2">'
                                str += '<div class="sche-start"></div>'
                                str += '<div class="sche-end"></div>'
                            str += '</div>'
                        str += '</div>'

                        $('.oneday').append(str);

                }else{

                    $.each(result, function(i) {

                        str += '<div class="oneDay-modal">'
                            str += '<div class="oneDay-div1">'
                                str += '<div class="sche-assort" style="background-color:'+ result[i].color +'"></div>'
                                str += '<div class="sche-title-box">'
                                    str += '<p class="sche-title">'+ result[i].title +'</p>'
                                    str += '<p class="sche-crewName">'+ result[i].crewName +'</p>'
                                str += '</div>'
                            str += '</div>'
                            str += '<div class="oneDay-div2">'
                                str += '<div class="sche-start">'+ result[i].start +'</div>'
                                str += '<div class="sche-end">'+ result[i].end +'</div>'
                            str += '</div>'
                        str += '</div>'

                    });

                    $('.oneday').append(str);

                }

            },
            error : function(xhr,status,error){
                console.log('일정조회 에러..! >>>>>>>>'+error);
            }
    });


}