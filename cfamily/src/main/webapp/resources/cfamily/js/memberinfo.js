$("#userinfo").hide();
$("#edu").hide();
$("#shouhuo").hide();
function query(oElm){
	$("#userinfo").hide();
	$("#edu").hide();
	$("#shouhuo").hide();
	$("#tbody").text("");
	phoneNumber = $("#zw_f_phonenumber").val();
	member_code = $("#zw_f_member_code").val();
	if(phoneNumber == "" && member_code == ""){
		return;
	}
	if(!oElm){
	}else{
		if(!checkPhone(phoneNumber)){
			return;
		}
		$("#zw_f_member_code").val("");
	}
	oElm = $("#search");
	var oForm = $(oElm).parents("form");
	zapjs.f.ajaxsubmit(oForm, "../func/" + oElm.attr('zapweb_attr_operate_id'), function(data){
		if(data.resultCode == "1"){
			var datare = data.resultObject;
			memberinfo.phoneNumber = datare.phonenumber;
			$("#phonenumber").text(datare.phonenumber);
			$("#zw_f_phonenumber").val(datare.phonenumber);
			$("#nickname").text(datare.nickname);
			$("#usercode").text(datare.member_code);
			$("#registertime").text(datare.create_time);
			$("#youhuijuan").text(datare.number);
//			$("#jifen").text(datare.jifen);
//			$("#zancunkuan").text(datare.zancunkuan);
//			$("#chuzhijin").text(datare.chuzhijin);
			$("#zw_f_member_code").val(datare.member_code);
			var info = "<span>{0}</span><a class='btn btn-link' href='page_member_v_sc_member_jinfenchart?zw_f_phonenumber={1}&flag={2}' target='_blank'>{3}明细</a>";
			$("#jifen").html(info.format(datare.jifen,datare.phonenumber,"jifen","积分"));
			$("#zancunkuan").html(info.format(datare.zancunkuan,datare.phonenumber,"zancunkuan","暂存款"));
			$("#chuzhijin").html(info.format(datare.chuzhijin,datare.phonenumber,"chuzhijin","储值金"));
//			$("#jifena").click(function(){
//				window.open("page_member_v_sc_member_jinfenchart?zw_f_phonenumber="+datare.phonenumber+"&flag=jifen","_blank"); 
//			});
//			$("#zancunkuana").click(function(){
//				window.open("page_member_v_sc_member_jinfenchart?zw_f_phonenumber="+datare.phonenumber+"&flag=zancunkuan","_blank"); 
//			});
//			$("#chuzhijina").click(function(){
//				window.open("page_member_v_sc_member_jinfenchart?zw_f_phonenumber="+datare.phonenumber+"&flag=chuzhijin","_blank"); 
//			});
			var datarelist = data.resultList;
			var str = "<tr><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td><td><input zapweb_attr_operate_id=\"0d00ca77957811e4be41005056925439\" class=\"btn btn-small\" onclick=\"zapadmin.window_url('page_member_v_sc_member_addressedit?zw_f_uid={4}&member_code={5}')\" type=\"button\" value=\"修改\"> <input zapweb_attr_operate_id=\"6ba8a15ceea8406e8f01e7750c27a357\" class=\"btn btn-small\" onclick=\"func_delete(this,'{6}')\" type=\"button\" value=\"删除\"></td></tr>>";
			for(j = 0,len=datarelist.length; j < len; j++) {
				var r = datarelist[j];
				$("#tbody").append(str.format(r.address_name,r.address_mobile,r.address,r.address_default,r.uid,datare.member_code,r.uid));
			}
			$("#userinfo").show();
			$("#edu").show();
			$("#shouhuo").show();
		}else{
			alert(data.resultMessage);
			$("#userinfo").hide();
			$("#edu").hide();
			$("#shouhuo").hide();
		}
	}, zapjs.zw.func_error)
}
function checkPhone(phone){ 
    if(!(/^1[3456789]\d{9}$/.test(phone))){ 
        alert("手机号码有误，请重填");  
        return false; 
    }else{
    	return true;
    } 
}
function func_delete(oElm,sUid){
	debugger;
	var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
	$(document.body).append(sModel);
	$('#zapjs_f_id_modal_message').html(
			'<div class="w_p_20">删除后不可恢复，确定要删除该地址?</div>');
	var aButtons = [];
	aButtons.push({
		text : '是',
		handler : function() {
			$('#zapjs_f_id_modal_message').dialog('close');
			$('#zapjs_f_id_modal_message').remove();
			deleteaddress(oElm, null, {
				zw_f_uid : sUid
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
}
function deleteaddress(oElm,sOperate,uid){
	sOperate = $(oElm).attr('zapweb_attr_operate_id');
	zapjs.f.ajaxjson("../func/" + sOperate, uid, function(o) {
		switch (o.resultType) {
		case "116018010":
			eval(o.resultObject);
			break;
		default:
			var str = "window.location.href=\"page_member_v_sc_member?zw_f_member_code={0}\"";
		    str = str.format($("#zw_f_member_code").val());
			if (o.resultCode == "1") {
				if (o.resultMessage == "") {
					o.resultMessage = "操作成功";
				}
				zapjs.zw.modal_show({
					content : o.resultMessage,
					//okfunc : 'zapjs.f.autorefresh()'
					okfunc : str
				});
			} else {
				zapjs.zw.modal_show({
					content : o.resultMessage
				});
			}

			break;
		}
	});
}
$("#query").click(function(){
	var phonequery = $("#phonequery").val();
	query(null,phonequery)
});
$("#addAddress").click(function(){
	zapadmin.window_url('page_member_v_sc_member_addressedit?member_code='+$("#zw_f_member_code").val());
});
String.prototype.format=function(){  
	 if(arguments.length==0) 
	 	return this;  
	 for(var s=this, i=0; i<arguments.length; i++)  
		s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);  
	 	return s;  
};
var memberinfo = {
		phoneNumber:"",
		init:function(){
			
		},
		// 提交前处理一些特殊的验证模式
		modBeforesubmit : function() {
			var zw_f_sellPrice = $("#zw_f_sell_price").val();	//销售价格
			
			//防止与editSkuProduct冲突，可根据销售价格来判断，如果值为空，则是进入”修改界面“否则为”销售价、库存修改 “界面
			if (null == zw_f_sellPrice || "" == zw_f_sellPrice) {	
				return true;
			}
//			var zw_f_stock_num =$(".zw_f_stock_num").val();			//	增加或减少的库存数量
			//销售价格的个位不能超过10位
			if (zw_f_sellPrice.substring(0 , zw_f_sellPrice.indexOf(".") == -1 ? zw_f_sellPrice.length :  zw_f_sellPrice.indexOf(".") ).length > 10) {
				zapjs.f.message("*销售价格：值过大，不符合规则，小数点前最长为10位");
				return false;
			}
			//库存变化量值过大，最长为8位
//			if (zw_f_stock_num.length > 8) {
//				zapjs.f.message("库存变化量值过大，最长为8位");
//				return false;
//			}
//			库存变化量不能为空
//			if (null == zw_f_stock_num || "" == zw_f_stock_num) {
//				zapjs.f.message("库存变化量不能为空");
//				return false;
//			}
			return true;
		},
		//输入的必须为正整数
		checkout_number : function(obj){
			 //先把非数字的都替换掉，除了数字和.
	        obj.value = obj.value.replace(/[^\d]/g,"");
		},
		//输入的必须为数字（包括小数）
		checkout_decimal : function(obj){
			 //先把非数字的都替换掉，除了数字和.
	       obj.value = obj.value.replace(/[^\d.]/g,"");
	       //必须保证第一个为数字而不是.
	       obj.value = obj.value.replace(/^\./g,"");
	       //保证只有出现一个.而没有多个.
	       obj.value = obj.value.replace(/\.{2,}/g,".");
	       //保证.只出现一次，而不能出现两次以上
	       obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modSkuStock", function() {
		return modSkuStock;
	});
}
