<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
	<script type="text/javascript" src="${ss_assign_resources}js/order.js"></script>
<@ss_common_body "ddbg"/>
<header><a id="backurl_id"><span class="fl jt">向左</span></a>订单确认</div></header>
<div class="wrap share-dd">
	<div class="address">
		<div><b class="fr ico-jt2">&nbsp;</b><b class="fl ico-edit">&nbsp;</b>请填写收货人信息</div>
	</div>
	<div class="sbox money">
		<p><span id="totalMoney">￥0.00</span>商品总金额：</p>
		<p style="display:none;"><span>-  ￥0.00</span>优惠券：</p>  
		<p><span>+  ￥0.00</span>运费：</p>
	</div>
	<#-- <div class="tip"><span>注：</span>新疆、西藏地区每单收24元运费，由派件员送货时向您收取；</div> -->
	<div class="tip"><span>注：</span>新疆、西藏地区每单收24元运费；</div>
</div>
<div class="dd-fexed">
	<a href="javascript:;" class="btn">确  认</a>
	<span>实付款:￥<b id="realMoney">0.00</b></span>
</div>
<@ss_common_jscall "order.orderconfirm();"/>
<@ss_common_footer />