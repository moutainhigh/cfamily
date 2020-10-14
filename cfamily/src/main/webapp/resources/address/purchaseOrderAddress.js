var purchaseOrderAddress ={
		getAreaData:function (pCode) {
			var content = {};
			if(pCode && pCode != null && pCode != '') {
				content.parentCode = pCode;
			}
			
			var items = [];
			zapjs.zw.api_async_call("com_cmall_familyhas_api_ApiForGetStoreDistrictNew", content, function(resultV) {
				items = resultV.areaList;
			});
			return items;
		},

		loadProvince:function () {
			var optionChars = '<option value="">请选择</option>';
			var provinceItems = getAreaData();
			$.each(provinceItems, function(index, item) {
				optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
			});
			$('#_province').html(optionChars);
		},

		loadCity: function (pCode) {
			var optionChars = '<option value="">请选择</option>';
			var cityItems = getAreaData(pCode);
			$.each(cityItems, function(index, item) {
				optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
			});
			$('#_city').append(optionChars);
		},

		loadArea:function (pCode) {
			var optionChars = '<option value="">请选择</option>';
			var areaItems = getAreaData(pCode);
			$.each(areaItems, function(index, item) {
				optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
			});
			$('#_area').append(optionChars);
		},

		loadStreet:function (pCode) {
			var optionChars = '<option value="">请选择</option>';
			var streetItems = getAreaData(pCode);
			$.each(streetItems, function(index, item) {
				optionChars += '<option value=' + item.areaId + '>' + item.areaName + '</option>';
			});
			$('#_street').append(optionChars);
		},

		//初始化wz
		initAddress:function(){
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
			
			
		}

	
}
