<div class="zw_page_common_inquire">
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_inquire b_page />
	
	
	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001009" />
</form>
</div>
<hr/>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>



<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<#if e_field.getPageFieldName()=="zw_f_flag_show">
				<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>><#if e_key.getV()=="449746530001">已发布<#else>未发布</#if></option>
					</#list>
	      		</select>
			<#else>
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
			</#if>
	<@m_zapmacro_common_field_end />
</#macro>
	
<#assign e_pagedata=b_page.upChartData()>
<div class="zw_page_common_data">
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	<th>
		      		${e_list}
		      	</th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
		      		<#if e_index=6>
		      			<#-- fq 更改“e_list[5] = "上线"”  为 “e_list[6] = "上线"” -->
		      			<#if e_list[6] = "上线">
		      				已发布
		      			<#else>
		      				未发布
		      			</#if>
	  			   <#else>
	  				    ${e?default("")}
	  			   </#if>
      		   </td>
	      	</#list>
	      	</tr>
	 	</#list>
</tbody>
</table>
<@m_zapmacro_common_page_pagination b_page  e_pagedata />
</div>