<#-- 查看页 -->
<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>优惠码明细</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
<#assign createTime = b_page.getReqMap()["zw_f_create_time"] >

<#assign logData=b_method.upControlPage("page_chart_v_v_oc_cdkey_detail","zw_f_activity_code=${activityCode}&zw_f_create_time=${createTime}&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=logData />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if e_list_index = 6 || e_list_index = 7 || e_list_index = 8>
	        <#else>
	       	  <th>
	      	 	${e_list}
	      	 </th>
	        </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
				<#assign brandCodes=e_list[6]>
				<#assign productCodes=e_list[7]>
				<#assign categoryCodes=e_list[8]>
		  		<#list e_list as e>
		  			<#if e_index = 5>
		  				<#if e_list[5]?default("") == "" || e_list[5]?default("") == "4497471600070001">
		  					<td>
		  						无限制
		      				</td>
		  				<#elseif e_list[5]?default("") == "4497471600070002">
		  					<#assign brandService=b_method.upClass("com.cmall.familyhas.service.BrandService")>
			      			<#assign brandMap=brandService.getBrandNames(brandCodes)>
		      				<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
			      			<#assign categoryMap=sellercategoryService.getCateGoryNmaes(categoryCodes,"SI2003")>
		  					<td>
		  						<#if brandCodes != "">
		  						品牌编号：${brandMap.brandNames?default("")}<br>
		  						</#if>
		  						<#if categoryCodes != "">
		  						分类编号：${categoryMap.categoryName?default("")}<br>
		  						</#if>
		  						<#if productCodes != "">
		  						商品编号：${e_list[7]?default("")}
		  						</#if>
		  						
		      				</td>
		      			<#else>
		      				<td>
		  						${e?default("")}
		      				</td>
		  				</#if>
		  			<#elseif e_index = 6 || e_index = 7 || e_index = 8>
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
