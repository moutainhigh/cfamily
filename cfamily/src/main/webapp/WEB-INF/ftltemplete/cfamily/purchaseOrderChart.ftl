<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<#--活动设置页面-->
<@m_zapmacro_common_page_chart b_page />
<@m_common_html_js ["cfamily/js/purchaseOrder.js"]/>
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>

	<#assign e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
		
	</form>
</#macro>

<#-- 按钮显示 -->
<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
    		
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    				<input type="button" class="btn  btn-success"  onclick="bachOrdersImport()" value="批量下单">
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    		
    			</#if>
    		</#list>
</#macro>


<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_datesfm e e_page/>
		<#elseif e.getFieldName()=="channel_limit">
			<#-- 走特殊处理 -->
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>

<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		从
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}_zw_a_between_from" name="${e_field.getPageFieldName()}_zw_a_between_from" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_from"))?default("")}">
		到
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}_zw_a_between_to" name="${e_field.getPageFieldName()}_zw_a_between_to" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_to"))?default("")}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	      <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index <9 )>
			      		<td>
				      			${e_list}
			      		</td>
		      	 </#if>
	      </#list>
	        <th colspan="4" width="20%">	操作</th>
	    </tr>
  	</thead>
  
  	<#assign statusIndex = 6>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  			 <#if (e_index <9 )>
			      		<td>
				      			${e?default("")}
			      		</td>			 
	      		 </#if>
	      	</#list>
	      	
	      	<td colspan="4" style="vertical-align:middle; text-align:center;">
				    <#if (e_list[statusIndex] == '等待审核'||e_list[statusIndex] == '采购完成')>
				        <a class="btn btn-link" target="_blank" href="page_book_v_oc_purchase_order?zw_f_purchase_order_id=${e_list[0]}">查看</a>
				    <#elseif (e_list[statusIndex]=='审核拒绝')> 
				           <a class="btn btn-link" target="_blank" href="page_edit_v_oc_purchase_order?zw_f_uid=${e_list[0]}&zw_f_purchase_order_id=${e_list[0]}">修改</a>
				           <a class="btn btn-link" target="_blank" href="page_book_v_oc_purchase_order?zw_f_purchase_order_id=${e_list[0]}">查看</a>
				   <#elseif (e_list[statusIndex]=='执行成功')>         
				 		   <input type="button" value="确认收货"  class="btn btn-small" onclick="purchaseOrder.func_tip(this,'${e_list[0]}','449748490004');">
				 	       <a class="btn btn-link" target="_blank" href="page_book_v_oc_purchase_order?zw_f_purchase_order_id=${e_list[0]}">查看</a>
				   </#if>
			</td>
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>

<script type="text/javascript">
 function bachOrdersImport() {
 zapadmin.window_url('../show/page_import_purchaseOrders')
 }
</script>
