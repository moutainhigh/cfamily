<script type="text/javascript" src="../resources/fileconcat/js-autoconcat.js?v=2.0.0.1"></script>
<script type="text/javascript" src="../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
<script>
require(['cfamily/js/coupon'],
function()
{
	var sUrl = window.location.href;
	var sParams = sUrl.split('?')[1].split('&');
	var mobile = "";
	for (var i = 0, j = sParams.length; i < j; i++) {
		var sKv = sParams[i].split("=");
		if (sKv[0] == "p") {
			mobile = sKv[1];
			break;
		}
	}
	zapjs.f.ready(function()
		{
			coupon.init(mobile);
		}
	);
}

);
</script>

<!DOCTYPE html>
<html lang="en-US">
<head>
<link href="../resources/css/style.css" rel="stylesheet">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>惠家有,送现金大礼包啦!</title>

</head>

<body id="bd1">

</body>
</html>
