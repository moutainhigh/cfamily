couponExamination = {};
couponExamination.et = {
		
		couponapprove : function(taskCode){
			var showHtml = "<form class='form-horizontal' id='coupon_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_taskCode' name='zw_f_taskCode' value='"+taskCode+"'>";
			showHtml+="<div class='control-group'><label class='control-label' for='zw_f_source_code'>处理到:</label><div class='controls'><div class='btn-toolbar'><input class='btn btn-primary' type='button' value='审核通过' zap_tostatus_attr='4497471600250002' zapweb_attr_operate_id='00ed4cb117dc4b54aa452ab72f81a35a' style='margin-right:5px;'><input class='btn btn-primary' type='button' value='驳回' zap_tostatus_attr='4497471600250004' zapweb_attr_operate_id='00ed4cb117dc4b54aa452ab72f81a35a' style='margin-right:5px;'>";
			showHtml+="</div></div></div>";
			showHtml+="</form>";
			var modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
			zapjs.f.window_box(modalOption);
			$("#coupon_form_forsubmit").find("input[zap_tostatus_attr]").bind(
					"click",function(){
						couponExamination.et.callSubmit(this,$(this).attr("zap_tostatus_attr"));
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

