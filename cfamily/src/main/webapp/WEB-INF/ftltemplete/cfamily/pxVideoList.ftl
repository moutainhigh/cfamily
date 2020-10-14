
<@m_zapmacro_common_page_chart b_page />



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if e_list_index=7>
	        	 <th>
		      	 	${e_list}
		      	 </th>
		      	 <th>
		      	 	视频预览
		      	 </th>
		    <#elseif e_list_index=8> 
		          <th>
		      	 	操作
		      	 </th> 	
		    <#elseif e_list_index=9>
		    <#elseif e_list_index=6>    
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
	  		 <#list e_list as e>
	  		  
	  		  <#if e_index=7>
	  		  <#assign arr=("${e_list[7]}"?split("|"))>
	        	 <td>
		 		<#list arr as a>
		 		<#if (arr?size>1)||(a?contains("_"))>
		 		<#assign subArr=("${a}"?split("_"))>
		 		<#assign endArr=("${a}"?split("."))>
		 		<#if a_index=((arr?size)-1)>
		 		<a href="${subArr[1]}">${subArr[0]}.${endArr[(endArr?size)-1]}</a>
		 		<#else>
		 		<a href="${subArr[1]}">${subArr[0]}.${endArr[(endArr?size)-1]},</a>
		 		</#if>
		 		<#else>

	            </#if>
	            
		 		</#list>
	      		</td>
	      			 <td>
	      			<a href="../web/pxVideo/peiXunVideo.html?videoId='${e_list[2]}'">预览</a>
	      	      </td>
	      	<#elseif  e_index=8>
	      	    <td>
	      	    	${e?default("")}
	      			<input zapweb_attr_operate_id="275d8d6dc22711e9abac005056165069" onclick="zapjs.zw.func_tip(this,'${e_list[6]}','删除')" type="button" class="btn btn-small" value="删除">
	      		</td>
	      	<#elseif  e_index=9>
	      	<#elseif  e_index=6>
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
