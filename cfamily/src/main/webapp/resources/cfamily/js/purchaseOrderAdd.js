var purchaseOrderAdd ={
	temp : {
		//基本信息
		purchaseBaseInfoJson:{},
		//订单信息
		purchaseOrdersJson:[],
		//地址信息
		addressInfosJson:[],
		//下单编号
		purchaseId:"",
		//选中地址
		selectAddressId:""
	},
	init:function (obj){
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',purchaseOrderAdd.beforesubmit);
		purchaseOrderAdd.temp.purchaseId ="TEM"+ purchaseOrderAdd.selfuuid();
		if (!obj) {
		}else{
			purchaseOrderAdd.temp.purchaseBaseInfoJson = obj.purchaseBaseInfo;
			purchaseOrderAdd.temp.purchaseOrdersJson = obj.orderDetailList;
			purchaseOrderAdd.temp.addressInfosJson = obj.addressDetailList;
		}
		//进行一系列赋值操作
		purchaseOrderAdd.initDate();
		purchaseOrderAdd.initValue();
	},
	beforesubmit:function(){
		var copyValue = $("#zw_f_purchase_text_copy").val();
		$("#zw_f_purchase_text").val(copyValue);
		if (null == purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus || "" == purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
		  alert("请选择采购下单商品");
		  return false;
		}else{
			var temInitOrderSkusInfo = [];
			var hasSelect = false;
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		    for(var i in temInitOrderSkusInfo){
		    	var subItem = temInitOrderSkusInfo[i].split("_");
		        if(subItem[4]==1){
		        	hasSelect = true;
		        	break;
		        }
		    }
		    if(hasSelect==false){
		    	alert("请选择采购下单商品");
		    	return false;
		    }else{
		    	$("#zw_f_basic_order_skus").val(purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus);
		    }
		    if(purchaseOrderAdd.temp.selectAddressId==""){
		    	alert("请选择确认收货地址");
		    	return false;
		    }else{
		    	$("#zw_f_adress_id").val(purchaseOrderAdd.temp.selectAddressId);
		    }
		    $("#zw_f_purchase_order_id").val(purchaseOrderAdd.temp.purchaseId);
		    
		}
		return true;
	},
	initDate : function(){
		if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson && '{}' != JSON.stringify(purchaseOrderAdd.temp.purchaseBaseInfoJson)) {
			if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.purchase_text && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.purchase_text) {
				$("#zw_f_purchase_text").val(purchaseOrderAdd.temp.purchaseBaseInfoJson.purchase_text);
				$("#zw_f_purchase_text_copy").val(purchaseOrderAdd.temp.purchaseBaseInfoJson.purchase_text);
			}
			if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
				$("#zw_f_basic_order_skus").val(purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus);
				purchaseOrderAdd.temp.purchaseId =purchaseOrderAdd.temp.purchaseBaseInfoJson.purchase_order_id;
				purchaseOrderAdd.temp.selectAddressId = purchaseOrderAdd.temp.purchaseBaseInfoJson.adress_id;
			}
/*			for(var p in purchaseOrderAdd.temp.purchaseOrdersJson){
				if(purchaseOrderAdd.temp.purchaseOrdersJson[p].if_selected=="1"){
					 purchaseOrderAdd.temp.selectSkuCodes.push(purchaseOrderAdd.temp.purchaseOrdersJson[p].sku_code);
				}
			}*/
		}
	},
	initValue:function(){
		purchaseOrderAdd.productTable_init();
		purchaseOrderAdd.addressInfo_init();
		
	},
	
	delAddress:function(v){
	    var addId = $(".sc-dz").parent().parent().find("input[type=radio][value="+v+"]").val();
		var obj = {};
		obj.purchase_order_id =purchaseOrderAdd.temp.purchaseId ;
		obj.adress_id =addId ;
		obj.select_flag = "0";
		obj.receiver ="" ;
		obj.province_city_district_code = "" ;
		obj.detail_addtess= "";
		obj.postcode = "" ;
		obj.phone = "" ;
		obj.identity_number = "";
		obj.if_delete ="0" ;
		obj.operateName ="delete" ;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseAddress',obj,function(result) {
			if(result.resultCode==1){
				purchaseOrderAdd.temp.addressInfosJson = result.addressDetailList;
				purchaseOrderAdd.addressInfo_init();
				if(purchaseOrderAdd.temp.selectAddressId==v){
					purchaseOrderAdd.temp.selectAddressId="";
				}
				}
			else{
					zapadmin.model_message('删除地址失败');
				}
			});
	},
	editAddress:function(){
		if($("#receiverId").val()==null||$("#receiverId").val()==""){
			alert("收货人不能为空！");
			return ;
		}
		if($("#_city").val()==null||$("#_city").val()==""){
			alert("所在地不能为空！");
			return ;
		}
		if($("#detailId").val()==null||$("#detailId").val()==""){
			alert("详细地址不能为空！");
			return ;
		}
		if($("#phoneId").val()==null||$("#phoneId").val()==""){
			alert("联系电话不能为空！");
			return ;
		}
	/*	if($("#idenId").val()==null||$("#idenId").val()==""){
			alert("身份证号不能为空！");
		}*/
		if($("#_area").val()==null){
			$("#_area").val("")
		}
		
		var obj = {};
		obj.purchase_order_id =purchaseOrderAdd.temp.purchaseId ;
		obj.adress_id =$("#addressId").val() ;
		obj.select_flag = purchaseOrderAdd.temp.selectAddressId==$("#addressId").val()?"1":"0";
		obj.receiver =$("#receiverId").val() ;
		obj.province_city_district_code = $("#_province").val()+"_"+$("#_city").val()+"_"+$("#_area").val()+"_"+$("#_street").val() ;
		obj.detail_addtess= $("#detailId").val();
		obj.postcode = $("#postId").val() ;
		obj.phone = $("#phoneId").val() ;
		obj.identity_number = $("#idenId").val();
		obj.if_delete ="1" ;
		obj.operateName ="edit" ;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseAddress',obj,function(result) {
			if(result.resultCode==1){
				purchaseOrderAdd.temp.addressInfosJson = result.addressDetailList;
				purchaseOrderAdd.addressInfo_init();
				
				//清空地址字段
				$("#receiverId").val("");
				$("#_province").val("");
				$("#_city").val("");
				$("#_area").val("");
				$("#_street").val("");
		        $("#detailId").val("");
		        $("#postId").val("");
		        $("#phoneId").val("");
		        $("#idenId").val("");
		        $("#addressId").val("");
		        $("#ht-zjdz").hide();

		        
			}else{
				zapadmin.model_message('编辑地址失败');
			}
		});
		},

	   saveAddress:function(){
		if($("#addressId").val()!=""){
			purchaseOrderAdd.editAddress();
			return ;
		}
		if($("#receiverId").val()==null||$("#receiverId").val()==""){
			alert("收货人不能为空！");
			return ;
		}
		if($("#_city").val()==null||$("#_city").val()==""){
			alert("所在地不能为空！");
			return ;
		}
		if($("#detailId").val()==null||$("#detailId").val()==""){
			alert("详细地址不能为空！");
			return ;
		}
		if($("#phoneId").val()==null||$("#phoneId").val()==""){
			alert("联系电话不能为空！");
			return ;
		}
	/*	if($("#idenId").val()==null||$("#idenId").val()==""){
			alert("身份证号不能为空！");
		}*/
		if($("#_area").val()==null){
			$("#_area").val("")
		}

		var obj = {};
		obj.purchase_order_id =purchaseOrderAdd.temp.purchaseId ;
		obj.adress_id ="" ;
		obj.select_flag = "0";
		obj.receiver =$("#receiverId").val() ;
		obj.province_city_district_code = $("#_province").val()+"_"+$("#_city").val()+"_"+$("#_area").val()+"_"+$("#_street").val() ;
		obj.detail_addtess= $("#detailId").val();
		obj.postcode = $("#postId").val() ;
		obj.phone = $("#phoneId").val() ;
		obj.identity_number = $("#idenId").val();
		obj.if_delete ="1" ;
		obj.operateName ="add" ;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseAddress',obj,function(result) {
			if(result.resultCode==1){
				purchaseOrderAdd.temp.addressInfosJson = result.addressDetailList;
				purchaseOrderAdd.addressInfo_init();
		 	
				//清空地址字段
				$("#receiverId").val("");
				$("#_province").val("");
				$("#_city").val("");
				$("#_area").val("");
				$("#_street").val("");
		        $("#detailId").val("");
		        $("#postId").val("");
		        $("#phoneId").val("");
		        $("#idenId").val("");
		        $("#addressId").val("");
		        $("#ht-zjdz").hide();

		        
			}else{
				zapadmin.model_message('保存地址失败');
			}
		});
	},
	selfuuid:function () {
	    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
	        var r = Math.random() * 16 | 0,
	            v = c == 'x' ? r : (r & 0x3 | 0x8);
	        return v.toString(16);
	    });
	},
	openAddressForEdit:function(v){
		var addId = $(".sc-dz").parent().parent().find("input[type=radio][value="+v+"]").val();
		var temArray = purchaseOrderAdd.temp.addressInfosJson;
        for(var p in temArray){
        	var item = temArray[p];
        	if(item.adress_id==v){
        		var sbuItem = item.province_city_district_code.split("_");
        		$("#ht-zjdz").show();
        		$("#_province").change();
        		$("#receiverId").val(item.receiver);
				$("#detailId").val(item.detail_addtess);
		        $("#postId").val(item.postcode);
		        $("#phoneId").val(item.phone);
		        $("#idenId").val(item.identity_number);
		        $("#addressId").val(item.adress_id);
		        $("#_province").attr("value",sbuItem[0]);
				$("#_city").attr("value",sbuItem[1]);
				$("#_area").attr("value",sbuItem[2]);
				if(sbuItem.length==4){
					$("#_street").attr("value",sbuItem[3]);
				}
				
				purchaseOrderAddress.initAddress();
				$('#_province').find("option[value='"+sbuItem[0]+"']").attr('selected','selected');
				$('#_province').change();
				$('#_city').find("option[value='"+sbuItem[1]+"']").attr('selected','selected');
				$('#_city').change();
				$('#_area').find("option[value='"+sbuItem[2]+"']").attr('selected','selected');
				$('#_area').change();
				if(sbuItem.length==4){
					$('#_street').find("option[value='"+sbuItem[3]+"']").attr('selected','selected');
				}
				
        	}
        }
        $("#ht-zjdz").show();
	},
	addressInfo_init:function(){
	    $("body").css("height","auto");
	    $("body").css("overflow","auto"); 
	 	$("#xz-sp-ht").hide();
	 	$("#ht-zjdz").hide();
		$('#ht-dizhi').html("");
		var temArray = [];
		var objArray = purchaseOrderAdd.temp.addressInfosJson;
		temArray.push('<div class="ht-dz-g">确认收货地址   <span id="zjdz-btn"><img src="../resources/cfamily/images/ht-tjdz.png" /></span></div>');
		//var objArray =[{adress_id:1111,detail_addtess:2222,receiver:33333,phone:19222222222},{adress_id:1221,detail_addtess:2222,receiver:33333,phone:19222222222}];
		for(var p in objArray){
			var item = objArray[p];
			var pcdv = item.pcdv;
			var flag = (purchaseOrderAdd.temp.selectAddressId==item.adress_id)?"checked":"";
			if(p==0){
				temArray.push('<div class="ht-dz-d">');
				temArray.push('<div class="ht-p-1"><input type="radio" '+flag+' value='+item.adress_id+' class = "dizhi" name="dizhi"/>'+pcdv+item.detail_addtess+'<span><b>'+item.receiver+'</b> 收</span><span>'+item.phone+'</span><div><span class="bj-dz" onclick = "purchaseOrderAdd.openAddressForEdit(\''+item.adress_id+'\')" >编辑</span><span class="sc-dz" onclick = "purchaseOrderAdd.delAddress(\''+item.adress_id+'\')">删除</span></div></div>');
			}else if(p==1){
				temArray.push('<div style="display:none" class="dizhi-zk" >');
				temArray.push('<div class="ht-p-1"><input type="radio" '+flag+' value='+item.adress_id+' class = "dizhi" name="dizhi"/>'+pcdv+item.detail_addtess+'<span><b>'+item.receiver+'</b> 收</span><span>'+item.phone+'</span><div><span class="bj-dz" onclick = "purchaseOrderAdd.openAddressForEdit(\''+item.adress_id+'\')" >编辑</span><span class="sc-dz" onclick = "purchaseOrderAdd.delAddress(\''+item.adress_id+'\')">删除</span></div></div>');
			}else if(p==(objArray.length-1)){
				temArray.push('<div class="ht-p-1"><input type="radio" '+flag+' value='+item.adress_id+' class = "dizhi" name="dizhi"/>'+pcdv+item.detail_addtess+'<span><b>'+item.receiver+'</b> 收</span><span>'+item.phone+'</span><div><span class="bj-dz" onclick = "purchaseOrderAdd.openAddressForEdit(\''+item.adress_id+'\')">编辑</span><span class="sc-dz" onclick = "purchaseOrderAdd.delAddress(\''+item.adress_id+'\')">删除</span></div></div></div>');
			}else{
				temArray.push('<div class="ht-p-1"><input type="radio" '+flag+' value='+item.adress_id+' class = "dizhi" name="dizhi"/>'+pcdv+item.detail_addtess+'<span><b>'+item.receiver+'</b> 收</span><span>'+item.phone+'</span><div><span class="bj-dz" onclick = "purchaseOrderAdd.openAddressForEdit(\''+item.adress_id+'\')">编辑</span><span class="sc-dz" onclick = "purchaseOrderAdd.delAddress(\''+item.adress_id+'\')">删除</span></div></div>');
			}
		}
		temArray.push('<div class="ht-sz-gd"></div></div><p class="gd-ht-dz"><img src="../resources/cfamily/images/xl-ht.png" /></p>');
		
		$('#ht-dizhi').append(temArray.join(''));
	},
	productTable_init : function(){
		$('#productTable').html("");
		$('#productTable').append('<tr><th scope="col"><input type="checkbox" id="ht_table2_qx"/> 全选</th><th scope="col">商品</th><th scope="col">商品成本</th><th scope="col">商品售价</th><th scope="col">数量</th><th scope="col">小计(元)</th> <th scope="col">操作</th></tr>');
		var temTable = [];
		//var objArray =purchaseOrderAdd.deepCopy(purchaseOrderAdd.temp.purchaseOrdersJson);
		var objArray =purchaseOrderAdd.temp.purchaseOrdersJson;
		var initOrderSkusInfo = [];
		var temNewBasicOrderSkus="";
		var selectNum = 0;
		var payMoney = 0;
		if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			initOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}

		for(var p in objArray){
			var othis = objArray[p];
	        for(var i in initOrderSkusInfo){
	        	var subItem = initOrderSkusInfo[i].split("_");
	        	if(subItem[0]==othis.sku_code){
	        		othis.cost_money = subItem[1];
	        		othis.sell_money = subItem[2];
	        		othis.sku_num = subItem[3];
	        		othis.if_select = subItem[4];
	        		break;
	        	}
	        }
	        othis.rowSumMoney = othis.sell_money*othis.sku_num;	
	        if(temNewBasicOrderSkus==""){
	        	temNewBasicOrderSkus = othis.sku_code+"_"+othis.cost_money+"_"+othis.sell_money+"_"+othis.sku_num+"_"+othis.if_selected;
	        	}
	        else{temNewBasicOrderSkus = temNewBasicOrderSkus+","+othis.sku_code+"_"+othis.cost_money+"_"+othis.sell_money+"_"+othis.sku_num+"_"+othis.if_selected;
                }
			if(othis.if_selected==1){
				temTable.push("<tr> <td><input type='checkbox' checked=true  class='sku_code_class' value="+othis.sku_code+" /></td>");
				//统计选中的数据
				selectNum = selectNum+Number(othis.sku_num);
		        payMoney = payMoney+othis.rowSumMoney;
			}else{
				temTable.push("<tr> <td><input type='checkbox'  class='sku_code_class' value="+othis.sku_code+" /></td>");
			}	
			var propArray = othis.product_property.split("&");
			temTable.push(" <td> <img src='"+othis.product_img +"' width='66' height='66'  alt=''/> <p>"+othis.product_name+"</p>  <p class='ht-ks'>"+propArray[0]+"<br />");
			if(propArray.length>1){
				temTable.push(""+propArray[1]+"</p></td>");	
			}else{
				temTable.push("</p></td>");
			}
			temTable.push("<td><input type='text' class='sku_cost_class' style='width:70px' value="+othis.cost_money+" /></td>");
			temTable.push("<td><input type='text' class='sku_sell_class' style='width:70px' value="+othis.sell_money+" /></td>");
			temTable.push("<td><span class='ht-jian' >-</span><input type='text' class='sku_num_class' value="+othis.sku_num+" /><span  class='ht-jia'>+</span></td>");
			temTable.push(" <td><b class='ht-chgeng'>¥"+othis.rowSumMoney+"</b></td>");
			temTable.push("<td><b class='ht-lan'>删除</b></td></tr>");
		}
		$('#productTable').append(temTable.join(''));
		$('.choose_num').text(selectNum);
		var copyPayMoney = Number(payMoney);
		copyPayMoney =  copyPayMoney.toFixed(2);
		$('.choose_money').text("¥"+copyPayMoney);
		purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temNewBasicOrderSkus;
		$("#zw_f_basic_order_skus").val(temNewBasicOrderSkus);
		
	},
	
	deepCopy : function (obj){
	    if(typeof obj != 'object'){
	        return obj;
	    }
	    var newobj = {};
	    for ( var attr in obj) {
	        newobj[attr] = purchaseOrderAdd.deepCopy(obj[attr]);
	    }
	    return newobj;
	},
	initProductSelect : function(rows){
		 var productCodes = "";
		 var subsidyCodes = "";
		 for ( var int = 0; int < rows.length; int++) {
			 productCodes += rows[int].skuCode+",";
			 subsidyCodes += rows[int].subsidy+",";
		}
		if (productCodes.length > 0) {
			productCodes = productCodes.substr(0, productCodes.length - 1);
		}
		$('#zw_f_sku_code').val(productCodes);
		$('#zw_f_sku_code_show_text_show_subsidy').val(subsidyCodes);
	},
	
};

/*$(document).ready(function(){
	
	purchaseOrderAdd.addNum();
	purchaseOrderAdd.reduceNum();
	
});*/