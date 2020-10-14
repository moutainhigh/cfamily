<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>都在买</title>
<style type="text/css">
	html {-webkit-text-size-adjust: none;}body,p,h1,h2,h3,h4,h5,h6,ul,ol,li,dl,dt,dd,table,th,td,form,fieldset,legend,input,textarea,button,select{margin:0;padding:0}body,input,textarea,select,button,table{font-size:.75em;line-height:1.25em}
	body{background:#fff; min-width:320px; font-family:'microsoft yahei',"\5b8b\4f53",Arial,sans-serif; color:#333;}
    img,fieldset{border:0}ul,ol{list-style:none}em,address{font-style:normal}a{color:#000;text-decoration:none}table{border-collapse:collapse}h1,h2,h3,h4,h5,h6 {font-size:100%;font-weight:500;}
    img {display:block; max-width:100%; height: auto;  }
	
	/* page */ 
     header{ background:url(images/headbg.png) no-repeat center top; background-size:100% 100%; width:100%; height:2.2em; color:#fff; text-align: center; font-size: 1.8em; line-height: 2.2em  }
    .wrap{ width:100%; padding:.5em 0 0em; overflow: hidden;}
    .phli{ position:relative;width:100%; padding:.6em 0 .6em 2.125%;border-bottom:1px solid #e0e0df;box-sizing:border-box; -webkit-box-sizing:border-box;overflow: hidden;}
    .phli .imgd{position:relative;float:left;margin:0 1em 0 0; width:9.4em; height: 9.4em; overflow: hidden;}
    .phli .imgd img{display:block; width:100%;}
    .phli .imgd span{display:block; position: absolute; top:50%; left:50%; width:6em; height:6em; margin:-3em 0 0 -3em; z-index: 9;background:url(../../resources/cfamily/zzz_js/monthSalesTop/ico-qgl.png) no-repeat 0 0; background-size:100% 100%;}
    .phli .txtd{float:left; width:50%;padding:0 2.6em 0 0;height: 9.4em;overflow:hidden;}
    .phli .txtd h3{ font-size: 1.3em; height:2.4em;line-height: 1.2em; margin:.1em 0 .4em; overflow: hidden;}
    .phli .price{font-size: 1.7em; font-weight:bold;color :#dc0f50;padding:.2em 0 .1em; overflow:hidden;}
    .phli .price b{font-size: .6em; font-weight: normal}
    .phli .price span{color:#999; font-size: .6em; font-weight: normal; padding:0 0.8em; text-decoration: line-through;}
    .phli .icod{padding:0 0 .9em; height:1.5em;overflow: hidden;}
    .phli .icod span{color:#fff; background: #fea829; border-radius: 2px; -webkit-border-radius: 2px; height:1.6em;line-height:1.6em; margin:0 .5em 0 0;font-size: 1em; display: inline-block; padding:0 .4em;}
    .phli p{color:#999;}
    .phli .inum{ position:absolute; right:0; top:50%; margin-top:-1.7em; display:block;width:1.8em; height: 3.6em; line-height: 3.6em;color:#fff; font-size: 1.4em; text-indent: .65em; background:url(../../resources/cfamily/zzz_js/monthSalesTop/icos.png) no-repeat 0 -258px; background-size:100% auto; }
    .phli .num1{background:url(../../resources/cfamily/zzz_js/monthSalesTop/icos.png) no-repeat 0 0; background-size:100% auto;}
    .phli .num2{background:url(../../resources/cfamily/zzz_js/monthSalesTop/icos.png) no-repeat 0 -85px; background-size:100% auto;}
    .phli .num3{background:url(../../resources/cfamily/zzz_js/monthSalesTop/icos.png) no-repeat 0 -172px; background-size:100% auto;}
</style>
</head>
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<script language="javascript" type="text/javascript"> 
	//获取参数
	function up_urlparam(key,sUrl) {
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
			if (sKv[0] == key) {
				sReturn = sKv[1];
				break;
			}
		}
		return sReturn;
	}
	var param = '&baseValue=base64&categoryOrBrand=top50&keyWord=dG9wNTA=&pageNo=1&pageSize=50&screenWidth=1&sortFlag=2&sortType=1&buyerType='+up_urlparam('buyerType','');
	var syurl="/cfamily/jsonapi/com_cmall_productcenter_service_api_ApiSearchResults?api_key=betafamilyhas"+param;
	$(function(){
		$.ajax({
	 		type:"GET",
	 		url:syurl,
	 		dataType:"json",
	 		success:function(data){
	 			if(data.resultCode == 1) {
	 				var items = data.item;
	 				var content = "";
		 			for(var j=0;j<items.length;j++){
		 				var itm = items[j];
		 				var sortNum = eval(j+1);
						if((j+1) < 10) {
		 					sortNum = '&nbsp;'+sortNum;
		 				}
		 				content += '<div class="phli" onclick="sendCommand(\''+itm.productCode+'\')"><span class="inum num'+eval(j+1)+'">'+sortNum+'</span><div class="imgd">';
		 				if(itm.stockNum == '售罄') {
		 					content += '<span>&nbsp;</span>';
		 				}
		 				content += '<img class="delay" src="../../resources/cfamily/zzz_js/monthSales_bg.png" data-original="'+itm.imgUrl+'" alt="" /></div><div class="txtd"><h3>'+itm.productName+'</h3><div class="icod">';
		 				var activitylst = itm.activityList;
		 				if(null != activitylst && activitylst != 'null') { 
			 				for(var k=0;k<activitylst.length;k++){
			 					content += '<span>'+activitylst[k]+'</span>';
			 				}
		 				}
		 				//content += '</div><div class="price"><b>¥</b>'+itm.currentPrice+'<span>¥'+itm.originalPrice+'</span></div><p>月销'+itm.productNumber+'件</p></div></div>';
		 				content += '</div><div class="price"><b>¥</b>'+itm.currentPrice+'</div><p>月销'+itm.productNumber+'件</p></div></div>';
		 			}
		 			
		 			$('.wrap').html(content);
		 			$("img.delay").lazyload({threshold:100,placeholder:""});
	 			}
	 			
	 		}
	 	});
		
	})
	function sendCommand(param){
		<#assign weiXinUrl=b_method.upConfig("cfamily.hjyWeiXinUrl") >
		var url='${weiXinUrl}'+param+"&goods_num:"+param;;
		document.location = url;
	}
</script>
<body>
<div class="wrap">
    
</div>
</div>
</body>
</html>
