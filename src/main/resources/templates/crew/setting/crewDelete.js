
// 모달 숨기기, 보이기 함수
function hiddenModalFunction(modalDOM) {
    modalDOM.classList.remove('visibleModal');
    modalDOM.classList.add('hiddenModal');
}

function visibleModalFunction(modalDOM) {
    modalDOM.classList.remove('hiddenModal');
    modalDOM.classList.add('visibleModal');
}




// 크루 삭제할지말지 결정하는 창 ========================================================================


var modalBack = document.getElementById('modalBackground');

// 항해 중단하기를 눌렀을 때

    var deleteYNModal = document.getElementById('deleteYNModal')

    function deleteBtnClick() {
        visibleModalFunction(modalBack);
        visibleModalFunction(deleteYNModal);
    }



// 항해 정말로 그만하기를 눌렀을 때
    var cannotDeleteModal = document.getElementById('cannotDeleteModal');

    var deleteWarningTitle = document.getElementById('deleteWarningTitle');
    var deleteWarningMsg = document.getElementById('deleteWarningMsg');

    var memberDivNBtn = document.getElementById('memberDivNBtn');
    var gotoMemberBanBtn = document.getElementById('gotoMemberBanBtn');
    function deleteYesBtnClick(accountBalance, crewMemNum) {
                        // 계좌 잔액        크루 선원 수

        /*
            TODO
             0. deleteYNModal 닫기
             1. 크루 계좌의 잔액이 0인지 검사하기
              - if문으로 검사한 후 0이면 넘어가고, 아니면 return후 관련 Modal 창 띄우기
             2. 크루 선원의 인원이 선장 제외 0명인지 검사하기
              - if문으로 검사한 후 0명이면 넘어가고, 아니면 return후 관련 Modal창 띄우기
             3. if문을 다 넘긴 후에는 유예기간 3일
         */

        hiddenModalFunction(deleteYNModal);
        hiddenModalFunction(modalBack);

        if(accountBalance!==0) {

            visibleModalFunction(cannotDeleteModal);
            visibleModalFunction(modalBack);

            deleteWarningTitle.textContent = "대표계좌에 잔액이 남아있어요.";
            deleteWarningMsg.textContent = "대표계좌의 잔액을 0원으로 만들어주세요."

            gotoMemberBanBtn.classList.add('hidden');
            memberDivNBtn.classList.remove('hidden');

            return;
        }

        if((crewMemNum-1)!==0) {

            visibleModalFunction(cannotDeleteModal);
            visibleModalFunction(modalBack);

            deleteWarningTitle.textContent = "크루에 아직 선원이 남아있어요.";
            deleteWarningMsg.textContent = "크루의 선원들을 내보내주세요."

            memberDivNBtn.classList.add('hidden');
            gotoMemberBanBtn.classList.remove('hidden');
            return;
        }

        alert("삭제 처리하는 자바스크립트 넣기"); // FIXME 삭제 처리하는 링크로 이동

        var crewDeleteYNDiv = document.getElementById('crewDeleteYNDiv');
        var crewDeleteCompleteDiv = document.getElementById('crewDeleteComplete');


        hiddenModalFunction(crewDeleteYNDiv);
        visibleModalFunction(crewDeleteCompleteDiv);


    }


    function modalWindowClose() {
        hiddenModalFunction(modalBack);
    }


// 선원 강퇴하기 버튼을 눌렀을 때 '선원 강퇴' 메뉴로 이동하게 하기
    function gotoMemberBanBtnClick() {

        var crewBoxDiv = document.getElementById('crewSettingBox');
        var crewTapDiv = document.getElementById('crewTap');
        var memberBoxDiv = document.getElementById('memberSettingBox');
        var memberTapDiv = document.getElementById('memberTap');
        var memberBanTap = memberTapDiv.querySelectorAll('div');

        // 1. 모달 창 꺼주고
        modalWindowClose();
        hiddenModalFunction(cannotDeleteModal);
        hiddenModalFunction(modalBack);

        // 2. 선원관리 Div 보여주고
        memberBoxDiv.classList.remove('hidden');
        memberBoxDiv.classList.add('visibleBox');
        memberTapDiv.classList.remove('hidden');
        memberTapDiv.classList.add('tapVisible');

        crewBoxDiv.classList.remove('visibleBox');
        crewBoxDiv.classList.add('hidden');
        crewTapDiv.classList.remove('tapVisible');
        crewTapDiv.classList.add('hidden');

        // 3. 선원관리 - 선원관리하기 탭에 select 클래스 적용하고
            // 선원관리하기 탭이 div 4개중 3번째에 해당하므로 [2]에 해당함
        memberBanTap[1].classList.add('select');

        // 4. 선원관리 Div를 보여주기
        setMemTapClick('memberBan');
    }


// 잔액 1/N 하기 버튼을 눌렀을 때 '잔액 1/N 하기' 메뉴로 이동하게 하기

    function memberDivNBtnClick() {

        var crewBoxDiv = document.getElementById('crewSettingBox');
        var crewTapDiv = document.getElementById('crewTap');
        var memberBoxDiv = document.getElementById('memberSettingBox');
        var memberTapDiv = document.getElementById('memberTap');
        var memberBanTap = memberTapDiv.querySelectorAll('div');

        // 1. 모달 창 꺼주고
        modalWindowClose();
        hiddenModalFunction(cannotDeleteModal);
        hiddenModalFunction(modalBack);

        // 2. 선원관리 Div 보여주고
        memberBoxDiv.classList.remove('hidden');
        memberBoxDiv.classList.add('visibleBox');
        memberTapDiv.classList.remove('hidden');
        memberTapDiv.classList.add('tapVisible');

        crewBoxDiv.classList.remove('visibleBox');
        crewBoxDiv.classList.add('hidden');
        crewTapDiv.classList.remove('tapVisible');
        crewTapDiv.classList.add('hidden');

        // 3. 선원관리 - 잔액 1/N 하기 탭에 select 클래스 적용하고
        // 선원관리하기 탭이 div 4개중 4번째에 해당하므로 [3]에 해당함
        memberBanTap[2].classList.add('select');

        // 4. 선원관리 Div를 보여주기
        setMemTapClick('memberDivN');
    }


// ================================================================================================================================================


// 삭제 결정 후 유예기간 동안 뜨는 창 =============================================================================================================

    // 항해 중단 일자, 유예기간 남은 일자 뜨는 창
    var deleteRequestDateDiv = document.getElementById('deleteRequestDate');
    var deleteCompleteDDayDiv = document.getElementById('deleteCompleteDDay');

    function deleteDateStrMaker(DBdeleteRequestDate) {
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

    // 크루로 돌아가기 버튼을 눌렀을 때

    function turnBackBtnClick() {

        modalWindowClose(); // 모달 끄고

        // FIXME 유예시작일을 없애는 메소드를 실행하는 링크로 이동
        // FIXME 그 후에 크루로 돌아가도록 하기

        window.location.href = 'http://localhost:63342/project_cruise/src/main/resources/templates/crew/main/crewmain.html'

    }




