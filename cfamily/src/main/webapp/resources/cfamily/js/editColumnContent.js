var editColumnContent = {
		// 栏目类型
		columnType : "",
		initLinkValue : "",
		init : function(columnType,numLanguanggao){
			
			this.columnType = columnType;
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editColumnContent.beforeSubmit);
			editColumnContent.defaultHidden();			
			editColumnContent.initPosition(columnType,numLanguanggao);		
			editColumnContent.initLinkType(columnType);
			editColumnContent.shareLinkType();
			editColumnContent.initShowmore();			
			var tem =  $("#zw_f_showmore_linkvalue").val();
			console.log(tem);
			if($("#zw_f_showmore_linktype").val() == "4497471600020004"){//类型为商品
				var opt ={};
				opt.productStrs = $("#zw_f_showmore_linkvalue").val();
				zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductName',opt,function(result) {
					$("#zw_f_showmore_linkvalue_text").text(result.productName);//显示商品名称
				});
			}
			if($("#zw_f_showmore_linktype").val() == "4497471600020014"){//类型为品牌
				var opt ={};
				opt.brandCode = $("#zw_f_showmore_linkvalue").val();
				zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetBrandName',opt,function(result) {
					$("#zw_f_showmore_linkvalue_text").text(result.brandInfo.brandName);//显示品牌名称
				});
			}
			editColumnContent.shareShow();
			
			if (columnType != '4497471600010012') {
				//当栏目类型不是通知栏时，链接类型字段把显示浮层类型去掉
				$("#zw_f_showmore_linktype option[value='4497471600020005']").remove();
			}
			
			if (columnType == '4497471600010018') {
				//当栏目类型是右两栏闪购时只能选择URL
				$("#zw_f_showmore_linktype option[value!='4497471600020001']").remove();
			}
			
			if (columnType != '4497471600010004') {
				//当栏目类型不是导航栏时，链接类型字段把品牌搜索去掉
				$("#zw_f_showmore_linktype option[value='4497471600020014']").remove();
			}
			if (columnType != '4497471600010004'&&columnType != '4497471600010001'&&columnType != '4497471600010002') {
				//一栏广告添加视频列表和直播列表
				$("#zw_f_showmore_linktype option[value='4497471600020016']").remove();
				$("#zw_f_showmore_linktype option[value='4497471600020017']").remove();
			}
		},
		
		// 数据初始化时候默认隐藏的内容加到这个方法中，然后调用initPosition方法选择性的展示 - Yangcl
		defaultHidden:function(){
			// 隐藏【视频播放列表】相关字段
			$("#zw_f_select_product").parent().parent().hide();
			$("#zw_f_product_name").parent().parent().hide();
			$("#zw_f_product_code").parent().parent().hide();
			$("#zw_f_product_price").parent().parent().hide();
			$("#zw_f_video_ad").parent().parent().hide();
			$("#zw_f_video_link").parent().parent().hide();
			// 隐藏【……】相关字段
		},
		// 可以在此方法中自定义每个栏目类型对应的链接类型
		initLinkType: function(columnType){
			var linkTypeVal = $('#zw_f_showmore_linktype').val();
			// 小程序导航栏自定义链接类型
			if(columnType == '4497471600010004' && viewType == '4497471600100004') {
				$('#zw_f_showmore_linktype').empty();
				$('#zw_f_showmore_linktype').append('<option value="4497471600020001">URL</option>');
				$('#zw_f_showmore_linktype').append('<option value="4497471600020002">关键词搜索</option>');
				$('#zw_f_showmore_linktype').append('<option value="4497471600020003">分类搜索</option>');
				$('#zw_f_showmore_linktype').append('<option value="4497471600020004">商品详情</option>');
				$('#zw_f_showmore_linktype').append('<option value="4497471600020014">品牌搜索</option>');
				$('#zw_f_showmore_linktype').append('<option value="4497471600020012">小程序</option>');
			}
			
			$('#zw_f_showmore_linktype').val(linkTypeVal);
		},		
		initPosition:function(columnType,numLanguanggao){			
			if (columnType=='4497471600010001' 			//轮播广告
				|| columnType=='4497471600010004' 		//导航栏
				|| columnType=='4497471600010008' 	//商品推荐
				|| columnType=='4497471600010009'//两栏多行推荐
				|| columnType=='4497471600010013' 	//三栏两行推荐
				|| columnType=='4497471600010014'//两栏两行推荐
				|| columnType == '4497471600010012' // 通知模板
				|| columnType == '4497471600010031' // 通知模板
				) {
				$("#zw_f_position").show();
				$("#zw_f_position_select").hide();
				
			}else if (columnType=='4497471600010002'	//一栏广告
				|| columnType=='4497471600010005'		//一栏推荐
				) {
				$("#zw_f_position").hide();
				$("#zw_f_position_select").show();
				$("#zw_f_position_select").val($("#zw_f_position").val());
			}else if (columnType=='4497471600010003'	//二栏广告
				) {
				$("#zw_f_position").hide();
				$("#zw_f_position_select").show();
				$("#zw_f_position_select").append('<option value="'+2+'">'+2+'</option>');
				$("#zw_f_position_select").val($("#zw_f_position").val());
			}
			else if (columnType == '4497471600010021' // 多栏广告
				
			) {
				
				$("#zw_f_position").hide();
				$("#zw_f_position_select").show();
				if(numLanguanggao=='4497471600360002'){
					$("#zw_f_position_select").append(
							'<option value="' + 2 + '">' + 2 + '</option>');
				}else  if(numLanguanggao=='4497471600360003'){
					$("#zw_f_position_select").append(
							'<option value="' + 2 + '">' + 2 + '</option>');
					$("#zw_f_position_select").append(
							'<option value="' + 3 + '">' + 3 + '</option>');
				}else  if(numLanguanggao=='4497471600360004'){
					$("#zw_f_position_select").append(
							'<option value="' + 2 + '">' + 2 + '</option>');
					$("#zw_f_position_select").append(
							'<option value="' + 3 + '">' + 3 + '</option>');
					$("#zw_f_position_select").append(
							'<option value="' + 4 + '">' + 4 + '</option>');
				}
				$("#zw_f_position_select").val($("#zw_f_position").val());
			}
			else if (columnType=='4497471600010006'	//右两栏推荐
					|| columnType=='4497471600010007'	//左两栏推荐
					|| columnType=='4497471600010018'	//右两栏闪购
				) {
				$("#zw_f_position").hide();
				$("#zw_f_position_select").show();
				$("#zw_f_position_select").append('<option value="'+2+'">'+2+'</option>');   
				$("#zw_f_position_select").append('<option value="'+3+'">'+3+'</option>');
				$("#zw_f_position_select").val($("#zw_f_position").val());
			}
			//栏目类型为，商品推荐，三栏两行，两栏两行时链接类型固定为“商品详情”
			if (columnType == '4497471600010008'
				||columnType == '4497471600010013'
					||columnType == '4497471600010014'
						||columnType == '4497471600010025') {
				/*$("#zw_f_showmore_linktype").val('4497471600020004');*/
				$("#zw_f_showmore_linktype").attr("disabled", true);
				$("#showmore_linktype").attr("disabled", false);
				$("#showmore_linktype").val($("#zw_f_showmore_linktype").val());// 商品详情传值到后台
				$("#zw_f_picture").parent().parent().hide();
				$("#zw_f_picture").removeAttr("name");
			}
			if(columnType != '4497471600010015'){ //楼层
				$("#zw_f_floor_model").parent().parent().hide();
				$("#zw_f_floor_model").val("");
			}
			
			// 一栏多行 屏蔽无关字段
			if(columnType == '4497471600010025'||columnType == '4497471600010035'){
				$("#zw_f_title").parent().parent().hide();
				$("#zw_f_title_color").parent().parent().hide();
				$("#zw_f_description").parent().parent().hide();
				$("#zw_f_description_color").parent().parent().hide();
			}
			
			if(columnType == '4497471600010017'){ 			//【视频播放列表】相关
				var sp_box = '<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box_for_radio()">';  
				$("#zw_f_select_product").parent().parent().show();  
				// .find("input").remove().append(sp_box)
				var div_ = $("#zw_f_select_product").parent();
				$("#zw_f_select_product").remove();
				$(div_).append(sp_box);
				
				$("#zw_f_product_name").attr("readonly", "readonly").parent().parent().show();                           
				$("#zw_f_product_price").attr("readonly", "readonly").parent().parent().show();

				// 根据原型图 重置此项
				var url_ = $("#zw_f_showmore_linktype option")[0].outerHTML;  
				$("#zw_f_showmore_linktype option").remove();
				$("#zw_f_showmore_linktype").append(url_);
				
				// 根据原型图 隐藏如下6项
				$("#zw_f_skip_place").parent().parent().hide();
				$("#zw_f_place_time").parent().parent().hide();
				$("#zw_f_title").parent().parent().hide();
				$("#zw_f_title_color").parent().parent().hide();
				$("#zw_f_description").parent().parent().hide();
				$("#zw_f_description_color").parent().parent().hide();
				
				
				// 加入必填标识 红色*  <span class="w_regex_need">*</span>
				var url_need = '<span class="w_regex_need">*</span>' + $("#linkvalueDiv label")[0].innerText;
				$("#linkvalueDiv").find("label").text("").append(url_need);  
				var pn_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_name").parent().parent().show().find("label").text();                     
				$("#zw_f_product_name").parent().parent().show().find("label").text("").append(pn_need); 
				var pp_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_price").parent().parent().show().find("label").text();
				$("#zw_f_product_price").parent().parent().show().find("label").text("").append(pp_need);
				
				
			}else{
				$("#zw_f_select_product").parent().parent().hide();
				$("#zw_f_product_name").parent().parent().hide();
				$("#zw_f_product_price").parent().parent().hide();
//				$("#zw_f_product_code").val("");
			}
			
			if(columnType == '4497471600010020'){
				/*$("#zw_f_showmore_linktype").val('4497471600020004');*/
				$("#zw_f_showmore_linktype").attr("disabled", true);
				$("#showmore_linktype").attr("disabled", false);
				$("#showmore_linktype").val($("#zw_f_showmore_linktype").val());// 商品详情传值到后台
//				$("#zw_f_start_time").parent().parent().hide(); 
//				$("#zw_f_end_time").parent().parent().hide();
				$("#linkvalueDiv").remove();
				var sp_box = '<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box_for_radio()"><input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden">';  
				$("#zw_f_select_product").parent().parent().show();  
				var div_ = $("#zw_f_select_product").parent();
				$("#zw_f_select_product").remove();
				$(div_).append(sp_box);
				
				$("#zw_f_product_name").attr("readonly", "readonly").parent().parent().show();                           
				$("#zw_f_product_price").attr("readonly", "readonly").parent().parent().show();
				$("#zw_f_product_code").attr("readonly", "readonly").parent().parent().show();				
				$("#zw_f_video_ad").parent().parent().show();
				$("#zw_f_video_link").parent().parent().show();		
				
				// 根据原型图 隐藏如下4项
				$("#zw_f_title").parent().parent().hide();
				$("#zw_f_title_color").parent().parent().hide();
				$("#zw_f_description").parent().parent().hide();
				$("#zw_f_description_color").parent().parent().hide();
				$("#zw_f_position").parent().parent().hide();
				$("#zw_f_position").val("1");
				
				var pn_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_name").parent().parent().show().find("label").text();                     
				$("#zw_f_product_name").parent().parent().show().find("label").text("").append(pn_need); 
				var pp_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_price").parent().parent().show().find("label").text();
				$("#zw_f_product_price").parent().parent().show().find("label").text("").append(pp_need);
				var pn_need = $("#zw_f_video_ad").parent().parent().show().find("label").text();                     
				$("#zw_f_video_ad").parent().parent().show().find("label").text("").append(pn_need); 
				var pp_need = '<span class="w_regex_need">*</span>' + $("#zw_f_video_link").parent().parent().show().find("label").text();
				$("#zw_f_video_link").parent().parent().show().find("label").text("").append(pp_need);
			}
			if(columnType == '4497471600010031'){
				$("#zw_f_picture").parent().parent().hide();
				$("#zw_f_picture").removeAttr("name");
				$("#zw_f_showmore_linktype").val('4497471600020004');
				$("#zw_f_showmore_linktype").attr("disabled", true);
				$("#showmore_linktype").attr("disabled", false);
				$("#showmore_linktype").val($("#zw_f_showmore_linktype").val());// 商品详情传值到后台
				$("#linkvalueDiv").remove();
				var sp_box = '<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box_for_radio()"><input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden">';  
				$("#zw_f_select_product").parent().parent().show();  
				var div_ = $("#zw_f_select_product").parent();
				$("#zw_f_select_product").remove();
				$(div_).append(sp_box);	
				$("#zw_f_product_name").attr("readonly", "readonly").parent().parent().show();                           
				$("#zw_f_product_price").attr("readonly", "readonly").parent().parent().show();
				$("#zw_f_product_code").attr("readonly", "readonly").parent().parent().show();
				$("#zw_f_video_ad").parent().parent().show();
				$("#zw_f_video_link").parent().parent().show();		

				// 根据原型图 隐藏如下4项
				$("#zw_f_title").parent().parent().hide();
				$("#zw_f_title_color").parent().parent().hide();
				$("#zw_f_description").parent().parent().hide();
				$("#zw_f_description_color").parent().parent().hide();
				
				var pn_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_name").parent().parent().show().find("label").text();                     
				$("#zw_f_product_name").parent().parent().show().find("label").text("").append(pn_need); 
				var pp_need = '<span class="w_regex_need">*</span>' + $("#zw_f_product_price").parent().parent().show().find("label").text();
				$("#zw_f_product_price").parent().parent().show().find("label").text("").append(pp_need);
				var pn_need = $("#zw_f_video_ad").parent().parent().show().find("label").text();                     
				$("#zw_f_video_ad").parent().parent().show().find("label").text("").append(pn_need); 

			}
		},
		
		initShowmore:function(){
			
			
			var linktype = $("#zw_f_showmore_linktype").val();//所选试用类型的值
			var linkValue = $("#zw_f_showmore_linkvalue").val();
			if (linkValue == "直播页面"){
				linkValue = "";
			}
			
			if(linktype == "4497471600020001"  && editColumnContent.columnType != "4497471600010017"){ //URL
//				$("#zw_f_product_code_select").parent().parent().parent().hide();
				$("#zw_f_is_share").parent().parent().show();
				$("#zw_f_skip_place").parent().parent().show();
				
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("URL：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div")
					.append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" value="'+ linkValue + '">')
					.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>');
			}else if(linktype == "4497471600020002"){//关键词类型
//				$("#zw_f_product_code_select").parent().parent().parent().hide();
				$("#zw_f_is_share").val("449746250002");//是否分享设为否
				editColumnContent.shareShow();//分享内容
				$("#zw_f_skip_place").val("449746250002");
				editColumnContent.sharePlaceTime();
				$("#zw_f_skip_place").parent().parent().hide();
				$("#zw_f_is_share").parent().parent().hide();
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("关键词：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="'+linkValue+'">');
			}else if(linktype == "4497471600020003"){//分类搜索
//				$("#zw_f_product_code_select").parent().parent().parent().hide();
				if(this.initLinkValue!=""&&this.initLinkValue != linkValue){
					$("#categroyName").val("");
				}
				if($("#categroyName").val()!=""){
					this.initLinkValue = linkValue;
				}
				$("#zw_f_is_share").val("449746250002");//是否分享设为否
				$("#zw_f_skip_place").val("449746250002");
				editColumnContent.sharePlaceTime();
				editColumnContent.shareShow();//分享内容
				$("#zw_f_skip_place").parent().parent().hide();
				$("#zw_f_is_share").parent().parent().hide();
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("分类：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
				$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden" value="'+linkValue+'">');
				$("#zw_f_showmore_linkvalue_text").html($("#categroyName").val());
			}else if(linktype == "4497471600020004"){//商品详情
				$("#linkvalueDiv").show();				
				$("#linkvalueDiv").find("label").text("选择商品：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
				$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden" value="'+linkValue+'">');
				$("#zw_f_is_share").val("449746250002");//是否分享设为否
				$("#zw_f_skip_place").val("449746250002");
				editColumnContent.sharePlaceTime();
				editColumnContent.shareShow();//分享内容
				$("#zw_f_is_share").parent().parent().hide();
				$("#zw_f_skip_place").parent().parent().hide();
			}else if(linktype == "4497471600020005"){//显示浮层
				$("#zw_f_is_share").val("449746250002");//是否分享设为否
				$("#zw_f_skip_place").val("449746250002");
				editColumnContent.sharePlaceTime();
				editColumnContent.shareShow();//分享内容
				$("#zw_f_is_share").parent().parent().hide();
				$("#zw_f_skip_place").parent().parent().hide();
				$("#linkvalueDiv").hide();
			}     else if    (linktype == "4497471600020007"){// 广告弹窗 3.9.6 需求添加此项 - Yangcl
				//$("#zw_f_is_share").parent().parent().show();       // TODO 这里是否要显示 【是否分享：】 选项，目前默认不显示
				//$("#zw_f_skip_place").parent().parent().show();  // TODO 【跳转对应场次：】是否显示？目前默认不显示

				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("广告弹窗：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				var html_ = '<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" value="'+ linkValue + '">';
				$("#linkvalueDiv").find("div").append(html_);
			}else if(linktype == "4497471600020008") {// 直播页面 3.9.8 需求添加此项 - Yangcl
				$("#linkvalueDiv").show();
//				$("#linkvalueDiv").find("label").text("直播页面：");
				$("#linkvalueDiv").find("label").text("");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				//var html_ = '<input id="zw_f_showmore_linkvalue" type="hidden" name="zw_f_showmore_linkvalue" value="">';
				var html_ = '<input id="zw_f_showmore_linkvalue" type="hidden" name="zw_f_showmore_linkvalue" value="'+ linkValue + '">';
				$("#linkvalueDiv").find("div").append(html_);
			}else if (linktype == "4497471600020014") {//品牌搜索
				$("#linkvalueDiv").show();
				$("#linkvalueDiv").find("label").text("选择品牌：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				// $("#zw_f_product_code_select").parent().parent().parent().show();
				$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择品牌" onclick="ProductPopSelect.show_box_for_brand(\'zw_f_showmore_linkvalue\')">');
				$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden" value="'+linkValue+'">');
				$("#zw_f_is_share").val("449746250002");
				$("#zw_f_skip_place").val("449746250002");
				editColumnContent.shareShow();// 分享内容
				editColumnContent.sharePlaceTime();
				$("#zw_f_is_share").parent().parent().hide();
				$("#zw_f_skip_place").parent().parent().hide();		
			}else if(linktype == "4497471600020016"||linktype == "4497471600020017"){
				//视频列表&直播列表
				$("#linkvalueDiv").find("label").text("");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#zw_f_is_share").parent().parent().hide();
				$("#zw_f_skip_place").parent().parent().hide();
				editColumnContent.shareShow();// 分享内容
				editColumnContent.sharePlaceTime();
				$("#zw_f_title").parent().parent().hide();
				$("#zw_f_title_color").parent().parent().hide();
				$("#zw_f_description").parent().parent().hide();
				$("#zw_f_description_color").parent().parent().hide();
			}else if(linktype == "4497471600020018"){
				$("#linkvalueDiv").find("label").text("兑换码：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div")
						.append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" value="'+ linkValue + '">')
			}else if(linktype == "4497471600020012"){
				if ("4497471600010009" == $("#zw_f_column_type").val()) {		//栏目类型为两栏多行时
					$("#zw_f_skip_place").parent().parent().show();
				}else{
					$("#zw_f_skip_place").parent().parent().hide();
					$("#zw_f_place_time").parent().parent().hide();
				}
	
				$("#zw_f_is_share").parent().parent().show();
				$("#linkvalueDiv").show();
				if(addColumnContent.columnType != "4497471600010017"){  // 规避【视频播放列表】
					$("#linkvalueDiv").find("label").text("PATH：");
				}
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div")
						.append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" value="">')
						.append('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(PATH 地址为：“XXX|APPID”，APPID可为空，XXX为小程序打开路径)</span>');
			}
			
		},
		shareShow:function(){//分享内容
			if($("#zw_f_is_share").val() == "449746250001"){//是
				$("#zw_f_share_pic").parent().parent().find("label").html('<span class="w_regex_need">*</span>分享图片:');
				$("#zw_f_share_pic").attr("zapweb_attr_regex_id","469923180002");
				$("#zw_f_share_title").parent().parent().find("label").html('<span class="w_regex_need">*</span>分享标题:');
				$("#zw_f_share_title").attr("zapweb_attr_regex_id","469923180002");
				$("#zw_f_share_content").parent().parent().find("label").html('<span class="w_regex_need">*</span>分享内容:');
				$("#zw_f_share_content").attr("zapweb_attr_regex_id","469923180002");
				$("#zw_f_share_link").parent().parent().find("label").html('<span class="w_regex_need">*</span>分享链接:');
				$("#zw_f_share_link").attr("zapweb_attr_regex_id","469923180002");
				$("#zw_f_share_pic").parent().parent().show();
				$("#zw_f_share_title").parent().parent().show();
				$("#zw_f_share_content").parent().parent().show();
				$("#zw_f_share_link").parent().parent().show();
			}else{
				$("#zw_f_share_pic").parent().parent().find("label").html('分享图片:');
				$("#zw_f_share_pic").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_share_title").parent().parent().find("label").html('分享标题:');
				$("#zw_f_share_title").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_share_content").parent().parent().find("label").html('分享内容:');
				$("#zw_f_share_content").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_share_link").parent().parent().find("label").html('分享链接:');
				$("#zw_f_share_link").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_share_pic").parent().parent().hide();
				$("#zw_f_share_title").parent().parent().hide();
				$("#zw_f_share_content").parent().parent().hide();
				$("#zw_f_share_link").parent().parent().hide();
			}
		},
		sharePlaceTime : function(){
			if ($("#zw_f_skip_place").val() == "449746250001") {	//是
				$("#zw_f_place_time").parent().parent().show();
			}else{
				$("#zw_f_place_time").parent().parent().hide();
			}
		},
		shareLinkType : function(){
			if($("#zw_f_floor_model").length > 0) {
				if ($("#zw_f_floor_model").val() == "4497471600220005") {
					/*$("#zw_f_showmore_linktype").val('4497471600020004');*/
					$("#zw_f_showmore_linktype").attr("disabled", true);
					$("#showmore_linktype").attr("disabled", false);
					$("#showmore_linktype").val($("#zw_f_showmore_linktype").val());// 商品详情传值到后台
				}else{
					$("#zw_f_showmore_linktype").attr("disabled", false);
					$("#showmore_linktype").val($("#zw_f_showmore_linktype").val());
					$("#showmore_linktype").attr("disabled", true);
				}
			}
		},
		beforeSubmit:function(){
			var zw_f_showmore_linktype = $("#zw_f_showmore_linktype").val();
//			var showText = 'URL/关键词/分类/商品';
			if(zw_f_showmore_linktype == "4497471600020001"){//URL
//				showText = 'URL';
				var lv = $("#zw_f_showmore_linkvalue").val();
				if(lv != "" && lv.toLowerCase().indexOf("http://share") != -1 ){
					zapjs.f.message('URL中不能包含"http://share"');
					return false;
				}
				$("#zw_f_showmore_linkvalue").val($("#zw_f_showmore_linkvalue").val().trim());
			}else if(zw_f_showmore_linktype == "4497471600020002"){//关键词类型
//				showText = '关键词';
			}else if(zw_f_showmore_linktype == "4497471600020003"){//分类搜索
//				showText = '分类';
			}else if(zw_f_showmore_linktype == "4497471600020004"){//商品详情
//				showText = '选择商品';
//				$("#zw_f_showmore_linkvalue").val($("#zw_f_product_code_select").val());
				if($("#zw_f_video_ad").length > 0){
					if ($("#zw_f_video_ad").val().length > 13) {
						zapjs.f.message('视频广告语长度不能大于13个字符');
						return false;
					}
					if ($("#zw_f_video_ad").val().indexOf('"') >= 0) {
						var temp = $("#zw_f_video_ad").val();
						var reg = new RegExp("\"","g");
						temp = temp.replace(reg,"“");
						$("#zw_f_video_ad").val(temp);
					}
				}
				
				if(editColumnContent.columnType  == '4497471600010020'||editColumnContent.columnType  == '4497471600010031') {
					$("#zw_f_showmore_linkvalue").val($("#zw_f_product_code").val());
				}
				
				if(editColumnContent.columnType  == '4497471600010001' // 轮播广告
					|| editColumnContent.columnType  == '4497471600010002' // 一栏广告
					|| editColumnContent.columnType  == '4497471600010003' // 二栏广告
					|| editColumnContent.columnType  == '4497471600010004' // 导航栏
					|| editColumnContent.columnType  == '4497471600010005' // 一栏推荐
					|| editColumnContent.columnType  == '4497471600010006' // 右两栏推荐
					|| editColumnContent.columnType  == '4497471600010007' // 左两栏推荐
					|| editColumnContent.columnType  == '4497471600010008' // 商品推荐
					|| editColumnContent.columnType  == '4497471600010009' // 两栏多行推荐
					|| editColumnContent.columnType  == '4497471600010012' // 通知模板
					|| editColumnContent.columnType  == '4497471600010013' //三栏两行推荐
					|| editColumnContent.columnType  == '4497471600010014' //两栏两行推荐
					|| editColumnContent.columnType  == '4497471600010021' //多栏广告
					|| editColumnContent.columnType  == '4497471600010031' //一栏多行通用大图
				){
					var linkvalue = $("#zw_f_showmore_linkvalue").val();
					if (linkvalue.length <= 0) {
						zapjs.f.message('请选择商品!');
						return false;
					}else{
						if(isNaN(linkvalue)){						
							zapjs.f.message('请选择商品!');
							return false;
						}
					}
				}
			}else if(zw_f_showmore_linktype == "4497471600020008"){
				$("#linkvalueDiv").show();
//				$("#linkvalueDiv").find("label").text("直播页面：");
				$("#linkvalueDiv").find("label").text("");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				var html_ = '<input id="zw_f_showmore_linkvalue" type="hidden" name="zw_f_showmore_linkvalue" value="">';
				$("#linkvalueDiv").find("div").append(html_);
			}else if (zw_f_showmore_linktype == "4497471600020014") {// 品牌搜索
				// 导航栏
				if(editColumnContent.columnType  == '4497471600010004' ){
					var linkvalue = $("#zw_f_showmore_linkvalue").val();
					if (linkvalue.length <= 0) {
						zapjs.f.message('请选择品牌!');
						return false;
					}else{
						if(isNaN(linkvalue)){						
							zapjs.f.message('请选择品牌!');
							return false;
						}
					}
				}
			}else if(zw_f_showmore_linktype == "4497471600020012"){
				var linkvalue = $("#zw_f_showmore_linkvalue").val();
					if (linkvalue.length <= 0) {
						zapjs.f.message('请填写跳转链接!');
						return false;
					}
			}
			//不加此句时保存到后台的showmore_linkvalue为空。原因为后台视图里面product_code_select字段关联了数据库字段showmore_linkvalue
//			$("#zw_f_product_code_select").val($("#zw_f_showmore_linkvalue").val());
//			if (!zapjs.zw.validate_field('#zw_f_showmore_linkvalue', '469923180002', showText)) {
//				return false;
//			}
			if($("#zw_f_is_share").val() == "449746250001"){//是否分享为是
				if($("#zw_f_share_title").val().length > 25){
					zapjs.f.message('分享标题长度不能大于25个字符');
					return false;
				}
				if($("#zw_f_share_content").val().length > 30){
					zapjs.f.message('分享内容长度不能大于30个字符');
					return false;
				}
			}else if($("#zw_f_is_share").val() == "449746250002"){//是否分享为否
				$("#zw_f_share_pic").val("");
				$("#zw_f_share_title").val("");
				$("#zw_f_share_content").val("");
				$("#zw_f_share_link").val("");
			}
			
			if ($("#zw_f_title").val().length > 40) {
				zapjs.f.message('标题长度不能大于40个字符');
				return false;
			}
			if ($("#zw_f_description").val().length > 200) {
				zapjs.f.message('描述长度不能大于200个字符');
				return false;
			}
			return true;
		}
};

$(document).ready(function(){
	//楼层
	$("#zw_f_floor_model").change(function() {
		editColumnContent.shareLinkType();
		editColumnContent.initShowmore();
	});	
	//位置赋值
	$("#zw_f_position_select").change(function(){
		$("#zw_f_position").val($("#zw_f_position_select").val());
	});
	//二级联动，切换分类时调用
	$("#zw_f_showmore_linktype").change(function(){
		editColumnContent.initShowmore();
		//保留原有数据
/*		//切换链接类型，数据置空
		zapweb_upload.change_index('zw_f_picture',0,'delete')
		
		var inputElem = $("input");
		var selectElem = $("select");
		//清空待添项
		for(var i = 0;i<inputElem.length;i++){
			if($(inputElem[i]).attr("id")!="zw_f_uid"&&
			   $(inputElem[i]).attr("id")!="zw_f_column_code"&&
			   $(inputElem[i]).attr("id")!="showmore_linktype"&&
			   $(inputElem[i]).attr("id")!="zw_f_column_type"&&  
			   $(inputElem[i]).attr("id")!="zw_f_product_price"&& 
			   $(inputElem[i]).attr("type")!="button"){
				 if( $(inputElem[i]).attr("id")=="zw_f_position"){
					 $(inputElem[i]).val("1"); 
				 }
				 else{
					 $(inputElem[i]).val(""); 
				 }
				 
			}
		}
		//select默认选择第一个
		for(var i = 0;i<selectElem.length;i++){
			if($(selectElem[i]).attr("id")!="zw_f_showmore_linktype"){
				if($(selectElem[i]).attr("id")=="zw_f_is_share"){
					$(selectElem[i]).val($(selectElem[i][1]).val());
				}
				
				else{
					$(selectElem[i]).val($(selectElem[i][0]).val());
				}
			}
		}*/

	});
	//切换分享内容
	$("#zw_f_is_share").change(function(){
		editColumnContent.shareShow();//分享内容
	});
	// 切换跳转对应场次
	$("#zw_f_skip_place").change(function() {
		editColumnContent.sharePlaceTime();
	});
//	alert($("#zw_f_product_code").val());
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/editColumnContent", function() {
		return editColumnContent;
	});
}
