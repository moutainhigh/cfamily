cmanage = {};
cmanage.flow = {
		refreshTimeoutForClear:"",
		callChangeFlow : function(flowCode,current_status){
			
			var url= "../func/115793e80b38485aaba8223e0ea101b8?flow_code="+flowCode+"&current_status="+current_status;
			
			$.getJSON(url, function(data){
				
				$("#flow_form_forsubmit").remove();
				
				
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_code' name='zw_f_flow_code' value='"+flowCode+"'><input type='hidden' id='zw_f_current_status' name='zw_f_current_status' value='"+current_status+"'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批意见:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
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
				
				zapjs.f.window_box(modalOption);
				
				$("#flow_form_forsubmit").find("input[zap_tostatus_attr]").bind(
						"click",function(){
							cmanage.flow.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							//alert(1);
						}	
				)
				
				
			});
		},
		//新增通用接口(过滤特殊（一般为三方商户）商户操作行为)
        callChangeFlowForSF : function(flowCode,current_status,merchant_code){
			
			var url= "../func/115793e80b38485aaba8223e0ea101b8?flow_code="+flowCode+"&current_status="+current_status;
			
			$.getJSON(url, function(data){
				
				$("#flow_form_forsubmit").remove();
				
				
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_code' name='zw_f_flow_code' value='"+flowCode+"'><input type='hidden' id='zw_f_current_status' name='zw_f_current_status' value='"+current_status+"'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批意见:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
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
				
				zapjs.f.window_box(modalOption);
				
				$("#flow_form_forsubmit").find("input[zap_tostatus_attr]").bind(
						"click",function(){
							cmanage.flow.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							//alert(1);
						}	
				)
				//当有多个特殊三方商户类似考拉的"SF03WYKLPT"时，可以采取bConfig配置文件的形式
				if("SF03WYKLPT"==merchant_code||"SF031JDSC"==merchant_code)    
				{
					$("input[value='驳回']").remove();
				}
				
			});
		},
		callSubmit : function(obj,toStatus){
			$("#zw_f_to_status").val(toStatus);
			zapjs.zw.func_call(obj);
			//var sAction = "../func/"+ $(obj).attr('zapweb_attr_operate_id')+"?zw_f_to_status=" + toStatus;
			
			/*var options = {
				url : sAction,
				type : "post",
				success : function(o) {
					
					zapjs.zw.modal_show({
						content : o.resultMessage,oktext:"关闭" //+"<br /> 本页面将在<span id='timeOutIdForReresh'>3</span>秒后自动刷新!"
					});
					
					//if(document.getElementById('main_iframe')  != null){
					//	cmanage.flow.refreshTimeoutForClear = setInterval("document.getElementById('main_iframe').contentWindow.cmanage.flow.refreshTimeout()",1000);
					//	setTimeout("document.getElementById('main_iframe').contentWindow.cmanage.flow.refreshSelf()",3000);
					//}else{
					//	cmanage.flow.refreshTimeoutForClear = setInterval("cmanage.flow.refreshTimeout()",1000);
					//	setTimeout("cmanage.flow.refreshSelf()",3000);
					//}
				},
				error : function(o) {
					alert('系统出现错误，请联系技术，谢谢！');
				}
			};
			
			$(obj).parents("form").ajaxSubmit(options);*/
			
		},
		refreshSelf:function(){
			var url = window.location.href;
			window.location.href="";
			window.location.href = url;
		},
		refreshTimeout:function(){
			if(window.parent!=null){
				var a =$(window.parent.document).find("#timeOutIdForReresh").html()-1;
				$(window.parent.document).find("#timeOutIdForReresh").html(a);
				
				if(a == 0){
					$('#zapjs_f_id_modal_box').dialog('close');
				}
				
			}else{
				var a = $("#timeOutIdForReresh").html()-1;
				$("#timeOutIdForReresh").html(a);
				
			}
		},
		//新增通用接口
		callChangeFlowCommon : function(flowCode,current_status, zw_define_parentId){
			
			var url= "../func/83ba8fb905b34f9abc5d9275d552b73a?flow_code="+flowCode+"&current_status="+current_status+"&zw_define_parentId="+zw_define_parentId;
			
			$.getJSON(url, function(data){
				
				$("#flow_form_forsubmit").remove();
				
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_code' name='zw_f_flow_code' value='"+flowCode+"'><input type='hidden' id='zw_f_current_status' name='zw_f_current_status' value='"+current_status+"'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批意见:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
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
				
				zapjs.f.window_box(modalOption);
				
				$("#flow_form_forsubmit").find("input[zap_tostatus_attr]").bind(
						"click",function(){
							cmanage.flow.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							//alert(1);
						}	
				)
				
			});
		}
};

