

<div class="cmb_cmanage_page">

<div class="zab_info_page_title w_clear">
<span>审批信息</span>
</div>


<@m_zapmacro_common_page_book b_page />

<#assign  a_flow_code=b_method.upFiledByFieldName(b_page.upBookData(),"flow_code").getPageFieldValue() />


<div class="zab_info_page_title w_clear">
<span>日志流水</span>
</div>

<#assign a_order_log=b_method.upControlPage("page_chart_v_sc_flow_history","zw_f_flow_code="+a_flow_code).upChartData()>
<@m_zapmacro_common_table a_order_log />


</div>
