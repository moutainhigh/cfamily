<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_js ["cfamily/js/select2/select2.js"]/>
<style type="text/css">
	.redColor{color:#FF0000;}
</style>
<script>
$(document).ready(function() {
	$("#zw_f_product_status option[value='4497153900060001']").remove();
	$("#zw_f_product_status option[value='4497153900060004']").remove();
	$("#zw_f_brand_code").select2();
	$("#s2id_zw_f_brand_code").attr("style","width:220px");
});
</script>
	
<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign plusSupportProduct=b_method.upClass("com.srnpr.xmassystem.support.PlusSupportProduct")>
<#-- 页面主内容显示  -->
<#assign e_page=b_page>
<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire e_page />
</div>
<hr/>
<#if RequestParameters['zw_f_product_cate']?? && RequestParameters['zw_f_product_cate'] != ''>
	    <#-- 商品分类条件特殊处理 -->
		<#assign e_pagedata=productService.upChartDataByProductCate(e_page) />
<#else>
		<#assign e_pagedata=e_page.upChartData()>
</#if>
<#macro m_zapmacro_common_auto_inquire e_page>	
	<#list e_page.upInquireData() as e>
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	</#list>
	<div class="control-group">
		<label class="control-label" for="zw_f_product_cate">商品分类</label>
		<div class="controls">
			<select id="zw_f_product_cate" name="zw_f_product_cate">
				<option value="">请选择</option>
				<#assign categoryMap=sellercategoryService.getCateGoryLevel2()>
				<#list categoryMap as map>
					<#if RequestParameters['zw_f_product_cate']?default('')==map['category_code']>
						<option value="${map['category_code']?trim}" selected>${map['category_name']?trim}</option>
					<#else>
						<option value="${map['category_code']?trim}">${map['category_name']?trim}</option>
					</#if>					
				</#list>
			</select>
		</div>
	</div>	
	
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>
<div class="zw_page_common_data">
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      	 	<#if e_list_index==2 >
						 	 <th>当前售价</th>
						 	 <th>商品分类</th>
						 	 <th>预览</th>
						</#if>
			      	 
		      </#list>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign productCode=e_list[0]>
		  		 <#list e_list as e>
	      			<#if e_index==2 >
	      				<td>
			      			${e?default("")}
			      		</td>
				 		 <td>
							<#assign sellPrice=productService.getNowPriceByProductCode(productCode)>
							${sellPrice}
						</td>
				 		 <td>
							<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
							<#assign keys=categoryMap?keys>
							<#list keys as key>
								${categoryMap[key]?trim}<br>
							</#list>
						</td>
						<td>
							<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${productCode}" target="_blank">预览</a>
						</td>
					<#elseif e_index==1>
						<td>
							<#assign mainPicUrl=productService.getMainPicUrl(productCode)>
							<front id="pic_tip_${e_list_index}" class="easyui-tooltip" > ${e?default("")} </front>
							
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
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>
		<script type="text/javascript">
			
			function closeImportWindow (flg,sId,msg) {
			
				zapjs.f.window_close(sId);
				if(flg == true) {
					zapadmin.model_message('已提交审核');
					zapjs.f.autorefresh();
				} else {
					zapadmin.model_message(msg);
				}
			
			}
			
			
		</script>