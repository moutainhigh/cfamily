<form class="form-horizontal" method="POST">
	<div id="chargeConfigDiv">
		<div class="control-group">
			<div class="chargeDiv" style="float: left;">
				<#if b_page.upAddData()??>
					<#list b_page.upAddData() as e>
					  	<#if e.getFieldTypeAid()=="104005019">
					  		<#if e.getPageFieldName()=="zw_f_seller_type">
				  				<select name="${e.getPageFieldName()}" onchange="sellerTypeChange(this);">
			      					<option value="">请选择商户类型</option>
			      					<#list b_page.upDataSource(e) as e_key>
										<option value="${e_key.getV()}" <#if e.getPageFieldValue()==e_key.getV()> selected="selected"</#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
				      		<#elseif e.getPageFieldName()=="zw_f_charge_type">
				      			<select name="${e.getPageFieldName()}">
			      					<option value="">请选择商品分类</option>
			      					<option value="0">全部类型</option>
			      					<#list b_page.upDataSource(e) as e_key>
										<option value="${e_key.getV()}" <#if e.getPageFieldValue()==e_key.getV()> selected="selected"</#if>>${e_key.getK()}</option>
									</#list>
					      		</select>
				      		<#elseif e.getPageFieldName()=="zw_f_charge_name">
				      			<select name="${e.getPageFieldName()}">
			      					<option value="">请选择佣金类型</option>
			      					<#--
			      					<#list b_page.upDataSource(e) as e_key>
										<option value="${e_key.getV()}" <#if e.getPageFieldValue()==e_key.getV()> selected="selected"</#if>>${e_key.getK()}</option>
									</#list>
									-->
					      		</select>
				      		</#if>
		  				<#else>
		  					<#if e.getPageFieldName()=="zw_f_profit_ratio">
		  						<input type="text" placeholder="利润百分比" name="${e.getPageFieldName()}" ${e.getFieldExtend()} />%
		  					<#elseif e.getPageFieldName()=="zw_f_charge_ratio">
		  						<input type="text" placeholder="佣金百分比" name="${e.getPageFieldName()}" ${e.getFieldExtend()} />%
		  					</#if>
		  				</#if>
				  	</#list>
				</#if>
			</div>
			<div style="float: left;">
				<a class="btn btn-link" href="javascript:void(0);" onclick="addColumn();">添加</a>
			</div>
		</div>
	</div>
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>


<script type="text/javascript" src="../resources/bf/chargeConfig.js"></script>