var zapadmin_apitest = {

	init : function() {
		require(['zapadmin/js/zapadmin_apitree', 'zapadmin/js/zapadmin_md5'], function(a) {
			

			a.tree_init();
		});
		
		
		var sApiKey=zapjs.f.urlget('key');
		var sApiPwd=zapjs.f.urlget('pwd');
		
		$('#api_key').val(sApiKey);
		$('#api_pass').val(sApiPwd);
		
		zapadmin_apitest.show_type('');
		
	},
	
	
	show_type:function(sType)
	{
		if(sType=="")
			{
			
			//$('.apitest_show_priv').hide();
			//$('.apitest_show_token').hide();
			
			}else if(sType=="467701200004")
				{
				//$('.apitest_show_priv').hide();
				//$('.apitest_show_token').show();
				}
			else if(sType=="467701200002")
			{
			//$('.apitest_show_priv').hide();
			//$('.apitest_show_token').hide();
			}
			else
			{
			$('.apitest_show_priv').show();
			//$('.apitest_show_token').show();
			}
	},
	
	
	
	replace_url:function(sSource)
	{
		
		sSource=sSource.replace(/\./g, '_1_1');
		
		sSource=sSource.replace(/[A-Z]/g, function(m){return '_'+m.toLowerCase();});
		
		return sSource;
	},
	

	click_func : function(node) {
		var sId = node.id;

		if (sId.toString().length > 12) {
			$('#manage_apitest_return').html('');
			zapjs.zw.api_call('com_srnpr_zapweb_webapi_InfoApi?zw_f_uid=' + node.attributes.uid,'', function(oData) {
				// alert(data);
				
				if(oData.resultCode!=1)
				{
					zapjs.f.message(oData.resultMessage);
				}
				

				$('#api_target').val(oData.resultObject.class_name.replace(/\./g, '_'));

				var sBaseUrl = "apiinfo?apicode=";
				var sExet = "";

				var aHtml = [];
				aHtml.push('<a href="' + sBaseUrl +oData.resultObject.api_code + sExet + '" target="_blank">接口参数描述</a>');
				aHtml.push("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				/*
				aHtml.push('<a href="' + sBaseUrl + zapadmin_apitest.replace_url(oData.resultObject.javadoc_result) + sExet + '" target="_blank">返回参数描述</a>');
				aHtml.push("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;接口类型：");
				*/
				if(oData.resultObject.api_type=="467701200003")
				{
					aHtml.push('[后台接口]');
				}
				else if(oData.resultObject.api_type=="467701200002")
				{
					aHtml.push('[公开接口]');
				}
				else if(oData.resultObject.api_type=="467701200004")
				{
					aHtml.push('[授权接口]');
				}
				else
				{
					aHtml.push('[私有接口]');
				}
				
				$('#manage_apitest_javadoc').html(aHtml.join(''));
				
				$('#api_input').val(oData.resultObject.template_input);
				
				
				zapadmin_apitest.show_type(oData.resultObject.api_type);
				

			});
		}
	},

	call_api : function() {

		if ($('#api_target').val() == "") {
			alert('目标呢？亲！');
			return;
		}

		if ($('#manage_apitest_timeauto').prop('checked')) {
			$('#api_timespan').val(zapadmin_apitest.date_time('YYYY-MM-DD HH:mm:ss'));
		}

		var sSource = $('#api_target').val() + $('#api_key').val() + $('#api_input').val() + $('#api_timespan').val() + $('#api_pass').val();

		var sMd5 = MD5(sSource);

		if ($('#manage_apitest_checkbox_apisecret').prop('checked')) {
			$('#api_secret').val(sMd5);
		}
		
		var lUrl=location.href;
		
		var aSplitUrl=lUrl.split('/');
		aSplitUrl.pop();
		aSplitUrl.pop();
		
		
		var sUrl = aSplitUrl.join('/')+"/jsonapi/" + $('#api_target').val();
		$('#manage_apitest_link').html(sUrl);

		var queryString = $('#manage_apitest_form').formSerialize();

		//$('#manage_apitest_submit').html(queryString);

		$.post(sUrl, {
			api_key : $('#api_key').val(),
			api_input : $('#api_input').val(),
			api_target : $('#api_target').val(),
			api_secret : $('#api_secret').val(),
			api_token : $('#api_token').val(),
			api_timespan : $('#api_timespan').val()
		}, function(data, status) {

			$('#manage_apitest_return').text(zapjs.f.tojson(data));
		});

	},
	date_time : function(formatStr) {

		var dthis = new Date();

		var str = formatStr;
		var Week = ['日', '一', '二', '三', '四', '五', '六'];

		str = str.replace(/yyyy|YYYY/, dthis.getFullYear());
		str = str.replace(/yy|YY/, (dthis.getYear() % 100) > 9 ? (dthis.getYear() % 100).toString() : '0' + (dthis.getYear() % 100));

		var iMonth = dthis.getMonth() + 1;

		str = str.replace(/MM/, iMonth > 9 ? iMonth.toString() : '0' + iMonth);
		str = str.replace(/M/g, iMonth);

		str = str.replace(/w|W/g, Week[dthis.getDay()]);

		str = str.replace(/dd|DD/, dthis.getDate() > 9 ? dthis.getDate().toString() : '0' + dthis.getDate());
		str = str.replace(/d|D/g, dthis.getDate());

		str = str.replace(/hh|HH/, dthis.getHours() > 9 ? dthis.getHours().toString() : '0' + dthis.getHours());
		str = str.replace(/h|H/g, dthis.getHours());
		str = str.replace(/mm/, dthis.getMinutes() > 9 ? dthis.getMinutes().toString() : '0' + dthis.getMinutes());
		str = str.replace(/m/g, dthis.getMinutes());

		str = str.replace(/ss|SS/, dthis.getSeconds() > 9 ? dthis.getSeconds().toString() : '0' + dthis.getSeconds());
		str = str.replace(/s|S/g, dthis.getSeconds());

		return str;
	}
};

if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/zapadmin_apitest", function() {
		return zapadmin_apitest;
	});
}