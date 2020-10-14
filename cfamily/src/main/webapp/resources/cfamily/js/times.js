// JavaScript Document

// Date: 2014-11-07
// Author: Agnes Xu


i = 0;
j = 0;
count = 0;
MM = 0;
SS = 10;  // 秒 90s
MS = 0;
totle = (MM+1)*600;
d = 180*(MM+1);
MM = "0" + MM;
var gameTime = 15;

var wz1 = "浏览页面10秒，积分＋20";
var wz2 = "浏览完成，积分＋20";
var wz3 = "今日机会已用光，明日继续哦。";

var cishu = 1;

var showTime = function(){
    totle = totle - 1;
    if (totle == 0) {
		
		//倒计时结束
		$(".sjf_time_v").html(10 + "s");
		$(".sjf_lquwz").html(wz2);
		$("#sjf_lingqu").show();
		$(".sjf_game_time").css("overflow","initial");
		$(".sjf_time_zt").css("overflow","initial");
		$(".sjf_time_v").animate({top:"-40px",opacity:"0"});
		$(".sjf_jinbi").animate({top:"-10px"});
		$(".sjf_jia20").animate({bottom:"-9px"});
		

        clearInterval(s);
        clearInterval(t1);
        //clearInterval(t2);
        //$(".pie2").css("-o-transform", "rotate(" + d + "deg)");
        //$(".pie2").css("-moz-transform", "rotate(" + d + "deg)");
        //$(".pie2").css("-webkit-transform", "rotate(" + d + "deg)");
    } else {
        if (totle > 0 && MS > 0) {
            MS = MS - 1;
            if (MS < 10) {
                MS = "0" + MS
            }
            ;
        }
        ;
        if (MS == 0 && SS > 0) {
            MS = 10;
            SS = SS - 1;
            if (SS < 10) {
                SS = "0" + SS
            }
            ;
        }
        ;
        if (SS == 0 && MM > 0) {
            SS = 10;
            MM = MM - 1;
            if (MM < 10) {
                MM = "0" + MM
            }
            ;
        }
        ;
    }
    ;
    $(".sjf_time_v").html(SS + "s");
	
};

var start1 = function(){
	//i = i + 0.6;
	i = i + 360/((gameTime)*10);  //旋转的角度  90s 为 0.4  60s为0.6
	count = count + 1;
	if(count <= (gameTime/2*10)){  // 一半的角度  90s 为 450
		$(".sjf_pie1").css("-o-transform","rotate(" + i + "deg)");
		$(".sjf_pie1").css("-moz-transform","rotate(" + i + "deg)");
		$(".sjf_pie1").css("-webkit-transform","rotate(" + i + "deg)");
	}else{
		$(".sjf_pie2").css("backgroundColor", "#d13c36");
		$(".sjf_pie2").css("-o-transform","rotate(" + i + "deg)");
		$(".sjf_pie2").css("-moz-transform","rotate(" + i + "deg)");
		$(".sjf_pie2").css("-webkit-transform","rotate(" + i + "deg)");
	}
};



var countDowng = function(time,integral) {
    //80*80px 时间进度条
	gameTime = time;
	wz1 = "浏览页面"+time+"秒，积分＋"+integral;
	wz2 = "浏览完成，积分＋"+integral;
	$(".sjf_lquwz").html(wz1);
	$(".sjf_jia20").html("+"+integral);
    i = 0;
    j = 0;
    count = 0;
    MM = 0;
    SS = gameTime;
    MS = 0;
    totle = (MM + 1) * gameTime * 10;
    d = 180 * (MM + 1);
    MM = "0" + MM;

    showTime();

    s = setInterval("showTime()", 100);
    start1();
    //start2();
    t1 = setInterval("start1()", 100);
}




function sjf_lq_jifen(){
if(cishu == 0){
	//每次数了
	
	$(".sjf_lquwz").html(wz3);
	$("#sjf_lingqu").hide();
	$("#sjf_lingqu1").show();
	$(".sjf_quan_w").show();
	$(".sjf_quan_w").animate({opacity:1});
	$(".sjf_time_v").hide();
	$(".sjf_jinbi").hide();
	$(".sjf_jia20").hide();
	$(".sjf_zt_bg").hide();
	$(".sjf_hold").hide();
	
	}else{
	$(".sjf_lquwz").html(wz1);
	$("#sjf_lingqu").hide();
	$(".sjf_pie2").css("background-color","#ff863d");
	$(".sjf_game_time").css("overflow","hidden");
	$(".sjf_time_zt").css("overflow","hidden");
	$(".sjf_time_v").animate({top:"-8px",opacity:"1"});
	$(".sjf_jinbi").animate({top:"22px"});
	$(".sjf_jia20").animate({bottom:"42px"});
	
	cishu--;
	//重置倒计时
	gameTime = 10;
	countDown();
	}
}

function sjf_lq_jifen1(){
//	window.open(template.huDongInfo.url);
	window.location.href=template.huDongInfo.url;
}