<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>优惠券类型管理</span>
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
	<#if product_support.isLdCoupon(activityCode)>
		<#assign pileInfo=product_support.getPileInfo(activityCode)>
		<div class="control-group">
			<label class="control-label" for="">最低金额限制方式:</label>
			<div class="controls">
				<div class="control_book">
					<#if "10" == pileInfo.minlimit_tp>
						叠加最低订购金额
					<#elseif "20" == pileInfo.minlimit_tp>
						礼金/商品金额占比
					<#else>
						无限制
					</#if>
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">单张不受叠加限制:</label>
			<div class="controls">
				<div class="control_book">${pileInfo.is_onelimit}</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">叠加不受最低金额限制:</label>
			<div class="controls">
				<div class="control_book">${pileInfo.mindis_amt}</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">叠加使用上限:</label>
			<div class="controls">
				<div class="control_book">${pileInfo.disup_amt}</div>
			</div>
		</div>
	</#if>
	
	<#-- 
	<#assign totalMoney=product_support.getTotalMoney(activityCode)>
	<div class="control-group">
		<label class="control-label" for="">成本总限额:</label>
		<div class="controls">
			<div class="control_book"> ${totalMoney} </div>
		</div>
	</div>
	-->
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
<div class="form-horizontal control-group">
	<#if creator != "ld">
		<a href="../page/page_add_v_oc_coupon_type_new3?zw_f_activity_code=${activity_code}" class="btn btn-link"  target="_blank">
			<input class="btn btn-success" type="button" value="添加优惠券类型"/>
		</a>
	</#if>
</div>

<div class="zab_info_page_title  w_clear">
<span>优惠券类型列表</span>&nbsp;&nbsp;&nbsp;
</div>
<#assign logData=b_method.upControlPage("page_chart_v_oc_coupon_type_new3","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>

<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<table  class="table  table-condensed table-striped table-bordered table-hover">
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
			<#assign provideNumMap = product_support.getCouponTypeProvide(e_list[e_pageField?seq_index_of("coupon_type_code")]) />
			<tr>
	  		 <#list e_list as e>
  				<td> 
  					<#if e_pageField[e_index]=="discount">
  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
			      			${e?default("")}%			      			
		      			<#else>
		      				<!--金额券时折扣券为空-->
		      			</#if>
		      		<#elseif e_pageField[e_index]=="money">
  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
			      			<!--折扣券时金额券为空-->
		      			<#else>
		      				${e?default("")}		      				
		      			</#if>	
		      		<#elseif e_pageField[e_index]=="privide_money">
		      			${provideNumMap["privideNum"]}
		      		<#elseif e_pageField[e_index]=="surplus_money">
		      			${provideNumMap["surplusNum"]}				      				      			      			
	      			<#elseif e_pageField[e_index]=="修改">
	      				<#if e_list[e_pageField?seq_index_of("status")] = "已发布">
			      			<!--已发布状态时不能再修改-->
		      			<#else>
		      				${e?default("")}
		      			</#if>
	  			    <#elseif e_pageField[e_index]=="使用限制维护">
	  			    	<#if e_list[e_pageField?seq_index_of("status")] == "未发布" && e_list[e_pageField?seq_index_of("limit_condition")] == "指定" && e_list[e_pageField?seq_index_of("privide_money")] == "0">
	  			    		<a id="limitBtn" class="btn btn-link" target="_blank" href="page_add_v_oc_coupon_type_limit?zw_f_coupon_type_code=${e_list[0]}&zw_f_activity_code=${activityCode}">使用限制维护</a>
		      			<#elseif creator = "ld" && e_list[e_pageField?seq_index_of("status")] == "未发布" && e_list[e_pageField?seq_index_of("limit_condition")] == "指定">
	  			    		<a id="limitBtn" class="btn btn-link" target="_blank" href="page_add_v_oc_coupon_type_limit?zw_f_coupon_type_code=${e_list[0]}&zw_f_activity_code=${activityCode}">使用限制维护</a>
		      			</#if>	
	  			    <#elseif e_pageField[e_index]=="发布/取消发布">
	  			    	<#if e_list[e_pageField?seq_index_of("status")] == "已发布">
	  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_coupon_type_code : '${e_list[0]}'});" zapweb_attr_operate_id="630fabc142ce4f0b9f40a3c45d129962">
		      			<#else>
		      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_coupon_type_code : '${e_list[0]}'});" zapweb_attr_operate_id="630fabc142ce4f0b9f40a3c45d129962">
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
