<@m_zapmacro_common_page_chart b_page />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>	
		      	 
		      	 <#if e_pageField[e_list_index] == 'hjy_account'>
			       	 <th>有效订单量 </th>
		      	 </#if>	  
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	        	<#if e_pageField[e_index] == 'url'>
	      		<td><a href="javascript:copyTextToClipboard('${e?default("")}')" title="点击复制">${e?default("")}</a></td>		        	
	        	<#elseif e_pageField[e_index] == 'wx_url'>
	      		<td><a href="javascript:copyTextToClipboard('${e?default("")}')" title="点击复制">${e?default("")}</a></td>		          		
	        	<#else>
	      		<td>
	      			${e?default("")}
	      		</td>	        	
	        	</#if>
	      		
		        <#if e_pageField[e_index] == 'hjy_account'>
		      	 <td>
		      	 	${m_page_helper("com.cmall.familyhas.pagehelper.SmallAppChannelOrderCountHelper", e_list[e_pageField?seq_index_of("channel_id")])}
		      	 </td>	      	 
			    </#if>     		
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>
<script>
function copyTextToClipboard(text) {
	var textArea = document.createElement("textarea");
	textArea.style.background = 'transparent';
	textArea.value = text;
	document.body.appendChild(textArea);
	textArea.select();
	try {
		if(document.execCommand('copy')){
			alert('已复制到剪贴板')
		}
	} catch (err) {
		console.log('Oops, unable to copy');
		alert('复制到剪贴板失败')
	}
	document.body.removeChild(textArea);
}
</script>
