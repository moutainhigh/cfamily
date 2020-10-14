<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
<@ss_common_body "ddbg"/>
<header><a id="backHrefId"><span class="fl jt">向左</span></a><div id="pageTitleId">订单提交成功</div></header>
<div id="bodyDivId" class="nodata2 pay-ok pay-ok2">
<input type="hidden" id="baseUri" value="${ss_assign_resources}"/>
<input type="hidden" id="pagetypeId" />
<input type="hidden" id="productCodeId"/>
<input type="hidden" id="amount"/>
<input type="hidden" id="tradeStatus"/>
<!--订单id-->
<input type="hidden" id="outTradeNoId" />
	<div class="img">
		<img id="payStatusImgId" src="${ss_assign_resources}img/pay-ok.png" alt=""/>
	</div>
	<div class="txt">
		<h2 id="payTypeTextId">下单成功!</h2>
		<p>订单编号：<br/><span id="orderNoId"></span></p>
		<p>应付金额：<span id="actualMoneyId">￥0.00</span></p>
		<p>预计返利：<span id="toRebateMoneyId">￥0.00</span></p>
	</div>
	<a id="orderOperateId" href="javascript:downloadapp.download();" class="btn">查看订单详情</a>
	<div id="setPasswdDivId" class="mmsz">
		<p>设置密码后，</br>登录APP可提取返利哦~</p>
		<input type="password" value="" id="newPassWdId" placeholder="请设置密码（限6-16位字符，不能包含空格）" />
		<a href="javascript:ordersucceed.saveNewUserPasswd();" class="btn-save">保存</a>
	</div>
</div>
<script type="text/javascript" src="${ss_assign_resources}js/ordersucceed.js"></script>
<script type="text/javascript" src="${ss_assign_resources}js/downloadapp.js"></script>
<@ss_common_jscall "ordersucceed.init();"/>
<@ss_common_footer />