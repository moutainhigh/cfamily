var chooseCategory = {
	data : {
		treeDataList: [],
		allCategoryList: [],
		chooseCallback: '',
		maxLevel: 0
	},
	init: function(chooseCallback,maxLevel){
		this.maxLevel = maxLevel || 0;
		this.data.chooseCallback = chooseCallback;
		this.treeDataList = []
		this.allCategoryList = []
		zapjs.zw.api_call('com.cmall.productcenter.process.SellerCategoryApi',{seller_code: 'SI2003', maxLevel: maxLevel},function(result) {
			chooseCategory.data.allCategoryList = result.list
			chooseCategory.formatTreeData({},1);
			chooseCategory.showTree();
		});		
	},
	formatTreeData: function(nodeMap,lvl){
		var item;
		var node;
		for(var i = 0; i < this.data.allCategoryList.length; i++) {
			item =  this.data.allCategoryList[i]
			if(item[6] == lvl) {
				node = {
					id: item[0],
					text: item[1],
					children: [],
					lvl: lvl,
					attributes: {fullName: item[1]}
				};
				
				if(!nodeMap[item[0]]) {
					nodeMap[item[0]] = node
				} 
				
				if(nodeMap[item[2]]) {
					node['attributes']['fullName'] = nodeMap[item[2]]['attributes']['fullName'] + '-&gt;' + node['attributes']['fullName'];
					nodeMap[item[2]]['children'].push(node)
				}
				
				if(lvl == 1) {
					this.data.treeDataList.push(node)
				}
			}
		}
		
		lvl++;
		if(lvl <= 4) {
			this.formatTreeData(nodeMap,lvl);
		}
	},
	showTree: function(){
		$('#zw_page_common_tree').tree({
			data: this.data.treeDataList
		});
		$('#zw_page_common_tree').tree('collapseAll')
	},
	deleteItem: function(key) {
		$('#li'+key).remove();
	},
	choosedItem: function(){
		var node = $('#zw_page_common_tree').tree('getSelected');
		var html = '<li id="li#categoryCode#">#fullName# &nbsp;&nbsp;<input type="hidden" name="zw_f_category_code" value="#categoryCode#" class="a_v_v_a"> <a href="javascript:void(0)" onclick="chooseCategory.deleteItem(\'#categoryCode#\')">删除</a></li>'
		
		var exists = false
		$("input[name='zw_f_category_code']").each(function(){
			if($(this).val() == node['id']) {
				exists = true;
			}
		});
		
		if(!exists) {
			html = html.replace(/#fullName#/g,node['attributes']['fullName'])
			html = html.replace(/#categoryCode#/g,node['id'])
			$('#v_v').append(html);
		} else {
			alert('已存在')
		}
	},
	submit: function() {
		var categoryCodes = []
		$("input[name='zw_f_category_code']").each(function(){
			categoryCodes.push($(this).val())
		});
		
		if(categoryCodes.length == 0) {
			alert("请选择分类");
			return;
		}
		
		if(this.data.chooseCallback != ''){
			eval( this.data.chooseCallback+'(\''+JSON.stringify(categoryCodes)+'\')' );
		}
		
		parent.zapjs.f.window_close('chooseCategoryShowBox');
	}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/chooseCategory",function() {
		return chooseCategory;
	});
}
