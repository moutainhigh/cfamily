<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign scdefineService=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>

<#-- 商品价格变更-商品详细信息页面 -->
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">


<@m_zapmacro_common_page_book b_page />
<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
<#assign flowCode=b_method.upFiledByFieldName(b_page.upBookData(),"flow_code").getPageFieldValue() />
<#macro m_zapmacro_common_page_book e_page>	

</#macro>

<#assign skuDetail=b_method.upControlPage("page_chart_v_sc_skunum_change","zw_f_flow_code=" + flowCode+"&zw_f_product_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table skuDetail>
<form class="form-horizontal" method="POST" >
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>  	 
		      	 
	      			<th>
		      	 		${e_list}
		      	 	</th>
		      	 
	      	</#list>
	    </tr>
  	</thead>
  
	<tbody id="productSkuPriceTable">
		<#list skuDetail.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
		      	
		      	<#if e_index=1>
		      		<#if e_list[1] = '1'>
		      			<td>增加到</td>
		      		</#if>
		      		<#if e_list[1] = '2'>
		      			<td>减少到</td>
		      		</#if>
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
</form>
</#macro>

</div>