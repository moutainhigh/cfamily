var cshop_modAddSkuProduct = {
	product : {},
	temp : {
		// 分类属性
		categoryproperty : [],
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
	},
	// 初始化
	init_product : function(str,ptr) {
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',cshop_modAddSkuProduct.beforesubmit);
		cshop_modAddSkuProduct.initCategoryProperty(str,ptr);
	},

	initCategoryProperty : function(str,ptr) {
		zapjs.zw.api_call('com_cmall_productcenter_process_ApiGetCategoryPropertyForSku', {categoryCode : str,productCode : ptr}, cshop_modAddSkuProduct.prop_list_success);
	},
	
	prop_list_success : function(oData) {
		cshop_modAddSkuProduct.temp.proplist = oData.listProperty;
		for ( var i in cshop_modAddSkuProduct.temp.proplist) {
			cshop_modAddSkuProduct.temp.p_list[cshop_modAddSkuProduct.temp.proplist[i]["property_code"]] = cshop_modAddSkuProduct.temp.proplist[i];
		}

		var sprop = cshop_modAddSkuProduct.temp.p_list;
		// 颜色属性
		var aColor = [];
		// 基本属性
		var aKey = [];
		for ( var p in sprop) {
			// 判断如果是主属性
			if (p.length == cshop_modAddSkuProduct.temp.fatherlength) {
				var iType = cshop_modAddSkuProduct.up_type_bycode(p);
				if (iType == 1) {
					aColor.push(p);
					aKey.push(cshop_modAddSkuProduct.up_child(p));
				} else if (iType == 2) {
					aKey.push(cshop_modAddSkuProduct.up_child(p));
				}
			}
		}
		// 如果超过一个颜色 则返回错误
		if (aColor.length > 1) {
			zapjs.f.message('该分类下颜色属性超过两个，请联系技术解决。谢谢！' + aColor);
			return false;
		}
		var aBaseHtml = [];
		for ( var i in aKey) {
			var oFather = cshop_modAddSkuProduct.up_prop_key(aKey[i][0]["parent_code"]);
			aBaseHtml.push('<div class="control-group"><label class="control-label" for="zw_f_ppp'+i+'"><span class="w_regex_need">*</span>'+oFather["property_name"]+'：</label><div class="controls">');
			aBaseHtml.push('<input type="hidden" id="zw_f_skukey_'+i+'_parent" value="'+oFather["property_name"]+'&'+oFather["property_code"]+'" />');
			aBaseHtml.push('<select id="zw_f_skukey_'+i+'" onchange="cshop_modAddSkuProduct.changeValue(this)" ><option selected="selected" value="">请选择</option>');
			for ( var k in aKey[i]) {
				var oItem = aKey[i][k];
				aBaseHtml.push('<option value="' + oItem["property_code"] + '">'+ oItem["property_name"] + '</option>');
			}
			aBaseHtml.push('</select><input id="zw_f_skukey_'+i+'_true" type="text" onblur="cshop_modAddSkuProduct.checkValue(this)" value="" name="zw_f_skukey_'+i+'_true" zapweb_attr_regex_id="469923180002" /></div></div>');
		}
		
		var sTargetUpload = $('#cshop_addproduct_uploadurl').val();
		aBaseHtml.push('<div id="cshop_addproductsku_colors" class="control-group"><label class="control-label" id="cshop_addproduct_ctext">图片：</label><div class="controls">'+ zapweb_upload.upload_html(sTargetUpload,'zw_f_skuPicUrl', '','')+'</div>');
		if (aBaseHtml.length > 0 ){
			$('#hualidefenggexian').after(aBaseHtml.join(""));
		}
		$('#cshop_addproductsku_colors').show();
		zapweb_upload.upload_file('zw_f_skuPicUrl');
	},
	
	//替换字符串中的空格、=、&
	checkValue : function(odata){
		var str = odata.value;
		var reg=new RegExp(" ","g");
		var reg1=new RegExp("=","g");
		var reg2=new RegExp("&","g");
		str=str.replace(reg,'');
		str=str.replace(reg1,'');
		str=str.replace(reg2,'');
		$('#'+odata.id).val(str);
	},
	
	changeValue : function(bq){
		if(bq.value!=""){
			var text= document.getElementById(bq.id).options[document.getElementById(bq.id).selectedIndex].text;
			$('#'+bq.id+'_true').val(text);
			//判断该商品下是否存在包含相同的颜色属性与规格属性的sku
			var productCode = $("#zw_f_productCode").val();
			var colorPro = $("#zw_f_skukey_0").val();
			var stylePro = $("#zw_f_skukey_1").val();
			var colorProName = $("#zw_f_skukey_0_true").val();
			var styleProName = $("#zw_f_skukey_1_true").val();
			
			//查询是否存在重复sku属性
			if (("" != colorPro && "" != stylePro) || ("" != colorProName && "" != styleProName)) {
				var obj = {};
				obj.productCode = productCode;
				obj.colorPro = colorPro;
				obj.stylePro = stylePro;
				obj.colorProName = colorProName;
				obj.styleProName = styleProName;
				zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiCheckRepeatSkuPro',obj,function(result) {
					if(result.flag==1){		//存在重复的sku规格型号，给予提示
						zapjs.f.message("商品下已经存在“"+$("#zw_f_skukey_0").find("option:selected").text()+"&"+$("#zw_f_skukey_1").find("option:selected").text()+"”,请选择其他组合！");
					}else if(result.flag==2){		//存在重复的sku规格型号名称，给予提示
						zapjs.f.message("商品下已经存在“"+$("#zw_f_skukey_0").find("option:selected").text()+"&"+$("#zw_f_skukey_1").find("option:selected").text()+"”,请重命名后重试！");
					}
				});
			}
		}else{
			$('#'+bq.id+'_true').val("");
		}
	},
	
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		var zw_f_skuKey = "";
		var zw_f_skuValue = "";
		var zw_f_sellPrice = $("#zw_f_sellPrice").val();	//销售价格
		var zw_f_stockNum =$("#zw_f_stockNum").val();			//	库存数量
		var zw_f_securityStockNum = $("#zw_f_securityStockNum").val();	//预警库存

		if (null == zw_f_stockNum || "" == zw_f_stockNum) {
			zapjs.f.message("库存数量不能为空");
			return false;
		}else if (null == zw_f_securityStockNum || "" == zw_f_securityStockNum) {
			zapjs.f.message("预警库存不能为空");
			return false;
		}
		//销售价格的个位不能超过10位
		if (zw_f_sellPrice.substring(0 , zw_f_sellPrice.indexOf(".") == -1 ? zw_f_sellPrice.length :  zw_f_sellPrice.indexOf(".") ).length > 10) {
			zapjs.f.message("*销售价格：值过大，不符合规则，小数点前最长为10位");
			return false;
		}
		//库存数量位数不能超过8位
		if (zw_f_stockNum.length > 8) {
			zapjs.f.message("*库存数量：库存数量位数超长，最长为8位");
			return false;
		}
		//预警库存位数不能超过8位
		if (zw_f_securityStockNum.length > 8) {
			zapjs.f.message("*预警库存：预警库存位数超长，最长为8位");
			return false;
		}
		
		for(var i = 0;i>=0;i++){
			if(zapjs.f.exist('zw_f_skukey_'+i)){
				var parentValue = $('#zw_f_skukey_'+i+'_parent').val().split('&')[0];
				var parentKey = $('#zw_f_skukey_'+i+'_parent').val().split('&')[1];
				if($('#zw_f_skukey_'+i+'_true').val()==null|| $('#zw_f_skukey_'+i+'_true').val()==''||$('#zw_f_skukey_'+i).val()==''||$('#zw_f_skukey_'+i).val()==null){
					zapjs.f.message(parentValue+"不能为空");
					return false;
				}else{
					if(i==0){
						zw_f_skuKey=parentKey+'='+$('#zw_f_skukey_'+i).val();
						zw_f_skuValue=parentValue+'='+$('#zw_f_skukey_'+i+'_true').val();
					}else{
						zw_f_skuKey+='&'+parentKey+'='+$('#zw_f_skukey_'+i).val();
						zw_f_skuValue+='&'+parentValue+'='+$('#zw_f_skukey_'+i+'_true').val();
					}
				}
			}else{
				break;
			}
		}
		$('#zw_f_skuKey').val(zw_f_skuKey);
		$('#zw_f_skuValue').val(zw_f_skuValue);
		return true;
	},

	// 根据key获取属性
	up_prop_key : function(sCode) {
		return cshop_modAddSkuProduct.temp.p_list[sCode];
	},
	// 获取子属性
	up_child : function(sCode) {
		var oRet = [];
		var sprop = cshop_modAddSkuProduct.temp.p_list;
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
	//校验只能输入数字和两位小数 
	checkNum : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,2})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
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
	}
	
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modAddSkuProduct", [ "zapweb/js/zapweb_upload",
			"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
		return cshop_modAddSkuProduct;
	});
}
