<#--活动查看页面-->

<#assign properties_code = b_page.getReqMap()["zw_f_properties_code"] >

<div class="zab_info_page" style="height:auto; overflow:hidden">
	<@m_zapmacro_common_page_book b_page />
</div>


<#macro m_zapmacro_common_page_book e_page>
	<form class="form-horizontal" method="POST" >
		<#list e_page.upBookData()  as e>
			<#if  e.getFieldName() = "properties_value_type">
				<#assign properties_value_type = e.getPageFieldValue() >
			</#if>
		  	<@m_zapmacro_common_book_field e e_page/>
		</#list>
	</form>
</#macro>


<#if  properties_value_type = "449748500001">
	
	<div class="zab_info_page_title  w_clear">
		<span>属性值</span>&nbsp;&nbsp;&nbsp;
	</div>
	
	<#assign logData=b_method.upControlPage("page_chart_v_uc_properties_value","zw_f_properties_code=${properties_code}&zw_p_size=-1")>
	<#assign  e_pagedata=logData.upChartData() />
	
	<@m_zapmacro_common_table e_pagedata />
	
</#if>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
	
	<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
		        	<#if e_list_index != 2 && e_list_index < 5>
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
		  		 	<#if e_index != 2  && e_index < 5>
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
