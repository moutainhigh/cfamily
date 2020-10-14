<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
<@ss_common_body "ddbg"/>
<div class="wrap wrap-wait" id="waiting_id" style="z-index: 100000;position: absolute;display:none">
	<div class="img" >
		<img src="${ss_assign_resources}img/waiting.gif" alt="">
	</div>
	<p>拼命加载中<br>...</p>
</div>
<header><span class="fl jt">向左</span>选择支付方式</div></header>
<div class="wrap share-dd share-dd2">
	<div class="money">请支付：<em id="order_money"><b>￥</b></em></div>
	<div class="sbox apply">
		<div class="fx">
			<div class="lid" style="display:none;">
				<a href="javascript:;" class="sela">&nbsp;</a><span class="fx-zf"><b>&nbsp;</b>支付宝</span>
			</div>
			<div class="lid" style="display:none;">
				<a href="javasscript:;" class="sela">&nbsp;</a><span class="fx-wx"><b>&nbsp;</b>微信支付</span>
			</div>
		</div>
	</div>
	<a href="javascript:;" class="btn-apply" id="btn">立即支付</a>
</div>
<div id="mask">&nbsp;</div>
<script type="text/javascript" src="${ss_assign_resources}js/ordersucceed.js"></script>
<@ss_common_jscall "ordersucceed.payagain();"/>
<@ss_common_footer />