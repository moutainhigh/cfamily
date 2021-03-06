var addColumnPc = {
	init : function() {
		$("#zw_f_view_type").val("4497471600100003");
		$("#view_type").val($("#zw_f_view_type").val()); // 在页面设置一个隐藏域，name为zw_f_view_type
		$("#zw_f_view_type").attr("disabled", true);// 把展示类型设置为pc页

		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', addColumnPc.beforeSubmit);
		addColumnPc.showMoreInit();
		addColumnPc.noticeInit();
		
	},
	showMoreInit : function() {
		var columnType = $("#zw_f_column_type").val();
		if (columnType == "4497471600010001"
				|| columnType == "4497471600010002"
				|| columnType == "4497471600010003"
				|| columnType == "4497471600010004"
				|| columnType == "4497471600010013"
				|| columnType == "4497471600010014"
				|| columnType == "4497471600010021"
				|| columnType == "4497471600010012") {
			$("#zw_f_is_showmore").val("449746250002");
			$("#zw_f_is_showmore").attr("disabled", true);// 把是否显示更多设置为不可用
			$("#showmore_type").attr("disabled", false);// 把隐藏域设置为可用
			$("#showmore_type").val($("#zw_f_is_showmore").val());// 把是否显示更多的值赋给隐藏域
		} else {
			$("#zw_f_is_showmore").attr("disabled", false);// 把是否显示更多设置为可用
			$("#showmore_type").attr("disabled", true);// 把隐藏域设置为不可用
		}
		addColumnPc.showMoreSelect();
	},
	showMoreSelect : function() {
		var columnType = $("#zw_f_column_type").val();
		var showmore = $("#zw_f_is_showmore").val();
		if (showmore == 449746250001) {// 是
			if (columnType == "4497471600010010") {// 如果栏目类型为TV直播
				$("#zw_f_showmore_title").attr("zapweb_attr_regex_id",
						"469923180002");// 标题设为必填
				$("#zw_f_showmore_linkvalue")
						.removeAttr("zapweb_attr_regex_id");// 设为非必填
				$("#zw_f_showmore_linktype").attr("disabled", true);// 链接类型设置为不可用
				$("#zw_f_showmore_linkvalue").attr("disabled", true);// 链接值设置为不可用
				$("#zw_f_showmore_title").parent().parent().show();
				$("#zw_f_showmore_linktype").parent().parent().hide();
				$("#linkvalueDiv").hide();
			} else {
				$("#zw_f_showmore_title").attr("zapweb_attr_regex_id",
						"469923180002");// 标题设为必填
				$("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id",
						"469923180002");// 链接值设为必填
				$("#zw_f_showmore_linktype").attr("disabled", false);// 链接类型设置为可用
				$("#zw_f_showmore_linkvalue").attr("disabled", false);// 链接值设置为可用
				$("#zw_f_showmore_title").parent().parent().show();
				$("#zw_f_showmore_linktype").parent().parent().show();
				$("#linkvalueDiv").show();
			}
		} else if (showmore == 449746250002) {// 否
			$("#zw_f_showmore_title").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_showmore_title").parent().parent().hide();
			$("#zw_f_showmore_linktype").parent().parent().hide();
			$("#linkvalueDiv").hide();
		}
	},
	noticeInit : function() {
		var zw_f_interval_second = $("#zw_f_interval_second").val();
		if (null == zw_f_interval_second || "" == zw_f_interval_second) {
			$("#zw_f_interval_second").val(1);
		}
		if ($("#zw_f_column_type").val() == "4497471600010012") {
//			$("#zw_f_notice_type").parent().parent().show();
			$("#zw_f_interval_second").parent().parent().show();
		} else {
//			$("#zw_f_notice_type").parent().parent().hide();
			$("#zw_f_interval_second").parent().parent().hide();
		}
		if ($("#zw_f_column_type").val() == "4497471600010021") {
			//多栏广告栏数
			$("#zw_f_num_languanggao").parent().parent().show();
		} else {
			$("#zw_f_num_languanggao").parent().parent().hide();
		}
	},
	beforeSubmit : function() {
		if ($("#zw_f_showmore_linktype").val() == "4497471600020001") {// URL
			var lv = $("#zw_f_showmore_linkvalue").val();
			if (lv != ""
					&& $("#zw_f_is_showmore").val() == 449746250001
					&& $("#zw_f_showmore_linkvalue").attr("disabled") != "disabled"
					&& lv.toLowerCase().indexOf("http://share") != -1) {
				zapjs.f.message('URL中不能包含"http://share"');
				return false;
			}
			$("#zw_f_showmore_linkvalue").val(
					$("#zw_f_showmore_linkvalue").val().trim());
		}
//		if ($("#zw_f_notice_type").val() == "4497471600170001") {
//			$("#zw_f_interval_second").val(1);
//		}

		return true;
	}

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addColumnPc", function() {
		return addColumnPc;
	});
}
// 切换是否显示更多时使用
$(document)
		.ready(
				function() {
					$("#zw_f_is_showmore").change(function() {
						addColumnPc.showMoreSelect();
					});
					// 切换分类时调用
					$("#zw_f_showmore_linktype")
							.change(
									function() {
										var linktype = $(
												"#zw_f_showmore_linktype")
												.val();// 所选试用类型的值
										if (linktype == "4497471600020001") {// URL
											$("#linkvalueDiv").find("label")
													.text("URL：");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											$("#slId")
													.append(
															'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="">')
													.append(
															'<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
										} else if (linktype == "4497471600020002") {// 关键词类型
											$("#linkvalueDiv").find("label")
													.text("关键词：");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											$("#slId")
													.append(
															'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="">');
										} else if (linktype == "4497471600020003") {// 分类搜索
											$("#linkvalueDiv").find("label")
													.text("分类：");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											$("#slId")
													.append(
															'<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
											$("#slId")
													.append(
															'<span id="zw_f_showmore_linkvalue_text"></span>');
											$("#slId")
													.append(
															'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden">');
										} else if (linktype == "4497471600020004") {// 商品详情
											$("#linkvalueDiv").find("label")
													.text("商品：");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											$("#slId")
													.append(
															'<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
											$("#slId")
													.append(
															'<span id="zw_f_showmore_linkvalue_text"></span>');
											$("#slId")
													.append(
															'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden">');
										}
									});
					// 切换栏目时调用
					$("#zw_f_column_type").change(function() {
						addColumnPc.showMoreInit();
						addColumnPc.noticeInit();
					});
					// 切换通知栏目的通知类型时调用
//					$("#zw_f_notice_type").change(function() {
//						addColumnWap.showMoreInit();
//						addColumnWap.noticeInit();
//					});
				});