var comment = {
		init : function(){
			
		},
		beforeSubmit : function(e){
			zapjs.zw.func_add(e);
		},
		getPicUrl : function(){
			//com_cmall_sharpei_api_GetCommentPictureService
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiGetCommentPictureCf?zw_f_uid='+$("#zw_f_uid").val(),'',function(data) {
				var url = data.resultMessage.split("|");
				var urlHtml = "";
				for(var i=0;i<url.length;i++){
					urlHtml += "<a target='_blank' href="+url[i]+"><img src="+url[i]+"></a>";
				}
				$("#picUrl").html(urlHtml);
			});
		}
};