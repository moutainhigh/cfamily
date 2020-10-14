<#assign ca_code=b_page.getReqMap()["category_code"] />
<#assign pc_code=b_page.getReqMap()["product_code"] />
<#assign manageCode = b_method.upUserInfo().getManageCode()>
<script>
require(['cfamily/js/modAddSkuProduct'],
function(p)
{
	zapjs.f.ready(function()
		{
			p.init_product('${ca_code}','${pc_code}');
		}
	);
}
);
function clolseAndrefresh (){
 		zapjs.zw.modal_show({
						content : '添加sku成功！',
						okfunc : 'parent.zapjs.f.tourl()'
					});
	}
</script>
<div class="w_p_20">
<input type="hidden" id="cshop_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
	<form class="form-horizontal">
	<input type="hidden" value="${pc_code}" name="zw_f_productCode" id="zw_f_productCode"/>
	<input type="hidden" value="${manageCode}" name="zw_f_sellerCode" id="zw_f_sellerCode"/>
	<input type="hidden"  name="zw_f_skuKey"  id="zw_f_skuKey"  value=""/>
	<input type="hidden"  name="zw_f_skuValue"  id="zw_f_skuValue"  value=""/>
	<input type="hidden"  id="hualidefenggexian"/>
	<div class="control-group">
		<label class="control-label" for="zw_f_costPrice">
			<span class="w_regex_need">*</span>
			成本价格：
		</label>
		<div class="controls">
			<input type="text" zapweb_attr_regex_title="成本价格" zapweb_attr_regex_id="469923180007" id="zw_f_costPrice" name="zw_f_costPrice" onkeyup="cshop_modAddSkuProduct.checkNum(this)"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_sellPrice">
			<span class="w_regex_need">*</span>
			销售价格：
		</label>
		<div class="controls">
			<input type="text" zapweb_attr_regex_title="销售价格" zapweb_attr_regex_id="469923180007" id="zw_f_sellPrice" name="zw_f_sellPrice" onkeyup="cshop_modAddSkuProduct.checkNum(this)"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_stockNum">
			<span class="w_regex_need">*</span>
			库存数量：
		</label>
		<div class="controls">
			<input type="text"  zapweb_attr_regex_title="库存数量" zapweb_attr_regex_id="469923180003" zapweb_attr_regex_id="" id="zw_f_stockNum" name="zw_f_stockNum" value="0" onkeyup="cshop_modAddSkuProduct.checkout_number(this)"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_securityStockNum">
			预警库存：
		</label>
		<div class="controls">
			<input type="text"  zapweb_attr_regex_title="预警库存" zapweb_attr_regex_id="" id="zw_f_securityStockNum" name="zw_f_securityStockNum" value="1" onkeyup="cshop_modAddSkuProduct.checkout_number(this)"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_miniOrder">
			起订数量：
		</label>
		<div class="controls">
			<input type="text"  zapweb_attr_regex_title="起订数量" id="zw_f_miniOrder" name="zw_f_miniOrder" value="1" onkeyup="cshop_modAddSkuProduct.checkout_number(this)"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_sellProductcode">
			货号：
		</label>
		<div class="controls">
			<input type="text"  zapweb_attr_regex_title="货号"  id="zw_f_sellProductcode" name="zw_f_sellProductcode" value=""/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_skuAdv">
			广告语：
		</label>
		<div class="controls">
			<textarea id="zw_f_skuAdv" name="zw_f_skuAdv" ></textarea>
		</div>
	</div>
	<#-- 提交按钮-->
	<@m_zapmacro_common_auto_operate b_page.getWebPage().getPageOperate() "116001016" />
	</form>
</div>