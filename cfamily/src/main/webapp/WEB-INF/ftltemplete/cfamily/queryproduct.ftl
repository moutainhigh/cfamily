<style type="text/css">
.redColor{
	color:#FF0000;
}
</style>
</br>
<#assign e_page=b_page>


	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
		
	</div>
	<hr/>
	
	<#assign e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
    <#assign e_pageField=e_pagedata.getPageField() />	
	<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	 
		      	 <!--商品分类追加到商品售价后面-->
	      	 	<#if e_pageField[e_list_index] == "sell_price" >
				 	 <th>商品分类</th>
				</#if>
		      	 
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<#assign productCode=e_list[0]>
	  		 <#list e_list as e>
	  		 	 <#if e_index==0 >
	  		 	 	<td>
		      			<a target="_blank" href="../page/page_preview_v_pc_productDetailInfo?zw_f_product_code=${e?default('')}">${e?default("")}</a>
		      		</td>
		  		 <#elseif e_pageField[e_index] == "sell_price" >
		      		<td>
		      			${e?default("")}
		      		</td>
		      		<td>
		      			${m_page_helper("com.cmall.familyhas.pagehelper.ProductCategoryHelper",e_list[0])}
		      		</td>
		  		 <#elseif e_pageField[e_index] == "uc_seller_type" >
		      		<td>
		      			<#if e == "4497478100050000" >
		      				LD
		      			<#else>
		      				${m_page_helper("com.cmall.familyhas.pagehelper.ScDefineNameHelper","449747810005",e)}
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


<!-- 商户类型特殊处理 -->
<#macro m_zapmacro_common_field_seller_type e_field  e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#assign sellerTypeList = b_method.upDataQuery("sc_define","","","parent_code","449747810005")>
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<option value="">请选择</option>
	      			<option value="4497478100050000" <#if  e_field.getPageFieldValue() == "4497478100050000"> selected="selected" </#if>>LD</option>
	      			<#list sellerTypeList as sellerType>
						<option value="${sellerType["define_code"]}" <#if  e_field.getPageFieldValue() == sellerType["define_code"]> selected="selected" </#if>>${sellerType["define_name"]}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field_custom e_field   e_page>
	
		<#if e_field.getPageFieldName() == "zw_f_uc_seller_type">
			<@m_zapmacro_common_field_seller_type e_field e_page/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e_field e_page/>
	  	</#if>
</#macro>

<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
		<#if e.getPageFieldName() == "zw_f_uc_seller_type">
			<@m_zapmacro_common_field_seller_type e e_page/>
		<#elseif e.getQueryTypeAid()=="104009002">
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
	