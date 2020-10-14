<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title></title>
 <meta name="description" content="">
 <meta name="keywords" content="">
 <meta name="renderer" content="webkit">
 <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <!-- 页面图标 -->
  <link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
	<!-- 页面主样式 -->
  <link rel="stylesheet" href="../../resources/cfamily/zzz_js/app.css">
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/handlebars.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.widgets.helper.js"></script>
<script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
<script language="javascript" type="text/javascript"> 
	function messageHandler(msg){
		var data=msg.data;
		if(data.do=='frameSize'){
	    	var iframe = document.getElementById("ifr"); 
	    	iframe.height =  (data.value.height-3)+"px";
	    	
		}
	}
	window.addEventListener('message',messageHandler,false);
	
	function shareOnIOS(){
	    return callbackFunc("ios");
	};
	
	function callbackFunc(param){
		var title = decodeURIComponent(up_urlparam("alt"));
		var imgUrl = up_urlparam("imgUrl");
		var share_img_url = up_urlparam("share_img_url");
		var share_title = decodeURIComponent(up_urlparam("share_title"));
		var share_content = decodeURIComponent(up_urlparam("share_content"));
		var link = up_urlparam("link");
		var flg = up_urlparam("flg");
		<#-- 控制悬浮的显隐 -->
		$("title").html(title);
		$('#ifr').attr('height','1000px');
		if(flg == '1001') {
			$('#show_d_f').hide();
		} else {
			$('#show_d_f').show();
			//$('#ifr').parent().css("padding-top",$('#show_d_f').height()+"px");
			pmobile = up_urlparam("pm", "");
			if(pmobile.length == 11) {
				var pre = pmobile.substring(0,3);
				var end = pmobile.substring(pmobile.length-4);
				$('#show_pm').prepend("您的好友"+pre+"****"+end+"送您");
			} else {
				$('#show_pm').prepend("您的好友送您");
			}
		}
		if(null == share_img_url || "" == share_img_url || "null" == share_img_url) { 
			share_img_url = imgUrl;
		}
		if(null == share_content || "" == share_content || "null" == share_content) {
			share_content = "我在惠家有发现一个不错的宝贝哦，赶快来看看吧！";
		}
		if(null == share_title || "" == share_title || "null" == share_title) {
			share_title = title;
		}
		var tsLink = window.location.href;
		
		var share_link = tsLink.split("?")[0]+"?link="+link+"&alt="+encodeURIComponent(title);
		if(param == "android"){
			var reg1=new RegExp("%2F","g");
			var reg2=new RegExp("%3A","g");
			link = link.replace(reg1, "/");
			link = link.replace(reg2, ":");
			$("#ifr").attr("src",repalce_urlparam_val("isSpecial","true",link));
			//$("#ifr").attr("src",repalce_urlparam_val("gfUrl",url_param_mix(window.location.href,true),link));
			try{
				window.share.shareOnAndroid(share_title,share_img_url,share_content,share_link);
			}catch(e){}
		}else{
			return share_title+"'/"+share_img_url+"'/"+share_content+"'/"+share_link;
		}
	}
	
	function close_tc_ico(){
		$("#fon").hide();
	}
	var jinzhi=0;
	function show_tc_ico(){
		$("#fon").show();
		document.addEventListener("touchmove",function(e){if(jinzhi==0){e.preventDefault();e.stopPropagation();}},false);
	}
	function pushIframeHeight(ifrheight) {
		$("#ifr").height(ifrheight);
	}
	function closeHead(obj){
		$(obj).parent().parent().hide();
	}
</script>
<style> 
	.mark{ background:#000; opacity:0.75; width:100%; height:1000%; position:absolute; left:0; top:0; z-index:9998;}
	.mark-c{ width:90%; margin:0 auto; position:fixed; left:50%; top:0; margin-left:-45%; z-index:9999; overflow:hidden;}
	.mark-c img{width:100%; margin:0 auto;}
	.btn-close{display: block;text-indent:-999em; position: absolute; top:1rem; right:1.125%; z-index: 9; width:2.4rem; height:2.4rem; overflow:hidden; background:url(../../resources/cfamily/zzz_js/btn-close.png) left top no-repeat; background-size:100% auto; line-height: 9.9rem;}
	#show_pm{padding-right:3rem;}
</style>
</head>
<body onload="callbackFunc('android');" style="overflow-x:hidden;   overflow-y:hidden;   ">
	<div id="fon" style="display:none;">
		<div class="mark" id="omark" onclick="close_tc_ico()"></div>
		<div class="mark-c" id="omarkc"><img src="../../resources/cfamily/zzz_js/tc-ico.png" alt="" onclick="close_tc_ico()" /></div>
	</div>
	<!-- 悬浮内容 [[-->
	<div class="e-bgc am-header-fixed" id="show_d_f">
	  <div class="am-g ">
	  	  <a class="btn-close" onclick="closeHead(this)">关闭</a>
		  <div class="col-sm-3 am-margin-top-sm "><img src="../../resources/cfamily/zzz_js/icon.png"></div>
		  <div class="col-sm-9 am-margin-top am-text-sm"  id="show_pm"><span class="e-tc">1次返现特权</span>，购买任一商品都可以使用哦~</div>
	  </div>
	  <div class="am-g am-padding-bottom-sm">
	      <ul class="sm-block-grid-2 am-padding-top-xs">
	          <li class="am-padding-left-sm am-padding-right-xs"><button type="button" class="am-btn am-btn-primary am-btn-block" onclick="openApp(2)">打开APP</button></li>
	          <li class="am-padding-left-xs am-padding-right-sm"><button type="button" class="am-btn am-btn-default am-btn-block"  onclick="getPrerogative()">领取返现特权</button></li>
	      </ul>
	  </div>
	</div>
	
	
	<!-- 悬浮内容 ]]-->
	<div class="am-vertical-align am-text-center" style="background-color:#eeeeee;" >
		<iframe src="" width="100%" frameborder="0" scrolling="auto" id="ifr" name="ifr"/>
	</div>
</body>
</html>

