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
function init(uid,address_name,address_mobile,address_default,address_street,four,three,two,one) {
	if(uid != ""){
		$("#zw_f_address_name").val(address_name);
		$("#zw_f_address_mobile").val(address_mobile);
		$("#_province").val(one);
		loadCity(one);
		$("#_city").val(two);
		loadArea(two);
		$("#_area").val(three);
		loadStreet(three);
		$("#_street").val(four);
		$("#zw_f_address_street").val(address_street);
		$("#zw_f_address_default").val(address_default);
	}else{
		$("#sub").attr("value","提交添加");
	}
}