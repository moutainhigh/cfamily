
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
		    	<th><input id="chb-item-all" type="checkbox" onclick=$('input[id^="chb-item-"]').not("[disabled='disabled']").prop('checked',$(this).prop('checked'))></th>
		        <#list e_pagedata.getPageHead() as e_list>
				      	 <#if (e_list_index <=6)&&(e_list_index > 1)>
					      	 <th>
					      	 	${e_list}
					      	 </th>
				      	 </#if>
		      	</#list>
		      	<th colspan="4" width="12%">	操作</th>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
					<td><input id="chb-item-${e_list[0]}" type="checkbox" value="${e_list[0]}" onclick="$('#chb-item-all').prop('checked',false)" data-productcode="${e_list[3]}" data-currentstatus="${e_list[5]}"></td>
			  		 <#list e_list as e>
						<#if (e_index <=6)&&(e_index >1)>
							
							<#if (e_index !=3)>
					      		<td>
						      			${e?default("")}
					      		</td>
				      		<#elseif (e_index ==3)>
				      			<td>
						      			<a href="page_book_v_fh_news_notification?zw_f_uid=${e_list[0]}" target="_blank">${e?default("")}</a>
					      		</td>
				      		</#if>
	      		 		</#if>
			      	</#list>
			      	<td colspan="4" style="vertical-align:middle; text-align:center;">
					    <#if (e_list[4]=='处罚'&&e_list[5]=='未发布')>
					           <input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="57130ddfebe443f8b6885a6d234e3ab0">
					           <input type="button" value="删除" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="9ea29f23e1a111e8aa95005056166666"> 
					    <#elseif (e_list[4]=='消息'&&e_list[5]=='未发布'&&e_list[7]=='否')>         
					           <input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="57130ddfebe443f8b6885a6d234e3ab0">
					           <a class="btn btn-link" target="_blank" href="page_edit_v_fh_news_notification?zw_f_uid=${e_list[0]}">修改</a>
					           <input type="button" value="删除" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="9ea29f23e1a111e8aa95005056166666">
					    <#elseif (e_list[4]=='消息'&&e_list[5]=='未发布'&&e_list[7]=='是')>         
					           <input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="57130ddfebe443f8b6885a6d234e3ab0">
					           <input type="button" value="删除" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="9ea29f23e1a111e8aa95005056166666"> 
					  	<#elseif (e_list[5]=='已发布')>         
					           <input type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="57130dffebe443f8b6685a6d234e3ab0">
						</#if>
					
					</td>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>
<script>
	function moreRelease(tt){
	    var ids = [];
	    var flag = 0;
	    var flag1 = 0;
		$('input[id^="chb-item-"]').each(function(){
			if($(this).prop('checked') && $(this).attr('id') != 'chb-item-all'){
				flag = 1;
				if($(this).data('currentstatus') == '已发布'){
					flag1 = 1
				}
				ids.push($(this).val());
			}
		});
		if(flag1 == 1){
			alert("勾选的项目有已发布状态,已发布的项不可重新发布");
		}else{
			if(flag == 0){
				alert("请勾选要发布的消息");
			}else{
				zapjs.zw.func_do(tt, null, {zw_f_uids:ids.join(',')});
			}
		}
		
		
	}
</script>