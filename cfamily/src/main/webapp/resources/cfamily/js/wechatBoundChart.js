wechatBoundChart = {
		activity_code:"",
		activity_name:"",
		
		init:function(resultMap){
			wechatBoundChart.activity_code = resultMap.activity_code;
			wechatBoundChart.activity_name = resultMap.activity_name;
			var cId =$("#zw_f_cust_id").val();
			var mCode =$("#zw_f_member_code").val();
			var nName =$("#zw_f_nick_name").val();
			var pNum =$("#zw_f_phone_num").val();
			var tFrom =$("#zw_f_registe_time_zw_a_between_from").val();
			var tTo =$("#zw_f_registe_time_zw_a_between_to").val();
			if(cId==""&&mCode==""&&nName==""&&pNum==""&&tFrom==""&&tTo==""){
				$("table").hide();
				$(".pagination").hide();
			}
		},
	   bachCustIdImport:function(){
		   zapadmin.window_url('../show/page_import_v_mc_member_wechat_bound')	
			},
		
	   boundCounpActivity : function() {

			var showHtml  =  "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'>" 
			    showHtml +=  "<div class='controls'>";
			    showHtml +=  "<p style='font-size:16px'>优惠券活动编号:</p>"
				showHtml +=  "<input id='zw_f_remark' name='zw_f_remark' value="+wechatBoundChart.activity_code+"></input><p style='font-size:14px;margin-top:10px;color:red'>"+wechatBoundChart.activity_name+"</p></div>"
			    showHtml += "<div class='controls'><div class='btn-toolbar'>";

			showHtml += "<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'>&nbsp;&nbsp;&nbsp;&nbsp;</div></div>";
			showHtml += "</form>";
			var modalOption = "";
			modalOption = {
				content : showHtml,
				title : "优惠券活动",
				oktext : "关闭",
				height : 220,
			};
			zapjs.f.window_box(modalOption);
			$("#okId")
					.bind(
							"click",
							function() {
								/*if ($("#zw_f_remark").val() == null||$("#zw_f_remark").val() == "") {
									zapadmin.model_message('请填写活动编号！');
									return;
								}*/
								var rk = $("#zw_f_remark").val();
								var obj = {};
								obj.activity_code = rk;
								zapjs.zw
										.api_call(
												'com_cmall_familyhas_api_ApiForAddWechatBoundActivity',
												obj,
												function(result) {
													if (result.resultCode == 1) {
														$(".panel").hide();
														wechatBoundChart.refreshSelf();
													} else {
														zapadmin
																.model_message(result.resultMessage);
													}
												});
							})

		},
		
		
		issueCoupon : function(memberCode) {

			var showHtml  =  "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'>" 
			    showHtml +=  "<div class='controls'>";
			    showHtml +=  "<p style='font-size:16px'>输入领券编号:</p>"
				showHtml +=  "<input id='zw_f_remark_validate' name='zw_f_remark' ></input><p style='font-size:14px;margin-top:10px;color:red'></p></div>"
			    showHtml += "<div class='controls'><div class='btn-toolbar'>";
				showHtml += "<input type='button' id='okId' style='margin-right:25px;' class='btn  btn-primary' value='确定'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' id='noOkId' style='margin-right:25px;' class='btn  btn-primary' value='取消'></div></div>";
				showHtml += "</form>";
				var modalOption = "";
				modalOption = {
					content : showHtml,
					title : "优惠券编号",
					oktext : "关闭",
					height : 220,
				};
				zapjs.f.window_box(modalOption);
				$("#okId")
						.bind(
								"click",
								function() {
									if ($("#zw_f_remark_validate").val() == null||$("#zw_f_remark_validate").val() == "") {
										zapadmin.model_message('请填写领券编号！');
										return;
									}
									var rk = $("#zw_f_remark_validate").val();
									var obj = {};
									obj.validate_code = rk;
									obj.member_code = memberCode;
									zapjs.zw
											.api_call(
													'com_cmall_familyhas_api_ApiIssueCouponForWeChatBoundUser',
													obj,
													function(result) {
														if (result.resultCode == 1) {
															
															modalOption1 = {
																	content : "<div style='margin-top:50px;' class='controls'><p style='font-size:22px;text-align: center'>优惠券发放成功!</p></div>",
																	height : 220,
																};
															 zapjs.f.window_box(modalOption1);
															 setTimeout(function(){
																 wechatBoundChart.refreshSelf();
															    },1000);
														
														} else {
															zapjs.f.modal({
																content :result.resultMessage,
																okfunc : okfunc,
																id:'zapjs_f_id_modal_message'
															});
															
														}
													});
								})
								
                 $("#noOkId")
						.bind(
								"click",
								function() {
									$(".panel").hide();
									wechatBoundChart.refreshSelf();
								})

			},
	changeWeChatBoundStatus : function(custId, toStatus) {
		
		if(toStatus==0){
			wechatBoundChart.func_tip(custId, toStatus, "解绑微信账号");
		}else{
			wechatBoundChart.createBoundLink(custId,toStatus);
		}
		
	},
	
	createBoundLink : function(custId,toStatus) {
		var obj = {};
		obj.custId = custId;
		obj.toStatus = toStatus;
		zapjs.zw.api_call(
				'com_cmall_familyhas_api_ApiForChangeWeChatBoundStatus',
				obj, function(result) {
					if (result.resultCode == 1) {
						
						var showHtml  =  "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'>" 
						    showHtml +=  "<div class='controls'>";
						    showHtml +=  "<p style='font-size:16px'>绑定微信链接，用户需要用微信打开链接:</p>"
							showHtml +=  "<textarea id='zw_f_remark_link' name='zw_f_remark' style='margin: 0px;width: 220px;height: 90px;'>"+result.resultMessage+"</textarea><p style='font-size:14px;margin-top:10px;color:red'></p></div>"
						    showHtml += "<div class='controls'><div class='btn-toolbar'>";

						showHtml += "<input type='button' id='copyId' style='margin-right:25px;' class='btn  btn-primary' value='复制内容'>&nbsp;&nbsp;&nbsp;&nbsp;</div></div>";
						showHtml += "</form>";
						var modalOption = "";
						modalOption = {
							content : showHtml,
							title : "微信绑定",
							oktext : "关闭",
							height : 280,
						};
						zapjs.f.window_box(modalOption);
						
						
						$("#copyId")
						.bind(
								"click",
								function() {
									var content = $("#zw_f_remark_link").val();
								    var aux = document.createElement("input"); 
								    aux.setAttribute("value", content); 
								    document.body.appendChild(aux); 
								    aux.select();
								    document.execCommand("copy"); 
								    document.body.removeChild(aux);
								    //加一个选中效果作为提醒
								    $("#zw_f_remark_link").focus();
								    setTimeout(function(){
								    	$("#zw_f_remark_link").blur()
								    },250);
								    
								})
		
					} else {
						zapadmin.model_message(result.resultMessage);
					}
				});
	},
	

	refreshSelf : function() {
		var url = window.location.href;
		window.location.href = "";
		window.location.href = url;
	},
	func_tip : function(custId, toStatus, tips) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html(
				'<div class="w_p_20">您确认要' + tips + '么？</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
				$('#zapjs_f_id_modal_message').dialog('close');
				$('#zapjs_f_id_modal_message').remove();
				var obj = {};
				obj.custId = custId;
				obj.toStatus = toStatus;
				zapjs.zw.api_call(
						'com_cmall_familyhas_api_ApiForChangeWeChatBoundStatus',
						obj, function(result) {
							if (result.resultCode == 1) {
								wechatBoundChart.refreshSelf();
							} else {
								zapadmin.model_message(result.resultMessage);
							}
						});

			}
		}, {
			text : '否',
			handler : function() {
				$('#zapjs_f_id_modal_message').dialog('close');
				$('#zapjs_f_id_modal_message').remove();
			}
		});

		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
	},
}