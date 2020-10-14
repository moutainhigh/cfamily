<#assign uid=b_page.getReqMap()["zw_f_uid"] />
<#assign skuMap= b_method.upDataOne("pc_skuinfo","","","","uid","${uid}")>
<#assign proMap= b_method.upDataOne("pc_productinfo","","","","product_code","${skuMap['product_code']}")>
<script>
require(['cfamily/js/editSkuProduct'],function(a){a.init_editSku("${proMap["product_name"]}");});
</script>
<@m_zapmacro_common_page_edit b_page />