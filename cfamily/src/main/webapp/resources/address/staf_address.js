

var startV='首件数(个)';
var appendV='续件数(个)';
var startUnit='件';

var prov="";
var city="";
var area="";
var street="";
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
	 	

//加载城市
/*function LoadCity(Pvalue,result){
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
	$("#_area").empty();
    $.each(result.list[Pvalue].cityList[Cvalue].districtList,function(){
        if($(this).attr("district")==area)
            $("#_area").append("<option  value="+$(this).attr("districtID")+" selected>"+$(this).attr("district")+"</option>");
        else
        $("#_area").append("<option value="+$(this).attr("districtID")+">"+$(this).attr("district")+"</option>");
    });
}            

function loadStreet(Pvalue, Cvalue, Avalue, result) {
	if(isNaN(Pvalue)){
    	Pvalue = 0;
    }
    if(isNaN(Cvalue)){
    	Cvalue = 0;
    }
    if(isNaN(Avalue)) {
    	Avalue = 0;
    }
    $("#_street").empty();
    $.each(result.list[Pvalue].cityList[Cvalue].districtList[Avalue].streetList,function(){
        if($(this).attr("street")==street)
            $("#_street").append("<option  value="+$(this).attr("streetId")+" selected>"+$(this).attr("street")+"</option>");
        else
        $("#_street").append("<option value="+$(this).attr("streetId")+">"+$(this).attr("street")+"</option>");
    });
}*/

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

function getAreaData(pCode) {
	var content = {};
	if(pCode && pCode != null && pCode != '') {
		content.parentCode = pCode;
	}
	
	var items = [];
	zapjs.zw.api_async_call("com_cmall_familyhas_api_ApiForGetStoreDistrictNew", content, function(resultV) {
		items = resultV.areaList;
	});
	return items;
}

function loadProvince() {
	var optionChars = '<option value="">请选择</option>';
	var provinceItems = getAreaData();
	$.each(provinceItems, function(index, item) {
		optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
	});
	$('#_province').html(optionChars);
}

function loadCity(pCode) {
	var optionChars = '<option value="">请选择</option>';
	var cityItems = getAreaData(pCode);
	$.each(cityItems, function(index, item) {
		optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
	});
	$('#_city').append(optionChars);
}

function loadArea(pCode) {
	var optionChars = '<option value="">请选择</option>';
	var areaItems = getAreaData(pCode);
	$.each(areaItems, function(index, item) {
		optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
	});
	$('#_area').append(optionChars);
}

function loadStreet(pCode) {
	var optionChars = '<option value="">请选择</option>';
	var streetItems = getAreaData(pCode);
	$.each(streetItems, function(index, item) {
		optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
	});
	$('#_street').append(optionChars);
}

//初始化wz
$(function(){
	loadProvince();
	
	$("#_province").change(function() {
		$("#_city").empty();
		$("#_area").empty();
		$("#_street").empty();
		
		var provinceCode = $(this).val();
		loadCity(provinceCode);
		
		var provinceName = $(this).find('option:selected').text();
		if(provinceName == '北京市' || provinceName == '天津市' || provinceName == '上海市' || provinceName == '重庆市') {
			$("#_city").hide();
			$('#_city').find('option:eq(1)').attr('selected','selected');
			$('#_city').change();
		}else {
			$("#_city").show();
		}
	});
	
	$('#_city').change(function() {
		$("#_area").empty();
		$("#_street").empty();
		
		var cityCode = $(this).val();
		loadArea(cityCode);
	});
	
	$('#_area').change(function() {
		$("#_street").empty();
		
		var areaCode = $(this).val();
		loadStreet(areaCode);
	});
	
	 /*var provinceModel = function(code,name){
	 	this.code = code;
	 	this.name = name;
	 	return this;
	 }*/
	 
	/* $.getJSON("../../resources/cshop/json/address.json",function(resultV){ */
 	 /*zapjs.zw.api_call("com_cmall_familyhas_api_ApiForGetStoreDistrict",{},function(resultV) {
		 console.log(resultV.list);
	 	$("#province_city_area").append("<select id='_province' name='_province'></select>&nbsp;<select id='_city' name='_city'></select><select id='_area' name='_area'></select><select id='_street' name='_street'></select>");
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
	   loadStreet($("#_province").selectedIndex,$("#_city").selectedIndex,$("#_area").selectedIndex,resultV);
            
        $("#_province").change(function(){
            $("#_city").empty();
            
            var provinceName = $(this).find('option:selected').text();
            if($("#_province").children.length==0){
                $("#_city").hide();
                $("#_area").remove();
                return;
            }else if(provinceName == '北京市' || provinceName == '天津市' || provinceName == '上海市' || provinceName == '重庆市') {
            	$("#_city").hide();
            	LoadCity($("#_province")[0].selectedIndex,resultV);
				LoadArea($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,resultV); //初始区
				loadStreet($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,$("#_area")[0].selectedIndex,resultV);
            }else{
                $("#_city").show();
            	LoadCity($("#_province")[0].selectedIndex,resultV);
            	LoadArea($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,resultV); //初始区
            	loadStreet($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,$("#_area")[0].selectedIndex,resultV);
            }
        });
        
        $("#_city").change(function(){
             LoadArea($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,resultV); //初始区
             loadStreet($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,$("#_area")[0].selectedIndex,resultV);
        });
	 	
        $('#_area').change(function() {
        	loadStreet($("#_province")[0].selectedIndex,$("#_city")[0].selectedIndex,$("#_area")[0].selectedIndex,resultV);
        });
	 })*/	
});
