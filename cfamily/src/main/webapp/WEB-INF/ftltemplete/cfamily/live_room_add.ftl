<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<@m_zapmacro_common_page_add b_page />


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	<#if e_field.getPageFieldName()=="zw_f_anchor_phone">
	<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;每个直播间需要绑定一个用于核实主播身份,不会展示给观众</span>
	</#if>
	<@m_zapmacro_common_field_end />
	
</#macro>




<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<#if e_field.getPageFieldName()=="zw_f_start_time">
	<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;开播时间仅供参考,不是实际直播间开播的时间</span>
	</#if>
	<@m_zapmacro_common_field_end />
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	
	  
</#macro>

<#-- 字段：上传 -->
<#macro m_zapmacro_common_field_upload e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>		
	<@m_zapmacro_common_field_end />	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
	  
</#macro>

<script>
    $(function(){ 
        var html_ = '</br>温馨提示：</br><span style="line-height:32px; color:red">直播背景墙是每个直播间的默认背景</span>';
        html_ += '</br><span style="line-height:32px; color:red">建议尺寸 1080像素*1920像素</span>';
        html_ += '</br><span style="line-height:32px; color:red">图片大小不得超过2M</span>';
        $($("#zw_f_live_background_picture")[0].parentElement).append(html_);  
        
        var html_2 = '</br>温馨提示：</br>';
        html_2 += '<span style="line-height:32px; color:red">建议尺寸 1068像素*600像素</span>';
        html_2 += '</br><span style="line-height:32px; color:red">图片大小不得超过2M</span>';
        $($("#zw_f_live_cover_picture")[0].parentElement).append(html_2); 

        })
</script>
