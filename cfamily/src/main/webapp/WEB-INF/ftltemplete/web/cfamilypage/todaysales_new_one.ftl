<#import "../../macro/macro_baidutongji.ftl" as tongji />
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="UTF-8">
<title>限时抢购</title>
<link  href="../../resources/css/ppth_todaysales_new_one/style.css" rel='stylesheet'>	
<link rel="stylesheet" href="../../resources/css/ppth_todaysales_new_one/idangerous.swiper.css">
<script type="text/javascript" src="../../resources/cfamily/zzz_js/jquery-1.11.1.min.js"></script>
<script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
<script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script> 
<#include "../../macro/macro_baidutongji.ftl"/>
<@tongji.m_common_tongji ""/>
<script type="text/javascript">
	loadInit();
	var alarmTime = 3*60*1000;//提前几分钟闹钟提醒
	var openAlarmClockInfo = '';
	var openClockList = "";
	var isNewVersion = false;
	var buyerType = up_urlparam('buyerType','');
	var syurl="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForFlashActive?api_key=betafamilyhas&buyerType="+buyerType;
	var point = new Array();
	var rev_point = new Array();
	var now_start_time_tmp="";
	var now_end_time_tmp="";
	function pushIosOpenAlarmClockInfo(info) {
  	  isNewVersion = true;
  	  if("" != info && null != info) {
		  openAlarmClockInfo = JSON.stringify(info);
		  if("" != openAlarmClockInfo) {
			 openClockObj = eval('(' + openAlarmClockInfo + ')');
			 openClockList = openClockObj.openAlarmList;
	  	  }  	  	
  	  }
	  return "success";
	}
	if(browser.versions.android) {
		try  { 
			openAlarmClockInfo = window.share.getOpenedAlarmList();
			if("" != openAlarmClockInfo) {
				 openClockObj = eval('(' + openAlarmClockInfo + ')');
				 openClockList = openClockObj.openAlarmList;
		  	}
		  	isNewVersion = true;
		} catch (e)  {}
	}
	var itvalTime ;//闪购倒计时(定时)
	$(document).ready(function() {
		 $.ajax({
			  type: "GET",
			  url: syurl,
			  dataType: "json",
			  success: function(data) {
				  var o = eval('(' + JSON.stringify(data) + ')');
				  var time = o.systemTime;
				  var isHasSalesingInfo = false;
				  if(o.resultCode == 1){
					  systm = Date.parse(time.replace(/-/g, "/"));
					  setInterval(function(){
					  	  systm = eval(systm + 1000);
					  }, 1000)
					  
					  var pointHTML = "";
					  var tempCnt = 0;
					  var activeList = o.activeList;
					  for(var i=0;i<activeList.length;i++) {
					  		var flgSalesing = false;
					  	  	var active = activeList[i];
			  	  			var bool = new Date(Date.parse(active.end_time.replace(/-/g, "/"))) < new Date(Date.parse(time.replace(/-/g, "/")));
			  	  			
			  	  			if (bool) {
				  				flgShow = false;
				  			  //已经结束暂不处理
						  	} else {
						  		
					  			
			  	  				bool = new Date(Date.parse(active.start_time.replace(/-/g, "/"))) < new Date(Date.parse(time.replace(/-/g, "/")));
					  			var pointC = active.start_time.split(' ')[1].substring(0,5);
					  			if (bool) {
					  				//判断是否是昨天场
						  			var tmwPre = '';
						  			if(isTomorrow(active.start_time,reverTime(systm))) {
						  				tmwPre += "昨天";
						  			} 
					  				point.push(active.end_time);
					  				rev_point.push(active.start_time);
									pointHTML += '<a href="javascript:void(0)" id="tab'+tempCnt+'" ichsy_start_time="'+active.start_time+'" ichsy_end_time="'+active.end_time+'" ichsy_code="'+active.activity_code+'" ichsy_isSales="1" class="active"><em>'+tmwPre+pointC+'场</em><span>&nbsp;</span></a>';
					  			} else {
					  				//判断是否是明天场
						  			var tmwPre = '';
						  			if(isTomorrow(reverTime(systm),active.start_time)) {
						  				tmwPre += "明天";
						  			} 
					  				point.push(active.start_time);
					  				rev_point.push(active.end_time);
								  	pointHTML += '<a href="javascript:void(0)" id="tab'+tempCnt+'" ichsy_start_time="'+active.start_time+'" ichsy_end_time="'+active.end_time+'" ichsy_code="'+active.activity_code+'" ichsy_isSales="0"><em>'+tmwPre+pointC+'场</em><span>&nbsp;</span></a>';
					  			} 
			  					tempCnt ++;
			  	  			}
			  	  			
					  }//循环结束
					  
					  $('.tabs').html(pointHTML);
					  
			  	  }
			  	  
			  	  	/*切换菜单点击状态*/	
					$(".tabs a").click(function(e){
						//切换动作
						e.preventDefault();
						var im = $(this).index();
						
						//加载商品信息
						var pcInfoHTML = '';
						var activity_code = $(this).attr("ichsy_code");
						var ichsy_start_time = $(this).attr("ichsy_start_time");
						var ichsy_end_time = $(this).attr("ichsy_end_time");
						var isSales = $(this).attr("ichsy_isSales");
						//加载数据
						if(!document.getElementById("tabid"+im)) { 
						
							//解析已经开启的闹钟数据
						  	var openClockObj = "";
						  
							var dateurl="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForFlashActiveProduct?api_key=betafamilyhas&activity_code="+activity_code+"&imgWidth=200&buyerType="+buyerType;
							$.ajax({
								type: "GET",
								url: dateurl,
								dataType: "json",
								success: function(data) {
								  	var o = eval('(' + JSON.stringify(data) + ')');
								  	if(o.resultCode == 1) {
								  		pcInfoHTML += '<div class="swiper-slide" id="tabid'+im+'" style="display:block">';
								  			pcInfoHTML += '<div class="content-slide">';
								  				pcInfoHTML += '<div class="pinp-sg">';
							  					if(isSales == "1") {
							  						var downTime = getCountDown(ichsy_end_time);
							  						var dnt = downTime.split(":");
							  						pcInfoHTML += '<div class="time-go" id="end_time_'+im+'"><span class="ico">&nbsp;</span><span>距离结束</span><em>'+dnt[0]+'</em><span>:</span><em>'+dnt[1]+'</em><span>:</span><em>'+dnt[2]+'</em></div>';
							  					} else {
							  						var downTime = getCountDown(ichsy_start_time);
						  							var dnt = downTime.split(":");
							  						pcInfoHTML += '<div class="time-go" id="end_time_'+im+'"><span class="ico">&nbsp;</span><span>距离开始</span><em>'+dnt[0]+'</em><span>:</span><em>'+dnt[1]+'</em><span>:</span><em>'+dnt[2]+'</em></div>';
							  					}
								  					
								  					pcInfoHTML += '<div class="lidw">';
												  	var pcList = o.productList;
												  		for(var j=0;j<pcList.length;j++){
												  			var pc = pcList[j];
												  			
												  			//判断闹钟是否已经开启
										  	  				var isHadOpenClock = false;
										  	  				if(openClockList.length == 0) {
										  	  					if("" != openAlarmClockInfo) {
																	 openClockObj = eval('(' + openAlarmClockInfo + ')');
																	 openClockList = openClockObj.openAlarmList;
																}
										  	  				}
														  	for(var k=0;k<openClockList.length;k++) {
														  	  	var clockInof = openClockList[k];
														  	  	if(clockInof.startTime == ichsy_start_time && clockInof.productCode == pc.product_code) {
														  	  		isHadOpenClock = true;
														  	  		break;
														  	  	}
														  	}
														  	
												  			pcInfoHTML += ' <div class="lid" onclick="sendCommand(\'goods_num:'+pc.product_code+'\')">';
												  				pcInfoHTML += '<div class="imgd"><img src="'+pc.img_url+'" alt="" /></div>';
												  				pcInfoHTML += ' <div class="txtd">';
												  					pcInfoHTML += '<h3>'+pc.product_name+'</h3>';
												  					pcInfoHTML += '<div class="price"><b>¥</b>'+pc.vip_price+'<span>¥'+pc.sell_price+'</span></div>';
												  					pcInfoHTML += '<div class="icod"><span>'+eval(pc.discountRate/10)+'折</span></div>';
												  					var t_dis = eval(pc.sell_count/pc.sales_num);
												  					if(isSales == "1") {
												  						//1、如果销量=库存  则显示已抢光
															  			//2、库存=销量=0   显示已抢光
															  			//3、销量>0&&库存=0 显示已抢光
															  			<#--
												  						var t_dis = eval(pc.sell_count/pc.sales_num);
															  			if(pc.sales_num == pc.sell_count || pc.sales_num == 0) {
															  				pcInfoHTML += '<div class="clock-open clock-close"><span></span><em>已抢光</em></div>';
															  			} else {
															  				if(t_dis == 1) {
															  					pcInfoHTML += '<div class="clock-open clock-close"><span></span><em>已抢光</em></div>';
															  				} else {
																	  			pcInfoHTML += '<div class="sche-open"><span  style="width:'+Math.round((t_dis*100))+'%;"></span><em>已抢'+Math.round((t_dis*100))+'%</em></div>';
															  				}
															  			}
															  			-->
												  					} else {
												  						//app是否是最新版本
												  						if(isNewVersion) {
												  							if(isHadOpenClock) {
																		  		pcInfoHTML += '<div class="clock-open clock-close" onclick="openAlarmClock(this,\''+0+'\',\''+pc.product_code+'\',\''+ichsy_start_time+'\',\''+ichsy_end_time+'\')"><b>&nbsp;</b>已开启</div>';
																	  		 } else {
																	  		 	pcInfoHTML += '<div class="clock-open" onclick="openAlarmClock(this,\''+1+'\',\''+pc.product_code+'\',\''+ichsy_start_time+'\',\''+ichsy_end_time+'\')"><b>&nbsp;</b>开启闹钟</div>';
																	  		 }
												  						}												  						
												  					}
												  					
												  				pcInfoHTML += '</div>';
												  			pcInfoHTML += '</div>';
												  		}
													pcInfoHTML += '</div>';
								  				pcInfoHTML += '</div>';
								  			pcInfoHTML += '</div>';
								  		pcInfoHTML += '</div>';
								  		$(".swiper-slide").hide();
								  		$(".swiper-wrapper").append(pcInfoHTML);
								  		$("#tabid"+im).show();
								  	}
								}
							})
						} 
						clearInterval(itvalTime);
						itvalTime = setInterval('GetRTime("' + point[im] + '",' + im + ')', 1000);
						
						
						//切换动作
						$(this).addClass('active').siblings().removeClass("active");
						$('#tabid'+im).css('display','block').siblings().css('display','none');
						
						var num =($('.tabs a').outerWidth(true)*(im));
						$(".tabsw .a").animate({scrollLeft:num},500); 
						
					});	
					//展示第一场
					$(".tabs a").eq(0).click();
					
			  }
		  })
	})
  
  function GetRTime(mydate, i) {
  	  
  	  var endt = getCountDown(mydate);
  	  var tm = endt.split(":");
	  var ems = $('#end_time_'+i).find('em');
	  
	  if(tm[0] == "00" && tm[1] == "00" && tm[2] == "00") {
	  	  if(point[i] >= rev_point[i]) {
		  	  $('#end_time_'+i).html('<span class="ico">&nbsp;</span><span>该时段抢购已结束</span>');
	  	  } else {
	  	  	  ems[0].innerHTML = tm[0];
			  ems[1].innerHTML = tm[1];
			  ems[2].innerHTML = tm[2];
	  	  }
	  } else {
	  	  ems[0].innerHTML = tm[0];
		  ems[1].innerHTML = tm[1];
		  ems[2].innerHTML = tm[2];
	  }
	 
	  
  };
  function getCountDown(t_time) {
  	//计算当前剩余时间
	var EndTime = new Date(t_time.replace(/-/g, "/"));
    var leftTime = eval(EndTime.getTime() - systm);
    if(leftTime >= 0) {
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
	    return hour+":"+minute+":"+second;
	} else {
		return "00:00:00";
	}
  }
  function sendCommand(param){
  	 var code = param.split(":");
  	 _hmt.push(['_trackPageview', '\/flashSales/skucode/'+code[1]]);
	 var url=param;
	 document.location = url;
  }
  function openAlarmClock(obj,switchStatus,productCode,startTime,endTime) {
  	stopBubble($(obj).parent().parent()[0]);
  	
  	//判断是否可以开启
  	if(switchStatus == 1 || switchStatus == '1'){
  		var t_start_time = getCountDown(startTime);
  		var t_start_timeArr = t_start_time.split(':');
  		var sTime = new Date(startTime.replace(/-/g, "/"));
  		var leftTime = eval(sTime.getTime() - systm);
		if(t_start_timeArr[0]== "00" && t_start_timeArr[1] == "00" && t_start_timeArr[2]== "00") {
		  	//活动已开始
	  		new Toast({context:$('body'),message:'活动已开始',top:'40%'}).show();
	  		return false;
		} else if(leftTime <= alarmTime) {
			//活动即将开始
			new Toast({context:$('body'),message:'活动即将开始',top:'40%'}).show();
  			return false;
		}
  		
  	}
  	var param = '{"systemTime":"'+reverTime(systm + alarmTime,'')+'","productCode":"'+productCode+'","startTime":"'+startTime+'","switchStatus":"'+switchStatus+'","type":"1"}';
  	if(browser.versions.android) {
  		window.share.notifyOnAndroid(param);
	} else if(browser.versions.ios || browser.versions.iPhone || browser.versions.iPad){
		window.location = "objc://openClock:/"+param;
	}
	if(switchStatus == '1' || switchStatus == 1) {
		//open
		$(obj).html('<b>&nbsp;</b>已开启');
		$(obj).attr("onclick","openAlarmClock(this,0,'"+productCode+"','"+startTime+"','"+endTime+"')");
		$(obj).attr('class','clock-open clock-close');
	} else if(switchStatus == '0' || switchStatus == 0) {
		//close
		$(obj).html('<b>&nbsp;</b>开启闹钟');
		$(obj).attr('class','clock-open');
		$(obj).attr("onclick","openAlarmClock(this,1,'"+productCode+"','"+startTime+"','"+endTime+"')");
	}
	
  }
  function reverTime(time,format) {
	var format = function(time, format){
	    var t = new Date(time);
	    var tf = function(i){return (i < 10 ? '0' : '') + i};
	    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
	        switch(a){
	            case 'yyyy':
	                return tf(t.getFullYear());
	                break;
	            case 'MM':
	                return tf(t.getMonth() + 1);
	                break;
	            case 'mm':
	                return tf(t.getMinutes());
	                break;
	            case 'dd':
	                return tf(t.getDate());
	                break;
	            case 'HH':
	                return tf(t.getHours());
	                break;
	            case 'ss':
	                return tf(t.getSeconds());
	                break;
	        }
	    })
	}
	//return format(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss');
	return format(time, 'yyyy-MM-dd HH:mm:ss');
  }
  function stopBubble(e){
	  if(e&&e.stopPropagation){//非IE
	  	e.stopPropagation();
	  }
	  else{//IE
	  	window.event.cancelBubble=true;
	  }
  } 
  //判断是否是明天场
  function isTomorrow(pre_time,end_time){
  	var preT = new Date(pre_time.replace(/-/g, "/"));
  	var endT = new Date(end_time.replace(/-/g, "/"));
  	if(endT.getDate() - preT.getDate() > 0) {
  		return true;
  	} else {
  		return false;
  	}
  	
  }
  
//是否在app端展示下拉刷新
{function isRefresh(flag){
	
	var type = load();
	if(type == "ios") {
		return pageIsRefresh;
	} else if(type == "android"){
		try {
			window.share.isNeedPullRefresh(pageIsRefresh);
		} catch (e) {}
	}
	
	
}}
 function load() {
			
		var thisOS = navigator.platform;
		
		var browser = { 
			versions: function() { 
				var u = navigator.userAgent, app = navigator.appVersion; 
				return {//移动终端浏览器版本信息  
					trident: u.indexOf('Trident') > -1, //IE内核 
					presto: u.indexOf('Presto') > -1, //opera内核 
					webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核 
					gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核 
					mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端 
					ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
					android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器 
					iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器 
					iPad: u.indexOf('iPad') > -1, //是否iPad 
					webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部 
				}; 
			}(), 
			language: (navigator.browserLanguage || navigator.language).toLowerCase() 
		};
	
		if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
			return "ios";
		} 
		else if (browser.versions.android) {
			return "android";
		}
	}
var pageIsRefresh = true;//定义页面是否需要刷新
isRefresh(pageIsRefresh);
</script>
</head>	
<body>
<div class="tabw">
	<div class="tabsw">
    	<div class="a">
        <div class="tabs">
             
        </div>
    </div>
</div>  
</div>   
<div class="swiper-container">
    <div class="swiper-wrapper">
         
    </div>
</div>
<#-- <div class="ftline">&nbsp;</div> -->

</body>
</html>