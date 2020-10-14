<!DOCTYPE html>
<html>

	<head>
		<script type="text/javascript" src="../resources/fileconcat/js-autoconcat.js?v=2.0.0.1"></script>

		<link type="text/css" href="../resources/fileconcat/css-autoconcat.css?v=2.0.0.1" rel="stylesheet">

		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<title>指定图片缓存-删除</title>

	</head>

	<body class="zab_page_default_body">
		<div class="w_h_20 "></div>
		<div class="w_h_20 "></div>
		<form class="form-horizontal" method="POST">
			<div class="control-group">
				<label class="control-label" for="zw_f_product_name"><span class='w_regex_need'>*</span>图片地址(URL)：</label>
				<div id="test" class="controls">
					<input type="text" id="zw_f_product_name" name="zw_f_product_name" zapweb_attr_regex_id="469923180002" value="" style="width:400px; ">
					<input type='button' class="btn  btn-success" zapweb_attr_operate_id="a5d6a773522d11e6bfb200505692798f" value='添加' onclick="fun()" /><br />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<input type="button" class="btn  btn-success" zapweb_attr_operate_id="a5d6a773522d11e6bfb200505692798f" onclick="onclickDo(this)" value="提交删除" />
				</div>
			</div>
		</form>

	</body>
	<script type="text/javascript">
		function fun() {
			var ipt = document.createElement("input");
			ipt.setAttribute("type", "text");
			ipt.setAttribute("name", "zw_f_product_name");
			var html_ = '<div style="margin-top:10px"><input type="text" name="zw_f_product_name" zapweb_attr_regex_id="469923180002" value="" style="width:400px;">';
			html_ += '<a style="cursor:pointer;" onclick="deleteOne(this)"> 删除 </></div> ';
			// var div = document.getElementById("test").innerHTML += html_;
			$("#test").append(html_);
		}

		function deleteOne(obj) {
			var devEle = $(obj)[0].parentElement;
			$(devEle).remove();
		}

		function onclickDo(obj) {
			var value = '';
			var arr = $("input[name='zw_f_product_name']");
			for(var i = 0; i < arr.length; i++) {
				value += arr[i].value + ',';
			}
			value = value.substring(0, value.length - 1);

			var size = null;
			var opt = new Object();
			opt.imageUrl = value;
			api_call('com_cmall_familyhas_api_ApiDelImagecache', opt, function(result) {
				size = result;
				zapadmin.model_message(result.resultMessage);
			});
			
		}

		function api_call(sTarget, oData, fCallBack) {
			//判断如果传入了oData则自动拼接 否则无所只传入key认证
			var defaults = oData ? {
				api_target: sTarget,
				api_input: zapjs.f.tojson(oData),
				api_key: 'jsapi'
			} : { api_key: 'jsapi', api_input: '' };

			//oData = $.extend({}, defaults, oData || {});

			zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
				//fCallBack(data);			
				fCallBack(data);
			});
		}
	</script>

</html>