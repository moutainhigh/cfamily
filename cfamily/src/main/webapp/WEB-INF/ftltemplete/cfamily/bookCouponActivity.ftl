<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>优惠券活动-查看</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	</#list>
	<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
	<#assign product_support=b_method.upClass("com.cmall.ordercenter.util.CouponUtil")>
	<#assign flag=product_support.getActivityStatus(activityCode)>
	<div class="control-group">
		<label class="control-label" for="">状态:</label>
		<div class="controls">
			<div class="control_book">${flag}</div>
		</div>
	</div>
	<#assign totalMoney=product_support.getTotalMoney(activityCode)>
	<div class="control-group">
		<label class="control-label" for="">成本总限额:</label>
		<div class="controls">
			<div class="control_book"> ${totalMoney} </div>
		</div>
	</div>
</form>
</#macro>

<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >
<div class="zab_info_page_title  w_clear">
<span>优惠券类型列表</span>&nbsp;&nbsp;&nbsp;
</div>
<#assign logData=b_method.upControlPage("page_chart_v_oc_coupon_type","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>

<#assign  e_pagedata=logData />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	       	  <#if (e_list_index <= 10)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      </#if>
		        <#if (e_list_index == 13)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
		  		 <#list e_list as e>
		  		 	 <#if (e_index <=10)>
		  				<td>
							${e?default("")}
			      		</td>
		      		 </#if>
		      		 
		      		  <#if (e_index == 13)>
		  				<td>
							${e?default("")}
			      		</td>
		      		 </#if>
		      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
