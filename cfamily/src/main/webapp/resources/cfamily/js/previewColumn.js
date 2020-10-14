var previewColumn = {
		init : function(){
			var opt ={};
			opt.maxWidth = "322";
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiHomeColumn',opt,function(result) {
				var resultCode = result.resultCode;
				if(resultCode == 1){
					var topThreeList = result.topThreeColumn.topThreeColumnList;
					for ( var i = 0; i < topThreeList.length; i++) {
						var topThreeContent = topThreeList[i];
						if(topThreeContent.columnType=="4497471600010001"){//栏目类型为轮播广告
							var lbPic = topThreeContent.contentList[0].picture;
							$("#u11_img").attr("src",lbPic);
						}
						if(topThreeContent.columnType=="4497471600010003"){//栏目类型为二栏广告
							$("#u101_img").attr("src",topThreeContent.contentList[0].picture);
							$("#u103_img").attr("src",topThreeContent.contentList[1].picture);
						}
						if(topThreeContent.columnType=="4497471600010004"){//栏目类型为导航栏
							$("#u15_img").attr("src",topThreeContent.contentList[0].picture);
							$("#u17_img").attr("src",topThreeContent.contentList[1].picture);
							$("#u19_img").attr("src",topThreeContent.contentList[2].picture);
							$("#u21_img").attr("src",topThreeContent.contentList[3].picture);
							$("#u24span").html(topThreeContent.contentList[0].title);
							$("#u26span").html(topThreeContent.contentList[1].title);
							$("#u28span").html(topThreeContent.contentList[2].title);
							$("#u30span").html(topThreeContent.contentList[3].title);
						}
						
					}
					var columnList = result.columnList;
					var rightFlag = "0";//右两栏类型显示次序
					var rf = "0";//右两栏第一个是否执行标记
					var firstBrandFlag = "0";//一栏广告显示次序
					var fb="0";//一栏广告第一个是否执行标记（共两个一栏广告）
					for ( var j = 0; j < columnList.length; j++) {
						columnContent = columnList[j];
						contentList = columnContent.contentList;
						columnType = columnContent.columnType;
						if(columnType == "4497471600010011"){//限时抢购
							$("#u34span").html(columnContent.columnName);
							$("#u120span").html(columnContent.showmoreTitle);
							$("#u37_img").attr("src",contentList[0].productInfo.mainpicUrl);
							$("#u71_img").attr("src",contentList[1].productInfo.mainpicUrl);
							$("#u69_img").attr("src",contentList[2].productInfo.mainpicUrl);
							var sellPrice1 = contentList[0].productInfo.sellPrice;
							var sellPrice2 = contentList[1].productInfo.sellPrice;
							var sellPrice3 = contentList[2].productInfo.sellPrice;
							if(sellPrice1 != ""){
								$("#u79span").html("￥"+sellPrice1);
							}
							if(sellPrice2 != ""){
								$("#u81span").html("￥"+sellPrice2);
							}
							if(sellPrice3 != ""){
								$("#u83span").html("￥"+sellPrice3);
							}
							var discount1 = contentList[0].productInfo.discount;
							var discount2 = contentList[1].productInfo.discount;
							var discount3 = contentList[2].productInfo.discount;
							if(discount1 != ""){
								$("#u85span").html(discount1+"折");
							}
							if(discount2 != ""){
								$("#u87span").html(discount2+"折");
							}
							if(discount3 != ""){
								$("#u89span").html(discount3+"折");
							}
						}else if(columnType == "4497471600010005"){//一栏推荐
							$("#u110span").html(columnContent.columnName);
							$("#u127span").html(columnContent.showmoreTitle);
							$("#u111_img").attr("src",contentList[0].picture);
							$("#u114span").html(contentList[0].productInfo.productName);
							$("#u131span").html("￥"+contentList[0].productInfo.sellPrice);
						}else if(columnType == "4497471600010002"){//一栏广告
							if(firstBrandFlag == "0"){
								$("#u117_img").attr("src",contentList[0].picture);
								fb = "1";
							}else if(firstBrandFlag == "1"){
								$("#u214_img").attr("src",contentList[0].picture);
							}
						}else if(columnType == "4497471600010006"){//右两栏推荐
							if(rightFlag == "0"){//首先显示每日推荐的右两栏
								$("#u135span").html(columnContent.columnName);
								$("#u138span").html(columnContent.showmoreTitle);
								for ( var k = 0; k < contentList.length; k++) {
									var sellPrice = contentList[k].productInfo.sellPrice;
									if(contentList[k].posion == "1"){
										$("#u141_img").attr("src",contentList[k].picture);
										$("#u252span").html(contentList[k].productInfo.productName);
										if(sellPrice != ""){
											$("#u254span").html("￥"+sellPrice);
										}
									}else if(contentList[k].posion == "2"){
										$("#u143_img").attr("src",contentList[k].picture);
									}else if(contentList[k].posion == "3"){
										$("#u147_img").attr("src",contentList[k].picture);
									}
								}
								rf = 1;
							}else if(rightFlag == "1"){//显示热门推荐的右两栏
								$("#u227span").html(columnContent.columnName);
								for ( var k = 0; k < contentList.length; k++) {
									if(contentList[k].posion == "1"){
										$("#u243_img").attr("src",contentList[k].picture);
										$("#u246span").html(contentList[k].title);
										$("#u280span").html(contentList[k].description);
									}else if(contentList[k].posion == "2"){
										$("#u220_img").attr("src",contentList[k].picture);
										$("#u250span").html(contentList[k].title);
										$("#u282span").html(contentList[k].description);
									}else if(contentList[k].posion == "3"){
										$("#u224_img").attr("src",contentList[k].picture);
										$("#u248span").html(contentList[k].title);
										$("#u284span").html(contentList[k].description);
									}
								}
							}
						}else if(columnType == "4497471600010007"){//左两栏推荐
							$("#u231span").html(columnContent.columnName);
							for ( var p = 0; p < contentList.length; p++) {
								var sp = contentList[p].productInfo.sellPrice;
								if(contentList[p].posion == "1"){
									$("#u235_img").attr("src",contentList[p].picture);
									$("#u256span").html(contentList[p].productInfo.productName);
									if(sp1 != ""){
										$("#u258span").html("￥"+sp);
									}
								}else if(contentList[p].posion == "2"){
									$("#u239_img").attr("src",contentList[p].picture);
									$("#u260span").html(contentList[p].productInfo.productName);
									if(sp1 != ""){
										$("#u262span").html("￥"+sp);
									}
								}else if(contentList[p].posion == "3"){
									$("#u233_img").attr("src",contentList[p].picture);
								}
							}
						}else if(columnType == "4497471600010008"){//商品推荐
							$("#u152span").html(columnContent.columnName);
							$("#u154_img").attr("src",contentList[0].productInfo.mainpicUrl);
							$("#u156_img").attr("src",contentList[1].productInfo.mainpicUrl);
							$("#u158_img").attr("src",contentList[2].productInfo.mainpicUrl);
							var p1 = contentList[0].productInfo.productName;
							if(p1.length > 6 && p1.length <= 12){
								p1 = p1.substring(0,6)+"<br>"+p1.substring(6,p1.length);
							}else if(p1.length > 12){
								p1 = p1.substring(0,6)+"<br>"+p1.substring(6,10)+"...";
							}
							var p2 = contentList[1].productInfo.productName;
							if(p2.length > 6 && p2.length <= 12){
								p2 = p2.substring(0,6)+"<br>"+p2.substring(6,p2.length);
							}else if(p2.length > 12){
								p2 = p2.substring(0,6)+"<br>"+p2.substring(6,10)+"...";
							}
							var p3 = contentList[2].productInfo.productName;
							if(p3.length > 6 && p3.length <= 12){
								p3 = p3.substring(0,6)+"<br>"+p3.substring(6,p3.length);
							}else if(p3.length > 12){
								p3 = p3.substring(0,6)+"<br>"+p3.substring(6,10)+"...";
							}
							$("#u163span").html(p1);
							var sp1 = contentList[0].productInfo.sellPrice;
							if(sp1 != ""){
								$("#u165span").html("￥"+sp1);
							}
							$("#u290span").html(p2);
							var sp2 = contentList[1].productInfo.sellPrice;
							if(sp2 != ""){
								$("#u167span").html("￥"+sp2);
							}
							$("#u292span").html(p3);
							var sp3 = contentList[2].productInfo.sellPrice;
							if(sp3 != ""){
								$("#169span").html("￥"+sp3);
							}
						}else if(columnType == "4497471600010009"){//两栏多行推荐
							$("#u175span").html(columnContent.columnName);
							for ( var q = 0; q < contentList.length; q++) {
								if(contentList[q].posion == "1"){
									$("#u200_img").attr("src",contentList[q].picture);
									$("#u203span").html(contentList[q].title);
									$("#u205span").html(contentList[q].description);
								}else if(contentList[q].posion == "2"){
									$("#u206_img").attr("src",contentList[q].picture);
									$("#u209span").html(contentList[q].title);
									$("#u211span").html(contentList[q].description);
								}else if(contentList[q].posion == "3"){
									$("#u176_img").attr("src",contentList[q].picture);
									$("#u264span").html(contentList[q].title);
									$("#u193span").html(contentList[q].description);
								}else if(contentList[q].posion == "4"){
									$("#u178_img").attr("src",contentList[q].picture);
									$("#u266span").html(contentList[q].title);
									$("#u189span").html(contentList[q].description);
								}else if(contentList[q].posion == "5"){
									$("#u180_img").attr("src",contentList[q].picture);
									$("#u270span").html(contentList[q].title);
									$("#u191span").html(contentList[q].description);
								}else if(contentList[q].posion == "6"){
									$("#u182_img").attr("src",contentList[q].picture);
									$("#u268span").html(contentList[q].title);
									$("#u195span").html(contentList[q].description);
								}else if(contentList[q].posion == "7"){
									$("#u184_img").attr("src",contentList[q].picture);
									$("#u272span").html(contentList[q].title);
									$("#u274span").html(contentList[q].description);
								}else if(contentList[q].posion == "8"){
									$("#u186_img").attr("src",contentList[q].picture);
									$("#u278span").html(contentList[q].title);
									$("#u276span").html(contentList[q].description);
								}
							}
						}else if(columnType == "4497471600010010"){//TV直播
							$("#u91span").html(columnContent.columnName);
							$("#u95span").html(columnContent.showmoreTitle);
							$("#u92_img").attr("src",contentList[0].productInfo.mainpicUrl);
							var pName = contentList[0].productInfo.productName;
							if(pName.length > 25){
								pName = pName.substring(0,25)+"...";
							}
							$("#u99span").html(pName);
							var start = contentList[0].startTime.substring(11,16);
							var end = contentList[0].endTime.substring(11,16);
							$("#u97span").html("正在直播 "+start+"-"+end);
						}
						if(rf=="1"){
							rightFlag = "1";
						}
						if(fb=="1"){
							firstBrandFlag="1";
						}
					}
//					$("#qk1").hide();
				}else{
					zapadmin.model_message('查询数据失败!');
				}
			});
		}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/previewColumn", function() {
		return previewColumn;
	});
}