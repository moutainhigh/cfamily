<#include "../zapmacro/zapmacro_common.ftl" />
<#include "../macro/macro_common.ftl" />
<@m_common_page_head_common e_title="刷新缓存" e_bodyclass="zab_page_default_body" />

<form class="form-horizontal" method="POST" >
  <fieldset>
    <legend>促销活动缓存刷新</legend>
	<div class="control-group">
		<label class="control-label" for="zw_f_product_code"><span class='w_regex_need'>*</span>活动编码：</label>
		<div class="controls">
		<textarea id="zw_f_event_code"  name="zw_f_event_code" placeholder="CX开头的活动编码，多个使用英文逗号分隔"></textarea>
		</div>
	</div>

	<div class="control-group">
		<div class="controls">
		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="263a56e5cfed11e7989e005056165069"  onclick="zapjs.zw.func_add(this)"  value="刷新" />
		</div>
	</div>
  </fieldset>
</form>
<div class="w_h_40"></div>
  
<@m_common_page_foot_base  />

<div class="w_h_20 "></div>
  


