// memTableBody에 스크롤바가 활성화되면 memTableHead에 10px padding-right주기

var memTableBody = document.querySelector('.memTableBody');
var memTableHead = document.querySelector('.memTableHead');
var memTableHeadLastTh = memTableHead.querySelector('tr th:last-child');

    // 만약 자료가 많으면 memTableHead를 스크롤바 간격만큼 왼쪽으로 땡긴다.
    memTableBody.addEventListener('scroll',function(){
        if(memTableBody.scrollHeight > memTableBody.clientHeight) {
            memTableHeadLastTh.style.paddingRight = '10px';
        } else {
            memTableHeadLastTh.style.paddingRight = '0';
        }
    })


// 강퇴버튼 누르면 모달창 뜨게하기

    var banMemName = "";
    var memberBanModalBg = document.getElementById('memberBanModalBg');

    // 탈퇴시킬건지 여부 묻는 모달
    var memberBanYNModal = document.getElementById('memberBanYNModal');
    var banMemNameYNDiv = document.getElementById('banMemNameYN'); // 탈퇴예정멤버 이름

    // 탈퇴 완료 모달
    var memberBanCompleteModal = document.getElementById('memberBanCompleteModal')
    var banMemNameComDiv = document.getElementById('banMemNameCom')   // 탈퇴완료멤버 이름
    var backMemBanSettingBtn = document.getElementById('backMemBanSettingBtn') // 확인버튼
    function memBanBtnClick(num) {
        var banMem = memTableBody.querySelector('table tr:nth-child(' + num + ')');
        banMemName = banMem.querySelector('td:nth-child(3)');
            // 3번째 칸에 해당하는 것 = 이름 가지고오기

        memberBanModalBg.classList.remove('hiddenModal');
        memberBanYNModal.classList.remove('hiddenModal');
        memberBanModalBg.classList.add('visibleModal');
        memberBanYNModal.classList.add('visibleModal');

        banMemNameYNDiv.textContent = banMemName.textContent;

        var banYesBtn = document.getElementById('banYes');
        var banNoBtn = document.getElementById('banNo');

        // 탈퇴 '네' 눌렀을 때
        banYesBtn.addEventListener('click',function() {

            // FIXME 탈퇴처리 자바스크립트 넣을 것
            alert('탈퇴처리과정이 들어갑니다.')

            memberBanYNModal.classList.remove('visibleModal');
            memberBanYNModal.classList.add('hiddenModal');
            memberBanCompleteModal.classList.remove('hiddenModal')
            memberBanCompleteModal.classList.add('visibleModal')

            banMemNameComDiv.textContent = banMemName.textContent;
        })

        // 탈퇴 '아니오' 눌렀을 때
        banNoBtn.addEventListener('click',function () {
            memberBanYNModal.classList.remove('visibleModal');
            memberBanYNModal.classList.add('hiddenModal');
            memberBanModalBg.classList.remove('visibleModal');
            memberBanModalBg.classList.add('hiddenModal');
        })

        // 탈퇴 후 확인 눌렀을 때
        backMemBanSettingBtn.addEventListener('click', function (){
            memberBanCompleteModal.classList.remove('visibleModal');
            memberBanModalBg.classList.remove('visibleModal');
            memberBanCompleteModal.classList.add('hiddenModal');
            memberBanModalBg.classList.add('hiddenModal');
        })

    }