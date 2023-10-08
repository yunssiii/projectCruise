
var memberPayViewBoxDiv = document.getElementById("memberPayViewBox");
var payViewMonthSelector = document.querySelector('select[name="payViewMonth"]');
var payTotalDiv = document.querySelector('.payTotal');
// 임시 데이터
    var memNames = [];
    memNames[0] = '김은지';
    memNames[1] = '황윤하';
    memNames[2] = '한두혁';
    memNames[3] = '이미연';
    memNames[4] = '서미진';

    var memPayMoney_Oct = []; // 10월
    memPayMoney_Oct[0] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Oct[1] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Oct[2] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Oct[3] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Oct[4] = 20000; // 0번째 멤버의 납부금액

    var memPayMoney_Sep = []; // 9월
    memPayMoney_Sep[0] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Sep[1] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Sep[2] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Sep[3] = 0; // 0번째 멤버의 납부금액
    memPayMoney_Sep[4] = 20000; // 0번째 멤버의 납부금액

    var memPayMoney_Aug = []; // 8월
    memPayMoney_Aug[0] = 0; // 0번째 멤버의 납부금액
    memPayMoney_Aug[1] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Aug[2] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Aug[3] = 20000; // 0번째 멤버의 납부금액
    memPayMoney_Aug[4] = 20000; // 0번째 멤버의 납부금액

    // select된 달에 대한 리스트를 html로 써주는 함수
    function monthSelectorChange(selectedMonth) {

        // 달이 바뀔 때 하단 Total창이 보이도록 하기
        // 미납자를 선택하고 나서 달을 바꾸면 total창으로 되돌아갈 수 없는 문제를 방지하기 위함
        document.querySelector('.sendMsgContainer').classList.add('hiddenContainer')
        document.getElementById('nonSelected').classList.remove('hiddenContainer');
        document.getElementById('nonSelected').classList.add('nonSelected');


        var memPayMoney = [];

        // 임시적인 코드!
        if(selectedMonth===8 || selectedMonth==='8') {
            memPayMoney = memPayMoney_Aug;
        } else if (selectedMonth===9 || selectedMonth==='9') {
            memPayMoney = memPayMoney_Sep;
        } else {
            memPayMoney = memPayMoney_Oct;
        }

        var html = "";
        var payTotal = 0;

        // 리스트 html 쓰기
        for(var i=0;i<memNames.length;i++) {

            if(memPayMoney[i]===0) {
                html +=
                    "<div class='highlighter'> <div class='oneLineContainer redName'>" +
                    "<div class='payMemName'>" + memNames[i] + "</div>" +
                    "<div class='payMoney'>" + memPayMoney[i].toLocaleString('ko-KR') + "원 </div>" +
                    "</div></div>";
            } else {
                html +=
                    "<div class='oneLineContainer'>" +
                    "<div class='payMemName'>" + memNames[i] + "</div>" +
                    "<div class='payMoney'>" + memPayMoney[i].toLocaleString('ko-KR') + "원 </div>" +
                    "</div>";
            }

            //.toLocaleString('ko-KR') = 숫자 쉼표표시
        }
        memberPayViewBoxDiv.innerHTML = html;

        // 총액 html 쓰기
        for(var i=0;i<memPayMoney.length;i++) {
            payTotal += memPayMoney[i];
        }
        payTotalDiv.textContent
            = payTotal.toLocaleString('ko-KR') + '원';



        var notPayMember = document.querySelectorAll('.highlighter');
        // 함수 밖에서 notPayMember를 초기화해주면
        // 문서가 로딩되었을 때 불러들여진 highlighter 클래스로만 초기화되는듯.
        // 함수가 호출될 때 마다 notPayMember를 선언해주어야
        // 달이 바뀌었을 때 해당하는 달의 highlighter div로 초기화가 된다.

        // 납부하지 않은 멤버의 div에 클릭이벤트리스너 추가
        notPayMember.forEach(function(member) {
            member.addEventListener('click', function(event) {

                // 미납자를 클릭하면 숨겨져있던 sendMsgContainer를 드러내기
                document.querySelector('.sendMsgContainer').classList.remove('hiddenContainer')
                document.getElementById('nonSelected').classList.add('hiddenContainer');
                document.getElementById('nonSelected').classList.remove('nonSelected');

                var notPayName = member.querySelector('.payMemName').textContent;
                document.getElementById('msgReceiver').textContent = notPayName;
                // 미납자의 이름을 가져와서 msgReceiver에 기입한다.


            });
        });
    }

    // memberPayMoneyView DIV에 기본적으로 불러올 데이터 = 현재 월에 해당하는 데이터
    // 이 코드를 써놓지 않으면 페이지 처음 로드 시 아무 데이터도 불러와지지 않음.
    monthSelectorChange(payViewMonthSelector.value);
    // selector의 change를 감지하는 이벤트리스너
    payViewMonthSelector.addEventListener('change', function(event){
        var selectedMonth = event.target.value;
        // 선택된 달을 불러와서, 해당하는 달에 대한 데이터를 작성
        monthSelectorChange(selectedMonth);
    });







