var advertisementInfoAdd ={
	adver_entry_type:"",
	channel_id:"",
	init:function (adver_entry_type,channel_id){
		advertisementInfoAdd.adver_entry_type = adver_entry_type;
		advertisementInfoAdd.channel_id = channel_id;
		$("#zw_f_advertise_code").parent().parent().hide();
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',advertisementInfoAdd.beforesubmit);
		advertisementInfoAdd.init_value();
		$('#zw_f_is_share').change(function(){
			advertisementInfoAdd.show_shareInfo();
		});
		$('#zw_f_link_type').change(function(){
			advertisementInfoAdd.jumpChange();
		});
		if($("#zw_f_is_share").val()=='449746250001'){
			$("label[for='zw_f_share_title']").html("<span class=\"w_regex_need\">*</span>分享标题：");
			$("label[for='zw_f_share_content']").html("<span class=\"w_regex_need\">*</span>分享内容：");
			$("label[for='zw_f_share_img_url']").html("<span class=\"w_regex_need\">*</span>分享图片：");
			$("#zw_f_share_title").parent().parent().show();
			$("#zw_f_share_content").parent().parent().show();
			$("#zw_f_share_img_url").parent().parent().show();
		}
	},
	
	init_value:function (){
		$("#zw_f_link_url").parent().append('<span id="showTipsOne" class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(PATH 地址为：“XXX|APPID”，APPID可为空，XXX为小程序打开路径)</span>').append('<span id="showTipsTwo" class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;填"# 不做任何跳转"</span>');
		$("#zw_f_share_title").parent().parent().hide();
		$("#zw_f_share_content").parent().parent().hide();
		$("#zw_f_share_img_url").parent().parent().hide();
		if(advertisementInfoAdd.adver_entry_type == 'ADTP001'){
			$("#zw_f_is_share").parent().parent().hide();
		}
		if(advertisementInfoAdd.channel_id == '449748610005'){
			$("#zw_f_link_type").val("4497471600640002");
			$("#zw_f_link_type").attr("disabled","disabled");
			$("label[for='zw_f_link_url']").html("<span class=\"w_regex_need\">*</span>URL链接地址：");
			$("#showTipsOne").hide();
			$("#showTipsTwo").show();
		}else{
			if($('#zw_f_link_type').val()=='4497471600640001'){
				$("label[for='zw_f_link_url']").html("<span class=\"w_regex_need\">*</span>小程序PATH：");
				$("#showTipsOne").show();
				$("#showTipsTwo").hide();
			}else{
				$("label[for='zw_f_link_url']").html("<span class=\"w_regex_need\">*</span>URL链接地址：");
				$("#showTipsOne").hide();
				$("#showTipsTwo").show();
			}
		}
	},
	show_shareInfo :function (){
		if($("#zw_f_is_share").val()=='449746250001'){
			$("label[for='zw_f_share_title']").html("<span class=\"w_regex_need\">*</span>分享标题：");
			$("label[for='zw_f_share_content']").html("<span class=\"w_regex_need\">*</span>分享内容：");
			$("label[for='zw_f_share_img_url']").html("<span class=\"w_regex_need\">*</span>分享图片：");
			$("#zw_f_share_title").parent().parent().show();
			$("#zw_f_share_content").parent().parent().show();
			$("#zw_f_share_img_url").parent().parent().show();
		}else{
			$("#zw_f_share_title").val("");
			$("#zw_f_share_content").val("");
			$("#zw_f_share_img_url").val("");
			javascript:zapweb_upload.change_index('zw_f_share_img_url',0,'delete')
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			$("#zw_f_share_img_url").parent().parent().hide();
			
			
		}
	},
	jumpChange : function(){
		if($('#zw_f_link_type').val()=='4497471600640001'){
			$("label[for='zw_f_link_url']").html("<span class=\"w_regex_need\">*</span>小程序PATH：");
			$("#showTipsOne").show();
			$("#showTipsTwo").hide();
		}else{
			$("label[for='zw_f_link_url']").html("<span class=\"w_regex_need\">*</span>URL链接地址：");
			$("#showTipsOne").hide();
			$("#showTipsTwo").show();
		}
	},
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		if(advertisementInfoAdd.adver_entry_type == 'ADTP002'&&$("#zw_f_is_share").val() == '449746250001'){
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
		$("#zw_f_link_type").removeAttr("disabled");		
		return  true;
		
	}
	
	
	
};