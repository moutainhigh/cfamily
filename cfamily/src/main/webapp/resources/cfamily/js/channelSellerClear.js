
cfamily_channelSellerClear = {
		
		channelSellerClear : function(channelSellerCode,channelSellerName){
			$("#invest_form_forsubmit").remove();
			
			var showHtml = "<form class='form-horizontal' id='invest_form_forsubmit' method='POST' style='margin-top:20px;'>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='zw_f_channel_seller_name'>渠道商名称：</label>" +
									"<label class='control-label' for='zw_f_channel_seller_name' style='text-align: left;' >" +channelSellerName+ "</label>" +
									"<input type='hidden' id='channelSellerCode' name='channelSellerCode' value='" +channelSellerCode+"'></input>" +
								"</div>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='zw_f_remark'><span class='w_regex_need'>*</span>清算说明：</label>" +
									"<textarea id='zw_f_remark' name='zw_f_remark'></textarea>" +
								"</div>";
			showHtml += "<div class='control-group' style='text-align:center; vertical-align:middel;'>" +
							"<input type='button' style=' width:100px;' id='submitForm' class='btn  btn-primary' zapweb_attr_operate_id='aabee970f3fb4b6d9f3f5850db498f58' value='确认清算'>" +
					  "</div>";
			showHtml += "</form>";
			
			var modalOption="";
			modalOption = {id:"invest_form", content:showHtml, title:"渠道商清算", oktext:"关闭", width:500, height:290};
			
			zapjs.f.window_box(modalOption);
			
			$(".panel-tool-max").hide();
			
			$("#cancleForm").bind("click",function(){
				zapjs.f.window_close("invest_form");
			});
			
			$("#submitForm").bind("click",function(){
				var channel_seller_code = $("#channelSellerCode").val();
				var remark = $("#zw_f_remark").val();
				if(remark == null || remark == ""){
					zapjs.f.message("请填写清算说明!");
					return false;
				}
				if(channel_seller_code == null || channel_seller_code == ""){
					zapjs.f.message("清算失败,请刷新页面重试!");
					return false;
				}
				
				var investForm = $("#submitForm").parents("form");
				zapjs.f.ajaxsubmit(
					investForm,
					'../func/'+$("#submitForm").attr('zapweb_attr_operate_id'),
					function(data){
						var o = data;
						switch (o.resultType) {
							case "116018010":
								eval(o.resultObject);
								break;
							default:
								if (o.resultCode == "1") {
									if (o.resultMessage == "") {
										o.resultMessage = "充值成功";
									}
									zapjs.zw.modal_show({
										content : o.resultMessage,
										okfunc : 'zapjs.f.tourl("page_chart_v_uc_channel_seller_advance");'
									});
								} else {
									zapjs.zw.modal_show({
										content : o.resultMessage
									});
								}
								break;
						}
					}, function(data){
						alert('系统出现错误，请联系技术，谢谢！');
					}
				);
			});
		}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/channelSellerClear", [], function() {
		return cfamily_channelSellerClear;
	});
}

