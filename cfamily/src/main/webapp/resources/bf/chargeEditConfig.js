$(function() {
	//绑定下拉框事件
	bindSellerTypeChange();
	
	var zw_f_charge_name = $('#zw_f_charge_name');
	zw_f_charge_name.empty();
	zw_f_charge_name.html('<option value="">请选择佣金类型</option>');
});

function bindSellerTypeChange() {
	var zw_f_charge_name = $('#zw_f_charge_name');
	zw_f_charge_name.empty();
	
	var selectChar = '<option value="">请选择佣金类型</option>';
	var  sellerType = $('#zw_f_seller_type').val();
	if(sellerType == '449748070001') {
		//自营代销
		 selectChar += '<option value="449748060001">自营代销在多彩宝销售佣金百分比</option>';
	}else if(sellerType == '449748070002') {
		//自营缤纷
		selectChar += '<option value="449748060002">自营缤纷在多彩宝销售佣金百分比</option>';
		selectChar += '<option value="449748060003">自营缤纷在惠家有销售佣金百分比</option>';
	}
	zw_f_charge_name.html(selectChar);
}