<style type="text/css">
.redColor{
	color:#FF0000;
}
</style>
<#-- 页面主内容显示  -->
	<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
	<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign e_page=b_page>

<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire e_page />
</div>
<hr/>
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<#assign sellerTypeList = b_method.upDataQuery("sc_define","","","parent_code","449747810005")>
		<div class="control-group">
			<label class="control-label" for="uc_seller_type">商户类型</label>
			<div class="controls">
		  		<select name="uc_seller_type" id="uc_seller_type">
		  			<option value="">请选择</option>
		  			<option value="4497478100050000"  <#if RequestParameters['uc_seller_type']?? && RequestParameters['uc_seller_type'] == "4497478100050000"> selected="selected" </#if>>LD</option>
		  			<#list sellerTypeList as sellerType>
						<option value="${sellerType["define_code"]}" <#if RequestParameters['uc_seller_type']?? && RequestParameters['uc_seller_type'] == sellerType["define_code"]> selected="selected" </#if>>${sellerType["define_name"]}</option>
					</#list>
		  		</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="correlation_status">分类关联</label>
			<div class="controls">
		  		<select name="correlation_status" id="correlation_status">
		  			<option value="">请选择</option>
		  			<option value="0"  <#if RequestParameters['correlation_status']?? && RequestParameters['correlation_status'] == "0"> selected="selected" </#if>>未关联</option>
		  			<option value="1"  <#if RequestParameters['correlation_status']?? && RequestParameters['correlation_status'] == "1"> selected="selected" </#if>>已关联</option>
		  		</select>
			</div>
		</div>
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>
	
<#-- 列表 特殊处理 只能这样搞了0.0-->
<#assign e_pagedata=productService.specialUpChartData(e_page) />
<#assign e_pageField=e_pagedata.getPageField() />
<div class="zw_page_common_data">
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		    	<#list e_pagedata.getPageHead() as e_list>
		      	 	<#if e_pageField[e_list_index] == "sell_price" >
						<th>${e_list} </th>
						<th>商品分类</th>
					<#elseif e_pageField[e_list_index] == "validate_flag" >
						<th>${e_list} </th>
						<th>关联分类</th>
						<th>是否显示ld品</th>
					<#elseif e_pageField[e_list_index] == "product_name" >
						<th>${e_list} </th>
						<th>商户类型</th>
					<#else>
						<#if e_list_index == 0 || e_list_index == 1>
		  				<#else>
							<th>${e_list} </th>
		  				</#if>
					</#if>		      	 
		    	</#list>
		    </tr>
	  	</thead>
  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign uid=e_list[0]>
				<#assign small_seller_code=e_list[1]>
				<#assign productCode=e_list[2]>
	  			<#list e_list as e>
				<#if e_pageField[e_index] == "product_name">
					<td>
						<#assign mainPicUrl=productService.getMainPicUrl(productCode)>
						<#-- 目前只能这样查看商品详情 大坑哦 -->
						<#if small_seller_code == "SI2003">
							<a target="_blank" href="../page/page_book_v_cf_pc_skuDetailInfo?zw_f_uid=${uid}">
						<#elseif small_seller_code == "SF031JDSC" || small_seller_code == "SF03WYKLPT">
							<a target="_blank" href="../page/page_book_v_cf_pc_skuDetailInfo_kl?zw_f_uid=${uid}">
						<#else>
							<a target="_blank" href="../page/page_book_v_cf_pc_skuDetailInfo_cshop?zw_f_uid=${uid}">
						</#if>
						<front id="pic_tip_${e_list_index}" class="easyui-tooltip" > ${e?default("")} </front>
						</a>
						<script>
							$(function(){
								$('#pic_tip_${e_list_index}').tooltip({
									position: 'top',
									content: '<img style="width:350px;height:350px;" src="${mainPicUrl?default("")}"/>',
									onShow: function(){
										$(this).tooltip('tip').css({
											borderColor: '#ff0000',
											boxShadow: '1px 1px 3px #292929'
										});
									},
									onPosition: function(){
										$(this).tooltip('tip').css('left', $(this).offset().left);
										$(this).tooltip('arrow').css('left', 20);
									}
								});
							});
						</script>
						
		      		</td>
		      		<td>
		      			${productService.getSellerTypeByProductCode(productCode)}
		      		</td>
		      	<#elseif e_pageField[e_index] == "sell_price">
			      	<td>${e?default("")}</td>
		      		<td>
						<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
						<#assign keys=categoryMap?keys>
						<#list keys as key>
							${categoryMap[key]?trim}<br>
						</#list>
					</td>
				<#elseif e_pageField[e_index] == "validate_flag" >
					<td>${e?default("")}</td>
					<#assign correlation=productService.getProductCorrelation(productCode)>
					<td>
						<#if correlation.correlation_category??>
							<#assign correlationCategory=sellercategoryService.getCateGoryNmaes1(correlation.correlation_category,"SI2003")>
							<#if correlationCategory??>
								${correlationCategory.categoryName?default("")}
							</#if>
						</#if>
					</td>
					<td>
						<#if correlation.show_ld??>
							<#if correlation.show_ld == "1">是<#elseif correlation.show_ld == "0">否<#else></#if>
						</#if>
					</td>
				<#else>
					<#if e_index == 0 || e_index == 1>
	  				<#else>
						<td>${e?default("")}</td>
	  				</#if>
				</#if>
	      	</#list>
	      	</tr>
		</#list>
	</tbody>
</table>
	
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
	

	