var cfamily = {
	temp : {
		user_token : "",
		user_name : "",
		class_id : "",
		sku_list : {},
		is_callback_error : "0"
	},
	config : {
		cookie_user_token : 'cfamily_usertoken',
		cookie_product_skulist : 'cfamily_skulist',
		cookie_product_buyinfo : 'cfamily_buyinfo',
		cookie_parent_mobile : 'cfamily_parent_mobile',
		encryptArr : ['j','c','a','b','u','i','p','o','y','q','w','x','m'],
		mobileHanler : {
			start : 3,
			end : 7,
			c : "*"
		},
		isPaging : false,
		totalPage : 0,
		isLoading : false,
		isCrollObj : ""
	},
	paging : {
		offset : 0,
		limit : 10
	},
	// API列表
	api_list : {
		//发送短信验证码
		send_mobile_code : {
			api_name : "com_cmall_systemcenter_api_MsgSend"
		},
		//区域信息
		store_message :{
			api_name : "com_cmall_familyhas_api_ApiForGetStoreDistrict"
		},
		getProductDetail : {
			api_name : "com_cmall_familyhas_api_ApiGetSkuInfo",
			flag_token : false
		},
		//活动商品详情接口
		getEventProductDetail : {
			api_name : "com_cmall_familyhas_api_ApiGetEventSkuInfo",
			flag_token : false
		},
		//活动商品sku接口
		getEventProductSkuList : {
			api_name : "com_srnpr_xmasproduct_api_ApiSkuInfo",
			flag_token : false
		},
		order_confirm: {
			api_name : "com_cmall_familyhas_api_APiOrderConfirm",
			flag_token : false
		},
		save_address_and_login: {
			api_name : "com_cmall_familyhas_api_ApiForSaveAddressAndLogin",
			flag_token : false
		},
		get_available_coupon: {
			api_name : "com_cmall_familyhas_api_ApiGetAvailableCoupon",
			flag_token : true
		},
		get_available_coupon: {
			api_name : "com_cmall_familyhas_api_ApiGetAvailableCoupon",
			flag_token : true
		},
		get_coupon_code_exchange: {
			api_name : "com_cmall_familyhas_api_ApiForCouponCodeExchange",
			flag_token : true
		},
		create_order: {
			api_name : "com_cmall_familyhas_api_APiCreateOrder",
			flag_token : true
		},
		get_order_detail: {
			api_name : "com_cmall_familyhas_api_ApiOrderDetails",
			flag_token : true
		},
		get_wx_pay_info: {
			api_name: "com_cmall_familyhas_api_ApiPaymentAll",
			flag_token: true
		},
		get_wx_pay_info2: {
			api_name: "com_cmall_familyhas_api_ApiWechatNetParams",
			flag_token: false
		}
		//
	},
	//加密，解密url的&，？符号
	//true为加密，false为解密
	url_param_mix : function(s_url,isMix){
		if(!s_url){
			return "";
		}
		var param = s_url;
		if(isMix){
			param = param.replace(/[\?]/g, "1M1");
			param = param.replace(/[\&]/g, "1M2");
			param = param.replace(/[\=]/g, "1M3");
		}else{
			param = param.replace(/(1M1)/g, "?");
			param = param.replace(/(1M2)/g, "&");
			param = param.replace(/(1M3)/g, "=");
		}
		return param;
	},
	// 初始化请求 会将对应的参数写入cookie中
	init_request : function() {

		// 定义usertoken
		var s_usertoken = zapbase.up_urlparam('web_api_token');
		if (s_usertoken && s_usertoken != "" && "undefined" != s_usertoken) {
			zapbase.cookie(cfamily.config.cookie_user_token, s_usertoken);
		}

		// 定义apikey
		var s_apikey = zapbase.up_urlparam('api_key');

		if (s_apikey && s_apikey != "" && "undefined" != s_usertoken) {
			zapbase.cookie(capi.config.cookie_api_key, s_apikey);
		}

	},
	// 跳转到页面
	href_page : function(s_url) {
		location.href = s_url;
	},
	// 调用API
	call_api : function(s_api_name, o_map_params, f_callback) {

		var o_config = cfamily.api_list[s_api_name]; 

		if (o_config == null) {
			capi.show_error("m_api_fail_msg");
			return false;
		}

		var s_usertoken = "";

		if (o_config.flag_token) {

			s_usertoken = cfamily.up_user_token();

		}
		capi.call_api({
			target : o_config.api_name,
			params : o_map_params,
			callback : f_callback,
			token : s_usertoken
		});
	},
	// 获取用户的token信息
	up_user_token : function() {
		if (!cfamily.temp.user_token || cfamily.temp.user_token == "" || "undefined" == cfamily.temp.user_token) {
			cfamily.temp.user_token = zapbase
					.cookie(cfamily.config.cookie_user_token);
		}
		return cfamily.temp.user_token;
	},
	up_user_name : function() {
		if (!cfamily.temp.user_name || cfamily.temp.user_name == "" || "undefined" == cfamily.temp.user_name) {
			cfamily.temp.user_name = zapbase
					.cookie(cfamily.config.cookie_user_name);
		}
		return cfamily.temp.user_name;
	},
	up_class_id : function() {
		if (!cfamily.temp.class_id || cfamily.temp.class_id == "" || "undefined" == cfamily.temp.class_id) {
			cfamily.temp.class_id = zapbase
					.cookie(cfamily.config.cookie_class_id);
		}
		return cfamily.temp.class_id;
	},
	up_product_sku_list : function(){
		if (typeof cfamily.temp.sku_list === "object" && !(cfamily.temp.sku_list instanceof Array)){
			var hasProp = false;
			for (var prop in cfamily.temp.sku_list){
				hasProp = true;
				break;
			}
			if(!hasProp){
				cfamily.temp.sku_list = JSON.parse(zapbase.cookie(cfamily.config.cookie_product_skulist));
			}
		}
		return cfamily.temp.sku_list;
	},
	/**
	 * option格式定义：{"type":"调用类型","obj":{}}
	 * type值：personalcenter=打开修改个人信息页面，open_app ==精彩发现调用客户端，page_version ==打开版本检测页面，back_link ==意见反馈的返回按钮，
	 *        user_login ==登陆/注册成功的调用,close_window=带返回的按钮,logout=退出登录,weixin_login=微信登录，addFriends=添加好友，pushInfoSetting=消息设置,favorites=打开我的收藏
	 * obj为各个不同调用方法的实际对象参数,列如：{"user_token":"用户的token"}
	 */
	jsInvokeClient : function(option){
		var istype = root.load();
		if(istype =='android'){
			try{
				window.notify.notifyOnAndroid(JSON.stringify(option));
			}catch(e){
				console.log(e.message);
			}
		}else if(istype =='ios'){
			var base = new Base64();
			var baseEncodeStr = base.encode(JSON.stringify(option));
			baseEncodeStr = baseEncodeStr.replace(/[\=]/g, "");
			baseEncodeStr = baseEncodeStr.replace(/[\+]/g, "//");
			window.location.href="pscoc://"+baseEncodeStr;
		}
	},
	//js请求客户端并返回数据
	jsRequestClient : function(){
		var result = "";
		var istype = root.load();
		try{
			if(istype =='android'){
				result = window.notify.getSQNum();
			}else if(istype =='ios'){
				result = window.weigongshe.getSQNum();
			}
		}catch(e){
			console.log(e.message);
		}
		return result;
	},
	delayJumpToTaget : function(option){
		setTimeout(function() { 
			cfamily.href_page(option.url); 
		},option.delayed_time);
	},
	set_default_apikey:function(default_key){
		
		
		// 判断apikey如果为空，则从cookie中取
		if (capi.config.api_key.length <= 0) {

			capi.config.api_key = zapbase.cookie(capi.config.cookie_api_key);

		}
		
		if (!capi.config.api_key || "undefined" ==capi.config.api_key || capi.config.api_key == "") {
			if(default_key){
				capi.config.api_key = default_key;
			}else {
				capi.config.api_key = "betafamilyhas";
			}
		}
		return capi.config.api_key;
	},
	replaceFunc : function(s_str,config){
		if(!s_str || s_str.length == 0)
			return "";
		var count = parseInt(config.end) - parseInt(config.start);
		var repalceChar = "";
		for(var i=0;i<count;i++){
			repalceChar += config.c;
		} 
		return s_str.substring(0,config.start) + repalceChar + s_str.substring(config.end);
	},
	//日期字符串转为YYYY-MM-D
	convertDate : function(dateStr,type){
		var dateObj=new Date(dateStr.replace(/-/g,"/"));
		if(type == "yyyy-mm-dd"){
			return dateObj.getFullYear() +"."+(dateObj.getMonth()+1)+"."+dateObj.getDate();
		}
		if(type == "mm-dd"){
			return (dateObj.getMonth()+1)+"."+dateObj.getDate();
		}
	},
	//微信前端配置
	wxConfig: function(order_code) {
		var _uri = encodeURIComponent('http://api-family.syapi.ichsy.com/cfamily/wechatWAP?orderCode='+order_code);
		return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7c73f526ee2324e8&redirect_uri="+_uri+"&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	},
	//微信前端NET配置
	wxConfig_test: function(order_code, mac, tradetime) {
		//var _uri = encodeURIComponent("http://http://qhbeta-cfamily.qhw.srnpr.com/cfamily/wechatWAPNet?orderCode="+order_code);//测试环境
		//return "http://gate.eplans.cn/Merchant/ReceiveMerchantRequest.aspx?merchantid=13&tradetype=oauth&orderno=b&TradeCode=WeiXin&channelid=5&CallBackURL="+_uri+"&mac="+mac+"&tradetime="+tradetime;//测试环境
		
		var _uri = encodeURIComponent("http://api-family.syapi.ichsy.com/cfamily/wechatWAPNet?orderCode="+order_code);//生产环境
		return "http://gate.ichsy.com/Merchant/ReceiveMerchantRequest.aspx?merchantid=ichsywap&tradetype=oauth&orderno=b&TradeCode=WeiXin&channelid=6&CallBackURL="+_uri+"&mac="+mac+"&tradetime="+tradetime;//生产环境
	}
};


var capi = {

	// 配置
	config : {

		base_url : "../../jsonapi/",
		// apiKey
		api_key : '',

		// cookie中存在的apikey的名称
		cookie_api_key : 'cfamily_apikey'
	},
	location : {
		"m_api_fail_msg" : "调用接口失败：无法找到接口配置",
		"m_api_fail_nokey" : "调用接口失败：没有对应的APIKEY"
	},
    ajaxSetup : function(flag){
    	if(flag == false){
    		$.ajaxSetup({async : false});
    	}else{
    		$.ajaxSetup({async : true});
    	}
    	
    },
 // 调用API
	call_api : function(o_set) {

		
		cfamily.set_default_apikey();
		// 定义链接地址
		var s_url = capi.config.base_url + o_set.target;

		$.post(s_url, {
			// apikey
			api_key : capi.config.api_key,
			// 输入参数
			api_input : zapbase.json_stringify(o_set.params),
			// 用户信息
			api_token : o_set.token,
			// 目标地址
			api_target : o_set.target
		}, function(data, status) {

			var o_result = data;
			if (o_result.resultCode == "1") {
				o_set.callback(o_result);
			} else {
				if(cfamily.temp.is_callback_error == '1'){
					o_set.callback(o_result);
				}else{
					capi.show_message(o_result.resultMessage);
					if(969905919 == o_result.resultCode){
						cfamily.callback_user_logout(o_result);
					}
				}
			}

		});
	},
	//一般的ajax调用
	call_ajax : function(o_set){
		// 定义链接地址
		var s_url = capi.config.base_url + o_set.target;
		$.ajax({
			url:s_url,
			data:o_set.params,
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success : function(data){
				var o_result = $.xml2json(data);
				if (o_result.resultCode == "1") {
					o_set.callback(o_result);
				} else {
					if(cfamily.temp.is_callback_error == '1'){
						o_set.callback(o_result);
					}else{
						capi.show_message(o_result.resultMessage);
					}
				}
			}
		});
	},
	//以jsonp形式调用
	call_api_jsonp : function(o_set){
		$.ajax({
			type: "GET",
			url: wapi.config.base_url,
			data: {// apikey
				api_key : wapi.config.api_key,
				// 输入参数
				api_input : zapbase.json_stringify(o_set.params),
				// 用户信息
				api_token : o_set.token,
				// 目标地址
				api_target : o_set.target
				},
			dataType: "jsonp",
			jsonp: "api_callback",
			success: function(data){
				var o_result = data;
				if (o_result.resultCode == "1") {
					o_set.callback(o_result);
				} else {
					wapi.show_message(o_result.resultMessage);
				}
			}
		});
	},
	// 调用失败
	show_error : function(s_code) {
		new Toast({context:$('body'),message:capi.location[s_code],top:'60%'}).show();
	},
	// 显示消息
	show_message : function(s_message,callback,type,order,cancelName,buttonName) {
		setTimeout(function() { 
			if($("#alert-layer").length && $("#alert-layer").length > 0)
				return;
			if(type && type == 2){
				$.kw.confirm(s_message,callback,order,cancelName,buttonName);
			} else if(type && type == 3) {
				$.kw.confirmButton(s_message,callback,cancelName);
			}else{
				new Toast({context:$('body'),message:s_message,top:'60%'}).show();
			}
		},700);
	}

};

var commonJs= {
	//判断按钮是否可用
	isDis : function(t){
		return $(t).attr('unable')=="true";
	},
	//设置按钮功能,包括不可用和可用
	//{obj:object,isUsable:true or false,msg:"信息"}
	setTagFunc : function(option){
		if(option.obj){
			if(option.isUsable && commonJs.isDis(option.obj))
				return true;
			$(option.obj).attr("unable",option.isUsable);
			if(option.msg)
				$(option.obj).html(option.msg);
		}
	}
};
var validateRegExp = {
	intege : "^-?[1-9]\\d*$", // 整数
	intege1 : "^[1-9]\\d*$", // 正整数
	intege2 : "^-[1-9]\\d*$", // 负整数
	num : "^([+-]?)\\d*\\.?\\d+$", // 数字
	num1 : "^[1-9]\\d*|0$", // 正数（正整数 + 0）
	num2 : "^-[1-9]\\d*|0$", // 负数（负整数 + 0）
	ascii : "^[\\x00-\\xFF]+$", // 仅ACSII字符
	chinese : "^[\\u4e00-\\u9fa5]+$", // 仅中文
	date : "^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$", // 日期
	email : "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", // 邮件
	numAndLetter : "^[A-za-z0-9]+$",
	letter : "^[A-Za-z]+$", // 字母
	letter_l : "^[a-z]+$", // 小写字母
	letter_u : "^[A-Z]+$", // 大写字母
	mobile : "^(0|86|17951)?(1)[0-9]{10}$", // 手机
	notempty : "^\\S+$", // 非空
	password : "^.*[A-Za-z\\w_-][0-9]+.*$", // 密码
	fullNumber : "^[0-9]+$", // 数字
	tel : "^[0-9\-()（）]{7,18}$", // 电话号码的函数(包括验证国内区号,国际区号,分机号)
	space : /\s+/g ,
	numberAndTwoDecimals : "^(([1-9]{1}\\d*)|([1-9]{1}\\d*\\.[0-9]{1,2})|([1-9]{1}\\d*\\.))$"
};

//验证规则
var validateRules = {
	isNull : function(str) {
		return (str == "" || typeof str != "string");
	},
	isNumber : function(str) {
		return new RegExp(validateRegExp.fullNumber).test(str);
	},
	betweenLength : function(str, _min, _max) {
		return (str.length >= _min && str.length <= _max);
	},
	isEmail : function(str) {
		return new RegExp(validateRegExp.email).test(str);
	},
	isTel : function(str) {
		return new RegExp(validateRegExp.tel).test(str);
	},
	isMobile : function(str) {
		return new RegExp(validateRegExp.mobile).test(str);
	},
	checkType : function(element) {
		return (element.attr("type") == "checkbox"
				|| element.attr("type") == "radio" || element.attr("rel") == "select");
	},
	checkSpace : function(str){
		return new RegExp(validateRegExp.space).test(str);
	},
	checkNumAndTwoDecimals : function(str){
		return new RegExp(validateRegExp.numberAndTwoDecimals).test(str);
	},
	checkNumAndLetter : function(str){
		return new RegExp(validateRegExp.numAndLetter).test(str);
	},
	isChinese: function(str) {
		return new RegExp(validateRegExp.chinese).test(str);
	}
};
var root = {
	load: function () {
		
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
			//var center_xz1 = document.getElementById("center_xz");
			//center_xz1.style.position = 'absolute';
			// var div2 = document.getElementById("android");
			//div2.style.display = 'none';
			return "ios";
		} 
		else if (browser.versions.android) {
			//var center_xz1 = document.getElementById("center_xz");
			//center_xz1.style.position = 'absolute';
			//var div2 = document.getElementById("ios");
			//div2.style.display = 'none';
			return "android";
		}
	}
}

$.kw = {  
        speed      : 400, //默认速度 可修改  
        buttonName : "确认", //确定按钮默认名称 可修改  
        cancel     : "取消", //取消按钮默认名称 可修改  
        content    : "确定执行此操作吗?",  
        order	   : 0,//默认按钮排序为正序，即【确认】【取消】,1为倒序 即【取消】【确认】
         //移除遮盖层  
        del : function () {  
            $("#alert-layer").remove();  
        },  
        //提示  
        confirm : function (sContent, callBack, sOrder, sCancelName, sButtonName) {  
            var content = sContent || this.content; 
            var order = sOrder || this.order;
            var cancelName = sCancelName || this.cancel; 
            var buttonName = sButtonName || this.buttonName;
            
            var layer = "<div id='alert-layer'><div id='alert-container'><div id='alert-content' class='confirm_alert_content'>" + content + "</div><div class='btns'>";
            if(order == 1){
            	layer += "<a id='alert-cancel' class='back'>" + cancelName + "</a><a id='alert-button'>" + buttonName + "</a>";
            }else{
            	layer += "<a id='alert-button' class='back'>" + buttonName + "</a><a id='alert-cancel'>" + cancelName + "</a>";
            }
            layer += "</div></div></div>";  
              
            $(layer).fadeIn(this.speed).appendTo("body");  
            this.setting();  
            $("#alert-button").focus(); //获得焦点  
            $("#alert-cancel").bind("click", this.del); //移除层  
            $("#alert-button").bind("click", function () {  
                $.kw.del();  
                if (callBack) {  
                    callBack();  
                };  
                  
            }); //移除层  
        }, 
        //一个按钮提示 
        confirmButton : function (sContent, callBack, sCancelName) {  
        	var content = sContent || this.content; 
            var cancelName = sCancelName || this.cancel; 
            
            var layer = "<div id='alert-layer'><div id='alert-container' style='top:50%;margin-top:-48%;'><div id='alert-content' style='text-align: left;padding: 1em 2em 1em;' class='confirm_alert_content'>" + content + "</div><div class='btns' style='display: block;margin: 0em 2em 2em;border:none;background: #FF5E5E;border-radius: 10px;'>";
        	layer += "<a id='alert-cancel' class='back' style='width: 100%;border:none;cursor:pointer;color:#fff;'>" + cancelName + "</a>";
            layer += "</div></div></div>";  
              
            $(layer).fadeIn(this.speed).appendTo("body");  
            this.setting();  
            $("#alert-button").focus(); //获得焦点  
            $("#alert-cancel").bind("click", this.del); //移除层  
            $("#alert-button").bind("click", function () {  
                $.kw.del();  
                if (callBack) {  
                    callBack();  
                };  
                  
            }); //移除层  
        },
        //设置背景层与内位置  
        setting : function () {  
           /* var bcw = document.documentElement.clientWidth,  
                bch = document.documentElement.clientHeight,  
                //bsh = document.documentElement.scrollHeight,  
                aw  = $("#alert-container").width() / 2,  
                ah  = $("#alert-container").height() / 2;  */
            $("#alert-layer").css("height", "100%");  
           /* $("#alert-container").css({  
                "top"  : bch / 2 - ah,  
                "left" : bcw / 2 - aw  
            });  */
        }  
    //$.kw  End   
    };

/*! 
 * Licensed under the MIT License (LICENSE.txt).
 *
 * Version: 3.1.12
 *
 * Requires: jQuery 1.2.2+
 */
!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):"object"==typeof exports?module.exports=a:a(jQuery)}(function(a){function b(b){var g=b||window.event,h=i.call(arguments,1),j=0,l=0,m=0,n=0,o=0,p=0;if(b=a.event.fix(g),b.type="mousewheel","detail"in g&&(m=-1*g.detail),"wheelDelta"in g&&(m=g.wheelDelta),"wheelDeltaY"in g&&(m=g.wheelDeltaY),"wheelDeltaX"in g&&(l=-1*g.wheelDeltaX),"axis"in g&&g.axis===g.HORIZONTAL_AXIS&&(l=-1*m,m=0),j=0===m?l:m,"deltaY"in g&&(m=-1*g.deltaY,j=m),"deltaX"in g&&(l=g.deltaX,0===m&&(j=-1*l)),0!==m||0!==l){if(1===g.deltaMode){var q=a.data(this,"mousewheel-line-height");j*=q,m*=q,l*=q}else if(2===g.deltaMode){var r=a.data(this,"mousewheel-page-height");j*=r,m*=r,l*=r}if(n=Math.max(Math.abs(m),Math.abs(l)),(!f||f>n)&&(f=n,d(g,n)&&(f/=40)),d(g,n)&&(j/=40,l/=40,m/=40),j=Math[j>=1?"floor":"ceil"](j/f),l=Math[l>=1?"floor":"ceil"](l/f),m=Math[m>=1?"floor":"ceil"](m/f),k.settings.normalizeOffset&&this.getBoundingClientRect){var s=this.getBoundingClientRect();o=b.clientX-s.left,p=b.clientY-s.top}return b.deltaX=l,b.deltaY=m,b.deltaFactor=f,b.offsetX=o,b.offsetY=p,b.deltaMode=0,h.unshift(b,j,l,m),e&&clearTimeout(e),e=setTimeout(c,200),(a.event.dispatch||a.event.handle).apply(this,h)}}function c(){f=null}function d(a,b){return k.settings.adjustOldDeltas&&"mousewheel"===a.type&&b%120===0}var e,f,g=["wheel","mousewheel","DOMMouseScroll","MozMousePixelScroll"],h="onwheel"in document||document.documentMode>=9?["wheel"]:["mousewheel","DomMouseScroll","MozMousePixelScroll"],i=Array.prototype.slice;if(a.event.fixHooks)for(var j=g.length;j;)a.event.fixHooks[g[--j]]=a.event.mouseHooks;var k=a.event.special.mousewheel={version:"3.1.12",setup:function(){if(this.addEventListener)for(var c=h.length;c;)this.addEventListener(h[--c],b,!1);else this.onmousewheel=b;a.data(this,"mousewheel-line-height",k.getLineHeight(this)),a.data(this,"mousewheel-page-height",k.getPageHeight(this))},teardown:function(){if(this.removeEventListener)for(var c=h.length;c;)this.removeEventListener(h[--c],b,!1);else this.onmousewheel=null;a.removeData(this,"mousewheel-line-height"),a.removeData(this,"mousewheel-page-height")},getLineHeight:function(b){var c=a(b),d=c["offsetParent"in a.fn?"offsetParent":"parent"]();return d.length||(d=a("body")),parseInt(d.css("fontSize"),10)||parseInt(c.css("fontSize"),10)||16},getPageHeight:function(b){return a(b).height()},settings:{adjustOldDeltas:!0,normalizeOffset:!0}};a.fn.extend({mousewheel:function(a){return a?this.bind("mousewheel",a):this.trigger("mousewheel")},unmousewheel:function(a){return this.unbind("mousewheel",a)}})});

function Base64() {  
    // private property  
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";  
   
    // public method for encoding  
    this.encode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = _utf8_encode(input);  
        while (i < input.length) {  
            chr1 = input.charCodeAt(i++);  
            chr2 = input.charCodeAt(i++);  
            chr3 = input.charCodeAt(i++);  
            enc1 = chr1 >> 2;  
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
            enc4 = chr3 & 63;  
            if (isNaN(chr2)) {  
                enc3 = enc4 = 64;  
            } else if (isNaN(chr3)) {  
                enc4 = 64;  
            }  
            output = output +  
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +  
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);  
        }  
        return output;  
    };  
   
    // public method for decoding  
    this.decode = function (input) {  
        var output = "";  
        var chr1, chr2, chr3;  
        var enc1, enc2, enc3, enc4;  
        var i = 0;  
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");  
        while (i < input.length) {  
            enc1 = _keyStr.indexOf(input.charAt(i++));  
            enc2 = _keyStr.indexOf(input.charAt(i++));  
            enc3 = _keyStr.indexOf(input.charAt(i++));  
            enc4 = _keyStr.indexOf(input.charAt(i++));  
            chr1 = (enc1 << 2) | (enc2 >> 4);  
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);  
            chr3 = ((enc3 & 3) << 6) | enc4;  
            output = output + String.fromCharCode(chr1);  
            if (enc3 != 64) {  
                output = output + String.fromCharCode(chr2);  
            }  
            if (enc4 != 64) {  
                output = output + String.fromCharCode(chr3);  
            }  
        }  
        output = _utf8_decode(output);  
        return output;  
    };  
   
    // private method for UTF-8 encoding  
    _utf8_encode = function (string) {  
        string = string.replace(/\r\n/g,"\n");  
        var utftext = "";  
        for (var n = 0; n < string.length; n++) {  
            var c = string.charCodeAt(n);  
            if (c < 128) {  
                utftext += String.fromCharCode(c);  
            } else if((c > 127) && (c < 2048)) {  
                utftext += String.fromCharCode((c >> 6) | 192);  
                utftext += String.fromCharCode((c & 63) | 128);  
            } else {  
                utftext += String.fromCharCode((c >> 12) | 224);  
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);  
                utftext += String.fromCharCode((c & 63) | 128);  
            }  
   
        }  
        return utftext;  
    };
   
    // private method for UTF-8 decoding  
    _utf8_decode = function (utftext) {  
        var string = "";  
        var i = 0;  
        var c = c1 = c2 = 0;  
        while ( i < utftext.length ) {  
            c = utftext.charCodeAt(i);  
            if (c < 128) {  
                string += String.fromCharCode(c);  
                i++;  
            } else if((c > 191) && (c < 224)) {  
                c2 = utftext.charCodeAt(i+1);  
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));  
                i += 2;  
            } else {  
                c2 = utftext.charCodeAt(i+1);  
                c3 = utftext.charCodeAt(i+2);  
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));  
                i += 3;  
            }  
        }  
        return string;  
    };  
};

//JavaScript Document
/** 
 * 模仿android里面的Toast效果，主要是用于在不打断程序正常执行的情况下显示提示数据 
 * @param config 
 * @return 
 */  
var Toast = function(config){  
    this.context = config.context==null?$('body'):config.context;//上下文  
    this.message = config.message;//显示内容  
    this.time = config.time==null?5000:config.time;//持续时间  
    this.left = config.left;//距容器左边的距离  
    this.top = config.top;//距容器上方的距离  
    this.init();  
};  
var msgEntity;  
Toast.prototype = {  
    //初始化显示的位置内容等  
    init : function(){  
        $("#toastMessage").remove();  
        //设置消息体  
        var msgDIV = new Array();  
        msgDIV.push('<div id="toastMessage">');  
        msgDIV.push('<span class="stxt">'+this.message+'</span>');  
        msgDIV.push('<span class="sbg">&nbsp;</span></div>');  
        msgEntity = $(msgDIV.join('')).appendTo(this.context);  
        //设置消息样式  
        var left = this.left == null ? this.context.width()/2-msgEntity.find('span').width()/2 : this.left;  
        var top = this.top == null ? '20px' : this.top;  
        //msgEntity.css({position:'fixed',top:top,'z-index':'9999',left:left,'background-color':'black',color:'white','font-size':'1em',padding:'10px',margin:'10px'});  
		msgEntity.css({top:top,left:left}); 
        msgEntity.hide();  
    },  
    //显示动画  
    show :function(){  
        //msgEntity.fadeIn(this.time/2);  
        //msgEntity.fadeOut(this.time/2); 
		msgEntity.fadeIn(this.time/5);  
		msgEntity.fadeOut(this.time/5); 
    }  
          
};

var displayHelp={
	defaultValue : function(val,defaultValue){
		if(val=='' || val==undefined){
			return defaultValue;
		}
		return val;
	}
};
/*
### jQuery XML to JSON Plugin v1.0 - 2008-07-01 ###
* http://www.fyneworks.com/ - diego@fyneworks.com
* Dual licensed under the MIT and GPL licenses:
*   http://www.opensource.org/licenses/mit-license.php
*   http://www.gnu.org/licenses/gpl.html
###
Website: http://www.fyneworks.com/jquery/xml-to-json/
*/
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}(';5(10.M)(w($){$.N({11:w(j,k){5(!j)t{};w B(d,e){5(!d)t y;6 f=\'\',2=y,E=y;6 g=d.x,12=l(d.O||d.P);6 h=d.v||d.F||\'\';5(d.G){5(d.G.7>0){$.Q(d.G,w(n,a){6 b=a.x,u=l(a.O||a.P);6 c=a.v||a.F||\'\';5(b==8){t}z 5(b==3||b==4||!u){5(c.13(/^\\s+$/)){t};f+=c.H(/^\\s+/,\'\').H(/\\s+$/,\'\')}z{2=2||{};5(2[u]){5(!2[u].7)2[u]=p(2[u]);2[u][2[u].7]=B(a,R);2[u].7=2[u].7}z{2[u]=B(a)}}})}};5(d.I){5(d.I.7>0){E={};2=2||{};$.Q(d.I,w(a,b){6 c=l(b.14),C=b.15;E[c]=C;5(2[c]){5(!2[c].7)2[c]=p(2[c]);2[c][2[c].7]=C;2[c].7=2[c].7}z{2[c]=C}})}};5(2){2=$.N((f!=\'\'?A J(f):{}),2||{});f=(2.v)?(D(2.v)==\'16\'?2.v:[2.v||\'\']).17([f]):f;5(f)2.v=f;f=\'\'};6 i=2||f;5(k){5(f)i={};f=i.v||f||\'\';5(f)i.v=f;5(!e)i=p(i)};t i};6 l=w(s){t J(s||\'\').H(/-/g,"18")};6 m=w(s){t(D s=="19")||J((s&&D s=="K")?s:\'\').1a(/^((-)?([0-9]*)((\\.{0,1})([0-9]+))?$)/)};6 p=w(o){5(!o.7)o=[o];o.7=o.7;t o};5(D j==\'K\')j=$.S(j);5(!j.x)t;5(j.x==3||j.x==4)t j.F;6 q=(j.x==9)?j.1b:j;6 r=B(q,R);j=y;q=y;t r},S:w(a){6 b;T{6 c=($.U.V)?A 1c("1d.1e"):A 1f();c.1g=W}X(e){Y A L("Z 1h 1i 1j 1k 1l")};T{5($.U.V)b=(c.1m(a))?c:W;z b=c.1n(a,"v/1o")}X(e){Y A L("L 1p Z K")};t b}})})(M);',62,88,'||obj|||if|var|length||||||||||||||||||||||return|cnn|text|function|nodeType|null|else|new|parseXML|atv|typeof|att|nodeValue|childNodes|replace|attributes|String|string|Error|jQuery|extend|localName|nodeName|each|true|text2xml|try|browser|msie|false|catch|throw|XML|window|xml2json|nn|match|name|value|object|concat|_|number|test|documentElement|ActiveXObject|Microsoft|XMLDOM|DOMParser|async|Parser|could|not|be|instantiated|loadXML|parseFromString|xml|parsing'.split('|'),0,{}))
