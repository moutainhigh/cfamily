<form class="form-horizontal" role="form">
<fieldset>
	<div id="catogoryDivInfo">
		<div class="control-group">
			<select id="threeCategory1" onchange="changeCategory1()" size="10" style="width: 300px; height: 250px;">
				
			</select>
			<select id="threeCategory2" onchange="changeCategory2()" size="10" style="width: 300px; height: 250px;">
				
			</select>
			<select id="threeCategory3" size="10" style="width: 300px; height: 250px;">
				
			</select>
		</div>
		
		<br />
		<p class="warning" style="color: red"></p>
		<div class="control-group">
			<button type="button" class="btn btn-large btn-primary" onclick="addThreeCategory()">添加</button>
		</div>
	</div>
</fieldset>
</form>

<script>
zapjs.f.ready(function() {
	var obj = {}; 
	zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryThreeApi',obj,function(result) {
		var selectChar = '';
		var categoryList = result.list;
		for(var i = 0; i < categoryList.length; i++) {
			var category = categoryList[i];
			var categoryCode = category.category_code;
			var uid = category.uid;
			var categoryName = category.category_name;
			selectChar += '<option value="' + categoryCode + '" data-uid="' + uid + '">' + categoryName + '</option>';
		}
		document.getElementById('threeCategory1').innerHTML = selectChar;
	});
});

function changeCategory1() {
	var parentCode = document.getElementById('threeCategory1').value;
	var obj = {
		parentCode: parentCode
	};
	zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryThreeApi',obj,function(result) {
		var selectChar = '';
		var categoryList = result.list;
		for(var i = 0; i < categoryList.length; i++) {
			var category = categoryList[i];
			var categoryCode = category.category_code;
			var uid = category.uid;
			var categoryName = category.category_name;
			selectChar += '<option value="' + categoryCode + '" data-uid="' + uid + '">' + categoryName + '</option>';
		}
		document.getElementById('threeCategory2').innerHTML = selectChar;
		document.getElementById('threeCategory3').innerHTML = '';
	});
}

function changeCategory2() {
	var parentCode = document.getElementById('threeCategory2').value;
	var obj = {
		parentCode: parentCode
	};
	zapjs.zw.api_call('com_cmall_productcenter_process_SellerCategoryThreeApi',obj,function(result) {
		var selectChar = '';
		var categoryList = result.list;
		for(var i = 0; i < categoryList.length; i++) {
			var category = categoryList[i];
			var categoryCode = category.category_code;
			var uid = category.uid;
			var categoryName = category.category_name;
			selectChar += '<option value="' + categoryCode + '" data-uid="' + uid + '">' + categoryName + '</option>';
		}
		document.getElementById('threeCategory3').innerHTML = selectChar;
	});
}

function addThreeCategory() {
	var threeCategory3 = document.getElementById('threeCategory3');
	var index = threeCategory3.selectedIndex;
	var threeCode = '';
	if(index > -1) {
		threeCode = threeCategory3.options[index].getAttribute('data-uid');
	}
	
	var category_code = $.trim(window.parent.$('#zw_page_common_tree').tree('getSelected').id);
	if(threeCode != '' && category_code != '') {
		var turnForm = document.createElement("form");  
    	document.body.appendChild(turnForm);
    	turnForm.method = 'POST';
 		turnForm.setAttribute("class","form-horizontal");
 		var n1 = document.createElement("input");
	    n1.setAttribute("id","zw_f_threeCode");
	    n1.setAttribute("name","zw_f_threeCode");
	    n1.setAttribute("type","hidden");
	    n1.setAttribute("value",threeCode);
	    turnForm.appendChild(n1);
	    var n2 = document.createElement("input");
	    n2.setAttribute("id","zw_f_category_code");
	    n2.setAttribute("name","zw_f_category_code");
	    n2.setAttribute("type","hidden");
	    n2.setAttribute("value",category_code);
	    turnForm.appendChild(n2);
	    var n3 = document.createElement("input");
	    n3.setAttribute("class","btn btn-small");
	    n3.setAttribute("style","display:none");
	    n3.setAttribute("type","button");
	    n3.setAttribute("onclick","zapjs.zw.func_add(this)");
	    n3.setAttribute("zapweb_attr_operate_id","ec3a73bca8de4922b8c86a474680492b");
	    turnForm.appendChild(n3);	
	    zapjs.zw.func_call(n3);	
	}else {
		zapadmin.model_message('请选择三级分类!');
	}
}
</script>