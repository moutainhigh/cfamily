$(function() {
		
});

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

function batch_push() {
	var checkUid = new Array();
	$("tbody input[name='auditCheck']").each(function(){
		if(this.checked){
			checkUid.push(this.value);
		}
	});
   
	if(checkUid.length <= 0) {
		alert('请选择要上架的sku!');
		return;
	}
	
	var sOperate = "0745f98cbc9b4c6b8171b7d0da3695c5";
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='goodDoAllPush(&#x27;" + sOperate + "&#x27;, &#x27;" + checkUid + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='pushCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function goodDoAllPush(sOperate, checkUid) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		skuCode: checkUid,
		saleYn: 'Y',
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

function batch_under() {
	var checkUid = new Array();
	$("tbody input[name='auditCheck']").each(function(){
		if(this.checked){
			checkUid.push(this.value);
		}
	});
   
	if(checkUid.length <= 0) {
		alert('请选择要下架的sku!');
		return;
	}
	
	var sOperate = "17330edf53424b26b761b1e059c6116a";
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='goodDoAllUnder(&#x27;" + sOperate + "&#x27;, &#x27;" + checkUid + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='underCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function goodDoAllUnder(sOperate, checkUid) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		skuCode: checkUid,
		saleYn: 'N',
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

function good_push(oElm, skuCode) {
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='goodDoPush(&#x27;" + sOperate + "&#x27;, &#x27;" + skuCode + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='pushCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function goodDoPush(sOperate, skuCode) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		skuCode: skuCode,
		saleYn: 'Y',
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

function pushCancel() {
	zapjs.f.window_close();
}

function good_under(oElm, skuCode) {
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
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='goodDoUnder(&#x27;" + sOperate + "&#x27;, &#x27;" + skuCode + "&#x27;);' value='确定'>" + 
						"<input type='button' style='margin-right:25px;' class='btn  btn-primary' onclick='underCancel();' value='取消'>" + 
					"</div>" +
				"</div>" +
			"</div>";
	showHtml+="</form>";
	modalOption = {content:showHtml,title:"请审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
}

function goodDoUnder(sOperate, skuCode) {
	var zw_bf_remark = $('#zw_bf_remark').val();
	if($.trim(zw_bf_remark) == '') {
		alert('请填写审批意见!');
		return;
	}
	
	zapjs.f.ajaxjson("../func/" + sOperate, 
	{
		skuCode: skuCode,
		saleYn: 'N',
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

function underCancel() {
	zapjs.f.window_close();
}