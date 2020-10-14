// 专题内特殊逻辑处理
var template618 = {
	onPageShow: function(e){
		this.refresh();
	},
	// 618活动提示
	refresh: function(){
		var pnm = template.up_urlparam("pNum")
		if(pnm != $('#zt_618_pnm').val()) {
			return
		}
		
		// 如果页面未加载完成则延迟一下请求数据
		if($('#zt_618_panel').size() == 0) {
			setTimeout('template618.refresh()',100);
			return;
		}
		
		// 隐藏引导下载的头
		$('#download_APP').hide();
		
		// 忽略未登录的情况
		if(template.user.token == '') {
			return;
		}
		
		var param = "api_token=" + template.user.token;
		$.ajax({ 
			type: "GET",
			url: "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForTemplateTips618?api_key=betafamilyhas&" + param,
			dataType: "json",
			success: function(data) {
				if(data.resultCode == 1 && data.resultMessage != '') {
					template618.showTips(data.resultMessage);
				} else {
					template618.showTips('');
				}
			}
		})
	},
	refreshPosition: function() {
		if($('#zt_618_panel').is(':hidden')){
			return;
		}
		
		if($('.citySwitch_nav_wrap').css('position') == 'fixed') {
			$('#zt_618_panel').css('top',$('.citySwitch_nav_wrap').height())
		} else {
			$('#zt_618_panel').css('top',0)
		}
	},
	showTips: function(tips) {
		if(tips && tips != '') {
			$('#zt_618_panel').show();
			$('#zt_618_text').html(tips);
		} else {
			$('#zt_618_panel').hide();
		}
	}
};