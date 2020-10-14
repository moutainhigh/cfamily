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
					 	 <th>商品分类-冲厕所</th>
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
				      		<#if e_list[4]="已下架">
				      			${e?default("")}
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
	

	