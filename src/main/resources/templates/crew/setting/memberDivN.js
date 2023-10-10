
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


    // 잔액 나누기 모달 내 버튼
    var divideYesBtn = document.getElementById('divideYes');
    var divideNo = document.getElementById('divideNo');

    divideNBtn.addEventListener('click',function (){
        visibleModalFunction(divideModalBg); // 클릭하면 모달창이 뜨도록
        visibleModalFunction(divideConfirmModal); // 클릭하면 모달창이 뜨도록
    })

    // 네를 눌렀을 때
    divideYesBtn.addEventListener('click',function() {
        hiddenModalFunction(divideConfirmModal);
        visibleModalFunction(divideCompleteModal);
        
        //FIXME 잔액나누기 백엔드 단으로 이동하기
    })

    // 아니오를 눌렀을 때
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
    })







