var editNavigate = {
		selectvalue : '',
		linkvalue : '',
		init : function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editNavigate.beforeSubmit);
			
			editNavigate.initData();
			editNavigate.initNavigate();
			editNavigate.initShowmore();
			//隐藏栏目内容类型值,此控件用作获取类型值
//			$("#showmore_linkvalue").parent().parent().hide();
//			$("#zw_f_showmore_linkvalue").val($("#showmore_linkvalue").val());
			$("#zw_f_showmore_linktype").val(editNavigate.linkvalue);
			if(editNavigate.linkvalue == "4497471600020012"){// 小程序单独处理
				var linkValue = $("#zw_f_showmore_linkvalue").val();
				 $("#linkvalueDiv").show();
					$("#linkvalueDiv").show();
					$("#linkvalueDiv").find("label").text("URL：");
					$("#linkvalueDiv").find("div").find("input").remove();
					$("#linkvalueDiv").find("div").find("span").remove();
					$("#linkvalueDiv").find("div")
							.append('<input id="zw_f_showmore_linkvalue" type="text" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" value="'+linkValue+'">')
							.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
					$("#zw_f_min_program_id").parent().parent().hide();
					$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
					
				$("#zw_f_min_program_id").parent().parent().show();
				$("#zw_f_min_program_id").attr("zapweb_attr_regex_id","469923180002");
			}else if(editNavigate.linkvalue == "4497471600020013"){
				 $("#zw_f_nav_code").parent().parent().show();
			     $("#zw_f_showmore_linkvalue").parent().parent().hide();
			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");
			}else if(editNavigate.linkvalue == "4497471600020016"||editNavigate.linkvalue == "4497471600020017"){
				 $("#zw_f_nav_code").parent().parent().hide();
			     $("#zw_f_showmore_linkvalue").parent().parent().hide();
			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");
			}
			if($("#zw_f_navigation_type").val() == '4497467900040006' || $("#zw_f_navigation_type").val() == '4497467900040007'){
				$("#zw_f_is_show").parent().parent().hide();
				$("#zw_f_channel_limit").parent().parent().show();
				editNavigate.changeChannelLimit();
			}else{
				$("#zw_f_is_show").parent().parent().hide();
			}
			if($('#zw_f_showmore_linkvalue').parent().parent().is(':hidden')){

				 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");

			 }else{

			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");

			 }
			$('#zw_f_channel_limit').change(function(){
				editNavigate.changeChannelLimit();
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
					editNavigate.changeChannelLimit();
				}
			});
		},
		initData:function(){
//			$("#zw_f_position").val($("#zw_f_position_select").val());
			if($("#zw_f_showmore_linktype").val() == "4497471600020004"){//类型为商品
				   //显示商品名称
				var opt ={};
				opt.productStrs = $("#zw_f_showmore_linkvalue").val();
				zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductName',opt,function(result) {
					$("#zw_f_showmore_linkvalue_text").text(result.productName);//显示商品名称
				});
			}
			if($('#zw_f_showmore_linkvalue').parent().parent().is(':hidden')){

				 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");

			 }else{

			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");

			 }
		},
		
		initNavigate:function(){
			$("#zw_f_nav_code").parent().parent().hide();
			$("#zw_f_channel_limit").parent().parent().hide();
			$("#zw_f_channels_0").parents('.control-group').hide();
			$("#zw_f_is_show").parent().parent().hide();
			var navigateType = $('#zw_f_navigation_type').find('option:selected').val();
			
			var linktype = $("#zw_f_showmore_linktype").find('option:selected').val();//所选试用类型的值
			
			 if(navigateType=='4497467900040006'||navigateType=='4497467900040007'||navigateType=='4497467900040008'){
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
				 $("#zw_f_type_name").val("");
				 $("#zw_f_before_fontcolor").val("");
				 $("#zw_f_after_pic").val("");
				 $("#zw_f_after_fontcolor").val("");
				 
				//$("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option><option value='4497471600020011'>摇一摇</option><option value='4497471600020012'>小程序</option><option value=\"4497471600020013\">首页导航</option><option value=\"4497471600020016\">直播列表</option><option value=\"4497471600020017\">视频列表</option>");
				 $("#zw_f_showmore_linktype").html("<option value=''>请选择</option><option value='4497471600020001'>URL</option><option value='4497471600020002'>关键词搜索</option><option value='4497471600020003'>分类搜索</option><option value='4497471600020004'>商品详情</option><option value='4497471600020006'>抽奖</option><option value='4497471600020008'>直播页面</option><option value='4497471600020011'>摇一摇</option><option value='4497471600020012'>小程序</option><option value='4497471600020013'>首页导航</option><option value='4497471600020016'>直播列表</option><option value='4497471600020017'>视频列表</option>");

				 $("#zw_f_showmore_linktype").val(linktype);		
				/* if(navigateType=='4497467900040007'){
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
				 /*if(navigateType=='4497467900040006'){
					 $("#zw_f_showmore_linktype").html(editNavigate.selectvalue);
					 $("#zw_f_channel_limit").parent().parent().show();
					 editNavigate.changeChannelLimit();
				 }*/
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
				 $("#zw_f_showmore_linktype").val("");
				 $("#zw_f_showmore_linkvalue").val("");
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
		
		initShowmore:function(){
			 var linktype = $("#zw_f_showmore_linktype").val();//所选试用类型的值
			 var linkValue = $("#zw_f_showmore_linkvalue").val();
			
			 $("#zw_f_nav_code").parent().parent().hide();
			 $("#zw_f_min_program_id").parent().parent().hide();
			 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");
			 if (linkValue == "直播页面"){
					linkValue = "";
			 }
			// var linktype = $('#zw_f_showmore_linktype').find('option:selected').val();
			 
			 if(linktype != "4497471600020003"){//抽奖
					$("#linkvalueDiv").hide();
				}
			 if (linktype == "4497471600020001") {// URL
				 
				    $("#linkvalueDiv").show();
					$("#linkvalueDiv").show();
					$("#linkvalueDiv").find("label").text("URL：");
					$("#linkvalueDiv").find("div").find("input").remove();
					$("#linkvalueDiv").find("div").find("span").remove();
					if(linkValue=="4497160400500001"){
						linkValue="";
					}
					$("#linkvalueDiv").find("div")
							.append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="'+linkValue+'">')
							.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
					$("#zw_f_min_program_id").parent().parent().hide();
					$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
			 	} else if (linktype == "4497471600020002") {// 关键词类型
			 		if(linkValue=="4497160400500001"){
						linkValue = "";
					}
					$("#linkvalueDiv").show();
					$("#linkvalueDiv").find("label").text("关键词：");
					$("#linkvalueDiv").find("div").find("input").remove();
					$("#linkvalueDiv").find("div").find("span").remove();
					$("#linkvalueDiv").find("div").append(
									'<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="'+linkValue+'">');
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
									'<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="hidden" value="'+linkValue+'">');
					$("#zw_f_showmore_linkvalue_text").html($("#categroyName").val());

				} else if (linktype == "4497471600020004") {// 商品详情
					
					$("#linkvalueDiv").show();
					$("#linkvalueDiv").find("label").text("选择商品：");
					$("#linkvalueDiv").find("div").find("input").remove();
					$("#linkvalueDiv").find("div").find("span").remove();
					$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
					$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
					$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="hidden" value="'+linkValue+'">');
					
				}else if(linktype == "4497471600020006"||linktype =="4497471600020016"||linktype =="4497471600020017"){//抽奖
					$("#linkvalueDiv").hide();
				}else if (linktype == "4497471600020008") {// 直播页面
					 $("#linkvalueDiv").show();
//					 $("#linkvalueDiv").find("label").text("直播页面：");
					 $("#linkvalueDiv").find("label").text("");
					 $("#linkvalueDiv").find("div").find("input").remove();
					 $("#linkvalueDiv").find("div").find("span").remove();
					 $("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" type="hidden" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="直播页面">');
				 }else if (linktype == "4497471600020011"){// 摇一摇
					$("#zw_f_min_program_id").parent().parent().hide();
					$("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
				 }else if (linktype == "4497471600020012"){// 小程序
					 $("#linkvalueDiv").show();
						$("#linkvalueDiv").find("label").text("URL：");
						$("#linkvalueDiv").find("div").find("input").remove();
						$("#linkvalueDiv").find("div").find("span").remove();
						$("#linkvalueDiv").find("div")
								.append('<input id="zw_f_showmore_linkvalue" type="text" zapweb_attr_regex_id="469923180002" name="zw_f_showmore_linkvalue" value="'+linkValue+'">')
								.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
					$("#zw_f_min_program_id").parent().parent().show();
					$("#zw_f_min_program_id").attr("zapweb_attr_regex_id","469923180002");
				 }else if(linktype == "4497471600020013"){//首页导航
					 $("#zw_f_nav_code").parent().parent().show();
				     $("#zw_f_showmore_linkvalue").parent().parent().hide();
				     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");
				 }
			 if(linktype == "4497471600020013"||linktype =="4497471600020016"||linktype =="4497471600020017"){
				 $("#zw_f_min_program_id").parent().parent().hide();
				 $("#zw_f_min_program_id").removeAttr("zapweb_attr_regex_id");
			 }
			 if($('#zw_f_showmore_linkvalue').parent().parent().is(':hidden')){

				 $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180001");

			 }else{

			     $("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id","469923180002");

			 }
		},
		changeChannelLimit :function (){
			if($("#zw_f_channel_limit").val()=='4497471600070002'){
				$("#zw_f_channels_0").parents('.control-group').show();
			} else {
				$("#zw_f_channels_0").parents('.control-group').hide();
			}
		},	

		beforeSubmit:function(){
			var zw_f_showmore_linktype = $("#zw_f_showmore_linktype").val();
//			var showText = 'URL/关键词/分类/商品';
			if(zw_f_showmore_linktype == "4497471600020001"){//URL
//				showText = 'URL';
				var lv = $("#zw_f_showmore_linkvalue").val();
				if(lv != "" && lv.toLowerCase().indexOf("http://share") != -1 ){
					zapjs.f.message('URL中不能包含"http://share"');
					return false;
				}
				$("#zw_f_showmore_linkvalue").val($("#zw_f_showmore_linkvalue").val().trim());
			}else if(zw_f_showmore_linktype == "4497471600020002"){//关键词类型
//				showText = '关键词';
			}else if(zw_f_showmore_linktype == "4497471600020003"){//分类搜索
//				showText = '分类';
			}else if(zw_f_showmore_linktype == "4497471600020004"){//商品详情
//				showText = '选择商品';
//				$("#zw_f_showmore_linkvalue").val($("#zw_f_product_code_select").val());
			}
			//不加此句时保存到后台的showmore_linkvalue为空。原因为后台视图里面product_code_select字段关联了数据库字段showmore_linkvalue
//			$("#zw_f_product_code_select").val($("#zw_f_showmore_linkvalue").val());
//			if (!zapjs.zw.validate_field('#zw_f_showmore_linkvalue', '469923180002', showText)) {
//				return false;
//			}
			return true;
		}
};

$(document).ready(function(){

	//二级联动，切换链接类型时调用
	$("#zw_f_showmore_linktype").change(function(){
		editNavigate.initShowmore();
	});
	
	//二级联动，切换导航类型时调用
	$("#zw_f_navigation_type").change(function(){
		editNavigate.initNavigate();
	});
	
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/editNavigate", function() {
		return editNavigate;
	});
}
