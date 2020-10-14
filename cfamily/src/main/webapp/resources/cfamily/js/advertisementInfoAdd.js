var advertisementInfoAdd ={
	
	init:function (){
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',advertisementInfoAdd.beforesubmit);
		advertisementInfoAdd.init_value();
		$('#zw_f_adver_entry_type').change(function(){
			advertisementInfoAdd.init_add();
		});
		$('#zw_f_is_share').change(function(){
			advertisementInfoAdd.show_shareInfo();
		});
		 
	},
	
	init_value:function (){
			advertisementInfoAdd.init_add();
	},
	show_shareInfo :function (){
		if($("#zw_f_is_share").val()=='Y'){
			$("label[for='zw_f_share_title']").html("<span class=\"w_regex_need\">*</span>分享标题：");
			$("label[for='zw_f_share_content']").html("<span class=\"w_regex_need\">*</span>分享内容：");
			$("label[for='zw_f_share_img_url']").html("<span class=\"w_regex_need\">*</span>分享图片：");
			$("#zw_f_share_title").parent().parent().show();
			$("#zw_f_share_content").parent().parent().show();
			$("#zw_f_share_img_url").parent().parent().show();
		}
		else{

			$("#zw_f_share_title").val("");
			$("#zw_f_share_content").val("");
			$("#zw_f_share_img_url").val("");
			javascript:zapweb_upload.change_index('zw_f_share_img_url',0,'delete')
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			$("#zw_f_share_img_url").parent().parent().hide();
			
			
		}
	},
	
	init_add:function (){
		var entryType=$("#zw_f_adver_entry_type").val();
		if(entryType == "ADTP001"){//个人中心
			$("#zw_f_is_share").parent().parent().hide();
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			$("#zw_f_share_img_url").parent().parent().hide();
			
			
		}else if(entryType == "ADTP002"){//支付成功
			$("label[for='zw_f_is_share']").html("<span class=\"w_regex_need\">*</span>是否分享：");
			$("#zw_f_is_share").parent().parent().show();
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			$("#zw_f_share_img_url").parent().parent().hide();
		}
		
	},
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		if($("#zw_f_adver_entry_type").val() == 'ADTP002'&&$("#zw_f_is_share").val() == 'Y'){
			if($("#zw_f_share_title").val() == ''){
				zapjs.f.message("请填写分享标题");
				return false;
			}
			if($("#zw_f_share_content").val() == ''){
				zapjs.f.message("请填写分享内容");
				return false;
			}
			if($("#zw_f_share_img_url").val() == ''){
				zapjs.f.message("请填选择分享图片");
				return false;
			}
	
		}		
		return  true;
		
	}
	
	
	
};