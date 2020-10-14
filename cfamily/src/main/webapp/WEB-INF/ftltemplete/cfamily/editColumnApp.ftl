<link rel="stylesheet" href="../resources/cfamily/js/colpick.css" type="text/css"/>
<script type="text/javascript" src="../resources/cfamily/js/colpick.js"></script>
<script>
require(['cfamily/js/editColumnApp'],

function()
{
	zapjs.f.ready(function()
		{
			editColumnApp.init();
			$("#zw_f_column_name_old").val($("#zw_f_column_name").val());
		}
	);
}
);
</script>

<@m_zapmacro_common_page_edit b_page />

<#-- 编辑页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
<input id="view_type" type="hidden" name= "zw_f_view_type">
<input id="column_type" type="hidden" name= "zw_f_column_type">
<input id="zw_f_column_name_old" type="hidden" name= "zw_f_column_name_old">
	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
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

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
				<#if e_field.getFieldName()=="showmore_linktype">
					<#if e_field.getPageFieldValue()=="4497471600020003">
						<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
						<#assign categroyCode=b_method.upFiledByFieldName(b_page.upBookData(),"showmore_linkvalue").getPageFieldValue() /> 
						<#assign categoryMap=sellercategoryService.getCateGoryShow(categroyCode,"SI2003")>
						<input id="categroyName" type="hidden" value= "${categoryMap.categoryName}">
					</#if>
				</#if>
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<#if e_field.getFieldName()=="showmore_linkvalue">
		<input id="showmore_type" type="hidden" name= "zw_f_is_showmore">
		<div id="linkvalueDiv" class="control-group">
		<script type="text/javascript">
	     zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_showmore_linkvalue","value":"","max":"1"});});
	   </script>
			<label class="control-label" for="zw_f_showmore_linkvalue">
				URL：
			</label>
			<div class="controls">
				<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" value="${e_field.getPageFieldValue()}" name="zw_f_showmore_linkvalue">
				<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>
			</div>
		</div>
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<@m_zapmacro_common_field_end />
	</#if>
</#macro>

<#--做个下拉选过滤-->
<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	<#if e_field.getPageFieldName()="zw_f_column_type">
	    <select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>
                        <#if e_key.getV()="4497471600010001"||e_key.getV()="4497471600010002"||e_key.getV()="4497471600010004"||e_key.getV()="4497471600010008"||e_key.getV()="4497471600010010"||e_key.getV()="4497471600010011"||e_key.getV()="4497471600010013"||e_key.getV()="4497471600010014"
                        ||e_key.getV()="4497471600010022"||e_key.getV()="4497471600010024"||e_key.getV()="4497471600010020"||e_key.getV()="4497471600010016"||e_key.getV()="4497471600010025"||e_key.getV()="4497471600010026"||e_key.getV()="4497471600010027"
                        ||e_key.getV()="4497471600010028"||e_key.getV()="4497471600010029"||e_key.getV()="4497471600010030"||e_key.getV()="4497471600010031"||e_key.getV()="4497471600010035"||e_key.getV()="4497471600010036"||e_key.getV()="4497471600010037">
                        <option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
                        </#if>
						
					</#list>
	      		</select>
	<#else>
		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	</#if>
	      		
	<@m_zapmacro_common_field_end />
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