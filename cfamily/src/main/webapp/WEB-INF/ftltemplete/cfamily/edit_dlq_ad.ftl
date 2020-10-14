
<@m_zapmacro_common_page_edit b_page />

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>

		
		<#if e_field.getPageFieldName() == "zw_f_location">
			<#if e_field.getPageFieldValue() == "449747760001" || e_field.getPageFieldValue() == "449747760002">
			<#else>
				<@m_zapmacro_common_field_text  e_field />
			</#if>
		
		<#elseif e_field.getPageFieldName() == "zw_f_pic">
			<#assign uid = b_page.getReqMap()["zw_f_uid"] >
			<#assign contentService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
			<#assign info = contentService.getADInfoByUid(uid) >
			<#if info["id_number"] == "1000" >
				<@m_zapmacro_common_field_upload  e_field  e_page />
				<span style=" position: absolute;   left: 360px;  line-height: 30px;    color: #BFBCBC;    top: 115px;">上部广告图片建议尺寸: 1080 * 252 下部广告图片建议尺寸: 1080 * X</span>
			<#else>
				<@m_zapmacro_common_field_upload  e_field  e_page />
				<span style="position: absolute;left: 380px;line-height:30px;color:#BFBCBC;top:125px;">建议尺寸: 1080 * 426</span>
			</#if>
			
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
	
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="preSubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>

<script type="text/javascript">
	function preSubmit (obje) {
		//url 验证
		var url = $(obje).parent().parent().parent().find("#zw_f_url").val();
		if(url.length != 0){
			if(!CheckUrl(url)) {
				zapjs.f.message('请填写正确的URL');
				return false;
			}
			var indx = url.indexOf("http://share");
			if( indx >= 0) {
				zapjs.f.message('广告连接不允许带有http://share字样 ');
				return false;
			}
		}
		zapjs.zw.func_edit(obje);
	}
	function CheckUrl(str) { 
		var RegUrl = new RegExp(); 
		RegUrl.compile("^[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=]+$");//jihua.cnblogs.com 
		if (!RegUrl.test(str)) { 
		return false; 
		} 
		return true; 
	} 
	
</script>

