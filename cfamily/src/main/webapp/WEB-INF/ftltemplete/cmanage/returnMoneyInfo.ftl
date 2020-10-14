<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<div class="cmb_cmanage_page_title  w_clear">
<span>退款信息</span>
</div>

<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>
<#assign sellerInfoService=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>
<#assign returnMoneyService=b_method.upClass("com.cmall.ordercenter.service.ReturnMoneyService")>
<#assign  order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() /> 
<#assign  return_goods_code=b_method.upFiledByFieldName(b_page.upBookData(),"return_goods_code").getPageFieldValue() /> 
<#assign  return_money_code=b_method.upFiledByFieldName(b_page.upBookData(),"return_money_code").getPageFieldValue() /> 
<#assign  returnMoneyExt=returnMoneyService.extMoneyInfoStr(order_code,return_money_code) /> 

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
			
			<#--
			
			<#elseif e.pageFieldName=='zw_f_small_seller_code'>
			
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
			 
				<div class="controls">

							<div class="control_book">
								${sellerInfoService.getSellerShortName(e.pageFieldValue)}
							</div>
				</div>
			</div>
			-->
			
			
		<#elseif e.pageFieldName=='zw_f_order_big_code'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getBig_order_code()}
							</div>
				</div>
			</div>
			
		<#elseif e.pageFieldName=='zw_f_order_pay_seq'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getPay_sequenceid()}
							</div>
				</div>
			</div>
			
		<#elseif e.pageFieldName=='zw_f_order_pay_type'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getPay_type_name()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_payed_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_payed_money1()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_returned_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_returned_money1()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_rr_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_rr_money1()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_current_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_current_money1()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_money1()}
							</div>
				</div>
			</div>	
			
		<#elseif e.pageFieldName=='zw_f_order_rr_transport_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
				<div class="controls">
							<div class="control_book">
								${returnMoneyExt.getOrder_rr_transport_money1()}
							</div>
				</div>
			</div>	
			
		<#else>
			<@m_zapmacro_common_book_field e b_page/>
			
		</#if>
	</#list>
</form>













<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />


<#assign  a_exchange_no=b_method.upFiledByFieldName(b_page.upBookData(),"return_money_code").getPageFieldValue() />
<#--
<div class="cmb_cmanage_page_title  w_clear">
<span>退款详细信息</span>
</div>

<#assign a_exchang_detail=b_method.upControlPage("page_chart_v_oc_return_money_detail","zw_f_return_code="+a_exchange_no+"&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_exchang_detail />

 -->

<div class="cmb_cmanage_page_title  w_clear">
<span>日志流水</span>
</div>
<#assign a_exchange_log=b_method.upControlPage("page_chart_v_lc_return_money_status","zw_f_return_money_no="+a_exchange_no+"&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_exchange_log />



<div class="cmb_cmanage_page_title  w_clear">
<span>退款确认原因</span>
</div>

<#assign a_order_cancel_remark=b_method.upControlPage("page_chart_v_oc_return_money_remark","zw_f_order_code="+a_order_code+"&" + "zw_p_size=1").upChartData()>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list a_order_cancel_remark.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list a_order_cancel_remark.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			<#if e_index==1>
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
































<div class="cmb_cmanage_page_title  w_clear">
<span>订单备注</span>
</div>

<#assign a_order_remark=b_method.upControlPage("page_chart_v_oc_order_remark","zw_f_order_code="+a_order_code+"&" + "zw_p_size=-1").upChartData()>
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



