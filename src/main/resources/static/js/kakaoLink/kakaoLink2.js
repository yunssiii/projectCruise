Kakao.init('9862f7eff0dc73e6f37d21910ffc74b0');
     function sendLink() {
     var group = document.getElementById("sessionGroup").textContent;
     var num = document.getElementById("sessionNum").textContent;
     var descriptionText = group + '모임통장에 초대 받으셨습니다.' ;




     var mobileWebUrl = 'http://192.168.16.27:8082/?group=' + group + '&num=' + num;
     var webUrl = 'http://192.168.16.27:8082/?group=' + group + '&num=' + num;

       Kakao.Link.sendDefault({
         objectType: 'feed',
         content: {
           title: 'Cruise에 승선하기',
           description: descriptionText,
           imageUrl:
             'https://cdn-media-1.freecodecamp.org/images/VKuRIyPcfnVrGvjRB3oe6NrQ026qn9DzmXru',
           link: {
             mobileWebUrl: mobileWebUrl,
             webUrl: webUrl,
           },
         },
         social: {
           likeCount:50, //99999가 최대입니다
           commentCount: 32, //99999가 최대입니다
           sharedCount: 19, //99999가 최대입니다
         },
         buttons: [
           {
             title: '초대에 응하기',
             link: {
               mobileWebUrl: mobileWebUrl,
               webUrl: webUrl,
             },
           },
         ],
       })
     }