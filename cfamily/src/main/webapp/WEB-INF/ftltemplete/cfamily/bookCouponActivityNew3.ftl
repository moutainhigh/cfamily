<#assign provide_type = "">
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
		      		<#if  e_field.getFieldTypeAid()=="104005019"><#-- 下拉框 -->
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
						<#if (e_field.getFieldName() == "provide_type")>
							<#assign provide_type = e_field.getPageFieldValue()?default("")>
						</#if>
					<#elseif e_field.getFieldTypeAid()=="104005021"><#-- 上传 -->
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      			<#if (e_field.getFieldName() == "provide_type")>
		      				<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
		      			    <#assign product_support=b_method.upClass("com.cmall.ordercenter.util.CouponUtil")>
		      				<#assign assignTrigger=product_support.getActivityAssignTrigger(activityCode)>
							${assignTrigger}
		      			<#else>
		      				${e_field.getPageFieldValue()?default("")}
		      		    </#if>
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>

<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >



<#if provide_type=="4497471600060005">
   <div class="zab_info_page_title  w_clear">
	<span>发放统计</span>&nbsp;&nbsp;&nbsp;
	</div>
	<#assign redeemData=b_method.upClass("com.cmall.ordercenter.util.CouponUtil").getCouponSendInfos(activity_code)>
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		    	<td>发放数量</td>
		    	<td>使用人数</td>
		    	<td>创建时间</td>

		    </tr>
	  	</thead>
		<tbody>
			<#list redeemData as e_list>
				<tr>
			      	<td>${e_list.send_count}</td>
			      	<td>${e_list.use_count}</td>
			      	<td>${e_list.create_time}</td>
			      	
		      	</tr>
		 	</#list>
		</tbody>
	</table>

<#elseif provide_type=="4497471600060004">

	<div class="zab_info_page_title  w_clear">
	<span>兑换码列表</span>&nbsp;&nbsp;&nbsp;
	</div>
	<#assign redeemData=b_method.upClass("com.cmall.ordercenter.util.CouponUtil").getCouponRedeem(activity_code)>
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		    	<td>兑换码发放数量</td>
		    	<td>兑换码使用人数</td>
		    	<td>创建时间</td>
		    	<td>导出</td>
		    </tr>
	  	</thead>
		<tbody>
			<#list redeemData as e_list>
				<tr>
			      	<td>${e_list.send_count}</td>
			      	<td>${e_list.redeem_count}</td>
			      	<td>${e_list.create_time}</td>
			      	<td><a class="btn btn-link" target="_blank" href="../export/page_chart_v_oc_coupon_redeem?zw_p_size=-1&zw_f_activity_code=${activity_code}&zw_f_import_count=${e_list.import_count}">导出</a></td>
		      	</tr>
		 	</#list>
		</tbody>
	</table>

<#elseif provide_type!="4497471600060004"&&provide_type!="4497471600060005">

	<div class="zab_info_page_title  w_clear">
	<span>优惠码列表</span>&nbsp;&nbsp;&nbsp;
	</div>
	<#assign cdKeyData=b_method.upControlPage("page_chart_v_oc_cdkey_provide","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>
	<#assign  e_pagedata=cdKeyData />
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
		       	  <#if (e_list_index == 0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      </#if>
			      <#if (e_list_index == 1)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      </#if>
			      <#if (e_list_index == 2)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      </#if>
			      <#if (e_list_index == 3)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			      </#if>
			      <#if (e_list_index == 6)>
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
			  		 	 <#if (e_index == 0)>
			  				<td>
			  					${e?default("")}
				      		</td>
			      		 </#if>
			      		 <#if (e_index == 1)>
			  				<td>
								${e?default("")}
				      		</td>
			      		 </#if>
			      		  <#if (e_index == 2)>
			  				<td>
			  					<#if e_list[0] == "是">
			  						${e_list[2]}
			  					<#elseif e_list[0] == "否">
			  						${e_list[4]}
			  					</#if>
				      		</td>
			      		 </#if>
			      		 
			      		  <#if (e_index == 3)>
			  				<td>
								<#if e_list[0] == "是">
			  						${e_list[3]}
			  					<#elseif e_list[0] == "否">
			  						${e_list[5]}
			  					</#if>
				      		</td>
			      		 </#if>
			      		 
			      		  <#if (e_index == 6)>
			  				<td>
								${e?default("")}
				      		</td>
			      		 </#if>		      		 
			      	</#list>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	
	
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
	      			<#if e_pageField[e_list_index] !="使用限制维护"
	      					&& e_pageField[e_list_index] != "发布/取消发布"
	      					&& e_pageField[e_list_index] != "修改">
			      	 <th>
			      	 	${e_list}
			      	 </th>
	  				</#if>	        
	
		      </#list>
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<#assign provideNumMap = product_support.getCouponTypeProvide(e_list[e_pageField?seq_index_of("coupon_type_code")]) />
				<tr>
			  		 <#list e_list as e>
		  				<#if e_pageField[e_index]=="discount">
	  						<td>
		  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
					      			${e?default("")}%			      			
				      			<#else>
				      				<!--金额券时折扣券为空-->
				      			</#if>		  						
				      		</td>
		  				<#elseif e_pageField[e_index]=="money">
	  						<td>
		  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
					      			<!--折扣券时金额券为空-->
				      			<#else>
				      				${e?default("")}		      				
				      			</#if>	
				      		</td>
		      			<#elseif e_pageField[e_index]=="privide_money">
			      			<td>
			      				${provideNumMap["privideNum"]}		
			      			</td>
		      			<#elseif e_pageField[e_index]=="surplus_money">
			      			<td>
			      				${provideNumMap["surplusNum"]}	
			      			</td>
		      			<#elseif e_pageField[e_index]=="使用限制维护"
		      				|| e_pageField[e_index]=="发布/取消发布"
		      				|| e_pageField[e_index]=="修改">
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
</#if>