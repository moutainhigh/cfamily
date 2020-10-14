var add_dlq_info = { 
		init:function(){
			
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',add_dlq_info.beforeSubmit);
			
			$("#zw_f_is_share").change(function(){
				var obj_val = $(this).val();
				
				if (obj_val == "449747800001") {// 是
					$("#zw_f_share_img").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享图片:');
					$("#zw_f_share_img").attr("zapweb_attr_regex_id", "469923180002");
					$("#zw_f_share_title").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享标题:');
					$("#zw_f_share_title").attr("zapweb_attr_regex_id", "469923180002");
					$("#zw_f_share_content").parent().parent().find("label").html(
							'<span class="w_regex_need">*</span>分享内容:');
					$("#zw_f_share_content").attr("zapweb_attr_regex_id",
							"469923180002");
					$("#zw_f_share_img").parent().parent().show();
					$("#zw_f_share_title").parent().parent().show();
					$("#zw_f_share_content").parent().parent().show();
				} else {
					$("#zw_f_share_img").parent().parent().find("label").html('分享图片:');
					$("#zw_f_share_img").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_title").parent().parent().find("label")
							.html('分享标题:');
					$("#zw_f_share_title").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_content").parent().parent().find("label").html(
							'分享内容:');
					$("#zw_f_share_content").removeAttr("zapweb_attr_regex_id");
					$("#zw_f_share_img").parent().parent().hide();
					$("#zw_f_share_title").parent().parent().hide();
					$("#zw_f_share_content").parent().parent().hide();
				}
			})
			
			$("#zw_f_is_share").trigger("change");
			
		},
		/**
		 * 
		 * @param flg 标识栏目类型
		 */
		show_windows : function(flg){ 
			var savetarget ="";
			if(flg == 2) {
				savetarget = "parent.add_dlq_info.saveProduct2";
			} else if(flg == 5){
				var zw_f_programa_name5 = $(":text[name='zw_f_programa_name5']").val();
				var zw_f_programa_english5 = $(":text[name='zw_f_programa_english5']").val();
				if(zw_f_programa_name5.length <= 0) {
					zapjs.f.message('请填写对应栏目名称');
					return false;
				}
				if(zw_f_programa_english5.length <= 0) {
					zapjs.f.message('请填写对应英文');
					return false;
				}
				savetarget = "parent.add_dlq_info.saveProduct5";
			}
			zapjs.f.window_box({
				id : 'as_productlist_productids',
				content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=1&zw_s_iframe_select_callback='+savetarget+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
		},
		/**
		 * 
		 * @param sId 
		 * @param sVal 商品编号
		 * @param a 商品名称
		 * @param b
		 * @param c
		 * @returns {Boolean}
		 */
		saveProduct2 : function(sId, sVal,a,b,c){
			//展示选择的商品
			$("#N2AddProductShowTime").html('<span style="padding: 5px 15px; border: 1px solid #008299;">'+a+'<input type="hidden" name="zw_f_common_number" value="'+sVal+'"/></span>');
			zapjs.f.window_close(sId);
			
		},
		/**
		 * 
		 * @param sId 
		 * @param sVal 商品编号
		 * @param a 商品名称
		 * @param b
		 * @param c
		 * @returns {Boolean}
		 */
		saveProduct5 : function(sId, sVal,a,b,c){
			
			var zw_f_programa_name5 = $(":text[name='zw_f_programa_name5']").val();
			var zw_f_programa_english5 = $(":text[name='zw_f_programa_english5']").val();
			
			var zw_f_page_number = $("#zw_f_page_number").val();
			var zw_f_tv_number = $("#zw_f_tv_number").val();
			var obj = {};
			obj.paramType = "1001";
			obj.common_number = sVal;
			obj.page_number = zw_f_page_number;
			obj.id_number = "N5";
			obj.programa_name = zw_f_programa_name5;
			obj.programa_english = zw_f_programa_english5;
			obj.tv_number = zw_f_tv_number;
			
			zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
				if(result.resultCode==1){
					$("#showN5SelectedProduct").append('<li style="position: relative;margin-left:9px;margin-botton:9px">'+a+'<i zapweb_attr_operate_id="0b346f9ed93d11e5aad9005056925439" style="position: absolute;background: #f1f1f1;border: solid 1px #ccc;text-align: center;  right: -10px;top: -10px;font-style:normal;  width: 15px;height: 15px;line-height: 15px;cursor: pointer;display: inline-block;" onclick="add_dlq_info.delZGProduct(this,\''+result.uid+'\')">X</i></li>');
					zapadmin.model_message('添加商品成功！');
					zapjs.f.window_close(sId);
				}else{
					zapadmin.model_message('添加商品失败');
				}
			});
				
				
		},
		//添加内容总提交fun
		subAddContent :function(obje,flg) {
			var obj = {};
			var zw_f_page_number = $("#zw_f_page_number").val();
			var zw_f_tv_number = $("#zw_f_tv_number").val();
			
			if(flg == "N1") {
				var tval1 = $(obje).parent().parent().find(":text:eq(0)").val();
				var tval2 = $(obje).parent().parent().find(":text:eq(1)").val();
				var zw_f_programa_name1 = $(":text[name='zw_f_programa_name1']").val();
				var zw_f_programa_english1 = $(":text[name='zw_f_programa_english1']").val();
				if(tval1.length <= 0) {
					zapjs.f.message('请填写食材名');
					return false;
				}
				if(tval2.length <= 0) {
					zapjs.f.message('请填写份量');
					return false;
				}
				if(zw_f_programa_name1.length <= 0) {
					zapjs.f.message('请填写对应栏目名称');
					return false;
				}
				if(zw_f_programa_english1.length <= 0) {
					zapjs.f.message('请填写对应英文');
					return false;
				}
				obj.paramType = "1001";
				obj.id_number = "N1";
				obj.page_number = zw_f_page_number;
				obj.food_name = tval1;
				obj.weight = tval2;
				obj.programa_name = zw_f_programa_name1;
				obj.programa_english = zw_f_programa_english1;
				obj.tv_number = zw_f_tv_number;
				zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
					if(result.resultCode==1){
						zapjs.f.autorefresh();
					}else{
						zapadmin.model_message('提交失败');
					}
				});
				
			} else if(flg == "N2") {
				
				var zw_f_programa_name2 = $(":text[name='zw_f_programa_name2']").val();
				var formObj = $(obje).parent().parent().parent();
				var zw_f_picture = formObj.find(":hidden[name='zw_f_picture']").val();
				var zw_f_location = formObj.find(":text[name='zw_f_location']").val();
				var zw_f_common_number = formObj.find(":hidden[name='zw_f_common_number']").val();
				if(zw_f_picture.length <= 0) {
					zapjs.f.message('请选择图片');
					return false;
				} 
				if(undefined == zw_f_common_number ||zw_f_common_number.length <= 0) {
					zapjs.f.message('请选择商品');
					return false;
				}
				
				obj.common_number = zw_f_common_number;
				obj.paramType = "1001";
				obj.programa_name = zw_f_programa_name2;
				obj.id_number = "N2";
				obj.page_number = zw_f_page_number;
				obj.picture = zw_f_picture;
				obj.location = zw_f_location;
				obj.tv_number = zw_f_tv_number;
				
				zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
					if(result.resultCode==1){
						zapjs.f.autorefresh();
					}else{
						zapadmin.model_message('添加商品失败');
					}
				});
				
			} else if(flg == "N3") {
				
				var zw_f_programa_name3 = $(":text[name='zw_f_programa_name3']").val();
				var zw_f_programa_english3 = $(":text[name='zw_f_programa_english3']").val();
				var formObj = $(obje).parent().parent().parent();
				var zw_f_picture = formObj.find(":hidden[name='zw_f_picture']").val();
				var zw_f_location = formObj.find(":text[name='zw_f_location']").val();
				var zw_f_co_describe = formObj.find("#zw_f_co_describe").val();
				
				
				if(zw_f_picture.length <= 0) {
					zapjs.f.message('请选择图片');
					return false;
				}
				if( undefined == zw_f_co_describe || zw_f_co_describe.length <= 0) {
					zapjs.f.message('请填写描述内容');
					return false;
				}
				
				obj.paramType = "1001";
				obj.programa_name = zw_f_programa_name3;
				obj.id_number = "N3";
				obj.page_number = zw_f_page_number;
				obj.picture = zw_f_picture;
				obj.location = zw_f_location;
				obj.programa_english = zw_f_programa_english3;
				obj.describe = zw_f_co_describe;
				obj.tv_number = zw_f_tv_number;
				
				zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
					if(result.resultCode==1){
						zapjs.f.autorefresh();
					}else{
						zapadmin.model_message('添加失败');
					}
				});
				
			} else if(flg == "N4") {
				
				var zw_f_programa_name4 = $(":text[name='zw_f_programa_name4']").val();
				
				var formObj = $(obje).parent().parent().parent();
				var zw_f_pic = formObj.find(":hidden[name='zw_f_pic']").val();
				var zw_f_start_time = formObj.find(":text[name='zw_f_start_time']").val();
				var zw_f_end_time = formObj.find(":text[name='zw_f_end_time']").val();
				var zw_f_url = formObj.find(":text[name='zw_f_url']").val();
				var zw_f_location = formObj.find("#zw_f_location").val();
				
				/*var tempflg = "";
				if("449747760001" == zw_f_location) {
					tempflg = "上部";
				} else if("449747760001" == zw_f_location) {
					tempflg = "下部";
				}
				//上下部是否只有一个
				var c4TrArr = $(".c4 tbody tr");
				for(var k=0;k<c4TrArr.length;k++) {
					var aa = c4TrArr.eq(k).find("td:eq(1)").html();
					if(jQuery.trim(aa) == tempflg) {
						zapjs.f.message('已存在'+tempflg+"广告");
						return false;
					}
				}*/
				
				if(zw_f_pic.length <= 0) {
					zapjs.f.message('请选择图片');
					return false;
				}
				if(zw_f_start_time.length <= 0) {
					zapjs.f.message('请填写开始时间');
					return false;
				}
				if(zw_f_end_time.length <= 0) {
					zapjs.f.message('请填写结束时间');
					return false;
				}
				if(zw_f_url.length <= 0) {
					zapjs.f.message('请填写URL');
					return false;
				}
				if(zw_f_location.length <= 0) {
					zapjs.f.message('请填写位置');
					return false;
				}
				if(zw_f_url.length != 0){
					if(!CheckUrl(zw_f_url)) {
						zapjs.f.message('请填写正确的URL');
						return false;
			  		}
					var indx = zw_f_url.indexOf("http://share");
					if( indx >= 0) {
						zapjs.f.message('广告连接不允许带有http://share字样 ');
						return false;
					}
				}
				obj.paramType = "1004";
				obj.programa_name = zw_f_programa_name4;
				obj.id_number = "1000";//1000 : 广告 ； 1001 ：轮播
				obj.page_number = zw_f_page_number;
				obj.gg_pic = zw_f_pic;
				obj.location = zw_f_location;
				obj.gg_url = zw_f_url;
				obj.gg_start_time = zw_f_start_time;
				obj.gg_end_time = zw_f_end_time;
				obj.tv_number = zw_f_tv_number;
				
				zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
					if(result.resultCode==1){
						zapjs.f.autorefresh();
					}else{
						zapadmin.model_message('添加失败');
					}
				});
				
				
				
			} else if (flg == "N5") {
				
				var zw_f_programa_name5 = $(":text[name='zw_f_programa_name5']").val();
				var zw_f_programa_english5 = $(":text[name='zw_f_programa_english5']").val();
				
				var formObj = $(obje).parent().parent().parent();
				var zw_f_picture = formObj.find(":hidden[name='zw_f_picture']").val();
				var zw_f_location = formObj.find(":text[name='zw_f_location']").val();
				var zw_f_common_number = formObj.find(":hidden[name='zw_f_common_number']").val();
				if(zw_f_picture.length <= 0) {
					zapjs.f.message('请选择图片');
					return false;
				} 
				if(undefined == zw_f_common_number ||zw_f_common_number.length <= 0) {
					zapjs.f.message('请选择商品');
					return false;
				}
				
				obj.common_number = zw_f_common_number;
				obj.paramType = "1001";
				obj.programa_name = zw_f_programa_name5;
				obj.programa_english = zw_f_programa_english5;
				obj.id_number = "N5";
				obj.page_number = zw_f_page_number;
				obj.picture = zw_f_picture;
				obj.location = zw_f_location;
				obj.tv_number = zw_f_tv_number;
				
				zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
					if(result.resultCode==1){
						zapjs.f.autorefresh();
					}else{
						zapadmin.model_message('添加商品失败');
					}
				});
				
			
			}
			
		},
		//食材盒批量排序
		resetSort : function (obje) {
			var formObj = $(obje).parents("form");
			var txtArr = formObj.find("tbody :text");
			var str = "";
			for(var i=0;i<txtArr.length;i++) {
				var val = txtArr.eq(i).val();
				var uid = txtArr.eq(i).attr("attr_web_uid");
				str += uid +"-" +val +",";
			}
			var obj = {};
			obj.paramType = '1002';
			obj.sortByUid = str;
			zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
				if(result.resultCode==1){
					zapjs.f.autorefresh();
				}else{
					zapadmin.model_message('排序设置失败');
				}
			});
			
		},
		//添加步骤的限制
		addStep : function () {
			var zw_f_programa_name3 = $(":text[name='zw_f_programa_name3']").val();
			var zw_f_programa_english3 = $(":text[name='zw_f_programa_english3']").val();
			if(zw_f_programa_name3.length <= 0) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			if(zw_f_programa_english3.length <= 0) {
				zapjs.f.message('请填写对应英文');
				return false;
			}
			var zw_f_page_number = $("#zw_f_page_number").val();
			zapadmin.window_url('../show/page_add_v_fh_dlq_content?zw_f_id_number=N3&zw_f_page_number='+zw_f_page_number);
		},
		addGuangg : function (){
			/*var c4TrArr = $(".c4 tbody tr");
			if(c4TrArr.length >= 2) {
				zapjs.f.message('上下部广告分别只允许添加一条，请删除后继续添加');
				return false;
			}*/
			var pageT = $("#zw_f_page_type").val();
			var zw_f_tv_number = $("#zw_f_tv_number").val();
			var zw_f_programa_name4 = $(":text[name='zw_f_programa_name4']").val();
			if(zw_f_programa_name4.length <= 0) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			zapadmin.window_url('../show/page_add_v_fh_dlq_picture?id_number=1000&zw_f_p_type='+pageT+"&zw_f_tv_number="+zw_f_tv_number);
			
		},
		//添加直购商品
		delZGProduct :function(obje,uid) {
			zapjs.zw.func_delete(obje,uid);
		},
		//页面提交
		btnSubmit :function () {
			//判断所属渠道
			var pageT = $("#zw_f_page_type").val();
			var isDLQCLS = false;
			if(pageT == "1000") {//大陆桥
				isDLQCLS = true;
			} 
			
			
			//监测所有必填项
			var zw_f_programa_name1 = $(":text[name='zw_f_programa_name1']").val();
			var zw_f_programa_english1 = $(":text[name='zw_f_programa_english1']").val();
			var zw_f_programa_name2 = $(":text[name='zw_f_programa_name2']").val();
			var zw_f_programa_name3 = $(":text[name='zw_f_programa_name3']").val();
			var zw_f_programa_english3 = $(":text[name='zw_f_programa_english3']").val();
			var zw_f_programa_name4 = $(":text[name='zw_f_programa_name4']").val();
			var zw_f_programa_name5 = $(":text[name='zw_f_programa_name5']").val();
			var zw_f_programa_english5 = $(":text[name='zw_f_programa_english5']").val();
			var zw_f_programa_name6 = $(":text[name='zw_f_programa_name6']").val();
			var zw_f_programa_name7 = $(":text[name='zw_f_programa_name7']").val();
			var zw_f_activity_code8 = $(":text[name='zw_f_activity_code8']").val();
			var zw_f_programa_name9 = "";
			var zw_f_t_describ = "";
			var zw_f_programa_name10 = "";
			var zw_f_column_desc = "";
			if(pageT == "1001") {
				zw_f_programa_name9 = $(":text[name='zw_f_programa_name9']").val();
				zw_f_t_describ = $("#zw_f_t_describ").val();
				zw_f_programa_name10 = $(":text[name='zw_f_programa_name10']").val();
				zw_f_column_desc = $("#zw_f_column_desc").val();
				
				if(zw_f_programa_name9.length <= 0) {
					zapjs.f.message('请填写本期介绍--栏目名称');
					return false;
				}
				if(zw_f_programa_name10.length <= 0) {
					zapjs.f.message('请填写栏目介绍--栏目名称');
					return false;
				}
			}
			if( isDLQCLS && zw_f_programa_name1.length <= 0) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			if(zw_f_programa_name4.length <= 0) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			if( isDLQCLS && zw_f_programa_name7.length <= 0) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			if( zw_f_programa_name3.length <= 0|| zw_f_programa_name5.length <= 0 || zw_f_programa_name6.length <= 0 ) {
				zapjs.f.message('请填写对应栏目名称');
				return false;
			}
			if( isDLQCLS && zw_f_programa_english1.length <= 0) {
				zapjs.f.message('请填写对应英文');
				return false;
			}
			if( zw_f_programa_english3.length <= 0 || zw_f_programa_english5.length <= 0) {
				zapjs.f.message('请填写对应英文');
				return false;
			}
			var zw_f_special_name = $("#zw_f_special_name").val();
			var zw_f_cuisine_name= "";
			if(isDLQCLS) {
				zw_f_cuisine_name = $("#zw_f_cuisine_name").val();
			}
			var zw_f_mark = $("#zw_f_mark").val();
			var zw_f_cuisine_picture = $("#zw_f_cuisine_picture").val();
			var zw_f_url = $("#zw_f_url").val();
			var zw_f_page_sort = $("#zw_f_page_sort").val();
			
			var zw_f_description_pic = $("#zw_f_description_pic").val();
			if(zw_f_special_name.length <= 0) {
				zapjs.f.message('请填写专题名称');
				return false;
			}
			if(zw_f_cuisine_picture.length <= 0) {
				zapjs.f.message('请上传专题图片');
				return false;
			}
			if(isDLQCLS && zw_f_cuisine_name.length <= 0) {//只有是大陆桥才进行校验
				zapjs.f.message('请填写菜系名称');
				return false;
			}
			if(zw_f_page_sort.length <= 0) {
				zapjs.f.message('请填写专题排序');
				return false;
			}
			var zw_f_page_number = $("#zw_f_page_number").val();
			var zw_f_tv_number = $("#zw_f_tv_number").val();
			var zw_f_uid = $("#zw_f_uid").val();
			
			/* 判断是否分享  及分享信息是否完整 */
			var is_share = $("#zw_f_is_share").val();
			var zw_f_share_img = $("#zw_f_share_img").val();
			var zw_f_share_title = $("#zw_f_share_title").val();
			var zw_f_share_content = $("#zw_f_share_content").val();
			if(is_share == "449747800001" ) {//是
				if(zw_f_share_title.length <= 0) {
					zapjs.f.message('请填写分享标题');
					return false;
				}
				if(zw_f_share_content.length <= 0) {
					zapjs.f.message('请填写分享内容');
					return false;
				}
				if(zw_f_share_img.length <= 0) {
					zapjs.f.message('请填写分享图片');
					return false;
				}
			} else {
				$("#zw_f_share_img").val("");
				$("#zw_f_share_img").parent().find(".control-upload").html("");
				
				$("#zw_f_share_title").val("");
				$("#zw_f_share_content").val("");
			}
			
			var obj = {};
			obj.paramType = '1007';
			obj.cls_num = pageT;
			obj.programa_name = zw_f_programa_name1 +"_" +zw_f_programa_name2 +"_"+zw_f_programa_name3 +"_"+zw_f_programa_name4 +"_"+zw_f_programa_name5 +"_"+zw_f_programa_name6 +"_"+zw_f_programa_name7+"_"+zw_f_programa_name9+"_"+zw_f_programa_name10 ;
			obj.programa_english = zw_f_programa_english1 +"_" +zw_f_programa_english3 + "_" + zw_f_programa_english5;
			obj.page_number = zw_f_page_number;
			obj.picture = zw_f_description_pic;
			obj.special_name = zw_f_special_name;
			obj.cuisine_name = zw_f_cuisine_name;
			obj.cuisine_picture = zw_f_cuisine_picture;
			obj.url = zw_f_url;
			obj.uid_str = zw_f_uid;
			obj.tv_number = zw_f_tv_number;
			obj.activity_code = zw_f_activity_code8;
			obj.share_title = zw_f_share_title;
			obj.share_img = zw_f_share_img;
			obj.share_content = zw_f_share_content;
			obj.is_share = is_share;
			obj.mark = zw_f_mark;
			obj.page_sort = zw_f_page_sort;
			if(pageT =="1001") {
				obj.column_desc = zw_f_column_desc;
				obj.t_describ = zw_f_t_describ;
			}
			zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
				if(result.resultCode==1){
					zapjs.zw.modal_show({
						content : '操作成功',
						okfunc : 'zapjs.f.autorefresh()'
					});
					//zapjs.f.autorefresh();
				}else{
					zapadmin.model_message('设置失败');
				}
			});
			
			
		},
		beforeSubmit : function () {
			var obj_val = $("#zw_f_is_share").val();
			if (obj_val == "449747800002") {// 是
				$("#zw_f_share_img").val("");
				$("#zw_f_share_title").val("");
				$("#zw_f_share_content").val("");
			}
			return true;
		}
			
};
function CheckUrl(str) { 
	var RegUrl = new RegExp(); 
	RegUrl.compile("^[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\?\/.=]+$");//jihua.cnblogs.com 
	if (!RegUrl.test(str)) { 
	return false; 
	} 
	return true; 
} 
