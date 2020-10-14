
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
			      	 <th>
			      	 	${e_list}
			      	 </th>
		      </#list>
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr class="column">
			  		 <#list e_list as e>
			  		 	<#if e_index = 5>
			  		 		<#if e = '01'>
						  		 <td>是</td>
					  		 <#else>
					  		  	 <td>否</td>
					  		 </#if>
					  		 
				  	    <#elseif e_index = 1>
					  		<#if e_list[1] = "">
					  			<td>
		  			    			默认小图标
		  			    		</td> 
	  			    		<#else>
					      		<td>
					      			${e?default("")}
					      		</td>
			      			</#if> 
					  	 <#elseif e_index = 6>
				  			<td>
	  			    			<img src="${e?default("")}">
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


<script type="text/javascript">
	$(function(){
		// 移除【导航编码】列头
        $($("table>thead>tr>th")[7]).remove();
        
        var trArr = $(".column");   // 纠正 e_index = 9  的情况，同时移除【导航编码】列
        for(var i = 0 ; i < trArr.length; i ++){
        	var tr_ = trArr[i];
        	var arr = tr_.children;
        	var isRelease = arr[5].innerText;
        	if(isRelease == '是'){
        		$(arr[12].children[0]).val("取消发布")
        	}else{
        		$(arr[12].children[0]).val("发布")
        	}
        	$(arr[11].children[0]).attr("href" , $(arr[11].children[0]).attr("href") + "&zw_f_nav_type='4497471600100002'");
        	$(arr[7]).remove();
        }
    });

</script>














































