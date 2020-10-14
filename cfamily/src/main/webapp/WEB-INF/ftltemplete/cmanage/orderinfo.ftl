
<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<div class="zab_info_page">



<div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">
<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}')" type="button" value="添加备注">
</div>



<div class="zab_info_page_title  w_clear">
<span>订单信息</span>
</div>


<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>

<form class="form-horizontal" method="POST" >
	<#list b_page.upBookData()  as e>
		
		<#if e.pageFieldName=='zw_f_buyer_code'>
			
			<div class="control-group">
			<#-- 
				<label class="control-label" for="">${e.fieldNote}:</label>
			 -->
			 <label class="control-label" for="">买家手机号:</label>
			 
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






















<div class="zab_info_page_title  w_clear">
<span>商品详细</span>
</div>
<#assign a_order_detail=b_method.upControlPage("page_chart_v_oc_orderdetail_cn","zw_f_order_code="+a_order_code).upChartData()>
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

<form class="form-horizontal" method="POST" >
	<#list a_order_address.upBookData()  as e>
	  	<@m_zapmacro_common_field_show1 e a_order_address/>
	  	 
	</#list>
</form>


<#macro m_zapmacro_common_field_show1 e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
	      		
	      			<#if  e_field.getColumnName()=="address">
	      			
	      				<#assign  area_code=b_method.upFiledByFieldName(e_page.upBookData(),"area_code").getPageFieldValue() /> 
	      				
	      				
	      				 <#assign  addressp=b_method.upDataOneOutNull("sc_tmp","name","","code=:code","code",(area_code?substring(0,2))+"0000") />
		      			${addressp["name"]}
		      			 &nbsp;
		      			  <#assign  addressc=b_method.upDataOneOutNull("sc_tmp","name","","code=:code and show_yn = :show_yn","code",(area_code?substring(0,4))+"00","show_yn","Y") />
		      			${addressc["name"]}
		      			 &nbsp;
		      			  <#assign  addressd=b_method.upDataOneOutNull("sc_tmp","name","","code=:code and show_yn = :show_yn","code",(area_code?substring(0,6)),"show_yn","Y") />
		      			${addressd["name"]}&nbsp;
	      				
	      			
	      			</#if>
	      		
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
		      		<#else>
		      		 ${e_field.getPageFieldValue()?default("")}
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>





<div class="zab_info_page_title  w_clear">
<span>支付信息:</span>
</div>
<#assign oc_order_pay=b_method.upControlPage("page_chart_v_oc_order_pay","zw_f_order_code="+a_order_code).upChartData()>
<@m_zapmacro_common_table oc_order_pay />




<div class="zab_info_page_title  w_clear">
<span>发货信息</span>
</div>

<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_order_shipments","zw_f_order_code="+a_order_code).upChartData()>
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
<span>订单备注:</span>
</div>

<#assign a_order_remark=b_method.upControlPage("page_chart_v_oc_order_remark","zw_f_order_code="+a_order_code).upChartData()>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list a_order_remark.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list a_order_remark.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			<#if e_index==4||e_index==5||e_index==6>
	      				<#if e!="">
	      					<a target="_blank" href="${e?default("")}">
							图片
						</a>
	      				</#if>
	      			<#else>
	      				${e?default("")}
	      			</#if>
	      			
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>


</div>


