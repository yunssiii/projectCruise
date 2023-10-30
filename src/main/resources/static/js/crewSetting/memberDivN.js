
// 모달 숨기기, 보이기 함수
    function hiddenModalFunction(modalDOM) {
        modalDOM.classList.remove('visibleModal');
        modalDOM.classList.add('hiddenModal');
    }

    function visibleModalFunction(modalDOM) {
        modalDOM.classList.remove('hiddenModal');
        modalDOM.classList.add('visibleModal');
    }


// 모달창 띄우기

    var divideNBtn = document.getElementById('divideNBtn');
        // 잔액 1/N 하기 - 1/N하기 버튼
    var divideModalBg = document.getElementById('divideModalBg');
        // 모달 배경
    var divideConfirmModal = document.getElementById('divideConfirmModal');
        // 확인창(확인 모달)
    var divideCompleteModal = document.getElementById('divideCompleteModal');
        // 나누기 완료 모달

    var memberListSize = $('#memberListSize').text(); // memberlist의 사이즈
    var crewNum = $('#crewNumHidden').val(); // 크루 번호
    var crewAccount = $('#crewAccountHidden').val(); // 크루 계좌
    var crewName = $('#crewNameHidden').val(); // 크루 이름

    // 잔액 나누기 모달 내 버튼
    var divideYesBtn = document.getElementById('divideYes');
    var divideNo = document.getElementById('divideNo');

    // 잔액 1/N 하기 - 1/N하기 버튼 눌렀을 때
    divideNBtn.addEventListener('click',function (){

        var accountSelectedBoolean = false;
        for(var i=1;i<=memberListSize;i++) {
            // 폼에서 선택한 값들을 모달 내에 넣기
            $('#divMemNameOK'+i).text('('+ $('#divMemName'+i).text() + ')')
            $('#dividingAccountOK'+i).text($('#dividingAccount'+i).val())
            $('#amountPerPaymentOK'+i).text($('#amountPerPayment'+i).text())

            if($('#dividingAccountOK'+i).text()==="") {
                accountSelectedBoolean = true;
            }
        }

        if(accountSelectedBoolean) {
            alert("선원들의 계좌를 모두 선택해주세요.");
            return;
        }

        visibleModalFunction(divideModalBg); // 클릭하면 모달창이 뜨도록
        visibleModalFunction(divideConfirmModal); // 클릭하면 모달창이 뜨도록


    })

    // 모달 - 네를 눌렀을 때
    divideYesBtn.addEventListener('click',function() {

        var transferDateObj;
        var transferY; var transferM; var transferD; var transferH; var transferMM; var transferS;
        var transferDateStr;

        for(var i=1;i<=memberListSize;i++) {

            // 입금 계좌
            var depositAccountStr = $('#dividingAccount'+i).val();
            var depositAccount = "";

            for(var j=0; j<depositAccountStr.length; j++) {
                if(depositAccountStr.charCodeAt(j)>=48 && depositAccountStr.charCodeAt(j)<=57) {
                    depositAccount += depositAccountStr.charAt(j);
                }
            }

            depositAccount = depositAccount.trim();

            // 입금액
            var amountPerPayment = $('#amountPerPaymentHidden'+i).val();
            console.log(amountPerPayment);

            // 입금 날짜
            transferDateObj = new Date();

            transferY = transferDateObj.getFullYear();
            transferM = transferDateObj.getMonth() +1;
            transferD = transferDateObj.getDate();
            transferH = transferDateObj.getHours();
            transferMM = transferDateObj.getMinutes();
            transferS = transferDateObj.getSeconds();

            transferDateStr
                = transferY + "-" + transferM  + "-" +  transferD  + " " +
                transferH + ":" + transferMM + ":" + transferS

            // ajax 전송
            var transperReq = $.ajax({
                url: "/develop/openbank/using/transfer",
                method: "POST",
                data: {
                    withdrawAccount:crewAccount,
                    depositAccount:depositAccount,
                    transferDate:transferDateStr,
                    transferMoney:amountPerPayment,
                    transferContent:crewNum+crewName.substr(0,2)+"_잔액" // 크루이름 2글자만 잘라서 넣음
                }
            })

            console.log(crewAccount + '/' + depositAccount + '/' + transferDateStr + '/' + amountPerPayment);

            transperReq.done(function (result) {
                if(result==="NODATA"){
                    alert("납입 실패 - 데이터가 입력되지 않았습니다.");
                    return;
                } else if(result==="LACKOFBALANCE"){
                    alert("납입 실패 - 출금 계좌의 잔액이 부족합니다.");
                    return;
                }
            })
        }

        // 완료 후 모달 닫기
        hiddenModalFunction(divideConfirmModal);
        visibleModalFunction(divideCompleteModal);
    })

    // 모달 - 아니오를 눌렀을 때
    divideNo.addEventListener('click',function() {
        hiddenModalFunction(divideConfirmModal);
        hiddenModalFunction(divideModalBg);
    })

    // 잔액나누기 완료 후
    var divideOKBtn = document.getElementById('divideOKBtn');
        // 확인 버튼

    divideOKBtn.addEventListener('click',function (){
        hiddenModalFunction(divideCompleteModal);
        hiddenModalFunction(divideModalBg);

        window.location.href = '/crew/setting?crewNum=' + crewNum;
    })







