<div class="zab_info_page">
<@m_zapmacro_common_page_book b_page />
</div>
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<#assign  brandCode=b_method.upFiledByFieldName(b_page.upBookData(),"brand_code").getPageFieldValue() />
<#assign brand_product=b_method.upClass("com.cmall.productcenter.service.BrandService")>
<#assign productNum = brand_product.productNumOfBrand(brandCode,"SI2003")>
<form class="form-horizontal" method="POST" >

	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>
	<div class="control-group">
	<label class="control-label" for="">橱窗商品数量:</label>
	<div class="controls">
	<div class="control_book">${productNum}</div>
	</div>
	</div>
	
	
</form>
</#macro>

<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	
	
	  		<@m_zapmacro_common_field_show e_field e_page/>
	  	
</#macro>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
	      			<#elseif e_field.getFieldName()=="brand_pic">
		      			<img src="${e_field.getPageFieldValue()?default("")}" width="100" height="100">
		      		<#else>
		      			${e_field.getPageFieldValue()?default("")}
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>

<#assign bpInfor =b_method.upControlPage("page_chart_v_brand_pc_productinfo","zw_f_brand_code=${brandCode}&zw_f_seller_code=SI2003&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=bpInfor />
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
	  		 <#list e_list as e>
	      		<td>
      				${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>

