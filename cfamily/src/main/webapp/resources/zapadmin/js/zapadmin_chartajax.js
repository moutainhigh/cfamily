var zapadmin_chartajax = {

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
			source : null
		};

		var s = $.extend({}, defaults, options || {});
		
		zapadmin_chartajax.temp.options=s;
		zapadmin_chartajax.temp.checkdata[s.id]={};
		
		var sValues=parent.$('#'+s.id).val();
		var sTexts=parent.$('#'+s.id+"_show_text").val();
		
		
		if(s.maxnum==1){
			
		}else if(sValues) {
			var aV=sValues.split(',');
			var aT=sTexts.split(',');
			
			for(var i=0,j=aV.length;i<j;i++)
			{
				
				zapadmin_chartajax.temp.checkdata[s.id][aV[i]]={key:aV[i],text:aT[i]};
			}
			
		}
		
		

		zapjs.zw.api_call('com_srnpr_zapweb_webapi_ChartApi', {
			pageCode : s.pagecode
		}, function(oData) {
			zapadmin_chartajax.upData(oData, s);

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


		zapadmin_chartajax.temp.loadcount[s.id] =$('#' + s.id).find('table').datagrid({
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
				//var opts =zapadmin_chartajax.temp.loadcount[s.id].getChecked();
				//window.console.debug(opts);
				
				var odata=tableData.rows;
				
				var tempcheck=zapadmin_chartajax.temp.checkdata[s.id];
				//zapjs.d(odata);
				for(var p in odata)
				{
					var othis=odata[p];
					if(tempcheck[othis["f_0"]])
					{
						tempcheck[othis["f_0"]]=null;
						
						zapadmin_chartajax.temp.loadcount[s.id].datagrid('checkRow',p);
					}
					
					
				}
				
			},
			onBeforeLoad:function(param)
			{
				zapadmin_chartajax.reset_checked(s.id);
			},

			url : sDataUrl
		});
		

	},
	reset_checked:function(sId)
	{
		if(zapadmin_chartajax.temp.loadcount[sId]!==undefined)
				{
					var checked = zapadmin_chartajax.temp.loadcount[sId].datagrid('getChecked');
					
					var tempcheck=zapadmin_chartajax.temp.checkdata[sId];
					
					for(var p in checked)
					{
						var othis=checked[p];
						tempcheck[othis["f_0"]]={
								key:othis["f_0"],
								text:othis["f_1"],
								price:othis["f_3"]   // 销售价格
								};
						/*  TODO 如果有你需要的可以加入进去 - 20170511-Yangcl
						f_0 : "8016410846"
						f_1 : "植美村 温和卸妆喷雾120ml"
						f_2 : "8016410846"                                           TODO 这里要选择市场价格还是销售价格？？？？？？
						f_3 : "89.00--89.00"
						f_4 : "158.00"
						f_5 : "已上架"
						*/
					}

					$('.datagrid-header-check input').attr('checked',false);
					
				}
	},
	ok_value:function(sFncName,sParentId,sBoxId)
	{
		//add by jlin 2015-07-01 09:58:00
//		debugger;
		var checked = zapadmin_chartajax.temp.loadcount[sBoxId].datagrid('getChecked');
		if(checked.length<1){
			zapadmin.model_message('请选择记录');
			return ;
		}
		
		zapadmin_chartajax.reset_checked(sBoxId);
		
		var tempcheck=zapadmin_chartajax.temp.checkdata[sBoxId];
		
		var aValues=[];
		var aTexts=[];
		var price=[];
		
		for(var p in tempcheck)
		{
			var othis=tempcheck[p];
			if(othis)
			{
				aValues.push(othis["key"]);
				aTexts.push(othis["text"]);
				price.push(othis["price"]); 
			}
			
			
		}
		
		
		
		//此处为解决eaysui单选分页的bug
		var maxnum = zapadmin_chartajax.temp.options["maxnum"];
		if(maxnum>0){
			if(aValues.length>maxnum){
				for (var i=0; i<(aValues.length-maxnum);i++) {
					aValues.shift();
					aTexts.shift();
					price.shift();
				}
			}
		}
		
		
		//由于时间关系，此处只对 " 做一个处理，其他特殊字符有时间再说
		var temp_aValues=aTexts.join(',');
		temp_aValues=temp_aValues.replace(/"/g,'\\\"');
		
		eval( sFncName+'("'+ sParentId +'","'+ aValues.join(',') +'","'+ temp_aValues +'","'+ price.join(',') +'")' );  
		
	}
	
	
};

if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/zapadmin_chartajax", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return zapadmin_chartajax;
	});
}
