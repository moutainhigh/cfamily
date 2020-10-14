<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
<@ss_common_body "ddbg"/>
<div class="wrap wrap-wait" id="waiting_id" style="z-index: 100000;position: absolute;display:none">
	<div class="img" >
		<img src="${ss_assign_resources}img/waiting.gif" alt="">
	</div>
	<p>拼命加载中<br>...</p>
</div>
<header><span class="fl jt">向左</span>支付失败</div></header>
<div class="nodata2">
	<div class="img">
		<img src="${ss_assign_resources}img/bg-nodata2.png" alt=""/>
	</div>
	<div class="txt">
		<h2>支付失败！</h2>
		<p id="orderNoId"></p>
		<p id="actualMoneyId"></p>
		<p id="toRebateMoneyId"></p>
	</div>
	<a href="javascript:;" class="btn" id="btn">继续支付</a>
	<div id="mask">&nbsp;</div>
<script type="text/javascript" src="${ss_assign_resources}js/ordersucceed.js"></script>
<@ss_common_jscall "ordersucceed.payfail();"/>
<@ss_common_footer />