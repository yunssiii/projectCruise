

var memberPayViewBoxDiv = document.getElementById("memberPayViewBox");
var payViewMonthSelector = document.querySelector('select[name="payViewMonth"]');
var payTotalDiv = document.querySelector('.payTotal');

    // 월별회비조회 - 카카오톡/알림보내기 기능 부분
var defaultViewContainer = document.getElementById('defaultViewContainer');
var kakaoTalkContainer = document.getElementById('kakaoTalkContainer');
var alertContainer = document.getElementById('alertContainer');

var msgReceiverDiv = document.getElementById('msgReceiver');
    // 월별회비조회 - 미납자의 이름을 클릭했을 때 아래에 나타나는 이름
var talkReceiverNameDiv = document.getElementById('talkReceiverName');
var alertReceiverNameDiv = document.getElementById('alertReceiverName');
    // 월별회비조회 - 오른쪽에 카카오톡을 받을 자

var crewNum = $('#crewNumHidden').val();
var crewAccount = $('#crewAccountHidden').val();


// 임시 데이터
    var memNames = [];

    //멤버 이름 데이터 구하기 function
    var forEachState = 0;
    function setMemNames(memberList) {
        // memberList = memberList.json();
        memberList.forEach(function (member) {
            memNames.push(member.MEM_NAME)
        })
    }

    var transferContents = [];
    var memberPayMoney = [];

    function setMemPayMoney(memberList, fullDates) {

        // 거래내용 세팅
        // crewNum+userNum+"_"+userName
        memberList.forEach(function (member){
            var memberName = member.MEM_NAME;
            var memberNum = member.MEM_NUM;
            transferContents.push(crewNum+memberNum+"_"+memberName);
        })

        console.log(transferContents);

        for(var i=0;i<fullDates.length;i++) {

            memberPayMoney[i] = [];

            var startDate
                = new Date(fullDates[i].getFullYear(),fullDates[i].getMonth(), 1);
            var endDate
                = new Date(fullDates[i].getFullYear(),fullDates[i].getMonth() +1, 0);

            var startMonth = startDate.getMonth() +1 ;
            var endMonth = endDate.getMonth() +1 ;
            if(startMonth<10) {
                startMonth = '0'+startMonth;
            }
            if(endMonth<10) {
                endMonth = '0'+endMonth;
            }

            var startDateStr
                = startDate.getFullYear() + "-" + startMonth  + "-01 00:00:00";
            var endDateStr
                = endDate.getFullYear() + "-" + endMonth  + "-" + endDate.getDate() + " 23:59:59" ;

            console.log(startDateStr);
            console.log(endDateStr);

            for(var j=0;j<memberList.length;j++) {

                let thisMemName = memberList[j].MEM_NAME;
                let thisMemEmail = memberList[j].MEM_EMAIL.split("@")[0];
                let transferContent = transferContents[j];
                console.log(thisMemName + " / " + transferContent);
                console.log(crewAccount);

                var inquieyReq = $.ajax({
                    url: "/develop/openbank/using/search",
                    method: "POST",
                    data: {
                        searchType:4,
                        selectedAccount:crewAccount+'',
                        content:transferContent,
                        startDate:startDateStr,
                        endDate:endDateStr
                    }
                })

                let payData = {};

                inquieyReq.done(function(result){
                     payData.memName = thisMemName;
                     payData.memEmail = thisMemEmail;
                     payData.payMoney = result[0].inMoney;
                })

                memberPayMoney[i].push(payData);
            }
        }
        console.log(memberPayMoney);
    }

    // up 버튼을 눌렀을 때 월별회비조회 부분 초기화하기
    // 첫 번째 option과 그에 해당하는 데이터가 나오도록!
    var upButton = document.getElementById('up');
    upButton.addEventListener('click', function() {
        payViewMonthSelector.selectedIndex = 0;
        monthSelectorChange(payViewMonthSelector.selectedIndex.value);
        settingDefaultDiv();

    })

    // 월별회비조회 란을 디폴트로 초기화하는 함수
    function settingDefaultDiv() {
        defaultViewContainer.classList.remove('hiddenBox');
        alertContainer.classList.remove('visibleRight');
        kakaoTalkContainer.classList.remove('visibleRight');
        alertContainer.classList.add('hiddenBox');
        kakaoTalkContainer.classList.add('hiddenBox');
    }



    // select된 달에 대한 리스트를 html로 써주는 함수
    function monthSelectorChange(selectedOption) {

        // 달이 바뀔 때 하단 Total창이 보이도록 하기
        // 미납자를 선택하고 나서 달을 바꾸면 total창으로 되돌아갈 수 없는 문제를 방지하기 위함
        document.querySelector('.sendMsgContainer').classList.add('hiddenContainer')
        document.getElementById('nonSelected').classList.remove('hiddenContainer');
        document.getElementById('nonSelected').classList.add('nonSelected');


        var selectedMonthList = memberPayMoney[selectedOption];

        //memNames 배열에서 selectedMonthList 배열에 없는 값을 찾아내기
            // 거래내역이 없으면 검색이 안 됐을것이기 때문에!


        // 리스트 html 쓰기
        var html = "";
        var payTotal = 0;



        for(var i=0;i<selectedMonthList.length;i++) {

            if (selectedMonthList[i].payMoney===0) {
                html +=
                    "<div class='highlighter'> <div class='oneLineContainer redName'>" +
                    "<div class='payMemName'>"
                            + selectedMonthList[i].memName
                            + " (" + selectedMonthList[i].memEmail + ") </div>" +
                    "<div id='payMemName' style='display: none;'>" +selectedMonthList[i].memName + "</div>" +
                    "<div class='payMoney'> 0원 </div>" +
                    "</div></div>";
            } else {
                html +=
                    "<div class='oneLineContainer'>" +
                    "<div class='payMemName'>" + selectedMonthList[i].memName + " (" + selectedMonthList[i].memEmail + ") </div>" +
                    "<div class='payMoney'>" + selectedMonthList[i].payMoney.toLocaleString('ko-KR') + "원 </div>" +
                    "</div>";
            }

        }
        //.toLocaleString('ko-KR') = 숫자 쉼표표시

        memberPayViewBoxDiv.innerHTML = html;

        // 총액 html 쓰기
        for(var i=0;i<selectedMonthList.length;i++) {
            payTotal += selectedMonthList[i].payMoney;
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

                var notPayName = member.querySelector('#payMemName').textContent;
                msgReceiverDiv.textContent = notPayName;
                // 미납자의 이름을 가져와서 msgReceiver에 기입한다.

            });
        });


    }

    // memberPayMoneyView DIV에 기본적으로 불러올 데이터 = 현재 월에 해당하는 데이터
    // 이 코드를 써놓지 않으면 페이지 처음 로드 시 아무 데이터도 불러와지지 않음.
     // --> crewSetting.js에서 crewMemberSetting을 눌렀을 때의 이벤트 함수에 monthSelectorChange(0); 을 추가해주었다!


    //selector의 change를 감지하는 이벤트리스너
    $('#payViewMonth').on('change',function() {
        var selectedOption = $(this).val();
        console.log(selectedOption)
        monthSelectorChange(selectedOption);
        settingDefaultDiv();
    })


    function sendSettingBtnClick(btn) {
        if(btn==='kakao') {
            defaultViewContainer.classList.add('hiddenBox');
            kakaoTalkContainer.classList.add('visibleRight');
            alertContainer.classList.remove('visibleRight');
            alertContainer.classList.add('hiddenBox');

        } else {
            defaultViewContainer.classList.add('hiddenBox');
            alertContainer.classList.add('visibleRight');
            kakaoTalkContainer.classList.remove('visibleRight');
            kakaoTalkContainer.classList.add('hiddenBox');
        }

        talkReceiverNameDiv.textContent = msgReceiverDiv.textContent;
        alertReceiverNameDiv.textContent = msgReceiverDiv.textContent;

    }


// TODO 카카오톡 보내기
var kakaoTalkSendBtn = document.getElementById('kakaoTalkSend');

    kakaoTalkSendBtn.addEventListener('click',function () {
        alert('카카오톡보내기 API를 실행합니다.')
    })


// TODO 알림 보내기
var alertSendBtn = document.getElementById('alertSend');

    alertSendBtn.addEventListener('click',function () {
        alert('알림 보내기 백엔드단으로 보냅니다.')
    })




