<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_js ["cfamily/js/select2/select2.js"]/>
<style type="text/css">
	.redColor{color:#FF0000;}
</style>
<script>
$(document).ready(function() {
	//$("#zw_f_product_status option[value='4497153900060001']").remove();
	$("#zw_f_product_status option[value='4497153900060004']").remove();
	$("#zw_f_brand_code").select2();
	$("#s2id_zw_f_brand_code").attr("style","width:220px");
});
</script>
	
<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#-- 页面主内容显示  -->
<#assign e_page=b_page>
<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire e_page />
</div>
<hr/>
<#assign e_pagedata=e_page.upChartData()>
<div class="zw_page_common_data">
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      	 	<#if e_list_index==2 >
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
			      	<#elseif e_index==8>
				      	<td>
				      		<#if e_list[3]="已下架">
				      			${e?default("")}
				      		<#elseif e_list[3]="已上架">
				      			<a href="page_modproduct_v_cf_pc_productinfo_cshop_up?zw_f_uid=${productCode}" class="btn btn-link" target="_blank">一般信息修改</a>
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
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>
