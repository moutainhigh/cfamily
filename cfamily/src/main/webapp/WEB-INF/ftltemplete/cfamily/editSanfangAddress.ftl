<#-- 修改页 -->
<#assign upData = b_page.upOneData()>
<#assign jdOrder = b_method.upDataOneOutNull("oc_order_jd","*","","","order_code", upData.hjy_order_code)>
<#assign orderAdress = b_method.upDataOneOutNull("oc_orderadress","*","","","order_code", upData.hjy_order_code)>
<form class="form-horizontal" method="POST" >
	<input name="orderCode" type="hidden" value="${upData.hjy_order_code}">
	<div class="control-group">
		<label class="control-label" for="zw_f_page_code"><span class='w_regex_need'>*</span>订单号：</label>
		<div class="controls">
			<input type="text" name=""  zapweb_attr_regex_id="469923180002"  value="${upData.hjy_order_code}" disabled>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_page_code"><span class='w_regex_need'>*</span>收货人手机号：</label>
		<div class="controls">
			<input type="text" name=""  zapweb_attr_regex_id="469923180002"  value="${orderAdress.mobilephone}" disabled>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_page_code"><span class='w_regex_need'>*</span>收货人：</label>
		<div class="controls">
			<input type="text" name=""  zapweb_attr_regex_id="469923180002"  value="${orderAdress.receive_person}" disabled>
		</div>
	</div>		
	<div class="control-group">
		<label class="control-label" for="zw_f_page_code"><span class='w_regex_need'>*</span>详细地址：</label>
		<div class="controls">
			<input type="text" name="address"  zapweb_attr_regex_id="469923180002"  value="${orderAdress.address}">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_page_code"><span class='w_regex_need'>*</span>详细地址：</label>
		<div class="controls">
	      		<select style="width:120px" name="province" id="zw_f_province">
						<option value="">请选择</option>
	      		</select>
	      		<select style="width:120px" name="city" id="zw_f_city">
						<option value="">请选择</option>
	      		</select>
	      		<select style="width:120px" name="county" id="zw_f_county">
						<option value="">请选择</option>
	      		</select>
	      		<select style="width:120px" name="town" id="zw_f_town">
						<option value="">请选择</option>
	      		</select>		      			      			      		
		</div>
	</div>		
	<div class="control-group">
    	<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="3d07e54c2a3144e3953aee3fc1d4034a"  onclick="zapjs.zw.func_add(this)"  value="提交" />
    	</div>
	</div>	
</form>

<script>
require(['cfamily/js/selectJdArea'],

function()
{
	zapjs.f.ready(function()
		{
			selectJdArea.init({
				provinceId: 'zw_f_province',
				provinceValue: '${jdOrder.province}',
				cityId: 'zw_f_city',
				cityValue: '${jdOrder.city}',
				countyId: 'zw_f_county',
				countyValue: '${jdOrder.county}',
				townId: 'zw_f_town',
				townValue: '${jdOrder.town}'
			});
		}
	);
}

);

// 重写函数的执行成功回调
zapjs.zw.func_success = function(o) {
	if (o.resultCode == "1") {
		if (o.resultMessage == "") {
			o.resultMessage = "操作成功";
		}

		//	刷新父级页面
		if(parent){
			zapjs.zw.modal_show({
				content : o.resultMessage,
				okfunc : 'parent.zapjs.f.autorefresh()'
			});			
		}else{
			zapjs.zw.modal_show({
				content : o.resultMessage,
				okfunc : 'zapjs.f.autorefresh()'
			});			
		}
	} else {
		zapjs.zw.modal_show({
			content : o.resultMessage
		});
	}
}
</script>