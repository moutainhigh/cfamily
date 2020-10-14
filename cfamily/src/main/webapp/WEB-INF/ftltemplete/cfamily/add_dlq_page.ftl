
<#assign page_type = b_page.getReqMap()["zw_f_page_type"] />
<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] />
<@m_zapmacro_common_page_add b_page page_type tv_number/>
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page page_type tv_number>
<form class="form-horizontal" method="POST" >
	
	<input name="zw_f_page_type" id="zw_f_page_type" type="hidden" value="${page_type }" />
	<input name="zw_f_tv_number" id="zw_f_tv_number" type="hidden" value="${tv_number }" />
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<script type="text/javascript">
	var add_dlq_page = {
		init:function() {
			$("#zw_f_is_share").val("449747800002");
			$("#zw_f_share_img").parent().parent().hide();
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',add_dlq_page.beforeSubmit);
			
			$("#zw_f_is_share").change(function(){
				var obj_val = $(this).val();
				
				if (obj_val == "449747800001") {// 是
					$("#zw_f_share_img").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享图片:');
					$("#zw_f_share_img").attr("zapweb_attr_regex_id", "469923180002");
					$("#zw_f_share_title").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享标题:');
					$("#zw_f_share_title").attr("zapweb_attr_regex_id", "469923180002");
					$("#zw_f_share_content").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享内容:');
					$("#zw_f_share_content").attr("zapweb_attr_regex_id",
							"469923180002");
					$("#zw_f_share_img").parent().parent().show();
					$("#zw_f_share_title").parent().parent().show();
					$("#zw_f_share_content").parent().parent().show();
				} else {
					$("#zw_f_share_img").parent().parent().find("label").html('分享图片:');
					$("#zw_f_share_img").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_title").parent().parent().find("label")
							.html('分享标题:');
					$("#zw_f_share_title").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_content").parent().parent().find("label").html(
							'分享内容:');
					$("#zw_f_share_content").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_img").parent().parent().hide();
					$("#zw_f_share_title").parent().parent().hide();
					$("#zw_f_share_content").parent().parent().hide();
				}
			})
	
	
		},
		beforeSubmit : function () {
			var obj_val = $("#zw_f_is_share").val();
			if (obj_val == "449747800002") {// 是
				$("#zw_f_share_img").val("");
				$("#zw_f_share_title").val("");
				$("#zw_f_share_content").val("");
			}
			return true;
		}
	}
	
	add_dlq_page.init();
</script>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	<#assign page_type = b_page.getReqMap()["zw_f_page_type"] />
	<#if e_field.getPageFieldName() == "zw_f_url">
		<div>
			<@m_zapmacro_common_field_text  e_field />
			<span style="position: absolute;left: 460px;line-height:30px;color:#BFBCBC;top:275px;">若填写的视频连接是ccvid,请以ccvid开头,例如：ccvid:3F394F2ACC69682D9C33DC5901307465</span>
		</div>
	<#elseif page_type == "1001" && e_field.getPageFieldName() == "zw_f_cuisine_name">
	<#else>
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
	</#if>
</#macro>



