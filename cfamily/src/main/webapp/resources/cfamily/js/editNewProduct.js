var cshop_product = {

	product : {},
	defaultuid:"",
	temp : {
		// 分类属性
		categoryproperty : [],
		//商品虚类
		usprList : [],
		// 属性列表
		proplist : [],
		// 基本属性
		baseprop : {},
		// 是否修改模式
		flagedit : false,

		// 父属性字段长度
		fatherlength : 16,
		// 属性字段长度
		extendlength : 20,

		// 颜色属性
		p_color : {},
		// 关键属性
		p_key : {},
		// 属性列 主键为属性的ID
		p_list : {},
		p_extend : {},
		
		copy_tr : {vPrice:"",vNum:"",color:"",size:""}
	},
	// 初始化
	init_product : function(p) {

//		cshop_product.init_category();
		cshop_product.product = p;

		// 京东和网易考拉隐藏售后地址
		if(cshop_product.product.smallSellerCode == "SF03WYKLPT" || cshop_product.product.smallSellerCode == "SF031JDSC"){
			$('#zw_f_after_sale_address_uid').parent().parent().hide();
		}
		
		cshop_product.move_elm();
		
		$('#zw_f_qualification_category_code').change(cshop_product.onCheckQualification);
		
//		$("#zw_f_purchase_type").val("4497471600160001");	//经销
//		$("#zw_f_settlement_type").val("4497471600110001");	//特殊结算
//		$("#zw_f_purchase_type option").each(function(index){
//			if ($("#zw_f_purchase_type").val() != $(this).val()) {
//				$(this).remove();
//			}
//		});
		
//		$("#zw_f_settlement_type option").each(function(index){
//			if ($("#zw_f_settlement_type").val() != $(this).val()) {
//				$(this).remove();
//			}
//		});
		//*市场价格
		document.getElementById("zw_f_market_price").setAttribute("onkeyup","cshop_product.checkNum(this)");
		//*销售价格
//		document.getElementById("zw_f_sell_price").setAttribute("onkeyup","cshop_product.checkNum(this)");
		//*成本价格
//		document.getElementById("zw_f_cost_price").setAttribute("onkeyup","cshop_product.checkNum(this)");
		//*商品库存
//		document.getElementById("zw_f_stock").setAttribute("onkeyup","cshop_product.checkout_number(this)");
		cshop_product.SecondStep();

		$("#zw_f_category_codes").on('onCategoryChange', this.onCategoryChange);
		var categoryCode = $("#zw_f_category_codes").val();
		// 海外购分类下的商品可以设置贸易类型
		if(categoryCode && categoryCode.indexOf(categoryHWG) == 0){
			$('#zw_f_product_trade_type').parents('.control-group').removeClass('hide');
			$('#zw_f_product_trade_type').val(cshop_product.product.pcProductinfoExt.productTradeType);
		}else{
			$('#zw_f_product_trade_type').parents('.control-group').addClass('hide');
			$('#zw_f_product_trade_type').val('');
		}
	},

	init_category : function() {
		$("#zw_f_listbox").comboboxC({
			"type" : 2,
			"maxlevel" : 3,
			"width" : 200,
			"height" : 200,
			"classid" : "",
			"selectValueName" : "zw_f_listbox",
			"onchange" : cshop_product.onchangeSelect,
			"parentReplaceName" : "",
			"url" : "",
			"api" : "com_cmall_productcenter_service_ApiGetSellerCategory"
		}, [ {
			"id" : "zw_f_listboxselectid",
			"name" : "zw_f_listboxselectname",
			"level" : 1,
			"currentSelectValue" : "",
			"currentSelectValueText" : "",
			"parentid" : "",
			"searchid" : "",
			"data" : []
		} ]);
	},

	onchangeSelect : function(node, dataKeys) {
		var textValue = "当前选中的分类 ：";
		$.each(dataKeys, function(i, n) {
			if (n.level <= node.level) {
				textValue += $("#" + n.id).find("option:selected").text();
				if (n.level != node.level)
					textValue += ">";
			}
		});
		$("#BreadcrumbNavigation").html(textValue);
	},
	onCategoryChange : function() {
		var categoryCode = $("#zw_f_category_codes").val();
		// 海外购分类下的商品可以设置贸易类型
		if(categoryCode && categoryCode.indexOf(categoryHWG) == 0){
			$('#zw_f_product_trade_type').parents('.control-group').removeClass('hide');
			$('#zw_f_product_trade_type').val(cshop_product.product.pcProductinfoExt.productTradeType);
			
			//5.5.4版本变更税率规则
			//普通商户、缤纷商户 海外购分类商品默认税率是0 允许修改
			if($("#merchant_seller_type").val()=="4497478100050001" || $("#merchant_seller_type").val()=="4497478100050005"){
				$('#zw_f_tax_rate').val('0').prop('disabled',false);
			}
			//跨境商户、跨境直邮商户、平台入驻商户 海外购分类商品默认是0 允许修改
			if($("#merchant_seller_type").val()=="4497478100050002" || $("#merchant_seller_type").val()=="4497478100050003" || $("#merchant_seller_type").val()=="4497478100050004"){
				$('#zw_f_tax_rate').val('0').prop('disabled',false);
			}		
		}else{
			$('#zw_f_product_trade_type').parents('.control-group').addClass('hide');
			$('#zw_f_product_trade_type').val('');
			
			//5.5.4版本变更税率规则
			//普通商户、缤纷商户 默认 可选
			if($("#merchant_seller_type").val()=="4497478100050001" || $("#merchant_seller_type").val()=="4497478100050005"){
				$('#zw_f_tax_rate').prop('disabled',false);
			}
			//跨境商户、跨境直邮 默认 可选
			if($("#merchant_seller_type").val()=="4497478100050002" || $("#merchant_seller_type").val()=="4497478100050003"){
				$('#zw_f_tax_rate').val('0').prop('disabled',false);
			}
			//平台商户 默认  可选
			if($("#merchant_seller_type").val()=="4497478100050004"){
				$('#zw_f_tax_rate').prop('disabled',false);
			}	
		}
	},	
	onCheckQualification: function(evt){
		var merchantSellerType = $("#merchant_seller_type").val();
		if(merchantSellerType=='4497478100050002' || merchantSellerType == '4497478100050003'){
			// 跨境类商户不做校验 
			return;
		}
		
		var brandCode = $('#zw_f_brand').val();
		var categoryCode = $('#zw_f_qualification_category_code').val();
		if(brandCode == '' || categoryCode == '') return;
		var totalSize = 0;  // 总资质证书
		var names = [];
		var names2 = [];
		$.each(cshop_product.product.qualification,function(i,data){
			if(data.category_code == categoryCode && data.brand_code == brandCode){
				totalSize += 1;
				
				if(data.expired > 0){
					names.push(data.qualification_name); // 已经过期的资质名称
				}
				
				if(data.expired <= 0 && data.expired > -7){
					names2.push(data.qualification_name); // 即将过期的资质名称
				}
			}
		});
		
		var modalOption = {
			content : ""
		};
		// 没有资质证书
		if(totalSize == 0){
			modalOption.content = "该品牌下的资质证书尚未通过审核或未上传，暂不能维护商品！";
			zapjs.f.modal(modalOption);
		} else if(names.length > 0){ // 资质证书已过期
			modalOption.content = "该品牌下的资质证书“"+names.join(',')+"”已经过期，请尽快重新提交审核！";
			zapjs.f.modal(modalOption);
		} else if(names2.length > 0){ // 资质证书即将过期
			modalOption.content = "该品牌下的资质证书“"+names2.join(',')+"”即将过期，请尽快重新提交审核！";
			zapjs.f.modal(modalOption);
		}		
	},	

	SecondStep : function() {

			$("#catogoryDivInfo").hide();
			$("#OtherProductInfo").show();
			$(".zab_page_default_header_title").html(
					"商品管理 > 编辑商品");
			$("#zw_f_listbox").val('44971603002900010001');
			cshop_product.show_edit();
	},

//	FistStep : function() {
//		$("#catogoryDivInfo").show();
//		$("#OtherProductInfo").hide();
//	},

	move_elm : function() {
		$('#zw_f_elm_description').hide();
		$('#cshop_product_insert_prop').insertAfter('#zw_f_elm_description');
		$('#zw_f_elm_brand').hide();
		$('#zw_f_elm_brand').attr("zapweb_attr_regex_id","");
		$('#zw_f_elm_brand')
				.after(
						'<select id="zw_f_brand"><option value="">请选择</option></select>');
		// 京东和网易考拉隐藏售后地址
		if(cshop_product.product.smallSellerCode != "SF03WYKLPT" && cshop_product.product.smallSellerCode != "SF031JDSC"){			
			// 售后地址下拉框
			$('#zw_f_after_sale_address_uid').hide();
			$('#zw_f_after_sale_address_uid').attr("zapweb_attr_regex_id","");
			$('#zw_f_after_sale_address_uid').after('<select id="zw_f_after_sale_address"><option value="">请选择</option></select>');
		}
		
		$('#zw_f_traval').hide();
		$('#cshop_addproduct_travel').insertAfter('#zw_f_traval');
	},

	// 显示修改信息
	show_edit : function() {

		cshop_product.set_category();

		cshop_product.up_category_prop();

		cshop_product.set_editor_info();
		
		//商品品牌select2
		zapjs.zw.api_call('com_cmall_productcenter_service_ApiGetSellBrandForSpecial', {
		}, cshop_product.up_brand_info);
		
		// 京东和网易考拉隐藏售后地址
		if(cshop_product.product.smallSellerCode != "SF03WYKLPT" && cshop_product.product.smallSellerCode != "SF031JDSC"){
			// 查询商户下的售后地址
			var obj1 = {};
			obj1.manageCode = cshop_product.product.smallSellerCode;
			obj1.afterSaleAddressUid = '';
			zapjs.zw.api_call('com_cmall_productcenter_service_ApiGetPartAfterSaleAddress', obj1, cshop_product.up_after_sale_address);
		}
		cshop_product.set_wvp();
		
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
						cshop_product.beforesubmit);
		cshop_product.set_value();
	},
	showdefault:function(trans){
		var uuid ={};
		if(cshop_product.defaultuid.length > 0){
			uuid.uid = cshop_product.defaultuid;
		}else{
			uuid.uid = trans;
		}
		zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
			var freightName = result.tplName;
			eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (uuid.uid) +'","'+ freightName +'")' );
		});
	},
	set_value:function(){//设置值
		
		$('#zw_f_product_name').val(cshop_product.product.produtName);//商品名称
		$('#zw_f_market_price').val(cshop_product.product.marketPrice);//商场价
//		$('#zw_f_cost_price').val(cshop_product.product.costPrice);//成本价
//		$('#zw_f_sell_price').val(cshop_product.product.sellPrice);//销售价
		$('#zw_f_tax_rate').val(cshop_product.product.taxRate);//税率
		var stype = cshop_product.product.sellerType;
//		if( stype == '4497478100050002' || stype == '4497478100050003' ){  // 如果是跨境类型商户则隐藏税率下拉框  
//			$($('#zw_f_tax_rate')[0].parentNode.parentNode).remove(); 
//		} 
		var videoUrlShow = cshop_product.product.videoUrlShow;
		if(videoUrlShow != ""){
			$("#my-video").attr("poster",cshop_product.product.videoMainPic);
			document.getElementById("product_desc_video").src = cshop_product.product.videoUrlShow;
		}else{
			$(".video_hidden").attr("style","display:none");
		}
		$('#zw_f_video_main_pic').val(cshop_product.product.videoMainPic);//商品视频链接
		$('#zw_f_video_url').val(cshop_product.product.videoUrl);//商品视频链接
		$('#zw_f_product_desc_video').val(cshop_product.product.productDescVideo);//商品视频链接
		zapweb_upload.upload_file('zw_f_video_main_pic',cshop_product.product.videoMainPic);//商品视频主图
		$('#zw_f_supplier_name').val(cshop_product.product.supplierName);//供应商名称			已隐藏
		$('#zw_f_fictitious_sales').val(cshop_product.product.pcProductinfoExt.fictitiousSales);//虚拟销售量
		//添加编辑不显示mainpic_url字段 2016-08-10 zhy
		//$('#zw_f_mainpic_url').val(cshop_product.product.mainPicUrl);//商品主图片
		//zapweb_upload.upload_file('zw_f_mainpic_url',cshop_product.product.mainPicUrl);
		
		$('#zw_f_adpic_url').val(cshop_product.product.adPicUrl);//商品广告图
		zapweb_upload.upload_file('zw_f_adpic_url',cshop_product.product.adPicUrl);
		$('#zw_f_product_adv').val(cshop_product.product.productAdv);//广告语
		var picUrls='';
		for(var i in cshop_product.product.pcPicList){
			if(cshop_product.product.pcPicList[i].skuCode == "" || cshop_product.product.pcPicList[i].skuCode == null){
				if(i==0){
					picUrls=cshop_product.product.pcPicList[i].picUrl;
				}else{
					picUrls=picUrls+'|'+cshop_product.product.pcPicList[i].picUrl;
				}
			}
		}
		
		$('#zw_f_piclist').val(picUrls);//商品图片
		zapweb_upload.upload_file('zw_f_piclist',picUrls);

		$('#zw_f_editor').val(cshop_product.product.description.descriptionInfo);//商品描述
//					zapweb_upload.upload_file('zw_f_editor',cshop_product.product.description.descriptionInfo);
		$('#zw_f_description_pic').val(cshop_product.product.description.descriptionPic);//描述图片
		zapweb_upload_modify.upload_file('zw_f_description_pic',cshop_product.product.description.descriptionPic);
		
		$('#zw_f_product_weight').val(cshop_product.product.productWeight);//商品重量
		$('#zw_f_product_volume').val(cshop_product.product.productVolume);//商品体积
		$('#zw_f_volumn_length').val(cshop_product.product.productVolumeItem.split(',')[0]);//长
		$('#zw_f_volumn_width').val(cshop_product.product.productVolumeItem.split(',')[1]);//宽
		$('#zw_f_volumn_high').val(cshop_product.product.productVolumeItem.split(',')[2]);//高
		
		$('#zw_f_purchase_type').val(cshop_product.product.pcProductinfoExt.purchaseType);//采购类型
		$('#zw_f_settlement_type').val(cshop_product.product.pcProductinfoExt.settlementType);//结算方式
		
		$('#zw_f_expiry_date').val(cshop_product.product.expiryDate);//保质期
		$('#zw_f_expiry_unit').val(cshop_product.product.expiryUnit);//保质期单位
		
		$('#zw_f_qualification_category_code').val(cshop_product.product.qualificationCategoryCode);//资质品类
		var trans = cshop_product.product.transportTemplate;
		if(trans!=null&&trans!=""&&trans=="0"){//运费模板
			cshop_product.showdefault(trans);
			$("#cshop_addproduct_travel input[name=zw_f_tra_select]:eq(0)").attr("checked",'checked'); 
		}else if(/^\d+$/.test(trans)) {
			cshop_product.showdefault(trans);
			$("#cshop_addproduct_travel input[name=zw_f_tra_select]:eq(1)").attr("checked",'checked'); 
			$('#zw_f_tra_user').val(trans);
		}else{
			var uuid ={};
			uuid.uid = trans;
			if(trans.length > 0){
				uuid.uid = trans;
			}else{
				if(cshop_product.defaultuid.length > 0){
					uuid.uid = cshop_product.defaultuid;
				}else{
					uuid.uid = trans;
				}
			}
			zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
				var freightName = result.tplName;
				$("#cshop_addproduct_travel input[name=zw_f_tra_select]:eq(2)").attr("checked",'checked');
				eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (uuid.uid) +'","'+ freightName +'")' );
			});
			/*var uuid ={};
			uuid.uid = trans;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
				var freightName = result.tplName;
				$("#cshop_addproduct_travel input[name=zw_f_tra_select]:eq(2)").attr("checked",'checked');
				eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (trans) +'","'+ freightName +'")' );
			});*/
		}

		var area = cshop_product.product.areaTemplate;
		if(area!=null && area!=""){
			var code = {};
			code.templateCode = area;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_getTemplateNameApi',code,function(result){
				var templateName = result.templateName;
				eval('parent.zapadmin_single.result'+'("zw_f_area_template","'+(area)+'","'+templateName+'")');
			});
		}
		
		$('#zw_f_sell_productcode').val(cshop_product.product.sellProductcode);//货号
		$('#zw_f_flag_payway').val(cshop_product.product.flagPayway);//是否货到付款
		$('#zw_f_labels').val(cshop_product.product.labels);//关键字
		if(cshop_product.product.description.keyword!=null&&cshop_product.product.description.keyword!=''){
			var labelsStr = cshop_product.product.description.keyword.replace(/,/g,' '); 				//将标签之间的逗号替换为空格
			$('#zw_f_keyword').val(labelsStr);//标签显示
		}
		
//		$('#zw_f_stock').val(cshop_product.product.stock);//商品库存字段
		$('#zw_f_pic_material_url').val(cshop_product.product.pcProductinfoExt.picMaterialUrl);//图片相关素材地址
		$('#zw_f_pic_material_upload').val(cshop_product.product.pcProductinfoExt.picMaterialUpload);//图片相关素材上传
		zapweb_upload.upload_file('zw_f_pic_material_upload',cshop_product.product.pcProductinfoExt.picMaterialUpload);
	},
	show_box : function(sId) {
		var s=zapadmin_single.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'zapadmin_single_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'&category_code='+cshop_product.product.category.categoryCode+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		var bFlag = true;
		
		if(cshop_product.product.smallSellerCode != "SF03WYKLPT" && cshop_product.product.smallSellerCode != "SF031JDSC"){			
			var address = $("#zw_f_after_sale_address").val();
			if(null != address && address != ""){
				$("#zw_f_after_sale_address_uid").val(address);
			}/*else{
				zapjs.f.message('*售后地址：不能为空');
				return false;
			}*/
		}
		
		//*商品标签
		var zw_f_keyword = $("#zw_f_keyword").val();
		//*市场价格
		var zw_f_market_price = $("#zw_f_market_price").val();
		//*成本价格
//		var zw_f_cost_price = $("#zw_f_cost_price").val();
		//*销售价格
//		var zw_f_sell_price = $("#zw_f_sell_price").val();
		//*税率
//		var zw_f_tax_rate = $("#zw_f_tax_rate").val();
		//*关键字
		var zw_f_labels = $("#zw_f_labels").val();
		//*买家承担运费
		var zw_f_tra_user = $("#zw_f_tra_user").val();
		//*商品重量
		var zw_f_product_weight = $("#zw_f_product_weight").val();
		//商品体积
		var zw_f_product_volume = $("#zw_f_product_volume").val();
		//*虚拟销量
		var zw_f_fictitious_sales = $("#zw_f_fictitious_sales").val();
		
		//*保质期
		var zw_f_expiry_date = $("#zw_f_expiry_date").val();
		//标签总长度不能超过10个字
//		if (zw_f_keyword.replace(/\s+/g,'').length > 10) {
//			zapjs.f.message("*商品标签：值过长，不符合规则，标签总长度不能超过10个字符");
////			 $("#zw_f_keyword").attr("class", "w_regex_error");
//			return false;
//		}
		//保质期不能超过10位
		if ($('#zw_f_expiry_date').val().length>10) {
			zapjs.f.message('*保质期：长度超过限制，最长为10位');
			return false;
		};
		//市场价格的个位不能超过10位
		if (zw_f_market_price.substring(0 , zw_f_market_price.indexOf(".") == -1 ? zw_f_market_price.length :  zw_f_market_price.indexOf(".") ).length > 10) {
			zapjs.f.message("*市场价格：值过大，不符合规则，小数点前最长为10位");
			return false;
		}
		//商品重量的个位不能超过10位
		if (zw_f_product_weight.substring(0 , zw_f_product_weight.indexOf(".") == -1 ? zw_f_product_weight.length :  zw_f_product_weight.indexOf(".") ).length > 10) {
			zapjs.f.message("*商品重量：值过大，不符合规则，小数点前最长为10位");
			return false;
		}
		//商品体积的个位不能超过10位
		if (zw_f_product_volume.substring(0 , zw_f_product_volume.indexOf(".") == -1 ? zw_f_product_volume.length :  zw_f_product_volume.indexOf(".") ).length > 10) {
			zapjs.f.message("商品体积：值过大，不符合规则，小数点前最长为10位");
			return false;
		}
		if (zw_f_fictitious_sales != "" && zw_f_fictitious_sales != null) {
			if (zw_f_fictitious_sales.length>8) {
				zapjs.f.message('虚拟销售量基数：长度超过限制，最长为8位');
				return false;
			};
			if (eval(zw_f_fictitious_sales) < eval(0)) {
				zapjs.f.message('虚拟销售量基数：虚拟销售量基数必须为大于等于0的数字');
				return false;
			}
		};
		if (zw_f_labels.length>40) {
			zapjs.f.message('关键字：长度超过限制，最长为40个字符');
			return false;
		};
		//成本价格的个位不能超过10位
//		if (zw_f_cost_price.substring(0 , zw_f_cost_price.indexOf(".") == -1 ? zw_f_cost_price.length :  zw_f_cost_price.indexOf(".") ).length > 10) {
//			zapjs.f.message("*成本价格：值过大，不符合规则，小数点前最长为10位");
//			return false;
//		}

		//销售价格的个位不能超过10位
//		if (zw_f_sell_price.substring(0 , zw_f_sell_price.indexOf(".") == -1 ? zw_f_sell_price.length :  zw_f_sell_price.indexOf(".") ).length > 10) {
//			zapjs.f.message("*销售价格：值过大，不符合规则，小数点前最长为10位");
//			return false;
//		}

		//运费的个位不能超过10位
		if (zw_f_tra_user.substring(0 , zw_f_tra_user.indexOf(".") == -1 ? zw_f_tra_user.length :  zw_f_tra_user.indexOf(".") ).length > 10) {
			zapjs.f.message("*买家承担运费 ：值过大，不符合规则，小数点前最长为10位");
			return false;
		}

//		if (bFlag) {
//			if ($('#zw_f_product_name').val().length>40) {
//				zapjs.f.message('商品名称：长度超过限制，最长为40个字符');
//				bFlag = false;
//			};
//		}
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_piclist', '469923180002')) {
				bFlag = false;
			}
		}
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_brand', '469923180002', '商品品牌')) {
				bFlag = false;
			}
		}
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_description_pic', '469923180002', '描述图片')) {
				bFlag = false;
			}
		}
		if (bFlag) {
			if ($('#zw_f_product_desc_video').val().length>400) {
				zapjs.f.message('视频链接：长度超过限制，最长为400个字符');
				bFlag = false;
			};
		}
		
		if($('#zw_f_product_desc_video').val().length>0&&$('#zw_f_video_main_pic').val().length<=0){
			zapjs.f.message('请上传视频封面图');
			bFlag = false;
		}
		
		if($('#zw_f_product_desc_video').val().length<=0&&$('#zw_f_video_main_pic').val().length>0){
			zapjs.f.message('请上传视频');
			bFlag = false;
		}
		if (bFlag) {
			if ($('#zw_f_video_url').val().length>400) {
				zapjs.f.message('视频链接：长度超过限制，最长为400个字符');
				bFlag = false;
			};
		}
		
		// 开始处理运费策略
		if (bFlag) {
			var sVal = $(
					'#cshop_addproduct_travel input:radio[name=zw_f_tra_select]:checked')
					.val();
			if (sVal == 0) {
				$('#zw_f_traval').val(0);
			} else if (sVal == 1) {

				if (!zapjs.zw.validate_field('#zw_f_tra_user', '469923180007',
						'买家承担运费')) {

					bFlag = false;
				} else {
					$('#zw_f_traval').val($('#zw_f_tra_user').val());
				}
			} else if (sVal == 2) {

			} else {
//				zapjs.f.message('请选择运费模式！');
//				bFlag = false;
			}

		}
		
		// 如果贸易类型被展示处理则必须选择一个值
		if($('#zw_f_product_trade_type').parents('.control-group').eq(0).hasClass('hide') == false){
			if($('#zw_f_product_trade_type').val() == ''){
				zapjs.f.message('请选择贸易类型！');
				return false;
			}
		}
		
		if (bFlag) {
			cshop_product.recheck_product();

			$('#zw_f_json').val(zapjs.f.tojson(cshop_product.product));
		}
		return bFlag
		
	},
	// 重新处理格式化商品信息
	recheck_product : function() {

		var thisproduct = cshop_product.product;
		
		function send(){
		    var id = document.getElementsByName('zw_f_authority_logo');
		    var value = new Array();
		    for(var i = 0; i < id.length; i++){
		     if(id[i].checked)
		     value.push(id[i].value);
		    } 
		    return value[0];
		}
		thisproduct.authorityLogoUid = send();//商品保障

		thisproduct.produtName = $('#zw_f_product_name').val();

		thisproduct.brandCode = $('#zw_f_brand').val();
		// 京东和网易考拉隐藏售后地址
		if(cshop_product.product.smallSellerCode != "SF03WYKLPT" && cshop_product.product.smallSellerCode != "SF031JDSC"){
			thisproduct.afterSaleAddressUid = $('#zw_f_after_sale_address_uid').val();//售后地址
		}
		thisproduct.brandName = $('#zw_f_brand > option:selected').text();

		thisproduct.productVolume = $('#zw_f_product_volume').val();

		thisproduct.productWeight = $('#zw_f_product_weight').val();

		thisproduct.sellProductcode = $('#zw_f_sell_productcode').val();

		thisproduct.transportTemplate = $('#zw_f_traval').val();

		thisproduct.taxRate = $('#zw_f_tax_rate').val();
		// 是否货到付款 
		thisproduct.flagPayway=$('#zw_f_flag_payway').val();

//		thisproduct.costPrice = $('#zw_f_cost_price').val();//成本价

//		thisproduct.sellPrice = $('#zw_f_sell_price').val();//销售价
		
		thisproduct.marketPrice = $('#zw_f_market_price').val();
		
//		thisproduct.stock = $('#zw_f_stock').val();//库存
		
		thisproduct.labels=$('#zw_f_labels').val();
		
		thisproduct.productDescVideo = $('#zw_f_product_desc_video').val();//商品视频链接
		thisproduct.videoMainPic = $('#zw_f_video_main_pic').val();//商品视频链接
		thisproduct.videoUrl = $('#zw_f_video_url').val();//商品视频链接
		
		thisproduct.areaTemplate = $('#zw_f_area_template').val();
		//长宽高
		var length = $('#zw_f_volumn_length').val().replace(/^\s+|\s+$/g, '');
		var width = $('#zw_f_volumn_width').val().replace(/^\s+|\s+$/g, '');
		var high = $('#zw_f_volumn_high').val().replace(/^\s+|\s+$/g, '');
		thisproduct.productVolumeItem=length+','+width+','+high;
		
		thisproduct.expiryDate = $('#zw_f_expiry_date').val();//商品保质期
		thisproduct.expiryUnit = $('#zw_f_expiry_unit').val();//商品保质期单位
		
		thisproduct.qualificationCategoryCode=$('#zw_f_qualification_category_code').val();//资质品类
		
		// 商品实类
		thisproduct.pcProductcategoryRel = {
			categoryCode : $('#zw_f_listbox').val()
		};
		//商品虚类
		var categoryCodeArr = $("#zw_f_category_codes").val().split(",");
		thisproduct.usprList = [];
		for (var i = 0; i < categoryCodeArr.length; i++) {
			thisproduct.usprList.push({"categoryCode":categoryCodeArr[i]});
		}
		// 商品图片
		thisproduct.pcPicList = [];
		var sPicList = $('#zw_f_piclist').val().split('|');
		for ( var i in sPicList) {
			thisproduct.pcPicList.push({
				picUrl : sPicList[i]
			});
		}
		;

//		if (sPicList.length > 0) {
//			thisproduct.mainPicUrl = sPicList[0];
//		}
		//添加编辑不显示mainpic_url字段 2016-08-10 zhy
		//thisproduct.mainPicUrl =$('#zw_f_mainpic_url').val();
		thisproduct.adPicUrl =$('#zw_f_adpic_url').val();
		//添加广告语 2017-03-15 zhy
		thisproduct.productAdv =$('#zw_f_product_adv').val();
//		cshop_product.recheck_prop();

		// SKU信息
		thisproduct.productSkuInfoList = [];

		// 商品属性
		thisproduct.pcProductpropertyList = [];

		// 商品基本属性
		$('#cshop_addproduct_prop :checked')
				.each(
						function(n, el) {
							var sVal = $(el).val();
							var othis = cshop_product.up_prop_key(sVal);
							var ofather = cshop_product
									.up_prop_key(othis["parent_code"]);

							thisproduct.pcProductpropertyList
									.push({
										propertyKeycode : ofather["property_code"],
										propertyCode : othis["property_code"],
										propertyKey : ofather["property_name"],
										propertyValue : othis["property_name"],
										propertyType : (cshop_product
												.up_type_bycode(sVal) == 1 ? 449736200001
												: 449736200002)
									});

						});
		// 销售属性
		for ( var p in cshop_product.temp.p_extend) {
			var othis = cshop_product.up_prop_key(p);
			var sV = $('#cshop_addproduct_extend_' + p).val();
			if (sV) {
				thisproduct.pcProductpropertyList
						.push({
							propertyKeycode : othis["property_code"],
							propertyCode : sV,
							propertyKey : othis["property_name"],
							propertyValue : cshop_product.up_prop_key(sV)["property_name"],
							propertyType : 449736200003
						});

			}

		}

		// 自定义属性
		$('#cshop_addproduct_custom table tbody tr').each(function(n, el) {
			var sValue = $(el).find('.c_value').val();
			var sText = $(el).find('.c_text').val();
			if (sValue && sText) {
				thisproduct.pcProductpropertyList.push({
					propertyKeycode : '',
					propertyCode : '',
					propertyKey : sText,
					propertyValue : sValue,
					propertyType : 449736200004
				});
			}

		});
		var sTextAdd = $("#cshop_addproduct_custom_text").val();
		var sValueAdd = $("#cshop_addproduct_custom_value").val();
		if (sTextAdd && sValueAdd) {
			thisproduct.pcProductpropertyList.push({
				propertyKeycode : '',
				propertyCode : '',
				propertyKey : sTextAdd,
				propertyValue : sValueAdd,
				propertyType : 449736200004
			});
		}
		// 商品描述
		thisproduct.description = {
			descriptionInfo : $('#zw_f_editor').val(),
			descriptionPic:$('#zw_f_description_pic').val(),
			keyword : $('#zw_f_keyword').val()
		};
		// 商品扩展信息
		thisproduct.pcProductinfoExt = {
				purchaseType : $('#zw_f_purchase_type').val(),
				settlementType : $('#zw_f_settlement_type').val(),
				mdId: $("#mdId").val(),
				mdNm: $("#mdName").val(),
				fictitiousSales : $('#zw_f_fictitious_sales').val(),
				picMaterialUrl: $("#zw_f_pic_material_url").val(),
				picMaterialUpload: $("#zw_f_pic_material_upload").val(),
				productTradeType: $("#zw_f_product_trade_type").val()
		};
	},

	// 设置图片
	set_piclist : function() {
		$('#cshop_addproduct_piclist').html(
				zapweb_upload.upload_html($('#cshop_addproduct_uploadurl')
						.val(), 'zw_f_piclist', '',
						'zw_s_number=100'));

		zapweb_upload.upload_file('zw_f_piclist');
	},
	// 设置分类名称
	set_category : function() {
//		cshop_product.product.category = {};
//		cshop_product.product.category.categoryCode = $('#zw_f_listbox').val();
		var categoryCodes = "";
		if (null != cshop_product.product.usprList) {
			for (var int = 0; int < cshop_product.product.usprList.length; int++) {
				categoryCodes += (cshop_product.product.usprList[int].categoryCode+",");
			}
		}
		if (categoryCodes.length > 0) {
			categoryCodes =  categoryCodes.substring(0, categoryCodes.length-1)	
		}
		$("#zw_f_category_codes").val(categoryCodes);
		var obj = {};
		obj.categoryCodes = categoryCodes;
		obj.sellerCode="SI2003";
		zapjs.zw.api_call('com_cmall_usercenter_service_api_ApiGetCategroyName',obj,function(result) {
			if(result.resultCode==1){
				$('#xlNameId').text(result.categoryName);
			}
		});
		
	},
	// 获取分类属性
	up_category_prop : function() {

		cshop_product.up_prop_list();

	},
	// 分类属性读取成功
//	category_prop_success : function(oData) {
//
//		cshop_product.temp.categoryproperty = oData;
//
//		cshop_product.up_prop_list();
//
//	},
	// 获取属性列表
	up_prop_list : function() {

		zapjs.zw.api_call(
				'com_cmall_productcenter_process_ApiGetCategoryProperty', {
					categoryCode : cshop_product.product.category.categoryCode
				}, cshop_product.prop_list_success);
	},
	// 设置商品描述信息
	set_editor_info : function() {
		zapjs.zw.editor_show('zw_f_editor', $('#cshop_addproduct_uploadurl')
				.val());
	},

	//处理重量、体积、货到付款
	set_wvp : function(){
		var ckg = "<div  class=\"controls\" id=\"cshop_addproduct_volume_item\">"
				+"长：&nbsp;&nbsp;&nbsp;<input style=\"width:90px\" onChange=\"cshop_product.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_length\" id=\"zw_f_volumn_length\"  value=\"\"/>"
				+"宽：&nbsp;&nbsp;&nbsp;<input style=\"width:90px\" onChange=\"cshop_product.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_width\" id=\"zw_f_volumn_width\"  value=\"\"/>"
				+"高：&nbsp;&nbsp;&nbsp;<input style=\"width:90px\" onChange=\"cshop_product.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_high\" id=\"zw_f_volumn_high\"  value=\"\"/></div>";
		if(cshop_product.product.sellerCode!=''&&cshop_product.product.sellerCode.length!=5){
			$('#zw_f_flag_payway').parent().parent().hide();
		}
		$('#zw_f_product_volume').parent().append('<label id=volumn22 >(单位：立方厘米&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
		$('#zw_f_product_volume').parent().parent().append(ckg);
		$('#zw_f_product_weight').parent().append('<label id=weight22 >(单位:千克&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
		//*商品重量
		document.getElementById("zw_f_product_weight").setAttribute("onkeyup","cshop_product.checkNumWeight(this)");
		document.getElementById("zw_f_product_volume").setAttribute("onkeyup","cshop_product.checkNumVolume(this)");//体积
		document.getElementById("zw_f_volumn_length").setAttribute("onkeyup","cshop_product.checkNumLimit3(this)");
		document.getElementById("zw_f_volumn_width").setAttribute("onkeyup","cshop_product.checkNumLimit3(this)");
		document.getElementById("zw_f_volumn_high").setAttribute("onkeyup","cshop_product.checkNumLimit3(this)");
		document.getElementById("zw_f_product_volume").setAttribute("onblur","cshop_product.checkBlur(this)");
		document.getElementById("zw_f_volumn_length").setAttribute("onblur","cshop_product.checkBlur(this)");
		document.getElementById("zw_f_volumn_width").setAttribute("onblur","cshop_product.checkBlur(this)");
		document.getElementById("zw_f_volumn_high").setAttribute("onblur","cshop_product.checkBlur(this)");
		
		document.getElementById("zw_f_expiry_date").setAttribute("onkeyup","cshop_product.checkout_number(this)");
	},
	//校验只能输入数字和两位小数 
	checkNum : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,2})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和两位小数，并且小数点前限制长度为3位
	checkNumLimit3 : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d{0,3}(?:\d\.\d{0,2})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和六位小数，商品体积字段用
	checkNumVolume : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,6})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	//只能输入数字和三位小数，商品重量用
	checkNumWeight : function(ol){
		if(ol.value==ol.value2)return;if(ol.value.search(/^\d*(?:\d\.\d{0,3})?$/)==-1)ol.value=(ol.value2)?ol.value2:'';else ol.value2=ol.value;
	},
	checkBlur : function(ol){
		if(ol.value!=''||ol.value!='0'){
			var len = ol.value.length;
			if(ol.value[len-1] =='.'){
				ol.value = ol.value.replace('.','');
				cshop_product.get_vloumn();
			}
		}
	},
	//计算体积
	get_vloumn : function(){
		var length = $('#zw_f_volumn_length').val().replace(/^\s+|\s+$/g, '');
		var width = $('#zw_f_volumn_width').val().replace(/^\s+|\s+$/g, '');
		var high = $('#zw_f_volumn_high').val().replace(/^\s+|\s+$/g, '');
		if(length!=""&&width!=""&&high!=""&&!isNaN(length)&&!isNaN(width)&&!isNaN(high)){
			var vlm = length*width*high;
			var reg = /\d{1,}\.\d{3,}/;
			if(length!="0"&&width!="0"&&high!="0"){
				if(reg.test(vlm.toString())){
//							$('#zw_f_product_volume').val(vlm.toString().split(".")[0]+'.'+vlm.toString().split('.')[1][0]+vlm.toString().split('.')[1][1]);
					$('#zw_f_product_volume').val(cshop_product.volumeMultiplication(length,width,high));
				}else{
					$('#zw_f_product_volume').val(length*width*high);
				}
			}else{
				$('#zw_f_product_volume').val(0);
			}
		}
	},
	//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。   
	//调用：accMul(arg1,arg2,arg3)   
	//返回值：arg1乘以arg2乘以arg3的精确结果   
	 volumeMultiplication : function(arg1,arg2,arg3)  
	{  
	    var m=0,s1=arg1.toString(),s2=arg2.toString(),s3=arg3.toString();  
	    try{m+=s1.split(".")[1].length;}catch(e){}  
	    try{m+=s2.split(".")[1].length;}catch(e){}  
	    try{m+=s3.split(".")[1].length;}catch(e){}  
	    return Number(s1.replace(".",""))*Number(s2.replace(".",""))*Number(s3.replace(".",""))/Math.pow(10,m);
	},
	// 读取属性列表数据
	prop_list_success : function(oData) {
		cshop_product.temp.proplist = oData.listProperty;
		for ( var i in cshop_product.temp.proplist) {
			cshop_product.temp.p_list[cshop_product.temp.proplist[i]["property_code"]] = cshop_product.temp.proplist[i];
		}
		cshop_product.new_show_prop();
	},
	up_brand_info : function(oResult) {
		var oList = oResult.list;
		for ( var i in oList) {
			$('#zw_f_brand').append('<option value="' + oList[i]["brandCode"] + '">'+ oList[i]["brandName"] + '</option>');
		}
		$('#zw_f_brand').val(cshop_product.product.brandCode);
		$("#zw_f_brand").select2(); 
		$("#s2id_zw_f_brand").attr("style","width:220px");
		$("#zw_f_brand").change(cshop_product.onCheckQualification);
	},
	// 填充 售后地址 下拉框
	up_after_sale_address : function(oResult) {
		var oList = oResult.list;
		for ( var i in oList) {
			$('#zw_f_after_sale_address').append('<option value="' + oList[i]["uid"] + '">'+ oList[i]["afterSaleAddresName"] + '</option>');
		}
		$('#zw_f_after_sale_address').val(cshop_product.product.afterSaleAddressUid);
		$("#zw_f_after_sale_address").select2();
		$("#s2id_zw_f_after_sale_address").attr("style","width:220px");
	},
	// 根据key获取属性
	up_prop_key : function(sCode) {

		return cshop_product.temp.p_list[sCode];

	},
	// 获取子属性
	up_child : function(sCode) {

		var oRet = [];
		var sprop = cshop_product.temp.p_list;
		for ( var p in sprop) {
			if (sprop[p]["parent_code"] == sCode) {
				oRet.push(sprop[p]);
			}
		}
		return oRet;
	},

	// 获取属性类型定义 返回1为颜色属性 返回2为关键属性 其他为商品属性
	up_type_bycode : function(sCode) {
		var iReturn = 0;
		if (sCode.indexOf('449746200001') == 0) {
			iReturn = 1;
		} else if (sCode.indexOf('449746200002') == 0) {
			iReturn = 2;
		}

		return iReturn;

	},
	//去重
	removeRe : function(a) {
		var o = {};
		var k=[];
		for (var i=0, j=a.length; i<j; i=i+1) {
			o[a[i]] = true;
		}
		for (var j in o) {
			if (o.hasOwnProperty(j)) {
				k.push(j);
			}
		}
		return k;
		},
	// 新版显示
	new_show_prop : function() {
		//计算已选择的sku属性
		var alCheckCode = [];
		for(var s in cshop_product.product.productSkuInfoList){
			var skl = cshop_product.product.productSkuInfoList[s];
			for(var skv in  skl.skuValue.split('&')){
				alCheckCode.push(skl.skuKey.split('&')[skv].split('=')[1]+'&'+skl.skuValue.split('&')[skv].split('=')[1]+'&'+skl.skuPicUrl);
			}
		}
		alCheckCode = cshop_product.removeRe(alCheckCode); 
		cshop_product.temp.CheckedSkuCode=alCheckCode;
		var sprop = cshop_product.temp.p_list;
		// 商品属性
		var aSale = [];
		for ( var p in sprop) {
			// 判断如果是主属性
			if (p.length == cshop_product.temp.fatherlength) {
				var iType = cshop_product.up_type_bycode(p);
				if (iType != 1 && iType != 2) {
					aSale.push(p);
				}
			}
		}
		// /开始循环展示定义///
		var aBaseHtml = [];
		var aHeadHtml = [];
		// 定义扩展属性显示
		var aExtendProp = [];
		// 扩展属性
		for ( var p in aSale) {
			var ofather = cshop_product.up_prop_key(aSale[p]);
			var oSub = cshop_product.up_child(aSale[p]);
			aExtendProp.push('<tr><td>' + ofather["property_name"]+ '</td><td>');
			if (oSub.length > 0) {
				aExtendProp.push('<select id="cshop_addproduct_extend_'+ aSale[p] + '">');
				aExtendProp.push('<option value=""></option>');
				for ( var i in oSub) {
					aExtendProp.push('<option value="'+ oSub[i]["property_code"] + '">'+ oSub[i]["property_name"] + '</option>');
				}
				aExtendProp.push('</select>');
			} else {
				aExtendProp.push('<input class="c_value" type="text" />');
			}
			aExtendProp.push('</td></tr>');
			cshop_product.temp.p_extend[aSale[p]] = ofather;
		}
		if (aExtendProp.length > 0) {
			$('#cshop_addproduct_pextend table tbody').prepend(aExtendProp.join(''));
			$('#cshop_addproduct_pextend').show();
		}
		//显示自定义属性、扩展属性
		for(var props in cshop_product.product.pcProductpropertyList){
			if(cshop_product.product.pcProductpropertyList[props]['propertyType']=='449736200004'){
				$('#cshop_addproduct_custom table tbody').append(
						'<tr><td><input class="c_text" value="' + cshop_product.product.pcProductpropertyList[props]['propertyKey']
						+ '"/></td><td><textarea rows="2" class="c_value" value="' + cshop_product.product.pcProductpropertyList[props]['propertyValue']
						+ '">' + cshop_product.product.pcProductpropertyList[props]['propertyValue']+ '</textarea></td>'
						+'<td><input type="button" class="btn" onclick="cshop_product.delete_custom(this)" value="删除"/></td></tr>');
				$('#cshop_addproduct_custom_text').val('');
				$('#cshop_addproduct_custom_value').val('');
				continue;
			}else if(cshop_product.product.pcProductpropertyList[props]['propertyType']=='449736200003'){
				$('#cshop_addproduct_extend_'+cshop_product.product.pcProductpropertyList[props]['propertyKeycode']).val(cshop_product.product.pcProductpropertyList[props]['propertyCode']);
				continue;
			}
		}
		
		//自定义属性
		//增加上移下移功能
		var trs = document.getElementById("move").getElementsByTagName("tr");
		for(var i=0;i<trs.length;i++){
			if(trs[i].rowIndex==0){
				$('#cshop_addproduct_custom table tbody tr:eq('+i+')').append('<td><a href="javascript:void(0);" onclick="cshop_product.move_down(this)">下移</a></td>');
			}else{
				$('#cshop_addproduct_custom table tbody tr:eq('+i+')').append('<td><a href="javascript:void(0);" onclick="cshop_product.move_up(this)">上移</a>&nbsp&nbsp&nbsp&nbsp<a href="javascript:void(0);" onclick="cshop_product.move_down(this)">下移</a></td>');
			}
		}
		cshop_product.refurbish_lastTd();
	},


	//替换基本属性背的空格、=、&
	replaceother : function (str){
		var reg=new RegExp(" ","g");
		var reg1=new RegExp("=","g");
		var reg2=new RegExp("&","g");
		str=str.replace(reg,'');
		str=str.replace(reg1,'');
		str=str.replace(reg2,'');
		return str;
	},
	//输入的必须为数字（包括小数）
	checkout_decimal : function(obj){
		 //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	},
	//输入的必须为正整数
	checkout_number : function(obj){
		 //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d]/g,"");
	},
	add_custom : function() {
		var sText = $('#cshop_addproduct_custom_text').val();
		var sValue = $('#cshop_addproduct_custom_value').val();
		if (sText == "") {
			zapjs.f.message('自定义属性名称不能为空');
			return false;
		}
		if (sValue == "") {
			zapjs.f.message('自定义属性内容不能为空');
			return false;
		}
		var ts = document.getElementById("move").getElementsByTagName("tr");
		var count = ts.length;
		if(count<=2){
			$('#cshop_addproduct_custom table tbody').append(
					'<tr><td><input class="c_text" value="' + sText
					+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
					+ '">' + sValue+ '</textarea></td>'
					+'<td><input type="button" class="btn" onclick="cshop_product.delete_custom(this)" value="删除"/></td>'
					+'<td><a href="javascript:void(0)" onclick="cshop_product.move_down(this)">下移</a></td></tr>');
		}else{
			$('#cshop_addproduct_custom table tbody').append(
					'<tr><td><input class="c_text" value="' + sText
					+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
					+ '">' + sValue+ '</textarea></td>'
					+'<td><input type="button" class="btn" onclick="cshop_product.delete_custom(this)" value="删除"/></td>'
					+'<td><a href="javascript:void(0);" onclick="cshop_product.move_up(this)">上移</a>&nbsp&nbsp&nbsp&nbsp<a href="javascript:void(0);" onclick="cshop_product.move_down(this)">下移</a></td></tr>');
		}
		cshop_product.refurbish_lastTd();
		$('#cshop_addproduct_custom_text').val('');
		$('#cshop_addproduct_custom_value').val('');
	},
	refurbish_lastTd : function(){
		if ($("#move tr").length>2) {
			$('#cshop_addproduct_custom table tfoot tr td:last').html('<a href="javascript:void(0);" onclick="cshop_product.move_up_add(this)">上移</a>');
		}else{
			$('#cshop_addproduct_custom table tfoot tr td:last').html('无');
		}
	},
	delete_custom : function(trNow){
		  $(trNow).parent().parent().remove(); 
		  cshop_product.refurbish_lastTd();
	},
	
	//上移
	move_up : function(trNow){//直接交换两个控件中的值，不移动tr
		var tr = $(trNow).parents("tr");
		var values = tr.children();
		var sText = values[0].getElementsByTagName("input")[0].value;
		var sValue = values[1].getElementsByTagName("textarea")[0].value;
		
		var prev = tr.prev();
		var prevValues = prev.children();
		var prevText = prevValues[0].getElementsByTagName("input")[0].value;
		var prevValue = prevValues[1].getElementsByTagName("textarea")[0].value;			
		
		values[0].getElementsByTagName("input")[0].setAttribute("value",prevText);
		values[1].getElementsByTagName("textarea")[0].innerHTML=prevValue;
		
		prevValues[0].getElementsByTagName("input")[0].setAttribute("value",sText);
		prevValues[1].getElementsByTagName("textarea")[0].innerHTML=sValue;
	},
	
	//下移
	move_down : function(trNow){//直接交换两个控件中的值，不移动tr
		var tr = $(trNow).parents("tr");
		var values = tr.children();
		var sText = values[0].getElementsByTagName("input")[0].value;
		var sValue = values[1].getElementsByTagName("textarea")[0].value;
		
		if(tr.next().length==0){
			var moveText = $('#cshop_addproduct_custom_text').val();
			var moveValue = $('#cshop_addproduct_custom_value').val();
			$('#cshop_addproduct_custom_text').val(sText);
			$('#cshop_addproduct_custom_value').val(sValue);
			
			values[0].getElementsByTagName("input")[0].setAttribute("value",moveText);
			values[1].getElementsByTagName("textarea")[0].innerHTML=moveValue;
		}else{
			var next = tr.next();
			var nextValues = next.children();
			var nextText = nextValues[0].getElementsByTagName("input")[0].value;
			var nextValue = nextValues[1].getElementsByTagName("textarea")[0].value;
			
			values[0].getElementsByTagName("input")[0].setAttribute("value",nextText);
			values[1].getElementsByTagName("textarea")[0].innerHTML=nextValue;
			
			nextValues[0].getElementsByTagName("input")[0].setAttribute("value",sText);
			nextValues[1].getElementsByTagName("textarea")[0].innerHTML=sValue;
		}
//		var tr = $(trNow).parents("tr");
//		tr.next().after(tr);
	},
	
	//最后一行的上移
	move_up_add :function(trNow){
		var tr = $(trNow).parents("tr");
		var values = tr.children();
		var sText = values[0].getElementsByTagName("input")[0].value;
		var sValue = values[1].getElementsByTagName("textarea")[0].value;
		
		var moveValues=null;
		var ts = document.getElementById("move").getElementsByTagName("tr");
		for(var i=0;i<ts.length;i++){
			if(ts[i].rowIndex==(ts.length-2)){
				moveValues = $('#cshop_addproduct_custom table tbody tr:eq('+(i-1)+')').children();
			}
		}
		var moveText = moveValues[0].getElementsByTagName("input")[0].value;
		var moveValue = moveValues[1].getElementsByTagName("textarea")[0].value;
		
		$('#cshop_addproduct_custom_text').val(moveText);
		$('#cshop_addproduct_custom_value').val(moveValue);
		
		moveValues[0].getElementsByTagName("input")[0].setAttribute("value",sText);
		moveValues[1].getElementsByTagName("textarea")[0].innerHTML=sValue;
	},
	onEditCategoryClick : function() {
		zapadmin.window_url('../show/page_ucategory_v_pc_productinfo?zw_f_category_codes='+$('#zw_f_listbox').val());
	},
	showMd: function(){
		$("#MdTable").css("display","block");
	},
	confirmMd: function(sId){
		var radioValue = $('input[name="mcheck"]:checked').val();
		if(radioValue == undefined){
			zapjs.f.message("请选择MD");
			return false;
		}
		if(confirm('确认提交给'+'"'+$("#m"+radioValue).text()+'"')){
			zapjs.f.window_close(sId);
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',
					cshop_product.beforesubmit);
		}
	},
	func_confirm: function(sId) {
		var radioValue = $('input[name="mcheck"]:checked').val();
		if(radioValue == undefined){
			zapjs.f.message("请选择MD");
			return false;
		}
		var tip = '确认提交给'+'"'+$("#m"+radioValue).text()+'"';
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">'+tip+'</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					zapjs.f.window_close(sId);
					$("#approveBtnSubmit").click();//提交修改
				}
		},{
			text : '否',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
				}
		});
		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
	},
	show_windows : function(sId){
		var MdContent = $("#MdTable").html();
		zapjs.f.window_box({
			id : sId,
			content : MdContent,
			width : '315',
			height : '330',
			title : '选择MD'
		});
	},
	radioClick : function(rd){
		$("#mdId").val($(rd).val());
		$("#mdName").val($("#m"+$(rd).val()).text());
	},
	uniqueArrSkuKey : function(arr){
		 var res = [];
		 var json = {};
		 for(var i = 0; i < arr.length; i++){
		  if(!json[arr[i].code]){
		   res.push({"code":arr[i].code,"value":arr[i].value,"colorPic":arr[i].colorPic});
		   json[arr[i].code] = 1;
		  }
		 }
		 return res;
	},
	approveBtnClick : function (){
		var bFlag = true;
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_piclist', '469923180002')) {
				bFlag = false;
			}
		}
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_brand', '469923180002', '商品品牌')) {
				bFlag = false;
			}
		}
//		if (bFlag) {
//			if (!zapjs.zw.validate_field('#zw_f_editor', '469923180002', '商品描述')) {
//				bFlag = false;
//			}
//		}
		if (bFlag) {
			if (!zapjs.zw.validate_field('#zw_f_description_pic', '469923180002', '描述图片')) {
				bFlag = false;
			}
		}

		// 开始处理运费策略
		if (bFlag) {
			var sVal = $(
					'#cshop_addproduct_travel input:radio[name=zw_f_tra_select]:checked')
					.val();
			if (sVal == 0) {
				$('#zw_f_traval').val(0);
			} else if (sVal == 1) {

				if (!zapjs.zw.validate_field('#zw_f_tra_user', '469923180007',
						'买家承担运费')) {

					bFlag = false;
				} else {
					$('#zw_f_traval').val($('#zw_f_tra_user').val());
				}
			} else if (sVal == 2) {

			} else {
				zapjs.f.message('请选择运费模式！');
				bFlag = false;
			}

		}
		
		//遍历基本属性
		//第一个ul为颜色属性
		var bFlagColorChecked = 0;
		$("#cshop_addproduct_prop ul:first input[id^='cshop_addproduct_prop_']").each(function(){
			if ($(this).is(':checked')) {
				bFlagColorChecked = 1
				return false;
			}
		});
		//第二个ul为款式属性
		var bFlagStyleChecked = 0;
		$("#cshop_addproduct_prop ul:last input[id^='cshop_addproduct_prop_']").each(function(){
			if ($(this).is(':checked')) {
				bFlagStyleChecked = 1
				return false;
			}
		});
		if (bFlagColorChecked == 0 && bFlagStyleChecked == 0) {
			zapjs.f.message("您必须至少维护一个sku，请勾选sku基本属性！");
			return false;
		}else if (bFlagColorChecked == 1 && bFlagStyleChecked == 0) {
			zapjs.f.message("您没有选择sku基本属性中的款式！");
			return false;
		}else if (bFlagColorChecked == 0 && bFlagStyleChecked == 1) {
			zapjs.f.message("您没有选择sku基本属性中的颜色！");
			return false;
		}
		
		if (bFlag) {
			cshop_product.show_windows('mdFrame');
		}
		return bFlag;
	},
	saveDraftBoxBtnClick : function (){
		
		$("form input").each(function(){
			$(this).removeAttr("zapweb_attr_regex_id");
		});
		$("#draftBoxBtnSubmit").click();
	},
	
	show_group_operation : function(nowTr){
		event.stopPropagation();//阻止事件冒泡
		$('#group_operation').toggle();//再点击设置按钮变为不可见
		var init_post = $(nowTr).offset().top;
		var init_posl = $(nowTr).offset().left;
		var init_div =$("#group_operation");
		init_div.css({"top":init_post-init_div.height()+160+'px',"left":init_posl-(init_div.width()/2)-100+'px'});
		var tr = $(nowTr).parents("tr");
		var tds = tr.children();
		cshop_product.temp.copy_tr.vPrice=tr.find(".c_price:first").val();
		cshop_product.temp.copy_tr.vNum=tr.find(".c_stock:first").val();
		cshop_product.temp.copy_tr.color = tds[0].innerText;
		cshop_product.temp.copy_tr.size = tds[1].innerText;
		if(cshop_product.temp.copy_tr.vPrice==""){
			$('#batch-price-0').prop("disabled",true);
			$('#batch-price-1').prop("disabled",true);
		}else{
			$('#batch-price-0').prop("disabled",false);
			$('#batch-price-1').prop("disabled",false);
		}
		if(cshop_product.temp.copy_tr.vNum==""){
			$('#batch-quantity-0').prop("disabled",true);
			$('#batch-quantity-1').prop("disabled",true);
		}else{
			$('#batch-quantity-0').prop("disabled",false);
			$('#batch-quantity-1').prop("disabled",false);
		}
	},
	
	onclick_ok : function(){
		//同颜色价格相同
		if($("#batch-price-0").is(":checked")){
			var colors = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				colors[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[0].innerText;
			}
			for(var i=0;i<colors.length;i++){
				if(cshop_product.temp.copy_tr.color==colors[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_price:first").val(cshop_product.temp.copy_tr.vPrice);
				}
			}
		}
		
		//同款式价格相同
		if($("#batch-price-1").is(":checked")){
			var size = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				size[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[1].innerText;
			}
			for(var i=0;i<size.length;i++){
				if(cshop_product.temp.copy_tr.size==size[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_price:first").val(cshop_product.temp.copy_tr.vPrice);
				}
			}
		}
		
		//同颜色库存相同
		if($("#batch-quantity-0").is(":checked")){
			var colors = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				colors[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[0].innerText;
			}
			for(var i=0;i<colors.length;i++){
				if(cshop_product.temp.copy_tr.color==colors[i]){
					
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_stock:first").val(cshop_product.temp.copy_tr.vNum);
				}
			}
		}
		
		//同款式库存相同
		if($("#batch-quantity-1").is(":checked")){
			var size = new Array();
			for(var i=0;i<$("#cshop_addproduct_plist table tbody").find("tr").length;i++){
				size[i] = $("#cshop_addproduct_plist table tbody tr:eq("+i+")").find("td")[1].innerText;
			}
			for(var i=0;i<size.length;i++){
				if(cshop_product.temp.copy_tr.size==size[i]){
					$("#cshop_addproduct_plist table tbody tr:eq("+i+")").find(".c_stock:first").val(cshop_product.temp.copy_tr.vNum);
				}
			}
		}
		//遍历所有的行，如果复制了之后有了数据则按钮变亮，复制库存
		$("#cshop_addproduct_plist table tbody .c_stock").each(function(){
			cshop_product.c_price_onkeyup(this);
		});
		//遍历所有的行，如果复制了之后有了数据则按钮变亮，复制价格
		$("#cshop_addproduct_plist table tbody .c_price").each(function(){
			cshop_product.c_price_onkeyup(this);
		});
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	
	onclick_cancle : function(){
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	
	onclick_x : function(){
		document.getElementById("group_operation").style.display="none";
		$(".batch-radio").prop("checked",false);
	},
	//当输入售价的输入框时校验
	c_price_onkeyup : function(text){
		var price = $(text).val();
		var stock = $(text).parents("tr").find(".c_stock:first").val();
		if(price=="" && stock==""){
			$(text).parents("tr").find(".sku-batch:first").removeAttr("onclick");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch");
		}else{
			$(text).parents("tr").find(".sku-batch:first").attr("onclick","cshop_product.show_group_operation(this)");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch sku-batch-enable");
		}
	},
	//当输入库存的输入框时校验
	c_stock_onkeyup : function(text){
		var stock = $(text).val();
		var price = $(text).parents("tr").find(".c_price:first").val();
		if(price=="" && stock==""){
			$(text).parents("tr").find(".sku-batch:first").removeAttr("onclick");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch");
		}else{
			$(text).parents("tr").find(".sku-batch:first").attr("onclick","cshop_product.show_group_operation(this)");
			$(text).parents("tr").find(".sku-batch:first").attr("class","sku-batch sku-batch-enable");
		}
	},
	deleteVideo : function(){
		$("#zw_f_product_desc_video").val("");//删除链接。
		//隐藏掉视频播放器
		$(".video_hidden").attr("style","display:none");
	}

};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/editNewProduct", [ "zapweb/js/zapweb_upload","zapweb/js/zapweb_upload_modify",
			"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
		return cshop_product;
	});
}

//改变窗口大小时，弹出的批量设置隐藏
$(window).resize(function(){
	document.getElementById("group_operation").style.display="none";
});

//点击弹出的批量设置以外的地方时隐藏弹出层
$("body").click(function(e){
	var e = e || window.event; //浏览器兼容性 
	var elem = e.target || e.srcElement; 
	while (elem) { //循环判断至跟节点，防止点击的是div子元素 
	if (elem.id && elem.id=='group_operation') { 
	return; 
	} 
	elem = elem.parentNode; 
	} 

	$('#group_operation').css('display','none');
	$(".batch-radio").prop("checked",false);
});

$(document).ready(function(){
	$("#zw_f_listbox").change(function(){
		cshop_product.set_category();//设置分类属性
	});
	$(".ico-helpw .ico-fques").mouseover(function(){
		$(this).attr("class","poptip-attention poptip-attention-active");
		var init_post = $(this).offset().top;
		var init_posl = $(this).offset().left;
		var init_div =$(this).parent('.ico-helpw').find('.help-pop');
		init_div.css({"top":init_post-init_div.height()+'px',"left":init_posl-(init_div.width()/2)+4+'px',"display":"block"});
	});
	$(".ico-helpw .ico-fques").mouseout(function(){
		$(this).attr("class","poptip-attention");
		$(this).parent('.ico-helpw').find('.help-pop').css('display','none');
	});
});
