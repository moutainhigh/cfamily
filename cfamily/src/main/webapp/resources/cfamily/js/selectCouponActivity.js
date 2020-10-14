var selectCouponActivity = {
		
	init: function(){
		var opt ={};
		selectCouponActivity.api_call('com_cmall_familyhas_api_ApiForCouponRelative',opt,function(result) {
			if(result.resultCode==1){
				var resultMap = result.relativeMap;
				if(resultMap.a != undefined && resultMap.a != ""){
					selectCouponActivity.showText(1, resultMap.a, resultMap.aa);
				}
				if(resultMap.b != undefined  && resultMap.b != ""){
					selectCouponActivity.showText(2, resultMap.b, resultMap.bb);
				}
				if(resultMap.c != undefined  && resultMap.c != ""){
					selectCouponActivity.showText(3, resultMap.c, resultMap.cc);
				}
				if(resultMap.d != undefined  && resultMap.d != ""){
					selectCouponActivity.showText(4, resultMap.d, resultMap.dd);
				}
				if(resultMap.e != undefined  && resultMap.e != ""){
					selectCouponActivity.showText(5, resultMap.e, resultMap.ee);
				}
				if(resultMap.f != undefined  && resultMap.f != ""){
					selectCouponActivity.showText(6, resultMap.f, resultMap.ff);
				}
				if(resultMap.g != undefined  && resultMap.g != ""){
					selectCouponActivity.showText(7, resultMap.g, resultMap.gg);
				}if(resultMap.sign != undefined  && resultMap.sign != ""){
					selectCouponActivity.showSignActivity(resultMap.signActivity);
				}if(resultMap.behavior_flag != undefined  && resultMap.behavior_flag != ""){
					selectCouponActivity.showBehaviorActivity(resultMap.behaviorActivity);
				}
			}else{
				zapadmin.model_message('获取信息失败');
			}
		});
		
		//添加 class=sign_time 值改变事件
//		$("table").on("click","button",function(){
//			$(this).parent().parent().remove();
//			}) 
		$("#sign_big_div").on("change","select",function(){
			var sign_days = $(this).val();
			var select_days=$(".sign_days");
			if(select_days!=null&&select_days!=""&&select_days.length>0){
				var flag =  0;
				for(var  i =  0;i<select_days.length;i++){
					if(sign_days == $(select_days[i]).val()){
						flag += 1;
					}
				}
				if(flag>=2){
					$(this).val("0")
					zapjs.f.message("连续天数不允许重复");
					return;
				}
			}
			
			var num = parseInt(sign_days) + 7;
			var div = $($(this).parent().next().children("div")[0]);
			if(sign_days!=0){
				
				
				div.prop("id","ac"+ num);
				var add = $(div.children("input")[0]);
				add.prop("onclick","");
				add.unbind();
				add.click(function(){
					selectCouponActivity.show_windows('activities'+num)
				})
				var next=div.next();
				next.prop("id","tx"+num);
				next.next().prop("name","zw_f_actictityIds"+num);
				next.next().prop("id","zw_f_actictityIds"+num);
			}
		})
		
	},
	checkInputIfNull :function(){
		var digReg = /^[1-9]\d*$/;
		if($(".start_up1").val()==""||$(".start_up1").val()==null){
			zapjs.f.message("请先填写连续启动天数！");
			return;
		}
		else if(!digReg.test($(".start_up1").val())) {
			zapjs.f.modal({
				content : '请输入正确的启动天数值!'
			});
			return;
		}else{
			selectCouponActivity.show_windows('activities29')
		}
	},
	checkInputIfNull2 :function(){
		var digReg = /^[1-9]\d*$/;
		var digReg2 = /^[0-9]\d*$/;
		if($(".start_up2").val()==""||$(".start_up2").val()==null){
			zapjs.f.message("请先填写连续启动天数！");
			return;
		}
		else if(!digReg.test($(".start_up2").val())) {
			zapjs.f.modal({
				content : '请输入正确的启动天数值!'
			});
			return;
		}else if($(".add_shop_car").val()==""||$(".add_shop_car").val()==null){
			zapjs.f.message("请先填写购物车件数！");
			return;
		}else if(!digReg2.test($(".add_shop_car").val())) {
			zapjs.f.modal({
				content : '请输入正确的购物车件数值!'
			});
			return;
		}else{
			selectCouponActivity.show_windows('activities30')
		}
	},
	
	
	showSignActivity : function(data){
		//data为返回字符串  格式: 1=优惠券a=AC888,2=优惠券b=AC999
		var split = data.split(",");
		for(var i = 0;i<split.length;i++){
			var eachData = split[i];
			//每条签到时间对应活动数据  格式: 8=优惠券=AC888
			var s =eachData.split("=");
			var day1 = s[0] ;
			var day = parseInt(day1)+7
			var acName = s[1];
			var acCode = s[2];
			var activity = "activities"+day;
			if(i==0){
				$("#sign_big_div").empty();
				$("#sign_big_div").append('<div class="control-group"><label class="control-label" for="">签到时间：<select class="sign_days" flag="'+day1+'" name="" style="width:80px"><option value="0">请选择</option><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option></select></label><div class="controls"><div id="ac'+day+'" class="w_left"><input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows(\''+activity+'\')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="btn btn-small" type="button" value="新增" onclick="selectCouponActivity.addNewActivity()" /></div><input type="hidden" id="zw_f_actictityIds'+day+'" name="zw_f_actictityIds'+day+'" value="'+acCode+'" /></div></div>')
				$("[flag="+day1+"]").val(day1)
				selectCouponActivity.showText(day,acCode,acName)
			}else{
				$("#sign_big_div").append('<div class="control-group"><label class="control-label" for="">签到时间：<select class="sign_days" flag="'+day1+'" name="" style="width:80px"><option value="0">请选择</option><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option></select></label><div class="controls"><div id="ac'+day+'" class="w_left"><input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows(\''+activity+'\')">&nbsp;&nbsp;&nbsp;&nbsp;			<input class="btn btn-small" type="button" value="删除" onclick="selectCouponActivity.removeCurrDiv(this)" /></div><input type="hidden" id="zw_f_actictityIds'+day+'" name="zw_f_actictityIds'+day+'" value="'+acCode+'" /></div></div>')
				$("[flag="+day1+"]").val(day1)
				selectCouponActivity.showText(day,acCode,acName)
			}
		}
	},
	showBehaviorActivity: function(data){
		//data为返回字符串  格式: 启动天数=添加购物车件数=优惠券名称=优惠券编号=relative_type(9 连续启动 ；10连续加入购物车)
		var split = data.split(",");
		for(var i = 0;i<split.length;i++){
			var eachData = split[i];
			var s =eachData.split("=");
			var startUpDays = s[0] ;
			var addShopCars = s[1];
			var acName = s[2];
			var acCode = s[3];
			var sub_relative_type = s[4];
			if(sub_relative_type==9){
				$(".start_up1").val(startUpDays)
				selectCouponActivity.showText(29,acCode,acName)
			}else if(sub_relative_type==10){
				$(".start_up2").val(startUpDays)
				$(".add_shop_car").val(addShopCars)
				selectCouponActivity.showText(30,acCode,acName)
			}
		}
	},
	addNewActivity : function(){
		var nums =$(".sign_days");
		if(nums.length==7){
			zapjs.f.message("最多只能添加七条数据")
		}else{
			$("#sign_big_div").append('<div class="control-group"><label class="control-label" for="">签到时间：<select class="sign_days" name="" style="width:80px"><option value="0">请选择</option><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option></select></label><div class="controls"><div id="" class="w_left"><input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.checkDaysAlert()">&nbsp;&nbsp;&nbsp;&nbsp;<input class="btn btn-small" type="button" value="删除" onclick="selectCouponActivity.removeCurrDiv(this)" /></div><div id="" class="w_left w_w_70p"></div><input type="hidden" id="" name="" value="" /></div></div>')
			//<ul id="" class="zab_js_zapadmin_single_ul"></ul>
		}
	},
	checkDaysAlert : function(){
		zapjs.f.message("请选择签到天数")
	},
	removeCurrDiv : function(it){
		$(it).parents(".control-group").remove();
	},
	api_call : function(sTarget, oData, fCallBack) {

		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'betafamilyhas'
		}:{api_key : 'betafamilyhas',api_input:''};
		

		//oData = $.extend({}, defaults, oData || {});

		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			//fCallBack(data);			
			fCallBack(data);
		});

	},
	show_windows : function(activities){
			zapjs.f.window_box({
				id : activities,
				content : '<iframe src="../show/page_chart_v_select_coupon_oc_activity_new3?zw_s_iframe_select_source='+activities+'&zw_s_iframe_select_page=page_chart_v_select_coupon_oc_activity_new3&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.selectCouponActivity.addcb" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	addcb : function(sId,sVal,sText,b,c){
		var activityCode = "";
		$(window.frames[0].document).find('input[name="f_0"]:checked').each(function(){
			activityCode = $(this).val();
        });
		sText = $(window.frames[0].document).find('input[name="f_0"]:checked').parent().parent().parent().children("td:eq(1)").find('div').html();
		if(sId == "activities1"){
			if(selectCouponActivity.validateText(1,activityCode) == 1) return;
			selectCouponActivity.showText(1, activityCode, sText);
		}else if(sId == "activities2"){
			if(selectCouponActivity.validateText(2,activityCode) == 1) return;
			selectCouponActivity.showText(2, activityCode, sText);
		}else if(sId == "activities3"){
			if(selectCouponActivity.validateText(3,activityCode) == 1) return;
			selectCouponActivity.showText(3, activityCode, sText);
		}else if(sId == "activities4"){
			if(selectCouponActivity.validateText(4,activityCode) == 1) return;
			selectCouponActivity.showText(4, activityCode, sText);
		}else if(sId == "activities5"){
			if(selectCouponActivity.validateText(5,activityCode) == 1) return;
			selectCouponActivity.showText(5, activityCode, sText);
		}else if(sId == "activities6"){
			if(selectCouponActivity.validateText(6,activityCode) == 1) return;
			selectCouponActivity.showText(6, activityCode, sText);
		}else if(sId == "activities7"){
			if(selectCouponActivity.validateText(7,activityCode) == 1) return;
			selectCouponActivity.showText(7, activityCode, sText);
		}else if(sId == "activities8"){
			if(selectCouponActivity.validateText(8,activityCode) == 1) return;
			selectCouponActivity.showText(8, activityCode, sText);
		}else if(sId == "activities9"){
			if(selectCouponActivity.validateText(9,activityCode) == 1) return;
			selectCouponActivity.showText(9, activityCode, sText);
		}else if(sId == "activities10"){
			if(selectCouponActivity.validateText(10,activityCode) == 1) return;
			selectCouponActivity.showText(10, activityCode, sText);
		}else if(sId == "activities11"){
			if(selectCouponActivity.validateText(11,activityCode) == 1) return;
			selectCouponActivity.showText(11, activityCode, sText);
		}else if(sId == "activities12"){
			if(selectCouponActivity.validateText(12,activityCode) == 1) return;
			selectCouponActivity.showText(12, activityCode, sText);
		}else if(sId == "activities13"){
			if(selectCouponActivity.validateText(13,activityCode) == 1) return;
			selectCouponActivity.showText(13, activityCode, sText);
		}else if(sId == "activities14"){
			if(selectCouponActivity.validateText(14,activityCode) == 1) return;
			selectCouponActivity.showText(14, activityCode, sText);
		}else if(sId == "activities15"){
			if(selectCouponActivity.validateText(15,activityCode) == 1) return;
			selectCouponActivity.showText(15, activityCode, sText);
		}else if(sId == "activities16"){
			if(selectCouponActivity.validateText(16,activityCode) == 1) return;
			selectCouponActivity.showText(16, activityCode, sText);
		}else if(sId == "activities17"){
			if(selectCouponActivity.validateText(17,activityCode) == 1) return;
			selectCouponActivity.showText(17, activityCode, sText);
		}else if(sId == "activities18"){
			if(selectCouponActivity.validateText(18,activityCode) == 1) return;
			selectCouponActivity.showText(18, activityCode, sText);
		}else if(sId == "activities19"){
			if(selectCouponActivity.validateText(19,activityCode) == 1) return;
			selectCouponActivity.showText(19, activityCode, sText);
		}else if(sId == "activities20"){
			if(selectCouponActivity.validateText(20,activityCode) == 1) return;
			selectCouponActivity.showText(20, activityCode, sText);
		}else if(sId == "activities21"){
			if(selectCouponActivity.validateText(21,activityCode) == 1) return;
			selectCouponActivity.showText(21, activityCode, sText);
		}else if(sId == "activities22"){
			if(selectCouponActivity.validateText(22,activityCode) == 1) return;
			selectCouponActivity.showText(22, activityCode, sText);
		}else if(sId == "activities23"){
			if(selectCouponActivity.validateText(23,activityCode) == 1) return;
			selectCouponActivity.showText(23, activityCode, sText);
		}else if(sId == "activities24"){
			if(selectCouponActivity.validateText(24,activityCode) == 1) return;
			selectCouponActivity.showText(24, activityCode, sText);
		}else if(sId == "activities25"){
			if(selectCouponActivity.validateText(25,activityCode) == 1) return;
			selectCouponActivity.showText(25, activityCode, sText);
		}else if(sId == "activities26"){
			if(selectCouponActivity.validateText(26,activityCode) == 1) return;
			selectCouponActivity.showText(26, activityCode, sText);
		}else if(sId == "activities27"){
			if(selectCouponActivity.validateText(27,activityCode) == 1) return;
			selectCouponActivity.showText(27, activityCode, sText);
		}else if(sId == "activities28"){
			if(selectCouponActivity.validateText(28,activityCode) == 1) return;
			selectCouponActivity.showText(28, activityCode, sText);
		}else if(sId == "activities29"){
			if(selectCouponActivity.validateText(29,activityCode) == 1) return;
			selectCouponActivity.showText(29, activityCode, sText);
		}else if(sId == "activities30"){
			if(selectCouponActivity.validateText(30,activityCode) == 1) return;
			selectCouponActivity.showText(30, activityCode, sText);
		}
		zapjs.f.window_close(sId);
	},
	validateText: function(num,sVal){//验证选择的活动是否重复
		var flag= 0;
		for ( var i = 1; i <= 30; i++) {
			if(sVal!=""&&i !=num && sVal == $("#zw_f_actictityIds"+i).val()){
				zapjs.f.message('送券对应的活动不能重复');
				flag = 1;
				break;
			}
		}
		return flag;
	},
	showText : function(num,sVal,sText){
		$("#zw_f_actictityIds"+num).val(sVal);
		$("#tx"+num).remove();
		$("#ul"+num).remove();
		$("#span"+num).remove();
		if(sText != ""&&sText!= undefined){
			if(num!=30){
				var actexts = sText.split(",");
				$("#ac"+num).after('<div id="tx'+num+'" class="w_left w_w_70p"><ul id="ul'+num+'" class="zab_js_zapadmin_single_ul"></ul></div>');
				for ( var i = 0; i < actexts.length; i++) {
					$("#ul"+num).append('<li>'+actexts[i]+'</li>');
				}
			}else{
				var actexts = sText.split(",");
				$("#ac"+num).after('<span id="span'+num+'" class="zab_js_zapadmin_single_ul" style="display:inline-block;padding: 3px;margin: 3px;border: solid 1px #006475;white-space: nowrap;margin-left:10px"></span>');
				for ( var i = 0; i < actexts.length; i++) {
					$("#span"+num).append('<span>'+actexts[i]+'</span>');
				}
			}
			
		}
	}
};
if (typeof define === "function" && define.amd) {
	define("cmanage/js/selectCouponActivity",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return selectCouponActivity;
	});
}
