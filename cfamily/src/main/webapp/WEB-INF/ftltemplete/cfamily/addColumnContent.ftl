<#assign column_type = b_page.getReqMap()["zw_f_column_type"] >
<#assign column_code = b_page.getReqMap()["zw_f_column_code"] >
<#assign num_languanggao = b_page.getReqMap()["zw_f_num_languanggao"] >
<#assign columnMap = b_method.upDataOne("fh_apphome_column","","","","column_code",column_code) >
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<script>
// 展示类型
var viewType = '${columnMap["view_type"]}';
require(['cfamily/js/addColumnContent'],
function()
{
	zapjs.f.ready(function()
		{
			addColumnContent.init(${column_type},${num_languanggao});
		}
	);
}

);
</script>

<@m_zapmacro_common_page_add b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<input id="zw_f_column_code" name="zw_f_column_code" type="hidden" value="${column_code}"/> 
	<input id="zw_f_column_type" name="zw_f_column_type" type="hidden" value="${column_type}"/>
	<input type="hidden" id="showmore_linktype" name="zw_f_showmore_linktype" disabled="disabled">
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
	
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_showmore_linkvalue">
				<div id="linkvalueDiv" class="control-group">
				<script type="text/javascript">
		         zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_showmore_linkvalue","value":"","max":"1"});});
	           </script>
					<label class="control-label" for="zw_f_showmore_linkvalue">
						URL：
					</label>
					<input id="zw_f_showmore_linkvalue_show_text" type="hidden" value="">
					<div id="slId" class="controls">
						<input id="zw_f_showmore_linkvalue" type="text" value="" name="zw_f_showmore_linkvalue">
					</div>
				</div>
		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<#if  e_field.getFieldName()=="product_code_select">
				<div class="control-group">
					<label class="control-label" for="zw_f_product_code_select">
						<span class="w_regex_need">*</span>商品选择：
					</label>
					<div class="controls">
						<div>
							<input id="aaaaa" type="hidden" value="">
							<input id="zw_f_product_code_select" type="hidden" value="" name="zw_f_product_code_select" >
							<input id="zw_f_product_code_select_show_text" type="hidden" value="">
							<script type="text/javascript">
								zapjs.f.require(['cfamily/js/product_single'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_code_select","value":"","max":"1"});});
							</script>
							<div class="w_left">
								<input class="btn" type="button" onclick="product_single.show_box('zw_f_product_code_select')" value="选择">
							</div>
							<div class="w_left w_w_70p"><ul id="zw_f_product_code_select_show_ul" class="zab_js_zapadmin_single_ul"></ul></div>
							<div class="w_clear"></div>
						</div>
					</div>
				</div>
	  		<#else>
	  			<@m_zapmacro_common_field_component  e_field  e_page/>
	  		 </#if>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	
		<#if e_field.getFieldName()=="position">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<select name="zw_f_position_select" id="zw_f_position_select" style="display:none">
				<option value="1">1</option>
			</select>
		<#elseif  e_field.getFieldName()=="title_color">
			<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" >
				<option value="#333333" style="color:#333333">黑色</option>
				<option value="#FFFFFF" style="color:#FFFFFF;">白色</option>
				<option value="#53359e" style="color:#53359e">紫色</option>
				<option value="#fd7f03" style="color:#fd7f03">橘色</option>
				<option value="#d80c18" style="color:#d80c18">红色</option>
				<option value="#6bbd00" style="color:#6bbd00">绿色</option>
			</select>
		<#elseif  e_field.getFieldName()=="description_color">
			<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" >
				<option value="#999999" style="color:#999999">灰色</option>
				<option value="#FFFFFF" style="color:#FFFFFF">白色</option>
			</select>
		<#elseif  e_field.getFieldName()=="title">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(建议6个字以内)</span>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：长文本框 -->
<#macro m_zapmacro_common_field_textarea e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<textarea id="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
		<#if  e_field.getFieldName()=="description">
			<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(建议10个字以内)</span>
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>
