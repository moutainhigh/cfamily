var cmanage = {};
cmanage.propertyInfo = {
		init:function(selectId){
			$("#"+selectId).change(cmanage.propertyInfo.onselect);
		},
		
		onselect:function(){
			$('#zw_page_three_button').attr("style","visibility:hidden");
			$('#zw_page_property_three_chart').attr("style","visibility:hidden");
			var selectValue=$(this).val();
			
			$.get("../show/page_second_list_v_cf_pc_propertyinfo?zw_f_parent_code="+selectValue,function(result1){
			var url = "../page/page_second_list_v_cf_pc_propertyinfo?zw_f_parent_code="+selectValue;
			$('#zw_page_property_chart').html("<iframe width='98%' height='98%' frameborder=0 border='0'  class='zab_home_home_iframe' " +
					" onload='zapadmin.load_complate(this)'" +
					"'  src='"+url+"'></iframe>");
			$('#zw_page_second_button').attr("style","true");
			});
		},
		addSecondMenu:function(selectId)
		{
			var  selectValue=$("#"+selectId).val();
		
			
			zapadmin.window_show({
				
				url : '../show/page_add_v_cf_pc_propertyinfo?zw_f_parent_code='+$.trim(selectValue),
				height:200
			});
			
			
			/**
			 * 
			 * zapadmin.window_show({
			url : '../show/page_modifystock_v_pc_skuinfo_view?zw_f_product_code=[@this$product_code]',
			width:800,
			height:400,
		})
			 */
			
		},
		
		addThirdMenu:function(selectId)
		{
			//zapadmin.window_url('../show/page_add_v_cf_pc_propertyinfo?zw_f_parent_code='+ thirdId);
			
			
			zapadmin.window_show({
				
				url : '../show/page_add_v_cf_pc_propertyinfo?zw_f_parent_code='+ thirdId,
				height:200
			});
		},

		three:function(a,parentCode)
		{
			thirdId =parentCode;
			$.get("../show/page_chart_three_v_cf_pc_propertyinfo?zw_f_parent_code="+$.trim(parentCode),function(result){
		    var url	= "../page/page_chart_three_v_cf_pc_propertyinfo?zw_f_parent_code="+$.trim(parentCode);
			$('#zw_page_property_three_chart').html("<iframe  width='98%' height='800px'  frameborder=0 border='0' class='zab_home_home_iframe' " +
					" onload='zapadmin.load_complate(this)'" +
					"'  src='"+url+"'></iframe>");
			$('#zw_page_three_button').attr("style","true");
			$('#zw_page_property_three_chart').attr("style","true");
			});
		}
};
