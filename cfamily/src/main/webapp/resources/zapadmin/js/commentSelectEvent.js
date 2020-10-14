var commentSelectEvent ={	
	init:function (appList,channel_list,channel_trans_list){
		$("#div_jump_position").hide();
		$("#div_product").hide();
		$('#zw_f_jump_type').change(function(){
			var type=$('#zw_f_jump_type').val();
			$("#zw_f_jump_position").val("");
			if(type=="4497465000080002"){
				$("#div_product").show();
				$("#div_jump_position").hide();
			}else if(type=="4497465000080008"){
				$("#div_jump_position").show();
				$("#div_product").hide();
			}else{
				$("#div_jump_position").hide();
				$("#div_product").hide();
			}
			
		});
		//校验输入
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',function(){
			var type=$('#zw_f_jump_type').val();
			var push_time = $("#zw_f_push_time").val();
			var jump_position = $("#zw_f_jump_position").val();
			
			if(push_time==''){
				zapjs.f.message("发送时间不能为空!");
				return false;
			}
			if(type=="4497465000080002"){
				if(jump_position==''){
					zapjs.f.message("商品名称不能为空!");
					return false;
				}
			}else if(type=="4497465000080008"){
				if(jump_position==''){
					zapjs.f.message("跳转位置不能为空!");
					return false;
				}
			}
			return true;	
			});	
	}
};
