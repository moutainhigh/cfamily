<#-- 商品详细信息页面 -->
<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />
<div class="zab_info_page">

<div class="zab_info_page_title  w_clear">
<span>商品详细信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
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
		<label class="control-label" for="">结算方式:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.settlementType?if_exists} </div>
		</div>
	</div>
	<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
	<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
	<div class="control-group">
		<label class="control-label" for="">商品分类:</label>
		<div class="controls">
			<div class="control_book">
					<#assign keys=categoryMap?keys>
						<#list keys as key>
						${categoryMap[key]}&nbsp;&nbsp;
					</#list>
 			</div>
		</div>
	</div>

</form>
</#macro>
</br>


<div class="zab_info_page_title  w_clear">
<span>商品SKU信息</span>
</div>
<#assign skuDetail=b_method.upControlPage("page_chart_vv_pc_skuinfo","zw_f_product_code=" + productCode + "&" + "zw_p_size=-1").upChartData()>
<#assign storeService=b_method.upClass("com.cmall.systemcenter.service.StoreService")>
<#assign storeMap=storeService.storeMap("SI2003")>
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
			<tr>
				<#assign sku_code = e_list[0]> 
	  		 <#list e_list as e>
	      			<#if (e_index ==6)>
	      				<#-- 替换库存 -->
	      					<#assign stores = storeService.getAllStockNumByStore("${sku_code}")> 
	      					<td title="总库存数:${stores[0]}  <table border=1><tr><th>库存地编码</th><th>库存地名称</th><th>库存数</th></tr>  <#list stores[1]?keys as key>  <#if storeMap[key] ??>    <tr> <td>${key}</td><td>${storeMap[key]}</td><td>${stores[1][key]}</td> </tr></#if> </#list> </table>" class="easyui-tooltip">	
	      						 	${stores[0]}  
	      					 </td>
      					 <#elseif e_index = 3>
		  					<td>	
		  						<#if e = 'Y'>
		  							<span style="color:blue">可卖</span>
		  						<#else>
		  							<span style="color:red">不可卖</span>
		  						</#if>
		  					</td>
	      				<#else>
	      				<td> ${e?default("")} </td>
	      			</#if>
	      			
	      		
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>

</div>
  