<script>
function openModSku(ca,pc)
{
	var url = '../page/page_add_v_cf_modSku_pc_skuinfo';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: hidden' frameborder='0'  src='"+url+"?category_code="+ca+"&product_code="+pc+"'></iframe>";
		var option={title:"添加SKU信息",content:productValue,width:640,height:645};
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
<#assign storeService=b_method.upClass("com.cmall.systemcenter.service.StoreService")>
<#assign formatUtil=b_method.upClass("com.cmall.systemcenter.util.FormatUtil")>

<#assign skuEditInfo=b_method.upControlPage("page_book_v_cf_pc_skuDetailInfo","zw_f_product_code="+e_list["product_code"])>
<#assign categoryCode = e_list["category_code"]>
<#assign productCode = e_list["product_code"]>
<@m_zapmacro_common_page_book skuEditInfo />

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
</form>
</#macro>
<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
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
<#assign skuDetail=b_method.upControlPage("page_chart_v_cf_modSku_pc_skuinfo","zw_f_product_code=" + e_list["product_code"] + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<@m_zapmacro_common_table skuDetail />

</div>

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
	  		 <#if e_index = 8>
	  		 <td>
	  		 	<div class="w_left w_w_100">
					<a target="_blank" href="${e?default("")}">
						<img src="${e?default("")}">
					</a>
				</div>
	  		 </td>
	  		 
	  		 <#elseif e_index = 5>
	  		 	<#assign stores = storeService.getAllStockNumByStore("${sku_code}")> 
  					<td>	
  						<front  title="总库存数:${stores[0]} <br> <#list stores[1]?keys as key> ${key}:${stores[1][key]}<br> </#list>" class="easyui-tooltip" > 	${stores[0]}  </front>
  					 </td>
	  		 	
	  		  <#elseif e_index = 9>
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
<script type="text/javascript">  
    $(window).load(function() { 
	 	$('img').mouseover(function(obj){
	 		var height_ = obj.target.naturalHeight;
	 		var width_ = obj.target.naturalWidth;
	 		var msg = "图片暂未加载完成，请稍后!"; 
	 		 
	 		var opt = new Object();
			opt.imageUrl = obj.target.currentSrc;
			api_call('com_cmall_familyhas_api_ApiForImageProperty', opt , function(result){
				var size = result.size;
				msg = "该图片高 = " + height_  + "px | 宽 = " + width_ + "px | 大小 = " + size + "Kb";  
	        	$(obj.target.parentElement).prop("title", msg);   
			}); 
	    });
	}); 
 	function api_call(sTarget, oData, fCallBack) {
		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		}:{api_key : 'jsapi',api_input:''};
		
		//oData = $.extend({}, defaults, oData || {});

		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			//fCallBack(data);			
			fCallBack(data);
		});
	} 
 
</script>