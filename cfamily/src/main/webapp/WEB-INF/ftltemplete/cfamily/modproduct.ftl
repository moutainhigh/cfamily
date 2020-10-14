
<#assign uuid = b_page.getReqMap()["zw_f_uid"]>

<#-- 商品属性 -->
<#assign propertiesList = b_method.upClass("com.cmall.productcenter.service.ProductService").getPropertiesByProductCode("${uuid}")>

<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="lib/datepicker/WdatePicker" src="../resources/lib/datepicker/WdatePicker.js"></script>
<script>
require(['cfamily/js/modproduct','cfamily/js/select2/select2'],
function(p)
{
	<!-- 商品标签 -->
	<#assign productMap=b_method.upDataOneOutNull("pc_productinfo","product_code,transport_template","","uid='${uuid}'")>
	<#assign autylogListChecked=b_method.upDataBysql("pc_product_authority_logo","select authority_logo_uid from pc_product_authority_logo where product_code = '${productMap['product_code']}'")>
	<#assign tplNameMap=b_method.upDataOneOutNull("uc_freight_tpl","*","","is_default = '449746250001' and isDisable = '449746250002'")>
	//var product={"zid":0,"uid":"","productCode":"","produtName":"","sellerCode":"","brandCode":"","productWeight":0.0,"flagSale":0,"createTime":"","updateTime":"","category":null,"description":null,"pcPicList":null,"pcProductpropertyList":null,"productSkuInfoList":null,"pcProdcutflow":null};
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(uuid)};
	//<#assign manageCode = b_method.upUserInfo().getManageCode()>
	//product.sellerCode='${manageCode}';
	<#if tplNameMap['uid']??>
		cfamily_modproduct.defaultuid = "${tplNameMap['uid']}";
	</#if>
	zapjs.f.ready(function()
	{
		p.init_product(product);
	});
	
	
	<#if autylogListChecked??>
		<#list autylogListChecked as e>
			$('input[name="zw_f_authority_logo"][value="${e["authority_logo_uid"]!""}"]').prop("checked",true);
		</#list>
	</#if>
});

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
	   		 		<div class="csb_cfamily_addproduct_title">自定义属性</div>
	   		 		<div class="csb_cfamily_addproduct_item">
			   		 	<table  class="table " id = "move">
				   		 	<thead>
				   		 		<tr>
				   		 			<th>名称</th>
				   		 			<th>内容</th>
				   		 			<th></th>
				   		 			<th>位置移动</th>
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
						   		 	<td id="inner-gift-time">
						   		 		开始时间：<input onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="start_date" name="start_date" type="text" value=""> |
						   		 		结束时间：<input onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="end_date" name="end_date" type="text" value="">
						   		 	</td>
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
						   		 	<td>无</td>
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
	  		<#elseif e_field.getPageFieldName() == 'zw_f_onlinepay_flag'>
	  			<@m_zapmacro_onlinepay/>
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
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005023">
	  		<@m_zapmacro_common_field_upload_upgrade  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
			<select id="zw_f_tax_rate_show">
				<option value="">0</option>
				<option value="0">0</option>
				<option value="0.03">3</option>
				<option value="0.04">4</option>
				<option value="0.05">5</option>
				<option value="0.09">9</option>
				<option value="0.1">10</option>
				<option value="0.11">11</option>
				<option value="0.13">13</option>
				<option value="0.17">17</option>
      		</select>&nbsp;&nbsp;%
			<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}" >

		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>

	<@m_zapmacro_common_field_end />
</#macro>

<#-- 仅限在线支付 -->
<#macro m_zapmacro_onlinepay>
	<div class="control-group">
		<label class="control-label">只支持在线支付：</label>
		<div class="controls">
			<div class="control_book">
				<#--<input type="checkbox" name="zw_f_onlinepay_flag" id="zw_f_onlinepay_flag" value="449747110002" onclick="$(this).prop('checked') ? $('#zw_f_onlinepay_time').show() : $('#zw_f_onlinepay_time').hide()">-->
				<input type="checkbox" name="zw_f_onlinepay_flag" id="zw_f_onlinepay_flag" value="449747110002">
				<label for="zw_f_onlinepay_flag">是</label> 
			</div>
		</div>
	</div>
	<div class="control-group" id="zw_f_onlinepay_time" style="display: none">
		<label class="control-label"><span class="w_regex_need">*</span>生效时间：</label>
		<div class="controls">
			<div class="control_book">
				<input type="text" id="zw_f_onlinepay_start" name="zw_f_onlinepay_start" onClick="WdatePicker({maxDate:'#F{$dp.$D(\'zw_f_onlinepay_end\',{d:-1});}',dateFmt: 'yyyy-MM-dd HH:mm:ss'})" value="">
				--
				<input type="text" id="zw_f_onlinepay_end" name="zw_f_onlinepay_end" onClick="WdatePicker({minDate:'#F{$dp.$D(\'zw_f_onlinepay_start\',{d:0});}',dateFmt: 'yyyy-MM-dd HH:mm:ss'})" value="">
			</div>
		</div>
	</div>	
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
 
</script>
