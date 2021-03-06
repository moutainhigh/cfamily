
<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

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
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if (e_field.getFieldName() == "flag")>
			<select name="zw_f_flag" id="zw_f_flag">
					<option value="" selected="selected">请选择</option>
	      			<option value="0">未发布</option>
	      			<option value="1">已发布</option>
      		</select>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index <=7)&&(e_list_index > 0)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	 </#if>
	      </#list>
	        <th colspan="4" width="20%">	操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 <#if (e_index <=7)&&(e_index > 0)>
	      		<td>
	      			 <#if (e_index = 5) && (e = '0')>
	      			 	未发布
	      			 <#elseif (e_index = 5) && (e = '1')>
	      			 	已发布
	      			 <#else>
		      			${e?default("")}
	      			 </#if>
	      		</td>
	      		 </#if>
	      	</#list>
	      	
	      	<td colspan="4" style="vertical-align:middle; text-align:center;">
				 <#if (e_list[5]=='0')>
					<a class="btn btn-link" target="_blank" href="page_edit_v_coupon_oc_activity?zw_f_uid=${e_list[0]}">修改</a>
					<a class="btn btn-link" target="_blank" href=" page_bookEdit_v_coupon_oc_activity?zw_f_uid=${e_list[0]}&zw_f_activity_code=${e_list[1]}">优惠券类型管理</a>
					<input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_activity_code: '${e_list[1]}'});" class="btn btn-small" zapweb_attr_operate_id="0623cd18188d40478d64b4cbaeedea70">
				</#if>
				 <#if (e_list[5]=='1')>
				 	<input type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, {zw_f_activity_code: '${e_list[1]}'});" class="btn btn-small" zapweb_attr_operate_id="0623cd18188d40478d64b4cbaeedea70">
				</#if>
					<a class="btn btn-link" target="_blank" href="page_book_v_coupon_oc_activity?zw_f_uid=${e_list[0]}&zw_f_activity_code=${e_list[1]}">查看</a>
			</td>
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>
	 