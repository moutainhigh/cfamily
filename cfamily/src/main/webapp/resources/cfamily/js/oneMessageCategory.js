var oneMessageCategory = {
		
		temp : {
			data : [],
			app_code:"SI2003"
		},
		
		tree_init : function() {
			
			var obj = {};
			obj.app_code = oneMessageCategory.temp.app_code;
			
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForMessageCategoryTree',obj,function(result) {
				
				oneMessageCategory.temp.data = result.list;
				oneMessageCategory.tree_show(result.list);
				
				//查找已经选择的节点
				var mess_category=$('#zw_f_mess_category').val();
				var n=$('#zw_page_common_tree').tree('find',mess_category);
				if(n){
					$('#zw_page_common_tree').tree('select',n.target);
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
		
		
        getSelected : function (){
            var node = $('#zw_page_common_tree').tree('getSelected');
            if (!node.target.nextSibling) {
            	 $('#zw_f_mess_category').val(node.id);
                 $('#mess_category').val(node.text);
            }
        },
        
		dosucc : function(){
			oneMessageCategory.getSelected();
			zapjs.f.window_close();
		}
		
		
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/oneMessageCategory",['cfamily/zs/zs',"zapjs/zapjs.zw"],function() {
		return oneMessageCategory;
	});
}
