var selectAddressArea = {
	config: {
		provinceId: '',
		provinceValue: '',
		cityId: '',
		cityValue: '',
		countyId: '',
		countyValue: '',
		townId: '',
		townValue: '',
	},
	init:function(option){
		this.config = $.extend(this.config, option);
		this.loadSelect('province',$('#'+this.config.provinceId),'',this.config.provinceValue);
		
		if(this.config.provinceValue != '' && this.config.provinceValue != '0') {
			this.loadSelect('city',$('#'+this.config.cityId),this.config.provinceValue,this.config.cityValue);
		}
		if(this.config.cityValue != '' && this.config.cityValue != '0') {
			this.loadSelect('county',$('#'+this.config.countyId),this.config.cityValue,this.config.countyValue);
		}
		if(this.config.countyValue != '' && this.config.countyValue != '0') {
			this.loadSelect('town',$('#'+this.config.townId),this.config.countyValue,this.config.townValue);
		}
		
		// 延迟1秒绑定事件
		setTimeout(() => this.bindChangeEvent(), 1000);
	},
	bindChangeEvent: function() {
		if(this.config.provinceId) {
			$('#'+this.config.provinceId).change((function(level){
				return function(){
					selectAddressArea.onChange(level, this);
				}
			})('province'));
		}
		if(this.config.cityId) {
			$('#'+this.config.cityId).change((function(level){
				return function(){
					selectAddressArea.onChange(level, this);
				}
			})('city'));
		}
		if(this.config.countyId) {
			$('#'+this.config.countyId).change((function(level){
				return function(){
					selectAddressArea.onChange(level, this);
				}
			})('county'));
		}
	},
	resetSelect: function(level){
		// 父级别变更重置下级选择框
		var targetId = '';
		if(level == 'province') {
			targetId = this.config.cityId;
		}
		
		if(level == 'city') {
			targetId = this.config.countyId;
		}
		
		if(level == 'county') {
			targetId = this.config.townId;
		}
		
		if(targetId != '') {
			$('#'+targetId).val('');
			$('#'+targetId).empty();
			$('#'+targetId).append('<option value="">请选择</option>');
			$('#'+targetId).change();
		}
	},
	loadSelect: function(level, $target, pCode, selectedValue){
		if(!$target ||  $target.size() == 0) {
			return;
		}
		var param = {
			parentCode: pCode
		};				
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForGetStoreDistrictNew',param, (function(){
			return function(data){
				if(data.resultCode != 1){
					zapjs.f.message(data.resultMessage);
					return;
				}
				if(data.areaList){
					$target.empty();
					$target.append('<option value="">请选择</option>');
					for(var i in data.areaList) {
						if(data.areaList[i].areaId == selectedValue) {
							$target.append('<option value="'+data.areaList[i].areaId+'" selected="selected">'+data.areaList[i].areaName+'</option>');
						} else {
							$target.append('<option value="'+data.areaList[i].areaId+'">'+data.areaList[i].areaName+'</option>');
						}
					}
					
					if(data.areaList.length == 0 && level == 'town') {
						$target.hide();
					} else {
						$target.show();
					}
				}
			}
		})());	
	},
	onChange: function(level, target){
		this.resetSelect(level); // 重置子级下拉框
		
		var selectedValue = $(target).val();
		if(selectedValue == '') {
			return;
		}
		
		// 四级地址下拉框在前几级变更的时候默认先展示出来
		$('#'+this.config.townId).show();
		
		// 加载下一级下拉框
		if(level == 'province') {
			this.loadSelect('city', $('#'+this.config.cityId), selectedValue, '');
		} else if(level == 'city') {
			this.loadSelect('county', $('#'+this.config.countyId), selectedValue, '');
		} else if(level == 'county') {
			this.loadSelect('town', $('#'+this.config.townId), selectedValue, '');
		}
	},
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/selectAddressArea",function() {
		return selectAddressArea;
	});
}