<#assign e_page=b_page>
<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire e_page />
</div>
<hr/>
<#assign e_pagedata=e_page.upChartData()>
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
				<#assign formCd=e_list[4]?default("")>
				<#assign soId=e_list[6]?default("")>
		  		 <#list e_list as e>
		  		 	<#if e_index==4>
	  		 			<td>
	  		 				<#if formCd=="10">
			      				直播
			      			<#elseif formCd=="20" >
	  		 					重播
	  		 				<#elseif formCd=="30" >
	  		 					录播
	  		 				</#if>
			      		</td>
		  		 		
	      			<#elseif e_index==6 >
	      				<td>
		      				<!--<#if soId="1000001">
					      		家有一台
					      	<#elseif soId=="1000017">
					      		南京二台
					      	<#elseif soId=="1000026">
					      		黑龙江二台
		      				</#if>-->
		      				${e?default("")}
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
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>