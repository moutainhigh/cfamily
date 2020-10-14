
flowbussinessForBrandPreference = {
		init:function(selectId){
			$("#"+selectId).change(flowbussinessForBrandPreference.onselect);
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
			
			var url= "../func/116780e80b44485iomx8223e0eaal110?flow_type="+flowType+"&flowbussinessid="+flowbussinessid;
			
			
			$.getJSON(url, function(data){
				
				$("#flow_form_forsubmit").remove();
				
				var showHtml = "<form class='form-horizontal' id='flow_form_forsubmit' method='POST' style='margin-top:20px;'><input type='hidden' id='zw_f_to_status' name='zw_f_to_status' value=''><input type='hidden' id='zw_f_flow_bussinessid' name='zw_f_flow_bussinessid' value='"+flowbussinessid+"'><input type='hidden' id='zw_f_flow_type' name='zw_f_flow_type' value='"+flowType+"'>";
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
						flowbussinessForBrandPreference.callSubmit(this,$(this).attr("zap_tostatus_attr"));
							//alert(1);
						}
				)
				
			});
		},
		callSubmit : function(obj,toStatus){
			$("#zw_f_to_status").val(toStatus);
			zapjs.zw.func_call(obj);			
		}
};

if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/flowbussinessForBrandPreference", [], function() {
		return flowbussinessForBrandPreference;
	});
}

