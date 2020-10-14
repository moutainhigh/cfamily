var couponTypeLimitNew3 = {
		temp : {
			// 品牌信息
			brandJson : [],
			// 分类信息
			categoryJson : [],
			// 商品信息
			productJson : [],
			//优惠券类型限制信息
			limitJson : {},
			//商户限定
			sellerLimit : {},
			//支付方式先定
			paymentTypeLimit:{}
		},
		init : function(limitObj){
//			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',couponTypeLimitNew3.beforesubmit);
			if (!limitObj) {
				couponTypeLimitNew3.temp.sellerLimit = '449748230001';
			}else{
				couponTypeLimitNew3.temp.brandJson = limitObj.brandInfoList;
				couponTypeLimitNew3.temp.categoryJson = limitObj.categoryInfoList;
				couponTypeLimitNew3.temp.productJson = limitObj.productInfoList;
				couponTypeLimitNew3.temp.channeltJson = limitObj.channelCodeList;
				couponTypeLimitNew3.temp.limitJson = limitObj.couponTypeLimit;
				couponTypeLimitNew3.temp.sellerLimit = limitObj.sellerLimit;
				couponTypeLimitNew3.temp.allowedActivityTypeJson = limitObj.allowedActivityTypeList;
				couponTypeLimitNew3.temp.paymentTypeLimit = limitObj.paymentTypeLimit;
			}
//			console.info("list:"+limitObj.allowedActivityTypeList);
//			console.info("String:"+couponTypeLimitNew3.temp.allowedActivityTypeJson);
//			console.info("payment:"+limitObj.paymentTypeLimit);
			couponTypeLimitNew3.brandTable_init();
			couponTypeLimitNew3.categoryTable_init();
			couponTypeLimitNew3.productTable_init();
			couponTypeLimitNew3.channelSelect_init();
			
			
			couponTypeLimitNew3.initDate();
			couponTypeLimitNew3.activitySelect_init();
			couponTypeLimitNew3.greyCheckBoxsButton();
//			var productRows = $('#productTable').datagrid("getRows");
//			couponTypeLimitNew3.initProductSelect(productRows);
//			var categoryRows = $('#categoryTable').datagrid("getRows");
//			couponTypeLimitNew3.initCategorySelect(categoryRows);
//			var brandRows = $('#brandTable').datagrid("getRows");
//			couponTypeLimitNew3.initBrandSelect(brandRows);
		},
		initDate : function(){
			debugger;
			var zw_f_brand_limit = couponTypeLimitNew3.temp.limitJson.brandLimit;
			var zw_f_product_limit = couponTypeLimitNew3.temp.limitJson.productLimit;
			var zw_f_category_limit = couponTypeLimitNew3.temp.limitJson.categoryLimit;
			var zw_f_channel_limit = couponTypeLimitNew3.temp.limitJson.channelLimit;
			var zw_f_activity_limit = couponTypeLimitNew3.temp.limitJson.activityLimit;
			var zw_f_seller_limit = couponTypeLimitNew3.temp.sellerLimit;
			var zw_f_payment_type = couponTypeLimitNew3.temp.paymentTypeLimit;
			if (null != zw_f_brand_limit && "" != zw_f_brand_limit) {
				$("#zw_f_brand_limit").val(zw_f_brand_limit);
			}
			if (null != zw_f_product_limit && "" != zw_f_product_limit) {
				$("#zw_f_product_limit").val(zw_f_product_limit);
			}
			if (null != zw_f_category_limit && "" != zw_f_category_limit) {
				 $("#zw_f_category_limit").val(zw_f_category_limit);
			}
			if (null != zw_f_channel_limit && "" != zw_f_channel_limit) {
				 $("#zw_f_channel_limit").val(zw_f_channel_limit);
			}
			if (null != zw_f_activity_limit && "" != zw_f_activity_limit) {
				 $("#zw_f_activity_limit").val(zw_f_activity_limit);
			}
			if (null != zw_f_seller_limit && "" != zw_f_seller_limit) {
				$("#zw_f_seller_limit").val(zw_f_seller_limit);
			}
			if ("449748290001"== zw_f_payment_type) {
				$("#zw_f_payment_type").val("449748290001");
			}else if("449748290002"== zw_f_payment_type){
				$("#zw_f_payment_type").val("449748290002");
			}else{
				$("#zw_f_payment_type").val("449748290003");
			}
			 
			 $("#zw_f_brand_codes").val(couponTypeLimitNew3.temp.limitJson.brandCodes);
			 $("#zw_f_product_codes").val(couponTypeLimitNew3.temp.limitJson.productCodes);
			 $("#zw_f_category_codes").val(couponTypeLimitNew3.temp.limitJson.categoryCodes);
			 
			 if ("1" == couponTypeLimitNew3.temp.limitJson.exceptBrand) {
				 $("#zw_f_except_brand").attr("checked", true);
			 }
			 if ("1" == couponTypeLimitNew3.temp.limitJson.exceptProduct) {
				 $("#zw_f_except_product").attr("checked", true);
			 }
			 if ("1" == couponTypeLimitNew3.temp.limitJson.exceptCategory) {
				 $("#zw_f_except_category").attr("checked", true);
			 }
		},
		brandTable_init : function(){
			$('#brandTable').datagrid({
			    data:couponTypeLimitNew3.temp.brandJson,
			    columns:[[
					{field:'brandCode',title:'品牌编号',width:150,align:'center',sortable:false},
					{field:'brandZNName',title:'品牌中文名称',width:250,align:'center',sortable:false},
					{field:'brandUNName',title:'品牌英文名称',width:200,align:'center',sortable:false},
					{field:'opt', title:'操作',width:100,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="couponTypeLimitNew3.delBrandTableRow(\''+ index +'\')">删除</a> ';  
                    }  }
			    ]],
			    title:'品牌列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			    singleSelect:true
//			    toolbar: [{
//					text:'Add',
//					iconCls:'icon-add',
//					handler:function(){alert('add')}
//				},{
//					text:'Cut',
//					iconCls:'icon-cut',
//					handler:function(){alert('cut')}
//				},'-',{
//					text:'Save',
//					iconCls:'icon-save',
//					handler:function(){alert('save')}
//				}]
			});
		},
		categoryTable_init : function(){
			$('#categoryTable').datagrid({
			    data:couponTypeLimitNew3.temp.categoryJson,
			    columns:[[
					{field:'categoryCode',title:'分类编号',width:150,align:'center',sortable:false},
					{field:'categoryName',title:'分类名称',width:450,align:'center',sortable:false},
					{field:'opt', title:'操作',width:100,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="couponTypeLimitNew3.delCategoryTableRow(\''+ index +'\')">删除</a> ';  
                    }  }
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
			    data:couponTypeLimitNew3.temp.productJson,
			    columns:[[
					{field:'productCode',title:'商品编号',width:100,align:'center',sortable:false},
					{field:'productName',title:'商品名称',width:500,align:'center',sortable:false},
					{field:'opt', title:'操作',width:100,align:'center', formatter:function(value,rec,index){  
						return '<a href="javascript:void(0)" mce_href="#" onclick="couponTypeLimitNew3.delProductTableRow(\''+ index +'\')">删除</a> ';  
                    }}
			    ]],
			    title:'商品列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			    singleSelect:true
			});
		},
		channelSelect_init : function(){
			var channelCodes = couponTypeLimitNew3.temp.channeltJson;
			if (null != channelCodes && "" != channelCodes) {
				for (var i=0;i<channelCodes.length ;i++ ) 
				{ 
					$("input[name='zw_f_channel_codes'][value='"+channelCodes[i]+"']").attr('checked','true');
				}
			}
			
		},
		delProductTableRow : function(rowIndex){
			 $('#productTable').datagrid('deleteRow', rowIndex);
			 var rows = $('#productTable').datagrid("getRows");
			 couponTypeLimitNew3.initProductSelect(rows);
             //重新加载table
			 $('#productTable').datagrid("loadData", rows);
		},
		delCategoryTableRow : function(rowIndex){
			 $('#categoryTable').datagrid('deleteRow', rowIndex);
			 var rows = $('#categoryTable').datagrid("getRows");
			 couponTypeLimitNew3.initCategorySelect(rows);
           //重新加载table
			 $('#categoryTable').datagrid("loadData", rows);
		},
		delBrandTableRow : function(rowIndex){
			 $('#brandTable').datagrid('deleteRow', rowIndex);
			 var rows = $('#brandTable').datagrid("getRows");
			 couponTypeLimitNew3.initBrandSelect(rows);
           //重新加载table
			 $('#brandTable').datagrid("loadData", rows);
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
		initCategorySelect : function(rows){
			var categoryCodes = "";
			 for ( var int = 0; int < rows.length; int++) {
				 categoryCodes += rows[int].categoryCode+",";
			}
			if (categoryCodes.length > 0) {
				categoryCodes = categoryCodes.substr(0, categoryCodes.length - 1);
			}
			$('#zw_f_category_codes').val(categoryCodes);
		},
		initBrandSelect : function(rows){
			var brandCodes = "";
			 for ( var int = 0; int < rows.length; int++) {
				 brandCodes += rows[int].brandCode+",";
			}
			if (brandCodes.length > 0) {
				brandCodes = brandCodes.substr(0, brandCodes.length - 1);
			}
			$('#zw_f_brand_codes').val(brandCodes);
		},
		activitySelect_init : function(){
			var allowedActivityTypes = couponTypeLimitNew3.temp.allowedActivityTypeJson;
			if (null != allowedActivityTypes && "" != allowedActivityTypes) {
				for (var i=0;i<allowedActivityTypes.length ;i++ ) 
				{ 
					$("input[name='zw_f_allowed_activity_type'][value='"+allowedActivityTypes[i]+"']").attr('checked','true');
				}
			}
			var checkValue=$("#zw_f_activity_limit").val();
			if(checkValue == "449747110001"){//不可以参与活动
				$("input[name='zw_f_allowed_activity_type']").attr('disabled','disabled');//置灰
			}
			
		},
		greyCheckBoxsButton : function(){
			 $("#zw_f_activity_limit").change(function(){
				 var checkValue=$("#zw_f_activity_limit").val();
				 var aa = document.getElementsByName("zw_f_allowed_activity_type");
				 if(checkValue == "449747110001"){//不可以参与活动
					 $("input[name='zw_f_allowed_activity_type']").attr('disabled','disabled');
					 for (i = 0; i < aa.length; i++) {
				            aa[i].checked = false;
					    }
				 }else{
					 $("input[name='zw_f_allowed_activity_type']").removeAttr('disabled');
				    for (i = 0; i < aa.length; i++) {
			            aa[i].checked = true;
				    }
				 }
			 });
			
		}
//		beforesubmit : function(){
//			return true;
//		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/couponTypeLimitNew3", function() {
		return couponTypeLimitNew3;
	});
}
$(document).ready(function(){
	$("#selectCategoryBtn").click(function(){
		zapadmin.window_url('../show/page_ucategory_v_oc_coupon_type_new3?zw_f_category_codes='+$('#zw_f_category_codes').val());
	});
});