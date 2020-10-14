<html class="zab_home_home_html"><head>
	<script type="text/javascript" src="../resources/fileconcat/js-autoconcat.js?v=2.0.0.1"></script>
	<link type="text/css" href="../resources/fileconcat/css-autoconcat.css?v=2.0.0.1" rel="stylesheet">
	<!--[if lte IE 7]> 
	<link type="text/css" href="../resources/zapadmin/hack/zab_base_ie6.css?v=2.0.0.1" rel="stylesheet">
	<![endif]-->
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量导入商品</title>

<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="zapweb/js/zapweb_upload" src="../resources/zapweb/js/zapweb_upload.js"></script></head>
<body class="zab_page_default_body">
<div class="w_h_20 "></div>
<div class="zab_page_default_header">
<div class="zab_page_default_header_title">
批量导入商品
</div>
 <div class="btn-group pull-right">
 </div></div>
  <div class="w_h_20 "></div>
<div class="control-group">
	    <div class="controls">
			<a class="btn  btn-success" href="../resources/cfamily/liveroom_product_import.xls">下载模板</a>
		</div>
	</div>
<form class="form-horizontal" method="POST">

	


<div class="control-group">
	<label class="control-label" for="zw_f_upload_show"><span class="w_regex_need">*</span>上传文件：</label>
	<div class="controls">
			<input type="hidden" zapweb_attr_target_url="../upload/" zapweb_attr_set_params="" id="zw_f_upload_show" name="zw_f_upload_show" value="">
			<span class="control-upload_iframe"><iframe src="../upload/upload?zw_s_source=zw_f_upload_show&amp;zw_s_description=" class="zw_page_upload_iframe" frameborder="0"></iframe></span>
			<span class="control-upload_process"></span>
			<span class="control-upload"></span>
	</div>
</div>
	

	<script type="text/javascript">
		zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('zw_f_upload_show','../upload/');}); });
	</script>

	  
	<div class="control-group">
    	<div class="controls">
			<input type="button" class="btn  btn-success"  onclick="importSubmit()" value="提交新增">
   	</div>
</form>
<script type="text/javascript">
 function importSubmit() {
    var liveRoomId = up_urlparam("liveRoomId",'');
 	var obj = {liveRoomId};
 	obj.liveRoomId=liveRoomId;
	obj.upload_show = $('#zw_f_upload_show').val();
	zapjs.zw.api_call('com_cmall_familyhas_api_ApiBatchLiveRoomProductImport',obj,function(result) {
	
		if(result.resultCode==1){
		    zapjs.f.window_close("liveroomproduct");
			zapadmin.model_message('操作成功');
			parent.location.reload();
			
		} else {
			zapadmin.model_message(result.resultMessage);
		}
	});			

 }
 
 function up_urlparam(key,sUrl) {
	var sReturn = "";
	if (!sUrl) {
		sUrl = window.location.href;
		if (sUrl.indexOf('?') < 1) {
			sUrl = sUrl + "?";
		}
	}
	var sParams = sUrl.split('?')[1].split('&');

	for (var i = 0, j = sParams.length; i < j; i++) {

		var sKv = sParams[i].split("=");
		if (sKv[0] == key) {
			sReturn = sKv[1];
			break;
		}
	}
	return sReturn;
}
</script>
  
</body></html>