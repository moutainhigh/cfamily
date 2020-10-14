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
<#assign productSkuPriceService=b_method.upClass("com.cmall.productcenter.service.ProductSkuPriceService")>
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
		        	<#if e_list_index != 7 && e_list_index != 8>
		        		<th>
							${e_list}
						</th>
					</#if>
		      	 	<#if e_list_index==4 >
					 	 <th>商品分类</th>
					 	 <th>预览</th>
					<#elseif e_list_index==7>
						 <th>商品成本价</th>
						 <th>供货价</th>
						 <th>佣金</th>
						 <th>运营成本</th>
						 <th>日志</th>
						 <th>状态</th>
						 <th>操作</th>
					</#if>
		      </#list>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign productCode=e_list[0]>
				<#assign skuCode=e_list[1]>
		  		 <#list e_list as e>
		  		 	<#if e_index==0>
		  		 		<td>
	  		 				<input name="auditCheck" disabled="disabled" type="checkbox" value="${skuCode}"/>
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
					<#elseif e_index==6 >
						<td>
							${e?default("")}
						</td>
						<#assign allPriceList=productSkuPriceService.getAllPriceAndStatus(skuCode)>
						<#assign skuStatus=allPriceList[4]>
						<#list allPriceList as price>
							<#if price_index==3>
								<td>${price}</td>
								<td>
									<a href="page_chart_v_pc_bf_review_log?sku_code=${skuCode}"  target="_blank">查看日志</a>
								</td>
							<#elseif price_index==4>
								<td>${price}</td>
								<td>
									<#if skuStatus=='未推'||skuStatus=='运营驳回'||skuStatus='缤纷驳回' || skuStatus='强制下架' || skuStatus='成本价变更' || skuStatus='佣金配置变更'>
										<input zapweb_attr_operate_id="180ccb5c44f745f4b9a939b0a067c967" class="btn btn-small" onclick="auditGood(this, '${skuCode}')" type="button" value="推送" name="pushButton">
									</#if>
								</td>
							<#else>
								<td>${price}</td>
							</#if>
						</#list>
					<#elseif e_index==7>	
					
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

<script type="text/javascript" src="../resources/bf/auditGood.js"></script>
<script type="text/javascript" >
	$(function(){
		$("tbody input[name='pushButton']").each(function(){
			$(this).parent().siblings().first().children().attr("disabled",false);
		});
	})
</script>