$(function () {
	//点击全选checkbox，选中所有checkbox
	bindAllBox();
});

function bindAllBox() {
	$("#checkAllOrNone").bind('click', function() {
		$("tbody input[name='auditCheck']").each(function(){
            this.checked = $("#checkAllOrNone").is(':checked');
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
	
	var sOperate = "6e848aa8fe754e0abe9194bc8b0890c6";
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditAllDoBf(&#x27;" + sOperate + "&#x27;, &#x27;" + checkUid + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function auditAllDoBf(sOperate, checkUid) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		sku_code: checkUid,
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

//批量驳回
function batch_reject() {
	var checkUid = new Array();
	$("tbody input[name='auditCheck']").each(function(){
		if(this.checked){
			checkUid.push(this.value);
		}
	});
   
	if(checkUid.length <= 0) {
		alert('请选择要驳回的sku!');
		return;
	}
	
	var sOperate = "2962bf1e8ab24b308056761d7e3cdba5";
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='rejectAllDoBf(&#x27;" + sOperate + "&#x27;, &#x27;" + checkUid + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='rejectCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function rejectAllDoBf(sOperate, checkUid) {
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


function auditOperate(oElm, skuCode) {
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditDoBf(&#x27;" + sOperate + "&#x27;, &#x27;" + skuCode + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='auditCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function rejectOperate(oElm, skuCode) {
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='rejectDoBf(&#x27;" + sOperate + "&#x27;, &#x27;" + skuCode + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='rejectCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function auditDoBf(sOperate, skuCode) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		sku_code: skuCode,
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

function auditCancel() {
	zapjs.f.window_close();
}

function rejectDoBf(sOperate, skuCode) {
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

function rejectCancel() {
	zapjs.f.window_close();
}