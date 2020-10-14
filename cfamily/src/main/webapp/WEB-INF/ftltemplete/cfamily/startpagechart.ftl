
<@m_zapmacro_common_page_chart b_page />


<#-- 列表页  -->
<#macro m_zapmacro_common_page_chart e_page >


	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>

</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>

<#--查询的自动输出判断 -->
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
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<#assign code=e_list[5]>
	  		 <#list e_list as e>
	  		 <#if e_index = 1>
		  		 <td>
		  		 	<div class="w_left w_w_100">
						<a target="_blank" href="${e?default("")}">
							<img src="${e?default("")}">
						</a>
					</div>
		  		 </td>
		  	 <#elseif e_index=5>
	      			<#if e_list[4]?default("") == "分类搜索">
	      				<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
	      				<#assign categoryMap=sellercategoryService.getCateGoryShow(code,"SI2003")>
	      				<td>${categoryMap.categoryName?default("")}</td>
	      			<#elseif e_list[4]?default("") == "商品详情">
	      				<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	      				<#assign product=productService.getProductInfoForMabyLoveChart(code)>
	      				<td>
		      				商品编号：${e?default("")}<br/>
		      				商品名称：${product.productName?default("")}
	      				</td>
	      			<#else>
	      				<td>
			      			${e?default("")}
			      		</td>
	      			</#if>
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