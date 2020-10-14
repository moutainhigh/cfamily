<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="UTF-8">
<title>大牌特卖</title>
<link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
<link  href="../../resources/css/ppth_todaysales_new_one/style.css" rel='stylesheet'>	
<script src="../../resources/cfamily/zzz_js/zzz.min.js"></script>
<script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script> 
<script type="text/javascript" >
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
	var buyerType = up_urlparam('buyerType','');
	var isSpecial = up_urlparam("isSpecial");
	var syurl = "../../../cfamily/jsonapi/com_cmall_familyhas_api_ApiForBrandPreference?api_key=betafamilyhas&picWidth=1080&activity=467703130008000100030001";
	$(document).ready(function() {
		$.ajax({
			type: "GET",
			url: syurl,
			dataType: "json",
			success: function(data) {
				var innHTML = '';
				if(data.resultCode == 1){
					
					systm = Date.parse(data.sysTime.replace(/-/g, "/"));
					setInterval(function(){
					  	systm = eval(systm + 1000);
					}, 1000)
					
					var itemArr = data.items;
					for(var i=0;i<itemArr.length;i++) {
						var item = itemArr[i];
						var shareInfo = item.share_info;
						innHTML += '<div class="lid">';
							innHTML += '<a id="tid'+i+'" href="javascript:void(0);" onclick="detail(\''+item.shareFlag+'\',\''+item.infoCode+'\',\''+shareInfo.share_content+'\',\''+shareInfo.share_img_url+'\',\''+shareInfo.share_title+'\',\''+item.item_name+'\','+i+')">';
								innHTML += '<b class="icob" style="display:none;" ichsy_end_time="'+item.downTime+'">已结束</b>';
								innHTML += '<img class="delay" src="../../resources/cfamily/zzz_js/bg.png" data-original="'+item.img_url+'" alt="" />';
								var nam = item.item_name;
								if(nam.length>10) {
									nam = nam.substring(0,11);
								}
								if(item.discount == "" || eval(item.discount) <= 0) {
									innHTML += '<span><b></b>'+nam+'</span>';
								} else {
									innHTML += '<span><b>'+item.discount+'折起</b>'+nam+'</span>';
								}
							innHTML += '</a>';
						innHTML += '</div>';
					}
				}
				$(".wrap").html(innHTML);
				$("img.delay").lazyload({threshold:100,placeholder:""});
				//倒计时定时
				$('.icob:hidden').each(function(i){
					var end_time = $(this).attr('ichsy_end_time');
					setInterval('GetRTime("' + end_time + '",' + i + ')', 1000);
				})
				
			}
		})
	})
	function detail(isShare,num,share_content,share_img_url,share_title,title,i){
		//判断是否已经结束
		var cobArr = $("#tid"+i).find(".icob:hidden");
		if(cobArr.length == 1) {
			var share_tm = false;
			if(isShare == '449747110002') {
				share_tm = true;
			} 
			var url = 'ppth_detail.html?isSpecial='+isSpecial+'&infoCode='+num+'&isShare='+share_tm+'&share_content='+encodeURIComponent(share_content)+'&share_img_url='+share_img_url+'&share_title='+encodeURIComponent(share_title)+'&title='+encodeURIComponent(title)+'&buyerType='+buyerType;
			document.location = url;
		} else {
			new Toast({context:$('body'),message:'专题已结束',top:'40%'}).show();
		}
	}
	function GetRTime(endTime,i) {
		var EndTime = new Date(endTime.replace(/-/g, "/"));
	  	var leftTime = eval(EndTime.getTime() - systm);
	  	if(leftTime >= 0) {
	  		var leftsecond = parseInt(leftTime / 1000);
		  	var day1 = Math.floor(leftsecond / (60 * 60 * 24));
		  	var hour = Math.floor((leftsecond - day1 * 24 * 60 * 60) / 3600);
		  	var minute = Math.floor((leftsecond - day1 * 24 * 60 * 60 - hour * 3600) / 60);
		  	var second = Math.floor(leftsecond - day1 * 24 * 60 * 60 - hour * 3600 - minute * 60);
		  	if (hour < 10) {
			  	hour = "0" + hour
		  	}
		  	if (minute < 10) {
			  	minute = "0" + minute
		  	}
		  	if (second < 10) {
			  	second = "0" + second
		  	}
			if(hour=="00" && minute=="00" && second=="00" ) {
				$(".icob:hidden").eq(i).show();
			}
	  	}
	}
	
</script>
</head>	
<body>	
<div class="wrap pinp-list">
	
</div>	
</body>
</html>