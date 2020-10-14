
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, shrink-to-fit=no">
  <title>腾讯云视频点播示例</title>
  <!-- 引入播放器 css 文件 -->
  <link href="//imgcache.qq.com/open/qcloud/video/tcplayer/tcplayer.min.css" rel="stylesheet">
  <!-- 如需在IE8、9浏览器中初始化播放器，浏览器需支持Flash并在页面中引入 -->
  <!--[if lt IE 9]>
  <script src="//imgcache.qq.com/open/qcloud/video/tcplayer/ie8/videojs-ie8.js"></script>
  <![endif]-->
  <!-- 如果需要在 Chrome Firefox 等现代浏览器中通过H5播放hls，需要引入 hls.js -->
  <script src="//imgcache.qq.com/open/qcloud/video/tcplayer/libs/hls.min.0.13.2m.js"></script>
  <!-- 引入播放器 js 文件 -->
  <script src="//imgcache.qq.com/open/qcloud/video/tcplayer/tcplayer.v4.1.min.js"></script>
  <!-- 示例 CSS 样式可自行删除 -->
  <style>
    html,body{
      margin: 0;
      padding: 0;
    }
    /* 通过 css 设置播放器尺寸 这时<video>中的宽高属性将被覆盖*/
    #player-container-id {
      width: 100%;
      max-width: 100%;
      height: 0;
      padding-top: 56.25%; /* 计算方式：播放器以16：9的比率显示，这里的值为 9/16 * 100 = 56.25  */
    }
    /* 外部容器也需要是自适应的*/
    #wrap {
      width: 80%;
      margin: 0 auto;
    }
    /* 设置logo在高分屏的显示样式 */
    @media only screen and (min-device-pixel-ratio: 2), only screen and (-webkit-min-device-pixel-ratio: 2) {
      .tcp-logo-img {
        width: 50%;
      }
    }
  </style>
</head>
<body>
<style>
.cideo-div .tcplayer{border: 5px solid #666; margin:0 }
</style>
<!-- 设置播放器容器 -->
<div class="cideo-div" style="padding:10px;">
 <div onclick="returnBack()" style="position: absolute;top: 30px;right: 30px;z-index: 1000;width: 40px;height: 40px;background-color: #fff;border-radius: 30px;overflow: hidden;padding: 10px;cursor: pointer;opacity: 0.7;">
    <img style="width:40px" src="../../resources/shareshopping/img/btn-close2.png" />
 </div>

<video id="player-container-id" preload="auto" playsinline webkit-playsinline>
  </video>
 </div>
<!--
注意事项：
* 播放器容器必须为 video 标签
* player-container-id 为播放器容器的ID，可自行设置
* 播放器区域的尺寸请按需设置，建议通过 css 进行设置，通过css可实现容器自适应等效果
* playsinline webkit-playsinline 这几个属性是为了在标准移动端浏览器不劫持视频播放的情况下实现行内播放，此处仅作示例，请按需使用
* 设置 x5-playsinline 属性会使用 X5 UI 的播放器
-->
<script>
   
   function returnBack(){

		window.history.back();
   }
   
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
      
      function decodeUTF8(str){  
	    return str.replace(/(\\u)(\w{4}|\w{2})/gi, function($0,$1,$2){  
	    return String.fromCharCode(parseInt($2,16));  
	  });   
	 }
	 
	     
  var fileID = decodeUTF8(decodeURIComponent(up_urlparam("videoId",'')));
  var player = TCPlayer('player-container-id', { // player-container-id 为播放器容器ID，必须与html中一致
    fileID:fileID, // 请传入需要播放的视频filID 腾讯5285890799710670616  
    appID: '1302395454' // 请传入点播账号的appID 腾讯1400329073  生产1400389132
    //其他参数请在开发文档中查看
  });
</script>
</body>
</html>
