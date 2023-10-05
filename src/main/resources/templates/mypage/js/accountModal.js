
        var modals = document.getElementsByClassName("account-modal"); //모달div를 감싸는 최상위 class
        var boxs = document.getElementsByClassName("select-modal-div"); //선택할 div class
        var btns = document.getElementsByClassName("closeBtn"); //닫기 버튼이 있는 div class
        var funcs = []; 
        
        // Modal을 띄우고 닫는 클릭 이벤트를 정의한 함수
        function Modal(num) {
            return function() {

                boxs[num].onclick =  function() {
                    modals[num].style.display = "block";
                    console.log(num);
                };
            
                btns[num].onclick = function() {
                    modals[num].style.display = "none";
                };
            };
        }

        for(var i = 0; i < boxs.length; i++) {
            funcs[i] = Modal(i); //함수 담기
        }

        for(var j = 0; j < boxs.length; j++) {
            funcs[j](); //함수 호출
        }
        
        // Modal 영역 밖을 클릭하면 Modal 닫음
        window.onclick = function(event) {
            if (event.target.className == "account-modal") {
                event.target.style.display = "none";
            }
        };
        
