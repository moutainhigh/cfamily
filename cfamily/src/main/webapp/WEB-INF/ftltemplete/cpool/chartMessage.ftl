<#if b_page.getReqMap()["mess_category"]??>
	<#assign mess_category = b_page.getReqMap()["mess_category"] >
<#else>
	  		<#assign mess_category = "" >
	  	</#if>


	<div class="zw_page_common_inquire">
	
	<form class="form-horizontal" method="POST" >
		
		<div class="control-group">
			<label class="control-label" for="zw_f_mess_title">消息分类</label>
			<div class="controls">
				<input id="mess_category" name="mess_category" value="${mess_category}" type="text">
				<input class="btn btn-small" onclick="zapadmin.window_url('../show/page_vv_hp_message_type_one')" value="类型选择" type="button">
			</div>
		</div>
	
		<@m_zapmacro_common_auto_inquire b_page />
		
		
		<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001009" />
		
	</form>
		
	</div>
	<hr/>
	
	
	
	
	<#assign e_pagedata=b_page.upChartData()>
	<div class="zw_page_common_data">
	
	<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
			      	<#if (e_list_index > 2) && (e_list_index < 14)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
		      	  </#if>
	      </#list>
	      <th>
			      	 	发布操作
			       </th>
	    </tr>
  	</thead>
<#assign dateUtil=b_method.upClass("com.cmall.familyhas.util.DateUtilA")>
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		  <#if (e_index > 2) && (e_index < 14)>
	      		<td>
	      			 <#if (e_index==5)>
	      				<#-- 在这里判断变态的发布状态 -->
	      				<#if e_list[2]=='449746250001'><#-- 要发布的，判断是否到时间发布了 -->
	      					<#if e_list[1]=='449746250001'>
	      						<#assign ind = dateUtil.compareNow("${e_list[0]}")> 
	      						<#if ind &lt;= 0>
	      						已发布
	      						<#else>
	      						未发布
	      						</#if>
	      					<#else>
	      						已发布
	      					</#if>
	      				<#else>
	      					未发布
	      				</#if>
	      			 <#else>
	      				 ${e?default("")}
	      			  </#if>
	      		</td>
	      		 </#if>
	      	</#list>
	      	
	      	<td><#-- 发布 -->
	      	<#if e_list[2]=='449746250001'>
	      		${e_list[15]?default("")}
	      	<#else>
	      	  ${e_list[14]?default("")}
	      	</#if>
	      	</td>
	      	
	      	</tr>
	 	</#list>
		</tbody>
</table>
	
	<@m_zapmacro_common_page_pagination b_page  e_pagedata />
	
	</div>
	
	
	
<script type="text/javascript">
  
	if($('#mess_category')){
		$('#mess_category').attr("readonly","readonly");    
	}
	
  </script>