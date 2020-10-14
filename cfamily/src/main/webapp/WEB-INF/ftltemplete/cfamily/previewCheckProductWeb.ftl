<link href="/cfamily/resources/cfamily/zzz_js/prerogative/css/style.css" rel="stylesheet"> 
<link rel="icon" type="/cfamily/resources/images/image/png" href="/cfamily/resources/cfamily/zzz_js/favicon.png">
<script type="text/javascript" src="/cfamily/resources/cfamily/zzz_js/prerogative/js/TouchSlide.1.1.js"></script>
<@m_common_html_css ["cfamily/css/video-js.min.css"] />
<style>
.focus1{ width:640px; height:640px;  margin:0 auto; position:relative; overflow:hidden;   }
	.focus1 .hd{ width:100%; height:30px;  position:absolute; z-index:1; bottom:10px; text-align:center;  }
	.focus1 .hd ul{ display:inline-block; height:20px; padding:3px 5px; background-color:rgba(255,255,255,0.7); 
		-webkit-border-radius:5px; -moz-border-radius:5px; border-radius:5px; font-size:0; vertical-align:top;
	}
	.focus1 .hd ul li{ display:inline-block; width:20px; height:20px; -webkit-border-radius:10px; -moz-border-radius:10px; border-radius:10px; background:#8C8C8C; margin:0 5px;  vertical-align:top; overflow:hidden;   }
	.focus1 .hd ul .on{ background:#FE6C9C;  }

	.focus1 .bd{ position:relative; z-index:0; }
	.focus1 .bd li img{ width:100%;  height:640px;   }
	.focus1 .bd li a{ -webkit-tap-highlight-color:rgba(0, 0, 0, 0); 
	.zab_page_default_header{margin-bottom:0!important;}
</style>
<style>
 .video-js .vjs-big-play-button{
        top:45%!important;
        left:50%!important;
        margin-left:-1.5em!important;
        }
</style>
<script type="text/javascript">
	<#assign product_code = b_page.getReqMap()["zw_f_product_code"]>
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(product_code)};
	$(document).ready(function(){
		$("body").removeClass("zab_page_default_body");
		init(product);
		function init(product){
		
		var pcPicList = product.pcPicList;		//轮播图
		var productName = product.productName;	//商品名称
		var productCode = product.productCode;	//商品编号
		var minSellPrice = product.minSellPrice;//商品售价
		var marketPrice = product.marketPrice;	//商品市场价
		var description = product.description;	//商品描述
		var pcProductpropertyList = product.pcProductpropertyList;//商品关联属性
		var productSkuInfoList = product.productSkuInfoList;//商品sku信息
		var video_url_show = product.videoUrlShow;
		var product_desc_video = product.productDescVideo;
		var video_main_pic = product.videoMainPic;
		for(var i = 0;i<pcPicList.length;i++){
			var picListHtml = '<li><a href="javascript:void(0)"><img src="'+pcPicList[i].picUrl+'" /></a></li>';
			$('#idSlider_22').append(picListHtml);
			TouchSlide({ 
					slideCell:"#focus1",
					titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
					mainCell:".bd ul", 
					effect:"left", 
					autoPlay:true,//自动播放
					autoPage:true, //自动分页
					interTime:5000,
					switchLoad:"_src" //切换加载，真实图片路径为"_src" 
				});
		}
		
	    
	    $("#productName").html(productName);
	    $("#productCode").html(productCode);
	    
	    $("#sellPrice").html(minSellPrice);
	    $("#marketPrice").html(marketPrice);
	    var skuKeyList = [];
	    var skuValueList = [];
	    var colorLength = 1;
	    for(var i=0;i<productSkuInfoList.length;i++){
	    	if(productSkuInfoList[i].saleYn != "Y") {
	    		continue;
	    	}
	    
	    	var skuKeyvalue = productSkuInfoList[i].skuKeyvalue;
	    	if(skuKeyvalue == '') {
	    		skuKeyvalue = productSkuInfoList[i].skuValue;
	    	}
   	
	    	var keyValue = skuKeyvalue.split("&");
	    	
	    	var key = "";
	    	var value="";
	    	if(keyValue[0].substr(0,2)=="颜色"){
	    		key = keyValue[0].split("=")[1];
	    		value = keyValue[1].split("=")[1];
	    	}else{
	    		key = keyValue[1].split("=")[1];
	    		value = keyValue[0].split("=")[1];
	    	}
	    	var flag = 0;
    		for(var j=0;j<skuKeyList.length;j++){
    			if(key == skuKeyList[j]){
    				flag = 1;
    				break;
    			}
    		}
    		if(flag == 0){
	    		skuKeyList.push(key);
    		}
    		flag = 0;
    		for(var j=0;j<skuValueList.length;j++){
    			if(value == skuValueList[j]){
    				flag = 1;
    				break;
    			}
    		}
    		if(flag == 0){
	    		skuValueList.push(value);
    		}
	    }
	    
	    //重新排序赋值显示
	     for(var i=skuKeyList.length;i>=1;i--){
	       for(var j=0;j<productSkuInfoList.length;j++){
	         if(j<(i*skuValueList.length)&&j>=((i-1)*skuValueList.length)){
	         var skuKeyvalue =productSkuInfoList[j].skuValue;
	    	  if(null == skuKeyvalue || "" == skuKeyvalue || skuKeyvalue.indexOf("颜色=共同&款式=共同") > 0 || skuKeyvalue.indexOf("颜色属性=共同&规格属性=共同") > 0){
	    		continue;
	    	}
	    	if(productSkuInfoList[j].saleYn=="Y"){
	    	$("#skuProperties").after('<tr><td>'+skuKeyvalue+'</td><td>'+productSkuInfoList[j].sellPrice+'</td></tr>');
	    	}
	       }
	       }

	     }
	    
	   var skuKeyHtml = ""	;

	  for(var j=0;j<skuKeyList.length;j++){
	    skuKeyHtml += '<span>'+skuKeyList[j]+'</span>';
	    if(j < skuKeyList.length-1){
	    	skuKeyHtml +='、';
	    }
      }
	   var skuValueHtml = "";
	   for(var j=skuValueList.length-1;j>=0;j--){
	    skuValueHtml += '<span>'+skuValueList[j]+'</span>';
	    if(j > 0){
	    	skuValueHtml +='、';
	    }
      }
      $("#skuKey").html(skuKeyHtml);
      $("#skuValue").html(skuValueHtml);
      
      var descriptionPic = description.descriptionPic;
      var descriptionPicList = descriptionPic.split("|");
      for(var i=0;i<descriptionPicList.length;i++){
      	 $("#descriptPic").append('<img src="'+descriptionPicList[i]+'" />');
      }
      if(product_desc_video != ''){
      	$("#descriptPic").prepend('<video id="my-video" class="video-js" controls preload="auto" width="600" height="300" poster="'+video_main_pic+'" data-setup="{}"><source src="'+video_url_show+'" type="video/mp4"></video>');
      }
      for(var i=0;i<pcProductpropertyList.length;i++){
      	if(pcProductpropertyList[i].propertyType=='449736200004'){
	      	 $("#properties").append('<p><span class="st">【'+pcProductpropertyList[i].propertyKey+'】</span><span>'+pcProductpropertyList[i].propertyValue+'</span></p>');
      	}
      	
      }
	}
	});
</script>




<div class="wrap">

     
 


    <div class="box pro">
    
        <div id="focus1" class="focus1">
				<div class="hd">
					<ul></ul>
				</div>
				<div class="bd">
					<ul id="idSlider_22">
						  	
					</ul>
				</div>
			</div>

    
      
        <div class="num">
            <h2 id="productName"></h2>
            <p class="bianh">商品编号：<span id="productCode">102239</span></p>
            <p class="price"><em class="nprice"><em>￥</em><font id="sellPrice"></font></em><span><em>￥</em><font id="marketPrice"></font></span></p>
        </div>
    </div>
    <div class="box sizes" id="skuInfo">
        <div class="lid">
            <em>颜色：</em>
            <div id="skuKey"></div>
        </div>
        <div class="lid">
            <em>款式：</em>
            <div id="skuValue"></div>
        </div>
        <div class="lid">
			<table border="1" width="100%" style="font-size:13px">
				<tr id="skuProperties">
					<td id="skuProperty">规格型号</td>
					<td id="skuSellPrice">价格(元)</td>
				</tr>
			</table>
        </div>
        
    </div>
    <div class="box">
        <div class='tabnav' id="menu">
            <ul>
                <li onclick="javascript:test_item(0);" class="on"><span>图文详情</span></li>
                <li onclick="javascript:test_item(1);"><span>规格参数</span></li>
            </ul>
         </div>
         <div id="tabc0" class='tabc'>
            <div class="imgs" id="descriptPic">
               
            </div>
         </div>
         <div  id="tabc1" class="no">
            <div class="param" id="properties">
            </div>
         </div>
         <div align = "center">
         	<input class="btn btn-success" onclick="returnTop()" value="返回顶部 " type="button">
 		 </div>
    </div>   
</div>
<script type="text/javascript" > 
    function test_item(n){
        var menu = document.getElementById("menu");
        var menuli = menu.getElementsByTagName("li");
        for(var i = 0; i< menuli.length;i++){
            menuli[i].className="";
            menuli[n].className="on";
            document.getElementById("tabc"+ i).className = "no";
            document.getElementById("tabc"+ n).className = "tabc";
        }
    }
    
    function returnTop() {
       document.body.scrollTop = 0;
       document.documentElement.scrollTop = 0;
    }
</script>
<script type="text/javascript">  
    $(window).load(function() { 
	 	$('img').mouseover(function(obj){
	 		var height_ = obj.target.naturalHeight;
	 		var width_ = obj.target.naturalWidth;
	 		var msg = "图片暂未加载完成，请稍后!"; 
	 		 
	 		var opt = new Object();
			opt.imageUrl = obj.target.currentSrc;
			api_call('com_cmall_familyhas_api_ApiForImageProperty', opt , function(result){
				var size = result.size;
				msg = "该图片高 = " + height_  + "px | 宽 = " + width_ + "px | 大小 = " + size + "Kb";  
	        	$(obj.target.parentElement).prop("title", msg);   
			}); 
	    });
	}); 
 	function api_call(sTarget, oData, fCallBack) {
		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		}:{api_key : 'jsapi',api_input:''};
		
		//oData = $.extend({}, defaults, oData || {});

		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			//fCallBack(data);			
			fCallBack(data);
		});
	} 
	
	
	

 
</script>
<script>
define('global/window', [], () => {
    return window;
});

define('global/document', ['global/window'], (window) => {
    return window.document;
});
require(['cfamily/js/video.min'],function(videojs){
	window.videojs = videojs;
})
</script> 
