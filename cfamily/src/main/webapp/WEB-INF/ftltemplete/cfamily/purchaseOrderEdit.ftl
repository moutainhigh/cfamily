<!--后台批量下单&修改下单公用-->
<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign pOrderId = b_page.getReqMap()["zw_f_purchase_order_id"] >
<#assign pService=b_method.upClass("com.cmall.familyhas.service.PurchaseOrderService")>
<#assign resultMap=pService.getPurchaseOrderInfos(pOrderId)>
<!--['../resources/cfamily/js/distpicker.data.js','../resources/cfamily/js/distpicker.js']-->
<script>
require(['cfamily/js/purchaseOrderAdd'],
function()
{
	zapjs.f.ready(function()
		{
			purchaseOrderAdd.init(${resultMap});
		}
	);
}

);

</script>
<@m_zapmacro_common_page_add b_page />
<!--样式-->
<style>
*{ margin:0; padding:0;}
.houtai-560{ padding:20px 30px}
.ht-bt{ font-size:22px; font-weight:bold; color:#333; line-height:50px; border-bottom:2px #008299 solid}
.ht-xzsp{width:80px;height:28px;background:rgba(0,130,153,1);border-radius:4px; border:none; color:#fff; font-size:14px;}
.ht-szp{ padding:10px 0;}
.houtai-560 table{border-collapse: collapse; border:1px solid rgba(204,204,204,1);}

.ht-table table tr th{ background:#FAFAFA; text-align:left; padding:0 10px; line-height:40px; border-left:none; border-right:none}
.ht-table table tr td{ border-left:none; padding:5px;line-height:40px; border-right:none}
.ht-table table tr td img{ float:left; width:40px; height:40px;}
.ht-table table tr td p{ float:left; width:150px; margin-left:10px; height:40px; letter-spacing: 0;
overflow: hidden; display: -webkit-box;text-overflow: ellipsis; -webkit-line-clamp: 2;  /*要显示的行数*/ -webkit-box-orient: vertical; line-height:20px; font-size:14px;}
.ht-table table tr td .ht-ks{ font-size:12px; color:#999;}
.ht-table table tr td span{ display:inline-block;cursor:pointer; float:left;width:24px; height:24px; line-height:24px; text-align:center;width:24px;
height:24px;background:rgba(240,240,240,1);border:1px solid rgba(229,229,229,1);}
.ht-table table tr td input[type="text"]{ width:40px;padding:0;height:24px;float:left;background:rgba(255,255,255,1);border:1px solid rgba(153,153,153,1); text-align:center}
.ht-table table tr td .ht-chgeng{ color:#F37121}
.ht-table table tr td .ht-lan{ color:#094AB2;cursor:pointer;}
.ht-zj{ padding:10px 0; height:auto; overflow:hidden;}
.ht-zj .ht-zj-l{ float:left}
.ht-zj .ht-zj-l span{cursor:pointer;}


.ht-zj .ht-zj-r{ float:right}
.ht-zj .ht-zj-r .ht-chgeng{ color:#F37121}
.ht-ml10{ margin-left:10px;}
.ht-dizhi .ht-dz-g{ margin:10px 0; margin-top:20px; height:28px;font-size:18px; line-height:28px; padding-bottom:10px; border-bottom:1px solid #E6E6E6; overflow:hidden;}
.ht-dizhi .ht-dz-g span{ float:right;cursor:pointer}
.ht-dz-d{ border-radius:5px; border:1px solid rgba(204,204,204,1); height:auto; overflow:hidden; clear:both; margin-top:15px;}
.ht-p-1{ height:40px; line-height:40px; border-bottom:1px solid rgba(204,204,204,1); padding:0 15px; margin-bottom:10px}
.ht-p-1 div{ float:right;}
.ht-p-1 div span{ margin-left:20px; color:#36C;cursor:pointer}
.gd-ht-dz{ text-align:center; padding:10px 0; cursor:pointer}
.ht-p-1 span{ margin-left:10px;}
.ht-p-1 span b{ color:#F37121; font-size:14px;}
.ht-sm{ width:70%; height:120px; margin-top:15px; border:1px solid rgba(204,204,204,1); border-radius:5px;}
.ht-tijiao{ margin-top:20px; text-align:center;}
.ht-tijiao button{width:185px; color:#fff; border:none; font-size:14px;height:48px;background:linear-gradient(180deg,rgba(0,126,149,1) 0%,rgba(0,104,121,1) 100%);border-radius:4px;}
.ht-spxz{ width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:fixed; top:0; left:0; z-index:9999;}
.ht-spxz-d{ width:800px; border-radius:6px; height:400px; position:absolute; top:50%; left:50%; margin-top:-200px; margin-left:-400px; background-color:#fff;}
.ht-spx-t{ height:50px; line-height:50px; border-bottom:1px solid rgba(204,204,204,1); overflow:hidden; padding:0 15px;}
.ht-spx-t span{ width:20px; cursor:pointer; height:20px; float:right; margin-top:15px;}
.ht-spx-t span img{ width:20px; display:block}
.ht-saixuan{ color:#666; font-size:14px; padding:30px; padding-bottom:20px;}
.ht-saixuan input{ width:120px; padding:0 10px; height:30px; border-radius:5px; border:1px solid #C4C6CF}
.ht-saixuan button{width:56px;height:30px;background:rgba(0,130,153,1);border-radius:4px; border:none; color:#fff; margin-left:15px;}
.ht-spxz-b table{ margin-left:30px;}
.ht-spxz-b table tr th{ background:#FAFAFA;padding:0 5px; font-size:14px; font-weight:normal; text-align:left;line-height:30px; border-left:none; border-right:none}
.ht-spxz-b table tr td{ border-left:none; padding:0 5px;line-height:30px;font-size:13px; border-right:none}
.ht-spxz-b table tr td .td-mc{ width:180px; height:30px; overflow:hidden; display: -webkit-box;text-overflow: ellipsis; -webkit-line-clamp: 1;  /*要显示的行数*/ -webkit-box-orient: vertical; }
.ht-spxz-b table tr td span{ color:#36F;}
.th-szk{ margin:20px 30px; height:30px; overflow:hidden;}
.th-szk .pl-jr{ width:124px; color:#fff; border:none; font-size:14px;height:30px;background:rgba(0,130,153,1);border-radius:4px;}
.ht-page{ float:right;}
.ht-page ul li{ width:28px; list-style:none; font-size:14px; color:#999; height:28px; line-height:28px; border:1px solid rgba(0,0,0,0.15); text-align:center; float:left; margin-left:10px; border-radius:5px; cursor:pointer;}
.ht-page ul .ht-li-hover{ color:#fff; background-color:#008299FF}
.dizhi-xz-ht{ padding:20px 50px;}
.dizhi-xz-ht p{ height:40px; line-height:35px; margin-top:10px;}
.dizhi-xz-ht p span{ display:inline-block; font-size:14px; width:120px; color:#666666FF; text-align:right;}
.dizhi-xz-ht p span b{ margin-right:3px; color:#FF3000FF;}
.dizhi-xz-ht p select{ width:100px; height:33px; border-radius:5px; padding:0 5px; border:1px solid rgba(196,198,207,1);}
.dizhi-xz-ht p input{ width:310px; height:33px; border-radius:5px; padding:0 5px; border:1px solid rgba(196,198,207,1);}
.dizhi-xz-ht .ht-ts-p{ padding-left:125px; padding-right:50px; font-size:12px; color:#F37121FF; line-height:20px;}
.ht-btni{ text-align:center; cursor:pointer;}
.dizhi-xz-ht .ht-btni span{width:185px;height:48px; color:#fff; text-align:center;line-height:48px;background:linear-gradient(180deg,rgba(0,126,149,1) 0%,rgba(0,104,121,1) 100%);border-radius:4px; border:none; color:#fff; font-size:14px; margin:15px auto;}
.dizhi-xz-ht p .dzxz-ht{ display:inline-block; width:600px; text-align:left}
.dzxz-ht div{ display:inline-block;}
.controls{margin-left: 0px;}
#zw_f_purchase_text{width: 500px; height: 100px;}
#selectPcCtrol{ margin-bottom:5px}
.form-horizontal .controls{ margin-left:30px}
.form-horizontal .control-group:before, .form-horizontal .control-group:after{width:0;padding:0;margin:0;}
.houtai-560 input[type="radio"]{margin:0;padding:0;width:15px;height:15px; margin-right:5px}
.houtai-560 input[type="checkbox"]{margin:0;padding:0;width:15px;height:15px; margin-right:5px;margin-left:10px;}

</style>

 <#-- 布局 -->
 <#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form id="1122334" class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>



<#-- 字段：纯展示 -->
<#macro m_zapmacro_common_field_span e_field>
    <#if (e_field.getFieldName()=="basic_order_skus")>
    <div class="control-group" id="selectPcCtrol">
       <div class="controls">
			<div>
			    <input id="zw_f_basic_order_skus" type="hidden" value="" name="zw_f_basic_order_skus">
				<input id="zw_f_sku_code" type="hidden" value="" name="zw_f_sku_code">
				<input id="zw_f_sku_code_show_text" type="hidden" value="">
				<script type="text/javascript">
					zapjs.f.require(['cfamily/js/purchaseOrderAdd_chooseProduct'],function(a){chooseProduct.init({"text":"","source":"page_chart_cfamily_v_cf_pc_skuinfo_status","id":"zw_f_sku_code","value":"","seller_code":"SI2003","max":"500"});});
				</script>
			</div>
		</div>
	</div>
	<div class="houtai-560">
	    <div class="ht-table" id="ht_table2">
	     <!--订单商品-->
       <table width="100%" border="1" id="productTable">

        </table>
        <div class="ht-zj">
            <div class="ht-zj-l"><span>删除选中的商品</span></div>
            <div class="ht-zj-r"><span>已选商品 <b class="ht-chgeng choose_num"></b> 件</span> <span class="ht-ml10">已应付款：<b class="ht-chgeng choose_money"></b> </span></div>
        </div>
    </div>
    
    
     <div class="ht-dizhi" id="ht-dizhi">
     </div>
     
     
     <div class="ht-dizhi">
      <div class="ht-dz-g">采购说明</div>
      <textarea class="ht-sm" id = "zw_f_purchase_text_copy"></textarea>
     </div>
     
    </div>     
     <!--增加地址模块-->
      <div class="ht-spxz" style="display:none;" id="ht-zjdz">
          <div class="ht-spxz-d" style="height:530px; margin-top:-275px;  width: 850px;">
               <p class="ht-spx-t">添加收货地址  <span id="gb_tc_ht"><img src="../resources/cfamily/images/ht-gb.png" /></span></p>  
           
           <div class="dizhi-xz-ht">
           <input  id="addressId" style="opacity:0"/>
               <p><span><b>*</b>收货人：</span><input type="text" placeholder="输入收货人姓名" id="receiverId"/></p>
               <p>
                  <span><b>*</b>所在地：</span>
                    <span class="dzxz-ht" data-toggle="distpicker" >
                       <select id='_province' name='_province'></select>&nbsp;
			           <select id='_city' name='_city'></select>&nbsp;
				       <select id='_area' name='_area'></select>&nbsp;
				       <select id='_street' name='_street'></select>
                   </span>
                </p>
               <script type="text/javascript" src="../resources/address/staf_address.js"></script>
               <script type="text/javascript" src="../resources/address/purchaseOrderAddress.js"></script>
               <p><span><b>*</b>详细地址：</span><input id="detailId" type="text" placeholder="输入详细地址" /></p>
               <p><span>邮编：</span><input id="postId" type="text" placeholder="输入邮编" /></p>
               <p><span><b>*</b>联系电话：</span><input  id="phoneId" type="text" placeholder="输入联系电话" /></p>
               <!--<p><span><b>*</b>身份证号：</span><input id="idenId" type="text" placeholder="输入身份证号" /></p>-->
               <!--<p class="ht-ts-p">提示：购买海外商品，海关需验收收货人身份证信息，请填写与收货人姓名一致的身份证号！ 否则将下单失败！</p>-->
               <p class="ht-btni"><span onclick = "purchaseOrderAdd.saveAddress()">保存收货地址</span></p>
           </div>
          </div>
      </div>



<script> 

$(function () {
$(".zab_page_default_header_title").text("采购商品—下单"); 
$("#zw_f_adress_id").parent().parent().hide();
$("#zw_f_purchase_order_id").parent().parent().hide();
$("#zw_f_purchase_text").parent().parent().hide();

});
   //关闭弹窗
$(document).on("click","#gb_tc_ht",function(){
     $("body").css("height","auto");
    $("body").css("overflow","auto"); 
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
    
	$("#xz-sp-ht").hide();
	$("#ht-zjdz").hide();
});


$(document).on("click",".ht-jian",function(){
   var shu = Number($(this).siblings("input").val());
	if(shu < 2){
		shu = 1;
	}else{
		shu = shu-1;
			$(this).siblings("input").val(shu);
	var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
    		subItem[3]=shu;
    		if(subItem[4]==1){
    		   var temNum = Number($('.choose_num').text())-1;
    		   var temIndex =  $('.choose_money').text().indexOf("¥");
    		   var sumMy = $('.choose_money').text().substr(temIndex+1);
    		   var temMoney = Number(sumMy)-Number(subItem[2]);
    		   temMoney = temMoney.toFixed(2);
    		   $('.choose_num').text(temNum);
		       $('.choose_money').text("¥"+temMoney);
    		}
    		$(this).parent().parent().find(".ht-chgeng").text("¥"+(Number(shu)*Number(subItem[2])).toFixed(2));
    	  }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
	}
	
});
 $(document).on("click",".ht-jia",function(){
    var shu = Number($(this).siblings("input").val());
    shu = shu+1;
	$(this).siblings("input").val(shu)
	var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
    		subItem[3]=shu;
    		if(subItem[4]==1){
    		   var temNum = Number($('.choose_num').text())+1;
    		   var temIndex =  $('.choose_money').text().indexOf("¥");
    		   var sumMy = $('.choose_money').text().substr(temIndex+1);
    		   var temMoney = Number(subItem[2])+Number(sumMy);
    		   temMoney = temMoney.toFixed(2);
    		   $('.choose_num').text(temNum);
		       $('.choose_money').text("¥"+temMoney);
    		}
    		$(this).parent().parent().find(".ht-chgeng").text("¥"+(Number(shu)*Number(subItem[2])).toFixed(2));
    	  }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
});

 
 //sku数量修改监听
  $(document).on("keyup",".sku_num_class",function(){
    var skuNum = $(this).val();
    var r = /^\+?[1-9][0-9]*$/;
    if(!r.test(skuNum)||skuNum==""||skuNum==null){
    alert("请输入大于0的整数！");
    return;
    }else{
    var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
        
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
    	    var sourceNum = subItem[3];
    		subItem[3]=skuNum;
    		if(subItem[4]==1){
    		   var temNum = Number($('.choose_num').text())-Number(sourceNum)+Number(skuNum);
    		   var temIndex =  $('.choose_money').text().indexOf("¥");
    		   var sumMy = $('.choose_money').text().substr(temIndex+1);
    		   var temMoney = Number(sumMy)-Number(sourceNum)*Number(subItem[2])+Number(skuNum)*Number(subItem[2]);
    		   temMoney = temMoney.toFixed(2);
    		   $('.choose_num').text(temNum);
		       $('.choose_money').text("¥"+temMoney);
    		}
    		 $(this).parent().parent().find(".ht-chgeng").text("¥"+(Number(skuNum)*Number(subItem[2])).toFixed(2));
    	  }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
         
    }
});
  //单选
  $(document).on("click","#ht_table2 input:checkbox",function(){
     if($(this).is(":checked")){
        $(this).prop("checked",true);
        flag = true;
     }else{
        $(this).prop("checked",false);
        flag=false;
     }
     var skuId = $(this).val();
     
    var temInitOrderSkusInfo = [];   
	var temStr = "";
	var temNum = 0;
	var temMoney = 0;
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuId){
    	   if(flag){
    	     temNum = Number($('.choose_num').text())+Number(subItem[3]);
    	     temMoney = Number($('.choose_money').text().substring(1))+Number(subItem[2])*Number(subItem[3]);
    	     subItem[4]=1;
    	   } else{
    	     temNum = Number($('.choose_num').text())-Number(subItem[3]);
    	     temMoney = Number($('.choose_money').text().substring(1))-Number(subItem[2])*Number(subItem[3]);
    	     subItem[4]=0;
    	   }
    	 }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
        temMoney = temMoney.toFixed(2);
        $('.choose_num').text(temNum);
		$('.choose_money').text("¥"+temMoney);
        purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;   
 
  });
 
 
//全选
   $(document).on("click","#ht_table2_qx",function(){
     var $ht_table2 = $("#ht_table2 input");
     var flag = true;
     if($("#ht_table2_qx").is(":checked")){
        $("#ht_table2_qx").prop("checked",true);
        flag = true;
     }else{
        $("#ht_table2_qx").prop("checked",false);
        flag=false;
     }
	 var selectCodes=[];
     $ht_table2.each(function(){
        $(this).prop("checked",flag);
	    if($(this).is(':checked')){
	    selectCodes.push($(this).val());
	    }
      });
      
    var temInitOrderSkusInfo = [];   
	var temStr = "";
	var temNum = 0;
	var temMoney = 0;
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if($.inArray(subItem[0],selectCodes)!=-1){
           temNum=Number(temNum)+Number(subItem[3]);
           temMoney=Number(temMoney)+Number(subItem[3])*Number(subItem[2]);
           subItem[4]=1;
    	 }else{
    	   subItem[4]=0;
    	 }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
        temMoney = temMoney.toFixed(2);
        $('.choose_num').text(temNum);
		$('.choose_money').text("¥"+temMoney);
        purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;

 });
 
 
 //删除商品
  $(document).on("click",".ht-lan",function(){
    var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	var skuIds = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
    		   var temNum = Number($('.choose_num').text())-Number(subItem[3]);
    		   var temMoney = Number($('.choose_money').text().substr(1))-Number(subItem[2])*Number(subItem[3]);
    		   temMoney = temMoney.toFixed(2);
    		   $('.choose_num').text(temNum);
    		   temMoney = temMoney.toFixed(2);
		       $('.choose_money').text("¥"+temMoney);
    	}else{
    		if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	skuIds =subItem[0]; 
        	}else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            skuIds = skuIds+","+subItem[0];
            }
    	}  
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
         $("#zw_f_sku_code").val(skuIds);
         
         $(this).parent().parent().remove();
    
 });
 
 //批量删除
 $(document).on("click",".ht-zj-l",function(){
     var $ht_table2 = $("#ht_table2 input");
	 var selectCodes=[];
     $ht_table2.each(function(){
	    if($(this).is(':checked')){
	    selectCodes.push($(this).val());
	    $(this).parent().parent().remove();
	    }
      });
    var temInitOrderSkusInfo = [];
	var temStr = "";
	var skuIds = "";
	var temNum = Number($('.choose_num').text());
	var temMoney = Number($('.choose_money').text().substr(1));
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
		
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if($.inArray(subItem[0],selectCodes)!=-1){
    		 temNum = temNum-Number(subItem[3]);
    		 temMoney = temMoney-Number(subItem[2])*Number(subItem[3]);
    		  
    	}else{
    		if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	skuIds =subItem[0]; 
        	}else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            skuIds = skuIds+","+subItem[0];
            }
    	}  
        }	
         temMoney = temMoney.toFixed(2);
         $('.choose_num').text(temNum);
		 $('.choose_money').text("¥"+temMoney);
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
         $("#zw_f_sku_code").val(skuIds);  
 });
 
 //sku成本监听
  $(document).on("keyup",".sku_cost_class",function(){
    var skuCost = $(this).val();
    if(skuCost==""||skuCost==null||isNaN(skuCost)){
    alert("请输入大于0的数值！");
    return;
    }else{
    var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");
		}
    for(var i in temInitOrderSkusInfo){ 
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
               subItem[1] = skuCost;
    	  }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
         
    }
});
 
 //售价监听
  $(document).on("keyup",".sku_sell_class",function(){
    var skuSell = $(this).val();
    if(skuSell==""||skuSell==null||isNaN(skuSell)){
    alert("请输入大于0的数值！");
    return;
    }else{
    var skuCode = $(this).parent().parent().find("input[type=checkbox]").val();
	var temInitOrderSkusInfo = [];
	var temStr = "";
	if (null != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus && "" != purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus) {
			temInitOrderSkusInfo = purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus.split(",");

		}
    for(var i in temInitOrderSkusInfo){
    	var subItem = temInitOrderSkusInfo[i].split("_");
    	if(subItem[0]==skuCode){
    		if(subItem[4]==1){
    		   var temMoney = Number($('.choose_money').text().substr(1))-Number(subItem[2])*Number(subItem[3])+Number(skuSell)*Number(subItem[3]);
		       subItem[2] = skuSell;
		       temMoney = temMoney.toFixed(2);
		       $('.choose_money').text("¥"+temMoney);
    		}
    		 $(this).parent().parent().find(".ht-chgeng").text("¥"+(Number(skuSell)*Number(subItem[3])).toFixed(2));
    	  }
	     if(temStr==""){
        	temStr = subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
        	}
        else{
            temStr = temStr+","+ subItem[0]+"_"+subItem[1]+"_"+subItem[2]+"_"+subItem[3]+"_"+subItem[4];
            }
        }	
         purchaseOrderAdd.temp.purchaseBaseInfoJson.basic_order_skus = temStr;
    }
});
 
 
 
  //删除地址
 //  $(document).on("click",".sc-dz",function(){
   // $(this).parent().parent().remove();
 //});


//添加地址弹窗
$(document).on("click","#zjdz-btn",function(){
    $("body").css("height","100%");
    $("body").css("overflow","hidden"); 
    $("#ht-zjdz").show();
});
//显示更多地址
$(document).on("click",".gd-ht-dz",function(){
  $(".dizhi-zk").toggle();
});
//地址选择
$(document).on("click",".dizhi",function(){
 var selectdeId=  $(this).val();
 purchaseOrderAdd.temp.selectAddressId = selectdeId;
 purchaseOrderAdd.addressInfo_init();
 
});



</script>
		
	<#else>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		${e_field.getPageFieldValue()?default("")}
	<@m_zapmacro_common_field_end />
    </#if>
</#macro>


