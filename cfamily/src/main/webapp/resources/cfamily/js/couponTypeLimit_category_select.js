
var couponTypeLimit_category_select = {
	temp : {
		step : 4,
		data : []
	},

	tree_init : function(oElm) {

		couponTypeLimit_category_select.temp.step = $('#zw_page_tree_zw_s_step').val();
		zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryApi','',function(result) {
			couponTypeLimit_category_select.temp.data = result.list;
			couponTypeLimit_category_select.tree_show(result.list);
			couponTypeLimit_category_select.setExpand($('#zw_page_tree_zw_s_openLevel').val());
		});
		
		
		$('#tgc_but').click(function (){
			
			var id=$('#zw_f_cuid').val();
			var caname=$('#zw_f_caname').val();
			
			if(id!=''){
				var crflag=false;
				$('.a_v_v_a').each(function (sid,obj){
					if(obj.value==id){
						crflag=true;
						return false;
					}
				});
				
				if(crflag){
					zapadmin.model_message('该分类已经存在，请选择其他分类!');
					return ;
				}
				
				
				
				var ht='<li id="li'+id+'"> '+caname+'&nbsp;&nbsp; <input type="hidden" name="zw_f_category_code" value="'+id+'" class="a_v_v_a"> <a href="javascript:void(0)" onclick="couponTypeLimit_category_select.del_category(\'li'+id+'\')">删除</a></li>';
				$('#v_v').append(ht);
				
			}else{
				zapadmin.model_message('请选择一个子节点进行操作!');
			}
			
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
		var x = couponTypeLimit_category_select.tree_data(oData);
		$('#zw_page_common_tree').tree(
				{
					data : x,
					onClick : function(node) {
//						$('#zw_f_cuid').val(node.attributes.id);//这里必须是三级菜单的时候才让操作
//						var id=node.attributes.id;
//						if(id.length>=12){  
//							
//						}
						
						couponTypeLimit_category_select.tree_sv(node.attributes.id,node.attributes.text);
					}
				});
	},
	
tree_sv : function(id,caname) {
		
		var node = $('#zw_page_common_tree').tree('getSelected');
		if (node) {

			var bFlag = false;		//判断是否有下级节点
			var bFlag1 = false;		//判断是否同时有下级节点与上级节点
			var sId = node.id;

			var oData = couponTypeLimit_category_select.temp.data;
			for ( var i = 0, j = oData.length; i < j; i++) {
				if (oData[i][2] == sId) {
					if (oData[i][6] == 4) {
						bFlag1 = true;	//同时有下级节点与上级节点
					}
					bFlag = true;
				}
			}
			if (!bFlag) {//没有下级节点
				var node1=$('#zw_page_common_tree').tree('getParent',node.target);
				var node2=$('#zw_page_common_tree').tree('getParent',node1.target);
				var showTex = node2.text + "->" + node1.text + "->" + node.text;
				$('#zw_f_cuid').val(id);			//这里必须是三级菜单的时候才让操作
				$('#zw_f_caname').val(showTex);		//这里必须是三级菜单的时候才让操作
			}else if(bFlag1){//同时有下级节点与上级节点
				var node1=$('#zw_page_common_tree').tree('getParent',node.target);
				var showTex = node1.text + "->" + node.text;
				$('#zw_f_cuid').val(id);			//这里必须是同时有下级节点与上级节点的时候才让操作
				$('#zw_f_caname').val(showTex);		//这里必须是同时有下级节点与上级节点的时候才让操作
			}else{
				$('#zw_f_cuid').val('');
				$('#zw_f_caname').val('');
			}
		}
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
					uid : oEvery[3],
					id : oEvery[0],
					text : oEvery[1]
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
	
	
	func_success : function(o) {

		switch (o.resultType) {
			case "116018010":
				eval(o.resultObject);
				break;
			default:
				// alert(o.resultMessage);

				if (o.resultCode == "1") {

					if (o.resultMessage == "") {
						o.resultMessage = "操作成功";
					}
					
					zapjs.zw.modal_show({
						content : o.resultMessage,
						okfunc : 'couponTypeLimit_category_select.autorefresh()'
					});

				} else {
					zapjs.zw.modal_show({
						content : o.resultMessage
					});
				}

				break;
		}

	},
	
	autorefresh : function() {
		if (zapjs.f.exist('main_iframe')) {
			document.getElementById("main_iframe").contentWindow.zapjs.f.tourl();
		} else {
			couponTypeLimit_category_select.tourl();
		}
	},
	
	tourl : function(sUrl) {
		if (!sUrl) {
			sUrl = location.href;
		}
		location.href = sUrl;
		location.reload();
	},
	
	// 判断是否存在元素
	exist : function(sId) {
		return document.getElementById(sId) ? true : false;

	},
	
	
	func_call : function(oElm) {
		var categoryCodes = "";
		var obj = {};
		obj.categoryCodeArr = new Array();
		$("input[name='zw_f_category_code']").each(function(){
			obj.categoryCodeArr[obj.categoryCodeArr.length] = $(this).val();
			categoryCodes += ($(this).val() + ","); 
		});
		obj.sellerCode="SI2003"
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForCouponLimitCategoryBaseInfo',obj,function(result) {
			if(result.resultCode==1){
				couponTypeLimitNew3.temp.categoryJson = result.categoryInfoList;
				if (categoryCodes.length > 0) {
					$('#zw_f_category_codes').val(categoryCodes.substr(0, categoryCodes.length-1));
				}
				couponTypeLimitNew3.categoryTable_init();
			}else{
				zapadmin.model_message('添加分类失败');
			}
			zapadmin.window_close();		//关闭选择分类窗口
		});
	},
	
	
	del_category:function (id){
		var parent=document.getElementById("v_v");
		var child=document.getElementById(id);
		parent.removeChild(child);
	}

};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/couponTypeLimit_category_select",function() {
		return couponTypeLimit_category_select;
	});
}
