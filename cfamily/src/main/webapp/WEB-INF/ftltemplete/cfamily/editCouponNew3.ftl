<#assign couponType = b_method.upDataOne("oc_coupon_type","","","","uid","${b_page.getReqMap()['zw_f_uid']}")>
<#assign activity = b_method.upDataQueryToJson("oc_activity","","","","activity_code","${couponType['activity_code']}")>

<script>
require(['cfamily/js/editCouponNew3'],

function()
{
	zapjs.f.ready(function()
		{
			editCouponNew3.init();
		}
	);
}

);

// 活动对象
var activity = eval('(${activity})')[0];
</script>

<@m_zapmacro_common_page_edit b_page />


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		 <#if  e_field.getFieldName()=="cdkey">
	  	 	<div class="control-group">
				<label class="control-label" for="zw_f_cdkey">
				<span class="w_regex_need">*</span>
				优惠码：
				</label>
				<div class="controls">
					<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
				</div>
			</div>
	  	 <#else>
	  		<@m_zapmacro_common_field_text  e_field />
	  	 </#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if (e_field.getFieldName()=="money" || e_field.getFieldName()=="limit_money" || e_field.getFieldName()=="total_money")>
			<span>元</span>
		<#elseif (e_field.getFieldName()=="discount")>
			<span>%</span>
		<#elseif (e_field.getFieldName()=="privide_money" || e_field.getFieldName()=="surplus_money")>
			<span>份</span>			
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>
<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
  		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
  			<#if e_text_select!="">
  					<option value="">${e_text_select}</option>
  				</#if>
  			<#list e_page.upDataSource(e_field) as e_key>

				<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
			</#list>
  		</select>
		<#if e_field.getFieldName()=="limit_condition">
			<span id="tipLimit" class="w_regex_need"></span>
			<input type="hidden" id="limit_condition_input" name="zw_f_limit_condition" value="${e_field.getPageFieldValue()}" disabled="disabled">
		</#if>	      	
	<@m_zapmacro_common_field_end />
</#macro>
<#-- 字段：长文本框 -->
<#macro m_zapmacro_common_field_textarea e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<textarea id="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
		<#if (e_field.getFieldName()=="limit_scope")>
			<span class="w_regex_need">(使用范围不能超过20个字)</span>
		<#elseif (e_field.getFieldName()=="limit_explain")>
			<span class="w_regex_need">(使用说明不能超过200个字)</span>
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>