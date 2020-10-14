<@m_zapmacro_common_page_edit b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upBookData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata e_page>

	<#if e_pagedata??>
	
	
			<div class="control-group">
				<label class="control-label" for="zw_f_code"><span class='w_regex_need'>*</span>编号：</label>
				<div class="controls">
				<#list e_pagedata as e_field>
					<#if e_field.getPageFieldName() = "zw_f_code">
						<input type="text" id="zw_f_code" name="zw_f_code"  zapweb_attr_regex_id="469923180002"  value="${e_field.getPageFieldValue()}">
					</#if>
				</#list>
					
				</div>
				</div>
	
			
			<div class="control-group">
				<label class="control-label" for="zw_f_account_day"><span class='w_regex_need'>*</span>结算日：</label>
				<div class="controls">
					<#list e_pagedata as e_field>
				
					<#if e_field.getPageFieldName() = "zw_f_account_day">
						${e_field.getPageFieldValue()}
						<input type="hidden" id="zw_f_account_day" name="zw_f_account_day"  zapweb_attr_regex_id="469923180002"  value="${e_field.getPageFieldValue()}">
					</#if>
					<#if e_field.getPageFieldName() = "zw_f_uid">
						<input type="hidden" id="zw_f_uid" name="zw_f_uid"  zapweb_attr_regex_id="469923180002"  value="${e_field.getPageFieldValue()}">
					</#if>
					
				</#list>
				</div>
			</div>
			
			
	
		
			<div class="control-group">
				<div class="controls"><b>销售统计周期</b></div>
			</div>
					<div class="control-group">
					<label class="control-label" for="zw_f_sale_begin_month"><span class='w_regex_need'>*</span>开始日期：</label>
					<div class="controls">
						
						<#list e_pagedata as e_field>
				
							<#if e_field.getPageFieldName() = "zw_f_sale_begin_month" || e_field.getPageFieldName() = "zw_f_sale_begin_day">
												      		
					      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
					      			
					      			<option value="" selected="selected" >请选择</option>
					      				
					      			<#list e_page.upDataSource(e_field) as e_key>
				
										<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected"  </#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
							</#if>
							
						</#list>
					</div>
					
					</div>
				
					<div class="control-group">
					
					<label class="control-label" for="zw_f_sale_end_month"><span class='w_regex_need'>*</span>结束日期：</label>
					
					<div class="controls">
						
						<#list e_pagedata as e_field>
				
							<#if e_field.getPageFieldName() = "zw_f_sale_end_month" || e_field.getPageFieldName() = "zw_f_sale_end_day">
								<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
					      			
					      			<option value="" selected="selected" >请选择</option>
					      				
					      			<#list e_page.upDataSource(e_field) as e_key>
				
										<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected"  </#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
							</#if>
							
						</#list>
						
					</div>
					
					</div>
		
		<div class="control-group">
				<div class="controls"><b>退货统计周期</b></div>
			</div>
					<div class="control-group">
					<label class="control-label" for="zw_f_return_begin_month"><span class='w_regex_need'>*</span>开始日期：</label>
					<div class="controls">
					
						<#list e_pagedata as e_field>
				
							<#if e_field.getPageFieldName() = "zw_f_return_begin_month" || e_field.getPageFieldName() = "zw_f_return_begin_day">
								<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
					      			
					      			<option value="" selected="selected" >请选择</option>
					      				
					      			<#list e_page.upDataSource(e_field) as e_key>
				
										<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected"  </#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
							</#if>
							
						</#list>
						
					</div>
					
					</div>
					
					
				
					<div class="control-group">
					<label class="control-label" for="zw_f_return_end_month"><span class='w_regex_need'>*</span>结束日期：</label>
					
					<div class="controls">
						
						<#list e_pagedata as e_field>
				
							<#if e_field.getPageFieldName() = "zw_f_return_end_month" || e_field.getPageFieldName() = "zw_f_return_end_day">
								<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
					      			
					      			<option value="" selected="selected" >请选择</option>
					      				
					      			<#list e_page.upDataSource(e_field) as e_key>
				
										<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected"  </#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
							</#if>
							
						</#list>
						
					</div>
					</div>
					

	  	

	</#if>
</#macro>




