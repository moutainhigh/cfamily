<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<#import "../../macro/macro_baidutongji.ftl" as tongji />
<@ss_common_head/>
<script type="text/javascript" src="${ss_assign_resources}js/activityproductdetail.js"></script>
<script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
<@tongji.m_common_tongji ""/>

<@ss_common_body/>
<@ss_common_user_defined_head />
<div class="wrap wrap-wait" id="waiting_id" style="z-index: 100000;position: absolute;display:none">
	<div class="img" >
		<img src="${ss_assign_resources}img/waiting.gif" alt="">
	</div>
	<p>拼命加载中<br>...</p>
</div>

<div class="wrap share-pro">
    <div class="box pro">
        <div class="banner" id="picList_id">
            <!--
            <div class="tip"><p>没货啦，下次早点来哦～</p><span>&nbsp;</span></div>
            <div class="scroll-wrapper" id="idContainer2" ontouchstart="touchStart(event)" ontouchmove="touchMove(event);" ontouchend="touchEnd(event);">
                <ul class="scroller" id="idSlider2">
                    <li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/demoImg/banner.jpg" /></a></li>
                    <li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/demoImg/banner.jpg" /></a></li>
                    <li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/demoImg/banner.jpg" /></a></li>
                    <li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/demoImg/banner.jpg" /></a></li>
                    <li style="width:-100%"><a href=''><img src="${ss_assign_resources}img/demoImg/banner.jpg" /></a></li>
                </ul>
                <div class="banner-numw">
                    <ul class="banner-num" id="idNum">
                    </ul>
                </div>
            </div>
            -->
        </div> 
            
        <div class="num" id="product_base_info_id">
         <!--<h2>摩登情人 七分袖斜拉牛仔外套七分 袖斜花纹牛仔外套</h2>
            <p class="bianh">商品编号：<span>102239</span></p>
            <div class="sbox">
            		<div class="time">剩&nbsp;<span>15</span>:<span>23</span>:<span>45</span></div>
            		<p class="price"><em class="nprice"><em>￥</em>390.00</em><span><em>￥</em>1245.00</span></p>
           </div>-->
        </div>
        <div class="num-toal" id="num_total_id">
         <!--<div>月销<span><b>3000</b> 件</span></div>
        		<div>返利<span class="price">￥<b>6.00</b></span></div>-->
        </div>
    </div>
    <div class="sales" id="sales_area_id">
    </div>
    <div class="box sizes">
        <div class="lid" <@ss_common_page_click "productdetail.buySelectInfo()"/>>
            <span class="fr jt">跳转</span>
            <em>查看：</em>颜色 款式 
        </div>
    </div>
    <div class="bz" id="bz_area_id">
        <!--
        <span><b class="zing">&nbsp;</b>正品保障</span>
        <span><b class="by">&nbsp;</b>全场包邮</span>
        <span class="sec"><b class="qi">&nbsp;</b>7天无理由退换货</span>-->
    </div>
    <div class="box">
        <div class='tabnav' id="menu">
            <ul>
                <li <@ss_common_page_click "productdetail.switchItme(0)"/> class="on"><span>图文详情</span></li>
                <li <@ss_common_page_click "productdetail.switchItme(1)"/>><span>规格参数</span></li>
            </ul>
         </div>
         <div id="tabc0" class='tabc'>
           
         </div>
         <div  id="tabc1" class="no">
         </div>
    </div>   
</div>
<div class="bottom"><a id="buy_select_info_id_1" <@ss_common_page_click "productdetail.buySelectInfo()"/> unable="false" style="width: 100%;text-align: center;padding: 1rem 0 1.2rem;">
	<b style="font-weight: normal;color: #F1F10F;" id="countDown_id_1"></b>&nbsp;&nbsp;<b id="buy_text_id_1">立即购买</b></a>
</div>
<div id="mask">&nbsp;</div>
<div class="tabw">
	<a href="javascript:void(0);" class="btn-close">关闭</a>
	<div class="pop-t" id="buy_product_info_id">
		<img src="${ss_assign_resources}img/demoImg/pic106x106.jpg" alt="" class="fl">
		<div class="imgr">
			<p class="pt">小熊煮蛋器双层蒸蛋器自动断电煮…</p>
			<p class="pprice"><b>￥</b>86.00</p>
			<p class="size">请选择：<span>颜色</span><span>款式</span><span class="fred" id="color_area_id">"银色"</span><span class="fred" id="size_area_id">"M"</span></p>
		</div>
	</div>
	<div class="abox">
    	<div class="bbox">
            <div class="pop">				
				<div class="pop-c" id="kinds_product_properties_id">
					
					<div class="sel selnum">
						<div class="tdiv fl">购买数量</div>
						<div class="bnum fr"><form><a  class="btn-minus">减一</a><input type="text" value="1" onkeyup="value=this.value.replace(/[^\d]/g,'')" maxlength="2" id="buy-num"/><a class="btn-add">加一</a></form></div>
						<div id="limitsale_count_id" style="display: block;margin: 0 .8rem 0 0;float: right;color: red;padding-top: .8rem;"></div>
					</div>
				</div>
			</div>
        </div>
	</div>  
	<footer><a id="buy_select_info_id_1" unable="false" class="btn-buy" <@ss_common_page_click "productdetail.submitProductInfo()"/> style="padding: 0 2rem;">
		<b style="font-weight: normal;color: #F1F10F;" id="countDown_id_2"></b>&nbsp;&nbsp;<b id="buy_text_id_2">立即购买</b></a></footer>  
	<div class="botline">&nbsp;</div>
</div>

<div class="fbox ftel" style="display:none;">
	<div class="sc">为节省您的时间，请告知客服</br>此商品的编号<span class="bh" style="color: #e83028;padding: 0 0 0 .5em;"></span></div>
    <div class="btns">
    	<a href="javascript:void(0);" onclick="productdetail.exitCall()">取消</a><a href="tel:400-867-8210" class="ok" onclick="productdetail.exitCall()">确定</a>
    </div>
</div>
<@ss_common_jscall "cfamily.init_request();productdetail.init();"/>
<@ss_common_footer />