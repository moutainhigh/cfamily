/*
 * zapweb_upload
 * 文件上传类
 */

var zapweb_upload = {

	// 上传显示
	upload_html : function(sTargetUpload, sId, sValue, sSet) {

		if (!sSet) {
			sSet = '';
		}

		var sNumber = zapjs.f.upset(sSet, 'zw_s_number');
		var iMaxNum = 1;
		if (sNumber != "") {
			iMaxNum = parseInt(sNumber);
		}

		return '<input type="hidden" zapweb_attr_target_url="'
				+ sTargetUpload
				+ '"  zapweb_attr_set_params="'
				+ sSet
				+ '"  id="'
				+ sId
				+ '" name="'
				+ sId
				+ '" value="'
				+ sValue
				+ '"  '
				+ (iMaxNum > 1 ? ('multiple="multiple"') : 0)
				+ '><span class="control-upload_iframe"></span><span class="control-upload"></span>';

	},

	// 上传文件
	upload_file : function(sFieldName, sUploadUrl) {

		// zapjs.f.setdomain();
		// sUploadUrl = "../upload/";
		zapweb_upload.upload_show(sFieldName);

	},

	is_image : function(sFix) {

		var sImageFile = ".jpg;.png;.jpeg;.bmp;.gif;";
		
		if(sFix != '' && sFix.indexOf('?') > 0){
			sFix = sFix.substring(0,sFix.indexOf('?'));
		}

		return sImageFile.indexOf('.' + sFix + ';') > -1;

	},

	// 上传文件结果
	upload_result : function(o) {
		// zapjs.f.setdomain();
		parent.zapweb_upload.upload_success(o, zapjs.f.urlget('zw_s_source'));
	},
	// 上传展示
	upload_show : function(sField) {
		var bFlagMul = false;

		var sSetParams = $('#' + sField)
				.attr(zapjs.c.field_attr + "set_params");

		// 定义最大上传数量
		var sNumber = zapjs.f.upset(sSetParams, 'zw_s_number');

		if (sNumber != "") {

			var iMax = parseInt(sNumber);
			var iNow = $('#' + sField).val().split(zapjs.c.split).length;

			if (iNow < iMax) {

				bFlagMul = true;
			}

		}
		// 定义上传目标地址
		var sUploadTarget = zapjs.f.upset(sSetParams, 'zw_s_target');
		if (!sUploadTarget) {
			sUploadTarget = "upload";
		}
		// 定义显示风格 如果定义为list的话则为顺序排列
		var sUploadView = zapjs.f.upset(sSetParams, 'zw_s_view');
		if(sUploadView == "" || sUploadView == undefined){
			if(sField == "zw_f_adpic_url" || sField == "zw_f_pic_material_upload" ||sField == "zw_f_video_main_pic" ){
				sUploadView = "13";
			}
		}
		// 定义是否带有链接参数
		var sUploadLink = zapjs.f.upset(sSetParams, 'zw_s_link');

		// 定义是否带有标题参数
		var sUploadTitle = zapjs.f.upset(sSetParams, 'zw_s_title');

		// 定义是否带有描述信息
		var sDescription = zapjs.f.upset(sSetParams, 'zw_s_description');
		if ($('#' + sField).val() == "" || bFlagMul) {
			if ($('#' + sField).nextAll('.control-upload_iframe').html() == "") {
				var sUploadUrl = $('#' + sField).attr(
						zapjs.c.field_attr + "target_url");

				$('#' + sField)
						.nextAll('.control-upload_iframe')
						.html(
								'<iframe src="'
										+ sUploadUrl
										+ sUploadTarget
										+ '?zw_s_source='
										+ sField
										+ '&zw_s_description='
										+ sDescription
										+ '" class="zw_page_upload_iframe" frameborder="0" id="zw_page_upload_iframe_' + sField + '"></iframe>');
				console.info("sField==>:"+sUploadUrl+sUploadTarget+'?zw_s_source='+sField)
			}
		} else {
			$('#' + sField).nextAll('.control-upload_iframe').html('');
		}

		if ($('#' + sField).val() != "") {
			var sFiles = $('#' + sField).val().split(zapjs.c.split);
			if ($('#' + sField).val() != "" && sFiles.length > 0) {

				var aHtml = [];

				aHtml.push('<ul>');

				for (var i = 0, j = sFiles.length; i < j; i++) {

					var aFname = sFiles[i].split('.');

					var sFix = aFname[aFname.length - 1];

					var sShowHex = zapweb_upload.is_image(sFix) ? ('<img src="'
							+ sFiles[i] + '" />') : sFix;

					if (sUploadView == "12") {
						aHtml
								.push('<li class="control-upload-image-li"><div class="control-upload-image-div"><a href="'
										+ sFiles[i]
										+ '" target="_blank">'
										+ sShowHex
										+ '</a></div><div class="control-upload-delete"><span class="btn btn-mini " onclick="zapweb_upload.upload_delete(\''
										+ sField
										+ '\','
										+ i
										+ ')"><i class="icon-trash "></i>&nbsp;&nbsp;删除</span></div></li>');
					}else if(sUploadView == "13"){
						var sView = '';

						aHtml.push('<li class="control-upload-list-li"><div>');

						aHtml.push('<div class="w_left w_w_100"><a href="'
								+ sFiles[i] + '" target="_blank">' + sShowHex
								+ '</a></div>');

						aHtml.push('<div class="w_left w_p_10">'
										+ '<a href="javascript:zapweb_upload.change_index(\''
										+ sField + '\',' + i
										+ ',\'delete\')">删除</a>'); 
						aHtml.push("</div>");
						aHtml.push('<div class="w_clear"></div>');
					} else {

						var sView = '';

						aHtml.push('<li class="control-upload-list-li"><div>');

						if("videocc"==sUploadTarget){
							aHtml.push('<div class="w_left w_w_100">' + sShowHex
									+ '</div>');
						}else{
							aHtml.push('<div class="w_left w_w_100"><a href="'
									+ sFiles[i] + '" target="_blank">' + sShowHex
									+ '</a></div>');
						}

						aHtml.push('<div class="w_left w_p_10">'
										+ (j > 1 ? '<a href="javascript:zapweb_upload.change_index(\''
												+ sField
												+ '\','
												+ i
												+ ',\'up\')">上移</a><a href="javascript:zapweb_upload.change_index(\''
												+ sField
												+ '\','
												+ i
												+ ',\'down\')">下移</a>'
												: '')
										+ '<a href="javascript:zapweb_upload.change_index(\''
										+ sField + '\',' + i
										+ ',\'delete\')">删除</a>');     
										
										
										if($("#cfamily_upload_iframe_zw_f_piclist").val()!=undefined){
											var modifyButton = '<a href="javascript:zapweb_upload.change_index(\''
											+ sField + '\',' + i
											+ ',\'modifyForPic\')">修改</a>';
											aHtml.push(modifyButton);
										}
										
										aHtml.push("</div>");
										

						aHtml.push('<div class="w_clear"></div>');

						if (sUploadLink != "") {
							var sLinks = $('#' + sUploadLink).val().split(
									zapjs.c.split);

							var sLinkUrl = sLinks[i] ? sLinks[i] : '';

							aHtml.push('<div>链接地址：<input id="' + sUploadLink
									+ '_zapweb_upload_link_' + i
									+ '" type="text" value="' + sLinkUrl
									+ '"/></div>');

						}

						if (sUploadTitle != "") {
							var sTitles = $('#' + sUploadTitle).val().split(
									zapjs.c.split);

							var sTitleUrl = sTitles[i] ? sTitles[i] : '';

							aHtml.push('<div>标题信息：<input id="' + sUploadTitle
									+ '_zapweb_upload_title_' + i
									+ '" type="text" value="' + sTitleUrl
									+ '"/></div>');

						}

						aHtml.push('</div></li>');
					}
				}

				aHtml.push('</ul>');

				$('#' + sField).nextAll('.control-upload').html(aHtml.join(''));
			}
		} else {

			$('#' + sField).nextAll('.control-upload').html('');
		}
	},
	array_change : function(aArray, iOne, iTwo) {
		if (iOne < 0 || iTwo < 0 || iOne > aArray.length - 1
				|| iTwo > aArray.length - 1) {
			return aArray;
		}

		var sTemp = aArray[iOne];
		aArray[iOne] = aArray[iTwo];
		aArray[iTwo] = sTemp;
		return aArray;
	},

	save_link : function(sField) {

		var sSetParams = $('#' + sField)
				.attr(zapjs.c.field_attr + "set_params");
		var sUploadLink = zapjs.f.upset(sSetParams, 'zw_s_link');
		if (sUploadLink != "") {
			var sFiles = zapjs.f.split($('#' + sField).val(), zapjs.c.split);

			var aLinks = [];
			for (var i = 0, j = sFiles.length; i < j; i++) {
				var sLinkUrl = zapjs.f.formatsplit($(
						'#' + sUploadLink + '_zapweb_upload_link_' + i).val());
				aLinks.push(sLinkUrl ? sLinkUrl : '');

			}
			$('#' + sUploadLink).val(aLinks.join(zapjs.c.split));
		}

	},
	save_title : function(sField) {

		var sSetParams = $('#' + sField)
				.attr(zapjs.c.field_attr + "set_params");
		var sUploadTitle = zapjs.f.upset(sSetParams, 'zw_s_title');
		if (sUploadTitle != "") {
			var sFiles = zapjs.f.split($('#' + sField).val(), zapjs.c.split);

			var aTitles = [];
			for (var i = 0, j = sFiles.length; i < j; i++) {
				var sTitleUrl = zapjs.f
						.formatsplit($(
								'#' + sUploadTitle + '_zapweb_upload_title_'
										+ i).val());
				aTitles.push(sTitleUrl ? sTitleUrl : '');

			}
			$('#' + sUploadTitle).val(aTitles.join(zapjs.c.split));
		}

	},

	// 更改
	change_index : function(sField, iIndex, sLink) {
		var sFiles = zapjs.f.split($('#' + sField).val(), zapjs.c.split);

		var sLinks = [];
		var sTitles = [];

		var sSetParams = $('#' + sField)
				.attr(zapjs.c.field_attr + "set_params");
		var sUploadLink = zapjs.f.upset(sSetParams, 'zw_s_link');
		if (sUploadLink != "") {
			zapweb_upload.save_link(sField);

			sLinks = zapjs.f.split($('#' + sUploadLink).val(), zapjs.c.split);
		}

		var sUploadTitle = zapjs.f.upset(sSetParams, 'zw_s_title');
		if (sUploadTitle != "") {
			zapweb_upload.save_title(sField);

			sTitles = zapjs.f.split($('#' + sUploadTitle).val(), zapjs.c.split);
		}

		// 如果是添加
		if (iIndex == -99) {

			// 开始判断数量限制
			var iMaxNumber = 1;
			var sMax = zapjs.f.upset(sSetParams, 'zw_s_number');
			if (sMax != "") {
				iMaxNumber = parseInt(sMax);
			}
			// 定义如果超出最大上传数量限制 则返回
			if (sFiles.length >= iMaxNumber) {
				
				alert("最多允许上传文件数量："+iMaxNumber);
				return;
			}

			sFiles.push(sLink);
			sLinks.push('');
			sTitles.push('');

		} else if (sLink == "delete") {
			sFiles.splice(iIndex, 1);
			if (sUploadLink != "") {
				sLinks.splice(iIndex, 1);
			}
			if (sUploadTitle != "") {
				sTitles.splice(iIndex, 1);
			}

		} else if (sLink == "up") {
			sFiles = zapweb_upload.array_change(sFiles, iIndex, iIndex - 1);
			if (sUploadLink != "") {
				sLinks = zapweb_upload.array_change(sLinks, iIndex, iIndex - 1);
			}
			if (sUploadTitle != "") {
				sTitles = zapweb_upload.array_change(sTitles, iIndex,
						iIndex - 1);
			}
		} else if (sLink == "down") {

			sFiles = zapweb_upload.array_change(sFiles, iIndex, iIndex + 1);
			if (sUploadLink != "") {
				sLinks = zapweb_upload.array_change(sLinks, iIndex, iIndex + 1);
			}
			if (sUploadTitle != "") {
				sTitles = zapweb_upload.array_change(sTitles, iIndex,
						iIndex + 1);
			}

		} else if (sLink == "modifyForPic") {

			$("#zw_page_upload_iframe_zw_f_piclist").contents().find("#file").click();
			$("#cfamily_upload_iframe_zw_f_piclist").val(iIndex+'_true');

		}

		$('#' + sField).val(sFiles.join(zapjs.c.split));

		if (sUploadLink != "") {
			$('#' + sUploadLink).val(sLinks.join(zapjs.c.split));
		}

		if (sUploadTitle != "") {
			$('#' + sUploadTitle).val(sTitles.join(zapjs.c.split));
		}
		zapweb_upload.upload_show(sField);

	},
	// 替换上传文件
	upload_replace : function(sField, iIndex, sLink) {
		var sFiles = zapjs.f.split($('#' + sField).val(), zapjs.c.split);

		var sLinks = [];
		var sTitles = [];

		var sSetParams = $('#' + sField)
				.attr(zapjs.c.field_attr + "set_params");
		var sUploadLink = zapjs.f.upset(sSetParams, 'zw_s_link');
		if (sUploadLink != "") {
			zapweb_upload.save_link(sField);

			sLinks = zapjs.f.split($('#' + sUploadLink).val(), zapjs.c.split);
		}

		var sUploadTitle = zapjs.f.upset(sSetParams, 'zw_s_title');
		if (sUploadTitle != "") {
			zapweb_upload.save_title(sField);

			sTitles = zapjs.f.split($('#' + sUploadTitle).val(), zapjs.c.split);
		}
		
		//开始替换
		sFiles[iIndex] = sLink;
		
		$('#' + sField).val(sFiles.join(zapjs.c.split));

		if (sUploadLink != "") {
			$('#' + sUploadLink).val(sLinks.join(zapjs.c.split));
		}

		if (sUploadTitle != "") {
			$('#' + sUploadTitle).val(sTitles.join(zapjs.c.split));
		}
		zapweb_upload.upload_show(sField);
	},
	// 删除上传文件
	upload_delete : function(sField, iIndex) {
		/*
		 * var sFiles = $('#' + sField).val().split(zapjs.c.split);
		 * 
		 * sFiles.splice(iIndex, 1);
		 * 
		 * $('#' + sField).val(sFiles.join(zapjs.c.split));
		 * 
		 * zapweb_upload.upload_show(sField);
		 */
		zapweb_upload.change_index(sField, iIndex, 'delete');

	},
	// 上传成功
	upload_success : function(o, sField) {
		// alert(sField);

		// alert(o.resultObject);

		if (o.resultCode == 1) {
			if (o.resultObject) {
				/*
				 * var sVal = $('#' + sField).val(); if (sVal != "") { sVal =
				 * sVal + zapjs.c.split; } sVal = sVal + o.resultObject;
				 * 
				 * $('#' + sField).val(sVal);
				 * 
				 * zapweb_upload.upload_show(sField);
				 */

		
				
				
				
				
				
				var hasObj = $("#cfamily_upload_iframe_zw_f_piclist").val();
				if(hasObj!=undefined){
					var iIndex = $("#cfamily_upload_iframe_zw_f_piclist").val().split('_')[0];
					var flag = $("#cfamily_upload_iframe_zw_f_piclist").val().split('_')[1];
					if("true" == flag){
						// 如果是单文件上传 则替换第一个文件
						if (o.resultList != null && o.resultList.length > 1) {
							zapweb_upload.upload_replace(sField, iIndex, o.resultList[0]);
						} else {
							zapweb_upload.upload_replace(sField, iIndex, o.resultObject);
						}
					} else {
						// 如果是多文件上传 则开始插入多个文件
						if (o.resultList != null && o.resultList.length > 1) {
							for ( var i in o.resultList) {
								zapweb_upload
										.change_index(sField, -99, o.resultList[i]);
							}

						} else {
							zapweb_upload.change_index(sField, -99, o.resultObject);
						}

					
					}
				}
				else{
					// 如果是多文件上传 则开始插入多个文件
					if (o.resultList != null && o.resultList.length > 1) {
						for ( var i in o.resultList) {
							zapweb_upload
									.change_index(sField, -99, o.resultList[i]);
						}

					} else {
						zapweb_upload.change_index(sField, -99, o.resultObject);
					}
				}

				
				if (zapjs.f.callextend("zapjs_e_zapweb_upload_upload_success")) {

				}
				
				zapweb_upload.saveSize(o.sizeMap);
				
			}
		} else {
			alert(o.resultMessage);
		}
		$("#cfamily_upload_iframe_zw_f_piclist").val('-1_false');
	},
	// 上传提交
	upload_upload : function(oElm,allowUploadFileSize) {
		
		var uploadFILE = $("#formsubmit").parents("form").find("#file")[0].files[0];
		var uplaodFile_name = uploadFILE.name;
		var uploadFIle_type = uplaodFile_name.substr(uplaodFile_name.lastIndexOf("\."));
		var t_size = -1;
		var allowFileSize = allowUploadFileSize.split(";");
		if(undefined != allowFileSize && null != allowFileSize && allowFileSize.length > 0) {
			for(var j = 0;j<allowFileSize.length;j++) {
				var filetype_size = allowFileSize[j].split("-");
				if(filetype_size[0] == uploadFIle_type) {
					t_size = filetype_size[1];
					break;
				}
			}
		}
		if(t_size != -1) {
			if(Math.ceil(uploadFILE.size/1000) > t_size) {
				alert("上传文件超过"+t_size+"KB");
				return false;
			}
		}

		$('form').parent().parent().hide();
		$('body').append('<span class="panel-loading">正在上传，请稍后……</span>');
		$('#formsubmit').click();
	},
	//保存上传文件大小
	saveSize : function(info) {
		for(var key in info) {
			zapweb_upload.imgSizeMap[key] = info[key];
		}  
	},
	//存放图片的大小（单位：B）
	imgSizeMap : {}
};

if (typeof define === "function" && define.amd) {
	define("zapweb/js/zapweb_upload", function() {
		return zapweb_upload;
	});
}
