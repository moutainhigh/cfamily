

<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>

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
		        	
			      	 <th>
			      	 	<#if e_list_index = 0>
			      	 		<input id="checkAllOrNone" type="checkbox"/>
			      	 	<#else>
			      	 		${e_list}
			      	 	</#if>
			      	 </th>
			      	 	<#if e_list_index==3 >
						 	 <th>商品分类</th>
						 	 <th>预览</th>
						</#if>
			      	 
		      </#list>
		    </tr>
	  	</thead>
		<tbody>
			<#list e_pagedata.getPageData() as e_list>
				<tr>
				<#assign productCode=e_list[1]>
		  		 <#list e_list as e>
		  		 	<#if e_index = 0>
		  		 		<td>
		      	 			<input name="delCheck" type="checkbox" value="${e?default("")}"/>
		      	 		</td>
	      			<#elseif e_index==3 >
	      				<td>
			      			${e?default("")}
			      		</td>
				 		 <td>
							<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
							<#assign keys=categoryMap?keys>
							<#list keys as key>
								${categoryMap[key]?trim}<br>
							</#list>
						</td>
						<td>
							<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${productCode}" target="_blank">预览</a>
						</td>
					<#elseif e_index==2>
						<td>
							<#assign mainPicUrl=productService.getMainPicUrl(productCode)>
							<front id="pic_tip_${e_list_index}" class="easyui-tooltip" > ${e?default("")} </front>
							
							<script>
								$(function(){
									$('#pic_tip_${e_list_index}').tooltip({
										position: 'top',
										content: '<img style="width:350px;height:350px;" src="${mainPicUrl?default("")}"/>',
										onShow: function(){
											$(this).tooltip('tip').css({
												borderColor: '#ff0000',
												boxShadow: '1px 1px 3px #292929'
											});
										},
										onPosition: function(){
											$(this).tooltip('tip').css('left', $(this).offset().left);
											$(this).tooltip('arrow').css('left', 20);
										}
									});
								});
							</script>
							
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
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
</div>
<script>

	function shenpi(){
			var  checkUid = new Array();
	 		$("tbody input[name='delCheck']").each(function(){
	            if(this.checked){
	            	checkUid.push(this.value);
	            }
	       	}); 
	 		if(checkUid.length <= 0){
	 			zapjs.f.message("你还没有选中任何商品！");
	 			return;
	 		}
		 	var showHtml = $('#flow_form_forsubmit_temp').html();
			var modalOption = {content:showHtml,title:"下架审批",oktext:"关闭",height:200};
			zapjs.f.window_box(modalOption);
 	}
 	
     	
 	function piliang_shenpi(){
		 	var showHtml = $('#flow_form_forsubmit_temp2').html();
			var modalOption = {content:showHtml,title:"下架审批",oktext:"关闭",height:200};
			zapjs.f.window_box(modalOption);
 	}
	$(document).ready(function(){
		$("#checkAllOrNone").click(function(){
	       $("tbody input[name='delCheck']").each(function(){
	            this.checked=$("#checkAllOrNone").is(':checked');
	       }); 
     	});
	});
	
	function xiajia(num,checkIdea){
		var  checkUid = new Array();
 		$("tbody input[name='delCheck']").each(function(){
            if(this.checked){
            	checkUid.push(this.value);
            }
       	}); 
       	
		var xiajiaFlag = '0';//全部下架
		var xiajiaUids  = '';
		if(num=='1'){//选中下架
			xiajiaFlag = '1';//选中下架
			for(var i = 0 ;i < checkUid.length ;i++ ){
				if(i<checkUid.length-1){
					xiajiaUids += checkUid[i] + ","
				}else{
					xiajiaUids += checkUid[i]
				}
			}
			
			
			//处理完 全局变量，重置
			checkUid = new Array();
		}
		
			var oData = {};
			oData.xiajiaFlag = xiajiaFlag;
			oData.xiajiaUids  = xiajiaUids;
			oData.checkIdea  = checkIdea;
			var defaults ={
				api_target : 'com_cmall_familyhas_api_ApiCheckUnshelveProduct',
				api_input : zapjs.f.tojson(oData),
				api_key : 'jsapi'
			};
			
			$.ajax({
	            url : '../jsonapi/com_cmall_familyhas_api_ApiCheckUnshelveProduct',
	            data: defaults,
	            cache : false, 
	            async : false,
	            type : "POST",
	            dataType : 'json',
	            success : function (data){
	            	if(data.resultCode==1){
	            		zapjs.zw.func_success(data)
	            	}else{
	            		
	            	}
	            }
	        });			
	}
	
	
</script>

<script type="text/template" id="flow_form_forsubmit_temp">
	<form class="form-horizontal" id="flow_form_forsubmit" method="POST" style='margin-top:20px;'>
	<div class="control-group">
		<div class="controls">
			<div class="btn-toolbar">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary"  value="下架" onclick="xiajia('1',true)">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary"  value="驳回" onclick="xiajia('1',false)">
			</div>
		</div>
	</div>
	</form>	
</script>

<script type="text/template" id="flow_form_forsubmit_temp2">
	<form class="form-horizontal" id="flow_form_forsubmit" method="POST" style='margin-top:20px;'>
	<div class="control-group">
		<div class="controls">
			<div class="btn-toolbar">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary"  value="下架" onclick="xiajia('0',true)">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary"  value="驳回" onclick="xiajia('0',false)">
			</div>
		</div>
	</div>
	</form>	
</script>


