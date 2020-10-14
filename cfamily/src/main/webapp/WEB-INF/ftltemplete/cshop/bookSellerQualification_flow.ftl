<#assign qualificationUid = b_page.getReqMap()["zw_f_uid"]>
<#assign flow_code = "">
<#assign current_status = ""> 
<script type="text/javascript"></script>
<script>
	require(['cfamily/js/bookSellerQualification','cmanage/js/flow'],
		function(p)
		{
		<#assign brand_support=b_method.upClass("com.cmall.productcenter.service.ProductBrandService")>
		<#assign sc_define_support=b_method.upClass("com.cmall.systemcenter.service.ScDefineService")>
		var brand=${brand_support.upSellerQualificationDraftMap(qualificationUid)["qualification_json"]};
		var brand_name = "${brand_support.getBranName(brand_support.upSellerQualificationDraftMap(qualificationUid)["brand_code"])}";
		var category_name = "${sc_define_support.getDefineNameByCode(brand_support.upSellerQualificationDraftMap(qualificationUid)["category_code"])}";
			zapjs.f.ready(function()
			{
				p.init(brand,brand_name,category_name);
			});
		}
	);
</script>

<style>
.table ul li{height:auto;}
.zw_page_upload_iframe{height:50px;min-width:300px;}
</style>

<@m_zapmacro_common_page_book b_page />
<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<#if e_field.getPageFieldName()=="zw_f_flow_code">
		<#assign flow_code = e_field.getPageFieldValue()>
	<#elseif e_field.getPageFieldName()=="zw_f_current_status">
		<#assign current_status = e_field.getPageFieldValue()>
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
			      		<#if e_field.getPageFieldName()=="zw_f_brand_code">
							<span id="brand_name_span"></span>
						<#elseif e_field.getPageFieldName()=="zw_f_category_code">
							<span id="category_name_span"></span>
						<#else>
				      		<#if e_field.getFieldTypeAid()=="104005019">
				      			<#list e_page.upDataSource(e_field) as e_key>
									<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
								</#list>
				      		<#else>
				      			${e_field.getPageFieldValue()?default("")}
				      		</#if>
			      		</#if>
	      		</div>
		<@m_zapmacro_common_field_end />
	</#if>
</#macro>

<div id="category_tip"></div>
<div id="cshop_addproduct_custom" class="csb_cshop_addproduct_pextend w_clear">
	<div class="csb_cshop_addproduct_title"></div>
		<div class="csb_cshop_addproduct_item">
	 		<table  class="table">
	   		 	<thead>
	   		 		<tr>
	   		 			<th>资质名称</th>
	   		 			<th>资质证件</th>
	   		 			<th>有效期到期时间</th>
	   		 		</tr>
	   		 	</thead>
	   		 	<tbody>
	   		 	</tbody>
	   		 	<tfoot>
   		 	</tfoot>
	 	</table>
	</div>
</div>
