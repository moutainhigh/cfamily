var startAdd = {
	init : function(){
		$yndiv = '<div id="ljContent"></div>';
		$("#zw_f_yn_jump_button").parent().parent().after($yndiv);
		$("#ljContent").append($("#zw_f_showmore_linktype").parent().parent()).append(
				$("#zw_f_showmore_linkvalue").parent().parent()).append(
				$("#zw_f_button_type").parent().parent()).append(
				$("#zw_f_button_pic").parent().parent()).append(
				$("#zw_f_button_text").parent().parent()).append(
				$("#zw_f_button_color").parent().parent()).append(
				$("#zw_f_button_background").parent().parent());// 把要切换隐藏显示的元素放到div里
		startAdd.ynShowLinkContent();
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', startAdd.beforeSubmit);
	},
	ynShowLinkContent: function(){//链接的先关内容显示和隐藏
		if($("#zw_f_yn_jump_button").val() == "4497471600180001"){//是否跳转为否
			
			$("#zw_f_showmore_linktype").parent().parent().find("label").find('span').remove();
			$("#zw_f_showmore_linkvalue").parent().parent().find("label").find('span').remove();
			//$("#zw_f_button_type").parent().parent().find("label").find('span').remove();
			//$("#zw_f_button_pic").parent().parent().find("label").find('span').remove();
			//$("#zw_f_button_text").parent().parent().find("label").find('span').remove();
			//$("#zw_f_button_color").parent().parent().find("label").find('span').remove();
			
			$("#zw_f_showmore_linktype").removeAttr("zapweb_attr_regex_id");// 链接类型设为非必填
			$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 链接值设为非必填
			$("#zw_f_button_type").removeAttr("zapweb_attr_regex_id");// 链接值设为非必填
			$("#zw_f_button_pic").removeAttr("zapweb_attr_regex_id");// 图片设为非必填
			$("#zw_f_button_text").removeAttr("zapweb_attr_regex_id");// 按钮文本设为非必填
			$("#zw_f_button_color").removeAttr("zapweb_attr_regex_id");// 按钮颜色设为非必填
			$("#ljContent").hide();
		}else{
			$("#zw_f_showmore_linktype").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			$("#zw_f_showmore_linkvalue").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_type").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_pic").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_text").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_color").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			
			$("#zw_f_showmore_linktype").attr("zapweb_attr_regex_id","469923180002");// 链接类型设为必填
			if($("#zw_f_showmore_linktype").val() != "4497471600210005"){//不是跳转到首页
				$("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");// 链接值设为必填
			}
			$("#zw_f_button_type").attr("zapweb_attr_regex_id","469923180002");// 按钮类型设为必填
			startAdd.buttonTypeChange();
			$("#ljContent").show();
		}
	},
	buttonTypeChange: function(){//按钮类型切换显示和隐藏
		if($("#zw_f_button_type").val() == "4497471600190001"){//按钮类型为图片
			//$("#zw_f_button_pic").attr("zapweb_attr_regex_id","469923180002");// 图片设为必填
			$("#zw_f_button_text").removeAttr("zapweb_attr_regex_id");// 按钮文本设为非必填
			$("#zw_f_button_color").removeAttr("zapweb_attr_regex_id");// 按钮颜色设为非必填
			$("#zw_f_button_pic").parent().parent().show();
			$("#zw_f_button_text").parent().parent().hide();
			$("#zw_f_button_color").parent().parent().hide();
			$("#zw_f_button_background").parent().parent().hide();
		}else{//按钮类型为文本
			//$("#zw_f_button_text").attr("zapweb_attr_regex_id","469923180002");// 按钮文本设为必填
			//$("#zw_f_button_color").attr("zapweb_attr_regex_id","469923180002");// 按钮颜色设为必填
			$("#zw_f_button_pic").removeAttr("zapweb_attr_regex_id");// 图片设为非必填
			$("#zw_f_button_pic").parent().parent().hide();
			$("#zw_f_button_text").parent().parent().show();
			$("#zw_f_button_color").parent().parent().show();
			$("#zw_f_button_background").parent().parent().show();
		}
	},
	beforeSubmit : function() {
		if($("#zw_f_yn_jump_button").val() == "4497471600180002"){//是否跳转为是
			var lv = $("#zw_f_showmore_linkvalue").val();
			if ($("#zw_f_showmore_linktype").val() == "4497471600210001") {// URL
				if (lv != ""
					&& lv.toLowerCase().indexOf("http://share") != -1) {
					zapjs.f.message('URL中不能包含"http://share"');
					return false;
				}
				$("#zw_f_showmore_linkvalue").val(
						$("#zw_f_showmore_linkvalue").val().trim());
				
				if(lv == ""){
					zapjs.f.message('URL不能为空');
					return false;
				}
			}
			if($("#zw_f_button_type").val()=="4497471600190002"){//按钮类型为文本
				var buttonText = $("#zw_f_button_text").val();
				if(buttonText != "" && buttonText.length > 4){
					zapjs.f.message('按钮文本不能大于4个字符');
					return false;
				}
			}
		}
		if($("#zw_f_residence_time").val() !="" && parseInt($("#zw_f_residence_time").val()) > parseInt(8)){
			zapjs.f.message('停留时间不能超过8秒');
			return false;
		}
		return true;
	}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/startAdd", function() {
		return startAdd;
	});
}

$(document).ready(function(){
	$("#zw_f_yn_jump_button").change(function(){//是否显示跳转按钮
		startAdd.ynShowLinkContent();
	});
	
	$("#zw_f_button_type").change(function(){//按钮类型切换显示和隐藏
		startAdd.buttonTypeChange();
	});
	
	// 切换分类时调用
	$("#zw_f_showmore_linktype")
			.change(
					function() {
						var linktype = $(
								"#zw_f_showmore_linktype")
								.val();// 所选试用类型的值
						if (linktype == "4497471600210001") {// URL
							$("#linkvalueDiv").find("label").text("URL：");
							$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
							$("#linkvalueDiv").show();
							$("#slId").find("input").remove();
							$("#slId").find("span").remove();
							$("#slId")
									.append('<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="">')
									.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
						} else if (linktype == "4497471600210002") {// 关键词类型
							$("#linkvalueDiv").find("label").text("关键词：");
							$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
							$("#linkvalueDiv").show();
							$("#slId").find("input").remove();
							$("#slId").find("span").remove();
							$("#slId").append('<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="">');
						} else if (linktype == "4497471600210003") {// 分类搜索
							$("#linkvalueDiv").find("label").text("分类：");
							$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
							$("#linkvalueDiv").show();
							$("#slId").find("input").remove();
							$("#slId").find("span").remove();
							$("#slId").append('<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
							$("#slId").append('<span id="zw_f_showmore_linkvalue_text"></span>');
							$("#slId").append('<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden">');
						} else if (linktype == "4497471600210004") {// 商品详情
							$("#linkvalueDiv").find("label").text("商品：");
							$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
							$("#linkvalueDiv").show();
							$("#slId").find("input").remove();
							$("#slId").find("span").remove();
							$("#slId").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
							$("#slId").append('<span id="zw_f_showmore_linkvalue_text"></span>');
							$("#slId").append('<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" type="hidden">');
						}else if (linktype == "4497471600210005") {// 跳转到首页
							$("#linkvalueDiv").hide();
							$("#slId").find("input").remove();
							$("#slId").find("span").remove();
						}
					});
	
});

