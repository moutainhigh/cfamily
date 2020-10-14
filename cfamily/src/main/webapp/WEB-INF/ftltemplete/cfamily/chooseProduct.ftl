<@m_common_page_head_common e_title=b_page.getWebPage().getPageName() e_bodyclass="zab_page_default_body" />

<div class="window_iframe_box  w_clear">

<@m_zapmacro_common_ajax_chart b_page/>

<div class="w_clear">
<table id="tableView" style="height:310px;">
</table>
</div>

</div>

<script type="text/javascript">
zapjs.f.require(["cfamily/js/chooseProduct"],
	function(a){
		chooseProduct.init({
			<#if b_page.upReqValue("zw_s_iframe_page_code")??>
			pageCode:'${b_page.upReqValue("zw_s_iframe_page_code")}',
			</#if>
			<#if b_page.upReqValue("zw_s_iframe_page_param")??>
			pageParam: '${b_page.upReqValue("zw_s_iframe_page_param")?default("")}',
			</#if>
			<#if b_page.upReqValue("zw_s_iframe_max_select")??>
			maxSelect:'${b_page.upReqValue("zw_s_iframe_max_select")?default("0")}',
			</#if>
			<#if b_page.upReqValue("zw_s_iframe_choose_callback")??>
			chooseCallback:'${b_page.upReqValue("zw_s_iframe_choose_callback")?default("")}',
			</#if>
			<#if b_page.upReqValue("zw_s_iframe_choosed_values")??>
			choosedValues:'${b_page.upReqValue("zw_s_iframe_choosed_values")?default("")}',
			</#if>			
			});
	}
);
</script>

<div class="w_p_20">
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</div>

<@m_common_page_foot_base  />
