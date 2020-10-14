var couponActivity = {
		
	init: function(activityCode){
		$("#activityInfor").hide();
		$("#zw_f_distribute_type option[value='4497471600240002']").remove();
		couponActivity.aInfor(activityCode);
		$(".control-upload_iframe").attr("style","float: left; overflow: hidden; width: 136px;");
		$("#zw_f_member_code").parent().append('<a href="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/271e1/d4e544841b1248caa04afb6eceab3857.xlsx">下载范例</a>');
	},
	api_call : function(sTarget, oData, fCallBack) {

		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'betafamilyhas'
		}:{api_key : 'betafamilyhas',api_input:''};
		
		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			fCallBack(data);
		});

	},
	aInfor : function(sVal){
		$("#zw_f_activity_code").val(sVal);
		$("#tx").remove();
		$("#ac").after('<div id="tx" class="w_left w_w_70p"><ul id="myul" class="zab_js_zapadmin_single_ul"></ul></div>');
		$("#myul").append('<li>'+sVal+'</li>');
		var opt ={};
		opt.activityCode=sVal;
		couponActivity.api_call('com_cmall_ordercenter_service_api_ApiGetActivityById',opt,function(result) {
			if(result.resultCode==1){
				var resultMap = result.acMap;
				if(resultMap.remark == ""){
					$("#activity_description").parent().parent().hide();
				}else{
					$("#activity_description").parent().parent().show();
				}
				$("#activityName").text(resultMap.activity_name);
				$("#activity_description").text(resultMap.remark);
				$("#activity_startTime").text(resultMap.begin_time);
				$("#activity_endTime").text(resultMap.end_time);
			}
		});
		$("#activityInfor").show();
	}
};
if (typeof define === "function" && define.amd) {
	define("cmanage/js/couponActivity",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return couponActivity;
	});
}
$(document).ready(function(){

});
