<@m_zapmacro_common_page_edit b_page />
<#assign uid = b_page.getReqMap()['zw_f_uid']>
<#assign orderAddressMap = b_method.upDataOneOutNull("sc_tv_area","","","","uid",uid)>
<#assign chinaAreaService = b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<#assign areaMap = chinaAreaService.getFullCode(orderAddressMap["code"])>

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
	      		<select style="width:120px;" name="town" id="zw_f_town">
						<option value="">请选择</option>
	      		</select>		      			      			      		
		</div>
	</div>
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
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}" onclick="presubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>

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
				cityValue: '${areaMap["lv2"]!}',
				countyId: 'zw_f_county',
				countyValue: '${areaMap["lv3"]!}',
				townId: 'zw_f_town',
				townValue: '${areaMap["lv4"]!}'
			});
		}
	);
}

);
function presubmit(obj) {		
	zapjs.zw.func_edit(obj);
}
</script>