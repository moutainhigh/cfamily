<@m_zapmacro_common_page_edit b_page />
	
<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
	
	<div class="control-group">
    	<div class="controls">
		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="d09c8fb5d92511e9abac005056165069" id="btnShowProductCount" onclick="xuanpinEdit.onClickProductCount()"  value="查询商品数量" />
		<span id="showProductCount"></span>
    </div>		
</form>
</#macro>
	
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
	  	<@m_zapmacro_common_auto_field_my e e_page/>
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field_my e_field   e_page>
		<#if e_field.getPageFieldName() == "zw_f_category_codes">
			<@m_field_category_codes e_field e_page/>
		<#elseif e_field.getPageFieldName() == "zw_f_brand_codes">
			<@m_field_brand_codes e_field e_page/>	
		<#elseif e_field.getPageFieldName() == "zw_f_uc_seller_type_codes">
			<@m_field_uc_seller_type_codes e_field e_page/>
		<#elseif e_field.getPageFieldName() == "zw_f_product_status_limit">		
			<@m_field_product_status_limit e_field e_page/>
		<#elseif e_field.getPageFieldName() == "zw_f_sell_price_range">		
			<@m_field_sell_price_range e_field e_page/>
		<#elseif e_field.getPageFieldName() == "zw_f_product_stock_range">		
			<@m_field_product_stock_range e_field e_page/>		
		<#elseif e_field.getPageFieldName() == "zw_f_small_seller_codes">		
			<@m_field_small_seller_codes e_field e_page/>						
	  	<#else>
	  		<@m_zapmacro_common_auto_field e_field e_page/>
	  	</#if>
</#macro>

<#macro m_field_category_codes_bak e_field   e_page>
<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<#assign list=b_method.upDataQuery("uc_sellercategory","","","flaginable","449746250001","level","2","seller_code","SI2003") />
	<#list list as e>
		<input type="checkbox" name="${e_field.getPageFieldName()}_checkbox" value="${e['category_code']}" id="${e['uid']}">
		<label for="${e['uid']}">${e['category_name']}</label>
	</#list>
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_uc_seller_type_codes e_field   e_page>
<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">	
	<#assign list=b_method.upDataQuery("sc_define","","","parent_code","44974747") />
	<#list list as e>
		<input type="checkbox" name="${e_field.getPageFieldName()}_checkbox" value="${e['define_code']}" id="${e['uid']}">
		<label for="${e['uid']}">${e['define_name']}</label>
	</#list>
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_product_status_limit e_field   e_page>
<@m_zapmacro_common_field_start text="<span class=\"w_regex_need\">*</span>商品状态限制：" for=e_field.getPageFieldName() />
<input type="hidden" id="${e_field.getPageFieldName()}_hidden" value="${e_field.getPageFieldValue()}">
<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
		<option value="" >不限</option>
		<option value="4497153900060002" >已上架</option>
		<option value="4497153900060001" >待上架</option>
		<option value="4497153900060003" >已下架</option>
</select>
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_sell_price_range e_field   e_page>
<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
<input type="text" class="input-mini" id="${e_field.getPageFieldName()}_start" value="">
-
<input type="text" class="input-mini" id="${e_field.getPageFieldName()}_end" value="">
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_product_stock_range e_field   e_page>
<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
<input type="text" class="input-mini" id="${e_field.getPageFieldName()}_start" value="">
-
<input type="text" class="input-mini" id="${e_field.getPageFieldName()}_end" value="">
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_small_seller_codes e_field   e_page>
<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
<#--后期根据产品需求再进行添加-->
<#assign sellerList=b_method.upDataQuery("uc_sellerinfo","","small_seller_code IN('SF031JDSC','SF03WYKLPT')") />
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
<#list sellerList as seller>
<input type="checkbox" name="${e_field.getPageFieldName()}_checkbox" value="${seller['small_seller_code']}" id="${seller['uid']}">
<label for="${seller['uid']}">${seller['seller_company_name']}</label>
</#list>
<@m_zapmacro_common_field_end />
</#macro>

<#macro m_field_category_codes e_field   e_page>
<div id="categoryTable_panel" class="control-group">
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
<div class="controls">
    <input class="btn" type="button" value="选择分类" onclick="xuanpinEdit.showChooseCategoryBox()">
	<table id="categoryTable" class="controls"></table>
</div>
</div>
</#macro>

<#macro m_field_brand_codes e_field   e_page>
<div id="brandTable_panel" class="control-group">
<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
<div class="controls">
    <input class="btn" type="button" value="选择品牌" onclick="xuanpinEdit.showChooseBrandBox()">
	<table id="brandTable" class="controls"></table>
</div>
</div>
</#macro>

<script>
require(['cfamily/js/xuanpinEdit'],function(a){a.init()});
</script>