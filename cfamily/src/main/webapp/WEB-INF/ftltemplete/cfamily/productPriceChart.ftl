<@m_zapmacro_common_page_chart b_page />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />

<script>
require(['cfamily/js/productSkuPrice','cfamily/js/select2/select2'],
function(p)
{
	zapjs.f.ready(function()
	{
		p.init_productSkuPrice();
	});
});
</script>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>


<#assign productCodeService=b_method.upClass("com.cmall.productcenter.service.ProductCheck")>
<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>

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
		<#assign productCode=e_list[0]>
			<tr>
	  		 <#list e_list as e>
	  		 	<#if e_index = 1>
	  		 		<td>
	  		 			<a target="_blank" href="page_book_v_v_product_base_info?zw_f_product_code=${e_list[0]}">${e?default("")}</a>
	  		 		</td>
	  			<#elseif e_index = 4>	  		 
	      			<td>
	      			<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
					<#assign keys=categoryMap?keys>
					<#list keys as key>
						${categoryMap[key]?trim}<br>
					</#list>
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

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSourceByParam(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>