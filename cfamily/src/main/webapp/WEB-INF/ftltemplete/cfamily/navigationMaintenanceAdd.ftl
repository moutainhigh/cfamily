<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<@m_zapmacro_common_page_add b_page />
<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
<#assign nav = b_method.upDataToJson('fh_apphome_nav','select nav_code,remark from fh_apphome_nav WHERE start_time < now() AND end_time  AND release_flag = \'01\' AND is_delete = \'02\' AND nav_icon is not null AND nav_icon != \'\'') >
<script>
require(['cfamily/js/addNavigate'],

function()
{
	zapjs.f.ready(function()
		{
		addNavigate.init();
		var navList = ${nav};
			var nav_code = "";
			var nav_name = "";
			var option = "";
			for(var i = 0;i < navList.length;i++){
				nav_code = navList[i].nav_code;
				remark = navList[i].remark;
				option = "option[value="+nav_code+"]";
				if($(option).text() == ""){
					$(option).text(remark);				
				}
			}
		}
	);
}

);
</script>
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	
	<input type="hidden" id="showmore_linktype" name="zw_f_showmore_linktype" disabled="disabled">
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
	
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
	 
		<#if e.getPageFieldName() = "zw_f_before_pic"> 
		<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e.getSourceParam()}"    id="${e.getPageFieldName()}" zapweb_attr_regex_id="469923180002" name="${e.getPageFieldName()}" value="${e.getPageFieldValue()}">
		<span class="control-upload_iframe"><iframe src="../upload/upload?zw_s_source=zw_f_before_pic&amp;zw_s_description=" class="zw_page_upload_iframe" frameborder="0"></iframe></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>		
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />

	   <#elseif e.getPageFieldName() = "zw_f_showmore_linktype"> 
		<div class="control-group" style="display:none;" id = "showmoreLinktype">
		<label class="control-label" for="zw_f_showmore_linktype"><span class="w_regex_need">*</span>链接类型：</label>
		<div class="controls">
		<select name="zw_f_showmore_linktype" id="zw_f_showmore_linktype" zapweb_attr_regex_id="469923180002">
		
	 </select>

   </div>
	</div>
	<#elseif e.getPageFieldName() = "zw_f_showmore_linkvalue"> 
	<div id="linkvalueDiv" class="control-group" style ="display:none;">
	<script type="text/javascript">
     zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_showmore_linkvalue","value":"","max":"1"});});
   </script>
		<label class="control-label" for="zw_f_showmore_linkvalue">
		<span class="w_regex_need">*</span>URL：
		</label>
		<input id="zw_f_showmore_linkvalue_show_text" type="hidden" value="">
		<div id="slId" class="controls">
			<input id="zw_f_showmore_linkvalue" type="text" value="" name="zw_f_showmore_linkvalue"  zapweb_attr_regex_id="469923180002">
			<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;('URL地址不能包含http://share')</span>
		</div>
	</div>
		<#elseif e.getPageFieldName() = "zw_f_type_name">
		<div style="display: block;" class="control-group">
		<label class="control-label" for="zw_f_type_name"><span class="w_regex_need">*</span>类型名称：</label>
		<div class="controls">

			<input id="zw_f_type_name" name="zw_f_type_name" zapweb_attr_regex_id="469923180002" value="" type="text">
		</div>
	</div>
	<#elseif e.getPageFieldName() = "zw_f_after_pic">
	
	<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
	<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e.getSourceParam()}"    id="${e.getPageFieldName()}" zapweb_attr_regex_id="469923180002" name="${e.getPageFieldName()}" value="${e.getPageFieldValue()}">
	<span class="control-upload_iframe"><iframe src="../upload/upload?zw_s_source=zw_f_after_pic&amp;zw_s_description=" class="zw_page_upload_iframe" frameborder="0"></iframe></span>
	<span class="control-upload_process"></span>
	<span class="control-upload"></span>		
	<@m_zapmacro_common_field_end />
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />

	<#elseif e.getPageFieldName() = "zw_f_before_fontcolor">
		<div style="display: block;" class="control-group">
		<label class="control-label" for="zw_f_before_fontcolor"><span class="w_regex_need">*</span>选中前字体颜色：</label>
		<div class="controls">

			<input id="zw_f_before_fontcolor" name="zw_f_before_fontcolor" zapweb_attr_regex_id="469923180002" value="" type="text">
		</div>
	</div>

	<#elseif e.getPageFieldName() = "zw_f_after_fontcolor">
		<div style="display: block;" class="control-group">
		<label class="control-label" for="zw_f_after_fontcolor"><span class="w_regex_need">*</span>选中后字体颜色：</label>
		<div class="controls">
	
			<input id="zw_f_after_fontcolor" name="zw_f_after_fontcolor" zapweb_attr_regex_id="469923180002" value="" type="text">
		</div>
	</div>
	<#elseif e.getPageFieldName() = "zw_f_start_time">
	<div class="control-group">
	<label class="control-label" for="zw_f_start_time"><span class="w_regex_need">*</span>开始时间：</label>
	<div class="controls">
	
		<input zapweb_attr_regex_id="469923180002" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" name="zw_f_start_time" value="${e.getPageFieldValue()}" type="text">
	</div>
	</div>
	<#elseif e.getPageFieldName() = "zw_f_end_time">
	<div class="control-group">
	<label class="control-label" for="zw_f_end_time"><span class="w_regex_need">*</span>结束时间：</label>
	<div class="controls">
	
		<input autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_end_time" name="zw_f_end_time" zapweb_attr_regex_id="469923180002" value="${e.getPageFieldValue()}" type="text">
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
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
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





<link rel="stylesheet" href="../resources/cfamily/js/colpick.css" type="text/css"/>
<script type="text/javascript" src="../resources/cfamily/js/colpick.js"></script>

<script type="text/javascript">
    $(function(){
	   $('#zw_f_before_fontcolor').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){
		});
		
		$('#zw_f_after_fontcolor').colpick({
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