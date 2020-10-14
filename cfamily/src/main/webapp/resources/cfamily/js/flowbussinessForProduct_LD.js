
cfamily_flowbussiness = {
		init:function(selectId){
			$("#"+selectId).change(cfamily_flowbussiness.onselect);
		},
		add:function(selectId){
			var selectValue=$("#"+selectId).val();
			
			if(selectValue == null || selectValue == "" )
			{
				var modalOption = {content:"请先选择一个类型!"};
				zapjs.zw.modal_show(modalOption);
				return;
			}
			
			$.get("../show/page_add_v_sc_flow_statuschange_bussiness?zw_f_flow_type="+ selectValue, function(result) {
				$('#zw_page_flow_add').html(result);
			});
		},
		onselect:function(){
			var selectValue=$(this).val();
			$.get("../show/page_chart_v_sc_flow_statuschange?zw_f_flow_type="+ selectValue, function(result) {
				$('#zw_page_flow_chart').html(result);
			});
			
			if($('#zw_page_flow_add').html()!=""){
				$.get("../show/page_add_v_sc_flow_statuschange_bussiness?zw_f_flow_type="+ selectValue, function(result) {
					$('#zw_page_flow_add').html(result);
				});
			}
		},
		onsubmit:function(){
			zapjs.zw.func_add(this);
		},
		callChangeFlow : function(flowType,flowbussinessid){
			
			var url= "../func/115793e80b38485aaba8223e0ea10110?flow_type="+flowType+"&flowbussinessid="+flowbussinessid;
			
			$.getJSON(url, function(data){
				
				$("#flow_form_forsubmit").remove();
				
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_bussinessid' name='zw_f_flow_bussinessid' value='"+flowbussinessid+"'><input type='hidden' id='zw_f_flow_type' name='zw_f_flow_type' value='"+flowType+"'><div class='control-group'><label class='control-label' for='zw_f_source_code'>备注信息:</label><div class='controls'><textarea id='zw_f_remark' name='zw_f_remark'></textarea></div></div>";
				showHtml+="<div class='control-group'><label class='control-label' for='zw_f_source_code'>处理到:</label><div class='controls'><div class='btn-toolbar'>";

				showHtml+=data.resultMessage;
				
				showHtml+="</div></div></div>";
				showHtml+="</form>";
				var modalOption="";
				if(data.resultMessage == ""){
					modalOption = {content:"无处理内容!",title:"请处理",oktext:"关闭",height:200};
				}else{
					modalOption = {content:showHtml,title:"请处理",oktext:"关闭",height:200};
				}
				
				zapjs.f.window_box(modalOption);
				
				$("#flow_form_forsubmit").find("input[zap_tostatus_attr]").bind(
						"click",function(){
							cfamily_flowbussiness.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							//alert(1);
						}
				)
				
			});
		},
		callSubmit : function(obj,toStatus){
			$("#zw_f_to_status").val(toStatus);
			zapjs.zw.func_call(obj);			
		},
		callCopyPic : function(productCode){
			$("#copyPic_form_forsubmit").remove();
			
			var showHtml = "<form class='form-horizontal' id='copyPic_form_forsubmit' method='POST' style='margin-top:20px;'><div><h3 style='text-align:center;' >商品图片复制</h3></div><div class='control-group'><label class='control-label' for='zw_f_source_code'>从：</label><input readonly='readonly' id='zw_f_from_product_code' name='zw_f_from_product_code' value='"
						+productCode+"'></input></div><div class='control-group'><label class='control-label' for='zw_f_source_code'>复制商品图片到：</label><input id='zw_f_to_product_code' name='zw_f_to_product_code'></input></div>";
			showHtml+="<div class='control-group' style='text-align:center; vertical-align:middel;'><input type='button' style='margin-right:90px; width:70px;' id='cancleForm' class='btn  btn-primary' value='取消'><input type='button' style='margin-right:5px; width:70px;' id='submitForm' class='btn  btn-primary' zapweb_attr_operate_id='6aba84ed361e49a381485d431fc2bdf0' value='确定'></div>";
			showHtml+="</form>";
			
			var modalOption="";
			modalOption = {id:"copyPic_form", content:showHtml, title:"商品图片复制", oktext:"关闭", width:542, height:290};
			
			zapjs.f.window_box(modalOption);
			
			$(".panel-tool-max").hide();
			
			$("#cancleForm").bind("click",function(){
				zapjs.f.window_close("copyPic_form");
			});
			
			$("#submitForm").bind("click",function(){
				var from_product_code = $("#zw_f_from_product_code").val();
				var to_product_code = $("#zw_f_to_product_code").val();
				if(from_product_code == null || from_product_code == ""){
					zapjs.f.message("获取商品编号失败!");
					return false;
				}
				if(to_product_code == null || to_product_code == ""){
					zapjs.f.message("请填写要复制到的商品编号!");
					return false;
				}
				
				var copyPicForm = $("#submitForm").parents("form");
				zapjs.f.ajaxsubmit(
					copyPicForm,
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
										o.resultMessage = "操作成功";
									}
									zapjs.zw.modal_show({
										content : o.resultMessage,
										okfunc : 'zapjs.f.tourl("page_productchart_v_cf_pc_skuinfo_view");'
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
			});
		}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/flowbussinessForProduct_LD", [], function() {
		return cfamily_flowbussiness;
	});
}

