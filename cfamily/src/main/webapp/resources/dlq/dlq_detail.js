var dlq_detail = {
		info : {
			mobile : ""
		},
		appVersion : "",
		pcUrl : "",
		activityCode : "",
		clientIp : "",
		tvNumber : "",
		page_number : "",
		wx_htp :"",
		isCallBack : false,
		init : function(url,wx_htp){
			
			dlq_detail.getClientIp();
			
			dlq_detail.pcUrl = url;
			dlq_detail.wx_htp = wx_htp;
			dlq_detail.appVersion = dlq_detail.up_urlparam("appVersion");
			var param_from = dlq_detail.up_urlparam("from");
			dlq_detail.tvNumber = dlq_detail.up_urlparam("tvNumber");
			dlq_detail.page_number = dlq_detail.up_urlparam("page_number");
			var p_type = dlq_detail.up_urlparam("p_type");
			var syurl ="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetDLQContentInfo?api_key=betafamilyhas&from="+param_from+"&tv_number="+dlq_detail.tvNumber+"&page_number="+dlq_detail.page_number+"&p_type="+p_type;
			
			$.ajax({
				type: "GET",
				url: syurl,
				dataType: "json",
				success: function(data) {
					if(data.resultCode == 1) {
						$("title").html(data.special_name);
						//添加数据
						var tHtml = "";
						var YHtml = "";//N1
						var RHtml = "";//N2
						var SHtml = "";//N3 ..
						var FHtml = "";//N4 上部
						var FXHtml = "";//N4 下部
						var WHtml = "";//N5 ..
						var LHtml = "";//N6
						var QHtml = "";//N7
						var JHtml = "";//N9
						var VVHtml = "";//N10
						var pcc   = 0;//判断步骤的数
						var n5ForCount = 0;//n5系统循环的个数（不包括第一个商品）
						var page_url ="dlq_page.ftl?from="+param_from+"&tvNumber="+dlq_detail.tvNumber+"&p_type="+p_type;//跳转列表页面
						
						
						tHtml += '<div class="detail">';
							//专题名称
							/*
							 * 查看是否超过指定的推荐数
							 */
							var recomendCnt = data.recomendList.length;
							if(recomendCnt > 0) {
								tHtml += '<div class="hd"><a onclick="dlq_detail.forwardByUrl(\''+page_url+'\',\'head_查看全集\')" href="javascript:void(0);">查看全集</a>'+data.special_name+'</div>';//查看全集跳转
							}
							//视频（视频地址）
							if(data.url.length > 0) {
								var tempUrl = data.url;
								if(tempUrl.indexOf("ccvid:") >=0 ) {
									tempUrl = tempUrl.substring(6);
									if(recomendCnt > 0) {
										tHtml += '<div class="video">';
									} else {
										tHtml += '<div class="video" style="padding-top:0px;">';
									}
									tHtml += '<script src="http://p.bokecc.com/player?vid='+tempUrl+'&siteid=7408202D39A123FF&autoStart=false&width=100%&height=210&playerid=4D719E1223A93E4B&playertype=1" type="text/javascript"></script>';
									tHtml += '</div>';
								}
							}
							tHtml += '<div class="bd">';
								//N1(食材准备)
								tHtml += '<div class="food">';
									//菜系名称
									tHtml += '<h1>'+data.cuisine_name+'</h1>';
									var List = data.contents;
									for(var j=0;j<List.length;j++) {
										var pc = List[j];
										if(pc.id_number=="N1" && p_type=="1000"){//N1(食材准备)
											if(YHtml==""){
												YHtml += '<h2><b>'+pc.programa_name+'<i>'+pc.programa_english+'</i></b></h2>';
											}
											if(pc.food_name != "" && pc.food_name != null ) {
												YHtml += '<p class="clearfix"><span class="f_name">'+pc.food_name+'</span><span class="f_que">'+pc.weight+'</span></p>';
											}
										} else if(pc.id_number=="N2" && p_type=="1000"){//N2(食材盒)
											if(pc.common_number!=""){
												RHtml += '<ul class="f_info au">';
												RHtml += '<li class="f_good" >';
												RHtml += '<div class="f_img">';
												
													RHtml += '<a onclick="dlq_detail.forwardByUrl(\''+dlq_detail.pcUrl+pc.common_number+'&from='+param_from+'&goods_num:'+pc.common_number+'\',\'食材盒\')" href="javascript:void(0);"><dt>';//商品连接
													if(pc.sales_num == "" || pc.sales_num == null || pc.sales_num == "0"){
														RHtml +='<em></em>';
													}
													RHtml +='<img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pc.picture+'" alt=""></dt></a>';
													RHtml += '</div>';
													RHtml += '<div class="f_txt">';
														RHtml += '<b>'+pc.product_name+'</b>';
														RHtml += '<font><i>¥</i>'+pc.sell_price+'<em>原价：¥'+pc.mark_price+'</em></font>';
													RHtml += '</div>';
												RHtml += '</li>';
												RHtml += '</ul>';
											}
										}else if(pc.id_number=="N3"){//N3(美食全纪录)
											if(SHtml==""){
												SHtml += '<div class="record">';
												SHtml += '<h2><b>'+pc.programa_name+'<i>'+pc.programa_english+'</i></b></h2>';
												SHtml += '<ul class="au">';
											}
											if(pc.picture != "" && pc.picture != null){
												pcc++;
												SHtml += '<li>';
													SHtml += '<div class="record_pic"><img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pc.picture+'" alt=""></div>';
													if(p_type == "1000") {//大陆桥展示步骤
														SHtml += '<p class="clearfix">';
															SHtml += '<b class="re_txt_01">'+pcc+'.</b>';
															SHtml += '<b class="re_txt_02">'+pc.describe+'</b>';
														SHtml += '</p>';
													}
													SHtml += '</li>';
											}
										}else if(pc.id_number=="N4" ){//N4
											var picList = data.picList;
											for(var i=0;i<picList.length;i++){
												var pic = picList[i];
												if(pic.location == "449747760001"){//（广告上部）
													FHtml  += '<div class="ad"><a onclick="dlq_detail.forwardByUrl(\''+pic.url+'\',\'上部广告\')" href="javascript:void(0);"><img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pic.pic+'" alt=""></a></div>';
												} else if(pic.location == "449747760002"){//（广告下部）
													FXHtml += '<div class="ad"><a onclick="dlq_detail.forwardByUrl(\''+pic.url+'\',\'下部广告\')" href="javascript:void(0);"><img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pic.pic+'" alt=""></a></div>';
												}
											}
										}else if(pc.id_number=="N5"){//N5(嘉宾厨具直购)
											if(WHtml==""){
												WHtml += '<div class="direct">';
													WHtml += '<h2><b>'+pc.programa_name+'<i>'+pc.programa_english+'</i></b></h2>';
													WHtml += '<ul class="f_info">';
														WHtml += '<li class="f_good" >';
														WHtml += '<div class="f_img">';
															WHtml += '<a onclick="dlq_detail.forwardByUrl(\''+dlq_detail.pcUrl+pc.common_number+'&from='+param_from+'&goods_num:'+pc.common_number+'\',\'食材盒\')" href="javascript:void(0);"><dt>';
															if(pc.sales_num == "" || pc.sales_num == null || pc.sales_num == "0"){
																WHtml +='<em></em>';//抢光图标
															}
															WHtml +='<img class="delay" src="../../resources/images/defaultImg/bg.png" data-original="'+pc.picture+'" alt=""></dt></a>';
														WHtml += '</div>';
														WHtml += '<div class="f_txt">';
														WHtml += '<b>'+pc.product_name+'</b>';
														WHtml += '<font><i>¥</i>'+pc.sell_price+'<em>原价：¥'+pc.mark_price+'</em></font>';
														WHtml += '</div>';
														WHtml += '</li>';
													WHtml += '</ul>';
													WHtml += '<div class="f_list_wrap">';
														WHtml += '<ul class="f_list" style="width:100%">';
											} else {
												if(pc.common_number!=""){
													n5ForCount++;
													WHtml += '<li>';
													WHtml += '<div class="f_list_img">';
													WHtml += '<a onclick="dlq_detail.forwardByUrl(\''+dlq_detail.pcUrl+pc.common_number+'&from='+param_from+'&goods_num:'+pc.common_number+'\',\'进入商品详情\')" href="javascript:void(0);">';//跳转商品
													if(pc.sales_num == "" || pc.sales_num == null || pc.sales_num == "0"){
														WHtml += '<em></em>';//“抢光了”图标
													}
													WHtml += '<img src="'+pc.picture+'" alt="">';
													WHtml += '</a>';
													WHtml += '</div>';
													WHtml += '<div class="f_txt">';
													WHtml += '<b>'+pc.product_name+'</b>';
													WHtml += '<font><i>¥</i>'+pc.sell_price+'</font>';
													WHtml += '</div>';
													WHtml += '</li>';
												}
											}
										}else if(pc.id_number=="N6"){//N6(推荐)
											var rList = data.recomendList;
											for(var q=0;q<rList.length;q++){
												var recomend = rList[q];
												if(recomend.page_number != "" && recomend.page_number != null){
													if(LHtml == ""){
														LHtml += '<div class="recommend">';
														LHtml += '<h2>';
														
														LHtml += '<a onclick="dlq_detail.forwardByUrl(\''+page_url+'\',\'推荐_查看全部剧集\')" href="javascript:void(0);">查看全部剧集</a>';//跳转连接
														
														LHtml += ''+pc.programa_name+'</h2>';
														if(rList.length >= 3) {
															LHtml += '<div class="cont">';
														} else {
															LHtml += '<div class="cont" style="overflow-x: hidden;">';
														}
														LHtml += '<ul style="width:'+eval(rList.length * 155)+'px;">';
													}
													LHtml += '<li><a onclick="dlq_detail.forwardByUrl(\'dlq_detail.html?page_number='+recomend.page_number+'&from='+param_from+'&tvNumber='+dlq_detail.tvNumber+'&p_type='+p_type+'\',\'推荐_菜系详情\')" href="javascript:void(0);"><img src="'+recomend.pic_link+'" alt=""></a></li>';//跳转
												}
											}
											
										}else if(pc.id_number=="N7"  && p_type=="1000"){//N7(赞助商)
											if(QHtml == ""){
												QHtml += '<div class="friends">';
												QHtml += '<h2>'+pc.programa_name+'</h2>';
												QHtml += '<ul>';
											}
											if(pc.picture != "" && pc.picture != null){
												var picArr = pc.picture.split("|");
												for(var k =0;k<picArr.length;k++) {
													QHtml += '<li><img src="'+picArr[k]+'" alt=""></li>';
												}
											}
										} else if (pc.id_number=="N8" && pc.activity_code.length > 0) {
											dlq_detail.activityCode = pc.activity_code;
										} else if(pc.id_number=="N9") {
											JHtml += '<div class="describe">';
												JHtml += '<div>';
													JHtml += '<h1>'+pc.programa_name+'</h1>';
													JHtml += '<p class="des_con">'+pc.t_descb+'</p>';
												JHtml += '</div>';
											JHtml += '</div>';
										} else if(pc.id_number=="N10") {
											VVHtml += '<div class="describe">';
												VVHtml += '<div>';
													VVHtml += '<h2><b>'+pc.programa_name+'<i>&nbsp;</i></b></h2>';
													VVHtml += '<p class="des_con">'+pc.column_descb+'</p>';
												VVHtml += '</div>';
											VVHtml += '</div>';
										}
										
									}
						
										//判断是否在qq|微信中打开
										var ImgUrl = '';
										if(p_type=="1000") {
											if(ua.match(/MicroMessenger/i)=="micromessenger" || ua.indexOf("qq")>0 || ua.indexOf("QQ")>0) {
												ImgUrl = '<li><div class="record_pic"><img src="../../resources/dlq/img/er.jpg" alt=""></div></li>';
											}
										}
										var plStr = '';
											plStr+='<div class="fabu clearfix">';
												plStr+='<textarea id="comment_cnt" placeholder="登陆之后即可发表评论"></textarea>';
												plStr+='<b onclick="dlq_detail.subComment()">发表</b>';
											plStr+='</div>';
											
										var plListHtml = '';
											plListHtml += '<div class="comment-wrap">';
												var commentList = data.commentList;
												var lenth = commentList.length;
												if(lenth > 10 ) {
													lenth = 10;
													plListHtml += '<h2>评论<a onclick="dlq_detail.forwardToPLList(\'plList.html?t_souce='+dlq_detail.tvNumber+'-'+dlq_detail.page_number+'\',\'评论_查看全部评论\')" href="javascript:void(0);">查看全部评论</a></h2>';
												} else {
													
													plListHtml += '<h2>评论</h2>';
												}
												plListHtml += '<ul class="comment-list">';
												
												for(var i=0;i<lenth;i++) {
													var comment = commentList[i];
													plListHtml += '<li class="clearfix">';
//														plListHtml += '<div class="comment-head">'+comment.head_photo.length>0?'<img src="'+comment.head_photo+'">':'<img src="../../resources/dlq/img/personal_picture.png"'+'</div>';
														if(null != comment.head_photo && "null" != comment.head_photo && comment.head_photo.length>0) {
															plListHtml += '<div class="comment-head"><img src="'+comment.head_photo+'">'+'</div>';
														} else {
															plListHtml += '<div class="comment-head"><img src="../../resources/dlq/img/personal_picture.png" ></div>';
														}
														plListHtml += '<div class="comment-txt">';
															if(comment.mobile.length > 0) {
																plListHtml += '<p class="comment-name">'+comment.mobile+'</p>';
															} else {
																plListHtml += '<p class="comment-name">'+comment.c_ip+'</p>';
															}
															plListHtml += '<p class="comment-time">'+comment.c_time+'</p>';
															plListHtml += '<p class="comment-con">'+comment.content+'</p>';
															
															if(comment.rtn_content.length > 0) {
																plListHtml += '<div class="reply" style="overflow:hidden; padding-top:6px; margin-top:7px;border-top:1px solid #e5e5e5;">';
																	plListHtml += '<p class="reply_txt" style="color:#666;word-break: break-all;">';
																		plListHtml += '<span style="color:#d8b887;font-style: normal;">'+comment.rtn_user+': </span>';
																		plListHtml += comment.rtn_content;
																	plListHtml += '</p>';
																	plListHtml += '<span style="color:#999; font-size:10px; float:right; margin-top:3px">';
																		plListHtml += '回复于'+comment.rtn_time;
																	plListHtml += '</span>';
																plListHtml += '</div>';
																
																
															}
															
														plListHtml += '</div>';
													plListHtml += '</li>';
												}
												plListHtml += '</ul>';
											plListHtml += '</div>';
											
										if(p_type=="1000") {
											tHtml += YHtml;
											tHtml += RHtml;
											tHtml += '</div>';
											tHtml += SHtml.length > 0 ? SHtml +ImgUrl+ '</ul><div class="more" style="display: none;"><img src="../../resources/dlq/img/more.png" alt="查看全部步骤"></div></div>': '<div class="record"><ul class="au">'+ImgUrl+'</ul></div>';
											tHtml += FHtml;
											tHtml += WHtml.length > 0 ? WHtml + '</ul></div></div>':'';
											tHtml += LHtml.length > 0 ? LHtml+ '</ul></div></div>' :''; 
											tHtml += FXHtml;
											tHtml += plListHtml;
											tHtml += plStr;
											tHtml += QHtml.length > 0 ?QHtml +'</ul></div>':'';
											tHtml += "</div>";
										} else if( p_type=="1001") {
											tHtml += '</div>';
											tHtml += JHtml;
											tHtml += SHtml.length > 0 ? SHtml + '</ul></div>': '';
											tHtml += VVHtml;
											tHtml += FHtml;
											tHtml += WHtml.length > 0 ? WHtml + '</ul></div></div>':'';
											tHtml += LHtml.length > 0 ? LHtml+ '</ul></div></div>' :''; 
											tHtml += FXHtml;
											tHtml += plListHtml;
											tHtml += plStr;
											tHtml += "</div>";
										}
										if(dlq_detail.appVersion.length <= 0 ) {
											tHtml += '<div class="splendid" ><a  href="javascript:void(0);" onclick="dlq_detail.open_app()">下载APP</a>更多精彩内容，请在惠家有中查看</div>' ;
										}
										
										tHtml += '</div>' ;

									/*
									 * 添加领取优惠券功能 (领取优惠券功能取消)
									 */	
									/*var pageGetcoupon = document.cookie.indexOf("DLQdetailGetcoupon=Success;");
									if( "undefined" != dlq_detail.activityCode && dlq_detail.activityCode.length >0 && pageGetcoupon <0) {
										tHtml += '<div class="fan_maskwrap"><div class="fan_mask"><div class="dialogWrap"><div class="dialog"><div class="logoWrap clearfix"><p class="logo1"></p><p class="and">&</p><p class="logo2"></p></div>'+
										'<p class="p1">领取惠家有优惠券，购物自动减免10元！</p><input id="inputCouponM" type="tel" placeholder="输入手机号，领取优惠" /><a onclick="dlq_detail.getCoupon(this)" href="javascript:void(0);">领取优惠券</a><div class="close" onclick="dlq_detail.closeGetCoupon(this)"></div></div></div></div></div>';
									}*/
									
						
						$("body").html(tHtml);
						//嘉宾厨具直购 横滑 的宽度计算
						$(".f_list").css("width",eval(120*n5ForCount)+"px");
						//展示动作屏蔽
						/*$('.more').click(function(){
							var open_status = $('.record ul').attr("open_status");
							if(open_status == "open") {//需要展示全部
								$('.record ul').attr("open_status","close");
								$('.record ul').css('height','auto');
								$('.record .more img:eq(0)').attr("src","../../resources/dlq/img/less.png");
								_hmt.push(['_trackEvent', window.location.href+'_click_查看全部步骤', 'click', '查看全部步骤', '1']);
							} else {//只展示两条
								$('.record ul').attr("open_status","open");
								$('.record ul').css('height','2.08rem');
								$('.record .more img:eq(0)').attr("src","../../resources/dlq/img/more.png");
								_hmt.push(['_trackEvent', window.location.href+'_click_收起全部步骤', 'click', '收起全部步骤', '1']);
							}
						});*/
						
						$("img.delay").lazyload({threshold:100,placeholder:""});
						
						$(".cont").scroll(function () {
							$(".cont img.delay").lazyload({threshold:100,placeholder:""});
						});
						
						$('.splendid').click(function(){
							$(this).hide();	
							_hmt.push(['_trackEvent', window.location.href+'_click_更多精彩内容，请在惠家有中查看', 'click', '更多精彩内容，请在惠家有中查看', '1']);
						});
						
						var shareInfo = data.share_info;
						var share_link = window.location.href;
						
						if(shareInfo.share_pic.length>0) {
							try{
								callbackFunc("android",shareInfo.share_title,shareInfo.share_pic,shareInfo.share_content,share_link,"true");
							}catch(e){}
						}
						
					}
					
					
					/*
					 * 判断是否是发表评论回调页面
					 */
					var p = dlq_detail.up_urlparam("p");
					if(null != p && p.length > 0 && dlq_detail.cookieContrl.checkCookie("HJY_PLcontent")) {
						dlq_detail.info.mobile = p;
						dlq_detail.isCallBack = true;
						//登陆之后需要发表评论
						dlq_detail.subComment();
					}
					
							
				}
			});
			
			
			
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
		
			return decodeURIComponent(sReturn);
		
		},
		//统计下载量 （中转）
		open_app :function() {
			_hmt.push(['_trackEvent', window.location.href+"_下载app按钮", 'click', '下载app按钮', '1']);
			openApp(0);
		},
		//各种跳转，不为别的，只为百度统计
		forwardByUrl :function (url,remark) {
			if(null != url && url.length >0) {
				_hmt.push(['_trackEvent', window.location.href+'_click_'+url, 'click', remark, '1']);
				window.open (url, 'newwindow') ;
			}
		},
		forwardToPLList :function(url,remark) {
			if(null != url && url.length >0) {
				_hmt.push(['_trackEvent', window.location.href+'_click_'+url, 'click', remark, '1']);
				window.location.href = url;
			}
		},
		//领取优惠券
		getCoupon : function (obj) {
			var mobile = $("#inputCouponM").val();
			var regNum = /^1[0-9]{10}$/;
			if(null != mobile.match(regNum) && mobile.match(regNum).length >0) {
				_hmt.push(['_trackEvent', window.location.href+"_领取优惠券按钮_手机号_"+mobile, 'click', '领取优惠券按钮', '1']);
				var getCouponUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForActivityCoupon?api_key=betafamilyhas&validateFlag=1&activityCode="+dlq_detail.activityCode+"&mobile="+mobile;
				$.ajax({
					type: "GET",
					url: getCouponUrl,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							var date = new Date();
							date.setTime(date.getTime()+60*60*1000);
							document.cookie = "DLQdetailGetcoupon=Success;expires="+data.toString();
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
				});
				
			} else {
				new Toast({context:$('.dialog'),message:"请输入正确的手机号",top:'30%',left:'26%',time:20000}).show();
				_hmt.push(['_trackEvent', window.location.href+"_领取优惠券按钮_手机号输入有误", 'click', '领取优惠券按钮', '1']);
			}
		},
		closeGetCoupon : function(obj) {
			$(".fan_maskwrap").hide();
			_hmt.push(['_trackEvent', window.location.href+"_关闭领取优惠券功能", 'click', '关闭领取优惠券功能', '1']);
		},
		subComment : function () {
			if(!dlq_detail.isCallBack) {
				_hmt.push(['_trackEvent', window.location.href+'_click_发表评论', 'click', "发表评论", '1']);
			}
			
			var comment_cnt = "";
			/*
			 * 判断是否只登陆之后需要发表评论
			 */
			if(dlq_detail.isCallBack) {
				comment_cnt = decodeURIComponent(dlq_detail.cookieContrl.getCookie("HJY_PLcontent"));
				dlq_detail.isCallBack = false;
				//清楚缓存
				dlq_detail.cookieContrl.clearCookie("HJY_PLcontent");
			} else {
				comment_cnt = $("#comment_cnt").val();
			}
			comment_cnt = dlq_detail.stripstr(comment_cnt);
			
			if(comment_cnt.length <= 0) {
				new Toast({context:$('body'),message:"请填写评论内容",top:'30%',left:'26%',time:5000}).show();
				return false;
			}
			
			/**
			 * 判断用户是否登陆
			 */
			if( undefined == dlq_detail.info.mobile || dlq_detail.info.mobile.length <= 0 ) {//非登录状态
				if(dlq_detail.appVersion.length <= 0) {//不在app中
					if(dlq_detail.cookieContrl.checkCookie("HJY_PLcontent")) {
						dlq_detail.cookieContrl.clearCookie("HJY_PLcontent");
					}
					dlq_detail.cookieContrl.setCookie("HJY_PLcontent", encodeURIComponent(comment_cnt), 0.5);
					dlq_detail.tologin();
				} else {
					//从不同渠道去查用户信息
					if(browser.versions.android){
						var androidInfo = window.share.getDataToJs(0);
						var info = eval('('+androidInfo+')');
						dlq_detail.info.mobile = info.mobilephone;
					} else if(browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
						var iosInfo = OCModel.getDataToJs(0);
						dlq_detail.info.mobile = iosInfo.mobilephone;
					}
					
				}
				
			} 
			if(!dlq_detail.regMobile(dlq_detail.info.mobile)) {
				new Toast({context:$('body'),message:"发表失败~~",top:'30%',left:'26%',time:20000}).show();
				return false;
			}
			var source = dlq_detail.tvNumber + "-" + dlq_detail.page_number;
			var addComment = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiDLQCommentAdd?api_key=betafamilyhas&content="+comment_cnt+"&rel_source="+source+"&ip="+dlq_detail.clientIp+"&mobile="+dlq_detail.info.mobile;
			$.ajax({
				type: "GET",
				url: addComment,
				dataType: "json",
				success: function(result) {
					if(result.resultCode == 1) {
						new Toast({context:$('body'),message:"发表成功",top:'30%',left:'36%',time:5000}).show();
						$("#comment_cnt").val("");
						dlq_detail.reloadComment();
					} else {
						new Toast({context:$('body'),message:"发表失败!!!",top:'30%',left:'36%',time:5000}).show();
					}
				}
			});
			
		},
		getClientIp : function() {
			//获取ip地址
			$.ajax({
				type : "GET",
				url : "../../../cfamily/getclientIp",
				dataType : "json",
				success: function(data) {
					dlq_detail.clientIp = data.clientIp;
				}
			})
		},
		reloadComment : function() {
			var vHtml = "";
			var source = dlq_detail.tvNumber+"-"+dlq_detail.page_number;
			var reqUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetDLQComment?api_key=betafamilyhas&source="+source;
			$.ajax({
				type: "GET",
				url: reqUrl,
				dataType: "json",
				success: function(data) {
					if(data.resultCode == 1) {
						
						var commentList = data.commentList;
						var le = commentList.length;
						if(le > 10) {
							$(".comment-wrap h2").html('评论<a onclick="dlq_detail.forwardToPLList(\'plList.html?t_souce='+dlq_detail.tvNumber+'-'+dlq_detail.page_number+'\',\'评论_查看全部评论\')" href="javascript:void(0);">查看全部评论</a>');
							le = 10;
						}
						for(var i=0;i<le;i++) {
							
							var comment = commentList[i];
							vHtml += '<li class="clearfix">';
								if(null != comment.head_photo && "null" != comment.head_photo && comment.head_photo.length>0) {
									vHtml += '<div class="comment-head"><img src="'+comment.head_photo+'">'+'</div>';
								} else {
									vHtml += '<div class="comment-head"><img src="../../resources/dlq/img/personal_picture.png" ></div>';
								}
								vHtml += '<div class="comment-txt">';
									if(comment.mobile.length > 0) {
										vHtml += '<p class="comment-name">'+comment.mobile+'</p>';
									} else {
										vHtml += '<p class="comment-name">'+comment.c_ip+'</p>';
									}
									vHtml += '<p class="comment-time">'+comment.c_time+'</p>';
									vHtml += '<p class="comment-con">'+comment.content+'</p>';
									
									if(comment.rtn_content.length > 0) {
										vHtml += '<div class="reply" style="overflow:hidden; padding-top:6px; margin-top:7px;border-top:1px solid #e5e5e5;">';
											vHtml += '<p class="reply_txt" style="color:#666;word-break: break-all;">';
												vHtml += '<span style="color:#d8b887;font-style: normal;">'+comment.rtn_user+': </span>';
												vHtml += comment.rtn_content;
											vHtml += '</p>';
											vHtml += '<span style="color:#999; font-size:10px; float:right; margin-top:3px">';
												vHtml += '回复于'+comment.rtn_time;
											vHtml += '</span>';
										vHtml += '</div>';
										
										
									}
								vHtml += '</div>';
							vHtml += '</li>';
							
						}
						
						$(".comment-list").html(vHtml);
					}
					
				}
			});
		},
		stripstr :function (s) {
		    var pattern = new RegExp("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?]");
		    var rs = "";
		    for (var i = 0; i < s.length; i++) {
		        rs = rs + s.substr(i, 1).replace(pattern, '');
		    }
		    return rs;
		},
		//login
		tologin: function () {
			
			window.location.href = dlq_detail.wx_htp+'Account/AppGroup.html?app=huijiayou&from=web&to=login&wxopenid=&t='+Math.random()+'&backurl='+encodeURIComponent(window.location.href);
			
		},
		cookieContrl:  {
			//设置cookie
			setCookie:function (cname, cvalue, exdays) {
			    var d = new Date();
			    d.setTime(d.getTime() + (exdays*24*60*60*1000));
			    var expires = "expires="+d.toUTCString();
			    document.cookie = cname + "=" + cvalue + "; " + expires;
			},
			//获取cookie
			getCookie:function (cname) {
			    var name = cname + "=";
			    var ca = document.cookie.split(';');
			    for(var i=0; i<ca.length; i++) {
			        var c = ca[i];
			        while (c.charAt(0)==' ') c = c.substring(1);
			        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
			    }
			    return "";
			},
			//清除cookie  
			clearCookie:function (name) {  
			    dlq_detail.cookieContrl.setCookie(name, "", -1);  
			},
			checkCookie:function (name) {
				var cname = dlq_detail.cookieContrl.getCookie(name);
				if(null != cname && cname.length>0) {
					return true;
				} else {
					return false;
				}
			}
			
		},
		regMobile : function(phone) {
			var regNum = /^1[0-9]{10}$/;
			if(null != phone.match(regNum) && phone.match(regNum).length >0) {
				return true;
			} else {
				return false;
			}
		}
};
function callbackFunc(param,share_title,ad_name,share_content,share_pic,isShare) {
	window.share.shareOnAndroid(share_title,ad_name,share_content,share_pic,isShare);
}
//用于用户等成功发表评论
function appBackInfoToFun (info) {
	if(browser.versions.android){
		dlq_detail.info.mobile = info.mobilephone;
	} else if(browser.versions.ios || browser.versions.iPhone) {
		dlq_detail.info.mobile = info.mobilephone;
	}
	dlq_detail.subComment();
	
}
