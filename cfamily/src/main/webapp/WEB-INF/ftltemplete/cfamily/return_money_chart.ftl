<script>
function exportDialog(){
		zapjs.f.window_box({
			id : 'bdwmOrder',
			content : '<iframe src="../page/page_upload_vv_oc_return_money_cf?xls=ReturnMoneyConfirm.xls' 
			+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
}
</script>
<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire b_page />
</div>
<hr/>

<#if RequestParameters['zw_f_product_channel']?? && RequestParameters['zw_f_product_channel'] != ''>
	<#assign e_pagedata=m_page_helper("com.cmall.familyhas.pagehelper.ChartByProductChannel",b_page) />
<#else>
	<#assign e_pagedata=b_page.upChartData()>
</#if>

<div class="zw_page_common_data">

<p style="color: red">微匠支付退款：只需把退款单状态变更为“退款中”，待微匠平台系统退款后系统会自动变更退款单状态为“已退款”。</p>
<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>

<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      		<#if e_list_index=10>
      			<th>
      			银行
      			</th>
	      		</#if>		      	 
	      </#list>
	    </tr>
  	</thead>
  <#assign returnMoneyService=b_method.upClass("com.cmall.ordercenter.service.ReturnMoneyService")>
    <#assign scDefineService=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>
  <#assign defMap = scDefineService.defineMap("44974628")> 
  <#assign bankMap = scDefineService.defineMap("44974798")> 
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			 <#assign returnMoneyMap = b_method.upDataOne("oc_return_money","","","","return_money_code","${e_list[0]}")> 
			 <#assign returnMoneyExt = returnMoneyService.extInfo(e_list[e_pageField?seq_index_of("order_pay_seq")])> 
	  		 <#list e_list as e>
	      		<td>
	      		<!--${e_pageField[e_index]}-->
	      		<#if e_pageField[e_index] == 'buyer_code'>
	      			<#if e_pagedata.getPageData()?size lt 2>
	  		 			${memberLoginSupport.getMemberLoginName(e)}
	  		 		</#if>
	  		 	<#elseif e_pageField[e_index] == 'order_big_code'>
	  		 		<#if returnMoneyExt.getPaygate_order_code() != "">
	  		 			${returnMoneyExt.getPaygate_order_code()}
	  		 		<#else>
	  		 			${returnMoneyExt.getBig_order_code()}
	  		 		</#if>
	  		 	<#elseif e_pageField[e_index] == 'order_pay_seq'>
	  		 		${returnMoneyExt.getPay_sequenceid()}
	  		 	<#elseif e_pageField[e_index] == 'order_pay_type'>
	  		 		${defMap[returnMoneyExt.getPay_type()]?default("")}
	  		 	<#else>
		      		<#if e_pageField[e_index] == '状态变更' && returnMoneyExt.getPay_type() == "449746280016" && returnMoneyMap["status"] == "4497153900040003">
		      			<input zapweb_attr_operate_id="92092ee50f5011e5a451005056925439" class="btn btn-small" onclick="require(['cmanage/js/flowbussiness_new'],function(a){a.callChangeFlow('449715390004','${returnMoneyMap["uid"]}','${returnMoneyMap["poundage"]}','${returnMoneyMap["return_money"]}','4497153900040006')})" type="button" value="状态变更"/>
		      		<#else>
		      			${e?default("")}
		      		</#if>	  		 		
	  		 	</#if>
	      		</td>
	      		
	      		<#if e_pageField[e_index] == 'order_pay_seq'>
	      			<td>
	      			${bankMap[returnMoneyExt.getPay_bank()]?default("")}
	      			</td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>







<@m_zapmacro_common_page_pagination b_page  e_pagedata />

</div>

