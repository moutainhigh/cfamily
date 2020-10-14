var addNavigate = {
	selectvalue : "<option value=\"\">请选择</option><option value=\"4497471600020001\">URL</option><option value=\"4497471600020002\">关键词搜索</option><option value=\"4497471600020003\">分类搜索</option><option value=\"4497471600020004\">商品详情</option><option value=\"4497471600020006\">抽奖</option><option value=\"4497471600020008\">直播页面</option><option value=\"4497471600020013\">首页导航</option><option value=\"4497471600020016\">直播列表</option><option value=\"4497471600020017\">视频列表</option>",
	init : function() {
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
				addNavigate.beforeSubmit);
		addNavigate.initNavigate();
		addNavigate.initShowmore();
		$('#zw_f_channel_limit').change(function(){
			addNavigate.changeChannelLimit();
		});	
		$('input[name="zw_f_channels"]').change(function(){
			var allChecked = true;
			$('input[name="zw_f_channels"]').each(function(){
				if(!$(this).prop('checked')) {
					allChecked = false;
				}
			});
			
			// 所有渠道都被选中时自动变更为无限制
			if(allChecked) {
				$('#zw_f_channel_limit').val('4497471600070001');
				$('input[name="zw_f_channels"]').prop('checked',false);
				addNavigate.changeChannelLimit();
			}
		});
	},
	initShowmore : function() {
		
		 var linktype = $('#zw_f_showmore_linktype').find('option:selected').val();
		 $("#zw_f_nav_code").parent().parent().hide();
		 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");
		 
		 if(linktype != "4497471600020003"){//抽奖
				$("#linkvalueDiv").hide();
			}
		 if (linktype == "4497471600020001") {// URL
				
			    $("#linkvalueDiv").show();
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("URL：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div")
						.append('<input id="zw_f_showmore_linkvalue" type="text" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" value="">')
						.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
				$("#zw_f_min_program_id").parent().parent().hide();
				$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
		 	} else if (linktype == "4497471600020002") {// 关键词类型
		
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("关键词：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append(
								'<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="">');
		 	} else if (linktype == "4497471600020003") {// 分类搜索

				$("#linkvalueDiv").show();
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
								'<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue"  zapweb_attr_regex_id="469923180002" type="hidden">');
		 	} else if (linktype == "4497471600020004") {// 商品详情
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("选择商品：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
				$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="hidden">');
			}else if (linktype == "4497471600020008") {// 直播页面
				 $("#linkvalueDiv").show();
//				 $("#linkvalueDiv").find("label").text("直播页面：");
				 $("#linkvalueDiv").find("label").text("");
				 $("#linkvalueDiv").find("div").find("input").remove();
				 $("#linkvalueDiv").find("div").find("span").remove();
				 $("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" type="hidden" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="">');
			}else if (linktype == "4497471600020011"){// 摇一摇
				$("#zw_f_min_program_id").parent().parent().hide();
				$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
			}else if (linktype == "4497471600020012"){// 小程序
				 $("#linkvalueDiv").show();
					$("#linkvalueDiv").show();
					$("#linkvalueDiv").find("label").text("URL：");
					$("#linkvalueDiv").find("div").find("input").remove();
					$("#linkvalueDiv").find("div").find("span").remove();
					$("#linkvalueDiv").find("div")
							.append('<input id="zw_f_showmore_linkvalue" type="text" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" value="">')
							.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
					$("#zw_f_min_program_id").parent().parent().hide();
					$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
				 $("#zw_f_min_program_id").parent().parent().show();
				 $("#zw_f_min_program_id").attr("zapweb_attr_regex_id","469923180002");
			 }else if(linktype == "4497471600020013"){//首页导航
				 
				 
				 $("#zw_f_nav_code").parent().parent().show();
			     $("#zw_f_showmore_linkvalue").parent().parent().hide();
			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");
			 }
		 if(linktype != "4497471600020012"){
			 $("#zw_f_min_program_id").parent().parent().hide();
			$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
		 }

		 if($('#zw_f_showmore_linkvalue').parent().parent().is(':hidden')){

			 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");

		 }else{

		     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");

		 }
		 addNavigate.changeChannelLimit();
		 
	},
	changeChannelLimit :function (){
		if($("#zw_f_channel_limit").val()=='4497471600070002'){
			$("#zw_f_channels_0").parents('.control-group').show();
		} else {
			$("#zw_f_channels_0").parents('.control-group').hide();
		}
	},	
	 initNavigate:function(){
		 /*zapjs.f.ajaxjson("../func/" + sOperate, data, function(data) {
				zapjs.zw.func_success(data);
		 });*/
		 $("#zw_f_is_show").parent().parent().hide();
		 $("#zw_f_is_show").val("449746250002");
		 $("#zw_f_nav_code").parent().parent().hide();
		 var navigateType = $('#zw_f_navigation_type').find('option:selected').val();
		 $("#zw_f_channel_limit").val("4497471600070001");
		 $("#zw_f_channel_limit").parent().parent().hide();
		 $("#zw_f_channels_0").parents('.control-group').hide();
		 
		 if(navigateType=='4497467900040006'||navigateType=='4497467900040007'||navigateType=='4497467900040008'){
			 $("#zw_f_showmore_linktype").attr("disabled",false);
			 $('#zw_f_type_name').parent().parent().hide();
			 $('#zw_f_before_fontcolor').parent().parent().hide();
			 $('#zw_f_after_pic').parent().parent().hide();
			 $('#zw_f_after_fontcolor').parent().parent().hide();
			 $('#linkvalueDiv').show();
			 $('#showmoreLinktype').show();
			 $("#zw_f_type_name").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_before_fontcolor").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_after_pic").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_after_fontcolor").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");
			 $("#zw_f_showmore_linktype").attr("zapweb_attr_regex_id","469923180002");
			 
			 //$("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option><option value='4497471600020011'>摇一摇</option><option value='4497471600020012'>小程序</option><option value=\"4497471600020013\">首页导航</option><option value=\"4497471600020016\">直播列表</option><option value=\"4497471600020017\">视频列表</option>");
			 $("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option><option value='4497471600020002'>关键词搜索</option><option value='4497471600020003'>分类搜索</option><option value='4497471600020004'>商品详情</option><option value='4497471600020006'>抽奖</option><option value='4497471600020008'>直播页面</option><option value='4497471600020011'>摇一摇</option><option value='4497471600020012'>小程序</option><option value='4497471600020013'>首页导航</option><option value='4497471600020016'>直播列表</option><option value='4497471600020017'>视频列表</option>");

			$("#zw_f_showmore_linktype").val("4497471600020001");			

			 /*if(navigateType=='4497467900040007'){
				 $("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option><option value='4497471600020011'>摇一摇</option><option value='4497471600020012'>小程序</option><option value=\"4497471600020013\">首页导航</option><option value=\"4497471600020016\">直播列表</option><option value=\"4497471600020017\">视频列表</option>");
				 $("#zw_f_showmore_linktype").val("4497471600020001");			
			 }
			 if(navigateType=='4497467900040008'){
				 $("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option>");
				 $("#zw_f_showmore_linktype").val("4497471600020001");	
			 }*/
			 if(navigateType=='4497467900040006'||navigateType=='4497467900040007'||navigateType=='4497467900040008'){
				 //$("#zw_f_showmore_linktype").html(addNavigate.selectvalue);
				 $("#zw_f_channel_limit").parent().parent().show();
				 
			 }
			 $('#zw_f_before_pic').parent().prev().html('<span class="w_regex_need">*</span>选中前图片：');
			 $("#zw_f_min_program_id").parent().parent().hide();
			 $("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
		 }else if(navigateType=='4497467900040009'||navigateType=='4497467900040010'||navigateType=='4497467900040011'){
			 $('#zw_f_type_name').parent().parent().hide();
			 $('#zw_f_before_pic').parent().prev().html('<span class="w_regex_need">*</span>选择图片：');
			 $('#zw_f_before_fontcolor').parent().parent().hide();
			 $('#zw_f_after_pic').parent().parent().hide();
			 $('#zw_f_after_fontcolor').parent().parent().hide();
			 $('#linkvalueDiv').hide();
			 $('#showmoreLinktype').hide();
			 $("#zw_f_type_name").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_before_fontcolor").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_showmore_linktype").removeAttr("zapweb_attr_regex_id");
			 $('#zw_f_after_pic').removeAttr("zapweb_attr_regex_id");
			 $('#zw_f_after_fontcolor').removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_min_program_id").parent().parent().hide();
			 $("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
		 }else{

			 $('#zw_f_type_name').parent().parent().show();
			 $('#zw_f_before_fontcolor').parent().parent().show();
			 $('#zw_f_after_pic').parent().parent().show();
			 $('#zw_f_after_fontcolor').parent().parent().show();
			 $('#linkvalueDiv').hide();
			 $('#showmoreLinktype').hide();
			 $("#zw_f_type_name").attr("zapweb_attr_regex_id","469923180002");
			 $("#zw_f_before_fontcolor").attr("zapweb_attr_regex_id","469923180002");
			 $("#zw_f_after_pic").attr("zapweb_attr_regex_id","469923180002");
			 $("#zw_f_after_fontcolor").attr("zapweb_attr_regex_id","469923180002");
			 $("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");
			 $("#zw_f_showmore_linktype").removeAttr("zapweb_attr_regex_id");
			 $('#zw_f_before_pic').parent().prev().html('<span class="w_regex_need">*</span>选中前图片：');
			 $("#zw_f_min_program_id").parent().parent().hide();
			 $("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
		 }
		 if($('#zw_f_showmore_linkvalue').parent().parent().is(':hidden')){

			 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");

		 }else{

		     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");

		 }
	    },
	
	
	beforeSubmit : function() {
		var zw_f_showmore_linktype = $("#zw_f_showmore_linktype").val();
//		var showText;
		if (zw_f_showmore_linktype == "4497471600020001") {// URL
//			showText = 'URL';
			var lv = $("#zw_f_showmore_linkvalue").val();
			if (lv != "" && lv.toLowerCase().indexOf("http://share") != -1) {
				zapjs.f.message('URL中不能包含"http://share"');
				return false;
			}
			$("#zw_f_showmore_linkvalue").val(
					$("#zw_f_showmore_linkvalue").val().trim());
		} else if (zw_f_showmore_linktype == "4497471600020002") {// 关键词类型
//			showText = '关键词';
		} else if (zw_f_showmore_linktype == "4497471600020003") {// 分类搜索
//			showText = '分类';
		} else if (zw_f_showmore_linktype == "4497471600020004") {// 商品详情
//			showText = '选择商品';
		}
	
		return true;
	}

};

$(document).ready(function() {

	// 二级联动，切换链接类型时调用
	$("#zw_f_showmore_linktype").change(function() {
		addNavigate.initShowmore();
	});
	// 二级联动，切换导航分类时调用
	$("#zw_f_navigation_type").change(function() {
		addNavigate.initNavigate();
	});
	
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addNavigate", function() {
		return addNavigate;
	});
}
