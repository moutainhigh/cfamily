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
	  	<#if (e.getFieldName() == "creator")>
	  		<#assign creator = e.getPageFieldValue() >
	  	</#if>
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
</form>
</#macro>

<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
    <@m_zapmacro_common_field_show e_field e_page/>
</#macro>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e_field.getFieldTypeAid()=="104005021">
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      			<#if (e_field.getFieldName() == "assign_trigger")>
		      				<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
		      			    <#assign product_support=b_method.upClass("com.cmall.ordercenter.util.CouponUtil")>
		      				<#assign assignTrigger=product_support.getActivityAssignTrigger(activityCode)>
							${assignTrigger}
						<#elseif (e_field.getFieldName() == "creator")>
							<#assign creator = e_field.getPageFieldValue()>
							${e_field.getPageFieldValue()?default("")}
		      			<#else>
		      				${e_field.getPageFieldValue()?default("")}
		      		    </#if>
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>

<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >

<div class="zab_info_page_title  w_clear">
<span>活动概括</span>&nbsp;&nbsp;&nbsp;
</div>
<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	      	<th>总领取人数</th>
	      	<th>总使用人数</th>
	      	<th>总领取次数</th>
	      	<th>总使用次数</th>
	    </tr>
  	</thead>
  	<tbody>
			<#assign  tempList=b_method.upDataBysql("oc_coupon_info","SELECT count(ci.zid) lqcs,sum(case when ci.`status` = 1 then 1 else 0 END) sycs,count(distinct ci.member_code) lqrs,count(distinct b.member_code) syrs from ordercenter.oc_coupon_info ci LEFT JOIN ordercenter.oc_coupon_info b ON ci.zid = b.zid AND b.`status` = 1 where ci.activity_code = :activity_code","activity_code","${activity_code}") />			
			<tr>
	      		<td>${tempList[0]['lqrs']}</td>
	      		<td>${tempList[0]['syrs']}</td>
	      		<td>${tempList[0]['lqcs']}</td>
	      		<td>${tempList[0]['sycs']?default(0)}</td>
	      	</tr>
		</tbody>
</table>

<div class="zab_info_page_title  w_clear">
<span>商品维护</span>&nbsp;&nbsp;&nbsp;
</div>
<#assign logData=b_method.upControlPage("page_chart_v_oc_activity_agent_product","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>

<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		    	<#if (e_list_index > 0)>
		      	 <th style="text-align:center;">
		      	 	${e_list}
		      	 </th>
		      	</#if>
	      </#list>
	      	<th>UV</th>
	      	<th>PV</th>
	      	<th>领取次数</th>
	      	<th>使用次数</th>
	    </tr>
  	</thead>
  	
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<#assign provideNumMap = product_support.getCouponTypeProvide(e_list[e_pageField?seq_index_of("coupon_type_code")]) />			
			<tr>
	  		 <#list e_list as e>
  				<#assign  eventList=b_method.upDataBysql("sc_event_item_product","SELECT COUNT(*) pv,COUNT(DISTINCT di.distribution_junior_member_id) uv from logcenter.lc_distribution_info di WHERE di.distribution_product_code = :product_code","product_code","${e_list[2]}") />
  				<#assign  ttList=b_method.upDataBysql("oc_coupon_info","SELECT count(*) lq,sum(case when ci.status = 1 then 1 else 0 END) sy from ordercenter.oc_coupon_info ci where ci.coupon_type_code = :coupon_type_code","coupon_type_code","${e_list[4]}") />
  				<#if (e_index >0)>
  				<td> 
  					<#if e_pageField[e_index]=="discount">
  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
			      			${e?default("")}%			      			
		      			<#else>
		      				<!--金额券时折扣券为空-->
		      			</#if>
	  			   <#else>
	  				    ${e?default("")}
	  			   </#if>
	      		</td>
	      		</#if>
	      	</#list>
	      		<td>${eventList[0]['uv']}</td>
	      		<td>${eventList[0]['pv']}</td>
	      		<td>${ttList[0]['lq']}</td>
	      		<td>${ttList[0]['sy']?default(0)}</td>
	      	</tr>
	 	</#list>
		</tbody>
</table>