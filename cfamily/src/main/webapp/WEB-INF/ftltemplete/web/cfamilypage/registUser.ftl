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
  <link rel="stylesheet" href="../../resources/cfamily/zzz_js/app.css">

</head>
<body onload="load_mobile()">
<script src="../../resources/lib/jquery/jquery-last.min.js"></script>
<script src="../../resources/lib/easyui/jquery.easyui.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/handlebars.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.widgets.helper.js"></script>
<script language="javascript" type="text/javascript"> 
	 var show_time_interval;
	 var clear_time;
	 $(function(){
	 	$('#is_reg_mobile').css('display','none');
	 	//展示发送验证码的时间
	 	$('#btn_isabled_send_verify').click(function(){
	 		send_verify();
	 	})
	 	
	 	
	 	$('#is_submit').click(function(){
	 	
	 		<#-- 验证验证码 -->
	 		var reg_verify = /^[0-9]+$/;
	 		var verify_code = $('#pwd_sid').val();
	 		if(null == verify_code.match(reg_verify)) {
		 		setTime();
		 		$('#show_message').html('验证码格式错误');
		 		return false;
		 	} 
	 		<#-- 验证验证码end -->
	 		
	 		<#-- 验证输入的密码 -->
	 		var reg_pwd = /^\w{6,16}$/;
	 		var password = $('#input_pwd').val();
	 		if(null == password.match(reg_pwd)) {
	 			setTime();
		 		$('#show_message').html('密码格式错误');
		 		return false;
		 	} 
	 		<#-- 验证输入的密码end-->
	 		$('#is_submit').attr('disabled','disabled');
	 	
		 	var login_name = $('#hide_mobile').val();
	 		
	 		var param = "&nickname="+login_name+"&login_name="+login_name+"&client_source=app&password="+password+"&verify_code="+verify_code;
	 		var syurl="/cfamily/jsonapi/com_cmall_membercenter_api_UserReginster?api_key=betafamilyhas"+param;
	 		$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				var hide_pmobile = $('#hide_pmobile').val();
		 				var reg = '^1[0-9]{10}$';
		 				if(null != hide_pmobile && "" != hide_pmobile && hide_pmobile.match(reg)) {
		 					addParentUser(login_name,hide_pmobile);
					 	} else {
					 		document.location = "addPmember.html?t_mobile="+login_name;
					 	}
		 			} else {
		 				$('#show_message').html(data.resultMessage);
		 				setTime();
		 				$('#is_submit').removeAttr('disabled');
		 			}
		 		}
		 	});
		 	
		 	
	 	})
	 	
	 	 function send_verify() {
	 	 	$('#tz_alt_id').html("验证码已发至您的手机，马上就能享受返现特权啦！");
		 	$('#btn_isabled_send_verify').attr('disabled','disabled');
	 		var input_mobile = $('#hide_mobile').val();
	 		var syurl="/cfamily/jsonapi/com_cmall_systemcenter_api_MsgSend?api_key=betafamilyhas&send_type=reginster&mobile="+input_mobile;
	 		//发送短信
	 		$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				show_time_interval = setInterval(function(){
							var tval = $('#btn_isabled_send_verify').html();
							var tArr = tval.split('S');
							if(tArr.length == 1) {
								tval = '60';
							} else {
								tval = tArr[0];
							}
							if(null == tval || "" == tval) {
								tval = '60';
							}
							var showTime = eval(tval - 1);
							if(showTime == 0) {
								clearInterval(show_time_interval);
								$('#btn_isabled_send_verify').removeAttr('disabled');
								$('#btn_isabled_send_verify').html("重新获取验证码");
								$('#tz_alt_id').html("只差一步就能享受返现特权啦！");
							} else {
								if(showTime < 10) {
									$('#btn_isabled_send_verify').html('0'+showTime+"S后可重发");
								}　else {
									$('#btn_isabled_send_verify').html(showTime+"S后可重发");
								}
								
							}
							
						},1000);
		 			} else {
						$('#btn_isabled_send_verify').removeAttr('disabled');
						$('#tz_alt_id').html("只差一步就能享受返现特权啦！");
						$('#show_message').html(data.resultMessage);
		 				setTime();
		 			}
		 		}
		 	});
		 }
		load_mobile();
		//页面加载即发送验证码
		send_verify();
	 }) 
	
	 function load_mobile() {
	 	var mobile = up_urlparam('st_mobile','');
	 	var pmobile = up_urlparam('pmobile','');
	 	$('#hide_mobile').val(mobile);
	 	$('#hide_pmobile').val(pmobile);
	 }
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
		
	function addParentUser(mobile,pmobile){
		var loginName = mobile;
		var parentLoginName = pmobile;
		var myDate = new Date(); 
		var createTime = formatDate(myDate, "yyyy-MM-dd HH:mm:ss");
		var param = "&loginName="+loginName+"&parentLoginName="+parentLoginName+"&createTime="+createTime;
		var syurl="/cfamily/jsonapi/com_cmall_groupcenter_account_api_ApiCreateRelation?api_key=betafamilyhas"+param;
 		$.ajax({
	 		type:"GET",
	 		url:syurl,
	 		dataType:"json",
	 		success:function(data){
	 			if(data.resultCode == 1){
				 	document.location = "getPrerogativeSuc.html";
	 			} else {
	 				$('#show_message').html(data.resultMessage);
	 				setTime();
	 				$('#is_submit').removeAttr('disabled');
	 			}
	 		}
	 	});
	}
	
	function formatDate(date, format) {   
	    if (!date) return;   
	    if (!format) format = "yyyy-MM-dd";   
	    switch(typeof date) {   
	        case "string":   
	            date = new Date(date.replace(/-/, "/"));   
	            break;   
	        case "number":   
	            date = new Date(date);   
	            break;   
	    }    
	    if (!date instanceof Date) return;   
	    var dict = {   
	        "yyyy": date.getFullYear(),   
	        "M": date.getMonth() + 1,   
	        "d": date.getDate(),   
	        "H": date.getHours(),   
	        "m": date.getMinutes(),   
	        "s": date.getSeconds(),   
	        "MM": ("" + (date.getMonth() + 101)).substr(1),   
	        "dd": ("" + (date.getDate() + 100)).substr(1),   
	        "HH": ("" + (date.getHours() + 100)).substr(1),   
	        "mm": ("" + (date.getMinutes() + 100)).substr(1),   
	        "ss": ("" + (date.getSeconds() + 100)).substr(1)   
	    };       
	    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {   
	        return dict[arguments[0]];   
	    });                   
	}
	
	function setTime() {
		$('#is_reg_mobile').css('display','block');
		clear_time = setTimeout(function(){
			$('#is_reg_mobile').css('display','none');
		},2000);
	}
	
	function clearTime() {
		$('#is_reg_mobile').css('display','none');
		clearTimeout(clear_time);
	}
	
</script>

<!-- 内容 [[-->

<!-- .am-u-sm-centered 始终居中 -->

<div class="am-container">
<div class="am-padding-top am-padding-bottom-sm am-text-left am-padding-left-sm" id="tz_alt_id">只差一步就能享受返现特权啦！</div>
<div class="am-g">
  <div class="am-u-md-8 am-u-sm-centered">
    <form class="am-form">
      <fieldset class="am-form-set">
        <div class="am-input-group">
        <input type="hidden" value="" class="am-form-field am-input-lg" id="hide_mobile" />
	   <input type="hidden" value="" class="am-form-field am-input-lg" id="hide_pmobile" />
      <input id="pwd_sid" type="text" class="am-form-field am-input-lg" placeholder="请输入验证码">
      <span class="am-input-group-btn">
        <button class="am-btn am-btn-secondary am-text-lg1 e-padding" type="button" id="btn_isabled_send_verify">59S后可重发</button>
      </span>
    </div>
        <input id="input_pwd" type="password" placeholder="请设置密码（限6-16位字符）" class="am-input-lg">
        
      </fieldset>
   
        <div class="am-u-sm-11 am-u-sm-centered am-margin-top">
  <button id="is_submit" type="button" class="am-btn am-btn-default am-btn-block am-btn-lg" >下一步</button>
  </div>
    </form>
  </div>
</div>
<div id="is_reg_mobile" class="am-u-sm-9 am-u-sm-centered am-margin-top-xs"><span class="e-hk am-padding am-block am-text-center " id="show_message"></span></div>
</div>
<!-- 内容 ]]-->



</body>
</html>
