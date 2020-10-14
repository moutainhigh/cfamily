<#assign qualificationUid = b_page.getReqMap()["zw_f_uid"]>
<#assign flow_code = "">
<#assign current_status = ""> 
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="zapweb/js/zapweb_upload" src="../resources/zapweb/js/zapweb_upload.js"></script>
<script type="text/javascript"></script>
<script>
	require(['cfamily/js/bookSellerQualification','cmanage/js/flow'],
		function(p)
		{
		<#assign brand_support=b_method.upClass("com.cmall.productcenter.service.ProductBrandService")>
		<#assign sc_define_support=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>
		var brand=${brand_support.upSellerQualificationDraftMap(qualificationUid)["qualification_json"]};
		var brand_name = "${brand_support.getBranName(brand_support.upSellerQualificationDraftMap(qualificationUid)["brand_code"])}";
		var category_name = "${sc_define_support.getDefineNameByCode(brand_support.upSellerQualificationDraftMap(qualificationUid)["category_code"])}";
			zapjs.f.ready(function()
			{
				p.init(brand,brand_name,category_name);
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
				showHtml +="<p style='color:red;'>温馨提示：仅支持Word文档、驳回请上传附件、通过无需上传</p>";
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

<style>
.table ul li{height:auto;}
.zw_page_upload_iframe{height:50px;min-width:300px;}
</style>

<@m_zapmacro_common_page_book b_page />
<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<#if e_field.getPageFieldName()=="zw_f_flow_code">
		<#assign flow_code = e_field.getPageFieldValue()>
	<#elseif e_field.getPageFieldName()=="zw_f_current_status">
		<#assign current_status = e_field.getPageFieldValue()>
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
			      		<#if e_field.getPageFieldName()=="zw_f_brand_code">
							<span id="brand_name_span"></span>
						<#elseif e_field.getPageFieldName()=="zw_f_category_code">
							<span id="category_name_span"></span>
						<#else>
				      		<#if e_field.getFieldTypeAid()=="104005019">
				      			<#list e_page.upDataSource(e_field) as e_key>
									<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
								</#list>
				      		<#else>
				      			${e_field.getPageFieldValue()?default("")}
				      		</#if>
			      		</#if>
	      		</div>
		<@m_zapmacro_common_field_end />
	</#if>
</#macro>

<div id="category_tip"></div>
<div id="cshop_addproduct_custom" class="csb_cshop_addproduct_pextend w_clear">
	<div class="csb_cshop_addproduct_title"></div>
		<div class="csb_cshop_addproduct_item">
	 		<table  class="table">
	   		 	<thead>
	   		 		<tr>
	   		 			<th>资质名称</th>
	   		 			<th>资质证件</th>
	   		 			<th>有效期到期时间</th>
	   		 		</tr>
	   		 	</thead>
	   		 	<tbody>
	   		 	</tbody>
	   		 	<tfoot>
   		 	</tfoot>
	 	</table>
	</div>
</div>
<form class="form-horizontal" method="POST">
	<div class="control-group">
		<div class="controls">
      		<div class="control_book">
				<input zapweb_attr_operate_id="80c57133ba69414193225033cca0ed19" class="btn btn-primary" onclick="callChangeFlowCommon('${flow_code}','${current_status}', '46992328')" type="button" value="审核">
      		</div>
		</div>
	</div>
</form>
