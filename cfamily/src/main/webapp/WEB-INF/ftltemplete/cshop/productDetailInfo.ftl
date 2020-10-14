<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign formatUtil=b_method.upClass("com.cmall.systemcenter.util.FormatUtil")>
<#assign sellerInfoService=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>


<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
<#assign smallSellerCode=b_method.upFiledByFieldName(b_page.upBookData(),"small_seller_code").getPageFieldValue() />

<#-- sellerType：跨境商户4497478100050002，跨境直邮4497478100050003，平台入驻4497478100050004的特殊判断 -->
<#assign sellerType=sellerInfoService.getSellerType(smallSellerCode) />

<#-- 毛利润/服务费-->
<#assign grossProfitLabel="毛利润"/>
<#-- 销售价格/代收价格-->
<#assign sellPriceLabel="销售价格"/>
<#-- 销售价/代收单价-->
<#assign sellPriceLabelS="销售价"/>
<#-- 成本价/应付代收-->
<#assign costPriceLabel="成本价"/>
<#-- 活动售价 -->
<#assign nowSellPriceLabel="当前售价"/>
<#-- 活动成本价 -->
<#assign nowCostPriceLabel="当前成本价"/>

<#if sellerType=="4497478100050002" || sellerType=="4497478100050003" || sellerType=="4497478100050004">
	<#assign grossProfitLabel="服务费"/>
	<#assign sellPriceLabel="代收价格"/>
	<#assign sellPriceLabelS="代收单价"/>
	<#assign costPriceLabel="应付代收"/>
</#if>

<#assign productExtInfo=product_support.getProductExtInfo(productCode)> 


<#-- 商家后台-商品详细信息页面 -->
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<div class="cmb_cmanage_page_title  w_clear">
<span>商品详细信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	</#list>
	<#assign prchType=product_support.getPrchType(productCode)>
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
		<label class="control-label" for="">${grossProfitLabel}:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.grossProfit?if_exists} </div>
		</div>
	</div>
	
	<#assign authority_logo_list=b_method.upDataBysql("pc_product_authority_logo","select * from pc_authority_logo where uid in (select authority_logo_uid from pc_product_authority_logo where product_code = '${productCode}')")>
	<#if authority_logo_list??>
	<div class="control-group">
		<label class="control-label" for="">商品保障:</label>
		<div class="controls">
			<div class="control_book">
				<#list authority_logo_list as e>
					<label><img src="${e.logo_pic}" style="width:25px">${e.logo_content}</label>
				</#list>
 			</div>
		</div>
	</div>		
	</#if>	

	<#if productExtInfo??>
		<#if productExtInfo["product_trade_type"] != "">	
		<div class="control-group">
			<label class="control-label" for="">贸易类型:</label>
			<div class="controls">
				<div class="control_book">
					<#if productExtInfo["product_trade_type"] == "0">	
						保税备货
					</#if>
					<#if productExtInfo["product_trade_type"] == "1">	
						海外直邮
					</#if>	
					<#if productExtInfo["product_trade_type"] == "2">	
						一般贸易
					</#if>								
	 			</div>
			</div>
		</div>	
		</#if>
	</#if>	
	
	<#assign deliveryStoreType=b_method.upDataOneOutNull("sc_define","","","","define_code", productExtInfo["delivery_store_type"],"parent_code","449747160043")>
	<div class="control-group">
		<label class="control-label" for="">配送仓库:</label>
		<div class="controls">
			<div class="control_book">
				${deliveryStoreType.define_name?default("")}							
 			</div>
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

	<#if e_field.getFieldName()=="sell_price">
		<@m_zapmacro_common_field_start text=sellPriceLabel+":" />
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	</#if>
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
<#assign skuDetail=b_method.upControlPage("page_chart_vv2_pc_skuinfo","zw_f_product_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<#-- 列表的自动输出 -->
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>
		      	 
			      	 	 <#if e_list_index = 2>
			      	 	 	<th>${sellPriceLabelS}</th>
		      	 	 	 <#elseif e_list_index = 3>
		      	 	 	 	<th>${costPriceLabel}</th>
		      	 	 	 	<th>${nowSellPriceLabel}</th>
		      	 	 	 	<th>${nowCostPriceLabel}</th>
			      	 	 <#else>
				      	 	<th>${e_list}</th>
			      	 	 </#if>
		      	 
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list skuDetail.getPageData() as e_list>
		<#assign sku_code = e_list[0]> 
			<tr>
	  		 <#list e_list as e>
	  		 <#if e_index = 4>
	  		 	<#assign stores = storeService.getStockNumBySkuBySum("${sku_code}")> 
  					<td>	
  						${stores}
  					 </td>
	  		 <#elseif e_index = 3>
	  		 	<td>
	      			${e?default("")}
	      		</td>
	      		<td><#-- 当前售价 -->
	      			<#assign sellPrice=product_support.getSellPriceBySkuCode("${sku_code}")>
	      			${sellPrice}
	      		</td>
	      		<td><#-- 当前成本价 -->
	      			<#assign costPrice=product_support.getCostPriceBySkuCode("${sku_code}")>
	      			${costPrice}
	      		</td>
	  		 <#elseif e_index = 6>
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
<div class="cmb_cmanage_page_title  w_clear">
<span>商品审批历史</span>
</div>
<#assign productFlowHistory=b_method.upControlPage("page_chart_v_v_sc_flow_history_cs_cf","zw_f_outer_code=" + productCode + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table productFlowHistory />

</div>
