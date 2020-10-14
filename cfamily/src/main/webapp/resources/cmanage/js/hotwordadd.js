var appColumnAddHost ={
	
	init:function (app_list_code){
		
		appColumnAddHost.init_appSel_hot(app_list_code);
		//校验输入
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',function(){
			var zw_f_column_nameZero_hot = $("#zw_f_column_nameZero_hot").val();
			var zw_f_column_nameOne_hot = $("#zw_f_column_nameOne_hot").val();
			 var reg =new RegExp("^[0-9]*$"); 
			
			if(zw_f_column_nameOne_hot==''){
				zapjs.f.message("请填写排序!");
				return false;
			}
			 if(!reg.test(zw_f_column_nameOne_hot)){  
			    zapjs.f.message("排序     必须为数字!");
				return false;
			 } 
			if(zw_f_column_nameZero_hot==''){
				zapjs.f.message("请填写关键词!");
				return false;
			}
			if(zw_f_column_nameZero_hot.length>18){
				zapjs.f.message("关键词最多18个字符!");
				return false;
			}
			return true;	
			});	
		
		 
	},
	
	init_appSel_hot:function (app_list_code){
		$.each(app_list_code, function(id, app) {
			$("<option value='"+app.app_code+"'>"+app.app_name+"</option>").appendTo("#zw_f_app_code_hot");
		});
	}
	
};