<@m_common_page_head_common e_title=b_page.getWebPage().getPageName() e_bodyclass="zab_page_default_body" />

<#assign parentid=b_page.upReqValue("zw_s_iframe_select_source") >

<#assign boxid=parentid >

<style>
.datagrid-row{height: 35px}
.datagrid-cell{height: 26px;}
</style>
<div id="${boxid}" class="window_iframe_box  w_clear">

<@m_zapmacro_common_ajax_chart b_page/>

</div>	
<div class="w_p_20">
<input type="button" class="btn  window_iframe_btn" value="确认选择" onclick="liveRoomProduct.ok_full_value('${b_page.upReqValue("zw_s_iframe_select_callback")?default("")}','${parentid}','${boxid}')"/>
</div>


<script type="text/javascript">
zapjs.f.require(["cfamily/js/liveRoomProduct"],function(a){liveRoomProduct.init({pagecode:'${b_page.upReqValue("zw_s_iframe_select_page")}',id:'${boxid}',maxnum:'${b_page.upReqValue("zw_s_iframe_max_select")?default("0")}'});});
</script>

<@m_common_page_foot_base  />
