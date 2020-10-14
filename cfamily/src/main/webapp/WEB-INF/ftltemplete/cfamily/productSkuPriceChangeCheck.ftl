<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign scdefineService=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>

<script>
require(['cfamily/js/productSkuPrice']);
</script>

<#-- 商品价格变更-商品详细信息页面 -->
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<div class="cmb_cmanage_page_title  w_clear">
<span>商品详细信息</span>
</div>

<@m_zapmacro_common_page_book b_page />
<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>	
<form class="form-horizontal" method="POST" >
	<#assign product_code=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
	<#assign productInfo=product_support.getProduct(product_code)>
	<#assign prchType=product_support.getPrchType(product_code)>
	<div class="control-group">
		<label class="control-label" for="">商品编号:</label>
		<div class="controls">
			<div class="control_book"> ${productInfo.productCode?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品名称:</label>
		<div class="controls">
			<div class="control_book"> ${productInfo.productName?if_exists} </div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">商品状态:</label>
		<div class="controls">
			<div class="control_book"> ${scdefineService.getDefineNameByCode(productInfo.productStatus)} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">品牌名称:</label>
		<div class="controls">
			<div class="control_book"> ${productInfo.brandName?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">供应商编号:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.dlrId?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">供应商名称:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.dlrNm?if_exists} </div>
		</div>
	</div>
</form>
</#macro>


<div class="cmb_cmanage_page_title  w_clear">
<span>商品SKU价格修改</span>
</div>
<#assign skuDetail=b_method.upControlPage("page_chart_v_pc_skuinfo_price","zw_f_product_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table skuDetail>
<form class="form-horizontal" method="POST" >
<table class="table  table-condensed table-striped table-bordered table-hover">
<input type="hidden" id="zw_f_json" name="zw_f_json" value = ""/>
<input type="hidden" id="zw_f_small_seller_code" name="zw_f_small_seller_code" value="${productInfo.smallSellerCode}">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>  	 
		      	 
		      	<#if e_list_index ==0>
	      		
	      		<#elseif e_list_index ==1>
	      		
	      		<#elseif e_list_index ==3>
	      		
	      			<th>
		      	 		${e_list}
		      	 	</th>
		      	 	
		      	 	<th>
		      	 		变更后成本价
		      	 	</th>
		      	 <#elseif e_list_index ==4>
	      		
	      			<th>
		      	 		${e_list}
		      	 	</th>
		      	 	
		      	 	<th>
		      	 		变更后销售价
		      	 	</th>
	      		
	      		<#else>
	      			<th>
		      	 		${e_list}
		      	 	</th>
	      		</#if>
		      	 
	      </#list>
	    </tr>
  	</thead>
  
	<tbody id="productSkuPriceTable">
		<#list skuDetail.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>			
	      		<#if e_index ==0>
	      			<input type="hidden" id="zw_f_sku_code_${e_list_index}" name="zw_f_sku_code"  value="${e_list[0]}">
	      		<#elseif e_index ==1>
	      			<input type="hidden" id="zw_f_product_code_${e_list_index}" name="zw_f_product_code"  value="${e_list[1]}">
	      		<#elseif e_index ==3>
	      		
	      			<td>
		      	 		${e?default("")}
		      	 	</td>
		      	 	
		      	 	<td>
		      	 		${e?default("")}
		      	 	</td>
		      	 
		      	 <#elseif e_index ==4>
	      		
	      			<td>
		      	 		${e?default("")}
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
	 		<tr>
	 			<td>
	 				备注
	 			</td>
	 			<td colspan='4'style='text-align:left;'>
					   <textarea rows="5" style="width:60%;" id="zw_f_flow_mark" zapweb_attr_regex_id="469923180002" name="zw_f_flow_mark" ></textarea>
	 			</td>
	 		</tr>
	 		<tr>
	      		<td colspan='5'style='text-align:center;'>
	      			<input type="button" value="审核" onclick="productSkuPrice.submit(this)" zapweb_attr_operate_id="afbaab08c803416fa607e38f7457ce12" class="btn  btn-success">
	      		</td>
	      	</tr>
		</tbody>
</table>
</form>
</#macro>

</div>