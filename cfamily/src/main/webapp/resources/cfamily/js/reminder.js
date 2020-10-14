var reminder = {
		temp : {
			// 商户信息
			sellerJson : []
		},
		init : function(type){
			//type值，1添加，2修改
			reminder.initSellerType();
			if (2 == type && ''!=$("#zw_f_seller_codes").val()) {
				reminder.loadEditSeller();
			}
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
					reminder.beforesubmit);
		},
		sellerTable_init : function(){
			$('#sellerTable').datagrid({
			    data:reminder.temp.sellerJson,
			    columns:[[
					{field:'smallSellerCode',title:'公司编号',width:150,align:'center',sortable:false},
					{field:'sellerName',title:'公司名称',width:350,align:'center',sortable:false},
					{field:'opt', title:'操作',width:150,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="reminder.delSellerTableRow(\''+ index +'\')">删除</a> ';  
                    }  }
			    ]],
			    title:'商户列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			    singleSelect:true
			});
		},
		delSellerTableRow : function(rowIndex){
			 $('#sellerTable').datagrid('deleteRow', rowIndex);
			 var rows = $('#sellerTable').datagrid("getRows");
			 reminder.initSellerSelect(rows);
           //重新加载table
			 $('#sellerTable').datagrid("loadData", rows);
		},
		initSellerSelect : function(rows){
			var sellerCodes = "";
			 for ( var int = 0; int < rows.length; int++) {
				 sellerCodes += rows[int].smallSellerCode+",";
			}
			if (sellerCodes.length > 0) {
				sellerCodes = sellerCodes.substr(0, sellerCodes.length - 1);
			}
			$('#zw_f_seller_codes').val(sellerCodes);
		},
		initSellerType : function(){
			var sellerType = $("#zw_f_seller_type").val();
			if ('4497471600260004' == sellerType) {
				$("#pointDiv").show();
				reminder.sellerTable_init();
			}else{
				$("#pointDiv").hide();
				
			}
		},
		loadEditSeller : function(){
			var obj = {};
			obj.smallSellerCodes = $("#zw_f_seller_codes").val();
			zapjs.zw.api_call('com_cmall_usercenter_service_api_ApiGetSellerName',obj,function(result) {
				if(result.resultCode==1){
					reminder.temp.sellerJson = result.sellerList;
					reminder.sellerTable_init();
				}else{
					zapadmin.model_message('添加商户失败');
				}
			});
		},
		// 提交前处理一些特殊的验证模式
		beforesubmit : function() {
			//展示界面的复选框判断非空有问题，需要在此写个特殊判断
			var selectFlag = false;
			$("form input[type='checkbox']").each(function(){
				if ($(this).is(':checked')) {
					selectFlag = true;
				}
			});
			if (!selectFlag) {
				zapjs.f.message("*展示界面：请至少选中一个！");
			}
			return selectFlag;
		}
		
};
$(document).ready(function(){
	$("#zw_f_seller_type").change(function(){
		reminder.initSellerType();
	});
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/reminder", function() {
		return reminder;
	});
}