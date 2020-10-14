<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>生活家</title>
<style type="text/css">
	/* 禁用iPhone中Safari的字号自动调整 */
	html {-webkit-text-size-adjust: none;}
	body,p,h1,h2,h3,h4,h5,h6,ul,ol,li,dl,dt,dd,table,th,td,form,fieldset,legend,input,textarea,button,select{margin:0;padding:0}
	body,input,textarea,select,button,table{font-size:16px;line-height:1.25em}
	body{min-width:320px;font-size:1em; font-family:"\5b8b\4f53",Arial,sans-serif; color:#000;-webkit-text-size-adjust:none}
	img,fieldset{border:0}
	ul,ol{list-style:none}
	em,address{font-style:normal}
	a{color:#000;text-decoration:none}
	table{border-collapse:collapse}
	/* 来自yahoo, 让标题都自定义, 适应多个系统应用 */
	h1,h2,h3,h4,h5,h6 {font-size:100%;font-weight:500;}
	/* 设置HTML5元素为块 */
	article, aside,details,figcaption, figure,footer,header,hgroup,menu,nav,section {display: block;}
	.video embed, .video object, .video iframe {width: 100%; height:auto;}
	/* 设置图片视频等自适应调整 */
	img {display:block; max-width:100%; height: auto; width:auto\9; /* ie8 */}
	
	/* page */
	body{ background:#f2f2f2;}
	.wrap{margin:0 auto; text-align:center;}
	.wrap,.xq-img,{width:100%; overflow:hidden;}
	.xq-pro{margin:0;width:100%;float:left;}
	.xq-pro1{margin:0 auto 0;width:72%;min-height:100px;}
	.xq-img a,.xq-pro a{ display:block; overflow:hidden; }
	.xq-pro img,.xq-img img,.xq-bj img,.xq-pro1 img{width:100%; }
	.xq-bj{width:100%;}
</style>

<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>

</head>
<body>
	<!--<div class="xq-pro">
        <a href="#"><img  class="lazy" src="zzz_js/bg.png" data-original="images/shj_banner.jpg" alt="完美甄选，新品抢先"  /></a>
    </div>
	<div class="wrap">


		<div class="xq-pro1">
				<a href="http://share.ichsy.com/index/tnbbkbf.html"><img  class="lazy" src="zzz_js/bg.png" data-original="images/shj_tnbbkbf.jpg" alt="糖尿病不可不防"/></a>
		</div>


		<div class="xq-pro1">
				<a href="http://share.ichsy.com/index/saibandao.html"><img  class="lazy" src="zzz_js/bg.png" data-original="images/shj_saibandao.jpg" alt="非侣勿行将错就错"/></a>
		</div>

	</div>-->
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<script type="text/javascript">
	var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiForLiveHome?api_key=betafamilyhas&activity=467703130008000100050001";
	(function() {
		  $.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 					
		 					var divStr = "";
		 						if(data.banner_img.length > 0){
		 							if(data.banner_link.length >0){
		 								var reg = /goods_num:/; 
										var r = data.banner_link.match(reg);
		 								if(null != r) {
		 									divStr += "<div class=\"xq-pro\"><a href=\"#\" onclick=\"sendCommand('"+data.banner_link+"')\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
		 								} else {
			 								divStr += "<div class=\"xq-pro\"><a href=\""+data.banner_link+"\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
		 								}
		 							} else {
		 								divStr += "<div class=\"xq-pro\"><a href=\"#\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div><div class=\"wrap\">";
		 							}
		 						}
		 						var items =data.items;
		 						for(var j=0;j<items.length;j++){
		 						  	var img_url = items[j].img_url;
									var link = items[j].link;
		 						  	var item_name = items[j].item_name;
									 divStr += '<div class="xq-pro1"><a onclick="redirectHandle(this,\''+items[j].share_info.share_img_url+'\',\''+items[j].share_info.share_title+'\',\''+items[j].share_info.share_content+'\')" link="'+link+'">'
										+'<img  class="delay" src="../../resources/cfamily/zzz_js/shj_bg.png" data-original="'+img_url+'" alt="'+item_name+'"/></a></div>';
		 						}
								divStr +="</div>";
		 						
		 					
		 					$("body").html(divStr);
		 				}else{
		 					 alert("获取数据出错，请刷新重试!");
		 					}

						$('.wrap').show();
						$("img.delay").lazyload({threshold:100,placeholder:""});
		 		}
		 	});
	})();
	
	function redirectHandle(obj,share_img_url,share_title,share_content){
		var link = $(obj).attr("link");
		var imgUrl = $(obj).find("img").attr("src");
		var alt = $(obj).find("img").attr("alt");
		var host = window.location.host;
		var hrefUrl = "http://share-"+host+"/cfamily/web/cfamilypage/"
		location.href = hrefUrl+"specialshare?flg=1001&share_img_url="+share_img_url+"&share_title="+encodeURIComponent(share_title)+"&share_content="+encodeURIComponent(share_content)+"&imgUrl="+imgUrl+"&link="+link+"&alt="+encodeURIComponent(alt);
	}
	
	function sendCommand(param){
		var url=param;
		document.location = url;
	}
</script>
</body>
</html>