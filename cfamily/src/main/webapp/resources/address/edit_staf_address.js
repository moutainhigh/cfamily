

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
	
	var uid = $('#zw_f_uid').val();
	var content = {};
	if(uid && uid != null && uid != '') {
		content.uid = uid;
	}
	var items = [];
	zapjs.zw.api_async_call("com_cmall_familyhas_api_ApiForGetNeigouAddress", content, 
		function(apiResult) {
			
			$('#_province').find("option[value='"+apiResult.province+"']").attr('selected','selected');
			$('#_province').change();
			$('#_city').find("option[value='"+apiResult.city+"']").attr('selected','selected');
			$('#_city').change();
			$('#_area').find("option[value='"+apiResult.area+"']").attr('selected','selected');
			$('#_area').change();
			$('#_street').find("option[value='"+apiResult.street+"']").attr('selected','selected');
		}
	);
	
	
});

