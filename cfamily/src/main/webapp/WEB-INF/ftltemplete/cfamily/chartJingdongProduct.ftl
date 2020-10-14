<#assign categorySelected=b_page.upReqMap()["zw_f_category"]!>
<#assign saleState=b_page.upReqMap()["zw_f_sale_state"]!>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	<#if e_field.getPageFieldName() == "zw_f_category">
      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
				<option value="">请选择</option>
				<#assign catList=b_method.upDataQuery("pc_jingdong_category","","","cat_class","0")>
      			<#list catList as e_key>
					<option value="${e_key['name']}" <#if categorySelected==e_key['name']> selected="selected" </#if>>${e_key['name']}</option>
				</#list>
      		</select>
	<#elseif e_field.getPageFieldName() == "zw_f_sale_state">     
      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
				<option value="">请选择</option>
				<option value="1" <#if saleState=="1"> selected="selected" </#if>>是</option>
				<option value="0" <#if saleState=="0"> selected="selected" </#if>>否</option>
      		</select>	 		
	<#else>
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign fieldList=e_pagedata.getPageField()>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
      			<#if fieldList[e_list_index]=="uid">
      				<th>
      				<input type="checkbox" onclick="checkAll(this)">
      				</th>
      			<#else>
			      	 <th>
			      	 	${e_list}
			      	 </th>	      			
      			</#if>	        
      			<#if fieldList[e_list_index]=="jd_price">
      				<#--销售价后面添加毛利率字段-->
			      	 <th>
			      	 	毛利率
			      	 </th>      			
      			</#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 	<#if fieldList[e_index]=="uid">
		  		 	<td>
		  		 	<#if e_list[fieldList?seq_index_of("push_state")]=="否">
		  		 		<input type="checkbox" name="check-item" value="${e}">
	  		 		</#if>
	  		 		</td>  
      			<#elseif fieldList[e_index]=="sale_state">
	      			<td>
		      			${(e == "1")?string("是","否")}
		      		</td>   
      			<#elseif fieldList[e_index]=="推送">
 	      			<td>
		      			<#if e_list[fieldList?seq_index_of("push_state")]=="否">
		      				${e?default("")}
		      			</#if>
		      		</td>      				
      			<#else>
	      			<td>
		      			${e?default("")}
		      		</td>
      			</#if>
      			
      			<#if fieldList[e_index]=="jd_price">
	      			<td>
		      			${m_page_helper("com.cmall.familyhas.pagehelper.PriceProfitHelper",e_list[fieldList?seq_index_of("cost_price")],e_list[fieldList?seq_index_of("jd_price")])}
		      		</td>      				
      			</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>
<script>
// 批量推送商品
function pushBatch(oElm){
	var checkItemList = $('input[name="check-item"]:checked')
	if(checkItemList.length == 0) {
		alert('请选择推送的商品')
		return;
	}
	
	var sUids = []
	for(var i = 0; i < checkItemList.length;i++) {
		sUids.push(checkItemList.eq(i).val())
	}
	
	push(oElm,sUids.join(','))
}
// 推送商品
function push(oElm,sUid) {
	if($('#zapjs_f_id_modal_message').size() == 0) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">确认推送商品吗？</div>');
	}
	var aButtons = [];
	aButtons.push({
		text : '是',
		handler : function() {
				$('#zapjs_f_id_modal_message').dialog('close');
				$('#zapjs_f_id_modal_message').remove();
				zapjs.zw.func_do(oElm, $(oElm).attr('zapweb_attr_operate_id'), {zw_f_uid : sUid});
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

// 更新商品信息
function refresh(oElm,sUid) {
	zapjs.zw.func_do(oElm, $(oElm).attr('zapweb_attr_operate_id'), {zw_f_uid : sUid});
}

// 全选
function checkAll(oElm){
	$('input[name="check-item"]').prop('checked',$(oElm).prop('checked'))
}
</script>
<@m_zapmacro_common_page_chart b_page />