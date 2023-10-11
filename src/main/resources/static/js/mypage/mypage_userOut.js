
var withdrawalBtns = document.getElementsByClassName("withdrawal");
var userOutDiv = document.getElementsByClassName("userOutmodal-div");
var yBtn = document.getElementsByClassName("yBtn");
var nBtn = document.getElementsByClassName("nBtn");

var yDivs = document.getElementsByClassName("y-div");
var nDivs = document.getElementsByClassName("n-div");

var okBtn1 = document.getElementsByClassName("yes");
var okBtn2 = document.getElementsByClassName("no");

// "회원 탈퇴" 버튼 js

withdrawalBtns[0].onclick = function() {

    userOutDiv[0].style.display = "block";
    document.body.style.overflow = "hidden";

}

yBtn[0].onclick = function() {

    //db에 회원정보 delete 

    userOutDiv[0].style.display = "none";
    yDivs[0].style.display = "block";
}

nBtn[0].onclick = function() {

    userOutDiv[0].style.display = "none";
    nDivs[0].style.display = "block";
}

okBtn1[0].onclick = function() {

    // 메인페이지 이동

    yDivs[0].style.display = "none";
    document.body.style.overflow = "auto";
    document.body.style.overflowX = "hidden";

}

okBtn2[0].onclick = function() {

    nDivs[0].style.display = "none";
    document.body.style.overflow = "auto";
    document.body.style.overflowX = "hidden";

}