
<style type="text/css">
	.column td{
		text-align: center;
	}
</style>

<#assign column_code = b_page.getReqMap()["zw_f_column_code"]?default("") >
<#assign nav_code = b_page.getReqMap()["zw_f_nav_code"]?default("") >

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
		    		<th>
			      	 	<input id="select-all" type="checkbox" onclick="selectAll()"> 全选
			      	 </th>
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
						<td>
			  		 		<input type="checkbox" name="zw_f_nav_code" value="">
			  		 	</td>
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
        $($("table>thead>tr>th")[8]).remove();
        
        var trArr = $(".column");   //移除【导航编码】列
        for(var i = 0 ; i < trArr.length; i ++){
        	var tr_ = trArr[i];
        	var arr = tr_.children; 
        	var navCode = arr[8].innerText;
        	$(arr[8]).remove();
        	$(arr[0])[0].children[0].value = navCode; 
        	if("${nav_code}" == navCode){
        		$($(arr[0])[0].children[0]).remove();   
        	}
        }
        $("input[type='button']").attr("column_code" , "${column_code}"); // 为复制按钮添加 附加属性
        $(".controls").removeClass("controls");
    });
    
    // 全选
    function selectAll(){
	    if($("#select-all")[0].checked == true){
			$("input[name='zw_f_nav_code']").prop("checked", true);     
		}else{
			$("input[name='zw_f_nav_code']").prop("checked", false);
		}
	}

</script>














































