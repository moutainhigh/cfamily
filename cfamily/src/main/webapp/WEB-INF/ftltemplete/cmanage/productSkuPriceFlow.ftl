<@m_zapmacro_cmanage_flow_page_chart b_page,b_method  />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />


<#macro m_zapmacro_cmanage_flow_table e_pagedata,b_method>

<@m_common_html_js ["cmanage/js/flow.js"]/>
<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
<#assign productCodeService=b_method.upClass("com.cmall.productcenter.service.ProductCheck")>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list> 	
			     	<#if e_list_index = 0>
			     	<#elseif e_list_index = 1>
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
	  		 
	  		 <#if e_index = 0>
	  		 
	  		 <#elseif e_index = 1>
	  		 
	  		 	<#elseif e_index = 5>
		      		<td>  				
		      			
		      			<a target="_blank" href="page_book_v_sc_flow_history_skuprice?zw_f_product_code=${e_list[4]}&zw_f_flow_code=${e_list[0]}&zw_f_current_status=${e_list[1]}">${e?default("")}</a>	   
		      			   		
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


<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    		<input type="button" class="btn  btn-success" onclick="exportDialog();" value="批量审核" />
    		
    	</div>
	</div>
</#macro>

<script type="text/javascript">
function exportDialog(){
		zapjs.f.window_box({
			id : 'bdwmOrder',
			content : '<iframe src="../page/page_batch_skuprice_flow?zw_f_uid=page_chart_v_sc_flow_mian_skuprice_cw" frameborder="0" style="width:100%;height:350px;"></iframe>',
			width : '700',
			height : '450'
		});
}
</script>




