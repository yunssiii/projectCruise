var modalBack = document.getElementById('modalBackground');
// // 유예시간 실시간으로 보여주기 위한 스크립트
var deleteRequestDateDiv = document.getElementById('deleteRequestDate');
var deleteCompleteDDayDiv = document.getElementById('deleteCompleteDDay');

var crewNum = $('#crewNum').text();

// 모달 숨기기, 보이기 함수
function hiddenModalFunction(modalDOM) {
    modalDOM.classList.remove('visibleModal');
    modalDOM.classList.add('hiddenModal');
}

function visibleModalFunction(modalDOM) {
    modalDOM.classList.remove('hiddenModal');
    modalDOM.classList.add('visibleModal');
}

function modalWindowClose() {
    hiddenModalFunction(modalBack);
}

function deleteDateStrMaker(DBdeleteRequestDate) {

    if(DBdeleteRequestDate==null) {
        return;
    }

    var DBdeleteRequestDateRep = DBdeleteRequestDate.replace(/ /g,'T');
    var deleteReqFullDate = new Date(DBdeleteRequestDateRep);

    var delReqYear = deleteReqFullDate.getFullYear();
    var delReqMonth = deleteReqFullDate.getMonth()+1;
    var delReqDate = deleteReqFullDate.getDate();


    var deleteRequestDateStr
        = delReqYear + "년 "
        + delReqMonth + "월 "
        + delReqDate + "일 ";
    deleteRequestDateDiv.textContent = deleteRequestDateStr;



    var deleteFullDate = deleteReqFullDate.setDate(deleteReqFullDate.getDate()+3)
    var today = new Date();


    var remaining = deleteFullDate - today;
    var remainingSeconds = Math.floor((remaining / 1000) % 60);
    var remainingMinutes = Math.floor((remaining / (1000 * 60)) % 60);
    var remainingHours = Math.floor((remaining / (1000 * 60 * 60)) % 24);
    var remainingDays = Math.floor(remaining / (1000 * 60 * 60 * 24));

    remainingSeconds = (remainingSeconds<10) ? "0" + remainingSeconds : remainingSeconds;
    remainingMinutes = (remainingMinutes<10) ? "0" + remainingMinutes : remainingMinutes;
    remainingHours = (remainingHours<10) ? "0" + remainingHours : remainingHours;

    deleteCompleteDDayDiv.textContent
        = remainingDays + "일 " + remainingHours + "시간 " + remainingMinutes + "분 " + remainingSeconds + "초";


}

// 항해 중단 취소를 눌렀을 때

var crewDeleteCancelModal = document.getElementById("crewDeleteCancelModal")
function cancelDeleteCrewBtnClick() {
    cancelWarningTitle.textContent = "항해를 다시 시작할까요?";
    turnBackCrewBtn.classList.add('hidden');
    cancelDeleteBtn.classList.remove('hidden');
    turnBackBtn.classList.remove('hidden');

    visibleModalFunction(crewDeleteCancelModal);
    visibleModalFunction(modalBack);
}

// 항해 다시 시작 버튼을 눌렀을 때
var cancelWarningTitle = document.getElementById('cancelWarningTitle');
var turnBackCrewBtn = document.getElementById('turnBackCrewBtn');
var turnBackBtn = document.getElementById('turnBackBtn');
var cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
function cancelDeleteBtnClick() {

    cancelWarningTitle.textContent = "항해를 다시 시작합니다.";
    turnBackCrewBtn.classList.remove('hidden');
    cancelDeleteBtn.classList.add('hidden');
    turnBackBtn.classList.add('hidden');
}


// 항해를 다시 시작합니다 - 확인 버튼을 눌렀을 때
function turnBackBtnClick() {

    modalWindowClose(); // 모달 끄고

    window.location.href = '/crew/setting/cancelSailingStop?crewNum=' + crewNum;


}