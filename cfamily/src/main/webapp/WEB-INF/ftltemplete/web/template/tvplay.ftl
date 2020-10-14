<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta charset="UTF-8">
	<title>tv直播</title>
    <script src="../../resources/cfamily/zzz_js/template/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="../../resources/sewise-player/player/sewise.player.min.js"></script>
    <script type="text/javascript" >
    	$(function(){
	    	var playerHtml = '';
			playerHtml += '<script type="text/javascript">';
				playerHtml += 'SewisePlayer.setup({';
					playerHtml += 'server: "vod",';
					playerHtml += 'type: "m3u8",';
					playerHtml += 'autostart: "false",';
					playerHtml += 'videourl: "'+parent.template.tvUrl+'",';
					playerHtml += 'skin: "vodWhite",';
					playerHtml += 'topbardisplay: "disable",';
					playerHtml += 'lang: "zh_CN",';
					playerHtml += 'poster: "'+parent.template.tvPic+'",';
					playerHtml += 'claritybutton: "disable"';
					playerHtml += '}, "player");';
			playerHtml += '<\/script>';
	    	$("#player").html(playerHtml);
	    	$("#player").css("height",parent.tvTempleteHeight);
    	})
    	
    </script>
</head>
<body style=" margin: 0;">

<div style="width:100%;">
	<div id="player" style="width: 100%;" ></div>
</div>
</body>
</html>