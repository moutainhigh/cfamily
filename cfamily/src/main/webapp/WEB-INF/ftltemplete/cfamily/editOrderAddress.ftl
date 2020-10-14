<#-- 修改页 -->
<#assign orderCode = b_page.getReqMap()["orderCode"]! >
<#assign chinaAreaService = b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<#assign orderEditService = b_method.upClass("com.cmall.familyhas.service.OrderEditService")>
<#assign orderInfoMap = b_method.upDataOneOutNull("oc_orderinfo","","","","order_code",orderCode)>
<#assign orderAddressMap = b_method.upDataOneOutNull("oc_orderadress","","","","order_code",orderCode)>
<#assign areaMap = chinaAreaService.getFullCode(orderAddressMap["area_code"])>
<form class="form-horizontal" method="POST" >
	<input name="orderCode" type="hidden" value="${orderCode}">
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>收货人手机号：</label>
		<div class="controls">
			<input type="text" name="mobilephone"  zapweb_attr_regex_id="469923180002"  value="${orderAddressMap["mobilephone"]}">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>收货人：</label>
		<div class="controls">
			<input type="text" name="receive_person"  zapweb_attr_regex_id="469923180002"  value="${orderAddressMap["receive_person"]}">
		</div>
	</div>		
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>详细地址：</label>
		<div class="controls">
			<input type="text" name="address"  zapweb_attr_regex_id="469923180002"  value="${orderAddressMap["address"]}">
		</div>
	</div>
	<#if orderEditService.checkShowIdcardNumber(orderCode)>
		<div class="control-group">
			<label class="control-label" ><span class='w_regex_need'>*</span>身份证号码：</label>
			<div class="controls">
				<input type="text" name="auth_idcard_number"  zapweb_attr_regex_id="469923180002"  value="${orderAddressMap["auth_idcard_number"]}">
			</div>
		</div>
	</#if>
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>所在地区：</label>
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
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="b4e54268853111eaabac005056165069"  onclick="zapjs.zw.func_add(this)"  value="提交" />
    	</div>
	</div>	
</form>

<script>
require(['cfamily/js/selectAddressArea'],

function()
{
	zapjs.f.ready(function()
		{
			selectAddressArea.init({
				provinceId: 'zw_f_province',
				provinceValue: '${areaMap["lv1"]}',
				cityId: 'zw_f_city',
				cityValue: '${areaMap["lv2"]}',
				countyId: 'zw_f_county',
				countyValue: '${areaMap["lv3"]}',
				townId: 'zw_f_town',
				townValue: '${areaMap["lv4"]!}'
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