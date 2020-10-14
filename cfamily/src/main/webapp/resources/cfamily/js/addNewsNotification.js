var addNewsNotification = {
	init : function() {
		addNewsNotification.appendElements();
		//默认为消息，隐藏处罚相关的内容
		$("#zw_f_company_code").parent().parent().hide();
		$("#zw_f_order_code").parent().parent().hide();
		$("#zw_f_order_time").parent().parent().hide();
		$("table").hide();
		
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', addNewsNotification.beforeSubmit);

	},
	beforeSubmit : function() {
		
		var noticeType = $("#zw_f_notice_type").val();
		if('4497471600370001'==noticeType){
			//消息
		}else if('4497471600370002'==noticeType){
			//惩罚
			var productsInfo = "";
			var flag = 1;
			$('table tr').each(function(i){                   // 遍历 tr
		       $(this).children('td').each(function(j){  // 遍历 tr 的各个 td
//		    	   console.log("第"+(i+1)+"行，第"+(j+1)+"个td的值："+$(this).text()+"。");
		    	   if(j==4||j==5){
		    		   var  x = $(this).children("input").val();
		    		   if(x.trim()==''){
		    			   flag =  0;
		    		   }
		    		   productsInfo  = productsInfo + x +  "="
		    	   }else if(j<4){
		    		   productsInfo  = productsInfo +  $(this).text() +  "="
		    	   }
		       });
		       if(""!=productsInfo){
		    	   productsInfo  = productsInfo.substr(0,productsInfo.length-1) + ","
		       }
		    });
			if(""!=productsInfo){
				productsInfo  = productsInfo.substr(0,productsInfo.length-1)
			}
			$('#zw_f_products_info').val(productsInfo)
			if(""==productsInfo){
				zapjs.f.message("当前无罚款商品，不能提交哦");
				return false;
			}
			if(flag==0){
				zapjs.f.message("罚款金额，处罚原因不能为空");
				return false;
			}
			//判断有无空内容
			if($('#zw_f_company_code').val().trim==''||$('#zw_f_order_code').val().trim==''||$('#zw_f_order_time').val().trim==''){
				zapjs.f.message("输入框不能为空");
				return false;
			}
			
		}
		
		
		
		return true;
	},
	appendElements: function(){
		var html1 = '<div class="control-group"><label class="control-label" for="zw_f_order_code">订单编号：</label><div class="controls"><input type="text" id="zw_f_order_code" name="zw_f_order_code"  zapweb_attr_regex_id="469923180001"  value="">&nbsp;&nbsp;&nbsp;<input type="button" class="btn  btn-success" zapweb_attr_operate_id=""  onclick="addNewsNotification.search()"  value="查询" /></div></div>'
		$("form").append(html1);
		var html2 = '<div class="control-group"><label class="control-label" for="zw_f_order_time">下单时间：</label><div class="controls"><input type="text" id="zw_f_order_time" name="zw_f_order_time"  zapweb_attr_regex_id="469923180001"  value=""></div></div>';
		$("form").append(html2);
		var html3 = '<table style="width:900px;margin:50px 180px;" class="table  table-condensed table-striped table-bordered table-hover"><thead><tr><th>sku编号</th><th>商品名称</th><th>商品成本</th><th>商品售价</th><th>处罚金额</th><th>处罚原因</th><th  colspan="4" width="12%">操作</th></tr></thead></table>'
		$("form").append(html3);
		var html4 = '<div style="display:none" class="control-group"><label class="control-label" for="zw_f_products_info"></label><div class="controls"><input type="text" id="zw_f_products_info" name="zw_f_products_info"  zapweb_attr_regex_id="469923180001"  value=""></div></div>'
		$("form").append(html4);
		var html5 = '<div class="control-group"><div class="controls"><input type="button" class="btn  btn-success" zapweb_attr_operate_id="37a2941183ef11e8b8444050561000uu"  onclick="zapjs.zw.func_add(this)"  value="提交" /></div></div>'
		$("form").append(html5);
	},
	search:function(){
		//获取到当前输入订单号和公司编号
		var orderCode =  $("#zw_f_order_code").val()
		var companyCode =  $("#zw_f_company_code").val()
		var oData = {};
		oData.orderCode = orderCode;
		oData.companyCode  = companyCode;
		var defaults ={
			api_target : 'com_cmall_familyhas_api_ApiOrderCodeSearchInfo',
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		};
		$.ajax({
	        url : '../jsonapi/com_cmall_familyhas_api_ApiOrderCodeSearchInfo',
	        data: defaults,
	        cache : false, 
	        async : false,
	        type : "POST",
	        dataType : 'json',
	        success : function (data){
	        	if(data.resultCode==1){
					//输入正确,将订单信息拼接进页面
	        		var orderTime  = data.orderTime;
	        		$("#zw_f_order_time").val(orderTime)
	        		var list = data.list;
	        		for(var i  =  0;i<list.length;i++){
	        			var obj  = list[i];
	        			var productCode =  obj.productCode;
	        			var productName = obj.productName;
	        			var productCost  = obj.productCost
	        			var productSellPrice =  obj.productSellPrice;
	        			
	        			//table表格追加元素 
	        			var tr  = "<tr> " +
	        							"<td>"+productCode+"</td> " +
	        							"<td>"+productName+"</td> " +
	        							"<td>"+productCost+"</td> " +
	        							"<td>"+productSellPrice+"</td> " +
	        							"<td><input type='text' id='zw_f_punish_money"+i+"' name='zw_f_punish_money"+i+"' value=''></td> " +
	        							"<td><input type='text' id='zw_f_punish_reason"+i+"' name='zw_f_punish_reason"+i+"' value=''></td> " +
	        							"<td><input type='button' class='btn  btn-success' zapweb_attr_operate_id=''  onclick='addNewsNotification.del(this)'  value='删除' /></td> " +
	        					"</tr>";
	        			//删除除第一列之外的所有行
	        			$("table tr:not(:first)").empty("");
	        			$("table").append(tr);
	        			
	        		}
	        		
				}else{
					//输入错误
					zapjs.f.message(data.resultMessage);
				}
	        }
	    });
		
	},
	del : function(obj){
		$(obj).parents("tr").remove();
	},
	initNewsInfo : function(){
		var noticeType = $("#zw_f_notice_type").val();
		if('4497471600370001'==noticeType){
			//消息，隐藏处罚相关的内容
			$("#zw_f_company_code").parent().parent().hide();
			$("#zw_f_order_code").parent().parent().hide();
			$("#zw_f_order_time").parent().parent().hide();
			$("table").hide();
			//显示消息相关的内容
			$("#zw_f_notice_content").parent().parent().show();
			$("#zw_f_opening_merchant_confirmation").parent().parent().show();
		}else if('4497471600370002'==noticeType){
			//惩罚，显示处罚相关的内容
			$("#zw_f_company_code").parent().parent().show();
			$("#zw_f_order_code").parent().parent().show();
			$("#zw_f_order_time").parent().parent().show();
			$("#zw_f_opening_merchant_confirmation").parent().parent().hide();
			$("table").show();
			//隐藏消息相关的内容
			$("#zw_f_notice_content").parent().parent().hide();
		}
	}

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addColumn", function() {
		return addColumn;
	});
}
// 切换是否显示更多时使用
$(document)
		.ready(
				function() {
					$("#zw_f_notice_type").change(function() {
						addNewsNotification.initNewsInfo();
					});
				});