var addMusicTemplate = {
	init : function(){
		
		//字段初始化
		{
			//隐藏
			addMusicTemplate.hideEle("");
			//显示
			addMusicTemplate.showEle("");
		}
		
		
		
		$("#zw_f_template_type").change(function(){
			
		});

	},
	hideEle:function(eleS){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			//清空数值
			$("#zw_f_"+eleArr[i]).val("");
			$("#zw_f_"+eleArr[i]).parent().parent().hide();
		}
	},
	showEle:function(eleS){
		var eleArr = eleS.split(",");
		for(var i=0;i<eleArr.length;i++) {
			$("#zw_f_"+eleArr[i]).parent().parent().show();
		}
	},
	submit:function(obj){	
		/*
		 * 校验上传的图片的大小是否大于1M（只允许上传1M一下的图片）
		 */
		var errMs = "";
		var flg = false;
		var validatResult = addMusicTemplate.validateImageSize();
		if(validatResult.length > 0) {
			flg = true;
			errMs = validatResult;
		}
		
		if(flg) {
			zapjs.f.message(errMs);
			return false;
		}
		
		zapjs.zw.func_add(obj);
		
	},
	//校验图片大小
	validateImageSize : function() {
		var preview_img = $("#zw_f_preview_img").val();
		var share_img = $("#zw_f_share_img").val();
		if(undefined != preview_img && undefined != zapweb_upload.imgSizeMap[preview_img] && preview_img.length > 0 ) {
			if(zapweb_upload.imgSizeMap[preview_img]/1024/1024 > 1) {//判断是否大于1M
				return "预览图片超1M,请重新上传";
			}
		}
		if(undefined != share_img && undefined != zapweb_upload.imgSizeMap[share_img] && share_img.length > 0 ) {
			if(zapweb_upload.imgSizeMap[share_img]/1024/1024 > 1) {//判断是否大于1M
				return "分享图片超1M,请重新上传";
			}
		}

		return "";
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addMusicTemplate", function() {
		return addMusicTemplate;
	});
}