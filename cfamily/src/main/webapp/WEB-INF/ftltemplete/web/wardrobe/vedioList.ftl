<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>视频列表</title>
<link rel="stylesheet" href="../../resources/css/wardrobe/app.css">
<style type="text/css">
.wrap{margin:100px auto 0 auto;}
.tabs{height:40px;}
.tabs a{display:block;float:left;width:33.33%;color:#333;text-align:center;background:#eee;line-height:40px;font-size:16px;text-decoration:none;}
.tabs a.active{color:#fff;background:#333;border-radius:5px 5px 0px 0px;}
.swiper-container{background:#333;height:325px;border-radius:0 0 5px 5px;width:100%;border-top:0;}
.swiper-slide{height:325px;width:100%;background:none;color:#fff;}
.content-slide{padding:40px;}
.content-slide p{text-indent:2em;line-height:1.9;}
</style>
<script type="text/javascript" src="../../resources/cfamily/zzz_js/zzz.min.js"></script> 
<script type="text/javascript" >
	var listTop = new Array();
	$(document).ready(function() {
		var syurl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetProgramList?api_key=betafamilyhas";
		 $.ajax({
			  type: "GET",
			  url: syurl,
			  dataType: "json",
			  success: function(data) {
			  		var o = eval('(' + JSON.stringify(data) + ')');
				  	var innHTML = "";
				  	var resultCode = o.resultCode;
				  	if(resultCode == 1){
				  		var proArr = o.proList;
				  		for(var i=0;i<proArr.length;i++){
				  			var pro = proArr[i];
				  			var plyTime = pro.play_time;
				  			var tTime = plyTime.substring(4,6);
				  			if(i == 0) {
				  				var yue = reverseNum(eval(tTime)+"");
				  				$("#power").html(yue+"月");
				  			}
				  			innHTML += '<div class="spli" flg="'+eval(tTime)+'" onclick="sendCommand(\''+pro.link+'\',2)">';
				  				innHTML += '<div class="imgd"><span>&nbsp;</span><b>'+pro.on_time+'</b><img src="'+pro.vedio_img+'" alt="" /></div>';
				  				innHTML += '<div class="txtd">';
				  					innHTML += '<h3>'+pro.title+'</h3>';
				  				innHTML += '</div>';
				  				innHTML += '<p>'+plyTime+' '+pro.count+'</p>';
				  			innHTML += '</div>';
				  		}
				  	}
				  	
				 	$(".sps").html(innHTML);
				 	//countElement();
				 	startTime();
				 	window.onload=countElement;
				 	if(parent.document.location.href.indexOf('specialshare') > 0) {
				 		$("#t_title").show();
				 	} else {
				 		$("#power").css("top","3.8rem");
				 		$(".sps").css("padding-top", "0px");
				 	}
			  }
		})
		$(parent.document).scrollTop(0);
		
	})
	var flgTime = "";
	function startTime() {
		clearTimeout(flgTime);
		$("#power").show();
		flgTime = setTimeout(function(){
			$("#power").hide();
		},1000);
	}
	function countElement() {
		$(".spli").each(function(index, el){
	 		listTop.push($(el).offset().top); 
	 	})
	} 
	window.onscroll = function () { 
		startTime();
		countElement();
		scrTop = $("#power").offset().top;
		//scrTop += 12;
		for(var i =0;i<listTop.length-1;i++) {
			if(listTop[i] <= scrTop &&  scrTop < listTop[i+1]) {
				var flg = $(".spli").eq(i).attr("flg");
				var t_month = reverseNum(flg);
				$("#power").html(t_month+"月");
				break;
			} 
		}
	};
	function reverseNum(num) {
			switch(num) {
				case "1":
				  x="一";
				  break;
				case "2":
				  x="二";
				  break;
				case "3":
				  x="三";
				  break;
				case "4":
				  x="四";
				  break;
				case "5":
				  x="五";
				  break;
				case "6":
				  x="六";
				  break;
				case "7":
				  x="七";
				  break;
				case "8":
				  x="八";
				  break;
				case "9":
				  x="九";
				  break;
				case "10":
				  x="十";
				  break;
				case "11":
				  x="十一";
				  break;
				case "12":
				  x="十二";
				  break;
				default:
	  			  x="一";
			}
			return x;
		}
		function sendCommand(param,flg){
			var url = param;
			if(flg == 1) {
				url ="../cfamilypage/"+param;
			}
			document.location = url;
			//window.open(url);
		}
</script> 

</head>
<body>
<header id="t_title" style="display:none;" class="hfixed">视频列表<a href="index.html" class="btn-arrowl"><ins>&nbsp;</ins></a></header>
<span id="power" class="ico-tag"></span>
<div class="sps">
	
</div>
</body>

</html>