var cruiseUrl = 'http://192.168.16.27:8082/';

(function(){
let navButtonClick = document.getElementsByClassName("navButtonClick");

   function handleClick(event) {

     if (event.target.classList[1] === "clicked") {
       event.target.classList.remove("clicked");
     } else {
       for (let i = 0; i < navButtonClick.length; i++) {
         navButtonClick[i].classList.remove("clicked");
       }

       event.target.classList.add("clicked");

     }
   }
   function resetButtonColors() {
   	  for (let i = 0; i < navButtonClick.length; i++) {
   	    navButtonClick[i].classList.remove("clicked");
   	  }
   	}

   function iconColor() {
       for (let i = 0; i < navButtonClick.length; i++) {
         navButtonClick[i].addEventListener("click", handleClick);
       }
       document.addEventListener("click", function(event) {
           if (!event.target.classList.contains("navButtonClick")) {
             resetButtonColors();
     }
       });
   }

     iconColor();




		//first_menuButton
		const menuButton = document.getElementById("first_menuButton");
		const menu = document.getElementById("first_menu");

		// 버튼을 클릭하면 메뉴를 토글합니다.
		menuButton.addEventListener("click", function() {
			if (menu.style.display === "block") {
				menu.style.display = "none";
			} else {
				menu.style.display = "block";
			}
		});

		// 메뉴 외부를 클릭하면 메뉴를 닫습니다.
		document.addEventListener("click", function(event) {
			if (event.target !== menuButton && event.target !== menu) {
				menu.style.display = "none";

			}
		});

		//second_menuButton
		const second_menuButton = document.getElementById("second_menuButton");
		const second_menu = document.getElementById("second_menu");

		second_menuButton.addEventListener("click", function() {
			if (second_menu.style.display === "block") {
				second_menu.style.display = "none";
			} else {
				second_menu.style.display = "block";
			}
		});

		document.addEventListener("click", function(event) {
			if (event.target !== second_menuButton && event.target !== second_menu) {
				second_menu.style.display = "none";


			}
		});
		})();


////-- 웹소켓 연결 ---------------------------------------------------------------------------------
let socket = new WebSocket("ws://192.168.16.27:8082/testSocket");

function openWebSocket() {

    socket.onclose = () => {
        // 웹소켓 세션이 닫히면 다시 연결 시도
        console.log('연결 끊김');

        socket = null;

        setTimeout(() => {
            openWebSocket();
        }, 100); // 0.1초 후에 다시 연결 시도
    };

    socket.onopen = function (e) {
        console.log('open server!')
    };

    socket.onerror = function (e){
        console.log(e);
    }

    socket.onmessage = function (e) {
        console.log('메인에까지 왔니?');

        var data = e.data; //웹소켓 메세지 내용

        var menuButtons = document.getElementById('first_menuButton');
        menuButtons.classList.add('clicked'); //아이콘 색 변화

        iconColor(); //색 변화 후 클릭 시 변화

    }
}

document.addEventListener("DOMContentLoaded", function() {
    openWebSocket();
});


//-- 알림 select ajax ------------------------------------------------------
// 알림 뱃지 누를 때 실행

const menuButton = document.getElementById("first_menuButton");

menuButton.addEventListener('click', getNavAlert);

function getNavAlert() {
    $.ajax({
        type:"POST",
        url:"/nav/alert",
        dataType:"json",
        success: function(result){
            console.log("nav alert 조회 성공..!");
            console.log("조회된 데이터 >>>>" + result);

            var str = '';

            console.log("결과 길이 >>" + result.length);

            if(result.length === 0){
                str += '<p style="text-align: center;padding-top: 30px;">😶‍🌫️ 알림이 없습니다.</p>'

                $('#alertDivId').children().remove(); //자식 요소 지웠다가
                $('#alertDivId').append(str); //더하기

            }else {

                $.each(result, function(i) {

                    console.log('분류명 >>>>>' + result[i].alertAssort);

                    if(result[i].alertAssort === '공지'){

                        str += '<div class="alertDiv">'

                            str += '<a href="' + cruiseUrl + 'board/article?num='+ result[i].boardNum +'&crewName='+result[i].crewName +'">'

                                str += '<span style="font-weight: 600;">'+ result[i].alertAssort +'</span><br/><br/>'
                                str += '<span>'+ result[i].alertContent +'</span>'
                            str += '</a></div>'




                    }else {

                        str += '<div class="alertDiv">'

                            str += '<a href="' + cruiseUrl + 'crew?crewNum='+ result[i].crewNum +'">'
                                str += '<span style="font-weight: 600;">'+ result[i].alertAssort +'</span><br/><br/>'
                                str += '<span>'+ result[i].alertContent +'</span>'
                            str += '</a></div>'
                    }
                });

                $('#alertDivId').children().remove(); //자식 요소 지웠다가
                $('#alertDivId').append(str); //더하기

            }
        },
        error:function(){
            console.log("nav alert 조회 에러..!");
        }
    })
}
