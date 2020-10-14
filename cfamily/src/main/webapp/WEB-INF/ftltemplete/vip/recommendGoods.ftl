<#assign app_list = b_method.upDataQueryToJson("uc_appinfo","app_code,app_name","","app_code='SI2003'") />

<script>
require(['cmanage/js/recommendGoods'],

function()
{
	zapjs.f.ready(function()
		{
			appColumnAdd.init(${app_list});
		}
	);
}

);
</script>


<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list    e_page  />
	
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list    e_page>


	  	<@m_zapmacro_common_auto_field  e_page/>
	
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field  e_page>
		<div class="control-group">
	<label class="control-label" for="zw_f_app_code"><span class="w_regex_need">*</span>APP名称：</label>
	<div class="controls">

	      		<select name="zw_f_app_code" id="zw_f_app_code">

	      		</select>
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="zw_f_page_codeZero">序号：</label>
	<div class="controls">
	      		<input type="text" id="zw_f_column_nameZero" name="zw_f_column_nameZero"  value="">
	      		
	</div>
	
</div>

	  	
<div class="control-group">
           <script type="text/javascript">
	         zapjs.f.require(['zapadmin/js/ProductMultiSelect'],function(a){a.init({"text":"","source":"page_chart_v_hjy_pc_skuinfo","id":"zw_f_sku_code","value":"","max":"0"});});
           </script>
           
	<label class="control-label" for="zw_f_page_codeZero"><span class="w_regex_need">*</span>选择商品：</label>
	<div class="controls">
	      		<input class="btn" type="button" onclick="ProductMultiSelect.show_box('zw_f_sku_code')" value="选择商品">
	      		<input type="hidden" id="keyValueList" name="keyValueList"  value="">
	      		<!--
	      		<input type="hidden" id="keyTextList" name="keyTextList"  value="">
	      		-->
	      		
	</div>
	
</div>	  	
	  	
	  	
	  	
	  	
<div class="control-group">
	<label class="control-label" for="zw_f_page_codeZero">商品名称：</label>
	<div class="controls">
	      		<input type="text" id="keyTextList" name="keyTextList"  value="" readonly="readonly">
	      		
	</div>
	
</div>
	  	
	  	
	  	
	  		

</#macro>




<!--

<form class="form-horizontal" method="POST">


	  	
 	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
 
</form>-->



