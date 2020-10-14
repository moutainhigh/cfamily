var commentPush ={	
	callPush:function (uid){
		$.getJSON("../func/c341f20f24654355a8e7a7b560db44a7?uid="+uid, function(o){
			if (o.resultCode == "1") {
				if (o.resultMessage == "") {
					o.resultMessage = "操作成功";
				}
				zapjs.zw.modal_show({
					content : o.resultMessage,
					okfunc : 'zapjs.f.autorefresh()'
				});

			} else {
				zapjs.zw.modal_show({
					content : o.resultMessage
				});
			}
		});
	}
};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/commentPush", [], function() {
		return commentPush;
	});
}