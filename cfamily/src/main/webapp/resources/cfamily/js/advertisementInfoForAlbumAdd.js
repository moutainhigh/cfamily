var advertisementInfoForAlbumAdd ={
	
	init:function (){
		advertisementInfoForAlbumAdd.init_add();
		$('#zw_f_page_type').change(function(){
			advertisementInfoForAlbumAdd.show_field();
		});
		$('#zw_f_advertise_type').change(function(){
			advertisementInfoForAlbumAdd.show_relatedfield();
		});
		
		 
	},
	show_field :function (){
		if($("#zw_f_page_type").val()=='44975022001'){//首页
			$("[value='44975019001']").show();
			$("[value='44975019002']").hide();
			$("[value='44975019003']").show();
		}else if($("#zw_f_page_type").val()=='44975022002'){//个人中心
			$("[value='44975019001']").hide();
			$("[value='44975019002']").show();
			$("[value='44975019002']").show();
		}else{//首页+个人中心
			$("[value='44975019001']").hide();
			$("[value='44975019002']").hide();
			$("[value='44975019003']").show();
		}
	},
	show_relatedfield :function (){
		if($("#zw_f_advertise_type").val()=='44975019001'){//插屏广告
			$("#zw_f_position").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_position").parent().parent().hide();
			$("#zw_f_position").val("");
		}else if($("#zw_f_advertise_type").val()=='44975019002'){//banner广告
			$("label[for='zw_f_position']").html("<span class=\"w_regex_need\">*</span>位置：");
			$("#zw_f_position").attr("zapweb_attr_regex_id", "469923180002");// 位置设为必填
			$("#zw_f_position").parent().parent().show();
			$("[value='44975020003']").hide();
		}else{//悬浮广告
			$("[value='44975020003']").show();
			$("label[for='zw_f_position']").html("<span class=\"w_regex_need\">*</span>位置：");
			$("#zw_f_position").attr("zapweb_attr_regex_id", "469923180002");// 位置设为必填
			$("#zw_f_position").parent().parent().show();
		}
	},
	
	init_add:function (){
		
		if($("#zw_f_page_type").val()=='44975022001'){//首页
			$("[value='44975019001']").show();
			$("[value='44975019002']").hide();
			$("[value='44975019003']").show();
		}else if($("#zw_f_page_type").val()=='44975022002'){//个人中心
			$("[value='44975019001']").hide();
			$("[value='44975019002']").show();
			$("[value='44975019002']").show();
		}else{//首页+个人中心
			$("[value='44975019001']").hide();
			$("[value='44975019002']").hide();
			$("[value='44975019003']").show();
		}
		var type=$("#zw_f_advertise_type").val();
		if(type == "44975019001"){
			$("#zw_f_position").removeAttr("zapweb_attr_regex_id");// 设为非必填
			$("#zw_f_position").parent().parent().hide();
			$("#zw_f_position").val("");
		}else if($("#zw_f_advertise_type").val()=='44975019002'){//banner广告
			$("label[for='zw_f_position']").html("<span class=\"w_regex_need\">*</span>位置：");
			$("#zw_f_position").attr("zapweb_attr_regex_id", "469923180002");// 位置设为必填
			$("#zw_f_position").parent().parent().show();
			$("[value='44975020002']").hide();
		}else{//悬浮广告 
			$("label[for='zw_f_position']").html("<span class=\"w_regex_need\">*</span>位置：");
			$("#zw_f_position").attr("zapweb_attr_regex_id", "469923180002");// 位置设为必填
			$("#zw_f_position").parent().parent().show();
			$("[value='44975020002']").show();
		}
		
	}
	
};