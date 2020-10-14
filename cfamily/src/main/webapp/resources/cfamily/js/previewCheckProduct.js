var previewCheckProduct = {
		product : {},
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
			previewCheckProduct.product = p;
			previewCheckProduct.show_edit();
		},

		// 显示基本信息
		show_edit : function() {
//					if(previewCheckProduct.product.productCodeOld==null||previewCheckProduct.product.productCodeOld==''){
//				previewCheckProduct.up_prop_list();
//					}else{
//						$("#cshop_addproduct_custom").parent().parent().parent().hide();
//					}
//			zapjs.zw.api_call('com_cmall_productcenter_service_ApiGetSellBrandForSpecial', {
//			}, previewCheckProduct.up_brand_info);
//			previewCheckProduct.set_wvp();
//			var ss=$('#zw_f_keyword').parent()[0].innerHTML;
//			ss=ss.replace('zapadmin_single.show_box(\'zw_f_keyword\')','previewCheckProduct.show_box(\'zw_f_keyword\')');
//			$('#zw_f_keyword').parent()[0].innerHTML=ss;
			previewCheckProduct.set_value();
		},
		
		set_value:function(){//设置值
			$('#zw_f_product_name').html(previewCheckProduct.product.produtName);//商品名称
			$('#zw_f_market_price').html(previewCheckProduct.product.marketPrice);//商场价
			$('#zw_f_cost_price').html(previewCheckProduct.product.costPrice);//成本价
			$('#zw_f_tax_rate').html(previewCheckProduct.product.taxRate);//税率
			$('#zw_f_video_url').html(previewCheckProduct.product.videoUrl);//商品视频链接
			$('#zw_f_sell_productcode').html(previewCheckProduct.product.sellProductcode);//货号
//			$('#zw_f_supplier_name').val(previewCheckProduct.product.supplierName);//供应商名称			已隐藏
			$('#zw_f_brand').html(previewCheckProduct.product.brandCode);
			$('#zw_f_product_weight').html(previewCheckProduct.product.productWeight+"(单位:千克&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)");//商品重量
			
			$('#zw_f_product_volume').html(previewCheckProduct.product.productVolume+"(单位：立方米&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)");//商品体积
			var ckg = "<p></p><div  class=\"controls\" id=\"cfamily_volume_item\">"
				+"长：<label style=\"width:90px\" id=\"zw_f_volumn_length\"></label>"
				+"&nbsp;&nbsp;&nbsp;宽：<label style=\"width:90px\" id=\"zw_f_volumn_width\"></label>"
				+"&nbsp;&nbsp;&nbsp;高：<label style=\"width:90px\" id=\"zw_f_volumn_high\"></label>";
			$('#zw_f_product_volume').parent().parent().append(ckg);
			$('#zw_f_volumn_length').html(previewCheckProduct.product.productVolumeItem.split(',')[0]);//长
			$('#zw_f_volumn_width').html(previewCheckProduct.product.productVolumeItem.split(',')[1]);//宽
			$('#zw_f_volumn_high').html(previewCheckProduct.product.productVolumeItem.split(',')[2]);//高
			
			var trans = previewCheckProduct.product.transportTemplate;
			if(trans!=null&&trans!=""&&trans=="0"){//运费模板
				$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(0)").attr("checked",'checked'); 
			}else if(/^\d+$/.test(trans)) {
				$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(1)").attr("checked",'checked'); 
				$('#zw_f_tra_user').val(trans);
			}else{
				var uuid ={};
				uuid.uid = trans;
				zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
					var freightName = result.tplName;
					$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(2)").attr("checked",'checked');
					eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (trans) +'","'+ freightName +'")' );
				});
			}
			
			if(previewCheckProduct.product.description.keyword!=null&&previewCheckProduct.product.description.keyword!=''){
				var labelsStr = previewCheckProduct.product.description.keyword.replace(/,/g,' '); 				//将标签之间的逗号替换为空格
				$('#zw_f_keyword').html(labelsStr);//标签显示
			}
			var trans = previewCheckProduct.product.transportTemplate;
			if(trans!=null&&trans!=""&&trans=="0"){//运费模板
				$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(0)").attr("checked",'checked'); 
			}else if(/^\d+$/.test(trans)) {
				$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(1)").attr("checked",'checked'); 
				$('#zw_f_tra_user').val(trans);
			}else{
				var uuid ={};
				uuid.uid = trans;
				zapjs.zw.api_call('com_cmall_productcenter_service_api_getFreightNameApi',uuid,function(result) {
					var freightName = result.tplName;
					$("#cfamily_addproduct_travel input[name=zw_f_tra_select]:eq(2)").attr("checked",'checked');
					eval('parent.zapadmin_single.result'+'("zw_f_traval","'+ (trans) +'","'+ freightName +'")' );
				});
			}
			
			$('#zw_f_editor').html(previewCheckProduct.product.description.descriptionInfo);//商品描述
			$('#zw_f_mainpic_url').html("<div class=\"w_left w_w_100\"><a href=\""+previewCheckProduct.product.mainPicUrl+"\" target=\"_blank\"><img src=\""+previewCheckProduct.product.mainPicUrl+"\"></a></div>");//商品主图片
			
			//商品图片
			for(var i in previewCheckProduct.product.pcPicList){
				if(previewCheckProduct.product.pcPicList[i].skuCode == "" || previewCheckProduct.product.pcPicList[i].skuCode == null){
					$('#zw_f_piclist').append("<div class=\"w_left w_w_100\"><a href=\""+previewCheckProduct.product.pcPicList[i].picUrl+"\" target=\"_blank\"><img src=\""+previewCheckProduct.product.pcPicList[i].picUrl+"\"></a></div>");
				}
			}
			$('#zw_f_description_pic').val(previewCheckProduct.product.description.descriptionPic);//描述图片
			//描述图片
			var descriptionPic = previewCheckProduct.product.description.descriptionPic;
			if (null != descriptionPic && "" != descriptionPic) {
				var descriptionPicArr = descriptionPic.split("\|");
				for(var i in descriptionPicArr){
					$('#zw_f_description_pic').append("<div class=\"w_left w_w_100\"><a href=\""+descriptionPicArr[i]+"\" target=\"_blank\"><img src=\""+descriptionPicArr[i]+"\"></a></div>");
				}
			}
			//显示自定义属性、扩展属性
			for(var props in previewCheckProduct.product.pcProductpropertyList){
				if(previewCheckProduct.product.pcProductpropertyList[props]['propertyType']=='449736200004'){
					$('#cshop_addproduct_custom table tbody').append(
							'<tr><td><input class="c_text" readOnly="readOnly" value="' + previewCheckProduct.product.pcProductpropertyList[props]['propertyKey']
							+ '"/></td><td><textarea rows="2" readOnly="readOnly" class="c_value" value="' + previewCheckProduct.product.pcProductpropertyList[props]['propertyValue']
							+ '">' + previewCheckProduct.product.pcProductpropertyList[props]['propertyValue']+'</textarea></td><td></td></tr>');
					$('#cshop_addproduct_custom_text').val('');
					$('#cshop_addproduct_custom_value').val('');
					continue;
				}
			}
			
//			var picUrls='';
//			for(var i in previewCheckProduct.product.pcPicList){
//				if(previewCheckProduct.product.pcPicList[i].skuCode == "" || previewCheckProduct.product.pcPicList[i].skuCode == null){
//					if(i==0){
//						picUrls=previewCheckProduct.product.pcPicList[i].picUrl;
//					}else{
//						picUrls=picUrls+'|'+previewCheckProduct.product.pcPicList[i].picUrl;
//					}
//				}
//			}
			
//			$('#zw_f_piclist').val(picUrls);//商品图片
//			zapweb_upload.upload_file('zw_f_piclist',picUrls);

//			$('#zw_f_editor').val(previewCheckProduct.product.description.descriptionInfo);//商品描述
////					zapweb_upload.upload_file('zw_f_editor',previewCheckProduct.product.description.descriptionInfo);
//			$('#zw_f_description_pic').val(previewCheckProduct.product.description.descriptionPic);//描述图片
//			zapweb_upload.upload_file('zw_f_description_pic',previewCheckProduct.product.description.descriptionPic);
			
			
		
		
		},
		
		show_box : function(sId) {
			var s=zapadmin_single.temp.opts[sId];
			zapjs.f.window_box({
				id : sId + 'zapadmin_single_showbox',
				content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'&category_code='+previewCheckProduct.product.category.categoryCode+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

				width : '700',
				height : '550'
			});

		},
		
		// 2222222222获取属性列表
		up_prop_list : function() {
			zapjs.zw.api_call('com_cmall_productcenter_process_ApiGetCategoryProperty', {categoryCode : previewCheckProduct.product.category.categoryCode}, previewCheckProduct.prop_list_success);
		},
		// 设置商品描述信息
		set_editor_info : function() {
			zapjs.zw.editor_show('zw_f_editor', $('#cshop_addproduct_uploadurl').val());
		},
		//处理重量、体积、货到付款
//		set_wvp : function(){
//			var ckg = "<li class=\"w_mt_5\"></li><div  class=\"controls\" id=\"cshop_addproduct_volume_item\">"
//					+"长：<input style=\"width:90px\" onChange=\"previewCheckProduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_length\" id=\"zw_f_volumn_length\"  value=\"\"/>"
//					+"&nbsp;&nbsp;&nbsp;宽：<input style=\"width:90px\" onChange=\"previewCheckProduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_width\" id=\"zw_f_volumn_width\"  value=\"\"/>"
//					+"&nbsp;&nbsp;&nbsp;高：<input style=\"width:90px\" onChange=\"previewCheckProduct.get_vloumn()\" type=\"text\" name=\"zw_f_volumn_high\" id=\"zw_f_volumn_high\"  value=\"\"/></div>";
//			if(previewCheckProduct.product.sellerCode!=''&&previewCheckProduct.product.sellerCode.length!=5){
//				$('#zw_f_flag_payway').parent().parent().hide();
//			}
//			$('#zw_f_product_volume').parent().append('<label id=volumn22 >(单位：立方米&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
//			$('#zw_f_product_volume').parent().parent().append(ckg);
//			$('#zw_f_product_weight').parent().append('<label id=weight22 >(单位:千克&nbsp;&nbsp;&nbsp;&nbsp;备注：含外包装)<label/>');
//			
//		},
		// 读取属性列表数据
		prop_list_success : function(oData) {
			previewCheckProduct.temp.proplist = oData.listProperty;
			for ( var i in previewCheckProduct.temp.proplist) {
				previewCheckProduct.temp.p_list[previewCheckProduct.temp.proplist[i]["property_code"]] = previewCheckProduct.temp.proplist[i];
			}
			previewCheckProduct.new_show_prop();
		},
//		up_brand_info : function(oResult) {
//			var oList = oResult.list;
//			for ( var i in oList) {
//				$('#zw_f_brand').append('<option value="' + oList[i]["brandCode"] + '">'+ oList[i]["brandName"] + '</option>');
//			}
//			$('#zw_f_brand').val(previewCheckProduct.product.brandCode);
//			$("#zw_f_brand").select2(); 
//			$("#s2id_zw_f_brand").attr("style","width:220px");
//		},
		// 根据key获取属性
		up_prop_key : function(sCode) {
			return previewCheckProduct.temp.p_list[sCode];
		},
		// 获取子属性
		up_child : function(sCode) {
			var oRet = [];
			var sprop = previewCheckProduct.temp.p_list;
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
			for(var s in previewCheckProduct.product.productSkuInfoList){
				var skl = previewCheckProduct.product.productSkuInfoList[s];
				for(var skv in  skl.skuValue.split('&')){
					alCheckCode.push(skl.skuKey.split('&')[skv].split('=')[1]+'&'+skl.skuValue.split('&')[skv].split('=')[1]+'&'+skl.skuPicUrl);
				}
			}
			alCheckCode = previewCheckProduct.removeRe(alCheckCode); 
			previewCheckProduct.temp.CheckedSkuCode=alCheckCode;
			var sprop = previewCheckProduct.temp.p_list;
			// 商品属性
			var aSale = [];
			for ( var p in sprop) {
				// 判断如果是主属性
				if (p.length == previewCheckProduct.temp.fatherlength) {
					var iType = previewCheckProduct.up_type_bycode(p);
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
				var ofather = previewCheckProduct.up_prop_key(aSale[p]);
				var oSub = previewCheckProduct.up_child(aSale[p]);
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
				previewCheckProduct.temp.p_extend[aSale[p]] = ofather;
			}
			if (aExtendProp.length > 0) {
				$('#cshop_addproduct_pextend table tbody').prepend(aExtendProp.join(''));
				$('#cshop_addproduct_pextend').show();
			}
			//显示自定义属性、扩展属性
//			$('#gift_name').val('内联赠品');
			for(var props in previewCheckProduct.product.pcProductpropertyList){
				if(previewCheckProduct.product.pcProductpropertyList[props]['propertyType']=='449736200004'){
					$('#cshop_addproduct_custom table tbody').append(
							'<tr><td><input class="c_text" value="' + previewCheckProduct.product.pcProductpropertyList[props]['propertyKey']
							+ '"/></td><td><textarea rows="2" class="c_value" value="' + previewCheckProduct.product.pcProductpropertyList[props]['propertyValue']
							+ '">' + previewCheckProduct.product.pcProductpropertyList[props]['propertyValue']+ '</textarea></td><td></td></tr>');
					$('#cshop_addproduct_custom_text').val('');
					$('#cshop_addproduct_custom_value').val('');
					continue;
				}else if(previewCheckProduct.product.pcProductpropertyList[props]['propertyType']=='449736200003'){
					$('#cshop_addproduct_extend_'+previewCheckProduct.product.pcProductpropertyList[props]['propertyKeycode']).val(previewCheckProduct.product.pcProductpropertyList[props]['propertyCode']);
					continue;
				}
			}
		},

		blur_prop : function(sCode) {
			var sText=$('#cshop_addproduct_pname_'+sCode).val();
			sText = previewCheckProduct.replaceother(sText);
			previewCheckProduct.temp.p_list[sCode]["property_name"]=sText;
			$('#cshop_addproduct_ctext_'+sCode).html(sText);
			$('#cshop_addproduct_pname_'+sCode).val(sText);
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
			$('#cshop_addproduct_custom table tbody').append(
					'<tr><td><input class="c_text" value="' + sText
							+ '"/></td><td><textarea rows="2" class="c_value" value="' + sValue
							+ '">' + sValue+ '</textarea></td><td></td></tr>');
			$('#cshop_addproduct_custom_text').val('');
			$('#cshop_addproduct_custom_value').val('');
		}
	};

	if (typeof define === "function" && define.amd) {
		define("cfamily/js/previewCheckProduct", [ "zapweb/js/zapweb_upload",
				"zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single" ], function() {
			return previewCheckProduct;
		});
	}
