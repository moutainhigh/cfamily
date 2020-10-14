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
		//隐藏域赋值
		$('#tmobile').val(up_urlparam('t_mobile',""));
	
	}) 
	function zf_redirect_fun(){
		<#-- 验证上级的手机号 -->
		var reg = /^1[0-9]{10}$/;
		var parentLoginName = $('#p_mob_input_nb').val();
		if(null == parentLoginName.match(reg)) {
			$('#show_message').html('输入的手机号格式错误');
			setTime();
			return false;
		} 
		$('#sub_id').attr('disabled','disaled');
		<#-- 验证上级的手机号end -->
		var loginName = $('#tmobile').val();
		var parentLoginName = $('#p_mob_input_nb').val();
		if(loginName == parentLoginName) {
			$('#show_message').html('操作失败，请输入微公社成员的手机号');
			setTime();
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
					 	zf_redirect_Sucfun();
		 			} else {
		 				$('#show_message').html('操作失败，请输入微公社成员的手机号');
						setTime();
						$('#sub_id').removeAttr('disabled');
		 			}
		 		}
		 	});
		}
		
	}
	function zf_redirect_Sucfun(){
		document.location = "getPrerogativeSuc.html";
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
<div class="am-padding-top am-padding-bottom-sm am-padding-left-sm am-u-sm-10 am-u-lg-centered">填写推荐好友的手机号，你购物他也有返现哦！</div>
<div class="am-g">
  <div class="am-u-md-8 am-u-sm-centered">
    <form class="am-form">
      <fieldset class="am-form-set">
      <input type="hidden" value="" class="am-form-field am-input-lg" id="tmobile" />
        <input id="p_mob_input_nb" type="text" placeholder="请输入推荐人手机号" class="am-input-lg easyui-numberbox">
      </fieldset>
      <span class="am-u-sm-2 am-u-sm-centered ">
  <div class="am-u-sm-11 am-u-sm-centered am-margin-top">
  <button id="sub_id" type="button" class="am-btn am-btn-default am-btn-block am-btn-lg" onclick='zf_redirect_fun()'>确认</button>
  </div>
    </form>
    <div class="am-cf am-margin-top">
  <span class="am-fl e-tc am-padding-left">注：确认后将无法修改</span>
 <span class="am-fr am-padding-right"><a class="am-link-muted e-bcp" data-am-modal="{target: '#my-modal-loading'}">跳过>></a></span>
<!-- 弹框 ]]-->
<div class="am-modal am-modal-confirm" tabindex="-1" id="my-modal-loading">
  <div class="am-modal-dialog">
    <div class="am-modal-hd"><strong>提示</strong></div>
    <div class="am-modal-bd">
      确定不填写推荐人吗？<br>只有这一次机会哦!
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm onclick='zf_redirect_Sucfun()'>确定</span>
    </div>
  </div>
</div>
<!-- 弹框 ]]-->
</div>
  </div>
</div>
<div id="is_reg_mobile" class="am-u-sm-9 am-u-sm-centered am-margin-top-xs"><span class="e-hk am-padding am-block am-text-center " id="show_message">请输入11位有效手机号</span></div>
</div>


</body>
</html>
