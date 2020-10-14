<@m_zapmacro_common_page_chart b_page />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	<#if e_pageField[e_list_index] != 'uid'>
		      	 <th>
		      	 	${e_list}
		      	 </th>		        	
	        	</#if>
	      </#list>
		      	 <th>
		      	 	操作
		      	 </th>		      
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	        	<#if e_pageField[e_index] != 'uid'>
	      		<td>
	      			${e?default()}
	      		</td>		        	
	        	</#if>	  		 
	      	</#list>
	      		<td>
	      			<a href="page_edit_v_sc_word_term?zw_f_uid=${e_list[e_pageField?seq_index_of('uid')]}" class="btn btn-small">修改</a>
	      			<a href="javascript:void(0)" zapweb_attr_operate_id="20e4d4179bf611eaabac005056165069" onclick="zapjs.zw.func_delete(this,'${e_list[e_pageField?seq_index_of('uid')]}')" class="btn btn-small">删除</a>
	      			<a href="javascript:void(0)" zapweb_attr_operate_id="6eb9d7bdf62f44508fb8fd7d3ff7a320" onclick="zapjs.zw.func_do(this,null,{zw_f_uid: '${e_list[e_pageField?seq_index_of('uid')]}'})" class="btn btn-small">更新索引</a>
	      		</td>	      	
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>