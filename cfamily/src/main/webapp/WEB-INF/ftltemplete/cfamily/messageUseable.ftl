<#assign cService=b_method.upClass("com.cmall.familyhas.service.ShoppingCartlikeService")>
<#assign vOpt=cService.getMessageUsableFlag("switch")>
<#assign operateValue="">
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
	<div class="control-group">
		<label class="control-label" for="zw_f_is_flag">是否启用</label>
		<div class="controls">
	  		<select name="zw_f_is_flag" id="zw_f_is_flag">
					<option value="4497480100020002"  <#if "4497480100020002" == vOpt> selected="selected" </#if>>否</option>
					<option value="4497480100020001"  <#if "4497480100020001" == vOpt> selected="selected" </#if>>是</option>
	  		</select>
		</div>
	</div>
</#macro>
