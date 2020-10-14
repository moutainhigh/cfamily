<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
	<script type="text/javascript" src="${ss_assign_resources}js/order.js"></script>
	<style type="text/css">
		.wu,.share-qq,.qq-footer{display:none;}
	</style>
<@ss_common_body "qqbg"/>
<header><a href="javascript:;" class="fl jt">返回</a>使用优惠券<a href="instruction.html" class="sysm">使用说明</a></header>
<section class="coupon coupon2">
	<p><input type="text" value="" placeholder="请输入优惠码" id="coupon_code"><a href="javascript:;" id="ok">兑换</a></p>
	<div class="wu"><img src="${ss_assign_resources}/img/wu_bg.png" alt="">暂无优惠券</div>
</section>
<div class="wrap share-qqw" >
	<div class="share-qq">
		<div class="tit"><span>以下券适用于所结算商品</span></div>
		<div class="qq-use" id="couponList"></div>
	</div>
	<div class="share-qq">
		<div class="tit"><span>以下券不适用于所结算商品</span></div>
		<div class="qq-use qq-unuse" id="disableCouponList"></div>
	</div>
</div>
<footer class="qq-footer"><a href="javascript:;" class="btn-buy" data_val="" data_code="" data_id="">确 定</a></footer>
<div class="mask" style="position:fixed;top:0px;width:100%;height:100%;z-index:99999; display:none;">
    <div class="masked" style="position:fixed;background:#000;width:100%;height:100%;filter:alpha(opacity=50); /*IE滤镜，透明度50%*/-moz-opacity:0.5; /*Firefox私有，透明度50%*/opacity:0.5;/*其他，透明度50%*/"></div>
    <div class="maskcon" init-data="正在发送..." result-data="发送成功" style="text-align:center;position:fixed;color:#fff;top:50%;left:50%;margin-left:-50px;width: 120px;font-size:16px;">正在发送...</div>
</div>
<@ss_common_jscall "order.orderCouponConfim();"/>
<@ss_common_footer />