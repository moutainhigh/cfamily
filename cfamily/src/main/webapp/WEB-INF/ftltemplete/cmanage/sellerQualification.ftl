<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_js ["cmanage/js/flow.js"]/>
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="zapweb/js/zapweb_upload" src="../resources/zapweb/js/zapweb_upload.js"></script>
<script>
	require(['cfamily/js/sellerQualification','cfamily/js/select2/select2'],
		function(p)
		{
			zapjs.f.ready(function()
			{
				p.init();
			});
		}
	);
	function callChangeFlowCommon(flowCode,current_status, zw_define_parentId){
			var url= "../func/83ba8fb905b34f9abc5d9275d552b73a?flow_code="+flowCode+"&current_status="+current_status+"&zw_define_parentId="+zw_define_parentId;
			$.getJSON(url, function(data){
				$("#flow_form_forsubmit").remove();
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_code' name='zw_f_flow_code' value='"+flowCode+"'><input type='hidden' id='zw_f_current_status' name='zw_f_current_status' value='"+current_status+"'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批意见:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
				showHtml +="<div class='control-group'>";
				showHtml +="<label class='control-label' for='zw_f_upload_show'>上传文件：</label>";
				showHtml +="<div class='controls'>";
				showHtml +="<input type='hidden' zapweb_attr_target_url='../upload/' zapweb_attr_set_params='' id='zw_f_upload_show' name='zw_f_upload_show' value=''>";
				showHtml +="<span class='control-upload_iframe'><iframe src='../upload/upload?zw_s_source=zw_f_upload_show&amp;zw_s_description=' class='zw_page_upload_iframe' frameborder='0'></iframe></span>";			
				showHtml +="<span class='control-upload'></span>";
				showHtml +="<span class='control-upload_process' style='color:red;'>温馨提示：仅支持Word文档、驳回请上传附件、通过无需上传</span>";
				showHtml +="</div>";
				showHtml +="</div>";
				showHtml+="<div class='control-group'><div class='controls'><div class='btn-toolbar'>";
				showHtml+=data.resultMessage;
				showHtml+="</div></div></div>";
				showHtml+="</form>";
				var modalOption="";
				if(data.resultMessage == ""){
					modalOption = {content:"您可能无此权限或此流程状态已经改变!",title:"请审批",oktext:"关闭",height:200};
				}else{
					modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
				}
				window_box(modalOption);
				$("#flow_form_forsubmit").find("input[zap_tostatus_attr]").bind(
						"click",function(){
							var upload = $("#zw_f_upload_show").val();
							if(upload){
								var array = upload.split(".");
								var type = array[array.length-1];
								if(type == 'doc' || type=='docx'){
									cmanage.flow.callSubmit(this,$(this).attr("zap_tostatus_attr"));
								}else{
									zapadmin.model_message("上传文件仅支持Word文档！请重新上传文件");
								}
							}else{
								cmanage.flow.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							}
						}
				)
			});
		}
		function window_box(options) {
				var defaults = {
					id : 'zapjs_f_id_window_box',
					content : '',
					width : 600,
					height : 700,
					type:'text',
					title : '&nbsp;',
					close : false
				};
				var s = $.extend({}, defaults, options || {});
				if (s.close) {
					$('#' + s.id).window('close');
					return;
				}
				if (!zapjs.f.exist(s.id)) {
					var sText = '<div id="' + s.id + '"  class="easyui-window" title="' + s.title + '"  data-options="iconCls:\'icon-save\',modal:true"></div>';
					$(document.body).append(sText);
				}
				if(s.url&&s.type=="iframe")
				{
					s.content='<iframe src="'+s.url+'" frameborder="0" style="width:100%;height:98%;"></iframe>';
				}
				$('#' + s.id).html(s.content);
				$('#' + s.id).window({
					width : s.width,
					height : 300,
					modal : true,
					closed : false,
					onClose:function()
					{
						// 修正 关闭时强制删除所有元素
						var oParent=$('#'+s.id).parent('.panel');
						oParent.next('.window-shadow').remove();
						oParent.next('.window-mask').remove();
						oParent.remove();
					}
				});
				if (s.url&&s.type!='iframe') {
					$('#' + s.id).window('refresh', s.url);
				}
		}
</script>
<@m_zapmacro_common_page_chart b_page />
<#-- 列表页  -->
<#macro m_zapmacro_common_page_chart e_page >
	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	<#local e_pagedata=e_page.upChartData()>  
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	</div>
</#macro>

<#-- 查询区域 -->
<#macro m_flow_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_flow_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>

<#--查询的自动输出判断 -->
<#macro m_flow_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->
	  	<#elseif  (e.getFieldTypeAid()=="104005019" && e.getPageFieldName() = "zw_f_current_status")>
	  		<@m_flow_common_field_select  e  e_page "请选择"/>
	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  	</#if>
	</#list>
</#macro>
