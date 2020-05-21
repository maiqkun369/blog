
 /* jQuery预装载机
 * ----------------------------------------------------------------------------
 * Copyright (C) 2016 Ser0x4
 * http://www.henhack.cn
 * http://serer.me
  -----------------------------------------------*/
$(window).load(function(){
    $('.preloader').fadeOut(1000); // ()=时间    
});


$(document).ready(function() {

  /* 首页的幻灯片
  -----------------------------------------------*/
  $(function() {
    $('body').vegas({
        slides: [
            { src: 'http://oafszmkn2.bkt.clouddn.com/42.jpg' },
            { src: 'http://oafszmkn2.bkt.clouddn.com/32.jpg' },
			{ src: 'http://oafszmkn2.bkt.clouddn.com/58.jpg' },
        ],
        timer: false,
        transition: [ 'zoomOut', ]
    });
  });


  /* wow
  -------------------------------*/
  new WOW({ mobile: false }).init();

  });
