

<@m_zapmacro_common_page_add b_page />


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field1 e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field1 e_field   e_page>
		<#assign id_number = b_page.getReqMap()["zw_f_id_number"] >
		<#assign page_number = b_page.getReqMap()["zw_f_page_number"] >
		<input type="hidden" value="${id_number}" name="zw_f_id_number"/>
		<input type="hidden" value="${page_number}" name="zw_f_page_number"/>
		<#if id_number == "N2" || id_number == "N5">
			<div style="width:100%;height:5px;"></div>
			<#if e_field.getPageFieldName() == "zw_f_picture">
				<div>
					<@m_zapmacro_common_field_upload  e_field  e_page />
					<span style="position: absolute;left: 360px;line-height: 30px;color: #BFBCBC;top: 56px;"><#if id_number == "N2">建议尺寸: 1080 * 540<#elseif id_number == "N5">主商品建议尺寸： 1080 * 540;<br/>小商品图建议尺寸: 180 * 180</#if></span>
				</div>
		  		<#if e_field.getPageFieldName() == "zw_f_picture">
		  			<div class="control-group">
		  				<label class="control-label" for="zw_f_picture"><span class="w_regex_need">*</span>添加商品 :</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn" value="选择商品" onclick="add_dlq_info.show_windows(2)"/><div id="N2AddProductShowTime" style="display:inline;margin-left:5px;"></div>
		  			</div>
		  		</#if>
			<#elseif e_field.getPageFieldName() == "zw_f_location"> 
				<@m_zapmacro_common_field_text  e_field />
			</#if>
		<#elseif  id_number == "N3">
			<div style="width:100%;height:5px;"></div>
			<#if e_field.getPageFieldName() == "zw_f_picture" || e_field.getPageFieldName() == "zw_f_co_describe" || e_field.getPageFieldName() == "zw_f_location">
				<#if e_field.getPageFieldName() == "zw_f_picture">
					<div>
						<@m_zapmacro_common_field_upload  e_field  e_page />
						<span style="position: absolute;left: 360px;line-height: 30px;color: #BFBCBC;top: 56px;">建议尺寸: 560 * 300</span>
					</div>
				<#else>
					<@m_zapmacro_common_auto_field e_field e_page/>
				</#if>
			</#if>
		<#else>
		
			<@m_zapmacro_common_auto_field e_field e_page/>
		  	
		</#if>
		
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == "zw_f_location">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="0">
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
		
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	
	<#-- <input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" /> -->
	<#assign id_number = b_page.getReqMap()["zw_f_id_number"] >
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="add_dlq_info.subAddContent(this,'${id_number}')"  value="提交新增" />
	
</#macro>




