var checkMessageCategory = {
		
		temp : {
			data : [],
			app_code:"SI2003"
		},
		
		tree_init : function() {
			
			var obj = {};
			obj.app_code = checkMessageCategory.temp.app_code;
			
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForMessageCategoryTree',obj,function(result) {
				
				checkMessageCategory.temp.data = result.list;
				checkMessageCategory.tree_show(result.list);
				
				//查找已经选择的节点，勾上复选框
				var mess_categorys=$('#zw_f_mess_category').val();
				if(mess_categorys && mess_categorys!=''){
					var categorys=mess_categorys.split(',');
					
					$.each(categorys, function(aa, bb) {
						var n=$('#zw_page_common_tree').tree('find',bb);
						if(n){
							$('#zw_page_common_tree').tree('check',n.target);
						}
					});
					
				}
				
			});
			
			
		},
		
		tree_show : function(oData) {
			var x = zapadmin.tree_data(oData);
			$('#zw_page_common_tree').tree(
					{
						data : x
					});
		},
		
		
		getChecked : function (){
            var nodes = $('#zw_page_common_tree').tree('getChecked');
            var s = '';
            var ts='';
            for (var i = 0; i < nodes.length; i++) {
            	if (!nodes[i].target.nextSibling) {
            		 if (s != '') {
            			 s += ',';
            		 }
            		 if (ts != '') {
            			 ts += ',';
            		 }
            		 
                     s += nodes[i].id;
                     ts += nodes[i].text;
            	}
            }
            
            $('#zw_f_mess_category').val(s);
            $('#zw_f_mess_category_name').val(ts);
        },
		
        
		dosucc : function(){
			checkMessageCategory.getChecked();
			zapjs.f.window_close();
		}
		
		
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/checkMessageCategory",['cfamily/zs/zs',"zapjs/zapjs.zw"],function() {
		return checkMessageCategory;
	});
}
