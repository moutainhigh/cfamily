template_type = {
	position_template_type_pre_id : "hjy_",
	smg_template_pc_code_pre : "IC_SMG_",
	app_smg_template_pc_code_pre : "IC_APPSMG_",
	template_18_position_temp_height : 40,//页面定位模板中，定位高度微调
	template_21_position_temp_height :40,
	lunboCont:0,//轮播模板使用
	timeNum:0,
	dValue:0,   //设备和服务器时间差
	loopCount:0, //循环次数
	fontSize:"",//big 为大字体展示
	type_switch : function(type , pc) {
		var returnHtml = "";

		switch(type) 
		{ 
			case "449747500001": returnHtml = template_type.type_1(pc);break;//轮播模版
			case "449747500002": returnHtml = template_type.type_2(pc);break;//两栏多行模版（pc五栏多行模板）
			case "449747500003": returnHtml = template_type.type_3(pc);break;//一栏多行模版（pc两栏多行模板）
			case "449747500004": returnHtml = template_type.type_4(pc);break;//视频模板
			case "449747500005": returnHtml = template_type.type_5(pc);break;//标题模板
			case "449747500006": returnHtml = template_type.type_6(pc);break;//两栏广告
			case "449747500007": returnHtml = template_type.type_7(pc);break;//优惠券(一行两栏)
			case "449747500008": returnHtml = template_type.type_8(pc);break;//优惠券（一行一栏）
			case "449747500009": returnHtml = template_type.type_9(pc);break;//普通视频模板
			case "449747500010": returnHtml = template_type.type_10(pc);break;//一行多栏（横滑）
			case "449747500011": returnHtml = template_type.type_11(pc);break;//一行三栏
			case "449747500012": returnHtml = template_type.type_12(pc);break;//分类模版
			case "449747500013": returnHtml = template_type.type_13(pc);break;//本地货模版
			case "449747500014": returnHtml = template_type.type_14(pc);break;//两栏多行（推荐）
			case "449747500015": returnHtml = template_type.type_15(pc);break;//右两栏推荐
			case "449747500016": returnHtml = template_type.type_16(pc);break;//左两栏推荐
			case "449747500017": returnHtml = template_type.type_17(pc);break;//扫码购模版
			case "449747500018": returnHtml = template_type.type_18(pc);break;//页面定位模板,定位模板会关联其他模板
			case "449747500019": returnHtml = template_type.type_19(pc);break;//倒计时模板
			case "449747500020": returnHtml = template_type.type_20(pc);break;//积分兑换模板
			case "449747500021": returnHtml = template_type.type_21(pc);break;//定位横滑模板
			case "449747500022": returnHtml = template_type.type_22(pc);break;//兑换码兑换模板 修复代码被覆盖BUG
			case "449747500023": returnHtml = template_type.type_23(pc);break;//拼团模板
			case "449747500024": returnHtml = template_type.type_24(pc);break;//悬浮模板
			case "449747500025": returnHtml = template_type.type_25(pc);break;//精编商品模板
			case "449747500026": returnHtml = template_type.type_26(pc);break;//拼团大图模板
		} 
		return returnHtml;
	},		
	type_1 : function(pc) {
		var ListTp =pc.pcList;
		var tHtml = "";
		if( ListTp.length <= 0) {
			return tHtml;
		}
		if( ListTp.length == 1) {
			tHtml +='<div class="banner" style="background:#'+pc.templeteBackColor+';">';
				tHtml += '<div class="hd">';
					tHtml += '<div class="swipe-wrap">';
					for(var i=0;i<ListTp.length;i++){
						var tp = ListTp[i];
						tHtml += '<div module="202105"><img class="categoryImg" onerror="template.imageLoadError(this)" src="'+tp.categoryImg+'" alt="" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)"></div>';
					}
					tHtml += '</div>';
				tHtml += '</div>';
			tHtml +='</div>';
		} else {
			tHtml +='<div class="banner banner_lunbo" style="background:#'+pc.templeteBackColor+';">';
				tHtml += '<div id="uiejSwipe'+template_type.lunboCont+'" class="swiper-container">';
				tHtml += '<div class="swiper-wrapper">';
				for(var i=0;i<ListTp.length;i++){
					var tp = ListTp[i];
					tHtml += '<div class="swiper-slide" module="202105"><img class="categoryImg" onerror="template.imageLoadError(this)" src="'+tp.categoryImg+'" alt="" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)"></div>';
				}
				tHtml += '</div>';
				tHtml += '</div>';
				tHtml += '<div id="position'+template_type.lunboCont+'" class="pagination">';
				//if(ListTp.length != 1) {//如果只有一个轮播图，则不展示轮播下标点
					
					for(var i=0;i<ListTp.length;i++){
						if(i == 0) {
							tHtml += '<span class="on"></span>';
						} else {
							tHtml += '<span></span>';
						}
					}
					
				//}
				tHtml += '</div>';
			tHtml +='</div>';
			template_type.lunboCont ++;
		}
		
		return tHtml;
	},
	type_2 : function(pc) {
		//内容列表
		var ListTp =pc.pcList;
		var tHtml = "";
		if(ListTp.length <= 0) {
			return tHtml;
		}
		
		tHtml += '<div id="type_2" class="mobile_2" style="background:#'+pc.templeteBackColor+';">';
			tHtml += '<section class="rebate">';
				tHtml += '<ul>';
				for(var i=0;i<ListTp.length;i++){
					var tp = ListTp[i];
//							if(i<6){
								tHtml += '<li module="202105" class="type2">';
//							}else{
//								tHtml += '<li module="202105" style="display: none;" class="type2">';
//							}
								tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ tp.productCode+'\',2)">';
									tHtml += '<div style="position: relative;"><img style="display:block" data-src="'+tp.pcImg+'" src="../../resources/images/defaultImg/pc_list_defalt_main_img.png" alt="" class="type2">';
//									if(tp.productLabelPicList && tp.productLabelPicList.length > 0) {
//										tHtml += '<img src="'+tp.productLabelPicList[0]+'" style="position: absolute;width: 25%;left: 0px;bottom: 0px;">';
//									}
									if(tp.labelsInfo && tp.labelsInfo.length > 0){
										//通栏下类型
										for(var v=0;v<tp.labelsInfo.length;v++){
											var info = tp.labelsInfo[v];
											if(info.labelPosition=="449748430005"){
												tHtml += '<img src="'+info.listPic+'" class="tonglan-562">';
											}
										}
										//通栏上类型
										tHtml += '<div class="si_left-div">';
										var ssi = 0;
										for(var v=0;v<tp.labelsInfo.length;v++){
											var info = tp.labelsInfo[v];
											if(info.labelPosition=="449748430001"&&ssi<4){
												tHtml += '<img src="'+info.listPic+'" class="si_left-top_2">';
												ssi++;
											}
										}
										tHtml += '</div>';
									}
									if(tp.salesNum == "0"){
										tHtml += '<em style="margin-top: -.32rem;"></em>';
									}
									tHtml += '</div>';
																		
									
									if(pc.pcTxtBackType == "449747520001"){
										tHtml += '<p style="background:url("'+pc.pcTxtBackPic+'");">';
									}else if(pc.pcTxtBackType == "449747520002"){
										tHtml += '<p style="background:#'+pc.pcTxtBackVal+';">';
									}
									if(tp.pcPrice.length <=0) {
										tp.pcPrice = "0";
									}
									if(tp.marketPrice.length <=0) {
										tp.marketPrice = "0";
									}
									tHtml += '<b>'+tp.pcName+'</b>';
									//显示活动、券标签，解决代码被覆盖问题
									//var tagList = tp.tagList;
									//tHtml += '<font class="quan_542">';
									//for(var j = 0;j<tagList.length;j++){
									//	tHtml += '<span class="tv_quan1">'+tagList[j]+'</span>';
									//}
									//tHtml += '</font>';
									if(tp.isShowDiscount =="449746250001") {
										var dis =eval(tp.pcDiscount * 10);
										if(10>dis && dis>0){
											tHtml += '<span>'+dis.toFixed(1)+'折</span>';
										}
									}
									
									// 划线价
									var skuPriceText = "";
									if(eval(tp.pcPrice) < eval(tp.skuPrice)) {
										skuPriceText += '<i>¥'+eval(tp.skuPrice)+'</i>';
									}
									
									tHtml += '<font><c>¥</c>'+eval(tp.pcPrice)+skuPriceText+'</font>';
									
									//if(pc.pcBuyTypeImg.length > 0) {
									//	tHtml += '<span><img src="'+pc.pcBuyTypeImg+'" alt="立即购买"></span>';
									//} else {
									//	tHtml += '<span><img src="../../resources/images/template/ljgm.png" alt="立即购买"></span>';
									//}
									tHtml += '</p>';
								tHtml += '</a>';
							tHtml += '</li>';
				}
				tHtml += '</ul>';
			tHtml += '</section>';
		tHtml += '</div>';
		
		return tHtml;
	},
	type_3 : function(pc) {
		//内容列表
		var ListTp =pc.pcList;
		console.info("商品數量"+ListTp.length)
		var tHtml = "";
		if(ListTp.length <= 0) {
			return tHtml;
		}
		tHtml += '<div class="mobile" style="background:#'+pc.templeteBackColor+';">';
		for(var i=0;i<ListTp.length;i++){
			var tp = ListTp[i];
			var classTagUrl = tp.proClassifyTag;//自营标签
			if(pc.pcTxtColorType == "449747530002"){
				tHtml += '<section class="goods a2" module="202106">';
			}else /*if(pc.pcTxtColorType == "449747530002")*/{
				tHtml += '<section class="goods a1" module="202106">';
			}
			tHtml += '<div class="bg"></div>';
			tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',2)">';
			tHtml += '<div style="height: 1.1rem;width: 1.1rem;position:absolute;left:0;top:0;"><img style="height: 1.1rem;width: 1.1rem;" src="'+tp.pcImg+'" onerror="template.imageLoadError(this)" alt="">';
//			if(tp.productLabelPicList && tp.productLabelPicList.length > 0) {
//				tHtml += '<img src="'+tp.productLabelPicList[0]+'" style="position: absolute;width: 0.3rem;height: auto;left: -0.07rem;top: -0.07rem;">';
//			}
			if(tp.labelsInfo && tp.labelsInfo.length > 0){
				//通栏下类型
				for(var v=0;v<tp.labelsInfo.length;v++){
					var info = tp.labelsInfo[v];
					if(info.labelPosition=="449748430005"){
						tHtml += '<img src="'+info.listPic+'" class="tonglan-562">';
					}
				}
				//通栏上类型
				tHtml += '<div class="si_left-div">';
				var ssi = 0;
				for(var v=0;v<tp.labelsInfo.length;v++){
					var info = tp.labelsInfo[v];
					if(info.labelPosition=="449748430001"&&ssi<4){
						tHtml += '<img src="'+info.listPic+'" class="si_left-top_3">';
						ssi++;
					}
				}
				tHtml += '</div>';
			}
			tHtml += '</div>';
			tHtml += '<p>';
			//自营标签
			if(classTagUrl!=''){
				tHtml += '<b><img src="'+classTagUrl+'" class="proClassifyTag">'+tp.pcName+'</b>';	
			}else{
				tHtml += '<b>'+tp.pcName+'</b>';
			}
			tHtml += '<em>'+tp.pcDesc+'</em>';
			//显示活动、券标签
			var tagList = tp.tagList;
			tHtml += '<b class="quan_542_1">';
			for(var j = 0;j<tagList.length;j++){
				tHtml += '<span class="tv_quan1">'+tagList[j]+'</span>';
			}
			tHtml += '</b>';
			if(tp.pcPrice.length <=0) {
				tp.pcPrice = "0";
			}
			if(tp.marketPrice.length <=0) {
				tp.marketPrice = "0";
			}
			// 划线价
			var skuPriceText = "";
			if(eval(tp.pcPrice) < eval(tp.skuPrice)) {
				skuPriceText += '<i>¥'+eval(tp.skuPrice)+'</i>';
			}
			if( null != pc.pcBuyTypeImg && pc.pcBuyTypeImg!="" && pc.pcBuyTypeImg.length != 0 ){
//				tHtml += '<font><span><img src="'+pc.pcBuyTypeImg+'" alt="马上抢购"></span><i>¥'+eval(tp.marketPrice)+'</i>¥'+eval(tp.pcPrice)+'</font>';
				if(tp.buttonType=='1'){
					tHtml += '<font><span>立即购买 &gt;</span>¥'+eval(tp.pcPrice)+skuPriceText+'</font>';
				}else if(tp.buttonType=='2'){
					tHtml += '<font><span>去拼团 &gt;</span></span>¥'+eval(tp.pcPrice)+skuPriceText+'</font>';
				}
			}else {
//				tHtml += '<font><span><img src="../../resources/images/template/msgm.png" alt="马上抢购"></span><i>¥'+eval(tp.marketPrice)+'</i>¥'+eval(tp.pcPrice)+'</font>';
				if(tp.buttonType=='1'){
					tHtml += '<font><span>立即购买 &gt;</span>¥'+eval(tp.pcPrice)+skuPriceText+'</font>';
				}else if(tp.buttonType=='2'){
					tHtml += '<font><span>去拼团 &gt;</span></span>¥'+eval(tp.pcPrice)+skuPriceText+'</font>';
				}
			}
			tHtml += '</p>';
			tHtml += '</a>';
			tHtml += '</section>';
		}
		tHtml += '</div>';
		return tHtml;
	},
	type_4 : function(pc) {
		template.tvUrl = pc.tvUrl;
		var vedioHtml = "";
		if(pc.templetePic.length > 0 ) {
			template.tvPic = pc.templetePic;
		} else {
			template.tvPic = "../../resources/images/template/tvimg.jpg";
		}								
		if(template.tvUrl.length > 0) {//是否有视频连接
			
			vedioHtml += '<div style="width:100%;line-height: 0;" class="ichsy_tv_show">';
				vedioHtml += '<iframe src="../template/tvplay.ftl?'+window.location.href.split('?')[1]+'" width="100%" height="'+tvTempleteHeight+';" scrolling="no" frameborder="0" id="ifr" name="ifr" />';
			/**
			 * 获取直播商品信息
			 */
			var list = pc.pcList;
			for(var k=0;k<list.length;k++) {
				var tp = list[k];
				vedioHtml +='<div class="video">';
					vedioHtml += '<div class="bd " module="202107">';
						vedioHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\'4497153900060002\',\'449747550002\',\''+ tp.productCode+'\',1)">';
							vedioHtml += '<img src="'+tp.categoryImg+'" alt="">';
							vedioHtml += '<b>'+tp.pcName+'</b>';
//							vedioHtml += '<p><i>立即购买</i>¥<strong>'+tp.pcPrice+'</strong><em>¥'+tp.marketPrice+'</em></p>';
							vedioHtml += '<p><i>立即购买</i>¥<strong>'+tp.pcPrice+'</strong></p>';
						vedioHtml += '</a>';
					vedioHtml += '</div>';
				vedioHtml += '</div>';
			}
			vedioHtml += '</div>';
			
		}
		return vedioHtml;
	},
	type_5 : function(pc) {
		var tHtml= "";
		tHtml += '<section class="special">';
		if(null != pc.templetePic && pc.templetePic.length > 0) {
			tHtml += '<h2><img src="'+pc.templetePic+'">'+pc.template_title_name+'</h2>';
		} else {
			tHtml += '<h2>'+pc.template_title_name+'</h2>';
		}
		tHtml += '</section>';
		return tHtml;
	},
	type_6 : function(pc) {
		var ListTp =pc.pcList;
		var tHtml = "";
		var t_Length = ListTp.length;
		if(t_Length > 1) {
			if(t_Length > 2) { //加此判断只为限制展示前两个
				t_Length = 2;
			}
			tHtml += '<section class="ad"  module="202107" style="background:#'+pc.templeteBackColor+';">';
			for(var i=0;i<t_Length;i++){
				var tp = ListTp[i];
				if(tp.openTypeVal.length > 0) {
					tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ tp.openTypeVal+'\',1)">';
					tHtml += '<img src="'+tp.categoryImg+'" onerror="template.imageLoadError(this)" alt="">';
					if(tp.salesNum == "0"){
						tHtml += '<em></em>';
					}
				} else {
					tHtml += '<a href="javascript:void(0);">';
					tHtml += '<img src="'+tp.categoryImg+'" alt="">';
				}
				tHtml += '</a>';
			}
			tHtml += '</section>';
		}
		return tHtml;
	},
	type_7 : function(pc) {
		var tHtml = "";
		tHtml += '<section class="ad" style="background:#'+pc.templeteBackColor+';">';
		var ListTp =pc.pcList;
		if( ListTp.length >= 2) {
			for(var i=0;i<2;i++) {
				var tp = ListTp[i];
				tHtml += '<a href="javascript:void(0);" onclick="template.getCoupons(\''+tp.coupon+'\','+tp.accountUseTime+',\''+tp.activity_code+'\')"><img src="'+tp.categoryImg+'" alt=""></a>';
			}
		}
		tHtml += '</section>';
		return tHtml;
	},
	type_8 : function(pc) {
		var tHtml = "";
		var ListTp =pc.pcList;
		if(ListTp.length > 0) {
			var wt = document.getElementById("bdy").clientWidth;
			//tHtml = '<div class="banner" style="background:#'+pc.templeteBackColor+';">';
			var reg = new RegExp("^[0-9]*[\.]?[0-9]*$");
			var div_width = 0;
			for(var i=0;i<ListTp.length;i++) {
				var tp = ListTp[i];
				var t_a_width = 0;
				if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width)) {
					t_a_width = parseFloat(tp.width)/100 * wt;
					t_a_width = template_type.round(t_a_width,2); // 保留两位小数
					t_a_width = t_a_width - 0.01; // 解决火狐浏览器上面内容超过容器宽度，造成浮动换行的问题
					div_width += t_a_width;
				}
				tHtml += '<a href="javascript:void(0);" style="width:'+t_a_width+'px;float: left;"  onclick="template.getCoupons(\''+tp.coupon+'\','+tp.accountUseTime+',\''+tp.activity_code+'\')"><img src="'+tp.categoryImg+'" alt=""></a>';
			}
			tHtml = '<div class="banner" style="background:#'+pc.templeteBackColor+';"><div style="width:'+div_width+'px">'+ tHtml+"</div></div>";
		}
		return tHtml;
	},
	type_9 : function(pc) {
		var tHtml = "";
		tHtml += '<div class="mu_vidio-wrap" style="background:#'+pc.templeteBackColor+';">';
			tHtml += '<ul class="mu_vidio_list">';
			var tplist = pc.pcList;
			for(var p=0;p<tplist.length;p++) {
				var tp = tplist[p]; 
				var temp = "";
				if(p == 0) {
					temp = "cur";
				}
				tHtml += '<li class="mu_vidio '+temp+'">';
				if(tp.url.length > 0){//判断是否有视频
					var spUrl = tp.url;
					tHtml += '<div>';
					tHtml += '<script src="https://p.bokecc.com/player?vid='+spUrl+'&siteid=7408202D39A123FF&autoStart=false&width=100%&height=210&playerid=4D719E1223A93E4B&playertype=1"type="text/javascript"></script>';
					tHtml += '</div>';
				}
				// 划线价
				var skuPriceText = "";
				if(eval(tp.pcPrice) < eval(tp.skuPrice)) {
					skuPriceText += '<i class="old_price">¥'+eval(tp.skuPrice)+'</i>';
				}
				if(tp.productCode !=null && tp.productCode != ""){
					tHtml += '<div class="live-goods-info">';
					tHtml += '<h1>';
					tHtml += '<a href="javascript:void(0);"  onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ tp.productCode+'\',2)">'+tp.pcName+'</a>';//跳转商品详情
					tHtml += '</h1>';						 
//					tHtml += '<p><i class="f12">¥</i><i class="f18">'+tp.pcPrice+'</i><i class="old_price">￥'+tp.marketPrice+'</i></p>';//pcPrice(商品价格)marketPrice(商品市场价)
					tHtml += '<p><i class="f12">¥</i><i class="f18">'+tp.pcPrice+'</i>'+skuPriceText+'</p>';//pcPrice(商品价格)marketPrice(商品市场价)
					tHtml += '<a class="mu_buy" href="javascript:void(0);"  onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ tp.productCode+'\',2)">立即购买</a>';//跳转商品详情
					tHtml += '</div>';
				}
				
				tHtml += '</li>';
				
				break;//应需求只取一条数据
			}
			tHtml += '</ul>';
			
			/*
			 * 热播横滑屏蔽
			tHtml += '<h2 class="hote_v_h">热播</h2>';
			//计算宽度
			var wh = 160 * parseInt(tplist.length);
			
			tHtml += '<div class="hote_v" >';
				tHtml += '<ul class="hote_v_list clearfix" style = "width:'+wh+'px">';
				for(var t=0;t<tplist.length;t++) {
					var tp = tplist[t];
					var temp = "";
					if(t == 0) {
						temp = "cur";
					}
					tHtml += '<li class="'+temp+'">'+tp.template_desc+'<b></b></li>';
				}
				tHtml += '</ul>';        
			tHtml += '</div>';*/
		tHtml += '</div>'; 
		return tHtml;
	},
type_10 : function(pc) {
		
		var tHtml = "";
		//获取屏幕宽度
		var versionFlag=true;
		var wt = document.getElementById("bdy").clientWidth;
		var columnNum = pc.columnNum;
		var subWidth = 0;
		if(columnNum=="44975011002"){subWidth = 50}
		else if(columnNum=="44975011003"){subWidth = 33.33}
		else if(columnNum=="44975011004"){subWidth = 25}
		else{versionFlag=false;}
		
		var tplist = pc.pcList;
		//先计算一下总宽度
		var reg = new RegExp("^[0-9]*[\.]?[0-9]*$");
		var zWidth = 0;
		for(var c=0;c<tplist.length;c++) {
			var tp = tplist[c]; 				
				if(versionFlag){
					zWidth = zWidth+parseFloat(subWidth);
				}else{
				   if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width)) {
					zWidth = zWidth + parseFloat(tp.width);	
				    }
				}
				
		}
		zWidth = zWidth /100 * wt;
		
		// 商品名称和价格
//		var templProduct = "<div class=\"goods-item-wrap\"><h1 class=\"goods-item-tit\">#productName#</h1><span class=\"goods-item-price\"><i>¥</i>#pcPrice#<b>¥#marketPrice#</b></span></div>";
		var templProduct = "<div class=\"goods-item-wrap\"><h1 class=\"goods-item-tit\">#productName#</h1><span class=\"goods-item-price\"><i>¥</i>#pcPrice# #skuPriceText#</span></div>";
		//计算
		var hd = 0;
		tHtml += '<div class="mu_02_botm_wrap" style="padding-bottom:.07rem ; background:#'+pc.templeteBackColor+';">';
			tHtml += '<ul class="mu_02_botm clearfix"  style="width:'+zWidth+'px">';
			for(var c=0;c<tplist.length;c++) {
				var tp = tplist[c];
				if(subWidth > 0) {
					hd = template_type.round(wt * (parseFloat(subWidth)/100),2); // 保留两位小数
				} else {
					hd = template_type.round(wt * (parseFloat(tp.width)/100),2); // 保留两位小数
				}
				
				// 忽略异常数据
				if(parseFloat(tp.width) > 100) {
					continue;
				}
				
				hd = hd - 0.01; // 解决火狐浏览器上面内容超过容器宽度，造成浮动换行的问题
				tHtml += '<li style="width:'+hd+'px;position: relative;">';
					tHtml += '<a href="javascript:void(0);"><div style="position: relative;"><img src="'+(tp.categoryImg == '' ? tp.pcImg : tp.categoryImg)+'" onerror="template.imageLoadError(this)" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';//跳转商品详情
					if(tp.openType == '449747550002' && tp.categoryImg == ''){ // 打开方式是商品且未传栏目图时显示商品名称和价格
//						if(tp.productLabelPicList && tp.productLabelPicList.length > 0) {
//							// 一栏横滑
//							tHtml += '<img src="'+tp.productLabelPicList[0]+'" style="position: absolute;width: 25%;left: 0px;top: 0px">';
//						}
						if(tp.labelsInfo && tp.labelsInfo.length > 0){
							//通栏下类型
							for(var v=0;v<tp.labelsInfo.length;v++){
								var info = tp.labelsInfo[v];
								if(info.labelPosition=="449748430005"){
									tHtml += '<img src="'+info.listPic+'" class="tonglan-562">';
								}
							}
							//通栏上类型
							tHtml += '<div class="si_left-div">';
							var ssi = 0;
							for(var v=0;v<tp.labelsInfo.length;v++){
								var info = tp.labelsInfo[v];
								if(info.labelPosition=="449748430001"&&ssi<4){
									tHtml += '<img src="'+info.listPic+'" class="si_left-top_3">';
									ssi++;
								}
							}
							tHtml += '</div>';
						}
						
						tHtml += '</div>';
						
						
						
						// 划线价
						var skuPriceText = "";
						if(eval(tp.pcPrice) < eval(tp.skuPrice)) {
							skuPriceText += '<b style="display: inline;">¥'+eval(tp.skuPrice)+'</b>';
						}
						
						tHtml += templProduct.replace('#productName#',tp.pcName).replace('#pcPrice#',tp.pcPrice).replace('#skuPriceText#',skuPriceText);
					}
					tHtml += '</a>';
				tHtml += '</li>';				
			}
			tHtml += '</ul>';
		tHtml += '</div>';
		return tHtml;
	},
	
	type_11 : function(pc) {
		var tHtml = "";
		tHtml += '<div class="" style="background:#'+pc.templeteBackColor+';padding: 3px 0;">';
		var templProduct_2 = "<div class=\"goods-item-wrap\"><h1 class=\"goods-item-tit\">#productName_2#</h1><span class=\"goods-item-price\"><i>¥</i>#pcPrice_2# #skuPriceText_2#</span></div>";
			var tplist = pc.pcList;
			var lenth = tplist.length>3 ?3 :tplist.length;
			if(pc.splitBar == "449748410001"){
				tHtml +='<ul class="mu_02_top clearfix" style="margin:0;padding:0">';
			}else{
				tHtml +='<ul class="mu_02_top clearfix">';
			}
			
			for(var a=0;a<lenth;a++) {
				var tp = tplist[a]; 
				if(tp.openType == '449747550002' && tp.categoryImg == ''){
				//if(pc.splitBar == "449748410001"){
					//tHtml += '<li style="width:33.333%;margin-left:0">';
					tHtml += '<li class="proClass">';
				}else{
					tHtml += '<li class="notproClass">';
				}
				
				tHtml += '<a href="javascript:void(0);"><img src="'+(tp.categoryImg == '' ? tp.pcImg : tp.categoryImg)+'" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';//跳转商品详情
				// 打开方式是商品且未传栏目图时显示商品主图
				if(tp.openType == '449747550002' && tp.categoryImg == ''){ 
				tHtml+= '<div class="goods-item-wrap1"><h1 class="goods-item-tit1">'+tp.pcName+'</h1><span class="goods-item-price1"><i>¥</i>'+tp.pcPrice+'</span><span class="qiang-561">抢</span></div></a>';
				}else{
					tHtml+= '<div ></div></a>';
				}
				tHtml += '</li>';
			}
			tHtml += '</ul>';
		tHtml += '</div>';
		return tHtml;
	},
	type_12 : function(pc) {
		
		var tHtml = "";
		tHtml += '<div class="" style="background:#'+pc.templeteBackColor+';">';
			//1
			tHtml += '<div class="mu_02_botm_wrap">';
			//计算宽度
			var tplist = pc.pcList;
			//获取屏幕宽度
			var wt = document.getElementById("bdy").clientWidth;
			var zWidth = 0;
			//先计算一下总宽度
			var reg = new RegExp("^[0-9]*$");
			for(var c=0;c<tplist.length;c++) {
				var tp = tplist[c]; 
				if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width)) {
					zWidth = zWidth + parseInt(tp.width);
				}
			}
			zWidth = zWidth /100 * wt; // 保留两位小数
			
				tHtml += '<ul class="mu_02_botm clearfix" style="width:'+zWidth+'px">';
				var hd = 0;
				for(var b=0;b<tplist.length;b++){
					var tp = tplist[b];
					var temp = "";
					if(b == 0) {
						temp = "act";
					}
					if(undefined != tp.width && tp.width.length > 0 && reg.test(tp.width) ) {
						hd = template_type.round(wt * (parseInt(tp.width)/100),2); // 保留两位小数
						hd = hd - 0.01; // 解决火狐浏览器上面内容超过容器宽度，造成浮动换行的问题
						tHtml += '<li class="mu_02_botm_li '+temp+'" style="width:'+hd+'px;">';
						 tHtml += '<a href="javascript:void(0);"><img src="'+tp.categoryImg+'" _src="'+tp.img+'"></a>';  
			            tHtml += '</li>';
					}
				}
				tHtml += '</ul>';
			tHtml += '</div>';
			//2
			tHtml += '<div >';
			for(var c=0;c<tplist.length;c++){
				template.iframeList[c] = tplist[c];
			}
			if(tplist.length >0 ) {
				//添加来源参数
				var t = tplist[0].url ;
				if(null != template.from && template.from.length >0) {
					t = template.replaecPram(t, "from", template.from);
				}
				
				if(t.indexOf('?') > 0) {
					tHtml += '<iframe src="'+t+'&frd=yes&share=false" frameborder="0" scrolling="auto"  id="showiframe_local" name="ifr" style="width:100%; height:'+template.pageHeight+'px;"></iframe>';
				} else {
					tHtml += '<iframe src="'+t+'?frd=yes&share=false" frameborder="0" scrolling="auto"  id="showiframe_local" name="ifr" style="width:100%; height:'+template.pageHeight+'px;"></iframe>';
				}
				
			} 
			tHtml += '</div>';
		tHtml += '</div>';
		return tHtml;
	},
	type_13 : function(pc) {
		var gpsHtml = "";
		 gpsHtml += '<div class="" style="background:#'+pc.templeteBackColor+';">';
		    var showCityRelIframeUrl = "";
		    var tplist = pc.pcList;
		   if(tplist.length > 0) {
			   	gpsHtml += '<div class="citySwitch_nav_wrap_kong" style="background:#'+pc.templeteBackColor+';display: block;"></div>';
			    //更新本地货定位头展示
			    gpsHtml += '<div class="citySwitch_nav_wrap" style="position: fixed;">';
				    	var updateCss = "";
			    		
			    		if( tplist.length == 2) {
			    			gpsHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav citySwitch_nav13" style="margin:0px;padding:0px;">';
					    		gpsHtml += '<ul class="clearfix">';
						    	updateCss = 'width: 50%;';
					    } else if(tplist.length == 1 ) {
					    	gpsHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav citySwitch_nav13" style="margin:0px;padding:0px;">';
					    		gpsHtml += '<ul class="clearfix">';
					    		updateCss = 'width: 100%;';
					    } else {
					    	gpsHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav citySwitch_nav13">';
				    			gpsHtml += '<ul class="clearfix">';

					    }
			    		var isHadPosition = false;
			    		var cityNameStr = "";//用于临时记录拼接好的城市名称，如果没有匹配到本地城市，则默认展示第一个城市
			    		var firstCityNameInfo = "";
			    		
					    for(var r=0;r<tplist.length;r++){
					    	var t = tplist[r].city_name;
			    			if(t.length > 5) {
			    				t = template_type.hjy_subStr(t,0,5) + '...';
			    			}
					    	//判断第一个是否是定位城市
					    	if(r == 0) {
					    		if(returnCitySN["cname"].substring(0,2) == tplist[r].city_name) {//是，则直接定位第一个
					    			cityNameStr += '<li class="curr" iframeUrl="'+tplist[r].url+'" style="color:#'+tplist[r].title_checked_color+';'+updateCss+'" click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'"><span>'+t+'</span></li>';
						    		showCityRelIframeUrl = tplist[r].url;
						    		isHadPosition = true;
					    		} else {//否则记录第一个城市，因为如果没有匹配的城市，则第一个城市为默认城市
					    			firstCityNameInfo = tplist[r];
					    		}
					    	} else {
					    		if(isHadPosition == false && returnCitySN["cname"].substring(0,2) == tplist[r].city_name) {
						    		cityNameStr += '<li class="curr" iframeUrl="'+tplist[r].url+'" style="color:#'+tplist[r].title_checked_color+';'+updateCss+'"  click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'"><span>'+t+'</span></li>';
						    		showCityRelIframeUrl = tplist[r].url;
						    		isHadPosition = true;
						    	} else {									    		
						    		cityNameStr += '<li iframeUrl="'+tplist[r].url+'" style="color:#'+tplist[r].title_color+';'+updateCss+'" click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'"><span>'+t+'</span></li>';
						    	}
					    	}
					    	
			    		}
					    
					    if("" != firstCityNameInfo) {
					    	var t = firstCityNameInfo.city_name;
			    			if(t.length > 5) {
			    				t = template_type.hjy_subStr(t,0,5) + '...';
			    			}
					    	if (!isHadPosition) {
					    		cityNameStr = '<li class="curr" iframeUrl="'+firstCityNameInfo.url+'" style="color:#'+firstCityNameInfo.title_checked_color+';'+updateCss+'" click_before_color="#'+firstCityNameInfo.title_color+'" click_after_color="#'+firstCityNameInfo.title_checked_color+'"><span>'+firstCityNameInfo.city_name+'</span></li>' + cityNameStr;
					    		showCityRelIframeUrl = firstCityNameInfo.url;
					    	} else {
					    		cityNameStr = '<li iframeUrl="'+firstCityNameInfo.url+'" style="color:#'+firstCityNameInfo.title_color+';'+updateCss+'" click_before_color="#'+firstCityNameInfo.title_color+'" click_after_color="#'+firstCityNameInfo.title_checked_color+'"><span>'+t+'</span></li>' + cityNameStr;
					    	}
					    	
					    }
					    
					    gpsHtml +=  cityNameStr;
			    		gpsHtml += '</ul>';
				    	/*gpsHtml += '<span class="mask_left"></span>';
				    	gpsHtml += '<span class="mask_right"></span>';*/
			    	gpsHtml += '</div>';
			    	//gpsHtml += '<span class="open" onclick="template_type.position_select_switch_13(1,this)"></span>';
			    	if(tplist.length >= 3) {//超过三个才能展示多个
			    		gpsHtml += '<span class="open" onclick="template_type.position_select_switch_13(1,this)"></span>';
			    	} 
				    gpsHtml += '<div class="switchWrap">';
					    gpsHtml += '<div class="switch">';
						    gpsHtml += '<h2>切换位置<span class="close" onclick="template_type.position_select_switch_13(0,this)"></span></h2>';
						    gpsHtml += '<ul class="cityList clearfix">';
						    for(var r=0;r<tplist.length;r++){
						    	
						    	var t = tplist[r].city_name;
				    			if(t.length > 5) {
				    				t = template_type.hjy_subStr(t,0,5) + '...';
				    			}
						    	
						    	//是否已经能匹配
						    	if(!isHadPosition && r == 0 ) {//没有匹配到，则取第一个
						    		gpsHtml += '<li class="on" style="color:#'+tplist[r].title_checked_color+'" click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'" iframeUrl="'+tplist[r].url+'">'+ t+'</li>';
						    	} else {
						    		if( returnCitySN["cname"].substring(0,2) == tplist[r].city_name) {
						    			gpsHtml += '<li class="on" style="color:#'+tplist[r].title_checked_color+'" click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'" iframeUrl="'+tplist[r].url+'">'+ t+'</li>';
						    		} else {
						    			gpsHtml += '<li style="color:#'+tplist[r].title_color+'" click_before_color="#'+tplist[r].title_color+'" click_after_color="#'+tplist[r].title_checked_color+'" iframeUrl="'+tplist[r].url+'">'+ t+'</li>';
						    		}
						    	}
				    		}
						    gpsHtml += '</ul>';
					    gpsHtml += '</div>';
				    gpsHtml += '</div>';
			    gpsHtml += '</div>';
			    
				//2.展示的页面
				gpsHtml += '<div class="" id="ifr_show_parent_div">';
				if(tplist.length >0 ) {
					template.isPosition = true;
					//添加来源参数
					var t = showCityRelIframeUrl ;
					if(null != template.from && template.from.length >0) {
						t = template.replaecPram(t, "from", template.from);
					}
					
					if(t.indexOf('?') > 0) {
						gpsHtml += '<iframe src="'+t+'&frd=yes&share=false" frameborder="0" scrolling="auto"  id="showiframe_local" name="ifr" style="width:100%; height:'+template.pageHeight+'px;"></iframe>';
					} else {
						gpsHtml += '<iframe src="'+t+'?frd=yes&share=false" frameborder="0" scrolling="auto"  id="showiframe_local" name="ifr" style="width:100%; height:'+template.pageHeight+'px;"></iframe>';
					}
					
				} else {
					gpsHtml += '<iframe src="" frameborder="0"  scrolling="auto"  id="showiframe_local" name="ifr" style="width:100%;height:'+template.pageHeight+'px;"></iframe>';
				}
				gpsHtml += '</div>';
		   }
			
			gpsHtml += '</div>';
		return gpsHtml;
	},
	type_14 : function(pc) {
		var tHtml = "";
		var tplist = pc.pcList;
		if(tplist.length > 0) {
			tHtml += '<div style="overflow: hidden;">';
			tHtml += '<ul class="hot-market clearfix" style="background:#'+pc.templeteBackColor+';">';
			for (var i = 0; i < tplist.length; i++) {
				/* 没换行清除浮动 */
				if(i%2 == 0 && i >0) {
					tHtml += "<div style='clear:both;'></div>";
				}
				var tp = tplist[i];
				tHtml += '<li>';
					tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';
						tHtml += '<img src="'+tp.categoryImg+'" onerror="template.imageLoadError(this)" alt="">';
						tHtml += '<aside class="market-dec">';
							var temp_title = tp.title;
							var temp_des_content = tp.des_content;
							if(temp_title.length > 6) {//最多展示六个字
								temp_title = template_type.hjy_subStr(temp_title,0,6 ) +"...";
								//temp_title = temp_title.substring(0,6)+"...";
							}
							tHtml += '<h1 style="color:#'+tp.title_color+'">'+temp_title+'</h1>';
							//描述：每行十个字，最多两行，显示不下  ...  
							if(temp_des_content.length > 20) {
								temp_des_content = template_type.hjy_subStr(temp_des_content,0,10 ) + "<br/>" + template_type.hjy_subStr(temp_des_content,10,20) + "<br/>" +template_type.hjy_subStr(temp_des_content,20);
							} else if(temp_des_content.length > 10){
								temp_des_content = template_type.hjy_subStr(temp_des_content ,0,10) + "<br/>" + template_type.hjy_subStr(temp_des_content,10);
							} 
							tHtml += '<p style="color:#'+tp.des_content_color+'">'+temp_des_content+'</p>';
						tHtml += '</aside>';
					tHtml += '</a>';
				tHtml += '</li>';
			}
			tHtml += '</ul>';
			tHtml += '</div>';
		}
		return tHtml;
	},
	type_15 : function(pc) {
		var tHtml = "";
		var tplist = pc.pcList;
		var temLeng = tplist.length;
		if(temLeng > 0) {
			if(temLeng > 3) {
				temLeng = 3;
			}
			tHtml += '<div class="column-right clearfix" style="background:#'+pc.templeteBackColor+';">';
			var t_title = "";
			var t_des_content = "";
			for (var i = 0; i < temLeng; i++) {
				var tp = tplist[i];
				if(pc.splitBar == "449748410001"){
					tHtml += '<a href="javascript:void(0);" style="border:none" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';
				}else{
					tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';
				}
					tHtml += '<img style="display:block;float:left" src="'+tp.categoryImg+'" onerror="template.imageLoadError(this)" alt="">';
					tHtml += '<aside class="column-dec">';
					 	t_title = tp.title;
					 	t_des_content = tp.des_content;
						if(t_title.length > 6 ) {
							t_title = template_type.hjy_subStr(t_title,0,6) + "...";
						}
						//描述：每行十个字，最多两行，显示不下  ...  
						if(t_des_content.length > 20) {
							t_des_content = template_type.hjy_subStr(t_des_content,0,10 ) + "<br/>" + template_type.hjy_subStr(t_des_content,10,20) + "<br/>" +template_type.hjy_subStr(t_des_content,20);
						} else if(t_des_content.length > 10){
							t_des_content = template_type.hjy_subStr(t_des_content ,0,10) + "<br/>" + template_type.hjy_subStr(t_des_content,10);
						} 
						
						tHtml += '<h1 style="color:#'+tp.title_color+'">'+t_title+'</h1>';
						tHtml += '<p style="color:#'+tp.des_content_color+'">'+t_des_content+'</p>';
					tHtml += '</aside>';
				tHtml += '</a>';
			}
			tHtml += '</div>';
		}
		return tHtml;
	},
	type_16 : function(pc) {
		
		var tHtml = "";
		var tplist = pc.pcList;
		var temLeng = tplist.length;
		if(temLeng > 0) {
			if(temLeng > 3) {
				temLeng = 3;
			}
			
			tHtml += '<div class="column-left clearfix" style="background:#'+pc.templeteBackColor+';">';
			var t_title = "";
			var t_des_content = "";
			for (var i = 0; i < temLeng; i++) {
				var tp = tplist[i];
				if(pc.splitBar == "449748410001"){
					tHtml += '<a href="javascript:void(0);" style="border:none" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';
				}else{
					tHtml += '<a href="javascript:void(0);" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)">';
				}
					tHtml += '<img src="'+tp.categoryImg+'" onerror="template.imageLoadError(this)" alt="">';
					tHtml += '<aside class="column-dec">';
						t_title = tp.title;
					 	t_des_content = tp.des_content;
						if(t_title.length > 6 ) {
							t_title = template_type.hjy_subStr(t_title,0,6) + "...";
						}
						//描述：每行十个字，最多两行，显示不下  ...  
						if(t_des_content.length > 20) {
							t_des_content = template_type.hjy_subStr(t_des_content,0,10 ) + "<br/>" + template_type.hjy_subStr(t_des_content,10,20) + "<br/>" +template_type.hjy_subStr(t_des_content,20);
						} else if(t_des_content.length > 10){
							t_des_content = template_type.hjy_subStr(t_des_content ,0,10) + "<br/>" + template_type.hjy_subStr(t_des_content,10);
						} 
						tHtml += '<h1 style="color:#'+tp.title_color+'">'+t_title+'</h1>';
						tHtml += '<p style="color:#'+tp.des_content_color+'">'+t_des_content+'</p>';
					tHtml += '</aside>';
				tHtml += '</a>';
			}
			tHtml += '</div>';
		}
		return tHtml;
	},
	type_17 : function(pc) {
		
		var tHtml = "";
		var tplist = pc.pcList;
		if(tplist.length > 0) {
			tHtml += '<div style="overflow:hidden;">';
			tHtml += '<ul class="codeBuy" style="background:#'+pc.templeteBackColor+';">';
			for (var i = 0; i < tplist.length; i++) {
				var tp = tplist[i];
				//判断是web扫码还是app扫码
				if(template.appVersion != "") {//code=prend+skuCode
					tHtml += '<li onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ template_type.app_smg_template_pc_code_pre+tp.pcNum+'\',2)">';
				} else {//code=prend+skuCode
					tHtml += '<li onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+ template_type.smg_template_pc_code_pre+tp.pcNum+'\',2)">';
				}
					tHtml += '<div class="pro-img"><img src="'+tp.pcImg+'" onerror="template.imageLoadError(this)">';
					
					if(tp.productLabelPicList.length > 0){
						tHtml += '<img src="'+tp.productLabelPicList[0]+'" style="position: absolute;width: 16%;left: 0px;">';
					}
					
					tHtml += '</div>';
					
					// 划线价
					var skuPriceText = "";
					if(eval(tp.pcPrice) < eval(tp.skuPrice)) {
						skuPriceText += '<span class="oldPrice" style="display: inline;"><em>￥</em>'+eval(tp.skuPrice)+'</span>';
					}
					
					tHtml += '<p class="pro-name">'+tp.pcName+'</p>';
					tHtml += '<div class="pro-price">';
						tHtml += '<em class="nprice"><em>￥</em>'+eval(tp.pcPrice)+'</em>';
						tHtml += skuPriceText;
						tHtml += '<b class="pro-tip">'+tp.preferential_desc+'</b>';
					tHtml += '</div>';
					tHtml += '<div class="buyBtn">立即购买</div>';
					
				tHtml += '</li>';
			}
			tHtml += '</ul>';
			tHtml += '</div>';
		}
		
		return tHtml;
	},
	type_18 : function(pc) {
		
		var tHtml = "";
		var tplist = pc.pcList;
		if(tplist.length > 0) {
			
			var rel_template_html_li_list = "";//关联模板拼接
			var switch_html_li_list = "";//下拉展示li拼接
			tHtml += '<div class="citySwitch_nav_wrap_kong" style="background:#'+pc.templeteBackColor+';"></div>';
			tHtml += '<div class="citySwitch_nav_wrap citySwitch_nav_wrap18" style="background:#'+pc.templeteBackColor+';">';
//				tHtml += '<div id="citySwitch_nav" class="citySwitch_nav">';
//					tHtml += '<ul class="noContent clearfix">';
					var updateCss = "";
					if( tplist.length == 2) {
						tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav" style="margin:0px;padding:0px;">';
							tHtml += '<ul class="noContent clearfix">';
					    	updateCss = 'width: 50%;';
				    } else if(tplist.length == 1 ) {
				    	tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav" style="margin:0px;padding:0px;">';
				    		tHtml += '<ul class="noContent clearfix">';
				    		updateCss = 'width: 100%;';
				    } else {
				    	tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav">';
				    		tHtml += '<ul class="noContent clearfix">';
				    }
					
					for (var i = 0; i < tplist.length; i++) {
						/**
						 * 导航展示li拼接
						 */
						var tp = tplist[i];
						var position_uid = template_type.upGuid();
						var tpTitle = tp.title;
						if(tpTitle.length > 5) {
							tpTitle = template_type.hjy_subStr(tpTitle,0,5) + "...";
							//tpTitle = tpTitle.substring(0,5) + "...";
						}
						if(i == 0) {
							tHtml += '<li class="curr" style="color:#'+tp.title_checked_color+';'+updateCss+'" onclick="template_type.position_template_type(\''+position_uid+'\',this);" show_type="0" position_id="'+position_uid+'" click_before_color="#'+tp.title_color+'" click_after_color="#'+tp.title_checked_color+'">'+tpTitle+'</li>';
							switch_html_li_list += '<li style="color:#'+tp.title_checked_color+';'+updateCss+'" class="on" onclick="template_type.position_template_type(\''+position_uid+'\',this);" show_type="1" position_id="'+position_uid+'" click_before_color="#'+tp.title_color+'" click_after_color="#'+tp.title_checked_color+'">'+tpTitle+'</li>';
						} else {
							tHtml += '<li style="color:#'+tp.title_color+';'+updateCss+'" onclick="template_type.position_template_type(\''+position_uid+'\',this);" position_id="'+position_uid+'" show_type="0" click_before_color="#'+tp.title_color+'" click_after_color="#'+tp.title_checked_color+'">'+tpTitle+'</li>';
							switch_html_li_list += '<li style="color:#'+tp.title_color+';'+updateCss+'" onclick="template_type.position_template_type(\''+position_uid+'\',this);" position_id="'+position_uid+'" show_type="1" click_before_color="#'+tp.title_color+'" click_after_color="#'+tp.title_checked_color+'">'+tpTitle+'</li>';
						}
						/**
						 * 关联模板拼接
						 */
						var rel_templete = tp.rel_templete;
						rel_template_html_li_list += '<div class="hjy_postion" id="'+ template_type.position_template_type_pre_id + position_uid +'">';//点击定位置顶使用
						//专题模板切换调用
						if(rel_templete.length > 0) {
							for(var k=0;k< rel_templete.length ; k++) {
								rel_template_html_li_list += template_type.type_switch(rel_templete[k].templeteType,rel_templete[k]);
							}
						}
						rel_template_html_li_list += '</div>';
						
					}
					tHtml += '</ul>';
					/*tHtml += '<span class="mask_left"></span>';
					tHtml += '<span class="mask_right"></span>';*/
				tHtml += '</div>';
				if(tplist.length > 3) {
					tHtml += '<span class="open" onclick="template_type.position_select_switch_18(1,this)"></span>';
				}
				
				//<!--切换位置（类别）-->
				tHtml += '<div class="switchWrap">';
					tHtml += '<div class="switch">';
						tHtml += '<h2>切换位置<span class="close" onclick="template_type.position_select_switch_18(0,this)"></span></h2>';
						tHtml += '<ul class="productList clearfix" style="display:block;">';
							tHtml += switch_html_li_list;
						tHtml += '</ul>';
					tHtml += '</div>';
				tHtml += '</div>';
				tHtml += '<div class="switchMask" style="display:none;"></div>';
				
				
			tHtml += '</div>';
			
			
			/*
			 * 添加本地货模板关联的内容
			 */
			tHtml += rel_template_html_li_list;
			
		}
		
		
		
		return tHtml;
	},
	/**
	 * 倒计时控件调节
	 * @param pc
	 * @returns
	 */
	type_19 : function(pc) {
		
		var tbColor = pc.templeteBackColor;
		var ListTp =pc.pcList;
		var tHtml = "";
		var temHtml = "";
		template_type.timeNum = ListTp.length;
		for(var i=0;i<template_type.timeNum;i++){
			var tStyle = "";
			var tp = ListTp[i];
			if(tp.img==""){
				tStyle = 'background-color:#'+tbColor;
			}
			else{
				tStyle = 'background:url('+tp.img+') no-repeat; background-size:100% 100%;';
			}
			
			
			temHtml = '<article class="djs" style="'+tStyle+'"><p>'
			      +'<span class="hr_djs" style="border-color:#'+tp.title_color+'"></span>'
			      +'<span class="t_djs" style= "color:#'+tp.title_color+'">'+tp.title+'</span>'
			      +'<span class="hr_djs" style="border-color:#'+tp.title_color+'"></span></p>'
			      +'<p class="tj_sk">'
			      +'<span class="sj_span tian1_'+i+'">0</span>'
			      +'<span class="sj_span tian2_'+i+'">0</span>'
			      +'<span class="sj_zi">天</span>'
			      +'<span class="sj_span shi1_'+i+'">0</span>'
			      +'<span class="sj_span shi2_'+i+'">0</span>'
			      +'<span class="sj_zi">时</span>'
			      +'<span class="sj_span fen1_'+i+'">0</span>'
			      +'<span class="sj_span fen2_'+i+'">0</span>'
			      +'<span class="sj_zi">分</span>'
			      +'<span class="sj_span miao1_'+i+'">0</span>'
			      +'<span class="sj_span miao2_'+i+'">0</span>'
			      +'<span class="sj_zi">秒</span></p>'
					+'</article>';
			 tHtml+= template_type.getDate(i,tp.targetTime,temHtml);
		}

		return tHtml;
	},	
	
	/**
	 * 积分兑换优惠券模版
	 */
	type_20 : function(pc) {
		if(pc.pcList && pc.pcList.length == 0) return '';
		
		var tHtml = [];
		tHtml.push('<div class="jifendh_542">')
		tHtml.push('<ul class="jfdh_ul_542">')
			 
		var	tem = '<li class="#li-class#" data-code="#couponTypeCode#" onclick="template.showCouponExchange(this,\'#couponTypeCode#\')">';
				tem += '<div class="coupon-info clearfix">';
					tem += '<div class="coupon-price">';
						tem += '<span>#moneyHtml#</span>';  // <span><i>¥</i>1000</span>
						tem += '<em>#limitMoneyHtml#</em>';
					tem += '</div>';
					tem += '<h1 class="coupon-txt"><span>#moneyTypeHtml#</span>#limitScopeHtml#</h1>';
					tem += '<p class="coupon-time">#timeHtml#</p>';
				tem += '</div>';
				tem += '<p class="jifen_h">#exchangeValueHtml#</p>'; // 花费积分<br/><strong>3000</strong>
			tem += '</li>';
		
		var tp;
		var itemHtml;
		for(var i=0;i<pc.pcList.length;i++){
			tp = pc.pcList[i];
			
			itemHtml = tem;
			itemHtml = itemHtml.replace(/#couponTypeCode#/g, tp.couponType.couponTypeCode);
			itemHtml = itemHtml.replace('#limitScopeHtml#', tp.couponType.limitScope);
			
			// 样式
			if(tp.couponType.exchangeStatus == 1) {
				itemHtml = itemHtml.replace('#li-class#', 'yilingqu');
			} else if(tp.couponType.exchangeStatus == 2) {
				itemHtml = itemHtml.replace('#li-class#', 'lingguang');
			} else {
				itemHtml = itemHtml.replace('#li-class#', '');
			}
			
			// 优惠券面额和类型
			if(tp.couponType.moneyType == '449748120001') { // 金额券
				itemHtml = itemHtml.replace('#moneyHtml#', '<i>¥</i>'+tp.couponType.money);
				itemHtml = itemHtml.replace('#moneyTypeHtml#', '金额券');
			} else if(tp.couponType.moneyType == '449748120002') { // 折扣券
				var money = (parseInt(tp.couponType.money) / 10).toFixed(1); // 保留1位小数
				if(money >= 10 || money <= 0) {
					// 排除异常情况
					continue;
				}
				itemHtml = itemHtml.replace('#moneyHtml#', money + '<i>折</i>');
				itemHtml = itemHtml.replace('#moneyTypeHtml#', '折扣券');
			} else {
				continue;
			}
			
			// 使用金额限额
			if(parseInt(tp.couponType.limitMoney) > 0) {
				itemHtml = itemHtml.replace('#limitMoneyHtml#', '满'+tp.couponType.limitMoney+'元可用');
			} else {
				itemHtml = itemHtml.replace('#limitMoneyHtml#', '无金额门槛');
			}
			
			// 过期时间
			if(tp.couponType.validType == '4497471600080001') { // 天数
				itemHtml = itemHtml.replace('#timeHtml#', '领取后'+tp.couponType.validDay+'天内有效');
			} else if(tp.couponType.validType == '4497471600080002') { // 日期范围
				var startTime = tp.couponType.startTime.substr(0,10).replace('-','.')
				var endTime = tp.couponType.endTime.substr(0,10).replace('-','.')
				itemHtml = itemHtml.replace('#timeHtml#', startTime + '-' + endTime);
			}
			
			// 限定是积分兑换类型，并且兑换的值必须是数字
			if('4497471600390002' == tp.couponType.exchangeType && /\d+/.test(tp.couponType.exchangeValue)){
				if(tp.couponType.exchangeStatus == 0) {
					itemHtml = itemHtml.replace('#exchangeValueHtml#', '花费积分<br/><strong>' + tp.couponType.exchangeValue + '</strong>');
				} else {
					itemHtml = itemHtml.replace('#exchangeValueHtml#', '');
				}
			}else {
				continue;
			}
			
			template.exchangeCouponTypeMap[tp.couponType.couponTypeCode] = tp.couponType;
			tHtml.push(itemHtml);
		}

		tHtml.push('</ul>');
		tHtml.push('</div>');
		return tHtml.join('');
	},	
	type_21 : function(pc) {

		var iphone_w = window.screen.width;
		var li_w3 = iphone_w/3;
		var li_w4 = iphone_w/4;
		var li_w5 = iphone_w/5;

		var tHtml = "";
		var tplist = pc.pcList;
		if(tplist.length > 0) {
			
			var rel_template_html_li_list = "";//关联模板拼接
			var switch_html_li_list = "";//下拉展示li拼接
			tHtml += '<div class="citySwitch_nav_wrap_kong1" style="background:#'+pc.templeteBackColor+';"></div>';
			tHtml += '<div class="citySwitch_nav_wrap1 citySwitch_nav_wrap21" style="background:#'+pc.templeteBackColor+';">';
//				tHtml += '<div id="citySwitch_nav" class="citySwitch_nav">';
//					tHtml += '<ul class="noContent clearfix">';
					var updateCss = "";
					
					if(tplist.length == 1 ) {
				    	tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21" style="margin:0px;overflow:scroll;padding:0px;color:#'+pc.templateTitleColor+'">';
				    		tHtml += '<ul class="noContent clearfix self_21">';
				    		updateCss = 'width: 100%;';
				    } else if( tplist.length == 2) {
						tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21" style="margin:0px;overflow:scroll;padding:0px;color:#'+pc.templateTitleColor+'">';
						tHtml += '<ul class="noContent clearfix self_21">';
				    	updateCss = 'width: 50%;';
			       } else if( tplist.length == 3) {
						tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21" style="margin:0px;overflow:scroll;padding:0px;color:#'+pc.templateTitleColor+'">';
							tHtml += '<div  style="width:'+(li_w3*tplist.length)+'px; overflow:hidden;"><ul class="noContent clearfix self_21">';
					    	updateCss = "width:"+li_w3+"px";
				    } else if( tplist.length == 4) {
						tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21" style="margin:0px;overflow:scroll;padding:0px;color:#'+pc.templateTitleColor+'">';
							tHtml += '<div  style="width:'+(li_w4*tplist.length)+'px; overflow:hidden;"><ul class="noContent clearfix self_21">';
					    	updateCss = "width:"+li_w4+"px";
				    } else  if( tplist.length >= 5) {
						tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21" style="overflow:scroll; margin:0px;padding:0px;color:#'+pc.templateTitleColor+'">';
							tHtml += '<div  style="width:'+(li_w5*tplist.length)+'px; overflow:hidden;"><ul class="noContent clearfix self_21">';
					    	updateCss = "width:"+li_w5+"px";
				    } else {
				    	tHtml += '<div id="citySwitch_nav_'+template_type.upGuid()+'" class="citySwitch_nav1 citySwitch_nav_self scrollLeft_21">';
				    		tHtml += '<ul class="noContent clearfix self_21">';
				    }
					
					for (var i = 0; i < tplist.length; i++) {
						/**
						 * 导航展示li拼接
						 */
						var tp = tplist[i];
						var position_uid = template_type.upGuid();
						var tpTitle = tp.title;
						var selfCurr = {color:"#"+pc.templateTitleColorSelected,
								       background:"#"+pc.templateBackcolorSelected};
						
						if(tpTitle.length > 5) {
							tpTitle = template_type.hjy_subStr(tpTitle,0,5) + "...";
							//tpTitle = tpTitle.substring(0,5) + "...";
						}
						if(i == 0) {
							tHtml += '<li class="li_21" style="color:#'+pc.templateTitleColor+';'+updateCss+'" onclick="template_type.position_template_type2(\''+position_uid+'\',this);" show_type="0" position_id="'+position_uid+'" click_before_color="#'+pc.templateTitleColor+'" click_after_color="#'+pc.templateTitleColorSelected+'" click_after_background_color="#'+pc.templateBackcolorSelected+'" click_before_background_color="#'+pc.templeteBackColor+'">'+tpTitle+'</li>';
							switch_html_li_list += '<li class="li_21" style="color:#'+pc.templateTitleColor+';'+updateCss+'" class="on" onclick="template_type.position_template_type2(\''+position_uid+'\',this);" show_type="1" position_id="'+position_uid+'" click_before_color="#'+pc.templateTitleColor+'" click_after_color="#'+pc.templateTitleColorSelected+'" click_after_background_color="#'+pc.templateBackcolorSelected+'" click_before_background_color="#'+pc.templeteBackColor+'">'+tpTitle+'</li>';
						} else {
							tHtml += '<li class="li_21" style="color:#'+pc.templateTitleColor+';'+updateCss+'" onclick="template_type.position_template_type2(\''+position_uid+'\',this);" position_id="'+position_uid+'" show_type="0" click_before_color="#'+pc.templateTitleColor+'" click_after_color="#'+pc.templateTitleColorSelected+'" click_after_background_color="#'+pc.templateBackcolorSelected+'" click_before_background_color="#'+pc.templeteBackColor+'">'+tpTitle+'</li>';
							switch_html_li_list += '<li class="li_21" style="color:#'+pc.templateTitleColor+';'+updateCss+'" onclick="template_type.position_template_type2(\''+position_uid+'\',this);" position_id="'+position_uid+'" show_type="1" click_before_color="#'+pc.templateTitleColor+'" click_after_color="#'+pc.templateTitleColorSelected+'" click_after_background_color="#'+pc.templateBackcolorSelected+'" click_before_background_color="#'+pc.templeteBackColor+'">'+tpTitle+'</li>';
						}
						/**
						 * 关联模板拼接
						 */
						var rel_templete = tp.rel_templete;
						rel_template_html_li_list += '<div class="hjy_postion2" id="'+ template_type.position_template_type_pre_id + position_uid +'">';//点击定位置顶使用
						//专题模板切换调用
						if(rel_templete.length > 0) {
							for(var k=0;k< rel_templete.length ; k++) {
								rel_template_html_li_list += template_type.type_switch(rel_templete[k].templeteType,rel_templete[k]);
							}
						}
						rel_template_html_li_list += '</div>';
						
					}
					tHtml += '</ul></div>';
				tHtml += '</div>';

			tHtml += '</div>';
				
			
			
			/*
			 * 添加本地货模板关联的内容
			 */
			tHtml += rel_template_html_li_list;
			
		}
		
		
		
		return tHtml;
	},
	
	/**
	 * 兑换码兑换模板
	 */
	type_22:function(pc){
		if(pc.pcList &&　pc.pcList.length == 0) return '';
		var picMain = pc.templetePic;
		var exchangeCode = pc.exchangeCode;
		var endTIme = pc.exchangeEndTime;
		var count = pc.exchangeCodeCount;
		var activityCode = pc.activityCode;
		var template_backcolor = pc.templeteBackColor;
		var tHtml = [];
		tHtml.push('<div class="div_ng">');
//		tHtml.push('<div class="toutu">');
//		tHtml.push('<img src="'+picMain+'" alt=""/> ');
//		tHtml.push('</div>');
			 
		var	tem = '<div class="dhm_w" style="background:url('+picMain+') no-repeat;background-size: 100% 100%;">';
		if(exchangeCode != ""){
			tem += '<p>您的兑换码：'+exchangeCode+'</p>';
			tem += '<p>过期时间：'+endTIme+'</p>';
			tem += '<p>（您有'+count+'张兑换码）</p>';  // <span><i>¥</i>1000</span>
		}else{
			tem += '<p>您的账户没有兑换码</p>'
		}
		tem += '</div>';
		tem += '<ul class="ul_dhli" style="margin-top:-1px;background-color:#'+template_backcolor+'">';
		for(var i=0;i<pc.pcList.length;i++){
			tp = pc.pcList[i];
			var pcImg = tp.pcImg;
			var pcName = tp.pcName;
			var pcPrice = tp.pcPrice;
			var productCode = tp.productCode;
			var skuCode = tp.pcNum;
			var showPic1="";
			var showPic2 = "";
			var salesNum = tp.salesNum;
			if(salesNum == '0'){
				showPic1='<img class="no_g" src="../../resources/images/dui_07.png" style="display:block" alt=""/><!--抢光了显示-->';
				showPic2 = '<p class="sp_tj"><img src="../../resources/images/btn2.png" alt=""/></p><!--btn1有货    btn2为没货-->';
			}else{
				showPic1='<img class="no_g" src="../../resources/images/dui_07.png" alt=""/><!--抢光了显示-->';
				showPic2 = '<p class="sp_tj"><img src="../../resources/images/btn1.png" alt="" onclick="template.exchangeProduct(\''+productCode+'\',\''+skuCode+'\',\''+pcPrice+'\',\''+pcName+'\',\''+exchangeCode+'\',\''+activityCode+'\')" /></p><!--btn1有货    btn2为没货-->';
			}
			tem += '<li>';
			tem += showPic1;
			tem += '<p class="sp_img"><img src="'+pcImg+'" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',1)" alt=""/></p>';
			tem += '<p class="sp_m">'+pcName+'</p>';
			tem += '<p class="sp_j">￥ '+pcPrice+'</p>';
			tem += showPic2;
			tem += '</li>';
		}
		tem += '</ul>';
		tHtml.push(tem);
		tHtml.push('</div>');
		return tHtml.join('');
	},
	/**
	 * 拼团模板
	 */
	type_23 : function(pc) {
		//内容列表
		var tHtml = "";
		var tHtmlCss = "";
		//页面样式
	    tHtmlCss += ' <link href="/cfamily/resources/css/template/pingtuan.css" rel="stylesheet" type="text/css" />';
	    
	    //大字体样式
	    if(template_type.fontSize=="big"){
		   tHtmlCss += ' <link href="/cfamily/resources/css/template/pageBigFontSize.css" rel="stylesheet" type="text/css" />';
	    }

		$("head").append(tHtmlCss);
		
		
		var ListTp =pc.pcList;
		
		if(ListTp.length <= 0) {
			return tHtml;
		}
		
		
		tHtml += '<div class="ping_list"  style="background:#'+pc.templeteBackColor+';">';
		for(var i=0;i<ListTp.length;i++){
			var tp = ListTp[i];
			tHtml += '<div class="ping_item">';
			tHtml += '<a href="javascript:;" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',1)">';
			/*tHtml += '<div class="ping_end"><img src="../../resources/images/template/sxk.png" alt=""/></div>';*/
			tHtml += '<div class="ping_img"><img src="' + tp.pcImg + '" onerror="template.imageLoadError(this)" alt=""/>';
			if(tp.rateOfProgress==100){
				tHtml += '<em style="display:block;width: .62rem;height: .62rem;background: url(/cfamily/resources/images/template/ico-qgl.png) no-repeat;background-size: 100%;position: absolute;left: 12%;top: .5rem;"></em>';
			}
			tHtml +="</div>"
			tHtml += '<div class="ping_content">';
			tHtml += '<p class="ping_name">'/*<span>自营</span>*/ + tp.pcName + '</p>';
			tHtml += '<p class="ping_info">' + tp.pcDesc + '</p>';
			tHtml += '<p class="ping_ren"><span>' + tp.tagList[0] + '</span></p>';
			
			tHtml += '<div class="jdt-562">';
			tHtml += '<div class="jdt-d-562">';
			tHtml += '<div class="jd-562" style="width: '+tp.rateOfProgress+'%;"></div>';
			tHtml += '</div>';
			tHtml += '<span class="bai-562">'+tp.rateOfProgress+'%</span>';
			tHtml += '</div>';
			
			tHtml += '<div class="pin562-xg">';
			tHtml += '<div class="pin562-xg-d">';
			tHtml += '<p class="ping_price"><span>￥</span>' + tp.pcPrice + '</p>';
			tHtml += '<p class="ping_price_x"><span>￥' + tp.marketPrice + '</span></p>';
			tHtml += '</div>';
			tHtml += '<span class="ping_start"><img src="../../resources/images/template/pinlist_10.jpg" alt=""></span>';
			tHtml += '</div>';
			
			tHtml += '</div>';
			tHtml += '</a>';
			tHtml += '</div>';
		}
		tHtml += '</div>';
		return tHtml;
	},
	/**
	 * 悬浮模板
	 */
	type_24 : function(pc) {
		var ListTp =pc.pcList;
		var tHtml = "";
		if( ListTp.length <= 0) {
			return tHtml;
		}
		var tp = ListTp[0];
		tHtml += '<a href="javascript:;" class="xfmb_550"><img src="'+tp.categoryImg+'" alt="" onclick="template.forward_url(\''+tp.pcStatus+'\',\''+tp.openType+'\',\''+ tp.openTypeVal+'\',1)"></a>';
		return tHtml;
	},	
	/**
	 * 精编模板
	 */
	type_25 : function(pc) {
		//内容列表
		var ListTp =pc.pcList;
		var tHtml = "";
		if(ListTp.length <= 0) {
			return tHtml;
		}
		tHtml += '<div class="divjb_550"  style="background:#'+pc.templeteBackColor+';padding-bottom:20px">';
		tHtml += '<div class="jbsm_550">';
		for(var i=0;i<ListTp.length;i++){
			var tp = ListTp[i];
			tHtml += '<div class="sp_550">';
			tHtml += '<a href="javascript:;" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',1)">';
			tHtml += '<p class="ping_name"><img src="' + tp.pcImg + '" onerror="template.imageLoadError(this)" alt=""/></p>';
			tHtml += '<p class="spm_550">' + tp.pcName + '</p>';
			tHtml += '<p class="spj_550">￥<strong>' + tp.pcPrice + '</strong>';
			if((tp.pcPrice*1)<(tp.marketPrice*1)){
				tHtml += '<span>￥' + tp.marketPrice + '</span>';
			}
			tHtml += '</p>';
			tHtml += '</a>';
			tHtml += '</div>';
		}
		tHtml += '</div>'
		tHtml += '</div>';
		return tHtml;
	},
	/**
	 * 拼团大图模板
	 */
	type_26 : function(pc) {
		//内容列表
		var tHtml = "";
		var tHtmlCss = "";
		//页面样式
		tHtmlCss += ' <link href="/cfamily/resources/css/template/pingtuan.css" rel="stylesheet" type="text/css" />';
	    tHtmlCss += ' <link href="/cfamily/resources/css/template/bigpicpingtuan.css" rel="stylesheet" type="text/css" />';
	    $("head").append(tHtmlCss);
		var ListTp =pc.pcList;
		
		if(ListTp.length <= 0) {
			return tHtml;
		}
		
		
		tHtml += '<article class="yilan-564" style="background:#'+pc.templeteBackColor+';"><div class="ping-564-div">';
		for(var i=0;i<ListTp.length;i++){
			var tp = ListTp[i];
			var pcDesc = tp.pcDesc;
			if("" != pcDesc){
				pcDesc = "["+pcDesc+"]";
			}
			tHtml +='<div class="ping-564">';
			tHtml += '<a href="javascript:;" onclick="template.forward_url(\''+tp.pcStatus+'\',\'449747550002\',\''+tp.productCode+'\',1)">';
			tHtml +='<div class="spmb-img-561"><div class="tuan_bq"><span>'+tp.manyCollage+'</span>人团</div>';
			tHtml +='<img src="'+tp.pcImg+'"  alt="">';
			if(tp.collageType == '4497473400050002'){
				tHtml += '<img style=" width: 60px; position: absolute; top: 8%; right: 0;" src="../../resources/cfamily/images/yqt.png" alt="">';
			}
           tHtml +='</div> <div class="spmb-xx-561">';
            	   //<!--商品名称-->
           tHtml +='<p class="spmb-n-561"><span>'+pcDesc+'  </span>'+tp.pcName+'</p>';
           tHtml +='<div class="spmb-zk-561" style="display:">';
           tHtml +='<img src="../../resources/cfamily/images/tv561_07.png" alt=""> ';
           tHtml +='<div class="zhe-sp-561-2"> <span>拼团省</span> <br><b>'+tp.saveValue+'</b> </div>';
            	        
           tHtml +='</div><div class="spmb-price-561"><p style="display:"><i>单买价</i> <span class="fuhao-q">￥</span>'+tp.marketPrice+'</p><p><i>拼团价 </i> <span class="sp-sjj-561"><span class="fuhao-q">￥</span> '+tp.pcPrice+'</span></p></div>';
            	
           tHtml +='</div> <span class="ypjian">已拼'+tp.sellNum+'件</span></a></div>';
		}
		tHtml += '</div></article>';
		return tHtml;
	},
	//倒计时js
	tow:function (n) {
		  return n >= 0 && n < 10 ? '0' + n : '' + n;
		 },
		
		 getDate:function (i,time,htmStr) {
			 // var oDate = new Date(template.serverTime.replace(/\-/g, "/")).getTime();//获取现在日期对象
			  var targetDate = new Date(time.replace(/\-/g, "/")).getTime();//获取指定日期对象
			  var second = Math.floor((targetDate - template.systm) / 1000);//未来时间距离现在的秒数
			  var day = Math.floor(second / 86400);//整数部分代表的是天；一天有24*60*60=86400秒 ；
			  second = second % 86400;//余数代表剩下的秒数；
			  var hour = Math.floor(second / 3600);//整数部分代表小时；
			  second %= 3600; //余数代表 剩下的秒数；
			  var minute = Math.floor(second / 60);
			  second %= 60;
			  
			  
			   var tian1 = template_type.tow(day).substring(0,1);
			   var tian2 = template_type.tow(day).substring(1);

			   var shi1 = template_type.tow(hour).substring(0,1);
			   var shi2 = template_type.tow(hour).substring(1);
			  
			   var fen1 = template_type.tow(minute).substring(0,1);
			   var fen2 = template_type.tow(minute).substring(1);

			   
			   var miao1 = template_type.tow(second).substring(0,1);
			   var miao2 = template_type.tow(second).substring(1);
	          
			   var htmStrObj = $("<div>"+htmStr+"</div>");
			   htmStrObj.find(".tian1_"+i).html(tian1);
			   htmStrObj.find(".tian2_"+i).html(tian2);
			   htmStrObj.find(".shi1_"+i).html(shi1);
			   htmStrObj.find(".shi2_"+i).html(shi2);
			   htmStrObj.find(".fen1_"+i).html(fen1);
			   htmStrObj.find(".fen2_"+i).html(fen2);
			   htmStrObj.find(".miao1_"+i).html(miao1);
			   htmStrObj.find(".miao2_"+i).html(miao2);
			
			   
			    if(template_type.tow(day) <= 0 && template_type.tow(hour) <= 0 && template_type.tow(minute) <= 0 && template_type.tow(second) <= 0){
					     
				       htmStrObj.find("article").eq(0).hide();
					   htmStrObj.find(".tian1_"+i).html(0);
					   htmStrObj.find(".tian2_"+i).html(0);
					   htmStrObj.find(".shi1_"+i).html(0);
					   htmStrObj.find(".shi2_"+i).html(0);
					   htmStrObj.find(".fen1_"+i).html(0);
					   htmStrObj.find(".fen2_"+i).html(0);
					   htmStrObj.find(".miao1_"+i).html(0);
					   htmStrObj.find(".miao2_"+i).html(0);
				}

			   return htmStrObj.html();
				
			 },
	
	
	//分类点击定位使用
	position_template_type : function (uid,obj) {
		//所有li恢复成之前点击的样子
		$("li[position_id="+uid+"]").parent().find("li").each(function() {
			$(this).css("color",$(this).attr("click_before_color"));
			var show_type = $(this).attr("show_type");
			if(show_type == 0) {
				$(this).removeClass("curr");
			} else if(show_type == 1) {
				$(this).removeClass("on");
			}
		});
		var show_type = $(obj).attr("show_type");
		if(show_type == 0) {
			$(obj).addClass("curr");
			$(obj).parents(".citySwitch_nav").eq(0).nextAll(".switchWrap").eq(0).find("li").eq($(obj).index()).addClass("on");
			
		} else if(show_type == 1) {
			$(obj).addClass("on");
			$(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").eq(0).find("li").eq($(obj).index()).addClass("curr");
			$(obj).parents(".switchWrap").eq(0).hide();
			$(obj).parents(".switchWrap").eq(0).next().hide();
			
			template_type.slideFun18(obj);

		}
		$("li[position_id="+$(obj).attr("position_id")+"]").css("color",$(obj).attr("click_after_color"));
		
		//$(document).scrollTop($("#"+template_type.position_template_type_pre_id  +  uid).offset().top - template_type.template_18_position_temp_height);
		// 多向下滚动1个像素，解决滑动时判断标签位置有半个像素误差的问题
		$(document).scrollTop($("#"+template_type.position_template_type_pre_id  +  uid).offset().top - template_type.template_18_position_temp_height + 1);
		
		$("body").css("overflow","auto");
		$(document).unbind('touchmove');
	},
	//分类点击定位使用(分开调用，防止同一专题配有多个定位模板时会相互影响)
	position_template_type2 : function (uid,obj) {
		//所有li恢复成之前点击的样子
		$("li[position_id="+uid+"]").parent().find("li").each(function() {
			$(this).css("color",$(this).attr("click_before_color"));
			$(this).css("background",$(this).attr("click_before_background_color"));
			var show_type = $(this).attr("show_type");
			if(show_type == 0) {
				$(this).removeClass("curr");
			} else if(show_type == 1) {
				$(this).removeClass("on");
			}
		});
		var show_type = $(obj).attr("show_type");
		if(show_type == 0) {
			$(obj).addClass("curr");
			$(obj).parents(".citySwitch_nav1").eq(0).nextAll(".switchWrap1").eq(0).find("li").eq($(obj).index()).addClass("on");
			
		} else if(show_type == 1) {
			$(obj).addClass("on");
			$(obj).parents(".citySwitch_nav_wrap1").find(".citySwitch_nav1").eq(0).find("li").eq($(obj).index()).addClass("curr");
			$(obj).parents(".switchWrap1").eq(0).hide();
			$(obj).parents(".switchWrap1").eq(0).next().hide();
			
			

		}
		$("li[position_id="+$(obj).attr("position_id")+"]").css("color",$(obj).attr("click_after_color"));
		
		$("li[position_id="+$(obj).attr("position_id")+"]").css("background",$(obj).attr("click_after_background_color"));
		
		$(document).scrollTop($("#"+template_type.position_template_type_pre_id  +  uid).offset().top - template_type.template_21_position_temp_height);
		
		$("body").css("overflow","auto");
		$(document).unbind('touchmove');
	},
	//guid生成
	upGuid : function() {
		var guid = "";
	    for (var i = 1; i <= 32; i++){
	      var n = Math.floor(Math.random()*16.0).toString(16);
	      guid +=   n;
	      if((i==8)||(i==12)||(i==16)||(i==20))
	        guid += "_";
	    }
	    return guid;
	},
	//位置选择开关<本地货模版449747500013>
	position_select_switch_13 :function (flg,obj) {
		if(flg == 0) {//close
			$(obj).parents(".switchWrap").hide();
			$("body").css("overflow","auto");
			$(document).unbind('touchmove');
		} else if (flg == 1) {//open
			$(obj).next(".switchWrap").eq(0).show();
			$("body").css("overflow","hidden");
			$(document).on('touchmove',function(e) {
	            var e = e || event;
	            e.preventDefault();
	        })
		}
		
	},
	//位置选择开关<页面定位模板449747500018>
	position_select_switch_18 :function (flg,obj) {
		if(flg == 0) {//close
			$(obj).parents(".switchWrap").eq(0).hide();
			$(obj).parents(".switchWrap").eq(0).next().hide();
			$("body").css("overflow","auto");
			$(document).unbind('touchmove');
		} else if (flg == 1) {//open
			$(obj).nextAll(".switchMask").eq(0).show();
			$(obj).nextAll(".switchWrap").eq(0).show();
			$("body").css("overflow","hidden");
			$(document).on('touchmove',function(e) {
	            var e = e || event;
	            e.preventDefault();
	        })
		}
		
	},
	hjy_subStr : function(str,start,stop) {
		var returnStr= "";
		if(undefined != str && "" != str ) {
			if(undefined == stop ) {
				stop = str.length;
			}
			if(start <= stop) {
				returnStr = str.substring(start,stop);
			}
		}
		return returnStr
		
	},
	//导航滑动  本地货模板使用
	slideFun13 : function(obj) {
		//将横滑标签居中
		var movepx = "";//记录总共要移动的数量
		var scrollWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").eq($(obj).index()).offset().left;
		var objWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").eq($(obj).index()).outerWidth();
		var bodyWidth = $("body").width();
		var ulWidth = $(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find("ul").eq(0).width();
		var outWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).css("margin-right");//父级div margin  值
		var idx = outWidth.indexOf("px");
		if(idx > 0) {
			outWidth = eval(outWidth.substring(0,idx));
		}
		var oldTransfor = $(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find("ul:eq(0)").css("transform");
		var oldArr =  oldTransfor.split(",");
		var oldV = eval(oldArr[4]);
		
		
		//判断移动方向
		if(scrollWidth > 0 && (scrollWidth+objWidth + outWidth) > bodyWidth ) {//在屏幕右侧,向左移动
			movepx = (scrollWidth  + objWidth + outWidth - bodyWidth - oldV) * -1;
		} else if(scrollWidth < 0){//在屏幕左侧,向右移动
			movepx = (scrollWidth - 10 - oldV) * -1;//+20 是因为父级div有一个padding-left，margin-left
		}
		
		if(movepx != "") {
			$(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find("ul").eq(0).css("transition","all 0.5s ease");
			$(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find("ul").eq(0).css("transform","translate("+movepx+"px, 0px) translateZ(0px)");
		}
	},
	//导航滑动  页面定位模板使用
	slideFun18 : function(obj) {
		//将横滑标签居中
		var movepx = "";//记录总共要移动的数量
		
		var scrollWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").eq($(obj).index()).offset().left;
		var objWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("li").eq($(obj).index()).outerWidth();
		var bodyWidth = $("body").width();
		var ulWidth = $(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find("ul").eq(0).width();
		
		
		var outWidth = $(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).css("margin-right");//父级div margin  值
		var idx = outWidth.indexOf("px");
		if(idx > 0) {
			outWidth = eval(outWidth.substring(0,idx));
		}
		var oldTransfor = $(obj).parents(".citySwitch_nav_wrap").find(".citySwitch_nav").find(".noContent").css("transform");
		var oldArr =  oldTransfor.split(",");
		var oldV = eval(oldArr[4]);
		
		
		//判断移动方向
		if(scrollWidth > 0 && (scrollWidth+objWidth + outWidth) > bodyWidth ) {//在屏幕右侧,向左移动
			movepx = (scrollWidth  + objWidth + outWidth - bodyWidth - oldV) * -1;
		} else if(scrollWidth < 0){//在屏幕左侧,向右移动
			movepx = (scrollWidth - oldV) * -1;
		}
		
		if(movepx != "") {
//			$(obj).parents("div[class=citySwitch_nav_wrap]").eq(0).find("div[class=citySwitch_nav]").eq(0).find("ul").css("transition-duration","1000ms");
			$(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("ul").css("transition","all 0.5s ease");
			$(obj).parents(".citySwitch_nav_wrap").eq(0).find(".citySwitch_nav").eq(0).find("ul").css("transform","translate("+movepx+"px, 0px) translateZ(0px)");
		}
	},
	//导航滑动  页面定位模板使用
	slideFun21 : function(obj) {
		//将横滑标签居中
		var movepx = "";//记录总共要移动的数量
		
		var scrollWidth = $(obj).parents(".citySwitch_nav_wrap1").eq(0).find(".citySwitch_nav1").eq(0).find("li").eq($(obj).index()).offset().left;
		var objWidth = $(obj).parents(".citySwitch_nav_wrap1").eq(0).find(".citySwitch_nav1").eq(0).find("li").eq($(obj).index()).outerWidth();
		var bodyWidth = $("body").width();
		var ulWidth = $(obj).parents(".citySwitch_nav_wrap1").find(".citySwitch_nav1").find("ul").eq(0).width();
		
		
		var outWidth = $(obj).parents(".citySwitch_nav_wrap1").eq(0).find(".citySwitch_nav1").eq(0).css("margin-right");//父级div margin  值
		var idx = outWidth.indexOf("px");
		if(idx > 0) {
			outWidth = eval(outWidth.substring(0,idx));
		}
		var oldTransfor = $(obj).parents(".citySwitch_nav_wrap1").find(".citySwitch_nav1").find(".noContent").css("transform");
		var oldArr =  oldTransfor.split(",");
		var oldV = eval(oldArr[4]);
		
		//判断移动方向
		if(scrollWidth > 0 && (scrollWidth+objWidth + outWidth) > bodyWidth ) {//在屏幕右侧,向左移动
			
			movepx = (scrollWidth  + objWidth + outWidth - bodyWidth - oldV) * -1;
		} else if(scrollWidth < 0){//在屏幕左侧,向右移动
			movepx = (scrollWidth - oldV) * -1;
		}
		
		if(movepx != "") {
//			$(obj).parents("div[class=citySwitch_nav_wrap]").eq(0).find("div[class=citySwitch_nav]").eq(0).find("ul").css("transition-duration","1000ms");
			$(obj).parents(".citySwitch_nav_wrap1").eq(0).find(".citySwitch_nav1").eq(0).find("ul").css("transition","all 0.5s ease");
			$(obj).parents(".citySwitch_nav_wrap1").eq(0).find(".citySwitch_nav1").eq(0).find("ul").css("transform","translate("+movepx+"px, 0px) translateZ(0px)");
		}
	},
	//页面滚动事件
	windowScroll : function () {
		
		$(".hjy_postion").each(function(){
			var window_top = $(window).scrollTop();
			var this_top = $(this).offset().top;
			var this_height = $(this).height();
			
			//导航定位使用变量  start 
			var citySwitch_nav_wrap =  $(this).prevAll(".citySwitch_nav_wrap:eq(0)");
			var kong = citySwitch_nav_wrap.prev();
			//导航定位使用变量  end 
			
			if((this_top - template_type.template_18_position_temp_height <= window_top) && (this_top + this_height - template_type.template_18_position_temp_height) > window_top) {
				var positionId = $(this).attr("id");
				var liPositionID = template_type.hjy_subStr(positionId,template_type.position_template_type_pre_id.length );
				//所有恢复为点击前的颜色
				$("li[position_id="+liPositionID+"]").parents(".citySwitch_nav_wrap").find("li").each(function(){
					$(this).css("color",$(this).attr("click_before_color"));
				});
				//更改当前点击的为点击后的色值
				$("li[position_id="+liPositionID+"]").each(function(){
					var show_type = $(this).attr("show_type");
					if(show_type == 1) {
						$(this).parent().find("li").removeClass("on");
						$(this).addClass("on");
						template_type.slideFun18(this);
					} else if(show_type == 0){
						$(this).parent().find("li").removeClass("curr");
						$(this).addClass("curr");
					}
					$(this).css("color",$(this).attr("click_after_color"));
				});
				
				return false;
			} 
			
			
		});
		
		$(".hjy_postion2").each(function(index){
			
			var window_top = $(window).scrollTop()+20;
			var this_top = $(this).offset().top;
			var this_height = $(this).height();
			
			//导航定位使用变量  start 
			//console.log($(this).attr("id"));
			var citySwitch_nav_wrap =  $(this).prevAll(".citySwitch_nav_wrap1:eq(0)");
			var kong = citySwitch_nav_wrap.prev();
			//导航定位使用变量  end 
			
			if((this_top - template_type.template_21_position_temp_height <= window_top) && (this_top + this_height - template_type.template_21_position_temp_height) > window_top) {
				var positionId = $(this).attr("id");
				var liPositionID = template_type.hjy_subStr(positionId,template_type.position_template_type_pre_id.length );
				//所有恢复为点击前的颜色
				$("li[position_id="+liPositionID+"]").parents(".citySwitch_nav_wrap1").find("li").each(function(){
					$(this).css("color",$(this).attr("click_before_color"));
					$(this).css("background",$(this).attr("click_before_background_color"));
				});
				//更改当前点击的为点击后的色值
				$("li[position_id="+liPositionID+"]").each(function(){
					var show_type = $(this).attr("show_type");
                      if(show_type == 0){
						$(this).parent().find("li").removeClass("curr");
						$(this).addClass("curr");
						
					}
					$(this).css("color",$(this).attr("click_after_color"));
					$(this).css("background",$(this).attr("click_after_background_color"));
				});
				
				$(document).on('touchmove',function(e) {
		            var e = e || event;
		            
		            if(e.target.className=="li_21"){
		            	console.log(e.target.className);
		            }
		            else{
						var li_a = $(".self_21 li");
						var li_w = $(".self_21").eq(0).width()/(li_a.length) * 5;
						var li_w1 = li_w*2;
						var li_w2 = li_w*3;
						for (var i = 0; i < li_a.length; i++) {
						if ($(li_a[i]).attr("class") == "li_21 curr") {
							if(i <= 4){
						    	  $(".scrollLeft_21").scrollLeft(0)  
						       }else if(i > 4 <= 9){
						    	  $(".scrollLeft_21").scrollLeft(li_w)  
						       }else if(i > 9 <= 14){
						    	  $(".scrollLeft_21").scrollLeft(li_w1)  
						       }else if(i > 14 <= 19){
						    	  $(".scrollLeft_21").scrollLeft(li_w2)  
						       }
						   }
						 }
		            }

		        })
				
				return false;
			} 
			
			
		});   
		
		//导航定位
		$(".citySwitch_nav_wrap18").each(function(){
			var window_top = $(window).scrollTop();
			var fisrtPosition = $(this).next(".hjy_postion");//第一个
			var lastPositionID = $(this).find(".citySwitch_nav li:last").attr("position_id");//最后一个
			var lastPosition = $("#"+template_type.position_template_type_pre_id+lastPositionID);
			if(window_top >= (fisrtPosition.offset().top -template_type.template_18_position_temp_height)  && window_top < (lastPosition.offset().top + lastPosition.height() - template_type.template_18_position_temp_height)) {
				if($(this).css("position") != "fixed") {
					$(this).css("position","fixed");
					$(this).prev().css("display","block");
				}
				
			} else {
				if($(this).css("position") == "fixed") {
					$(this).css("position","");
					$(this).prev().css("display","none");
				}
				
			}
			
		});
		
		$(".citySwitch_nav_wrap21").each(function(){
			var window_top = $(window).scrollTop();
			var fisrtPosition = $(this).next(".hjy_postion2");//第一个
			var lastPositionID = $(this).find(".citySwitch_nav1 li:last").attr("position_id");//最后一个
			var lastPosition = $("#"+template_type.position_template_type_pre_id+lastPositionID);
			if(window_top >= (fisrtPosition.offset().top -template_type.template_21_position_temp_height)  && window_top < (lastPosition.offset().top + lastPosition.height() - template_type.template_18_position_temp_height)) {

				if($(this).css("position") != "fixed") {
					$(this).css("position","fixed");
					$(this).prev().css("display","block");
				}
				
			} else {

				if($(this).css("position") == "fixed") {
					$(this).css("position","");
					$(this).prev().css("display","none");
				}
				
			}
			
		});
		
	},
	touchmove : function() {
		template_type.windowScroll();
	},
	round : function(val,len) { // 四舍五入保留指定位数的小数
		var zero = '';
		for(var i = 0;i < len; i++){
			zero += '0';
		}
		var scale = parseInt('1'+zero);
		
		return Math.round(parseFloat(val)*scale)/scale;
	}
	
}

