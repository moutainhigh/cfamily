var cdkeyAdd = {
		init:function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',cdkeyAdd.beforeSubmit);
			$("#zw_f_account_useTime").val("1");
			cdkeyAdd.initMultiType();
		},
		initMultiType:function(){
			if($("#zw_f_multi_account").val() == "449746250001"){//多账户使用为是的时候
				$("#zw_f_cdkey_prefix").parent().parent().remove();
				$("#zw_f_create_num").parent().parent().remove();
				$("#zw_f_account_useTime").parent().parent().after('<div class="control-group"><label class="control-label" for="zw_f_cdkey"><span class="w_regex_need">*</span>活动优惠码：</label><div class="controls"><input id="zw_f_cdkey" type="text" value="" zapweb_attr_regex_id="469923180002" name="zw_f_cdkey"></div></div');
				$("#zw_f_cdkey").parent().parent().after('<div class="control-group"><label class="control-label" for="zw_f_use_people"><span class="w_regex_need">*</span>使用人数：</label><div class="controls"><input id="zw_f_use_people" type="text" value="1" zapweb_attr_regex_id="469923180002" name="zw_f_use_people"></div></div');
			}else{//多账户使用为否的时候
				$("#zw_f_cdkey").parent().parent().remove();
				$("#zw_f_use_people").parent().parent().remove();
				$("#zw_f_account_useTime").parent().parent().after('<div class="control-group"><label class="control-label" for="zw_f_cdkey_prefix"><span class="w_regex_need">*</span>前缀：</label><div class="controls"><input id="zw_f_cdkey_prefix" type="text" value="" zapweb_attr_regex_id="469923180002" name="zw_f_cdkey_prefix"><span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(请输入四位英文数字字符)</span></div></div');
				$("#zw_f_cdkey_prefix").parent().parent().after('<div class="control-group"><label class="control-label" for="zw_f_create_num"><span class="w_regex_need">*</span>个数：</label><div class="controls"><input id="zw_f_create_num" type="text" value="" zapweb_attr_regex_id="469923180002" name="zw_f_create_num"><span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(一次最多只能生成50000个)</span></div></div');
			}
		},
		beforeSubmit : function() {
		var type = "^[0-9]*[1-9][0-9]*$";
		var re = new RegExp(type);
		if ($("#zw_f_account_useTime").val().match(re) == null) {
			zapjs.f.message('每账户使用次数只能为正整数');
			return false;
		}
		if ($("#zw_f_multi_account").val() == "449746250002") {// 多账户使用为否的时候
			var cdkeyPrefix = $("#zw_f_cdkey_prefix").val();
			var check = "^[A-Za-z0-9]+$";
			var reg = new RegExp(check);
			if (cdkeyPrefix.match(reg) == null || cdkeyPrefix.length != 4) {
				zapjs.f.message('前缀只能输入四位英文数字字符');
				return false;
			}
			if ($("#zw_f_create_num").val().match(re) == null) {
				zapjs.f.message('个数只能为正整数');
				return false;
			}
			if ($("#zw_f_create_num").val() > 50000) {
				zapjs.f.message('一次最多只能生成50000个');
				return false;
			}
		}else{
			if($("#zw_f_cdkey").val().length > 30){
				zapjs.f.message('优惠码不能超过30个字符');
				return false;
			}
			if ($("#zw_f_use_people").val().match(re) == null) {
				zapjs.f.message('使用人数只能为正整数');
				return false;
			}
		}
		return true;
	},
};

$(document).ready(function(){
	//生成类型
	$("#zw_f_multi_account").change(function(){
		cdkeyAdd.initMultiType();
	});
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/cdkeyAdd", function() {
		return cdkeyAdd;
	});
}
