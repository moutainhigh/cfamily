$(function() {
	$('#zw_f_name').attr('maxlength', 5);
	$('#zw_f_url_address').attr('maxlength', 800);
	
	//图片路径
	var zw_f_image_path = $('#zw_f_image_path');
	if(zw_f_image_path) {
		var controlGroup = zw_f_image_path.parents('.control-group');
		var pathHtmlChar = '<div class="controls" style="color: red;">' + 
						    	'建议尺寸：84px*84px、大小不超过3KB' + 
						    '</div>';
    	controlGroup.append(pathHtmlChar);
	}
	
	//除URL地址类型以外，其余，URL地址不能输入
	var value = $('#zw_f_type').find('option:selected').text();
	if(value != 'URL地址') {
		$('#zw_f_url_address').attr('disabled', 'disabled');
	}else {
		$('#zw_f_url_address').removeAttr('disabled');
	}
	
	//事件绑定
	$('#zw_f_type').bind('change', typeChange);
	
	/*var button = $('#zw_f_type').parent().parent().parent().find('.btn-success');
	var type = $('#zw_f_type').val();
	if(type != 'URL地址') {
		var zw_f_uid = $('#zw_f_uid');
		var content = {};
		content.type = type;
		if(zw_f_uid.length > 0) {
			content.uid = zw_f_uid.val();
		}
		$.ajax({
			url: '../../cfamily/jsonapi/com_cmall_familyhas_api_ApiMyCenterConfig?api_key=betafamilyhas',
			dataType: 'json',
			type: 'post',
			data: content,
			success: function(result) {
				if(result.success) {
					button.removeAttr('disabled');
				}else {
					button.attr('disabled', 'disabled');
					zapjs.zw.modal_show({
						content : result.msg
					});
				}
			}
		});
	}*/
});

function center_func_add(oElm) {
	var zw_f_type = $('#zw_f_type').val();
	if(zw_f_type == '449748030000') {
		zapjs.zw.modal_show({
			content : '请选择类型'
		});
		return;
	}
	if(zw_f_type == '449748030006') {//url地址类型
		var zw_f_url_address = $('#zw_f_url_address').val();
		if($.trim(zw_f_url_address) == '') {
			zapjs.zw.modal_show({
				content : 'URL地址不能为空'
			});
			return;
		}
	}
	
	var zw_f_position = $('#zw_f_position').val();
	$.ajax({
		url: '../../cfamily/jsonapi/com_cmall_familyhas_api_ApiMyCenterGetCount?api_key=betafamilyhas',
		dataType: 'json',
		type: 'post',
		data: {
			isAdd: 'Y',
			position: zw_f_position
		},
		success: function(result) {
			if(result.success) {
				zapjs.zw.func_call(oElm);
			}else {
				zapjs.zw.modal_show({
					content : result.msg
				});
			}
		}
	});
}

function center_func_edit(oElm) {
	var zw_f_type = $('#zw_f_type').val();
	if(zw_f_type == '449748030000') {
		zapjs.zw.modal_show({
			content : '请选择类型'
		});
		return;
	}
	if(zw_f_type == '449748030006') {//url地址类型
		var zw_f_url_address = $('#zw_f_url_address').val();
		if($.trim(zw_f_url_address) == '') {
			zapjs.zw.modal_show({
				content : 'URL地址不能为空'
			});
			return;
		}
	}
	
	var zw_f_uid = $('#zw_f_uid').val();
	var zw_f_position = $('#zw_f_position').val();
	$.ajax({
		url: '../../cfamily/jsonapi/com_cmall_familyhas_api_ApiMyCenterGetCount?api_key=betafamilyhas',
		dataType: 'json',
		type: 'post',
		data: {
			isAdd: 'N',
			zw_f_uid: zw_f_uid,
			position: zw_f_position
		},
		success: function(result) {
			if(result.success) {
				zapjs.zw.func_call(oElm);
			}else {
				zapjs.zw.modal_show({
					content : result.msg
				});
			}
		}
	});
}

function typeChange() {
	var value = $('#zw_f_type').find('option:selected').text();
	if(value == 'URL地址') {
		$('#zw_f_url_address').removeAttr('disabled');
	}else {
		$('#zw_f_url_address').attr('disabled', 'disabled');
		$('#zw_f_url_address').val('');
	}
	
	var button = $('#zw_f_type').parent().parent().parent().find('.btn-success');
	var type = $('#zw_f_type').val();
	if(type != 'URL地址') {
		var zw_f_uid = $('#zw_f_uid');
		var content = {};
		content.type = type;
		if(zw_f_uid.length > 0) {
			content.uid = zw_f_uid.val();
		}
		$.ajax({
			url: '../../cfamily/jsonapi/com_cmall_familyhas_api_ApiMyCenterConfig?api_key=betafamilyhas',
			dataType: 'json',
			type: 'post',
			data: content,
			success: function(result) {
				if(result.success) {
					button.removeAttr('disabled');
				}else {
					button.attr('disabled', 'disabled');
					zapjs.zw.modal_show({
						content : result.msg
					});
				}
			}
		});
	}
}