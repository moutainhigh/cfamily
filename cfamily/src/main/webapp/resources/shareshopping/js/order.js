var order = {
	cityList: [],
	countyList: [],
	orderconfirm: function() {
		var user_address = zapbase.config.cookie_base + 'address';
		var user_receipt = zapbase.config.cookie_base + 'receipt';
		var user_coupon = zapbase.config.cookie_base + 'coupon';
		var user_area = zapbase.config.cookie_base + 'area';
		//var user_token = zapbase.config.cookie_base + 'token';
		$.removeCookie(user_address,{path:'/'});
		$.removeCookie(user_receipt,{path:'/'});
		$.removeCookie(user_coupon,{path:'/'});
		$.removeCookie(user_area,{path:'/'});
		//$.removeCookie(user_token,{path:'/'});
		var backUrl = zapbase.up_urlparam('backUrl');
		if(backUrl && backUrl != "" && "undefined" != backUrl) {
    		$("#backurl_id").attr("href",cfamily.url_param_mix(backUrl));
    		zapbase.cookie('backUrl', cfamily.url_param_mix(backUrl));
    	} else {
    		backUrl = zapbase.cookie('backUrl');
    		$("#backurl_id").attr("href", backUrl);
    	}
		$(".address").click(function() {
			window.location = 'addressconfirm.html';
		});
		var obj = cfamily.up_product_sku_list();
		order.goodsGroup2(obj);
	},
	addressconfirm: function() {
		$('.maskcon').text('努力加载中');
		$('.mask').show();
		var backUrl = zapbase.up_urlparam('backUrl');
		if(backUrl && backUrl != "" && "undefined" != backUrl) {
    		$("#backurl_id").attr("href", cfamily.url_param_mix(backUrl));
    	} else {
    		$("#backurl_id").attr("href", 'orderconfirm.html');
    	}
		$('#getCode').click(function() {
			code.stime(60);
		});
		$("select").change(function() {
			var option = '<option value="0">请选择</option>';
			if(this.id=='province') {
				var value = this.value.substring(0,2);
				if(this.value!=0) {
					$(order.cityList).each(function(i) {
						if(this.cityID.substring(0,2)==value) {
							option += '<option value="'+this.cityID+'">'+this.cityName+'</option>';
						}
					});
				}
				$("#city").html(option);
				$("#county").html(option);
				$("#area").val('');
			} else if(this.id=='city') {
				if(this.value!=0) {
					var value = this.value.substring(0,4);
					$(order.countyList).each(function(i) {
						if(this.districtID.substring(0,4)==value) {
							option += '<option value="'+this.districtID+'">'+this.district+'</option>';
						}
					});
				}
				$("#county").html(option);
				$("#area").val('');
			} else {
				$("#area").val(this.value);
			}
		});
		cfamily.temp.is_callback_error = "1";
		cfamily.call_api("store_message", {}, function(data) {
			cfamily.temp.is_callback_error = "0";
			var provinceOption = '';
			$(data.list).each(function() {
				provinceOption += '<option value="'+this.provinceID+'">'+this.provinceName+'</option>';
				$(this.cityList).each(function() {
					order.cityList.push({'cityID': this.cityID, 'cityName':this.cityName});
					$(this.districtList).each(function() {
						order.countyList.push({'districtID':this.districtID, 'district':this.district});;
					});
				});
			});
			$("#province").append(provinceOption);
			var address = zapbase.cookie('address');
			if(address!=undefined && address!='undefined' && address!='') {
				address =  JSON.parse(address);
				$('#name').val(address.name);
				$('#mobile').val(address.mobile);
				$('#province').children().each(function() {
					if(this.value==address.province) {
						$(this).attr('selected', 'selected');
					}
				});
				var option = '<option value="0">请选择</option>';
				var value = address.province.substring(0,2);
				$(order.cityList).each(function(i) {
					if(this.cityID.substring(0,2)==value) {
						option += '<option value="'+this.cityID+'">'+this.cityName+'</option>';
					}
				});
				$('#city').html(option).children().each(function() {
					if(this.value==address.city) {
						$(this).attr('selected', 'selected');
					}
				});
				option = '<option value="0">请选择</option>';
				value = address.county.substring(0,4);
				$(order.countyList).each(function(i) {
					if(this.districtID.substring(0,4)==value) {
						option += '<option value="'+this.districtID+'">'+this.district+'</option>';
					}
				});
				$('#county').html(option).children().each(function() {
					if(this.value==address.county) {
						$(this).attr('selected', 'selected');
					}
				});
				$('#detail').val(address.detail);
				$('#area').val(address.area);
			}
			$('.mask').hide();
			$('.maskcon').text(function() {
				return $(this).attr('init-data');
			});
		});
		$("#address_save").click(function() {
			var flag = true;
			$(":input").each(function() {
				var $_this = $(this);
				if($_this.attr('id')=='name') {
					if($.trim($_this.val()).length==0) {
						capi.show_message("请填写收货人姓名");
						flag = false;
						return false;
					}
					if(!validateRules.isChinese($_this.val())||$_this.val().length<2||$_this.val().length>10) {
						capi.show_message("收货人姓名为2~10个汉字");
						flag = false;
						return false;
					}
				} else if($_this.attr('id')=='code' && (!validateRules.isNumber($_this.val()))) {
					$_this.val('');
					capi.show_message("请填写正确验证码");
					flag = false;
					return false;
				} else if($_this.attr('id')=='mobile' && (!order.validateMobile($_this.val()))) {
					flag = false;
					return false;
				} else if($_this.attr('id')=='detail' && ($_this.val().length<5||$_this.val().length>50)) {
					capi.show_message("详细地址字数需控制在5~40之间哦~");
					flag = false;
					return false;
				}
			});
			if(!flag) {
				return;
			}
			var area = '';
			$("select").each(function() {
				var $_this = $(this);
				if(($_this.attr('id')=='province'||$_this.attr('id')=='city'||$_this.attr('id')=='county')&&$_this.val()=='0') {
					capi.show_message("请选择地区");
					flag = false;
					return false;
				}
				area = this.value;
			});
			if(!flag) {
				return;
			}
			var name = $('#name').val();
			var mobile = $('#mobile').val();
			var code = $('#code').val();
			var province = $('#province').val();
			var city = $('#city').val();
			var county = $('#county').val();
			var detail = $('#detail').val();
			var cookie_parent_mobile = zapbase.cookie(cfamily.config.cookie_parent_mobile);
			var address_detail = {'name': name, 'mobile':mobile, 'province': province, 'city':city, 'county': county, 'detail': detail, 'area':area};
			
			$(".mask").show();
			cfamily.temp.is_callback_error = "1";
			cfamily.call_api("save_address_and_login", {'verifyCode':code, 'receiver':name, 'areaCode':county, 'detailAddress':detail, 'mobile':mobile, 'referrerMobile':cookie_parent_mobile}, function(data) {
				cfamily.temp.is_callback_error = "0";
				$(".maskcon").text(function() {
					if(data.resultCode==1) {
						return "信息保存成功";
					} else {
						return "信息保存失败";
					}
				});
				flag = 1;
				setTimeout(function() {
					if(data.resultCode!=1) {
						capi.show_message(data.resultMessage);
					} else {
						zapbase.cookie("area", area);
						zapbase.cookie("token", data.userToken);
						zapbase.cookie('address', JSON.stringify(address_detail));
						window.location = 'orderadd.html';
					}
					$(".mask").hide();
				}, 800);
			});
		});
	},
	validateMobile: function(str) {
		if(!validateRules.isMobile(str)) {
			capi.show_message("请填写正确的手机号码");
			return false;
		}
		return true;
	},
	orderadd: function() {
		$(".mask").show();
		var backUrl = zapbase.up_urlparam('backUrl');
		if(backUrl && backUrl != "" && "undefined" != backUrl) {
    		$("#backurl_id").attr("href",cfamily.url_param_mix(backUrl));
    	} else {
    		backUrl = zapbase.cookie('backUrl');
    		$("#backurl_id").attr("href", backUrl);
    	}
		
		$('.address2').click(function() {
			window.location = 'addressconfirm.html?backUrl='+cfamily.url_param_mix(window.location.href,true);
		});
		var token = zapbase.cookie("token");
		var receipt = zapbase.cookie("receipt");
		receipt = (receipt=='undefined'||receipt==undefined)?'':receipt;
		var coupon = zapbase.cookie("coupon");
		coupon = (coupon=='undefined'||coupon==undefined)?'':coupon;
		var area = zapbase.cookie("area");
		area = (area=='undefined'||area==undefined)?'':area;
		var goods = cfamily.up_product_sku_list();
		if(receipt!=undefined && receipt!='undefined' && receipt!='') {
			receipt = JSON.parse(receipt);
			$('<span class="kfp">'+receipt.bill_title+'<em>'+receipt.bill_detail+'</em></span>').insertAfter('.tspan');
		} else {
			$('<span class="bk">不开发票</span>').insertAfter('.tspan');
		}
		var arr = new Array();
		var dd = {'sku_code':goods.sku_code, 'sku_num':goods.sku_num, 'area_code': area, 'product_code':goods.product_code};
		arr.push(dd);
		cfamily.temp.is_callback_error = "1";
		capi.call_api({
			target : 'com_cmall_familyhas_api_APiOrderConfirm',
			params : {'api_key':'betafamilyhas', 'order_type':'449715200005', 'goods':arr, 'coupon_codes':[coupon], 'area_code':area},
			callback : function(data) {
				cfamily.temp.is_callback_error = "0";
				$(".mask").hide();
				if(data.resultCode!=1) {
					capi.show_message(data.resultMessage);
					return false;
				}
				var order_type = '449715200005';
				var buyer_name = '';
				var buyer_address_code = '';
				var buyer_province = '';
				var buyer_address = '';
				var buyer_mobile = '';
				var pay_type = '449746280003';//默认支付宝
				var check_pay_money = '';
				var _receipt = zapbase.cookie('receipt');
				var billInfo = (_receipt==undefined||_receipt=='undefined')?'':JSON.parse(zapbase.cookie('receipt'));
				if(billInfo=='') {
					billInfo = {"bill_Type":"","bill_title":"","bill_detail":""};
				}
				arr = new Array();
				dd = {'sku_code':data.resultGoodsInfo[0].sku_code, 'sku_num':data.resultGoodsInfo[0].sku_num, 'product_code':data.resultGoodsInfo[0].product_code, 'area_code': area};
				arr.push(dd);
				
				var information = data.information;
				buyer_name = information.address_name;
				buyer_address_code = information.area_code;
				buyer_province = information.address_province;
				buyer_address = information.address_street;
				buyer_mobile = information.address_mobile;
				check_pay_money = data.pay_money;
				
				$('.name').html('<b>&nbsp;</b>'+buyer_name);
				$('.phone').html('<b>&nbsp;</b>'+buyer_mobile);
				$('#address_street').html(buyer_province+buyer_address);
				order.goodsGroup1(data.resultGoodsInfo, data.sourceFlag);
				var ua = navigator.userAgent.toLowerCase();
				if(ua.match(/MicroMessenger/i) == "micromessenger") {//微信中打开
					$('.lid:eq(1)').show().find('a').addClass('on');
					pay_type = '449746280005';
				} else {
					$('.lid:eq(0)').show().find('a').addClass('on');
					//$('.lid:eq(1)').show();
				}
				if(data.pay_type=='449716200002') {
					$('.lid:eq(2)').show();
				}
				$('.lid').click(function() {
					$('.sela').removeClass('on');
					pay_type = $(this).find('.sela').addClass('on').attr('pay_type');
				});
				
				
				$('#receipt').click(function() {
					window.location = 'receipt.html?receipt='+encodeURI(JSON.stringify(data.bills));
				});
				var coupons = data.coupons;
				if(coupons!=null && coupons.length>0) {
					$('.qnum').hide();
					$('.bky1').hide();
					$('.bky2').text('已抵'+coupons[0].surplusMoney+'元').show();
					var money = 0;
					$(coupons).each(function() {
						money += this.surplusMoney;
					});
					$('#sub_money').html("- ￥"+money).parent('p').show();
				} else if(data.couponAbleNum!=0) {
					$('.qnum').html(data.couponAbleNum+'张可用').show();
					$('.bky1').text('未使用');
				}
				
				$('#coupon').click(function() {
					var data_id = zapbase.up_urlparam('data_id');
					if(data_id==undefined||data_id=='undefined') {
						data_id = '';
					}
					window.location = 'ordercouponconfirm.html?data_id='+data_id;
				});
				$('#cost_money').html(" ￥"+data.cost_money);
				$('#sent_money').html("+ ￥"+data.sent_money);
				//$('#prompt').html(data.prompt);
				$('#pay_money').html(data.pay_money);
				$('.dd-fexed').append('购买立返:￥'+data.cash_back);
				$('.btn').click(function() {
					cfamily.temp.is_callback_error = "1";
					$(".maskcon").text(function() {
						return $(this).attr('init-data');
					});
					$(".mask").show();
					capi.call_api({
						target: 'com_cmall_familyhas_api_APiCreateOrder',
						params: {"order_type":order_type,"goods":arr,"buyer_name":buyer_name,"buyer_address_code":buyer_address_code,"buyer_address":buyer_address,"buyer_mobile":buyer_mobile,"pay_type":pay_type,"check_pay_money":check_pay_money,"billInfo":billInfo,"coupon_codes":[coupon], app_vision:'1.0.0'},
						callback: function (data) {
							cfamily.temp.is_callback_error = "0";
							setTimeout(function() {
								if(data.resultCode==1) {
									if(pay_type=='449746280003') {//支付宝支付
										window.location = '../../payOrder/'+data.order_code+'wap/'+pay_type+'?source=jwap'; 
									} else if(pay_type=='449746280005') {//微信支付
										//var callback_url = 'http://qhbeta-cfamily.qhw.srnpr.com/cfamily/wechatWAPNet?orderCode='+data.order_code;//测试环境
										var callback_url = 'http://api-family.syapi.ichsy.com/cfamily/wechatWAPNet?orderCode='+data.order_code;//生产环境

										//cfamily.call_api("get_wx_pay_info2", {'merchantid':'13', 'tradetype':'oauth', 'orderno':'b', 'tradeCode':'WeiXin', 'channelid':'5', 'callBackURL':callback_url}, function(da) {//测试环境
										cfamily.call_api("get_wx_pay_info2", {'merchantid':'ichsywap', 'tradetype':'oauth', 'orderno':'b', 'tradeCode':'WeiXin', 'channelid':'6', 'callBackURL':callback_url}, function(da) {//生产环境
											if(da.resultCode!=1) {
												capi.show_message(da.resultMessage);
											} else {
												var url = cfamily.wxConfig_test(data.order_code, da.mac, da.tradetime);
												window.location = url;
											}
										});
									} else {//货到付款
										window.location = 'ordersucceed.html?out_trade_no='+data.order_code+'&trade_status=TRADE_FINISHED';
									}
									$(".maskcon").text(function() {
										return "加载成功";
									});
								} else {
									$(".maskcon").text(function() {
										return "加载失败";
									});
									//capi.show_message(data.resultMessage);
								}
								$(".mask").hide();
							}, 1500);
						},
						token : token
					});
				});
				$(".mask").hide();
			},
			token : token
		});
	},
	onBridgeReady: function() {
		var appid = $("#appid").val();
		var timestamp = $("#timestamp").val();
		var nonce_str = $("#nonce_str").val();
		var prepay_id = $("#prepay_id").val();
		var sign = $("#sign").val();
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId": appid,
			"timeStamp": timestamp,
			"nonceStr": nonce_str,
			"package": "prepay_id="+prepay_id,
			"signType": "MD5",
			"paySign": sign
		}, function(res) {
			if(res.err_msg == "get_brand_wcpay_request：ok" ) {
				alert('OK');
			}
		});
	},
	goodsGroup1: function(data, sourceFlag) {
		var ihtml = '';
		$(data).each(function() {
			ihtml += '<div class="sbox spro">';
			ihtml += '<img src="'+this.pic_url+'" alt="" />';
			ihtml += '<div class="imgr">';
			ihtml += '<p class="pt">'+this.sku_name+'</p>';
			$(this.sku_property).each(function() {
				ihtml += '<p>'+this.propertyKey+':'+this.propertyValue+'</p>';
			});
			ihtml += '</div>';
			ihtml += '<div class="imgrr">';
			ihtml += '<p class="price"><b>￥</b>'+this.sku_price+'</p>';
			ihtml += '<p class="pnum">x'+this.sku_num+'</p>';
			if(sourceFlag==1) {
				ihtml += '<p class="ptag"><span class="tag">闪购</span></p>';
			}
			ihtml += '</div></div>';
		});
		$(ihtml).insertAfter('.address2');
	},
	goodsGroup2: function(data) {
		var ihtml = '';
		ihtml += '<div class="sbox spro">';
		ihtml += '<img src="'+data.productPic+'" alt="" />';
		ihtml += '<div class="imgr">';
		ihtml += '<p class="pt">'+data.productName+'</p>';
		var obj = data.propertyKeyValue.split('&');
		for(var i=0; i<obj.length; i++) {
			ihtml += '<p>'+obj[i]+'</p>';
		}
		ihtml += '</div>';
		ihtml += '<div class="imgrr">';
		ihtml += '<p class="price"><b>￥</b>'+data.sellPrice+'</p>';
		ihtml += '<p class="pnum">x'+data.sku_num+'</p>';
		if(data.flagCheap==1) {
			ihtml += '<p class="ptag"><span class="tag">闪购</span></p>';
		}
		ihtml += '</div></div>';
		$(ihtml).insertAfter('.address');
		$("#totalMoney").text('￥'+data.totalPrice);
		$("#realMoney").html(data.totalPrice).parent('span').after("购买立返:￥"+data.disMoney);
		$('.btn').click(function() {
			capi.show_message('请填写收货人信息');
		});
	},
	receipt: function() {
		var receipt_cookie = zapbase.cookie('receipt');
		$('.jt').click(function() {
			$('.popup').show();
			$('.last_a').click(function() {
				$('.popup').hide();
			});
		});
		$('.bc').click(function() {
			var type = $('#sela').find('.on').attr('type');
			if(type=='1') {
				var type = $('#sela_divt').find('.on').attr('type');
				var title = '个人';
				if(type==1) {
					title = $('#ds').val();
					if($.trim(title).length==0) {
						capi.show_message('请先写发票抬头');
						return false;
					}
				}
				var detail = $('#receipt').val();
				zapbase.cookie('receipt', JSON.stringify({'bill_Type':'449746310001', 'bill_title':title, 'bill_detail':detail}));
			} else {
				var user_receipt = zapbase.config.cookie_base + 'receipt';
				$.removeCookie(user_receipt,{path:'/'});
			}
			window.location = 'orderadd.html';
		});
		var receipt = decodeURI(zapbase.up_urlparam('receipt'));
		var _receipt = '';
		$(JSON.parse(receipt)).each(function(i) {
			if(i==0) {
				_receipt += '<option value="'+this+'" selected="selected">'+this+'</option>';
			} else{
				_receipt += '<option value="'+this+'">'+this+'</option>';
			}
		});
		$('#receipt').html(_receipt);
		$('#sela').find('a').bind('click', function() {
			$('#sela').find('a').removeClass('on');
			$(this).addClass('on');
			$('.tip').removeClass('ds');
			if($(this).attr('type')=='1') {
				$('.sel-lx, .mx').removeClass('ds');
				$('#sela_divt').find('a').click(function() {
					$('#sela_divt').find('a').removeClass('on');
					$(this).addClass('on');
					if($(this).attr('type')=='1') {
						$("#ds").show();
					} else {
						$("#ds").hide().val('');
					}
				});
			} else {
				$('.sel-lx, .mx').addClass('ds');
				$('.tip').addClass('ds');
				$("#ds").hide().val('');
				$('#sela_divt').find('a').addClass('on').last().removeClass('on');
			}
		});
		if(receipt_cookie!=undefined&&receipt_cookie!='undefined'&&receipt_cookie!='') {
			receipt_cookie = JSON.parse(receipt_cookie);
			$('#sela').children().first().removeClass('on');
			$('#sela').children().last().addClass('on').click();
			$('.sel-lx, .mx, .tip').removeClass('ds');
			if(receipt_cookie.bill_title!='个人') {
				$('#sela_divt').children().first().removeClass('on');
				$('#sela_divt').children().last().addClass('on');
				$('#ds').val(receipt_cookie.bill_title).show();
			}
			$('#receipt option').each(function() {
				if(this.value==receipt_cookie.bill_detail) {
					$(this).attr('selected', 'selected');
				}
			});
		}
	},
	orderCouponConfim: function() {
		var dataId = zapbase.up_urlparam('data_id');
		$('.jt').attr('href', 'orderadd.html?data_id='+dataId);
		var goods = cfamily.up_product_sku_list();
		var arr = new Array();
		dd = {'sku_code':goods.sku_code, 'sku_num':goods.sku_num, 'product_code':goods.product_code};
		arr.push(dd);
		var token = zapbase.cookie("token");
		capi.call_api({
			target : 'com_cmall_familyhas_api_ApiGetAvailableCoupon',
			params : {'shouldPay':goods.totalPrice, 'goods':arr},
			callback : function(data) {
				if(data.resultCode==1) {//有
					var ihtml = '';
					$(data.couponList).each(function(i) {//可用
						ihtml += '<div class="lid">';
						ihtml += '<div class="quan-l"><span><b>¥</b>'+this.surplusMoney+'</span><p>'+this.useLimit+'</p></div>';
						ihtml += '<div class="quan-r"><p>'+this.limitExplain+'</p>';
						ihtml += '<p class="time"><span>使用期限:</span>'+cfamily.convertDate(this.startTime, 'yyyy-mm-dd')+'-'+cfamily.convertDate(this.endTime, 'yyyy-mm-dd')+'</p>';
						ihtml += '</div>';
						ihtml += '<a href="javascript:;" class="sela" coupon_code="'+this.couponCode+'" coupon_id="'+this.couponCode+i+'" coupon_val="'+this.initialMoney+'">&nbsp;</a>';
						ihtml += '</div>';
					});
					if(ihtml!='') {
						$('#couponList').html(ihtml);
						$('.share-qq:eq(0), .qq-footer').show();
					}
					var obj = $('#couponList').find('.sela');
					$(obj).each(function() {
						var id = $(this).attr('coupon_id');
						if(id==dataId) {
							$(this).addClass('on');
							$('.btn-buy').attr('data_code', $(this).attr('data_code')).attr('data_val', $(this).attr('coupon_val')).attr('data_id', dataId);
						}
					});
					ihtml = '';
					$(data.disableCouponList).each(function() {//不可用
						ihtml += '<div class="lid">';
						ihtml += '<div class="quan-l"><span><b>¥</b>'+this.surplusMoney+'</span><p>'+this.useLimit+'</p></div>';
						ihtml += '<div class="quan-r"><p>'+this.limitExplain+'</p>';
						ihtml += '<p class="time"><span>使用期限:</span>'+cfamily.convertDate(this.startTime, 'yyyy-mm-dd')+'-'+cfamily.convertDate(this.endTime, 'yyyy-mm-dd')+'</p>';
						ihtml += '</div></div>';
					});
					if(ihtml!='') {
						$('.share-qq:eq(1)').show();
						$('#disableCouponList').html(ihtml);
					}
					$('.sela').click(function() {
						$('.sela').removeClass('on');
						var data_id = $('.btn-buy').attr('data_id');
						var coupon_id = $(this).attr('coupon_id');
						var coupon_code = $(this).attr('coupon_code');
						var coupon_val =$(this).attr('coupon_val');
						if(coupon_id!=data_id) {
							$('.btn-buy').attr('data_code', coupon_code).attr('data_val', coupon_val).attr('data_id', coupon_id);
							$(this).addClass('on');
						} else {
							$('.btn-buy').attr('data_code', '').attr('data_val', '').attr('data_id', '');
						}
					});
					$('.btn-buy').click(function() {
						if($(this).attr('data_code')=='') {
							var coupon = zapbase.config.cookie_base + 'coupon';
							$.removeCookie(coupon,{path:'/'});
							window.location = 'orderadd.html';
						} else {
							zapbase.cookie('coupon', $(this).attr('data_code'));
							window.location = 'orderadd.html?data_id='+$(this).attr('data_id');
						}
					});
				} else {//无
					$('.wu').hide();
				}
			},
			token: token
		});
		
		$('#ok').click(function() {//兑换
			var value = $("#coupon_code").val();
			if($.trim(value).length==0) {
				capi.show_message('请输入优惠码');
			} else {
				cfamily.temp.is_callback_error = "1";
				$('.mask').show();
				capi.call_api({
					target: 'com_cmall_familyhas_api_ApiForCouponCodeExchange',
					params: {'couponCode': new Base64().encode(value)},
					callback: function(data) {
						cfamily.temp.is_callback_error = "0";
						$('.mask').hide();
						if(data.resultCode==1) {//有
							capi.show_message('兑换成功');
							setTimeout(function() {
								window.location = window.location;
							}, 1000);
						} else {//无
							capi.show_message(data.resultMessage);
						}
					},
					token: token
				});
			}
		});
	}
};
var code = {
	time: 1,
	stime : function(count) {
		if(!order.validateMobile($("#mobile").val())) {
			return false;
		}
		if(this.time==1) {
			cfamily.call_api("send_mobile_code", {'send_type':'binding', 'mobile':$("#mobile").val()}, function(data) {
				if(data.resultCode!=1) {
					capi.show_message(data.resultMessage);
				}
			});
		}
		if (count == 0) {
			this.time = 1;
			$('.submit button').attr('disabled', false);
			$('.submit button').removeClass('curr');
			$('.submit button').html('获取验证码');
			return false;
		} else {
			this.time = 0;
			$('.submit button').attr('disabled', 'disabled');
			$('.submit button').html(count + 's后可重发');
			$('.submit button').addClass('curr');
			count--;
		}
		setTimeout(function() {
			code.stime(count);
		}, 1000);
	}
};