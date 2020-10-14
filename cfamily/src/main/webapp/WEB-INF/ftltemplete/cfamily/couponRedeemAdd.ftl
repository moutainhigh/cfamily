<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >
<script>
require(['cfamily/js/selectRedeemCouponActivity'],function(){
	zapjs.f.ready(function()
		{
			couponActivity.init('${activity_code}');
		}
	);
});
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
	<#if e.getPageFieldName() = "zw_f_activity_code">
				<div id="linkvalueDiv" class="control-group">
					<label class="control-label" for="zw_f_activity_code">
						活动编号：
					</label>
					<div class="controls">
						<div id="ac" class="w_left">
						</div>
						<input type="hidden" id="zw_f_activity_code" name="zw_f_activity_code" value="${activity_code}" />
					</div>
				</div>
				<div id="activityInfor">
					<div class="control-group">
						<label class="control-label" for="activityName">
							活动名称：
						</label>
						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul"><li id="activityName"></li></ul></div>
					</div>
					<div class="control-group">
						<label class="control-label" for="activity_description">
							活动描述：
						</label>
						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul"><li id="activity_description"></li></ul></div>
					</div>
					<div class="control-group">
						<label class="control-label" for="activity_startTime">
							活动开始时间：
						</label>
						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul"><li id="activity_startTime"></li></ul></div>
					</div>
					<div class="control-group">
						<label class="control-label" for="activity_endTime">
							活动结束时间：
						</label>
						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul"><li id="activity_endTime"></li></ul></div>
					</div>
				</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>