var brandPreferenceContentAdd= {
		init : function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',brandPreferenceContentAdd.beforeSubmit);
			brandPreferenceContentAdd.changeType();
		},
		changeType : function(){//根据链接类型进行切换
			var linktype = $("#zw_f_link_type").val();//所选试用类型的值
			if(linktype == "4497471600020001"){//URL
				$("#linkvalueDiv").find("label").text("URL：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId")
						.append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_link_value" value=" ">');//初始化url添加空格，兼容配置广告位之后不需要添加跳转连接的需求。
			}else if(linktype == "4497471600020002"){//关键词类型
				$("#linkvalueDiv").find("label").text("关键词：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_link_value" value="">');
			}else if(linktype == "4497471600020003"){//分类搜索
				$("#linkvalueDiv").find("label").text("分类：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
				$("#slId").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#slId").append('<input id="zw_f_link_value" value="123" zapweb_attr_regex_id="469923180002" name="zw_f_link_value" type="hidden">');
			}else if(linktype == "4497471600020004"){//商品详情
				$("#linkvalueDiv").find("label").text("商品：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_link_value\')">');
				$("#slId").append('<span id="zw_f_link_value_text"></span>');
				$("#slId").append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" name="zw_f_link_value" type="hidden">');
			}
		},
		beforeSubmit : function() {
			if($("#zw_f_link_type").val() == "4497471600020003"){//连接类型为分类
				if($("#zw_f_showmore_linkvalue").val() == ""){
					zapjs.f.message('分类不能为空');
					return false;
				}
				$("#zw_f_link_value").val($("#zw_f_showmore_linkvalue").val());
			}
			return true;
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/brandPreferenceContentAdd", function() {
		return brandPreferenceContentAdd;
	});
}

$(document).ready(function(){
	//切换分类时调用
	$("#zw_f_link_type").change(function(){
		brandPreferenceContentAdd.changeType();
	});
});