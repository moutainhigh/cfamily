
<#assign page_type = b_page.getReqMap()["zw_f_page_type"] />
<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] />

<@m_zapmacro_common_page_edit b_page page_type tv_number/>


<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page page_type tv_number>
<form class="form-horizontal" method="POST" >
	<input id="zw_f_p_type" name="zw_f_p_type" type="hidden" value="${page_type }" />
	<input id="zw_f_tv_number" name="zw_f_tv_number" type="hidden" value="${tv_number }" />
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  page_type tv_number/>
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page page_type tv_number>
	<#-- 获取已经添加过的数据 -->
	<#assign servic=b_method.upClass("com.cmall.familyhas.service.DLQService")>
	<#assign adContent = servic.getAdOneCont(page_type,tv_number) />
	
	<#if e_pagedata??>
	<#list e_pagedata as e>
		
		<#if e.getPageFieldName() == "zw_f_uid">
		
			<input type="hidden" id="zw_f_uid" name="zw_f_uid" value="${adContent["zw_f_uid"]}">
			
		<#elseif e.getPageFieldName() == "zw_f_ad_name">
		
			<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
				<input type="text" id="${e.getPageFieldName()}" name="${e.getPageFieldName()}" ${e.getFieldExtend()} value="${adContent["zw_f_ad_name"]}">
			<@m_zapmacro_common_field_end />
			
		<#elseif e.getPageFieldName() == "zw_f_share_title">
		
			<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
				<input type="text" id="${e.getPageFieldName()}" name="${e.getPageFieldName()}" ${e.getFieldExtend()} value="${adContent["zw_f_share_title"]}">
			<@m_zapmacro_common_field_end />
			
		<#elseif e.getPageFieldName() == "zw_f_share_content">
		
			<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
				<input type="text" id="${e.getPageFieldName()}" name="${e.getPageFieldName()}" ${e.getFieldExtend()} value="${adContent["zw_f_share_content"]}">
			<@m_zapmacro_common_field_end />
			
		<#elseif e.getPageFieldName() == "zw_f_share_pic">
			<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
				<#if (adContent["zw_f_share_pic"]?length > 0) >
					<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e.getSourceParam()}" id="${e.getPageFieldName()}" name="${e.getPageFieldName()}" value="${adContent["zw_f_share_pic"]}">
					<span class="control-upload_iframe"></span>
					<span class="control-upload_process"></span>
					<span class="control-upload">
						<ul>
							<li class="control-upload-list-li">
								<div>
									<div class="w_left w_w_100">
										<a href="${adContent["zw_f_share_pic"]}" target="_blank">
											<img src="${adContent["zw_f_share_pic"]}">
										</a>
									</div>
									<div class="w_left w_p_10">
										<a href="javascript:zapweb_upload.change_index('zw_f_cuisine_picture',0,'delete')">删除</a>
									</div>
									<div class="w_clear"></div>
								</div>
							</li>
						</ul>
					</span>
				<#else>
					<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e.getSourceParam()}"    id="${e.getPageFieldName()}" name="${e.getPageFieldName()}" value="${e.getPageFieldValue()}">
					<span class="control-upload_iframe"></span>
					<span class="control-upload_process"></span>
					<span class="control-upload"></span>
				</#if>
			<@m_zapmacro_common_field_end />
			
			<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />

		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  	</#if>
	</#list>
	</#if>
</#macro>


<#-- 轮播列表 -->
<#assign lunboDetail=b_method.upControlPage("page_chart_v_fh_dlq_picture","zw_p_sql_where=id_number='1001' and delete_state = '1001' and p_type='${page_type}' and tv_number='${tv_number}'&zw_p_size=-1").upChartData()>

<#-- 列表页 -->
<a type="button" class="btn  btn-success" href="page_add_v_fh_dlq_picture?id_number=1001&zw_f_p_type=${page_type}&zw_f_tv_number=${tv_number}">添加轮播广告</a>
<div class="zab_info_page_title w_clear">
	<span>轮播广告列表</span><input type="button" class="btn" id="delAll" value="批量删除" onclick="delAll()"/>
</div>
<div class="zw_page_common_data">

	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		    	<th>
		    	 	<input type="checkbox" id="checkAll" onclick="selectAll(this)"/>
		    	</th>
		        <#list lunboDetail.getPageHead() as e_list>
			      	 <th>
			      	 	${e_list}
			      	 </th>
		        </#list>
		    </tr>
	  	</thead>	  
		<tbody>
			<#list lunboDetail.getPageData() as e_list>
				<tr>
					<th>
						<input type="checkbox" class="ckek" name="ckek"/>
					</th>
			  		<#list e_list as e>
		      				<td>
			      				${e?default("")}
			      			</td>
			      	</#list>
		      	</tr>
		 	</#list>
		</tbody>
	</table>
	
</div>


<script>
	
	function delAll() {
		var delUid = new Array();
		$(".ckek").each(function(i){
			if($(this).is(':checked')) {
				var uidStr = $(this).parent().parent().find("td:last input:eq(0)").attr("onclick");
				var preStr = uidStr.substring(0,20);
				if("zapjs.zw.func_delete" == preStr) {
					delUid[i] = uidStr.substring(27,uidStr.length-2);
				}
			}
		})
		delSelectedCheckboxInfo(delUid)
	}
	function selectAll (obje) {
		var isSelectAll = $(obje).is(':checked');
		$(".ckek").prop('checked', isSelectAll);
		
	}
	function delSelectedCheckboxInfo(uidArr) {
		if(uidArr.length > 0) {
			
			var obj = {}
			obj.paramType = "1005";
			obj.uid_str = uidArr.join(",");
			zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
				if(result.resultCode==1){
					zapjs.f.window_close("zapjs_f_id_window_box");
					zapjs.f.autorefresh();
				}else{
					zapadmin.model_message('添加失败');
				}
			});
		} else {
			zapjs.f.message('请选择需要删除的数据');
		}
	}
</script>



