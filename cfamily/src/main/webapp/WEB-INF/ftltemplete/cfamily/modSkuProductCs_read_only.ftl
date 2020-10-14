<script>
function openModSku(ca,pc)
{
	var url = '../page/page_add_v_cf_modSku_pc_skuinfo';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: hidden' frameborder='0'  src='"+url+"?category_code="+ca+"&product_code="+pc+"'></iframe>";
		var option={title:"添加SKU信息",content:productValue,width:740,height:645};
		zapjs.f.window_box(option);
}
</script>
<#-- 商家后台-商品详细信息页面 -->
<#-- <@m_common_html_css ["cfamily/css/cfb_base.css"] /> -->
<div class="zab_info_page">
<div class="zab_info_page_title  w_clear">
<span>商品信息</span>
</div>
<#assign uuid=b_page.getReqMap()["zw_f_uid"] />
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign e_list = product_support.getProductCategoryByUid("${uuid}")>
<#assign storeService=b_method.upClass("com.cmall.productcenter.service.ProductStoreService")>
<#assign formatUtil=b_method.upClass("com.cmall.systemcenter.util.FormatUtil")>
<#assign sellerInfoService=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>

<#assign skuEditInfo=b_method.upControlPage("page_book_v_cf_pc_skuDetailInfo_cshop","zw_f_product_code="+e_list["product_code"])>
<#assign categoryCode = e_list["category_code"]>
<#assign productCode = e_list["product_code"]>


<#assign smallSellerCode=b_method.upFiledByFieldName(skuEditInfo.upBookData(),"small_seller_code").getPageFieldValue() />

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

<#if sellerType=="4497478100050002" || sellerType=="4497478100050003" || sellerType=="4497478100050004">
	<#assign grossProfitLabel="服务费"/>
	<#assign sellPriceLabel="代收价格"/>
	<#assign sellPriceLabelS="代收单价"/>
	<#assign costPriceLabel="应付代收"/>
</#if>

<@m_zapmacro_common_page_book skuEditInfo />

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	</#list>
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
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
</form>
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

<div class="zab_info_page_title  w_clear">
<span>商品SKU信息</span>
</div>
<#assign skuDetail=b_method.upControlPage("page_chart_v_cf_modSku_pc_skuinfo_cshop_ro","zw_f_product_code=" + e_list["product_code"] + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />

</div>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table skuDetail>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list skuDetail.getPageHead() as e_list>
		      	 <th>
		      	 	 <#if e_list_index = 3>
			      	 	 	${sellPriceLabelS}
		      	 	 	 <#elseif e_list_index = 9>
		      	 	 	 	${costPriceLabel}
			      	 	 <#else>
				      	 	${e_list}
			      	 	 </#if>
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list skuDetail.getPageData() as e_list>
		<#assign sku_code = e_list[0]> 
			<tr>
	  		 <#list e_list as e>
	  		 <#if e_index = 7>
	  		 <td>
	  		 	<div class="w_left w_w_100">
					<a target="_blank" href="${e?default("")}">
						<img src="${e?default("")}">
					</a>
				</div>
	  		 </td>
	  		 
	  		 <#elseif e_index = 4>
	  		 	<#assign stores = storeService.getStockNumBySkuBySum("${sku_code}")> 
  					<td>	
  						${stores}
  					 </td>
	  		 	
	  		  <#elseif e_index = 8>
  					<td>	
  						<#if e = 'Y'>
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