<#assign smallSellerSodeSelected = b_page.getReqMap()["zw_f_small_seller_code"]! >
<@m_zapmacro_common_page_chart b_page />

<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->
	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#elseif  e.getFieldName()=="small_seller_code">
			<#-- 商户名称 -->
			<div class="control-group">
				<label class="control-label" for="zw_f_small_seller_code">商户名称</label>
				<div class="controls">
				<select name="zw_f_small_seller_code" id="zw_f_small_seller_code">
					<option value="">请选择</option>
					<#assign selectList = b_method.upDataQuery("uc_sellerinfo","","small_seller_code IN('SF031JDSC','SF03WYKLPT')")>
					<#list selectList as item>
						<option value="${item.small_seller_code}" ${(item.small_seller_code == smallSellerSodeSelected)?string('selected','')}>${item.seller_company_name}</option>
					</#list>
				</select>
				</div>
			</div>		
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
		
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign  e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	 <#if (e_list_index > 0)>  <#-- 忽略zid字段 -->
		      	 <th>
		      	 	${e_list}
		      	 </th>	        	 
	        	 </#if>
	      </#list>
		      	 <th>
		      	 	订单详情
		      	 </th>	  
		      	 <th>
		      	 	操作
		      	 </th>		      	     
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<#assign orderInfo = b_method.upDataQuery("oc_orderinfo","uid","","order_code",e_list[1])>
			<tr>
	  		 <#list e_list as e>
	  		 <#if (e_index > 0)>
	      		<td>
	      			${e?default("")}
	      		</td>
	      	 </#if>
	      	</#list>
	      		<td>
	      			<#assign orderInfo = b_method.upDataQuery("oc_orderinfo","uid","","order_code",e_list[1])>
	      			<#if (orderInfo?size > 0)>
	      				<a href="page_book_v_cf_sellororder_manage1?zw_f_uid=${orderInfo[0].uid}" class="btn btn-link"  target="_blank">查看订单</a>
	      			</#if>
	      		</td>
	      		<td>
	      			<input zapweb_attr_operate_id="f77a74aaf6904570a876f7a7fbd38312" onclick="zapjs.zw.func_tip(this,'${e_list[0]}','忽略')" type="button" class="btn btn-small" value="忽略">
	      			
	      			<#if (orderInfo?size > 0) && e_list[e_pageField?seq_index_of("ex_type")] == "下单失败" && orderInfo[0].order_status == '4497153900010002' && orderInfo[0].out_order_code == ''>
	      				<#-- 京东下单失败订单处理 -->
	      				<#if orderInfo[0].small_seller_code == 'SF031JDSC'>
	      					<input onclick="showDialog('page_edit_v_oc_order_sanfang_address?zw_f_uid=${e_list[0]}')" type="button" class="btn btn-small" value="处理">
	      				</#if>
	      			</#if>
	      		</td>	      		      	
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>

<script>
function showDialog(url){
	zapjs.f.window_box({
		id : 'edit',
		content : '<iframe src="'+url+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
		width : '800',
		height : '550'
	});
}
</script>