<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>惠家有-今日直播</title>
<style type="text/css">
*{margin:0;padding:0;}
a{ color:#666; text-decoration:none}
body{ font-size:14px;  font-family: 'microsoft yahei',Verdana,Arial,Helvetica,sans-serif; background-color:#f2f2f2; color:#666666}
img{border:none; float:left;}
.ad{ height:auto; overflow:hidden;}
.xq-pro{margin:0;width:100%;float:left;margin-top:20px;}
.xq-pro a{ display:block; overflow:hidden; }
.xq-pro img{width:100%;}
.live_list{ width:100%; height:auto; overflow:hidden}
.ad img{border-top:1px solid #CCC;border-bottom:1px solid #CCC;}
.live_list_1{ margin-left:5%; border-left:3px #e7e7e7 solid; float:left; width:95%; position:relative;}
.hot_live{ width:100px; line-height:30px; height:30px; background-color:#DC0F50; color:#FFF; display:block; float:left; text-align:center; border-radius:8px; margin:10px 0;margin-left:15px; }
.live_time{ line-height:50px; float:left;}
.time{ float:left;}
.time_i{ margin-left:-12px; margin-top:15px; display:block; width:20px; height:20px; float:left; position:absolute; left:0;}
.live{ height:auto; overflow:hidden;float:left; padding-left:5%;width:95%;}
.live_1{ width:100%; height:100px; height:100px; overflow:hidden; background-color:#FFF; border:1px solid #CCC; border-right:none;-moz-box-shadow:5px 5px 5px 2px #888888; /* 老的 Firefox */box-shadow:5px 5px 5px 2px #888888;}
.sp_img{ float:left;width:100px; height:100px;position:relative;}
.sp_js{float:left; width:57%; padding-left:5%; margin-top:5px; }
.sp_js h1{ font-size:16px;font-family:"微软雅黑";}
.zhekou{ width:30px; height:33px; background:url(http://app116.ichsy.com:8181/index/images/hjy_09.png) no-repeat; position:absolute; right:-1px; top:-1px; line-height:12px; text-align:center; }
</style>
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<script type="text/javascript">
		var syurl="../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForGetTVData?api_key=betafamilyhas&vipNo=123243";
		//直播:正在直播、折扣、商品详情连接、播出结束时间
		$(document).ready(function() {
			  $.ajax({
				  type: "GET",
				  url: syurl,
				  dataType: "json",
				  success: function(data) {
					  var o = eval('(' + JSON.stringify(data) + ')');
					  var innHTML = "";
					  var ztgood = "";
					  var resultCode = o.resultCode;
					  if(resultCode == 1){
					  	
					  
					  	 for (i = o.products.length -1; i >= 0; i--) {
							  ztgood = o.products[i];
							  var tempHtml = "";
							  tempHtml += '<div class=\"wrap\"><div class="live_list_1">';
							  tempHtml += '<span class="time_i">';
							  if (ztgood.playStatus == '1') {
								  tempHtml += '<img id="onlive" src="../../resources/cfamily/zzz_js/hjy_03.png" width="20"  alt=""/>'
							  } else {
								  tempHtml += '<img src="../../resources/cfamily/zzz_js/hjy_06.png" width="20"  alt=""/>'
							  }
							  tempHtml += '</span>';
							  tempHtml += '<div class="live">';
							  tempHtml += '<p class="live_time">';
							  tempHtml += '<span class="time">播出时间：' + ztgood.playTime.substr(11, 5) + '-' + ztgood.endTime.substr(11, 5) + '</span>';
							  if (ztgood.playStatus == '1') {
								  tempHtml += '<span class="hot_live">正在热播</span>'
							  }
							  tempHtml += '</p>';
							  tempHtml += '<div class="live_1">';
							  tempHtml += '<a href="" onClick="sendCommand(\'goods_num:'+ztgood.id+'\');return false;">';
							  tempHtml += '<div class="sp_img">';
							  if (ztgood.img_url == '') {
								  tempHtml += '<img src="../../resources/cfamily/zzz_js/moren.jpg" width="100" alt=""/>'
							  } else {
								  tempHtml += '<img src="' + ztgood.productPic + '" width="100" alt=""/>'
							  }
							  tempHtml += '</div>';
							  tempHtml += '<div class="sp_js">';
							  tempHtml += '<h1>' + ztgood.name + '</h1>';
							  tempHtml += '<p style="margin-top:5px; color:#DC0F50">现价:￥<strong style="font-size:18px;">' + ztgood.salePrice + '</strong></p>';
							  tempHtml += '<p style="text-decoration:line-through; color:#999">原价：￥' + ztgood.markPrice + '</p>';
							  tempHtml += '</div></a></div></div></div>';
							  innHTML += tempHtml
						  }
						  if(data.banner_img.length > 0){
 							if(data.banner_link.length >0){
 								var reg = /goods_num:/; 
								var r = data.banner_link.match(reg);
 								if(null != r) {
 									innHTML += "<div class=\"xq-pro\"><a href=\""+data.banner_link+"\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div>";
 								} else {
	 								innHTML += "<div class=\"xq-pro\"><a href=\""+data.banner_link+"\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div>";
 								}
 							} else {
 								innHTML += "<div class=\"xq-pro\"><a href=\"#\"><img  class=\"delay\" src=\""+data.banner_img+"\" data-original="+data.banner_img+" alt=\""+data.banner_name+"\"/></a></div>";
 							}
 						 }
 						 
					  }else{
					  	 alert("获取数据出错，请刷新重试!");
					  }
					  $("#live_list").html(innHTML);
					  myOnloadCategory()
				  }
			  })
			  
		  })
		
        <#--  定位正在直播的商品 -->
		function myOnload() {
			  setTimeout("$('#onlive').val($('#onlive').offset().top)", 200);
			  setTimeout("scrollOnlive();", 201)
		  }
		function scrollOnlive() {
	           $(window).scrollTop($('#onlive').val() - 15)
          }
         <#--  定位正在直播的商品end -->
         
         <#--  定位栏目图片 -->
        function myOnloadCategory() {
			  setTimeout("$('.xq-pro').val($('.xq-pro').offset().top)", 200);
			  setTimeout("scrollCategory();", 201)
		 }
		function scrollCategory() {
			   $('.xq-pro').css('top','250px');
	           $(window).scrollTop($('.xq-pro').val())
          }
		<#--  定位栏目图片end -->
				
		function sendCommand(param){
			var url=param;
			document.location = url;
		}
	</script>

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


<body onload="myOnloadCategory()">
<div class="today_live">
    <div class="live_list" id="live_list">
         
        <!-- <div class="live_list_1">
            <span class="time_i"><img src="images/hjy_03.png" width="25"  alt=""/></span>
            <div class="live">
                 <p class="live_time"><span class="time">播出时间：8：00-9：00</span>  <span class="hot_live">正在热播</span> </p>
                 <div class="live_1">
                     <div class="sp_img">
                         <img src="images/hjy1.jpg" width="100" alt=""/>
                         <span class="zhekou"> 
                            <p>50</p>
                            <p style="font-size:10px;">%</p>
                         </span>
                      </div>
                     <div class="sp_js">
                          <h1>韩国进口惠人原汁机挤干每一滴果蔬</h1>
                          <p style="margin-top:10px; color:#dc0f50">现价：￥<strong style="font-size:20px;">1999</strong></p>
                          <p style="text-decoration:line-through; color:#999">原价：￥ 2999</p>
                     </div>
                </div>
            </div>
         </div> 
         
         <div class="live_list_1">
            <span class="time_i"><img src="images/hjy_06.png" width="25"  alt=""/></span>
            <div class="live">
                 <p class="live_time"><span class="time">播出时间：8：00-9：00</span> </p>
                 <div class="live_1">
                     <div class="sp_img">
                         <img src="images/hjy1.jpg" width="100" alt=""/>
                         <span class="zhekou"> 
                            <p>50</p>
                            <p style="font-size:10px;">%</p>
                         </span>
                      </div>
                     <div class="sp_js">
                          <h1>韩国进口惠人原汁机挤干每一滴果蔬</h1>
                          <p style="margin-top:10px; color:#dc0f50">现价：￥<strong style="font-size:20px;">1999</strong></p>
                          <p style="text-decoration:line-through; color:#999">原价：￥ 2999</p>
                     </div>
                </div>
            </div>
         </div>  
         
         <div class="live_list_1">
            <span class="time_i"><img src="images/hjy_06.png" width="25"  alt=""/></span>
            <div class="live">
                 <p class="live_time"><span class="time">播出时间：8：00-9：00</span> </p>
                 <div class="live_1">
                     <div class="sp_img">
                         <img src="images/hjy1.jpg" width="100" alt=""/>
                         <span class="zhekou"> 
                            <p>50</p>
                            <p style="font-size:10px;">%</p>
                         </span>
                      </div>
                     <div class="sp_js">
                          <h1>韩国进口惠人原汁机挤干每一滴果蔬</h1>
                          <p style="margin-top:10px; color:#dc0f50">现价：￥<strong style="font-size:20px;">1999</strong></p>
                          <p style="text-decoration:line-through; color:#999">原价：￥ 2999</p>
                     </div>
                </div>
            </div>
         </div>   
         
         
         <div class="live_list_1">
            <span class="time_i"><img src="images/hjy_06.png" width="25"  alt=""/></span>
            <div class="live">
                 <p class="live_time"><span class="time">播出时间：8：00-9：00</span> </p>
                 <div class="live_1">
                     <div class="sp_img">
                         <img src="images/hjy1.jpg" width="100" alt=""/>
                         <span class="zhekou"> 
                            <p>50</p>
                            <p style="font-size:10px;">%</p>
                         </span>
                      </div>
                     <div class="sp_js">
                          <h1>韩国进口惠人原汁机挤干每一滴果蔬</h1>
                          <p style="margin-top:10px; color:#dc0f50">现价：￥<strong style="font-size:20px;">1999</strong></p>
                          <p style="text-decoration:line-through; color:#999">原价：￥ 2999</p>
                     </div>
                </div>
            </div>
         </div>  
   -->
    
    
    </div>

</div>

</body>
</html>