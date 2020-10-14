var msgpay ={
	plusData : {
		product_codes: ""
	},
	productCodeList: [],
	init:function (plusData){
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',msgpay.beforesubmit);
		this.plusData = plusData;
		
		this.productCodeList = this.plusData.product_codes.split(',')
		
		this.refreshProductTable();
	},
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		msgpay.plusData.product_codes = msgpay.productCodeList.join(',');
		
		$('#zw_f_product_codes').val(msgpay.plusData.product_codes);
		
		return true;
	},
	showImportProductbox: function() {
		zapjs.f.window_box({
			id : 'importProductShowBox',
			content : '<iframe src="../page/page_batch_upload_product?callback=parent.msgpay.importProductCompleted&xls=chengyika_product.xls" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '780',
			height : '550'
		});		
	},
	importProductCompleted: function(data) {
		for(var i = 0; i< data.resultList.length; i++) {
			if(msgpay.productCodeList.indexOf(data.resultList[i]) == -1) {
				msgpay.productCodeList.push(data.resultList[i]);
			}
		}
		msgpay.plusData.product_codes = msgpay.productCodeList.join(',')
		msgpay.refreshProductTable();
	},	
	showShooseProductbox: function() {
		zapjs.f.window_box({
			id : 'chooseProductShowBox',
			content : '<iframe src="../show/page_chart_choose_product_cfamily?zw_s_iframe_choose_callback=parent.msgpay.chooseProductCompleted&zw_s_iframe_choosed_values=" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '780',
			height : '550'
		});		
	},
	chooseProductCompleted: function(data) {
		var objList = jQuery.parseJSON(data);
		
		for(var i = 0;i < objList.length;i++) {
			if(this.productCodeList.indexOf(objList[i]) < 0) {
				this.productCodeList.push(objList[i])
			}
		}
		
		this.plusData.product_codes = this.productCodeList.join(',')
		this.refreshProductTable();
	},
	//刷新商品列表
	refreshProductTable : function(){
		var obj = {};
		obj.productCode = this.plusData.product_codes;
		obj.subsidy = this.plusData.product_codes; // 兼容后台接口
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForProductInfo',obj,function(result) {
			if(result.resultCode != 1){
				zapadmin.model_message('查询商品失败');
				return;
			}
			
			$('#productTable').datagrid({
			    data:result.skuInfoList,
			    columns:[[
			        {field:'check',title:'商品编号',width:50,checkbox:true},  
					{field:'skuCode',title:'商品编号',width:100,align:'center',sortable:false},
					{field:'skuName',title:'商品名称',width:500,align:'center',sortable:false},
					{field:'opt', title:'操作',width:50,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="msgpay.delTableRow(\'productTable\',\''+ index +'\')">删除</a> ';  
	                }}
			    ]],
			    title:'商品列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			});
		});
	},
	delTableRow : function(table,rowIndex){
		var rows = $('#'+table).datagrid('getRows');
		var rowData = rows[rowIndex];
		$('#'+table).datagrid('deleteRow', rowIndex);
		
		if(table == 'productTable') {
			var delIndex = this.productCodeList.indexOf(rowData['skuCode'])
			if(delIndex > -1) {
				this.productCodeList.splice(delIndex,1)
				this.plusData.product_codes = this.productCodeList.join(',');
			}
		}
		msgpay.refreshProductTable();
	},
	delSelectedTableRow : function(table){
		var checkedList = $('#'+table).datagrid('getChecked');
		if(checkedList.length == 0) return
		
		var rows = $('#'+table).datagrid('getRows');
		for(var i = 0; i<checkedList.length; i++) {
			var index = -1;
			for(var j = 0; j < rows.length; j++) {
				if(table == 'productTable' && checkedList[i]['skuCode'] == rows[j]['skuCode']) {
					index = j
					break;
				}
				
				if(table == 'categoryTable' && checkedList[i]['categoryCode'] == rows[j]['categoryCode']) {
					index = j
					break;
				}
			}
			if(index >= 0) {
				rows.splice(j,1)
			}
		}
		if(table == 'productTable') {
			this.productCodeList = [];
			for(var j = 0; j < rows.length; j++) {
				this.productCodeList.push(rows[j]['skuCode'])
			}
			this.plusData.product_codes = this.productCodeList.join(',');
			msgpay.refreshProductTable();
		}
	},
	onProductLimitChange: function(){
		if("4497476400020001" == $('#zw_f_product_limit').val()) {
			msgpay.plusData.product_codes = '';
			msgpay.plusData.productCodeList = [];
			msgpay.refreshProductTable();
		}
	},
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/msgpay", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return msgpay;
	});
}