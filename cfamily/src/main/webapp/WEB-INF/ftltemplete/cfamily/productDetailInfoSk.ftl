<script>
function openModSku(ca,pc)
{
	var url = '../page/page_add_v_cf_modSku_pc_skuinfo_batch';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: hidden' frameborder='0'  src='"+url+"?category_code="+ca+"&product_code="+pc+"'></iframe>";
		var option={title:"添加SKU信息",content:productValue,width:1000,height:645};
		zapjs.f.window_box(option);
}
</script>
<#-- 商品详细信息页面 -->
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign productCode=b_method.upFiledByFieldName(b_page.upBookData(),"product_code").getPageFieldValue() />

<#assign product = product_support.upProductInfoJsonForCshop("${productCode}")>
<#assign sku_list = product.productSkuInfoList>

<#assign e_list = product_support.getProductCategoryByUid("${product.uid}")>
<#assign categoryCode = e_list["category_code"]>
<#assign productCode = e_list["product_code"]>
<div class="zab_info_page">

<div class="zab_info_page_title  w_clear">
<span>商品详细信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<div class="control-group">
		<label class="control-label" for="">商品编码:</label>
		<div class="controls">
			<div class="control_book"> ${product.productCode?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品名称:</label>
		<div class="controls">
			<div class="control_book"> ${product.productName?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">市场价格:</label>
		<div class="controls">
			<div class="control_book"> ${product.marketPrice?if_exists?string('0.00')} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">销售价格:</label>
		<div class="controls">
			<div class="control_book"> ${product.minSellPrice?if_exists?string('0.00')}--${product.maxSellPrice?if_exists?string('0.00')}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">毛利润:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.grossProfit?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">税率:</label>
		<div class="controls">
			<div class="control_book"> ${product.taxRatePercent?if_exists}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">品牌名称:</label>
		<div class="controls">
			<div class="control_book"> ${product.brandName?if_exists}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">结算方式:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.settlementType?if_exists}</div>
		</div>
	</div>
	<#assign sc_define_support=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>
	<#assign categoryName = sc_define_support.getDefineNameByCode(product.qualificationCategoryCode)>
	<div class="control-group">
		<label class="control-label" for="">资质品类:</label>
		<div class="controls">
			<div class="control_book"> ${categoryName?if_exists}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">采购类型:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.purchaseType?if_exists}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">入库类型:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.prchType?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">仓库编号:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.oaSiteNo?if_exists} </div>
		</div>
	</div>
		<div class="control-group">
		<label class="control-label" for="">供应商编号:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.dlrId?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">供应商名称:</label>
		<div class="controls">
			<div class="control_book"> ${product.pcProductinfoExt.dlrNm?if_exists} </div>
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
						${categoryMap[key]?if_exists}&nbsp;&nbsp;
					</#list>
 			</div>
		</div>
	</div>
	<#--
	<div class="control-group">
		<label class="control-label" for="">商品状态:</label>
		<div class="controls">
			<div class="control_book"> ${product.productStatus?if_exists}</div>
		</div>
	</div>
	-->
	
	<div class="control-group">
		<label class="control-label" for="">创建时间:</label>
		<div class="controls">
			<div class="control_book"> ${product.createTime?if_exists}</div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">更新时间:</label>
		<div class="controls">
			<div class="control_book"> ${product.updateTime?if_exists}</div>
		</div>
	</div>
	
</form>
</#macro>
</br>
<div class="form-horizontal control-group">
 <a target="_blank" class="btn btn-success" href="page_add_v_cf_modSku_pc_skuinfo_batch?category_code=${categoryCode}&product_code=${productCode}">添加SKU</a>
</div>

<div class="zab_info_page_title  w_clear">
<span>商品SKU信息</span>
</div>
</div>
<#-- 列表的重写输出 -->
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	      	 <th>产品编号</th>
	      	 <th>产品名称 </th>
	      	 <th>规格 </th>
	      	 <th>是否可卖 </th>
	      	 <th>销售价</th>
	      	 <th>成本价</th>
	      	 <th>库存数</th>
	      	 <th>预警库存 </th>
	      	 <th>产品图片</th>
	      	 <th>广告语</th>
	      	 <th>起订数量</th>
	      	 <th>货号</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list sku_list as e_list1>
			<tr>
				<td>
				   <#if e_list1.skuCode != "" && e_list1.skuCode?index_of("WSP") == -1>
						${e_list1.skuCode?if_exists}
					<#else>
						<#assign flowStatus=product_support.getProductFlowStatus("${productCode}")>
						<span style="color:red">${flowStatus}</span>
				   </#if>
				</td>
				<td>
					${e_list1.skuName?if_exists}
				</td>
				<td>
					${e_list1.skuValue?if_exists}
				</td>
				<td>
					<#if e_list1.saleYn = 'Y'>
						<span style="color:blue">可卖</span>
					<#else>
						<span style="color:red">不可卖</span>
					</#if>
				</td>
				<td>
					${e_list1.sellPrice?if_exists?string('0.00')}
				</td>
				<td>
					${e_list1.costPrice?if_exists?string('0.00')}
				</td>
				<td>
					${e_list1.stockNum?if_exists}
				</td>
				<td>
					${e_list1.securityStockNum?if_exists}
				</td>
				 <td>
		  		 	<div class="w_left w_w_100">
						<a target="_blank" href="${e_list1.skuPicUrl}">
							<img src="${e_list1.skuPicUrl}">
						</a>
					</div>
	  		    </td>
	      		<td>
					${e_list1.skuAdv?if_exists}
				</td>
				<td>
					${e_list1.miniOrder?if_exists}
				</td>
				<td>
					${e_list1.sellProductcode?if_exists}
				</td>
	      	</tr>
      	</#list>
	</tbody>
</table>
</div>
  

  