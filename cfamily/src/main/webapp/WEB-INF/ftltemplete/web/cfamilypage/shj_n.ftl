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
<link href="/Public/css/style.css" rel="stylesheet" type="text/css" />
<link href="/Public/css/base.css" rel="stylesheet" type="text/css" />
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
<script type="text/javascript" src="/Public/js/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#top_nav").click(function(){
			$(".nav_top").toggle();
		});
	});
		
	var syurl="/cfamily/jsonpajax/com_cmall_familyhas_api_ApiForLiveHome?api_key=betafamilyhas&activity=467703130008000100050001";
	 $.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType: "jsonp",
				jsonp: "api_callback",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 					
		 					var divStr = "";
		 					
		 						divStr += "<div class=\"xq-pro\"><a href=\"#\"><img  class=\"delay\" src=\"../../resources/cfamily/zzz_js/bg.png\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div>"
								  +"<div class=\"wrap\">";
		 						var items =data.items;
		 						for(var j=0;j<items.length;j++){
		 						  	var img_url = items[j].img_url;
									var link = items[j].link;
		 						  	var item_name = items[j].item_name;
									 divStr += "<div class=\"xq-pro1\"><a href=\""+link+"\">"
										+"<img  class=\"delay\" src=\"../../resources/cfamily/zzz_js/bg.png\" data-original=\""+img_url+"\" alt=\""+item_name+"\"/></a></div>";
		 						}
								divStr +="</div>";
		 						
		 					
		 					$(".wrap").html(divStr);
		 				}else{
		 					 alert("获取数据出错，请刷新重试!");
		 					}

						$('.wrap').show();
						$("img.delay").lazyload({threshold:100,placeholder:""});
		 		}
		 	});	
</script>
</head>
<body>
	<div class="top">
	<div class="top_nav1 fl">
		<a onclick="self.location=document.referrer;" style="padding-right:5px;margin-right:5px;" href="javascript::"><img alt="" src="/Public/images/login/cslogo_03.png"></a>
	</div>
	<div class="main_tit1 ft34">惠家有商城</div>
		<span  class="refresh"  id="top_nav"><img src="/Public/images/login/nab.png"  alt=""/></span> 
	</div>
	<div class="nav_top">
		<ul>
			<li>
				<a href="/">
					<p><img src="/Public/images/login/wode5.png" width="30" alt=""/></p>
					<p class="nav_title">首页</p>
				</a>
			</li>
			<li>
				<a href="/Home/Nav/navpage">
					<p><img src="/Public/images/login/wode4.jpg" width="30" alt=""/></p>
					<p class="nav_title">分类导航</p>
				</a>
			</li>
			<li>
				<a href="/Home/Cart/cart">
					<p><img src="/Public/images/login/wode1.jpg" width="30" alt=""/></p>
					<p class="nav_title">购物车</p>
				</a>
			</li>
			<li>
				<a href="/Home/user/PersonalCenter">
					<p><img src="/Public/images/login/wode.jpg" width="30" alt=""/></p>
					<p class="nav_title">我的</p>
				</a>
			</li>
		</ul>
	</div>
	<div class="wrap">
	
	
	</div>
</div>
<footer>
 <div class="foot mt20">
     <div class="fot_nav">
         <div class="foot_cen">
            
             <span class="fl">
               
               <a href="/home/user/login">登录</a>
               
               <a class="ml10" href="/home/user/register">注册</a>
			
            </span>           
            <span class="fr"><a target="_self" href="javascript:window.scrollTo(0,0)">返回顶部</a></span>
         
       </div>
     </div>
     <div class="lxfs" style="height:200px;">
         <div class="lxfs_cen p10">
             <p>客服电话：4008-678-999</p>
             <p>Copyright © 2008-2013 m.jyh.com </p>
             <p>家有购物集团  版权所有</p>
         </div>
     </div>
 </div>
</footer>
</body>
</html>