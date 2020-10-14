<#assign uid = b_page.getReqMap()["zw_f_uid"]!"" >
<#assign member_code = b_page.getReqMap()["member_code"]!"" >
<#assign addressEditService=b_method.upClass("com.cmall.familyhas.service.AddressEditService")/>
<#assign dataMap=addressEditService.upChartData(uid)!""/>  
<script>
require(['cfamily/js/memberinfo_addressedit'],

function()
{
	zapjs.f.ready(function()
		{
			loadProvince();
			init('${uid}','${dataMap['address_name']!""}','${dataMap['address_mobile']!""}','${dataMap['address_default']!""}','${dataMap['address_street']!""}','${dataMap['four']!""}','${dataMap['three']!""}','${dataMap['two']!""}','${dataMap['one']!""}');
			$("#_province").change(function() {
				$("#_city").empty();
				$("#_area").empty();
				$("#_street").empty();
				
				var provinceCode = $(this).val();
				if(provinceCode != ""){
					loadCity(provinceCode);
				}
			});
	
			$('#_city').change(function() {
				$("#_area").empty();
				$("#_street").empty();
				
				var cityCode = $(this).val();
				if(cityCode != ""){
					loadArea(cityCode);
				}
			});
	
			$('#_area').change(function() {
				$("#_street").empty();
				
				var areaCode = $(this).val();
				if(areaCode != ""){
					loadStreet(areaCode);
				}
			});
			$('#sub').click(function(){
				var oForm = $("#sub").parents("form");
	if (zapjs.zw.func_regex(oForm)) {
		zapjs.zw.modal_process();
		if (zapjs.f.ajaxsubmit(oForm, "../func/"
				+ $("#sub").attr('zapweb_attr_operate_id'),
				function(o){
			switch (o.resultType) {
			case "116018010":
				eval(o.resultObject);
				break;
			default:
				var str = "window.location.href=\"page_member_v_sc_member?zw_f_member_code={0}\"";
			    str = str.format($("#zw_f_member_code").val());
				if (o.resultCode == "1") {
					if (o.resultMessage == "") {
						o.resultMessage = "操作成功";
					}
					zapjs.zw.modal_show({
						content : o.resultMessage,
						//okfunc : 'zapjs.f.autorefresh()'
						okfunc : str
					});
				} else {
					zapjs.zw.modal_show({
						content : o.resultMessage
					});
				}

				break;
			}
		}, zapjs.zw.func_error)) {

		} else {
			zapjs.f.modal_close();
		}
	}
			});
		}
	);
}

);
</script>
<form class="form-horizontal" method="POST">

		
	
	<input type="hidden" id="zw_f_uid" name="zw_f_uid" value="${uid}">
	  	
	<input type="hidden" id="zw_f_address_code" name="zw_f_address_code" value="${member_code}">	
	

<div class="control-group">
	<label class="control-label" for="zw_f_address_name"><span class="w_regex_need">*</span>收货人姓名：</label>
	<div class="controls">

		<input type="text" id="zw_f_address_name" name="zw_f_address_name" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_address_mobile"><span class="w_regex_need">*</span>收货人手机号：</label>
	<div class="controls">

		<input type="text" id="zw_f_address_mobile" name="zw_f_address_mobile" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
	  	
	
<div class="control-group">
		<label class="control-label" for="zw_f_area_code"><span class="w_regex_need">*</span>所在地区：</label>
		
		<div class="controls">
			<span id="province_city_area" name="province_city_area">
				<select zapweb_attr_regex_id="469923180002" id='_province' name='_province'></select>
				<select zapweb_attr_regex_id="469923180002" id='_city' name='_city'></select>
				<select zapweb_attr_regex_id="469923180002" id='_area' name='_area'></select>
				<select zapweb_attr_regex_id="469923180002" id='_street' name='_street'></select>
			</span>
		</div>
	</div>

<div class="control-group">
	<label class="control-label" for="zw_f_address_street"><span class="w_regex_need">*</span>详细地址：</label>
	<div class="controls">

		<textarea id="zw_f_address_street" name="zw_f_address_street" zapweb_attr_regex_id="469923180002" value=""></textarea>
	</div>
</div>	

<div class="control-group">
	<label class="control-label" for="zw_f_address_default"><span class="w_regex_need">*</span>是否默认地址：</label>
	<div class="controls">

	      		<select name="zw_f_address_default" id="zw_f_address_default">

						<option value="1">是</option>

						<option value="0" selected="selected">否</option>
	      		</select>
	</div>
</div>
	  	
		

	

		
	<div class="control-group">
    	<div class="controls">

    		
	
	<input id="sub" type="button" class="btn  btn-success" zapweb_attr_operate_id="ddf5b45dece34240ae960fec326ab215" value="提交修改">
    		

    	</div>
	</div>
</form>
