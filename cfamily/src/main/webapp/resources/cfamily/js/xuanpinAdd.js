var xuanpinAdd = {
	plusData : {
		categoryCodes: [],
		brandCodes: [],
	},
	init : function() {
		// 默认先隐藏的字段
		$('#categoryTable_panel').hide();
		$('label[for="zw_f_uc_seller_type_codes"]').parent().hide();
		$('label[for="zw_f_small_seller_codes"]').parent().hide();
		$('label[for="zw_f_sell_price_range"]').parent().hide();
		$('label[for="zw_f_product_stock_range"]').parent().hide();
		$('#brandTable_panel').hide();
		$('label[for="zw_f_comment_num"]').parent().hide();
		$('label[for="zw_f_comment_good_num"]').parent().hide();
		$('label[for="zw_f_rebuy_rate"]').parent().hide();
		
		$('#zw_f_category_limit').change(this.onCategoryLimitChange);
		$('#zw_f_uc_seller_type_limit').change(this.onSellerTypeLimitChange);
		$('#zw_f_small_seller_limit').change(this.onSmallSellerLimitChange);
		$('#zw_f_sell_price_limit').change(this.onSellPriceLimitChange);
		$('#zw_f_product_stock_limit').change(this.onProductStockLimitChange);
		
		$('#zw_f_brand_limit').change(this.onBrandLimitChange);
		$('#zw_f_comment_limit').change(this.onCommentLimitChange);
		$('#zw_f_comment_good_limit').change(this.onCommentGoodLimitChange);
		$('#zw_f_rebuy_rate_limit').change(this.onRebuyRateLimitChange);
		
		this.refreshBrandTable();
		this.refreshCategoryTable();
		
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', this.beforeSubmit);
	},
	onRebuyRateLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_rebuy_rate"]').parent().show();
		} else {
			$('label[for="zw_f_rebuy_rate"]').parent().hide();
		}
	},		
	onCommentGoodLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_comment_good_num"]').parent().show();
		} else {
			$('label[for="zw_f_comment_good_num"]').parent().hide();
		}
	},		
	onCommentLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_comment_num"]').parent().show();
		} else {
			$('label[for="zw_f_comment_num"]').parent().hide();
		}
	},		
	onBrandLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('#brandTable_panel').show();
		} else {
			$('#brandTable_panel').hide();
		}
	},	
	onCategoryLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('#categoryTable_panel').show();
		} else {
			$('#categoryTable_panel').hide();
		}
	},
	onSellerTypeLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_uc_seller_type_codes"]').parent().show();
		} else {
			$('label[for="zw_f_uc_seller_type_codes"]').parent().hide();
		}
	},
	onSmallSellerLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_small_seller_codes"]').parent().show();
		} else {
			$('label[for="zw_f_small_seller_codes"]').parent().hide();
		}
	},
	onSellPriceLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_sell_price_range"]').parent().show();
		} else {
			$('label[for="zw_f_sell_price_range"]').parent().hide();
		}
	},	
	onProductStockLimitChange: function() {
		if($(this).val() == '4497471600070002'){
			$('label[for="zw_f_product_stock_range"]').parent().show();
		} else {
			$('label[for="zw_f_product_stock_range"]').parent().hide();
		}
	},	
	onClickProductCount: function(){
		if($('#btnShowProductCount').val().indexOf('查询中') != -1){
			return;
		}
		
		if(!this.beforeSubmit()){
			return;
		}
		
		$('#btnShowProductCount').val('查询中...');
		
		var param = {};
		param['zw_f_category_limit'] = $('#zw_f_category_limit').val();
		param['zw_f_category_codes'] = $('#zw_f_category_codes').val();
		param['zw_f_uc_seller_type_limit'] = $('#zw_f_uc_seller_type_limit').val();
		param['zw_f_uc_seller_type_codes'] = $('#zw_f_uc_seller_type_codes').val();
		param['zw_f_small_seller_limit'] = $('#zw_f_small_seller_limit').val();
		param['zw_f_small_seller_codes'] = $('#zw_f_small_seller_codes').val();
		param['zw_f_sell_price_limit'] = $('#zw_f_sell_price_limit').val();
		param['zw_f_sell_price_range'] = $('#zw_f_sell_price_range').val();
		param['zw_f_product_status_limit'] = $('#zw_f_product_status_limit').val();
		param['zw_f_product_stock_limit'] = $('#zw_f_product_stock_limit').val();
		param['zw_f_product_stock_range'] = $('#zw_f_product_stock_range').val();
		param['zw_f_product_create_time_limit'] = $('#zw_f_product_create_time_limit').val();
		param['zw_f_medical_flag'] = $('#zw_f_medical_flag').val();
		param['zw_f_brand_limit'] = $('#zw_f_brand_limit').val();
		param['zw_f_brand_codes'] = $('#zw_f_brand_codes').val();
		param['zw_f_comment_limit'] = $('#zw_f_comment_limit').val();
		param['zw_f_comment_num'] = $('#zw_f_comment_num').val();
		param['zw_f_comment_good_limit'] = $('#zw_f_comment_good_limit').val();
		param['zw_f_comment_good_num'] = $('#zw_f_comment_good_num').val();
		param['zw_f_rebuy_rate_limit'] = $('#zw_f_rebuy_rate_limit').val();
		param['zw_f_rebuy_rate'] = $('#zw_f_rebuy_rate').val();
		
		zapjs.zw.modal_process();
		zapjs.f.ajaxjson('../func/'+$('#btnShowProductCount').attr('zapweb_attr_operate_id'), param, function(data){
			zapjs.f.modal_close();
			if(data.resultCode == 1) {
				$('#btnShowProductCount').val('查询商品数量')
				$('#showProductCount').text("总数量："+data.resultMessage);
				zapjs.f.message("查询成功，数量："+data.resultMessage);
			} else {
				zapjs.f.message(data.resultMessage);
			}
		});		
	},
	showChooseCategoryBox: function(){
		zapjs.f.window_box({
			id : 'chooseCategoryShowBox',
			content : '<iframe src="../show/page_chart_choose_category_cf?zw_f_category_code='+xuanpinAdd.plusData.categoryCodes.join(',')+'&zw_s_iframe_choose_callback=parent.xuanpinAdd.chooseCategoryCompleted&zw_f_maxLevel=3" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '780',
			height : '580'
		});			
	},
	chooseCategoryCompleted: function(data) {
		var objList = jQuery.parseJSON(data);
		xuanpinAdd.plusData.categoryCodes = objList
		xuanpinAdd.refreshCategoryTable();
	},	
	//刷新分类列表
	refreshCategoryTable : function(){
		var obj = {};
		obj.categoryCodeArr = xuanpinAdd.plusData.categoryCodes;
		obj.sellerCode="SI2003"
		
		// 兼容无分类的情况，默认写一个不存在的编号
		if(obj.categoryCodeArr.length == 0) {
			obj.categoryCodeArr = ['1']
		}
		
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForCouponLimitCategoryBaseInfo',obj,function(result) {
			if(result.resultCode!=1){
				zapadmin.model_message('添加分类失败');
				return;
			}
			
			$('#categoryTable').datagrid({
			    data:result.categoryInfoList,
			    columns:[[
			            {field:'check',title:'编号',width:50,checkbox:true},  
						{field:'categoryCode',title:'分类编号',width:100,align:'center',sortable:false},
						{field:'categoryName',title:'分类名称',width:450,align:'center',sortable:false},
						{field:'opt', title:'操作',width:50,align:'center', formatter:function(value,rec,index){  
							return '<a href="javascript:void(0)" mce_href="#" onclick="xuanpinAdd.delTableRow(\'categoryTable\',\''+ index +'\')">删除</a> ';  
		                }}
			    ]],
			    title:'分类列表',
			    width:750,
			    height:305,
			    rownumbers:true,			//设置为 true，则显示带有行号的列。
			});	
		});		
	},	
	showChooseBrandBox: function(){
		zapjs.f.window_box({
			id : 'chooseItemShowBox',
			content : '<iframe src="../show/page_chart_choose_item_cf?zw_s_iframe_page_code=page_chart_v_pc_brandinfo_for_select&zw_s_iframe_choosed_values='+xuanpinAdd.plusData.brandCodes.join(',')+'&zw_s_iframe_choose_callback=parent.xuanpinAdd.chooseBrandCompleted" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '780',
			height : '550'
		});			
	},	
	chooseBrandCompleted: function(data) {
		var objList = jQuery.parseJSON(data);
		xuanpinAdd.plusData.brandCodes = objList
		xuanpinAdd.refreshBrandTable();
	},	
	//刷新品牌列表
	refreshBrandTable : function(){
		var obj = {};
		obj.brandCodeArr = xuanpinAdd.plusData.brandCodes.join(',');
			
		// 兼容无数据的情况，默认写一个不存在的编号
		if(obj.brandCodeArr.length == 0) {
			obj.brandCodeArr = ['1']
		}
			
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForCouponLimitBrandBaseInfo',obj,function(result) {
			if(result.resultCode!=1){
				zapadmin.model_message('添加品牌失败');
				return;
			}
			
			$('#brandTable').datagrid({
			    data:result.brandInfoList,
			    columns:[[
			            {field:'brandCode',title:'品牌编号',width:50,checkbox:true},  
						{field:'brandZNName',title:'品牌中文名称',width:100,align:'center',sortable:false},
						{field:'brandUNName',title:'品牌英文名称',width:450,align:'center',sortable:false},
						{field:'opt', title:'操作',width:50,align:'center', formatter:function(value,rec,index){  
							return '<a href="javascript:void(0)" mce_href="#" onclick="xuanpinAdd.delTableRow(\'brandTable\',\''+ index +'\')">删除</a> ';  
		                }}
			    ]],
			    title:'品牌列表',
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
		
		if(table == 'brandTable') {
			var delIndex = this.plusData.brandCodes.indexOf(rowData['skuCode'])
			if(delIndex > -1) {
				this.plusData.brandCodes.splice(delIndex,1)
			}
		}
		
		if(table == 'categoryTable') {
			var delIndex = this.plusData.categoryCodes.indexOf(rowData['categoryCode'])
			if(delIndex > -1) {
				this.plusData.categoryCodes.splice(delIndex,1)
			}
		}
	},	
	beforeSubmit: function() {
		if($('#zw_f_name').val() == '') {
			zapjs.f.message('名称不能为空');
			return false;
		}
		
		if($('#zw_f_sell_price_limit').val() == "4497471600070002") {
			var start = $('#zw_f_sell_price_range_start').val();
			var end = $('#zw_f_sell_price_range_end').val();
			
			if(!/\d+(\.\d+)?/.test(start) || !/\d+(\.\d+)?/.test(end)
					|| parseFloat(start) > parseFloat(end)){
				zapjs.f.message('最小价格和最大价格不能为空，且最大价格需要大于最小价格');
				return false;
			}
			
			$('#zw_f_sell_price_range').val(start+'-'+end)
		} else {
			$('#zw_f_sell_price_range').val('')
		}
		
		if($('#zw_f_product_stock_limit').val() == "4497471600070002") {
			var start = $('#zw_f_product_stock_range_start').val();
			var end = $('#zw_f_product_stock_range_end').val();
			
			if(!/\d+/.test(start) || !/\d+/.test(end)
					|| parseFloat(start) > parseFloat(end)){
				zapjs.f.message('最小库存和最大库存不能为空，且最大库存需要大于最小库存');
				return false;
			}
			
			$('#zw_f_product_stock_range').val(start+'-'+end)
		} else {
			$('#zw_f_product_stock_range').val('')
		}
		
		if($('#zw_f_category_limit').val() == "4497471600070002") {
			var codes = xuanpinAdd.plusData.categoryCodes.join(',');
			if(codes == '') {
				zapjs.f.message('请至少选择一个分类');
				return false;
			}
			$('#zw_f_category_codes').val(codes)
		} else {
			$('#zw_f_category_codes').val('')
		}
		
		if($('#zw_f_brand_limit').val() == "4497471600070002") {
			var codes = xuanpinAdd.plusData.brandCodes.join(',');
			if(codes == '') {
				zapjs.f.message('请至少选择一个品牌');
				return false;
			}
			$('#zw_f_brand_codes').val(codes)
		} else {
			$('#zw_f_brand_codes').val('')
		}
		
		if($('#zw_f_small_seller_limit').val() == "4497471600070002") {
			var checkedList = $('input[name="zw_f_small_seller_codes_checkbox"]:checked')
			var codes = '';
			for(var i = 0; i < checkedList.size(); i++) {
				if(codes != '') codes += ',';
				codes += checkedList.eq(i).val();
			}
			
			if(codes == '') {
				zapjs.f.message('请至少选择一个商户');
				return false;
			}
			$('#zw_f_small_seller_codes').val(codes)
		} else {
			$('#zw_f_small_seller_codes').val('')
		}		
		
		if($('#zw_f_uc_seller_type_limit').val() == "4497471600070002") {
			var checkedList = $('input[name="zw_f_uc_seller_type_codes_checkbox"]:checked')
			var codes = '';
			for(var i = 0; i < checkedList.size(); i++) {
				if(codes != '') codes += ',';
				codes += checkedList.eq(i).val();
			}
			
			if(codes == '') {
				zapjs.f.message('请至少选择一个商户类型');
				return false;
			}
			$('#zw_f_uc_seller_type_codes').val(codes)
		} else {
			$('#zw_f_uc_seller_type_codes').val('')
		}
		
		if($('#zw_f_comment_limit').val() == "4497471600070002") {
			var val = $('#zw_f_comment_num').val();
			if(val == '') {
				zapjs.f.message('请填写最少评论数');
				return false;
			}
			if(!/^\d+$/.test(val)) {
				zapjs.f.message('最少评论数仅支持填写数字');
				return false;
			}
		} else {
			$('#zw_f_comment_num').val('')
		}
		
		if($('#zw_f_comment_good_limit').val() == "4497471600070002") {
			var val = $('#zw_f_comment_good_num').val();
			if(val == '') {
				zapjs.f.message('请填写最少好评数');
				return false;
			}
			if(!/^\d+$/.test(val)) {
				zapjs.f.message('最少好评数仅支持填写数字');
				return false;
			}
		} else {
			$('#zw_f_comment_good_num').val('')
		}
		
		if($('#zw_f_rebuy_rate_limit').val() == "4497471600070002") {
			var val = $('#zw_f_rebuy_rate').val();
			if(val == '') {
				zapjs.f.message('请填写最低复购率');
				return false;
			}
			if(!/^\d+$/.test(val)) {
				zapjs.f.message('最低复购率仅支持填写数字');
				return false;
			}
			
			if(parseInt(val) > 100) {
				zapjs.f.message('最低复购率值不能超过100');
				return false;
			}
		} else {
			$('#zw_f_rebuy_rate').val('')
		}
		
		return true;
	},	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/xuanpinAdd", function() {
		return xuanpinAdd;
	});
}