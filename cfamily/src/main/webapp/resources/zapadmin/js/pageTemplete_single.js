
var pageTemplete_single = {

		temp : {
			opts : {},
			checkdata : {}

		},

		init : function(options) {

			var defaults = {
				pagecode : '',
				id : '',
				buttonflag:true,
				callback:'parent.pageTemplete_single.result'
			};

			var s = $.extend({}, defaults, options || {});

			pageTemplete_single.temp.opts[s.id] = s;

			if(s.buttonflag)
			{
				$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn" type="button" value="选择" onclick="pageTemplete_single.show_box(\'' + s.id + '\')" /></div>');
			
				$('#' + s.id+"_show_text").parent().append('<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul" id="' + s.id + '_show_ul"></ul></div><div class="w_clear"></div>');
			}
			pageTemplete_single.show_text(s.id);
		},
		show_box : function(sId) {
			var s=pageTemplete_single.temp.opts[sId];
			zapjs.f.window_box({
				id : sId + 'pageTemplete_single_showbox',
				content : '<iframe src="../show/'+s.source+'?zw_f_seller_code='+s.seller_code+'&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

				width : '700',
				height : '550'
			});

		},
		close_box:function(sId)
		{
			zapjs.f.window_close(sId + 'pageTemplete_single_showbox');
		},
		show_text : function(sId) {

			var sText = $('#' + sId + "_show_text").val();
			$('#' + sId + '_show_ul').html('');
			
			var sValues =  $('#' + sId).val();
			
			if (sText && sValues) {
				var aT = sText.split(',');
				var aV = sValues.split(',');
				for (var i in aT) {
					if(pageTemplete_single.temp.opts[sId].single == true) {
						$('#' + sId + '_show_ul').append('<li id="select_num_'+aT[i]+'" style="position: relative;margin-left:9px;margin-botton:9px">' + aT[i] + '<i style="position: absolute;background: #f1f1f1;border: solid 1px #ccc;text-align: center;  right: -10px;top: -10px;font-style:normal;  width: 15px;height: 15px;line-height: 15px;cursor: pointer;display: inline-block;" onClick="javascript:pageTemplete_single.delTemPlete(\''+aT[i]+'\')">X</i></li>');
					} else {
						$('#' + sId + '_show_ul').append('<li id="select_num_'+aT[i]+'" class="control-upload-list-li"><div><div class="w_left w_p_10"><span>'+aT[i]+'</span><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'up\')">上移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'down\')">下移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,\'delete\')">删除</a></div><div class="w_clear"></div></div></li>');
					}
				}

			}

		},
		result : function(sId, sValues, sTexts,minSellPrice,minStock) {

			$('#' + sId).val(sValues);
			$('#' + sId + "_show_text").val(sTexts);

			zapjs.f.window_close(sId + 'pageTemplete_single_showbox');

			pageTemplete_single.show_text(sId);

		},
		delTemPlete : function(sid){
			$("#zw_f_template_number").val("");
			$("#select_num_"+sid).remove();
		},
		//上下移动选中的多个模板
		change_index: function(obj,flg) {
			if(flg == "up") {
				
				var curntHtml = $(obj).parent().find("span").eq(0).html();
				var upHtml = $(obj).parents("li").prev().find("span").eq(0).html();
				$(obj).parent().find("span").eq(0).html(upHtml);
				$(obj).parents("li").prev().find("span").eq(0).html(curntHtml);
				
			} else if(flg == "down") {
				
				var curntHtml = $(obj).parent().find("span").eq(0).html();
				var nextHtml = $(obj).parents("li").next().find("span").eq(0).html();
				$(obj).parent().find("span").eq(0).html(nextHtml);
				$(obj).parents("li").next().find("span").eq(0).html(curntHtml);
				
			} else if(flg == "delete") {
				$(obj).parent().parent().parent().remove();
			}
		}
	};

	if ( typeof define === "function" && define.amd) {
		define("cevent/js/pageTemplete_single", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
			return pageTemplete_single;
		});
	}
