
<html class="zab_home_home_html"><head>


	<script type="text/javascript" src="../resources/fileconcat/js-autoconcat.js?v=2.0.0.1"></script>
	
	<link type="text/css" href="../resources/fileconcat/css-autoconcat.css?v=2.0.0.1" rel="stylesheet">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>渠道合作商预付款充值</title>

<#assign channelSellerName = b_page.getReqMap()["channelSellerName"]>
<#assign channelSellerCode = b_page.getReqMap()["channelSellerCode"]>

<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="zapweb/js/zapweb_upload" src="../resources/zapweb/js/zapweb_upload.js"></script></head>
<body class="zab_page_default_body">

	<div class="w_h_20 "></div>
	<div class="zab_page_default_header">
		<div class="zab_page_default_header_title">
			渠道合作商预付款充值
		</div>

 		<div class="btn-group pull-right">

  		</div>
  	</div>
  	<div class="w_h_20 "></div>
	
	<form class="form-horizontal" method="POST">
		<div class="control-group"> 
			<label class="control-label" for="zw_f_channel_seller_name">渠道商名称：</label> 
			<label class="control-label" for="zw_f_channel_seller_name" style="text-align: left;" > ${channelSellerName} </label> 
			<input type="hidden" id="channelSellerCode" name="channelSellerCode" value="${channelSellerCode}"></input> 
		</div> 
		<div class="control-group"> 
			<label class="control-label" for="zw_f_recharge_money"><span class="w_regex_need">*</span>充值金额：</label> 
			<input id="zw_f_recharge_money" name="zw_f_recharge_money"></input> 
		</div> 
		<div class="control-group"> 
			<label class="control-label" for="zw_f_remark"><span class="w_regex_need">*</span>充值备注：</label> 
			<textarea id="zw_f_remark" name="zw_f_remark"></textarea> 
		</div>
		<div class="control-group">
			<label class="control-label" for="zw_f_upload_show">上传充值凭证：</label>
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
	
		<div class="control-group" style="text-align:center; vertical-align:middel;">
			<input type="button" style=" width:100px;" id="submitForm" class="btn  btn-primary" onclick="rechargeSubmit()" zapweb_attr_operate_id="daff3a216d874a0c8eb807f5b29fd019" value="确认充值">
		</div>
		
	</form>
	
	<script type="text/javascript">
	 function rechargeSubmit() {
	 	var channel_seller_code = $("#channelSellerCode").val();
		var recharge_money = $("#zw_f_recharge_money").val();
		var remark = $("#zw_f_remark").val();
		if(recharge_money == null || recharge_money == ""){
			zapjs.f.message("请填写充值金额!");
			return false;
		}
		if(remark == null || remark == ""){
			zapjs.f.message("请填写充值备注!");
			return false;
		}
		if(channel_seller_code == null || channel_seller_code == ""){
			zapjs.f.message("充值失败,请刷新页面重试!");
			return false;
		}
		
		if(confirm("本次充值金额："+recharge_money+"元")){
			var investForm = $("#submitForm").parents("form");
			zapjs.f.ajaxsubmit(
				investForm,
				'../func/'+$("#submitForm").attr('zapweb_attr_operate_id'),
				function(data){
					var o = data;
					switch (o.resultType) {
						case "116018010":
							eval(o.resultObject);
							break;
						default:
							if (o.resultCode == "1") {
								if (o.resultMessage == "") {
									o.resultMessage = "充值成功";
								}
								zapjs.zw.modal_show({
									content : o.resultMessage,
									okfunc : 'zapjs.f.tourl("page_chart_v_uc_channel_seller_advance");'
								});
							} else {
								zapjs.zw.modal_show({
									content : o.resultMessage
								});
							}
							break;
					}
				}, function(data){
					alert('系统出现错误，请联系技术，谢谢！');
				}
			);
			
		}
		
	 }
	</script>
  
</body></html>