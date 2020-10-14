
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
			      	 <#if (e_list_index >0)&&(e_list_index !=14)>
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
						<#if (e_index >0)&&(e_index !=14)>
						<td>
						      			${e?default("")}
					    </td>
					    </#if>
			      	</#list>
			      	<td style="vertical-align:middle; text-align:center;">
					    <#if (e_list[11]=='审核驳回')>
					           <a href="page_edit_v_uc_detain_integral?zw_f_uid=${e_list[0]}" class="btn btn-link">修改</a>
					           <input type="button" value="删除" onclick="javascript:deleteAll('${e_list[0]}')" class="btn btn-link" zapweb_attr_operate_id="809ee8e5c3e211e9abac005056165069"> 
					  	</#if>
					</td>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>

<script>
function deleteAll(uid){
	 if(confirm("删除后不能恢复，确认删除？")){
	 	var obj = {};
	 	zapjs.zw.func_do(obj, '809ee8e5c3e211e9abac005056165069', {zw_f_uid:uid});
	 }
	
};
function exportAll(obj){
	//zapjs.f.tourl(zapjs.f.upurl().replace("/page/", "/export/"));
		var operate_id=obj.getAttribute('zapweb_attr_operate_id');
		var sUrl = zapjs.f.upurl().replace("/page/", "/export/");

		//处理url
		if(sUrl.indexOf("?")>=0){
			var tmp_=sUrl.split("?");
			sUrl=tmp_[0]+"/"+operate_id+"?"+tmp_[1];
			
		}else{
			sUrl+="/"+operate_id;
		}
		
		var aHtml = [];
		aHtml.push('<div class="w_p_20">');
		aHtml.push('<a class="btn" target="_blank" href="' + sUrl + '">导出当前页</a>&nbsp;&nbsp;&nbsp;&nbsp;');
		console.log(sUrl);
		if(sUrl.indexOf("?")>=0){
			sUrl+="&amp;zw_p_size=-1";
			
		}else{
			sUrl+="?&amp;zw_p_size=-1";
		}
		
		aHtml.push('<a class="btn" target="_blank" href="' + sUrl + '">导出所有页</a>');
		aHtml.push('</div>');

		zapjs.f.window_box({
			content : aHtml.join(''),
			width : 400,
			height : 150
		});
};
</script>