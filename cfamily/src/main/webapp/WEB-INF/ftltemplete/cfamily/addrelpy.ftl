<#assign reply = b_method.upDataBysql("nc_order_evaluation_reply","select reply_content from nc_order_evaluation_reply where manage_code='SI2003'","","") />
<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<@m_zapmacro_common_page_add b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST">	
		<div class="control-group">
				<label class="control-label" for="">回复内容选择：</label>
				<div class="controls">
					<select name="reply_content" id="reply_content">
						<option value="0">--请选择--</option>
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
	<input id="zw_f_uid" type="hidden"  name="zw_f_uid" value="${uuid}">  	
	<div class="control-group">
		<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="94b06df9a52c11e5aad9005056925439" onclick="comment.beforeSubmit(this)" value="提交新增">
		</div>
	</div>
</form>
</#macro>
<@m_zapmacro_common_html_script "require(['cfamily/js/comment'],function(){zapjs.f.ready(function(){comment.init();});});" />	
