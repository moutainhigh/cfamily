

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

	<#assign zw_f_id_number = b_page.getReqMap()["zw_f_id_number"] >
	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if zw_f_id_number == 'N2' || zw_f_id_number == 'N5'>
			<#if e.getPageFieldName() == "zw_f_common_number" >
				<input type="hidden" name="zw_f_common_number" id="edit_dlq_content_select_zw_f_common_number" value="${e.getPageFieldValue()}"/>
				<div class="control-group">
	  				<label class="control-label" for="zw_f_common_number"><span class="w_regex_need">*</span>添加商品 :</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn" value="选择商品" onclick="edit_dlq_content_select_show_windows()"/>
	  				<#-- 获取商品信息 -->
	  				<#if (e.getPageFieldValue()?length >0) >
						<#assign pcservice=b_method.upClass("com.cmall.familyhas.service.DLQService")>
						<#assign pcInfo = pcservice.getProductInfo("${e.getPageFieldValue()}") >
						<#if pcInfo??>
							<div id="edit_dlq_content_select_AddProductShowTime" style="display:inline;margin-left:5px;"><span style="padding: 5px 15px; border: 1px solid #008299;">${pcInfo["product_name"]}</span></div>
						<#else>
							<div id="edit_dlq_content_select_AddProductShowTime" style="display:inline;margin-left:5px;"></div>
						</#if>
						
					<#else>
						<div id="edit_dlq_content_select_AddProductShowTime" style="display:inline;margin-left:5px;"></div>
	  				</#if>
	  				<script type="text/javascript">
						
						function edit_dlq_content_select_show_windows() {
							zapjs.f.window_box({
									id : 'as_productlist_productids',
									content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.edit_dlq_content_select_saveProduct" frameborder="0" style="width:100%;height:500px;"></iframe>',
									width : '700',
									height : '550'
								});
						}
						/**
						 * 
						 * @param sId 
						 * @param sVal 商品编号
						 * @param a 商品名称
						 * @param b
						 * @param c
						 * @returns {Boolean}
						 */
						function edit_dlq_content_select_saveProduct(sId, sVal,a,b,c) {
							$("#edit_dlq_content_select_zw_f_common_number").val(sVal);
							$("#edit_dlq_content_select_AddProductShowTime").html('<span style="padding: 5px 15px; border: 1px solid #008299;">'+a+'</span>');
							zapjs.f.window_close(sId);
						}
					</script>
	  			</div>
			<#elseif e.getPageFieldName() == "zw_f_picture" || e.getPageFieldName() == "zw_f_location" || e.getPageFieldName() == "zw_f_uid">
				<#if e.getPageFieldName() == "zw_f_picture">
					<div>
						<@m_zapmacro_common_field_upload  e  e_page />
						<span style="position: absolute;left: 360px;line-height: 30px;color: #BFBCBC;top: 200px;">建议尺寸: 1080 * 540</span>
					</div>
				<#else>
					<@m_zapmacro_common_auto_field e e_page/>
				</#if>
			</#if>
		<#elseif zw_f_id_number == 'N3'>
			<#if e.getPageFieldName() == "zw_f_picture" || e.getPageFieldName() == "zw_f_location" || e.getPageFieldName() == "zw_f_co_describe" || e.getPageFieldName() == "zw_f_uid">
				<#if e.getPageFieldName() == "zw_f_picture">
					<div>
						<@m_zapmacro_common_field_upload  e  e_page />
						<span style="position: absolute;left: 400px;line-height: 30px;color: #BFBCBC;top: 110px;">建议尺寸: 660 * 438</span>
					</div>
				<#else>
					<@m_zapmacro_common_auto_field e e_page/>
				</#if>
			</#if>
		<#elseif zw_f_id_number == 'N1'>
			<#if e.getPageFieldName() == "zw_f_food_name" || e.getPageFieldName() == "zw_f_weight" || e.getPageFieldName() == "zw_f_uid">
				<@m_zapmacro_common_auto_field e e_page/>
			</#if>
		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  	</#if>
	</#list>
	</#if>
</#macro>

