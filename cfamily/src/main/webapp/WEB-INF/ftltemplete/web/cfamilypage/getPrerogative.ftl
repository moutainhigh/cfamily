<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>领取返现特权</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <!-- Add to homescreen for Safari on iOS -->
<!-- 添加至主屏, 从主屏进入会隐藏地址栏和状态栏, 全屏(content="yes") -->
<meta name="apple-mobile-web-app-capable" content="yes">
<!-- 系统顶栏的颜色 -->
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!-- 页面图标 -->
  <link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
<!-- 页面主样式 -->
<link rel="stylesheet" href="../../resources/cfamily/zzz_js/amazeui.min.css">
  <link rel="stylesheet" href="../../resources/cfamily/zzz_js/app.css">

</head>
<body>

<script src="../../resources/lib/jquery/jquery-last.min.js"></script>
<script src="../../resources/lib/easyui/jquery.easyui.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/handlebars.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.widgets.helper.js"></script>
<script language="javascript" type="text/javascript"> 
	var clear_time;
	$(function(){
		$('#is_reg_mobile').css('display','none');
		
		pmobile = up_urlparam("pmobile",'');
		
		
		$('#is_get_preprogative').click(function(){ 
			<#-- 验证输入内容 -->
			var input_mobile = $('#input_mobile').val();
			if( input_mobile.length == 0 ){
				$('#is_reg_mobile').css('display','block');
				clear_time = setTimeout(function(){
					$('#is_reg_mobile').css('display','none');
				},2000);
				return false;
			}
			if(check_input_mobile() == false) {
				return false;
			}
			<#-- 验证结束 -->
			
			var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiRegIsUser?api_key=betafamilyhas&mobile="+input_mobile;
			$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				//已经是会员
		 				document.location = "getPrerogativeSuc.html";
		 			} else {
						document.location = "registUser.html?st_mobile="+input_mobile+"&pmobile="+pmobile;
		 			}
		 		}
		 	});
		})
		
		function check_input_mobile(){
			var val = $('#input_mobile').val();
			var regNum = /^1[0-9]{10}$/;
			if(null != val.match(regNum)) {
				return true;
			} else if( val.length != 0 ){
				$('#is_reg_mobile').css('display','block');
				clear_time = setTimeout(function(){
					$('#is_reg_mobile').css('display','none');
				},2000);
				return false;
			} else {
				return false;
			}
				
		}
		
		//加载时进行验证输入的手机号
		check_input_mobile();
	})
	
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
</script> 

<!-- .am-u-sm-centered 始终居中 -->
<div class="am-container">
<div class="am-input-group am-margin-top">
  <span class="am-input-group-label am-text-lg">+86</span>
  <input type="text"  id="input_mobile" class="am-form-field am-input-lg easyui-numberbox" placeholder="请输入11位手机号">
</div>

  <div class="am-u-sm-11 am-u-sm-centered am-margin-top"><button type="button" class="am-btn am-btn-default am-btn-block am-btn-lg" id="is_get_preprogative">立即领取</button></div>
    <div id="is_reg_mobile" class="am-u-sm-9 am-u-sm-centered am-margin-top-xs"><span class="e-hk am-padding am-block am-text-center ">请输入11位有效手机号</span></div>
</div>
<!-- 内容 ]]-->



</body>
</html>
