var jdAfterSale = {
	// 提交物流信息给京东售后
	confirmShipmentsInfo: function(target,afterSaleCode){
		zapjs.f.modal({
			content: '确认提交售后物流信息到京东系统？',
			okfunc: 'jdAfterSale.sendShipmentsInfo(\"'+$(target).attr('zapweb_attr_operate_id')+'\",\"'+afterSaleCode+'\")'
		});
	},		
	// 提交物流信息给京东售后
	sendShipmentsInfo: function(operate_id,afterSaleCode){
		zapjs.zw.modal_process();
		zapjs.f.ajaxjson("../func/"+operate_id,{afterSaleCode: afterSaleCode},function(data){
			zapjs.f.modal_close();
			zapjs.zw.func_success(data);
		});
	},
	// 同步京东售后单状态
	refreshAfterSaleStatus: function(target,afterSaleCode){
		zapjs.zw.modal_process();
		zapjs.f.ajaxjson("../func/"+$(target).attr('zapweb_attr_operate_id'),{afterSaleCode: afterSaleCode},function(data){
			zapjs.f.modal_close();
			zapjs.zw.func_success(data);
		});
	},
	// 创建京东售后单
	rsyncAfterSaleCreate: function(target,afterSaleCode){
		zapjs.zw.modal_process();
		zapjs.f.ajaxjson("../func/"+$(target).attr('zapweb_attr_operate_id'),{afterSaleCode: afterSaleCode},function(data){
			zapjs.f.modal_close();
			zapjs.zw.func_success(data);
		});
	},
}