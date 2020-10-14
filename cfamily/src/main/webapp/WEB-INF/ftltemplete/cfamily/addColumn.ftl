<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<link rel="stylesheet" href="../resources/cfamily/js/colpick.css" type="text/css"/>
<script type="text/javascript" src="../resources/cfamily/js/colpick.js"></script>
<script>
require(['cfamily/js/addColumn'],

function()
{
	zapjs.f.ready(function()
		{
			addColumn.init();
		}
	);
}

);
</script>

<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>

<#-- 接受列表页面的传值 nav_code -->
<#assign nav_code = b_page.getReqMap()["zw_f_nav_code"]?default("") >
<#assign nav_type = b_page.getReqMap()["zw_f_nav_type"]?default("") >

<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
	
</form>
</#macro>




<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<input id="zw_f_nav_code" type="hidden" value="${nav_code}"  name="zw_f_nav_code">
	<input id="zw_f_nav_type" type="hidden" value="${nav_type}"  name="zw_f_nav_type">  
	<input id="showmore_type" type="hidden" name= "zw_f_is_showmore">
	<#-- <input id="view_type" type="hidden" value="${nav_type}" name= "zw_f_view_type"> -->
	<input id="miaoshalinktype" type="hidden" name= "zw_f_showmore_linktype">
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
						<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" value="" name="zw_f_showmore_linkvalue">
						<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>
					</div>
				</div>
		<#elseif e.getPageFieldName() = "zw_f_category_codes">
		    <@m_zapmacro_common_auto_field e e_page/>
		    

    
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
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<#if e_field.getPageFieldName() == 'zw_f_future_program'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "请选择档数"/>
	  		<#else>
	  			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  		</#if>
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

<script type="text/javascript">

   $(function(){
	   $('#zw_f_column_name_corlor').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){
		
		
		});
		});
		
</script>