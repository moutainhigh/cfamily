<@m_common_html_css ["cmanage/css/cmb_base.css"] />

<div class="cmb_cmanage_page">



<div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">
<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<#assign  orderStatus="" />
<#list b_page.upBookData()  as e_field>
	<#if  e_field.getPageFieldName()="zw_f_order_status">
		<#assign  orderStatus=e_field.getPageFieldValue()?default("") />
		<#if  orderStatus="4497153900010002">
		
			<input  class="btn btn-small btn-primary" onclick="zapadmin.window_url('../show/page_add_v_oc_order_shipments?zw_f_order_code=${a_order_code?default("")}')" type="button" value="发货">
		
		</#if>
	</#if>
</#list>


<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}')" type="button" value="添加备注">
</div>


<div class="cmb_cmanage_page_title  w_clear">
<span>订单信息:</span>
</div>
<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>
<form class="form-horizontal" method="POST" >
	<#list b_page.upBookData()  as e>
		
		<#if e.pageFieldName=='zw_f_buyer_code'>
			
			<div class="control-group">
			<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${memberLoginSupport.getMemberLoginName(e.pageFieldValue)}
							</div>
				</div>
			</div>
		<#else>
			<@m_zapmacro_common_book_field e b_page/>
		</#if>
	</#list>
</form>











<div class="cmb_cmanage_page_title  w_clear">
<span>配送信息:</span>
</div>
<#assign a_order_address=b_method.upControlPage("page_chart_v_oc_orderadress_forseller","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_address />


<div class="cmb_cmanage_page_title  w_clear">
<span>商品详细:</span>
</div>
<#assign a_order_detail=b_method.upControlPage("page_chart_v_oc_orderdetail_forseller","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_detail />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table a_order_detail >
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list a_order_detail.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list a_order_detail.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      		<#if (e_index ==4)>
      				<#assign  skuKeyValue=b_method.upDataBysql("pc_skuinfo","SELECT sku_keyvalue from pc_skuinfo where sku_code=:sku_code","sku_code",(e?default(""))) />
					<#list skuKeyValue as ea_key>
							${ea_key.sku_keyvalue}
						</#list>
  				<#else>
      				${e?default("")}
      			</#if>
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>


<div class="cmb_cmanage_page_title  w_clear">
<span>配送商信息:</span>
</div>
<#assign oc_order_shipments=b_method.upControlPage("page_chart_v_oc_order_shipments","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table oc_order_shipments />

<div class="cmb_cmanage_page_title  w_clear">
<span>商品参与的活动信息:</span>
</div>
<#assign oc_order_activity=b_method.upControlPage("page_chart_v_oc_order_activity","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table oc_order_activity />

<div class="cmb_cmanage_page_title  w_clear">
<span>支付信息:</span>
</div>
<#assign oc_order_pay=b_method.upControlPage("page_chart_v_oc_order_pay","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table oc_order_pay />

<div class="cmb_cmanage_page_title  w_clear">
<span>日志流水:</span>
</div>

<#assign a_order_log=b_method.upControlPage("page_chart_v_lc_orderstatus","zw_f_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_log />


<div class="cmb_cmanage_page_title  w_clear">
<span>订单备注:</span>
</div>

<#assign a_order_remark=b_method.upControlPage("page_chart_v_oc_order_remark","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table a_order_remark />


</div>
