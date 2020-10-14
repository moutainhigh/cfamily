
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
		      	<th width="12%">操作</th>
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
					    <#if (e_list[11]=='待审核')>
					           <input type="button" value="审核" onclick="examine('${e_list[0]}')" class="btn btn-small" zapweb_attr_operate_id="ebd2b738f382485490fce057b17a3b17"> 
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
function examine(uid){
			var showHtml = "<form class='form-horizontal' id='detain_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_uid' name='zw_f_uid' value='"+uid+"'>";
			showHtml+="<div class='control-group'><label class='control-label' for='zw_f_source_code'>审批意见:</label><div class='controls'><textarea id='zw_f_auditmind' name='zw_f_auditmind'></textarea><div class='btn-toolbar'><input class='btn btn-primary' type='button' value='通过' zap_tostatus_attr='4497471600460002' zapweb_attr_operate_id='ebd2b738f382485490fce057b17a3b17' style='margin-right:5px;'><input class='btn btn-primary' type='button' value='驳回' zap_tostatus_attr='4497471600460003' zapweb_attr_operate_id='ebd2b738f382485490fce057b17a3b17' style='margin-right:5px;'>";
			showHtml+="</div></div></div>";
			showHtml+="</form>";
			var modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
			zapjs.f.window_box(modalOption);
			$("#detain_form_forsubmit").find("input[zap_tostatus_attr]").bind(
					"click",function(){
						$("#zw_f_to_status").val($(this).attr("zap_tostatus_attr"));
						zapjs.zw.func_call(this);
					}	
			);
			
};
</script>