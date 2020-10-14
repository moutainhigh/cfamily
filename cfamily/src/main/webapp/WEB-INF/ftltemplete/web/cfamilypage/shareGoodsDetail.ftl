<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta charset="UTF-8">
	<title>惠家有购物商城</title>
    <link href="../../resources/cfamily/zzz_js/prerogative/css/style.css" rel="stylesheet">
    <script src="../../resources/cfamily/zzz_js/prerogative/js/jquery-1.11.1.min.js"></script>
    <link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
    <script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
    <script type="text/javascript">
    	<#-- 商品详情分享接口 -->
    	var goodsNum = up_urlparam("pc",'');
    	if(true) {
	    	window.location = "../shareshopping/productdetail.ftl?pc="+goodsNum;
    	} else {
	    	var pmobile = up_urlparam("pm","");
	    	var param = "productCode="+goodsNum;
	    	var syurl="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetSkuInfo?api_key=betafamilyhas&"+param;
	    	$(document).ready(function() {
	    		//如果是微公社分享的，标题栏不显示
		    	var s_apikey = up_urlparam("api_key");
		    	if (s_apikey && s_apikey != "" && s_apikey == "betagroup") {
		    		$(".top").attr("style","display:none;");
		    	}
	    	
	    		loadInit();//判断浏览器类型
	    		if(null != pmobile && pmobile != '' && pmobile.length == 11) {
		    		var pm = pmobile.substring(0, 3) + "****" + pmobile.substring(pmobile.length - 4);
		    		$("#adPm").html("您的好友"+pm+"送您<em> 1 次返现特权</em>，购买任一产品都可以使用哦~");
		    	}
	    		$.ajax({
					  type: "GET",
					  url: syurl,
					  dataType: "json",
					  success: function(data) {
					  	  var detail = eval('(' + JSON.stringify(data) + ')');
					  	  var htmlText = "";
					  	  if(detail.resultCode == 1) {
					  	  	
					  	  	systm = Date.parse(detail.sysDateTime.replace(/-/g, "/"));
							setInterval(function(){
								systm = eval(systm + 1000);
							}, 1000)
							
					  	  	var skuList = detail.skuList;
					  	  	var pcPicList = detail.pcPicList;//轮播
					  	  	var skuList = detail.skuList;
					  	  	htmlText += '<div class="box pro">';
						  	  	htmlText += '<div class="banner">';
						  	  		//判断是否有库存
						  	  		var cntStockNum = 0;
						  	  		for(var i=0;i<skuList.length;i++) {
						  	  			cntStockNum += skuList[i].stockNumSum;
						  	  		}
						  	  		if(cntStockNum <= 0) {
							  	  		htmlText += '<div class="tip"><p>没货啦，下次早点来哦～</p><span>&nbsp;</span></div>';
						  	  		}
						  	  		htmlText += '<div class="scroll-wrapper" id="idContainer2" ontouchstart="touchStart(event)" ontouchmove="touchMove(event);" ontouchend="touchEnd(event);">';
							  	  		htmlText += '<ul class="scroller" id="idSlider2">';
							  	  		for(var i=0; i< pcPicList.length;i++) {
							  	  			htmlText += '<li style="width:-100%"><a href="javascript:void(0);"><img src="'+pcPicList[i].picNewUrl+'" /></a></li>';
							  	  		}
								  	  	htmlText += '</ul>';
								  	  	htmlText += '<div class="banner-numw"><ul class="banner-num" id="idNum"></ul></div>';
							  		htmlText += '</div>';
							  	htmlText += '</div>';
							  	var scpt = document.createElement("script");  
							    scpt.setAttribute('src', '../../resources/cfamily/zzz_js/prerogative/js/focus.js');  
							    document.body.appendChild(scpt);
							  	htmlText += '<div class="num">';
								  	htmlText += '<h2>'+detail.productName+'</h2>';
								  	htmlText += '<p class="bianh">商品编号：<span>'+detail.productCode+'</span></p>';
								  	$('.bh').html(detail.productCode);
								  	htmlText += '<p class="price"><em class="nprice"><em>￥</em>'+detail.sellPrice+'</em><span><em>￥</em>'+detail.marketPrice+'</span></p>';
								  	htmlText += '<div class="xlang">';
								  	if(detail.flagCheap == 1) {
								  		isSalesing = true;
								  		var endTime = detail.endTime;
								  		var rtnEndTime = getCountDown(endTime);
								  		var splitTime = rtnEndTime.split(':');
								  		htmlText += '<div class="time" id="count_down" ichsy_end_time="'+endTime+'">剩&nbsp;<span>'+splitTime[0]+'</span>:<span>'+splitTime[1]+'</span>:<span>'+splitTime[2]+'</span></div>月销'+detail.saleNum+'件';
								  	} else {
								  		isSalesing = false;
								  		htmlText += '<div class="time"></div>月销'+detail.saleNum+'件';
								  	}
								  	htmlText += '</div>';
								  	htmlText += '<div class="qian">';
								  		htmlText += '<p>返现<span class="big"><em>￥</em>'+detail.disMoney+'</span></p><b>&nbsp;</b>';
								  	htmlText += '</div>';
								htmlText += '</div>';
								var ishasCXinfo = false;
								var cxInfo = "";
								cxInfo += '<div class="sales">';
									cxInfo += '<h2><b>&nbsp;</b>促销信息</h2>';
									if(skuList.length>0) {
										var actInfo = skuList[0].activityInfo;
										for(var i=0;i<actInfo.length;i++) {
											ishasCXinfo = true;
											cxInfo += '<div class="lid">';
												cxInfo += '<em>'+actInfo[i].activityName+'</em>';
												cxInfo += '<div>'+actInfo[i].remark+'</div>';
											cxInfo += '</div>';
										}
									}
									if(detail.flagIncludeGift == 1) {
										ishasCXinfo = true;
										cxInfo += '<div class="lid">';
											cxInfo += '<em>赠品</em>';
											cxInfo += '<div>'+detail.gift+'</div>';
										cxInfo += '</div>';
									}
								cxInfo += '</div>';
								if(ishasCXinfo) {
									htmlText += cxInfo;
								}
							htmlText += '</div>';
							var prptyList =  detail.propertyList;
							var isShowSizes = false;//是否展示规格参数一栏  true:展示;false:不展示;
							var showSizesContent = '';
							for(var i = 0;i<prptyList.length;i++) {
								var prptyvaluelist = prptyList[i].propertyValueList;
								var temp_htmlText = '';
								temp_htmlText += '<div class="lid"><em>'+prptyList[i].propertyKeyName+'：</em><div>';
								var isTrue = false;//true: 该规格下有propertyValueName ; false:没有规格参数（或者只是共同）
								for(var j=0;j<prptyvaluelist.length;j++) {
									if(prptyvaluelist[j].propertyValueCode != 0) {
										isTrue = true;
										isShowSizes = true;
										temp_htmlText += '<span>'+prptyvaluelist[j].propertyValueName+'</span>、';
									} else {
										isTrue = false;
									}
								}
								if(isTrue == true) {
									temp_htmlText = temp_htmlText.substring(0,temp_htmlText.length-1);
									temp_htmlText += '</div></div>';
									showSizesContent += temp_htmlText;								
								}
							}
							if(isShowSizes) {
								htmlText += '<div class="box sizes">';
								htmlText += showSizesContent;
								htmlText += '</div>';
							}
							htmlText += '<div class="bz">';
							var authorityLogo = detail.authorityLogo;
							for(var i=0;i<authorityLogo.length;i++) {
								var styleType = '';
								//if(authorityLogo[i].logoContent.length > 9) {
								//	styleType = 'style="width:50%;"';
								//}
								htmlText += '<span '+styleType+'><b style="background-image: url('+authorityLogo[i].logoPic+');background-repeat: no-repeat;background-position:left 0px;background-size: 100% auto;">&nbsp;</b>'+authorityLogo[i].logoContent+'</span>';
							}
							
							htmlText += '</div>';
							htmlText += '<div class="box">';
								htmlText += '<div class="tabnav" id="menu">';
									htmlText += '<ul>';
										htmlText += '<li onclick="javascript:test_item(0);" class="on"><span>图文详情</span></li>';
										htmlText += '<li onclick="javascript:test_item(1);"><span>规格参数</span></li>';
									htmlText += '</ul>';
								htmlText += '</div>';
								htmlText += '<div id="tabc0" class="tabc">';
									htmlText += '<div class="imgs">';
										//图文详情图
										var discriptList = detail.discriptPicList;
										for(var i=0;i<discriptList.length;i++) {
											htmlText += '<img src="'+discriptList[i].picNewUrl+'" />';
										}
									htmlText += '</div>';
								htmlText += '</div>';
								htmlText += '<div  id="tabc1" class="no">';
									htmlText += '<div class="param">';
									var propertyList = detail.propertyInfoList;
									for(var i=0;i<propertyList.length;i++) {
										htmlText += '<p><span class="st">【'+propertyList[i].propertykey+'】</span><span>'+propertyList[i].propertyValue+'</span></p>';
									}
									htmlText += '</div>';
								htmlText += '</div>';
							htmlText += '</div>';
					  	    $('.wrap').html(htmlText);
					  	    
					  	    //判断是否正在闪购
					  	    if(isSalesing) {
					  	    	setInterval(function(){
						  	    	var t_endTime = $("#count_down").attr("ichsy_end_time");
						  	    	var e_time = getCountDown(t_endTime);
						  	    	var e_timeArr = e_time.split(":");
						  			var contSpan = $("#count_down").find("span");
						  			contSpan[0].innerHTML = e_timeArr[0];
						  			contSpan[1].innerHTML = e_timeArr[1]; 
						  			contSpan[2].innerHTML = e_timeArr[2];
						  			
						  		}, 1000)
					  	    }
					  	    
					  	  }
					  	  
					  }
				})
	    	})
    	}
    	
    	//获取参数
		function up_urlparam(key,sUrl) {
			var sReturn = "";
			if (!sUrl) {
				sUrl = window.location.href;
				if (sUrl.indexOf('?') < 1) {
					sUrl = sUrl + "?";
				}
			}
	
			var sParams = sUrl.split('?')[1].split('&');
	
			for (var i = 0, j = sParams.length; i < j; i++) {
	
				var sKv = sParams[i].split("=");
				if (sKv[0] == key) {
					sReturn = sKv[1];
					break;
				}
			}
			return sReturn;
		}
		function tostIsCall() {
			$("#mask").show();
			$(".ftel").show();
		}
		function exitCall() {
			setTimeout(function(){
				$("#mask").hide();
				$(".ftel").hide();
			},100);
			
		}
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
    </script>
</head> 
<body class="bg2">
<div class="top">
    <div class="txt"><div><img src="../../resources/cfamily/zzz_js/prerogative/images/logo2.png" alt=""></div><span  id="adPm">您的好友送您<em> 1 次返现特权</em>，购买任一产品都可以使用哦~</span></div>
	<div class="btns"><span class='lspan' ><a id="ichsy_openingApp" val="1" href="javascript:void(0);" onclick="openApp(1)">立即打开</a></span><span class='rspan'><a href="javascript:void(0);" onclick="getPrerogative();">领取返现特权</a></span></div>
</div>
<iframe style="display:none;" id="open_app_ifr" src="javascript:void(0);"  width="0" height="0" frameborder="0"></iframe>
<div class="wrap">
	<#-- 内容展示 -->
</div>
<div class="fbox ftel" style="display:none;">
	<div class="sc">为节省您的时间，请告知客服</br>此商品的编号<span class="bh"></span></div>
    <div class="btns">
    	<a href="javascript:void(0);" onclick="exitCall()">取消</a><a href="tel:400-867-8210" class="ok" onclick="exitCall()">确定</a>
    </div>
</div>
<div id="mask" style="display:none;">&nbsp;</div>
<div class="bottom"><span onclick="tostIsCall()">电话订购</span><a href="javascript:void(0);" onclick="openApp(1)">在线购买</a></div>
<script type="text/javascript" > 
    function test_item(n){
        var menu = document.getElementById("menu");
        var menuli = menu.getElementsByTagName("li");
        for(var i = 0; i< menuli.length;i++){
            menuli[i].className="";
            menuli[n].className="on";
            document.getElementById("tabc"+ i).className = "no";
            document.getElementById("tabc"+ n).className = "tabc";
        }
    }
</script>

<script>
<#-- 添加百度统计代码 --> 
var _hmt = _hmt || []; 
(function() { 
var hm = document.createElement("script"); 
hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227"; 
var s = document.getElementsByTagName("script")[0]; 
s.parentNode.insertBefore(hm, s); 
})(); 
</script>
</body>
</html>