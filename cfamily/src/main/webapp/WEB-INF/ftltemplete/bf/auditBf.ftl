<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_js ["cfamily/js/select2/select2.js"]/>
<style type="text/css">
	.redColor{color:#FF0000;}
</style>
<script>
$(document).ready(function() {
	//$("#zw_f_product_status option[value='4497153900060001']").remove();
	$("#zw_f_product_status option[value='4497153900060004']").remove();
	$("#zw_f_brand_code").select2();
	$("#s2id_zw_f_brand_code").attr("style","width:220px");
});
</script>

<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#-- 页面主内容显示  -->
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
		        	<#if e_list_index == 0>
		        		<th>
		        			<input id="checkAllOrNone" type="checkbox"/>
		        		</th>
		        	</#if>
        			<th>
						${e_list}
					</th>
		      	 	<#if e_list_index==4 >
					 	<th>商品分类</th>
					 	<th>预览</th>
					<#elseif e_list_index==7>
						<th>日志</th>
					</#if>
		      </#list>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign productCode=e_list[0]>
				<#assign skuCode=e_list[1]>
				<#assign skuStatus=e_list[8]>
		  		 <#list e_list as e>
		  		 	<#if e_index==0>
		  		 		<td>
		  		 			<input name="auditCheck" type="checkbox" value="${skuCode}"/>
		  		 		</td>
		  		 		<td>
			      			${e?default("")}
			      		</td>
	      			<#elseif e_index==4 >
	      				<td>
			      			${e?default("")}
			      		</td>
				 		 <td>
							<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
							<#assign keys=categoryMap?keys>
							<#list keys as key>
								${categoryMap[key]?trim}<br>
							</#list>
						</td>
						<td>
							<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${productCode}" target="_blank">预览</a>
						</td>
					<#elseif e_index==8>
						<td>
							<a href="page_chart_v_pc_bf_review_log?sku_code=${skuCode}"  target="_blank">查看日志</a>
						</td>
						<td>
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

<script type="text/javascript" src="../resources/bf/auditBf.js"></script>