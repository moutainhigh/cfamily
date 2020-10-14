<@m_zapmacro_cmanage_flow_page_chart b_page,b_method  />


<#macro m_zapmacro_cmanage_flow_table e_pagedata,b_method>

<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
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
	  		 	<#if e_index = 8>
	  		 		<td>
	  		 			<#if e?default("") = "">
	  		 				无内容
	  		 			<#else>
	  		 				<a target="_blank" href="${e?default("")}">预览</a>
	  		 			</#if>
		      			
		      		</td>
	  		 	
		      	<#elseif e_index = 0>
	  		 	
	  		 	<td><a target="_blank" href="page_book_v_sc_sc_flow_main?zw_f_flow_code=${e?default("")}">${e?default("")}</a>
		      	</td>
		      	
		      	<#else>
		      		<td>
		      			${e?default("")}
		      		</td>
	  		 	</#if>
	      		
	      	 </#list>
	      		
	      	</tr>
	 	</#list>
	</tbody>
	
</table>

</#macro>


<#-- 列表页 -->
<#macro m_zapmacro_cmanage_flow_page_chart e_page,b_method >

	<div class="zw_page_common_inquire">
		<@m_flow_common_page_inquire e_page />
	</div>
	
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_cmanage_flow_table e_pagedata,b_method />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>



<#-- 查询区域 -->
<#macro m_flow_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_flow_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>



<#--查询的自动输出判断 -->
<#macro m_flow_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  (e.getFieldTypeAid()=="104005019" && e.getPageFieldName() = "zw_f_current_status")>
	  		<@m_flow_common_field_select  e  e_page "请选择"/>
	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>

</#macro>


<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_flow_common_field_select   e_field    e_page    e_text_select="">
	<div class="control-group">
			<label class="control-label" for="zw_f_current_status">单据状态</label>
			<div class="controls">
			      <select name="zw_f_current_status" id="zw_f_current_status">
			      				<option value="">请选择</option>
			      				<option value="4497172300040002" <#if  e_field.getPageFieldValue()=='4497172300040002'> selected="selected" </#if>>商家初审通过</option>
								<option value="4497172300040004" <#if  e_field.getPageFieldValue()=='4497172300040004'> selected="selected" </#if>>商家终审通过</option>
								<option value="4497172300040003" <#if  e_field.getPageFieldValue()=='4497172300040003'> selected="selected" </#if>>商家审核不通过</option>
								<option value="4497172300050002" <#if  e_field.getPageFieldValue()=='4497172300050002'> selected="selected" </#if>>模板审核通过</option>
								<option value="4497172300050003" <#if  e_field.getPageFieldValue()=='4497172300050003'> selected="selected" </#if>>模板审核不通过</option>
			      
			      </select>
		</div>
	</div>
</#macro>








