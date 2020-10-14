<#-- 修改页 -->
<#assign orderEditService=b_method.upClass("com.cmall.familyhas.service.OrderEditService")>
<#assign orderCode = b_page.getReqMap()["orderCode"]! >
<#assign skuCode = b_page.getReqMap()["skuCode"]! >
<#assign skuInfo = b_method.upDataOneOutNull("pc_skuinfo","","","","sku_code",skuCode)>
<#assign skuList = orderEditService.getOtherSkuList(orderCode,skuCode)> 
<form class="form-horizontal" method="POST" >
	<input name="orderCode" type="hidden" value="${orderCode}">
	<input name="skuCode" type="hidden" value="${skuCode}">
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>原规格属性：</label>
		<div class="controls">
	      		<span>${skuInfo["sku_keyvalue"]}</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>新规格属性：</label>
		<div class="controls">
	      		<select name="newSkuCode">
						<option value="">请选择</option>
						<#list skuList as e_list>
							<option value="${e_list["sku_code"]}">${e_list["sku_keyvalue"]}</option>
						</#list>
	      		</select>
		</div>
	</div>		
	<div class="control-group">
    	<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="d93fb6cc878411eaabac005056165069"  onclick="zapjs.zw.func_add(this)"  value="提交" />
    	</div>
	</div>	
</form>

<script>
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