<#assign uuid = b_page.getReqMap()["zw_f_uid"] >
<#assign productCode = b_page.getReqMap()["zw_f_product_code"] >
<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<#assign productService = b_method.upClass("com.cmall.productcenter.service.ProductService")>
<#assign correlation = productService.getCorrelationByProduct(productCode)>

<#list 1..3 as i>
	关联分类${i}：
	<span id="categoryName${i}">${correlation.name[i-1]?default("")}</span>
	<input type="hidden" id="categoryCode${i}" value="${correlation.code[i-1]?default("")}"/>
	<input type="button" class="btn btn-small" onclick="operationClick(${i})" value="添加/修改" style="margin-left:10px;">
	<input type="button" class="btn btn-small" onclick="delClick(${i})" value="删除" style="margin-left:10px;">
	<br/>
</#list>
<br/>

<div style="display:none;" id="selectContainer">
	<select style="width:30%;height:175px;" size="8" onchange="level2Change()" id="level2">
		<#assign level2 = sellercategoryService.getCateGoryLevel2()>
		<#list level2 as l>
			<option value=${l.category_code}>${l.category_name}</option>
		</#list>
	</select>
	<select style="width:30%;height:175px;" size="8" onchange="level3Change()" id="level3">
	</select>
	<select style="width:30%;height:175px;" size="8" onchange="level4Change()" id="level4">
	</select>
</div>

是否显示ld品：
<select id="showLd">
	<option value="0" <#if correlation.show_ld?? && correlation.show_ld == "0">selected</#if>>否</option>
	<option value="1" <#if correlation.show_ld?? && correlation.show_ld == "1">selected</#if>>是</option>
</select>
<br/>

<input type="button" class="btn btn-big" value="提交" style="margin-left:50px;" onclick="subClick(${productCode})">
<input type="button" class="btn btn-big" value="取消" style="margin-left:20px;" onclick="cancelClick()">

<script>
$(function(){

});

var idx = 0;

function level2Change(){
	$("#level3").html("");
	$("#level4").html("");
	var pCode = $("#level2").val();
	zapjs.f.ajaxjson("../func/8f91330c425e4a1799d1dec0ce43ca6a",{pCode: pCode},function(data){
		if(data.resultCode == "1"){
			for(var i = 0; i < data.resultObject.length; i++){
				var str = "<option value='" + data.resultObject[i].category_code + "'>" + data.resultObject[i].category_name + "</option>";
				$("#level3").append(str);
			}
		}
	});
}

function level3Change(){
	$("#level4").html("");
	var pCode = $("#level3").val();
	zapjs.f.ajaxjson("../func/8f91330c425e4a1799d1dec0ce43ca6a",{pCode: pCode},function(data){
		if(data.resultCode == "1"){
			for(var i = 0; i < data.resultObject.length; i++){
				var str = "<option value='" + data.resultObject[i].category_code + "'>" + data.resultObject[i].category_name + "</option>";
				$("#level4").append(str);
			}
		}
	});
}

function level4Change(){
	var level4Code = $("#level4").val();
	var level4Name = $("#level4").find("option:selected").text();
	for(var i = 1; i <= 3; i ++){
		if(i == idx){
			continue;
		}
		if($("#categoryCode"+i).val().indexOf(level4Code) >= 0){
			alert("关联分类不允许重复！");
			$("#level4").find("option:selected").attr("selected", false);
			return;
		}
	}
	var level3Code = $("#level3").val();
	var level3Name = $("#level3").find("option:selected").text();
	var level2Code = $("#level2").val();
	var level2Name = $("#level2").find("option:selected").text();
	$("#categoryName"+idx).text(level2Name + '>' + level3Name + '>' + level4Name);
	$("#categoryCode"+idx).val(level2Code + '>' + level3Code + '>' + level4Code);
}

function operationClick(id){
	idx = id;
	clearSelect();
	var categoryCodes = $("#categoryCode"+id).val();
	if(categoryCodes && categoryCodes!=undefined && categoryCodes!=''){
		var CategoryCodeArr = categoryCodes.split('>');
		//回显二级
		$("#level2").val(CategoryCodeArr[0]);
		
		//加载并回显三级
		zapjs.f.ajaxjson("../func/8f91330c425e4a1799d1dec0ce43ca6a",{pCode: CategoryCodeArr[0]},function(data){
			if(data.resultCode == "1"){
				for(var i = 0; i < data.resultObject.length; i++){
					var str = "<option value='" + data.resultObject[i].category_code + "'>" + data.resultObject[i].category_name + "</option>";
					$("#level3").append(str);
				}
				$("#level3").val(CategoryCodeArr[1]);
			}
		});
		
		//加载并回显四级
		zapjs.f.ajaxjson("../func/8f91330c425e4a1799d1dec0ce43ca6a",{pCode: CategoryCodeArr[1]},function(data){
			if(data.resultCode == "1"){
				for(var i = 0; i < data.resultObject.length; i++){
					var str = "<option value='" + data.resultObject[i].category_code + "'>" + data.resultObject[i].category_name + "</option>";
					$("#level4").append(str);
				}
				$("#level4").val(CategoryCodeArr[2]);
			}
		});
	}
	
	$("#selectContainer").show();
}

function delClick(id){
	$("#categoryName"+id).text('');
	$("#categoryCode"+id).val('');
	if(idx==id){
		clearSelect();
	}
}

function cancelClick(){
	$('#zapjs_f_id_window_box').dialog('close');
	$('#zapjs_f_id_window_box').remove();
}

function subClick(productCode){
	var showLd = $("#showLd").val();
	var correlationCategory = "";
	for(var i = 1; i<=3; i++){
		var code = $("#categoryCode"+i).val()
		if(code && code!=''){
			level4Code = code.split('>')[2];
			if(level4Code && level4Code!=undefined){
				correlationCategory += level4Code + ',';
			}
		}
	}
	
	if(correlationCategory.length > 0){
		correlationCategory = correlationCategory.substr(0,correlationCategory.length-1);
	}
	
	zapjs.f.ajaxjson("../func/d39cd41425ee11eaabac005056165069",{productCode:productCode,correlationCategory:correlationCategory,showLd:showLd},function(data){
		zapjs.f.modal_close();
		zapjs.zw.func_success(data);
	});
}

function clearSelect(){
	$("#level2").find("option:selected").attr("selected", false);
	$("#level3").html("");
	$("#level4").html("");
}
</script>