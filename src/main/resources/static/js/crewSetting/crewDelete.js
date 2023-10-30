
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

        window.location.href='/crew/setting/updateDelDate?crewNum=' + crewNum;

    }






    function modalWindowClose() {
        hiddenModalFunction(modalBack);
        hiddenModalFunction(cannotDeleteModal);
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






