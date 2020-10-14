var brandPreferenceEdit = {
		init : function(){//分享默认不显示
			brandPreferenceEdit.showShare();
		},
		showShare : function(){//是否显示分享的东西
			var shareflag = $("#zw_f_share_flag").val();
			if(shareflag == "449747110002"){//是(显示分享的东西)
				$("#zw_f_share_img_url").parent().parent().show();
				$("#zw_f_share_title").parent().parent().show();
				$("#zw_f_share_content").parent().parent().show();
			}else if(shareflag == "449747110001"){//否(隐藏分享的东西)
				$("#zw_f_share_img_url").parent().parent().hide();
				$("#zw_f_share_title").parent().parent().hide();
				$("#zw_f_share_content").parent().parent().hide();
			}
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/brandPreferenceEdit", function() {
		return brandPreferenceEdit;
	});
}
//切换是否显示时使用
$(document).ready(function(){
	$("#zw_f_share_flag").change(function(){
		brandPreferenceEdit.showShare();
	});
});