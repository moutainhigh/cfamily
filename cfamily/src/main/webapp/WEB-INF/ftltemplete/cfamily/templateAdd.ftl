
<@m_zapmacro_common_page_add b_page />

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
		
		<#if e_field.getPageFieldName() =="zw_f_share_title">
			
			<@m_zapmacro_common_field_start text="<span class='w_regex_need'>*</span>"+e_field.getFieldNote() for=e_field.getPageFieldName() />
				<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<@m_zapmacro_common_field_end />
			
		<#elseif e_field.getPageFieldName() =="zw_f_share_content">
			<@m_zapmacro_common_field_start text="<span class='w_regex_need'>*</span>"+e_field.getFieldNote() for=e_field.getPageFieldName() />
				<textarea id="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
			<@m_zapmacro_common_field_end />
		<#elseif e_field.getPageFieldName() =="zw_f_share_img">
		
			<@m_zapmacro_common_field_start text="<span class='w_regex_need'>*</span>"+e_field.getFieldNote() for=e_field.getPageFieldName() />
				<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
				<span class="control-upload_iframe"></span>
				<span class="control-upload_process"></span>
				<span class="control-upload"></span>
			<@m_zapmacro_common_field_end />
			
			<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
		 
		<#elseif e_field.getPageFieldName() =="zw_f_share_link">
			<#--<@m_zapmacro_common_field_start text="<span class='w_regex_need'>*</span>"+e_field.getFieldNote() for=e_field.getPageFieldName() />-->
			<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
				<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
				<span style="line-height: 30px;color: #BFBCBC;margin-left:30px;">(分享链接默认为当前专题页链接)</span>
			<@m_zapmacro_common_field_end />
		
		<#elseif e_field.getFieldTypeAid()=="104005008">
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




<script type="text/javascript">

	zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',beforeSubmit);


	var tempZw_f_share_imgContrl = $("#zw_f_share_img").parent().find(".control-upload_iframe").html();
	$("#zw_f_share_title").parent().parent().hide();
	$("#zw_f_share_content").parent().parent().hide();
	$("#zw_f_share_img").parent().parent().hide();
	$("#zw_f_share_link").parent().parent().hide();
	
	
	
   <#-- 控制是否可分享按钮 -->
   $("#zw_f_app_is_share").change(function(){
   		var shareKV = $(this).val();
   		if(shareKV == '449747860002') {
   		
   			$("#zw_f_share_title").parent().parent().show();
   			
   			$("#zw_f_share_content").parent().parent().show();
   			
   			$("#zw_f_share_img").parent().parent().show();
   			zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('zw_f_share_img','');}); });
   			
   			$("#zw_f_share_link").parent().parent().show();
   			
   		} else if(shareKV == '449747860001') {
   			//清空分享信息
   			$("#zw_f_share_title").val("");
   			$("#zw_f_share_title").parent().parent().hide();
   			
   			$("#zw_f_share_content").val("");
   			$("#zw_f_share_content").parent().parent().hide();
   			
   			$("#zw_f_share_img").val("");
   			$("#zw_f_share_img").parent().find(".control-upload").html("");
   			$("#zw_f_share_img").parent().parent().hide();
   			
   			$("#zw_f_share_link").val("");
   			$("#zw_f_share_link").parent().parent().hide();
   		}
   }) 
   
   function beforeSubmit() {
   		var isShareVal = $("#zw_f_app_is_share").val();
   		if(isShareVal == '449747860002') {
   			var zw_f_share_title = $("#zw_f_share_title").val();
   			var zw_f_share_content = $("#zw_f_share_content").val();
   			var zw_f_share_img = $("#zw_f_share_img").val();
   			var zw_f_share_link = $("#zw_f_share_link").val();
   			
   			if(zw_f_share_title.length <= 0) {
   				zapjs.f.message('请填写分享标题');
   				return false;
   			}
   			if(zw_f_share_content.length <= 0) {
   				zapjs.f.message('请填写分享内容');
   				return false;
   			}
   			if(zw_f_share_img.length <= 0) {
   				zapjs.f.message('请上传分享图片');
   				return false;
   			}
   			<#--
   			if(zw_f_share_link.length <= 0) {
   				zapjs.f.message('请填写分享连接');
   				return false;
   			}
   			
   			if(!CheckUrl(zw_f_share_link)) {
   				zapjs.f.message('请填写正确的分享连接');
   				return false;
   			}
   			-->
   		}
   		return true;
   }
   function CheckUrl(str) { 
		var RegUrl = new RegExp(); 
		RegUrl.compile("^[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=]+$");//jihua.cnblogs.com 
		if (!RegUrl.test(str)) { 
		return false; 
		} 
		return true; 
	} 
</script>
