var book_reminder = {
		temp : {
			// 商户信息
			sellerJson : []
		},
		init : function(){
			$("form textarea").attr("disabled", true);
			$("form select").attr("disabled", true);
			$("form input[type='checkbox']").attr("disabled", true);
			book_reminder.initSellerType();
			if (''!=$("#zw_f_seller_codes").val()) {
				book_reminder.loadEditSeller();
			}
		},
		sellerTable_init : function(){
			$('#sellerTable').datagrid({
			    data:book_reminder.temp.sellerJson,
			    columns:[[
					{field:'smallSellerCode',title:'公司编号',width:150,align:'center',sortable:false},
					{field:'sellerName',title:'公司名称',width:350,align:'center',sortable:false}
			    ]],
			    title:'商户列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			    singleSelect:true
			});
		},
		initSellerType : function(){
			var sellerType = $("#zw_f_seller_type").val();
			if ('4497471600260004' == sellerType) {
				$("#pointDiv").show();
				book_reminder.sellerTable_init();
			}else{
				$("#pointDiv").hide();
			}
		},
		loadEditSeller : function(){
			var obj = {};
			obj.smallSellerCodes = $("#zw_f_seller_codes").val();
			zapjs.zw.api_call('com_cmall_usercenter_service_api_ApiGetSellerName',obj,function(result) {
				if(result.resultCode==1){
					book_reminder.temp.sellerJson = result.sellerList;
					book_reminder.sellerTable_init();
				}else{
					zapadmin.model_message('添加商户失败');
				}
			});
			
			
		}
		
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/book_reminder", function() {
		return book_reminder;
	});
}

