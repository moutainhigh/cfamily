
<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<div class="zab_info_page">


<div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">
	
	<#assign  orderMap=b_method.upDataOneOutNull("oc_orderinfo","order_status,seller_code,small_seller_code","","order_code=:order_code","order_code",(a_order_code?default("")))/> 
	<#if orderMap??>
        <#if orderMap['order_status']='4497153900010002'&&orderMap['seller_code']='SI2003'&&orderMap['small_seller_code']!='SI2003'>
			<input class="btn btn-small btn-primary" zapweb_attr_operate_id="4a33419cadcc4cad860fa6e395cfd0d9" onclick="zapjs.zw.func_tip(this,'${a_order_code?default("")}','取消发货')" type="button" value="取消发货">
        </#if>
    </#if>
	
	
		
<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}')" type="button" value="添加备注">
</div>

<div class="zab_info_page_title  w_clear">
<span>订单信息</span>
</div>


<@m_zapmacro_common_page_book b_page />





<div class="zab_info_page_title  w_clear">
<span>商品详细</span>
</div>
<#assign a_order_detail=b_method.upControlPage("page_chart_v_cf_oc_orderdetail","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_detail />

<div class="zab_info_page_title  w_clear">
<span>订单活动</span>
</div>
<#assign a_order_activity=b_method.upControlPage("page_chart_v_oc_order_activity","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_activity />

<div class="zab_info_page_title  w_clear">
<span>配送信息</span>
</div>
<#assign a_order_address=b_method.upControlPage("page_book_v_oc_orderadress","zw_f_order_code="+a_order_code)>
<@m_zapmacro_common_page_book a_order_address />



<div class="zab_info_page_title  w_clear">
<span>支付信息:</span>
</div>
<#assign oc_order_pay=b_method.upControlPage("page_chart_v_oc_order_pay","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table oc_order_pay />




<div class="zab_info_page_title  w_clear">
<span>发货信息</span>
</div>

<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_order_shipments_cf","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_shipments />


</div>

<div class="zab_info_page_title  w_clear">
<span>运单流水</span>
</div>

<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_express_detail","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_shipments />


</div>


<div class="zab_info_page_title  w_clear">
<span>日志流水</span>
</div>

<#assign a_order_log=b_method.upControlPage("page_chart_v_lc_orderstatus","zw_f_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_log />




<div class="zab_info_page_title  w_clear">
<span>订单备注</span>
</div>

<#assign a_order_remark=b_method.upControlPage("page_chart_v_oc_order_remark","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_remark />



</div>






