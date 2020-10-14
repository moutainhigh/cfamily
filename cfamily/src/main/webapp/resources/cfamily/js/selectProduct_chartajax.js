var selectProduct_chartajax = {

	temp : {
		loadcount : {},
		checkdata:{}

	},
	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			height:'310px',
			width:'100%',
			//是否有选择框
			checked:1,
			//最大选择数量
			maxnum:1,
			source : null
		};

		var s = $.extend({}, defaults, options || {});
		
		
		selectProduct_chartajax.temp.checkdata[s.id]={};
		
		var sValues=parent.$('#'+s.id).val();
		var sTexts=parent.$('#'+s.id+"_show_text").val();
		
		
		if(s.maxnum==1)
		{
		}
		else if(sValues)
		{
			var aV=sValues.split(',');
			var aT=sTexts.split(',');
			
			for(var i=0,j=aV.length;i<j;i++)
			{
				
				selectProduct_chartajax.temp.checkdata[s.id][aV[i]]={key:aV[i],text:aT[i]};
			}
			
		}

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			selectProduct_chartajax.upData(oData, s);

		});
	},

	upData : function(oData, s) {
			var aTable = [];
			aTable.push('<div style="height:'+s.height+';width:'+s.width+';" class="w_clear">');
			aTable.push('<table style="height:'+s.height+';"><thead><tr>');
			for (var p in oData.pageFields) {
				var othis = oData.pageFields[p];
				aTable.push('<th data-options="field:\'f_' + p + '\''+(s.checked>0&&p==0?',checkbox:true':'')+'">' + othis + '</th>');
			}
			aTable.push('<th data-options="field:\'f_' + 6 + '\''+(s.checked>0&&p==0?',checkbox:true':'')+'">' + '操作' + '</th>');

			aTable.push('</tr></thead></table>');
			
			aTable.push('</div>');
			$('#' + s.id).append(aTable.join(''));
			
			
			
			var sBaseUrl=zapjs.f.upurl();
			
			var sDataUrl=zapjs.zw.api_link('com_srnpr_zapweb_webapi_ChartApi') + "&pagecode=" + s.pagecode+"&"+sBaseUrl.split('?')[1];


		selectProduct_chartajax.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
			rownumbers : true,
			autoRowHeight : false,
			pagination : true,
			checkOnSelect:true,
			singleSelect:(s.maxnum==1?true:false),
			method:'get',
			pageSize : 10,
			onLoadSuccess:function(tableData)
			{
				//zapjs.d(data);
				//var opts =selectProduct_chartajax.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=selectProduct_chartajax.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						selectProduct_chartajax.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				selectProduct_chartajax.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(selectProduct_chartajax.temp.loadcount[sId]!==undefined)
				{
					var checked = selectProduct_chartajax.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=selectProduct_chartajax.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={key:othis["f_0"],text:othis["f_1"]};
						
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_full_value:function(sFncName,sParentId,sBoxId)
	{
		
		selectProduct_chartajax.reset_checked(sBoxId);
		
		var tempcheck=selectProduct_chartajax.temp.checkdata[sBoxId];
		
		var aValues=[];
		var aTexts=[];
		var subsidys=[];
		
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				aValues.push(othis["key"]);
				aTexts.push(othis["text"]);

			}
			
			
		}
		
		
		
		eval( sFncName+'("'+ sParentId +'","'+ aValues.join(',') +'","'+ aTexts.join(',') +'","'+ subsidys.join(',') +'")' );
		
	},
	ok_single_value:function(defaultParam,skuCode)
	{
		var sFncName = 'parent.chooseProduct.result';
		var sParentId='zw_f_sku_code';
		var sBoxId = 'zw_f_sku_code';
		selectProduct_chartajax.reset_checked(sBoxId);
		var tempcheck=selectProduct_chartajax.temp.checkdata[sBoxId];
		
		var aValues=[];
		var aTexts=[];
		var subsidys=[];
		
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				aValues.push(othis["key"]);
				aTexts.push(othis["text"]);

			}
			
			
		}
		aValues.push(skuCode);
		
		
		eval( sFncName+'("'+ sParentId +'","'+ aValues.join(',') +'","'+ aTexts.join(',') +'","'+ subsidys.join(',') +'")' );
		
	}
	
	
	
};

if ( typeof define === "function" && define.amd) {
	define("cevent/js/selectProduct_chartajax", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return selectProduct_chartajax;
	});
}
