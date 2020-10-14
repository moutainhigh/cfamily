
<script>
require(['cfamily/js/contractBook'],

function()
{
	zapjs.f.ready(function()
		{
			contractBook.init();
		}
	);
}

);
</script>

<@m_zapmacro_common_page_book b_page />

<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	  <#if  e_field.getFieldName() = "contract_type">
	  		<div class="control-group">
				<label class="control-label" for="">合同类型:</label>
				<div class="controls">
					<div class="control_book">
						<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} <input type="hidden" id="contractType" value="${e_key.getK()}"></#if>
						</#list>
					</div>
				</div>
			</div>
	  <#elseif e_field.getFieldName() = "tax_rate">
	  	<div class="control-group">
			<label class="control-label" for="">进价税率:</label>
			<div class="controls">
				<div id="taxRate" class="control_book"></div>
			</div>
		</div>
	  <#elseif e_field.getFieldName() = "dissolution_time">
	  	<div id="jcrq" class="control-group">
			<label class="control-label" for="">解除协议日期:</label>
			<div class="controls">
				<div class="control_book">
					${e_field.getPageFieldValue()}
				</div>
			</div>
		</div>
	  <#elseif e_field.getFieldName() = "dissolution_instructions">
	  	<div id="jcsm" class="control-group">
			<label class="control-label" for="">解除协议说明:</label>
			<div class="controls">
				<div class="control_book">
					${e_field.getPageFieldValue()}
				</div>
			</div>
		</div>
	  <#elseif e_field.getFieldName() = "product_code">
	  	<div class="control-group">
			<label class="control-label" for="">商品编号:</label>
			<div class="controls">
				<div class="control_book">
					${e_field.getPageFieldValue()}
					<input type="hidden" id="productCode" value="${e_field.getPageFieldValue()}">
				</div>
			</div>
		</div>
	  <#elseif e_field.getFieldName() = "gross_profit">
	  	<div class="control-group">
			<label class="control-label" for="">毛利率:</label>
			<div class="controls">
				<div id="grossProfit" class="control_book"></div>
			</div>
		</div>
	  <#else>
	  	<@m_zapmacro_common_field_show e_field e_page/>
	  </#if>
  		<#if e_field.getFieldName() = "tax_rate">
			<div class="control-group">
				<label class="control-label" for="">不含税进价:</label>
				<div class="controls">
					<div id="nt" class="control_book"></div>
				</div>
			</div>
		<#elseif e_field.getFieldName() = "cost_price">
			<div class="control-group">
				<label class="control-label" for="">进价税额:</label>
				<div class="controls">
					<div id="costTax" class="control_book"></div>
				</div>
			</div>
		<#elseif e_field.getFieldName() = "md_name">
			<div class="control-group">
				<label class="control-label" for="">赠品:</label>
				<div class="controls">
					<div id="gift" class="control_book"></div>
				</div>
			</div>
		</#if>
	  
	  		
</#macro>
