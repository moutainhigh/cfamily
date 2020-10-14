cdkeyExamination = {};
cdkeyExamination.et = {
		
		cdkeyApproval : function(activityCode,createTime){
			var showHtml = "<form class='form-horizontal' id='cdky_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_activityCode' name='zw_f_activityCode' value='"+activityCode+"'><input type='hidden' id='zw_f_createTime' name='zw_f_createTime' value='"+createTime+"'>";
			showHtml+="<div class='control-group'><label class='control-label' for='zw_f_source_code'>处理到:</label><div class='controls'><div class='btn-toolbar'><input class='btn btn-primary' type='button' value='审核通过' zap_tostatus_attr='0' zapweb_attr_operate_id='51f911336c2940f58bcb9d9f15a33525' style='margin-right:5px;'><input class='btn btn-primary' type='button' value='驳回' zap_tostatus_attr='4' zapweb_attr_operate_id='51f911336c2940f58bcb9d9f15a33525' style='margin-right:5px;'>";
			showHtml+="</div></div></div>";
			showHtml+="</form>";
			var modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
			zapjs.f.window_box(modalOption);
			$("#cdky_form_forsubmit").find("input[zap_tostatus_attr]").bind(
					"click",function(){
						cdkeyExamination.et.callSubmit(this,$(this).attr("zap_tostatus_attr"));
					}	
			);
			
		},
		callSubmit : function(obj,toStatus){
			$("#zw_f_to_status").val(toStatus);
			zapjs.zw.func_call(obj);
		},
		refreshSelf:function(){
			var url = window.location.href;
			window.location.href="";
			window.location.href = url;
		}
};

