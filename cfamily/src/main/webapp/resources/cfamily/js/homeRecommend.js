var homeRecommend = {
	temp : {
		category_code:""
	},
	init : function(p) {
		
		if (null != p && ""!=p) {
			homeRecommend.temp.category_code = p;
			homeRecommend.set_category();
		}
		
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
				homeRecommend.beforesubmit);
	},
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		$("#zw_f_category_name").val($("#zw_f_showmore_linkvalue_text").text());		//分类名称
		var note = $("#zw_f_category_note").val();
		if (null != note && "" != note) {
			if(homeRecommend.strLength(note)>10) {
				//zapadmin.model_message('显示名称不能大于5个字符');
				alert('显示名称不能大于5个字符');
				return false;	
			}
		}
		return true;
	},
	// 设置分类名称
	set_category : function() {
		var categoryCodes = homeRecommend.temp.category_code;
		if (categoryCodes.length > 0) {
			categoryCodes =  categoryCodes.substring(0, categoryCodes.length-1)	
		}
		$("#zw_f_category_codes").val(categoryCodes);
		var obj = {};
		obj.categoryCodes = categoryCodes;
		obj.sellerCode="SI2003";
		zapjs.zw.api_call('com_cmall_usercenter_service_api_ApiGetCategroyName',obj,function(result) {
			if(result.resultCode==1){
				$('#zw_f_showmore_linkvalue_text').text(result.categoryName);
			}
		});
		
	},
	
	strLength : function(fData) { 
	    var intLength=0;
	    for (var i=0;i<fData.length;i++) 
	    { 
	        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255)) 
	            intLength=intLength+2;
	        else 
	            intLength=intLength+1;   
	    } 
	    return intLength;
	} 
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/homeRecommend", function() {
		return homeRecommend;
	});
}