<@m_zapmacro_cmanage_flow_page_chart b_page,b_method  />

<#macro m_zapmacro_cmanage_flow_table e_pagedata,b_method>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list> 	
				
			     	<#if e_list_index = 0 ||e_list_index = 8>
			     	<#elseif e_list_index = 2>
			     		<th>
			      	 		${e_list}
			      	 	</th>
			      	 	<th>
			      	 		价格类型
			      	 	</th>
			     	<#else>
			     	<th>
			      	 	${e_list}
			      	 </th>
			     	</#if>
			      	 
	        </#list>	
	    </tr>
  	</thead>
   <tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 
	  		 	<#if e_index = 0 ||e_index = 8>
	 
	  		 	<#elseif e_index = 2	>
	  		 	
	  		 		<td>
	  		 		
	  		 		<a target="_blank" href="page_book_v_sc_flow_history_skuprice?zw_f_zid=${e_list[0]}">${e?default("")}</a>
		      	
		      		</td>
		      		<td>
						<#assign  skuInfo=b_method.upDataBysql("pc_skuprice_change_flow","SELECT DISTINCT CASE WHEN do_type = 1 then '档案价' WHEN do_type = 2 then '活动价' ELSE '老记录' END AS do_type from pc_skuprice_change_flow where flow_code = (SELECT flow_code from systemcenter.sc_flow_history where zid = :zid)","zid",(e_list[0])) />
					    <#list skuInfo as sku>
							${sku.do_type}
						</#list>
					</td>
		      		
		      		<#elseif e_index = 4 &&e_list[8]="1">
	  		 	
	  		 		<td>
	  		 		
	  		 		系统
		      	
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






