<html class="zab_home_home_html"><head>


	<script type="text/javascript" src="../resources/fileconcat/js-autoconcat.js?v=2.0.0.1"></script>
	
	<link type="text/css" href="../resources/fileconcat/css-autoconcat.css?v=2.0.0.1" rel="stylesheet">
	<!--[if lte IE 7]> 
	<link type="text/css" href="../resources/zapadmin/hack/zab_base_ie6.css?v=2.0.0.1" rel="stylesheet">
	<![endif]-->
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>商品导入</title>

<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="zapweb/js/zapweb_upload" src="../resources/zapweb/js/zapweb_upload.js"></script></head>
<body class="zab_page_default_body">

<div class="w_h_20 "></div>
<div class="zab_page_default_header">
<div class="zab_page_default_header_title">
商品导入
</div>
 <div class="btn-group pull-right">
 </div>
</div>
  <div class="w_h_20 "></div>
<div class="control-group">
	    <div class="controls">
			<a class="btn  btn-success" href="../resources/cfamily/ImportGoods.xls">下载模板</a>
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
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="7315af82aebc4317aad3a9a715921ca1" onclick="importSubmit()" value="提交新增">
   	</div>
</form>
<script type="text/javascript">
 function importSubmit() {
 	var obj = {};
	obj.upload_show = $('#zw_f_upload_show').val();
	zapjs.zw.api_call('com_cmall_familyhas_api_ApiLabelImportProduct',obj,function(result) {
		if(result.resultCode==1){
			console.log(result);
			if(result.resultMessage!='') {
			    var resultMsg = JSON.parse(result.resultMessage); 
				var success = resultMsg.success;
				var same = resultMsg.same;
				var notFound = resultMsg.notFound;
				var msg = '';
				if(success.length > 0) {
					for(var i = 0; i < success.length; i++) {
						var productInfo = success[i].split(",");
						if(parent.label_product_select.checkProductSame(productInfo[0])) {
							//重复
							if(same.length > 0) {
								same += "," + productInfo[0];
							} else {
								same += productInfo[0] + ",";
							}
							continue;
						} else {
							//不重复
							parent.label_product_select.addProductRow(productInfo[0], productInfo[1]);
						}
					}
				}
				
				if (same.length > 0 && same.substring(same.length-",".length)==",") {
					same = same.substring(0, same.length - 1);
				}	
				
				if(notFound!='') {
					msg = '没有查到的商品: ' + notFound + '<br>';
				}
				if(same!='') {
					msg += '重复导入的商品: ' + same + '<br>';
				}
				parent.label_product_select.close_import_product_win(msg);			
			}
		} else {
			zapadmin.model_message('调用远程接口com_cmall_familyhas_api_ApiLabelImportProduct导入商品失败!');
		}
	});
		
 	//zapjs.zw.func_add(this);
 }
</script>
  
</body></html>