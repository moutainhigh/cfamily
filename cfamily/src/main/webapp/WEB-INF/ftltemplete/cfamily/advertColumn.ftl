<#assign adver_entry_type = b_page.getReqMap()["zw_f_adver_entry_type"] >
<#assign advertise_code = b_page.getReqMap()["zw_f_advertise_code"] >
<#assign programa_num = b_page.getReqMap()["zw_f_programa_num"] >
<#assign start_time = b_page.getReqMap()["zw_f_start_time"] >
<#assign end_time = b_page.getReqMap()["zw_f_end_time"] >
<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>栏目信息</span>
	</div>
	<form class="form-horizontal" method="POST">
		<div class="control-group">
			<label class="control-label" for="">入口类型:</label>
			<div class="controls">
				<div class="control_book">
			  		${adver_entry_type}
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">广告栏数:</label>
			<div class="controls">
				<div class="control_book">
			  		${programa_num}
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">开始时间:</label>
			<div class="controls">
				<div class="control_book">
			  		${start_time}
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">结束时间:</label>
			<div class="controls">
				<div class="control_book">
			  		${end_time}
				</div>
			</div>
		</div>
	</form>
</div>




<div class="zab_info_page_title  w_clear myClass">
<span>栏目内容列表</span>
<a class="btn btn-small" href="javascript:void(0);" id="jump">新增栏目内容</a>
<input id="batchDel" class="btn btn-small" type="button" value="批量删除">
</div>


	
<#assign logData=b_method.upControlPage("page_chart_v_fh_advert_column","zw_f_advertise_code=${advertise_code}").upChartData()>
<#assign  e_pagedata=logData />
<#assign  e_pageField=logData.getPageField() />
<#assign  datasize=logData.getPageData()?size />
<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
		        	<#if e_list_index = 0>
			      	 	<th>
			      	 		<input id="checkAllOrNone" type="checkbox"/>
			      	 	</th>
			      	<#else>
			      	 	<th>
			      	 		${e_list}
			      	 	</th>
			    	</#if>
	 			</#list>
	 			<th colspan="4" width="20%">	操作</th>
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
					<#list e_list as e>
						<#if e_index = 0>
			      	 		<td>
			      	 			<input name="delCheck" type="checkbox" value="${e?default("")}"/>
			      	 		</td>
			      		<#elseif e_index = 1>
			      	 		<td>
	  		    				<img src="${e?default("")}" style="width:150px;" />
	  		    			</td>
			      		<#-- <#elseif e_index = 4>
			      	 		<td>
			      	 			<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${e_list[3]}" target="_blank">${e?default("")}</a>
			      			</td>
			      		<#elseif e_index = 8> -->
			      			
						<#else>
							<td>
					      		${e?default("")}
					      	</td>
				      	</#if>	
		      		</#list>
		      		<td colspan="4" style="vertical-align:middle; text-align:center;">
					    <a class="btn btn-link" target="_blank" href="page_edit_v_fh_advert_column?zw_f_uid=${e_list[0]}&zw_f_advertise_code=${advertise_code}">修改</a>
					    <input zapweb_attr_operate_id="7036534b5eea11eaabac005056165069" class="btn btn-small" onclick="zapjs.zw.func_delete(this,'${e_list[0]}')" type="button" value="删除">
					</td>
				</tr>
		 	</#list>
		</tbody>
	</table>

<script>
$(document).ready(function(){
 	$("#checkAllOrNone").click(function(){
       $("tbody input[name='delCheck']").each(function(){
            this.checked=$("#checkAllOrNone").is(':checked');
       }); 
    });
	$("#batchDel").click(function(){
 		var checkUid = new Array();
 		$("tbody input[name='delCheck']").each(function(){
            if(this.checked){
            	checkUid.push(this.value);
            }
       }); 
 		if(checkUid.length <= 0){
 			zapjs.f.message("你还没有选中任何栏目内容！");
 			return false;
 		}
 		var obj = {};
		obj.uids = checkUid;
		func_confirm("这样会删除选中的"+checkUid.length+"个栏目内容，确认要继续吗？",obj)
	 });
	 $("#jump").click(function(){
      if(${programa_num} > ${datasize}){
			window.location.href = "page_add_v_fh_advert_column?zw_f_advertise_code=${advertise_code}";
		}else{
			alert("广告栏数已满");
		}
    });
	// 确认是否执行删除操作
	function func_confirm(tip,obj) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">'+tip+'</div>');
		var aButtons = [];
		method = 'com_cmall_familyhas_api_ApiBatchDelAdvertColumn';
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					zapjs.zw.api_call(method,obj,function(result) {
						if(result.resultCode==1){
							zapjs.zw.modal_show({
								content : "删除成功！",
								okfunc : 'zapjs.f.autorefresh()'
							});
					 	}
			     	});
				}
		},{
			text : '否',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
				}
		});
		
		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
	}
});

</script>