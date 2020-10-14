	

<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire b_page />
</div>
<hr/>

<#assign e_pagedata=b_page.upChartData()>
<div class="zw_page_common_data">



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
	      		
	      		<#if e_index=2>
	      			<#if e_pagedata.getPageData()?size lt 2>
	  		 			 ${e?default("")}
	  		 		</#if>
	  		 	<#else>
	      		
	      			${e?default("")}
	      		</#if>
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>









<@m_zapmacro_common_page_pagination b_page  e_pagedata />

</div>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<#if e_field.getPageFieldName() == "zw_f_small_seller_code">
		<#-- 商户名称 -->
		<div class="control-group">
			<label class="control-label" for="zw_f_small_seller_code">商户名称</label>
			<div class="controls">
			<select name="zw_f_small_seller_code" id="zw_f_small_seller_code">
				<option value="">请选择</option>
				<#assign selectList = b_method.upDataQuery("uc_sellerinfo","","small_seller_code IN('SF031JDSC','SF03WYKLPT')")>
				<#list selectList as item>
					<option value="${item.small_seller_code}" ${(item.small_seller_code == e_field.getPageFieldValue())?string('selected','')}>${item.seller_company_name}</option>
				</#list>
			</select>
			</div>
		</div>			
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<@m_zapmacro_common_field_end />		
	</#if>
</#macro>