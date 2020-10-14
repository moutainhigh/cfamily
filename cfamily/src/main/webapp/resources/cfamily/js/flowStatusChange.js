var cfamily = {};
cfamily.flowStatusChange = {
		init:function(selectId){
			$("#"+selectId).change(cfamily.flowStatusChange.onselect);
		},
		add:function(selectId){
			var selectValue=$("#"+selectId).val();
			
			if(selectValue == null || selectValue == "" )
			{
				var modalOption = {content:"请先选择一个类型!"};
				zapjs.zw.modal_show(modalOption);
				return;
			}
			
			$.get("../show/page_add_v_cf_sc_flow_statuschange?zw_f_flow_type="+ selectValue, function(result) {
				$('#zw_page_flow_add').html(result);
			});
		},
		onselect:function(){
			var selectValue=$(this).val();
			$.get("../show/page_chart_v_cf_sc_flow_statuschange?zw_f_flow_type="+ selectValue, function(result) {
				$('#zw_page_flow_chart').html(result);
			});
			
			if($('#zw_page_flow_add').html()!=""){
				$.get("../show/page_add_v_cf_sc_flow_statuschange?zw_f_flow_type="+ selectValue, function(result) {
					$('#zw_page_flow_add').html(result);
				});
			}
		},
		onsubmit:function(){
			zapjs.zw.func_add(this);
		}
};