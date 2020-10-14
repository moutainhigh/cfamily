
<@m_common_html_script "require(['cfamily/js/addReturnGoodsNew'],function(a){a.init_page()});" />

<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  b_page.upAddData()   b_page  />
		
		
		
		<input type="hidden" id="small_seller_code"  value="">
		
	<div class="control-group">
		<label class="control-label" for=""><span class="w_regex_need">*</span>退款金额：</label>
		<div class="controls">
			<input type="text" id="return_money" name=""  value="0.00" readonly="readonly">
		</div>
	</div>
	
	
	<div id="details"></div>
	
	
	<div class="control-group">
    	<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="${b_page.getWebPage().getPageOperate()[0].getOperateUid()}" onclick="addReturnGoodsNew.func_add(this)" value="${b_page.getWebPage().getPageOperate()[0].getOperateName()}">
    	</div>
	</div>
	
	
	
	
</form>
