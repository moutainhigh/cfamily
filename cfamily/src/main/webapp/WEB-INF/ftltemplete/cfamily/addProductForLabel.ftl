	<#assign keyword = b_page.getReqMap()["zw_f_label_code"] >
	<#assign coupon_support=b_method.upClass("com.cmall.productcenter.service.ProductLabelsService")>
	<#assign limitMap=coupon_support.getLabelProducts(keyword)>
	
	
<script>
require(['cfamily/js/addProductForLabel'],

function()
{
	zapjs.f.ready(function()
		{
		addProductForLabel.init(${limitMap});
		}
	);
}

);
</script>
<@m_zapmacro_common_page_add b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
    <input type="hidden" id="zw_f_label" name="zw_f_label" value="${keyword}">
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
	
	</#list>
	</#if>
	<div style="margin-top:-9px;width:400px">
	<input id="zw_f_product_codes" type="hidden" value="" name="zw_f_product_codes">
	<input id="zw_f_product_codes_show_text" type="hidden" value="">
	<script type="text/javascript">
		zapjs.f.require(['cfamily/js/label_product_select'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_codes","max":"","value":""});});
	</script>
</div>
<p></p>
<table id="productTable"></table><br><br>
</#macro>

