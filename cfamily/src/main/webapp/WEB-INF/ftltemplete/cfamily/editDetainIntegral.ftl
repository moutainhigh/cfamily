
<@m_common_html_script "require(['cfamily/js/addDetainIntegral'],function(a){a.init_page()});" />

<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  b_page.upEditData()   b_page  />
			
	<div class="control-group">
    	<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="${b_page.getWebPage().getPageOperate()[0].getOperateUid()}" onclick="addDetainIntegral.func_add(this)" value="${b_page.getWebPage().getPageOperate()[0].getOperateName()}">
    	</div>
	</div>
</form>
