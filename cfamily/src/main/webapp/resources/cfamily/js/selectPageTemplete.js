var selectPageTemplete = {

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
		
		
		selectPageTemplete.temp.checkdata[s.id]={};
		
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
				
				selectPageTemplete.temp.checkdata[s.id][aV[i]]={key:aV[i],num:aT[i]};
			}
			
		}

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			selectPageTemplete.upData(oData, s);

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


		selectPageTemplete.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
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
				//var opts =selectPageTemplete.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=selectPageTemplete.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						selectPageTemplete.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				selectPageTemplete.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(selectPageTemplete.temp.loadcount[sId]!==undefined)
				{
					var checked = selectPageTemplete.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=selectPageTemplete.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={key:othis["f_0"],num:othis["f_1"],name:othis["f_2"]};
						
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_value:function(sFncName,sParentId,sBoxId)
	{
		selectPageTemplete.reset_checked(sBoxId);
		var tempcheck=selectPageTemplete.temp.checkdata[sBoxId];

		var templeteNum = "";
		var templeteTypeName = "";
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				templeteNum = othis["num"];
				templeteTypeName = othis["name"];
			}
		}
		if(parent.pageTemplete_single.temp.opts[sBoxId].single == true) {
			parent.$('#' + sBoxId + '_show_ul').html('<li id="select_num_'+templeteNum+'" style="position: relative;margin-left:9px;margin-botton:9px">' + templeteNum + '<i style="position: absolute;background: #f1f1f1;border: solid 1px #ccc;text-align: center;  right: -10px;top: -10px;font-style:normal;  width: 15px;height: 15px;line-height: 15px;cursor: pointer;display: inline-block;" onClick="pageTemplete_single.delTemPlete(\''+templeteNum+'\')">X</i></li>');
		} else {
			parent.$('#' + sBoxId + '_show_ul').append('<li id="select_num_'+templeteNum+'" class="control-upload-list-li"><div><div class="w_left w_p_10"><span>'+templeteNum+'</span><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'up\')">上移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'down\')">下移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'delete\')">删除</a></div><div class="w_clear"></div></div></li>');
		}
		
		parent.$("#"+sParentId).val(templeteNum);
		parent.$('#' + sParentId + 'pageTemplete_single_showbox').window('close');
	}
	
	
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/selectPageTemplete", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return selectPageTemplete;
	});
}
