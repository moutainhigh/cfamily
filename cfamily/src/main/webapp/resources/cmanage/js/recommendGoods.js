var appColumnAdd ={
	
	init:function (appList){
		
		appColumnAdd.init_appSel(appList);
		//校验输入
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',function(){
			var zw_f_column_nameZero = $("#zw_f_column_nameZero").val();
			var keyValueList = $("#keyValueList").val();
			var keyTextList = $("#keyTextList").val();
			 var reg = new RegExp("^[0-9]*$"); 
			/*if(zw_f_column_nameZero==''){
				zapjs.f.message("请填写序号!");
				return false;
			}*/
			 if(!reg.test(zw_f_column_nameZero)){  
			    zapjs.f.message("序号     必须为数字!");
				return false;
			 } 
			if(keyValueList==''){
				zapjs.f.message("请选择商品!");
				return false;
			}
			if(keyTextList==''){
				zapjs.f.message("请选择商品!");
				return false;
			}
			return true;	
			});	
		
		 
	},
	
	init_appSel:function (appList){
		$.each(appList, function(id, app) {
			$("<option value='"+app.app_code+"'>"+app.app_name+"</option>").appendTo("#zw_f_app_code");
		});
	}
	
};