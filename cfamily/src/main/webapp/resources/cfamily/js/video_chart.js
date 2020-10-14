video_chart = {
	changeVideoStatus : function(videoCode, toStatus) {

		var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批备注:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
		showHtml += "<div class='control-group'><div class='controls'><div class='btn-toolbar'>";
		showHtml += "<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'></div></div></div>";
		showHtml += "</form>";
		var modalOption = "";
		modalOption = {
			content : showHtml,
			title : "请审核",
			oktext : "关闭",
			height : 200
		};
		zapjs.f.window_box(modalOption);
		$("#okId")
				.bind(
						"click",
						function() {
							var rk = $("#zw_f_remark").val();
							var obj = {};
							var obj = {};
							obj.videoCodes = videoCode;
							obj.nextStatus = toStatus;
							obj.remark = rk;
							zapjs.zw
									.api_call(
											'com_cmall_familyhas_api_ApiForBatchChangeVideoStatus',
											obj,
											function(result) {
												if (result.resultCode == 1) {
													$(".panel").hide();
													$("#zw_f_remark").val("");
													video_chart.refreshSelf();
												} else {
													zapadmin
															.model_message('审批异常');
												}
											});
						})

	},
	editVideo : function(videoCode,titleContent) {

		var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><div class='control-group'><label class='control-label' for='zw_f_source_code'>视频标题:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark' >"+titleContent+"</textarea></div></div>";
		showHtml += "<div class='control-group'><div class='controls'><div class='btn-toolbar'>";
		showHtml += "<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'></div></div></div>";
		showHtml += "</form>";
		var modalOption = "";
		modalOption = {
			content : showHtml,
			title : "标题",
			oktext : "关闭",
			height : 200
		};
		zapjs.f.window_box(modalOption);
		$("#okId")
				.bind(
						"click",
						function() {
							var rk = $("#zw_f_remark").val();
							var obj = {};
							var obj = {};
							obj.videoCodes = videoCode;
							obj.nextStatus = "01";
							obj.remark = rk;
							zapjs.zw
									.api_call(
											'com_cmall_familyhas_api_ApiForBatchChangeVideoStatus',
											obj,
											function(result) {
												if (result.resultCode == 1) {
													$(".panel").hide();
													$("#zw_f_remark").val("");
													video_chart.refreshSelf();
												} else {
													zapadmin
															.model_message('编辑异常');
												}
											});
						})

	},

	delVideo : function(videoCode) {
		video_chart.func_tip(videoCode, "", "删除");
	},

	batchDelVideo : function() {
		var videoCodes = [];
		$(".video_class").each(function() {
			if ($(this).is(':checked')) {
				videoCodes.push($(this).val());
			}
		});
		if (videoCodes.length == 0) {
			zapadmin.model_message('请选择视频！');
			return;
		}
		var pIds = videoCodes.join(',');
		video_chart.func_tip(pIds, "", "删除");

	},

	publishOrNot : function(videoCode, toStatus) {
		if ("4497471600600003" == toStatus) {
			video_chart.func_tip(videoCode, toStatus, "发布");
		} else {
			video_chart.func_tip(videoCode, toStatus, "暂停发布");
		}

	},

	batchChangeVideoStatus : function(toStatus) {
		var videoCodes = [];
		$(".video_class").each(function() {
			if ($(this).is(':checked')) {
				videoCodes.push($(this).val());
			}
		});
		var pIds = videoCodes.join(',');
		var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批备注:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
		showHtml += "<div class='control-group'><div class='controls'><div class='btn-toolbar'>";

		showHtml += "<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'>&nbsp;&nbsp;&nbsp;&nbsp;</div></div></div>";
		showHtml += "</form>";
		var modalOption = "";
		modalOption = {
			content : showHtml,
			title : "请审核",
			oktext : "关闭",
			height : 200
		};
		zapjs.f.window_box(modalOption);
		$("#okId")
				.bind(
						"click",
						function() {
							if (videoCodes.length == 0) {
								zapadmin.model_message('请选择视频！');
								return;
							}
							var rk = $("#zw_f_remark").val();
							var obj = {};
							obj.videoCodes = pIds;
							obj.nextStatus = toStatus;
							obj.remark = rk;
							zapjs.zw
									.api_call(
											'com_cmall_familyhas_api_ApiForBatchChangeVideoStatus',
											obj,
											function(result) {
												if (result.resultCode == 1) {
													$(".panel").hide();
													$("#zw_f_remark").val("");
													video_chart.refreshSelf();
												} else {
													zapadmin
															.model_message('审批异常');
												}
											});
						})

	},

	allSelect : function() {
		if ($("#item-all").is(':checked')) {
			$(".video_class").each(function() {
				$(this).prop("checked", true);
			});
		} else {
			$(".video_class").each(function() {
				$(this).prop("checked", false);
			});
		}
	},

	refreshSelf : function() {
		var url = window.location.href;
		window.location.href = "";
		window.location.href = url;
	},
	func_tip : function(pid, nextState, tips) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html(
				'<div class="w_p_20">您确认要' + tips + '么？</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
				$('#zapjs_f_id_modal_message').dialog('close');
				$('#zapjs_f_id_modal_message').remove();
				var obj = {};
				obj.videoCodes = pid;
				obj.nextStatus = nextState;
				obj.remark = "";
				zapjs.zw.api_call(
						'com_cmall_familyhas_api_ApiForBatchChangeVideoStatus',
						obj, function(result) {
							if (result.resultCode == 1) {
								video_chart.refreshSelf();
							} else {
								zapadmin.model_message('审批异常');
							}
						});

			}
		}, {
			text : '否',
			handler : function() {
				$('#zapjs_f_id_modal_message').dialog('close');
				$('#zapjs_f_id_modal_message').remove();
			}
		});

		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
	},
}