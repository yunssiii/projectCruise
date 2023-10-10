
//-- 모임 탈퇴 js------------------------------------------------------------
/*
    탈퇴 버튼 클릭 시 모달
    예/아니오 -> 모달
*/

var functions = []; //함수 저장 변수

//모달창 클릭, 모달창
var outBtn = document.getElementsByClassName("user-out");
var crewdelDiv = document.getElementsByClassName("crewDelmodal-div");

//예스버튼 클릭, 모달창
var yBtns = document.getElementsByClassName("yBtn");
var nBtns = document.getElementsByClassName("nBtn");
var ymodal = document.getElementsByClassName("y-div");
var nmodal = document.getElementsByClassName("n-div");
var okBtn1 = document.getElementsByClassName("yes");
var okBtn2 = document.getElementsByClassName("no");

function DelModal(num) {
    return function() {

        outBtn[num].onclick = function() {
            crewdelDiv[num].style.display = "block";
            document.body.style.overflow = "hidden";
        }

        yBtns[num].onclick = function() {
            crewdelDiv[num].style.display = "none";
            ymodal[num].style.display = "block";

        }

        nBtns[num].onclick = function() {
            crewdelDiv[num].style.display = "none";
            nmodal[num].style.display = "block";
        }

        okBtn1[num].onclick = function() {
            ymodal[num].style.display = "none";

            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";
        }

        okBtn2[num].onclick = function() {
            nmodal[num].style.display = "none";

            document.body.style.overflow = "auto";
            document.body.style.overflowX = "hidden";
        }
    }
}

for(var i = 0; i < outBtn.length; i++) {
    functions[i] = DelModal(i); //함수 담기
}

for(var j = 0; j < outBtn.length; j++) {
    functions[j](); //함수 호출
}