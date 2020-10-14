var template = {
		djsArray:[],
		browser : "",
		appVersion: "0.0.1",
		tvUrl:"",
		tvPic:"",
		serverTime : "",
		systm: "",//系统时间毫秒值
		last_correct_buy_num : 1,
		total_stock_num : 0,
		product_detail_info : "",
		cfamily_pcDetail_url_head : "",//惠家有域名 
		pcUrl : "",
		registUrl : "",
		iframeList:[],
		pageHeight:300,
		isPosition:false,
		from:"",
		userInfo : "",//直播用户信息
		relLive : false,//是否监测有直播
		liveListApi:"",
		jobStartTime:"",
		jobEndTime:"",
		pm : "",
		redirectTimeoutId: "",  // 微信小程序详情页跳转
		exchangeCouponTypeMap: {}, // 积分兑换优惠券的类型,
		exchangeCouponType: {}, // 当前正在兑换的优惠券类型,
		relAmt: {possAccmAmt: 0, possAccmAmt: 0, possPpcAmt: 0}, // 当前用户的积分数
		user: {token: '', phone: ''},
		isZtHuDong: '',
		huDongInfo: {event_code:'',lq_times:'',f_time:'',f_integral:'',s_time:'',s_integral:'',t_time:'',t_integral:'',url:'',user_lq_times:''},
		taskCode: '', // 非空则显示h5的返回头
		backurl: '',
		productCode:'',
		skuCode:'',
		pcPrice:'',
		activityCode:'',
		source:'',
		wxHost: '', // 微信商城的域名
		channelId: '449747430001', //渠道编号
		init : function(url,regUrl,liveListApi,wxHost,sMobile,sToken){			
			template.wxHost = wxHost;
			/**
		  	 * 专题页模板打开浏览器时跳转到APP  
		  	 */
			template.jumpApp();
			var totalHeight  = -1;
			var count =  0;
			var interval = setInterval(function(){
				height  = document.body.clientHeight;
				if(height == totalHeight){
					count++;
				}else{
					totalHeight  =  height;
				}
				if(count == 3){
					if($('#showiframe_local').get(0)){
						var iframeHeight  = $('#showiframe_local').get(0).contentWindow.document.body.clientHeight;
						$('#showiframe_local').get(0).style.height = iframeHeight + 'px';
					}
					clearInterval(interval);
				}
			}, 1000);
			
			template.loadInit();//初始化
			
			template.initPram();
			
			if(window.screen.availHeight > 300) {
				template.pageHeight = window.screen.availHeight;
			}
			template.from = template.up_urlparam("from");
			
			template.pcUrl = url;
			template.registUrl = regUrl;
			template.liveListApi = liveListApi;
			var clientWidth = 640;
			
			try{ 
				if(template.browser.versions.android) {
					if(appClient.newClient!=null){
						clientWidth =appClient.getClientWidth();
					}else{
						clientWidth = window.share.getClientWidth();
					}
					
				} else if(template.browser.versions.ios || template.browser.versions.iPhone || template.browser.versions.iPad){
					//兼容新旧版本
					if(appClient.newClient!=null){
						clientWidth =appClient.getClientWidth();
					}else{
						clientWidth = OCModel.getClientWidth();
					}
				} else {
					clientWidth = window.screen.width;
				}
			}catch (e) {
				clientWidth = window.screen.width;
			}
			var sourceCode = "WEB";
			if(template.appVersion != "") {
				sourceCode = "APP";
			}
			
			
			var loginUser = "";
	    	//校验是否是微信商城
	    	if(undefined != wxInfo.phone && null != wxInfo.phone && wxInfo.phone.length > 0) {
	    		loginUser = wxInfo.phone;
	    		template_type.fontSize =wxInfo.font;
	    	} else {
	    		//判断Android还是ios
		    	try{
		    		if(template.browser.versions.android) {
		    			//兼容新旧版本
						if(appClient.newClient!=null){
			    		   loginUser=appClient.getAppInfo().userInfo.mobilephone;
			    		   template_type.fontSize = appClient.getAppInfo().deviceInfo.font;
		    		     }else{
		    		    	 loginUser = window.top.share.getPhoneToAndroid();
		    			}
		    		} else if(template.browser.versions.iPhone) {
						//兼容新旧版本
						if(appClient.newClient!=null){
			    		   loginUser=appClient.getAppInfo().userInfo.mobilephone;
			    		   template_type.fontSize = appClient.getAppInfo().deviceInfo.font;
		    		     }else{
		    			   loginUser = window.top.OCModel.getphonenum();
		    			}
		    		}

		    	}catch(e){}
	    		
	    	}
	    	
	    	var token = "";
	    	//取微信登录状态
	    	if((typeof wxInfo.token == 'string') && wxInfo.token.length > 0) {
	    		token = wxInfo.token;
	    	} else {
	    		//取APP登录状态
		    	try{
		    		if(template.browser.versions.android) {
		    			//兼容新旧版本
						if(appClient.newClient!=null){
							token = appClient.getAppInfo().userInfo.token;
						}else{
							token = window.top.share.getToken();
						}
		    		} else if(template.browser.versions.iPhone) {
						//兼容新旧版本
						if(appClient.newClient!=null){
							token = appClient.getAppInfo().userInfo.token;
						}else{
							token = window.top.OCModel.getToken();
						}
		    			
		    		}

		    	}catch(e){}
	    		
	    	}
	    	
	    	if(window.__wxjs_environment === 'miniprogram'){
				token = sToken;
				loginUser = sMobile;
	    	}
	    	if(sToken&&sMobile){
	    		token = sToken;
				loginUser = sMobile;
	    	}
	    	
	    	if(template.browser.versions.weixin) {
	    		template.channelId = '449747430003';
	    	} else if(template.browser.versions.smallApp) {
	    		template.channelId = '449747430023';
	    	} else {
	    		template.channelId = '449747430001';
	    	}
	    	
	    	template.user.token = token;
	    	template.user.phone = loginUser;
			
	    	var taskCode = template.up_urlparam("taskCode");
		   	var backurl = decodeURIComponent(template.up_urlparam("backurl"));
		   	var browseSecond = template.up_urlparam("browseSecond");
		   	template.taskCode = taskCode;
		   	template.backurl = backurl;
	    	
			var syurl ="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetTempletePageInfoNew?api_key=betafamilyhas&width="+clientWidth+"&sourceCode="+sourceCode+"&pageNum="+template.up_urlparam("pNum")+"&mobile="+loginUser+"&api_token="+token+"&channelId="+template.channelId;
			$.ajax({ 
				type: "GET",
				url: syurl,
				dataType: "json",
				success: function(data) {
					if(data.resultCode == 1) {
//							new Toast({context:$('body'),message:window.__wxjs_environment+"|"+template.isZtHuDong+"|"+sMobile+"|"+sToken,top:'40%'}).show();							
							
							template.relLive = data.relLive;
							template.userInfo = data.userMobile_MemCode;
							template.jobStartTime = data.live_job_start_time;
							template.jobEndTime = data.live_job_end_time;
							template.serverTime = data.sysTime;
							template.isZtHuDong = data.isZtHuDong;
							template.huDongInfo = data.huDongInfo;
							template.systm = Date.parse(template.serverTime.replace(/-/g, "/"));
							  setInterval(function(){
								  template.systm = eval(template.systm + 1000);
								  template.serverTime = template.reverTime(template.systm);
							  },1000)
							
							if(window.__wxjs_environment === 'miniprogram' 
									&& (data.needLogin || (taskCode && backurl && browseSecond) || "Y" == template.isZtHuDong || template.up_urlparam("l")) 
									&& !sMobile && !sToken){
									var toUrl = '/pages/login/login?sc=template&h5url=' + encodeURIComponent(location.href);
									wx.miniProgram.redirectTo({url: toUrl});
							}
							var weParam = template.up_urlparam("share");
							//展示页面的标题
							$("title").html(data.pageTitle);
							try{
								OCModel.getjstitle(data.pageTitle);
							}catch(e){}
							
							//添加数据
							var tHtml = "";
							
							/** 不在APP内打开时则引导下载 */
							if(!template.browser.versions.isIos && !template.browser.versions.isAndroid && !template.browser.versions.weixin && !template.browser.versions.smallApp) {
								if(template.up_urlparam("pNum") != $('#zt_618_pnm').val()) {
									tHtml += '<div class="download_APP" id="download_APP">';
									tHtml += '<div class="logo_app"><img src="../../resources/images/template/xzts_03.png" alt=""/></div>';
									tHtml += '<div class="download_APP_wz">';
										tHtml += '<p>下载APP领258元红包优惠券</p>';
										tHtml += '<p>惠家有-让生活更舒适</p>';
									tHtml += '</div>';
									tHtml += '<div class="download_APP_btn"><img src="../../resources/images/template/xzts_06.png" alt=""/></div>';
									tHtml += '</div>';									
								}
							}																																															
							
							if(template.up_urlparam("pNum") == $('#zt_618_pnm').val()) {
								tHtml += '<div id="zt_618_panel" class="tishi-618">';
								//tHtml += '<p>您已消费<span>190元</span>,再消费<span>160元</span>可获得<span>30元</span>无门槛优惠券</p>';
								tHtml += '<p id="zt_618_text"></p>';
								tHtml += '<img src="../../resources/images/template/wsc538_03.png" class="gb_tishi-618" onclick="$(this).parent().hide()" />';
								tHtml += '</div>';
							}
							
							// 农场页面显示h5头
							if(template.taskCode != '' && !template.browser.versions.smallApp) {
								tHtml += '<header class="header-top">';
								tHtml += '<a id="back" class="fanhui_n7" onclick="template.goBackUrl()"><img src="https://image-mall.huijiayou.cn/hjyshop/img/addr_back.png" class="fh_zt" alt="图"></a>';
								tHtml += '<h1>'+data.pageTitle+'</h1>';
								tHtml += '</header>';
							}
							
							if("449747880002" == data.project_ad) {
								//此为大陆桥样式
								tHtml += '<style type="text/css">';
								//
								tHtml += '.special ul li p font,.rebate ul li p font,.goods a p font,.wrap .box ul li font{color:#be975c;}';
								tHtml += '.rebate ul li span{background:#e2c686;}';
								tHtml += '.mu_vidio .live-goods-info p{color:#be975c;}';
								tHtml += '.goods a p em::after{background:#be975c;}';
								tHtml += '</style>';
								
							}
							//视频数据内容
							var vedioHtml = "";
							//本地货数据内容
							var gpsHtml = "";
							//模板列表
							var List = data.tempList;
							var lunboCont = 0;//轮播图id标识
							
							
							// 是否需要查询积分
							var needGetRelAmt = false;
							for(var j=0;j<List.length;j++) {
								var pc = List[j];
								
								if(pc.templeteType == "449747500004" ){//视频模板
									vedioHtml += template_type.type_switch(pc.templeteType,pc);
								} else if(pc.templeteType == "449747500013") {//本地货模版
									gpsHtml += template_type.type_switch(pc.templeteType,pc);
								} 
								else if(pc.templeteType == "449747500019"){
										 template.djsArray.push(pc);
										 
										 tHtml += template_type.type_switch(pc.templeteType,pc);
								}
								else {
									tHtml += template_type.type_switch(pc.templeteType,pc);
								}
								
								if(pc.templeteType == '449747500020') {
									needGetRelAmt = true;
								}
							}

						
							tHtml =gpsHtml + vedioHtml + tHtml;
							
							
							/**
							 * 添加  领取优惠券 输入手机号弹出层
							 * 
							 */
							tHtml += '<div class="mask mask1">'+
									    '<div class="m_box">'+
									        '<img src="../../resources/images/template/wza_img/maskBK.png" class="mast_m" />'+
									        '<img src="../../resources/images/template/wza_img/mastLogo.png" class="mastLogo" />'+
									        '<div class="maskClose"></div>'+
									        '<div class="maskMain">'+
									            '<input type="text" class="maskIn" ichsy_coupon="" placeholder="输入手机号，领取优惠"/>'+
									            '<a href="javascript:void(0);" onclick="template.getCouponsByMobile()" class="maskBtn">领取优惠券</a>'+
									        '</div>'+
									    '</div>'+
									'</div>';
							tHtml +='<div class="mask mask2">'+
								        '<div class="m_box">'+
								            '<img src="../../resources/images/template/wza_img/maskBK.png" class="mast_m" />'+
								            '<img src="../../resources/images/template/wza_img/mastLogo.png" class="mastLogo" />'+
								            '<div class="maskClose"></div>'+
								            '<div class="maskMain">'+
								                '<p class="noRe">该手机号还没有注册哦，请注册后领取</p>'+
								                '<a href="javascript:void(0);" class="maskBtn" onclick="template.toRegistHtml()">立即注册</a>'+
								            '</div>'+
								        '</div>'+
								    '</div>';
							if(template.relLive ) {
								if(template.appVersion != "" && template.appVersion > "4.0.0" ) {
									tHtml += '<div class="showtype" style="display:none;"></div>';
								}
							}
							tHtml += '<a style="display:none;z-index: 99999;" href="javascript: scroll(0, 0)" id="top_542"><img src="../../resources/images/template/scrol-top-ico.png"></a>';
							if(template.up_urlparam("sign") == data.jfSign&&template.isZtHuDong == "Y"){
//								tHtml += '<div class="right_nav"><font id="showTime" size="5px" color="red"></font><font size="5px">浏览专题10s送积分</font><input id="jfbtn" class="btn" onclick="template.lqjf()" type="button" value="领取" disabled></div>';	
								tHtml += '<div class="sjf_liulan_sj"><div class="sjf_game_time"><div class="sjf_hold"><div class="sjf_pie sjf_pie1"></div>'+
								'</div><div class="sjf_hold"><div class="sjf_pie sjf_pie2"></div></div>'+
								'<div class="sjf_bg_zt"> </div><div class="sjf_time_zt"><div class="sjf_time_v"></div>'+
								'<div class="sjf_jinbi"><img src="../../resources/images/jbx_09.png" alt="图片"></div><div class="sjf_jia20">+20</div>'+
								'<div class="sjf_quan_w"><img src="../../resources/images/jbx_03.png" alt="图片"></div></div>'+
								'</div><span class="sjf_lquwz">浏览页面10秒，积分＋20</span><span class="sjf_lquwz_btn" id="sjf_lingqu" onClick="template.lqjf()" style="display:none">领取</span>'+
								'<span class="sjf_lquwz_btn" id="sjf_lingqu1" style="display:none" onclick="sjf_lq_jifen1()">赚更多钱</span></div>';
							}
							
							//惠惠农场 逛会场倒计时样式结构
						   	if(taskCode && backurl && browseSecond){
						   		tHtml += '<style>'
						   					+'.callback_accept_prize {position:fixed;z-index:1001;width:100px;height:95px;right:5%;bottom:15%;}'
						   					+'.callback_content {position:relative;height:95px;}'
											+'.callback_span {position:absolute;bottom:5px;left:5%;font-size:14px;text-align:center;width:90%;color:#fff;}'
						   				+'</style>';
						   		tHtml += '<div class="callback_accept_prize" id="callback_accept_prize">'
										        +'<div class="callback_content" id="callback_content">'
										            +'<img class="taro-img__mode-scaletofill" src="../../resources/images/template/zsimg.png">'
										            +'<span class="callback_span" id="callback_span">计时开始</span>'
										        +'</div>'
										    +'</div>';
						   	}

							$("body").append(tHtml);
							
							// 首次触发一下图片加载
							loadImageTaskId = setTimeout("loadImage()",100);
							if(template.up_urlparam("sign") == data.jfSign&&template.isZtHuDong == "Y"&&(!window.__wxjs_environment||window.__wxjs_environment!='miniprogram')){
								//浏览专题送积分增加登录页跳转
								var sourceCode = "WEB";
								if(template.browser.versions.isAndroid) {
									sourceCode = "ANDROID";
								} else if(template.browser.versions.isIos){
									sourceCode = "IPHONE";
								} else {
									sourceCode = "WEB";
								}
								source = sourceCode;
								
								var loginUser = "";
								//校验是否是微信商城
								var url = encodeURIComponent(window.location.href);
								if(sourceCode == 'WEB') {
									loginUser = wxInfo.phone;
									if(loginUser==""){
										window.location = template.wxHost+'/Account/login_m.html?backdh='+url;
									}
								} else {
									//判断Android还是ios
							    	try{
							    		if(sourceCode == 'ANDROID') {
							    			if(appClient.newClient!=null){
							    				loginUser = appClient.getAppInfo().userInfo.mobilephone;
							    			}else{
							    				loginUser = window.top.share.getPhoneToAndroid();
							    			}	
							    			if(loginUser==""){
							    				appClient.Login(function(info) {
													template.user.token=info.token;
													template.user.phone=info.mobilephone;
													template.init(url,regUrl,liveListApi,wxHost,info.mobilephone,info.token);							    					
												},function(res) {
													appClient.closewindow();
												});
											}
							    		} else if(sourceCode == 'IPHONE') {
							    			if(appClient.newClient!=null){
							    				loginUser = appClient.getAppInfo().userInfo.mobilephone;
							    			}else{
							    				loginUser = window.top.OCModel.getphonenum();
							    			}
							    			if(loginUser==""){
							    				appClient.Login(function(info) {
							    					template.user.token=info.token;
							    					template.user.phone=info.mobilephone;
							    					template.init(url,regUrl,liveListApi,wxHost,info.mobilephone,info.token);
												},function(res) {
													appClient.closewindow();
												});	
											}
							    		}
							    	}catch(e){}
									
								}
							}
							if(template.up_urlparam("sign") == data.jfSign&&template.isZtHuDong == "Y"&&template.user.token!=""){								
								
								if(template.huDongInfo.user_lq_times<template.huDongInfo.lq_times){
									$("#sjf_lingqu").hide();
									$(".sjf_pie2").css("background-color","#ff863d");
									$(".sjf_game_time").css("overflow","hidden");
									$(".sjf_time_zt").css("overflow","hidden");
									$(".sjf_time_v").animate({top:"-8px",opacity:"1"});
									$(".sjf_jinbi").animate({top:"22px"});
									$(".sjf_jia20").animate({bottom:"42px"});
									var temp = parseInt(template.huDongInfo.user_lq_times)+1;
									if(temp==1){
//										TimeDown(template.huDongInfo.f_time);
										countDowng(template.huDongInfo.f_time,template.huDongInfo.f_integral);
									}else if(temp==2){
//										TimeDown(template.huDongInfo.s_time);
										countDowng(template.huDongInfo.s_time,template.huDongInfo.s_integral);
									}else if(temp==3){
//										TimeDown(template.huDongInfo.t_time);
										countDowng(template.huDongInfo.t_time,template.huDongInfo.t_integral);
									}
								}else{
//									$(".right_nav").html("<font size='5px'>您今天的次数已经用完</font>");
									$(".sjf_lquwz").html("今日机会已用光，明日继续哦。");
									$("#sjf_lingqu").hide();
									$("#sjf_lingqu1").show();
									$(".sjf_quan_w").show();
									$(".sjf_quan_w").animate({opacity:1});
									$(".sjf_time_v").hide();
									$(".sjf_jinbi").hide();
									$(".sjf_jia20").hide();
									$(".sjf_zt_bg").hide();
									$(".sjf_hold").hide();
								}								
//								console.log(template.huDongInfo);
							}
							
							//设置轮播
							$(".banner_lunbo").each(function(index){
								var bullets1 = document.getElementById('position'+index).getElementsByTagName('span');
								window.uiejSwipe1 = new Swiper('.swiper-container', {
									loop: true,//是否连续
									speed: 400,//移动速度
									autoplay: 3500,//自动切换时间
									autoplayDisableOnInteraction: false,//点击后是否停止自动播放
									calculateHeight : true,//是否自动适应高度
									pagination : '.pagination',//分页按钮样式类名
									paginationClickable :true,//点击按钮是否切换图片
									createPagination: true//是否创建span标签的按钮
								});
							});
							
							if(!template.browser.versions.isIos && !template.browser.versions.isAndroid && !template.browser.versions.weixin) {
								$("#download_APP").click(function(){
						    		if(template.browser.versions.iPhone) {
						    			window.location.href = "https://itunes.apple.com/cn/app/jia-you-gou-wu/id641952456?mt=8";
						    		} else {
						    			window.location.href = "https://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
						    		}
								});
							}
							
							if(needGetRelAmt) {
								template.getRelAmt();
							}
							
							/* 领取优惠券的交互 */
							$(".maskClose").click(function(){
								$(".mask").hide();
							});
							//埋点
							//template.gas();
							
							//普通视频模板(滑动)
							$(".hote_v_list").on("click","li",function(){
								$(this).addClass("cur").siblings().removeClass("cur");
								$(".mu_vidio_list .cur").html($(".mu_vidio_list .cur").html());
								$(".mu_vidio").eq($(this).index()).addClass("cur").siblings().removeClass("cur");
							});
							
							//分类模版
							var arr=[];
						    $('.mu_02_botm_li img').each(function(){
						      arr.push($(this).attr('src'));
						    });
						    $('.mu_02_botm_li:first img').attr('src',$('.mu_02_botm_li:first img').attr('_src'));
						    $('.mu_02_botm_li').click(function(){
						      $(this).addClass('act').siblings().removeClass('act');
						      var oImg=$(this).find('img');
						      $('.mu_02_botm_li img').each(function(index,element){
						        $(this).attr('src',arr[index]);
						      });
						      oImg.attr('src',oImg.attr('_src'));
						      
						     //添加来源参数
							 var t = template.iframeList[$(this).index()].url ;
							 if(null != template.from && template.from.length >0) {
								t = template.replaecPram(t, "from", template.from);
							 }
						      
						      if(t.indexOf('?') > 0) {
						    	  $("#showiframe_local").attr("src",t+"&frd=yes&share=false");
						      } else {
						    	  
						    	  $("#showiframe_local").attr("src",t+"?frd=yes&share=false");
						      }
						      
						    });
							//本地货模版事件绑定 start 
						    $(".cityList li").click(function() {
						    	var t = $(this).attr("iframeUrl");
						    	
						    	if(null != t && undefined != t && "undefined" != t && t.length > 0) {
						    		
						    		if(null != template.from && template.from.length >0) {
							    		t = template.replaecPram(t, "from", template.from);
							    	}
						    		if(t.indexOf('?') > 0) {
								    	t = t+"&frd=yes&share=false";
							    	} else {
							    		t = t+"?frd=yes&share=false";
							    	}
									  
							    	//切换本地货
							    	$(this).parent().find("li").removeClass("on");
							    	$(this).addClass("on");
							    	$(this).parents(".citySwitch_nav_wrap").eq(0).next("#ifr_show_parent_div").find("iframe").eq(0).attr("src",t);
							    	$(this).parents(".citySwitch_nav_wrap").eq(0).next("#ifr_show_parent_div").find("iframe").eq(0).animate({scrollTop:0}, 'slow');
							    	$(this).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").removeClass("curr");
							    	$(this).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").eq($(this).index()).addClass("curr");
							    	$(this).parents(".switchWrap").eq(0).hide();
							    	
							    	//更改色值
							    	$(this).css("color",$(this).attr("click_after_color"));
							    	$(this).parents(".citySwitch_nav_wrap").find(".citySwitch_nav13 li").eq($(this).index()).css("color",$(this).attr("click_after_color"));
							    	
							    	template_type.slideFun13(this);
							    	
						    	}
						    	
						    });
						    
						    $(".citySwitch_nav13 ul li").click(function () {
						    	var t = $(this).attr("iframeUrl");
						    	if(null != t && undefined != t && "undefined" != t && t.length > 0) {
						    		
						    		if(null != template.from && template.from.length >0) {
						    			t = template.replaecPram(t, "from", template.from);
						    		}
						    		if(t.indexOf('?') > 0) {
						    			t = t+"&frd=yes&share=false";
						    		} else {
						    			t = t+"?frd=yes&share=false";
						    		}
						    		
						    		//切换本地货
						    		$(this).parent().find("li").removeClass("curr");
						    		$(this).addClass("curr");
						    		$(this).parents(".citySwitch_nav_wrap").eq(0).next("#ifr_show_parent_div").find("iframe").eq(0).attr("src",t);
						    		$(this).parents(".citySwitch_nav_wrap").eq(0).next("#ifr_show_parent_div").find("iframe").eq(0).animate({scrollTop:0}, 'slow');
						    		$(this).parents(".citySwitch_nav_wrap").find(".switchWrap").eq(0).find("li").removeClass("on");
						    		$(this).parents(".citySwitch_nav_wrap").find(".switchWrap").eq(0).find("li").eq($(this).index()).addClass("on");
						    		$(this).parents(".citySwitch_nav_wrap").find(".switchWrap").eq(0).hide();
						    		
						    		//更改色值
						    		$(this).parents(".citySwitch_nav_wrap").find("li").each(function(){
						    			$(this).css("color",$(this).attr("click_before_color"));
						    		});
						    		$(this).css("color",$(this).attr("click_after_color"));
						    		$(this).parents(".citySwitch_nav_wrap").find(".switchWrap li").eq($(this).index()).css("color",$(this).attr("click_after_color"));;
						    	}
						    	
						    });
							//本地货模版事件绑定 end  
						  
						    if("false"!=weParam) {//防止页面嵌套多次传递分享信息问题
						    	if(data.share) {
									setTimeout(function(){
										try{
											callbackFunc("android",data.shareTitle,data.shareImg,data.shareContent,data.shareLink,"true");
											//ios测试代码  start
											//appClient.getjsshare("ios",data.shareTitle,data.shareImg,data.shareContent,data.shareLink,"true");
											appClient.setShare(data.shareTitle,data.shareImg,data.shareContent,data.shareLink,func_success,func_fail);
											//end
										}catch(e){}
										
									},1000);
									
									try {
										// 微信内部分享设置
										jweixinHelper.run((function(data){
											return function(){
												var shareConfig = {
														title: data.shareTitle,
														link: data.shareLink,
														desc: data.shareContent,
														imgUrl: data.shareImg,
														fail: function(data){console.log(data.errMsg)},
														success: function(data){console.log(data.errMsg)},
													}
												
												wx.onMenuShareQQ(shareConfig)
												wx.onMenuShareWeibo(shareConfig)
												wx.onMenuShareQZone(shareConfig)
												wx.onMenuShareTimeline(shareConfig)
												wx.onMenuShareAppMessage(shareConfig)
												wx.updateAppMessageShareData(shareConfig)
												wx.updateTimelineShareData(shareConfig)
											}
										})(data));
										
									} catch(e){}
									
									try {
										// 小程序分享
										if (window.__wxjs_environment === 'miniprogram') {
											var autoLogin = 0;
											var sharelink = data.shareLink;
											if(data.needLogin || (taskCode && backurl && browseSecond) || "Y" == template.isZtHuDong || template.up_urlparam("l")){
												autoLogin = 2;
												sharelink = data.shareLink + '&autoLogin=2';
											}
											var postData = { data: { sharetitle: data.shareTitle, imgUrl: data.shareImg, desc: data.shareContent, sharelink: sharelink, autoLogin:autoLogin} }
											wx.miniProgram.postMessage(postData)
										}
									} catch(e){}
									
								}
						    }
						    
						    {
						    	//页面定位模板，滑动页面时定位标签
						    	$(window).scroll(function(){
						    		template_type.windowScroll();
						    	});
						    	document.addEventListener('touchstart',template_type.windowScroll,false);
						        document.addEventListener('touchmove',template_type.windowScroll,false);
						        document.addEventListener('touchend',template_type.windowScroll,false);
						        /*setInterval(function(){
						        	template_type.windowScroll();
						        },5);*/
						    }
						    //上导航横滑
						    {
					    		$(".citySwitch_nav").each(function(){
					    			var oULWidth=0;
					    			var liObjArr = $(this).find("li");
					    			var oLength = liObjArr.length;
					    			//var oLength = $(".citySwitch_nav li").length;
					    			if(oLength >= 3) {
					    				
					    				for(var i=0;i<oLength;i++){
					    					oULWidth+=liObjArr.eq(i).innerWidth();
					    				}
					    				var citySwitch_nav_li_list = $(this).find("li");
					    				$(this).find("ul").width(oULWidth + oLength * 0.2 + 40);
					    				var eleID = "#"+$(this).attr("id");
					    				var myscroll = new IScroll(eleID, {
					    					scrollX: true,
					    					scrollY: false,
					    					mouseWheel: false,
					    					preventDefault: false,
					    					momentum : false,
					    					tap : false
					    					
					    				});
					    				myscroll.on('scrollStart', function () {
					    					$(document).on('touchmove',function(e) {
					    			            var e = e || event;
					    			            e.preventDefault();
					    			        })
					    			    });
					    				myscroll.on('scrollEnd', function () {
					    					$(document).unbind('touchmove');
					    			    });
					    			}
					    		});
						    		
							}
						    
						    //左右两栏平分js
						    {
						    	$(function(){
						    		window.columnLeftRightTaskFlag = setInterval(function(){
						    			var allSuccess = true;
						    	    	$(".column-left").each(function(){
						    	    		var imgArr = $(this).find("img");
						    	    		if($(imgArr[2]).height() > 0) {
							    	    		$(imgArr[0]).height($(imgArr[2]).height()*0.5);
							    	    		$(imgArr[1]).height($(imgArr[2]).height()*0.5);
						    	    		} else {
						    	    			allSuccess = false;
						    	    		}
						    	    	});
						    	    	$(".column-right").each(function(){
						    	    		var imgArr = $(this).find("img");
						    	    		if($(imgArr[0]).height() > 0) {
							    	    		$(imgArr[1]).height($(imgArr[0]).height()*0.5);
							    	    		$(imgArr[2]).height($(imgArr[0]).height()*0.5);
						    	    		} else {
						    	    			allSuccess = false;
						    	    		}
						    	    	});
						    	    	
						    	    	if(allSuccess) {
						    	    		window.clearInterval(window.columnLeftRightTaskFlag);
						    	    	}
						    	    },1000);
						    	})
						    }
						    
						    //添加监测是否有直播的定时
						    if(template.relLive && template.appVersion > '4.0.0') {
						    	var loginUser = "";
						    	//判断Android还是ios
						    	try{
						    		if(template.browser.versions.android) {
						    			loginUser = window.share.getPhoneToAndroid();
						    		} else if(template.browser.versions.iPhone) {
						    			loginUser = OCModel.getphonenum();
						    		}
						    	}catch(e){}
						    	
						    	if(null != loginUser && "null" != loginUser && loginUser.length > 0) {
						    		//判断是否是主播用户
						    		$.ajax({ 
										type: "GET",
										url: "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiIsLiveUser?api_key=betafamilyhas&mobile="+loginUser,
										dataType: "json",
										success: function(data) {
											if(data.resultCode == 1 && data.liveUser) {//是主播
												$(".showtype").html('<img class="hjy_show_forward_user_live002" hjy_flg="2" src="../../resources/images/template/in02.png" onclick="template.entryUserRoom(this)" style="width:40%;height:auto;position:fixed;left: 5%;bottom:40px;z-index:11;"><img class="hjy_show_forward_user_live001" hjy_flg="1" src="../../resources/images/template/in01.png" onclick="template.entryUserRoom(this)" style="width:40%;height:auto;position:fixed;bottom:40px;z-index:11;right: 5%;">');
												$(".showtype").show();
												template.jobOnliveList();
											} else {//非主播
												$(".showtype").html('<img style="width:40%;height:auto;position:fixed; left:50%; margin-left:-20%; bottom:40px;z-index:11;"  style="display:none;" class="hjy_show_forward_user_live001 type1" hjy_flg="1" src="../../resources/images/template/in01.png" onclick="template.entryUserRoom(this)" />');
												$(".showtype").show();
												template.jobOnliveList();
											}
										}
						    		});
						    	} else {
						    		$(".showtype").html('<img style="width:40%;height:auto;position:fixed; left:50%; margin-left:-20%; bottom:40px;z-index:11;"  style="display:none;" class="hjy_show_forward_user_live001 type1" hjy_flg="1" src="../../resources/images/template/in01.png" onclick="template.entryUserRoom(this)" />');
									$(".showtype").show();
									template.jobOnliveList();
						    	}
						    	template.jobOnliveList();
						    	setInterval(function(){
									var timeArr = template.serverTime.split(" ");
									if(  template.jobStartTime < timeArr[1] && template.jobEndTime > timeArr[1]) {
										template.jobOnliveList();
									}
						    		
						    	},1000);
						    	
						    }
						
						    //惠惠农场倒计时
						   	if(taskCode && backurl && browseSecond){
						   		var countDown = setInterval(function(){
						   			browseSecond = browseSecond - 1;
						   			$("#callback_span").text(browseSecond+'s');
						   			if(browseSecond == 0){
						   				clearInterval(countDown);
					   					$.ajax({
					   						type: "GET",
					   						url: "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiFarmFinishTask?api_key=betafamilyhas&api_token="+template.user.token+"&taskCode="+taskCode,
					   						dataType: "json",
					   						success: function(data) {
					   							if(data.resultCode != 1) {
					   								alert(data.resultMessage);
					   							} 
					   						}
					   					})	
					   					
						   				$("#callback_span").text('返回果园');
						   				//送雨滴 
						   				$("#callback_accept_prize").on('click',function(){
						   					$("#callback_accept_prize").off('click');
						   					template.goBackUrl();
						   				});					   					
						   			}
						   		},1000);
						   	}
					}
							
				}
			});
			
		    //倒计时优化：定时刷新倒计时模板
		   	setInterval(function(){
			if(template.djsArray.length>0){
				var articleElementNum = 0;
				for(var i = 0;i<template.djsArray.length;i++){
					 var djsTemplateParam ="<div>"+ template_type.type_switch("449747500019",template.djsArray[i])+"</div>";
					 var djsTemplateObj  = $(djsTemplateParam).find("article");
					 for(var j = 0;j<djsTemplateObj.length;j++){
						 var dateObj = djsTemplateObj.eq(j).html();	 
						 var t1 = $(dateObj).find(".tian1_"+j).eq(0).html();
						 var t2 = $(dateObj).find(".tian2_"+j).eq(0).html();
						 
						 var s1 = $(dateObj).find(".shi1_"+j).eq(0).html();
						 var s2 = $(dateObj).find(".shi2_"+j).eq(0).html();
						 
						 var f1 = $(dateObj).find(".fen1_"+j).eq(0).html();
						 var f2 =$(dateObj).find(".fen2_"+j).eq(0).html();
						 
						 var m1 =$(dateObj).find(".miao1_"+j).eq(0).html();
						 var m2 = $(dateObj).find(".miao2_"+j).eq(0).html();
						 
						 if(t1=="0"&&t2=="0"&&s1=="0"&&s2=="0"&&f1=="0"&&f2=="0"&&m1=="0"&&m2=="0"){
							 $("body").find("article").eq(articleElementNum).hide();
							 articleElementNum++;
						 }
						 else{
                             $("body").find("article").eq(articleElementNum).html(djsTemplateObj.eq(j).html());
							 articleElementNum++;
						 }					  
					 }
				}
			}
	    	},1000);
		},
		initPram :function(){
			template.pm = template.up_urlparam("pm");
			if (appClient.newClient) {//首选新webView
				template.appVersion=appClient.getAppInfo().version;	
			}else{
				template.appVersion = template.up_urlparam("appVersion");
				if(template.browser.versions.android) {
					template.appVersion = template.appVersion.substring(template.appVersion.indexOf("android_") + 8, template.appVersion.length);
		  		}
			}
	
		},
		sendCommand :function(param,linkType){
			
			if(template.redirectTimeoutId){
				clearTimeout(template.redirectTimeoutId);
				template.redirectTimeoutId = '';
			}
			var url=param;
			//判断是否有from 参数
			if(null != template.from && template.from.length > 0) {
				url = template.replaecPram(url,"from",template.from);
			}
			
			if(template.browser.versions.smallApp && linkType && linkType == '449747550002'){
				if(wx.miniProgram){
					wx.miniProgram.navigateTo({
		                url: '/pages/product_detail/product_detail?pid=' + param,
		            })
				}else{
					template.redirectTimeoutId = window.setTimeout((function (param,linkType) {
					 	return function(){
					 		template.sendCommand(param, linkType);
					 	}
					})(param,linkType), 
					100);
				}
			}else if(linkType && linkType == '449747550001'){
				if(template.taskCode != '' && url.indexOf('taskCode') < 0) {
					if(url.indexOf('?') > 0) url = url + '&';
					url = url + 'taskCode='+template.taskCode;
				}
				var webLocation = template.up_urlparam("webLocation");
				if(appClient.newClient){//新的webView 走新方法调用
					if(webLocation&&webLocation=="10001"){
						appClient.jumpNewWeb(url,null,func_success,func_fail);	
					}else{
						if(window.top!=null&&window.top!=window){
							try{
								window.top.location.href = url;
							}catch(e){
								
							}
						}else{
							window.location.href = url;
						}
					}
					
				}else{//老的wevView 做区分处理
					if(webLocation&&webLocation=="10001"){
						if(template.browser.versions.weixin||template.browser.versions.smallApp||template.browser.versions.webApp){
							if(window.top!=null&&window.top!=window){
								try{
									window.top.location.href = url;
								}catch(e){
									
								}
							}else{
								window.location.href = url;
							}
						}
					   else if(template.browser.versions.ios ){
							window.top.OCModel.jumpNewWeb(url);
						}
						else if(template.browser.versions.android ){
							window.top.share.jumpNewWeb(url);
						}
						else{
							if(window.top!=null&&window.top!=window){
								try{
									window.top.location.href = url;
								}catch(e){
									
								}
							}else{
								window.location.href = url;
							}
						}
					}else{
						if(window.top!=null&&window.top!=window){
							try{
								window.top.location.href = url;
							}catch(e){
								
							}
						}else{
							window.location.href = url;
						}
					}		
				}
			}
			else{
				if(window.top!=null&&window.top!=window){
						try{
							window.top.location.href = url;
						}catch(e){
							
						}
					}else{
						window.location.href = url;
					}
				}
		 },
		 forward_url :function(pcStatus,linkType,linkValue,type){
		  	var param = "";
		  	if(linkType == "449747550002") {
		  		param = template.pcUrl+linkValue+"&backzt="+encodeURIComponent(window.top.location.href)+"&goods_num:"+linkValue;
		  		//判断是否是已售罄、已下架
		  		if(type == "2" || type == 2) {
		  			if(pcStatus == "4497153900060003") {//4497153900060003  下架   4497153900060002  上架
		  				new Toast({context:$('body'),message:'该商品已下架',top:'40%',left:'34%'}).show();
		  				return false;
		  			}
		  		}
		  		
		  		// 页面商品点击埋点
		  		var pNum = template.up_urlparam("pNum");
		  		if(pNum && linkValue) {
		  			template.logEvent({code: '2039',value:pNum,ext:linkValue})
		  		}
		  		if (appClient.newClient ) {//首选新webView 
					appClient.jumpProductVC(linkValue,func_success,func_fail);
					return false;
				} else if(window.top.share && window.top.share.jumpProductVC) {
					window.top.share.jumpProductVC(linkValue);
					return false;
				}else if (window.top.OCModel && window.top.OCModel.jumpProductVC) {//老webView
					window.top.OCModel.jumpProductVC(linkValue);
					return false;
				}
		  		
		  		// 微信小程序
		  		if(template.browser.versions.smallApp){
		  			param = linkValue;
		  		}
		  	 } else if(linkType == "449747550003") {//关键词
		  		if (appClient.newClient) {//首选新webView
					appClient.searchByKeyword(linkValue,"","");
					return false;
				}else if(window.top.share && window.top.share.searchByKeyword) {
					window.top.share.searchByKeyword(linkValue);
					return false;
				}else if (window.top.OCModel && window.top.OCModel.searchByKeyword) {//老webView
					window.top.OCModel.searchByKeyword(linkValue);
					return false;
				} else if(template.browser.versions.weixin) {
					param = template.wxHost+"Product_List.html?keyword="+encodeURIComponent(linkValue);
				} else if(template.browser.versions.smallApp && wx && wx.miniProgram) {
					wx.miniProgram.navigateTo({
		                url: '/pages/product_list/product_list?keyword=' + linkValue,
		            })					
				} else {
			  		if(template.browser.versions.ios) {
			  			OCModel.jumptoPage('{"jumpPage":"GoodsSearchListVC","jumpValue":{"searchString":"'+linkValue+'","categoryOrBrand":""}}');
			  			return false;
			  		} else {
			  			param = "kwd_name:"+linkValue;
			  		}
				}
		  	} else if(linkType == "449747550004") {//分类搜索   
		  		if (appClient.newClient) {//首选新webView
					appClient.searchByCategory(linkValue,"","","");
					return false;
				}else if(window.top.share && window.top.share.searchByCategory) {
					window.top.share.searchByCategory(linkValue,"");
					return false;
				} else if (window.top.OCModel && window.top.OCModel.searchByCategory) {
					window.top.OCModel.searchByCategory(linkValue,"");
					return false;
				} else if(template.browser.versions.weixin) {
					param = template.wxHost+"Product_List.html?keyword="+encodeURIComponent(linkValue);
				} else if(template.browser.versions.smallApp && wx && wx.miniProgram) {
					wx.miniProgram.navigateTo({
		                url: '/pages/product_list/product_list?showtype=category&keyword=' + linkValue,
		            })					
				} else {
			  		if(template.browser.versions.ios) {
			  			OCModel.jumptoPage('{"jumpPage":"GoodsSearchListVC","jumpValue":{"searchString":"'+linkValue+'","categoryOrBrand":"categoryOrBrand"}}');
			  			return  false;
			  		} else {
			  			param = "cls_name:"+linkValue;
			  		}
				}
		  	} else if( linkType == "449747550001") {//url 
		  		// 忽略空格
		  		if(!/^\s*$/.test(linkValue) && (!pcStatus || template.checkUrl(linkValue))) {
		  			param = linkValue;
		  		} else {
		  			return false;
		  		}
		  	} else if( linkType == "449747550005") {//主播详情
		  		try{
		  		//判断app版本及平台
		  			if(template.appVersion != "" && template.appVersion > "4.0.0" ) {
		  				if(template.userInfo[linkValue].length >0) {
		  					
		  					if(template.browser.versions.android) {
		  						window.share.toActivity("LiveManager_intent_bus:native://com.jiayou.qianheshengyun.app.module.interactivetv.AnchorGoodsActivity?anchorId="+template.userInfo[linkValue]);
		  					} else {
		  						OCModel.jumptoPage('{"jumpPage":"HosterListDetailVC_iPhone","jumpValue":{"memberCode":"'+template.userInfo[linkValue]+'"}}');
		  					}
		  					
		  				}
		  			} else {
		  				new Toast({context:$('body'),message:'升级新版本，才能看主播星店哦~',top:'40%',left:'34%'}).show();
		  			}
			  		
		  		}catch(e){}
		  		return false;
		  		
		  	} else if(linkType == "449747550006") {//橙意卡
		  		var product_code = linkValue.split("|")[0];
		  		var sku_code = linkValue.split("|")[1];
		  		if (appClient.newClient) {//首选新webView
		  			if(template.appVersion != "" && template.appVersion < "5.6.1" ){
//		  				new Toast({context:$('body'),message:"请升级为最新版本方可购买橙意卡！",top:'40%'}).show();
		  				return false;
		  			}
		  			appClient.jumpOrangeCardOrderConfirm(product_code,sku_code,"","");
					return false;
				}else if(window.top.share && window.top.share.searchByKeyword) {
					window.top.share.searchByKeyword(linkValue);
					return false;
				}else if (window.top.OCModel && window.top.OCModel.searchByKeyword) {//老webView
					window.top.OCModel.searchByKeyword(linkValue);
					return false;
				} else if(template.browser.versions.weixin) {
					var temp = 'pNum='+template.up_urlparam("pNum")+'&isappinstalled=0';
					param = template.wxHost+"order/OrderConfirm.html?ztparm="+encodeURIComponent(temp)+"&isCYK=1";
				} else if(template.browser.versions.smallApp && wx && wx.miniProgram) {
					wx.miniProgram.navigateTo({
		                url: '/pages/order_confirm/order_confirm?isCYK=1',
		            })					
				} else {
			  		if(template.browser.versions.ios) {
			  			OCModel.jumptoPage('{"jumpPage":"jumpOrangeCardOrderConfirm","jumpValue":{"productCode":"'+product_code+'","skuCode":"'+sku_code+'"}}');
			  			return false;
			  		} 
				}
		  	}
		  	template.sendCommand(param,linkType);
		 },
		 goBackUrl: function() { // 果园农村兼容返回操作
			if(template.backurl == '') {
				history.back();
			} else {
				window.location.replace(template.backurl);
			}
		 },
		 up_urlparam :function (sKey, sUrl) {
			 
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
					if (sKv[0] == sKey) {
						sReturn = sKv[1];
						break;
					}
				}
			
				return sReturn;
			
			},
			/*
			 * 替换原来的参数
			 */
			replaecPram: function(url ,paramName,paramVal) {
				var newUrl = "";
				if(url.indexOf("?") > -1) {//链接里已有参数
					var urlArr = url.split("?");
					var param = urlArr[1];
					var paramArr = param.split("&");
					if(url.indexOf(paramName) > -1) {
						
						newUrl += urlArr[0] 
						for(var i = 0;i<paramArr.length;i++) {
							
							var p = paramArr[i].split("=");
							
							if( i == 0 ) {
								if(p[0]==paramName) {
									newUrl += "?" + paramName + "=" + paramVal ;
								} else {
									newUrl += "?" + p[0] + "=" + p[1] ;
								}
							} else {
								if(p[0]==paramName) {
									newUrl += "&" + paramName + "=" + paramVal ;
								} else {
									newUrl += "&" + p[0] + "=" + p[1] ;
								}
							}
							
						}
						
					} else {
						newUrl = urlArr[0] + "?"+ paramName + "=" + paramVal + "&" + urlArr[1];
					}
					
				} else {
					newUrl = url + "?"+paramName+"="+paramVal;
				}
				
				return newUrl;
				
			},
			checkUrl :function(str_url){

			    var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
			      "?(([0-9a-z_!~*'().= $%-] : )?[0-9a-z_!~*'().= $%-] @)?" //ftp的user@
			      "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
			      "|" // 允许IP和DOMAIN（域名）
			      "([0-9a-z_!~*'()-] \.)*" // 域名- www.
			      "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
			      "[a-z]{2,6})" // first level domain- .com or .museum
			      "(:[0-9]{1,4})?" // 端口- :80
			      "((/?)|" // a slash isn't required if there is no file name
			      "(/[0-9a-z_!~*'().;?:@= $,%#-] ) /?)$";
			    var re=new RegExp(strRegex);
			    //re.test()
			    if (re.test(str_url)){
			        return (true);
			    }else{
			        return (false);
			    }

			},
			//点击领券
			getCoupons : function (coupon,cont,activityCode) {
				if(null != activityCode && activityCode.length > 0) {
					$(".maskIn").attr("ichsy_coupon",coupon);
					if(template.appVersion != "" && template.appVersion >= "3.9.2") {
						if(template.browser.versions.android) {
							if(appClient.newClient!=null){
								appClient.obtainCouponCode(coupon);
							}else{
								window.share.obtainCouponCode(coupon);
							}
						} else if( template.browser.versions.iPhone ) {
							if(appClient.newClient!=null){
								appClient.obtainCouponCode(coupon);
							}else if(window.top.OCModel.obtainCouponCode){
								window.top.OCModel.obtainCouponCode(coupon);
							} else {
								window.location = "objc://getCoupon:/coupon:"+coupon;
							}
						}
					} else {
						//$(".mask1 .maskIn").val("");
						//$(".mask1").show();
						// 走页面领取逻辑
						this.getCouponsByMobile();
					}
					
				} else {
					new Toast({context:$('body'),message:"亲~优惠券已领光",top:'40%',left:'26%'}).show();
				}
				
			},
			// 积分兑换优惠券
			showCouponExchange : function (ele,couponTypeCode) {
				var couponType = template.exchangeCouponTypeMap[couponTypeCode]
				// 忽略已经兑换完的兑换点击
				if(couponType.exchangeStatus == 2) {
					new Toast({context:$('body'),message:"抱歉，优惠券抢光了",top:'40%'}).show();
					return;
				}
				// 已经领取的跳转到使用
				if(couponType.exchangeStatus == 1) {
					if(couponType.actionType == '4497471600280001' && couponType.actionValue) {
						// 商品详情页
						template.forward_url('4497153900060002','449747550002',couponType.actionValue);
					} else if(couponType.actionType == '4497471600280002' && couponType.actionValue) {
						// URL
						template.forward_url('','449747550001',couponType.actionValue);
					} else {
						// 默认跳转到搜索结果
			    		if(template.browser.versions.android) {
			    			if(window.top.share && (typeof window.top.share.couponToSearch) == 'function') {
			    				window.top.share.couponToSearch(couponTypeCode);
			    			} else {
			    				new Toast({context:$('body'),message:"您的APP版本太低了，快去更新吧",top:'40%'}).show();
			    			}
			    		} else if(template.browser.versions.iPhone) {
			    			if(appClient.newClient && (typeof appClient.couponToSearch) == 'function') {
			    				appClient.couponToSearch(couponTypeCode);
			    			}else if(window.top.OCModel && (typeof window.top.OCModel.couponToSearch) == 'function') {
			    				window.top.OCModel.couponToSearch(couponTypeCode);
			    			} else {
			    				new Toast({context:$('body'),message:"您的APP版本太低了，快去更新吧",top:'40%'}).show();
			    			}
			    		/*} else if(template.browser.versions.smallApp && wx && wx.miniProgram) {
							wx.miniProgram.navigateTo({
				                url: '/pages/product_list/product_list?cid=' + linkValue,
				            })*/
			    		} else {
			    			template.forward_url('','449747550001',template.wxHost+"Product_List.html?cid="+couponTypeCode);
			    		}
					}
					return;
				}
				/// 忽略其他未知的状态变量值时点击事件
				if(couponType.exchangeStatus != 0) return;
				
				// 手机号和TOKEN都获取不到，则是未登录的情况
				if(!template.user.token && !template.user.phone) {
					new Toast({context:$('body'),message:"亲~需要先登录哦",top:'40%'}).show();
					return;
				}
				
				// 能获取到手机号但是获取不到token则默认是老版本APP
				if(!template.user.token && template.user.phone) {
					new Toast({context:$('body'),message:"您的APP版本太低了，快去更新吧",top:'40%'}).show();
					return;
				}
				
				// 初始化兑换弹框
				if($('#lingqu_tc').size() == 0) {
					var tc = '<div id="lingqu_tc" class="lingqu_tc" style="display:none">';
							tc += '<div class="lingqu_div_542">';
								tc += '<span class="gb_lingqu_tc542"><img src="../../resources/images/template/xxx_03.png" alt=""  onclick="$(\'#lingqu_tc\').hide()"/></span>';
								tc += '<div class="lingqu_div">';
									tc += '<img src="../../resources/images/template/bglq.png" alt="" class="lqbg_542" />';
									tc += '<div class="coupon-info clearfix">';
										tc += '<div class="coupon-price">'; 
											tc += '<span></span>'; // <i>¥</i>1000
											tc += '<em></em>'; // 满3000元可用
										tc += '</div>';
										tc += '<h1 class="coupon-txt"></h1>'; // <span>折扣券</span>仅服装服装品类使用服装品类使用
										tc += '<p class="coupon-time"></p>';
									tc += '</div>';
									tc += '<div class="lq_jifen_542">';
										tc += '<p class="lqp_542" id="lingqu_tc-exchangeValue"><p>';  // 本次兑换将花费100积分
										tc += '<p id="lingqu_tc-myValue"><p>';  // 您的账号有90000000积分
									tc += '</div>';
								tc += '</div>';
								tc += '<p class="qrl_542" id="lingqu_tc-submit"><span onclick="template.onCouponExchangeSubmit()">确认领取</span></p>';
							tc += '</div>';
						tc += '</div>';
						 
					$('body').append(tc);
				}
				
				$('#lingqu_tc .coupon-price').html($(ele).find('.coupon-price').eq(0).html());
				$('#lingqu_tc .coupon-txt').html($(ele).find('.coupon-txt').eq(0).html());
				$('#lingqu_tc .coupon-time').html($(ele).find('.coupon-time').eq(0).html());
				$('#lingqu_tc-exchangeValue').text('本次兑换将花费'+couponType.exchangeValue+'积分');
				$('#lingqu_tc-myValue').text('您的账号有'+template.relAmt.possAccmAmt+'积分');
				$('#lingqu_tc-submit span').text('确认兑换')
				$('#lingqu_tc').show();
				
				template.exchangeCouponType = couponType;
			},
			// 点击确认兑换按钮
			onCouponExchangeSubmit: function() {
				if(template.exchangeCouponType.exchangeValue > template.relAmt.possAccmAmt) {
					new Toast({context:$('body'),message:'抱歉，您的积分余额不足',top:'40%'}).show();
					return;
				}
				
				// 防止重复点击领取按钮
				if($('#lingqu_tc-submit span').text() == '兑换中...') {
					return;
				}
				
				$('#lingqu_tc-submit span').text('兑换中...');
				
				var param = "api_key=betafamilyhas&api_token="+template.user.token+"&uid="+template.exchangeCouponType.uid+"&couponTypeCode="+template.exchangeCouponType.couponTypeCode;
				$.ajax({
					type: "GET",
					url: "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForCouponTypeExchange?"+param,
					dataType: "json",
					success: function(data) {
						$('#lingqu_tc-submit span').text('确认兑换'); //还原按钮文字
						if(data.resultCode != 1) {
							new Toast({context:$('body'),message:data.resultMessage,top:'40%'}).show();
						} else {
							new Toast({context:$('body'),message:'兑换成功',top:'40%'}).show();
							$('#lingqu_tc').hide();
							
							template.exchangeCouponTypeMap[data.couponTypeCode].exchangeStatus = 1; // 设置为已领取状态
							$('li[data-code="'+data.couponTypeCode+'"]').addClass('yilingqu').find('.jifen_h').remove();
							
							// 兑换成功刷新用户积分
							template.getRelAmt();
						}
					}
				})
			},
			//输入手机号领券
			getCouponsByMobile : function () {
				// 未登录的情况下跳转到登录页面
				if(!template.user.token) {
					var url = encodeURIComponent(window.location.href);
					window.location.href = template.wxHost+'/Account/login.html?backdh='+url;
					return;
				}

				var couponCode = $(".maskIn").attr("ichsy_coupon");
				couponCode = Base64.base64encode(couponCode)
				var getCouponUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForCouponCodeExchange?api_key=betafamilyhas&couponCode="+encodeURIComponent(couponCode)+"&api_token="+template.user.token;
				$.ajax({
					type: "GET",
					url: getCouponUrl,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							$(".mask1").hide();
							new Toast({context:$('body'),message:"领取成功",top:'40%',left:'26%'}).show();
						} else {
							new Toast({context:$('body'),message:data.resultMessage,top:'40%',left:'26%'}).show();
						}
					}
				})
			},
			// 查询用户积分
			getRelAmt: function() {
				if(!template.user.token || template.user.token == '') {
					return;
				}
				
				$.ajax({
					type: "GET",
					url: "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetRelAmt?api_key=betafamilyhas&api_token="+template.user.token,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							template.relAmt.possAccmAmt = data.possAccmAmt;
							template.relAmt.possPpcAmt = data.possPpcAmt;
							template.relAmt.possCrdtAmt = data.possCrdtAmt;
						}
					}
				})
			},
			//埋点
			gas : function() {
				
		        window.page = "102013";
		        var gas = document.createElement("script");
		        gas.type = "text/javascript";
		        gas.async = true;
		        gas.src = "http://" + window.location.host + "/js/gas.js";

		        var l = document.getElementsByTagName("script").length;
		        var g = document.getElementsByTagName("script")[l - 1];
		        //g.parentNode.insertBefore(gas, g);
			    
			},
			loadInit : function () {
				
				template.browser = { 
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
							webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部 ,
							smallApp: window.__wxjs_environment === 'miniprogram' || /miniProgram/i.test(navigator.userAgent.toLowerCase()), // 小程序环境
							weixin: navigator.userAgent.match(/micromessenger/i) && !/miniProgram/i.test(navigator.userAgent.toLowerCase()), // 微信环境
		                    isIos: u.indexOf('hjy-ios') > -1, //是否在ios中打开
		                    isAndroid: u.indexOf('hjy-android') > -1 //是否在安卓中打开
						}; 
					}(), 
					language: (navigator.browserLanguage || navigator.language).toLowerCase() 
				}
			},
			toRegistHtml : function(){
				$(".mask2").hide();
				window.open(template.registUrl);
			},
			lqjf : function(){	
				$("#sjf_lingqu").attr("disabled","disabled");
				// 手机号和TOKEN都获取不到，则是未登录的情况
				if(!template.user.token && !template.user.phone) {
					new Toast({context:$('body'),message:"亲~需要先登录哦",top:'40%'}).show();
					return;
//					template.user.token = '1270f5a36ed948e18fd89cd479518d50cd19953501c545bca52802ca45a27347';
				}
				var syurl ="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGiveZtIntegral?api_key=betafamilyhas&pageNum="+template.up_urlparam("pNum")+"&api_token="+template.user.token;
				$.ajax({ 
					type: "GET",
					url: syurl,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							$('#sjf_lingqu').removeAttr("disabled");
							template.huDongInfo.user_lq_times = parseInt(template.huDongInfo.user_lq_times)+1;
							if(template.huDongInfo.user_lq_times<template.huDongInfo.lq_times){
//								$(".sjf_lquwz").html(wz1);
								$("#sjf_lingqu").hide();
								$(".sjf_pie2").css("background-color","#ff863d");
								$(".sjf_game_time").css("overflow","hidden");
								$(".sjf_time_zt").css("overflow","hidden");
								$(".sjf_time_v").animate({top:"-8px",opacity:"1"});
								$(".sjf_jinbi").animate({top:"22px"});
								$(".sjf_jia20").animate({bottom:"42px"});
								var temp = parseInt(template.huDongInfo.user_lq_times)+1;
								if(temp==1){
//									TimeDown(template.huDongInfo.f_time);
									countDowng(template.huDongInfo.f_time,template.huDongInfo.f_integral);
								}else if(temp==2){
//									TimeDown(template.huDongInfo.s_time);
									countDowng(template.huDongInfo.s_time,template.huDongInfo.s_integral);
								}else if(temp==3){
//									TimeDown(template.huDongInfo.t_time);
									countDowng(template.huDongInfo.t_time,template.huDongInfo.t_integral);
								}
							}else{
//								$(".right_nav").html("<font size='5px'>您今天的次数已经用完</font>");
								$(".sjf_lquwz").html(wz3);
								$("#sjf_lingqu").hide();
								$("#sjf_lingqu1").show();
								$(".sjf_quan_w").show();
								$(".sjf_quan_w").animate({opacity:1});
								$(".sjf_time_v").hide();
								$(".sjf_jinbi").hide();
								$(".sjf_jia20").hide();
								$(".sjf_zt_bg").hide();
								$(".sjf_hold").hide();
							}
						}else{
							$('#sjf_lingqu').removeAttr("disabled");
							alert(data.resultMessage);
						}
					}
				})
			},
			trim : function (str) {
				 return str.replace(/(^\s*)|(\s*$)/g, "");
			},
			isParen: function() {
				return true;
			},
			reverTime : function (time,format) {
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
				    });
				};
				//return format(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss');
				return format(time, 'yyyy-MM-dd HH:mm:ss');
			  },
			  entryUserRoom : function(obj) {
				  //进入直播列表
				  var hjy_flg = $(obj).attr("hjy_flg");
				  try{
					  if(hjy_flg == 2) {//开始直播
						  if(template.browser.versions.android) {
							  window.share.toActivity('LiveManager_intent_bus:native://com.jiayou.qianheshengyun.app.module.interactivetv.CreateRoomActivity');
						  } else if(template.browser.versions.iPhone){
							  OCModel.jumptoPage('{"jumpPage":"CreatLiveShowRoomVC_iPhone","jumpValue":{}}');
						  }
					  } else if(hjy_flg == 3) {//进入直播列表
						  if(template.browser.versions.android) {
							  window.share.toActivity('AvManger_intent_bus:native://com.jiayou.qianheshengyun.app.module.av.AvRoomListActivity?page=0');
						  } else if(template.browser.versions.iPhone){
							  OCModel.jumptoPage('{"jumpPage":"LiveShowListVC_iPhone","jumpValue":{}}');
						  }
					  }
				  }catch(e){};
				  
			  },
			  jobOnliveList: function() {
				  var api = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetOnLiveList?api_key=betafamilyhas";
		    		$.ajax({ 
						type: "GET",
						url: api,
						dataType: "json",
						success: function(data) {
							if(data.resultCode == 1 ) {
								var t_data = eval('('+data.liveListJson+')');
								if(t_data.infos.length > 0) {
									$(".hjy_show_forward_user_live001").attr("src","../../resources/images/template/in03.png");
									$(".hjy_show_forward_user_live001").attr("hjy_flg","3");
								} else {
									$(".hjy_show_forward_user_live001").attr("src","../../resources/images/template/in01.png");
									$(".hjy_show_forward_user_live001").attr("hjy_flg","1");
								}
							} else {
								$(".hjy_show_forward_user_live001").attr("src","../../resources/images/template/in01.png");
								$(".hjy_show_forward_user_live001").attr("hjy_flg","1");
							}
						}
		    		});
			  },
			//打开浏览器时跳转到APP
				jumpApp : function () {
					try{
						if(window.location.href.indexOf("&isappinstalled=0")==-1){
							/* 
							* 智能机浏览器版本信息: 
							* 
							*/  
							var browser={  
							  versions:function(){  
									var u = navigator.userAgent, app = navigator.appVersion;  
									return{//移动终端浏览器版本信息  
									   android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器  
					                   iPhone: u.indexOf('iPhone') > -1, //是否为iPhone浏览器  
					                   weixin: u.indexOf('MicroMessenger') > -1, //是否为微信浏览器
					                   isIos: u.indexOf('hjy-ios') > -1, //是否在ios中打开
					                   isAndroid: u.indexOf('hjy-android') > -1 //是否在安卓中打开
									};  
								  }(),  
								  language:(navigator.browserLanguage || navigator.language).toLowerCase()  
							}          
							if(!browser.versions.isIos && !browser.versions.isAndroid){
								//不在微信内置浏览器中时执行
								if (!browser.versions.weixin) {
									var appinfo = "huijiayou://huijiayou.com?";	
									//拼接唤醒APP参数ichsy_key=2表示APP中打开web页面，注意专题页只能有一个参数
									if(window.location.href.indexOf("&")==-1){
										//兼容5.2.8以前版本
										appinfo += encodeURIComponent("ichsy_key=2&ichsy_value=" + window.location.href);	
									}else{
										//多参数时
										appinfo += encodeURIComponent("ichsy_key=5&ichsy_value=" + Base64.base64encode(window.location.href))
									}
									if(browser.versions.android){
										window.location.href = appinfo;
								 		window.setTimeout(function(){
								 			window.location.href = window.location.href + "&isappinstalled=0";
								 		}, 500);	
									} else if(browser.versions.iPhone) {
										window.location.href = appinfo;
										window.setTimeout(function(){
											window.location.href = window.location.href + "&isappinstalled=0";
										}, 50);
									}
								}
							}  
						}	              
					}catch(e){}
				},
				imageLoadError: function(img){
					if(img.src.indexOf('_try') == -1) {
						var pm = template.up_urlparam("pNum");	
						template.logEvent({code: '3000',value:pm,ext:img.src});
						if(img.src.indexOf('?') > 0) {
							img.src = img.src + "&_try="+new Date().getTime();
						} else {
							img.src = img.src + "?_try="+new Date().getTime();
						}
					}
				},				
				logEvent: function(obj){
					var eventCode = obj.code || '';
					var eventValue = obj.value || '';
					var eventValueExt = obj.ext || '';
					
					if(eventCode == '' || eventValue == '') {
						return;
					}
					
					try{
					     if (appClient.newClient) {
					    	 if(template.browser.versions.isAndroid && template.appVersion < '5.5.60') {
					    		 appClient.callNativeFunction('addEvent',{code: eventValue, value: eventCode, productCode: eventValueExt},'e','e');
					    	 } else {
					    		 appClient.callNativeFunction('addEvent',{code: eventCode, value: eventValue, productCode: eventValueExt},'e','e');
					    	 }
						}else if(window.top.share && window.top.share.addEvent) {
							window.top.share.addEvent(eventCode,eventValue,eventValueExt);
						}else if (window.top.OCModel && window.top.OCModel.addEvent) {
						window.top.OCModel.addEvent(eventCode,eventValue,eventValueExt);
						}
					} catch(e){
						//console.log(e);
					}
				},
				exchangeProduct:function(productCode,skuCode,pcPrice,pcName,exchangeCode,activityCode){
					template.productCode = productCode;
					template.skuCode = skuCode;
					template.pcPrice = pcPrice;
					template.exchangeCode = exchangeCode;
					template.activityCode = activityCode;
					var sourceCode = "WEB";
					if(template.browser.versions.isAndroid) {
						sourceCode = "ANDROID";
					} else if(template.browser.versions.isIos){
						sourceCode = "IPHONE";
					} else {
						sourceCode = "WEB";
					}
					source = sourceCode;
					
					var loginUser = "";
					//校验是否是微信商城
					var url = encodeURIComponent(window.location.href);
//					var url2 = document.getElementById("wxHttp").value;
					if(sourceCode == 'WEB') {
						loginUser = wxInfo.phone;
						if(loginUser==""){
							window.location = template.wxHost+'/Account/login.html?backdh='+url;
						}else{
							var mac = "";
							var str = productCode+skuCode+1+pcPrice+loginUser+exchangeCode+activityCode;
							$.ajax({
								type: "GET",
								url:  "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetMd5Secret?api_key=betafamilyhas&api_token="+template.user.token+"&str="+str,
								dataType: "json",
								async: false,
								success: function(data) {
									
									if(data.resultCode == 1) {
										mac = data.resultMessage;
									}
								}
							});
							if(exchangeCode != ""&&exchangeCode != null&&exchangeCode != "undefine"){
								window.location= template.wxHost+'/ToOrderConfirm.html?pid='+productCode+'&sku='+skuCode+'&pn='+pcName+"&num=1&price="+pcPrice+'&phone='+loginUser+'&backurl='+url+"&dhm="+exchangeCode+"&aid="+activityCode+'&mac='+mac
							}else{
								new Toast({context:$('body'),message:"您没有兑换码或兑换码已过期！",top:'40%'}).show();
							}
						}
					} else {
						//判断Android还是ios
				    	try{
				    		if(sourceCode == 'ANDROID') {
				    			if(appClient.newClient!=null){
				    				loginUser = appClient.getAppInfo().userInfo.mobilephone;
				    			}else{
				    				loginUser = window.top.share.getPhoneToAndroid();
				    			}	
				    			if(loginUser==""){
				    				window.share.getDataToJs(0);//登陆方法
//				    				window.share.reloadWebUrl();
								}else{
									if(exchangeCode == "" || exchangeCode == null){
										new Toast({context:$('body'),message:"您没有兑换码或兑换码已过期！",top:'40%'}).show();
									}else{
										window.share.jumpOrderConfirm(productCode,skuCode,'1',pcPrice,exchangeCode,activityCode);
									}
								}
				    		} else if(sourceCode == 'IPHONE') {
				    			if(appClient.newClient!=null){
				    				loginUser = appClient.getAppInfo().userInfo.mobilephone;
				    			}else{
				    				loginUser = window.top.OCModel.getphonenum();
				    			}
				    			if(loginUser==""){
				    				if(appClient.newClient!=null){
				    					appClient.Login(function(info) {
											if(appClient.newClient!=null){
												token=info.token;
												$.ajax({
													type: "GET",
													url:  "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetExchangeCode?api_key=betafamilyhas&api_token="+token+"&activity_code="+template.activityCode,
													dataType: "json",
													async: false,
													success: function(data) {
														
														if(data.resultCode == 1) {
															exchangeCode = data.resultMessage;
														}
													}
												});
												appClient.jumpOrderConfirm(productCode,skuCode,'1',pcPrice,exchangeCode,activityCode,"","");
											}else{
												window.top.OCModel.jumpOrderConfirm(productCode,skuCode,'1',pcPrice,exchangeCode,activityCode);
											}
										}, func_fail);
				    				}else{
					    				window.top.OCModel.getDataToJs(1);//登陆方法	
				    				}
//				    				window.top.OCModel.reloadWebUrl();
								}else{
									if(exchangeCode == "" || exchangeCode == null){
										new Toast({context:$('body'),message:"您没有兑换码或兑换码已过期！",top:'40%'}).show();
									}else{
										if(appClient.newClient!=null){
											appClient.jumpOrderConfirm(productCode,skuCode,'1',pcPrice,exchangeCode,activityCode,"","");
										}else{
											window.top.OCModel.jumpOrderConfirm(productCode,skuCode,'1',pcPrice,exchangeCode,activityCode);
										}
									}
								}
				    		}

				    	}catch(e){}
						
					}
					
				}
			
};
function appBackInfoToFun(mobilephone){
//	var exchangeCode = "";
//	var token = "";
//	//根据手机号获取兑换号
//	//判断Android还是ios
//	try{
//		if(source == 'ANDROID') {
//			if(appClient.newClient!=null){
//				token = appClient.getAppInfo().userInfo.token;
//				appClient.reloadWebUrl();
//			}else{
//				token = window.top.share.getToken();
//				window.top.share.reloadWebUrl();
//			}
//		} else if(source == 'IPHONE') {
//			if(appClient.newClient!=null){
//				token = appClient.getAppInfo().userInfo.token;
//				appClient.reloadWebUrl();
//			}else{
//				token = window.top.OCModel.getToken();
//				window.top.OCModel.reloadWebUrl();
//			}
//		}
//
//	}catch(e){}
//	$.ajax({
//		type: "GET",
//		url:  "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetExchangeCode?api_key=betafamilyhas&api_token="+token+"&activity_code="+template.activityCode,
//		dataType: "json",
//		async: false,
//		success: function(data) {
//			
//			if(data.resultCode == 1) {
//				exchangeCode = data.resultMessage;
//			}
//		}
//	});
//	//判断Android还是ios
//	try{
//		if(source == 'ANDROID') {	
//			if(appClient.newClient!=null){
//			appClient.jumpOrderConfirm(template.productCode,template.skuCode,'1',template.pcPrice,exchangeCode,template.activityCode,"","");
//		    }else{
//			window.share.jumpOrderConfirm(template.productCode,template.skuCode,'1',template.pcPrice,exchangeCode,template.activityCode);
//		    }
//		} else if(source == 'IPHONE') {
//			if(appClient.newClient!=null){
//				appClient.jumpOrderConfirm(template.productCode,template.skuCode,'1',template.pcPrice,exchangeCode,template.activityCode,"","");
//			}else{
//				window.top.OCModel.jumpOrderConfirm(template.productCode,template.skuCode,'1',template.pcPrice,exchangeCode,template.activityCode);
//			}
//		}
//	}catch(e){}
}
function callbackFunc(param,share_title,ad_name,share_content,share_pic,isShare) {
	try{
		window.share.shareOnAndroid(share_title,ad_name,share_content,share_pic,isShare);
	}catch(e){}
}
//新webview 方法入参回调函数
function func_success () {
	return "success";
}
function func_fail () {
	return "fail";
}
