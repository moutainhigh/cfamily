var cshop_product = {

	product : {},

	temp : {
		// 分类属性
		categoryproperty : [],
		//商品虚类
		usprList : [],
		// 属性列表
		proplist : [],
		// 基本属性
		baseprop : {},
		// 是否修改模式
		flagedit : false,

		// 父属性字段长度
		fatherlength : 16,
		// 属性字段长度
		extendlength : 20,

		// 颜色属性
		p_color : {},
		// 关键属性
		p_key : {},
		// 属性列 主键为属性的ID
		p_list : {},
		p_extend : {},
		
		copy_tr : {vPrice:"",vNum:"",vCost:"",color:"",size:""},
		//默认选中的颜色属性
		checkColorInit:[],
		//默认存在的sku信息
		productSkuInfoList:[]
	},
	// 初始化
	init_product : function(p) {

		cshop_product.product = p;
		cshop_product.temp.productSkuInfoList = cshop_product.product.productSkuInfoList;
		cshop_product.SecondStep();
	},
	SecondStep : function() {
			$("#zw_f_listbox").val('44971603002900010001');
			cshop_product.show_edit();
	},

	// 显示修改信息
	show_edit : function() {

//		cshop_product.set_category();

		cshop_product.up_category_prop();

		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
						cshop_product.beforesubmit);
	},
	set_skuCheckbox : function(){
		//sku颜色与款式复选框赋值
		var productSkuInfoList = cshop_product.temp.productSkuInfoList;
		var skuColor = [];
		var skuStyle = [];
		var skuInfo = [];
		if (null != productSkuInfoList) {
			for (var i = 0; i < productSkuInfoList.length; i++) {
				var skukeyCode = productSkuInfoList[i].skuKey.split("&");
				var skuKeyValue = productSkuInfoList[i].skuValue.split("&");
				if (null != skukeyCode && skukeyCode.length > 1 && null != skuKeyValue&& skuKeyValue.length > 1 ) {
					var colorKey = skukeyCode[0].split("=");
					var styleKey = skukeyCode[1].split("=");
					var colorValue = skuKeyValue[0].split("=");
					var styleValue = skuKeyValue[1].split("=");
					
					var color_style = colorKey[1]+"_"+styleKey[1];
					skuInfo.push({"color_style":color_style,"skuInfo":productSkuInfoList[i]});
					
					skuColor.push({"code":colorKey[1],"value":colorValue[1],"colorPic":productSkuInfoList[i].skuPicUrl});
					skuStyle.push({"code":styleKey[1],"value":styleValue[1],"colorPic":productSkuInfoList[i].skuPicUrl});
				}
			}
			skuColor =  cshop_product.uniqueArrSkuKey(skuColor);
			skuStyle = cshop_product.uniqueArrSkuKey(skuStyle);
			
			//默认选中的颜色属性，控制颜色图片与默认已经存在的sku不显示在sku列表中
			cshop_product.temp.checkColorInit=skuColor;
			
		}
		
		if (null != skuColor && skuColor.length > 0) {
			for (var i = 0; i < skuColor.length; i++) {
//				$('#cshop_addproduct_prop_'+skuColor[i].code).click();
				$('#cshop_addproduct_prop_'+skuColor[i].code).prop("checked", true);
				cshop_product.init_prop($('#cshop_addproduct_prop_'+skuColor[i].code));
				$('#cshop_addproduct_pname_'+skuColor[i].code).val(skuColor[i].value);
				//已经存在的sku的规格型号禁止修改
				$('#cshop_addproduct_prop_'+skuColor[i].code).attr("disabled",true);
				$('#cshop_addproduct_pname_'+skuColor[i].code).attr("readonly","readonly");
			}
		}
		if (null != skuStyle && skuStyle.length > 0) {
			for (var i = 0; i < skuStyle.length; i++) {
//				$('#cshop_addproduct_prop_'+skuStyle[i].code).click();
				$('#cshop_addproduct_prop_'+skuStyle[i].code).prop("checked", true);
				cshop_product.init_prop($('#cshop_addproduct_prop_'+skuStyle[i].code));
				$('#cshop_addproduct_pname_'+skuStyle[i].code).val(skuStyle[i].value);
				//已经存在的sku的规格型号禁止修改
				$('#cshop_addproduct_prop_'+skuStyle[i].code).attr("disabled",true);
				$('#cshop_addproduct_pname_'+skuStyle[i].code).attr("readonly","readonly");
			}
		}
		//sku各颜色图片赋值
//		if (null != skuColor && skuColor.length > 0) {
//			for (var i = 0; i < skuColor.length; i++) {
//				$('#cshop_addproduct_ctext_'+skuColor[i].code).text(skuColor[i].value);
//				if (null!=skuColor[i].colorPic&&""!=skuColor[i].colorPic) {
//					if(!zapjs.f.exist('cshop_addproduct_color_' + skuColor[i].code)) {
//					}else{
//						$('#cshop_addproduct_color_'+skuColor[i].code).val(skuColor[i].colorPic);
//						zapweb_upload.upload_file('cshop_addproduct_color_'+skuColor[i].code,skuColor[i].colorPic);
//					}
//				}
//			}
//		}
//		cshop_product.new_show_sku();		//这句必须要加的。不然在SKU设置里款式的名字会显示原生的名字(如：款式一)
		//SKU设置赋值
//		if (null != skuInfo && skuInfo.length > 0) {
//			for (var i = 0; i < skuInfo.length; i++) {
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").siblings(".c_price:first").val(skuInfo[i].skuInfo.sellPrice);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").siblings(".c_skuCode:first").val(skuInfo[i].skuInfo.skuCode);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().siblings("td").find(".c_costPrice:first").val(skuInfo[i].skuInfo.costPrice);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().nextAll("td").find(".c_stock:first").val(skuInfo[i].skuInfo.stockNum);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().nextAll("td").find(".c_securityStockNum:first").val(skuInfo[i].skuInfo.securityStockNum);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().nextAll("td").find(".c_miniOrder:first").val(skuInfo[i].skuInfo.miniOrder);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().nextAll("td").find(".c_sellProductcode:first").val(skuInfo[i].skuInfo.sellProductcode);
//				$("#cshop_addproduct_plist table tbody tr .c_id[value='"+skuInfo[i].color_style+"']").parent().nextAll("td").find(".c_skuAdv:first").val(skuInfo[i].skuInfo.skuAdv);
//			}
//		}
	},
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		var bFlag = true;
		//第一个ul为颜色属性
		var colorText = [];
		$("#cshop_addproduct_prop ul:first input[id^='cshop_addproduct_pname_']").each(function(){
			colorText.push($(this).val());
		});
		var colorSingle = {};
		if (null != colorText && colorText.length > 0) {
			var nary=colorText.sort(); 
			for(var i=0;i<nary.length-1;i++){
				if (nary[i]==nary[i+1]) 
			       {
					zapjs.f.message('颜色：'+nary[i]+'出现重复，请修改后重试！');
					bFlag = false;
					return false;
			       } 
		    }
		}
		//第二个ul为款式属性
		var styleText = [];
		$("#cshop_addproduct_prop ul:last input[id^='cshop_addproduct_pname_']").each(function(){
			styleText.push($(this).val());
		});
		if (null != styleText && styleText.length > 0) {
			var nary=styleText.sort(); 
			for(var i=0;i<nary.length-1;i++){
				if (nary[i]==nary[i+1]) 
			       {
					zapjs.f.message('款式：'+nary[i]+'出现重复，请修改后重试！');
					bFlag = false;
					return false;
			       } 
		    }
		}
		//第一个ul为颜色属性
		$("#cshop_addproduct_prop ul:first input[id^='cshop_addproduct_pname_']").each(function(){
			if (null == $(this).val() || '' == $(this).val()) {
				zapjs.f.message('颜色属性名称存在空，请修改后重试！');
				bFlag = false;
				return false;
			}
		});
		//第二个ul为款式属性
		$("#cshop_addproduct_prop ul:last input[id^='cshop_addproduct_pname_']").each(function(){
			if (null == $(this).val() || '' == $(this).val()) {
				zapjs.f.message('款式属性名称存在空，请修改后重试！');
				bFlag = false;
				return false;
			}
		});
		if (bFlag) {
			cshop_product.recheck_product();
			
			if (cshop_product.product.productSkuInfoList == null 
					|| cshop_product.product.productSkuInfoList.length<=0) {
				zapjs.f.message('请维护上sku信息后重试！');
				bFlag = false;
				return false;
			}
			
			$('#zw_f_json').val(zapjs.f.tojson(cshop_product.product));
		}
		return bFlag
		
	},
	// 重新处理格式化商品信息
	recheck_product : function() {

		var thisproduct = cshop_product.product;

		// 商品实类
//		thisproduct.pcProductcategoryRel = {
//			categoryCode : $('#zw_f_listbox').val()
//		};
		cshop_product.recheck_prop();

		var productSkuInfoList = cshop_product.temp.productSkuInfoList;
		// SKU信息
		thisproduct.productSkuInfoList = [];

		// 商品属性
		thisproduct.pcProductpropertyList = [];

		for ( var p in cshop_product.temp.p_key) {

			var oProp = cshop_product.temp.p_key[p];
			
			var flagExist = true;		//默认存在的sku不展示出来
			if (oProp["flag"] != "1") {
				flagExist = false;
			}
			for (var skuList in productSkuInfoList) {
				var skuList_ = productSkuInfoList[skuList];
				if (skuList_["skuKey"] == oProp["sku_key"]) {
					flagExist = false;
					break;
				}
			}
			if (flagExist) {
				var othis = {
					skuCode : oProp["skuCode"],
					sellPrice : oProp["price"],
					costPrice : oProp["costPrice"],
					stockNum : oProp["stock"],
					marketPrice : $('#zw_f_market_price').val(),
					skuKey : oProp["sku_key"],
					skuValue : oProp["sku_value"],
					skuKeyvalue : oProp["sku_value"],
					skuPicUrl : '',
					skuAdv : oProp["skuAdv"],
					securityStockNum : oProp["securityStockNum"],
					miniOrder : oProp["miniOrder"],
					sellProductcode : oProp["sellProductcode"],
					skuName : thisproduct.produtName + '  ' + oProp["sku_name"]
				};

				if (oProp["sku_color"] != "") {
					othis.skuPicUrl = cshop_product.temp.p_color[oProp["sku_color"]]["image"];
				}

				thisproduct.productSkuInfoList.push(othis);

			}
		}
	},

	// 获取分类属性
	up_category_prop : function() {

		cshop_product.up_prop_list();

	},
	// 获取属性列表
	up_prop_list : function() {

		zapjs.zw.api_call(
				'com_cmall_productcenter_process_ApiGetCategoryProperty', {
					categoryCode : cshop_product.product.category.categoryCode
				}, cshop_product.prop_list_success);
	},
	//校验只能输入数字和两位小数 
	checkNum : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,2})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和两位小数，并且小数点前限制长度为3位
	checkNumLimit3 : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d{0,3}(?:\d\.\d{0,2})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和六位小数，商品体积字段用
	checkNumVolume : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,6})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和三位小数，商品重量用
	checkNumWeight : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,3})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。   
	//调用：accMul(arg1,arg2,arg3)   
	//返回值：arg1乘以arg2乘以arg3的精确结果   
	 volumeMultiplication : function(arg1,arg2,arg3)  
	{  
	    var m=0,s1=arg1.toString(),s2=arg2.toString(),s3=arg3.toString();  
	    try{m+=s1.split(".")[1].length;}catch(e){}  
	    try{m+=s2.split(".")[1].length;}catch(e){}  
	    try{m+=s3.split(".")[1].length;}catch(e){}  
	    return Number(s1.replace(".",""))*Number(s2.replace(".",""))*Number(s3.replace(".",""))/Math.pow(10,m);
	},
	// 读取属性列表数据
	prop_list_success : function(oData) {
		cshop_product.temp.proplist = oData.listProperty;
		for ( var i in cshop_product.temp.proplist) {
			cshop_product.temp.p_list[cshop_product.temp.proplist[i]["property_code"]] = cshop_product.temp.proplist[i];
		}
		cshop_product.new_show_prop();
	},
	// 根据key获取属性
	up_prop_key : function(sCode) {

		return cshop_product.temp.p_list[sCode];

	},
	// 获取子属性
	up_child : function(sCode) {

		var oRet = [];
		var sprop = cshop_product.temp.p_list;
		for ( var p in sprop) {
			if (sprop[p]["parent_code"] == sCode) {
				oRet.push(sprop[p]);
			}
		}
		return oRet;
	},

	// 获取属性类型定义 返回1为颜色属性 返回2为关键属性 其他为商品属性
	up_type_bycode : function(sCode) {
		var iReturn = 0;
		if (sCode.indexOf('449746200001') == 0) {
			iReturn = 1;
		} else if (sCode.indexOf('449746200002') == 0) {
			iReturn = 2;
		}

		return iReturn;

	},

	// 新版显示
	new_show_prop : function() {

		var sprop = cshop_product.temp.p_list;

		// 颜色属性
		var aColor = [];
		// 基本属性
		var aKey = [];
		// 商品属性
		var aSale = [];

		for ( var p in sprop) {
			// 判断如果是主属性
			if (p.length == cshop_product.temp.fatherlength) {

				var iType = cshop_product.up_type_bycode(p);

				if (iType == 1) {
					aColor.push(p);

					aKey.push(cshop_product.up_child(p));
				} else if (iType == 2) {

					aKey.push(cshop_product.up_child(p));
				} else {
					aSale.push(p);
				}
			}
			;
		}

		// 如果超过一个颜色 则返回错误
		if (aColor.length > 1) {
			zapjs.f.message('该分类下颜色属性超过两个，请联系技术解决。谢谢！' + aColor);
			return false;
		}

		// /开始循环颜色///

		// 循环颜色属性
		for ( var k in aColor) {
			var oColor = cshop_product.up_prop_key(aColor[k]);
			var oChild = cshop_product.up_child(oColor["property_code"]);

			var defaultColor = {};

			// var aPcolor = [];

			for ( var i in oChild) {
				var oThis = oChild[i];
				defaultColor = {
					p_name : oThis["property_name"],
					p_code : oThis["property_code"],
					p_f_code : oColor["property_code"],
					p_f_name : oColor["property_name"],
					image : '',
					flag : 0
				};

				// aPcolor.push(defaultColor);

				cshop_product.temp.p_color[oThis["property_code"]] = defaultColor;

			}

			// cshop_product.temp.p_color[oColor["property_code"]] = aPcolor;
		}

		// /开始循环属性定义///

		var aAllKeyList = [];

		for ( var i in aKey) {

			var oNew = [];

			for ( var k in aKey[i]) {
				var oThis = [];
				var oItem = aKey[i][k];
				if (i == 0) {
					oThis.push(oItem);
					oNew.push(oThis);
				} else {
					for ( var j in aAllKeyList) {
						oThis = aAllKeyList[j].concat();
						oThis.push(oItem);
						oNew.push(oThis);
					}
				}

			}

			aAllKeyList = oNew;
		}

		aAllKeyList.sort(function(a, b) {
			return a[0]["property_code"] > b[0]["property_code"] ? 1 : -1;
		});

		// /开始循环定义///
		for ( var i in aAllKeyList) {
			var aItem = aAllKeyList[i];

			var aCode = [];
			var aSkuKey = [];
			var aSkuValue = [];

			var sColorKey = "";

			var sSkuName = [];

			for ( var k in aItem) {
				var oThis = aItem[k];
				aCode.push(oThis["property_code"]);
				aSkuKey.push(oThis["parent_code"] + "="
						+ oThis["property_code"]);
				aSkuValue
						.push(cshop_product.up_prop_key(oThis["parent_code"])["property_name"]
								+ "=" + oThis["property_name"]);

				sSkuName.push(oThis["property_name"]);

				var iType = cshop_product
						.up_type_bycode(oThis["property_code"]);
				if (iType == 1) {
					sColorKey = oThis["property_code"];
				}

			}

			var defaultKey = {
				flag : 0,
				sku_key : aSkuKey.join('&'),
				sku_value : aSkuValue.join('&'),
				sku_color : sColorKey,
				price : '',
				costPrice : '',
				stock : 0,
				skuAdv : '',
				securityStockNum : 1,
				miniOrder : 1,
				sellProductcode : '',
				sku_name : sSkuName.join('  ')
			};

			cshop_product.temp.p_key[aCode.join('_')] = defaultKey;

		}

		// /开始循环展示定义///

		var aBaseHtml = [];
		var aHeadHtml = [];
		// 定义扩展属性显示
		var aExtendProp = [];
		for ( var i in aKey) {

			var oFather = cshop_product.up_prop_key(aKey[i][0]["parent_code"]);

			aBaseHtml.push('<div class="csb_cshop_addproduct_prop_title w_c">'
					+ oFather["property_name"] + '</div><ul>');

			for ( var k in aKey[i]) {
				var oItem = aKey[i][k];
				aBaseHtml
						.push('<li><input onclick="cshop_product.change_prop(this)" id="cshop_addproduct_prop_'
								+ oItem["property_code"]
								+ '" type="checkbox" value="'
								+ oItem["property_code"] + '"/>');
				aBaseHtml.push('<label >'
						+ oItem["property_name"] + '</label></li>');
			}
			aBaseHtml.push('</ul><div class="w_h20 w_clear"></div>');

			aHeadHtml.push('<th>' + oFather["property_name"] + '</th>');

		}

		if (aBaseHtml.length > 0) {
			$('#cshop_addproduct_prop .csb_cshop_addproduct_item').append(
					aBaseHtml.join(''));
			$('#cshop_addproduct_prop').show();
		}

		if (aHeadHtml.length > 0) {
			$('#cshop_addproduct_plist table thead tr').prepend(
					aHeadHtml.join(''));
			// $('#cshop_addproduct_plist').show();
		}
		
		cshop_product.set_skuCheckbox();
	},

	// 重新检查属性信息
	recheck_prop : function() {

		$('#cshop_addproduct_colors table  tbody tr').each(
				function(n, el) {
					var sId = $(el).find('.c_id').val();
					cshop_product.temp.p_color[sId]["image"] = $(
							'#cshop_addproduct_color_' + sId).val();
				});

		$('#cshop_addproduct_plist table tbody tr').each(
				function(n, el) {
					var sId = $(el).find('.c_id').val();
					cshop_product.temp.p_key[sId]["price"] = $(el).find(
							'.c_price').val();
					cshop_product.temp.p_key[sId]["stock"] = $(el).find(
							'.c_stock').val();
					cshop_product.temp.p_key[sId]["sku_name"] = $(el)
					.find('.c_name').val();
					cshop_product.temp.p_key[sId]["securityStockNum"] = $(el)
							.find('.c_securityStockNum').val();
					cshop_product.temp.p_key[sId]["sellProductcode"] = $(el)
							.find('.c_sellProductcode').val();
					cshop_product.temp.p_key[sId]["skuAdv"] = $(el).find('.c_skuAdv').val();
					

					cshop_product.temp.p_key[sId]["costPrice"] = $(el).find(
							'.c_costPrice').val();
					cshop_product.temp.p_key[sId]["miniOrder"] = $(el).find(
					'.c_miniOrder').val();
					
					var key = cshop_product.temp.p_key[sId]["sku_key"];
					for(var cd in key.split('&')){
						var cds = key.split('&')[cd].split('=');
						key = key.replace(cds[0]+'=',cshop_product.up_prop_key(cds[0])["property_name"]+'=');
						if(typeof($('#cshop_addproduct_pname_'+cds[1]).val()) == 'undefined'){
							key = key.replace(cds[1],cshop_product.temp.p_list[$('#cshop_addproduct_prop_'+cds[1]).val()].property_name);
						}else{
							key = key.replace(cds[1],$('#cshop_addproduct_pname_'+cds[1]).val());
						}
					}
					cshop_product.temp.p_key[sId]["sku_value"]=key;
				});

	},

	change_prop : function(oElm) {
		var sVal = $(oElm).val();

		var bFlagChecked = $(oElm).is(':checked') ? 1 : 0;

		for ( var p in cshop_product.temp.p_color) {
			if (p.indexOf(sVal) > -1) {
				cshop_product.temp.p_color[p]["flag"] = bFlagChecked;
			}
		}
		for ( var p in cshop_product.temp.p_key) {
			if (p.indexOf(sVal) > -1) {

				//var bCheckAll = false;

				var pKeyItem = p.split('_');
				var iChecked = 0;
				for ( var k in pKeyItem) {

					var bFlagName = zapjs.f.exist('cshop_addproduct_pname_'
							+ pKeyItem[k]);

					if ($('#cshop_addproduct_prop_' + pKeyItem[k]).is(
							':checked')) {
						iChecked = iChecked + 1;

						if (!bFlagName) {
							var othisProp = cshop_product
									.up_prop_key(pKeyItem[k]);

							$('#cshop_addproduct_prop_' + pKeyItem[k])
									.next('label')
									.html(
											'<input onblur="cshop_product.blur_prop(\''
													+ pKeyItem[k]
													+ '\',this)" id="cshop_addproduct_pname_'
													+ pKeyItem[k]
													+ '" class="w_w_number" type="text" value="'
													+ othisProp["property_name"]
													+ '"/>');
						}

					} else {
						if (bFlagName) {
							var othisProp = cshop_product
									.up_prop_key(pKeyItem[k]);
							$('#cshop_addproduct_prop_' + pKeyItem[k]).next(
									'label').html(othisProp["property_name"]);
						}
					}

				}

				if (iChecked == pKeyItem.length) {
					cshop_product.temp.p_key[p]["flag"] = 1;
				} else {
					cshop_product.temp.p_key[p]["flag"] = 0;
				}

			}
		}

		cshop_product.recheck_prop();
		cshop_product.new_show_sku();
	},
	
	init_prop : function(oElm) {
		var sVal = $(oElm).val();

		var bFlagChecked = $(oElm).is(':checked') ? 1 : 0;

		for ( var p in cshop_product.temp.p_color) {
			if (p.indexOf(sVal) > -1) {
				cshop_product.temp.p_color[p]["flag"] = bFlagChecked;
			}
		}
		for ( var p in cshop_product.temp.p_key) {
			if (p.indexOf(sVal) > -1) {

				//var bCheckAll = false;

				var pKeyItem = p.split('_');
				var iChecked = 0;
				for ( var k in pKeyItem) {

					var bFlagName = zapjs.f.exist('cshop_addproduct_pname_'
							+ pKeyItem[k]);

					if ($('#cshop_addproduct_prop_' + pKeyItem[k]).is(
							':checked')) {
						iChecked = iChecked + 1;

						if (!bFlagName) {
							var othisProp = cshop_product
									.up_prop_key(pKeyItem[k]);

							$('#cshop_addproduct_prop_' + pKeyItem[k])
									.next('label')
									.html(
											'<input onblur="cshop_product.blur_prop(\''
													+ pKeyItem[k]
													+ '\',this)" id="cshop_addproduct_pname_'
													+ pKeyItem[k]
													+ '" class="w_w_number" type="text" value="'
													+ othisProp["property_name"]
													+ '"/>');
						}

					} else {
						if (bFlagName) {
							var othisProp = cshop_product
									.up_prop_key(pKeyItem[k]);
							$('#cshop_addproduct_prop_' + pKeyItem[k]).next(
									'label').html(othisProp["property_name"]);
						}
					}

				}

				if (iChecked == pKeyItem.length) {
					cshop_product.temp.p_key[p]["flag"] = 1;
				} else {
					cshop_product.temp.p_key[p]["flag"] = 0;
				}

			}
		}
	},

	blur_prop : function(sCode) {
		
		var sText=$('#cshop_addproduct_pname_'+sCode).val();
		if ('' == sText) {
			zapjs.f.message('属性值不能为空！');
			return false;
		}
		sText = cshop_product.replaceother(sText);
		cshop_product.temp.p_list[sCode]["property_name"]=sText;
		$('#cshop_addproduct_ctext_'+sCode).html(sText);
		$('#cshop_addproduct_pname_'+sCode).val(sText);
		
		cshop_product.recheck_prop();
		cshop_product.new_show_sku();
		//第一个ul为颜色属性
		var colorText = [];
		$("#cshop_addproduct_prop ul:first input[id^='cshop_addproduct_pname_']").each(function(){
			colorText.push($(this).val());
		});
		if (null != colorText && colorText.length > 0) {
			var nary=colorText.sort(); 
			for(var i=0;i<nary.length-1;i++){
				if (nary[i]==nary[i+1]) 
			       {
					zapjs.f.message('颜色：'+nary[i]+'出现重复！');
					return false;
			       } 
		    }
		}
		//第二个ul为款式属性
		var styleText = [];
		$("#cshop_addproduct_prop ul:last input[id^='cshop_addproduct_pname_']").each(function(){
			styleText.push($(this).val());
		});
		if (null != styleText && styleText.length > 0) {
			var nary=styleText.sort(); 
			for(var i=0;i<nary.length-1;i++){
				if (nary[i]==nary[i+1]) 
			       {
					zapjs.f.message('款式：'+nary[i]+'出现重复！');
					return false;
			       } 
		    }
		}
	},

	new_show_sku : function() {

		var sTargetUpload = $('#cshop_addproduct_uploadurl').val();

		for ( var p in cshop_product.temp.p_color) {
			var othis = cshop_product.temp.p_color[p];

			var flagExist = true;		//默认存在的颜色的图片不展示出来
			if (othis["flag"] != "1") {
				flagExist = false;
			}
			for (var p_ in cshop_product.temp.checkColorInit) {
				var checkColorP = cshop_product.temp.checkColorInit[p_];
				if(checkColorP["code"] == othis["p_code"]){
					flagExist = false;
					break;
				}
					
			}
			if (flagExist) {

				if (!zapjs.f.exist('cshop_addproduct_color_' + othis["p_code"])) {
					var aHtml = '<tr><td><label id="cshop_addproduct_ctext_' + othis["p_code"]+'">'
							+ othis["p_name"]
							+ '</label></td><td><input class="c_id" type="hidden" value="'
							+ p
							+ '" />'
							+ zapweb_upload
									.upload_html(sTargetUpload,
											'cshop_addproduct_color_'
													+ othis["p_code"], '',
											'')
							+ '</td></tr>';
					$('#cshop_addproduct_colors table tbody').append(aHtml);

					$('#cshop_addproduct_colors').show();
					// zapjs.d($('#cshop_addproduct_colors').html());

					zapweb_upload.upload_file('cshop_addproduct_color_'
							+ othis["p_code"]);
					var sText=$('#cshop_addproduct_pname_'+othis["p_code"]).val();
					sText = cshop_product.replaceother(sText);
					$('#cshop_addproduct_ctext_'+othis["p_code"]).html(sText);	//各颜色图片中显示的颜色文字与所选不一致问题
				}
			} else {
				$('#cshop_addproduct_color_' + othis["p_code"]).parent('td')
						.parent('tr').remove();
			}

		}

		$('#cshop_addproduct_plist table tbody').html('');

		for ( var p in cshop_product.temp.p_key) {
			var othis = cshop_product.temp.p_key[p];
			var flagExist = true;		//默认存在的sku不展示出来
			if (othis["flag"] != "1") {
				flagExist = false;
			}
			for (var skuList in cshop_product.temp.productSkuInfoList) {
				var skuList_ = cshop_product.temp.productSkuInfoList[skuList];
				if (skuList_["skuKey"] == othis["sku_key"]) {
					flagExist = false;
					break;
				}
			}
			if (flagExist) {
				var aHtml = [];

				aHtml.push('<tr>');

				var pKeyItem = p.split('_');

				var aSkuName = [];

				for ( var k=0,kl=pKeyItem.length;k<kl;k++) {
					
					var sPropKey=pKeyItem[k];
					
					//var sName = cshop_product.up_prop_key(pKeyItem[k])["property_name"];
					
					var sName = $('#cshop_addproduct_pname_'+sPropKey).val();
					
					aHtml.push('<td>' + sName + '</td>');

					aSkuName.push(sName);
				}

				var sSkuName = aSkuName.join(' ');
				aHtml.push('<td><input class="c_costPrice w_w_number" style="width:114px" onkeyup="cshop_product.checkNum(this);cshop_product.c_cost_onkeyup(this)" type="text"  zapweb_attr_regex_id="469923180007" zapweb_attr_regex_title="SKU'
						+ sSkuName
						+ '  成本价" value="'
						+ othis["costPrice"]
						+ '"/></td>');
				aHtml.push('<td><input class="c_id" type="hidden" value="'
								+ p
								+ '" /><input class="c_skuCode" type="hidden" value="'
								+ othis["skuCode"]
								+ '" /><input class="c_text" type="hidden" value="'
								+ othis["sku_value"]
								+ '" /><input class="c_name" type="hidden" value="'
								+ sSkuName
								+ '" /><input class="c_price w_w_number" style="width:114px" onkeyup="cshop_product.checkNum(this);cshop_product.c_price_onkeyup(this)" type="text"  zapweb_attr_regex_id="469923180007" zapweb_attr_regex_title="SKU'
								+ sSkuName + '  销售价" value="' + othis["price"]
								+ '"/></td>');
				aHtml.push('<td><input class="c_stock w_w_number" style="width:114px" onkeyup="cshop_product.checkout_number(this);cshop_product.c_price_onkeyup(this)"  type="text"  zapweb_attr_regex_id="469923180003" zapweb_attr_regex_title="SKU'
								+ sSkuName
								+ '  库存" value="'
								+ othis["stock"]
								+ '"/></td>');
				aHtml.push('<td><input class="c_securityStockNum w_w_number" style="width:114px" onkeyup="cshop_product.checkout_number(this)" type="text" value="'
								+ othis["securityStockNum"] + '"/></td>');
				aHtml.push('<td><input class="c_miniOrder w_w_number" style="width:114px" onkeyup="cshop_product.checkout_number(this)" type="text" value="'
						+ othis["miniOrder"] + '"/></td>');
				aHtml.push('<td><input class="c_sellProductcode" style="width:114px" type="text"  value="'
								+ othis["sellProductcode"] + '"/></td>');
				aHtml.push('<td><input class="c_skuAdv" type="text" value="'+ othis["skuAdv"] + '"/></td>');
				aHtml.push('<td><i class="sku-batch sku-batch-enable" onclick="cshop_product.show_group_operation(this)"></td>');
				aHtml.push('</tr>');

				$('#cshop_addproduct_plist table tbody').append(aHtml.join(''));

				$('#cshop_addproduct_plist').show();
				
				var input_c_price = $('#cshop_addproduct_plist table tbody tr:last').find(".c_price:first").val();
				var input_c_stock = $('#cshop_addproduct_plist table tbody tr:last').find(".c_stock:first").val();
				if(input_c_price=="" && input_c_stock==""){
					$(".sku-batch").attr("class","sku-batch");
					$(".sku-batch").removeAttr("onclick");
				}
			}

		}
	},
	//替换基本属性背的空格、=、&
	replaceother : function (str){
		var reg=new RegExp(" ","g");
		var reg1=new RegExp("=","g");
		var reg2=new RegExp("&","g");
		str=str.replace(reg,'');
		str=str.replace(reg1,'');
		str=str.replace(reg2,'');
		return str;
	},
	//输入的必须为数字（包括小数）
	checkout_decimal : function(obj){
		 //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	},
	//输入的必须为正整数
	checkout_number : function(obj){
		 //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d]/g,"");
	},
	
	showMd: function(){
		$("#MdTable").css("display","block");
	},
	uniqueArrSkuKey : function(arr){
		 var res = [];
		 var json = {};
		 for(var i = 0; i < arr.length; i++){
		  if(!json[arr[i].code]){
		   res.push({"code":arr[i].code,"value":arr[i].value,"colorPic":arr[i].colorPic});
		   json[arr[i].code] = 1;
		  }
		 }
		 return res;
	},
//	approveBtnClick : function (){
//		var bFlag = true;
//		
//		//遍历基本属性
//		//第一个ul为颜色属性
//		var bFlagColorChecked = 0;
//		$("#cshop_addproduct_prop ul:first input[id^='cshop_addproduct_prop_']").each(function(){
//			if ($(this).is(':checked')) {
//				bFlagColorChecked = 1
//				return false;
//			}
//		});
//		//第二个ul为款式属性
//		var bFlagStyleChecked = 0;
//		$("#cshop_addproduct_prop ul:last input[id^='cshop_addproduct_prop_']").each(function(){
//			if ($(this).is(':checked')) {
//				bFlagStyleChecked = 1
//				return false;
//			}
//		});
//		if (bFlagColorChecked == 0 && bFlagStyleChecked == 0) {
//			zapjs.f.message("您必须至少维护一个sku，请勾选sku基本属性！");
//			return false;
//		}else if (bFlagColorChecked == 1 && bFlagStyleChecked == 0) {
//			zapjs.f.message("您没有选择sku基本属性中的款式！");
//			return false;
//		}else if (bFlagColorChecked == 0 && bFlagStyleChecked == 1) {
//			zapjs.f.message("您没有选择sku基本属性中的颜色！");
//			return false;
//		}
//		
//		if (bFlag) {
//			cshop_product.show_windows('mdFrame');
//		}
//		return bFlag;
//	},
//	saveDraftBoxBtnClick : function (){
//		
//		$("form input").each(function(){
//			$(this).removeAttr("zapweb_attr_regex_id");
//		});
//		$("#draftBoxBtnSubmit").click();
//	},
	
	show_group_operation : function(nowTr){
		var isFirefox=navigator.userAgent.toUpperCase().indexOf("FIREFOX")?true:false; 
		if(isFirefox)//兼容火狐浏览器
	     {
	         var $E = function(){var c=$E.caller; while(c.caller)c=c.caller; return c.arguments[0]};
	         __defineGetter__("event", $E);
	     }
		event.stopPropagation();//阻止事件冒泡
		$('#group_operation').toggle();//再点击设置按钮变为不可见
		var init_post = $(nowTr).offset().top;
		var init_posl = $(nowTr).offset().left;
		var init_div =$("#group_operation");
		init_div.css({"top":init_post-init_div.height()+160+'px',"left":init_posl-(init_div.width()/2)-150+'px'});
		var tr = $(nowTr).parents("tr");
		var tds = tr.children();
		cshop_product.temp.copy_tr.vPrice=tr.find(".c_price:first").val();
		cshop_product.temp.copy_tr.vNum=tr.find(".c_stock:first").val();
		cshop_product.temp.copy_tr.vCost=tr.find(".c_costPrice:first").val();
		cshop_product.temp.copy_tr.color = tds[0].innerHTML;
		cshop_product.temp.copy_tr.size = tds[1].innerHTML;
		if(cshop_product.temp.copy_tr.vCost==""){
			$('#batch-cost-0').prop("disabled",true);
			$('#batch-cost-1').prop("disabled",true);
		}else{
			$('#batch-cost-0').prop("disabled",false);
			$('#batch-cost-1').prop("disabled",false);
		}
		if(cshop_product.temp.copy_tr.vPrice==""){
			$('#batch-price-0').prop("disabled",true);
			$('#batch-price-1').prop("disabled",true);
		}else{
			$('#batch-price-0').prop("disabled",false);
			$('#batch-price-1').prop("disabled",false);
		}
		if(cshop_product.temp.copy_tr.vNum==""){
			$('#batch-quantity-0').prop("disabled",true);
			$('#batch-quantity-1').prop("disabled",true);
		}else{
			$('#batch-quantity-0').prop("disabled",false);
			$('#batch-quantity-1').prop("disabled",false);
		}
	},
	
	onclick_ok : function(){
		//同颜色分类成本价相同
		if($("#batch-cost-0").is(":checked")){
			var colors = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				colors[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[0].innerHTML;
			}
			for(var i=0;i<colors.length;i++){
				if(cshop_product.temp.copy_tr.color==colors[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_costPrice:first").val(cshop_product.temp.copy_tr.vCost);
				}
			}
		}
		
		//同款式成本价相同
		if($("#batch-cost-1").is(":checked")){
			var size = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				size[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[1].innerHTML;
			}
			for(var i=0;i<size.length;i++){
				if(cshop_product.temp.copy_tr.size==size[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_costPrice:first").val(cshop_product.temp.copy_tr.vCost);
				}
			}
		}
		
		//同颜色价格相同
		if($("#batch-price-0").is(":checked")){
			var colors = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				colors[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[0].innerHTML;
			}
			for(var i=0;i<colors.length;i++){
				if(cshop_product.temp.copy_tr.color==colors[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_price:first").val(cshop_product.temp.copy_tr.vPrice);
				}
			}
		}
		
		//同款式价格相同
		if($("#batch-price-1").is(":checked")){
			var size = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				size[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[1].innerHTML;
			}
			for(var i=0;i<size.length;i++){
				if(cshop_product.temp.copy_tr.size==size[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_price:first").val(cshop_product.temp.copy_tr.vPrice);
				}
			}
		}
		
		//同颜色库存相同
		if($("#batch-quantity-0").is(":checked")){
			var colors = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				colors[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[0].innerHTML;
			}
			for(var i=0;i<colors.length;i++){
				if(cshop_product.temp.copy_tr.color==colors[i]){
					
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_stock:first").val(cshop_product.temp.copy_tr.vNum);
				}
			}
		}
		
		//同款式库存相同
		if($("#batch-quantity-1").is(":checked")){
			var size = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				size[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[1].innerHTML;
			}
			for(var i=0;i<size.length;i++){
				if(cshop_product.temp.copy_tr.size==size[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_stock:first").val(cshop_product.temp.copy_tr.vNum);
				}
			}
		}
		//遍历所有的行，如果复制了之后有了数据则按钮变亮，复制库存
		$("#cshop_addproduct_plist table tbody .c_stock").each(function(){
			cshop_product.c_price_onkeyup(this);
		});
		//遍历所有的行，如果复制了之后有了数据则按钮变亮，复制价格
		$("#cshop_addproduct_plist table tbody .c_price").each(function(){
			cshop_product.c_price_onkeyup(this);
		});
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	
	onclick_cancle : function(){
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	
	onclick_x : function(){
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	
	//当输入成本价的输入框时校验
	c_cost_onkeyup :function(text){
		var cost = $(text).val();
		var price = $(text).parents("tr").find(".c_price:first").val();
		var stock = $(text).parents("tr").find(".c_stock:first").val();
		if(price=="" && stock=="" && cost==""){
			$(text).parents("tr").find(".sku-batch:first").removeAttr("onclick");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch");
		}else{
			$(text).parents("tr").find(".sku-batch:first").attr("onclick","cshop_product.show_group_operation(this)");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch sku-batch-enable");
		}
	},
	//当输入售价的输入框时校验
	c_price_onkeyup : function(text){
		var price = $(text).val();
		var stock = $(text).parents("tr").find(".c_stock:first").val();
		if(price=="" && stock==""){
			$(text).parents("tr").find(".sku-batch:first").removeAttr("onclick");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch");
		}else{
			$(text).parents("tr").find(".sku-batch:first").attr("onclick","cshop_product.show_group_operation(this)");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch sku-batch-enable");
		}
	},
	//当输入库存的输入框时校验
	c_stock_onkeyup : function(text){
		var stock = $(text).val();
		var price = $(text).parents("tr").find(".c_price:first").val();
		if(price=="" && stock==""){
			$(text).parents("tr").find(".sku-batch:first").removeAttr("onclick");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch");
		}else{
			$(text).parents("tr").find(".sku-batch:first").attr("onclick","cshop_product.show_group_operation(this)");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch sku-batch-enable");
		}
	}

};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modAddSkuProductBatch", [ "zapweb/js/zapweb_upload",
			"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
		return cshop_product;
	});
}

//改变窗口大小时，弹出的批量设置隐藏
$(window).resize(function(){
	document.getElementById("group_operation").style.display="none";
});

//点击弹出的批量设置以外的地方时隐藏弹出层
$("body").click(function(e){
	var e = e || window.event; //浏览器兼容性 
	var elem = e.target || e.srcElement; 
	while (elem) { //循环判断至跟节点，防止点击的是div子元素 
	if (elem.id && elem.id=='group_operation') { 
	return; 
	} 
	elem = elem.parentNode; 
	} 

	$('#group_operation').css('display','none');
	$(".batch-radio").prop("checked",false);
});

$(document).ready(function(){
	$("#zw_f_listbox").change(function(){
		cshop_product.set_category();//设置分类属性
	});
	$(".ico-helpw .ico-fques").mouseover(function(){
		$(this).attr("class","poptip-attention poptip-attention-active");
		var init_post = $(this).offset().top;
		var init_posl = $(this).offset().left;
		var init_div =$(this).parent('.ico-helpw').find('.help-pop');
		init_div.css({"top":init_post-init_div.height()+'px',"left":init_posl-(init_div.width()/2)+4+'px',"display":"block"});
	});
	$(".ico-helpw .ico-fques").mouseout(function(){
		$(this).attr("class","poptip-attention");
		$(this).parent('.ico-helpw').find('.help-pop').css('display','none');
	});
});
