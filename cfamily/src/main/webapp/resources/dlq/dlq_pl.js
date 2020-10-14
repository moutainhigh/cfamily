var dlq_pl = {
		souce : '',
		init : function(){
			dlq_pl.souce = dlq_pl.up_urlparam("t_souce");
			if(dlq_pl.souce.length >0) {
				
				var reqUrl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetDLQComment?api_key=betafamilyhas&source="+dlq_pl.souce;
				$.ajax({
					type: "GET",
					url: reqUrl,
					dataType: "json",
					success: function(data) {
						if(data.resultCode == 1) {
							
							var vHtml = "";
							var commentList = data.commentList;
							for(var i=0;i<commentList.length;i++) {
								
								var comment = commentList[i];
								vHtml += '<li class="clearfix">';
								if(null != comment.head_photo && "null" != comment.head_photo && comment.head_photo.length>0) {
									vHtml += '<div class="comment-head"><img src="'+comment.head_photo+'">'+'</div>';
								} else {
									vHtml += '<div class="comment-head"><img src="../../resources/dlq/img/personal_picture.png" ></div>';
								}
								vHtml += '<div class="comment-txt">';
								if(comment.mobile.length > 0) {
									vHtml += '<p class="comment-name">'+comment.mobile+'</p>';
								} else {
									vHtml += '<p class="comment-name">'+comment.c_ip+'</p>';
								}
								vHtml += '<p class="comment-time">'+comment.c_time+'</p>';
								vHtml += '<p class="comment-con">'+comment.content+'</p>';
								
								
								if(comment.rtn_content.length > 0) {
									vHtml += '<div class="reply" style="overflow:hidden; padding-top:6px; margin-top:7px;border-top:1px solid #e5e5e5;">';
										vHtml += '<p class="reply_txt" style="color:#666;word-break: break-all;">';
											vHtml += '<span style="color:#d8b887;font-style: normal;">'+comment.rtn_user+': </span>';
											vHtml += comment.rtn_content;
										vHtml += '</p>';
										vHtml += '<span style="color:#999; font-size:10px; float:right; margin-top:3px">';
											vHtml += '回复于'+comment.rtn_time;
										vHtml += '</span>';
									vHtml += '</div>';
								}
								
								
								
								vHtml += '</div>';
								vHtml += '</li>';
								
							}
							
							$(".comment-list").html(vHtml);
						}
						
					}
				});
				
			}
			
		},
		up_urlparam :function (sKey, sUrl) {
			var sReturn = "";
			
			if (!sUrl) {
				sUrl = window.location.href;
				if (sUrl.indexOf('?') < 1) {
					sUrl = sUrl + "?";
				}
			}
		
			var sParams = sUrl.split('?')[1].split('&');
		
			for (var i = 0, j = sParams.length; i < j; i++) {
		
				var sKv = sParams[i].split("=");
				if (sKv[0] == sKey) {
					sReturn = sKv[1];
					break;
				}
			}
		
			return sReturn;
		
		},
		forwardprePge : function () {
			_hmt.push(['_trackEvent', window.location.href+'_click_返回', 'click', "发表评论", '1']);
			history.back(-1);
		}
		
};