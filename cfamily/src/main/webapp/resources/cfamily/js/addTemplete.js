var addTemplete = {
	init : function(){
		
		//字段初始化
		{
			//隐藏
			addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
			//显示
			addTemplete.showEle("template_backcolor");
		}
		
		
		
		$("#zw_f_template_type").change(function(){
			//获取模版类型编号
			var val = $(this).val();
			if(val == "449747500001" || val == "449747500014" || val == "449747500017" || val == "449747500018" || val == "449747500025"){
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			} else if(val == "449747500002") {
				//隐藏
				addTemplete.hideEle("skip,commodity_text_color,commodity_picture,template_title_name,commodity_min_dis,coupon,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("commodity_text_value,commodity_text_picture,template_backcolor,commodity_buy_picture,commodity_text_pic,sell_price_color");
				//初始化  商品文本背景类型
				{
					$("#zw_f_commodity_text_picture").val("449747520001");
					$("#zw_f_commodity_text_pic").parent().parent().show();
					$("#zw_f_commodity_text_value").parent().parent().hide();
				}
			} else if(val == "449747500003") {
				//隐藏
				addTemplete.hideEle("commodity_text_picture,skip,commodity_picture,commodity_text_value,commodity_text_pic,template_title_name,commodity_min_dis,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor,commodity_buy_picture,commodity_text_color");
			} else if(val == "449747500004") {//视频模板
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_text_value,commodity_text_color,commodity_text_pic,template_backcolor,template_title_name,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("commodity_picture");
			} else if(val == "449747500005") {//标题模板
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_text_value,commodity_text_color,commodity_text_pic,template_backcolor,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_title_name,commodity_picture");
			} else if(val == "449747500006"||val == "449747500019") {//两栏广告&倒计时模板
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			} else if(val == "449747500007" || val == "449747500008") {//优惠券 
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor,coupon");
			}else if(val == "449747500009") {//普通视频模板
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			}else if(val == "449747500010") {//一行多栏（横滑）
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor,column_num");
			}else if(val == "449747500011") {//一行三栏
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code");
				//显示
				addTemplete.showEle("template_backcolor,split_bar");
				$("#zw_f_split_bar").val('449748410001');
			}else if(val == "449747500012") {//分类模版
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			}else if(val == "449747500013") {//本地货模版
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			}else if(val == "449747500015") {//右两栏
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code");
				//显示
				addTemplete.showEle("template_backcolor,split_bar");
				$("#zw_f_split_bar").val('449748410001');
			}else if(val == "449747500016") {//左两栏
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,coupon,sell_price_color,template_title_color,template_title_color_selected,template_backcolor_selected,column_num,activity_code,event_code");
				//显示
				addTemplete.showEle("template_backcolor,split_bar");
				$("#zw_f_split_bar").val('449748410001');
			}else if(val == "449747500020") {//积分兑换模板
				//隐藏
				addTemplete.hideEle("split_bar");
			}else if(val == "449747500021") {//本地货模版
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,activity_code,column_num,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor,template_title_color,template_title_color_selected,template_backcolor_selected");
			}else if(val == "449747500022") {//兑换商品模板
				//隐藏
				addTemplete.hideEle("commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_backcolor_selected,template_title_color,template_title_color_selected,column_num,event_code,split_bar");
				//显示
				addTemplete.showEle("template_backcolor,commodity_picture,remarks,activity_code");
			}else if(val == "449747500023"||val == "449747500026") {//拼团模版
				//隐藏
				addTemplete.hideEle("event_code,commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_picture,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_backcolor_selected,template_title_color,template_title_color_selected,activity_code,column_num,split_bar");
				//显示
				addTemplete.showEle("template_backcolor");
			}else if(val == "449747500024") {//悬浮模板
				//隐藏
				addTemplete.hideEle("commodity_picture,commodity_buy_picture,commodity_text_picture,commodity_min_dis,skip,commodity_text_value,commodity_text_color,commodity_text_pic,template_title_name,sell_price_color,template_backcolor_selected,template_title_color,template_title_color_selected,activity_code,column_num,template_backcolor,event_code,split_bar");
				//显示
				addTemplete.showEle("");
			}
		});
		
		//选择背景类型
		$("#zw_f_commodity_text_picture").change(function(){
			var val = $(this).val();
			if(val == "449747520001"){
				$("#zw_f_commodity_text_pic").parent().parent().show();
				$("#zw_f_commodity_text_value").parent().parent().hide();
			} else if(val == "449747520002") {
				$("#zw_f_commodity_text_value").parent().parent().show();
				$("#zw_f_commodity_text_pic").parent().parent().hide();
			}
		});
	},
	hideEle:function(eleS){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			//清空数值
			$("#zw_f_"+eleArr[i]).val("");
			$("#zw_f_"+eleArr[i]).parent().parent().hide();
		}
	},
	showEle:function(eleS){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			$("#zw_f_"+eleArr[i]).parent().parent().show();
		}
	},
	submit:function(obj){
		var type =$("#zw_f_template_type").val();
		var backcolor = $("#zw_f_template_backcolor").val();
		var errMs = "";
		var flg = false;
		
		if(type != "449747500004" && type != "449747500005" && type != "449747500024") {//除直播视频模板外，其他视频的背景色都是必填项(悬浮模板背景色不是必填项)
			if(backcolor==null || backcolor==""){
				errMs = "模版背景色是必填项";
				flg = true;
			}
		}
		
		if(type == "449747500002") {
			if($("#zw_f_commodity_text_picture").val()==null || $("#zw_f_commodity_text_picture").val()==""){
				errMs = "商品文本背景类型是必填项";
				flg = true;
			}
		} else if(type == "449747500005") {
			if($("#zw_f_template_title_name").val()==null || $("#zw_f_template_title_name").val()==""){
				errMs = "标题名称是必填项";
				flg = true;
			}
		}else if(type == "449747500010") {
				if($("#zw_f_column_num").val()==null || $("#zw_f_column_num").val()==""){
					errMs = "多栏数量是必填项";
					flg = true;
				}
		}else if(type == "449747500021") {
			if($("#zw_f_template_backcolor").val()==null || $("#zw_f_template_backcolor").val()==""){
				errMs = "模版背景色是必填项";
				flg = true;
			}
	    }else if(type == "449747500021") {
			if($("#zw_f_template_backcolor_selected").val()==null || $("#zw_f_template_backcolor_selected").val()==""){
				errMs = "模板选中色是必填项";
				flg = true;
			}
	    }else if(type == "449747500021") {
			if($("#zw_f_template_title_color").val()==null || $("#zw_f_template_title_color").val()==""){
				errMs = "标题颜色是必填项";
				flg = true;
			}
	    }else if(type == "449747500021") {
			if($("#zw_f_template_title_color_selected").val()==null || $("#zw_f_template_title_color_selected").val()==""){
				errMs = "标题选中色是必填项";
				flg = true;
			}
		} /*else if (type == "449747500023"){ // 拼团模板
			if($("#zw_f_event_code").val()==null || $("#zw_f_event_code").val()==""){
				errMs = "请填写活动编号";
				flg = true;
			}
		}*/
		
		/*
		 * 校验上传的图片的大小是否大于1M（只允许上传1M一下的图片）
		 */
		var validatResult = addTemplete.validateImageSize();
		if(validatResult.length > 0) {
			flg = true;
			errMs = validatResult;
		}
		
		if(flg) {
			zapjs.f.message(errMs);
			return false;
		}
		
		zapjs.zw.func_add(obj);
		
//		zapjs.f.message("sda");
//		return false;
	},
	//校验图片大小
	validateImageSize : function() {
		var commodity_buy_picture = $("#zw_f_commodity_buy_picture").val();
		var commodity_picture = $("#zw_f_commodity_picture").val();
		var commodity_text_pic = $("#zw_f_commodity_text_pic").val();
		if(undefined != commodity_buy_picture && undefined != zapweb_upload.imgSizeMap[commodity_buy_picture] && commodity_buy_picture.length > 0 ) {
			if(zapweb_upload.imgSizeMap[commodity_buy_picture]/1024/1024 > 1) {//判断是否大于1M
				return "商品购买方式的图片超1M,请重新上传";
			}
		}
		if(undefined != commodity_picture && undefined != zapweb_upload.imgSizeMap[commodity_picture] && commodity_picture.length > 0 ) {
			if(zapweb_upload.imgSizeMap[commodity_picture]/1024/1024 > 1) {//判断是否大于1M
				return "模版图片超1M,请重新上传";
			}
		}
		if(undefined != commodity_text_pic && undefined != zapweb_upload.imgSizeMap[commodity_text_pic] && commodity_text_pic.length > 0 ) {
			if(zapweb_upload.imgSizeMap[commodity_text_pic]/1024/1024 > 1) {//判断是否大于1M
				return "商品文本背景图片超1M,请重新上传";
			}
		}
		return "";
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addTemplete", function() {
		return addTemplete;
	});
}