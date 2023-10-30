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

    var banCrewNum = 0;
    var banMemName = "";
    var banMemEmail = "";

    var banMemtr; // 탈퇴할 멤버의 tr
    var memberBanModalBg = document.getElementById('memberBanModalBg');

    // 탈퇴시킬건지 여부 묻는 모달
    var memberBanYNModal = document.getElementById('memberBanYNModal');
    var banMemNameYNDiv = document.getElementById('banMemNameYN'); // 탈퇴예정멤버 이름

    // 탈퇴 완료 모달
    var memberBanCompleteModal = document.getElementById('memberBanCompleteModal')
    var banMemNameComDiv = document.getElementById('banMemNameCom')   // 탈퇴완료멤버 이름
    var backMemBanSettingBtn = document.getElementById('backMemBanSettingBtn') // 확인버튼
    function memBanBtnClick(crewNum,tdNum,banName, banEmail) {
        var banMemtd = $(memTableBody).find("table").find("td").filter(function() {
            return $(this).text() === tdNum+'';
        });
            // tdNum을 가진 td를 가져와서
        banMemtr = banMemtd.parent(); // 그의 부모요소 (tr)을 찾기
        banCrewNum = crewNum;
        banMemName=banName;
        banMemEmail=banEmail;

        memberBanModalBg.classList.remove('hiddenModal');
        memberBanYNModal.classList.remove('hiddenModal');
        memberBanModalBg.classList.add('visibleModal');
        memberBanYNModal.classList.add('visibleModal');

        banMemNameYNDiv.textContent = banMemName;

    }
    // 이벤트 리스너 중첩등록 해제를 위해 리스너를 바깥으로 뺌

    // 탈퇴 '네' 눌렀을 때
    $('#banYes').on('click',function() {
        var request = $.ajax({
            url: "/crew/setting/memberBan",
            method: "POST",
            data: {
                crewNum: banCrewNum,
                email:banMemEmail
            }
        })

        request.done(function () {
            banMemtr.remove(); // 겉에서 없애기
            window.location.href = '/crew/setting?crewNum=' + banCrewNum
        })

        memberBanYNModal.classList.remove('visibleModal');
        memberBanYNModal.classList.add('hiddenModal');
        memberBanCompleteModal.classList.remove('hiddenModal')
        memberBanCompleteModal.classList.add('visibleModal')
        $('#banMemNameCom').text(banMemName)
    })

    // 탈퇴 '아니오' 눌렀을 때
    $('#banNo').on('click',function() {
        $('#banYesBtn').off('click');
        memberBanYNModal.classList.remove('visibleModal');
        memberBanYNModal.classList.add('hiddenModal');
        memberBanModalBg.classList.remove('visibleModal');
        memberBanModalBg.classList.add('hiddenModal');
    })
    
    // 탈퇴 후 확인 눌렀을 때
    $('#backMemBanSettingBtn').on('click',function() {
        memberBanCompleteModal.classList.remove('visibleModal');
        memberBanModalBg.classList.remove('visibleModal');
        memberBanCompleteModal.classList.add('hiddenModal');
        memberBanModalBg.classList.add('hiddenModal');
        //
        // $('#banYesBtn').off('click');
        // $('#banNoBtn').off('click');
        // $('#backMemBanSettingBtn').off('click');
    })