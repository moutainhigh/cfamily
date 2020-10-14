<#-- 接受列表页面的传值 column_type -->
<#assign column_type = b_page.getReqMap()["column_type"]?default("")>

<@m_zapmacro_common_page_edit b_page />
<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
	  		<@m_zapmacro_common_auto_field e e_page/>
	</#list>
	</#if>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if column_type == "4497471600010025" 
			&& (e_field.getFieldName()=="picture"
				|| e_field.getFieldName()=="title"
				|| e_field.getFieldName()=="description"
				|| e_field.getFieldName()=="title_color"
				|| e_field.getFieldName()=="description_color"
				|| e_field.getFieldName()=="video_link")>
			<#-- 一栏多行屏蔽图片 -->
		<#elseif e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
<#if e_field.getFieldName()=="showmore_linkvalue">
		<div id="linkvalueDiv" class="control-group">
		<script type="text/javascript">
	     zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_showmore_linkvalue","value":"","max":"1"});});
	     
	   </script>
			<label class="control-label" for="zw_f_showmore_linkvalue">
				URL：
			</label>
			<div class="controls">
				<input id="zw_f_showmore_linkvalue" type="text" value="${e_field.getPageFieldValue()}" name="zw_f_showmore_linkvalue">
			</div>
		</div>
	<#else>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	
		<#if e_field.getFieldName()=="position">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<select name="zw_f_position_select" id="zw_f_position_select" style="display:none">
				<option value="1">1</option>
			</select>
		<#elseif  e_field.getFieldName()=="title_color">
			<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" >
				<option value="#333333" style="color:#333333" <#if  e_field.getPageFieldValue()=="#333333"> selected="selected" </#if>>黑色</option>
				<option value="#FFFFFF" style="color:#FFFFFF;" <#if  e_field.getPageFieldValue()=="#FFFFFF"> selected="selected" </#if>>白色</option>
				<option value="#53359e" style="color:#53359e" <#if  e_field.getPageFieldValue()=="#53359e"> selected="selected" </#if>>紫色</option>
				<option value="#fd7f03" style="color:#fd7f03" <#if  e_field.getPageFieldValue()=="#fd7f03"> selected="selected" </#if>>橘色</option>
				<option value="#d80c18" style="color:#d80c18" <#if  e_field.getPageFieldValue()=="#d80c18"> selected="selected" </#if>>红色</option>
				<option value="#6bbd00" style="color:#6bbd00" <#if  e_field.getPageFieldValue()=="#6bbd00"> selected="selected" </#if>>绿色</option>
			</select>
		<#elseif  e_field.getFieldName()=="description_color">
			<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" >
				<option value="#999999" style="color:#999999" <#if  e_field.getPageFieldValue()=="#999999"> selected="selected" </#if>>灰色</option>
				<option value="#FFFFFF" style="color:#FFFFFF" <#if  e_field.getPageFieldValue()=="#FFFFFF"> selected="selected" </#if>>白色</option>
			</select>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
	</#if>
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
				<#if e_field.getFieldName()=="showmore_linktype">
					<#if e_field.getPageFieldValue()=="4497471600020003">
						<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
						<#assign categroyCode=b_method.upFiledByFieldName(b_page.upBookData(),"showmore_linkvalue").getPageFieldValue() /> 
						<#assign categoryMap=sellercategoryService.getCateGoryShow(categroyCode,"SI2003")>
						<input id="categroyName" type="hidden" value= "${categoryMap.categoryName}">
					</#if>
				</#if>
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<script>
	$(document).ready(function(){
		defaultHidden();
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
		
		var linktype = $("#zw_f_showmore_linktype").val();//所选试用类型的值
		var linkValue = $("#zw_f_showmore_linkvalue").val();
		if(linktype == "4497471600020001"){//URL
			$("#zw_f_is_share").parent().parent().show();
			$("#zw_f_skip_place").parent().parent().show();
			$("#linkvalueDiv").show();
			$("#linkvalueDiv").find("label").text("URL：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="'+linkValue+'">');
		}else if(linktype == "4497471600020002"){//关键词类型
			$("#zw_f_is_share").parent().parent().hide();
			$("#zw_f_skip_place").parent().parent().hide();
			$("#linkvalueDiv").show();
			$("#linkvalueDiv").find("label").text("关键词：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" type="text" name="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" value="'+linkValue+'">');
		}else if(linktype == "4497471600020003"){//分类搜索
			$("#zw_f_is_share").parent().parent().hide();
			$("#zw_f_skip_place").parent().parent().hide();
			$("#linkvalueDiv").show();
			$("#linkvalueDiv").find("label").text("分类：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url(\'../show/page_chart_v_uc_sellercategory_select\')">');
			$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
			$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden">');
			$("#zw_f_showmore_linkvalue_text").text($("#categroyName").val());
		}else if(linktype == "4497471600020004"){//商品详情
			$("#linkvalueDiv").find("label").text("选择商品：");
			$("#linkvalueDiv").find("div").find("input").remove();
			$("#linkvalueDiv").find("div").find("span").remove();
			$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择商品" onclick="ProductPopSelect.show_box(\'zw_f_showmore_linkvalue\')">');
			$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
			$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden" value="'+linkValue+'">');
			$("#zw_f_is_share").parent().parent().hide();
			$("#zw_f_skip_place").parent().parent().hide();
		}else if (linktype == "4497471600020014") {//品牌搜索
				$("#linkvalueDiv").find("label").text("选择品牌：");
				$("#linkvalueDiv").find("div").find("input").remove();
				$("#linkvalueDiv").find("div").find("span").remove();
				$("#linkvalueDiv").find("div").append('<input class="btn" type="button" value="选择品牌" onclick="ProductPopSelect.show_box_for_brand(\'zw_f_showmore_linkvalue\')">');
				$("#linkvalueDiv").find("div").append('<span id="zw_f_showmore_linkvalue_text"></span>');
				$("#linkvalueDiv").find("div").append('<input id="zw_f_showmore_linkvalue" name="zw_f_showmore_linkvalue" type="hidden" value="'+linkValue+'">');
				$("#zw_f_is_share").parent().parent().hide();
				$("#zw_f_skip_place").parent().parent().hide();
				
		}else if(linktype == "4497471600020005"){//显示浮层
			$("#zw_f_is_share").parent().parent().hide();
			$("#zw_f_skip_place").parent().parent().hide();
			$("#linkvalueDiv").hide();
			}
		if($("#zw_f_is_share").val() == "449746250002"){//否
			$("#zw_f_share_pic").parent().parent().hide();
			$("#zw_f_share_title").parent().parent().hide();
			$("#zw_f_share_content").parent().parent().hide();
			$("#zw_f_share_link").parent().parent().hide();
		}
		if($("#zw_f_skip_place").val() == "449746250002"){//否
			$("#zw_f_place_time").parent().parent().hide();
		}
		$("form input").attr("disabled","disabled");
		$("form select").attr("disabled","disabled");
		$("form textarea").attr("disabled","disabled");
		
		if('${column_type}' == '4497471600010017'){  // 由【视频播放列表】模板跳转而来
			$("#zw_f_skip_place").parent().parent().hide();
			$("#zw_f_title").parent().parent().hide(); 
			$("#zw_f_title_color").parent().parent().hide(); 
			$("#zw_f_description").parent().parent().hide(); 
			$("#zw_f_description_color").parent().parent().hide(); 
			$("#zw_f_product_name").parent().parent().show();
			$("#zw_f_product_code").parent().parent().show();
			$("#zw_f_product_price").parent().parent().show();  
		}
		
		if("${column_type}" == '4497471600010020'){  // 由【视频播放模板】模板跳转而来
			$("#zw_f_skip_place").parent().parent().hide();
			$("#zw_f_title").parent().parent().hide(); 
			$("#zw_f_title_color").parent().parent().hide(); 
			$("#zw_f_description").parent().parent().hide(); 
			$("#zw_f_description_color").parent().parent().hide(); 
			$("#zw_f_product_name").parent().parent().show();
			$("#zw_f_product_code").parent().parent().show();
			$("#zw_f_product_price").parent().parent().show(); 
			$("#zw_f_video_ad").parent().parent().show();
			$("#zw_f_video_link").parent().parent().show(); 
		}
		
		if("${column_type}" == '4497471600010031'){  // 由【一栏多行通用大图】模板跳转而来
		    $("#zw_f_picture").parent().parent().hide();
			$("#zw_f_skip_place").parent().parent().hide();
			$("#zw_f_title").parent().parent().hide(); 
			$("#zw_f_title_color").parent().parent().hide(); 
			$("#zw_f_description").parent().parent().hide(); 
			$("#zw_f_description_color").parent().parent().hide(); 
			$("#zw_f_product_name").parent().parent().show();
			$("#zw_f_product_code").parent().parent().show();
			$("#zw_f_product_price").parent().parent().show(); 
			$("#zw_f_video_ad").parent().parent().show();
			$("#zw_f_video_link").parent().parent().show(); 
		}
		
		if("${column_type}" == '4497471600010035'){  // 由【滑动轮播】模板跳转而来
			$("#zw_f_title").parent().parent().hide(); 
			$("#zw_f_title_color").parent().parent().hide(); 
			$("#zw_f_description").parent().parent().hide(); 
			$("#zw_f_description_color").parent().parent().hide(); 

		}
		
	});
	
	// 数据初始化时候默认隐藏的内容加到这个方法中，然后调用initPosition方法选择性的展示 - Yangcl
	function defaultHidden(){
		// 隐藏【视频播放列表】相关字段
		
		$("#zw_f_select_product").parent().parent().hide();
		$("#zw_f_product_name").parent().parent().hide();
		$("#zw_f_product_code").parent().parent().hide();
		$("#zw_f_product_price").parent().parent().hide();
		$("#zw_f_video_ad").parent().parent().hide();
		$("#zw_f_video_link").parent().parent().hide();
		// 隐藏【……】相关字段
	}
</script>