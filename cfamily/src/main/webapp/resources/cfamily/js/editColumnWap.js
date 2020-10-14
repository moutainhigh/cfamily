var editColumnWap = {
	init : function() {
		$("#zw_f_view_type").val("4497471600100002");
		$("#view_type").val($("#zw_f_view_type").val()); // 在页面设置一个隐藏域，name为zw_f_view_type
		$("#zw_f_view_type").attr("disabled", true);// 把展示类型设置为APP端
		$("#zw_f_column_type").attr("disabled", true);// 栏目类型不允许修改
//		$("#zw_f_notice_type").attr("disabled", true);// 通知类型不允许修改

		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editColumnWap.beforeSubmit);
		editColumnWap.showMoreInit();
		editColumnWap.noticeInit();
		//链接类型字段把显示浮层类型去掉
		$("#zw_f_showmore_linktype option[value='4497471600020005']").remove();
		
	},
	showMoreInit : function() {
		var columnType = $("#zw_f_column_type").val();
		if (columnType == "4497471600010001" // 轮播广告
				|| columnType == "4497471600010002" // 一栏广告
				|| columnType == "4497471600010003" // 二栏广告
				|| columnType == "4497471600010004"
				|| columnType == "4497471600010013"
				|| columnType == "4497471600010014") {// 导航栏（是否显示更多为否）
			$("#zw_f_is_showmore").val("449746250002");
			$("#zw_f_is_showmore").attr("disabled", true);// 把是否显示更多设置为不可用
			$("#showmore_type").attr("disabled", false);// 把隐藏域设置为可用
			$("#showmore_type").val($("#zw_f_is_showmore").val());// 把是否显示更多的值赋给隐藏域
		} else {
			$("#zw_f_is_showmore").attr("disabled", false);// 把是否显示更多设置为可用
			$("#showmore_type").attr("disabled", true);// 把隐藏域设置为不可用
		}
		editColumnWap.showMoreSelect();
		if ($("#zw_f_showmore_linktype").val() == "4497471600020004") {// 类型为商品
			var opt = {};
			opt.productStrs = $("#zw_f_showmore_linkvalue").val();
			zapjs.zw.api_call(
					'com_cmall_productcenter_service_api_ApiGetProductName',
					opt, function(result) {
						$("#zw_f_showmore_linkvalue_text").html(
								result.productName);// 显示商品名称
					});

		}
		if ($("#zw_f_showmore_linktype").val() == "4497471600020003") {// 类型为分类
			$("#zw_f_showmore_linkvalue_text").html($("#categroyName").val());
		}
		
		//导航栏时显示模版背景图片，否则隐藏
		if (columnType == "4497471600010004"){ 			// 导航栏
			$("#zw_f_column_bgpic").parent().parent().show();
		}else{
			$("#zw_f_column_bgpic").parent().parent().hide();
		}
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
				$("#zw_f_showmore_linkvalue").val("");
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
				editColumnWap.linkTypeSelect();
			}
		} else if (showmore == 449746250002) {// 否
			$("#zw_f_showmore_title").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_showmore_title").parent().parent().hide();
			$("#zw_f_showmore_linktype").parent().parent().hide();
			$("#linkvalueDiv").hide();
		}
	},
	linkTypeSelect : function() {
		var linktype = $("#zw_f_showmore_linktype").val();// 所选试用类型的值
		var linkValue = $("#zw_f_showmore_linkvalue").val();
		if (linktype == "4497471600020001") {// URL
			$("#linkvalueDiv").find("label").text("URL：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="'
									+ linkValue + '">')
					.append(
							'<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
			;
		} else if (linktype == "4497471600020002") {// 关键词类型
			$("#linkvalueDiv").find("label").text("关键词：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="'
									+ linkValue + '">');
		} else if (linktype == "4497471600020003") {// 分类搜索
			$("#linkvalueDiv").find("label").text("分类：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
			$("#linkvalueDiv").find("div").append(
					'<span id="zw_f_showmore_linkvalue_text"></span>');
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden" value="'
									+ linkValue + '">');
		} else if (linktype == "4497471600020004") {// 商品详情
			$("#linkvalueDiv").find("label").text("商品：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
			$("#linkvalueDiv").find("div").append(
					'<span id="zw_f_showmore_linkvalue_text"></span>');
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden" value="'
									+ linkValue + '">');
		}
	},
	noticeInit : function() {
		var zw_f_interval_second = $("#zw_f_interval_second").val();
		if (null == zw_f_interval_second || "" == zw_f_interval_second) {
			$("#zw_f_interval_second").val(1);
		}
		if ($("#zw_f_column_type").val() != "4497471600010012") {
//			$("#zw_f_notice_type").parent().parent().hide();
			$("#zw_f_interval_second").parent().parent().hide();
		} else {
//			$("#zw_f_notice_type").parent().parent().show();
//			if ($("#zw_f_notice_type").val() == "4497471600170001") {
//				$("#zw_f_interval_second").parent().parent().hide();
//			}else{
				$("#zw_f_interval_second").parent().parent().show();
//			}
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
	define("cfamily/js/editColumnWap", function() {
		return editColumnWap;
	});
}
// 切换是否显示更多时使用
$(document).ready(function() {
	$("#zw_f_is_showmore").change(function() {
		editColumnWap.showMoreSelect();
	});
	// 切换分类时调用
	$("#zw_f_showmore_linktype").change(function() {
		editColumnWap.linkTypeSelect();
	});
	// 切换栏目时调用
	$("#zw_f_column_type").change(function() {
		editColumnWap.showMoreInit();
		editColumnWap.noticeInit();
	});
	// 切换通知栏目的通知类型时调用
//	$("#zw_f_notice_type").change(function() {
//		editColumnWap.noticeInit();
//	});
});