<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
	<style type="text/css">
		.ds, #ds{display: none;}
		.popup{overflow:hidden;}
		.popup i{display:block;width:100%;height:100%;position:fixed;left:0;top:0;background:#000;z-index:200;filter:alpha(opacity=55);-moz-opacity:0.55;opacity:0.55;_position:absolute;*position:absolute;filter:alpha(opacity=55); -moz-opacity:0.55; opacity:0.55;}
		.popup .cont{width: 260px;height: auto;overflow: hidden;background: #fff;z-index: 201;position: fixed;left: 50%;top: 50%;margin: -100px 0 0 -130px;border-radius: 10px;}
		.popup .cont h3 {font-size:16px;font-weight:normal;line-height:30px;height:30px;text-align:center;}
		.popup .cont h3 {padding-top:10px;}
		.popup .cont p {text-align:center;font-size:16px;margin-bottom:10px;}
		.popup .cont .popup_btn a{width: 49%;text-align: center;height: 45px;line-height: 45px;display: inline-block;border-top: solid 1px #B2B2B2;color:#2A94FA; float: left; font-size:16px;}
		.popup .cont .popup_btn a.first_a{border-right:solid 1px #B2B2B2;width: 50%;}
	</style>
	<script type="text/javascript" src="${ss_assign_resources}js/order.js"></script>
<@ss_common_body "ddbg"/>
<header><a href="javascript:;" class="jt">返回</a>发票信息<a href="javascript:;" class="bc">保存</a></header>
<div class="wrap wrap-fp">
	<div class="sbox seld">
		<div class="divt">发票类型</div>
		<div class="sela" id="sela"><a href="javascript:;" class="on" type='0'><b>&nbsp;</b>不开发票</a><a href="javascript:;" type='1'><b>&nbsp;</b>普通发票</a></div>
	</div>
	<div class="sbox seld sel-lx ds">
		<div class="divt">发票抬头</div>
		<div class="sela" id="sela_divt"><a href="javascript:;" class="on" type='0'><b>&nbsp;</b>个人</a><a href="javascript:;" type='1'><b>&nbsp;</b>公司</a></div>
	</div>
	<textarea placeholder="请填写公司名称" id="ds" maxLength="50"></textarea>
	<div class="sbox mx ds">
		<b class="jt3">&nbsp;</b><span clsss="fl">发票内容：</span>
		<select id="receipt"></select>
	</div>
	<div class="tip ds"><span>注：</span>使用优惠券抵押的金额不可开具发票。</div>
</div>
<div class="popup" style="display:none;">
    <i></i>
    <div class="cont">
        <div class="info_del">
            <h3>提示</h3>
            <p>放弃填写的内容吗？</p>
            <div class="popup_btn">
                <a class="first_a" href="orderadd.html">放弃</a>
                <a class="last_a" href="javascript:;">继续填写</a>
                <div class="clear"></div>
            </div>
        </div>
    </div>
</div>
<@ss_common_jscall "order.receipt();"/>
<@ss_common_footer />