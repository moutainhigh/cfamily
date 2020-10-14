
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
					      	 <#if (e_list_index >0)&&(e_list_index !=9)>
					      	 <th> 	
					      	 	${e_list}	
					      	 </th>
					      	 </#if>
		      	</#list>
		      	<th width="12%">	操作</th>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
			  		 <#list e_list as e>
						<#if (e_index >0)&&(e_index !=9)>
						<td>
						      			${e?default("")}
					    </td>
					    </#if>
			      	</#list>
			      	<td style="vertical-align:middle; text-align:center;">
					    <#if (e_list[3]=='未发布')>
					           <input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-link" zapweb_attr_operate_id="43334d550c0e4af3a85fe8d06333c787">
					           <a href="page_edit_v_fh_message_notification?zw_f_uid=${e_list[0]}" class="btn btn-link">修改</a>
					           <input type="button" value="删除" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-link" zapweb_attr_operate_id="08073e6e09eb4471b7720ea474c6f75d"> 
					    <#elseif (e_list[3]=='已发布')>         
					           <input type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-link" zapweb_attr_operate_id="afc7ea2d7cbb4d399f2182e0e2e74c41">
					  	</#if>
					</td>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>