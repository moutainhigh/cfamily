var brandPreferenceContentEdit= {
		init : function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',brandPreferenceContentEdit.beforeSubmit);
			brandPreferenceContentEdit.changeType();
			if($("#zw_f_link_type").val() == "4497471600020004"){//类型为商品
				var opt ={};
				opt.productStrs = $("#zw_f_link_value").val();
				zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductName',opt,function(result) {
					$("#zw_f_link_value_text").html(result.productName);//显示商品名称
				});
				
			}
			if($("#zw_f_link_type").val() == "4497471600020003"){//类型为分类
				$("#zw_f_showmore_linkvalue_text").html($("#categroyName").val());
			}
		},
		changeType : function(){//根据链接类型进行切换
			var linktype = $("#zw_f_link_type").val();//所选试用类型的值
			var linkValue = $("#zw_f_link_value").val();
			if(linktype == "4497471600020001"){//URL
				$("#linkvalueDiv").find("label").text("URL：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId")
						.append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_link_value" value="'+linkValue+'">');
			}else if(linktype == "4497471600020002"){//关键词类型
				$("#linkvalueDiv").find("label").text("关键词：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_link_value" value="'+linkValue+'">');
			}else if(linktype == "4497471600020003"){//分类搜索
				$("#linkvalueDiv").find("label").text("分类：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
				$("#slId").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#slId").append('<input id="zw_f_link_value" value="'+linkValue+'" zapweb_attr_regex_id="469923180002" name="zw_f_link_value" type="hidden">');
			}else if(linktype == "4497471600020004"){//商品详情
				$("#linkvalueDiv").find("label").text("商品：");
				$("#slId").find("input").remove();
				$("#slId").find("span").remove();
				$("#slId").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_link_value\')">');
				$("#slId").append('<span id="zw_f_link_value_text"></span>');
				$("#slId").append('<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" name="zw_f_link_value" type="hidden" value="'+linkValue+'">');
			}
		},
		beforeSubmit : function() {
			if($("#zw_f_link_type").val() == "4497471600020003"){//连接类型为分类
				if($("#zw_f_showmore_linkvalue").val() != ""){
					$("#zw_f_link_value").val($("#zw_f_showmore_linkvalue").val());
				}
			}
			return true;
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/brandPreferenceContentEdit", function() {
		return brandPreferenceContentEdit;
	});
}

$(document).ready(function(){
	//切换分类时调用
	$("#zw_f_link_type").change(function(){
		brandPreferenceContentEdit.changeType();
	});
});