
var category_tree = {
	temp : {
		step : 4,
		data : []
	},

	tree_init : function(oElm) {
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',category_tree.beforesubmit);
		category_tree.temp.step = $('#zw_page_tree_zw_s_step').val();
		var obj = {}; 
		obj.showAll = '1'; 
		zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryPreApi',obj,function(result) {
			category_tree.temp.data = result.list;  
			category_tree.tree_show(result.list);
			category_tree.setExpand($('#zw_page_tree_zw_s_openLevel').val());
		});
	},

	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		var href = $("#zw_f_adv_href").val();	//预警库存
		if (null != href&&"" != href&& href.toLowerCase().indexOf("http://share") != -1) {
			zapjs.f.message('URL中不能包含"http://share"');
			return false;
		}else{
			return true;
		}
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
		var x = category_tree.tree_data(oData);
		$('#zw_page_common_tree').tree(
				{
					data : x,
					onClick : function(node) {
						var data = category_tree.temp.data;
						for(var i = 0; i < data.length; i ++) {
							if(data[i][0] != null && data[i][0] == node.id) {
								var tree_level = data[i][6];
								var category_type = data[i][7];
								if(category_type == '449748510001'){
									$.get($('#zw_page_tree_zw_s_bookpage').val() + "?"
											+ $('#zw_page_tree_zw_s_uid').val() + '='
											+ node.attributes.uid + '&tree_level=' + tree_level, function(result) {
										$('#zw_page_tree_right').html(result);
										
										if(tree_level == 2) {
											category_tree.tree_brands(node);
										}
										// 只有二级分类显示“新建子分类”:tree_level == 3
										if(tree_level == 3) {
											var a = $("a[zapweb_attr_operate_id='48c5bc1cea264c7895f6c879344d8dbc']");
											if(a.length == 0){											
												$("a[zapweb_attr_operate_id='12531c5991a74762ad05a26bb3e6c848']").after('&nbsp;&nbsp;<a class="btn btn-small" zapweb_attr_operate_id="48c5bc1cea264c7895f6c879344d8dbc" href="javascript:" onclick="category_tree.new_tree_add(this)">新建子分类</a>');
											}
										}else{
											$("a[zapweb_attr_operate_id='48c5bc1cea264c7895f6c879344d8dbc']").remove();
										}
										
									});
								}else{ // 如果是新建子分类
									$.get("../show/page_book_v_uc_sellercategory_pre_new?zw_f_uid="+node.attributes.uid, function(result) {
										$('#zw_page_tree_right').html(result);
										
										$("a[zapweb_attr_operate_id='48c5bc1cea264c7895f6c879344d8dbc']").remove();
										
										if(tree_level == 4) {
											if(category_type == '449748510002'){ // 关联商品
												category_tree.tree_products(node);
											}
											if(category_type == '449748510003'){ // 属性分类
												category_tree.tree_properties(node);
											}
										}
									});
								}
							}
						}
					}
				});
	},
	
	tree_brands : function(oTag){
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			if($("#categoryBrands-group").length>0){
				$("#categoryBrands-group").remove();
			}
				var obj = {};
				obj.categoryCode = node.id;
				zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryBrandsApi',obj,function(result) {
				var content ="<div id=\"categoryBrands-group\"><h4><span style=\"margin:10px;\">品牌列表</span><input style=\"margin:10px;\"  " +
						"type=\"button\" value=\"选择品牌\" class=\"btn  btn-primary\" onclick=\"category_tree.addBrand()\"/></h4>" 
					+"<table class=\"table table-condensed table-striped table-bordered table-hover\"><thead>";
				content+="<tr><th> 品牌编号 </th><th> 品牌中文名称 </th><th> 品牌英文名称 </th><th> 创建时间 </th><th> 操作 </th></tr></thead><tbody>";
				for(var i=0,j=result.listProperty.length;i<j;i++){
					var o  = result.listProperty[i];
					content+="<tr><td>"+o.brand_code+"</td>";
					content+="<td>"+o.brand_name+"</td>";
					content+="<td>"+o.brand_name_en+"</td>";
					content+="<td>"+o.create_time+"</td>";
					content+="<td><input class=\"btn btn-small\" type=\"button\" value=\"删除\" " +
							"onclick=\"zapjs.zw.func_delete(this,'"+o.uid+"')\"  " +
							"zapweb_attr_operate_id=\"0745ty8d358f49e9ki8cabc6b2eb3476\"></tr>";
				}
				content+=	"</tbody></table></div>";
				$("#zw_page_tree_right").children().append(content);
				});
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	
	// 新建子分类-关联商品
	tree_products : function(oTag){
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			if($("#categoryBrands-group").length>0){
				$("#categoryBrands-group").remove();
			}
			var obj = {};
			obj.categoryCode = node.id;
			zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryPreProdApi',obj,function(result) {
				var content ="<div id=\"categoryBrands-group\"><h4><span style=\"margin:10px;\">商品列表</span>" +
						"<input style=\"margin:10px;\" type=\"button\" value=\"选择商品\" class=\"btn  btn-primary\" onclick=\"category_tree.addProduct()\"/>" +
						"<input style=\"margin:10px;\" type=\"button\" value=\"导入商品\" class=\"btn  btn-primary\" onclick=\"zapadmin.window_url('../show/page_import_v_uc_sellercategory_pre_product?categoryCode="+node.id+"')\">" +
						"<input style=\"margin:10px;\" type=\"button\" value=\"批量删除\" class=\"btn  btn-primary\" onclick=\"category_tree.beachDelProd('"+node.id+"')\"/></h4>" +
						"<table class=\"table table-condensed table-striped table-bordered table-hover\"><thead>";
				content+="<tr><th><input id=\"checkAllOrNone\" onclick=\"category_tree.checkAllOrNone()\" type=\"checkbox\"> 全选 </th><th> 商品编号 </th><th> 商品名称 </th><th> 创建时间 </th><th> 更新时间 </th><th> 操作 </th></tr></thead><tbody>";
				for(var i=0,j=result.listProperty.length;i<j;i++){
					var o  = result.listProperty[i];
					content+="<tr><td><input name=\"delCheck\" type=\"checkbox\" value=\""+o.uid+"\"></td>";
					content+="<td>"+o.product_code+"</td>";
					content+="<td>"+o.product_name+"</td>";
					content+="<td>"+o.create_time+"</td>";
					content+="<td>"+o.update_time+"</td>";
					content+="<td><input class=\"btn btn-small\" type=\"button\" value=\"删除\" " +
					"onclick=\"zapjs.zw.func_delete(this,'"+o.uid+"')\"  " +
					"zapweb_attr_operate_id=\"983325a568fd11eaabac005056165069\"></tr>";
				}
				content+=	"</tbody></table></div>";
				$("#zw_page_tree_right").children().append(content);
			});
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	// 批量删除商品
	beachDelProd : function(nodeId){
		var checkProdUids = "";
		$("tbody input[name='delCheck']").each(function(){
	    	if(this.checked){
	    		checkProdUids+=this.value+",";
	    	}
		}); 
		if(checkProdUids.length <= 0){
			zapjs.f.message("请选择要删除的商品！");
			return false;
		}
		
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认删除该条数据吗？</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					
					var obj = {};
					obj.delProdUids = checkProdUids;
					obj.nodeId = nodeId;
					zapjs.zw.modal_process();
					zapjs.zw.api_call('com_cmall_familyhas_api_ApiBeachDelProdForPre',obj,function(result) {
						zapjs.zw.modal_show({
							content : result.resultMessage,
							okfunc : $("div[node-id='"+result.nodeId+"']").click()
						});
					});
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
		/*if(confirm("确认删除选择的商品吗?")){			
			var obj = {};
			obj.delProdUids = checkProdUids;
			obj.nodeId = nodeId;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiBeachDelProdForPre',obj,function(result) {
				zapjs.zw.modal_show({
					content : result.resultMessage,
					okfunc : $("div[node-id='"+result.nodeId+"']").click()
				});
			});
		}*/
		
	},
	// 商品全选
	checkAllOrNone : function(){
		$("tbody input[name='delCheck']").each(function(){
	    	this.checked=$("#checkAllOrNone").is(':checked');
	    	
	    });
	},
	// 新建子分类-关联属性
	tree_properties : function(oTag){
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			if($("#categoryBrands-group").length>0){
				$("#categoryBrands-group").remove();
			}
			var obj = {};
			obj.categoryCode = node.id;
			zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryPrePropertiesApi',obj,function(result) {
				var content ="<div id=\"categoryBrands-group\"><h4><span style=\"margin:10px;\">属性值列表</span>" +
						"<input style=\"margin:10px;\" type=\"button\" value=\"维护属性值\" class=\"btn  btn-primary\" onclick=\"category_tree.addProperties()\"/></h4>" +
						"<table class=\"table table-condensed table-striped table-bordered table-hover\"><thead>";
				content+="<tr><th> 属性值编号 </th><th> 属性值 </th><th> 所属属性 </th><th> 分类名称 </th><th> 分类级别 </th><th> 操作 </th></tr></thead><tbody>";
				for(var i=0,j=result.listProperty.length;i<j;i++){
					var o  = result.listProperty[i];
					content+="<tr><td>"+o.properties_value_code+"</td>";
					content+="<td>"+o.properties_value+"</td>";
					content+="<td>"+o.properties_name+"</td>";
					content+="<td>"+o.category_name+"</td>";
					content+="<td>"+o.level+"</td>";
					content+="<td><input class=\"btn btn-small\" type=\"button\" value=\"删除\" " +
					"onclick=\"zapjs.zw.func_delete(this,'"+o.sppv_uid+"')\"  " +
					"zapweb_attr_operate_id=\"97e77a3068fd11eaabac005056165069\"></tr>";
				}
				content+=	"</tbody></table></div>";
				$("#zw_page_tree_right").children().append(content);
			});
		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},
	
	addProduct : function(){
		var url = '../page/page_chart_vv_cf_pc_productinfo';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: scroll' frameborder='0'  src='"+url+"'></iframe>";
		var option={title:"选择商品",content:productValue,width:1000,height:600};
		zapjs.f.window_box(option);
	},
	
	addProperties : function(){
		var url = '../page/page_chart_v_v_uc_properties_value';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: scroll' frameborder='0'  src='"+url+"'></iframe>";
		var option={title:"选择商品",content:productValue,width:1000,height:600};
		zapjs.f.window_box(option);
	},
	
	addBrand : function(){
		var url = '../page/page_chart_vv_cf_pc_brandinfo';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: scroll' frameborder='0'  src='"+url+"'></iframe>";
		var option={title:"选择品牌",content:productValue,width:1000,height:600};
		zapjs.f.window_box(option);
	},
	
	tree_data : function(oData, sValues) {

		if (!sValues) {
			sValues = '';
		} else {
			sValues = ',' + sValues + ',';
		}

		var x = [];
		var step = [];

		for (var n = 0, m = oData.length; n < m; n++) {

			var oEvery = oData[n];

			var oThis = {
				id : oEvery[0],
				text : oEvery[1],
				attributes : {
					uid : oEvery[3]
				}
			};

			if (sValues.indexOf(',' + oThis.id + ',') > -1) {
				oThis.checked = true;

			}

			if (n == 0) {
				x.push(oThis);
				step[0] = x[0];
			} else {

				var iStepLength = step.length;
				for (var i = 0; i < iStepLength; i++) {
					if (step[i].id == oEvery[2]) {
						if (!step[i].hasOwnProperty("children")) {
							step[i].children = [];
						}
						step[i].children.push(oThis);

						step[step.length] = step[i].children[step[i].children.length - 1];

						i = iStepLength;
					}

				}

			}

		}

		return x;

	},
	
	tree_nodeup : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			var k  = 0;
			var oData = category_tree.temp.data;
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
				category_tree.tree_forSortUp(oTag,nodeData,oData);
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
			var oData = category_tree.temp.data;
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
				category_tree.tree_forSortDown(oTag,nodeData,oData);
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
			var sId = node.id;
			var oData = category_tree.temp.data;
			var category_type = "449748510001";
			for(var i=0,j=oData.length;i<j;i++ ){
				if(oData[i][0]==sId){
					category_type = oData[i][7];
					if(category_type == "449748510001"){						
						if(oData[i][6]!=undefined){
							if(parseInt(oData[i][6])>3){
								zapadmin.model_message('不能修改三级分类！');
								return;
							}
						}
					}
				}
			}
			
			if(category_type == "449748510001"){				
				$.get($('#zw_page_tree_zw_s_editpage').val() + "?"
						+ $('#zw_page_tree_zw_s_uid').val() + '='
						+ node.attributes.uid, function(result) {
					$('#zw_page_tree_right').html(result);
				});
			}else{
				$.get("../show/page_edit_v_uc_sellercategory_pre_new?zw_f_uid="
						+ node.attributes.uid, function(result) {
					$('#zw_page_tree_right').html(result);
				});
			}

		} else {
			zapadmin.model_message('请选择后在进行操作！');
		}
	},

	tree_delete : function(oTag) {
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {

			var bFlag = false;
			var sId = node.id;

			var oData = category_tree.temp.data;
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

		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			var sId = node.id;
			var sortmax = 0;//增加排序字段
			var iMax = 0;
			var level = 0;//分类级别,最大为2级
			var oData = category_tree.temp.data;
			for ( var i = 0, j = oData.length; i < j; i++) {
				if (oData[i][2] == sId && oData[i][0].substr(oData[i][2].length,oData[i][0].length) > iMax) {
					iMax = oData[i][0].substr(oData[i][0].length
							- 4);
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
					if(oData[i][6]!=undefined){
						if(parseInt(oData[i][6])<3){
							level = (parseInt(oData[i][6])+1).toString();
						}else if(parseInt(oData[i][6]) == 3) {
							category_tree.tree_add_three(oTag);
							return;
						}else{
							zapadmin.model_message('不能添加子分类！');
							return;
						}
					}
				}
			}

			iMax = sId + iMax;
			var url = $('#zw_page_tree_zw_s_addpage').val() + "?"
						+ $('#zw_page_tree_zw_s_parent').val() + '=' + sId + "&"
						+ $('#zw_page_tree_zw_s_code').val() + '=' + iMax;
			if($('#zw_page_tree_zw_s_sort').val()!=undefined){
				url+="&"+$('#zw_page_tree_zw_s_sort').val()+'='+sortmax;
			}	
			if($('#zw_page_tree_zw_s_level').val()!=undefined){
				url+="&"+$('#zw_page_tree_zw_s_level').val()+'='+level;
			}
			$.get(url,function(result) {
				$('#zw_page_tree_right').html(result);
			});

		} else {

			zapadmin.model_message('请选择后在进行操作！');
		}

	},
	
	// 新建子分类
	new_tree_add : function(oTag) {
		
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {
			var sId = node.id;
			
			var url = "../show/page_add_v_uc_sellercategory_pre_new?zw_f_parent_code="+sId;
			
			$.get(url,function(result) {
				$('#zw_page_tree_right').html(result);
			});
			
		} else {
			
			zapadmin.model_message('请选择后在进行操作！');
		}
		
	},

	tree_add_three : function(oTag) {
		var url = '../page/page_add_v_uc_three_category';
		var productValue="<iframe width='100%' height='98%' style='overflow-x: hidden;overflow-y: scroll' frameborder='0'  src='"+url+"'></iframe>";
		var option={title:"选择前台三级分类",content:productValue,width:1000,height:600};
		zapjs.f.window_box_func(option, function() {
			category_tree.tree_init();
		});
	}
};

if (typeof define === "function" && define.amd) {
	define("zapadmin/js/category_pre_tree",function() {
		return category_tree;
	});
}
