

<#assign  small_seller_code=b_method.upFiledByFieldName(b_page.upBookData(),"merchant_code").getPageFieldValue() />
<#assign  start_time=b_method.upFiledByFieldName(b_page.upBookData(),"start_time").getPageFieldValue() />
<#assign  end_time=b_method.upFiledByFieldName(b_page.upBookData(),"end_time").getPageFieldValue() />
<div class="zab_info_page">


<div class="zab_info_page_title  w_clear">
<span>供应商商品结算单</span>
</div>


<@m_zapmacro_common_page_book b_page />




<div class="zab_info_page_title  w_clear">
<span>商品详细</span>
</div>


<#assign small_seller_code_detail=b_method.upControlPage("page_chart_v_oc_bill_product_detail_new","zw_f_small_seller_code="+small_seller_code+"&zw_f_start_time="+start_time+"&zw_f_end_time="+end_time+"&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table small_seller_code_detail />




</div>






