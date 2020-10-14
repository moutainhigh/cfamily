
<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<#assign up = b_page.getReqMap()["up"]>

<#-- 商品属性 -->
<#assign propertiesList = b_method.upClass("com.cmall.productcenter.service.ProductService").getPropertiesByProductCode("${uuid}")>

<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/modproductKjt','cfamily/js/select2/select2'],
function(p)
{
	//var product={"zid":0,"uid":"","productCode":"","produtName":"","sellerCode":"","brandCode":"","productWeight":0.0,"flagSale":0,"createTime":"","updateTime":"","category":null,"description":null,"pcPicList":null,"pcProductpropertyList":null,"productSkuInfoList":null,"pcProdcutflow":null};
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(uuid)};
	//<#assign manageCode = b_method.upUserInfo().getManageCode()>
	//product.sellerCode='${manageCode}';
	zapjs.f.ready(function()
	{
		p.init_product(product,${up});
	});
	
	<!-- 商品标签 -->
	<#assign productMap=b_method.upDataOneOutNull("pc_productinfo","product_code","","uid='${uuid}'")>
	<#assign autylogListChecked=b_method.upDataBysql("pc_product_authority_logo","select authority_logo_uid from pc_product_authority_logo where product_code = '${productMap['product_code']}'")>
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

function editProductSubmit(type,obj){
	if(type){
		$("#zw_f_savetype").val(type);
	}
	if(${up} == 1){
		$("#zw_f_market_price").prop("disabled",false);
		$("#zw_f_product_weight").prop("disabled",false);
		$("#zw_f_product_volume").prop("disabled",false); 
		
		$("#zw_f_settlement_type").prop("disabled",false);
		$("#zw_f_volumn_length").prop("disabled",false);
		$("#zw_f_volumn_width").prop("disabled",false);
		$("#zw_f_volumn_high").prop("disabled",false);
		CKEDITOR.instances["zw_f_editor"].setReadOnly(true);
	}
	zapjs.zw.func_edit(obj);
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
	<input type="hidden" id="zw_f_savetype" name="zw_f_savetype" value="submit">
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

			    	<input type="checkbox" name="zw_f_authority_logo" id="zw_f_authority_logo_${e.zid}" value="${e.uid}" onclick="checkAuthorityLogo(this)" tn="${e.logo_content}" >

			    	<label for="zw_f_authority_logo_${e.zid}"><img style="width:35px" src="${e.logo_pic}">${e.logo_content}</label>
			    </#list>
			</div>
		</div>
	</div>	
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
  		<#assign merchant_seller_type=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>
		<#assign sellerType = merchant_seller_type.getSellerType(manageCode)>
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
		  	<#elseif  e_field.getPageFieldName()=="zw_f_settlement_type">
		  		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		  		<#if sellerType == "4497478100050001">
					<select name="zw_f_settlement_type" id="zw_f_settlement_type">
						<option value="4497471600110001">常规结算</option>
					</select>
		  		<#else>
		  		
					<select name="zw_f_settlement_type" id="zw_f_settlement_type">
						<option value="4497471600110002">特殊结算</option>
					</select>
		  		</#if>
		  		<@m_zapmacro_common_field_end />
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
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	     	<#if e_field.getPageFieldName() == 'zw_f_sell_productcode'>
	     	<@m_zapmacro_common_field_text_special  e_field />
	  		<#else>
	  			<@m_zapmacro_common_field_text  e_field />
	  		</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text_special e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input disabled="disabled" type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
</#macro>
