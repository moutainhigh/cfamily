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



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	       <#list e_pagedata.getPageHead() as e_list>
		       <#if (e_list_index >0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			   </#if>
	      </#list>
	      <th>日志</th>
	      
	    </tr>
  	</thead>
  	
  	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
				<#list e_list as e>
		 			<#if (e_index >0)>
			      		<td style="text-align: center;">
			      			<#if (e_index == 7)>
			      				<#if e_list[5] == '清算中'>
				      				${e?default("")}
							  	<#else>								  	
							  		
							  	</#if>
							<#elseif (e_index == 6)>
			      				<#if e_list[5] == '等待合作' || e_list[5] == '合作中' || e_list[5] == '已冻结'>
				      				${e?default("")}
							  	<#else>								  	
							  		
							  	</#if>
							<#else>								  	
								${e?default("")}
						  	</#if>
			      		</td>
			      	</#if>
		  		</#list>
		  		
		  		<td style="text-align: center;">
		  			<a class="btn btn-link" target="_blank" href="page_chart_v_lc_channel_seller_invest_log?channel_seller_code='${e_list[1]}'">查看</a>
		  		</td>
		  		
	  		</tr>
	 	</#list>
	</tbody>
</table>
</#macro>