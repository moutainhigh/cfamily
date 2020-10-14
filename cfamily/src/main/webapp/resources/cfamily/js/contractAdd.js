var contractAdd = {
		init : function(){
			$("#zw_f_contract_type").prepend('<option value="" selected>请选择</option>');
			contractAdd.contractTypeChange();
			$("#zw_f_contract_type").attr("zapweb_attr_regex_id","469923180002");
			$("#zw_f_start_time").attr("zapweb_attr_regex_id","469923180002");
			$("#zw_f_end_time").attr("zapweb_attr_regex_id","469923180002");
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',contractAdd.beforeSubmit);
		},
		contractTypeChange: function(){//只有选择解除协议的时候才显示说明和日期
			var contractType = $("#zw_f_contract_type").find(
			"option:selected").text();
			if (contractType == "代销补充协议" || contractType == "经销补充协议") {
				$("#addPro").show();
				$("#prod").show();
			}else{
				$("#addPro").hide();
				$("#prod").hide();
			}
			if (contractType == "解除协议") {
				$("#zw_f_dissolution_time").parent().parent().show();
				$("#zw_f_dissolution_instructions").parent().parent().show();
				$("#zw_f_dissolution_time").attr("zapweb_attr_regex_id","469923180002");//解除协议时间设为必填
			}else{
				$("#zw_f_dissolution_time").parent().parent().hide();
				$("#zw_f_dissolution_instructions").parent().parent().hide();
				$("#zw_f_dissolution_time").removeAttr("zapweb_attr_regex_id");//解除协议时间设为非必填
			}
		},
		show_windows : function(){
			zapjs.f.window_box({
				id : 'as_productlist_productids',
				content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.contractAdd.setProductInfor" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	setProductInfor : function(sId, sVal,a,b,c){
		var opt ={};
		opt.productCode = sVal;
		zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductSimpleInfor', opt, contractAdd.up_product_info);
	},
	up_product_info : function(oResult) {
		$("#prod").remove();
		var pi = oResult.productMap;
		$("#zw_f_product_code").val(pi.product_code);
		var costPrice = "";
		var taxRate = "";
		var dlrId = "";
		var dlrName = "";
		var bankAccount = "";
		var branchName = "";
		var qualityRetentionMoney = "";
		var mdName = "";
		var tax = "";
		var noTaxPrice = "";
		var settlementType = "";
		if(pi.cost_price != null){
			costPrice = pi.cost_price;
		}
		if(pi.tax_rate != null){
			taxRate = pi.tax_rate*100+"%";
		}else{
			taxRate = "0%";
		}
		if(pi.tax != null){
			tax = pi.tax;
		}
		if(pi.noTaxPrice != null){
			noTaxPrice = pi.noTaxPrice;
		}
		if(pi.dlr_id != null){
			dlrId = pi.dlr_id;
		}
		if(pi.seller_name != null){
			dlrName = pi.seller_name;
		}
		if(pi.bank_account != null){
			bankAccount = pi.bank_account;
		}
		if(pi.branch_name != null){
			branchName = pi.branch_name;
		}
		if(pi.quality_retention_money != null){
			qualityRetentionMoney = pi.quality_retention_money;
		}
		if(pi.md_name != null){
			mdName = pi.md_nm;
		}
		if(pi.define_name != null){
			settlementType = pi.define_name;
		}
		$("#addPro")
				.after(
						'<div id="prod"><table  class="table  table-condensed table-striped table-bordered table-hover"><thead><tr> <th>商品编号</th><th>商品名称</th><th>SKU</th> </tr></thead>'
						+ '<tbody><tr><td>'+pi.product_code+'</td><td>'+pi.product_name+'</td><td><a target="_blank" href="../page/page_book_v_cf_pc_skuDetailInfo?zw_f_uid='+pi.uid+'">查看SKU</a></td></tr></tbody></table>'
						+ '<div class="control-group"><label class="control-label" for="">采购类型：</label><div class="controls"><div class="control_book"></div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">毛利率：</label><div class="controls"><div class="control_book">'+pi.grossProfit*100+'%</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">进价税率：</label><div class="controls"><div class="control_book">'+taxRate+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">不含税进价：</label><div class="controls"><div class="control_book">'+noTaxPrice+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">进价：</label><div class="controls"><div class="control_book">'+costPrice+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">进价税额：</label><div class="controls"><div class="control_book">'+tax+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">商品结算方式：</label><div class="controls"><div class="control_book">'+settlementType+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">供应商编号：</label><div class="controls"><div class="control_book">'+dlrId+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">供应商名称：</label><div class="controls"><div class="control_book">'+dlrName+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">开户行名称：</label><div class="controls"><div class="control_book">'+branchName+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">银行账号：</label><div class="controls"><div class="control_book">'+bankAccount+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">最大质保金：</label><div class="controls"><div class="control_book">'+qualityRetentionMoney+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">MD名称：</label><div class="controls"><div class="control_book">'+mdName+'</div></div></div>'
						+ '<div class="control-group"><label class="control-label" for="">赠品：</label><div class="controls"><div class="control_book">'+pi.gift+'</div></div></div></div>');
		zapjs.f.window_close('as_productlist_productids');
	},
	beforeSubmit : function() {
		var contractCode = $("#zw_f_contract_code").val();
		var contractName = $("#zw_f_contract_name").val();
		var boxNumber = $("#zw_f_contract_box_number").val();
		var boxMoney = $("#zw_f_contract_box__money").val();
		var advancePayment = $("#zw_f_advance_payment_percent").val();
		var secondMoney = $("#zw_f_second_money_percent").val();
		var lastMoney = $("#zw_f_last_money_percent").val();
		var specialTerms = $("#zw_f_special_terms").val();
		if(contractCode !="" && contractCode.length > 50){
			zapjs.f.message("合同编号不能大于50个字符！");
			return false;
		}
		if(contractName !="" && contractName.length > 50){
			zapjs.f.message("合同名称不能大于50个字符！");
			return false;
		}
		if(boxNumber !="" && boxNumber.length > 50){
			zapjs.f.message("合约箱号不能大于50个字符！");
			return false;
		}
		if(boxMoney !="" && boxMoney.length > 50){
			zapjs.f.message("合约包装箱费不能大于50个字符！");
			return false;
		}
		if(advancePayment !="" && advancePayment.length > 50){
			zapjs.f.message("合同预付款比例不能大于50个字符！");
			return false;
		}
		if(secondMoney !="" && secondMoney.length > 50){
			zapjs.f.message("合同第二笔款比例不能大于50个字符！");
			return false;
		}
		if(lastMoney !="" && lastMoney.length > 50){
			zapjs.f.message("合同尾款比例不能大于50个字符！");
			return false;
		}
		if(specialTerms !="" && specialTerms.length > 100){
			zapjs.f.message("合特殊约定不能大于100个字符！");
			return false;
		}
		if ($("#zw_f_contract_type").find("option:selected").text() == "代销补充协议"
				|| $("#zw_f_contract_type").find("option:selected").text() == "经销补充协议") {
			if($("#zw_f_product_code").val()==""){
				zapjs.f.message("此合同类型必须选择商品！");
				return false;
			}
		}else{
			$("#zw_f_product_code").val("");
		}
		return true;
	}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/contractAdd", ["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return contractAdd;
	});
}
$(document).ready(function(){
	$("#zw_f_contract_type")
	.change(
			function() {
				contractAdd.contractTypeChange();
			});
});