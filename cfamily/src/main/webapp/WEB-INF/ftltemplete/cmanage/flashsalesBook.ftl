


<div class="zab_info_page">
<@m_zapmacro_common_page_book b_page />
</div>

<#assign  activity_code=b_method.upFiledByFieldName(b_page.upBookData(),"activity_code").getPageFieldValue() />
<div class="cmb_cmanage_page_title  w_clear">
<span>商品信息:</span>
</div>
<#assign skus=b_method.upControlPage("page_chart_v_oc_flashsales_skuInfo","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>
<#assign flashsalesService=b_method.upClass("com.cmall.ordercenter.service.FlashsalesService")>
<#assign productStoreService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>

<#assign  e_pagedata=skus />

<input zapweb_attr_operate_id="8be8d7a141b54f14b2cf9df28a7718a9" class="btn btn-small" onclick="zapadmin.window_url('../show/page_chart_v_v_flash_product_03?zw_f_activity_code=${activity_code}&zw_p_size=-1') " type="button" value="设置商品排序">
<br>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if (e_list_index >0)>
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
	  		 <#if (e_index >0)>
	      		<td>
	      			<#if (e_index ==5)>
	      				<#--  在这里转换成商品状态 -->
	      				<#assign  statuss=b_method.upDataBysql("pc_productinfo","SELECT p.product_status from pc_productinfo p  RIGHT JOIN pc_skuinfo s on s.product_code=p.product_code where s.sku_code=:sku_code","sku_code",(e?default(""))) />
	      					<#if statuss??&&statuss?size gt 0 &&statuss[0]??&&statuss[0]["product_status"]??>
	      						<#assign  snames=b_method.upDataBysql("sc_define","SELECT define_name from sc_define where define_code=:define_code and parent_code='449715390006' ","define_code",(statuss[0]["product_status"])) />
								<#if snames??&&snames?size gt 0>
									${snames[0]["define_name"]}
								</#if>
							</#if>
							
							
						<#elseif (e_index ==6)>	
							${productStoreService.getStockNumBySkuBySum(e_list[2])}
							
						<#elseif (e_index ==8)>	
							${flashsalesService.salesNum(e_list[0],e_list[2],"SI2003")}
							
							
	      				<#else>
	      				${e?default("")}
	      			
	      			</#if>
		      			
		      			
		      			
	      			
	      		</td>
	      		</#if> 
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>

