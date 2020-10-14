<#assign product_code = b_page.getReqMap()["product_code"]>
<@m_common_html_css ["css/icon-min.css"] />
<@m_common_html_css ["css/csb_base.css"] />
<script>
require(['cfamily/js/modAddSkuProductBatch'],
function(p)
{
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(product_code)};
	zapjs.f.ready(function()
	{
		p.init_product(product);
	});
});
</script>
<style>
.hint-layer {
    width:530px;
    background-color: #fff;
    border: 1px solid #bed3f1;
    box-shadow: 1px 1px 2px 0 rgba(0, 0, 0, 0.3);
    padding:10px 0 20px;
     position:absolute;
     overflow:hidden
}
.hint-layer .hint-ext-close {
    cursor: pointer;
    height: 13px;
    outline: 0 none;
    overflow: hidden;
    position: absolute;
    right: 5px;
    text-decoration: none;
    top: 5px;
    vertical-align: middle;
    width: 13px;
}
.hint-layer .hint-ext-close:hover {
    background: #fff none repeat scroll 0 0;
    border: 1px solid #ced5e0;
}
.hint-layer .hint-ext-close-x {
    background: rgba(0, 0, 0, 0) url("//img.alicdn.com/tps/i3/T161zXXjXhXXblwoHk-19-100.png") no-repeat scroll -999em -999em;
}
.hint-layer .hint-ext-close-x {
    background-position: -5px -45px;
    display: block;
    height: 9px;
    margin: 2px;
    text-indent: -9999px;
    width: 20px;
}
.hint-layer .batch-header{height:20px; line-height:20px; padding:0 0 0 10px;font-weight:bold; }
.hint-layer .batch-type{float:left; display:inline; width:30%; border-left:1px dotted #ccc; padding:0 0 0 15px}
.hint-layer .first{border:none;}
.hint-layer .batch-type .caption{width:100%; }
.hint-layer .batch-type .list{width:100%; list-style:none; margin:10px 0 0 0}
.hint-layer .batch-type .list input{float:left; display:inline;}
.hint-layer .batch-foot{float:left; display:inline; width:100%; text-align:center; padding:10px 0 0; overflow:hidden}
.hint-layer .batch-foot a{display:inline-block; margin:0 10px;}

.help-pop{position:absolute; left:300px; top:300px; display:none; overflow: hidden;}
.help-pop .hpop-c{width:290px; height:auto; background:#ffffe5; padding:10px; border:1px solid #eee9ba; margin:0 0 8px; font-size:12px; line-height:18px; color:#333; overflow: hidden;}
.help-pop i{display:block; position:absolute;  left:50%; margin-left:-5px; bottom:1px; width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-top: 8px solid #ffffe5; z-index:2 }
.help-pop b{display:block; position:absolute;  left:50%; margin-left:-7px; bottom:0; width: 0;height: 0;border-left: 10px solid transparent;border-right: 10px solid transparent;border-top: 9px solid #eee9ba; z-index:1  }
</style>
<input type="hidden" id="cshop_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
<form class="form-horizontal" role="form">
<input type="hidden" id="zw_f_json" name="zw_f_json" value=""/>
<input type="hidden" id="zw_f_product_code" name="zw_f_product_code" value="${product_code}"/>
<fieldset>
	<input name="zw_f_listbox" id="zw_f_listbox" type="hidden" value="">
	<input name="zw_f_category_codes" id="zw_f_category_codes" type="hidden" value="">
	<div id="OtherProductInfo">
			<div class="control-group">
				<div class="controls" style="margin-left:0px">
 					<div id="cshop_product_insert_prop">
			   		 	<div id="cshop_addproduct_prop" class="csb_cshop_addproduct_prop w_display  w_clear">
			   		 		<div class="csb_cshop_addproduct_title">基本属性</div>
			   		 		<div class="csb_cshop_addproduct_item"></div>
			   		 	</div>
	   		 	
			   		 	<div id="cshop_addproduct_colors" class="csb_cshop_addproduct_colors  w_display w_clear">
				   		 	<div class="csb_cshop_addproduct_title">各颜色图片</div>
				   		 	<div class="csb_cshop_addproduct_item">
					   		 	<table  class="table ">
						   		 	<thead>
						   		 		<tr>
							   		 		<th>图片(没有可以不上传)
							   		 		</th>
						   		 		</tr>
						   		 	</thead>
						   		 	<tbody>
						   		 	
						   		 	</tbody>
					   		 	</table>
				   		 	</div>
			   		 	
			   		 	</div>
	   		 	
			   		 	<div id="cshop_addproduct_plist"  class="csb_cshop_addproduct_plist  w_display w_clear">
			   		 		<div class="csb_cshop_addproduct_title">SKU设置</div>
			   		 		<div class="csb_cshop_addproduct_item">
					   		 	<table  class="table">
						   		 	<thead>
						   		 		<tr>
						   		 		<th><span class="w_regex_need">*</span>成本价
						   		 		</th>
						   		 		<th><span class="w_regex_need">*</span>销售价
						   		 		</th>
						   		 		<th><span class="w_regex_need">*</span>库存
						   		 		</th>
						   		 		<th>预警库存
						   		 		</th>
						   		 		<th>起订数量
						   		 		</th>
						   		 		<th>货号
						   		 		</th>
						   		 		<th>广告语</th>
						   		 		<th>批量操作</th>
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
		<div>
	</div>
</fieldset>
	<#-- 提交按钮-->
	<@m_zapmacro_common_auto_operate b_page.getWebPage().getPageOperate() "116001016" />
</form>

<#--批量设置弹出层-->
<div class="sku-batchlayer hint-layer hint-overlay hint-ext-position" id="group_operation" style="display:none;">
	<div class="hint-contentbox" id="second_div">
		<div class="batch-header">批量设置</div>
		<div class="batch-body">
			<div class="batch-type">
				<div class="caption">成本价：</div>
				<ul class="list">
					<li>
						<input id="batch-cost-0" class="batch-radio" type="radio" name="batch-cost" data-index="0" data-type="price" >
						<label for="batch-cost-0">同颜色分类成本价相同</label>
					</li>
					<li>
						<input id="batch-cost-1" class="batch-radio" type="radio" name="batch-cost" data-index="1" data-type="price" >
						<label for="batch-cost-1">同款式分类成本价相同</label>
					</li>
				</ul>
			</div>
			<div class="batch-type first">
				<div class="caption">价格：</div>
				<ul class="list">
					<li>
						<input id="batch-price-0" class="batch-radio" type="radio" name="batch-price" data-index="0" data-type="price" >
						<label for="batch-price-0">同颜色分类价格相同</label>
					</li>
					<li>
						<input id="batch-price-1" class="batch-radio" type="radio" name="batch-price" data-index="1" data-type="price" >
						<label for="batch-price-1">同款式分类价格相同</label>
					</li>
				</ul>
			</div>
			<div class="batch-type">
				<div class="caption">库存：</div>
				<ul class="list">
					<li>
						<input id="batch-quantity-0" class="batch-radio" type="radio" name="batch-quantity" data-index="0" data-type="quantity" >
						<label for="batch-quantity-0">同颜色分类库存相同</label>
					</li>
					<li>
						<input id="batch-quantity-1" class="batch-radio" type="radio" name="batch-quantity" data-index="1" data-type="quantity" >
						<label for="batch-quantity-1">同款式分类库存相同</label>
					</li>
				</ul>
			</div>
		</div>
		<div class="batch-foot">
			<a id="copyOk" class="btn btn-default" onclick="cshop_product.onclick_ok()">
				<span class="btn-txt">确定</span>
			</a>
			<a class="cancel" href="javascript:void(0)" onclick="cshop_product.onclick_cancle()">取消</a>
		</div>
	</div>
	<a class="hint-ext-close" role="button" href="javascript:void("关闭")" tabindex="0" onclick="cshop_product.onclick_x()">
		<span class="hint-ext-close-x" >关闭</span>
	</a>
</div>
