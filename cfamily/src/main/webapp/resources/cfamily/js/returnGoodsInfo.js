var returnGoodsInfo = {
	
	init_page:function (){
		
	},
	
	
	changeAddr : function(small_seller_code){
		
		zapjs.f.window_box({
			id : 'changeAddr',
			content : '<iframe src="../show/page_chart_v_select_oc_address_info?zw_s_iframe_select_source=changeAddr&zw_f_small_seller_code='+small_seller_code+'&zw_s_iframe_select_page=page_chart_v_select_oc_address_info&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.returnGoodsInfo.addAddr" frameborder="0" style="width:100%;height:600px;"></iframe>',
			width : '800',
			height : '650'
		});
	},
	
	addAddr : function(sId,va, sVal){
		var obj = {};
		obj.uid = va;
		obj.asale_code=$("#return_code").val();
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForUpdateAfterSaleAddr',obj,function(result) {
			if(result.resultCode==1){
				location.reload() 
				zapjs.f.window_close(sId);
			}
		});
	},
	
	message:function (flag,id){
		
		var asale_code=$("#return_code").val();
		
		var obj = {};
		obj.flag = flag;
		obj.asale_code=asale_code;
		obj.type="ret";
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForAfterSaleMessage',obj,function(result) {
			if(result.resultCode==1){
				
				if('check'==flag){
					
					var id='zapjs_f_id_modal_message';
					var sModel = '<div id="' + id + '" ></div>';
					$(document.body).append(sModel);
					$('#' + id).html('<div class="w_p_20">' + result.resultMessage + '</div>');
					
					
					$('#'+id).dialog({
						title : "发送短信",
						closed : false,
						cache : false,
						modal : true,
						buttons:[{
							text: '收货人', 
							handler: function() { 
								returnGoodsInfo.message("owner",id);
								zapjs.f.window_close(id);
							}
						},{
							text: '下单人', 
							handler: function() { 
								returnGoodsInfo.message("buyer",id);
								zapjs.f.window_close(id);
							}
							
						}]
						
					});
					
					
				}
			}
		});
		
	},
	
	
	approve:function (flag,return_code){
		
		var txt="";
		var opid="";
		if(flag=="cancel"){
			txt="取消退货";
			opid="ce4ecd6abf97493c8b8c756b5693ebdf";
		}else{
			txt="客服确认";
			opid="a6584caf71a84104a49043be53181b64";
		}
		
		var html='';
		
		html+='<form class="form-horizontal" method="POST" style="margin-top:20px;">';
		html+='<input type="hidden" id="zw_f_return_code" name="zw_f_return_code" value="'+return_code+'">';
		html+='<div class="control-group">';
		html+='	<label class="control-label" for="zw_f_source_code">备注信息:</label>';
		html+='		<div class="controls">';
		html+='			<textarea id="zw_f_remark" name="zw_f_remark"></textarea>';
		html+='		</div>';
		html+='</div>';
		
		if(flag!="cancel"){
			
			html+='<div class="control-group">';
			html+='	<label class="control-label" for="zw_f_source_code">寄回货品:</label>';
			html+='		<div class="controls">';
			html+='			<select name="zw_f_flag_return_goods" id="zw_f_flag_return_goods"><option value="4497477800090001">需要</option><option value="4497477800090002">不需要</option></select>';
			html+='		</div>';
			html+='</div>';
		}
		
		html+='<div class="control-group">';
		html+='	<label class="control-label" for="zw_f_source_code"></label>';
		html+='	<div class="controls">';
		html+='		<div class="btn-toolbar">';
		html+='			<input type="button" style="margin-right:5px;" class="btn  btn-primary" zapweb_attr_operate_id="'+opid+'" onclick="zapjs.zw.func_add(this)" value="'+txt+'">';
		html+='		</div>';
		html+='	</div>';
		html+='</div>';
		html+='</form>';
		
		modalOption = {content:html,title:"请审批",oktext:"关闭",height:300};
		
		zapjs.f.window_box(modalOption);
		
	},
	
	sendMessage:function (){
		
//		var ms=$("#ms").val();
//		if(ms==''){
//			zapjs.f.message("请稍后重试");
//			return ;
//		}
		
		returnGoodsInfo.message("check");
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/returnGoodsInfo",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return returnGoodsInfo;
	});
}
