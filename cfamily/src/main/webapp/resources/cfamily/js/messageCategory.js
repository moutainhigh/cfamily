
var messageCategory = {
	temp : {
		step : 4,
		data : [],
		app_code:"SI2003"
	},

	tree_init : function(oElm) {
		var obj = {};
		
		obj.app_code = messageCategory.temp.app_code;
		
		messageCategory.temp.step = $('#zw_page_tree_zw_s_step').val();
		
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForMessageCategoryTree',obj,function(result) {
			
			messageCategory.temp.data = result.list;
			
			messageCategory.tree_show(result.list);
			
			messageCategory.setExpand($('#zw_page_tree_zw_s_openLevel').val());
		});
	},

	tree_window : function(sElm) {

		var sData = zapjs.zw.upExtend(sElm, 'data');

		$.get(sData, function(result) {
			
			var x = zapadmin.tree_data(result,$('#'+sElm).val());
			$('#' + sElm + '_tree_ul').tree({
				data : x,
				checkbox : true,
				lines : true

			});
		});

	},
	init_window : function(sElm) {

		//$('#'+sElm).after('<span id="'+sElm+'_span_show"></span>');
		
	},
	
	//设置展开几级节点    0：root节点	1：一级节点   2：二级节点    其它：默认关闭所有节点
	setExpand : function(openLevel){//参数：打开几级节点
		$('#zw_page_common_tree').tree('collapseAll');
		var data = $('#zw_page_common_tree').tree("getChildren");
		if(openLevel!=undefined&&!isNaN(parseInt(openLevel))&&parseInt(openLevel)>=0&&parseInt(openLevel)<3){
			var n = parseInt(8)+parseInt(4)*parseInt(openLevel);
			for(var i=0;i<data.length;i++){
				if(data[i].id.length<=n){
					$('#zw_page_common_tree').tree('expand', data[i].target);
				}
			}
		}
	},
	
	tree_select : function(sElm) {

		var nodes = $('#' + sElm + '_tree_ul').tree('getChecked');
		var s = '';
		
		var aLi=[];
		
		for ( var i = 0; i < nodes.length; i++) {
	
			if (!nodes[i].target.nextSibling) {
				if (s != '')
					s += ',';
				s += nodes[i].id;
				aLi.push(nodes[i].text);
			}
		}
		
		$('#'+sElm).val(s);
	
		$('#'+sElm).nextAll('span').html(aLi.join(','));
	

		zapadmin.window_close();
	},

	tree_show : function(oData) {
		var x = zapadmin.tree_data(oData);
		$('#zw_page_common_tree').tree(
				{
					data : x,
					onClick : function(node) {
						$.get($('#zw_page_tree_zw_s_bookpage').val() + "?"
								+ $('#zw_page_tree_zw_s_uid').val() + '='
								+ node.attributes.uid, function(result) {
							$('#zw_page_tree_right').html(result);
							var data = messageCategory.temp.data;
							for ( var i = 0, j = data.length; i < j; i++) {
								if(data[i][0]!=null&&data[i][0]==node.id&&data[i][6]==3){
									messageCategory.tree_products(node);
								}
							}
						});
					}
				});
	},

	
	tree_products : function(oTag){ 
		var node = $('#zw_page_common_tree').tree('getSelected');
		
		var appCode = $('#zw_page_tree_zw_f_app_code').val();
		var categoryCode = node.id;
		if (node) {
			if($("#categoryProducts-group").length>0){
				$("#categoryProducts-group").remove();
			}
				var ourl = "zapadmin_single.show_box('cate_productlist_productids')";
				var content ='<div id="categoryProducts-group"><h4>'+
					'<input style="margin:10px;" type="button" value="添加商品" class="btn  btn-primary" '+
				'onclick="'+ourl+'" /></h4></div>'; 
				zs.r([ 'zapadmin/js/zapadmin_single' ], function(a) {
					a.init({
						"text" : "",
						buttonflag : false,
						"source" : "page_chart_v_cate_pc_productinfo",
						"id" : "cate_productlist_productids",
						"callback" : "parent.messageCategory.addProduct",
						"value" : "",
						"max" : "0&zw_f_app_code="+appCode+"&zw_f_category_code="+categoryCode
					});
				});
				var url = "../page/page_chart_v_show_cate_pc_productinfo?zw_f_app_code="+appCode+"&zw_f_category_code="+categoryCode;
				content+="<div style='height:650px;'><iframe width='100%' frameborder='0' height='100%'  class='zab_home_home_iframe' " +
				" onload='zapadmin.load_complate(this)'" +
				"' src='"+url+"'></iframe></div>";
				$("#zw_page_tree_right").children().append(content);
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	
	addProduct : function(sId, sVal){
		var obj = {};
		obj.category_code = $('#zw_page_common_tree').tree('getSelected').id;
		obj.app_code = $('#zw_page_tree_zw_f_app_code').val();
		obj.product_codes = sVal;
		zapjs.zw.api_call('com_cmall_productcenter_process_AppCategoryProductsApi',obj,function(result) {
			if(result.code=='909101001'){
				zapadmin.model_message(result.name);
				zapadmin_single.close_box('cate_productlist_productids');
			}else{
				zapadmin.model_message(result.name);
			}
		});
	},
	
	tree_nodeup : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			var k  = 0;
			var oData = messageCategory.temp.data;
			var nodeData = [];
			for ( var i = 0, j = oData.length; i < j; i++) {
				if(node.id==oData[i][0]){
					nodeData = oData[i];
				}
			}
			for(var i = 0,j=oData.length;i<j;i++){
				var nlen = nodeData[4].length;
				var olen = oData[i][4].length;
				if (olen==nlen && nodeData[2]==oData[i][2] &&parseInt(nodeData[4].substr(nlen-4,nlen))>parseInt(oData[i][4].substr(olen-4,olen))) {
					k++;
				}
			}
			if(k>0){
				messageCategory.tree_forSortUp(oTag,nodeData,oData);
			}else{
				zapadmin.model_message('不能进行向上移动！');
			}
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	
	tree_forSortUp : function(oTag,node,nodeAll){
		var length = node[4].length;
		var downNodeId = "";
		for(var i=1;i<10000;i++){
			var flag = false;
			var id = (parseInt(node[4].substr(length-4,length))-i).toString();
			for ( var l = 0, m = (4 - id.length); l < m; l++) {
				id = "0" + id;
			}
			var ai = node[4].substr(0,length-4)+id;
			for ( var k = 0, j = nodeAll.length; k < j; k++) {
				if(nodeAll[k][4]!=null&&ai==nodeAll[k][4]){
					downNodeId = ai;
					flag = true;
					break;
				}
			}
			if(flag){break;}
		}
		for ( var i = 0, j = nodeAll.length; i < j; i++) {
			//节点向上(本节点和孩子-1)
			if(node[4]==nodeAll[i][4] || nodeAll[i][4].substr(0,length)==node[4]){
				var sort = downNodeId+nodeAll[i][4].substr(length,nodeAll[i][4].length);
				zapjs.zw.func_do(oTag, null, {zw_f_uid:nodeAll[i][3],zw_f_sort:sort});
			//节点向下(上节点+1)
			}else if(downNodeId==nodeAll[i][4] || nodeAll[i][4].substr(0,length)==downNodeId){
				var sort = node[4]+nodeAll[i][4].substr(length,nodeAll[i][4].length);
				zapjs.zw.func_do(oTag, null, {zw_f_uid:nodeAll[i][3],zw_f_sort:sort});	
			}
		}
	},
	
	tree_nodedown : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			var k  = 0;
			var oData = messageCategory.temp.data;
			var nodeData = [];
			for ( var i = 0, j = oData.length; i < j; i++) {
				if(node.id==oData[i][0]){
					nodeData = oData[i];
				}
			}
			for(var i = 0,j=oData.length;i<j;i++){
				var nlen = nodeData[4].length;
				var olen = oData[i][4].length;
				if (olen==nlen && nodeData[2]==oData[i][2]&& parseInt(nodeData[4].substr(nlen-4,nlen))<parseInt(oData[i][4].substr(olen-4,olen))) {
					k++;
				}
			}
			if(k>0){
				messageCategory.tree_forSortDown(oTag,nodeData,oData);
			}else{
				zapadmin.model_message('不能进行向下移动！');
			}
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	
	tree_forSortDown : function(oTag,node,nodeAll){
		var length = node[4].length;
		var downNodeId = "";
		for(var i=1;i<10000;i++){
			var flag = false;
			var id = (parseInt(node[4].substr(length-4,length))+i).toString();
			for ( var l = 0, m = (4 - id.length); l < m; l++) {
				id = "0" + id;
			}
			var ai = node[4].substr(0,length-4)+id;
			for ( var k = 0, j = nodeAll.length; k < j; k++) {
				if(nodeAll[k][4]!=null&&ai==nodeAll[k][4]){
					downNodeId = ai;
					flag = true;
					break;
				}
			}
			if(flag){
				break;
			}
		}
		for ( var i = 0, j = nodeAll.length; i < j; i++) {
			//节点向下(本节点和孩子-1)
			if(node[4]==nodeAll[i][4] || nodeAll[i][4].substr(0,length)==node[4]){
				var sort = downNodeId+nodeAll[i][4].substr(length,nodeAll[i][4].length);
				zapjs.zw.func_do(oTag, null, {zw_f_uid:nodeAll[i][3],zw_f_sort:sort});
			//节点向上(上节点+1)
			}else if(downNodeId==nodeAll[i][4] || nodeAll[i][4].substr(0,length)==downNodeId){
				var sort = node[4]+nodeAll[i][4].substr(length,nodeAll[i][4].length);
				zapjs.zw.func_do(oTag, null, {zw_f_uid:nodeAll[i][3],zw_f_sort:sort});	
			}
		}
	},
	
	tree_edit : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			$.get($('#zw_page_tree_zw_s_editpage').val() + "?"
					+ $('#zw_page_tree_zw_s_uid').val() + '='
					+ node.attributes.uid, function(result) {
				$('#zw_page_tree_right').html(result);
			});

		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},

	tree_delete : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {

			var bFlag = false;
			var sId = node.id;
//			if(sId.length==8){
//				zapadmin.model_message('总分类不允许删除！');
//				return;
//			}
			var oData = messageCategory.temp.data;
			for ( var i = 0, j = oData.length; i < j; i++) {
				if (oData[i][2] == sId) {
					bFlag = true;
				}
			}

			if (bFlag) {
				zapadmin.model_message('有下级分类不允许删除！');
			} else {
				zapjs.zw.func_delete(oTag, node.attributes.uid);
			}

		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},

	tree_add : function(oTag) {
		
		//update by jlin 2015-01-23 20:27:00
//alert("5555555555");
		var node = $('#zw_page_common_tree').tree('getSelected');
		
		if (node) {
			var sId = node.id;
			var sortmax = 0;//增加排序字段
			var iMax = 0;//要新增节点的code
			var oData = messageCategory.temp.data;
			for ( var i = 0, j = oData.length; i < j; i++) {
				if (oData[i][2] == sId && oData[i][0].substr(oData[i][2].length,oData[i][0].length) > iMax) {
					iMax = oData[i][0].substr(oData[i][0].length - 4);
				}
			}

			iMax = (parseInt(iMax) + 1).toString();

			for ( var i = 0, j = (4 - iMax.length); i < j; i++) {
				iMax = "0" + iMax;

			}
			for(var i=0,j=oData.length;i<j;i++ ){
				if(oData[i][0]==sId){
					if(oData[i][4]!=undefined){
						sortmax = oData[i][4].substr(0,sId.length)+iMax;
					}
				}
			}
			
			iMax = sId + iMax;
			var url = $('#zw_page_tree_zw_s_addpage').val() + "?"
						+ $('#zw_page_tree_zw_s_parent').val() + '=' + sId + "&"
						+ $('#zw_page_tree_zw_s_code').val() + '=' + iMax+"&zw_f_manage_code="+messageCategory.temp.app_code;
			if($('#zw_page_tree_zw_s_sort').val()!=undefined){
				url+="&"+$('#zw_page_tree_zw_s_sort').val()+'='+sortmax;
			}	
			
			$.get(url,function(result) {
						$('#zw_page_tree_right').html(result);
					});

		} else {

			zapadmin.model_message('请选择后在进行操作！');
		}

	},
	// 删除函数调用
	func_delP : function(oElm, sUid,sPd) {
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认删除该条数据吗？</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					zapjs.zw.func_do(oElm, null, {zw_f_uid : sUid,zw_f_product_code : sPd,zw_f_seller_code : parent.$('#zw_page_tree_zw_f_app_code').val(),zw_f_category_code : parent.$('#zw_page_common_tree').tree('getSelected').id});
				}
		},{
			text : '否',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
				}
		});
		
		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
	}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/messageCategory",['cfamily/zs/zs',"zapjs/zapjs.zw"],function() {
		return messageCategory;
	});
}
