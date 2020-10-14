<#assign reply = b_method.upDataBysql("nc_order_evaluation_reply","select reply_content from nc_order_evaluation_reply where manage_code='SI2003'","","") />
<#assign zid = b_page.getReqMap()["zw_f_zid"]>
<@m_zapmacro_common_page_add b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST">	
<input id="zw_f_zid" type="hidden"  name="zw_f_zid" value="${zid}">  	
<div class="control-group">
	<label class="control-label" for="">回复内容选择：</label>
	<div class="controls">
		<select name="reply_content" id="reply_content">
			<option value="">--请选择--</option>
  			<#list reply as e_key>
				<option value="${e_key.reply_content}">${e_key.reply_content}</option>
			</#list>
  		</select>
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="">回复内容自定义：</label>
	<div class="controls">
		<textarea name="custom_reply"  value="" id="custom_reply"></textarea>
	</div>
</div>
	
<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />	
</form>
</#macro>
