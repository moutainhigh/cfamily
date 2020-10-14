<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>栏目信息</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#assign column_code = b_page.getReqMap()["zw_f_column_code"] >
<#assign column_type = b_page.getReqMap()["zw_f_column_type"] >
<#assign num_languanggao = b_page.getReqMap()["zw_f_num_languanggao"] >
<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign prod_codeOrName = b_page.getReqMap()["prodCodeOrName"] >

<#assign zw_p_index = b_page.getReqMap()["zw_p_index"]! >
<#if zw_p_index == "">
	<#assign zw_p_index = "1" >
</#if>
<div class="form-horizontal control-group">
	<#if  column_type=="4497471600010027">
		<a href="javascript:;" class="btn btn-link" id="addProduct"  target="">
			<input class="btn btn-success" type="button" value="添加商品"/>
		</a>
	<#else>
		<a href="../page/page_add_v_fh_apphome_column_content?zw_f_column_code=${column_code}&zw_f_column_type=${column_type}&zw_f_num_languanggao=${num_languanggao}" onclick="return checknum()" class="btn btn-link"  target="_blank">
			<input class="btn btn-success" type="button" value="新增栏目内容"/>
		</a>
	</#if>
</div>

<#if  column_type=="4497471600010027">
	<div align="right">
		<#if  prod_codeOrName!="">
			<input type="text" id="prodCodeOrName" style="margin: 0"  value="${prod_codeOrName}" />
		<#else>
			<input type="text" id="prodCodeOrName" style="margin: 0" placeholder="商品编号/商品名称" />
		</#if>
		<input class="btn btn-success" type="button" onclick="select_prod()" value="查询"/>
	</div>
</#if>

<div class="zab_info_page_title  w_clear myClass">
<span>栏目内容列表</span>
<input id="batchDel" class="btn btn-small" type="button" value="批量删除">
<#if  column_type=="4497471600010008"||column_type=="4497471600010013"||column_type=="4497471600010014"||column_type=="4497471600010025"||column_type=="4497471600010031">
<input class="btn btn-small" type="button" value="批量导入商品" onclick="show_import_windows()"/>
</#if>
</div>

<#if  column_type=="4497471600010027">
	<#assign logData=b_method.upControlPage("page_chart_v_fh_apphome_evaluation","prod_codeOrName=${prod_codeOrName}&column_code=${column_code}&zw_p_size=10&zw_p_index=${zw_p_index}").upChartData()>
	
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
			      	<#elseif e_list_index = 8>
			      		
			      	<#else>
			      	 	<th>
			      	 		${e_list}
			      	 	</th>
			    	</#if>
	 			</#list>
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
			  					<div class="w_left w_w_100">
			  						<#if e?default("") != "">
				  						<a target="_blank" href="${e?default("")}">
				      						<img src="${e?default("")}"> 
				      					</a>
				      				</#if>
			      				</div>
			      			</td>
			      		<#elseif e_index = 4>
			      	 		<td>
			      	 			<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${e_list[3]}" target="_blank">${e?default("")}</a>
			      			</td>
			      		<#elseif e_index = 8>
			      			
						<#else>
							<td>
					      		${e?default("")}
					      	</td>
				      	</#if>	
		      		</#list>
				</tr>
		 	</#list>
		</tbody>
	</table>
	
<#else>
	<#assign logData=b_method.upControlPage("page_chart_v_fh_apphome_column_content","zw_f_column_code=${column_code}&zw_p_size=20&zw_p_index=${zw_p_index}").upChartData()>
	
	<#assign  e_pagedata=logData />
	<#assign  e_pageField=logData.getPageField() />
	<#assign  datasize=logData.getPageData()?size />
	<table  class="table  table-condensed table-striped table-bordered table-hover">
		<thead>
		    <tr>
		        <#list e_pagedata.getPageHead() as e_list>
			      	 
			      	 	<#--一栏多行隐藏部分字段-->
			      	 	<#if column_type == "4497471600010025" 
			      	 			&& (e_pageField[e_list_index] == 'picture'
			      	 					|| e_pageField[e_list_index] == 'title'
			      	 					|| e_pageField[e_list_index] == 'description'
			      	 					|| e_pageField[e_list_index] == 'video_link')>
			      	 					
			      	 	<#elseif column_type == "4497471600010031" 
			      	 			&&(e_pageField[e_list_index] == 'picture'
			      	 					|| e_pageField[e_list_index] == 'title'
			      	 					|| e_pageField[e_list_index] == 'description')>	
			      	 					
			      	 	<#--非视频播放模版则隐藏部分字段-->				
			      	 	<#elseif column_type != "4497471600010020"  &&column_type != "4497471600010031"
			      	 			&& (e_pageField[e_list_index] == 'video_ad'
			      	 					|| e_pageField[e_list_index] == 'product_code')>		 
	                  
	                    	
			      	 	<#--视频播放列表隐藏部分字段-->				
			      	 	<#elseif column_type == "4497471600010017" 
			      	 			&& (e_pageField[e_list_index] == 'title'
			      	 					|| e_pageField[e_list_index] == 'description')>		
			      	 					
			      	 	<#--非视频播放列表隐藏部分字段-->				
			      	 	<#elseif column_type != "4497471600010017" 
			      	 			&& (e_pageField[e_list_index] == 'product_name'
			      	 					|| e_pageField[e_list_index] == 'product_price')>	
			      	 							      	 						      	 					     	 					
			      	 	<#elseif e_list_index = 0>
			      	 		<th>
			      	 		<input id="checkAllOrNone" type="checkbox"/>
			      	 		</th>
			      	 	<#else>
			      	 		<th>
			      	 		${e_list}
			      	 		</th>
			      	 	</#if>
		      </#list>
		    </tr>
	  	</thead>
	  
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign code=e_list[4]>
		  		 <#list e_list as e>
			      	 	<#--一栏多行隐藏部分字段-->
			      	 	<#if column_type == "4497471600010025" 
			      	 			&& (e_pageField[e_index] == 'picture'
			      	 					|| e_pageField[e_index] == 'title'
			      	 					|| e_pageField[e_index] == 'description'
			      	 					|| e_pageField[e_index] == 'video_link')>
			      	 					
			      	 	<#elseif column_type == "4497471600010031" 
			      	 			&& (e_pageField[e_index] == 'picture'
			      	 					|| e_pageField[e_index] == 'title'
			      	 					|| e_pageField[e_index] == 'description')>	
			      	 								
			      	 	<#--非视频播放模版则隐藏部分字段-->				
			      	 	<#elseif (column_type != "4497471600010020" &&column_type != "4497471600010031")
			      	 			&& (e_pageField[e_index] == 'video_ad'
			      	 					|| e_pageField[e_index] == 'product_code')>	
			      	 								
			      	 		 
	
			      	 	<#--视频播放列表隐藏部分字段-->				
			      	 	<#elseif column_type == "4497471600010017" 
			      	 			&& (e_pageField[e_index] == 'title'
			      	 					|| e_pageField[e_index] == 'description')>		
			      	 					
			      	 	<#--非视频播放列表隐藏部分字段-->				
			      	 	<#elseif column_type != "4497471600010017" 
			      	 			&& (e_pageField[e_index] == 'product_name'
			      	 					|| e_pageField[e_index] == 'product_price')>	
			      	 							      	 						      	 					     	 					
			      	 	<#elseif e_index = 0>
			      	 		<td>
			      	 		<input name="delCheck" type="checkbox" value="${e?default("")}"/>
			      	 		</td>
			      	 	<#elseif e_index = 1>
			      	 		<td>
			  					<div class="w_left w_w_100">
			  						<#if e?default("") != "">
				  						<a target="_blank" href="${e?default("")}">
				      						<img src="${e?default("")}"> 
				      					</a>
				      				</#if>
			      				</div>
			      			</td>	
			      		<#elseif e_index = 14>
			      			<td>
			  					<div class="editButten">
			  						${e?default("")}
			  					</div>
			  				</td>	
			  			<#elseif e_index = 16>
			  				<td>
			  					<div class="seeButten">
			  						${e?default("")}
			  					</div>
			      			</td>
			      		<#elseif e_index=5>
			      			<td>
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
				      		</td>	
				  		 <#else>
				  		 	<td>
				      			${e?default("")}
				      		</td>
				      	</#if>
		      	</#list>
		      	</tr>
		 	</#list>
			</tbody>
	</table>
</#if>
	

<@m_zapmacro_common_page_pagination b_page  e_pagedata />

<script>
$(document).ready(function(){

	if("${column_type}" != "4497471600010004"){  // 栏目类型 不是 导航栏 则隐藏 一行显示几栏
		$($(".form-horizontal > div")[8]).hide();
	}
    $("#checkAllOrNone").click(function(){
       $("tbody input[name='delCheck']").each(function(){
            this.checked=$("#checkAllOrNone").is(':checked');
       }); 
    });
	
    if("${column_type}" == "4497471600010027"){  // 栏目类型 是 商品评价时
		// 首页商品评价模板添加商品
		$("#addProduct").click(function(){
			var str = "";
			if(${datasize}>0){
				str = "确认重新添加商品吗?";
			}else{
				str = "确认添加商品?";
			}
			if(confirm(str)){
				var obj = {};
				zapjs.zw.api_call('com_cmall_familyhas_api_ApiHomeAddEvaluationProduct',obj,function(result) {
					if(result.resultCode==1){
						zapjs.zw.modal_show({
							content : "添加成功！",
							okfunc : 'zapjs.f.autorefresh()'
						});
				 	}
				});
			}
			return true;
		});
	}else{ // 栏目类型不为商品评价时
		
		$(".editButten a:first-child").each(function(){
			$(this).attr("href",$(this).attr("href")+"&column_type=${column_type}&num_languanggao=${num_languanggao}");
		});
		$(".seeButten a:first-child").each(function(){
			$(this).attr("href",$(this).attr("href")+"&column_type=${column_type}");
		});
	}
	
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
		var method = '';
		if("${column_type}" == "4497471600010027"){  // 栏目类型 是 商品评价时
			method = 'com_cmall_familyhas_api_ApiBatchDelEvaluationProd';
		}else{
			method = 'com_cmall_familyhas_api_ApiBatchDelColumnContent';
		}
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

function checknum(){
	if("${column_type}" == "4497471600010020"){
		if(${datasize}<0){
			zapjs.f.message('此类型栏目只能有一个内容');
			return false;
		}
		
	}
	return true;
};

// 商品评价商品查询
function select_prod(){
	var prodCodeOrName = $("#prodCodeOrName").val();
	window.location.href = '../page/page_book_v_fh_apphome_column?zw_f_column_code=${column_code}&zw_f_uid=${uid}&zw_f_column_type=${column_type}&zw_f_num_languanggao=${num_languanggao}&prodCodeOrName='+prodCodeOrName;
};

function show_import_windows(){
	zapjs.f.window_box({
		id : 'pph_import_product',
		content : '<iframe src="../show/page_import_ColumnContent?zw_f_seller_code=SI2003&column_code=${column_code}&column_type=${column_type}&infoCode=" frameborder="0" style="width:100%;height:500px;"></iframe>',
		width : '700',
		height : '550'
	});
}

</script>