<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<#assign  a_small_seller_code=b_method.upFiledByFieldName(b_page.upBookData(),"small_seller_code").getPageFieldValue() />
<div class="zab_info_page">


<!-- <div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">
	
	<#assign  orderMap=b_method.upDataOneOutNull("oc_orderinfo","order_status,seller_code,small_seller_code","","order_code=:order_code","order_code",(a_order_code?default("")))/> 
	<#if orderMap??>
        <#if orderMap['order_status']='4497153900010002'&&orderMap['seller_code']='SI2003'&&orderMap['small_seller_code']!='SI2003'>
			<input class="btn btn-small btn-primary" zapweb_attr_operate_id="4a33419cadcc4cad860fa6e395cfd0d9" onclick="zapjs.zw.func_tip(this,'${a_order_code?default("")}','取消发货')" type="button" value="取消发货">
        </#if>
    </#if>
	
	
		
<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}')" type="button" value="添加备注">
</div> -->

<div class="zab_info_page_title  w_clear">
<span>订单信息</span>
</div>

<#assign a_order_info=b_method.upControlPage("page_book_v_cf_oc_orderinfo1","zw_f_order_code="+a_order_code)>

<#-- 不要问我为什么坑你，我也被坑着 -->
<form class="form-horizontal" method="POST" >
	<#list a_order_info.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e a_order_info/>
	</#list>
	
	
	<div class="control-group">
	<label class="control-label" for="">发货商:</label>
	<div class="controls">

	      		<div class="control_book">
	      			<#-- 修改订单查询发货商获取方式 2016-09-07 zhy -->
	      			<#--
	      			<#if a_small_seller_code==''>
	      				LD系统
	      				<#elseif a_small_seller_code=='SI2003'>
	      				LD系统
	      				<#elseif a_small_seller_code=='SF03KJT'>
	      				跨境通
	      				<#else>
	      				商户
	      			</#if>
	      			-->
	      			<#assign searchOrderService=b_method.upClass("com.cmall.familyhas.service.SearchOrderService")/>
	      			<#assign sellerType = searchOrderService.getSellerType(a_small_seller_code)>
	      			${sellerType}
	      		</div>
	</div>
	</div>
	
	<div class="control-group">
	<label class="control-label" for="">商户名称:</label>
	<div class="controls">

	      		<div class="control_book">
	      		<#assign sellerInfoService=b_method.upClass("com.cmall.familyhas.service.CSellerInfoService")>
		      		 <#assign sellerName = sellerInfoService.getSellerName(a_small_seller_code)> 
			  		 ${sellerName}
	      		</div>
	</div>
	</div>
	
	<#if a_small_seller_code == "SF031JDSC"> 
	<div class="control-group">
		<label class="control-label" for="">京东订单运费:</label>
		<div class="controls">
	  		<div class="control_book">
	  		<#assign  jdOrder=b_method.upDataOneOutNull("oc_order_jd","freight","","order_code=:order_code","order_code",a_order_code) />
		  		 ${jdOrder["freight"]}
	  		</div>
		</div>
	</div>	
	</#if>
	
	
</form>





<div class="zab_info_page_title  w_clear">
<span>商品详细</span>
</div>
<#assign a_order_detail=b_method.upControlPage("page_chart_v_cf_oc_orderdetail","zw_f_order_code="+a_order_code).upChartData()>
<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
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
	      		<#assign  sku=productService.getSkuInfo(e_list[1]) />
	      		<td>
	      			<#if e_index==4>
	      				${sku.getSkuKeyvalue()}
	      			<#elseif e_index==5>
	      				
	      				<#if sku.getSmallSellerCode()=="SI2003">
	      					
	      					<#if sku.getValidateFlag()=="Y">
	      						厂商配送
	      					<#else>
	      						仓库配送
	      					</#if>
	      					
	      				<#else>
	      					&nbsp;
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
	      				  <#assign  address_s=b_method.upDataOneOutNull("sc_tmp","name,code_lvl","","code=:code and show_yn = :show_yn","code",(area_code),"show_yn","Y") />
	      				  	${address_s["name"]?default("")}&nbsp;
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

<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_order_shipments_cf","zw_f_order_code="+a_order_code+"&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_order_shipments />


</div>

<div class="zab_info_page_title  w_clear">
<span>运单流水</span>
</div>
<#assign waybillMap=b_method.upDataOneOutNull("oc_order_shipments","waybill","","order_code=:order_code","order_code",(a_order_code?default("")))/>
<#if (waybillMap['waybill'])??>
	<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_express_detail","zw_f_order_code="+a_order_code+"&" +"zw_f_waybill="+waybillMap['waybill']+"&"+ "zw_p_size=-1").upChartData()>
<#else>
	<#assign a_order_shipments=b_method.upControlPage("page_chart_v_oc_express_detail","zw_f_order_code="+a_order_code+"&" + "zw_p_size=-1").upChartData()>
</#if>
<#assign a_order_express=b_method.upControlPage("page_chart_v_oc_order_tracking","zw_f_order_code="+a_order_code+"&" + "zw_p_size=-1").upChartData()>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list a_order_express.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list a_order_express.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
	 	<#list a_order_shipments.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</div>


<div class="zab_info_page_title  w_clear">
<span>日志流水</span>
</div>

<#assign a_order_log=b_method.upControlPage("page_chart_v_lc_orderstatus","zw_f_code="+a_order_code+"&" + "zw_p_size=-1").upChartData()>
<#-- <@m_zapmacro_common_table a_order_log /> -->
<#assign orderStatusLogService=b_method.upClass("com.cmall.ordercenter.service.OrderStatusLogService")>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list a_order_log.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list a_order_log.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			<#if e_index==3>
	      			<#-- 
	      				${orderStatusLogService.converCreateUser(e?default(""))}   -->
	      			<#else>
	      				${e?default("")}
	      			</#if>
	      			
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>




<div class="zab_info_page_title  w_clear">
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






