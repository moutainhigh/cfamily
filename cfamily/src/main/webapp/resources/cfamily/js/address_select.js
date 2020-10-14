var address_select = {

	temp : {
		loadcount : {},
		checkdata:{}

	},
	init : function(options) {
		//$('#zw_f_small_seller_code,#zw_f_small_seller_name').attr("readonly","readonly");
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
		
		
		address_select.temp.checkdata[s.id]={};
		
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
				
				address_select.temp.checkdata[s.id][aV[i]]={key:aV[i],text:aT[i]};
			}
			
		}

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			address_select.upData(oData, s);

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


		address_select.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
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
				//var opts =address_select.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=address_select.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						address_select.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				address_select.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(address_select.temp.loadcount[sId]!==undefined)
				{
					var checked = address_select.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=address_select.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={key:othis["f_1"],text:othis["f_2"]};
						
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_value:function(sFncName,sParentId,sBoxId)
	{
		var checked = address_select.temp.loadcount[sBoxId].datagrid('getChecked');
		if(checked.length<1){
			zapadmin.model_message('请选择记录');
			return ;
		}
		address_select.reset_checked(sBoxId);
		var tempcheck=address_select.temp.checkdata[sBoxId];
		var skuCodes = [];
		var code = "";
		var name = "";
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				skuCodes.push(othis["key"]);
				code = othis["key"];
				name = othis["text"];
			}
		}
		var opt ={};
		opt.skuCodes = skuCodes;
		opt.eventCode = parent.$("#zw_f_event_code").val();
		parent.$("#zw_f_small_seller_code").val(code);
		parent.$("#zw_f_small_seller_name").val(name);
		parent.$('#' + sParentId + 'zapadmin_single_showbox').window('close');
	}
	
	
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/address_select", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return address_select;
	});
}
