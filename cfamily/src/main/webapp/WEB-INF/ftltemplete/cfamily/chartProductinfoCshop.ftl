
<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/chartProductinfoCshop','cfamily/js/select2/select2'],
function(p)
{
	//var product={"zid":0,"uid":"","productCode":"","produtName":"","sellerCode":"","brandCode":"","productWeight":0.0,"flagSale":0,"createTime":"","updateTime":"","category":null,"description":null,"pcPicList":null,"pcProductpropertyList":null,"productSkuInfoList":null,"pcProdcutflow":null};
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(uuid)};
	//<#assign manageCode = b_method.upUserInfo().getManageCode()>
	//product.sellerCode='${manageCode}';
	zapjs.f.ready(function()
	{	
		p.init_chartProductinfoCshop(product);
	});
});
</script>

<input type="hidden" id="cfamily_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_description_pic" value="-1_false"/>
<form class="form-horizontal" role="form" data_Block="ReadOnly">
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
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	
		<#if e_field.getPageFieldName() == 'zw_f_keyword'>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">（多个标签以空格分隔，标签总长度不超过10个字符）
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
      					<option value="">${e_text_select}</option>
      				</#if>
      				<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
      					<option value="0.13">13</option>
      					<option value="0.17">17</option>
      				<#else>
	      				<#list e_page.upDataSource(e_field) as e_key>
							<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
						</#list>
      				</#if>
	      		</select>
	      		<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
	      			&nbsp;&nbsp;%
	      		<#else>
      			</#if>
	<@m_zapmacro_common_field_end />
</#macro>

