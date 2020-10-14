<!doctype html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>亲艾的衣橱</title>
<link rel="stylesheet" href="../../resources/css/wardrobe/app.css">
<script type="text/javascript" src="../../resources/cfamily/zzz_js/zzz.min.js"></script> 
<script type="text/javascript">
	var share_title = "";
	var share_img_url="";
	var share_content = "";
	var share_link = "";
	var locationHref = window.location.href;
	$(document).ready(function() {
		var syurl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiGetProgramInfo?api_key=betafamilyhas";
		 $.ajax({
			  type: "GET",
			  url: syurl,
			  dataType: "json",
			  success: function(data) {
			  	  var o = eval('(' + JSON.stringify(data) + ')');
				  var innHTML = "";
				  var resultCode = o.resultCode;
				  if(resultCode == 1){
				  	//设置分享信息
				  	var shareInfo = o.shareProgram;
				  	share_content = shareInfo.share_content;
				  	if(null == share_content || share_content.length<=0) {
				  		share_content = "看亲爱的衣橱，选同款才对味！";
				  	}
				  	//share_link = shareInfo.share_link;
				  	share_link = locationHref.split('wardrobe')[0]+"cfamilypage/specialshare.html?link="+locationHref+"&alt="+encodeURIComponent("亲艾的衣橱");
				  	share_img_url = shareInfo.share_img_url;
				  	if(null == share_img_url || share_img_url.length <= 0) {
				  		share_img_url = "../../resources/cfamily/zzz_js/logo.jpg";
				  	}
				  	share_title = shareInfo.share_title;
				  	if(null == share_title || share_title.length<=0) {
				  		share_title = "来自惠家有的分享";
				  	}
				  	 var prog = o.program;//最新节目
				  	 var img = "";
				  	 if(prog.vedio_img.length > 0) {
				  	 	img = prog.vedio_img;
				  	 } else {
				  	 	img = "#";
				  	 }
				  	 innHTML += "<div class='tpic'><span  onclick='sendCommand(\""+prog.link+"\",2)'>&nbsp;</span><img  onclick='sendCommand(\""+prog.link+"\",2)' src='"+img+"' alt='' /><div>&nbsp;</div></div>";
				  	 innHTML += "<div class='tabw'>";
				  	 	innHTML += "<div class='tabnavw'>";
					  	 	innHTML += "<div class='tabnav'>";
					   	 		innHTML += "<div class='tab-arrow' id='line'><span>&nbsp;</span></div>";
						  	 	innHTML += "<ul>";
						  	 		innHTML += "<li id='tab0' onclick='tab(0,3)' class='on'>同款推荐</li>";
						  	 		innHTML += "<li id='tab1' onclick='tab(1,3)'>所有商品</li>";
						  	 		innHTML += "<li id='tab2' onclick='tab(2,3)'>往期精彩</li>";
						  	 	innHTML += "</ul>";
				  	 		innHTML += "</div>";
				  	 	innHTML += "</div>";
				  	 	innHTML += "<div id='tabid0' class='listsub'>";
				  	 		innHTML += "<div class='xq'>";
				  	 			//innHTML += "<div id='content_des' ><p style='display:-webkit-box!important;-webkit-line-clamp: 2;-webkit-box-orient: vertical;overflow: hidden!important;'>"+o.categoryDes+"</p></div>";
				  	 			innHTML += "<div id='content'>";
				  	 				innHTML += "<div><p>"+o.categoryDes+"</p></div>";
				  	 			innHTML += "</div>";
				  	 			innHTML += "<div id='toggle' class='btn gdown'><a href='javascript:void(0);'>更多详情</a></div>";
				  	 		innHTML += "</div>";
				  	 		var recommend = o.newrecommend;
				  	 		innHTML += "<div class='pro1 pro2'>";
					  	 		for(var i=0;i<recommend.length;i++) {
					  	 			var remd = recommend[i];
					  	 			var isShowNew = "";
					  	 			if(remd.status == "1001") {
					  	 				isShowNew = "<b>&nbsp;</b>";
					  	 			}
					  	 			var t_float = (i%2 ==0) ?"fl":"fr";
					  	 			innHTML += "<div class='imgd "+t_float+"' onclick='sendCommand(\"goods_num:"+remd.product_code+"\",1)'><div><span>"+remd.product_name+"</span></div>"+isShowNew+"<img src='"+remd.product_img+"' alt='' /></div>";
					  	 		}
				  	 		innHTML += "</div>";
				  	 	innHTML += "</div>";
				  	 	innHTML += "<div id='tabid1' class='listsub' style='display:none;'>";
				  	 		innHTML += "<div class='pro1'>";
				  	 			var productList = o.allProducts;
				  	 			for(var i=0;i<productList.length;i++) {
				  	 				var product = productList[i];
				  	 				var isShowNew = "";
					  	 			if(product.status == "1001") {
					  	 				isShowNew = "<b>&nbsp;</b>";
					  	 			}
					  	 			var t_float = (i%2 ==0) ?"fl":"fr";
					  	 			innHTML += "<div class='imgd "+t_float+"' onclick='sendCommand(\"goods_num:"+product.product_code+"\",1)'><div><span>"+product.product_name+"</span></div>"+isShowNew+"<img src='"+product.product_img+"' alt='' /></div>";
				  	 			}
				  	 		innHTML += "</div>";
				  	 	innHTML += "</div>";
				  	 	innHTML += "<div id='tabid2' class='listsub' style='display:none;'>";
				  	 		innHTML += "<div class='sps'>";
				  	 			var oldProList = o.oldPrograms;
				  	 			for(var i=0;i<oldProList.length;i++) {
				  	 				var oldPro = oldProList[i];
					  	 			innHTML += "<div class='spli' onclick='sendCommand(\""+oldPro.link+"\")',2>";
					  	 				innHTML += '<div class="imgd"><span onclick="sendCommand(\''+oldPro.link+'\',2)">&nbsp;</span><b>'+oldPro.on_time+'</b><img src="'+oldPro.vedio_img+'" alt="" /></div>';
				  	 					innHTML += '<div class="txtd">';
				  	 						innHTML += "<h3>"+oldPro.title+"</h3>";
				  	 					innHTML += "</div>";
				  	 					innHTML += "<p>"+oldPro.play_time+" "+oldPro.count+"</p>";
				  	 				innHTML += "</div>";
				  	 			} 
				  	 		innHTML += "</div>";
				  	 	if(o.isMore == '1001') {
				  	 		innHTML += '<a href="javascript:void(0);" style="font-size: 1.3rem;" onclick="sendCommand(\'vedioList.html\',2)"  class="more-video">更多视频<b>&gt;</b></a>';
				  	 	}
				  	 	innHTML += "</div>";
				  	 innHTML += "</div>";
				  }
				  
				  $(".wrap").html(innHTML);
		   		 //收起与展开
				 var bol = 1;
			     $("#toggle").click(function() {
					if(bol == 1){
						$('#content').animate({height:$('#content div').height()},"fast");
						bol =0;
						$(this).removeClass('gdown').addClass('gup');
					}else{
						$('#content').animate({height:"3.2rem"},"fast");
						bol = 1;
						$(this).removeClass('gup').addClass('gdown');
					};
					/*if($("#content").is(":hidden") ? "1" : "0" === 1){
						 $(this).removeClass('gdown').addClass('gup')
						}else{
						 $(this).removeClass('gup').addClass('gdown')
					};
			        $("#content").slideToggle();*/
					
			     });
			     setTimeout(function(){
				   	 parent.pushIframeHeight(document.body.scrollHeight);
			     },2000);
			     <#-- 取消页面分享功能
			   	 callbackFunc('android');
			   	 -->
			  }
		})
	})
	function sendCommand(param,flg){
		var url = param;
		if(flg == 1) {
			url ="../cfamilypage/"+param;
		}
		document.location = url;
		//window.open(url);
	}
	<#-- 页面分享取消
	function shareOnIOS(){
	    return callbackFunc("ios");
	};
	function callbackFunc(param){
		if(param == "android"){
			window.share.shareOnAndroid(share_title,share_img_url,share_content,share_link);
		}else{
			return share_title+"'/"+share_img_url+"'/"+share_content+"'/"+share_link;
		}
	}
	-->
</script>
</head>
<body>
<#-- <div class="do-way"><div>&nbsp;</div><a href="#" class="back fl">返回</a><a href="#" class="share fr">分享</a><span>&nbsp;</span></div> -->
<div class="wrap">
	
</div>
<script type="text/javascript">
$(function() {
	
	//顶部菜单
	var tfix = $('.do-way div'),
	 ttxt = $('.do-way span'),
	 fth = $('.tabw').offset().top
    $(window).scroll(function () {
		//var num = parseInt(7/2); 
		var scroh = $(document).scrollTop();   
		if(scroh>0){
			tfix.addClass('dis');
			ttxt.addClass('dis');
			$('.tpic').addClass('dis');
			}else{
			tfix.removeClass('dis')
			ttxt.removeClass('dis');
			$('.tpic').removeClass('dis');
		};
		
		
		/*//固定菜单
		if(scroh>=($('.tpic').height()-$('.tabsw').height())){
			$('.tabsw').css('height','10rem')
			$('.tabs').addClass('tfix');
		}else{
			$('.tabsw').css('height',' 4.6rem')
			$('.tabs').removeClass('tfix');
		};*/
		
	});
});
/*切换菜单*/
		function tab(n,m){
			var tabnav="tab"+n;
			var tabid="tabid"+n;
			cleardisplay(m);
			document.getElementById(tabid).style.display="block";
			document.getElementById(tabnav).className="on";
			document.getElementById('line').style.left=33.33333*n+'%';
		};
		function cleardisplay(m){
			for (i=0;i<m;i++)
			{
			var ctabid="tabid"+i;
			var ctabnav="tab"+i;
			document.getElementById(ctabid).style.display="none";
			document.getElementById(ctabnav).className="";
			}
		};
		
</script>
</body>
</html>