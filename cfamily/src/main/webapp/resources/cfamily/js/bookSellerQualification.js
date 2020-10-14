var cshop_bookSellerQualification = {
	temp : {
		seller_qualification:{},
		dress_in_category:"选择的是服装、床品类-国产，需要上传对应资质：<b>第三方检测报告",
		dress_out_category:"选择的是服装、床品类-进口，需要上传对应资质：<b>进口报关单 、入境检验检疫证明",
		
		cosmetic_in_category:"选择的是化妆品类-国产，需要上传对应资质：<b>第三方检测报告、生产许可证、卫生许可证、国产非特殊用途化妆品备案凭证 、委托加工合同、专利证书",
		cosmetic_out_category:"选择的是化妆品类-进口，需要上传对应资质：<b>进口报关单 、入境检验检疫证明、进口特殊用途化妆品卫生许可批件、进口非特殊用途化妆品备案凭证",
		
		food_in_category:"选择的是食品、保健品类-国产，需要上传对应资质：<b>第三方检测报告、生产许可证、食品流通许可证、酒类销售许可证、保健食品经营许可证、委托加工合同、卫生许可证及附件、保健食品批准证书及附件、产地证明、农产品检测报告、卫生检疫合格证明",
		food_out_category:"选择的是食品、保健品类-进口，需要上传对应资质：<b>进口报关单 、入境检验检疫卫生证明",
		
		kitchen_in_category:"选择的是厨房、家居类-国产，需要上传对应资质：<b>第三方检测报告、生产许可证、专利证书",
		kitchen_out_category:"选择的是厨房、家居类-进口，需要上传对应资质：<b>进口报关单 、入境检验检疫卫生证明",
		
		appliance_in_category:"选择的是电器类-国产，需要上传对应资质：<b>第三方检测报告或3C证书、生产许可证、专利证书",
		appliance_out_category:"选择的是电器类-进口，需要上传对应资质：<b>进口报关单 、入境检验检疫卫生证明",

		shoes_in_category:"选择的是鞋类-国产，需要上传对应资质：<b>第三方检测报告、专利证书",
		shoes_out_category:"选择的是鞋类-进口，需要上传对应资质：<b>进口报关单",

		glasses_in_category:"选择的是眼镜类-国产，需要上传对应资质：<b>第三方检测报告、生产许可证",
		glasses_out_category:"选择的是眼镜类-进口，需要上传对应资质：<b>进口报关单",

		watch_in_category:"选择的是手表、箱包类-国产，需要上传对应资质：<b>第三方检测报告、生产许可证",
		watch_out_category:"选择的是手表、箱包类-进口，需要上传对应资质：<b>进口报关单",

		jewelry_in_category:"选择的是珠宝玉石贵金属首饰类-国产，需要上传对应资质：<b>第三方检测报告",
		jewelry_out_category:"选择的是珠宝玉石贵金属首饰类-进口，需要上传对应资质：<b>进口报关单",

		digital_in_category:"选择的是3C数码类-国产，需要上传对应资质：<b>第三方检测报告、国家强制性产品认证证书 、电信设备进网许可证 、无线电发射设备型号核准证、*电源适配器国家强制性产品认证证书",

		health_in_category:"选择的是健康用品类-国产，需要上传对应资质：<b>第三方检测报告、制造计量器具许可证、医疗器械注册证、计量器具检定证书",
		health_out_category:"选择的是健康用品类-进口，需要上传对应资质：<b>进口报关单、入境货物检验检疫证明"
	},
	init:function(b,brand_name,category_name){
		//如此设计防止审批后品牌与品类的值变成空
		$("#brand_name_span").html(brand_name);
		$("#category_name_span").html(category_name);
		cshop_bookSellerQualification.temp.seller_qualification = b;
		cshop_bookSellerQualification.set_value();
		cshop_bookSellerQualification.set_category_tip();//设置品类提示语
		
	},
	set_value:function(){	//设置值
		var qualificationList = cshop_bookSellerQualification.temp.seller_qualification.qualificationList;
		if (null != qualificationList && qualificationList.length > 0) {
			//授权书,商标注册证,品牌逐级销售授权三个资质在页面写死在前三个，所以要特殊判断一下
			for (var j = 0; j < qualificationList.length; j++) {
				var qualificationName = qualificationList[j].qualificationName;
				var qualificationPicArr = qualificationList[j].qualificationPic.split("|");
				var endTime = qualificationList[j].endTime;
				//开始循环添加表格的行
				
				var picView = '';
				//循环上传图片
				for(var i=0;i<qualificationPicArr.length;i++){
					picView += '<li class="control-upload-list-li"><div><div class="w_left w_w_100">'+
					'<a href="'+qualificationPicArr[i]+'" target="_blank"><img src="'+qualificationPicArr[i]+'"></a>'+
					'</div><div class="w_clear"></div></div></li>';
				}
				
				var picElement = '<span class="control-upload"><ul>'+picView+'</ul></span>';
				
				$('#cshop_addproduct_custom table tbody').append(
					'<tr><td><span>'+qualificationName+'</span></td><td>'+picElement+'</td>'
					+'<td><span>'+endTime+'</span></td>'
					+'</tr>');
			}
		}
		
	},
	set_category_tip : function(){
		var categoryCode=cshop_bookSellerQualification.temp.seller_qualification.categoryCode;
		if ("4497471600310001" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.dress_in_category);
		}else if ("4497471600310002" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.dress_out_category);
		}else if ("4497471600310003" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.cosmetic_in_category);
		}else if ("4497471600310004" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.cosmetic_out_category);
		}else if ("4497471600310005" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.food_in_category);
		}else if ("4497471600310006" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.food_out_category);
		}else if ("4497471600310007" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.kitchen_in_category);
		}else if ("4497471600310008" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.kitchen_out_category);
		}else if ("4497471600310009" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.appliance_in_category);
		}else if ("4497471600310010" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.appliance_out_category);
		}else if ("4497471600310011" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.shoes_in_category);
		}else if ("4497471600310012" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.shoes_out_category);
		}else if ("4497471600310013" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.glasses_in_category);
		}else if ("4497471600310014" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.glasses_out_category);
		}else if ("4497471600310015" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.watch_in_category);
		}else if ("4497471600310016" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.watch_out_category);
		}else if ("4497471600310017" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.jewelry_in_category);
		}else if ("4497471600310018" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.jewelry_out_category);
		}else if ("4497471600310019" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.digital_in_category);
		}else if ("4497471600310020" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.health_in_category);
		}else if ("4497471600310021" == categoryCode) {
			$("#category_tip").html(cshop_bookSellerQualification.temp.health_out_category);
		}
	}
		
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/bookSellerQualification", function() {
		return cshop_bookSellerQualification;
	});
}
