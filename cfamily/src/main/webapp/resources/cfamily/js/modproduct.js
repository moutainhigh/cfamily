var cfamily_modproduct = {
	product : {},
	defaultuid : "",
	picUrl : 'x',
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
		$('#zw_f_uid').parent().prepend('<input type="hidden" id="zw_f_picEdit_flag" name="zw_f_picEdit_flag" value="0">')

		
		cfamily_modproduct.product = p;
		cfamily_modproduct.move_elm();
		cfamily_modproduct.SecondStep();
		
		this.picUrl  = $('#zw_f_piclist').val();
		
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
//		
//		$("#zw_f_settlement_type option").each(function(index){
//			if ($("#zw_f_settlement_type").val() != $(this).val()) {
//				$(this).remove();
//			}
//		});
		
		
		
	},

	SecondStep : function() {
			$("#catogoryDivInfo").hide();
			$("#OtherProductInfo").show();
			$(".zab_page_default_header_title").html(
					"商品管理 > 商品修改");
			cfamily_modproduct.show_edit();
	},

	move_elm : function() {
		$('#zw_f_elm_description').hide();
		$('#cfamily_modifyproduct_insert_prop').insertAfter('#zw_f_elm_description');
		$('#zw_f_elm_brand').hide();
		$('#zw_f_elm_brand').attr("zapweb_attr_regex_id","");
		$('#zw_f_elm_brand').after('<select id="zw_f_brand"><option value="">请选择</option></select>');
		$('#zw_f_traval').hide();
		$('#cfamily_addproduct_travel').insertAfter('#zw_f_traval');
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
		cfamily_modproduct.set_wvp();
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',cfamily_modproduct.beforesubmit);
		var ss=$('#zw_f_keyword').parent()[0].innerHTML;
		ss=ss.replace('zapadmin_single.show_box(\'zw_f_keyword\')','cfamily_modproduct.show_box(\'zw_f_keyword\')');
		$('#zw_f_keyword').parent()[0].innerHTML=ss;
		document.getElementById("zw_f_market_price").setAttribute("onkeyup","cfamily_modproduct.checkout_decimal(this)");//市场价
		cfamily_modproduct.set_value();
		
		//对商品主图中图片后的修改功能进行隐藏
		$('#zw_f_mainpic_url').parent().find("[href='javascript:zapweb_upload.change_index('zw_f_mainpic_url',0,'modifyForPic')']").hide();
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
		// 是否自动上架
		$("#zw_f_auto_sell").val(cfamily_modproduct.product.autoSell);
		
		$('#zw_f_product_name').val(cfamily_modproduct.product.produtName);//商品名称
		$('#zw_f_market_price').val(cfamily_modproduct.product.marketPrice);//商场价
		$('#zw_f_product_shortname').val(cfamily_modproduct.product.productShortname);//商品简称
		$('#zw_f_product_shortname').attr({ readonly: 'true' });

		$('#zw_f_tax_rate_show').val(cfamily_modproduct.product.taxRate);//税率
		$('#zw_f_tax_rate').val(cfamily_modproduct.product.taxRate);
		$('#zw_f_tax_rate_show').attr({ disabled: 'true' });
//		$('#zw_f_cost_price').val(cfamily_modproduct.product.costPrice);//成本价
		$('#zw_f_video_url').val(cfamily_modproduct.product.videoUrl);//商品视频链接
		$('#zw_f_supplier_name').val(cfamily_modproduct.product.supplierName);//供应商名称			已隐藏
		$('#zw_f_fictitious_sales').val(cfamily_modproduct.product.pcProductinfoExt.fictitiousSales);//虚拟销售量
		
		$("#zw_f_purchase_type").val(cfamily_modproduct.product.pcProductinfoExt.purchaseType);
		$("#zw_f_settlement_type").val(cfamily_modproduct.product.pcProductinfoExt.settlementType);
		
		$('#zw_f_mainpic_url').val(cfamily_modproduct.product.mainPicUrl);//商品主图片
		zapweb_upload.upload_file('zw_f_mainpic_url',cfamily_modproduct.product.mainPicUrl);
		
		//$('#zw_f_adpic_url').val(cfamily_modproduct.product.adPicUrl);//商品广告图
		//zapweb_upload_upgrade.upload_file('zw_f_adpic_url',cfamily_modproduct.product.adPicUrl);
		
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
		
		var adpicUrls='';
		for(var i in cfamily_modproduct.product.pcAdpicList){
			if(cfamily_modproduct.product.pcAdpicList[i].sku_code == "" || cfamily_modproduct.product.pcAdpicList[i].sku_code == null){
				if(i==0){
					adpicUrls=cfamily_modproduct.product.pcAdpicList[i].pic_url;
				}else{
					adpicUrls=adpicUrls+'|'+cfamily_modproduct.product.pcAdpicList[i].pic_url;
				}
			}			
		}
		$('#zw_f_adpic_url').val(adpicUrls);//商品广告图
		zapweb_upload_upgrade.upload_file('zw_f_adpic_url',adpicUrls);
		
		//加载展示时间
		for(var i in cfamily_modproduct.product.pcAdpicList){
			$('#zw_f_adpic_url_sdate_'+i).val(cfamily_modproduct.product.pcAdpicList[i].start_date);
			$('#zw_f_adpic_url_edate_'+i).val(cfamily_modproduct.product.pcAdpicList[i].end_date);
		}
		
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
		
		if(cfamily_modproduct.product.onlinepayFlag == '449747110002'){
			$('#zw_f_onlinepay_flag').prop('checked',true);	
			$('#zw_f_onlinepay_start').val(cfamily_modproduct.product.onlinepayStart);
			$('#zw_f_onlinepay_end').val(cfamily_modproduct.product.onlinepayEnd);
		}else{
			$('#zw_f_onlinepay_flag').prop('checked',false);	
			$('#zw_f_onlinepay_time').hide();		
		}
		// 不显示在线支付的时间输入框
		$('#zw_f_onlinepay_time').hide();
		
		$('#zw_f_tv_tips').val(cfamily_modproduct.product.tvTips);
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
		
		var picUrl1 =  $('#zw_f_piclist').val();
		if(picUrl1!=cfamily_modproduct.picUrl){
			$('#zw_f_picEdit_flag').val('1')
		}
		
		var bFlag = true;

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
		//广告图
		var sAdpicList = $('#zw_f_adpic_url').val().split('|');
		//标签总长度不能超过10个字
//		if (zw_f_keyword.replace(/\s+/g,'').length > 10) {
//			zapjs.f.message("*商品标签：值过长，不符合规则，标签总长度不能超过10个字符");
//			return false;
//		}
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
//		if (bFlag) {
//			if (!zapjs.zw.validate_field('#zw_f_adpic_url', '469923180002')) {
//				bFlag = false;
//			}
//		}
		//多广告图的处理逻辑
		//时间为非必填项，未填显示时间的图片为有效图片
		//只填写开始或结束时间，则填写无效，保存时提示：“广告图时间错误”，不能保存。
		//时间不能重合，若时间重合，保存时提示：“广告图时间重复”，不能保存。（是否能填写时控制）
		//一张图片：无显示时间则正常显示，有显示时间，时间结束则图片无效，前端不展示。
		//多张图片：有显示时间优先＞排序优先，有效图片失效则显示其他有效图片，无有效图片则前端不展示。
		//图片数量不做限制，只能传一张时间为空的图片，上传两张以上，保存时提示“广告图不能上传2张时间为空的图片”。
		if(sAdpicList.length > 0){
			if(sAdpicList.length == 1){
				var start_date = $('#zw_f_adpic_url_sdate_0').val();
				var end_date = $('#zw_f_adpic_url_edate_0').val();
				if(typeof start_date === "undefined") {
					start_date = '';
				}
				if(typeof end_date === "undefined") {
					end_date = '';
				}
				if(start_date == '' ^ end_date == ''){
					zapjs.f.message('广告图时间：不能只填写开始时间或者结束时间');
					bFlag = false;
				}
				if(start_date == end_date && start_date != ''){
					zapjs.f.message('广告图时间：开始时间不能等于结束时间');
					bFlag = false;
				}
			} else {
				var count = 0;
				for ( var i in sAdpicList) {
					var sdate = $('#zw_f_adpic_url_sdate_'+i).val();
					var edate = $('#zw_f_adpic_url_edate_'+i).val();
					if(typeof sdate === "undefined") {
						sdate = '';
					}
					if(typeof edate === "undefined") {
						edate = '';
					}
					if(sdate == '' && edate == ''){
						count ++;
					}
					if(sdate == '' ^ edate == ''){
						zapjs.f.message('广告图时间：不能只填写开始时间或者结束时间');
						bFlag = false;
						break;
					}
					var compare = (Date.parse(sdate) - Date.parse(edate)) / 3600 / 1000;
					if(compare >= 0) {
						zapjs.f.message('广告图时间：开始时间不能大于或者等于结束时间');
						bFlag = false;
						break;
					}
					//验重
					for( var j in sAdpicList) {
						if(j >= i){
							break;
						}
						var csdate = $('#zw_f_adpic_url_sdate_'+j).val();
						var cedate = $('#zw_f_adpic_url_edate_'+j).val();
						if(csdate == '' && cedate == ''){
							continue;
						}
						var compare1 = (Date.parse(sdate) - Date.parse(csdate)) / 3600 / 1000;
						var compare2 = (Date.parse(edate) - Date.parse(cedate)) / 3600 / 1000;
						if(compare1 >= 0 && compare2 <= 0) {
							zapjs.f.message('广告图时间：广告图时间不能重复');
							bFlag = false;
							break;
						}
						if(compare1 <= 0 && compare2 >= 0) {
							zapjs.f.message('广告图时间：广告图时间不能重复');
							bFlag = false;
							break;
						}
						//不允许出现时间交叉的情况
						var compare3 = (Date.parse(sdate) - Date.parse(cedate)) / 3600 / 1000;
						var compare4 = (Date.parse(edate) - Date.parse(csdate)) / 3600 / 1000;
						if(compare1 > 0 && compare3 < 0 && compare2 > 0){
							zapjs.f.message('广告图时间：广告图时间不能出现交叉');
							bFlag = false;
							break;
						}
						if(compare1 < 0 && compare4 > 0 && compare2 < 0){
							zapjs.f.message('广告图时间：广告图时间不能出现交叉');
							bFlag = false;
							break;
						}
					}
				}
				if(count >= 2){
					zapjs.f.message('广告图时间：广告图不能上传2张时间为空的图片');
					bFlag = false;
				}
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
		if(bFlag){
			// 设定了仅支持在线支付则必须设定生效时间
			//if($('#zw_f_onlinepay_flag').prop('checked')){
				//if($('#zw_f_onlinepay_start').val() == '' || $('#zw_f_onlinepay_end').val() == ''){
				//	zapjs.f.message('请设置仅支持在线支付的生效时间！');
				//	bFlag = false;
				//}
			//}
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
		
		// 是否自动上架
		thisproduct.autoSell = $('#zw_f_auto_sell').val();
		
		thisproduct.produtName = $('#zw_f_product_name').val();
		thisproduct.labels=$('#zw_f_labels').val();
		thisproduct.productShortname = $('#zw_f_product_shortname').val();//商品简称
//		thisproduct.costPrice = $('#zw_f_cost_price').val();//成本价
		thisproduct.videoUrl = $('#zw_f_video_url').val();//商品视频链接
		thisproduct.supplierName = $('#zw_f_supplier_name').val();//供应商名称
		// zw_f_brand
		thisproduct.marketPrice = $('#zw_f_market_price').val();
		thisproduct.brandCode = $('#zw_f_brand').val();
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
		// 分类信息
		thisproduct.category = {categoryCode : $('#zw_f_listbox').val()};
		// 商品图片
		thisproduct.pcPicList = [];
		var sPicList = $('#zw_f_piclist').val().split('|');
		for ( var i in sPicList) {
			thisproduct.pcPicList.push({picUrl : sPicList[i]});
		}
//		if (sPicList.length > 0) {
			thisproduct.mainPicUrl =$('#zw_f_mainpic_url').val();
//		}
		//thisproduct.adPicUrl =$('#zw_f_adpic_url').val();
		thisproduct.pcAdpicList = [];
		var sAdpicList = $('#zw_f_adpic_url').val().split('|');
		for ( var i in sAdpicList) {
			thisproduct.pcAdpicList.push({pic_url : sAdpicList[i], start_date : $('#zw_f_adpic_url_sdate_'+i).val(), end_date : $('#zw_f_adpic_url_edate_'+i).val(), ord_no : i});
		}	
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
		thisproduct.pcProductinfoExt.purchaseType = $('#zw_f_purchase_type').val();
		thisproduct.pcProductinfoExt.settlementType = $('#zw_f_settlement_type').val();
		
		// 仅支持在线支付的属性
		if($('#zw_f_onlinepay_flag').prop('checked')){
			thisproduct.onlinepayFlag = $('#zw_f_onlinepay_flag').val();
			thisproduct.onlinepayStart = $('#zw_f_onlinepay_start').val();
			thisproduct.onlinepayEnd = $('#zw_f_onlinepay_end').val();
		}else{
			thisproduct.onlinepayFlag = '';
			thisproduct.onlinepayStart = '';
			thisproduct.onlinepayEnd = '';
		}
		
		// 直播商品促销语
		thisproduct.tvTips = $('#zw_f_tv_tips').val();
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
		$('#zw_f_product_volume').parent().append('<label id=volumn22 >(单位：立方米&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
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
		cfamily_modproduct.refurbish_lastTd();
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
					$('#start_date').val(cfamily_modproduct.product.pcProductpropertyList[props]['startDate']);
					$('#end_date').val(cfamily_modproduct.product.pcProductpropertyList[props]['endDate']);
					
					$('#cfamily_addproduct_custom_text').val('');
					$('#cfamily_addproduct_custom_value').val('');
					continue;
				}
				
				$('#cfamily_addproduct_custom table tbody').append(
						'<tr><td><input class="c_text" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyKey']
						+ '"/></td><td><textarea rows="2" class="c_value" value="' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']
						+ '">' + cfamily_modproduct.product.pcProductpropertyList[props]['propertyValue']+ '</textarea></td><td><input type="button" class="btn" onclick="cfamily_modproduct.delete_custom(this)" value="删除"/></td></tr>');
				$('#cfamily_addproduct_custom_text').val('');
				$('#cfamily_addproduct_custom_value').val('');
				continue;
			}else if(cfamily_modproduct.product.pcProductpropertyList[props]['propertyType']=='449736200003'){
				$('#cfamily_addproduct_extend_'+cfamily_modproduct.product.pcProductpropertyList[props]['propertyKeycode']).val(cfamily_modproduct.product.pcProductpropertyList[props]['propertyCode']);
				continue;
			}
		}
		//自定义属性
		//增加上移下移功能
		var trs = document.getElementById("move").getElementsByTagName("tr");
		//方便校验有无更改图片
		this.picUrl  = $('#zw_f_piclist').val();
		for(var i=0;i<trs.length;i++){
			if(trs[i].rowIndex==1){
				$('#cfamily_addproduct_custom table tbody tr:eq('+i+')').append('<td><a href="javascript:void(0);" onclick="cfamily_modproduct.move_down(this)">下移</a></td>');
			}else if(trs[i].rowIndex==0){
				$('#cfamily_addproduct_custom table tbody tr:eq('+i+')').append('<td></td>');
			}else{
				$('#cfamily_addproduct_custom table tbody tr:eq('+i+')').append('<td><a href="javascript:void(0);" onclick="cfamily_modproduct.move_up(this)">上移</a>&nbsp&nbsp&nbsp&nbsp<a href="javascript:void(0);" onclick="cfamily_modproduct.move_down(this)">下移</a></td>');
			}
		}
		cfamily_modproduct.refurbish_lastTd();
	},
	
	
	refurbish_lastTd : function(){
//		if ($("#move tr").length>3) {
//			$('#cfamily_addproduct_custom table tfoot tr td:last').html('<a href="javascript:void(0);" onclick="cfamily_modproduct.move_up_add(this)">上移</a>');
//		}else{
//		}
		$('#cfamily_addproduct_custom table tfoot tr td:last').html('无');
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
		cfamily_modproduct.refurbish_lastTd();
	},
	
	//下移
	move_down : function(trNow){//直接交换两个控件中的值，不移动tr
		var tr = $(trNow).parents("tr");
		var values = tr.children();
		var sText = values[0].getElementsByTagName("input")[0].value;
		var sValue = values[1].getElementsByTagName("textarea")[0].value;
		
		if(tr.next().length==0){
			var moveText = $('#cfamily_addproduct_custom_text').val();
			var moveValue = $('#cfamily_addproduct_custom_value').val();
			$('#cfamily_addproduct_custom_text').val(sText);
			$('#cfamily_addproduct_custom_value').val(sValue);
			
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
		cfamily_modproduct.refurbish_lastTd();
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
				moveValues = $('#cfamily_addproduct_custom table tbody tr:eq('+(i-1)+')').children();
			}
		}
		var moveText = moveValues[0].getElementsByTagName("input")[0].value;
		var moveValue = moveValues[1].getElementsByTagName("textarea")[0].value;
		
		$('#cfamily_addproduct_custom_text').val(moveText);
		$('#cfamily_addproduct_custom_value').val(moveValue);
		
		moveValues[0].getElementsByTagName("input")[0].setAttribute("value",sText);
		moveValues[1].getElementsByTagName("textarea")[0].innerHTML=sValue;
		cfamily_modproduct.refurbish_lastTd();
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
//		$('#cfamily_addproduct_custom table tbody').append(
//				'<tr><td><input class="c_text" value="' + sText
//						+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
//						+ '">' + sValue+ '</textarea></td><td></td></tr>');
		var ts = document.getElementById("move").getElementsByTagName("tr");
		var count = ts.length;
		if(count<=2){
			$('#cfamily_addproduct_custom table tbody').append(
					'<tr><td><input class="c_text" value="' + sText
					+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
					+ '">' + sValue+ '</textarea></td>'
					+'<td><input type="button" class="btn" onclick="cfamily_modproduct.delete_custom(this)" value="删除"/></td>'
					+'<td><a href="javascript:void(0)" onclick="cfamily_modproduct.move_down(this)">下移</a></td></tr>');
		}else{
			$('#cfamily_addproduct_custom table tbody').append(
					'<tr><td><input class="c_text" value="' + sText
					+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
					+ '">' + sValue+ '</textarea></td>'
					+'<td><input type="button" class="btn" onclick="cfamily_modproduct.delete_custom(this)" value="删除"/></td>'
					+'<td><a href="javascript:void(0);" onclick="cfamily_modproduct.move_up(this)">上移</a>&nbsp&nbsp&nbsp&nbsp<a href="javascript:void(0);" onclick="cfamily_modproduct.move_down(this)">下移</a></td></tr>');
		}
		cfamily_modproduct.refurbish_lastTd();
		$('#cfamily_addproduct_custom_text').val('');
		$('#cfamily_addproduct_custom_value').val('');
	},
	delete_custom : function(trNow){
		  $(trNow).parent().parent().remove(); 
		  cfamily_modproduct.refurbish_lastTd();
	},
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/modproduct", [ "zapweb/js/zapweb_upload","zapweb/js/zapweb_upload_upgrade","zapweb/js/zapweb_upload_modify",
			"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
		return cfamily_modproduct;
	});
}
