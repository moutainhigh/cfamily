<!DOCTYPE html>
<html lang="en">
<head> 
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="UTF-8">
<title></title>
<#assign weiXinUrl=b_method.upConfig("cfamily.hjyWeiXinUrl") >
<link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
<link  href="../../resources/css/ppth_todaysales_new_one/style.css" rel='stylesheet'>	
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);})();
</script>
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>

<script type="text/javascript" >
	var isSpecial =  up_urlparam("isSpecial");
	var pmobile = up_urlparam("pm","");
	var htp = "${weiXinUrl}";
	$(document).ready(function() {
		flg = up_urlparam("flg");
		if(null != pmobile && pmobile != '' && pmobile.length == 11) {
    		var pm = pmobile.substring(0, 3) + "****" + pmobile.substring(pmobile.length - 4);
    		$("#adPm").html("您的好友"+pm+"送您<em>1 次返现特权</em>，购买任一商品都可以使用哦~");
    	}
    	
		infoCode = up_urlparam("infoCode");
		var t_content = decodeURIComponent(up_urlparam("share_content"));
		if("" != t_content && null != t_content) {
			t_content = decodeURIComponent(up_urlparam("title"));
		}
		share_content = t_content;
		var isShare = up_urlparam("isShare");
		share_img_url = up_urlparam("share_img_url");
		share_title = decodeURIComponent(up_urlparam("share_title"));
		$("title").html(decodeURIComponent(up_urlparam("title")));
		var lc = window.location.href;
		share_link = lc.split("?")[0]+"?flg=1001&infoCode="+infoCode+"&title="+up_urlparam("title");
		var syurl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForBrandPreferenceContent?api_key=betafamilyhas&picWidth=1080&&infoCode="+infoCode+"&buyerType="+up_urlparam("buyerType");
		$.ajax({
			type: "GET",
			url: syurl,
			dataType: "json",
			success: function(data) {
				//alert(JSON.stringify(data));
				var innHTML = '';
				if(data.resultCode == 1){
				
					var brandList = data.brandPicList;
					var headHTML = '';
					var footHTML = '';
					for(var i=0;i<brandList.length;i++) {
						if(brandList[i].brandLocation == '1') {
							headHTML += '<div class="pinp-ad" onclick="forward_url(\''+brandList[i].linkType+'\',\''+brandList[i].linkValue+'\')"><img class="delay" src="../../resources/cfamily/zzz_js/bg.png" data-original="'+brandList[i].brandPic+'" alt=""/></div>';
						} else if(brandList[i].brandLocation == '2') {
							footHTML += '<div class="pinp-ad" onclick="forward_url(\''+brandList[i].linkType+'\',\''+brandList[i].linkValue+'\')"><img class="delay" src="../../resources/cfamily/zzz_js/bg.png" data-original="'+brandList[i].brandPic+'" alt=""/></div>';
						}
					}
					innHTML += headHTML;
					if(data.discount != null && data.discount != "" && eval(data.discount) > 0) {
						innHTML += '<h1><b>&nbsp;</b>全场<span>'+data.discount+'</span>折起</h1>';
					}
					innHTML += '<div class="lidw">';
						var pcList = data.productList;
						for(var i= 0;i<pcList.length;i++) {
						
							/* 没换行清除浮动 */
							if(i%2 == 0 && i >0) {
								innHTML += "<div style='clear:both;'></div>";
							}
							var pc = pcList[i];
							if(i%2 == 0) {
							innHTML += '<div class="lid fl" onclick="sendCommand(\'goods_num:'+pc.procuctCode+'\',1)">';
							} else {
							innHTML += '<div class="lid fr" onclick="sendCommand(\'goods_num:'+pc.procuctCode+'\',1)">';
							}
								innHTML += '<div class="imgd">';
									if(pc.storeFlag == "0") {
										innHTML += '<span>&nbsp;</span>';
									}
									innHTML += '<img class="delay" src="../../resources/images/ppth_todaysales_new_one/bg_defalt_list.png" data-original="'+pc.pic+'" alt="" />';
								innHTML += '</div>';
								innHTML += '<div class="txtd">';
									innHTML += '<h3>'+pc.productName+'</h3>';
									if(pc.discount != "" || eval(pc.discount) > 0) {
										innHTML += '<div class="icod"><span>'+pc.discount+'折</span></div>';
									}
									innHTML += '<div class="price"><b style="font-size: 1.2rem;font-weight: normal;">¥</b>'+pc.salePrice+'<span>¥'+pc.marketPrice+'</span></div>';
								innHTML += '</div>';
							innHTML += '</div>';
						}
					innHTML += '</div>';
					innHTML += footHTML;
					//展示分享的头信息
					if(flg == "1001") {
						$("#ichsy_share_info").show();
					}
					$('.wrap').html(innHTML);
					$("img.delay").lazyload({threshold:100,placeholder:""});
					//是否展示分享按钮
					if(isShare == true || isShare == "true") {
						if(self.frameElement.tagName=="IFRAME"){//嵌套的页面不进行分享
							return false;
						}
						callbackFunc("android",share_title,share_img_url,share_content,share_link,isShare);
					}
				}
			}
		})
	})
	function up_urlparam(sKey, sUrl) {
		var sReturn = "";
	
		if (!sUrl) {
			sUrl = window.location.href;
			if (sUrl.indexOf('?') < 1) {
				sUrl = sUrl + "?";
			}
		}
	
		var sParams = sUrl.split('?')[1].split('&');
	
		for (var i = 0, j = sParams.length; i < j; i++) {
	
			var sKv = sParams[i].split("=");
			if (sKv[0] == sKey) {
				sReturn = sKv[1];
				break;
			}
		}
	
		return sReturn;
	
	}
	//加密，解密url的&，？符号
//true为加密，false为解密
function url_param_mix(s_url,isMix){
	if(!s_url){
		return "";
	}
	var param = s_url;
	if(isMix){
		param = param.replace(/[\?]/g, "1M1");
		param = param.replace(/[\&]/g, "1M2");
		param = param.replace(/[\=]/g, "1M3");
	}else{
		param = param.replace(/(1M1)/g, "?");
		param = param.replace(/(1M2)/g, "&");
		param = param.replace(/(1M3)/g, "=");
	}
	return param;
}
	function callbackFunc(param,share_title,share_img_url,share_content,share_link,isShare) {
		window.share.shareOnAndroid(share_title,share_img_url,share_content,share_link,isShare);
	}
	function sendCommand(param,flag){
		_hmt.push(['_ppthDetailClick', '\/PageClickListen/'+param]);
		if(isSpecial && isSpecial.length > 0 && isSpecial == "true"){
			
			document.location = htp+param.split(":")[1]+"&backUrl="+url_param_mix(window.location.href,true);
		}else{
			if(flag == 1) {
				var tcode = param.split(":")[1];
				param = htp + tcode + '&fromshare=1&goods_num:'+tcode;
			}
			var url=param;
		 	document.location = url;
		}
  }
  function forward_url(linkType,linkValue){
  	var param = "";
  	if(linkType == "4497471600020004") {
  		param = htp+linkValue+"&fromshare=1&goods_num:"+linkValue;
  	} else if(linkType == "4497471600020002") {//关键词
  		param = "kwd_name:"+linkValue;
  	} else if(linkType == "4497471600020003") {//分类搜索
  		param = "cls_name:"+linkValue;
  	} else if( linkType == "4497471600020001") {//url
  		if(CheckUrl(linkValue)) {
  			param = linkValue;
  		} else {
  			return false;
  		}
  	}
	sendCommand(param,2);
  }
  function CheckUrl(str_url){
    var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
      "?(([0-9a-z_!~*'().= $%-] : )?[0-9a-z_!~*'().= $%-] @)?" //ftp的user@
      "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
      "|" // 允许IP和DOMAIN（域名）
      "([0-9a-z_!~*'()-] \.)*" // 域名- www.
      "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
      "[a-z]{2,6})" // first level domain- .com or .museum
      "(:[0-9]{1,4})?" // 端口- :80
      "((/?)|" // a slash isn't required if there is no file name
      "(/[0-9a-z_!~*'().;?:@= $,%#-] ) /?)$";
    var re=new RegExp(strRegex);
    //re.test()
    if (re.test(str_url)){
        return (true);
    }else{
        return (false);
    }
}
</script>

</head>	
<body>
<#include "../../web/shareHead.ftl" />
<div class="wrap pinp-th">
	
</div>	
</body>
</html>