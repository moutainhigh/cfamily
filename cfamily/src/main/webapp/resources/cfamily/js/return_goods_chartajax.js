var return_goods_chartajax = {

	temp : {
		loadcount : {},
		checkdata:{},
		options:{}
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
			source : null,
			callback:'parent.return_goods_chartajax.result'
		};

		var s = $.extend({}, defaults, options || {});
		
		return_goods_chartajax.temp.options=s;
		return_goods_chartajax.temp.checkdata[s.id]={};
		
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
				
				return_goods_chartajax.temp.checkdata[s.id][aV[i]]={key:aV[i],text:aT[i]};
			}
			
		}
		
		

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			return_goods_chartajax.upData(oData, s);

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


		return_goods_chartajax.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
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
				//var opts =return_goods_chartajax.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=return_goods_chartajax.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						return_goods_chartajax.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				return_goods_chartajax.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(return_goods_chartajax.temp.loadcount[sId]!==undefined)
				{
					var checked = return_goods_chartajax.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=return_goods_chartajax.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={key:othis["f_0"],text:othis["f_1"]};
						
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_value:function(sFncName,sParentId,sBoxId)
	{
		
		//add by jlin 2015-07-01 09:58:00
		var checked = return_goods_chartajax.temp.loadcount[sBoxId].datagrid('getChecked');
		if(checked.length<1){
			zapadmin.model_message('请选择记录12121212');
			return ;
		}
		
		return_goods_chartajax.reset_checked(sBoxId);
		
		var tempcheck=return_goods_chartajax.temp.checkdata[sBoxId];
		
		var aValues=[];
		var aTexts=[];
		
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
			
				aValues.push(othis["key"]);
				aTexts.push(othis["text"]);
			}
			
			
		}
		
		
		
		//此处为解决eaysui单选分页的bug
		var maxnum = return_goods_chartajax.temp.options["maxnum"];
		if(maxnum>0){
			if(aValues.length>maxnum){
				for (var i=0; i<(aValues.length-maxnum);i++) {
					aValues.shift();
					aTexts.shift();
				}
			}
		}
		
		
		//由于时间关系，此处只对 " 做一个处理，其他特殊字符有时间再说
		var temp_aValues=aTexts.join(',');
		temp_aValues=temp_aValues.replace(/"/g,'\\\"');
		
		eval( sFncName+'("'+ sParentId +'","'+ aValues.join(',') +'","'+ temp_aValues +'")' );
		
	},	
	result : function(sId, sValues, sTexts) {
		alert("1243zmm")
		var mobile = '';
		var receiver = '';
		mobile = $('#f_2').val(sValues);
		receiver = $('#f_3').val(sTexts);
		alert(mobile+receiver);
		zapjs.f.window_close(sId + 'zapadmin_single_showbox');

		zapadmin_single.show_text(sId);

	}
	
	
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/return_goods_chartajax", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return return_goods_chartajax;
	});
}
