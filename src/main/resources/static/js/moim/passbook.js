
let checkedBox = 'checkedOld'; // 기존 계좌 or 새 계좌 체크 박스
var addBtn = document.getElementsByClassName("addBtn"); // 모달3 등록 버튼
var bankButtons = document.querySelectorAll(".bankBtn"); // 모달2에서 선택한 은행
var selectedBank; // 모달2에서 선택한 은행명
var accountNumber; // 모달3에서 입력한 계좌번호
var modalDiv3 = document.getElementsByClassName("modal-div3");

function sendIt(){
    f = document.myForm;

    var accountChoiceElements = document.getElementsByName("account_choice");
    var checked = false;

        for (var i = 0; i < accountChoiceElements.length; i++) {
            if (accountChoiceElements[i].checked) {
                checked = true;
                break;
            }
        }

        if (!checked) {
            alert("계좌를 선택하세요.");
            return;
        }
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

    // 목표 금액(crew_goal)이 빈 문자열일때 null로 변경
    var crewGoalInput = document.getElementById("crewGoal").value;
    if (crewGoalInput === '') {
        crewGoalInput = null;
    }

    // 서버로 보낼 데이터를 HTML 폼에 추가----------------------
    // 선택한 체크 박스의 값을 hidden으로 넘겨줌(기존 계좌 or 새 계좌)
    const hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.name = "checkedBox";
    hiddenField.value = checkedBox;
    document.myForm.appendChild(hiddenField);

    /* 새 계좌인 경우
        모달2에서 선택한 은행명,
        모달3에서 작성한 계좌번호를 hidden으로 넘겨줌 */
    if(checkedBox === 'checkedNew') {
        const hiddenFieldBank = document.createElement("input");
        hiddenFieldBank.type = "hidden";
        hiddenFieldBank.name = "selectedBank";
        hiddenFieldBank.value = selectedBank;
        document.myForm.appendChild(hiddenFieldBank);

        crew_accountidValue = document.getElementById("account-input1").value;
        const hiddenField2 = document.createElement("input");
        hiddenField2.type = "hidden";
        hiddenField2.name = "crew_accountid";
        hiddenField2.value = crew_accountidValue;
        document.myForm.appendChild(hiddenField2);
    }
    // ----------------------서버로 보낼 데이터를 HTML 폼에 추가

    // 폼 데이터를 객체로 가져옴
    var formData = new FormData(f);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/moim/passbook');
    xhr.onload = function() {
      if (xhr.status === 200) {
      var response = xhr.responseText;
      var responseData = JSON.parse(response);

      var group = responseData.group;
      var num = responseData.num;

    document.getElementById("sessionGroup").innerText = group;
    document.getElementById("sessionNum").innerText = num;
    console.log(group);
    console.log(num);
        showModal();
      } else {
        console.log("passbook 전환 완료 실패");
      }
    };

    // 데이터를 전송
    xhr.send(formData);


    // 모임통장 전환 완료 모달--------------------------------------------
    // 모임통장 이름 전달
    var crewNameValue = document.getElementById("crewName").value;
    document.getElementById("crewNameTd").innerText = crewNameValue;

    // 은행명, 계좌번호 전달
    if (checkedBox === 'checkedOld') {  // 기존 계좌 선택한 경우
        var accountSelect = document.getElementById("my_account");
        var accountValue = accountSelect.options[accountSelect.selectedIndex].text;
        document.getElementById("accountTd").innerText = accountValue;
    } else {    // 새로운 계좌 선택한 경우
        document.getElementById("accountTd").innerText = selectedBank + " " + accountNumber;
        console.log("입력한 계좌번호: " + accountNumber);
    }

    // 가입일(현재 시각) 전달
    var createdTdElement = document.getElementById("createdTd");
    var currentDate = new Date();
    var formattedDate = currentDate.toLocaleString();
    createdTdElement.textContent = formattedDate;
    // --------------------------------------------모임통장 전환 완료 모달
}

// 전환 신청 완료 모달창 열기
function showModal() {
    const modal = document.getElementById("modal");
    modal.style.display = "flex";
}

// 전환 신청 완료 모달창 닫기
const closeBtn = document.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    const modal = document.getElementById("modal")
    modal.style.display = "none"

    window.location.href = '/mypage/mypage_all';
});

//체크박스 선택시 활성화(기존계좌)
function myAccount(checkbox) {
    checkedBox = 'checkedOld';
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
    checkedBox = 'checkedNew';
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
    }
}

// 등록 버튼을 클릭했을 때 insertNewAccount() 함수 실행
addBtn[0].addEventListener("click", insertNewAccount);

// 새로운 계좌 등록
function insertNewAccount() {
    // 한 번 실행한 후 이벤트 리스너 제거
    // 인증 실패한 경우 insertNewAccount() 함수가 두 번 호출되어 이를 방지
    addBtn[0].removeEventListener("click", insertNewAccount);

    // 모달3 폼에 있는 값에서 myaccount_anum 추출
    var newAnum = $('[name=accForm]').serialize(); // 예) crew_accountid=01012341234
    var parts = newAnum.split('='); // 숫자만 추출하기 위해 '='를 기준으로 분리
    accountNumber = parts[1];

    var myNewAccElement = document.getElementById("myNewAcc");  // 계좌 추가 후 메시지 띄울 영역

    $.ajax({
        url: '/moim/passbook/new',
        type: 'post',
        data: {
            myaccount_anum: accountNumber,
            checkedBox: checkedBox
            },
        success: function(data) {
            if(data === "insertNewAccount") {
                alert("새로운 계좌가 추가되었습니다.");
                modalDiv3[0].style.display = "none"; //모달3 닫기
                document.body.style.overflow = "auto";
                document.body.style.overflowX = "hidden";
                myNewAccElement.innerText = selectedBank + " " + accountNumber;   // 추가한 계좌번호 띄우기
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

// 모달2에서 선택한 은행명 가져오기 위한 함수
function selectBank(event) {

    bankButtons.forEach((e) => {
        e.classList.remove("click");
    });

    event.target.classList.add("click");

    selectedBank = event.target.innerText;
    console.log("선택한 은행: " + selectedBank);
}
bankButtons.forEach((e) => {
    e.addEventListener("click", selectBank);
});
