
<div class="alert alert-warning" style="font-size:16px;">现PC版专题页支持以下几种模板：&nbsp;&nbsp;轮播模版、pc五栏多行模板、pc两栏多行模板、视频模板、优惠券(一行两栏)、优惠券（一行多栏）、一行多栏（横滑）</div>


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



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

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
	      			<#if (e_list[1]=="标题模板" || e_list[1]=="视频模板" )&& e_index==18>
	      			
	      			<#elseif (e_index==3 || e_index==7 || e_index==10) && e?default("") != "">
	      				<img src="${e?default("")}" style="width:150px;" />
	      			<#else>
	      				${e?default("")}
	      			</#if >
	      			
	      			
	      			
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>





