
/* red 크루 정보수정 */

var crewInfoForm = document.crewInfoForm;

// green 입력 검사
    // bold 크루 소개를 50자 이내로 제한하기
    // 바이트 수 계산 함수
    function countBytes(str) {
        let count = 0;
        for (let i = 0; i < str.length; i++) {
            if (str.charCodeAt(i)<=127){ // 아스키코드 0~127 까지는 1byte로 계산
                count += 1;
            } else { // 나머지는 2byte로 계산
                count += 2;
            }
        }
        return count;
    }

    var countCrewInfoText = countBytes($('#crewInfo').val());
    $('#countCrewInfoText').text('* 50자 까지만 작성 가능합니다. (' + countCrewInfoText + '/50byte)')
    $('#crewInfo').on('input',function(){
        countCrewInfoText = countBytes($('#crewInfo').val());
        $('#countCrewInfoText').text('* 50자 까지만 작성 가능합니다. (' + countCrewInfoText + '/50byte)')

        if(countCrewInfoText > 50) {
            $('#countCrewInfoText').css({'color':'red', 'font-weight':'bold'});
        } else {
            $('#countCrewInfoText').css({'color':'', 'font-weight':''});
        }
    })

    // bold 납입일자에 1~31 까지만 들어오도록 하기
    $('#payDate').on('change',function(){
        var value = $('#payDate').val();
        if(value<1) {
            $('#payMoneyWarn').text('* 납입일자는 1 ~ 31일까지만 설정 가능합니다.');
            $('#payDate').val(1);
        } else if (value>31) {
            $('#payMoneyWarn').text('* 납입일자는 1 ~ 31일까지만 설정 가능합니다.');
            $('#payDate').val(31);
        } else {
            $('#payMoneyWarn').text('');
        }
    })

    // bold 납입액수에 숫자만 들어오도록 하기
    $('#payMoney').on('input',function(){
        var value = $('#payMoney').val(); // 입력된 value를 불러오기

        for(var i = 0; i<value.length; i++) {
            if(!(value.charCodeAt(i)>=48&&value.charCodeAt(i)<=57)){
                $('#payMoneyWarn').text('* 납입 액수에는 숫자만 입력가능합니다.');
                break; // 반복문 빠져나오기
            } else {
                $('#payMoneyWarn').text('');
            }

            if(value<1000) {
                $('#payMoneyWarn').text('* 납입액은 최소 1000원 이상이어야 합니다.');
            }
        }

    })

    // bold 목표금액에 숫자만 들어오게 하기
    $('#goalMoney').on('input',function(){
        var value = $('#goalMoney').val(); // 입력된 value를 불러오기

        for(var i = 0; i<value.length; i++) {
            if(!(value.charCodeAt(i)>=48&&value.charCodeAt(i)<=57)){
                $('#goalMoneyWarn').text('* 목표 금액에는 숫자만 입력가능합니다.');
                break; // 반복문 빠져나오기
            } else {
                $('#goalMoneyWarn').text('');
            }

            if(value<1000) {
                $('#goalMoneyWarn').text('* 목표액은 최소 1000원 이상이어야 합니다.');
            }
        }
    })

function crewUpdateBtnClick(crewNum) {
    // input 값 검사하기

    // 0. 크루 소개는 50자까지
    var crewInfo = crewInfoForm.crew_info;
    if(countBytes(crewInfo.value)>50) {
        alert("크루 소개는 50자까지만 입력해주세요.");
        crewInfo.focus()
        return;
    }

    // 1. 납입일자는 1~31일 까지
    var payDate = crewInfoForm.crew_paydate;
    if(payDate.value<1 || payDate.value>31) {
        alert("납입일자는 1 ~ 31일까지만 설정 가능합니다.");
        payDate.focus();
        return;
    }

    // 2. 납입액수는 숫자만
    var payMoney = crewInfoForm.crew_paymoney;
    for(var i = 0; i<payMoney.value.length; i++) {
        if (!(payMoney.value.charCodeAt(i) >= 48 && payMoney.value.charCodeAt(i) <= 57)) {
            alert("납입 액수는 숫자만 입력가능합니다.");
            payMoney.focus();
            return; // 반복문 빠져나오기
        }
    }

    if(payMoney.value<1000) {
        alert("납입 액수는 최소 1000원 이상이여야 합니다.");
        payMoney.focus();
        return; // 반복문 빠져나오기
    }

    // 3. 목표금액도 숫자만
    var goalMoney = crewInfoForm.crew_goal;
    for(var i = 0; i<goalMoney.value.length; i++) {
        if (!(goalMoney.value.charCodeAt(i) >= 48 && goalMoney.value.charCodeAt(i) <= 57)) {
            alert("목표 금액에는 숫자만 입력가능합니다.");
            goalMoney.focus();
            return; // 반복문 빠져나오기
        }
    }

    if(goalMoney.value<1000) {
        alert("목표 금액은 최소 1000원 이상이여야 합니다.");
        goalMoney.focus();
        return; // 반복문 빠져나오기
    }
    // FIXME submit 처리하기
    $(function () {
        var request = $.ajax({
            url: "/crew/setting/updateCrewInfo",
            method: "POST",
            data: {
                crewNum: crewNum,
                crewInfo: crewInfo.value,
                payDate: payDate.value,
                payMoney: payMoney.value,
                goalMoney: goalMoney.value
            }
        })

        // jsonArray로 받아와서 크루정보수정에 뿌려주기
        request.done(function(resp){
            $('#crewInfo').val(resp.newCrewInfo);
            $('#payDate').val(resp.newPayDate);
            $('#payMoney').val(resp.newPayMoney);
            $('#goalMoney').val(resp.newGoalMoney);
        });
    })



    alert("수정 완료!")
}