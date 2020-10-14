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
	
	</div>

</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>


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
	
	<#assign uuid = b_page.getReqMap()["zw_f_uid"] >
	<input type="hidden" id="zw_f_uid" name="zw_f_uid" value="${uuid}"/>
	
	  		 	<div class="w_left w_w_100" id="picUrl">
				</div>
		</tbody>
</#macro>
<@m_zapmacro_common_html_script "require(['cfamily/js/comment'],function(){zapjs.f.ready(function(){comment.getPicUrl();});});" />	