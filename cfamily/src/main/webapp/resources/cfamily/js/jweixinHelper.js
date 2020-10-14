(function ($) {
	
	var initConfig = {
		action: 'wxshare',
		jsApiList: 'updateAppMessageShareData,updateTimelineShareData,onMenuShareAppMessage,onMenuShareTimeline,onMenuShareQQ,onMenuShareWeibo,onMenuShareQZone',
		debug: false,
		surl: window.location.href
	}
	
	var context = {
		state: 'none',	
		apiConfig: initConfig,
		taskId: 0,
		funcList: []
	}
	
	function init(apiPath) {
		if(this.context.state == 'none') {
			this.context.state = 'processing'
			if(apiPath){
				this.context.apiPath = apiPath
			}
			
			$.post(this.context.apiPath, this.context.apiConfig, (function(helper){
				return function(data){
					helper.wxConfig($.parseJSON(data))
				}
			})(this));
		}
	}
	
	function wxConfig(param) {
		if(window.wx && param.resultcode == 0) {
			window.wx.ready((function(helper){
				return function(){
					helper.context.state = 'inited';
				}
			})(this))
		    
			window.wx.error(function(res){console.log(res)})
			window.wx.config(param)
		}
	}
	
	function execFunc() {
		clearTimeout(this.context.taskId)
		this.context.taskId = setTimeout((function(helper){
			return function(){
				if(helper.context.state == 'inited') {
					while(true){
						var funcCall = helper.context.funcList.pop()
						if(typeof funcCall == 'function') {
							funcCall();
						}else {
							break;
						}
					}	
				}else{
					helper.exec();
				}
			}
			
		})(this),500);
	}
	
	var jweixinHelper = {
		context: context,
		init: init,
		wxConfig: wxConfig,
		exec: execFunc,
		run: function(func){
			try{
				this.context.funcList.push(func);
			}catch(e){
				console.log(e)
			}
			this.exec();
		}
	}
	
	window.jweixinHelper = jweixinHelper;
})(jQuery)