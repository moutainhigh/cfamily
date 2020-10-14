var contractBook = {
		init : function(){
			if($("#contractType").val()!="解除协议"){
				$("#jcrq").hide();
				$("#jcsm").hide();
			}
			contractBook.setProductInfor($("#productCode").val());
		},
		setProductInfor : function(sVal){
			var opt ={};
			opt.productCode = sVal;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductSimpleInfor', opt, contractBook.up_product_info);
		},
		up_product_info : function(oResult) {
			var pi = oResult.productMap;
			var tax = "";
			var noTaxPrice = "";
			var gift="";
			if(pi.tax != "undefined" && pi.tax != null){
				tax = pi.tax;
				$("#costTax").html(tax);//进价税额
			}
			if(pi.tax_rate != "undefined" && pi.tax_rate != null){
				taxRate = pi.tax_rate*100+"%";
				$("#taxRate").html(taxRate);//进价税率
			}
			if(pi.noTaxPrice != "undefined" && pi.noTaxPrice != null){
				noTaxPrice = pi.noTaxPrice;
				$("#nt").html(noTaxPrice);//不含税进价
			}
			if(pi.gift != "undefined" && pi.gift != null){
				gift = pi.gift;
				$("#gift").html(gift);//赠品
			}
			if(pi.grossProfit != "undefined" && pi.gift != null){
				$("#grossProfit").html(pi.grossProfit*100+"%");//毛利率
			}
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/contractBook", ["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return contractBook;
	});
}