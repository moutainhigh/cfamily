var chooseProduct = {
		data: {
			opts: {
				pageCode: 'page_chart_fullCut_v_cf_pc_proinfo_status',
				pageParam: '',
				maxSelect: 10,
				chooseCallback: '',
				choosedValues: ''
			},
			headerColumns : [],
			choosedArray: []
		},
		init: function(options) {
			this.headerColumns = []
			this.choosedArray = []
			this.data.opts = $.extend({}, this.data.opts, options || {});
			if(this.data.opts.choosedValues != '') {
				this.data.choosedArray = this.data.opts.choosedValues.split(',')
			}
			this.initHeader();
		},
		initHeader: function() {
			zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {pageCode : this.data.opts.pageCode}, function(oData) {
				if(oData.resultCode == 1) {
					chooseProduct.data.headerColumns = []
					for(var i = 0; i < oData.pageFields.length; i++) {
						chooseProduct.data.headerColumns.push({
							field:'f_'+i,
							title:oData.pageFields[i],
							checkbox: (chooseProduct.data.opts.maxSelect > 1 && i == 0)
						})
					}
					
					chooseProduct.loadData();
				}
			});
		},
		refreshChoosedArray: function(){
			if(chooseProduct.data.opts.maxSelect <= 1) {
				this.data.choosedArray = []
				return;
			}
			
			var dataList = $('#tableView').datagrid('getRows');
			var checkedList = $('#tableView').datagrid('getChecked');
			for(var i = 0; i < dataList.length; i++) {
				var isChecked = false;
				var key = dataList[i]['f_0'];
				for(var j = 0; j < checkedList.length; j++) {
					if(key == checkedList[j]['f_0']) {
						isChecked = true;
						break;
					}
				}
				
				var existIndex = this.data.choosedArray.indexOf(key)
				if(existIndex > -1 && !isChecked) {
					// 被取消选中的要删除
					this.data.choosedArray.splice(existIndex,1)
				} else if(existIndex == -1 && isChecked) {
					// 没有则添加
					this.data.choosedArray.push(key);
				}
			}
		},
		loadData: function() {
			var param = this.toJsonParam(this.data.opts.pageParam);
			var formParam = $('form').eq(0).serializeArray();
			for(var i in formParam) {
				param[formParam[i]['name']] = formParam[i]['value']
			}
			param['pagecode'] = this.data.opts.pageCode;
			
			if(this.data.headerColumns.length == 0) {
				this.initHeader();
				return;
			}
			
			param['api_key'] = "jsapi"
			param['api_input'] = ""
			param['zw_f_seller_code'] = "SI2003"
			param['api_target'] = "com_srnpr_zapweb_webapi_ChartApi"
			
			$('#tableView').datagrid({
			    url:'../jsonapi/com_srnpr_zapweb_webapi_ChartApi',
			    queryParams: param,
			    rownumbers: true,
			    pagination : true,
			    autoRowHeight : false,
			    checkOnSelect:true,
			    method:'get',
			    pageSize : 10,
			    columns:[chooseProduct.data.headerColumns],
			    onLoadSuccess: function(tableData){
			    	var rows = tableData.rows;
			    	for(var i = 0; i < rows.length; i++) {
			    		if(chooseProduct.data.choosedArray.indexOf(rows[i]['f_0']) > -1){
			    			$('#tableView').datagrid('checkRow',i);
			    		}
			    	}
			    },
			    onBeforeLoad: function(){
			    	// 解决翻页时默认全选没有取消问题
			    	$('#tableView').parent().find("div.datagrid-header-check").children("input[type='checkbox']").eq(0).attr("checked", false);
			    	
			    	chooseProduct.refreshChoosedArray()
			    }
			});
		},
		reloadData: function(oData) {
			loadData();
		},
		submit: function() {
			chooseProduct.refreshChoosedArray();
			
			if(this.data.choosedArray.length == 0) {
				alert("请选择商品");
				return;
			}
			
			if(this.data.opts.chooseCallback != ''){
				eval( this.data.opts.chooseCallback+'(\''+JSON.stringify(this.data.choosedArray)+'\')' );
			}
			
			parent.zapjs.f.window_close('chooseProductShowBox');
		},
		toJsonParam: function(param) {
			if(!param || param == '') return {};
			var obj = {}
			var kvs = param.split('&')
			for(var i = 0; i < kvs.length; i++) {
				var kv = kvs[i];
				var v = kv.split('=');
				if(v.length == 2 && v[0] != '' && v[1] != '') {
					obj[v[0]] = v[1]
				}
			}
			return param;
		},		
	};

	if ( typeof define === "function" && define.amd) {
		define("cfamily/js/chooseCategory", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
			return chooseProduct;
		});
	}
