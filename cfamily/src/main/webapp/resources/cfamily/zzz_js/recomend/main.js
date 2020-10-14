pmobile = up_urlparam("pm",'');
headImg = up_urlparam("headImg",'');
nickNm = decodeUTF8(decodeURIComponent(up_urlparam("nickNm",'')));
if(headImg.length > 0) {
	headImg=unescape(headImg);
	$(".ly_tx  img").attr("src",headImg);
}
if(null != pmobile && pmobile != '' && pmobile.length == 11) {
	var pmV = "";
	if(nickNm.length>0) {
		pmV = nickNm;
	} else {
		pmV = pmobile.substring(0, 3) + "****" + pmobile.substring(pmobile.length - 4);
	}
	$(".liuyan_y p:eq(0)").html("我是"+pmV+"，一直用惠家有买家居日用品，价格实惠质量好~");
}
//更换图形验证码
reflush_verifycode();
$("#inputWaterCode").val("");
/**
 * 判断是否有优惠券
 */
var recommedWeal="";
var turl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetCouponNum?api_key=betafamilyhas&reletiveType=1";
$.ajax({
	type:"GET",
	url:turl,
	dataType:"json",
	success:function(data){
		if(data.resultCode == 1){
	
			if(data.couponNum == 0) {
				/**
				 * 剩余券树为0
				 */
				//window.location = "overMoney.html?headImg="+headImg;
				 $(".lyh_d1").hide();
				 $(".oldUser").hide();
				 
				 $(".lyh_d2").show();
				 $(".oneClass").show();
				 
				 $(".bg_fx").show();
 				 $(".lyh").show()
 				 
			} else {
			  
				if(data.money!=0&&data.many==0){
					$(".libao_p").html(data.money+"元大礼包");
				}
				else if(data.money==0&&data.many!=0){
					$(".libao_p").html(data.many+"张折扣券");
				}
				else if(data.money!=0&&data.many!=0){
					$('.libao_t').addClass('none');
					$(".libao_t1").removeClass('none');
					$(".libao_p1").html(data.money+"元大礼包和"+data.many+"张<br/>折扣券");
				}
				
			}
		} else {
			/**
			 * 接口调用失败 
			 */
			 $(".lyh_d1").hide();
			 $(".oldUser").hide();
			 
			 $(".lyh_d2").show();
			 $(".oneClass").show();
			 
			 $(".bg_fx").show();
				 $(".lyh").show()
		}
	}
});



///********************************************
//添加删除按钮
$(".guanbi_lyh").on('click',function(){
	$(".bg_fx").hide();
	$(".lyh").hide()
});


function subfunc() {
	var mobile = $("#iphone").val();
	var regcode = $("#iphone_code").val();
	var regNum = /^1[0-9]{10}$/;
	if(mobile.length == 0) {
		$(".error").html("请填写手机号").show();
		setTimeout('$(".error").hide()',2500);
		return false;
	} else if(null == mobile.match(regNum)) {
		$(".error").html("手机号码填写不正确").show();
		setTimeout('$(".error").hide()',2500);
		return false;
	} 
	if(regcode.length == 0) {
		$(".error").html("请填写验证码").show();
		setTimeout('$(".error").hide()',2500);
		return false;
	}
	/**
	 * 验证结束，提交信息
	 */
	var parentMobile = "";
	
	if(null != pmobile && pmobile != '' && pmobile.length == 11) {
		parentMobile = "&referrerMobile="+pmobile;
	}
	var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_RecommendUserRegisterApi?api_key=betafamilyhas&mobile="+mobile+"&verifyCode="+regcode+"&recommedWeal"+recommedWeal+parentMobile;
	$.ajax({
 		type:"GET",
 		url:syurl,
 		dataType:"json",
 		success:function(data){
 			if(data.resultCode == 1&&data.returnStatus==true){
 				document.location = "newUser.html?mobile="+mobile+"&couponInfo="+data.couponInfo;
 			} else {
 				$(".error").html(data.resultMessage).show();
 				setTimeout('$(".error").hide()',2500);
 				return ;
 			}
 		}
 	});
	
}
var countdown=60;
function setT(countdown) {
	if (countdown == 0) {
		$('.yzm1').attr('disabled',false);
		$('.yzm1').val('重新获取');
		$('.yzm1').css('color','#f37121');
		$('.yzm1').removeClass('curr');
		//$('.bonus .tips').removeClass('curr');
		return false;
	}else{ 
		$('.yzm1').attr('disabled','disabled'); 
		$('.yzm1').css('color','#999');
		$('.yzm1').val('已发送(' + countdown + 'S)');
		$('.yzm1').addClass('curr');
		$('.bonus .tips').addClass('curr');
		countdown--; 
	} 
	setTimeout(function() { 
		setT(countdown);
	},1000) 
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
function reflush_verifycode(e){
  var 	s= 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	      var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	      return v.toString(16);
	    });
	$("#verfiyCodeId").prop('src','../../verify/image/'+s);
	$("#water_code").val(s);
	
}
function decodeUTF8(str){  
	return str.replace(/(\\u)(\w{4}|\w{2})/gi, function($0,$1,$2){  
	    return String.fromCharCode(parseInt($2,16));  
	});   
	}  


function settime(val) {


	var mobile = $("#iphone").val();
	var regNum = /^1[0-9]{10}$/;
/*
 * 验证手机号
 */
if(mobile.length == 0) {
	$(".error").html("手机号码不能为空").show();
	setTimeout('$(".error").hide()',2500);
	return false;
} else if(null == mobile.match(regNum)) {
	$(".error").html("手机号码不正确").show();
	setTimeout('$(".error").hide()',2500);
	return false;
} 

var water_code = $("#water_code").val();
var img_verify_code = $("#code").val();
if(img_verify_code.length <= 0) {
	$(".error").html("请填写图片验证码").show();
	setTimeout('$(".error").hide()',2500);
	return false;
}

var syurl="/cfamily/jsonapi/com_cmall_systemcenter_api_MsgSend?api_key=betafamilyhas&send_type=reginster&mobile="+mobile+"&water_code="+water_code+"&verify_code="+img_verify_code;;
var flag = false;
//发送短信
$.ajax({
		type:"GET",
		url:syurl,
		dataType:"json",
		success:function(data){
			if(data.resultCode == 1){
			    $(".error").html("短信已发送,请注意查收").show();
			    setTimeout('$(".error").hide()',2000);
	        setT(countdown);
	        return;

			} else if(data.resultCode == 949702105) { 
				/**
				 * 判断是否领取过优惠券
				 */
				/// com_cmall_familyhas_api_ApiForCheckOldUser 
				var getInfoUrl  = "/cfamily/jsonapi/com_cmall_familyhas_api_ApiForCheckOldUser?api_key=betafamilyhas&mobile="+mobile;
				$.ajax({
			 		type:"GET",
			 		url:getInfoUrl,
			 		dataType:"json",
			 		success:function(data){
			 			if(data.resultCode == 1) {
			 				/**
			 				 * 领过优惠券
			 				 */
			 				window.location = "newUserNothing.html?mobile="+mobile;
			 			} else {
			 				/**
			 				 * 没有领过优惠券，老用户领取提示修改为弹窗
			 				 */
			 				//window.location = "oldUserNothing.html";
			 				//alert("已经存在");
			 				
			 				
			 				 $(".lyh_d2").hide();
			 				 $(".oneClass").hide();
							 
							 
							 $(".lyh_d1").show();
							 $(".oldUser").show();
							 
							
			 				 $(".bg_fx").show();
			 				 $(".lyh").show()

			 			} 			 		
			 		}
				})
			} else {
				$(".error").html(data.resultMessage).show();
	        setTimeout('$(".error").hide()',2500);
	        return false;
			}
		}
	});
}

