<@m_common_html_script "require(['cfamily/js/chartOrder'],
	function(a){
		
	
	});" />
<@m_zapmacro_common_page_chart b_page />

<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>


	<#assign a_session1=b_method.upClass("com.srnpr.zapweb.helper.WebSessionHelper").upSessionUser().getUserCode() />
	<input type="hidden" id="usercode" value="${a_session1}">
	
	
	<div class="zw_page_common_inquire">
	
	<#--
		<@m_zapmacro_common_page_inquire e_page />
		-->
		
				<form class="form-horizontal" method="POST" >
			<@m_zapmacro_common_auto_inquire e_page />
			
			
			
				<div class="control-group">
			    	<div class="controls">
						<input type="button" class="btn  btn-success" zapweb_attr_operate_id="dd0b7174340e11e49fed005056925439" onclick="query(this)" value="查询">
						
						 <script type="text/javascript">//<![CDATA[
						        
						        function query(obj){
						        	var usercode=$('#usercode').val();
						           var queryString = $(obj).parents("form").formSerialize();
						           queryString+=2;
						           $.ajax(  
									    {  
									        type:'get',  
									        url : 'http://tj.huijiayou.cn/AjaxHandler/HJYHandler.ashx?action=hjylog&uid='+usercode+'&order='+encodeURIComponent(queryString),  
									        dataType : 'jsonp',  
									        jsonp:"jsoncallback",  
									        success  : function(data) {  
									        }
									    }  
									); 
						           
						           
						     	   zapjs.zw.func_inquire(obj);
						        }
						        
						        
						       //]]>
						   </script>
						
			    	</div>
				</div>
		</form>
		
		
		
	</div>
	<hr/>
	<#assign a_session=b_method.upClass("com.srnpr.zapweb.helper.WebSessionHelper").upHttpRequest().getParameterMap()?size />
	<#if (a_session>0)>
		<#-- 重写页面获取数据方法 2016-09-07 zhy -->
		<#assign searchOrder=b_method.upClass("com.cmall.familyhas.service.SearchOrderService")/>
		<#local e_pagedata=searchOrder.upChartData(e_page)>
		<div class="zw_page_common_data">
		<@m_zapmacro_common_table e_pagedata />
		<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
		<#-- 查询结果是单个时每天如果查询次数超过设置的最大值则冻结用户 -->
		<#if e_pagedata.getPageData()?size == 1>
			<#local result = b_method.upClass("com.cmall.familyhas.webfunc.FuncFreezeOperator").funcDo(a_session1, null)>
			<#if result.upFlagTrue() == false>
				<script>window.parent.zapjs.zw.login_out('../manage/logout')</script>
			</#if>
		</#if>	
	
	</#if>
	</div>
	
</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>

	       		 <#if e_list_index != 0 && e_list_index != 1 >
			      	 <th>
			      	 	${e_list}
			      	 </th>
		      	 </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
	    <#assign searchOrderService=b_method.upClass("com.cmall.familyhas.service.SearchOrderService")/>
		<#list e_pagedata.getPageData() as e_list>
		
			<tr>
	  		 <#list e_list as e>
	  		 	
	  		   <#if e_index != 0 && e_index != 1>
		  		 	<#if e_index = 1>
		  		 		<#if e_list[0]=='1'>
			      			<td  title="订单已删除,只为查看订单详情，不做他用">
			      				<font color="red"> ${e?default("")}</font>
			      			</td>
			      		<#else>
			      			<td>
		      				 	${e?default("")}
		      				 </td>
			      		</#if>
			      	<#elseif e_index = 18>
			      		<#if e_list[6]?default("") == "已发货">
		  		 		  <td>
				 		    ${e?default("")}
			      		  </td>
		  		 	   <#else>
		  		 		  <td>
			      		  </td>
		  		 	   </#if>
		  		 	<#elseif e_index = 19>
			      		<#if e_list[6]?default("") == "下单成功-未付款">
		  		 		  <td>
				 		    ${e?default("")}
			      		  </td>
		  		 	   <#else>
		  		 		  <td>
			      		  </td>
		  		 	   </#if>
		  		 	<#elseif e_index = 4>
				     	 <#if e_pagedata.getPageData()?size gt 1>
				     	   <td> </td>
				     	 <#else>
			      			<td>
		      				 	${e?default("")}
		      				 </td>
				     	</#if>
		  		 	<#elseif e_index = 8>
				     	 <#if e_pagedata.getPageData()?size gt 1>
				     	   <td> </td>
				     	 <#else>
			      			<td>
		      				 	${e?default("")}
		      				 </td>
				     	</#if>
			     	<#elseif e_index == 16>
		      			<td>
		      				<#assign sellerType = searchOrderService.getSellerType(e_list[1])>
	      				 	${sellerType?default("")}
	      				 </td>
	      			<#elseif e_index == 21>
		      			<td>
		      				<#if e_list[1]?default("") == "SI2003"&&e_list[3]?default("") == ""&&e_list[6]?default("") != "交易失败">
				 		  		${e?default("")}
				 		  	</#if>
	      				 </td>	 
	      			<#elseif e_index == 10>
		      			<td>
		      				<#if e?default("") != "第三方渠道商">
				 		  		${e?default("")}
				 		  	</#if>
	      				 </td>	
		  		 	<#else>
			      		<td>
				 		  ${e?default("")}
			      		</td>
			  		 </#if>
		  		</#if>
		  		
	      	</#list>
	      	</tr>
	      	
	 	</#list>
	</tbody>
</table>
</#macro>

