const swiper = new Swiper('.swiper', {
    slidesPerView: 3,
    slidesPerGroup : 3,
    centeredSlides: true,
    spaceBetween: 30,
    centeredSlides : false,
    navigation: {
      nextEl: '.slideBtn-next',
      prevEl: '.slideBtn-prev',
    },
    virtual: {
      loopFillGroupWithBlank : true,
      watchOverflow : true,
      slides: (function () {
        const slides = [];
      //   i < 10; -> slide의 개수!! 모임 count해서 조회한 값 넣으면 될 듯
        for (var i = 0; i < 4; i += 1) {
          slides.push('');
        }
        return slides;
      })(),
    },
  });