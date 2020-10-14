var modSkuStock = {
		
		init_sku:function(){
			document.getElementById("zw_f_sell_price").setAttribute("onkeyup","modSkuStock.checkout_decimal(this)");//销售价格

			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',modSkuStock.modBeforesubmit);
		},
		// 提交前处理一些特殊的验证模式
		modBeforesubmit : function() {
			var zw_f_sellPrice = $("#zw_f_sell_price").val();	//销售价格
			
			//防止与editSkuProduct冲突，可根据销售价格来判断，如果值为空，则是进入”修改界面“否则为”销售价、库存修改 “界面
			if (null == zw_f_sellPrice || "" == zw_f_sellPrice) {	
				return true;
			}
//			var zw_f_stock_num =$(".zw_f_stock_num").val();			//	增加或减少的库存数量
			//销售价格的个位不能超过10位
			if (zw_f_sellPrice.substring(0 , zw_f_sellPrice.indexOf(".") == -1 ? zw_f_sellPrice.length :  zw_f_sellPrice.indexOf(".") ).length > 10) {
				zapjs.f.message("*销售价格：值过大，不符合规则，小数点前最长为10位");
				return false;
			}
			//库存变化量值过大，最长为8位
//			if (zw_f_stock_num.length > 8) {
//				zapjs.f.message("库存变化量值过大，最长为8位");
//				return false;
//			}
//			库存变化量不能为空
//			if (null == zw_f_stock_num || "" == zw_f_stock_num) {
//				zapjs.f.message("库存变化量不能为空");
//				return false;
//			}
			return true;
		},
		//输入的必须为正整数
		checkout_number : function(obj){
			 //先把非数字的都替换掉，除了数字和.
	        obj.value = obj.value.replace(/[^\d]/g,"");
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
		}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modSkuStock", function() {
		return modSkuStock;
	});
}
