var addProductForLabel = {
		temp : {
			// 商品信息
			productJson : [],
			limitJson : {}
		},
		init : function(limitObj){
//			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',addProductForLabel.beforesubmit);
			if (!limitObj) {
			}else{
				addProductForLabel.temp.productJson = limitObj.productInfoList;
				addProductForLabel.temp.limitJson = limitObj.productCodes;
			}
	
			addProductForLabel.productTable_init();
			
			addProductForLabel.initDate();

		},
		initDate : function(){
			
			 $("#zw_f_product_codes").val(addProductForLabel.temp.limitJson);
			
		},
		
		productTable_init : function(){
			$('#productTable').datagrid({
			    data:addProductForLabel.temp.productJson,
			    columns:[[
					{field:'productCode',title:'商品编号',width:100,align:'center',sortable:false},
					{field:'productName',title:'商品名称',width:500,align:'center',sortable:false},
					{field:'opt', title:'操作',width:100,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="addProductForLabel.delProductTableRow(\''+ index +'\')">删除</a> ';  
                    }}
			    ]],
			    title:'商品列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			    singleSelect:true
			});
		},
		
		delProductTableRow : function(rowIndex){
			 $('#productTable').datagrid('deleteRow', rowIndex);
			 var rows = $('#productTable').datagrid("getRows");
			 addProductForLabel.initProductSelect(rows);
             //重新加载table
			 $('#productTable').datagrid("loadData", rows);
		},
		
		initProductSelect : function(rows){
			 var productCodes = "";
			 for ( var int = 0; int < rows.length; int++) {
				 productCodes += rows[int].productCode+",";
			}
			if (productCodes.length > 0) {
				productCodes = productCodes.substr(0, productCodes.length - 1);
			}
			$('#zw_f_product_codes').val(productCodes);
		},

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addProductForLabel", function() {
		return addProductForLabel;
	});
}