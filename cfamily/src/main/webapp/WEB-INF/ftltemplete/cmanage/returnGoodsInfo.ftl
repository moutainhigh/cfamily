<#include "../zappage/jd_after_sale.ftl" />
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">
<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<#assign  return_code=b_method.upFiledByFieldName(b_page.upBookData(),"return_code").getPageFieldValue() />
<#assign  status=b_method.upFiledByFieldName(b_page.upBookData(),"status").getPageFieldValue() />

<#assign afterSaleOrderForGetTime=b_method.upClass("com.cmall.familyhas.service.AfterSaleOrderForGetTime")>
<#assign timeMap=afterSaleOrderForGetTime.getDeadLineForFillShipments(return_code)>


<div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">
		<#if status=='4497153900050005'>
			<span style="color:#000000;">距离物流完善过期时间还剩：</span>
			<span style="color:#FF0000;">${timeMap['day']}</span>
			<span style="color:#000000;">天</span>
			<span style="color:#FF0000;">${timeMap['hour']}</span>
			<span style="color:#000000;">小时</span>
			<span style="color:#FF0000;">${timeMap['min']}</span>
			<span style="color:#000000;">分</span>
		</#if>
		<#if status=='4497153900050003'||status=='4497153900050002'>
		
			<input class="btn btn-small btn-primary" onclick="returnGoodsInfo.approve('cancel','${return_code}')" type="button" value="取消退货">
			<input class="btn btn-small btn-primary" onclick="returnGoodsInfo.approve('xxx','${return_code}')" type="button" value="客服确认">
		</#if>
	<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}&zw_f_asale_code=${return_code?default("")}')" type="button" value="添加备注">
</div>



<div class="cmb_cmanage_page_title  w_clear">
<span>退货信息</span>
</div>
<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>

<#assign returnMoneyService=b_method.upClass("com.cmall.ordercenter.service.ReturnMoneyService")>


<input type="hidden" value="${return_code}" id="return_code">

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
			
		
		<#elseif e.pageFieldName=='zw_f_due_money'>
			
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
			 
				<div class="controls">

							<div class="control_book">
								${returnMoneyService.orderMoneyStr(a_order_code)}
							</div>
				</div>
			</div>
			<#--  
		<#elseif e.pageFieldName=='zw_f_rr_money'>
			<div class="control-group">
				<label class="control-label" for="">${e.fieldNote}:</label>
			 
				<div class="controls">

							<div class="control_book">
								${returnMoneyService.returnMoney(a_order_code)}
							</div>
				</div>
			</div>
		
		 -->
		<#else>
			<@m_zapmacro_common_book_field e b_page/>
			
		</#if>
	</#list>
</form>


<#assign  b_exchange_no=b_method.upFiledByFieldName(b_page.upBookData(),"return_code").getPageFieldValue() />
<#assign  small_seller_code=b_method.upFiledByFieldName(b_page.upBookData(),"small_seller_code").getPageFieldValue() />

<!--京东订单需要判断一下有售后地址的时候才显示-->
<#assign showJdAddress = false />
<#if small_seller_code == "SF031JDSC">
	<#if (b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",return_code))??>
		<#assign jdAfterSale = b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",return_code) />
		<#if jdAfterSale.afs_address != ''>
			<#assign showJdAddress = true />
		</#if>
	</#if>
</#if>

<#if showJdAddress || small_seller_code != "SF031JDSC">
<div class="cmb_cmanage_page_title  w_clear">
<span>售后地址信息</span>
</div>	
										 
<#assign e_page=b_method.upControlPage("page_book_v_oc_return_goods_cxx","zw_f_return_code="+b_exchange_no)>
<form class="form-horizontal" method="POST" >
	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>	
	
		<div class="control-group">
			<div class="controls">
				<div class="control_book">
					<#assign duohzAfterSaleSupport=b_method.upClass("com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport")>
					<#if !duohzAfterSaleSupport.checkDuohzStore(return_code)>
				    	<input type="button" class="btn  btn-success" zapweb_attr_operate_id="623ae751a3d511e5aad9005056925439" onclick="returnGoodsInfo.changeAddr('${small_seller_code}')" value="选择地址">
					</#if>
				</div>
			</div>
		</div>
	
	<div class="control-group">
		<div class="controls">
		      		<div class="control_book">
			      		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="623ae751a3d511e5aad9005056925439" onclick="returnGoodsInfo.sendMessage()" value="发送短信">
		      		</div>
		</div>
	</div>
	
	
</form>
</#if>

<#-- 京东售后信息  -->
<#if small_seller_code == "SF031JDSC">
	<@m_zapmacro_jd_after_sale return_code />
</#if>

<div class="cmb_cmanage_page_title  w_clear">
<span>用户售后物流</span>
</div>
<#assign v_oc_order_shipments_sale=b_method.upControlPage("page_chart_v_oc_order_shipments_sale","zw_f_order_code="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<#-- 如果没有售后物流则显示添加按钮  -->
<#if v_oc_order_shipments_sale.getPageData()?size == 0>
<p style="text-align: right"><input type="button" class="btn  btn-success" zapweb_attr_operate_id="" onclick="zapadmin.window_url('page_add_v_oc_order_shipments_sale?asale_code=${return_code}')" value="添加物流信息"></p>
</#if>
<@m_zapmacro_common_table v_oc_order_shipments_sale />

<div class="cmb_cmanage_page_title  w_clear">
<span>退货详细信息</span>
</div>
<#assign a_exchang_detail=b_method.upControlPage("page_chart_v_oc_return_goods_detail","zw_f_return_code="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_exchang_detail />

<div class="cmb_cmanage_page_title  w_clear">
<span>日志流水</span>
</div>											 
<#assign v_exchange_log=b_method.upControlPage("page_chart_v_lc_return_goods_status","zw_f_return_no="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>


<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list v_exchange_log.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list v_exchange_log.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      		
	      			<#if e_index = 3>
							<#if e??>
								<#if e?length lt 1>
									用户
								<#else>
									${e?default("")}
								</#if>
								
							<#else>
								${e?default("")}
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
<span>备注信息</span>
</div>											 
<#assign v_oc_order_remark=b_method.upControlPage("page_chart_v_oc_order_remark","zw_f_asale_code="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list v_oc_order_remark.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list v_oc_order_remark.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      		
	      			<#if e_index = 4 || e_index = 5|| e_index = 6|| e_index = 7|| e_index = 8>
						<a target="_blank" href="${e?default("")}">
							<img src="${e?default("")}" style="height: 50px;">
						</a>
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
<span>售后运单流水</span>
</div>											 
<#assign v_oc_order_tracking_ser=b_method.upControlPage("page_chart_v_oc_order_tracking_ser","zw_f_order_code="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table v_oc_order_tracking_ser />

<div class="cmb_cmanage_page_title  w_clear">
<span>售后运单备注</span>
</div>											 
<#assign v_oc_order_shipments_ext_rem=b_method.upControlPage("page_chart_v_oc_order_shipments_ext_rem","zw_f_order_code="+b_exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table v_oc_order_shipments_ext_rem />



</div>

<@m_common_html_script "require(['cfamily/js/returnGoodsInfo'],function(a){a.init_page()});" />

