var addColumn = {
		
	init : function() {
		$("#zw_f_view_type").val("4497471600100005");
		// $("#view_type").val($("#zw_f_view_type").val()); // 在页面设置一个隐藏域，name为zw_f_view_type
		// $("#zw_f_view_type").attr("disabled", true);// 把展示类型设置为APP端
		
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', addColumn.beforeSubmit);
		addColumn.showMoreInit();
		addColumn.noticeInit();
		// 链接类型字段把显示浮层类型去掉
		$("#zw_f_showmore_linktype option[value='4497471600020005']").remove();
		$('#zw_f_product_maintenance').change(function(){
			addColumn.show_releated_field();
		});
		//$('#zw_f_prod_recommend').change(function(){
		//	addColumn.show_category_limit();
		//});
		$('#zw_f_category_limit').change(function(){
			addColumn.show_category();
		});
		$('#zw_f_rule_code').after('<span class="w_regex_need" id="rule_name"></span>');
		$('#zw_f_rule_code').blur(function(){
			var rulename = $('#zw_f_rule_code').val();
			zapjs.f.ajaxjson('../func/0bded441de114723ad6fa8dfa517eda2',"rule_name="+rulename,function(msg){
				if(msg.resultCode == 1){
					$('#rule_name').text(msg.resultObject);
				}
			});
		});

	},

	show_releated_field :function (){
		// 先重置
		$("#zw_f_rule_code").parent().parent().hide();
		$("#zw_f_update_frequency").parent().parent().hide();
		$("#zw_f_rule_code").val("");
		$("#zw_f_update_frequency").val("");
		$("#zw_f_rule_code").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_update_frequency").removeAttr("zapweb_attr_regex_id");
		
		$("#zw_f_category_limit").parent().parent().hide();
		$("#zw_f_category_limit").removeAttr("zapweb_attr_regex_id");
		$('label[for="zw_f_category_codes"]').parent().hide();
		$("#zw_f_category_limit").val("449748560001");
		$("form input[name='zw_f_category_codes']").each(function(){
			this.checked = false;
		}); 
		$("#zw_f_show_num_bar_parent").remove();
		$('#zw_f_show_num').val("0");		
		
		if($("#zw_f_product_maintenance").val()=='44975017002'){
			// 自动选品
			$('label[for="zw_f_rule_code"]').html('<span class="w_regex_need">*</span>规则编号：');
			$('label[for="zw_f_update_frequency"]').html('<span class="w_regex_need">*</span>更新频率：');
			$("#zw_f_rule_code").parent().parent().show();
			$("#zw_f_update_frequency").parent().parent().show();
			$("#zw_f_rule_code").attr("zapweb_attr_regex_id","469923180002");
			$("#zw_f_update_frequency").attr("zapweb_attr_regex_id","469923180002");
		} else if($("#zw_f_product_maintenance").val()=='44975017003'){
			// 智能推荐
			$("#zw_f_category_limit").parent().parent().show();             
			$("#zw_f_category_limit").attr("zapweb_attr_regex_id","469923180002");
			$('label[for="zw_f_category_limit"]').html('<span class="w_regex_need">*</span>分类限制：');
			var temDiv = '<div class="control-group"  id="zw_f_show_num_bar_parent" > <label class="control-label" for="zw_f_show_num_bar"><span class="w_regex_need">*</span> 显示数量：</label><div class="controls"><input  type="text" id="zw_f_show_num_bar" name="zw_f_show_num_bar" class="zw_f_show_num_bar" placeholder="1~20之间的整数" value=""></div></div>';
			$("label[for='zw_f_category_codes']").parent().after(temDiv);
			$('#zw_f_show_num_bar').keyup(function(){
				var digReg = /^[1-9]\d*$/;
				if($(".zw_f_show_num_bar").val()!=""&&$(".zw_f_show_num_bar").val()!=null){
					if(!digReg.test($(".zw_f_show_num_bar").val())||$(".zw_f_show_num_bar").val()<1||$(".zw_f_show_num_bar").val()>20) {
						if(!digReg.test($(".zw_f_show_num_bar").val())){
							$(".zw_f_show_num_bar").val("");
							}
						zapjs.f.modal({
							content : '请填写1~20之间的整数!'
						});
						return;
					}else{
						$('#zw_f_show_num').val($('#zw_f_show_num_bar').val());
					}
				}
			});			
		}
	},

	// 分类限制控制分类
	show_category :function (){
		if($("#zw_f_category_limit").val()=='449748560002'){
			// 分类限制
			$('label[for="zw_f_category_codes"]').parent().show();
			$('label[for="zw_f_category_codes"]').html('<span class="w_regex_need">*</span>分类：');
			
		} else {
			// 无限制
			$('label[for="zw_f_category_codes"]').parent().hide();
			$("form input[name='zw_f_category_codes']").each(function(){
				this.checked = false;
			});
			
			
		}
	},
	
	showMoreInit : function() {
		//recovery();
		$("#zw_f_product_maintenance").parent().parent().hide();
		$("#zw_f_rule_code").parent().parent().hide();
		$("#zw_f_update_frequency").parent().parent().hide();
		$("#zw_f_event_code").parent().parent().hide();
		$("#zw_f_rule_code").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_update_frequency").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_product_maintenance").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_event_code").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_prod_recommend").parent().parent().hide();
		$("#zw_f_prod_recommend").removeAttr("zapweb_attr_regex_id");
		$("#zw_f_category_limit").parent().parent().hide();
		$('label[for="zw_f_category_codes"]').parent().hide();
		$("#zw_f_column_name_corlor").parent().parent().hide();
		$("#zw_f_show_style").parent().parent().hide();
		
		var columnType = $("#zw_f_column_type").val();
		if (columnType == "4497471600010001"
				|| columnType == "4497471600010002"
				|| columnType == "4497471600010003"
				|| columnType == "4497471600010004"
				|| columnType == "4497471600010013"
				|| columnType == "4497471600010014"
				|| columnType == "4497471600010021"
				|| columnType == "4497471600010012"
				|| columnType == "4497471600010027") {
			$("#zw_f_is_showmore").val("449746250002");   			//否
			$("#zw_f_is_showmore").attr("disabled", true);			// 把是否显示更多设置为不可用
			$("#showmore_type").attr("disabled", false);			// 把隐藏域设置为可用
			$("#showmore_type").val($("#zw_f_is_showmore").val());	// 把是否显示更多的值赋给隐藏域
		} else if(columnType == "4497471600010019") {
			//导航栏URL add by zht.20170721
			$("#zw_f_is_showmore").val("449746250001");   			//是
			$("#zw_f_is_showmore").attr("disabled", true);			// 把是否显示更多设置为不可用
			$("#showmore_type").attr("disabled", false);			// 把隐藏域设置为可用
			$("#showmore_type").val($("#zw_f_is_showmore").val());	// 把是否显示更多的值赋给隐藏域
		}  else if(columnType == "4497471600010024") {
			$("#zw_f_event_code").parent().parent().show();
			   $('label[for="zw_f_event_code"]').html('<span class="w_regex_need">*</span>拼团活动编号：');
			   $("#zw_f_event_code").attr("zapweb_attr_regex_id","469923180002");
			   $("#zw_f_is_showmore").attr("disabled", false);// 把是否显示更多设置为可用
			   $("#showmore_type").attr("disabled", true);// 把隐藏域设置为不可用

		}  else if(columnType == "4497471600010025") {
			$("#zw_f_is_showmore").attr("disabled", false);// 把是否显示更多设置为可用
			$("#showmore_type").attr("disabled", true);// 把隐藏域设置为不可用
			
		}else {
			$("#zw_f_is_showmore").attr("disabled", false);// 把是否显示更多设置为可用
			$("#showmore_type").attr("disabled", true);// 把隐藏域设置为不可用
		}
		
		//商品维护
		if(columnType == "4497471600010008" || columnType == "4497471600010013"|| columnType == "4497471600010014" || columnType == "4497471600010025"){
		   $("#zw_f_product_maintenance").parent().parent().show();
		   $('label[for="zw_f_product_maintenance"]').html('<span class="w_regex_need">*</span>商品维护：');
		   $("#zw_f_product_maintenance").attr("zapweb_attr_regex_id","469923180002");
		}
		
		if(columnType == "4497471600010030") {
			// 564买家秀入口添加展示样式
			$("#zw_f_show_style").parent().parent().show();
			$('label[for="zw_f_show_style"]').html('<span class="w_regex_need">*</span>展示样式：');
		}
		
		addColumn.showMoreSelect();
		// 导航栏时显示模版背景图片，否则隐藏
		if (columnType == "4497471600010004") { // 导航栏
			$("#zw_f_column_bgpic").parent().parent().show();
			$("#zw_f_columns_per_row").parent().parent().show();
		}else if(columnType == "4497471600010011"){//闪购
			$("#zw_f_column_bgpic").parent().parent().show();
			$("#zw_f_column_name_corlor").parent().parent().show();
		} else if (columnType == "4497471600010017"){ //|| columnType == "4497471600010022") { // 视频播放列表、秒杀 是否显示更多 需要隐藏
			$("#zw_f_is_showmore").parent().parent().hide();
		}  else if(columnType == "4497471600010030" || columnType == "4497471600010029") {
			$("#zw_f_show_name").val("449746250002"); 
			$("#zw_f_show_name").parent().parent().hide();
			$("#zw_f_is_showmore").val("449746250002"); 
			$("#zw_f_is_showmore").attr("disabled", true);			// 把是否显示更多设置为不可用
			$("#showmore_type").attr("disabled", false);			// 把隐藏域设置为可用
			$("#showmore_type").val($("#zw_f_is_showmore").val());	// 把是否显示更多的值赋给隐藏域
			$("#zw_f_is_showmore").parent().parent().hide();
			$("#zw_f_is_had_edge_distance").val("4497479100010001");
			$("#zw_f_is_had_edge_distance").parent().parent().hide();
			
		} else {
			$("#zw_f_column_bgpic").parent().parent().hide();
			$("#zw_f_columns_per_row").parent().parent().hide();
			$("#zw_f_is_showmore").parent().parent().show();
			if(columnType == "4497471600010019") {
				//导航栏URL add by zht.20170721
				$("#zw_f_show_name").val("449746250002");    //是否显示栏目名称-否
				$("#zw_f_is_had_edge_distance").val("4497479100010001");  //是有边距-否
				$("#zw_f_column_name").val("导航栏URL");		
				$("#zw_f_column_name").parent().parent().hide(); 			//隐藏'栏目名称'
				$("#zw_f_position").val("0");			
				$("#zw_f_position").parent().parent().hide();				//隐藏'位置'
				$("#zw_f_is_had_edge_distance").parent().parent().hide();   //隐藏'是否有边距选择框'
				$("#zw_f_show_name").parent().parent().hide();              //隐藏'是否显示栏目名称'
				$("#linkvalueDiv").show();
			}else if(columnType == "4497471600010036"){
				$("#zw_f_show_name").val("449746250002");
				$("#zw_f_is_showmore").val("449746250002");
				$("#zw_f_show_name").parent().parent().hide();
				$("#zw_f_is_showmore").parent().parent().hide();
			} else if(columnType == "4497471600010035"){
				$("#zw_f_show_name").parent().parent().hide();
				$("#zw_f_show_name").val("449746250002");
				$("#zw_f_is_had_edge_distance").parent().parent().hide();
				$("#zw_f_is_had_edge_distance").val("4497479100010002");
				$("#zw_f_is_showmore").parent().parent().hide();
				$("#zw_f_is_showmore").val("449746250002");			
			}else {
				$("#zw_f_column_name").val("");	
				$("#zw_f_column_name").parent().parent().show(); 			//显示'栏目名称'
				$("#zw_f_position").val("");			
				$("#zw_f_position").parent().parent().show();				//显示'位置'
				$("#zw_f_is_had_edge_distance").parent().parent().show();   //显示'是否有边距选择框'
				$("#zw_f_show_name").parent().parent().show();              //显示'是否显示栏目名称'
			}
		}
	},
	showMoreSelect : function() {
		var columnType = $("#zw_f_column_type").val();
		var showmore = $("#zw_f_is_showmore").val();
		if (showmore == 449746250001) {// 是
			if (columnType == "4497471600010010"
					|| columnType == "4497471600010016" || columnType == "4497471600010036") {// 如果栏目类型为TV直播
				$("#zw_f_showmore_title").attr("zapweb_attr_regex_id", "469923180002");// 标题设为必填
				$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 设为非必填
				$("#zw_f_showmore_linktype").attr("disabled", true);// 链接类型设置为不可用
				$("#zw_f_showmore_linkvalue").attr("disabled", true);// 链接值设置为不可用
				$("#zw_f_showmore_title").parent().parent().show();
				$("#zw_f_showmore_linktype").parent().parent().hide();
				$("#linkvalueDiv").hide();
			} else if(columnType == "4497471600010019") {
				//导航栏URL add by zht.20170721
				$("#zw_f_showmore_title").removeAttr("zapweb_attr_regex_id");// 标题设为非必填
				$("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id", "469923180002");// 链接值设为必填
				$("#zw_f_showmore_linkvalue").attr("disabled", false);// 链接值设置为可用
				
				$("#zw_f_showmore_title").val("导航栏URL");
				$("#zw_f_showmore_title").parent().parent().hide();
				$("#zw_f_showmore_linktype").parent().parent().hide();
				$("#zw_f_showmore_linktype").val("4497471600020001");
			} else if(columnType == "4497471600010023"||columnType == "4497471600010034"){//拼团 
				$("#zw_f_showmore_linktype").parent().parent().hide();
				$("#zw_f_showmore_linktype").attr("disabled",true);
				if(columnType == "4497471600010023"){
					$("#miaoshalinktype").val("4497471600020009");
				}else{
					$("#miaoshalinktype").val("4497471600020015");
				}
				$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_showmore_linkvalue").parent().parent().hide();
				$("#zw_f_showmore_title").parent().parent().show();
			}else if(columnType == "4497471600010024") {
				$("#zw_f_event_code").parent().parent().show();
				   $('label[for="zw_f_event_code"]').html('<span class="w_regex_need">*</span>拼团活动编号：');
				   $("#zw_f_event_code").attr("zapweb_attr_regex_id","469923180002");
				   $("#miaoshalinktype").val("4497471600020009");
				   $("#zw_f_showmore_linkvalue").val("4497471600020009");
					//导航栏URL add by zht.20170721
				   $("#zw_f_showmore_title").attr("zapweb_attr_regex_id", "469923180002");// 标题设为必填
					$("#zw_f_showmore_linktype").removeAttr("zapweb_attr_regex_id");// 标题设为非必填
					$("#zw_f_showmore_linkvalue").removeAttr("zapweb_attr_regex_id");// 标题设为非必填
					$("#zw_f_showmore_title").parent().parent().show();
					$('label[for="zw_f_showmore_title"]').html('<span class="w_regex_need">*</span>标题：');
					$("#zw_f_showmore_linktype").attr("disabled",true);
					$("#zw_f_showmore_linktype").parent().parent().hide();
					$("#zw_f_showmore_linkvalue").parent().parent().hide();

			}else {
				$("#zw_f_showmore_title").attr("zapweb_attr_regex_id", "469923180002");// 标题设为必填
				$("#zw_f_showmore_linkvalue").attr("zapweb_attr_regex_id", "469923180002");// 链接值设为必填
				$("#zw_f_showmore_linktype").attr("disabled", false);// 链接类型设置为可用
				$("#zw_f_showmore_linkvalue").attr("disabled", false);// 链接值设置为可用
				$("#zw_f_showmore_title").val("");
				$("#zw_f_showmore_title").parent().parent().show();
				$("#zw_f_showmore_linktype").parent().parent().show();
				$("#linkvalueDiv").show();
				if(columnType == "4497471600010022"){
					$("#zw_f_showmore_linktype").val("4497471600020001");
					$("#zw_f_showmore_linktype").attr("disabled","disabled");
					$("#miaoshalinktype").val("4497471600020001");
				}else{
					$("#miaoshalinktype").attr("disabled","disabled");
				}
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
			//通知模板
			// $("#zw_f_notice_type").parent().parent().show();
			$("#zw_f_interval_second").parent().parent().show();
		} else {
			// $("#zw_f_notice_type").parent().parent().hide();
			$("#zw_f_interval_second").parent().parent().hide();
		}
		
		if ($("#zw_f_column_type").val() == "4497471600010016") {
			//TV直播滑动
			$("#zw_f_future_program").parent().parent().show();
		} else {
			$("#zw_f_future_program").parent().parent().hide();
		}
		if ($("#zw_f_column_type").val() == "4497471600010023"||$("#zw_f_column_type").val() == "4497471600010034") {
			//拼团
			$("#zw_f_event_code").parent().parent().show();
			$("#zw_f_show_num").parent().parent().show();
			$("#zw_f_show_num").val("");
			$("#zw_f_event_code").attr("zapweb_attr_regex_id", "469923180002");
			$("#zw_f_event_code").parent().prev().html("<span class=\"w_regex_need\">*</span>拼团活动编号：");
		}else if($("#zw_f_column_type").val()  == "4497471600010024") {
			$("#zw_f_event_code").parent().parent().show();
			   $('label[for="zw_f_event_code"]').html('<span class="w_regex_need">*</span>拼团活动编号：');
			   $("#zw_f_event_code").attr("zapweb_attr_regex_id","469923180002");

		} else {
			$("#zw_f_event_code").parent().parent().hide();
			$("#zw_f_show_num").parent().parent().hide();
			$("#zw_f_show_num").val("-1");
		}
		if ($("#zw_f_column_type").val() == "4497471600010021") {
			//多栏广告栏数
			$("#zw_f_num_languanggao").parent().parent().show();
		} else {
			$("#zw_f_num_languanggao").parent().parent().hide();
		}
		
		
	},
	recovery : function() {
		//导航栏URL add by zht.20170721
		$("#zw_f_show_name").val("449746250001");    //是否显示栏目名称-是
		$("#zw_f_is_showmore").val("449746250002");  //是否显示更多-否
		$("#zw_f_is_had_edge_distance").val("4497479100010002");  //是有边距-是
		
		//必填字段,在导航栏URL类型中无用,设为默认值,否则后台校验通不过
		$("#zw_f_column_name").val("");		
		$("#zw_f_position").val("");			
		$("#zw_f_showmore_title").val("");
		
		
//		$("#zw_f_is_showmore").empty();
//	    var option = '<option value="449746250001">是</option>';
//	    $("#zw_f_is_showmore").append(option);
//	    
//		$("#zw_f_showmore_linktype").empty();
//	    var option1 = '<option value="4497471600020001">URL</option>';
//	    $("#zw_f_showmore_linktype").append(option1);

		$("#zw_f_column_name").parent().parent().show(); 			//显示'栏目名称'
		$("#zw_f_show_name").parent().parent().show();              //显示'是否显示栏目名称'
		$("#zw_f_position").parent().parent().show();				//显示'位置'
		$("#zw_f_is_had_edge_distance").parent().parent().show();   //显示'是否有边距选择框'
		
//		$("#zw_f_showmore_title").parent().parent().hide();
//		$("#zw_f_showmore_linktype").parent().parent().show();
//		$("#linkvalueDiv").show();
	},
	beforeSubmit : function() {
		//return false;
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
		if($("#zw_f_column_type").val() == '4497471600010023'||$("#zw_f_column_type").val() == '4497471600010034'){
			if(!$.trim($("#zw_f_event_code").val())){
				alert("拼团活动编号不能为空!");
				return false;
			}
			var reg = /^[0-9]+$/;
			if (!reg.test($("#zw_f_show_num").val())) {
				alert("展示数量不能为空且必须为整数");
				return false;
		    }
		}
		
		var prod_recommend = $("#zw_f_product_maintenance").val();
		if (prod_recommend == '44975017003'){
			var category_limit = $("#zw_f_category_limit").val();
			var show_num = $("#zw_f_show_num").val();
			var digReg = /^[1-9]\d*$/;
			if(category_limit == ''){
				alert("分类限制不能为空!");
				return false;
			}else if (category_limit == '449748560002'){
				var checkCode = new Array();
		 		$("form input[name='zw_f_category_codes']").each(function(){
		            if(this.checked){
		            	checkCode.push(this.value);
		            }
		 		}); 
		 		if(checkCode.length <= 0){
		 			zapjs.f.message("请您选择分类!");
		 			return false;
		 		}
			}else if(show_num==""||show_num==null||show_num==-1){
				alert("展示数量不能为空!");
				return;
			}else if(!digReg.test(show_num)||show_num<1||show_num>20){
				zapjs.f.modal({
					content : '展示数量请填写1~20之间的整数!'
				});
				alert("展示数量请填写1~20之间的整数!");
				return;
			}
		}
		
		if($("#zw_f_column_type").val() == '4497471600010027'){
			alert("商品评价模板发布之后,该导航下将只展示商品评价模板内容,其他栏目将会隐藏!");
		}
		if($("#zw_f_column_type").val() == '4497471600010029'){
			alert("买家秀列表模板发布之后,该导航下将只展示买家秀列表模板内容,其他栏目将会隐藏!");
		}
		if($("#zw_f_column_type").val() == '4497471600010030'){
			var show_style = $("#zw_f_show_style").val();
			if(show_style == ''){
				alert("展示样式不能为空!");
				return false;
			}
		}
		// if ($("#zw_f_notice_type").val() == "4497471600170001") {
		// $("#zw_f_interval_second").val(1);
		// }
		return true;
	}

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addColumn", function() {
		return addColumn;
	});
}
// 切换是否显示更多时使用
$(document)
		.ready(
				function() {
					$("#zw_f_columns_per_row").parent().parent().hide(); // 一行显示几栏
																			// 默认隐藏
					$("#zw_f_is_showmore").change(function() {
						addColumn.showMoreSelect();
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
										} else if (linktype == "4497471600020007") {// 广告弹窗
																					// 3.9.6
																					// 需求添加此项
																					// -
																					// Yangcl
											$("#linkvalueDiv").find("label")
													.text("广告弹窗：");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											var html_ = '<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" name="zw_f_showmore_linkvalue" value="">'
											// html_ += '<span
											// class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>';
											$("#slId").append(html_);
										} else if (linktype == "4497471600020008") {// 直播页面
																					// 3.9.8
																					// 需求添加此项
																					// -
																					// Yangcl
										// $("#linkvalueDiv").find("label").text("直播页面：");
											$("#linkvalueDiv").find("label")
													.text("");
											$("#slId").find("input").remove();
											$("#slId").find("span").remove();
											var html_ = '<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="hidden" name="zw_f_showmore_linkvalue" value="直播页面">'
											$("#slId").append(html_);
										}

									});
					// 切换栏目时调用
					$("#zw_f_column_type").change(function() {
						addColumn.showMoreInit();
						addColumn.noticeInit();
					});
					// 切换通知栏目的通知类型时调用
					// $("#zw_f_notice_type").change(function() {
					// addColumn.showMoreInit();
					// addColumn.noticeInit();
					// });
				});