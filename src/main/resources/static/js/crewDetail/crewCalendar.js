

// 자세히 보기 누르면 캘린더 모달 띄우기
$('#detail_href').on('click',function(){
    var monthViewBtn = document.querySelectorAll('button[title="month view"]');
    var listViewBtn = document.querySelectorAll('button[title="list view"]');
    var PrevMonthBtn = document.querySelectorAll('button[title="Previous month"]');
    var NextMonthBtn = document.querySelectorAll('button[title="Next month"]');
    var ThisMonthBtn = document.querySelectorAll('button[title="This month"]');
    var calendarEl = document.getElementById('calendar');
    var calendarTagA = calendarEl.querySelectorAll('a');

    var clickEvent = new MouseEvent("click", {
        bubbles: true,
        cancelable: true,
        view: window
    });

    if($('#modalBg').hasClass('hiddenModalBg')){
        $('#modalBg').removeClass('hiddenModalBg');

        // 왜인지 모르겠는데 처음으로 Modal을 켜면 달력이 깨진채로 나와서...
        // month 버튼을 눌러주니 제자리를 찾아가길래, modal이 띄워지면 month버튼이 같이 눌러지도록 설정해주었다.

        monthViewBtn[0].dispatchEvent(clickEvent);
        
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
            calendarTagA.forEach(function (item) {
                btnNameCustomAddEvent(item);
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

})





// X 누르면 모달 닫히게 하기
$('#closeXBtn').on('click',function(){
    if(!$('#modalBg').hasClass('hiddenModalBg')){
        $('#modalBg').addClass('hiddenModalBg');
    }
})