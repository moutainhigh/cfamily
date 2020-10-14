var editCommodity = {
	isFirst:true,
	init : function(t_type){
		
		//获取模版类型编号
		if(t_type == "449747500007" ){//   优惠券(一行两栏) 
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,commodity_name,commodity_describe,good_number,commodity_number,skip,skip_input,url,width,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
			
			//显示
			editCommodity.showEle("template_number,programa_picture,commodity_location,coupon");
			
		} else if( t_type == "449747500008" ){//    优惠券（一行一栏   更改为一行多栏）
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,commodity_name,commodity_describe,good_number,commodity_number,skip,skip_input,url,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
			
			//显示
			editCommodity.showEle("template_number,programa_picture,commodity_location,coupon,width");
			
		} else if(t_type == "449747500009"){//普通视频模板
			//显示
			editCommodity.showEle("template_number,commodity_location,url,good_number,start_time,end_time,template_desc");
			//隐藏
			editCommodity.hideEle("event_code,commodity_number,commodity_picture,is_dis,commodity_name,commodity_describe,skip,skip_input,create_time,dal_status,coupon,width,programa_picture,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			
		}else if(t_type == "449747500010"){//一行多栏（横滑）
			//显示
			editCommodity.showEle("template_number,commodity_location,programa_picture,skip,skip_input,start_time,end_time");
			//隐藏
			editCommodity.hideEle("event_code,url,width,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		}else if(t_type == "449747500011"){//一行三栏
			//显示
			editCommodity.showEle("template_number,commodity_location,programa_picture,skip,skip_input,start_time,end_time");
			//隐藏
			editCommodity.hideEle("event_code,url,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,width,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		}else if(t_type == "449747500012"){//分类模版
			//显示
			editCommodity.showEle("template_number,commodity_location,programa_picture,start_time,end_time,url,img,width");
			//隐藏
			editCommodity.hideEle("event_code,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,skip,skip_input,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
		}else if(t_type == "449747500013"){//本地货模版
			//显示
			editCommodity.showEle("template_number,commodity_location,start_time,end_time,url,city_name,title_checked_color,title_color");
			//隐藏
			editCommodity.hideEle("event_code,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,width,skip,skip_input,img,programa_picture,live_mobile,preferential_desc,title,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
		}else if(t_type == "449747500001" || t_type == "449747500024"){//模版一  或者  悬浮模板
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,commodity_name,commodity_describe,good_number,commodity_number,coupon,url,width,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			//显示
			editCommodity.showEle("template_number,skip,skip_input,programa_picture,commodity_location");
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		}else if(t_type == "449747500023"||t_type == "449747500026"){//556：拼团模板
			//隐藏
			editCommodity.hideEle("start_time,end_time,skip,skip_input,programa_picture,commodity_picture,is_dis,commodity_name,commodity_describe,good_number,commodity_number,coupon,url,width,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number");
			//显示
			editCommodity.showEle("template_number,commodity_location,event_code");
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		}  else if(t_type == "449747500006"){//两栏广告
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,commodity_name,commodity_describe,good_number,commodity_number,skip,skip_input,coupon,url,width,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			//显示
			editCommodity.showEle("template_number,programa_picture,commodity_location");//skip,skip_input,
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			
			
			$("#selectPcCtrol").show();
			//选项商品显示项目
			$("#zw_f_commodity_number").parent().parent().show();
			$("#zw_f_good_number").parent().parent().show();
			$("#zw_f_commodity_name").parent().parent().show();
			//隐藏
			$("#zw_f_skip_input").parent().parent().hide();
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			$("#linkvalueDiv").hide();
			
			
		}else if(t_type == "449747500014"){//两栏多行（推荐） 2017-02-28 zhy
			//显示
			editCommodity.showEle("template_number,commodity_location,programa_picture,skip,skip_input,start_time,end_time,title,title_color,describes,describe_color");
			//隐藏
			editCommodity.hideEle("event_code,url,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,img,city_name,width,live_mobile,title_checked_color,preferential_desc,sub_template_number");			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();			
		}else if(t_type == "449747500015" || t_type == "449747500016"){	// 右两栏推荐 |左两栏推荐- Yangcl
			//显示
			editCommodity.showEle("template_number,commodity_location,programa_picture,skip,skip_input,start_time,end_time");
			//隐藏
			editCommodity.hideEle("event_code,url,good_number,commodity_number,template_desc,commodity_picture,is_dis,commodity_name,commodity_describe,coupon,create_time,dal_status,img,city_name,width,live_mobile,title_checked_color,preferential_desc,sub_template_number",false);
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		} else if(t_type == "449747500017"){//扫码购模版
			//显示
			editCommodity.showEle("template_number,commodity_location,good_number,commodity_number,commodity_name,start_time,end_time,commodity_describe,preferential_desc");
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,skip,skip_input,create_time,dal_status,coupon,width,programa_picture,img,city_name,live_mobile,template_desc,url,title,title_color,title_checked_color,describes,describe_color,sub_template_number",false);
			$("#linkvalueDiv").hide();
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
		} else if(t_type == "449747500018"){//页面定位模板 2017-02-24 zhy
			//隐藏
			editCommodity.hideEle("event_code,commodity_name,good_number,commodity_number,commodity_picture,is_dis,skip,commodity_describe,skip_input,programa_picture,create_time,dal_status,coupon,url,width,template_desc,img,city_name,live_mobile,describes,describe_color,preferential_desc",false);
			//显示
			/**
			 * sub_template_name=模板名称,sub_template_number=定位模板,commodity_location=标签位置,title=标签内容,title_color=标签内容颜色,title_checked_color=标签内容选中颜色
			 */
			editCommodity.showEle("template_number,commodity_location,sub_template_name,sub_template_number,start_time,end_time,title,title_color,title_checked_color");
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			
			//隐藏
			$("#zw_f_skip_input").parent().parent().hide();
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
		} else if(t_type == "449747500019"){//倒计时模板 2018-11-28 zhangb
			
			$("#zw_f_img").parent().prev().text("模板背景图 :");
			$("#zw_f_end_time").parent().prev().html("<span class='w_regex_need'>*</span>目标时间 :");
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			
		} else if(t_type == "449747500020"){//积分兑换模板
			//显示
			editCommodity.showEle("template_number,commodity_location,start_time,end_time,coupon");
			//隐藏
			editCommodity.hideEle("event_code,commodity_picture,is_dis,good_number,skip,commodity_name,commodity_number,commodity_describe,preferential_desc,skip_input,create_time,dal_status,width,programa_picture,img,city_name,live_mobile,template_desc,url,title,title_color,title_checked_color,describes,describe_color,sub_template_number");
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
			$('label[for="zw_f_coupon"]').html('<span class="w_regex_need">*</span>优惠券类型编号：');
		
		}else if(t_type == "449747500021"){//定位横滑模板 2019-07-04 zhangb
			//隐藏
			editCommodity.hideEle("event_code,commodity_name,good_number,commodity_number,commodity_picture,is_dis,skip,commodity_describe,skip_input,programa_picture,create_time,dal_status,coupon,url,width,template_desc,img,city_name,live_mobile,describes,describe_color,preferential_desc",false);
			//显示
			/**
			 * sub_template_name=模板名称,sub_template_number=定位模板,commodity_location=标签位置,title=标签内容,title_color=标签内容颜色,title_checked_color=标签内容选中颜色
			 */
			editCommodity.showEle("template_number,commodity_location,sub_template_name,sub_template_number,start_time,end_time,title,title_color,title_checked_color");
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			
		}else if(t_type == "449747500025"){// 精编商品模板
			//隐藏
			editCommodity.hideEle("event_code,programa_picture,is_dis,commodity_describe,commodity_number,coupon,url,width,template_desc,img,city_name,live_mobile,title_checked_color,preferential_desc,title,title_color,describes,describe_color,sub_template_number",false);
			//显示
			editCommodity.showEle("template_number,skip,commodity_location,commodity_picture,good_number,commodity_name,start_time,end_time");
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCommodity.beforeSubmit);
			editCommodity.changeType();
		} else {//模版二和模版三
			//隐藏
			if(t_type != "449747500022"){
				editCommodity.hideEle("event_code,commodity_number,skip,programa_picture,coupon,url,width,template_desc,skip_input,img,city_name,live_mobile,preferential_desc,title,title_color,title_checked_color,describes,describe_color,sub_template_number",false);
				$("#slId").parent().hide();
				//显示
				editCommodity.showEle("template_number,commodity_picture,commodity_location,is_dis,commodity_name,commodity_describe,good_number");
			}else{
				editCommodity.hideEle("event_code,skip,programa_picture,coupon,url,width,template_desc,skip_input,img,city_name,live_mobile,preferential_desc,title,title_color,title_checked_color,describes,describe_color,sub_template_number",false);
				$("#slId").parent().hide();
				//显示
				editCommodity.showEle("template_number,commodity_number,commodity_picture,commodity_location,is_dis,commodity_name,commodity_describe,good_number");
			}
			
		} 
	},
	hideEle:function(eleS,isCleanVal){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			//清空数值
			if(isCleanVal) {
				$("#zw_f_"+eleArr[i]).val("");
			}
			$("#zw_f_"+eleArr[i]).parent().parent().hide();
		}
	},
	showEle:function(eleS){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			$("#zw_f_"+eleArr[i]).parent().parent().show();
		}
	},
	show_windows : function(){
		zapjs.f.window_box({
			id : 'as_productlist_productids',
			content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.editCommodity.addcb" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	},
	addcb : function(sId, sVal,a,b,c){
		var obj = {};
		obj.product_code = sVal;
		$('#zw_f_good_number').val(sVal);
		$('#zw_f_commodity_name').val(a);
		$('#zw_f_commodity_describe').val(a);
		zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetSkuinfoByProductCode',obj,function(result) {
			if(result.resultCode==1){
				//设置值
				$('#zw_f_commodity_number').val(result.info.skuCode);
				$('#zw_f_sku_code').val(result.info.skuCode);
				zapjs.f.window_close(sId);
			}else{
				zapadmin.model_message('查询操作失败');
			}
		});
	},
	removeEle:function(eles){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			$("#zw_f_"+eleArr[i]).parent().parent().remove();
		}
	},
	changeType : function(){//根据打开方式进行切换
		$('label[for="zw_f_programa_picture"] > .w_regex_need').show();
		
		var linktype = $("#zw_f_skip").val();//所选试用类型的值
		if(linktype == "449747550001"){//URL
			//隐藏的项目
			editCommodity.hideEle("live_mobile,good_number,commodity_number,commodity_name,commodity_describe",true);
			$("#selectPcCtrol").hide();
			$("#linkvalueDiv").hide();
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			//显示
			editCommodity.showEle("skip_input");
			$("#zw_f_skip_input").show();
		}else if(linktype == "449747550002"){//商品详情
			$("#selectPcCtrol").show();
			//选项商品显示项目
			if($("#zw_f_template_type").val() == "449747500025"){
				// 精编商品模板:不展示SKU编号
				editCommodity.showEle("good_number,commodity_name");
			}else{				
				editCommodity.showEle("good_number,commodity_name");
			}
			//隐藏
			editCommodity.hideEle("skip_input,live_mobile",true);
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			$("#linkvalueDiv").hide();
			
			// 一行多栏（横滑） 打开方式为商品时非必填，默认可以去商品主图做栏目图
			if($("#zw_f_template_type").val() == "449747500010"||$("#zw_f_template_type").val() == "449747500011"){
				$('label[for="zw_f_programa_picture"] > .w_regex_need').hide();
			}
		}else if(linktype == "449747550003"){//关键词类型
			$("#selectPcCtrol").hide();
			//隐藏的项目
			editCommodity.hideEle("good_number,commodity_number,commodity_name,live_mobile,commodity_describe",true);
			$("#linkvalueDiv").hide();
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			//显示
			editCommodity.showEle("skip_input");
			$("#zw_f_skip_input").show();
		}else if(linktype == "449747550004"){//分类搜索
			//隐藏的项目
			editCommodity.hideEle("live_mobile,good_number,commodity_number,commodity_name,commodity_describe",true);
			$("#selectPcCtrol").hide();
			
			editCommodity.showEle("skip_input");
			$("#zw_f_skip_input").hide();
			if(editCommodity.isFirst) {
				$("#zw_f_skip_input").after('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#zw_f_showmore_linkvalue_text").text($("#zw_f_skip_input").val());
			} else {
				$("#zw_f_showmore_linkvalue_text").remove();
				$("#zw_f_skip_input").after('<span id="zw_f_showmore_linkvalue_text"></span>');
			}
			$("#linkvalueDiv").show();
		}else if(linktype == "449747550005") {//主播商店
			
			editCommodity.hideEle("skip_input,good_number,commodity_number,commodity_name,commodity_describe,commodity_picture",true);
			$("#linkvalueDiv").hide();
			$("#selectPcCtrol").hide();
			
			editCommodity.showEle("live_mobile");
		}else if(linktype == "449747550006") {//橙意卡
			//隐藏的项目
			editCommodity.hideEle("live_mobile,good_number,commodity_number,commodity_name,commodity_describe",true);
			$("#selectPcCtrol").hide();
			$("#linkvalueDiv").hide();
			$("#zw_f_showmore_linkvalue_text").hide();//展示分类选项
			//显示
			editCommodity.showEle("skip_input");
			$("#zw_f_skip_input").show();
		}
		editCommodity.isFirst = false;
	},
	//倒计时时间天时分秒拆解转换
	timeTransfer:function (n) {
		  return n >= 0 && n < 10 ? '0' + n : '' + n;
		 },
	beforeSubmit : function() {
		if($("#zw_f_skip").val() == "449747550004"){//连接类型为分类
			if($("#zw_f_showmore_linkvalue_text").text() == ""){
				zapjs.f.message('分类不能为空');
				return false;
			}
			$("#zw_f_skip_input").val($("#zw_f_showmore_linkvalue_text").text());
			$("input [name='zw_f_skip_input']").val($("#zw_f_showmore_linkvalue_text").text());
		}else if(($("#zw_f_template_type").val()=="449747500023"||$("#zw_f_template_type").val()=="449747500026")&&$("#zw_f_event_code").val() == ""){
			zapjs.f.message('活动编号不能为空');
			return false;
	}
		return true;
	},
	submit:function(obj){
		var type =$("#zw_f_template_type").val();
		var backpic = $("#zw_f_programa_picture").val();
		var width = $("#zw_f_width").val();
		var url = $("#zw_f_url").val();
		var img = $("#zw_f_img").val();
		var skip = $("#zw_f_skip").val();
		var liveMobie = $("#zw_f_live_mobile").val();
		var city = $("#zw_f_city_name").val();
		var start_time = $("#zw_f_start_time").val();
		var end_time = $("#zw_f_end_time").val();
		var commodity_number = $("#zw_f_commodity_number").val();
		var preferential_desc = $("#zw_f_preferential_desc").val();
		var title = $("#zw_f_title").val();
		var commodity_picture = $("#zw_f_commodity_picture").val();
		var is_dis = $("#zw_f_is_dis").val();
		var good_number = $("#zw_f_good_number").val();
		
		var sub_template_number = "";
		/*
		 * 获取关联的所有模板
		 */
		$("#zw_f_sub_template_number_show_ul li").each(function() {
			var t_html = $(this).find("span").eq(0).html();
			sub_template_number += t_html +",";
		});
		if(sub_template_number.length > 0) {
			sub_template_number = sub_template_number.substring(0,sub_template_number.length-1);
		}
		$("#zw_f_sub_template_number").val(sub_template_number);
		
		var errMs = "";
		var flg = false;
		if(type == "449747500001" || type == "449747500024" ){//轮播模版  或者  悬浮模板
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
		}  else if(type == "449747500023"||type == "449747500026"){//连接类型为分类
			if($("#zw_f_event_code").val() == ""||$("#zw_f_event_code").val() == null){
				errMs = "活动编号不能为空";
				flg = true;
			}
			
      	 }else if(type == "449747500002" || type == "449747500003" ) {
			if(commodity_number==null || commodity_number==""){
				errMs = "商品编号是必填项";
				flg = true;
			}
		} else if(type == "449747500006" ){//两栏广告
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
			if(flg == false &&(commodity_number==null || commodity_number=="")){
				errMs = "商品编号是必填项";
				flg = true;
			}
		} else if(type == "449747500007" ) {//优惠券模板 一行两栏
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
		} else if(type == "449747500008") {//优惠券模板  一行多栏
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
			if(flg == false &&(width==null || width=="")){
				errMs = "宽度是必填项";
				flg = true;
			}
		} else if(type == "449747500009"){//普通视频模板
			if(url==null || url==""){
				errMs = "视频连接是必填项";
				flg = true;
			}
			if(flg == false &&(commodity_number==null || commodity_number=="")){
				errMs = "商品编号是必填项";
				flg = true;
			}
		}else if(type == "449747500010"||type == "449747500011"){//一行多栏（横滑）
			/*	if(width==null || width==""){
					errMs = "宽度是必填项";
					flg = true;
				}*/
				var skip_input = $("#zw_f_skip_input").val();
				// 跳转类型不是商品时栏目图必填
				if("449747550002" != skip && flg == false &&(backpic==null || backpic=="")){
					errMs = "栏目图是必填项";
					flg = true;
				}
		}/*else if(type == "449747500011"){//一行三栏
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
			
		}*/else if(type == "449747500012"){//分类模版
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
			if(flg == false &&(url==null || url=="")){
				errMs = "连接是必填项";
				flg = true;
			}
			if(flg == false &&(img==null || img=="")){
				errMs = "图是必填项";
				flg = true;
			}
			var width = $("#zw_f_width").val();
			if(flg == false &&(width==null || width=="")) {
				errMs = "宽度是必填项";
				flg = true;
			}
			
		}else if(type == "449747500013"){//本地货模版
			if(url==null || url==""){
				errMs = "连接是必填项";
				flg = true;
			}
			if(flg == false &&(city==null || city=="")){
				errMs = "城市名称是必填项";
				flg = true;
			}
		} else if(type == "449747500014" ) {
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
		} else if(type == "449747500015" || type == "449747500016"){		 // 右两栏推荐 | 左两栏推荐 - Yangcl
			var a = $("#zw_f_commodity_location").val().replace(/(^\s+$)/g, "");  // 位置
			var b = $("#zw_f_title").val();   // 标题
//			var c = $("#zw_f_title_color").val();  // 标题字体颜色
			var d = $("#zw_f_describes").val();		// 描述
//			var e = $("#zw_f_describe_color").val();  // 描述字体颜色
			if(backpic==null || backpic==""){
				errMs = "栏目图是必填项";
				flg = true;
			}
			if(flg == false && isNaN(a)){
				errMs = "位置必须是数字";
				flg = true;
			}
			if(flg == false && (a==null || a=="")){
				errMs = "位置是必填项";
				flg = true;
			}
			if(flg == false && b!=null && b.length > 20){
				errMs = "标题超长!!!";
				flg = true;
			}
//			if(c==null || c==""){
//				errMs = "标题字体颜色是必填项";
//				flg = true;
//			}
			if(flg == false && d!=null && d.length > 200){
				errMs = "描述超长!!.";
				flg = true;
			}
//			if(e==null || e==""){
//				errMs = "描述字体颜色是必填项";
//				flg = true;
//			}
			
			$("#zw_f_commodity_location").val(a);  
		} else if(type == "449747500017"){//扫码购模版
			if(commodity_number==null || commodity_number==""){
				errMs = "商品编号是必选项";
				flg = true;
			}
			if(flg == false &&(preferential_desc==null || preferential_desc=="")){
				errMs = "优惠描述是必填项";
				flg = true;
			}
			if(flg == false && preferential_desc.length > 4) {
				errMs = "优惠描述最多4个字！";
				flg = true;
			}
		} else if (type == "449747500018"||type == "449747500021") {
			if(title==null || title==""){
				errMs = "标题是必填项";
				flg = true;
			}
			if(flg == false && (sub_template_number==null || sub_template_number=="") ) {
				errMs = "定位模板是必填项";
				flg = true;
			}
		}
		else if (type == "449747500019") {
			if(title==null || title==""){
				errMs = "标题是必填项";
				flg = true;
			}
			if(flg == false && (end_time==null || end_time=="") ) {
				errMs = "目标时间是必填项";
				flg = true;
			}
			if(flg == false && (end_time!=null && end_time!="") ) {
				var nowDate = new Date();
				var targetDate = new Date(end_time);
				var second = Math.floor((targetDate - nowDate) / 1000);//未来时间距离现在的秒数
				var day = Math.floor(second / 86400);//整数部分代表的是天；一天有24*60*60=86400秒 
				    second = second % 86400;//余数代表剩下的秒数
				var hour = Math.floor(second / 3600);//整数部分代表小时
				    second %= 3600; //余数代表 剩下的秒数
				var minute = Math.floor(second / 60);
				    second %= 60;
				if(editCommodity.timeTransfer(day)<=0&&editCommodity.timeTransfer(hour)<=0&&editCommodity.timeTransfer(minute)<=0&&editCommodity.timeTransfer(second)<=0){
					errMs = "目标时间是不能小于当前时间";
					flg = true;
				}
				else if(editCommodity.timeTransfer(day)>99){
					errMs = "目标时间距当前时间不能大于100天";
					flg = true;
				}
				
			}
		} else if (type == "449747500025") { // 精编商品模板
			if(is_dis == null || is_dis == ""){
			}else{
				$("#zw_f_is_dis").val("");
			}
			if(commodity_picture==null || commodity_picture==""){
				errMs = "商品图是必填项";
				flg = true;
			}
			// 如果是精编商品模板,不保存SKU编号
			$("#zw_f_commodity_number").val("");
		}
		if(flg == false &&(start_time == null || start_time.length<= 0)&& type != "449747500019"&& type != "449747500023"&& type != "449747500026") {
			errMs = "开始时间是必填项";
			flg = true;
		}
		if(flg == false &&(end_time == null || end_time.length<= 0)&& type != "449747500019"&& type != "449747500023"&& type != "449747500026") {
			errMs = "结束时间是必填项";
			flg = true;
		}
		
		//判断跳转方式的输入的必填项是否输入
		if(undefined != skip && "undefined" != skip && skip.length > 0) {
			
			if(type == "449747500001" || type == "449747500010" || type == "449747500011" || type == "449747500014" || type == "449747500015" || type == "449747500016" || type == "449747500025" || type == "449747500024") {
				var skip_input = $("#zw_f_skip_input").val();
				if("449747550001" == skip){//URL
					if(flg == false &&(skip_input == null || skip_input.length<= 0)) {
						errMs = "URL是必填项";
						flg = true;
					}
				} else if("449747550002" == skip) {//商品
					if(type == "449747500025"){
						// 精编商品模板校验商品编号						
						if(flg == false &&(good_number == null || good_number.length<= 0)) {
							errMs = "商品编号是必填项";
							flg = true;
						}
					}else{						
						if(flg == false &&(commodity_number == null || commodity_number.length<= 0)) {
							errMs = "商品编号是必填项";
							flg = true;
						}
					}
				} else if("449747550003" == skip) {//关键词
					if(flg == false &&(skip_input == null || skip_input.length<= 0)) {
						errMs = "关键词是必填项";
						flg = true;
					}
				} else if("449747550004" == skip) {//分类商品列表
					var textVal = $("#zw_f_showmore_linkvalue_text").text();
					$("#zw_f_skip_input").val(textVal);
					if(flg == false &&(textVal == null || textVal.length<= 0)) {
						errMs = "请选择分类";
						flg = true;
					}
				} else if("449747550005" == skip) {//如果跳转方式是主播商店，则添加主播手机号
					var regNum = /^1[0-9]{10}$/;
					if(flg == false && (liveMobie==null || liveMobie=="")){
						errMs = "主播手机号是必填项";
						flg = true;
					} else if(flg == false && null == liveMobie.match(regNum)) {
						errMs = "请填写正确的主播手机号";
						flg = true;
					}
					
				} 
			}
		}
		
		
		
		/*
		 * 校验上传的图片的大小是否大于1M（只允许上传1M一下的图片）
		 */
		var validatResult = editCommodity.validateImageSize();
		if(validatResult.length > 0) {
			flg = true;
			errMs = validatResult;
		}
		
		if(flg) {
			zapjs.f.message(errMs);
			return false;
		}
		zapjs.zw.func_add(obj);
		
	},
	//校验图片大小
	validateImageSize : function() {
		var commodity_picture = $("#zw_f_commodity_picture").val();
		var programa_picture = $("#zw_f_programa_picture").val();
		var img = $("#zw_f_img").val();
		if( undefined != commodity_picture && commodity_picture.length > 0 && undefined != zapweb_upload.imgSizeMap[commodity_picture]) {
			if(zapweb_upload.imgSizeMap[commodity_picture]/1024/1024 > 1) {//判断是否大于1M
				return "商品图片超1M,请重新上传";
			}
		}
		if( undefined != programa_picture && programa_picture.length > 0 && undefined != zapweb_upload.imgSizeMap[programa_picture] ) {
			if(zapweb_upload.imgSizeMap[programa_picture]/1024/1024 > 1) {//判断是否大于1M
				return "栏目图片超1M,请重新上传";
			}
		}
		if( undefined != img && img.length > 0 && undefined != zapweb_upload.imgSizeMap[img]  ) {
			if(zapweb_upload.imgSizeMap[img]/1024/1024 > 1) {//判断是否大于1M
				return "图片超1M,请重新上传";
			}
		}
		return "";
	}
	
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/editCommodity", function() {
		return editCommodity;
	});
}

$(document).ready(function(){
	//切换分类时调用
	$("#zw_f_skip").change(function(){
		editCommodity.changeType();
		
		$("#zw_f_commodity_number").val("");
		$("#zw_f_good_number").val("");
		$("#zw_f_commodity_name").val("");
	});
});
