
<@m_zapmacro_common_page_chart b_page />


<#-- 列表页  -->
<#macro m_zapmacro_common_page_chart e_page >


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
			<#assign formCd=e_list[3]?default("")>
			<#assign formCd0 = e_list[0]?default("")>
	  		 <#list e_list as e>
	  		 <td align="center" style="text-align:center">
	  		 <#if e_index==0 >	
	  		 	   <#if formCd0=="4497471600610001">
	  		 		      推广收益		      				 
			      	<#elseif formCd0=="4497471600610002" >
			      	      买家秀
	  		 		</#if>	
		  	  <#elseif e_index==3 >	
	  		 		<#if formCd=="4497473700010001">
	  		 		<font color="#008000">启动 </font> 			      				 
			      	<#elseif formCd=="4497473700010002" >
			      	<font color="#FF0000">关闭 </font> 	
	  		 		</#if>
	  		 	  
	  		 	<#else>
		      			${e?default("")}
		      	<#if e_index==2 || e_index==1 >	
	  		 		%	
			   </#if>
			   </#if>
		  		 </td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table> 

</#macro>