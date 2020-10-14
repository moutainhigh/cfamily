<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#-- 商家后台-商品详细信息页面 -->
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<div class="cmb_cmanage_page_title  w_clear">
<span>商品详细信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
 <#assign formatUtil=b_method.upClass("com.cmall.systemcenter.util.FormatUtil")>
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
<#assign product_code=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
	
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	</#list>
	<#assign prchType=product_support.getPrchType(product_code)>
	<div class="control-group">
		<label class="control-label" for="">入库类型:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.prchType?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">仓库编号:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.oaSiteNo?if_exists} </div>
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
	<div class="control-group">
		<label class="control-label" for="">采购类型:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.purchaseType?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">结算方式:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.settlementType?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">毛利润:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.grossProfit?if_exists} </div>
		</div>
	</div>
</form>
</#macro>
<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	
	
	  		<@m_zapmacro_common_field_show e_field e_page/>
	  	
</#macro>
<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e_field.getFieldTypeAid()=="104005021">
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
			      		<#if e_field.getFieldName()=="tax_rate">
			      			<#assign taxRatePercent = formatUtil.stringToPercent('${e_field.getPageFieldValue()?default("0")}',-1)> 
			      			${taxRatePercent}
			      		<#else>
			      			${e_field.getPageFieldValue()?default("")}
			      		</#if>
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>
<div class="cmb_cmanage_page_title  w_clear">
<span>商品SKU信息</span>
</div>
<#assign skuDetail=b_method.upControlPage("page_chart_vv2_pc_skuinfo_cf","zw_f_product_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table skuDetail>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list skuDetail.getPageData() as e_list>
		<#assign sku_code = e_list[0]> 
			<tr>
	  		 <#list e_list as e>
	  		 <#if e_index = 3>
	  		 	<#assign stores = storeService.getStockNumBySkuBySum("${sku_code}")> 
  					<td>	
  						${stores}
  					 </td>
	  		 <#elseif e_index = 5>
  					<td>	
  						<#if e = '可卖'>
  							<span style="color:blue">可卖</span>
  						<#else>
  							<span style="color:red">不可卖</span>
  						</#if>
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

<div class="cmb_cmanage_page_title  w_clear">
<span>商品审批历史</span>
</div>
<#assign productFlowHistory=b_method.upControlPage("page_chart_v_v_sc_flow_history_cs_cf","zw_f_outer_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table productFlowHistory />

</div>