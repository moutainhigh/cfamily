<div class="zab_info_page">

<div class="zab_info_page_title  w_clear">
<span>结算单信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#assign  a_account_code=b_method.upFiledByFieldName(b_page.upBookData(),"account_code").getPageFieldValue() />

<div class="zab_info_page_title  w_clear">
<span>结算单详细信息</span></div><a class="btn  btn-success" target="_blank" href="../export/page_chart_vv_oc_accountinfo_relation?zw_p_size=-1&zw_f_account_code=${a_account_code}">导出</a>


<#assign a_exchang_detail=b_method.upControlPage("page_chart_vv_oc_accountinfo_relation","zw_f_account_code="+a_account_code+"&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_exchang_detail />