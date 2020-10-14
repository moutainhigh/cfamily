<#assign checkProperties = b_page.getReqMap()["checkProperties"] >

<form class="form-horizontal" role="form">
<fieldset>
	<div>
		<input type="button" class="btn  btn-success" id="backDiv" onclick="backCatogory()" value="惠家有">
		<input type="hidden" id="parentCode"  value="44971604">
	</div>
    <div class="w_h_20 "></div>
	<div id="catogoryDivInfo">
		<div class="control-group">
			<div id="sellerCategory"  size="10" style="width: 300px;">
				
			</div>
			
		</div>
		
		<br />
		<p class="warning" style="color: red"></p>
		<div class="control-group">
			<button type="button" class="btn btn-large btn-primary" onclick="copyProperties()">确认</button>
		</div>
	</div>
</fieldset>
</form>

<script>
$(function(){
	var obj = {}; 
	zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryAllApi',obj,function(result) {
		$('#parentCode').val(result.parentCategoryCode);
		$('#backDiv').val(result.categoryName);
		var selectChar = '';
		var categoryList = result.list;
		for(var i = 0; i < categoryList.length; i++) {
			var category = categoryList[i];
			var categoryCode = category.category_code;
			var categoryName = category.category_name;
			
			selectChar += '<div style="margin: 5px;"><input name="addCheck" style="margin-top: -2px;" type="checkbox" value="'+categoryCode+'"><span style="cursor: default" onclick="changeCategory('+categoryCode+')">'+categoryName+'</span></div>';
		}
		$('#sellerCategory').html(selectChar);
	});
});

// 返回上级分类
function backCatogory() {
	var parentCode = $('#parentCode').val();
	// 根据父级分类编码查询子分类
	changeCategory(parentCode);
}

// 根据父级分类编码查询子分类
function changeCategory(categoryCode) {
	var obj = {
		parentCode: categoryCode
	};
	zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryAllApi',obj,function(result) {
		$('#parentCode').val(result.parentCategoryCode);
		$('#backDiv').val(result.categoryName);
		var selectChar = '';
		var categoryList = result.list;
		for(var i = 0; i < categoryList.length; i++) {
			var category = categoryList[i];
			var categoryCode = category.category_code;
			var categoryName = category.category_name;
			var level = category.level;
			if(level < 4){
				selectChar += '<div style="margin: 5px;"><input name="addCheck" style="margin-top: -2px;" type="checkbox" value="'+categoryCode+'"><span style="cursor: default" onclick="changeCategory('+categoryCode+')">'+categoryName+'</span></div>';
			}else{
				selectChar += '<div style="margin: 5px;"><input name="addCheck" style="margin-top: -2px;" type="checkbox" value="'+categoryCode+'"><span style="cursor: default">'+categoryName+'</span></div>';
			}
		}
		document.getElementById('sellerCategory').innerHTML = selectChar;
	});
}


function copyProperties() {
	var checkCategory = "";
	$("div[id='sellerCategory'] input[name='addCheck']").each(function(){
    	if(this.checked){
    		checkCategory+=this.value+",";
    	}
	}); 
	if(checkCategory.length <= 0){
		zapjs.f.message("请选择要复制到的分类！");
		return false;
	}
	var checkProperties = '${checkProperties}';
	if(checkProperties.length <= 0){
		zapjs.f.message("选择属性丢失！");
		return false;
	}
	
	var turnForm = document.createElement("form");  
	document.body.appendChild(turnForm);
	turnForm.method = 'POST';
	turnForm.setAttribute("class","form-horizontal");
	var n1 = document.createElement("input");
    n1.setAttribute("id","zw_f_checkProperties");
    n1.setAttribute("name","zw_f_checkProperties");
    n1.setAttribute("type","hidden");
    n1.setAttribute("value",checkProperties);
    turnForm.appendChild(n1);
    var n2 = document.createElement("input");
    n2.setAttribute("id","zw_f_checkCategory");
    n2.setAttribute("name","zw_f_checkCategory");
    n2.setAttribute("type","hidden");
    n2.setAttribute("value",checkCategory);
    turnForm.appendChild(n2);
    var n3 = document.createElement("input");
    n3.setAttribute("class","btn btn-small");
    n3.setAttribute("style","display:none");
    n3.setAttribute("type","button");
    n3.setAttribute("onclick","zapjs.zw.func_add(this)");
    n3.setAttribute("zapweb_attr_operate_id","01605658195c4ecf9ce4b51a07796d76");
    turnForm.appendChild(n3);
    zapjs.zw.func_call(n3);
}
</script>