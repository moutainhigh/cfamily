<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta charset="UTF-8">
    <title>领取返现特权</title>
    <link href="../../resources/cfamily/zzz_js/prerogative/css/style.css" rel="stylesheet">
    <script src="../../resources/cfamily/zzz_js/prerogative/js/jquery-1.11.1.min.js"></script>
    <script src="../../resources/lib/easyui/jquery.easyui.min.js"></script>
    <script src="../../resources/cfamily/zzz_js/prerogative/js/tost.js"></script>
    <script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
</head>
<body class="bg1">

<div class="wrap fpass" style="display:none;">
    <div class="wc">
        <form>
            <div class="lid"><div><input class="easyui-numberbox" type="text" placeholder="请输入您的手机号" pattern="[0-9]*" id="txt" onpaste="return false" onkeyup="this.value=this.value.replace(/\D/g,'')" onbeforepaste="this.value=this.value.replace(/\D/g,'')"  maxLength=11/><span>&nbsp;</span></div></div>
        	<div class="wtc">
        		<input type="hidden" id="water_code1" name="water_code"  ><input class="easyui-numberbox" type="text" placeholder="请输入图片验证码" pattern="[0-9]*" id="put_water_code1" onpaste="return false" onkeyup="this.value=this.value.replace(/\D/g,'')" onbeforepaste="this.value=this.value.replace(/\D/g,'')"  maxLength=11/>
        		<img id="verfiyCodeId1" src="" onclick="reflush_verifycode(1)" />	
        	</div>
        	
            <a href="javascript:;" class="btn-reg" id="is_get_preprogative">立即领取</a>
        </form>
      </div>
</div>

<div class="wrap yreg" style="display:none;">
    <p id="tz_alt_id">验证码已发至您的手机，马上就能享受返现特权啦！</p>
	<div class="wc">
		<form>
			<div class="wtc" style="display:none;">
        		<input type="hidden" id="water_code2" name="water_code"  ><input class="easyui-numberbox" type="text" placeholder="请输入图片验证码" pattern="[0-9]*" id="put_water_code2" onpaste="return false" onkeyup="this.value=this.value.replace(/\D/g,'')" onbeforepaste="this.value=this.value.replace(/\D/g,'')"  maxLength=11/>
        		<img id="verfiyCodeId2" src="" onclick="reflush_verifycode(2)" />	
        	</div>
			<div class="lid yzm"><input type="button" id="getTime" value="59s后可重发" /><div><input type="text"  id="pwd_sid" placeholder="输入验证码" value="" pattern="[0-9]*" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9]/g,'')" onafterpaste="this.value=this.value.replace(/[^a-zA-Z0-9]/g,'')"/></div></div>
            <div class="lid tel"><div><input  id="input_pwd" type="password" placeholder="请设置密码(限6-16位字符，不能包含空格)" value=""  pattern="[^\u4e00-\u9fa5 ]*"  onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5 ]/g,'')" onafterpaste="this.value=this.value.replace(/[\u4e00-\u9fa5 ]/g,'')" maxLength=16></div></div>
			<a href="javascript:;" class="btn-sure"  id="is_submit">下一步</a>
		</form>
      </div>
</div>

<div class="wrap tuij" style="display:none;">
    <p>填写推荐好友的手机号，你购物他也有返现哦！</p>
    <div class="wc">
        <form>
            <div class="lid"><div><input id="p_mob_input_nb" class="easyui-numberbox" type="text" placeholder="请输入推荐人手机号" value="" pattern="[0-9]*" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxLength=11/></div></div>
            <a href="javascript:;" class="btn-sure"  onclick='zf_redirect_fun()'>确认</a>
            <div class="alert"><a href="javascript:void(0);" onclick="for_suc()">跳过&gt;&gt;</a>注：确认后将无法修改</div>
        </form>
      </div>
</div>
<div class="fbox" id="pop" style="display:none;">
    <div class="sc"><h2>提示</h2>确定不填写推荐人吗？</br>只有这一次机会哦！</div>
    <div class="btns">
        <a href="javascript:;" onclick="cancelAddPant()">取消</a><a href="javascript:;" class="ok" onclick='contrl_procedure(3)'>确定</a>
    </div>
</div>
<div id="mask" style="display:none;">&nbsp;</div>

<div class="wrap" style="display:none;">
    <div class="cgtj">
        <div class="cgtj-t"><h2>恭喜您已成为特权用户！</h2><p>去“惠家有”购物可得到返现哦！</p></div>
        <div class="cgtj-c">
            <p>惠家有介绍</p>
            <div class="img"><img src="../../resources/cfamily/zzz_js/prerogative/images/logo.png" alt="" /></div>
            <ul>
                <li>时尚便捷的一站式家庭购物商城；</li>
                <li>家有购物集团严格质检，100%正品保证；</li>
                <li>支持货到付款、全场包邮，无忧退货；</li>
                <li>每日爆款特价限时购，优惠不只一点点。</li>
            </ul>
        </div>
        <div class="btnd"><a href="javascript:void(0);" class="btn-down" onclick="openApp()">下载“惠家有APP”</a></div>
    </div>
</div>
<script>
var show_time_interval;
var send_vef_type = "reginster";//发送验证码的类型
var hasPMember = false;
var browser='';//浏览器标识

$(function(){
	pmobile = up_urlparam("pmobile",'');
	var regNum = /^1[0-9]{10}$/;
	if(null == pmobile.match(regNum)) {
		pmobile = '';
	} 
	$('#is_get_preprogative').click(function(){ 
	
		<#-- 验证图形验证码是否输入 -->
		var img_verify_code = $('#put_water_code1').val();
 	 	if(img_verify_code.length <= 0) {
 	 		//图形验证码校验失败
 	 		show_message('请输入图片验证码');
 	 		return false;
 	 	}
 	 	
		<#-- 验证输入内容 -->
		var input_mobile = $('#txt').val();
		if( input_mobile.length == 0 ){
			show_message('请输入11位有效手机号');
			return false;
		}
		if(check_input_mobile() == false) {
			return false;
		}
		<#-- 验证结束 -->
		$(this).attr('disabled','disabled');
		
		var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiRegIsUser?api_key=betafamilyhas&mobile="+input_mobile;
		$.ajax({
	 		type:"GET",
	 		url:syurl,
	 		dataType:"json",
	 		success:function(data){
	 			if(data.resultCode == 1){
	 				//已经是会员
	 				if(data.flagUser == true) {
	 					hasPMember = data.hasPMember;
	 					if(data.hasPwd == true) {
	 						//window.location.href = "../shareshopping/downloadapp.ftl?showRebateMsg=0";
	 						contrl_procedure(3);
	 					} else {
	 						//修改发送验证码的类型为忘记密码
	 						send_vef_type = "forgetpassword";
	 						contrl_procedure(1);
	 					}
	 				} else {
	 					contrl_procedure(1);
	 				}
	 			} 
	 		}
	 	});
	})
	
	function check_input_mobile(){
		var val = $('#txt').val();
		var regNum = /^1[0-9]{10}$/;
		if(null != val.match(regNum)) {
			return true;
		} else if( val.length != 0 ){
			show_message('请输入11位有效手机号');
			return false;
		} else {
			return false;
		}
			
	}
	
	
	//加载时进行验证输入的手机号
	check_input_mobile();
	
	//调用流程
	contrl_procedure(0);  
});
</script>
<script language="javascript" type="text/javascript">
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
	//添加上级
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
				 	contrl_procedure(3);
	 			} else {
	 				show_message(data.resultMessage);
	 				$('#is_submit').removeAttr('disabled');
	 			}
	 		}
	 	});
	}
	
	//日期格式化
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
	
	
	//控制流程
	function contrl_procedure(flg){
		if(flg == 3){
			window.location.href = "../shareshopping/downloadapp.ftl";
			return;
		}
		$('.wrap').hide();
		$('.wrap').eq(flg).show();
		if(flg == 1) {
			procedure_registUser();
		} else if(flg == 3) {
			cancelAddPant();
		}
	}
	
	function cancelAddPant(){
		$('#pop').hide();
		$("#mask").hide();
	}
	
	function procedure_registUser() {//注册页面加载
	 	//展示发送验证码的时间
	 	$('#getTime').click(function(){
	 		//填写图形验证码
	 		reflush_verifycode(2);
	 		$(this).parent().parent().find(".wtc").show();
	 	
	 		send_verify(2);
	 	})
	 	
	 	$('#is_submit').click(function(){
	 		<#-- 验证验证码 -->
	 		var reg_verify = /^[0-9]+$/;
	 		var verify_code = $('#pwd_sid').val();
	 		if(null == verify_code.match(reg_verify)) {
		 		show_message('验证码格式错误');
		 		return false;
		 	} 
	 		<#-- 验证验证码end -->
	 		
	 		<#-- 验证输入的密码 -->
	 		var reg_pwd = /^\w{6,16}$/;
	 		var password = $('#input_pwd').val();
	 		if(null == password.match(reg_pwd)) {
		 		show_message('密码格式错误');
		 		return false;
		 	} 
	 		<#-- 验证输入的密码end-->
	 		$('#is_submit').attr('disabled','disabled');
	 	
		 	var login_name = $('#txt').val();
	 		
	 		var param = "";
	 		var syurl="";
	 		if(send_vef_type == "reginster") {
	 			param = "&nickname="+login_name+"&login_name="+login_name+"&client_source=app&password="+password+"&verify_code="+verify_code;
	 			syurl="/cfamily/jsonapi/com_cmall_membercenter_api_UserReginster?api_key=betafamilyhas"+param;
	 			
	 		} else if(send_vef_type == "forgetpassword") {
	 			param = "&login_name="+login_name+"&client_source=app&password="+password+"&verify_code="+verify_code+"&version=1";
	 			syurl="/cfamily/jsonapi/com_cmall_membercenter_api_ForgetPassword?api_key=betafamilyhas"+param;
	 			
	 		}
	 		$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 		
		 			if(data.resultCode == 1){
		 				if(hasPMember == true) {
		 					contrl_procedure(3);
		 				} else {
		 					var reg = '^1[0-9]{10}$';
		 					var pm = up_urlparam("pmobile",'');
			 				if(pm && pm.length > 0 && pm.match(reg)) {
			 					addParentUser(login_name,pm);
						 	} else {
						 		//document.location = "addPmember.html?t_mobile="+login_name;
						 		contrl_procedure(2);
						 	}
		 				}
		 			} else {
		 				show_message(data.resultMessage);
		 				$('#is_submit').removeAttr('disabled');
		 			}
		 		}
		 	});
		 	
		 	
	 	})
	 	
	 	 function send_verify(fg) {
	 	 	//验证图形验证码是否已经输入
	 	 	var img_verify_code = "";
	 	 	var water_code = "";
	 	 	if(fg==2) {//非第一次发送验证码
	 	 		water_code = $("#water_code2").val();
	 	 		img_verify_code = $('#put_water_code2').val();
	 	 	} else {//第一次发送验证码
	 	 		water_code = $("#water_code1").val();
	 	 		img_verify_code = $('#put_water_code1').val();
	 	 	}
	 	 	if(img_verify_code.length <= 0) {
	 	 		//图形验证码校验失败
	 	 		show_message('请输入图片验证码');
	 	 		return false;
	 	 	}
	 	 	$('#tz_alt_id').html("验证码已发至您的手机，马上就能享受返现特权啦！");
		 	$('#getTime').attr('disabled','disabled');
	 		var input_mobile = $('#txt').val();
	 		var syurl="/cfamily/jsonapi/com_cmall_systemcenter_api_MsgSend?api_key=betafamilyhas&send_type="+send_vef_type+"&mobile="+input_mobile+"&water_code="+water_code+"&verify_code="+img_verify_code;
	 		//发送短信
	 		$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				show_time_interval = setInterval(function(){
							var tval = $('#getTime').val();
							var tArr = tval.split('s');
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
								$('#getTime').removeAttr('disabled');
								$('#getTime').val("重新获取验证码");
								$('#tz_alt_id').val("只差一步就能享受返现特权啦！");
							} else {
								if(showTime < 10) {
									$('#getTime').val('0'+showTime+"s后可重发");
								}　else {
									$('#getTime').val(showTime+"s后可重发");
								}
								
							}
							
						},1000);
		 			} else {
						$('#getTime').removeAttr('disabled');
						$('#tz_alt_id').html("只差一步就能享受返现特权啦！");
						show_message(data.resultMessage);
		 			}
		 		}
		 	});
		 }
		//页面加载即发送验证码
		send_verify(1);
	}
	function zf_redirect_fun(){
		<#-- 验证上级的手机号 -->
		var reg = /^1[0-9]{10}$/;
		var parentLoginName = $('#p_mob_input_nb').val();
		if(null == parentLoginName.match(reg)) {
			show_message('输入的手机号格式错误');
			return false;
		} 
		$('#sub_id').attr('disabled','disaled');
		<#-- 验证上级的手机号end -->
		var loginName = $('#txt').val();
		var parentLoginName = $('#p_mob_input_nb').val();
		if(loginName == parentLoginName) {
			show_message('操作失败，请输入微公社成员的手机号');
			$('#sub_id').removeAttr('disabled');
		} else {
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
					 	contrl_procedure(3);
		 			} else {
						show_message('操作失败，请输入微公社成员的手机号');
						$('#sub_id').removeAttr('disabled');
		 			}
		 		}
		 	});
		}
		
	}
	function for_suc(){
		$('#pop').show();
		$("#mask").show();
	} 
	function loadInit() {
	
		browser = { 
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
		}
	
		
	}
	function isAbled_open(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.indexOf("Weibo")>0 || ua.indexOf("MicroMessenger")>0 || ua.match(/MicroMessenger/i)=="micromessenger" || ua.indexOf("qq")>0 || ua.indexOf("QQ")>0){
			return false;
		} else {
			return true;
		}
	}
	function show_message(mesg) {
		new Toast({context:$('body'),message:mesg,top:'50%'}).show();
	}
	function reflush_verifycode(e){
	  var 	s= 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		      var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		      return v.toString(16);
		    });
		if(e == 2) {
			$("#verfiyCodeId2").prop('src','../../verify/image/'+s);
			$("#water_code2").val(s);
		} else {
			$("#verfiyCodeId1").prop('src','../../verify/image/'+s);
			$("#water_code1").val(s);
		}
		
	}
	reflush_verifycode(1);
</script>
</body>
</html>