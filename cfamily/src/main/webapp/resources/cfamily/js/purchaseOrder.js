purchaseOrder={
		changePurcharseStatus : function(purchaseOrderCode,nextStatusCode){
			
	       
			var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批备注:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";;

			showHtml+="<div class='control-group'><div class='controls'><div class='btn-toolbar'>";
			
			showHtml+="<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'></div></div></div>";
			showHtml+="</form>";
			var modalOption="";
			modalOption = {content:showHtml,title:"请审核",oktext:"关闭",height:200};				
			zapjs.f.window_box(modalOption);		
			$("#okId").bind("click",function(){
				var rk = $("#zw_f_remark").val();
				var obj = {};
				obj.purchase_order_id =purchaseOrderCode ;
				obj.next_status =nextStatusCode ;
				obj.remark = rk;
				zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseShenPi',obj,function(result) {
					if(result.resultCode==1){
						$(".panel").hide();
						$("#zw_f_remark").val("");
						purchaseOrder.refreshSelf();
						}
					else{
						 zapadmin.model_message('审批异常');
						}
					});
					}	
			)
			
		},
		batchChangePurcharseStatus : function(){
		var purcharseIds= []; 
		$(".purcharse_class").each(function(){
	    if($(this).is(':checked')){
	    	purcharseIds.push($(this).val());
	    }
      });
		var pIds = purcharseIds.join(',');
        console.log(purcharseIds);
		var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><div class='control-group'><label class='control-label' for='zw_f_source_code'>审批备注:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
		showHtml+="<div class='control-group'><div class='controls'><div class='btn-toolbar'>";
		
		showHtml+="<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='通过'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' id='noOkId' style='margin-right:25px;' class='btn  btn-primary' value='驳回'></div></div></div>";
		showHtml+="</form>";
		var modalOption="";
		modalOption = {content:showHtml,title:"请审核",oktext:"关闭",height:200};				
		zapjs.f.window_box(modalOption);		
		$("#okId").bind("click",function(){
			if(purcharseIds.length==0){
				zapadmin.model_message('请选择采购记录！');
				return ;
			}
			var rk = $("#zw_f_remark").val();
			var obj = {};
			obj.purchase_order_id =pIds ;
			obj.next_status ='449748490003' ;
			obj.remark = rk;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseShenPi',obj,function(result) {
				if(result.resultCode==1){
					$(".panel").hide();
					$("#zw_f_remark").val("");
					purchaseOrder.refreshSelf();
					}
				else{
					 zapadmin.model_message('审批异常');
					}
				});
				}	
		)
		$("#noOkId").bind("click",function(){
			if(purcharseIds.length==0){
				zapadmin.model_message('请选择采购记录！');
				return ;
			}
			var rk = $("#zw_f_remark").val();
			var obj = {};
			obj.purchase_order_id =pIds ;
			obj.next_status ='449748490002';
			obj.remark = rk;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseShenPi',obj,function(result) {
				if(result.resultCode==1){
					$(".panel").hide();
					$("#zw_f_remark").val("");
					purchaseOrder.refreshSelf();
					}
				else{
					 zapadmin.model_message('审批异常');
					}
				});
				}	
		)
		
			
		},
		
		allSelect  : function(){
			if($("#item-all").is(':checked')){
			$(".purcharse_class").each(function(){
			   $(this).prop("checked",true);
		      });	
			}else{
			$(".purcharse_class").each(function(){
			   $(this).prop("checked",false);
			  });	
			}
			},
			
		refreshSelf:function(){
			var url = window.location.href;
			window.location.href="";
			window.location.href = url;
		},
		func_tip : function(oElm, pid,nextState) {
			var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
			$(document.body).append(sModel);
			$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您要\'确认收货\'么？</div>');
			var aButtons = [];
			aButtons.push({
				text : '是',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
						var obj = {};
						obj.purchase_order_id =pid ;
						obj.next_status =nextState ;
						obj.remark = "";
						zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseShenPi',obj,function(result) {
							if(result.resultCode==1){
								purchaseOrder.refreshSelf();
								}
							else{
								 zapadmin.model_message('审批异常');
								}
							});

				
				}
			},{
				text : '否',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
					}
			});
			
			$('#zapjs_f_id_modal_message').dialog({
				title : '提示消息',
				width : '400',
				resizable : true,
				closed : false,
				cache : false,
				modal : true,
				buttons : aButtons
			});
		},
}