$(function() {
		
});

function addColumn() {
	var chargeConfigDiv = $('#chargeConfigDiv');
	var chargeDiv = chargeConfigDiv.find('.chargeDiv')[0];
	var oldChar = $(chargeDiv).html();
	var newChar = '<div class="control-group">' + 
					  '<div class="chargeDiv" style="float: left;">' + oldChar + '</div>' + 
					  '<div style="float: left;">' + 
					  	'<a class="btn btn-link" href="javascript:void(0);" onclick="delColumn(this);">删除</a>' + 
					  '</div>' + 
				  '</div>';
	chargeConfigDiv.append(newChar);
}

function delColumn(scope) {
	var delDiv = $(scope).parent().parent()[0];
	$(delDiv).remove();
}

function sellerTypeChange(sellerTypeDom) {
	var zw_f_charge_name = $(sellerTypeDom).parent().find('[name="zw_f_charge_name"]');
	zw_f_charge_name.empty();
	
	var selectChar = '<option value="">请选择佣金类型</option>';
	var sellerType = $(sellerTypeDom).val();
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