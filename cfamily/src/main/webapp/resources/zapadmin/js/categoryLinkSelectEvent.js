var categoryLinkSelectEvent ={	
	init:function (sId){
		
		$('#zw_f_selectedProduct_id').attr("disabled","disabled");
		$('#zw_f_link_url').attr("disabled","disabled");
		
		var selectProductObj = $('#form_product_single_id').find('label[for=zw_f_product_code]').eq(0);
		var urlObj = $('#form_product_single_id').find('label[for=zw_f_link_url]').eq(0);
		
		
		$('#'+sId).change(function(){			
			var selectedVal = $('#'+sId).val();
			if(selectedVal == "449747030001") {
				//选择专题链接
				$('#zw_f_selectedProduct_id').attr("disabled","disabled");
				$('#zw_f_link_url').removeAttr("disabled");
				urlObj.prepend('<span class="w_regex_need">*</span>');
				
				//清空数据
				selectProductObj.find('span').remove();
				$("#zw_f_product_code").val("");
				$("#zw_f_product_name").val("");
				$('#zw_f_product_code_show_name').html("");
				$('#zw_f_product_code_show_code').html("");
				
			} else if(selectedVal == "449747030002"){
				//选择商品连接
				$('#zw_f_link_url').attr("disabled","disabled");
				$('#zw_f_selectedProduct_id').removeAttr("disabled");
				selectProductObj.prepend('<span class="w_regex_need">*</span>');
				
				urlObj.find('span').remove();				
				//清空数据
				$('#zw_f_link_url').val("");
			} else {
				
				$('#zw_f_selectedProduct_id').attr("disabled","disabled");
				$('#zw_f_link_url').attr("disabled","disabled");
				//清空数据
				selectProductObj.find('span').remove();
				$("#zw_f_product_code").val("");
				$("#zw_f_product_name").val("");
				$('#zw_f_product_code_show_name').html("");
				$('#zw_f_product_code_show_code').html("");
				$('#zw_f_link_url').val("");
				//删除（必填）标识
				urlObj.find('span').remove();
				selectProductObj.find('span').remove();
			}
		});
		
	}
	
};

