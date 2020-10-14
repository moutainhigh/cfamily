

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>视频播放</title>
    <script type="text/javascript" src="../../resources/yulan/js/jquery-2.1.4.js?v=xb1552563555284"></script>
    <style>
	*{ margin:0; padding:0}
        .curr {
            color: #f37121;
        }
		#myvideo video::-internal-media-controls-download-button {


}

#myvideo video::-webkit-media-controls-enclosure {

    overflow:hidden;

}

#myvideo video::-webkit-media-controls-panel {

    width: calc(100% + 300px);

}
#videolist li{ line-height:30px; text-decoration:underline }
#myvideo{ border:10px solid #666}
.new_lb h1{ font-size:20px; margin-bottom:10px; margin-top:10px;}
.new_lb p{ font-size:16px; line-height:25px;}
    </style>
</head>
<body>


    <div style="padding:10px;">
       <div id="video">

            <video id="myvideo" width="1200" height="700" style="object-fit:fill"
				controlsList="nodownload"
                   webkit-playsinline="true"
                   x-webkit-airplay="true"
                   playsinline="true"
                   x5-video-orientation="h5"
                   x5-video-player-type="h5"
                   x5-video-player-fullscreen="false"
                   preload="auto"
                   controls="controls"
                   autobuffer=""
                   src=""
                   loop="true"
                   poster=../../resources/cfamily/zzz_js/tv.png></video>
        </div>
        
        <script>
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
      ccvid = decodeUTF8(decodeURIComponent(up_urlparam("videoId",'')));
       
               $.ajax({
               	type:'post',
               	dataType:'json',
               	data:{},
               	url:'/cfamily/jsonapi/com_cmall_groupcenter_account_api_ApiForGetVideo?api_key=betafamilyhas&videoid='+ccvid+'&operate=play',
               	success:function(msg){
               		console.log(msg);
               	  if (msg.resultCode == 1) {
               	   setTimeout(function () {
                       var playVideo = $("#myvideo")
                       playVideo[0].pause();
                       playVideo.attr("src", msg.videoLink);
                       playVideo[0].play()

                   }, 50);
               	}
               	  else{
               		 alert("视频解析失败,请检查视频链接是否正确!");
               	  }
               	}
               	})
      
      
        
       
        </script>


</body>
</html>
