<#assign yyMsgService=b_method.upClass("com.cmall.newscenter.service.YyMsgService")>

<#-- 页面主内容显示  -->
<#assign e_page=b_page>
<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire e_page />
</div>
<hr/>
<#assign e_pagedata=e_page.upChartData()>
<div class="zw_page_common_data">
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
	        	<#list e_pagedata.getPageHead() as e_list>
	        		<#if e_list_index == 0>
	        			<#-- 隐藏域uid -->
	        		<#elseif e_list_index == 3>
	        			<th>
	        				${e_list}
	        			</th>
	        			<th>
	        				图片
	        			</th>
	        		<#elseif e_list_index == 5>
	        			<th>
	        				${e_list}
	        			</th>
	        			<th>
	        				查看
	        			</th>
	        		<#elseif e_list_index == 6>
	        		<#elseif e_list_index == 8>
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
				<tr>
					<#assign wordId=e_list[0]>
					<#assign msgType=e_list[2]>
					<#assign seeFlag=e_list[6]>
			  		<#list e_list as e>
			  	       <#if seeFlag=='1'>
			  	         <#if e_index == 0>
			  				<#-- 隐藏域uid -->
		  		 		<#elseif e_index == 3>
		  		 			<td>
		  		 				<#if msgType == '文字留言'>
		  		 					<#if e?length gt 46>
		  		 						${e?substring(0,46)}...
		  		 					<#else>
		  		 						${e?default('')}
		  		 					</#if>
		  		 				<#else>
		  		 					<audio controls="controls">
							    		<source src="${e?default('')}" type="audio/mp3" />
									</audio>
		  		 				</#if>
			  		 		</td>
			  		 		<td style="text-align: center;">
			  		 			<#assign imageCount=yyMsgService.getImageCount(wordId)>
			  		 			${imageCount?trim}
			  		 		</td>
			  		 	<#elseif e_index == 5>
							<td style="text-align: center;">
			  		 			${e?default("")}
			  		 		</td>
			  		 		<td style="text-align: center;">
			  		 			<a href="page_detail_v_uc_user_words?zw_f_uid=${wordId}" target="_blank">查看</a>
			  		 		</td>
			  		 	<#elseif e_index == 6>
			  		 	
			  		 	<#elseif e_index == 8>
			  		 		
			  		 	<#elseif e_index == 9>
			  		 		<td>
				      			<#if e_list[8] == "">
				      				${e?default("")}
				      			</#if>
				      		</td>
		  		 		<#else>
		  		 			<td style="text-ali	gn: center;">
			  		 			${e?default("")}
			  		 		</td>
		  		 		</#if>
		  		 <#-- 未查看的数据颜色 为红色-->
			  	       <#else>
			  	         <#if e_index == 0>
			  				<#-- 隐藏域uid -->
		  		 		<#elseif e_index == 3>
		  		 			<td style="color:red">
		  		 				<#if msgType == '文字留言'>
		  		 					<#if e?length gt 46>
		  		 						${e?substring(0,46)}...
		  		 					<#else>
		  		 						${e?default('')}
		  		 					</#if>
		  		 				<#else>
		  		 					<audio controls="controls" style="color:red">
							    		<source src="${e?default('')}" type="audio/mp3" />
									</audio>
		  		 				</#if>
			  		 		</td>
			  		 		<td style="text-align: center;">
			  		 			<#assign imageCount=yyMsgService.getImageCount(wordId)>
			  		 			${imageCount?trim}
			  		 		</td>
			  		 	<#elseif e_index == 5>
							<td style="text-align: center;color:red">
			  		 			${e?default("")}
			  		 		</td>
			  		 		<td style="text-align: center;">
			  		 			<a href="page_detail_v_uc_user_words?zw_f_uid=${wordId}" target="_blank">查看</a>
			  		 		</td>
			  		 	<#elseif e_index == 6>			  		 	
			  		 	
			  		 	<#elseif e_index == 8>
			  		 		
			  		 	<#elseif e_index == 9>
			  		 		<td>
				      			<#if e_list[8] == "">
				      				${e?default("")}
				      			</#if>
				      		</td> 		 	
		  		 		<#else>
		  		 			<td style="text-ali	gn: center;color:red">
			  		 			${e?default("")}
			  		 		</td>
		  		 		</#if>

			  	       </#if>
			      	</#list>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>
