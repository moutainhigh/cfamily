var startEdit = {
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
		startEdit.linkTypeSelect();
		startEdit.ynShowLinkContent();
		if ($("#zw_f_showmore_linktype").val() == "4497471600210004") {// 类型为商品
			var opt = {};
			opt.productStrs = $("#zw_f_showmore_linkvalue").val();
			zapjs.zw.api_call(
					'com_cmall_productcenter_service_api_ApiGetProductName',
					opt, function(result) {
						$("#zw_f_showmore_linkvalue_text").html(
								result.productName);// 显示商品名称
					});

		}
		if ($("#zw_f_showmore_linktype").val() == "4497471600210003") {// 类型为分类
			$("#zw_f_showmore_linkvalue_text").html($("#categroyName").val());
		}
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', startEdit.beforeSubmit);
	},
	ynShowLinkContent: function(){//链接的先关内容显示和隐藏
		if($("#zw_f_yn_jump_button").val() == "4497471600180001"){
			$("#zw_f_showmore_linktype").parent().parent().find("label").find('span').remove();
			$("#zw_f_showmore_linkvalue").parent().parent().find("label").find('span').remove();
			$("#zw_f_button_type").parent().parent().find("label").find('span').remove();
			$("#zw_f_button_pic").parent().parent().find("label").find('span').remove();
			$("#zw_f_button_text").parent().parent().find("label").find('span').remove();
			$("#zw_f_button_color").parent().parent().find("label").find('span').remove();
			
			$("#zw_f_showmore_linktype").removeAttr("zapweb_attr_regex_id");// 链接类型设为非必填
			$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 链接值设为非必填
			$("#zw_f_button_type").removeAttr("zapweb_attr_regex_id");// 链接值设为非必填
			$("#zw_f_button_pic").removeAttr("zapweb_attr_regex_id");// 图片设为非必填
			$("#zw_f_button_text").removeAttr("zapweb_attr_regex_id");// 按钮文本设为非必填
			$("#zw_f_button_color").removeAttr("zapweb_attr_regex_id");// 按钮颜色设为非必填
			$("#ljContent").hide();
		}else{
			$("#zw_f_showmore_linktype").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			$("#zw_f_showmore_linkvalue").parent().parent().find("label").find('span').remove();//为了去除初始化的时候重复添加
			$("#zw_f_showmore_linkvalue").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_type").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_pic").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_text").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			//$("#zw_f_button_color").parent().parent().find("label").prepend('<span class="w_regex_need">*</span>');
			
			$("#zw_f_showmore_linktype").attr("zapweb_attr_regex_id","469923180002");// 链接类型设为必填
			if($("#zw_f_showmore_linktype").val() != "4497471600210005"){//不是跳转到首页
				$("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");// 链接值设为必填
			}else if($("#zw_f_showmore_linktype").val() == "4497471600210005"){
				$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 链接值设为非必填
			}
			//$("#zw_f_button_type").attr("zapweb_attr_regex_id","469923180002");// 按钮类型设为必填
			startEdit.buttonTypeChange();
			$("#ljContent").show();
		}
	},
	linkTypeSelect : function() {//切换分类时调用
		var linktype = $("#zw_f_showmore_linktype").val();// 所选试用类型的值
		var linkValue = "";
		if($("#zw_f_showmore_linkvalue").val() != undefined){
			linkValue = $("#zw_f_showmore_linkvalue").val();
		}
		if (linktype == "4497471600210001") {// URL
			$("#linkvalueDiv").find("label").text("URL：");
			$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
			$("#linkvalueDiv").show();
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
		} else if (linktype == "4497471600210002") {// 关键词类型
			$("#linkvalueDiv").find("label").text("关键词：");
			$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
			$("#linkvalueDiv").show();
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv")
					.find("div")
					.append(
							'<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="'
									+ linkValue + '">');
		} else if (linktype == "4497471600210003") {// 分类搜索
			$("#linkvalueDiv").find("label").text("分类：");
			$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
			$("#linkvalueDiv").show();
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
		} else if (linktype == "4497471600210004") {// 商品详情
			$("#linkvalueDiv").find("label").text("商品：");
			$("#linkvalueDiv").find("label").prepend('<span class="w_regex_need">*</span>');
			$("#linkvalueDiv").show();
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
		}else if (linktype == "4497471600210005") {// 跳转到首页
			$("#linkvalueDiv").hide();
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
		}
	},
	buttonTypeChange: function(){//按钮类型切换显示和隐藏
		if($("#zw_f_button_type").val() == "4497471600190001"){
			//$("#zw_f_button_pic").attr("zapweb_attr_regex_id","469923180002");// 图片设为必填
			$("#zw_f_button_text").removeAttr("zapweb_attr_regex_id");// 按钮文本设为非必填
			$("#zw_f_button_color").removeAttr("zapweb_attr_regex_id");// 按钮颜色设为非必填
			$("#zw_f_button_pic").parent().parent().show();
			$("#zw_f_button_text").parent().parent().hide();
			$("#zw_f_button_color").parent().parent().hide();
			$("#zw_f_button_background").parent().parent().hide();
		}else{
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
		if($("#zw_f_yn_jump_button").val() == "4497471600210001"){//是否跳转为是
			var lv = $("#zw_f_showmore_linkvalue").val();
			if ($("#zw_f_showmore_linktype").val() == "4497471600020001") {// URL
				if (lv != ""
					&& lv.toLowerCase().indexOf("http://share") != -1) {
					zapjs.f.message('URL中不能包含"http://share"');
					return false;
				}
				$("#zw_f_showmore_linkvalue").val(
						$("#zw_f_showmore_linkvalue").val().trim());
			}
			if(lv !=undefined && lv !="" && lv.length>100){
				zapjs.f.message('URL或关键词不能超过100个字符');
				return false;
			}
			if($("#zw_f_button_type").val()=="4497471600190002"){//按钮类型为文本
				var buttonText = $("#zw_f_button_text").val();
				if(buttonText != "" && buttonText.length > 4){
					zapjs.f.message('按钮文本不能大于4个字符');
					return false;
				}
			}
		}
		if(parseInt($("#zw_f_residence_time").val()) > parseInt(8)){
			zapjs.f.message('停留时间不能超过8秒');
			return false;
		}
		return true;
	}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/startEdit", function() {
		return startEdit;
	});
}

$(document).ready(function(){
	$("#zw_f_yn_jump_button").change(function(){//是否显示跳转按钮
		startEdit.ynShowLinkContent();
	});
	
	$("#zw_f_button_type").change(function(){//按钮类型切换显示和隐藏
		startEdit.buttonTypeChange();
	});
	
	// 切换分类时调用
	$("#zw_f_showmore_linktype").change(function() {
		startEdit.linkTypeSelect();
	});
	
});

