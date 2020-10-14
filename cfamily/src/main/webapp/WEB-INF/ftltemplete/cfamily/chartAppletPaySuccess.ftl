<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 页面按钮 -->

<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
    		
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    		
    			</#if>
    		</#list>
    		
    		<input type="button" class="btn  btn-success"  onclick="batchDel()"   value="批量删除">

</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <th><input id="item-all" type="checkbox" onclick = "allSelect()"></th> 
	    	<#list e_pagedata.getPageHead() as e_list>
		       <#if (e_list_index > 0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			   </#if>
	    	</#list>
	    </tr>
  	</thead>
  	
  	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<#assign product_code=e_list[1]>
			<tr>
			<td><input type='checkbox'  class='zb_class' value= '${e_list[0]}'/>
				<#list e_list as e>
					<#if (e_index > 0)>
			  				<#if e_index = 5>
			  					<td>
			  						<div class="w_left w_w_100">
			  							<a target="_blank" href="${e?default("")}">
			      							<img src="${e?default("")}"> 
			      						</a>
			      					</div>
			      				</td>		
			  		 		<#else>
			      				<td>
			      					${e?default("")}
			      				</td>
			      			</#if>
			      	</#if>
		  		</#list>
	  		</tr>
	 	</#list>
	</tbody>
</table>


<script>
		   function allSelect(){
				if($("#item-all").is(':checked')){
				$(".zb_class").each(function(){
				   $(this).prop("checked",true);
			      });	
				}else{
				$(".zb_class").each(function(){
				   $(this).prop("checked",false);
				  });	
				}
			}
			
			function batchDel() {
				var codes = [];
				$(".zb_class").each(function() {
					if ($(this).is(':checked')) {
						codes.push($(this).val());
					}
				});
				if (codes.length == 0) {
					zapadmin.model_message('请选择商品！');
					return;
				}
				var pIds = codes.join(',');
				
				var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
				$(document.body).append(sModel);
				$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认要删除么？</div>');
				var aButtons = [];
				aButtons.push({
					text : '是',
					handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
						var obj = {};
						obj.tableName = "pc_applet_pay_success";
						obj.fieldName="uid";
						obj.fieldValus=pIds;
						obj.delFlag="0";
						zapjs.zw.api_call(
								'com_cmall_familyhas_api_ApiForBatchDel',
								obj, function(result) {
									if (result.resultCode == 1) {
										var url = window.location.href;
										window.location.href="";
										window.location.href = url;
									} else {
										zapadmin.model_message('删除异常');
									}
								});
		
					}
				}, {
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

</script>

</#macro>