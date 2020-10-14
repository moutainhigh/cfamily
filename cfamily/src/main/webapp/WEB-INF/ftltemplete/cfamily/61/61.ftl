<#import "../../macro/macro_baidutongji.ftl" as tongji />
﻿<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta charset="UTF-8">
    <title>家有购物，六一特卖</title>
    <link rel="shortcut icon" type="image/x-icon" href="/cfamily/resources/cfamily/zzz_js/huijiayyou32x32.ico" />
    <link href="../../resources/css/61.css" rel="stylesheet">
    <script src="/cfamily/resources/fileconcat/js-autoconcat.js?v=2.0.0.1" type="text/javascript"></script>
    <script src="/cfamily/resources/cfamily/js/sixOne.js" type="text/javascript"></script>
    <script src="/cfamily/resources/cfamily/js/openProductDetail.js" type="text/javascript"></script>
    <@tongji.m_common_tongji ""/>
</head>
<body>

<script type="text/javascript">
	function messageHandler(msg){
		var data=msg.data;
		if(data.do=='frameSize'){
	    	var iframe = document.getElementById("ifr"); 
	    	iframe.height =  (data.value.height-3)+"px";
	    	
		}
	}
	window.addEventListener('message',messageHandler,false);
</script>

<div class="box">
    <ul>
        <li><img src="../../resources/images/61img/banner1.jpg" /></li>
        <li><img src="../../resources/images/61img/banner2.jpg" /></li>
        <li><img src="../../resources/images/61img/banner3.jpg" /></li>
        <li class="childDayL10">
        
        	 <div class="childCon">
                <div class="childInput">
                    <form action="" method="get">
                        <input type="text" id="mobile" class="cInput" placeholder="请输入手机号领取" style="margin-top:2px;"/>
                         <input id="receive_btn" type="button" value="" class="cBtn" onclick="sixOne.receiveImmediate();_hmt.push(['_trackEvent', 'inputPhone', 'click']);"/>
                    </form>
                    <div style="clear:both;"></div>
                </div>
                <img src="../../resources/images/61img/line.png" style="margin:5px 0px;"/>
                <h3>活动规则：特价商品限时抢购中，输入手机号还送610元 购物红包，数量有限，先到先得，有效期截止到6月1日。</h3>
            </div>
            <img src="../../resources/images/61img/banner4.png" />
        </li>
    </ul>
</div>
<iframe src="61product.html" style="border:none;overflow-x:hidden;vertical-align:top;display:block;"  scrolling="no" width="100%" height="100%" id="ifr"></iframe>

</body>

</html>