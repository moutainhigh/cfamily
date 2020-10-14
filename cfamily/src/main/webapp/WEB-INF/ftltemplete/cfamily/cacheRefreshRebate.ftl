<#assign cService=b_method.upClass("com.cmall.familyhas.service.CacheOperateService")>
<#assign vOpt=cService.getCgroupMoneySwitch("view")>
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
	<div class="control-group">
		<label class="control-label" for="zw_f_operate_value">是否显示</label>
		<div class="controls">
	  		<select name="zw_f_operate_value" id="zw_f_operate_value">
	  				<option value="">请选择</option>
					<option value="0"  <#if "0"==vOpt> selected="selected" </#if>>不显示</option>
					<option value="1"  <#if "1"==vOpt> selected="selected" </#if>>显示</option>
	  		</select>
		</div>
	</div>
</#macro>
