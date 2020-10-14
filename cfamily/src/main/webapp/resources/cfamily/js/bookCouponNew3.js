var bookCouponNew3 = {
	temp : {
		// 品牌信息
		brandJson : [],
		// 分类信息
		categoryJson : [],
		// 商品信息
		productJson : [],
		//优惠券类型限制信息
		limitJson : {}
	},
	init : function(limitObj){
		bookCouponNew3.initValidType();
		bookCouponNew3.initSkpType();
		bookCouponNew3.initLimit();
		bookCouponNew3.initCouponType();
//		bookCouponNew3.initLimitCondition();
		
		$("form input").attr("disabled", true);
		$("form select").attr("disabled", true);
		$("form textarea").attr("disabled", true);
		
		if (!limitObj) {
		}else{
			bookCouponNew3.temp.brandJson = limitObj.brandInfoList;
			bookCouponNew3.temp.categoryJson = limitObj.categoryInfoList;
			bookCouponNew3.temp.productJson = limitObj.productInfoList;
			bookCouponNew3.temp.channeltJson = limitObj.channelCodeList;
			bookCouponNew3.temp.limitJson = limitObj.couponTypeLimit;
			bookCouponNew3.temp.allowedActivityTypeJson = limitObj.allowedActivityTypeList;
		}
		console.info("list:"+limitObj.allowedActivityTypeList);
		console.info("String:"+bookCouponNew3.temp.allowedActivityTypeJson);
		bookCouponNew3.brandTable_init();
		bookCouponNew3.categoryTable_init();
		bookCouponNew3.productTable_init();
		bookCouponNew3.channelSelect_init();
		bookCouponNew3.activitySelect_init();

	},
	initValidType:function(){
		//有效类型为天数时，有效天数字段显示，开始结束时间隐藏
		if($("#zw_f_valid_type").val() == "4497471600080001"){
			$("#zw_f_valid_day").parent().parent().show();
			$("#zw_f_start_time").parent().parent().hide();
			$("#zw_f_end_time").parent().parent().hide();
		}else{
			$("#zw_f_valid_day").parent().parent().hide();
			$("#zw_f_start_time").parent().parent().show();
			$("#zw_f_end_time").parent().parent().show();
		}
	},
	initSkpType : function(){
		//跳转类型为URL时，跳转链接字段显示，否则隐藏
		if($("#zw_f_action_type").val() == "4497471600280002"){
			$("#zw_f_action_value").parent().parent().show();
		}else{
			$("#zw_f_action_value").parent().parent().hide();
		}
	},
	initCouponType : function(){
		//优惠券类型为金额券时，优惠券面额字段显示，否则隐藏
		if($("#zw_f_money_type").val() == "449748120001" || $("#zw_f_money_type").val() == "449748120003"){
			$("#zw_f_money").parent().parent().show();				
			$("#zw_f_discount").parent().parent().hide();
			
			$("#zw_f_surplus_money").val(Math.round(parseInt($("#zw_f_surplus_money").val()) / parseInt($("#zw_f_money").val())));
			$("#zw_f_privide_money").val(Math.round(parseInt($("#zw_f_privide_money").val()) / parseInt($("#zw_f_money").val())));
		}else{
			$("#zw_f_money").parent().parent().hide();				
			$("#zw_f_discount").parent().parent().show();
			
			//已发放金额显示为XX张
			//$("#zw_f_privide_money").parent().children("span:first").html("<span>张<span>");
			//剩余金额显示为空
			//$("#zw_f_surplus_money").val(null);
		}
	},
	initLimit:function(){
		if($("#zw_f_limit_condition").val() == "4497471600070001"){
			$("#limitCondition").hide();
		}
	},
	brandTable_init : function(){
		$('#brandTable').datagrid({
		    data:bookCouponNew3.temp.brandJson,
		    columns:[[
				{field:'brandCode',title:'品牌编号',width:150,align:'center',sortable:false},
				{field:'brandZNName',title:'品牌中文名称',width:300,align:'center',sortable:false},
				{field:'brandUNName',title:'品牌英文名称',width:250,align:'center',sortable:false},
		    ]],
		    title:'品牌列表',
		    width:750,
		    height:305,
		    rownumbers:true,			//设置为 true，则显示带有行号的列。
		    singleSelect:true
		});
	},
	categoryTable_init : function(){
		$('#categoryTable').datagrid({
		    data:bookCouponNew3.temp.categoryJson,
		    columns:[[
				{field:'categoryCode',title:'分类编号',width:150,align:'center',sortable:false},
				{field:'categoryName',title:'分类名称',width:550,align:'center',sortable:false},
		    ]],
		    title:'分类列表',
		    width:750,
		    height:305,
		    rownumbers:true,			//设置为 true，则显示带有行号的列。
		    singleSelect:true
		});
	},
	productTable_init : function(){
		$('#productTable').datagrid({
		    data:bookCouponNew3.temp.productJson,
		    columns:[[
				{field:'productCode',title:'商品编号',width:150,align:'center',sortable:false},
				{field:'productName',title:'商品名称',width:550,align:'center',sortable:false},
		    ]],
		    title:'商品列表',
		    width:750,
		    height:305,
		    rownumbers:true,			//设置为 true，则显示带有行号的列。
		    singleSelect:true
		});
	},
	channelSelect_init : function(){
		var channelCodes = bookCouponNew3.temp.channeltJson;
		if (null != channelCodes && "" != channelCodes) {
			for (var i=0;i<channelCodes.length ;i++ ) 
			{ 
				$("input[name='zw_f_channel_codes'][value='"+channelCodes[i]+"']").attr('checked','true');
			}
		}
		
	},
	activitySelect_init : function(){
		var allowedActivityTypes = bookCouponNew3.temp.allowedActivityTypeJson;
		debugger;
		if (null != allowedActivityTypes && "" != allowedActivityTypes) {
			for (var i=0;i<allowedActivityTypes.length ;i++ ) 
			{ 
				$("input[name='zw_f_allowed_activity_type_limits'][value='"+allowedActivityTypes[i]+"']").attr('checked','true');
			}
		}
		
	}
//	initLimitCondition : function(){
		//限制条件为指定时，显示使用范围与使用说明
//		if($("#zw_f_limit_condition").val() == "4497471600070002"){
//			$("#zw_f_limit_scope").parent().parent().show();
//			$("#zw_f_limit_explain").parent().parent().show();
//			$("#zw_f_limit_scope").attr("zapweb_attr_regex_id","469923180002");
//		}else{
//			$("#zw_f_limit_scope").parent().parent().hide();
//			$("#zw_f_limit_explain").parent().parent().hide();
//			$("#zw_f_limit_scope").removeAttr("zapweb_attr_regex_id");
//		}
//	}
	
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/bookCouponNew3", function() {
		return bookCouponNew3;
	});
}

