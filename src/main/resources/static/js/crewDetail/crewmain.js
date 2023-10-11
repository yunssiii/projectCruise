
// 알림창 -----------------------------------------------------------------

    // 알림창 상단 현재 날짜 불러오기
        let today = new Date(); // 현재 날짜와 시간을 가지는 객체를 리턴한다.

        let year = today.getFullYear(); // 연도
        let month = today.getMonth() + 1 ; // 월
        let date = today.getDate(); // 날짜
        let day = today.getDay(); // 요일
        let dayStr = "";

        switch (day) {

            case 0:
                dayStr = "일";
                break;

            case 1:
                dayStr = "월";
                break;
                
            case 2: 
                dayStr = "화";
                break;
                
            case 3:
                dayStr = "수";
                break;
                
            case 4:
                dayStr = "목";
                break;
                
            case 5:
                dayStr = "금";
                break;
                
            case 6:
                dayStr = "토";
                break;
                
        }

        var todayStr = year + "년 " + month + "월 " + date + "일 (" + dayStr + ") ";
        var todayDiv = document.getElementById('todayDiv');

        todayDiv.textContent = todayStr;





    // 알림창 호버

        function alertBarHover() {

            var aniMsgDiv = document.getElementById("animated_msg");
            var msgDiv = document.getElementById("msg");

            if (msgDiv.classList.contains("hidden")){
                msgDiv.classList.remove("hidden");
                msgDiv.classList.add("visible_detailBox");
                aniMsgDiv.classList.remove("visible_bar");
                aniMsgDiv.classList.add("hidden");
            }
        }

        function alertBarUnhover() {

            var aniMsgDiv = document.getElementById("animated_msg");
            var msgDiv = document.getElementById("msg");

            if (msgDiv.classList.contains("visible_detailBox")){
                msgDiv.classList.remove("visible_detailBox");
                msgDiv.classList.add("hidden");
                aniMsgDiv.classList.remove("hidden");
                aniMsgDiv.classList.add("visible_bar");
            }
        }




// 회비 납입하기  -----------------------------------------------------------------


    // 납입회차 조절 스크립트
    /*
    * TODO
    *  1. 내려가고 올라가는 버튼 누르면 숫자가 오르고 내리게 하기 V
    *  2. 0회 미만으로 내려가지 않게 설정하기. V
    *  3. 0회 일시 화살표를 회색으로 변경하기 V
    *  4. (의무 납입횟수) - (현재 납입횟수) 만큼을 num에 기본으로 띄우기 V
    *  5. num이 (의무 납입횟수) - (현재 납입횟수) 면 화살표 회색 변경 V
    *
    * */
    var up = document.getElementById('up');
    var down = document.getElementById('down');
    var num = document.getElementById('num');
    var mustpaycount = document.getElementById("mustpaycount");
    var realpaycount = document.getElementById("realpaycount");
    var availablepay = mustpaycount.textContent - realpaycount.textContent;

    var payMoney = document.getElementById('payMoney');


    num.value = availablepay + '';
    let numValue = num.value;

    if(parseInt(numValue)===0) {
        down.style.color = 'lightgray'
        down.style.cursor = 'not-allowed'
    }

    if(parseInt(numValue)===availablepay) {
        up.style.color = 'lightgray'
        up.style.cursor = 'not-allowed'
    }

    function clickUpArrow(mustPayMoney) {

        if(numValue>=availablepay) {
            up.style.color = 'lightgray'
        }else {
            numValue++;
            num.value = numValue;
            up.style.color = ''
            down.style.color = ''
            down.style.cursor = 'pointer'
            payMoney.textContent = String((mustPayMoney * num.value).toLocaleString("ko-KR"));
        }

        if(numValue===availablepay) {
            up.style.color = 'lightgray'
            up.style.cursor = 'not-allowed'
        }

    }

    function clickDownArrow(mustPayMoney) {
        if(numValue<=0) {
            numValue=0;
            // down.style.cursor = 'not-allowed'
        } else {
            numValue--;
            num.value = numValue;
            up.style.color = ''
            up.style.cursor = 'pointer'
            down.style.color = ''
            payMoney.textContent = String((mustPayMoney * num.value).toLocaleString("ko-KR"));
        }

        if(numValue===0) {
            down.style.color = 'lightgray'
            down.style.cursor = 'not-allowed'
        }
    }



    // 계좌 선택했을 때
    /* TODO [계좌선택]
     *  1. 선택한 은행의 값이 id="bank"로 들어가게 하기
     *  2. 선택한 계좌의 값이 id="account"로 들어가게 하기
     */

    var bankBlank = document.getElementById("bank");
    var accountBlank = document.getElementById("account");
    var selectValue = document.getElementById("selectAccount");

    function selectAccountOnChange() {

        // 0. select로 선택된 값 가져오기
        var selectValueStr = selectValue.value;

        // 1. 값을 은행과 계좌번호로 따로 나눠 분리하기
        var bankName = "";
        var accountNum = "";

            // 숫자의 아스키코드 값 0(48) ~ 9(57)을 이용해 계좌번호 분리하기
        for(var i=0; i<selectValueStr.length ; i++ ) {
            if(selectValueStr.charCodeAt(i)>=48 && selectValueStr.charCodeAt(i)<=57) {
                accountNum += selectValueStr.charAt(i);
            } else {
                bankName += selectValueStr.charAt(i);
            }
        }

        // 2. bankName 끝에 공백이 들어가있을 것이므로 공백 제거
        bankName = bankName.trim();

        // 3. 값 넣기
        bankBlank.textContent = bankName;
        accountBlank.textContent = accountNum;


    }

    /* TODO 납입버튼 눌렀을 때,
     *  1. 회차나 액수가 0이면 납입버튼을 눌러도 이동하지 못하게 하기
     *  2. 계좌가 지정되지 않았을 때 이동하지 못하게 하기
     */

    function payButtonClick() {

        if(num.value=='0' || num.value===0 || payMoney.value==='0' || payMoney.value===0) {
            alert("납입 횟수를 지정해주세요.");
            return;
        }

        if(selectValue.value==='--- 계좌 선택하기 ---') {
            alert("계좌를 지정해주세요.");
            return;
        }


        // FIXME 납입 처리하러 가는 링크 넣어야함

    }







// 회비내역 조회 -----------------------------------------------------------------
    /*
        TODO [토글 버튼 만들기]
         1. button의 값이 바뀌어야 한다.
            - 계좌내역 조회, 회비내역 조회
         2. id=crewname이 바뀌어야 한다.
            - 계좌내역 조회를 누르면  앙큼불여우단 계좌내역조회 로
            - 회비내역 조회를 누르면  앙큼불여우단 회비내역조회 로
         3. <div id="fee_history"> 와 <div id="account_history">이 서로 변경되어야 한다.
     */


    var toggleButton = document.getElementById("tap");
    var crewNameDiv = document.getElementById("crewname");
    var feeHistoryDiv = document.getElementById("fee_history");
    var accountHistoryDiv = document.getElementById("account_history");


    function toggleBtnOnClick(crewname) {

        if(toggleButton.textContent === "거래내역 조회") {
            // 1. 눌렀을 때 버튼은 회비내역 조회로 바뀌고
            // 2. crewname은 [크루이름] 계좌내역 조회 로 바뀌고
            // 3. feeHistoryDiv 이 보이지 않고
            // 4. accountHistory 가 보여야 한다.

            toggleButton.textContent = "회비내역 조회";
            crewNameDiv.textContent = crewname + " 거래내역 조회"
            feeHistoryDiv.style.display = 'none';
            feeHistoryDiv.style.height = 0;
            accountHistoryDiv.style.display = 'block';

        } else {
            // 1. 눌렀을 때 버튼은 거래내역 조회로 바뀌고
            // 2. crewname은 [크루이름] 회비내역 조회 로 바뀌고
            // 3. feeHistoryDiv 이 보이고
            // 4. accountHistory 가 보여지 않아야 한다.

            toggleButton.textContent = "거래내역 조회";
            crewNameDiv.textContent = crewname + " 회비내역 조회"
            feeHistoryDiv.style.display = '';
            feeHistoryDiv.style.height = '';
            accountHistoryDiv.style.display = 'block';
        }
    }

// 크루 탈퇴 -----------------------------------------------------------------

    /*
        TODO
         - '크루 탈퇴' 누르면 탈퇴 모달창 띄위지게 하기
         - '예' 누르면 choiceYN가 hidden, exitYesResult div가 보여지게
         - '아니오' 누르면 choiceYN가 hidden, exitNoResult div가 보여지게
         - exitYesResult 에서 확인 누르면 모달창 hidden 후 마이페이지로 이동
         - exitNoResult 에서 확인 누르면 모달창만 hidden
     */

    // 크루 탈퇴 누르면 모달창 보이게 하기
    function crewExitClick() {
        var exitModalDiv = document.getElementById("exitModal");
        var choiceYNDiv = document.getElementById("choiceYN");
        var exitNoResultDiv = document.getElementById("exitNoResult");
        var exitYesResultDiv = document.getElementById("exitYesResult");

        if(exitModalDiv.classList.contains("exitHidden")){
            exitModalDiv.classList.remove("exitHidden");
            exitModalDiv.classList.add("exitVisible");
        }

        // 크루탈퇴 처음 실행 시
        // choiceYNDiv에서 '아니오'를 눌렀다가 다시 크루탈퇴를 하려 할 때,
        // choiceYNDiv가 뜨지 않고 exitNoResultDiv 또는 exitYesResultDiv 가 뜨는 현상을
        // 막기 위함
        if(choiceYNDiv.classList.contains("exitHidden")) {
            choiceYNDiv.classList.remove("exitHidden");
            choiceYNDiv.classList.add("exitVisible");
        }


        if(exitNoResultDiv.classList.contains("exitVisible")){
            exitNoResultDiv.classList.remove("exitVisible");
            exitNoResultDiv.classList.add("exitHidden");
        }

        if(exitYesResultDiv.classList.contains("exitVisible")){
            exitYesResultDiv.classList.remove("exitVisible");
            exitYesResultDiv.classList.add("exitHidden");
        }

    }

    function crewExitCloseXClick() {
        var exitModalDiv = document.getElementById("exitModal");

        if(exitModalDiv.classList.contains("exitVisible")){
            exitModalDiv.classList.remove("exitVisible");
            exitModalDiv.classList.add("exitHidden");
        }
    }

    // 크루 탈퇴 모달에서 "예" 누르면 exitYesResult 보이게 하기
    function exitYesClick() {
        // FIXME 탈퇴처리하기
        exitCompleteModal();
    }

    function exitCompleteModal() {
        var exitYesResultDiv = document.getElementById("exitYesResult");
        var choiceYNDiv = document.getElementById("choiceYN");

        if(choiceYNDiv.classList.contains("exitVisible")){
            choiceYNDiv.classList.remove("exitVisible");
            choiceYNDiv.classList.add("exitHidden");
            exitYesResultDiv.classList.remove("exitHidden");
            exitYesResultDiv.classList.add("exitVisible");
        }
    }
    

    function exitYesOkClick() {

        var exitModalDiv = document.getElementById("exitModal");

        exitModalDiv.classList.remove("exitVisible");
        exitModalDiv.classList.add("exitHidden");
       
        /*
            FIXME
             위는 임시방편 자바스크립트이므로,
             위 스크립트를 지우고 마이페이지로 돌아가게 하기
         */
    
    }
    

    // 크루 탈퇴 모달에서 "아니오" 누르면 exitNoResult 보이게하기
    function exitNoClick() {

        var exitNoResultDiv = document.getElementById("exitNoResult");
        var choiceYNDiv = document.getElementById("choiceYN");
        var exitNoOKBtn = document.getElementById("exitNoOKBtn");

        if(choiceYNDiv.classList.contains("exitVisible")){
            choiceYNDiv.classList.remove("exitVisible");
            choiceYNDiv.classList.add("exitHidden");
            exitNoResultDiv.classList.remove("exitHidden");
            exitNoResultDiv.classList.add("exitVisible");
        }
    }

    function exitNoOkClick() {
        var exitModalDiv = document.getElementById("exitModal");

        exitModalDiv.classList.remove("exitVisible");
        exitModalDiv.classList.add("exitHidden");

    }




