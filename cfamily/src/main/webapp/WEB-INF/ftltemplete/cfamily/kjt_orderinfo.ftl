
<div class="zab_info_page">


<div class="zab_info_page_title  w_clear">
<span>发货单详情</span>
</div>
<@m_zapmacro_common_page_book b_page />


<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<#assign  order_code_seq=b_method.upFiledByFieldName(b_page.upBookData(),"order_code_seq").getPageFieldValue() />

<div class="zab_info_page_title  w_clear">
<span>配送信息</span>
</div>
<#assign a_order_address=b_method.upControlPage("page_book_v_oc_orderadress_cf","zw_f_order_code="+a_order_code)>
<@m_zapmacro_common_page_book a_order_address />


<div class="zab_info_page_title  w_clear">
<span>商品明细</span>
</div>
<#assign a_order_detail=b_method.upControlPage("page_chart_v_oc_order_kjt_detail","zw_f_order_code_seq="+order_code_seq).upChartData()>
<@m_zapmacro_common_table a_order_detail />


</div>






