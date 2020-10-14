<style>

	body, button, input, select, textarea {
		font: 12px/1.5 tahoma,arial,\5b8b\4f53,sans-serif;
	}
	.table th{
		padding: 8px;
		line-height: 20px;
		text-align: center;
		vertical-align:middle;
		border-top: 1px solid #ddd;
	}
	.table td {
		text-align: center;
		vertical-align:middle;
	}
	ul li
	{
		list-style-type:none;
		margin:0em 1em 1em 0;
	}
	label {
		display: inline;
	}
	
	input {
		vertical-align: middle;
	}
	
	.J_Section{  
		margin:1em 2em .5em 5em;
	}
	
	.savebutton{
		margin:2em 1em .5em 26em;
	}
	
	
	.postageDetail{
		margin: 1em 0;
		padding: 5px;
		border: 1px solid #b2d1ff;
	}
	.postageDetail input{
		width: 6em;
		vertical-align: middle;
		margin: 1em 0;
	}
	
	.detailFirstContent{
		line-height: 32px;
		background-color: #f3feed;
	}
	
	.detailSecondContent{
		margin: 1em 1em 0 0;
	}
	
	.editRight{
		float: right;
	}
	
	.area-group {
		padding-right: 3em;
		display: block;
	}
	.cellGroup{
		margin: 0;
		padding: 0;
		text-align: left;
	}
	.ecity {
		position: relative;
		float: left;
		margin-right: 1px;
		padding-right: 8px;
		height: 30px;
		width: 80px;
	}
	.diglogTitle .ui-widget-header,.diglogTitle .even{
		background: #e9f1f4;
	}
	
	.pli{
		margin: .5em 1em .5em 1em;
	}
</style>
<script>

var startV='首件数(个)';
var appendV='续件数(个)';
var startUnit='件';

var prov="";
var city="";
var area="";
//省份信息
var provinceInfos=[];	

var sourceTarget;
function cshop_freighttpl_ok(event) {
	var result = "",resultCodes="";
	//赋值
	$("li.pli span :checked").each(function(){
		result += $(this).next().text() +",";
		
		resultCodes += $(this).val() +",";
	})
	//去掉最后的 逗号
	result = result.substring(0,result.lastIndexOf(","));
	resultCodes = resultCodes.substring(0,resultCodes.lastIndexOf(","));
	$(sourceTarget).next("div").html(result);
	//设置隐藏域的数值
	$(sourceTarget).next("div").next().attr("value",resultCodes); 
	//$( this ).dialog( "close" );
	zapjs.f.window_close();
}


//初始化弹出框
var initDialogProvince = function (event,ui){
	$('#cshop_freighttpl_box').find("input[type='checkbox']").attr("disabled",false).prop("checked",false);
	var hiBaby = disabledAndSelectedValues(sourceTarget).split(";");
	var disableVs = hiBaby[0].split(",");
	var selectVs =  hiBaby[1].split(",");
	
	for(var i=0;i<selectVs.length;i++)	 		
	{
		$('#cshop_freighttpl_box').find("input[value='"+selectVs[i]+"']").attr("disabled",false).prop("checked",true);
	}
	
	for(var i=0;i<disableVs.length;i++)	 		
	{
		$('#cshop_freighttpl_box').find("input[value='"+disableVs[i]+"']").prop("checked",false).attr("disabled",true);
	}

}

//找出不可用的checkbox  
   var disabledAndSelectedValues = function(targetObj){
   		var resultBaby="";
   		//遍历表格所以的tr，找出出自身外的，所以已经选择了的城市。
   		var parentsTr = $(targetObj).parents("tr:first").siblings();
   		$.each(parentsTr,function(n,curretnObj){
   			resultBaby += $(curretnObj).find("input[type=hidden]").val()+",";
   		});
   		//已选择的 和 当前选择的 用; 隔开
   		resultBaby = resultBaby.substring(0,resultBaby.lastIndexOf(","))+";";
   		parentsTr = $(targetObj).parents("tr:first");
   		$.each(parentsTr,function(n,curretnObj){
   			resultBaby += $(curretnObj).find("input[type=hidden]").val()+",";
   		});
   		resultBaby = resultBaby.substring(0,resultBaby.lastIndexOf(","));
   		return resultBaby;
   }
   
   
   
//省运费表格
	 function provinceTable(event) {
	 		var parentDiv = $("div.J_Section"); 
	 		//指定城市 添加事件
	    	var appendTr = "";
	    	if(parentDiv.children("div").children("div .thirdContent").children().size()>0){
	    		appendTr +='		<tr>';
				appendTr +='	      		<td>';
				appendTr +='	      		<div class="cellGroup">';
				appendTr +='					<a href="#" class="editRight">编辑</a>';
				appendTr +='					<div class="area-group" id="express_areas">';
				appendTr +='	      					未知区域';
				appendTr +='					</div>';
				appendTr +='					<input type="hidden" name="express_areas_n1" id="express_areas_n1"/>';
				appendTr +='	      		</div>';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<span><select id="isEnable"><option value="1">是</option><option value="0">否</option></select></span>';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<input type="text" maxlength="6" name="start_n1"  id="start_n1" />';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<input type="text" maxlength="6" name="start_money_n1" id="start_money_n1" />';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<input type="text" maxlength="6" name="append_n1" id="append_n1"/>';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<input type="text" maxlength="6" name="append_meoney_n1" id="append_meoney_n1"/>';
				appendTr +='	      		</td>';
				appendTr +='	      		<td>';
				appendTr +='	      			<a href="#" class="deleteCenter">删除</a>';
				appendTr +='	      		</td>';
				appendTr +='	      		';
				appendTr +='	      	</tr>';
	    		$(this).parent().prev().children("table").children("tbody").append(appendTr);
	    	}else{
	    		var thirdContent = "";
		    	thirdContent +='<table class="table  table-condensed table-striped table-bordered table-hover">';
				thirdContent +='	<thead>';
				thirdContent +='	    <tr>';
				thirdContent +='	      	 <th width="30%">';
				thirdContent +='	      	 	运送到';
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 <th width="10%">';
				thirdContent +='	      	 	是否可售';
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 <th width="15%">';
				thirdContent +='	      	 	'+startV;
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 <th width="15%">';
				thirdContent +='	      	 	首费(元)';
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 ';
				thirdContent +='	      	 <th width="10%">';
				thirdContent +='	      	 	'+appendV;
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 <th width="10%">';
				thirdContent +='	      	 	续费';
				thirdContent +='	      	 </th>';
				thirdContent +='	      	 <th width="10%">';
				thirdContent +='	      	 	操作';
				thirdContent +='	      	 </th>';
				thirdContent +='	    </tr>';
				thirdContent +='  	</thead>';
				thirdContent +='  ';
				thirdContent +='	<tbody>';
				thirdContent +='		<tr>';
				thirdContent +='	      		<td>';
				thirdContent +='	      		<div class="cellGroup">';
				thirdContent +='					<a href="javascript:void(0);" class="editRight">编辑</a>';
				thirdContent +='					<div class="area-group" id="express_areas">';
				thirdContent +='	      					未知区域';
				thirdContent +='					</div>';
				thirdContent +='					<input type="hidden" name="express_areas_n1" id="express_areas_n1" />';
				thirdContent +='	      		</div>';
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<span><select id="isEnable"><option value="1">是</option><option value="0">否</option></select></span>'; 
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<input type="text" maxlength="6" name="start_n1"  id="start_n1"/>';
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<input type="text" maxlength="6" name="start_money_n1" id="start_money_n1"/>';
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<input type="text" maxlength="6" name="append_n1" id="append_n1" />';
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<input type="text" maxlength="6" name="append_meoney_n1" id="append_meoney_n1" />';
				thirdContent +='	      		</td>';
				thirdContent +='	      		<td>';
				thirdContent +='	      			<a href="javascript:void(0);" class="deleteCenter">删除</a>';
				thirdContent +='	      		</td>';
				thirdContent +='	      		';
				thirdContent +='	      	</tr>';
				thirdContent +='			';
				thirdContent +='	</tbody>';
				thirdContent +='</table>';
				parentDiv.children("div").children("div .thirdContent").append(thirdContent);
	    	}
	    	
	   		//编辑方法
			$(".editRight").click(sayHello);
			
			 $("input[type='text']").attr("zapweb_attr_regex_id","469923180002");
			//删除
			$(".deleteCenter").click(function(){
				$(this).parents("tr").remove(); 
			});
			
			 //验证数值
			onlyNumberValidate(parentDiv);
  					
	 	}   
	 	
	//点击的编辑按扭
	function sayHello(event){
	 		
	 		sourceTarget=event.target;
		 	//省份是否添加
		 	if($( "#dialogProvince" ).children().size()==0){
				var dialogContent ="<div id='cshop_freighttpl_box'>";
				dialogContent +="<ul>";
				$.each(provinceInfos,function(n,model){
					
					var code = model.code;
					var name = model.name;
					if(n%5==0){  //换行
						if(n%2==1){ //加样式
							dialogContent +='<li class="pli even" >';
						}else{
							dialogContent +='<li class="pli">';
						}
						dialogContent +='<div style="width:420px;">';
					}
					
					dialogContent +='	<div style="width:80px;height:30px;display: inline-block;"><span ><input type="checkbox" id="Province'+code+'" value="'+code+'" ';
					
					dialogContent +='/><label for="Province'+code+'" >'+name+'</label></span></div>';
					
					if(n%5==4){
						dialogContent +='</div>';
						dialogContent +='</li>';
						}
					})
					
					dialogContent +="</ul>";
					dialogContent+="</div>"
					//$( "#dialogProvince" ).append(dialogContent);
				}	
			content:dialogContent+='<div style="margin: 2em 0em 0em 26em"><input type="button" onclick="cshop_freighttpl_ok(this)" class="btn btn-success" style="float:center" value="确定"/><input type="button" onclick="zapjs.f.window_close();" class="btn btn-success" style="float:center" value="取消"/></div>';
			zapjs.f.window_box({content:dialogContent});
			initDialogProvince();
			
	 	}
	 	
//只能数值
var onlyNumberValidate = function(detailDiv){
	$(detailDiv).find(":text").keypress(function(event){
		var getValue = $(this).val();
        //控制第一个不能输入小数点"."
        if (getValue.length == 0 && event.which == 46) {
            event.preventDefault();
            return;
        }
        //控制只能输入一个小数点"."
        if (getValue.indexOf('.') != -1 && event.which == 46) {
            event.preventDefault();
            return;
        }
        //控制只能输入的值
        if (event.which && (event.which < 48 || event.which > 57) && event.which != 8 && event.which != 46) {
            event.preventDefault();
            return;
        }     
	})
}
//加载城市
function LoadCity(Pvalue,result){
	
    $("#_city").empty();
    if(isNaN(Pvalue)){
    	Pvalue = 0;
    }
    $.each(result.list[Pvalue].cityList,function(){
       if($(this).attr("cityName")==city)
            $("#_city").append("<option value="+$(this).attr("cityID")+" selected>"+$(this).attr("cityName")+"</option>");
  	   else
    		$("#_city").append("<option value="+$(this).attr("cityID")+">"+$(this).attr("cityName")+"</option>");
    });
} 
//加载区域    
function LoadArea(Pvalue,Cvalue,result){
	if(isNaN(Pvalue)){
    	Pvalue = 0;
    }
    if(isNaN(Cvalue)){
    	Cvalue = 0;
    }

    $("#_area").remove();
    $("#_city").after("&nbsp;<select id=_area></select>");
    $.each(result.list[Pvalue].cityList[Cvalue].districtList,function(){
        if($(this).attr("district")==area)
            $("#_area").append("<option  value="+$(this).attr("districtID")+" selected>"+$(this).attr("district")+"</option>");
        else
        $("#_area").append("<option value="+$(this).attr("districtID")+">"+$(this).attr("district")+"</option>");
    });
}            
//付费方式修改
function tplChange(){
	var detailDiv = $(this).parents("div.J_Section"); 
	//选中的时候 没有的时候添加子节点，未选中 有的时候隐藏，选中有的时候 显示
	if(this.checked){
		if(detailDiv.children("div").size()==0){
		
	    	var firstContent= "<div class='postageDetail'>";
		    firstContent+='<div class="detailFirstContent">';
			firstContent+='	默认运费：<input type="text" name="express_start" id="express_start" value="1" maxlength="6" aria-label="默认运费kg数"> '+startUnit+'内，';
			firstContent+='	<input type="text" data-field="postage" name="express_postage" id="express_postage" value="1"  autocomplete="off" maxlength="6" aria-label="默认运费价格"> 元，';
			firstContent+='	每增加 <input type="text" name="express_plus"  id="express_plus" data-field="plus" value="1" class="input-text " autocomplete="off" maxlength="6" aria-label="每加kg"> '+startUnit+'，';
			firstContent+='	增加运费 <input type="text" name="express_postageplus" id="express_postageplus"  value="1" autocomplete="off" maxlength="6" aria-label="加kg运费">元';
			firstContent+=	'<div class="J_DefaultMessage"></div>';
			firstContent+='	</div>';
			firstContent+='</div>';
	    	detailDiv.append(firstContent);
	    	
	    	var secondContent='<div class="detailSecondContent">';
			secondContent+='	<a href="javascript:"javascript:void(0);" class="JAddRule">为指定地区城市设置运费</a>';
			secondContent+='</div>';
			$(this).parent().next().append('<div class="thirdContent"></div>');
			$(this).parent().next().append(secondContent); 
			
			//省份表格
			$(".JAddRule").click(provinceTable);
			 $("input[type='text']").attr("zapweb_attr_regex_id","469923180002");
		    //验证数值
			onlyNumberValidate(detailDiv);
		}else{
			detailDiv.children("div").show();
		}
	}else{
		detailDiv.children("div").hide();
	}
	
}

//快递 方式及其 区域价格
	function expressGenerate(expressObj,expressType){
		var exp=[];
		var tmp={};
		var expDetail = $(expressObj).parent().next();
		//默认运费 全国 
		var sequence=1;
		tmp.area="全国",
		tmp.areaCode="global";
		tmp.tplTypeId=expressType;
		//开始值
		tmp.expressStart = $(expDetail).find("#express_start").val();
		//开始价格
		tmp.expressPostage = $(expDetail).find("#express_postage").val();
		//增加值
		tmp.expressPlus = $(expDetail).find("#express_plus").val();
		//增加价格
		tmp.expressPostageplus = $(expDetail).find("#express_postageplus").val();		
		tmp.sequence=1;
		tmp.isEnable="1";
		exp.push(tmp);
		//遍历单元格
		var tmp;
		//不算标题行
		$(expDetail).find("table tr:gt(0)").each(function(){
			tmp={}
			tmp.area = $(this).find("#express_areas").html();
			tmp.tplTypeId=expressType;
			tmp.areaCode = $(this).find("#express_areas_n1").val();
			tmp.expressStart = $(this).find("#start_n1").val();
			tmp.expressPostage = $(this).find("#start_money_n1").val();
			tmp.expressPlus = $(this).find("#append_n1").val();
			tmp.expressPostageplus = $(this).find("#append_meoney_n1").val();
			sequence +=1;
			tmp.sequence=sequence;
			tmp.isEnable=$(this).find("#isEnable").val();
			exp.push(tmp);
		})
		
		return exp;
	}

//提交方法
function submitForm(){

	 //包邮
    //var isFree = $("input[name='is_free']:checked").val();
    var isFree = "449746250002";
	//计价方式
	var valuationType = $("input[name='valuation_type']:checked").val();
	
	var formValue={
		storeId:$('#store_id').val(),
		tplName:$('#tpl_name').val(),
		province:$('#_province').val(),
		city:$('#_city').val(),
		area:$('#_area').val(),
		isDisable:$("input[name='isDisable']:checked").val(),
		consignmentTime:$('#consignment_time').val(),
		isFree:isFree,
		valuationType:valuationType
	};
	
	$(".expressArea").find("[name='tplType']:checked").each(function(){
		formValue[this.id]= expressGenerate($("#"+this.id),$(this).val());
	})
	
	
	var jsonData = zapjs.f.tojson(formValue);
	
	 return jsonData;
}
	
	
function cshop_freightpl_submit(e)
{
	//未知区域 判断
	$(".area-group").each(function(){
		
		if("未知区域"==$.trim($(this).html())){
			$(this).addClass("w_regex_error")
			zapjs.f.message("请设置未知区域");
			return false;
		}else{
			$(this).removeClass("w_regex_error")
		}
	})
	//买家付款，没有选择运费方式 //"449746250002"==$("input[name='is_free']:checked").val()&&  是否包邮去掉了
	 if("449746210002"!=$("input[name='tplType']:checked").val()){
	 	alert("请选择运送方式");
	 	return;
	 }

	
	var json = submitForm();
	$('#data').val(json);
	zapjs.zw.func_call(e);
	
	
}	 		 	
//初始化
$(function(){
	 var provinceModel = function(code,name){
	 	this.code = code;
	 	this.name = name;
	 	return this;
	 }
	 
	/* $.getJSON("../../resources/cshop/json/address.json",function(resultV){ */
 	 zapjs.zw.api_call("com_cmall_familyhas_api_ApiForGetStoreDistrict",{},function(resultV) {
		 console.log(resultV.list);
	 	$("#province_city_area").append("<select id='_province'></select>&nbsp;<select id='_city'></select><select id='_area'></select>");
	 	 var m;
        $.each(resultV.list,function(){
            if($(this).attr("province")==prov)
                $("#_province").append("<option value="+$(this).attr("provinceID")+" selected>"+$(this).attr("provinceName")+"</option>");
            else
            	$("#_province").append("<option value="+$(this).attr("provinceID")+">"+$(this).attr("provinceName")+"</option>");
       		
       		//初始化ProvinceInfo 信息 
       		 m = new provinceModel($(this).attr("provinceID"),$(this).attr("provinceName"));
       		 provinceInfos.push(m);
       		
        });//初始省
	 		
	 	   LoadCity($("#_province").selectedIndex,resultV); //初始市 
           LoadArea($("#_province").selectedIndex,$("#_city").selectedIndex,resultV); //初始区		
            
            
        $("#_province").change(function(){
            $("#_city").empty();
            
            console.log()
            if($("#_province").children.length==0){
                $("#_city").hide();
                $("#_area").remove();
                return;
            }else{
                $("#_city").show();
            LoadCity($("#_province")[0].selectedIndex,resultV);
            LoadArea($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,resultV); //初始区
            }
        });
        
        $("#_city").change(function(){
             LoadArea($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,resultV); //初始区
        });
	 
	 })	
		
	
		
	
	    //点击买家付款 ，卖家付款
	    $( "#J_buyerBearFre,#J_sellerBearFre" ).click(function() {
	    	if("J_buyerBearFre"==this.id){
	    		alert("您的运费设置将变为未设置状态，请设置运费");
	    		$("input[name='tplType']").bind("change",tplChange);
	    	}else{
	    		alert("选择“卖家承担运费”后，所有区域的运费将设置为0元且原运费设置无法恢复，请保存原有运费设置。");
	    		$("input[name='tplType']").unbind("change");
	    		$("input[name='tplType']").parent().siblings("div").remove();
	    		$("input[name='tplType']").prop("checked",false);
	    	}
			
		});
	    	     //改变计价方式
	    $("#J_CalcRuleNumber,#J_CalcRuleWeight,#J_CalcRuleVolume").click(function(){
	    	var message ="切换计价方式后，所设置当前模板的运输信息将被清空，确定继续么？";
	    	var flag = window.confirm(message);
	    	if(flag){
	    		if("449746290001"==this.value){
	    			startV='首件数(个)';
					appendV='续件数(个)';
					startUnit="件";
	    		}else if("449746290002"==this.value){
	    			startV='首重量(kg)';
					appendV='续重量(kg)';
					startUnit="kg";
	    		}else{
	    			startV='首体积(m³)';
					appendV='续体积(m³)';
					startUnit="m³";
	    		}
	    		$("input[name='tplType']").parent().siblings("div").remove();
	    		$("input[name='tplType']").prop("checked",false);
	    	
	    	}else{
	    		console.log("未改变计价方式");
	    	}
	    })
	    
	     //运送方式
	   $("input[name='tplType']").change(tplChange);
	   
	   
});
	
	
	
	
</script>

<div id="dialogProvince"  title="选择省份"> 
</div>

<form method="post" id="J_TPLForm">
			<input type="hidden" name="data" id="data"/>
    		<!--  店铺id  -->
    		<input type="hidden" name="store_id" id="store_id" value="">
    		<ul>
			<!--  模板名称设置  -->
			<li>
				模板名称：
				<input type="text" name="tpl_name" value="" id="tpl_name" zapweb_attr_regex_id="469923180002">
			</li>
			<!--  卖家地址设置  -->
			<li>
				<span>宝贝地址：</span>
				<span id="province_city_area"></span>
			</li>
			<li>
				<span>发货时间：</span>
				<span>
					<select name="consignment_time" id="consignment_time" zapweb_attr_regex_id="469923180002">
						<option value="">请选择..</option>
						<option value="12">12小时内</option>
						<option value="24">24小时内</option>
						<option value="48">48小时内</option>
						<option value="72">72小时内</option>
						<option value="120">5天内</option>
						<option value="168">7天内</option>
						<option value="360">15天内</option>
						<option value="720">30天内</option>
						<option value="1080">45天内</option>
					</select>
				</span>
    				<span>承诺买家付款后该时间内录入物流信息并发货，以物流公司收单信息为准。</span>
			</li>
			<li>
				<span >是否禁用：</span>
				<span>
				    <input type="radio" name="isDisable" checked="" value="449746250002" id="isDisableN">
					<label for="isDisableN">否</label>
					<input type="radio" name="isDisable" value="449746250001" id="isDisableY">
					<label for="isDisableY">是</label>
				</span>
			</li>
			
			<!--  是否包邮设置  -->
			<!--li>
				<span >是否包邮：</span>
				<span>
					<input type="radio" name="is_free" value="449746250001" id="J_sellerBearFre">
					<label for="J_sellerBearFre">是</label>
					<input type="radio" name="is_free" checked="" value="449746250002" id="J_buyerBearFre">
					<label for="J_buyerBearFre">否</label>
				</span>
			</li-->
			<!--  记价方式设置  -->
				<li>
					<span >计价方式：</span>
					 <span>
    						<input type="radio" name="valuation_type" value="449746290002"  data-type="weight" id="J_CalcRuleWeight">
    						<label for="J_CalcRuleWeight">&nbsp;按重量</label>
    					</span>
    					<span>
    						<input type="radio" name="valuation_type" value="449746290003"  data-type="volume" id="J_CalcRuleVolume">
    						<label for="J_CalcRuleVolume">&nbsp;按体积</label>
    					</span>
    					<span>
						<input type="radio" name="valuation_type" checked="" value="449746290001"  data-type="number" id="J_CalcRuleNumber">
						<label for="J_CalcRuleNumber">&nbsp;按件数</label>
					</span>
				</li>
			<!--  区域限售  -->
						
			<!-- 运费方式设置  -->
			<li class="expressArea">
			<span >运送方式：</span>
			
				<span class="field-note">除指定地区外，其余地区的运费采用“默认运 ”</span>
						<div class="J_Section">
							<p>
								<input type="checkbox" name="tplType" value="449746210002" id="express" class="J_Delivery">
								<label for="express">快递</label>
							</p>
						</div>
						<!--暂时 只支持 快递
						<div  class="J_Section">
							<p>
								<input type="checkbox" name="tplType" value="2" id="ems">
								<label for="ems">EMS</label>
							</p>
						</div>
						<div  class="J_Section">
							<p>
								<input type="checkbox" name="tplType" value="4" id="normal" > 
								<label for="normal">平邮</label>
							</p>
						</div>
						-->
			</li>
			</ul>
			
			<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001016" />
		
</form>
		
		
	
		