var template = {
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
		userInfo : "",//直播用户信息
		relLive : false,//是否监测有直播
		liveListApi:"",
		jobStartTime:"",
		jobEndTime:"",
		pm : "",
		init : function(url,regUrl,liveListApi){
			
			
			template.loadInit();//初始化
			
			template.initPram();
			
			if(window.screen.availHeight > 300) {
				template.pageHeight = window.screen.availHeight;
			}
			
			///pc专题模板进入web版商品详情页
			template.pcUrl = url;
			
			template.registUrl = regUrl;
			template.liveListApi = liveListApi;
			
			var clientWidth = screen.width;
			
			var loginUser = "";
	    	//校验是否是微信商城
	    	if(undefined != wxInfo.phone && null != wxInfo.phone && wxInfo.phone.length > 0) {
	    		loginUser = wxInfo.phone;
	    	} 
			
			var syurl ="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetTempletePageInfoNew?api_key=betafamilyhas&width="+clientWidth+"&pageNum="+template.up_urlparam("pNum")+"&mobile="+loginUser;
				
			$.ajax({ 
				type: "GET",
				url: syurl,
				dataType: "json",
				success: function(data) {
					
					if(data.resultCode == 1) {
						template.relLive = data.relLive;
						template.userInfo = data.userMobile_MemCode;
						template.jobStartTime = data.live_job_start_time;
						template.jobEndTime = data.live_job_end_time;
						template.serverTime = data.sysTime;
						
						template.systm = Date.parse(template.serverTime.replace(/-/g, "/"));
						  setInterval(function(){
							  template.systm = eval(template.systm + 1000);
							  template.serverTime = template.reverTime(template.systm);
						  }, 1000)
						
						var weParam = template.up_urlparam("share");
						//展示页面的标题
						$("title").html(data.pageTitle);
						try{
							OCModel.getjstitle(data.pageTitle);
						}catch(e){}
						
						//添加数据
						var tHtml = "";
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
						for(var j=0;j<List.length;j++) {
							var pc = List[j];
							if(pc.templeteType == "449747500001"){
								var ListTp =pc.pcList;
								if( ListTp.length <= 0) {
									continue;
								}
								var courntTempleteWidth = "";
								var courntTempleteClass = "banner_lunbo1";
								if( j != 0 )  {
									courntTempleteWidth = "width:1200px;margin:0 auto;";
									courntTempleteClass = "banner_lunbo";
								}
									if( ListTp.length == 1) {
										var tp = ListTp[0];
										tHtml += '<div class="main" id="mainContent" module="202014" style="background:#'+pc.templeteBackColor+';'+courntTempleteWidth+'">';
											tHtml += '<div class="banner" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)"><a><img src="'+tp.categoryImg+'"></a></div>';
										tHtml +='</div>';
									    
									    
									} else {
										tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
										tHtml +='<div class="'+courntTempleteClass+' lunbo" style="overflow:hidden;'+courntTempleteWidth+'">';
											tHtml += '<div id="uiejSwipe'+lunboCont+'" class="banner_lunbo_hd" style="'+courntTempleteWidth+'">';
											tHtml += '<div class="swipe-wrap">';
											for(var i=0;i<ListTp.length;i++){
												var tp = ListTp[i];
												tHtml += '<div module="202105" style="float：left;"><img class="categoryImg" src="'+tp.categoryImg+'" alt="" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)"></div>';
											}
											tHtml += '</div>';
											tHtml += '</div>';
											tHtml += '<div id="position'+lunboCont+'" class="banner_lunbo_bd">';
												
												for(var i=0;i<ListTp.length;i++){
													if(i == 0) {
														tHtml += '<span class="on"></span>';
													} else {
														tHtml += '<span></span>';
													}
												}
												
											tHtml += '</div>';
										tHtml +='</div>';
										tHtml += '</div>';
										lunboCont ++;
									}
										
							}else if (pc.templeteType == "449747500002") {
								//内容列表
								var ListTp =pc.pcList;
								if(ListTp.length <= 0) {
									continue;
								}
								tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
								tHtml += '<ul class="mu-list clearfix" >';
									for(var i=0;i<ListTp.length;i++){
										var tp = ListTp[i];
										/* 没换行清除浮动 */
										if(i%5 == 0 && i >0) {
											tHtml += "<div style='clear:both;'></div>";
										}
										tHtml += '<li class="mu-list-first">';
											tHtml += '<a num="202040-1"  onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ tp.productCode+'\',2)">';
												tHtml += '<div >';
												tHtml += '<img src="'+tp.pcImg+'" alt="'+tp.pcName+'">';
												tHtml += '</div>';
												tHtml += '<b alt="'+tp.pcName+'">'+tp.pcName+'</b>';
												if(tp.salesNum == "0"){
													tHtml += '<em></em>';
												}
												if(tp.pcPrice.length <=0) {
													tp.pcPrice = "0";
												}
												if(tp.marketPrice.length <=0) {
													tp.marketPrice = "0";
												}
//												tHtml += '<p style="color:#'+pc.sell_price_color+'">¥'+eval(tp.pcPrice)+'<i>¥'+eval(tp.marketPrice)+'</i></p>';
												tHtml += '<p style="color:#'+pc.sell_price_color+'">¥'+eval(tp.pcPrice)+'</p>';
												if(pc.pcBuyTypeImg.length > 0) {
													tHtml += '<span><img src="'+pc.pcBuyTypeImg+'" alt="立即购买" ></span>';
												} else {
													tHtml += '<a class="mu-qg"">立即抢购</a>';
												}
											tHtml += '</a>';
										tHtml += '</li>';
									}
								tHtml += '</ul>';
								tHtml += '</div>';
							}else if (pc.templeteType == "449747500003") {
								//内容列表
								var ListTp =pc.pcList;
								if(ListTp.length <= 0) {
									continue;
								}
								tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
								tHtml += '<ul class="mu-list02 clearfix" >';
								for(var i=0;i<ListTp.length;i++){
									var tp = ListTp[i];
									if(i%2 == 0 && i >0) {
										tHtml += "<div style='clear:both;'></div>";
									}
									tHtml += '<li class="mu-list02-first clearfix" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',2)">';
										tHtml += '<div class="mu-list02-imgWrap">';
											tHtml += '<img src="'+tp.pcImg+'" alt="'+tp.pcName+'">';
											if(tp.isShowDiscount =="449746250001") {
												var dis =eval(tp.pcDiscount * 10);
												if(10>dis && dis>0){
													tHtml += '<b>'+dis.toFixed(1)+'折</b>';
												}
											}
										tHtml += '</div>';
										
										if(pc.pcTxtColorType == "449747530002"){
											tHtml += '<div class="mu-list02-conWrap a2">';
										}else /*if(pc.pcTxtColorType == "449747530002")*/{
											tHtml += '<div class="mu-list02-conWrap a1">';
										}
										tHtml += '<h3>'+tp.pcName+'</h3>';
										tHtml += '<b>'+tp.pcDesc+'</b>';
//										tHtml += '<p>¥'+eval(tp.pcPrice)+'<i>¥'+eval(tp.marketPrice)+'</i></p>';
										tHtml += '<p>¥'+eval(tp.pcPrice)+'</p>';
										/*tHtml += '<div class="d_num">';
											tHtml += '<b class="minus"></b>';
											tHtml += ' <i>0</i>';
											tHtml += '<b class="plus"></b>';
										tHtml += '</div>';*/
										if( null != pc.pcBuyTypeImg && pc.pcBuyTypeImg!="" && pc.pcBuyTypeImg.length != 0 ){
											tHtml += '<span><img src="'+pc.pcBuyTypeImg+'" alt="立即抢购"></span>';
										}else {
											tHtml += '<a class="mu-qg" >立即抢购</a>';
										}
									
									tHtml += '</div>';
									tHtml += '</li>';
								}
								tHtml += '</ul>';
								tHtml += '</div>';
							}else if (pc.templeteType == "449747500004") {//视频模板
								template.tvUrl = pc.tvUrl;
								if(pc.templetePic.length > 0 ) {
									template.tvPic = pc.templetePic;
								} else {
									template.tvPic = "../../resources/images/template/tvimg.jpg";
								}								
								if(template.tvUrl.length > 0) {//是否有视频连接
									
									tHtml += '<div class="video">';
										tHtml += '<div class="fl">';
											tHtml += '<iframe src="../template/tvplay.ftl?'+window.location.href.split('?')[1]+'" width="100%" height="'+tvTempleteHeight+';" scrolling="no" frameborder="0" id="ifr" name="ifr" />';
										tHtml += '</div>';
										tHtml += '<div class="fr">';
											tHtml += '<div class="wares" id="focus3">';
												tHtml += '<ul class="hd" id="cont3" module="202015" style="position: relative; width: 460px; height: '+tvTempleteHeight+';">';
									
												/**
												 * 获取直播商品信息
												 */
												var list = pc.pcList;
												for(var k=0;k<list.length;k++) {
													var tp = list[k];
													
													tHtml += '<li style="position: absolute; width: 460px; left: 0px; top: 0px;">';
														tHtml += '<h2>'+tp.pcName+'</h2>';
														tHtml += '<p>';
															tHtml += '<a onclick="template.forward_url(\'4497153900060002\',\'449747550002\',\''+ tp.productCode+'\',1)">';
																tHtml += '<img src="'+tp.categoryImg+'">';
															tHtml += '</a>';
															/*tHtml += '<a onclick="TvLive.LoadProductDetail(\'150195\')" num="202015-2">';
																tHtml += '<img src="http://image.sycdn.ichsy.com/cfiles/staticfiles/imzoom/273c4/ea2e62884fc64002916fe008d1db2170.jpg">';
															tHtml += '</a>';*/
														tHtml += '</p>';
														tHtml += '<font><a onclick="template.forward_url(\'4497153900060002\',\'449747550002\',\''+ tp.productCode+'\',1)" num="202015-4">立即购买</a>¥'+tp.pcPrice+'</font>';
													tHtml += '</li>';
													
												}
												tHtml += '</ul>';
											tHtml += '</div>';
										tHtml += '</div>';
									tHtml += '</div>';
								}
							}else if (pc.templeteType == "449747500005") {//标题模板
								
								/*tHtml += '<div class="mu_hd">';
									tHtml += '<h2>';
									if(null != pc.templetePic && pc.templetePic.length > 0) {
										tHtml += '<b style="background: url('+pc.templetePic+') no-repeat;" >'+pc.template_title_name+'</b>';
									} else {
										tHtml += '<b>'+pc.template_title_name+'</b>';
									}
									tHtml += '</h2>';
								tHtml += '</div>';*/
								
							} else if(pc.templeteType == "449747500006") {//一行两栏(两栏广告)
								
							} else if(pc.templeteType == "449747500007") {//优惠券(一行两栏)
								tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
								tHtml += '<section class="ad" >';
									var ListTp =pc.pcList;
									if( ListTp.length >= 2) {
										for(var i=0;i<2;i++) {
											var tp = ListTp[i];
											tHtml += '<a onclick="template.getCoupons(\''+tp.coupon+'\','+tp.accountUseTime+',\''+tp.activity_code+'\')"><img src="'+tp.categoryImg+'" alt=""></a>';
										}
									}
								tHtml += '</section>';
								tHtml += '</div>';
							} else if(pc.templeteType == "449747500008") {//优惠券(一行一栏)
								var ListTp =pc.pcList;
								if(ListTp.length == 1) {
									var tp = ListTp[0];
									tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
									tHtml += '<div class="coupon" ><a  onclick="template.getCoupons(\''+tp.coupon+'\','+tp.accountUseTime+',\''+tp.activity_code+'\')"><img src="'+tp.categoryImg+'" alt=""></a></div>';
									tHtml += '</div>';
								}
							} else if(pc.templeteType == "449747500009"){//普通视频模板
								
							} else if(pc.templeteType == "449747500010"){//一行多栏（横滑）
								
								//获取屏幕宽度
								var wt = document.getElementById("bodyInfo").clientWidth;
								var tplist = pc.pcList;
								//先计算一下总宽度
								var reg = new RegExp("^[0-9]*$");
								var zWidth = 0;
								var otherWidth = 0;//模块直接的宽度 （margin已经padding）
								for(var c=0;c<tplist.length;c++) {
									var tp = tplist[c]; 
									if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width)) {
										zWidth = zWidth + parseInt(tp.width);
										otherWidth += 33;
									}
								}
								zWidth = zWidth /100 * wt;
								//计算
								var hd = 0;
								tHtml += '<div style="background:#'+pc.templeteBackColor+';">';
								tHtml += '<div class="bd" >';
									tHtml += '<ul class="maybeLove" id="maybeLove" style="width:1200px">';
									for(var c=0;c<tplist.length;c++) {
										var tp = tplist[c];
										if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width) ) {
											hd = wt * (parseInt(tp.width)/100);
											tHtml += '<li style="width:200px">';
												tHtml += '<a onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1);" num="202006-1">';
													tHtml += '<img src="'+tp.categoryImg+'" style="width:200px" alt="">';
												tHtml += '</a>';
											tHtml += '</li>';
										}
									}
									tHtml += '</ul>';
									
									tHtml += '<a class="prev"  num="202006-21"></a>';
									tHtml += '<a class="next"  num="202006-22"></a>';
								tHtml += '</div>';
								tHtml += '</div>';
								
							} else if(pc.templeteType == "449747500011"){//一行三栏 
								
							}else if(pc.templeteType == "449747500012"){//分类模版
								
							}else if(pc.templeteType == "449747500013"){//本地货模版
								
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
								            '<a onclick="template.getCouponsByMobile()" class="maskBtn">领取优惠券</a>'+
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
							                '<a class="maskBtn" onclick="template.toRegistHtml()">立即注册</a>'+
							            '</div>'+
							        '</div>'+
							    '</div>';
						
						
						$("#bodyInfo").html(tHtml);
						//设置轮播
						$(".lunbo").each(function(index){
							var bullets1 = document.getElementById('position'+index).getElementsByTagName('span');
							window.uiejSwipe1 = new Swipe(document.getElementById('uiejSwipe'+index), {
								  startSlide: 0,
								  speed: 400,
								  auto: 3500,
								  continuous: true,
								  disableScroll: true,
								  stopPropagation: false,
								  callback: function(pos) {
									var i=bullets1.length;
								      while (i--) {
								        bullets1[i].className = '';
								      }
								      bullets1[pos].className = 'on';  
								},
								transitionEnd: function(index, elem) {}
							});
						});
						/* 一栏横滑 */
						$(function(){
					        var index=0;
					        var count=$('#maybeLove li').length;
					        //下一页
					        $('.next').click(function(){
					            if (count > 5) {
					                ++index;
					                var abs = Math.abs(233 * 5 * index);
					                var cheap = "-" + abs + "px";
					                var marg = "+" + abs + "px";
					                if (index < 0) {
					                    cheap = "+" + abs + "px";
					                    marg = "-" + abs + "px";
					                }
					                $('#maybeLove').animate({ "left": cheap }, 300, function () {
					                    for (var i = 0; i < 5; i++) {
					                        var first = $("#maybeLove li:first").remove();
					                        $('#maybeLove').append(first);
					                    }
					                    $('#maybeLove').css({ "margin-left": marg });
					                });
					            }
					        })
					        //上一页
					         $('.prev').click(function(){
					           if (count > 5) {
					                --index;
					                var abs = Math.abs(233 * 5 *index);
					                var cheap = "-" + abs + "px";
					                var marg = "+" + abs + "px";
					                if (index< 0) {
					                    cheap = "+" + abs + "px";
					                    marg = "-" + abs + "px";
					                }
					                for (var i = 0; i < 5; i++) {
					                    var last = $("#maybeLove li:last");
					                   $('#maybeLove').prepend("<li>" + $(last).html() + "</li>");
					                    $(last).remove();
					                }
					                $('#maybeLove').css({ "margin-left": marg });
					                $('#maybeLove').animate({ left: cheap }, 300);
					            }
					        })

					    })
						
						
						/* 领取优惠券的交互 */
						$(".maskClose").click(function(){
							$(".mask").hide();
						});
											    
					    if("false"!=weParam) {//防止页面嵌套多次传递分享信息问题
					    	if(data.share) {
								setTimeout(function(){
									try{
										callbackFunc("android",data.shareTitle,data.shareImg,data.shareContent,data.shareLink,"true");
										//ios测试代码  start
										OCModel.getjsshare("ios",data.shareTitle,data.shareImg,data.shareContent,data.shareLink,"true");
										//end
									}catch(e){}
									
								},1000);
							}
					    }
					   
					}
							
				}
			});
		},
		initPram :function(){
			template.pm = template.up_urlparam("pm");
			template.appVersion = template.up_urlparam("appVersion");
			if(template.browser.versions.android) {
				template.appVersion = template.appVersion.substring(template.appVersion.indexOf("android_") + 8, template.appVersion.length);
	  		}
		},
		sendCommand :function(param){
			var url=param;
		 	//document.location = url;
		 	window.open (url, 'newwindow') ;
		 	return false;
		 },
		 forward_url :function(pcStatus,linkType,linkValue,type){
		  	var param = "";
		  	if(linkType == "449747550002") {
		  		param = template.pcUrl+linkValue+"&goods_num:"+linkValue;
		  		//判断是否是已售罄、已下架
		  		if(type == "2" || type == 2) {
		  			if(pcStatus == "4497153900060003") {//4497153900060003  下架   4497153900060002  上架
		  				new Toast({context:$('body'),message:'该商品已下架',top:'40%',left:'34%'}).show();
		  				return false;
		  			}
		  		}
		  	} else if(linkType == "449747550003") {//关键词
		  		param = "kwd_name:"+linkValue;
		  	} else if(linkType == "449747550004") {//分类搜索
		  		param = "cls_name:"+linkValue;
		  	} else if( linkType == "449747550001") {//url
		  		if(template.checkUrl(linkValue)) {
		  			param = linkValue;
		  		} else {
		  			return false;
		  		}
		  	} else if( linkType == "449747550005") {//主播详情
		  		return false;
		  		/*try{
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
			  		
		  		}catch(e){}*/
		  		return false;
		  		
		  	}
		  	template.sendCommand(param);
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
					
					$(".maskIn").attr("ichsy_coupon",activityCode);
					if(template.appVersion != "" && template.appVersion >= "3.9.2") {
						if(template.browser.versions.android) {
							window.share.obtainCouponCode(coupon);
						} else if( template.browser.versions.iPhone ) {
							window.location = "objc://getCoupon:/coupon:"+coupon;
						}
					} else {
						$(".mask1 .maskIn").val("");
						$(".mask1").show();
					}
					
				} else {
					new Toast({context:$('body'),message:"亲~优惠券已领光",top:'40%',left:'47%'}).show();
				}
				
			},
			//输入手机号领券
			getCouponsByMobile : function () {
				var mobile = $(".maskIn").val();
				var regNum = /^1[0-9]{10}$/;
				if(null != mobile.match(regNum) && mobile.match(regNum).length >0) {
					var activityCode = $(".maskIn").attr("ichsy_coupon");
					var getCouponUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForActivityCoupon?api_key=betafamilyhas&validateFlag=1&activityCode="+activityCode+"&mobile="+mobile;
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
					
				} else {
					new Toast({context:$('body'),message:"请输入正确的手机号",top:'40%',left:'26%'}).show();
				}
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
							webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部 
						}; 
					}(), 
					language: (navigator.browserLanguage || navigator.language).toLowerCase() 
				}
			},
			toRegistHtml : function(){
				$(".mask2").hide();
				window.open(template.registUrl);
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
			  }
			
};
function callbackFunc(param,share_title,ad_name,share_content,share_pic,isShare) {
	try{
		window.share.shareOnAndroid(share_title,ad_name,share_content,share_pic,isShare);
	}catch(e){}
}
