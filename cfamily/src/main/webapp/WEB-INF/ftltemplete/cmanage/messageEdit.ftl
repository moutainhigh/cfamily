
<@m_zapmacro_common_page_edit b_page />
<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="presubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>

<script>
	$(document).ready(function(){
		var type = $("#zw_f_type").val();
		var ifPush = $("#zw_f_if_push").val();
		if(type == "4497471600410001"){
			$("#zw_f_url").parent().parent().hide();
			$("#zw_f_product_code").parent().parent().parent().hide();
		}else if(type == "4497471600410002"){
			$("#zw_f_url").parent().parent().show();
			$("#zw_f_product_code").parent().parent().parent().hide();
		}else if(type == "4497471600410003"){
			$("#zw_f_url").parent().parent().hide();
			$("#zw_f_product_code").parent().parent().parent().show();
		}
		if(ifPush == "4497477800090002"){
			$("#zw_f_push_time").parent().parent().hide();
			$("#zw_f_push_time").val("");
		}else{
			$("#zw_f_push_time").parent().parent().show();
		}
	});
	
	function presubmit(obj) {
		if($("#zw_f_type").val() == "4497471600410003") {
			if($("#zw_f_product_code").val() == ""){
				zapjs.f.modal({
					content : '商品类型消息必须选择商品!'
				});
				return;
			}			
		}
		if($("#zw_f_type").val() == "4497471600410002") {
			if($("#zw_f_url").val() == ""){
				zapjs.f.modal({
					content : 'url类型消息必须填写链接地址!'
				});
				return;
			}			
		}
		//判断是否选是，如果选是则校验推送时间是否为null
		if($("#zw_f_if_push").val() == '4497477800090001'){
			var push_time = $("#zw_f_push_time").val();
			if( push_time == '' || push_time == null){
				zapjs.f.modal({
					content : '推送时间不能为空!'
				});
				return;
			}
		}else{
			$("#zw_f_push_time").val("2019-08-13 00:00:00")
		}
		zapjs.zw.func_edit(obj);
	}
	
	$("#zw_f_if_push").change(function(){
		var obj_val = $(this).val();
		if("4497477800090002"==obj_val){
			$("#zw_f_push_time").parent().parent().hide();
			$("#zw_f_push_time").val("");
		}else if("4497477800090001"==obj_val){
			$("#zw_f_push_time").parent().parent().show();
		}				
	})
	
	$("#zw_f_type").change(function(){
		var obj_val = $(this).val();
		if("4497471600410001"==obj_val){
			$("#zw_f_url").parent().parent().hide();
			$("#zw_f_product_code").parent().parent().parent().hide();
		}else if("4497471600410002"==obj_val){
			$("#zw_f_url").parent().parent().show();
			$("#zw_f_product_code").parent().parent().parent().hide();
		}else if("4497471600410003"==obj_val){
			$("#zw_f_url").parent().parent().hide();
			$("#zw_f_product_code").parent().parent().parent().show();
		}				
	})
</script>


