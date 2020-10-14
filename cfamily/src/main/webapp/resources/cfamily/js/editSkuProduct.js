var editSkuProduct = {
	picUrl : '',
	// 初始化
		init_editSku : function(product_name) {
		
		document.getElementById("zw_f_security_stock_num").setAttribute("onkeyup","editSkuProduct.checkout_number(this)");//预警库存
		document.getElementById("zw_f_security_stock_num").setAttribute("zapweb_attr_regex_id","");//绕过框架的验证
		
		//sku名称设置为商品名称
		$("label[for='zw_f_sku_name']").html("<span class='w_regex_need'>*</span>商品名称：");
		$("input[name='zw_f_sku_name']").val(product_name)
		$('#zw_f_uid').parent().prepend('<input type="hidden" id="zw_f_picEdit_flag" name="zw_f_picEdit_flag" value="0">')
		picUrl  = $('#zw_f_sku_picurl').val();
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editSkuProduct.editBeforesubmit);
		$('#zw_f_sku_name').parent().append('<input type="checkbox" name = "zw_f_sel" id="zw_f_sel" value="1"/>修改并应用到相同颜色的SKU上')
	},
	// 提交前处理一些特殊的验证模式
	editBeforesubmit : function() {
		var zw_f_security_stock_num = $("#zw_f_security_stock_num").val();	//预警库存
		var zw_f_sellPrice = $("#zw_f_sell_price").val();	//销售价格
		var zw_f_mini_order = $("#zw_f_mini_order").val();	//起订数量
		
		var picUrl1 =  $('#zw_f_sku_picurl').val();
		if(picUrl1!=picUrl){
			$('#zw_f_picEdit_flag').val('1')
		}
		
		//防止与modSkuProduct提交前处理重复执行，可根据销售价格来判断，如果值为空，则是进入”修改界面“否则为”销售价、库存修改 “界面
		if (null != zw_f_sellPrice && "" != zw_f_sellPrice) {	
			return true;
		}
		 if (null == zw_f_security_stock_num || "" == zw_f_security_stock_num) {
				zapjs.f.message("安全库存不能为空");
				return false;
		}
		//预警库存位数不能超过8位
		if (zw_f_security_stock_num.length > 8) {
			zapjs.f.message("*安全库存：安全库存位数超长，最长为8位");
			return false;
		}
		
		//起订数量必须大于0
		if (eval(zw_f_mini_order) <= eval(0)) {
			zapjs.f.message("起订数量：起订数量必须大于0");
			return false;
		}
		if (zw_f_mini_order.length>8) {
			zapjs.f.message('起订数量：长度超过限制，最长为8位');
			return false;
		};
		return true;
	},
	//输入的必须为正整数
	checkout_number : function(obj){
		 //先把非数字的都替换掉，除了数字和.
       obj.value = obj.value.replace(/[^\d]/g,"");
	}
};
	
if (typeof define === "function" && define.amd) {
	define("cfamily/js/editSkuProduct", [], function() {
		return editSkuProduct;
	});
};
