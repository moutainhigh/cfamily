
<#-- 页面主内容显示  -->
<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#assign channelProductService=b_method.upClass("com.cmall.productcenter.service.ChannelProductService")>
	
<#assign e_page=b_page>


<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
		
</div>
	
<#assign e_pagedata=e_page.upChartData()>
<div class="zw_page_common_data">
	
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
		        	<#if (e_list_index >0)>
				      	 <th>
				      	 	${e_list}
				      	 </th>
			      	 	 <#if e_list_index==5 >
						 	 <th>供货价</th>
						 </#if>
			      	 	 <#if e_list_index==6 >
						 	 <th>商品分类</th>
						 	 <th>预览</th>
						 </#if>
			      	 	 <#if e_list_index==8 >
						 	 <th>商户类型</th>
						 </#if>
			      	</#if>
		      	</#list>
		      	
			 	<th>日志</th>
			 	
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
					<#assign productCode = e_list[1]>
			  		<#list e_list as e>
			  		 	<#if (e_index >0)>
				      		<td style="text-align: center;">
				      			${e?default("")}
				      		</td>
			      		
			      			<#if e_index==5 >
							 	<td style="text-align: center;">
							 		<#assign supplyPrice=channelProductService.getSupplyPrice(e_list[5],e_list[6])>
							 		${supplyPrice}
							 	</td>
							</#if>
							
			      			<#if e_index==6 >
							 		<td style="text-align: center;">
										<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
										<#assign keys=categoryMap?keys>
										<#list keys as key>
											${categoryMap[key]?trim}<br>
										</#list>
									</td>
									
									<td style="text-align: center;">
										<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${productCode}" target="_blank">预览</a>
									</td>
							</#if>
							<#if e_index==8 >
							 	<td style="text-align: center;">
									<#assign sellerType=channelProductService.getSellerType(e_list[8])>
							 		${sellerType}
								</td>
							</#if>
						</#if>
			      	</#list>
			      	
				 	<td style="text-align: center;">
						<a class="btn btn-link" target="_blank" href="page_chart_v_lc_channel_product_register_log?sku_code='${e_list[2]}'">查看日志</a>
					</td>
				 	
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />

</div>
	

	