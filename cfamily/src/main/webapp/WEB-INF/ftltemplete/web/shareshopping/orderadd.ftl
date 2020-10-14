<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
	<script type="text/javascript" src="${ss_assign_resources}js/order.js"></script>
<@ss_common_body "ddbg"/>
<header><a id="backurl_id"><span class="fl jt">向左</span></a>订单确认</div></header>
<div class="wrap share-dd">
	<div class="address2">
		<div>
			<p class="address-t"><span class="name"><b>&nbsp;</b></span><span class="phone"><b>&nbsp;</b></span></p>
			<p id="address_street"></p>
		</div>
	</div>
	
	<div class="sbox apply">
		<div class="fx">
			<div class="divt">支付方式</div>
			<div class="lid" style="display:none;">
				<a href="javascript:;" class="sela" pay_type="449746280003">&nbsp;</a><span class="fx-zf"><b>&nbsp;</b>支付宝</span>
			</div>
			<div class="lid " style="display:none;">
				<a href="javascript:;" class="sela" pay_type="449746280005">&nbsp;</a><span class="fx-wx"><b>&nbsp;</b>微信支付</span>
			</div>
			<div class="lid" style="display:none;">
				<a href="javascript:;" class="sela" pay_type="449716200002">&nbsp;</a><span class="fx-df"><b>&nbsp;</b>货到付款</span>
			</div>
		</div>
		<div class="sub"><b class="jt3" id="receipt">&nbsp;</b><span class="tspan">发票信息</span></div>
		<div class="sub"><b class="jt3" id="coupon">&nbsp;</b>优惠券<span class="qnum" style="display:none;"></span><span class="fr bky bky1">无可用</span><span class="fr bky bky2" style="color:red;display:none;"></span></div>
	</div>
	<div class="sbox money">
		<p><span id="cost_money">￥0.00</span>商品总金额：</p>
		<p style="display:none;"><span id="sub_money">-  ￥0.00</span>优惠券：</p>  
		<p><span id="sent_money">+  ￥0.00</span>运费：</p>
	</div>
	<#-- <div class="tip"><span id="prompt">注：</span>新疆、西藏地区每单收24元运费，由派件员送货时向您收取；</div>-->
	<div class="tip"><span>注：</span>新疆、西藏地区每单收24元运费；</div>
</div>
<div class="dd-fexed">
	<a href="javascript:;" class="btn">确  认</a>
	<span id="cash_back">实付款:￥<b id="pay_money">0.00</b></span>
</div>
<div class="mask" style="position:fixed;top:0px;width:100%;height:100%;z-index:99999; display:none;">
    <div class="masked" style="position:fixed;background:#000;width:100%;height:100%;filter:alpha(opacity=50); /*IE滤镜，透明度50%*/-moz-opacity:0.5; /*Firefox私有，透明度50%*/opacity:0.5;/*其他，透明度50%*/"></div>
    <div class="maskcon" init-data="我正在努力啊..." result-success="订单创建成功" result-fail="订单创建失败" style="text-align:center;position:fixed;color:#fff;top:50%;left:50%;margin-left:-50px;width: 120px;font-size:16px;">我正在努力啊...</div>
</div>
<@ss_common_jscall "order.orderadd();"/>
<@ss_common_footer />