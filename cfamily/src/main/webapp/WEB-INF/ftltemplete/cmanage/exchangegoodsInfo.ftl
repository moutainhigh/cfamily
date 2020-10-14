<#include "../zappage/jd_after_sale.ftl" />
<@m_common_html_css ["cmanage/css/cmb_base.css"] />
<div class="cmb_cmanage_page">

<#assign  a_order_code=b_method.upFiledByFieldName(b_page.upBookData(),"order_code").getPageFieldValue() />
<#assign  exchange_no=b_method.upFiledByFieldName(b_page.upBookData(),"exchange_no").getPageFieldValue() />
<#assign  status=b_method.upFiledByFieldName(b_page.upBookData(),"status").getPageFieldValue() />

<div style="padding-left:30px;padding-top:7px;margin-bottom:15px;height: 30px;">


	<#if status=='4497153900020002'||status=='4497153900020003'>
			<input class="btn btn-small btn-primary" onclick="exchangegoodsInfo.approve('cancel','${exchange_no}')" type="button" value="取消换货">
			<input class="btn btn-small btn-primary" onclick="exchangegoodsInfo.approve('xxx','${exchange_no}')" type="button" value="客服确认">
	</#if>
	<input  class="btn btn-small btn-primary" style="float:right" onclick="zapadmin.window_url('../show/page_add_v_oc_order_remark?zw_f_order_code=${a_order_code?default("")}&zw_f_asale_code=${exchange_no?default("")}')" type="button" value="添加备注">
</div>

<input type="hidden" id="return_code" name="return_code" value="${exchange_no}">

<div class="cmb_cmanage_page_title  w_clear">
<span>换货信息</span>
</div>

<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>
<#assign duohzAfterSaleSupport=b_method.upClass("com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport")>
<#assign duohzOrderSupport=b_method.upClass("com.cmall.groupcenter.duohuozhu.support.OrderForDuohuozhuSupport")>

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

<#assign  b_exchange_no=b_method.upFiledByFieldName(b_page.upBookData(),"exchange_no").getPageFieldValue() />
<#assign  small_seller_code=b_method.upFiledByFieldName(b_page.upBookData(),"small_seller_code").getPageFieldValue() />

<!--京东订单需要判断一下有售后地址的时候才显示-->
<#assign showJdAddress = false />
<#if small_seller_code == "SF031JDSC">
	<#if (b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",exchange_no))??>
		<#assign jdAfterSale = b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",exchange_no) />
		<#if jdAfterSale.afs_address != ''>
			<#assign showJdAddress = true />
		</#if>
	</#if>
</#if>

<#if showJdAddress || small_seller_code != "SF031JDSC">
<div class="cmb_cmanage_page_title  w_clear">
<span>售后地址信息</span>
</div>											 
<#assign e_page=b_method.upControlPage("page_book_v_exchange_goods_cf_sa","zw_f_exchange_no="+b_exchange_no)>
<form class="form-horizontal" method="POST" >
	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>
	
			
	<div class="control-group">
		<div class="controls">
      		<div class="control_book">
				<#if !duohzAfterSaleSupport.checkDuohzStore(exchange_no)>
		      		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="3f4796069dec453083d0afaaa530c833" onclick="exchangegoodsInfo.changeAddr('${small_seller_code}')" value="选择地址">
				</#if>
      		</div>
		</div>
	</div>
	
	<div class="control-group">
		<div class="controls">
		      		<div class="control_book">
			      		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="14284970e17c416b9ca0205a5d115cd0" onclick="exchangegoodsInfo.sendMessage()" value="发送短信">
		      		</div>
		</div>
	</div>
	
</form>
</#if>

<#-- 京东售后信息  -->
<#if small_seller_code == "SF031JDSC">
	<@m_zapmacro_jd_after_sale exchange_no />
</#if>

<input type="hidden" value="${exchange_no}" id="exchange_no">


<div class="cmb_cmanage_page_title  w_clear">
<span>用户售后物流</span>
</div>
<#assign v_oc_order_shipments_sale=b_method.upControlPage("page_chart_v_oc_order_shipments_sale","zw_f_order_code="+exchange_no+ "&" + "zw_p_size=-1").upChartData()>
<#-- 如果没有售后物流则显示添加按钮  -->
<#if v_oc_order_shipments_sale.getPageData()?size == 0>
<p style="text-align: right"><input type="button" class="btn  btn-success" zapweb_attr_operate_id="" onclick="zapadmin.window_url('page_add_v_oc_order_shipments_sale?asale_code=${exchange_no}')" value="添加物流信息"></p>
</#if>
<@m_zapmacro_common_table v_oc_order_shipments_sale />

<div class="cmb_cmanage_page_title  w_clear">
<span>换货详细信息</span>
</div>

<#assign a_exchang_detail=b_method.upControlPage("page_chart_v_oc_exchange_goods_detail","zw_f_exchange_no=" +b_exchange_no + "&" + "zw_p_size=-1").upChartData()>
<@m_zapmacro_common_table a_exchang_detail />

<#if (b_method.upDataOne("oc_order_duohz_after","","","asale_code = :asale_code AND dhz_asale_code != ''","asale_code",exchange_no))??>
<#assign  trackList=duohzOrderSupport.getOrderTrackList(exchange_no) />
<div class="cmb_cmanage_page_title  w_clear">
<span>多货主换货新单物流信息:</span>
</div>											 
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    <th>物流公司</th>
	    <th>运单号</th>
	    <th>内容</th>
	    <th>时间</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list trackList as e>
		<tr>
			<td>${e.dlverName}</td>
			<td>${e.invcId}</td>		
			<td>${e.context}</td>
			<td>${e.time}</td>
	  	</tr>		
		</#list>
	</tbody>
</table>
</#if>

<div class="cmb_cmanage_page_title  w_clear">
<span>日志流水</span>
</div>
<#assign a_exchange_log=b_method.upControlPage("page_chart_v_lc_exchangegoods","zw_f_exchange_no=" +b_exchange_no + "&" + "zw_p_index=0" + "&" + "zw_p_size=10").upChartData()>
<#-- 老方法 -->
<#--<@m_zapmacro_common_table a_exchange_log />-->

<#-- 新方法（显示换货状态code的对应值） -->
<@m_zapmacro_cmanage_exchange_log_page a_exchange_log,b_method  />







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

<#-- 日志数据区域宏定义  -->
<#macro m_zapmacro_cmanage_exchange_log_page e_pagedata,b_method>
<#-- 调用获取数据源的java类  -->
<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>
<table  class="table table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	        </#list>
	    </tr>
	</thead>
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
				<#list e_list as e>
					<#if e_index = 1 || e_index = 2>
						<td>
							<#if sc_defineService??>
								${sc_defineService.getDefineNameByCode(e)}
							</#if>
						</td>
					<#elseif e_index = 4>
						<td>
							<#if e??>
								<#if e?length lt 1>
									用户
								<#else>
									${e?default("")}
								</#if>
								
							<#else>
								${e?default("")}
							</#if>
			      			
			      		</td>
					<#else>
			      		<td>
			      			${e?default("")}
			      		</td>
	  		 		</#if>
				</#list>
			</tr>
		</#list>
	</tbody>
</table>
</#macro>

<@m_common_html_script "require(['cfamily/js/exchangegoodsInfo'],function(a){a.init_page()});" />
