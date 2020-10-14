<#assign couponTypeCode = b_page.getReqMap()["zw_f_coupon_type_code"] >
<#assign coupon_support=b_method.upClass("com.cmall.ordercenter.service.CouponsService")>
<#assign limitMap=coupon_support.getCouponTypeLimit(couponTypeCode,"SI2003")>

<#assign couponType = b_method.upDataOne("oc_coupon_type","","","","coupon_type_code","${couponTypeCode}")>
<#assign activity = b_method.upDataQueryToJson("oc_activity","","","","activity_code","${couponType['activity_code']}")>
<script>
require(['cfamily/js/bookCouponNew3'],

function()
{
	zapjs.f.ready(function()
		{
			bookCouponNew3.init(${limitMap});
		}
	);
}

);

// 活动对象
var activity = eval('(${activity})')[0];
</script>

<@m_zapmacro_common_page_edit b_page />


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
	
	
	<#assign couponTypeLimit=coupon_support.getCouponTypeLimitBaseInfo(couponTypeCode)>
	
	<div id="limitCondition">
		<div class="control-group">
			<label class="control-label" for="zw_f_category_limit">分类限制：</label>
			<div class="controls">
				<input  type="text" id="zw_f_category_limit" name="zw_f_category_limit" <#if couponTypeLimit.categoryLimit='4497471600070002'>value="指定"<#else>value="无限制"</#if>>
				<div style="margin-top:10px">
					<input type="checkbox" value="1" name="zw_f_except_category" id="zw_f_except_category" <#if couponTypeLimit.exceptCategory='1'>checked="true"</#if>>&nbsp;&nbsp;以下分类除外
				</div>
				<table id="categoryTable"></table>
			</div>
			
			<#assign typeLimit=coupon_support.getLdCategoryLimitName(couponTypeCode)>
			<#if typeLimit.create_user == "ld" && typeLimit.category_limit == "4497471600070002">
				<label class="control-label" for="category_name" style="padding-top:20px;">集团分类：</label>
				<div class="controls" style="padding-top:20px;font-size:14px;" name="category_name">
					${typeLimit.category_nm?default('')}
				</div>
			</#if>
		</div>
		<div class="control-group">
			<label class="control-label" for="zw_f_brand_limit">品牌限制：</label>
			<div class="controls">
				<input  type="text" id="zw_f_brand_limit" name="zw_f_brand_limit" <#if couponTypeLimit.brandLimit='4497471600070002'>value="指定"<#else>value="无限制"</#if>>
				<div style="margin-top:10px">
					<input id="zw_f_except_brand" type="checkbox" value="1" name="zw_f_except_brand" <#if couponTypeLimit.exceptBrand='1'>checked="true"</#if>>&nbsp;&nbsp;以下品牌除外
				</div>
				
				<table id="brandTable"></table>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="zw_f_product_limit">商品限制：</label>
			<div class="controls">
				<input  type="text" id="zw_f_product_limit" name="zw_f_product_limit" <#if couponTypeLimit.productLimit='4497471600070002'>value="指定"<#else>value="无限制"</#if>>
				<div style="margin-top:10px">
					<input id="zw_f_except_product" type="checkbox" value="1" name="zw_f_except_product" <#if couponTypeLimit.exceptProduct='1'>checked="true"</#if>>&nbsp;&nbsp;以下商品除外
				</div>
				<table id="productTable"></table>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="zw_f_product_limit">渠道限制：</label>
			<div class="controls">
				<input  type="text" id="zw_f_channel_limit" name="zw_f_channel_limit" <#if couponTypeLimit.channelLimit='4497471600070002'>value="指定"<#else>value="无限制"</#if>>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="zw_f_channel_codes">渠道编号：</label>
			<div  class="controls" style="margin-top:10px">
					<input type="checkbox" value="449747430001" name="zw_f_channel_codes" id="zw_f_channel_codes_0">
					<label for="zw_f_channel_codes_0">App</label>
						
					<input type="checkbox" value="449747430002" name="zw_f_channel_codes" id="zw_f_channel_codes_1">
					<label for="zw_f_channel_codes_1">Wap</label>
						
					<input type="checkbox" value="449747430003" name="zw_f_channel_codes" id="zw_f_channel_codes_2">
					<label for="zw_f_channel_codes_2">微信商城</label>
					
					<input type="checkbox" value="449747430004" name="zw_f_channel_codes" id="zw_f_channel_codes_3">
					<label for="zw_f_channel_codes_2">PC</label>
					
					<input type="checkbox" value="449747430023" name="zw_f_channel_codes" id="zw_f_channel_codes_4">
					<label for="zw_f_channel_codes_2">惠家有小程序</label>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="zw_f_seller_limit">商户限制：</label>
			<div  class="controls">
					<input  type="text" id="zw_f_activity_limit" name="zw_f_seller_limit" <#if couponTypeLimit.sellerLimit='449748230002'>value="仅LD品可用"<#else>value="无限制"</#if>>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="zw_f_activity_limit">是否可以参与活动：</label>
			<div class="controls">
				<input  type="text" id="zw_f_activity_limit" name="zw_f_activity_limit" <#if couponTypeLimit.activityLimit='449747110002'>value="是"<#else>value="否"</#if>>
			</div>
	</div>
	
	<div class="control-group">
			<label class="control-label" for="zw_f_allowed_activity_type_limits">活动类型：</label>
			<div  class="controls" style="margin-top:10px">
					<input type="checkbox" value="4497472600010001" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_0">
					<label for="zw_f_channel_codes_0">秒杀</label>
						
					<input type="checkbox" value="4497472600010002" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_1">
					<label for="zw_f_channel_codes_1">特价</label>
						
					<input type="checkbox" value="4497472600010004" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_2">
					<label for="zw_f_channel_codes_2">扫码购-微信</label>
					
					<input type="checkbox" value="4497472600010015" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_3">
					<label for="zw_f_channel_codes_2">扫码购-APP</label>
					<input type="checkbox" value="4497472600010005" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_4">
					<label for="zw_f_channel_codes_0">闪购</label>
						
					<input type="checkbox" value="4497472600010008" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_5">
					<label for="zw_f_channel_codes_1">满减</label>
						
					<input type="checkbox" value="4497472600010024" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_6">
					<label for="zw_f_channel_codes_2">拼团</label>
					
					<input type="checkbox" value="4497472600010018" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_7">
					<label for="zw_f_channel_codes_2">会员日折扣</label>
					
					<input type="checkbox" value="4497472600010030" name="zw_f_allowed_activity_type_limits" id="zw_f_allowed_activity_type_limits_7">
					<label for="zw_f_channel_codes_2">打折促销</label>
			</div>
		</div>
	<div class="control-group">
			<label class="control-label" for="zw_f_payment_type_limit">支付类型：</label>
			<div class="controls">
				<input  type="text" id="zw_f_payment_type_limit" name="zw_f_payment_type_limit" <#if couponTypeLimit.paymentTypeLimit='449748290001'>value="在线支付"<#elseif couponTypeLimit.paymentTypeLimit='449748290002'>value="货到付款"<#else>value="不限制"</#if>>
			</div>
	</div>
	
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
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if (e_field.getFieldName()=="money" || e_field.getFieldName()=="limit_money"
			 || e_field.getFieldName()=="total_money")>
			<span>元</span>
		<#elseif (e_field.getFieldName()=="discount")>
			<span>%</span>		
		<#elseif (e_field.getFieldName()=="privide_money" || e_field.getFieldName()=="surplus_money")>
			<span>份</span>			
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>
