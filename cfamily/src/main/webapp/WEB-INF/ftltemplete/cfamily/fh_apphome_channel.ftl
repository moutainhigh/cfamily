
<style type="text/css">
	.column td{
		text-align: center;
	}
</style>

<@m_zapmacro_common_page_add b_page />
<#macro m_zapmacro_common_page_add e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>  
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>

</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		    <#list e_pagedata.getPageHead() as e_list>
			    <#if (e_list_index >0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
				 </#if>
	      </#list>
		    	<th>内容维护</th>
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr class="column">
					
			  		 <#list e_list as e>
			  		 	<#if e_index = 7>
			  		 		<#if e = '1'>
						  		 <td>未发布</td>
					  		 <#else>
					  		  	 <td>已发布</td>
					  		 </#if>
			      		<#elseif (e_index>0)>
				      		<td>
				      			${e?default("")}
				      		</td>
		      			</#if> 
			      	</#list>
			      	<td>
			      		<#if (e_list[3]=='加价')>
			      			<a class="btn btn-link" target="_blank" href="page_extracharge_v_fh_apphome_channel?zw_f_uid=${e_list[0]}">维护</a>
			      		</#if>
			      		<#if (e_list[3]=='兑换')>
			      			<a class="btn btn-link" target="_blank" href="page_exchange_v_fh_apphome_channel?zw_f_uid=${e_list[0]}">维护</a>
			      		</#if>
			      		<#if (e_list[3]=='活动')>
			      			<a class="btn btn-link" target="_blank" href="page_activity_v_fh_apphome_channel?zw_f_uid=${e_list[0]}">维护</a>
			      		</#if>
			      		<#if (e_list[3]=='视频')>
			      			<a class="btn btn-link" target="_blank" href="page_movie_v_fh_apphome_channel?zw_f_uid=${e_list[0]}">维护</a>
			      		</#if>
			      	</td>
		      	</tr>
		 	</#list>
		</tbody>
	</table> 

</#macro>


<script type="text/javascript">
$(function(){
    
    var trArr = $(".column");   // 
    for(var i = 0 ; i < trArr.length; i ++){
    	var tr_ = trArr[i];
    	var arr = tr_.children;
    	var isRelease = arr[6].innerText;
    	if(isRelease == '已发布'){
    		$(arr[9].children[0]).val("取消发布")
    	}else{
    		$(arr[9].children[0]).val("发布")
    	}
    }
});

</script>














































