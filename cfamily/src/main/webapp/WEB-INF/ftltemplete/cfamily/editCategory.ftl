<script>
require(['zapadmin/js/categoryLinkSelectEvent'],

<#assign category_code = b_page.getReqMap()["zw_f_category_code"] >
function()
{
	zapjs.f.ready(function()
		{
			categoryLinkSelectEvent.init('zw_f_link_address');
		}
	);
	
 	var oData = {category_code:${category_code}};
	var defaults = oData?{
		api_target : 'com_cmall_familyhas_api_ApiGetADCategory',
		api_input : zapjs.f.tojson(oData),
		api_key : 'betafamilyhas'
	}:{api_key : 'betafamilyhas',api_input:''};
	zapjs.f.ajaxjson("../jsonapi/com_cmall_familyhas_api_ApiGetADCategory" , defaults, function(data) {
		if (data.resultCode == "1") {
			var selectProductObj = $('#form_product_single_id').find('label[for=zw_f_product_code]').eq(0);
			var urlObj = $('#form_product_single_id').find('label[for=zw_f_link_url]').eq(0);
			$('#zw_f_line_head').val(data.line_head);
			if(data.line_head.length != 0 || "" != data.line_head) {
				$('#zw_f_line_head').nextAll('span').eq(0).html('');
				$('#zw_f_line_head').nextAll('span').eq(2).html('<ul><li class="control-upload-list-li"><div><div class="w_left w_w_100"><a href="'+data.line_head+'" target="_blank"><img src="'+data.line_head+'"></a></div><div class="w_left w_p_10"><a href="javascript:zapweb_upload.change_index(\'zw_f_line_head\',0,\'delete\')">删除</a></div><div class="w_clear"></div></div></li></ul>');
				$('#zw_f_link_address').val(data.link_address);
			}
			if(data.link_address == '449747030001') {
				//专题页
				$('#zw_f_selectedProduct_id').attr("disabled","disabled");
				$('#zw_f_link_url').removeAttr("disabled");
				$('#zw_f_link_url').val(data.link_url);
				urlObj.prepend('<span class="w_regex_need">*</span>');
				
				//清空数据
				selectProductObj.find('span').remove();
				$("#zw_f_product_code").val("");
				$("#zw_f_product_name").val("");
				$('#zw_f_product_code_show_name').html("");
				$('#zw_f_product_code_show_code').html("");
			} else if(data.link_address == '449747030002') {
				//商品详情页
				//设值
				$('#zw_f_product_code_show_name').html(data.product_name);
				$('#zw_f_product_name').val(data.product_name);
				$('#zw_f_product_code_show_code').html(data.product_code);
				$('#zw_f_product_code').val(data.product_code);
				$('#zw_f_product_link').val('goods_num:'+data.product_code);
				
				//选择商品连接
				$('#zw_f_link_url').attr("disabled","disabled");
				$('#zw_f_selectedProduct_id').removeAttr("disabled");
				selectProductObj.prepend('<span class="w_regex_need">*</span>');
				
				urlObj.find('span').remove();				
				//清空数据
				$('#zw_f_link_url').val("");
			} 
			
		} 
		
	});
}
);
</script>
<@m_zapmacro_common_page_edit b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form id="form_product_single_id" class="form-horizontal" method="POST" >
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
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<#if e_field.getFieldName()=="link_address">
	  			<div> 
	  				<@m_zapmacro_common_field_select  e_field  e_page "请选择"/>
	  			</div>
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
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro> 

<#-- 字段：纯展示 -->
<#macro m_zapmacro_common_field_span e_field>

	<#if e_field.getFieldName()=="product_code">
		<div class="control-group">
			<label class="control-label" for="zw_f_product_code">
				商品选择：
			</label>
			<div class="controls">
				<div>
					<input id="zw_f_product_code" type="hidden" name="zw_f_product_code"  value=""  >
					<input id="zw_f_product_name" type="hidden" name="zw_f_product_name" value="" >
					<input id="zw_f_product_link" type="hidden" name="zw_f_product_link" value="" >
					<script type="text/javascript">
						zapjs.f.require(['zapadmin/js/tryout_single'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_code","value":"","max":"1"});});
					</script>
					<div class="w_left">
						<input id="zw_f_selectedProduct_id" class="btn" type="button" onclick="tryout_single.show_box('zw_f_product_code','SI2003')" value="选择">
					</div>
				</div>
			</div>
		</div>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			${e_field.getPageFieldValue()?default("")}
			<span  class="control-group" id="zw_f_product_code_show_code"></span>
		<@m_zapmacro_common_field_end />
	<#elseif e_field.getFieldName()=="product_name">
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			${e_field.getPageFieldValue()?default("")}
			<span  class="control-group" id="zw_f_product_code_show_name"></span>
		<@m_zapmacro_common_field_end />
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			${e_field.getPageFieldValue()?default("")}
		<@m_zapmacro_common_field_end />
	</#if>
		
	
</#macro>

