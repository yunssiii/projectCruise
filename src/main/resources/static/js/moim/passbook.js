
function sendIt(){
    console.log(bankClick.selectedBank);
    f = document.myForm;

    str = f.crewName.value;
    str = str.trim();
    if(!str){
        alert("\n모임통장 이름을 입력하세요.");
        f.crewName.focus();
        return;
    }
    f.crewName.value = str;

    str = f.crewPaymoney.value;
    str = str.trim();
    if(!str){
        alert("\n회비금액을 입력하세요.");
        f.crewPaymoney.focus();
        return;
    }
    f.crewPaymoney.value = str;

    str = f.crewPaydate.value;
    str = str.trim();
    if(!str){
        alert("\n납입일을 입력하세요.");
        f.crewPaydate.focus();
        return;
    }
    f.crewPaydate.value = str;

    let value = parseInt(f.crewPaydate.value, 10);
    if (isNaN(value) || value < 1 || value > 31) {
    alert("1~31 범위로 입력하세요.");
    f.crewPaydate.value = '';
    f.crewPaydate.focus();
     return;
    }

     f.crewPaydate.value = str;

    if(!f.consent.checked){
        alert("\n약관에 동의하세요");
        f.consent.focus();
        return;
    }

     // 전환 신청 완료 모달창 열기
    const modal = document.getElementById("modal")
    modal.style.display = "flex";

    // 모임 통장 이름, 계좌 번호를 완료 모달 창으로 전달
    var crewNameValue = document.getElementById("crewName").value;
    document.getElementById("crewNameTd").innerText = crewNameValue;

    var accountSelect = document.getElementById("my_account");
    var accountValue = accountSelect.options[accountSelect.selectedIndex].text;
    document.getElementById("accountTd").innerText = accountValue;

    var createdTdElement = document.getElementById("createdTd");
    var currentDate = new Date();
    var formattedDate = currentDate.toLocaleString();
    createdTdElement.textContent = formattedDate;
}

// 전환 신청 완료 모달창을 닫을 때 데이터 전달
const closeBtn = document.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    const modal = document.getElementById("modal")
    modal.style.display = "none"
    document.myForm.action = "/moim/passbook";
    document.myForm.submit();
});


let checkedNew = null;  // 새로운 계좌 선택 시에만 'checkedNew' 값 설정
//체크박스 선택시 활성화(기존계좌)
function myAccount(checkbox) {
    checkedNew = null;
    const textbox_elem = document.getElementById('my_account');
    textbox_elem.disabled = checkbox.checked ? false : true;

    if(textbox_elem.disabled) {
        textbox_elem.value = null;
    } else {
        textbox_elem.focus();
    }

    // 새로운 계좌 선택 체크박스 선택 시 기존계좌 체크 해제
    const newAccountCheckbox = document.getElementsByName('account_choice')[1];
    if (checkbox.checked) {
        newAccountCheckbox.checked = false;
        // 새로운 계좌 버튼도 비활성화
        document.getElementById('new_account_button').disabled = true;
    }
}

//체크박스 선택시 활성화(새로운 계좌 버튼)
function newAccount(checkbox) {
    checkedNew = 'checkedNew';
    const textbox_elem = document.getElementById('new_account_button');
    textbox_elem.disabled = checkbox.checked ? false : true;

    if(textbox_elem.disabled) {
        textbox_elem.value = null;
    } else {
        textbox_elem.focus();
    }

    const myAccountCheckbox = document.getElementsByName('account_choice')[0];
    if (checkbox.checked) {
        myAccountCheckbox.checked = false;
        // 기존계좌 입력 필드도 비활성화
        document.getElementById('my_account').disabled = true;
        // 새로운 계좌 선택 시 값 설정
        checkedNew = 'checkedNew';
    }
}


// 새로운 계좌 등록
function insertNewAccount() {
    var newAnum = $('[name=accForm]').serialize();  // 모달3 폼
    const modal3 = document.getElementsByClassName("modal-div3");   // 모달3 div
    var myNewAccElement = document.getElementById("myNewAcc");  // 계좌 추가 후 메시지 띄울 영역
    var inputAccount = document.getElementById("account-input1").value; // 입력한 계좌번호

    $.ajax({
        url: '/moim/passbook/new',
        type: 'post',
        data: {
            myaccount_anum: newAnum,
            checkedNew: checkedNew
            },
        success: function(data) {
            if(data === "insertNewAccount") {
                alert("새로운 계좌가 추가되었습니다.");
                myNewAccElement.innerText = inputAccount;   // 추가한 계좌번호 띄우기
            }else{
                alert("Error: 새로운 계좌 추가 에러");
            }
        },
        error: function(data) {
            console.log('계좌 추가: AJAX 요청 오류');
            console.log(data);
        }
    });
}