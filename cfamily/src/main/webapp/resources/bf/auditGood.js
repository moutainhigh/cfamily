$(function() {
	//点击全选checkbox，选中所有checkbox
	bindAllBox();
});

function bindAllBox() {
	$("#checkAllOrNone").bind('click', function() {
		$("tbody input[name='auditCheck']").each(function(){
			if(!this.disabled) {
				this.checked = $("#checkAllOrNone").is(':checked');
			}
       });
	});
}

//批量审批
function batch_audit() {
	var checkUid = new Array();
	$("tbody input[name='auditCheck']").each(function(){
		if(this.checked){
			checkUid.push(this.value);
		}
	});
   
	if(checkUid.length <= 0) {
		alert('请选择要推送的sku!');
		return;
	}
	
	var sOperate = "180ccb5c44f745f4b9a939b0a067c967";
	var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'>" +
						"<div class='control-group'>" +
							"<label class='control-label' for='zw_f_source_code'>审批意见:</label>" +
							"<div class='controls'>" +
								"<textarea id='zw_bf_remark' name='zw_f_remark'></textarea>" +
							"</div>" +
						"</div>";
	showHtml+="<div class='control-group'>" +
				"<div class='controls'>" +
					"<div class='btn-toolbar'>" +
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditAllDoGood(&#x27;" + sOperate + "&#x27;, &#x27;" + checkUid + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function auditAllDoGood(sOperate, checkUid) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		sku_codes: checkUid,
		remark: $.trim(zw_bf_remark)
	}, 
	function(data) {
		if(data.resultCode == '0') {
			zapjs.zw.modal_show({
				content : data.resultMessage,
				okfunc : 'zapjs.f.autorefresh()'
			});
		}else {
			zapjs.zw.modal_show({
				content : data.resultMessage
			});
		}
	});
}

function auditGood(oElm, skuCode) {
	var sOperate = $(oElm).attr('zapweb_attr_operate_id');
	var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'>" +
						"<div class='control-group'>" +
							"<label class='control-label' for='zw_f_source_code'>审批意见:</label>" +
							"<div class='controls'>" +
								"<textarea id='zw_bf_remark' name='zw_f_remark'></textarea>" +
							"</div>" +
						"</div>";
	showHtml+="<div class='control-group'>" +
				"<div class='controls'>" +
					"<div class='btn-toolbar'>" +
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditDoGood(&#x27;" + sOperate + "&#x27;, &#x27;" + skuCode + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

//推送
function auditDoGood(sOperate, skuCode) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		sku_codes: skuCode,
		remark: $.trim(zw_bf_remark)
	}, 
	function(data) {
		if(data.resultCode == '0') {
			zapjs.zw.modal_show({
				content : data.resultMessage,
				okfunc : 'zapjs.f.autorefresh()'
			});
		}else {
			zapjs.zw.modal_show({
				content : data.resultMessage
			});
		}
	});
}

//取消推送
function auditCancel() {
	zapjs.f.window_close();
}