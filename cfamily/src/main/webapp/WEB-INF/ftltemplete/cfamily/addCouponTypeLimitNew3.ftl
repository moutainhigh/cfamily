	<#assign couponTypeCode = b_page.getReqMap()["zw_f_coupon_type_code"] >
	<#assign coupon_support=b_method.upClass("com.cmall.ordercenter.service.CouponsService")>
	<#assign limitMap=coupon_support.getCouponTypeLimit(couponTypeCode,"SI2003")>
	<#assign typeLimit=coupon_support.getLdCategoryLimitName(couponTypeCode)>
	
	
<script>
require(['cfamily/js/couponTypeLimitNew3'],

function()
{
	zapjs.f.ready(function()
		{
			couponTypeLimitNew3.init(${limitMap});
		}
	);
}

);
</script>
<@m_zapmacro_common_page_add b_page />
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
	  		<#if (e_field.getFieldName()=="category_codes")>
	  			<@m_zapmacro_common_auto_common />
	  		</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：组件框 -->
<#macro m_zapmacro_common_field_component e_field e_page>  	
	
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if (e_field.getFieldName()=="product_codes")>
			<div style="margin-top:-9px;width:400px">
				<input id="zw_f_product_codes" type="hidden" value="" name="zw_f_product_codes">
				<input id="zw_f_product_codes_show_text" type="hidden" value="">
				<div style="float:right;">
						<input id="zw_f_except_product" name="zw_f_except_product" type="checkbox" value="1">&nbsp;&nbsp;&nbsp;<span class='w_regex_need'>以下商品除外</span>
				</div>
				<script type="text/javascript">
					zapjs.f.require(['cfamily/js/couponTypeLimit_product_select'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_codes","max":"","value":""});});
				</script>
			</div>
			<p></p>
			<table id="productTable"></table>
	<#else>
			${e_field.getPageFieldValue()?default("")}
	</#if>
		<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if (e_field.getFieldName()=="category_codes")>
			<div style="width:400px">
				<input type="hidden" id="zw_f_category_codes" name="zw_f_category_codes">
				<input id="selectCategoryBtn" class="btn" type="button" value="选择分类" onclick="">
				<div style="float:right;">
						<input id="zw_f_except_category" name="zw_f_except_category" type="checkbox" value="1">&nbsp;&nbsp;&nbsp;<span class='w_regex_need'>以下分类除外</span>
				</div>
			</div>
			<p></p>
			<table id="categoryTable"></table>
		<#elseif (e_field.getFieldName()=="brand_codes")>
			<div style="margin-top:-9px;width:400px">
				<input id="zw_f_brand_codes" type="hidden" value="" name="zw_f_brand_codes">
				<input id="zw_f_brand_codes_show_text" type="hidden" value="">
				<div style="float:right;">
					<input id="zw_f_except_brand" name="zw_f_except_brand" type="checkbox" value="1">&nbsp;&nbsp;&nbsp;<span class='w_regex_need'>以下品牌除外</span>
				</div>
				
				<script type="text/javascript">
					zapjs.f.require(['cfamily/js/couponTypeLimit_brand_select'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_brandinfo_multiSelect","id":"zw_f_brand_codes","max":"","value":""});});
				</script>
			</div>
			<p></p>
			<table id="brandTable"></table>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#macro m_zapmacro_common_auto_common >
	<#if typeLimit.create_user == "ld" && typeLimit.category_limit == "4497471600070002">
		<label class="control-label" for="category_name" style="padding:10px 0;margin-bottom:10px;">集团分类：</label>
		<div class="controls" style="padding:10px 0;margin-bottom:10px;font-size:14px;" name="category_name">
			${typeLimit.category_nm?default('')}
		</div>
	</#if>
</#macro>

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    	</div>
	</div>
</#macro>

<#-- 按钮显示 -->
<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
    		
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    		
    			</#if>
    		</#list>
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
      					<option value="">${e_text_select}</option>
      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>
						<#if e_field.getPageFieldName() == "zw_f_seller_limit" && typeLimit.create_user == "ld" && e_key.getV() == "449748230001">
							<#-- 优惠券一体化的券 暂时仅能LD品可用 since 2020-04-28 -->
						<#else>
							<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
						</#if>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  value="${e_operate.getOperateName()}" onclick="check(this)"/>
</#macro>
<script>
		function check(obj) {
			//分类限制检查
			var rows = $('#categoryTable').datagrid("getRows");
			var vlimit = $('#zw_f_category_limit').val();
			if(vlimit == '4497471600070002') {
				//有限定
				if(rows.length == 0) {
					zapadmin.model_message('分类编号不能为空!');
					return ;
				}
			} else if(vlimit == '4497471600070001') {
				//无限定
				if(rows.length > 0) {
					zapadmin.model_message("分类限制不正确!");
					return ;
				}
			}
			
			//品牌限制检查
			rows = $('#brandTable').datagrid("getRows");
			vlimit = $('#zw_f_brand_limit').val();
			if(vlimit == '4497471600070002') {
				//有限定
				if(rows.length == 0) {
					zapadmin.model_message('品牌编号不能为空!');
					return ;
				}                
			} else if(vlimit == '4497471600070001') {
				//无限定
				if(rows.length > 0) {
					zapadmin.model_message("品牌限制不正确!");
					return ;
				}
			}
			
			//商品限制检查
			rows = $('#productTable').datagrid("getRows");
			vlimit = $('#zw_f_product_limit').val();
			if(vlimit == '4497471600070002') {
				//有限定
				if(rows.length == 0) {
					zapadmin.model_message('商品编号不能为空!');
					return ;
				}                
			} else if(vlimit == '4497471600070001') {
				//无限定
				if(rows.length > 0) {
					zapadmin.model_message("商品限制不正确!");
					return ;
				}
			}
			
			//渠道限制检查
			rows = $('#productTable').datagrid("getRows");
			vlimit = $('#zw_f_channel_limit').val();
			if(vlimit == '4497471600070002') {
				//有限定
				if($("input[name='zw_f_channel_codes']:checked").length == 0) {
					zapadmin.model_message('渠道编号不能为空!');
					return ;
				}             
			} else if(vlimit == '4497471600070001') {
				//无限定
				if($("input[name='zw_f_channel_codes']:checked").length > 0) {
					zapadmin.model_message("渠道限制不正确!");
					return ;
				}
			}
			//是否允许参与活动判断，如果允许参与活动则必须有一个选中活动
			vlimit = $('#zw_f_activity_limit').val();
			if(vlimit == '449747110002'){//可以参与活动
				//无限定
				if($("input[name='zw_f_allowed_activity_type']:checked").length == 0) {
					zapadmin.model_message("可参与活动不能为空!");
					return ;
				}
			}
			zapjs.zw.func_add(obj);
		}
</script>