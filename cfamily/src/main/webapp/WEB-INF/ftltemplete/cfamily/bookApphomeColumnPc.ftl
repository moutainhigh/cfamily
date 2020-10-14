<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>栏目信息</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#assign column_code = b_page.getReqMap()["zw_f_column_code"] >
<#assign column_type = b_page.getReqMap()["zw_f_column_type"] >
<#assign num_languanggao = b_page.getReqMap()["zw_f_num_languanggao"] >
<div class="form-horizontal control-group">
	<a href="../page/page_add_v_fh_apphome_column_content_pc?zw_f_column_code=${column_code}&zw_f_column_type=${column_type}&zw_f_num_languanggao=${num_languanggao}" class="btn btn-link"  target="_blank">
		<input class="btn btn-success" type="button" value="新增栏目内容"/>
	</a>
</div>

<div class="zab_info_page_title  w_clear">
<span>栏目内容列表</span>
<input id="batchDel" class="btn btn-small" type="button" value="批量删除">
</div>
<#assign logData=b_method.upControlPage("page_chart_v_fh_apphome_column_content_pc","zw_f_column_code=${column_code}&zw_p_size=-1").upChartData()>

<#assign  e_pagedata=logData />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	<#if e_list_index = 0>
		      	 		<input id="checkAllOrNone" type="checkbox"/>
		      	 	<#else>
		      	 		${e_list}
		      	 	</#if>
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<#assign code=e_list[4]>
	  		 <#list e_list as e>
  				<td>
  					<#if e_index = 0>
		      	 		<input name="delCheck" type="checkbox" value="${e?default("")}"/>
		      	 	<#elseif e_index = 1>
		  					<div class="w_left w_w_100">
		  						<#if e?default("") != "">
			  						<a target="_blank" href="${e?default("")}">
			      						<img src="${e?default("")}"> 
			      					</a>
			      				</#if>
		      				</div>
		      		<#elseif e_index = 12>
		  					<div class="editButten">
		  						${e?default("")}
		  					</div>
		      			
		      		<#elseif e_index=5>
			      			<#if e_list[3]?default("") == "分类搜索">
			      				<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
			      				<#assign categoryMap=sellercategoryService.getCateGoryShow(code,"SI2003")>
			      				${categoryMap.categoryName?default("")}
			      			<#elseif e_list[3]?default("") == "商品详情">
			      				<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
			      				<#assign product=productService.getProductInfoForMabyLoveChart(code)>
			      				商品编号：${e?default("")}<br/>
			      				商品名称：${product.productName?default("")}
			      			<#else>
			      				${e?default("")}
			      			</#if>
			  		 <#else>
			      			${e?default("")}
			      	</#if>
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>

<script>
$(document).ready(function(){
	$(".editButten a:first-child").each(function(){
		$(this).attr("href",$(this).attr("href")+"&column_type=${column_type}&num_languanggao=${num_languanggao}");
	});
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
	 // 确认是否执行删除操作
	function func_confirm(tip,obj) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">'+tip+'</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					 zapjs.zw.api_call('com_cmall_familyhas_api_ApiBatchDelColumnContent',obj,function(result) {
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