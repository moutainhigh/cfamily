<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
<@ss_common_body "dowbg"/>
<div class="wrap download">
<input type="hidden" id="baseUri" value="${ss_assign_resources}"/>
	<p id="newUserShowRebateId">恭喜您已成为特权用户</br>去“惠家有”购物可得到返现哦！</p>
	<div class="banner">
		<div class="scroll-wrapper" id="idContainer2" ontouchstart="touchStart(event)" ontouchmove="touchMove(event);" ontouchend="touchEnd(event);">
		<ul class="scroller" id="idSlider2">
		<li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/down-pic1.jpg" /></a></li>
		<li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/down-pic2.jpg" /></a></li>
		<li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/down-pic3.jpg" /></a></li>
		<li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/down-pic4.jpg" /></a></li>
		</ul>
		<div class="banner-numw">
			<ul class="banner-num" id="idNum">
			</ul>
		</div>
	</div>
	<div class="btns">
		<a id="androidDownload" href="javascript:downloadapp.download('android');"><img src="${ss_assign_resources}img/btn-an.png"></a>
		<a id="iosDownload" href="javascript:downloadapp.download('ios');"><img src="${ss_assign_resources}img/btn-ap.png"></a>
		<!--<a href="http://www.ichsy.com/apps/Hui_Jia_You_ichsy.apk "><img src="${ss_assign_resources}img/btn-an.png"></a>
		<a href="https://itunes.apple.com/cn/app/jia-you-gou-wu/id641952456?mt=8"><img src="${ss_assign_resources}img/btn-ap.png"></a>-->
	</div>
</div> 
<script type="text/javascript" src="${ss_assign_resources}js/focus.js"></script>
<script type="text/javascript" src="${ss_assign_resources}js/downloadapp.js"></script>
<@ss_common_jscall "downloadapp.init();"/>
<@ss_common_footer />