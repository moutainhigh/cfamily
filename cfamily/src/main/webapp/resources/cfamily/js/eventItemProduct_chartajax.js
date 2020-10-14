var eventItemProduct_chartajax = {

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
		
		
		eventItemProduct_chartajax.temp.checkdata[s.id]={};
		
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
				
				eventItemProduct_chartajax.temp.checkdata[s.id][aV[i]]={key:aV[i],text:aT[i]};
			}
			
		}

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			eventItemProduct_chartajax.upData(oData, s);

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
			aTable.push('</tr></thead></table>');
			
			aTable.push('</div>');
			$('#' + s.id).append(aTable.join(''));
			
			
			
			var sBaseUrl=zapjs.f.upurl();
			
			var sDataUrl=zapjs.zw.api_link('com_srnpr_zapweb_webapi_ChartApi') + "&pagecode=" + s.pagecode+"&"+sBaseUrl.split('?')[1];


		eventItemProduct_chartajax.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
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
				//var opts =eventItemProduct_chartajax.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=eventItemProduct_chartajax.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						eventItemProduct_chartajax.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				eventItemProduct_chartajax.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(eventItemProduct_chartajax.temp.loadcount[sId]!==undefined)
				{
					var checked = eventItemProduct_chartajax.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=eventItemProduct_chartajax.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={key:othis["f_0"],text:othis["f_1"],desc:othis["f_3"],code:othis["f_4"]};
						
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_value:function(sFncName,sParentId,sBoxId)
	{
		eventItemProduct_chartajax.reset_checked(sBoxId);
		var tempcheck=eventItemProduct_chartajax.temp.checkdata[sBoxId];

		var skuCodes = [];
		var code = "";
		var name = "";
		var desc = "";
		var goodCode = "";
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				skuCodes.push(othis["key"]);
				code = othis["key"];//sku编号
				name = othis["text"];//sku名称
				desc = othis["desc"];//商品名称
				goodCode = othis["code"];//商品编码
			}
		}
		var opt ={};
		opt.skuCodes = skuCodes;
		opt.eventCode = parent.$("#zw_f_event_code").val();
		parent.$("#zw_f_commodity_number").val(code);
		parent.$("#zw_f_commodity_name").val(desc);
		parent.$("#zw_f_commodity_describe").val(desc);
		parent.$("#zw_f_good_number").val(goodCode);
		parent.$('#' + sParentId + 'eventItemProduct_select_showbox').window('close');
	}
	
	
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/eventItemProduct_chartajax", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return eventItemProduct_chartajax;
	});
}
