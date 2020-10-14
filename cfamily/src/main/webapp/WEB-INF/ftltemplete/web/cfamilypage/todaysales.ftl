<#import "../../macro/macro_baidutongji.ftl" as tongji />
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>惠家有-今日闪购</title>
<style type="text/css">
*{margin:0;padding:0;}
a{ color:#666; text-decoration:none;-webkit-tap-highlight-color: rgba(0,0,0,0);}
body{ font-size:14px; font-family: 'microsoft yahei',Verdana,Arial,Helvetica,sans-serif; background-color:#f2f2f2; color:#666666}
img{border:none; float:left;}
.ad{ height:auto; overflow:hidden;}
.xq-pro{margin:0;width:100%;float:left;}
.xq-pro a{ display:block; overflow:hidden; }
.xq-pro img{width:100%; }
.live_list{ width:100%; height:auto; overflow:hidden}
.ad img{border-top:1px solid #CCC;border-bottom:1px solid #CCC;}
.hot_live{ float:right; }
.hot_live strong{ font-size:16px;}
.live_time{background-color:#f2f2f2 ; z-index:1; position:absolute; color:#999; width:96%; height:40px;line-height:40px; padding:0 2%;}
.live_time1{background-color:#f2f2f2; z-index:1; position:absolute; color:#DC0F50; width:96%; height:40px;line-height:40px; padding:0 2%;}
.live_time1 img{ margin-top:14px; margin-right:5px;}
.live_time img{ margin-top:14px; margin-right:5px;}
.time{ float:left;}
.time_i{ margin-left:-15px; margin-top:12px; display:block; width:25px; height:26px; float:left; position:absolute; left:0;}
.live{ height:auto; overflow:hidden;float:left;width:100%;}
.live_1{ width:100%; height:120px; position:relative; overflow:hidden; background-color:#FFF; border:1px solid #e7e7e7; border-right:none;border-left:none; margin-top:10px; float:left;}
.sp_img{ float:left; width:120px; height:120px;position:relative;min-width:100px;}
.sp_js{float:left; width:57%; padding-left:5%; margin-top:10px; }
.sp_js h1{ font-size:16px;font-family:"微软雅黑";}
.zhekou{ width:30px; height:33px; background:url(http://app116.ichsy.com:8181/index/images/hjy_09.png) no-repeat; position:absolute; right:-1px; top:-1px; line-height:12px; text-align:center; }
.biaoqian1{ width:60px; height:60px; position:absolute; right:0; bottom:0;}
.none{ display:none;}
.the_end{ color:#666;}
.mt40{width:100%; height:120px; position:relative; overflow:hidden; background-color:#FFF; border:1px solid #e7e7e7; border-right:none;border-left:none; margin-top:40px; float:left;}
</style>
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<#include "../../macro/macro_baidutongji.ftl"/>
<@tongji.m_common_tongji ""/>
<script type="text/javascript">
		//var syurl="/52yungo/demo/home/appHomePage?module_num=14032817122537838364&pagecount=100&currentpage=1&outputWidth=720";
		var syurl="../../../cfamily/jsonapi/com_cmall_ordercenter_service_api_ApiGetFSkuNowService?api_key=betafamilyhas";
		var thsI=0;
		var one_start_time="";
		var two_start_time="";
		var three_start_time="";
		var one_end_time="";
		var two_end_time="";
		var three_end_time="";
		var now_start_time_tmp="";
		var now_end_time_tmp="";
		var p_replace_i_tmp=parseInt("0");
		var hid_time_i_tmp=parseInt("0");
		
		Date.prototype.Format = function(fmt) {
			var o = {
				"M+": this.getMonth() + 1,
				"d+": this.getDate(),
				"h+": this.getHours(),
				"m+": this.getMinutes(),
				"s+": this.getSeconds(),
				"q+": Math.floor((this.getMonth() + 3) / 3),
				"S": this.getMilliseconds()
			};
			if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			for (var k in o) if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			return fmt
		};
		
	
		 
		
		
		
		//闪购:开始时间、折扣、商品详情连接、第二天的节目预告、三个状态：正在进行的、即将进行的、已经结束的
		$(document).ready(function() {
			  $.ajax({
				  type: "GET",
				  url: syurl,
				  dataType: "json",
				  success: function(data) {
					  var o = eval('(' + JSON.stringify(data) + ')');
					  var innHTML = "";
					  var ztgood = "";
					  var begin = "距离本场开始:";
					  var end = "距离本场结束:";
					  var time = o.systemTime;
					  systm = Date.parse(time.replace(/-/g, "/"));
					  //var time_date = time.split(" ");
					  //one_start_time = time_date[0] + " 10:00:00";
					  //one_end_time = time_date[0] + " 12:00:00";
					  //two_start_time = time_date[0] + " 20:00:00";
					  //two_end_time = time_date[0] + " 22:00:00";
					  //last_time = time_date[0] + " 23:59:59";
					  var three_date_tmp = new Date(Date.parse(time.replace(/-/g, "/")));
					  three_date_tmp.setDate(three_date_tmp.getDate() + 1);
					  three_date_tmp = three_date_tmp.Format("yyyy-MM-dd hh:mm:ss");
					  var three_time_date = three_date_tmp.split(" ");
					  three_start_time = three_time_date[0] + " 10:00:00";
					  three_end_time = three_time_date[0] + " 12:00:00";
					  var tmp_startTime = "";
					  var tmp_endTime = "";
					  var mt40_i = 0;
					  var lastbool = "";
					  
					  if(o.resultCode == 1){
					  	if(data.banner_img.length > 0){
					  		var blink = data.banner_link;
 							if(blink.length >0){
 								var reg = /goods_num:/; 
								var r = blink.match(reg);
 								if(null != r) {
 									innHTML += "<div class=\"xq-pro\" id=\"banner_id\"><a href=\"#\" onclick=\"sendCommand('"+blink+"')\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
 								} else {
	 								innHTML += "<div class=\"xq-pro\" id=\"banner_id\"><a href=\""+blink+"\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
 								}
 							} else {
 								innHTML += "<div class=\"xq-pro\"><a href=\"#\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
 							}
 						}
					  	var goodslists = o.list;
					  	var onliveI = parseInt("0");
					  	for(var i=0;i<goodslists.length;i++){
					  		var ztgood = goodslists[i];
					  		if (ztgood.start_time != now_start_time_tmp && ztgood.end_time != now_end_time_tmp) {
					  			if (i != 0) {
								  innHTML += '</div>';
								  mt40_i = i
							  	}
							  	innHTML += '<div class="live">';
							  	innHTML += '<div id="p_replace_' + p_replace_i_tmp + '" class="live_time1">';
							  	var bool = new Date(Date.parse(ztgood.end_time.replace(/-/g, "/"))) < new Date(Date.parse(time.replace(/-/g, "/")));
					  			if (bool) {
					  			  //continue;
								  innHTML += '<img src="../../resources/cfamily/zzz_js/naoz_06.png" width="16"/><div class="time the_end">' + ztgood.start_time.substr(11, 5) + '-' + ztgood.end_time.substr(11, 5) + '</div>';
								  innHTML += '<div class="hot_live the_end">本场已结束</div>'
							  	} else {
								  bool = new Date(Date.parse(ztgood.start_time.replace(/-/g, "/"))) < new Date(Date.parse(time.replace(/-/g, "/")));
								  if (bool) {
									  if (onliveI == 0) {
										  innHTML += '<div id="onlive"></div>';
										  onliveI = onliveI + parseInt("1")
									  }
									  innHTML += '<img src="../../resources/cfamily/zzz_js/naoz_03.png" width="16"/><div class="time">' + ztgood.start_time.substr(11, 5) + '-' + ztgood.end_time.substr(11, 5) + '</div>';
									  innHTML += '<div class="hot_live">' + end + '<div id="hid_time_' + hid_time_i_tmp + '" class="none">' + ztgood.end_time + '</div><strong id="time_' + hid_time_i_tmp + '"></strong></div>'
								  } else {
								  	  //continue;
									  innHTML += '<img src="../../resources/cfamily/zzz_js/naoz_06.png" width="16"/><div class="time the_end">' + ztgood.start_time.substr(11, 5) + '-' + ztgood.end_time.substr(11, 5) + '</div>';
									  innHTML += '<div class="hot_live the_end">' + begin + '<div id="hid_time_' + hid_time_i_tmp + '" class="none">' + ztgood.start_time + '</div><strong id="time_' + hid_time_i_tmp + '"></strong></div>'
								  }
								  hid_time_i_tmp = hid_time_i_tmp + parseInt("1")
							  	}
							  	p_replace_i_tmp = p_replace_i_tmp + parseInt("1");
							  	innHTML += '</div>';
							  	now_start_time_tmp = ztgood.start_time;
							  	now_end_time_tmp = ztgood.end_time
					  		}
					  		if (i == mt40_i) {
							  	innHTML += '<div class="mt40" >'
							  } else {
								  innHTML += '<div class="live_1">'
							  }
							  innHTML += '<a id="'+ztgood.sku_code+'-'+i+'" onclick="_hmt.push([\'_trackPageview\', \'\/flashSales/skucode/'+ztgood.sku_code+'\'])" href="'+ztgood.goods_link+'" style="width: 100%;height: 120px;display: block;">';
							  innHTML += '<span class="biaoqian1"><img src="../../resources/cfamily/zzz_js/shan_img_07.png" width="60" /></span>';
							  if (ztgood.on_status == '-1') {
								  innHTML += '<span class="biaoqian1"><img src="../../resources/cfamily/zzz_js/shan_img_07.png" width="60" /></span>'
							  } else if (ztgood.on_status == '0') {
								  innHTML += '<span class="biaoqian1"><img src="../../resources/cfamily/zzz_js/shan_img_05.png" width="60" /></span>'
							  } else {
								  innHTML += '<span class="biaoqian1"><img src="../../resources/cfamily/zzz_js/shan_img_03.png" width="60" /></span>'
							  }
							  innHTML += '<div class="sp_img">';
							  if (ztgood.img_url == '') {
								  innHTML += '<img src="../../resources/images/ppth_todaysales_new_one/today_sales_bg.png" width="120" alt=""/>'
							  } else {
								  innHTML += '<img class="delay" src="../../resources/images/ppth_todaysales_new_one/today_sales_bg.png" data-original="' + ztgood.img_url + '" width="120" alt=""/>'
							  }
							  innHTML += '<span class="zhekou">';
							  innHTML += '<p style="padding-top:5px;">' + ztgood.discountRate + '</p>';
							  innHTML += '<p style="font-size:10px;">%</p>';
							  innHTML += '</span>';
							  innHTML += '</div>';
							  innHTML += '<div class="sp_js">';
							  innHTML += '<h1>' + ztgood.sku_name + '</h1>';
							  if (ztgood.on_status == '0') {
								  innHTML += '<p style="margin-top:10px; color:#DC0F50;">闪购价:￥<strong style="font-size:20px;">' + ztgood.vip_price + '</strong></p>'
							  } else {
								  innHTML += '<p style="margin-top:10px;">闪购价:￥<strong style="font-size:20px;">' + ztgood.vip_price + '</strong></p>'
							  }
							  if (ztgood.market_price == '') {
								  innHTML += '<p style="text-decoration:line-through; color:#999">原价:￥ 暂无价格</p>'
							  } else {
								  innHTML += '<p style="text-decoration:line-through; color:#999">原价:￥' + ztgood.sell_price + '</p>'
							  }
							  innHTML += '</div></a></div>';
							  if (ztgood.start_time != now_start_time_tmp && ztgood.end_time != now_end_time_tmp) {
								  innHTML += '</div>'
							  }
					  	}
					  }else{
					  	alert("获取数据出错，请刷新重试!");
					  }
					  $("#live_list").html(innHTML);
					  $("img.delay").lazyload({threshold:100,placeholder:""});
					  startI();
					  setInterval(function(){
					  	  systm = eval(systm + 1000);
					  }, 1000)
				  }
			  })
		  });
		
		var flagstTime1 = false;
		var flagstTime2 = false;
		function GetRTime(mydate, i) {
			  var EndTime = new Date(mydate.replace(/-/g, "/"));
			  var leftTime = eval(EndTime.getTime() - systm);
			  var leftsecond = parseInt(leftTime / 1000);
			  var day1 = Math.floor(leftsecond / (60 * 60 * 24));
			  var hour = Math.floor((leftsecond - day1 * 24 * 60 * 60) / 3600);
			  var minute = Math.floor((leftsecond - day1 * 24 * 60 * 60 - hour * 3600) / 60);
			  var second = Math.floor(leftsecond - day1 * 24 * 60 * 60 - hour * 3600 - minute * 60);
			  if (hour < 10) {
				  hour = "0" + hour
			  }
			  if (minute < 10) {
				  minute = "0" + minute
			  }
			  if (second < 10) {
				  second = "0" + second
			  }
			  document.getElementById("time_" + i).innerHTML = hour + ":" + minute + ":" + second
		  };
				 
		function setEndTime(endTime) {
			  var str = endTime.split(" ");
			  return str[1]
		  };
		
		
		//eval(function(p,a,c,k,e,r){e=String;if('0'.replace(0,e)==0){while(c--)r[e(c)]=k[c];k=[function(e){return r[e]||e}];e=function(){return'[2-4]'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('function startI(){myOnload();2 3="";4(2 j=0;j<hid_time_i_tmp;j++){3=document.getElementById("hid_time_"+j).innerHTML;setInterval(\'GetRTime("\'+3+\'",\'+j+\')\',1000)}4(2 k=0;k<p_replace_i_tmp;k++){$("#p_replace_"+k).smartFloat()}};',[],5,'||var|thsTime|for'.split('|'),0,{}));
		
		function startI() {
			  myOnload();
			  var thsTime = "";
			  for (var j = 0; j < hid_time_i_tmp; j++) {
				  thsTime = document.getElementById("hid_time_" + j).innerHTML;
				  setInterval('GetRTime("' + thsTime + '",' + j + ')', 1000)
			  }
		  };
		
		function myOnload(){
			setTimeout("$('#onlive').val($('#onlive').offset().top)", 200); 
			setTimeout("scrollOnlive()", 201); 
		};
		
		function scrollOnlive() {
			  $(window).scrollTop($('#onlive').val() - 5)
		};
				 
		function shangou_end() {
				alert("来晚了，闪购商品已结束")
		}
   	
		   
		function jiangks_end() {
			  alert("闪购还未开始，敬请期待")
		}
		
		function sendCommand(param){
			var url=param;
			document.location = url;
		}
		
</script>

    
</head>

<body onLoad="startI();">
<div class="today_live">
    <!-- <div class="ad"><img src="http://app116.ichsy.com:8181/index/images/shangou_02.jpg" width="100%"  alt=""/></div> -->
    <div class="live_list" id="live_list">
    
  </div>

</div>
</body>
</html>
