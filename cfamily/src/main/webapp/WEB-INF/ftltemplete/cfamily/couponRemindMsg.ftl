
<#assign setting_uid=b_page.getReqMap()["zw_f_setting_uid"] />

<div class="zab_info_page">

	<div class="zab_info_page_title  w_clear">
		<span>优惠券提醒设置</span>
	</div>
	<#assign setting_book_page=b_method.upControlPage("page_book_v_oc_coupon_remind_setting","zw_f_uid="+setting_uid)>
	<#assign coupon_remind_setting=setting_book_page.upBookData()>
	<#-- 查看页 -->
	<form class="form-horizontal" method="POST" >
		<#list coupon_remind_setting as e>
	  		<@m_zapmacro_common_field_start text=e.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e.getFieldTypeAid()=="104005019">
		      			<#list setting_book_page.upDataSource(e) as e_key>
							<#if  e.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e.getFieldTypeAid()=="104005021">
						<#if  e.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e.getPageFieldValue()}" target="_blank">
									<img src="${e.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      		${e.getPageFieldValue()?default("")}
		      		</#if>
	      		</div>
			<@m_zapmacro_common_field_end />
		</#list>
	</form>

	<div class="zab_info_page_title  w_clear">
		<span>提醒信息列表</span>
	</div>
	
	<@m_zapmacro_common_page_chart b_page />
	
	<#-- 列表页 -->
	<#macro m_zapmacro_common_page_chart e_page>
	
		<div class="zw_page_common_inquire">
			<@m_zapmacro_common_page_inquire e_page />
		</div>
		
		<#local e_pagedata=e_page.upChartData()>
		<div class="zw_page_common_data">
		<@m_zapmacro_common_table e_pagedata />
		<@m_zapmacro_common_page_pagination e_page  e_pagedata />
		
		</div>
	</#macro>
	
	<#-- 列表的自动输出 -->
	<#macro m_zapmacro_common_table e_pagedata>
		<#assign couponRemindSupport=b_method.upClass("com.cmall.familyhas.support.CouponRemindSupport")>
		<table  class="table  table-condensed table-striped table-bordered table-hover">
			<thead>
			    <tr>
			        <#list e_pagedata.getPageHead() as e_list>
			        	<#if e_list_index==7>
				      	<#else>
					      	 <th>
					      	 	${e_list}
					      	 </th>
				      	</#if>
			      </#list>
			    </tr>
		  	</thead>
			<tbody>
				<#list e_pagedata.getPageData() as e_list>
					<#assign msgMap=couponRemindSupport.getMsgMap(e_list[7]) />
					<tr>
				  		 <#list e_list as e>
				  		 	<#if e_index==3><!--完成时间-->
				  		 		<td>
				  		 			${msgMap.finish_time}
				  		 		</td>
							<#elseif e_index==5><!--发送状态-->
								<td>
									<#if msgMap.flag_finish=="1">
										已发送
			  		 				<#else>
			  		 					未发送
			  		 				</#if>
				  		 		</td>
							<#elseif e_index==7>
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
</div>






