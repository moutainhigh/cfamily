
<#assign productCode = b_page.getReqMap()["zw_f_product_code"]>
<script>
require(['cfamily/js/previewCheckProduct'],
function(p)
{
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(productCode)};
	zapjs.f.ready(function()
	{
		p.init_product(product);
	});
});
</script>
<div class="zab_info_page">
<div class="zab_info_page_title  w_clear">
<span>商品详细信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<div class="control-group">
		<label class="control-label" for="">商品名称:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_product_name"> </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">市场价格:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_market_price"></div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">成本价格:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_cost_price"> </div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">税率:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_tax_rate"></div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">商品视频链接:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_video_url"></div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">货号:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_sell_productcode"></div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品品牌:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_brand"></div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">商品重量:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_product_weight"></div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">商品体积:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_product_volume"></div>
			
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">商品标签:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_keyword"></div>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">运费模式:</label>
		<div class="controls">
			<div class="control_book"> 
				<div id="cfamily_addproduct_travel">
	 				<ul class="w_ullist">
	 					<li>
	 					<input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_0" value="0" checked="checked"/><label for="zw_f_tra_select_0">卖家包邮</label>
	 					</li>
	 					<li class="w_mt_15">
	 					<input type="radio" name="zw_f_tra_select"  id="zw_f_tra_select_1" value="1"/><label for="zw_f_tra_select_1">买家承担运费&nbsp;&nbsp;&nbsp;</label><input type="text" id="zw_f_tra_user"   class="span2" value="">
	 					</li>
	 					<li class="w_mt_15">
	 						<div class="w_left"><input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_2" value="2"/><label for="zw_f_tra_select_2"><input class="btn btn-small" type="button" value="选择运费模板" onclick="zapadmin_single.show_box('zw_f_traval')"/></label>
	 						<input id="zw_f_traval_show_text" type="hidden" value="">
	 						<script type="text/javascript">zapjs.f.require(['zapadmin/js/zapadmin_single'],function(a){a.init({"text":"","buttonflag":false,"source":"page_chart_v_chart_uc_freight_tpl","id":"zw_f_traval","value":"","max":"1"});});</script>
	 						</div>
	 						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul" id="zw_f_traval_show_ul"></ul></div>
	 					</li>
	 				</ul>
 				</div>
			
			 </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品主图:</label>
		<div class="controls" id="zw_f_mainpic_url">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品描述:</label>
		<div class="controls">
			<div class="control_book" id="zw_f_editor"></div>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="">商品图片:</label>
		<div class="controls" id="zw_f_piclist">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">描述图片:</label>
		<div class="controls" id="zw_f_description_pic">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">商品规格:</label>
		<div class="controls" id="zw_f_description_pic">
				<div id="cshop_addproduct_custom"  class="csb_cshop_addproduct_pextend   w_clear">
	   		 		<div class="csb_cshop_addproduct_title">自定义属性</div>
	   		 		<div class="csb_cshop_addproduct_item">
			   		 	<table  class="table ">
				   		 	<thead>
				   		 		<tr>
				   		 			<th>名称</th>
				   		 			<th>内容</th>
				   		 			<th></th>
				   		 		</tr>
				   		 	</thead>
				   		 	<tbody>
				   		 	</tbody>
			   		 	</table>
	   		 		</div>
	   		 	</div>
			</div>
		</div>
	</div>
</form>
</#macro>
</br>
