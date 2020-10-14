
<#assign page_type = b_page.getReqMap()["zw_f_p_type"] />
<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] />
<div style="width:100%;height:5px;"></div>
<@m_zapmacro_common_page_add b_page page_type/>

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page page_type>
<form class="form-horizontal" method="POST" >
	<input id="zw_f_p_type" name="zw_f_p_type" type="hidden" value="${page_type}" />
	<input id="zw_f_tv_number" name="zw_f_tv_number" type="hidden" value="${tv_number}" />
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
		
		<#assign id_number = b_page.getReqMap()["id_number"] >
		<#if e_field.getPageFieldName() == "zw_f_location" && id_number == "1000">
      		<div class="control-group">
				<label class="control-label" for="zw_f_location"><span class="w_regex_need">*</span>位置：</label>
				<div class="controls">
			      		<select name="zw_f_location" id="zw_f_location">
		
								<option value="449747760001">上部</option>
		
								<option value="449747760002">下部</option>
			      		</select>
				</div>
			</div>
      		
		<#elseif  e_field.getPageFieldName() == "zw_f_pic" && id_number == "1000">
			<div>
				<@m_zapmacro_common_field_upload  e_field  e_page />
				<span style="position: absolute;left: 370px;line-height: 30px;color: #BFBCBC;top: 26px;">上部广告图片建议尺寸: 1080 * 252   下部广告图片建议尺寸: 1080 * X</span>
			</div>
		<#elseif  e_field.getPageFieldName() == "zw_f_pic" && id_number == "1001">
			<div>
				<@m_zapmacro_common_field_upload  e_field  e_page />
				<span style="position: absolute;left: 400px;line-height: 30px;color: #BFBCBC;top: 106px;">建议尺寸: 1080 * 426</span>
			</div>
		<#else>
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
		  		<@m_zapmacro_common_field_text  e_field />
		  	<#else>
		  		<@m_zapmacro_common_field_span e_field/>
		  	</#if>
		</#if>
		
</#macro>


<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>

	<#assign id_number = b_page.getReqMap()["id_number"] >
	<#if id_number == "1001">
		<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	<#else>
		<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="add_dlq_info.subAddContent(this,'N4')"  value="${e_operate.getOperateName()}" />
	</#if>
	
</#macro>




