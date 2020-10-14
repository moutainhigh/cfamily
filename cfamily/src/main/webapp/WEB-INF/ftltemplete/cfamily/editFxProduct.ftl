<@m_zapmacro_common_page_edit b_page />
<#assign uid = b_page.getReqMap()['zw_f_uid']>
<#assign tempList=b_method.upDataToJson("oc_activity_agent_product","SELECT floor((a.sell_price-a.cost_price)*(b.coupon_rate+b.company_rate)) limit_price from ordercenter.oc_activity_agent_product a,familyhas.fh_agent_profit_setting b where a.uid = :uid","uid",uid) />

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() == "zw_f_end_amt" >
			<@m_zapmacro_common_auto_field e e_page/>
		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  	</#if>
	</#list>
	</#if>
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == "zw_f_start_amt" || e_field.getPageFieldName() == "zw_f_end_amt" >
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<span>元</span>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="presubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>

<script>	
	function presubmit(obj) {		
		var tempList = ${tempList};
		var regex = /^\d+$/;
		var limitPrice = tempList[0].limit_price;
		if(!regex.test($("#zw_f_coupon_money").val())||$("#zw_f_coupon_money").val()<=0||$("#zw_f_coupon_money").val()>limitPrice){
			zapjs.f.modal({
					content : '只能填整数或者优惠券金额超过上限！'
				});
				return;
		}
		
		zapjs.zw.func_edit(obj);
	}
	
	
	// 重写函数的执行成功回调
	zapjs.zw.func_success = function(o) {
		if (o.resultCode == "1") {
			if (o.resultMessage == "") {
				o.resultMessage = "操作成功";
			}
	
			//	刷新父级页面
			zapjs.zw.modal_show({
				content : o.resultMessage,
				okfunc : 'parent.zapjs.f.autorefresh()'
			});	
		} else {
			zapjs.zw.modal_show({
				content : o.resultMessage
			});
		}
	}
</script>