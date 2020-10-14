<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign scdefineService=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>

<#-- 商品价格变更-商品详细信息页面 -->
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<div class="cmb_cmanage_page_title  w_clear">
<span>商品详细信息</span>
</div>

<#assign bookdata = b_page.upBookData()>
<#assign productCode=b_method.upFiledByFieldName(bookdata,"product_code").getPageFieldValue() />
<#assign flowCode=b_method.upFiledByFieldName(bookdata,"flow_code").getPageFieldValue() />
<#assign flowRemark=b_method.upFiledByFieldName(bookdata,"flow_remark").getPageFieldValue() />
<@m_zapmacro_common_page_book b_page />


<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>	
<form class="form-horizontal" method="POST" >
	<#-- <#assign product_code=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() /> -->
	<#assign productInfo=product_support.getProduct(productCode)>
	<#assign prchType=product_support.getPrchType(productCode)>
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
<#assign skuDetail=b_method.upControlPage("page_chart_v_skuprice_change_flow","zw_f_flow_code=" + flowCode+"&zw_f_product_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table skuDetail>
<form class="form-horizontal" method="POST" >
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>  	 
		      	       <#if e_list_index = 0>
				        
				        <#elseif e_list_index = 1>
				      		<th>${e_list}</th>
				      		 <th>SKU名称</th>
	      			<#else>
				      		<th>${e_list}</th>
				 </#if>
		      	 
	      </#list>
	    </tr>
  	</thead>
  
	<tbody id="productSkuPriceTable">
		<#list skuDetail.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
		      	 
	      					<#if e_index = 0>
				  			
				  			<#elseif e_index = 1>
					            <td>${e?default("")}</td>
					            <td>
					            <#assign  skuInfo=b_method.upDataBysql("pc_skuinfo","SELECT sku_name from pc_skuinfo where sku_code=:sku_code","sku_code",(e?default(""))) />
					              <#list skuInfo as sku>
							        ${sku.sku_name}
						          </#list>
					            </td>
					         <#elseif e_index = 9>
					         <td>${e?default("")}%</td>
					         <#elseif e_index = 8>
					         <td>${e?default("")}%</td>
					        <#else>
				      			<td>${e?default("")}</td>
				      		</#if>
	      	</#list>
	      	</tr>
	      	
	 	</#list>
	 		<tr>
	 			<td>
	 				备注
	 			</td>
	 			<td colspan='11'style='text-align:center;'>
					   <textarea readonly="readonly" rows="5" style="width:60%;" id="zw_f_flow_mark">${flowRemark}</textarea>
	 			</td>
	 		</tr>
		</tbody>
</table>
</form>
</#macro>

</div>