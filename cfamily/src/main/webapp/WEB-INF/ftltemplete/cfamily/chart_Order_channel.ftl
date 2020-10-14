<script>
		function orderchannel(obj) {
			var starttime = $('#zw_f_daytime_zw_a_between_from').val();
			var endtime = $('#zw_f_daytime_zw_a_between_to').val();
			obj.href=obj.href + '&starttime=' + starttime + "&endtime=" + endtime;		
		}
</script>
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
		        <#if (e_list_index ==0)>
			      	 <th width="83px">
			      	 	${e_list}
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
	      		<td>
	      			${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    	</div>
	</div>
</#macro>

<#-- 按钮显示 -->
<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
    		
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    			</#if>
    		</#list>
</#macro>


<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_link  e_operate  e_style_css>
	<#if e_operate.getOperateName()=="导出">
		<input id="zw_f_url1" type="hidden" value="${e_operate.getOperateLink()}" name="zw_f_url1">
		<a class="${e_style_css}" href="${e_operate.getOperateLink()}" onclick="orderchannel(this)">${e_operate.getOperateName()}</a>
	</#if>
</#macro>