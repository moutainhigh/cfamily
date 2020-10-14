var productdetail = {
		serverTime : "",
		skuMaxBuyArr : {},
		skushowLimitNum :{},
		last_correct_buy_num : 1,
		total_stock_num : 0,
		product_detail_info : "",
		init : function(){
			productdetail.laoding_gif("show");
			var productCode = zapbase.up_urlparam('pc');
			var pmobile = zapbase.up_urlparam('pm');
			if(pmobile && pmobile != "" && pmobile.length == 11 && "undefined" != pmobile) {
				zapbase.cookie(cfamily.config.cookie_parent_mobile, pmobile);
	    		$("#parent_mobile_id").html(cfamily.replaceFunc(pmobile, cfamily.config.mobileHanler));
	    	}
			var backUrl = zapbase.up_urlparam('backUrl');
			if(backUrl && backUrl != "" && "undefined" != backUrl) {
	    		$("#back_item_id").attr("style","");
	    		$("#top_id").hide();
	    		$("#back_item_id").parent().attr("href",cfamily.url_param_mix(backUrl));
	    	}
			cfamily.call_api("getProductDetail", {productCode : productCode}, function(data){
				if(data.resultCode == 1) {
					productdetail.product_detail_info = data;
					productdetail.serverTime = Date.parse(data.sysDateTime.replace(/-/g, "/"));
					setInterval(function(){
						productdetail.serverTime = eval(productdetail.serverTime + 1000);
					}, 1000);
					//1,拼装商品轮播图、商品基本信息、月销及返利
					productdetail.assembleProductBaseInfo(data);
					
					//2,促销
					productdetail.assembleSalePromotionInfo(data);
					//3,图文详情及规格参数
					productdetail.assembleProductDetail(data);
					//4,颜色+款式
					productdetail.assembleProductColorAndStyle(data);
					
					productdetail.laoding_gif("hide");
				}
			});
		},
		laoding_gif : function(activityId){
			if(activityId){
				if(activityId == "show"){
					var fth = $('.botline').offset().top;
					$('#mask').css({'display':'block','height':fth+'px'});
					$('#waiting_id').show();
				}else if(activityId == "hide"){
					$('#mask').css('display','none');
					$('#waiting_id').hide();
				}
			}
		},
		assembleProductBaseInfo : function(obj){
		  	//var skuList = detail.skuList;
			var htmlText = "";
	  	  	var pcPicList = obj.pcPicList;//轮播
	  	  	var skuList = obj.skuList;
	  	  	
  	  		htmlText += '<div class="scroll-wrapper" id="idContainer2" ontouchstart="touchStart(event)" ontouchmove="touchMove(event);" ontouchend="touchEnd(event);"><ul class="scroller" id="idSlider2">';
	  		for(var i=0; i< pcPicList.length;i++) {
	  			htmlText += '<li style="width:-100%"><a href="javascript:void(0);"><img src="'+pcPicList[i].picNewUrl+'" /></a></li>';
	  		}
	  		htmlText += '</ul>'+
	  				  '<div class="banner-numw"><ul class="banner-num" id="idNum"></ul></div>'+
		  		     '</div>';
  		
	  		//判断是否有库存
  	  		var cntStockNum = 0;
  	  		for(var i=0;i<skuList.length;i++) {
  	  			cntStockNum += skuList[i].stockNumSum;
  	  			/*fq 限制最大限购数*/;
  	  			productdetail.skuMaxBuyArr[skuList[i].skuCode] = skuList[i].skuMaxBuy;
  	  			productdetail.skushowLimitNum[skuList[i].skuCode] = skuList[i].showLimitNum;
  	  		}
  	  		if(cntStockNum <= 0) {
  	  			//20151020 22:28  周国辉屏蔽此句话
	  	  		htmlText += '<div class="tip"><p>没货啦，下次早点来哦～</p><span>&nbsp;</span></div>';
	  	  		
	  	  		$("#buy_select_info_id").attr("unable",true).attr("style","background: #AAAAAA;");
  	  		}
  	  		if(cntStockNum > 0){
  	  			productdetail.total_stock_num = cntStockNum;
	  		}
		  	$("#picList_id").html(htmlText);
		  	var scpt = document.createElement("script");  
		    scpt.setAttribute('src', '../../resources/shareshopping/js/focus.js');  
		    document.body.appendChild(scpt);
		    
		  	//商品基本信息
		    htmlText = '';
		    htmlText += '<h2>'+obj.productName+'</h2>'+
            			'<p class="bianh">商品编号：<span id="product_code_id">'+obj.productCode+'</span></p>'+
            			'<div class="sbox">';
		    if(obj.flagCheap == 1) {
		  		var endTime = obj.endTime;
		  		var rtnEndTime = productdetail.getCountDown(endTime);
		  		var splitTime = rtnEndTime.split(':');
		  		htmlText += '<div class="time" id="count_down" ichsy_end_time="'+endTime+'">剩&nbsp;<span>'+splitTime[0]+'</span>:<span>'+splitTime[1]+'</span>:<span>'+splitTime[2]+'</span></div>';
		  	} 
		    htmlText +='<p class="price"><em class="nprice"><em>￥</em>'+obj.sellPrice+'</em><span><em>￥</em>'+obj.marketPrice+'</span></p></div>';
		    $("#product_base_info_id").html(htmlText);
		    
		    //设置电话订购的商品编号
		    $(".bh").html(obj.productCode);
		    
		    if(obj.flagCheap == 1) {
		    	 setInterval(function(){
			  	    	var t_endTime = $("#count_down").attr("ichsy_end_time");
			  	    	var e_time = productdetail.getCountDown(t_endTime);
			  	    	var e_timeArr = e_time.split(":");
			  			var contSpan = $("#count_down").find("span");
			  			contSpan[0].innerHTML = e_timeArr[0];
			  			contSpan[1].innerHTML = e_timeArr[1]; 
			  			contSpan[2].innerHTML = e_timeArr[2];
			  			
			  		}, 1000);
		    }
		    //月销
		    $("#num_total_id").html('<div>月销<span><b>'+obj.saleNum+'</b> 件</span></div><div>返利<span class="price">￥<b>'+obj.disMoney+'</b></span></div>');
		},
		assembleSalePromotionInfo : function(obj){
			var htmlText = "";
			if(obj.flagIncludeGift == 1) {
				htmlText += '<div class="lid"><em>赠品</em>'+
							'<div>'+obj.gift+'</div></div>';
			}
			var skuList = obj.skuList;
			if(skuList.length>0) {
				var actInfo = skuList[0].activityInfo;
				for(var i=0;i<actInfo.length;i++) {
					htmlText += '<div class="lid">'+
								'<em>'+actInfo[i].activityName+'</em>'+
								'<div>'+actInfo[i].remark+'</div></div>';
				}
			}
			$("#sales_area_id").html(htmlText);
		},
		assembleProductDetail : function(obj){
			var htmlText = "";
			var authorityLogo = obj.authorityLogo;
			for(var i=0;i<authorityLogo.length;i++) {
				htmlText += '<span ><b style="background:url('+authorityLogo[i].logoPic+') no-repeat left top; background-size:100% auto;">&nbsp;</b>'+authorityLogo[i].logoContent+'</span>';
			}
			$("#bz_area_id").html(htmlText);
			
			//图文详情
			htmlText = "";
			htmlText += '<div class="imgs">';
			var discriptList = obj.discriptPicList;
			for(var i=0;i<discriptList.length;i++) {
				htmlText += '<img src="'+discriptList[i].picNewUrl+'" />';
			}
			htmlText += '</div>';
			$("#tabc0").html(htmlText);
			//规格参数
			htmlText = "";
			htmlText += '<div class="param">';
			var propertyList = obj.propertyInfoList;
			for(var i=0;i<propertyList.length;i++) {
				htmlText += '<p><span class="st">【'+propertyList[i].propertykey+'】</span><span>'+propertyList[i].propertyValue+'</span></p>';
			}
			htmlText += '</div>';
			$("#tabc1").html(htmlText);
		},
		assembleProductColorAndStyle : function(obj){
			var skuCode = obj.productCode;
			
			var htmlText = '<img src="'+obj.mainpicUrl.picNewUrl+'" alt="" class="fl">'+
							'<div class="imgr" sku_code="'+skuCode+'" id="product_name_id"><p class="pt">'+obj.productName+'</p>'+
							'<p class="pprice" id="price_area_id" priceVal="'+obj.sellPrice+'"><b>￥</b>'+obj.sellPrice+'</p>'+
							'<p class="size" id="size_view_id"></p>'+
							'</div>';
			$("#buy_product_info_id").html(htmlText);
			
			//如果sku只有一个，不显示颜色和款式
			if(obj.skuList.length != 1){
				//颜色和款式
				htmlText = '';
				var sizeViewHtml1 = '';
				var prptyList =  obj.propertyList;
				if(prptyList.length > 0){
					sizeViewHtml1 += '请选择：';
					for(var i = 0;i<prptyList.length;i++) {
						var prptyvaluelist = prptyList[i].propertyValueList;
						
						if(prptyvaluelist.length > 1){
							sizeViewHtml1 +='<span propertyKey="'+prptyList[i].propertyKeyCode+'">'+prptyList[i].propertyKeyName+'</span>';
							htmlText += '<div class="sel"><div class="tdiv">'+prptyList[i].propertyKeyName+'</div>'+
										'<div class="sdiv" key_code="'+prptyList[i].propertyKeyCode+'">';
							for(var j=0;j<prptyvaluelist.length;j++){
								htmlText += '<a propertyValue="'+prptyvaluelist[j].propertyValueName+'" property_str="'+prptyList[i].propertyKeyCode+'='+prptyvaluelist[j].propertyValueCode+'"><b>&nbsp;</b>'+prptyvaluelist[j].propertyValueName+'</a>';
							}
							htmlText +=	'</div></div>';
						}
					}
				}
				if(sizeViewHtml1 != '请选择：'){
					$("#size_view_id").html(sizeViewHtml1);
				}
				$("#kinds_product_properties_id").prepend(htmlText);
				
				//如果只有一种规格参数，直接判断是否有库存，并不可点击
				if($("#size_view_id").find("span").length == 1){
					var propertyArray = new Array();
					$("#kinds_product_properties_id").find("a").each(function(i){
						propertyArray[0] = $(this).attr("property_str");
						if(productdetail.checkIsStock(propertyArray) == 0){
							commonJs.setTagFunc({obj:this,isUsable:true});
							$(this).attr("style","border: 1px solid #F5EFF1;color: #B1AAAC;");
						}
					});
				}
			}
			//设置a标签监听
			$("#kinds_product_properties_id").find("a").click(function(){
				if(!($(this).hasClass("btn-minus") || $(this).hasClass("btn-add"))){
					if(commonJs.isDis(this))
						return;
					var currentObj = $(this);
					var propertyStrArry = currentObj.attr("property_str").split("=");
					
					if($(this).hasClass("on")){
						$(this).removeClass("on");
						$("#size_view_id").find("span").each(function(i){
							if($(this).attr("propertyKey") == propertyStrArry[0]){
								$(this).removeAttr("class");
								$(this).html(currentObj.parent().parent().children().eq(0).html());
							}
						});
					}else{
						$("#size_view_id").find("span").each(function(i){
							if($(this).attr("propertyKey") == propertyStrArry[0]){
								$(this).html('"'+currentObj.attr("propertyValue")+'"');
								$(this).attr("class","fred");
							}
						});
						$(this).siblings().removeClass("on");
						$(this).addClass("on");
					}
					
					//替换价格
					var propertyArray = new Array();
					if($("#kinds_product_properties_id").find("a").filter(".on").length > 0){
						$("#kinds_product_properties_id").find("a").filter(".on").each(function(i){
							propertyArray[i] = $(this).attr("property_str");
						});
						for(var i=0;i<productdetail.product_detail_info.skuList.length;i++){
							var skuObj = productdetail.product_detail_info.skuList[i];
							if(skuObj.keyValue && skuObj.keyValue.length > 0){
								var count = 0;
								for(var j=0;j<propertyArray.length;j++){
									if(skuObj.keyValue.indexOf(propertyArray[j]) != -1){
										count ++;
									}
								}
								if(count == propertyArray.length){
									$("#product_name_id").attr("sku_code",skuObj.skuCode);
									$("#price_area_id").html("<b>￥</b>"+skuObj.sellPrice);
								}
								
							}
						}
					}
					//点击是判断是否有库存
					if($("#size_view_id").find("span").length > 1 && $(this).parent().attr("key_code") == $("#size_view_id").find("span").eq(0).attr("propertykey")){
						var currentObj = $(this);
						var propertyArray = new Array();
						propertyArray[0] = currentObj.attr("property_str");
						$("#kinds_product_properties_id").find("div").filter(".sdiv").eq(1).children().each(function(i){
							if(currentObj.hasClass("on")){
								propertyArray[1] = $(this).attr("property_str");
								if(productdetail.checkIsStock(propertyArray) == 0){
									commonJs.setTagFunc({obj:this,isUsable:true});
									$(this).attr("style","border: 1px solid #F5EFF1;color: #B1AAAC;");
								}else{
									commonJs.setTagFunc({obj:this,isUsable:false});
									$(this).attr("style","");
								}
							}else{
								commonJs.setTagFunc({obj:this,isUsable:false});
								$(this).attr("style","");
							}
						});
					}
				}
				if($(this).hasClass("btn-minus")){
					productdetail.min_add("-");
				}
				if($(this).hasClass("btn-add")){
					productdetail.min_add("+");
				}
				productdetail.showMaxBuyNum();
			});
		},
		buySelectInfo : function(){
			
			if(commonJs.isDis("#buy_select_info_id"))
				return;
			
			/*选择弹出层*/
			var smask =function(){
				var fth = $('.botline').offset().top-40;
				$('.tabw').animate({'height':'37rem'},300);
				$('#mask').css({'display':'block','height':fth+'px'});
				$('.tabw footer').css('display','block');
			};
			smask();
			$('.tabw .btn-close').click(function(){
				$('.tabw').css('height','0');
				$('#mask').css('display','none');
				$('.tabw footer').css('display','none');
			});
		},
		tostIsCall : function() {
			$("#mask").show();
			$(".ftel").show();
		},
		exitCall : function() {
			setTimeout(function(){
				$("#mask").hide();
				$(".ftel").hide();
			},100);
			
		},
		submitProductInfo : function(){
			
			//判断是否选择规格参数
			if($("#size_view_id").find("span").length != $("#size_view_id").find("span").filter(".fred").length){
				$("#size_view_id").find("span").each(function(i){
					if($(this).attr("class") != "fred"){
						capi.show_message("请选择"+$(this).html());
						return false;
					}
				});
				return;
			}
			
			var selectProductInfo = cfamily.temp.sku_list;
			selectProductInfo.sku_num = productdetail.last_correct_buy_num;
			selectProductInfo.product_code = productdetail.product_detail_info.productCode;
			selectProductInfo.sku_code = $("#product_name_id").attr("sku_code");
			if(productdetail.product_detail_info.skuList.length ==1)
				selectProductInfo.sku_code = productdetail.product_detail_info.skuList[0].skuCode;
			
			selectProductInfo.productName = productdetail.product_detail_info.productName;
			selectProductInfo.sellPrice = $("#price_area_id").attr("priceVal");
			selectProductInfo.flagCheap = productdetail.product_detail_info.flagCheap;
			
			var propertyArray = "";
			$("#kinds_product_properties_id").find("a").filter(".on").each(function(i){
				propertyArray += ($(this).parent().parent().children().eq(0).html() + ":" +$(this).attr("propertyvalue")) + "&";
			});
			if(propertyArray.length > 0 && "&" == propertyArray.substring(propertyArray.length-1)){
				propertyArray = propertyArray.substring(0,propertyArray.length-1);
			}
			selectProductInfo.propertyKeyValue = propertyArray;
			selectProductInfo.totalPrice = (productdetail.last_correct_buy_num * $("#price_area_id").attr("priceVal"));
			selectProductInfo.productPic = productdetail.product_detail_info.mainpicUrl.picNewUrl;
			var disMoney = 0;
			for(var i=0;i<productdetail.product_detail_info.skuList.length;i++){
				if(selectProductInfo.sku_code == productdetail.product_detail_info.skuList[i].skuCode){
					disMoney = productdetail.product_detail_info.skuList[i].disMoney;
					break;
				}
			}
			selectProductInfo.disMoney = disMoney;
			
			zapbase.cookie(cfamily.config.cookie_product_skulist,JSON.stringify(selectProductInfo));
			
			cfamily.href_page("orderconfirm.html?backUrl="+cfamily.url_param_mix(window.location.href,true));
			
		},
		closeHead : function(obj){
			$(obj).parent().hide();
		},
		switchItme : function(n){
			var menu = document.getElementById("menu");
	        var menuli = menu.getElementsByTagName("li");
	        for(var i = 0; i< menuli.length;i++){
	            menuli[i].className="";
	            menuli[n].className="on";
	            document.getElementById("tabc"+ i).className = "no";
	            document.getElementById("tabc"+ n).className = "tabc";
	        }
		},
		getCountDown : function(t_time) {
		  	//计算当前剩余时间
			var EndTime = new Date(t_time.replace(/-/g, "/"));
		    var leftTime = eval(EndTime.getTime() - productdetail.serverTime);
		    if(leftTime >= 0) {
			    var leftsecond = parseInt(leftTime / 1000);
			    var day1 = Math.floor(leftsecond / (60 * 60 * 24));
			    var hour = Math.floor((leftsecond - day1 * 24 * 60 * 60) / 3600);
			    var minute = Math.floor((leftsecond - day1 * 24 * 60 * 60 - hour * 3600) / 60);
			    var second = Math.floor(leftsecond - day1 * 24 * 60 * 60 - hour * 3600 - minute * 60);
			    if (hour < 10) {
				   hour = "0" + hour;
			    }
			    if (minute < 10) {
				   minute = "0" + minute;
			    }
			    if (second < 10) {
				   second = "0" + second;
			    }
			    return hour+":"+minute+":"+second;
			} else {
				return "00:00:00";
			}
		},
		min_add : function(opt){
			var num = $("#buy-num").val();
			var isError = false;
			if(opt == '-' && num == 1){
				capi.show_message("您至少要购买 1 件商品");
				num=productdetail.last_correct_buy_num;
				isError = true;
			}
			/* fq  最大限购数判断 product_name_id*/
			var sku_code = $("#product_name_id").attr("sku_code");
			var maxSalesNum = 0;
			var showlimit = productdetail.skushowLimitNum[sku_code];
			if(sku_code.length > 0) {
				maxSalesNum = eval(productdetail.skuMaxBuyArr[sku_code]);
			}
			if(opt == '+' && (num >= 99 || num > productdetail.total_stock_num || (showlimit == 1 && Number(num) > maxSalesNum))){
				capi.show_message("商品不能购买更多了哦");
				num=productdetail.last_correct_buy_num;
				isError = true;
			}
			if(!isError){
				opt == '+' ? num++ : ((num == 1) ? 1 :num--);
				productdetail.last_correct_buy_num = num;//记录本次正确购买数量
				
			}
			$("#buy-num").val(num);
		},
		getPrerogative : function(){
			cfamily.href_page("../cfamilypage/prerogative.html?pmobile="+zapbase.up_urlparam('pm'));
		},
		checkIsStock :function(propertyArray){
			var stockNum = 0;
			if(productdetail.product_detail_info && productdetail.product_detail_info.skuList.length > 0){
				for(var i=0;i<productdetail.product_detail_info.skuList.length;i++){
					var skuObj = productdetail.product_detail_info.skuList[i];
					if(skuObj.keyValue && skuObj.keyValue.length > 0){
						var count = 0;
						for(var j=0;j<propertyArray.length;j++){
							if(skuObj.keyValue.indexOf(propertyArray[j]) != -1){
								count ++;
							}
						}
						if(count == propertyArray.length){
							stockNum = skuObj.stockNumSum;
						}
					}
				}
			}
			return stockNum;
		},
		/** fq  展示限购数 */
		showMaxBuyNum : function() {
			var sku_code = $("#product_name_id").attr("sku_code");
			//showLimitNum
			var t_skuMaxBuyNum = productdetail.skuMaxBuyArr[sku_code];
			if(t_skuMaxBuyNum == undefined) {
				for ( var ele in productdetail.skuMaxBuyArr) {
					t_skuMaxBuyNum = productdetail.skuMaxBuyArr[ele];
					break;
				}
			}
			if(t_skuMaxBuyNum < 0 ) {
				t_skuMaxBuyNum = 0;
			}
			var sowlimit = productdetail.skushowLimitNum[sku_code];
			if(sowlimit == 1) {
				$("#showSkuMaxBuy").html("限购"+t_skuMaxBuyNum+"件");
			}
		}
};