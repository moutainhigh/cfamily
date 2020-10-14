
<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>

<#-- 商品属性 -->
<#assign propertiesList = product_support.getPropertiesByProductCode("${uuid}")>

<#assign productMap=b_method.upDataOne("pc_productinfo","","","","product_code",uuid)>
<#assign tplNameMap=b_method.upDataOneOutNull("uc_freight_tpl","*","","is_default = '449746250001' and isDisable = '449746250002'")>
<#-- <#assign areaNameMap=b_method.upDataOneOutNull("sc_area_template","template_name","","template_code = '${productMap['area_template']}'")> -->
<#assign merchant_seller_type=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>

<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_css ["cfamily/css/video-js.min.css"] />
<style>
 .video-js .vjs-big-play-button{
        top:45%!important;
        left:50%!important;
        margin-left:-1.5em!important;
        }
</style>
<script>
// 海外购商品分类编码
var categoryHWG = '${b_page.upConfig('productcenter.category_haiwaigou')}';
require(['cfamily/js/modproductCshopUp','cfamily/js/select2/select2'],
function(p)
{
	//var product={"zid":0,"uid":"","productCode":"","produtName":"","sellerCode":"","brandCode":"","productWeight":0.0,"flagSale":0,"createTime":"","updateTime":"","category":null,"description":null,"pcPicList":null,"pcProductpropertyList":null,"productSkuInfoList":null,"pcProdcutflow":null};
	
	var product=${product_support.upProductInfoJson(uuid)};
	//<#assign manageCode = b_method.upUserInfo().getManageCode()>
	//product.sellerCode='${manageCode}';
	<#if tplNameMap['uid']??>
		cfamily_modproduct.defaultuid = "${tplNameMap['uid']}";
	</#if>
	<#-- 商户全部品类资质 -->
	<#assign qualification = b_method.upDataQueryToJson("pc_seller_qualification","brand_code,category_code,qualification_name,DATEDIFF(SYSDATE(),end_time) expired","","","small_seller_code",productMap["small_seller_code"])>
	<#if qualification??>
	product.qualification = ${qualification};
	<#else>
	product.qualification = [];
	</#if>		
	
	zapjs.f.ready(function()
	{
		p.init_product(product);
		
		<!-- 商品标签 -->
		<#assign autylogListChecked=b_method.upDataBysql("pc_product_authority_logo","select authority_logo_uid from pc_product_authority_logo where product_code = '${uuid}'")>
		<#if autylogListChecked??>
			<#list autylogListChecked as e>
				$('input[name="zw_f_authority_logo"][value="${e["authority_logo_uid"]!""}"]').prop("checked",true);
			</#list>
		</#if>
	});	

});
//商品结算方式，采购类型确认提示
function formConfirm() {
	var smallSellerCode = cfamily_modproduct.product.smallSellerCode;
	<#assign product=product_support.getProduct(uuid)>
	<#assign searchOrderService=b_method.upClass("com.cmall.familyhas.service.SearchOrderService")/>
	<#assign sellerType = searchOrderService.getSellerType(product.getSmallSellerCode())>
	var html = '商户类型为：${sellerType}<br>';
	var purchase_type = $('#zw_f_purchase_type').val();
	var purchase_type_val = '';
	if(purchase_type == '4497471600160001') {
		purchase_type_val = '代销';
	} else if(purchase_type == '4497471600160002') {
		purchase_type_val = '经销';
	} else if(purchase_type == '4497471600160003') {
		purchase_type_val = '代收代付';
	}
	html += '采购类型：' + purchase_type_val + '<br>';
	var settlement_type = $('#zw_f_settlement_type').val();
	var settlement_type_val = '';
	if(settlement_type == '4497471600110001') {
		settlement_type_val = '常规结算';
	} else if(settlement_type == '4497471600110002') {
		settlement_type_val = '特殊结算';
	} else if(settlement_type == '4497471600110003') {
		settlement_type_val = '服务费结算';
	}
	html += '结算方式：' + settlement_type_val + '<br>';
	html += '是否确认修改?';

	var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
	$(document.body).append(sModel);
	$('#zapjs_f_id_modal_message').html('<div class="w_p_20">' + html + '</div>');
	var aButtons = [];
	aButtons.push({
		text: '是',
		handler: function() {
			$('#zapjs_f_id_modal_message').dialog('close');
			$('#zapjs_f_id_modal_message').remove();
			zapjs.zw.func_edit(this);
		}
	}, {
		text: '否',
		handler: function() {
			$('#zapjs_f_id_modal_message').dialog('close');
			$('#zapjs_f_id_modal_message').remove();
		}
	});

	$('#zapjs_f_id_modal_message').dialog({
		title: '提示消息',
		width: '400',
		resizable: true,
		closed: false,
		cache: false,
		modal: true,
		buttons: aButtons
	});
}

function checkAuthorityLogo(target){
	var current = $(target);
	var currentVal = current.val();
	var vs = ['${b_method.bConfig('productcenter.authority_logo_sevenday')}','${b_method.bConfig('productcenter.authority_logo_sevenday_no')}'];
	if(vs.indexOf(currentVal) < 0) return;
	
	// 7天退货规则必选，且只能选择一个
	if(current.prop('checked')){
		$('input[name="zw_f_authority_logo"]:checked').each(function(){
		if(current.attr('id') != $(this).attr('id')) {
			if(vs.indexOf($(this).val()) >= 0){
			    $(this).prop("checked",false);
			}
		}
		});	
	}else{
		$('input[name="zw_f_authority_logo"]:checked').each(function(){
		if(current.attr('id') != $(this).attr('id')) {
			if(vs.indexOf($(this).val()) >= 0){
			    $(this).prop("checked",false);
			}
		}
		});
	}
}
</script>

<input type="hidden" id="merchant_seller_type" value="${merchant_seller_type.getSellerType(productMap['small_seller_code'])}"/>
<input type="hidden" id="cfamily_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_description_pic" value="-1_false"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_piclist" value="-1_false"/>
<form class="form-horizontal" role="form">
<input type="hidden" id="zw_f_json" name="zw_f_json" value=""/>
<fieldset>
	<div id="OtherProductInfo" class="w_display">
 			<div id="cfamily_modifyproduct_insert_prop">
	   		 	
	   		 	<div id="cfamily_addproduct_pextend"  class="csb_cfamily_addproduct_pextend  w_display w_clear">
	   		 		<div class="csb_cfamily_addproduct_title">扩展属性</div>
	   		 		<div class="csb_cfamily_addproduct_item">
			   		 	<table  class="table ">
				   		 	<thead>
				   		 		<tr>
				   		 		<th>名称
				   		 		</th>
				   		 		<th>内容
				   		 		</th>
				   		 		</tr>
				   		 	</thead>
			   		 	
				   		 	<tbody>
				   		 	
				   		 	</tbody>
			   		 	</table>
	   		 		</div>
	   		 	
	   		 	</div>
	   		 	
	   		 	
	   		 	<div id="cfamily_addproduct_custom"  class="csb_cfamily_addproduct_pextend   w_clear">
	   		 		<div class="csb_cfamily_addproduct_title">自定义属性（规格参数）</div>
	   		 		<div class="csb_cfamily_addproduct_item">
			   		 	<table  class="table ">
				   		 	<thead>
				   		 		<tr>
				   		 			<th>名称</th>
				   		 			<th>内容</th>
				   		 			<th></th>
				   		 		</tr>
				   		 	</thead>
				   		 	
			   		 	
				   		 	<tbody>
				   		 		<tr id="first_tr">
						   		 	<td>
						   		 		<input id="gift_name" type="text" class="c_text" readonly="readonly" />
						   		 	</td>
						   		 	<td>
						   		 		<textarea id="gift_value" rows="2" class="c_value"></textarea>
						   		 	</td>
						   		 	<td></td>
						   		</tr>
					   		 	<tr>
						   		</tr>
				   		 	</tbody>
				   		 	<tfoot>
				   		 	<tr>
						   		 	<td>
						   		 		<input id="cfamily_addproduct_custom_text" />
						   		 	</td>
						   		 	<td>
						   		 		<textarea rows="2" id="cfamily_addproduct_custom_value"></textarea>
						   		 	</td>
						   		 	<td>
						   		 		<input type="button" class="btn" onclick="cfamily_modproduct.add_custom()" value="添加"/>
						   		 	</td>
				   		 	</tr>
				   		 	</tfoot>
			   		 	</table>
			   		 	
	   		 		</div>
	   		 	
	   		 	</div>
	   		 	
	   		 	<#if (propertiesList?size>0)>
		   		 	<div id="cfamily_addproduct_properties"  class="csb_cfamily_addproduct_pextend  w_clear">
		   		 		<div class="csb_cfamily_addproduct_title">商品属性</div>
		   		 		<div class="csb_cfamily_addproduct_item">
				   		 	<table  class="table ">
					   		 	<thead>
					   		 		<tr>
					   		 		<th>名称
					   		 		</th>
					   		 		<th>内容
					   		 		</th>
					   		 		</tr>
					   		 	</thead>
	
					   		 	<tbody>
									<#list propertiesList as e_list>
										<tr>
											<td>
												<label class="control-label" for="">
													<#if (e_list.is_must == "449747110002")>
														<span class="w_regex_need">*</span>
													</#if>
													${e_list.properties_name?if_exists}：
												</label>
											</td>
											<td>
												<input class="zw_f_ppr_properties_value_type" type="hidden" value="${e_list.properties_value_type}">
												<input class="zw_f_ppr_is_must" type="hidden" value="${e_list.is_must}">
												<#if (e_list.properties_value_type == "449748500001")>
													<select class="ppr_properties_value_code" name="zw_f_properties_value_code+${e_list.properties_code?if_exists}" id="zw_f_properties_value_code+${e_list.properties_code?if_exists}">
										      				<option value="">--请选择--</option>
															<#list e_list.list as e_list1>
																<#if (e_list.properties_value_code == e_list1.properties_value_code)>
																	<option selected="selected" value="${e_list1.properties_value_code?if_exists}">${e_list1.properties_value?if_exists}</option>
																<#else>
																	<option value="${e_list1.properties_value_code?if_exists}">${e_list1.properties_value?if_exists}</option>
																</#if>
															</#list>
										      		</select>
										      	<#else>
										      		<input class="ppr_properties_value_code" type="text" id="zw_f_properties_value_code+${e_list.properties_code?if_exists}" name="zw_f_properties_value_code+${e_list.properties_code?if_exists}" value="${e_list.properties_value?if_exists}">
												</#if>
										      		
											</td>
										</tr>
									</#list>
					   		 	</tbody>
				   		 	</table>
								
		   		 		</div>
	
		   		 	</div>
	   		 	</#if>
	   		</div>	
 				<div id="cfamily_addproduct_travel">
	 				<ul class="w_ullist">
	 					<li>
	 					<input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_0" value="0" checked="checked"/><label for="zw_f_tra_select_0">卖家包邮</label>
	 					</li>
	 					<li class="w_mt_15">
	 					<input type="radio" name="zw_f_tra_select"  id="zw_f_tra_select_1" value="1"/><label for="zw_f_tra_select_1">买家承担运费&nbsp;&nbsp;&nbsp;</label><input type="text" id="zw_f_tra_user"   class="span2" value="">
	 					</li>
	 					<li class="w_mt_15">
	 						<div class="w_left"><input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_2" value="2"/><label for="zw_f_tra_select_2"><input class="btn btn-small" type="button" value="选择运费模板" onclick="zapadmin_single.show_box('zw_f_traval')"/></label>
	 						<input id="zw_f_traval_show_text" type="hidden" value="">
	 						<script type="text/javascript">zapjs.f.require(['zapadmin/js/zapadmin_single'],function(a){a.init({"text":"","buttonflag":false,"source":"page_chart_v_chart_uc_freight_tpl","id":"zw_f_traval","value":"","max":"1"});});</script>
	 						</div>
	 						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul" id="zw_f_traval_show_ul"></ul></div>
	 					</li>
	 				</ul>
 				</div>
		  <@m_zapmacro_common_page_add b_page />
		<div>
			<br />
		</div>
</fieldset>
</form>


<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	<div class="control-group">
		<label class="control-label"><span class="w_regex_need">*</span>商品保障：</label>
		<div class="controls">
			<div class="control_book">
			    <#assign autylogList=b_method.upDataBysql("pc_authority_logo","select * from pc_authority_logo where show_type = '449747960002'")>
			    <#list autylogList as e>
			    	<input type="checkbox" name="zw_f_authority_logo" id="zw_f_authority_logo_${e.zid}" value="${e.uid}" onclick="checkAuthorityLogo(this)" tn="${e.logo_content}">
			    	<label for="zw_f_authority_logo_${e.zid}"><img style="width:35px" src="${e.logo_pic}">${e.logo_content}</label>
			    </#list>
			</div>
		</div>
	</div>
	<div class="control-group hide">
		<label class="control-label"><span class="w_regex_need">*</span>贸易类型：</label>
		<div class="controls">
			<select name="zw_f_product_trade_type" id="zw_f_product_trade_type">
				<option value="">请选择</option>
				<option value="0">保税备货</option>
				<option value="1">海外直邮</option>
			</select>
		</div>
	</div>			
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
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
	  	
	  		<#if e_field.getPageFieldName() == 'zw_f_keyword'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "--请选择--"/>
	  		<#elseif e_field.getPageFieldName() == 'zw_f_qualification_category_code'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "请选择"/>
	  		<#else>
	  			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  		</#if>
	  		
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<#if e_field.getPageFieldName() == 'zw_f_product_desc_video'>
	  			<@m_zapmacro_common_field_video_upload  e_field/>
	  		<#else>
	  			<@m_zapmacro_common_field_upload  e_field  e_page />
	  		</#if>
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：视频上传 -->
<#macro m_zapmacro_common_field_video_upload e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<video id="my-video" class="video-js video_hidden" controls preload="auto" width="600" height="300"  data-setup="{}"><source id="product_desc_video" src="" type="video/mp4"></video>
		<div  class="w_left w_p_10 video_hidden"><a href="javascript:cfamily_modproduct.deleteVideo()">删除视频</a></div>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
	      		<select id="zw_f_tax_rate_show">
	      			<#if e_text_select!="">
      					<option value="">${e_text_select}</option>
      				</#if>
  					<option value="0">0</option>
  					<option value="0.09">9</option>
  					<option value="0.1">10</option>
  					<option value="0.11">11</option>
  					<option value="0.13">13</option>
  					<option value="0.16">16</option>
  					<option value="0.17">17</option>
	      		</select>
	      		<input type="hidden" id="zw_f_tax_rate" name="zw_f_tax_rate" value="${e_field.getPageFieldValue()}" >
			<#else>
		      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
      					<option value="">${e_text_select}</option>
      				</#if>
      				<#list e_page.upDataSource(e_field) as e_key>
						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
			</#if>
	      		<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
	      			&nbsp;&nbsp;%
	      		<#else>
      			</#if>
	<@m_zapmacro_common_field_end />
</#macro>
<script type="text/javascript">  
    $(window).load(function() { 
	 	$('img').mouseover(function(obj){
	 		var height_ = obj.target.naturalHeight;
	 		var width_ = obj.target.naturalWidth;
	 		var msg = "图片暂未加载完成，请稍后!"; 
	 		 
	 		var opt = new Object();
			opt.imageUrl = obj.target.currentSrc;
			api_call('com_cmall_familyhas_api_ApiForImageProperty', opt , function(result){
				var size = result.size;
				msg = "该图片高 = " + height_  + "px | 宽 = " + width_ + "px | 大小 = " + size + "Kb";  
	        	$(obj.target.parentElement).prop("title", msg);   
			}); 
	    });
	    CKEDITOR.config.readOnly = true;
	}); 
 	function api_call(sTarget, oData, fCallBack) {
		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		}:{api_key : 'jsapi',api_input:''};
		
		//oData = $.extend({}, defaults, oData || {});

		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			//fCallBack(data);			
			fCallBack(data);
		});
	} 
 	function sub(a){
 		debugger;
 		zapjs.zw.func_edit(a);
 		$("#zw_f_market_price").prop("disabled",true);
				$("#zw_f_qualification_category_code").prop("disabled",true);
				$("#zw_f_product_weight").prop("disabled",true);
				$("#zw_f_product_volume").prop("disabled",true);
				$("#zw_f_expiry_date").prop("disabled",true);
				$("#zw_f_expiry_unit").prop("disabled",true);
				
			
				$("#zw_f_volumn_length").prop("disabled",true);
				$("#zw_f_volumn_width").prop("disabled",true);
				$("#zw_f_volumn_high").prop("disabled",true);
				CKEDITOR.config.readOnly = true;
 		
 	}
</script>
<script>
define('global/window', [], () => {
    return window;
});

define('global/document', ['global/window'], (window) => {
    return window.document;
});
require(['cfamily/js/video.min'],function(videojs){
	window.videojs = videojs;
})
</script> 
