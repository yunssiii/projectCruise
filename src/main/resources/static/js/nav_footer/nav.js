
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
let socket = new WebSocket("ws://localhost:8082/testSocket");

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
        console.log(e.data);

        var data = e.data; //웹소켓 메세지 내용

        $('#first_menuButton').css('color','#FFD966');

       $('#first_menuButton').click(function() {
            $('#first_menuButton').css('color', 'blue');
       });
    }
}

window.onload = function() {
    openWebSocket();
};




