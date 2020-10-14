<#assign small_seller_code = b_page.getReqMap()["small_seller_code"] />
<#assign rmService=b_method.upClass("com.cmall.ordercenter.service.RetentionMoneyService")>
<#assign rm=rmService.getSellerRetentionMoney("${small_seller_code}")>
<#assign logList=rmService.getOperateLogs("${small_seller_code}",1)>
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<input type="hidden" name="small_seller_code" id="small_seller_code" value="${small_seller_code}">
	<div class="control-group">
		<label class="control-label">最大质保金</label>
		<div class="controls">
			<input disabled="disabled" type="text" id="max_retention_money" value="${rm["max_retention_money"]}">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">已扣质保金</label>
		<div class="controls">
			<input disabled="disabled" type="text" id="deduct_retention_money" name="deduct_retention_money"  value="${rm["deduct_retention_money"]}">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span class="w_regex_need">*</span>调整质保金</label>
		<div class="controls">
			<input type="text" id="adjust_retention_money" name="adjust_retention_money">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"><span class="w_regex_need">*</span>调整质保金时间</label>
		<div class="controls">
			<input type="text"   onClick="WdatePicker({dateFmt:'yyyy-MM-dd',realDateFmt:'yyyy-MM-dd'})"  id="adjust_retention_money_date" name="adjust_retention_money_date" value="">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">调整质保金原因</label>
		<div class="controls">
			<textarea id="remark" maxlength="40" name="remark"></textarea>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<input type="button" class="btn  btn-success" onclick="subFrm();" value="确定">
		</div>
	</div>
</form>
</#macro>
<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
<@m_zapmacro_common_page_edit b_page />
<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear" style="padding-bottom: 10px;">
		<span>调整质保金维护日志</span>
		<a style="float:right;" class="btn  btn-success" href="../export/page_edit_retention_money_adjust/264b968ec8d6453a840bd64f13149674?zw_f_uid=fake&small_seller_code=${small_seller_code}&type=1">导出</a>
	</div>
	<table  class="table  table-condensed table-striped table-bordered table-hover" style="margin-left:0%">
		<thead>
		    <tr>
				<th>调整质保金</th>
				<th>调整质保金时间</th>
				<th>调整质保金原因</th>
				<th>维护人</th>
				<th>维护时间</th>
				
		    </tr>
	  	</thead>
		<tbody>
			<#if logList??>
				<#list logList as e_list>
					<tr>
						<td>${e_list["retention_money"]?string("0.##")}</td>
						<td>${e_list["operate_date"]?default("")}</td>
						<td>${e_list["remark"]?default("")}</td>
						<td>${e_list["creator"]?default("")}</td>
						<td>${e_list["create_time"]?default("")}</td>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
</div>
<script>
//验证输入是否为整数
function validate(obj){
	var val = $(obj).val();
	var reg = new RegExp("^(-?\\d+)(");
	if(!reg.test(obj.value)){
		$(obj).val("");
	}
}

function subFrm(){
	var obj = {};
	obj.type=1;
	obj.smallSellerCode="${small_seller_code}";
	obj.adjustRetentionMoney=$("#adjust_retention_money").val();
	obj.remark = $("#remark").val();
	var reg = /^-?\d+\.?\d{0,2}$/;
	if(obj.adjustRetentionMoney == ""){
		zapadmin.model_message("调整质保金：不能为空");
		return false;
	}else if(!reg.test(obj.adjustRetentionMoney)){
		zapadmin.model_message("调整质保金：仅支持数字,且保留两位小数");
		$("#receive_retention_money").val("");
		return false;	
	}
	obj.adjustRetentionMoneyDate=$("#adjust_retention_money_date").val();
	if(obj.adjustRetentionMoneyDate == ""){
		zapadmin.model_message("调整质保金时间：不能为空");
		return false;
	}
	zapjs.zw.api_call('com_cmall_ordercenter_service_api_ApiRetentionMoneyManage',obj,function(result) {
		if(result.resultCode==1){
			if(result.resultMessage){
				zapjs.zw.modal_show({
						content : result.resultMessage,
						okfunc : 'zapjs.f.autorefresh()'
				});
			}else{
			 	zapjs.f.autorefresh();
			}
		}else{
			zapadmin.model_message(result.resultMessage);
		}
	});
}
</script>