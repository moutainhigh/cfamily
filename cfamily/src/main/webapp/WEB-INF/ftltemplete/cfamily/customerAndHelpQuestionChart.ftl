<#-- 客服与帮助-问题 2018-12-03 rhb -->
<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

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
			        	<#if (e_list_index gt 0) && (e_list_index lt 7)>
			        		<th>
								${e_list}
							</th>
						</#if>
			      	</#list>
			      	<th colspan="3" width="15%">
						操作
					</th>
			    </tr>
		  	</thead>
			<tbody>
				<#list e_pagedata.getPageData() as e_list>
					<tr>
			  		 <#list e_list as e>
			  		 	<#if (e_index gt 0) && (e_index lt 7)>
			  		 		<td style="vertical-align:middle; text-align:center;">
				  		 		<#if (e_index==3)>
				  		 			<a style="font-size:13px" class="btn btn-link" target="_blank" href="page_book_v_fh_common_problem_new?zw_f_uid=${e_list[0]}">${e?default("")}</a>
				  		 		<#elseif (e_index==4)>
				  		 			<#if e=="449748270001">
				  		 				发布
				  		 			<#elseif e=="449748270002">
				  		 				未发布
				  		 			</#if>
				  		 		<#else>
						      			${e?default("")}
						      	</#if>
					      	</td>
			      		</#if>
			      	</#list>
			      	
			      	<td colspan="3" style="vertical-align:middle; text-align:center;">
				      	<#if e_list[4]="449748270001">
				      		<input type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, {zw_f_qa_code: '${e_list[1]}'});" class="btn btn-small" zapweb_attr_operate_id="5650639a253b42faa5cd7fbb965c0281" />
				      	<#else>
				      		<input type="button" value="发布" onclick="zapjs.zw.func_do(this, null, {zw_f_qa_code: '${e_list[1]}'});" class="btn btn-small" zapweb_attr_operate_id="5650639a253b42faa5cd7fbb965c0281" />
				      		<a class="btn btn-small" target="_blank" href="page_edit_v_fh_common_problem_new?zw_f_uid=${e_list[0]}">修改</a>
				      		<input type="button" value="删除" onclick="zapjs.zw.func_delete(this,'${e_list[0]}');" class="btn btn-small" zapweb_attr_operate_id="7f32ddbcf2cf11e8aa34005056922802" />
				      	</#if>
			      	</td>
			      	
			      	</td>
			      	</tr>
			 	</#list>
			</tbody>
		</table>
		<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	</div>
</#macro>