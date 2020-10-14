var newUser ={
		init :function(){
			//手机号
			pmobile = up_urlparam("mobile",'');
			var regNum = /^1[0-9]{10}$/;
		

			if(null != pmobile && pmobile != '' && null != pmobile.match(regNum)) {
				var pm = pmobile.substring(0, 3) + "****" + pmobile.substring(pmobile.length - 4);
				$(".yhqts").html("优惠劵已存入账户"+pm+"，请在惠家有「我的优惠券」查看");
				
			}

			//新用户领取成功获得的优惠信息
           couponInfo = up_urlparam("couponInfo",'');
           var jsonList = couponInfo.split(';');
			if(jsonList!=null&&jsonList.length>0){
				for(var j=0;j<jsonList.length;j++){
					var cpInfo = jsonList[j].split(',');
					var temMoneyType = "";
					var startTime="";
					var endTime ="";
					var money="";
					var startTimeStr ="";
					var endTimeStr = "";
					for(var i = 0;i<cpInfo.length;i++){
						var childArray = cpInfo[i].split('#');
						//开始时间
						if(childArray[0]=="start_time"){
							 startTime = childArray[1];
							// var st = new Date(eval(startTime));
							 startTimeStr = startTime.substring(0,10).replace("-",".");
							 startTimeStr= startTimeStr.replace("-",".");
						}
					
						//结束时间
						if(childArray[0]=="end_time"){
							 endTime = childArray[1];
							// var et = new Date(eval(endTime));
							 endTimeStr = endTime.substring(0,10).replace("-",".");
							 endTimeStr=endTimeStr.replace("-",".");
							
						}
						
						
						//优惠卷金额
						
						if(childArray[0]=="money_type"){
							temMoneyType = childArray[1];
						}
						if(childArray[0]=="money"){
							money=childArray[1];
						}
					}
					
					if(temMoneyType!=""&&temMoneyType!="449748120002"){
						var html = document.getElementById("myId").innerHTML;
						document.getElementById("myId").innerHTML = html+"<div class='yhq_zh'> <img src='../../resources/images/recomend/quan_03.png' /> <div class='quan_j'><span>￥</span>"+money+"</div>"
                                +"<div class='quan_xrq'>— 新人券 —</div>"
                                +"<div class='quan_sj'>"
                                +"<p>使用期限</p>"
                                +"<p>"+startTimeStr+"-"+endTimeStr+"</p>"
                                +"</div></div>"         
					}
					else if (temMoneyType!=""&&temMoneyType=="449748120002"){
					
						money = money/10;
						
						var html = document.getElementById("myId").innerHTML;
						document.getElementById("myId").innerHTML = html+"<div class='yhq_zh'> <img src='../../resources/images/recomend/quan_03.png' /> <div class='quan_j'>"+money+"折</div>"
                                +"<div class='quan_xrq'>— 新人券 —</div>"
                                +"<div class='quan_sj'>"
                                +"<p>使用期限</p>"
                                +"<p>"+startTimeStr+"-"+endTimeStr+"</p>"
                                +"</div></div>"       
					}
				}
			}
		
			/**
			 * 查询已经领取的优惠券
			 */
			var getInfoUrl  = "/cfamily/jsonapi/com_cmall_familyhas_api_ApiForCheckOldUser?api_key=betafamilyhas&mobile="+pmobile;
			$.ajax({
		 		type:"GET",
		 		url:getInfoUrl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1) {
		 				//新用户领取失败
		 				$(".moneyN").find("p span:eq(1)").html(data.startTime+"-"+data.endTime);
		 				$(".moneyN").find("h2").html("<i>￥</i>"+data.money);
		 			} 	 		
		 		}
			});
		},
		up_urlparam:function(key,sUrl) {
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

		
		
		
};