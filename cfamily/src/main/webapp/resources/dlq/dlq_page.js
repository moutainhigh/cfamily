var dlq_page = {
		appVersion: "",
		url : "",
		activityCode : "",
		init : function(){
			dlq_page.activityCode = dlq_page.up_urlparam("accode");//用于优惠券活动
			
			dlq_page.appVersion = dlq_page.up_urlparam("appVersion");
			var param_from = dlq_page.up_urlparam("from");
			var tvNumber = dlq_page.up_urlparam("tvNumber");
			var p_type = dlq_page.up_urlparam("p_type");
			var syurl ="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetDLQInfo?api_key=betafamilyhas&from="+param_from+"&tv_number="+tvNumber+"&p_type="+p_type;
			
			$.ajax({
				type: "GET",
				url: syurl,
				dataType: "json",
				success: function(data) {
					
					if(data.resultCode == 1) {
						
						var shareInfo = data.share_info;
						$("title").html(data.page_title);
						
						//添加数据
						var tHtml = "";
						
						/*
						 * 添加领取优惠券功能 (领取功能取消)
						 */	
//						var pageGetcoupon = document.cookie.indexOf("DLQpageGetcoupon=Success;");
//						if( "undefined" != dlq_page.activityCode && dlq_page.activityCode.length >0 && pageGetcoupon <0) {
//							tHtml += '<div class="fan_maskwrap"><div class="fan_mask"><div class="dialogWrap"><div class="dialog"><div class="logoWrap clearfix"><p class="logo1"></p><p class="and">&</p><p class="logo2"></p></div>'+
//							'<p class="p1">领取惠家有优惠券，购物自动减免10元！</p><input id="inputCouponM" type="tel" placeholder="输入手机号，领取优惠" /><a onclick="dlq_page.getCoupon(this)" href="javascript:void(0);">领取优惠券</a><div class="close" onclick="dlq_page.closeGetCoupon(this)"></div></div></div></div></div>';
//						}
						
						//获取广告图信息
						var List = data.picList;
							if(List.length > 0) {//没有轮播广告则不加载插件
								
								//页面轮播图
								tHtml += '<div class="banner">';
								tHtml += '<div id="swipe" class="hd">';
								tHtml += '<div class="swipe-wrap">';
								for(var j=0;j<List.length;j++) {
									var pic = List[j];
									tHtml += '<div onclick="dlq_page.forwardurl(\''+pic.url+'\',\'轮播图点击\')"><img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pic.pic+'" alt=""></div>';
								}	
								tHtml += '</div>';
								tHtml += '</div>';
								tHtml += '<div id="position" class="bd">';
								for(var j=0;j<List.length;j++) {
									if(j == 0) {
										tHtml += '<span class="on"></span>';
									} else {
										tHtml += '<span></span>';
									}
								}
								tHtml += '</div>';
								tHtml += '</div>';
								
							}
						
						//获取分享信息
						var cList = data.conntentList;
							//专题列表
							tHtml += '<ul class="list">';
							for(var i=0;i<cList.length;i++){
								var cpc = cList[i];
								tHtml += '<li>';
								tHtml += '<a onclick="dlq_page.forwardurl(\'dlq_detail.ftl?page_number='+cpc.page_number+'&p_type='+p_type+'&from='+param_from+'&tvNumber='+tvNumber+'\',\'菜系详情'+cpc.page_number+'_'+param_from+'\')" href="javascript:void(0);">';
								tHtml += '<img  class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+cpc.pic_link+'" alt="'+cpc.title+'">';
								//tHtml += '<span>'+cpc.title+'</span>';
								tHtml += '</a>';
								tHtml += '</li>';
							}
							tHtml += '</ul>';
							
							/**
							 * 添加全集页下载app按钮
							 */
							if(dlq_page.appVersion.length <= 0) {
								tHtml += '<div class="splendid"><a  href="javascript:void(0);" onclick="dlq_page.open_app()">下载APP</a>更多精彩内容，请在惠家有中查看</div>';
							}
							$("body").html(tHtml);
							
							$('.splendid').click(function(){
								$(this).hide();	
								_hmt.push(['_trackEvent', window.location.href+'_click_更多精彩内容，请在惠家有中查看', 'click', '更多精彩内容，请在惠家有中查看', '1']);
							});
						
							//加载图片
							$("img.delay").lazyload({threshold:100,placeholder:""});
						//设置轮播
						try {
							var bullets = document.getElementById('position').getElementsByTagName('span');
							window.uiejSwipe = new Swipe(document.getElementById('swipe'), {
								startSlide: 0,
								speed: 400,
								auto: 3500,
								continuous: true,
								disableScroll: true,
								stopPropagation: false,
								callback: function(pos) {
									var i=bullets.length;
									while (i--) {
									     bullets[i].className = '';
									}
									bullets[pos].className = 'on';  
								},
								transitionEnd: function(index, elem) {}
							});
						} catch (e) {}
						
						//是否展示分享按钮
						var share_link = window.location.href ;
						if(shareInfo.share_pic.length>0) {
							if(self.frameElement.tagName=="IFRAME"){//嵌套的页面不进行分享
								return false;
							}
							callbackFunc("android",shareInfo.share_title,shareInfo.share_pic,shareInfo.share_content,share_link,"true");
						}
						
					}
							
				}
			});
		},
		forwardurl :function (url,remark) {
			if(null != url && url.length >0) {
				_hmt.push(['_trackEvent', window.location.href+'_click_'+url, 'click', remark, '1']);
				window.open (url, 'newwindow') ;
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
		//领取优惠券
		getCoupon : function (obj) {
			var mobile = $("#inputCouponM").val();
			var regNum = /^1[0-9]{10}$/;
			if(null != mobile.match(regNum) && mobile.match(regNum).length >0) {
				_hmt.push(['_trackEvent', window.location.href+"_领取优惠券按钮_手机号_"+mobile, 'click', '领取优惠券按钮', '1']);
				var getCouponUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForActivityCoupon?api_key=betafamilyhas&validateFlag=1&activityCode="+dlq_page.activityCode+"&mobile="+mobile;
				$.ajax({
					type: "GET",
					url: getCouponUrl,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							var date = new Date();
							date.setTime(date.getTime()+60*60*1000);
							document.cookie = "DLQpageGetcoupon=Success;expires="+data.toString();
							$(".fan_maskwrap").hide();
							var preMobile = mobile.substring(0,3);
							var endMobile = mobile.substring(mobile.length-3);
							new Toast({context:$('body'),message:"领取成功!登陆"+preMobile+"*****"+endMobile+"(手机号)\<br\/\>惠家有账户查收",top:'40%',left:'16%',time:20000}).show();
						} else if(data.resultCode == 939301303){
							$(".fan_maskwrap").hide();
							var preMobile = mobile.substring(0,3);
							var endMobile = mobile.substring(mobile.length-3);
							new Toast({context:$('body'),message:"已领取!登陆"+preMobile+"*****"+endMobile+"(手机号)\<br\/\>惠家有账户查收",top:'40%',left:'16%',time:20000}).show();
						} else {
							new Toast({context:$('.dialog'),message:data.resultMessage,top:'30%',left:'26%',time:20000}).show();
						}
					}
				})
				
			} else {
				new Toast({context:$('.dialog'),message:"请输入正确的手机号",top:'30%',left:'26%',time:20000}).show();
				_hmt.push(['_trackEvent', window.location.href+"_领取优惠券按钮_手机号输入有误", 'click', '领取优惠券按钮', '1']);
			}
		},
		closeGetCoupon : function(obj) {
			$(".fan_maskwrap").hide();
			_hmt.push(['_trackEvent', window.location.href+"_关闭领取优惠券功能", 'click', '关闭领取优惠券功能', '1']);
		},
		//统计下载量 （中转）
		open_app :function() {
			_hmt.push(['_trackEvent', window.location.href+"_下载app按钮", 'click', '下载app按钮', '1']);
			openApp(0);
		},
		//获取cookie
		getCookies: function() {
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.indexOf(name)){
				return unescape(arr[2]);
			} else {
				return null;
			}
		}
		
		
};
function callbackFunc(param,share_title,ad_name,share_content,share_pic,isShare) {
	window.share.shareOnAndroid(share_title,ad_name,share_content,share_pic,isShare);
}
