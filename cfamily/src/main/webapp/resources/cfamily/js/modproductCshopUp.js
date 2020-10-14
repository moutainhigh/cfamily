var cfamily_modproduct = {
	product : {},
	defaultuid : "",
	temp : {
		// 分类属性
		categoryproperty : [],
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
		//已选择的sku
		CheckedSkuCode:[],
	},
	// 初始化
	init_product : function(p) {
		cfamily_modproduct.product = p;
		cfamily_modproduct.move_elm();
		cfamily_modproduct.SecondStep();
		$('#zw_f_qualification_category_code').change(cfamily_modproduct.onCheckQualification);
		
		//*成本价格
//		document.getElementById("zw_f_cost_price").setAttribute("onkeyup","cfamily_modproduct.checkNum(this)");
		//*市场价格
		document.getElementById("zw_f_market_price").setAttribute("onkeyup","cfamily_modproduct.checkNum(this)");
		//*商品重量
		document.getElementById("zw_f_product_weight").setAttribute("onkeyup","cfamily_modproduct.checkNumWeight(this)");
		//商品体积
		document.getElementById("zw_f_product_volume").setAttribute("onkeyup","cfamily_modproduct.checkNumVolume(this)");
		

//		if ("SI2003" == cfamily_modproduct.product.smallSellerCode || "SI3003" == cfamily_modproduct.product.smallSellerCode) {
//			$("#zw_f_purchase_type").val("4497471600160002");	//经销
//			$("#zw_f_settlement_type").val("4497471600110002");	//特殊结算
//			
//		}else if (cfamily_modproduct.product.smallSellerCode.indexOf('SF03') == 0 && "SF03KJT" != cfamily_modproduct.product.smallSellerCode) {
//			$("#zw_f_purchase_type").val("4497471600160001");	//代销
//			$("#zw_f_settlement_type").val("4497471600110001");	//常规结算
//		}else if ("SF03KJT" == cfamily_modproduct.product.smallSellerCode) {
//			$("#zw_f_purchase_type").val("4497471600160001");	//代销
//			$("#zw_f_purchase_type").parent().parent().hide();
//			$("#zw_f_settlement_type").val("4497471600110003");	//服务费结算
//		}
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
		
		// 海外购分类下的商品可以设置贸易类型
		if(p.usprList && categoryHWG != '' && p.usprList[0].categoryCode.indexOf(categoryHWG) == 0){
			$('#zw_f_product_trade_type').parents('.control-group').removeClass('hide');
			$('#zw_f_product_trade_type').val(cfamily_modproduct.product.pcProductinfoExt.productTradeType);
		}
		$("#zw_f_market_price").prop("disabled",true);
		$("#zw_f_qualification_category_code").prop("disabled",true);
		$("#zw_f_product_weight").prop("disabled",true);
		$("#zw_f_product_volume").prop("disabled",true);
		$("#zw_f_expiry_date").prop("disabled",true);
		$("#zw_f_expiry_unit").prop("disabled",true);
		//$("#zw_f_fictitious_sales").prop("disabled",true); 
		//$("#zw_f_authority_logo_18").prop("disabled",true);
		$("#zw_f_volumn_length").prop("disabled",true);
		$("#zw_f_volumn_width").prop("disabled",true);
		$("#zw_f_volumn_high").prop("disabled",true);
		
		
	},
	SecondStep : function() {
			$("#catogoryDivInfo").hide();
			$("#OtherProductInfo").show();
			$(".zab_page_default_header_title").html(
					"商品管理 > 商品修改");
			cfamily_modproduct.show_edit();
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
		$.each(cfamily_modproduct.product.qualification,function(i,data){
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

	move_elm : function() {
		$('#zw_f_elm_description').hide();
		$('#cfamily_modifyproduct_insert_prop').insertAfter('#zw_f_elm_description');
		$('#zw_f_elm_brand').hide();
		$('#zw_f_elm_brand').attr("zapweb_attr_regex_id","");
		$('#zw_f_elm_brand').after('<select id="zw_f_brand"><option value="">请选择</option></select>');
		// 售后地址下拉框
		$('#zw_f_after_sale_address_uid').hide();
		$('#zw_f_after_sale_address_uid').attr("zapweb_attr_regex_id","");
		$('#zw_f_after_sale_address_uid').after('<select id="zw_f_after_sale_address"><option value="">请选择</option></select>');
		
		$('#zw_f_traval').hide();
		$('#cfamily_addproduct_travel').insertAfter('#zw_f_traval');
		$("#zw_f_brand").change(cfamily_modproduct.onCheckQualification);
	},

	// 显示修改信息
	show_edit : function() {
//		if(cfamily_modproduct.product.productCodeOld==null||cfamily_modproduct.product.productCodeOld==''){
			cfamily_modproduct.up_prop_list();
//		}else{
//			$("#cfamily_addproduct_custom").parent().parent().parent().hide();
//		}
		zapjs.zw.api_call('com_cmall_productcenter_service_ApiGetSellBrandForSpecial', {
		}, cfamily_modproduct.up_brand_info);
		// 查询商户下的售后地址
		var obj1 = {};
		obj1.manageCode = cfamily_modproduct.product.smallSellerCode;
		obj1.afterSaleAddressUid = '';
		zapjs.zw.api_call('com_cmall_productcenter_service_ApiGetPartAfterSaleAddress', obj1, cfamily_modproduct.up_after_sale_address);
		
		cfamily_modproduct.set_wvp();
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',cfamily_modproduct.beforesubmit);
		var ss=$('#zw_f_keyword').parent()[0].innerHTML;
		ss=ss.replace('zapadmin_single.show_box(\'zw_f_keyword\')','cfamily_modproduct.show_box(\'zw_f_keyword\')');
		$('#zw_f_keyword').parent()[0].innerHTML=ss;
		document.getElementById("zw_f_market_price").setAttribute("onkeyup","cfamily_modproduct.checkout_decimal(this)");//市场价
		cfamily_modproduct.set_value();
		//商品扩展信息为空时商品是有问题的,这句话不影响正常的修改功能，只是给个提示
		if(cfamily_modproduct.product.pcProductinfoExt==null||cfamily_modproduct.product.pcProductinfoExt.productCode==''){
			zapjs.f.message('商品'+cfamily_modproduct.product.productCode+'扩展信息为空，无法对"虚拟销售量基数"进行修改，如果无须操作此字段请忽略此消息继续操作，请复制此消息给技术！');
//			return false;
		}
	},
	showdefault:function(trans){
		var uuid ={};
		if(cfamily_modproduct.defaultuid.length > 0){
			uuid.uid = cfamily_modproduct.defaultuid;
		}else{
			uuid.uid = trans;
		}
		zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
			var freightName = result.tplName;
			eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (uuid.uid) +'","'+ freightName +'")' );
		});
	},
	set_value:function(){//设置值
		$('#zw_f_product_name').val(cfamily_modproduct.product.produtName);//商品名称
		$('#zw_f_market_price').val(cfamily_modproduct.product.marketPrice);//商场价
		$('#zw_f_product_shortname').val(cfamily_modproduct.product.productShortname);//商品简称
//		$('#zw_f_product_shortname').attr({ readonly: 'true' });
//		$('#zw_f_cost_price').val(cfamily_modproduct.product.costPrice);//成本价
		$('#zw_f_tax_rate').val(cfamily_modproduct.product.taxRate);//税率
		
		var stype = cfamily_modproduct.product.sellerType;
		if( stype != '4497478100050002' && stype != '4497478100050003' ){
			$('#zw_f_tax_rate_show').val(cfamily_modproduct.product.taxRate);//税率
			$('#zw_f_tax_rate_show').attr({ disabled: 'true' });
		}else{
			$($('#zw_f_tax_rate_show')[0].parentNode.parentNode).remove(); 
		}
		
		var videoUrlShow = cfamily_modproduct.product.videoUrlShow;
		debugger;
		if(videoUrlShow != ""){
			$("#my-video").attr("poster",cfamily_modproduct.product.videoMainPic);
			document.getElementById("product_desc_video").src = cfamily_modproduct.product.videoUrlShow;
		}else{
			$(".video_hidden").attr("style","display:none");
		}
		
		$('#zw_f_video_main_pic').val(cfamily_modproduct.product.videoMainPic);//商品视频链接
		$('#zw_f_product_desc_video').val(cfamily_modproduct.product.productDescVideo);//商品视频链接
		$('#zw_f_video_url').val(cfamily_modproduct.product.videoUrl);//商品视频链接
		zapweb_upload.upload_file('zw_f_video_main_pic',cfamily_modproduct.product.videoMainPic);//商品视频主图
		$('#zw_f_supplier_name').val(cfamily_modproduct.product.supplierName);//供应商名称			已隐藏
		$('#zw_f_fictitious_sales').val(cfamily_modproduct.product.pcProductinfoExt.fictitiousSales);//虚拟销售量
		//添加编辑不显示mainpic_url字段 2016-08-10 zhy
		//$('#zw_f_mainpic_url').val(cfamily_modproduct.product.mainPicUrl);//商品主图片
		//zapweb_upload.upload_file('zw_f_mainpic_url',cfamily_modproduct.product.mainPicUrl);
		
		$('#zw_f_adpic_url').val(cfamily_modproduct.product.adPicUrl);//商品广告图
		zapweb_upload.upload_file('zw_f_adpic_url',cfamily_modproduct.product.adPicUrl);
		$('#zw_f_product_adv').val(cfamily_modproduct.product.productAdv);//商品广告语
		$('#zw_f_labels').val(cfamily_modproduct.product.labels);//关键字
		
		//显示自定义属性、扩展属性
//		for(var props in cfamily_modproduct.product.pcProductpropertyList){
//			if(cfamily_modproduct.product.pcProductpropertyList[props]['propertyType']=='449736200004'){
//				$('#cfamily_addproduct_custom table tbody').prepend(
//						'<tr><td><input class="c_text" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyKey']
//						+ '"/></td><td><textarea rows="2" class="c_value" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']
//						+ '">' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']+'</textarea></td><td></td></tr>');
//				$('#cfamily_addproduct_custom_text').val('');
//				$('#cfamily_addproduct_custom_value').val('');
//				continue;
//			}else if(cfamily_modproduct.product.pcProductpropertyList[props]['propertyType']=='449736200003'){
//				$('#cfamily_addproduct_extend_'+cfamily_modproduct.product.pcProductpropertyList[props]['propertyKeycode']).val(cfamily_modproduct.product.pcProductpropertyList[props]['propertyCode']);
//				continue;
//			}
//		}
		
		var picUrls='';
		for(var i in cfamily_modproduct.product.pcPicList){
			if(cfamily_modproduct.product.pcPicList[i].skuCode == "" || cfamily_modproduct.product.pcPicList[i].skuCode == null){
				if(i==0){
					picUrls=cfamily_modproduct.product.pcPicList[i].picUrl;
				}else{
					picUrls=picUrls+'|'+cfamily_modproduct.product.pcPicList[i].picUrl;
				}
			}
		}
		
		$('#zw_f_piclist').val(picUrls);//商品图片
		zapweb_upload.upload_file('zw_f_piclist',picUrls);

		$('#zw_f_editor').val(cfamily_modproduct.product.description.descriptionInfo);//商品描述
//		zapweb_upload.upload_file('zw_f_editor',cfamily_modproduct.product.description.descriptionInfo);
		$('#zw_f_description_pic').val(cfamily_modproduct.product.description.descriptionPic);//描述图片
		zapweb_upload_modify.upload_file('zw_f_description_pic',cfamily_modproduct.product.description.descriptionPic);
		
		$('#zw_f_product_weight').val(cfamily_modproduct.product.productWeight);//商品重量
		$('#zw_f_product_volume').val(cfamily_modproduct.product.productVolume);//商品体积
		$('#zw_f_volumn_length').val(cfamily_modproduct.product.productVolumeItem.split(',')[0]);//长
		$('#zw_f_volumn_width').val(cfamily_modproduct.product.productVolumeItem.split(',')[1]);//宽
		$('#zw_f_volumn_high').val(cfamily_modproduct.product.productVolumeItem.split(',')[2]);//高
		
		$('#zw_f_purchase_type').val(cfamily_modproduct.product.pcProductinfoExt.purchaseType);//采购类型
		$('#zw_f_settlement_type').val(cfamily_modproduct.product.pcProductinfoExt.settlementType);//结算方式
		
		$('#zw_f_expiry_date').val(cfamily_modproduct.product.expiryDate);//保质期
		$('#zw_f_expiry_unit').val(cfamily_modproduct.product.expiryUnit);//保质期单位
		
		$('#zw_f_qualification_category_code').val(cfamily_modproduct.product.qualificationCategoryCode);//保质期单位
		
		var trans = cfamily_modproduct.product.transportTemplate;
		if(trans!=null&&trans!=""&&trans=="0"){//运费模板
			$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(0)").attr("checked",'checked'); 
			cfamily_modproduct.showdefault(trans);
		}else if(/^\d+$/.test(trans)) {
			$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(1)").attr("checked",'checked'); 
			$('#zw_f_tra_user').val(trans);
			cfamily_modproduct.showdefault(trans);
		}else{
			var uuid ={};
			uuid.uid = trans;
			if(trans.length > 0){
				uuid.uid = trans;
			}else{
				if(cfamily_modproduct.defaultuid.length > 0){
					uuid.uid = cfamily_modproduct.defaultuid;
				}else{
					uuid.uid = trans;
				}
			}
			zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
				var freightName = result.tplName;
				$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(2)").attr("checked",'checked');
				eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (uuid.uid) +'","'+ freightName +'")' );
			});
		}
		var area = cfamily_modproduct.product.areaTemplate;
		if(area!=null && area!=""){
			var code = {};
			code.templateCode = area;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_getTemplateNameApi',code,function(result){
				var templateName = result.templateName;
				eval('parent.zapadmin_single.result'+'("zw_f_area_template","'+(area)+'","'+templateName+'")');
			});
		}

		$('#zw_f_sell_productcode').val(cfamily_modproduct.product.sellProductcode);//货号
		$('#zw_f_flag_payway').val(cfamily_modproduct.product.flagPayway);//是否货到付款
		
		if(cfamily_modproduct.product.description.keyword!=null&&cfamily_modproduct.product.description.keyword!=''){
			var labelsStr = cfamily_modproduct.product.description.keyword.replace(/,/g,' '); 				//将标签之间的逗号替换为空格
			$('#zw_f_keyword').val(labelsStr);//标签显示
		}
		
		$('#zw_f_pic_material_url').val(cfamily_modproduct.product.pcProductinfoExt.picMaterialUrl);//图片相关素材地址
		$('#zw_f_pic_material_upload').val(cfamily_modproduct.product.pcProductinfoExt.picMaterialUpload);//图片相关素材上传
		zapweb_upload.upload_file('zw_f_pic_material_upload',cfamily_modproduct.product.pcProductinfoExt.picMaterialUpload);
	},
	
	show_box : function(sId) {
		var s=zapadmin_single.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'zapadmin_single_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'&category_code='+cfamily_modproduct.product.category.categoryCode+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	
	// 提交前处理一些特殊的验证模式
	beforesubmit : function() {
		var bFlag = true;

		var address = $("#zw_f_after_sale_address").val();
		if(null != address && address != ""){
			$("#zw_f_after_sale_address_uid").val(address);
		}/*else{
			zapjs.f.message('*售后地址：不能为空');
			return false;
		}*/
		
		//*商品标签
		var zw_f_keyword = $("#zw_f_keyword").val();
		//*市场价格
		var zw_f_market_price = $("#zw_f_market_price").val();
		//*商品重量
		var zw_f_product_weight = $("#zw_f_product_weight").val();
		//商品体积
		var zw_f_product_volume = $("#zw_f_product_volume").val();
		//*关键字
		var zw_f_labels = $("#zw_f_labels").val();
		//*虚拟销量
		var zw_f_fictitious_sales = $("#zw_f_fictitious_sales").val();
		
		//*保质期
		var zw_f_expiry_date = $("#zw_f_expiry_date").val();
		//标签总长度不能超过10个字
//		if (zw_f_keyword.replace(/\s+/g,'').length > 10) {
//			zapjs.f.message("*商品标签：值过长，不符合规则，标签总长度不能超过10个字符");
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
		if (zw_f_labels.length>40) {
			zapjs.f.message('关键字：长度超过限制，最长为40个字符');
			return false;
		};
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
		
		if (bFlag) {
			if ($('#zw_f_product_name').val().length>40) {
				zapjs.f.message('商品名称：长度超过限制，最长为40个字符');
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
//		if (bFlag) {
//			if ($('#zw_f_product_shortname').val().length>200) {
//				zapjs.f.message('商品简称：长度超过限制，请重新填写！');
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
					'#cfamily_addproduct_travel input:radio[name=zw_f_tra_select]:checked')
					.val();
			if (sVal == 0) {
				$('#zw_f_traval').val(0);
			} else if (sVal == 1) {
				if (!zapjs.zw.validate_field('#zw_f_tra_user', '469923180007','买家承担运费')) {
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
		
		// 如果贸易类型被展示处理则必须选择一个值
		if($('#zw_f_product_trade_type').parents('.control-group').eq(0).hasClass('hide') == false){
			if($('#zw_f_product_trade_type').val() == ''){
				zapjs.f.message('请选择贸易类型！');
				return false;
			}
		}
		
		/*if(bFlag){
			$('#cfamily_addproduct_properties table tbody tr').each(function(n, el) {
				var is_must = $(el).find('.zw_f_ppr_is_must').val();// 是否必填
				var properties_value = $(el).find('.ppr_properties_value_code').val();
				var properties_code = $(el).find('.ppr_properties_value_code').attr("id").split("+")[1];
				if(is_must == "449747110002"){ // 必填
					if(!properties_value || properties_value == ""){
						zapjs.f.message('请填写必填商品属性！');
						bFlag = false;
					}
				}
				
			});
		}*/
		
		if (bFlag) {
			debugger;
			$("#zw_f_market_price").prop("disabled",false);
			$("#zw_f_qualification_category_code").prop("disabled",false);
			$("#zw_f_product_weight").prop("disabled",false);
			$("#zw_f_product_volume").prop("disabled",false);
			$("#zw_f_expiry_date").prop("disabled",false);
			$("#zw_f_expiry_unit").prop("disabled",false);
			$("#zw_f_fictitious_sales").prop("disabled",false);
			//$("#zw_f_authority_logo_18").prop("disabled",false);
			$("#zw_f_volumn_length").prop("disabled",false);
			$("#zw_f_volumn_width").prop("disabled",false);
			$("#zw_f_volumn_high").prop("disabled",false);
			CKEDITOR.config.readOnly = false;
			cfamily_modproduct.recheck_product();
			$('#zw_f_json').val(zapjs.f.tojson(cfamily_modproduct.product));
		}
		return bFlag;
	},
	// 重新处理格式化商品信息
	recheck_product : function() {
		var thisproduct = cfamily_modproduct.product;
		
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
		thisproduct.labels=$('#zw_f_labels').val();
		thisproduct.productShortname = $('#zw_f_product_shortname').val();//商品简称
//		thisproduct.costPrice = $('#zw_f_cost_price').val();//成本价
		thisproduct.productDescVideo = $('#zw_f_product_desc_video').val();//商品视频链接
		thisproduct.videoMainPic = $('#zw_f_video_main_pic').val();//商品视频链接
		thisproduct.videoUrl = $('#zw_f_video_url').val();//商品视频链接
		thisproduct.supplierName = $('#zw_f_supplier_name').val();//供应商名称
		thisproduct.taxRate = $('#zw_f_tax_rate').val();	//税率
		// zw_f_brand
		thisproduct.marketPrice = $('#zw_f_market_price').val();
		thisproduct.brandCode = $('#zw_f_brand').val();
		thisproduct.afterSaleAddressUid = $('#zw_f_after_sale_address_uid').val();//售后地址
		thisproduct.productVolume = $('#zw_f_product_volume').val();
		thisproduct.productWeight = $('#zw_f_product_weight').val();
		thisproduct.sellProductcode = $('#zw_f_sell_productcode').val();
		thisproduct.transportTemplate = $('#zw_f_traval').val();
		thisproduct.areaTemplate = $('#zw_f_area_template').val();
		// 是否货到付款 
		thisproduct.flagPayway=$('#zw_f_flag_payway').val();
		//长宽高
		var length = $('#zw_f_volumn_length').val().replace(/^\s+|\s+$/g, '');
		var width = $('#zw_f_volumn_width').val().replace(/^\s+|\s+$/g, '');
		var high = $('#zw_f_volumn_high').val().replace(/^\s+|\s+$/g, '');
		thisproduct.productVolumeItem=length+','+width+','+high;
		
		thisproduct.expiryDate = $('#zw_f_expiry_date').val();//商品保质期
		thisproduct.expiryUnit = $('#zw_f_expiry_unit').val();//商品保质期单位
		thisproduct.qualificationCategoryCode=$('#zw_f_qualification_category_code').val();//保质期单位
		
		// 分类信息
		thisproduct.category = {categoryCode : $('#zw_f_listbox').val()};
		// 商品图片
		thisproduct.pcPicList = [];
		var sPicList = $('#zw_f_piclist').val().split('|');
		for ( var i in sPicList) {
			thisproduct.pcPicList.push({picUrl : sPicList[i]});
		}
//		if (sPicList.length > 0) {
			//添加编辑不显示mainpic_url字段 2016-08-10 zhy
			//thisproduct.mainPicUrl =$('#zw_f_mainpic_url').val();
//		}
		thisproduct.adPicUrl =$('#zw_f_adpic_url').val();
		thisproduct.productAdv = $('#zw_f_product_adv').val();
//		cfamily_modproduct.recheck_prop();
		// SKU信息
		thisproduct.productSkuInfoList = [];
		// 商品属性
		thisproduct.pcProductpropertyList = [];
		for ( var p in cfamily_modproduct.temp.p_key) {
			var oProp = cfamily_modproduct.temp.p_key[p];
			if (oProp["flag"] == "1") {
				var othis = {
					sellPrice : oProp["price"],
					stockNum : oProp["stock"],
					marketPrice : $('#zw_f_market_price').val(),
					skuKey : oProp["sku_key"],
					skuValue : oProp["sku_value"],
					skuPicUrl : '',
					skuAdv : oProp["skuAdv"],
					skuCode:oProp["skuCode"],
					securityStockNum : oProp["securityStockNum"],
					sellProductcode : oProp["sellProductcode"],
					skuName : thisproduct.produtName + '  ' + oProp["sku_name"]
				};
				if (oProp["sku_color"] != "") {
					othis.skuPicUrl = cfamily_modproduct.temp.p_color[oProp["sku_color"]]["image"];
				}
				thisproduct.productSkuInfoList.push(othis);
			}
		}
		// 如果没有基本属性则自动添加一条SKU信息
		if (thisproduct.productSkuInfoList.length == 0) {
			var othis = {
				sellPrice : 0,
				stockNum : 0,
				marketPrice : $('#zw_f_market_price').val(),
				skuKey : '',
				skuValue : '',
				skuPicUrl : '',
				skuAdv : '',
				securityStockNum : 1,
				sellProductcode : $('#zw_f_sell_productcode').val(),
				skuName : thisproduct.produtName
			};
			thisproduct.productSkuInfoList.push(othis);
		}
		// 销售属性
		for ( var p in cfamily_modproduct.temp.p_extend) {
			var othis = cfamily_modproduct.up_prop_key(p);
			var sV = $('#cfamily_addproduct_extend_' + p).val();
			if (sV) {
				thisproduct.pcProductpropertyList.push({
							propertyKeycode : othis["property_code"],
							propertyCode : sV,
							propertyKey : othis["property_name"],
							propertyValue : cfamily_modproduct.up_prop_key(sV)["property_name"],
							propertyType : 449736200003
						});
			}
		}
		// 自定义属性
		$('#cfamily_addproduct_custom table tbody tr').each(function(n, el) {
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
		var sTextAdd = $("#cfamily_addproduct_custom_text").val();
		var sValueAdd = $("#cfamily_addproduct_custom_value").val();
		if (sTextAdd && sValueAdd) {
			thisproduct.pcProductpropertyList.push({
				propertyKeycode : '',
				propertyCode : '',
				propertyKey : sTextAdd,
				propertyValue : sValueAdd,
				propertyType : 449736200004
			});
		}
		
		// 商品分类属性
		thisproduct.pprList = [];
		$('#cfamily_addproduct_properties table tbody tr').each(function(n, el) {
			var is_must = $(el).find('.zw_f_ppr_is_must').val();// 是否必填
			var properties_value_type = $(el).find('.zw_f_ppr_properties_value_type').val();// 固定值/自定义
			var properties_value = $(el).find('.ppr_properties_value_code').val();
			var properties_code = $(el).find('.ppr_properties_value_code').attr("id").split("+")[1];
			var properties_value_code = '';
			if(properties_value_type == "449748500001"){							
				properties_value_code = properties_value;
				properties_value = '';
			}
			if (properties_code && (properties_value||properties_value_code)) {
				thisproduct.pprList.push({
					product_code : thisproduct.productCode,
					properties_code : properties_code,
					properties_value_code : properties_value_code,
					properties_value : properties_value							
				});
			}
		});
		
		// 商品描述
		thisproduct.description = {descriptionInfo : $('#zw_f_editor').val(),descriptionPic:$('#zw_f_description_pic').val(),keyword:$('#zw_f_keyword').val()};
		// 商品扩展信息
		thisproduct.pcProductinfoExt = cfamily_modproduct.product.pcProductinfoExt;
		thisproduct.pcProductinfoExt.fictitiousSales = $('#zw_f_fictitious_sales').val();
		thisproduct.pcProductinfoExt.settlementType = $('#zw_f_settlement_type').val();
		thisproduct.pcProductinfoExt.purchaseType = $('#zw_f_purchase_type').val();
		thisproduct.pcProductinfoExt.picMaterialUrl = $('#zw_f_pic_material_url').val();
		thisproduct.pcProductinfoExt.picMaterialUpload = $('#zw_f_pic_material_upload').val();
		thisproduct.pcProductinfoExt.productTradeType = $('#zw_f_product_trade_type').val();
	},

	// 2222222222获取属性列表
	up_prop_list : function() {
		zapjs.zw.api_call('com_cmall_productcenter_process_ApiGetCategoryProperty', {categoryCode : cfamily_modproduct.product.category.categoryCode}, cfamily_modproduct.prop_list_success);
	},
	// 设置商品描述信息
	set_editor_info : function() {
		zapjs.zw.editor_show('zw_f_editor', $('#cfamily_addproduct_uploadurl').val());
	},
	//处理重量、体积、货到付款
	set_wvp : function(){
		var ckg = "<li class=\"w_mt_5\"></li><div  class=\"controls\" id=\"cfamily_addproduct_volume_item\">"
				+"长：<input style=\"width:90px\" onChange=\"cfamily_modproduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_length\" id=\"zw_f_volumn_length\"  value=\"\"/>"
				+"&nbsp;&nbsp;&nbsp;宽：<input style=\"width:90px\" onChange=\"cfamily_modproduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_width\" id=\"zw_f_volumn_width\"  value=\"\"/>"
				+"&nbsp;&nbsp;&nbsp;高：<input style=\"width:90px\" onChange=\"cfamily_modproduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_high\" id=\"zw_f_volumn_high\"  value=\"\"/></div>";
		if(cfamily_modproduct.product.sellerCode!=''&&cfamily_modproduct.product.sellerCode.length!=5){
			$('#zw_f_flag_payway').parent().parent().hide();
		}
		$('#zw_f_product_volume').parent().append('<label id=volumn22 >(单位：立方厘米&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
		$('#zw_f_product_volume').parent().parent().append(ckg);
		$('#zw_f_product_weight').parent().append('<label id=weight22 >(单位:千克&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
		
		document.getElementById("zw_f_product_volume").setAttribute("onkeyup","cfamily_modproduct.checkNum(this)");//体积
		document.getElementById("zw_f_volumn_length").setAttribute("onkeyup","cfamily_modproduct.checkNumLimit3(this)");
		document.getElementById("zw_f_volumn_width").setAttribute("onkeyup","cfamily_modproduct.checkNumLimit3(this)");
		document.getElementById("zw_f_volumn_high").setAttribute("onkeyup","cfamily_modproduct.checkNumLimit3(this)");
		document.getElementById("zw_f_product_volume").setAttribute("onblur","cfamily_modproduct.checkBlur(this)");
		document.getElementById("zw_f_volumn_length").setAttribute("onblur","cfamily_modproduct.checkBlur(this)");
		document.getElementById("zw_f_volumn_width").setAttribute("onblur","cfamily_modproduct.checkBlur(this)");
		document.getElementById("zw_f_volumn_high").setAttribute("onblur","cfamily_modproduct.checkBlur(this)");
		//商品保质期
		document.getElementById("zw_f_expiry_date").setAttribute("onkeyup","cfamily_modproduct.checkout_number(this)");
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
				cfamily_modproduct.get_vloumn();
			}
		}
	},
	//输入的必须为正整数
	checkout_number : function(obj){
		 //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d]/g,"");
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
//					$('#zw_f_product_volume').val(vlm.toString().split(".")[0]+'.'+vlm.toString().split('.')[1][0]+vlm.toString().split('.')[1][1]);
					$('#zw_f_product_volume').val(cfamily_modproduct.volumeMultiplication(length,width,high));
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
	 volumeMultiplication:function(arg1,arg2,arg3)  
	{  
	    var m=0,s1=arg1.toString(),s2=arg2.toString(),s3=arg3.toString();  
	    try{m+=s1.split(".")[1].length;}catch(e){}  
	    try{m+=s2.split(".")[1].length;}catch(e){}  
	    try{m+=s3.split(".")[1].length;}catch(e){}  
	    return Number(s1.replace(".",""))*Number(s2.replace(".",""))*Number(s3.replace(".",""))/Math.pow(10,m);
	},
	// 读取属性列表数据
	prop_list_success : function(oData) {
		cfamily_modproduct.temp.proplist = oData.listProperty;
		for ( var i in cfamily_modproduct.temp.proplist) {
			cfamily_modproduct.temp.p_list[cfamily_modproduct.temp.proplist[i]["property_code"]] = cfamily_modproduct.temp.proplist[i];
		}
		cfamily_modproduct.new_show_prop();
	},
	up_brand_info : function(oResult) {
		var oList = oResult.list;
		for ( var i in oList) {
			$('#zw_f_brand').append('<option value="' + oList[i]["brandCode"] + '">'+ oList[i]["brandName"] + '</option>');
		}
		$('#zw_f_brand').val(cfamily_modproduct.product.brandCode);
		$("#zw_f_brand").select2(); 
		$("#s2id_zw_f_brand").attr("style","width:220px");
	},
	// 填充 售后地址 下拉框
	up_after_sale_address : function(oResult) {
		var oList = oResult.list;
		for ( var i in oList) {
			$('#zw_f_after_sale_address').append('<option value="' + oList[i]["uid"] + '">'+ oList[i]["afterSaleAddresName"] + '</option>');
		}
		$('#zw_f_after_sale_address').val(cfamily_modproduct.product.afterSaleAddressUid);
		$("#zw_f_after_sale_address").select2();
		$("#s2id_zw_f_after_sale_address").attr("style","width:220px");
	},
	// 根据key获取属性
	up_prop_key : function(sCode) {
		return cfamily_modproduct.temp.p_list[sCode];
	},
	// 获取子属性
	up_child : function(sCode) {
		var oRet = [];
		var sprop = cfamily_modproduct.temp.p_list;
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
		for(var s in cfamily_modproduct.product.productSkuInfoList){
			var skl = cfamily_modproduct.product.productSkuInfoList[s];
			for(var skv in  skl.skuValue.split('&')){
				alCheckCode.push(skl.skuKey.split('&')[skv].split('=')[1]+'&'+skl.skuValue.split('&')[skv].split('=')[1]+'&'+skl.skuPicUrl);
			}
		}
		alCheckCode = cfamily_modproduct.removeRe(alCheckCode); 
		cfamily_modproduct.temp.CheckedSkuCode=alCheckCode;
		var sprop = cfamily_modproduct.temp.p_list;
		// 商品属性
		var aSale = [];
		for ( var p in sprop) {
			// 判断如果是主属性
			if (p.length == cfamily_modproduct.temp.fatherlength) {
				var iType = cfamily_modproduct.up_type_bycode(p);
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
			var ofather = cfamily_modproduct.up_prop_key(aSale[p]);
			var oSub = cfamily_modproduct.up_child(aSale[p]);
			aExtendProp.push('<tr><td>' + ofather["property_name"]+ '</td><td>');
			if (oSub.length > 0) {
				aExtendProp.push('<select id="cfamily_addproduct_extend_'+ aSale[p] + '">');
				aExtendProp.push('<option value=""></option>');
				for ( var i in oSub) {
					aExtendProp.push('<option value="'+ oSub[i]["property_code"] + '">'+ oSub[i]["property_name"] + '</option>');
				}
				aExtendProp.push('</select>');
			} else {
				aExtendProp.push('<input class="c_value" type="text" />');
			}
			aExtendProp.push('</td></tr>');
			cfamily_modproduct.temp.p_extend[aSale[p]] = ofather;
		}
		if (aExtendProp.length > 0) {
			$('#cfamily_addproduct_pextend table tbody').prepend(aExtendProp.join(''));
			$('#cfamily_addproduct_pextend').show();
		}
		//显示自定义属性、扩展属性
		$('#gift_name').val('内联赠品');
		for(var props in cfamily_modproduct.product.pcProductpropertyList){
			if(cfamily_modproduct.product.pcProductpropertyList[props]['propertyType']=='449736200004'){
				
				//专为惠家有与家有汇设置的内联赠品
				if ('内联赠品' == cfamily_modproduct.product.pcProductpropertyList[props]['propertyKey']) {
					$('#gift_value').val(cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']);
					$('#cfamily_addproduct_custom_text').val('');
					$('#cfamily_addproduct_custom_value').val('');
					continue;
				}
				
				$('#cfamily_addproduct_custom table tbody').append(
						'<tr><td><input class="c_text" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyKey']
						+ '"/></td><td><textarea rows="2" class="c_value" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']
						+ '">' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']+ '</textarea></td><td></td></tr>');
				$('#cfamily_addproduct_custom_text').val('');
				$('#cfamily_addproduct_custom_value').val('');
				continue;
			}else if(cfamily_modproduct.product.pcProductpropertyList[props]['propertyType']=='449736200003'){
				$('#cfamily_addproduct_extend_'+cfamily_modproduct.product.pcProductpropertyList[props]['propertyKeycode']).val(cfamily_modproduct.product.pcProductpropertyList[props]['propertyCode']);
				continue;
			}
		}
	},

	blur_prop : function(sCode) {
		var sText=$('#cfamily_addproduct_pname_'+sCode).val();
		sText = cfamily_modproduct.replaceother(sText);
		cfamily_modproduct.temp.p_list[sCode]["property_name"]=sText;
		$('#cfamily_addproduct_ctext_'+sCode).html(sText);
		$('#cfamily_addproduct_pname_'+sCode).val(sText);
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
	add_custom : function() {
		var sText = $('#cfamily_addproduct_custom_text').val();
		var sValue = $('#cfamily_addproduct_custom_value').val();
		if (sText == "") {
			zapjs.f.message('自定义属性名称不能为空');
			return false;
		}
		if (sValue == "") {
			zapjs.f.message('自定义属性内容不能为空');
			return false;
		}
		$('#cfamily_addproduct_custom table tbody').append(
				'<tr><td><input class="c_text" value="' + sText
						+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
						+ '">' + sValue+ '</textarea></td><td></td></tr>');
		$('#cfamily_addproduct_custom_text').val('');
		$('#cfamily_addproduct_custom_value').val('');
	},
	deleteVideo : function(){
		$("#zw_f_product_desc_video").val("");//删除链接。
		//隐藏掉视频播放器
		$(".video_hidden").attr("style","display:none");
	}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modproductCshopUp", [ "zapweb/js/zapweb_upload","zapweb/js/zapweb_upload_modify",
			"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
		return cfamily_modproduct;
	});
}