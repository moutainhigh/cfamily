<#assign zid = b_page.getReqMap()["zw_f_zid"]>
<br><br>
<#-- 添加页 -->
<form class="form-horizontal" method="POST">	
<div class="control-group">
	<label class="control-label" for="">处理内容：</label>
	<div class="controls">
		<textarea name="content"  value="" placeholder="最多可以保存200字" rows="6" style="width:350px"></textarea>
	</div>
</div>
<input id="zw_f_zid" type="hidden"  name="zw_f_zid" value="${zid}">  	
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>