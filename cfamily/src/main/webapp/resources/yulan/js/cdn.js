//http强转https
/*if (document.location.protocol == "http:") {
    location.replace(location.href.replace(/http:/g, "https:"))
}*/


var cdn_path = "";
var cdn_css = "";//"https://image-mall.huijiayou.cn/hjyshop";
var static_path = "/PubImg/hjyapi";
//输出静态文件 [[css列表],[js列表]]
function WriteStatic(staticlist) {
	
    for (var i = 0; i < staticlist[0].length; i++) {
        //document.write('<link rel="stylesheet" type="text/css" href="' + cdn_path + staticlist[0][i] + '?v=' + new Date().getTime() + '">');
        document.write('<link rel="stylesheet" type="text/css" href="' + cdn_path + staticlist[0][i] + '?v=1">');
    }
    for (var i = 0; i < staticlist[1].length; i++) {
        //document.write('<script type="text/javascript" src="' + cdn_path + staticlist[1][i] + '?v=a' + new Date().getTime() + '"></scr' + 'ipt>');
        document.write('<script type="text/javascript" src="' + cdn_path + staticlist[1][i] + '?v=883"></scr' + 'ipt>');
    }
	
if (document.title==undefined || document.title=="") {
        document.write('<title>首页导航预览</ti' + 'tle>');
    }
	var haskeywords=true;
	for(var i=0;i<document.head.childNodes.length;i++){
		if(document.head.childNodes[i].name!=undefined && document.head.childNodes[i].name=="keywords"){
			haskeywords=false;
			break;
		}
	}
    if (haskeywords) {
        document.write('<meta name="keywords" content="惠家有,厨房电器,料理机,豆浆机,电磁炉,电压力锅,空调扇,床上用品,床品套件,品牌,价格,图片,家居百货,好神拖,锅具,营养保健,保健品,收藏品,网上购物,电视购物,视频购物,家有购物网络商城,m.jyh.com"/>');
    }
	var hasdescription=true;
	for(var i=0;i<document.head.childNodes.length;i++){
		if(document.head.childNodes[i].name!=undefined && document.head.childNodes[i].name=="description"){
			hasdescription=false;
			break;
		}
	}
    if (hasdescription) {
        document.write('<meta name="description" content="惠家有,家有购物唯一官方网站,微信商城,首选家庭购物淘宝商城、电视购物、网络商城。家居百货,家用电器,美容服饰等万种超值商品1折起,100%正品保证,支持货到付款,400-867-8210"/>');
    }

}

//输出管理后台生成的静态文件 [[js列表]]
function WriteStatic_tj(staticlist) {
    for (var i = 0; i < staticlist[0].length; i++) {
       document.write('<script type="text/javascript" src="' + cdn_path + staticlist[0][i] + '?v=' + new Date().getTime() + '"></scr' + 'ipt>');
        //document.write('<script type="text/javascript" src="' + cdn_path + staticlist[0][i] + '?v=11"></scr' + 'ipt>');
    }
    
}