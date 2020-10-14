/// <reference path="../jquery-2.1.4.js" />
/// <reference path="../iscroll.js" />
/// <reference path="../g_header.js" />
/// <reference path="../functions/g_Const.js" />
/// <reference path="../functions/g_Type.js" />

var videoanimate;
var canvas;
var playvideoid;



//TVLive_new拖拽函数
function TVLive_new_drag(obj, id) {
  var move_width = 0;
  var speedX = 0;
  var num_speedX = 0;
  var lastX = 0;
  var timer = null;
  var win_with = $(window).width();
  var li_width = win_with * 0.5;
  clearInterval(timer);
  timer = setInterval(function () {
      $("#TV_module-ul_" + id + " li").each(function () {
          $(this).css("transform", "scale(" + (1.4 - 0.2 * Math.abs(($(this).offset().left + li_width * 0.5 - win_with * 0.5) / (win_with * 0.5))) + ")")
      })

      //计算z-dindex
      var z_disArr = [];
      var z_minDis = 0;
      var z_minIndex = 0;
      $("#TV_module-ul_" + id + " li").each(function (i, nth) {
          z_disArr.push(Math.abs(($(this).offset().left + li_width * 0.5 - win_with * 0.5)));
      })
      z_minDis = z_disArr[0];
      for (var n = 0; n < z_disArr.length; n++) {
          if (z_minDis > z_disArr[n]) {
              z_minDis = z_disArr[n];
          }
      }
      z_minIndex = z_disArr.indexOf(z_minDis);
      $("#TV_module-ul_" + id + " li").eq(z_minIndex).css({ "z-index": "999", "border-left": "10px solid #fff", "border-right": "10px solid #fff" }).siblings().css({ "z-index": "9", "border": "none" });
      $("#TV_module-cont").css({ "background": "rgba(0,0,0,.6)" });
      $("#TV_module-cont").eq(z_minIndex).css({ "background": "rgba(0,0,0,.5)" });
  }, 20)

  obj.addEventListener('touchstart', function (ev) {
      Page_Index.IsCanLeftRight = false;
      var oTouch = ev.targetTouches[ev.targetTouches.length - 1];
      var move_start = oTouch.pageX;
      var disX = oTouch.pageX - obj.offsetLeft;

      var id = oTouch.identifier;
      function move(ev) {
          for (var i = 0; i < ev.targetTouches.length; i++) {
              if (ev.targetTouches[i].identifier == id) {
                  move_width = ev.targetTouches[i].pageX - move_start;
                  obj.style.left = ev.targetTouches[i].pageX - disX + 'px';
              }
          }
          //算速度
          speedX = obj.offsetLeft - lastX;
          num_speedX = Math.round(speedX / 8);
          lastX = obj.offsetLeft;
      }
      function end(ev) {
          Page_Index.IsCanLeftRight = true;

          for (var i = 0; i < ev.changedTouches.length; i++) {
              if (id == ev.changedTouches[i].identifier) {
                  obj.removeEventListener('touchmove', move, false);
                  obj.removeEventListener('touchend', end, false);
              }
          }

          //手指离开屏幕后
          var disArr = [];
          var minDis = 0;
          var minIndex = 0;
          if (num_speedX == 0) {

              //$("#TV_module-ul_" + id + " li").each(function (i, nth) {
              $("#" + ev.currentTarget.id + " li").each(function (i, nth) {
                  disArr.push(Math.abs(($(this).offset().left + li_width * 0.5 - win_with * 0.5)));
              })
              minDis = disArr[0];
              for (var n = 0; n < disArr.length; n++) {
                  if (minDis > disArr[n]) {
                      minDis = disArr[n];
                  }
              }
              minIndex = disArr.indexOf(minDis);
              $(obj).stop(true).animate(
                  { "left": -(minIndex) * li_width + 'px' },
                  {
                      easing: 'easeOutCirc',
                      duration: 500
                  }
              );
          } else {
              if (obj.offsetLeft - move_width + num_speedX * li_width > 100) {
                  $(obj).stop(true).animate(
                      { "left": 0 },
                      {
                          easing: 'easeOutCirc',
                          duration: 500
                      }
                  );
              } else if (obj.offsetLeft - move_width + num_speedX * li_width < $(window).outerWidth() - $(obj).width()) {
                  $(obj).stop(true).animate(
                      { "left": $(window).width() - $(obj).outerWidth() + 'px' },
                      {
                          easing: 'easeOutCirc',
                          duration: 500
                      }
                  );
              } else {
                  $(obj).stop(true).animate(
                      { "left": obj.offsetLeft - move_width + num_speedX * li_width + 'px' },
                      {
                          easing: 'easeOutCirc',
                          duration: 500
                      }
                  );
              }
          }


      }
      obj.addEventListener('touchmove', move, { passive: true });
      obj.addEventListener('touchend', end, { passive: true });
      //住址绑定事件
      //ev.preventDefault();
  }, { passive: true });
}

//5.1.2 新视频播放模板，滑动播放  开始

var allstop = 0;

function new_tv1() {
  obj = document.getElementById("bodycontent");

  obj.addEventListener('touchstart', function (ev) {

      function move(ev) {

      }

      function end(ev) {
          var scrollTop = $(document).scrollTop();
          allstop = 0;
          $.each(Page_Index.newvideoID, function (i, n) {
              PlayOrStop(n, scrollTop);
          });

          //全部视频都没有在播放位置
          if (allstop >= Page_Index.newvideoID.length) {
              $("#myvideo_temp").attr("src", "");

          }
          allstop = 0;
      }
      obj.addEventListener('touchmove', move, { passive: true });
      obj.addEventListener('touchend', end, { passive: true });

  }, { passive: true });
}

/*安卓中点击播放*/
function videoplay_az(id) {
  //alert(id)
  $("#myvideo" + id)[0].play();
  $("#video_play_i_az" + id).css({ 'background-image': 'url(img/bf_03_wait.gif)' });
  //判断是否开始播放
  //videoplay_az_wait(id)

  //$("#video_play_i_az"+id).hide();
  $("#myvideo" + id)[0].addEventListener('canplaythrough', function () {
      $("#video_play_i_az" + id).hide();
  });

}
function videoplay_az_wait(id) {
  setTimeout(function () {
      var ttem = $("#myvideo" + id)[0];
      if (!ttem.paused) {
          $("#video_play_i_az" + id).hide();
      }
      else {
          videoplay_az_wait(id);
      }
  }
	  , 100);

}


function PlayOrStop(id, scrollTop) {
  var obj_play = Page_Index.newvideoMode[id - 1];

  if (typeof (obj_play) != "undefined" && typeof ($("#div_commonAD" + id).offset()) != "undefined" && typeof ($(".common-ad-new" + id)) != "undefined") {
      var atv_height1 = $(".common-ad-new").height() / 2;
      var atvtop_b = $("#div_commonAD" + id).offset().top - atv_height1;
      var atvtop_e = $("#div_commonAD" + id).offset().top + atv_height1;
      if (scrollTop > atvtop_b && scrollTop < atvtop_e) {
          //判断是否有视频连接
          var vvvv = "";
          if (obj_play.videoLink == "") {
              //无视频不操作

          }
          else {
              //安卓对canvas的支持不太好，而且不同机型对video的展现效果也不同，暂时去掉视频播放
              if (isAndroid) {

                  //vvvv = "<div id=\"atv" + id + "\" class=\"video\"><span><div id=\"video_play_i_az" + id + "\" class=\"fa fa-play-circle-o\" onclick=\"videoplay_az(" + id + ")\" style=\" font-size: initial;\"></div></span><video class=\"video_sy\" id=\"myvideo" + id + "\" width=\"100%\" style=\"object-fit:fill;\" webkit-playsinline=\"true\" x-webkit-airplay=\"true\" playsinline=\"true\" x5-video-orientation=\"h5\" x5-video-player-type=\"h5\" x5-video-player-fullscreen=\"false\" preload=\"auto\"  autobuffer poster=\"" + obj_play.productInfo.mainpicUrl + "\" loop  src=\"" + obj_play.videoLink + "\" ></video></div>";
                  playvideoid = id;
              }
              else {
                  //ios特殊处理
                  vvvv = '<div id="atv" class="video"><span><div id="video_play_i_' + id + '" class="fa fa-play-circle-o" style=" font-size: initial;"></div></span>'
                      + '<canvas id="canvas_' + id + '" class="shipinios"></canvas></div>'



                  $("#ssdd").html('<video id="myvideo_temp" width="100%" style="object-fit:fill" webkit-playsinline="true" x-webkit-airplay="true" playsinline="true" x5-video-orientation="h5" x5-video-player-type="h5" x5-video-player-fullscreen="false" preload="auto" controls="controls" autobuffer="" '
                  + 'src="' + obj_play.videoLink + '" loop></video>')
              }

              if (vvvv != "") {
                  $("#div_commonAD" + id).html(vvvv);
              }

              if (isiOS) {
                  //监听点击播放事件
                  $("#video_play_i_" + id).click(function () {
                      //显示加载中
                      $("#video_play_i_" + id).css({ 'background-image': 'url(img/bf_03_wait.gif)' });
                      //alert(id)
                      video = $("#myvideo_temp");
                      //if(isAndroid){
                      animate();//在这里调用。
                      if (!video.paused) {//判断视频时候暂停。
                          //video.pause();
                      } else {
                          video.play();
                          //$("#video_play_i_"+id).hide();
                      }
                      //}
                  });

                  /*if(isAndroid){
                  setTimeout(function () {
                         video.addEventListener('play', function() {
                      animate();//在这里调用。
                  }, false);
                  
                  video.play();
                  animate();//在这里调用。
                        },200);
                  }*/





                  playvideoid = id;
                  setTimeout(function () {
                      video = $("#myvideo_temp");
                      canvas = document.getElementById('canvas_' + playvideoid);
                      context = canvas.getContext('2d');
                      context.fillStyle = '#fff';
                      context.fillRect(0, 0, canvas.width, canvas.height);//绘制1920*1080像素的已填充矩形。
                      var img = new Image();//新建一个图片，模仿video里面的poster属性。
                      img.src = obj_play.productInfo.mainpicUrl;
                      context.drawImage(img, 0, 0, canvas.width, canvas.height);//将图片绘制进canvas。

                      video[0].play();
                      animate();//在这里调用。


                  }
                        , 100);

                  //监听视频播放后，隐藏加载
                  $("#myvideo_temp")[0].addEventListener('canplaythrough', function () {
                      $("#video_play_i_" + playvideoid).hide();
                  });

              }
          }

      } else {
          allstop++;



          var temp_caozuo = Page_Index.GetLocationByShowmoreLinktype(g_const_showmoreLinktype.ProductDetail, obj_play.productInfo.productCode);

          var temp_vide = '';
          if (typeof (obj_play.productInfo.videoAd) != "undefined" && obj_play.productInfo.videoAd != "") {
              var temp_vide = '<a title="" class="atv" id="atvimg' + id + '" ' + temp_caozuo + '><div class="v_m_bg"></div><img src="' + obj_play.productInfo.mainpicUrl + '" request-url="" alt=""></a>';
              

              if (isAndroid) {
                  temp_vide += '<p class="hdy_az">' + obj_play.productInfo.videoAd + '</p>';
                  temp_vide += '<p class="hdy_az_2">' + obj_play.productInfo.productName + '</p>';
                  temp_vide += '<p class="hdy_az_3">￥' + obj_play.productInfo.sellPrice;
                  if (obj_play.productInfo.isActivity != "") {
                      temp_vide += '<span class="hd_az_3">' + obj_play.productInfo.isActivity + '</span>';
                  }
                  temp_vide += '</p>';
              }
              else {
                  temp_vide += '<p class="hdy">' + obj_play.productInfo.videoAd + '</p>';
              }

          }
          else {
              var temp_vide = '<a title="" class="atv" id="atvimg' + id + '" ' + temp_caozuo + '><img src="' + obj_play.productInfo.mainpicUrl + '" request-url="" alt=""></a>';
          }
          //$("#div_commonAD" + id).attr("class","common-ad-new")
          $("#div_commonAD" + id).html(temp_vide);
      }
  }

}

//渲染方法。
function animate(id) {
  canvas = document.getElementById('canvas_' + playvideoid);
  video = document.getElementById('myvideo_temp');
  try {
      if (!video.paused) {
          //$("#video_play_i_"+playvideoid).hide();
      }
      else {
          //video.pause();

      }
      //alert(context)
      if (canvas != null) {
          context.drawImage(video, 0, 0, canvas.width, canvas.height);//将视频当中的一帧绘制到canvas当中。			
          videoanimate = requestAnimationFrame(animate);//每秒60帧渲染页面。关于此方法具体解释请自行百度。
      }
  } catch (e) {
      //alert(e)
      $("#myvideo_temp").attr("src", "");

  }
}

var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

function videoAutoPlay(id) {
  var video = document.getElementById(id);
  if (video != null) {
      video.play();
      document.addEventListener("WeixinJSBridgeReady", function () {
          if (video.paused || video.ended) {
              video.play();
          }

      }, false);
      document.addEventListener('YixinJSBridgeReady', function () {
          if (video.paused || video.ended) {
              video.play();
          }

      }, false);
      //video.onended = function() { //播放结束
      //	$(".video_play").show();
      //};

      if (typeof (wx) != "undefined") {
          wx.ready(function () {//微信内，必须需要等到wx jsapi加载完成之后才能执行播放视频的动作
              //document.getElementById('video').play();
              //为防止开播失败，尝试在8s内不断请求开播
              //var video=document.getElementById("video");
              var _now_time = Date.now();

              var play_interval = setInterval(function () {
                  //var _new_time = Date.now();
                  //if(_new_time - _now_time < 8000 && video.played.length == 1){
                  //document.getElementById('video').play();
                  if (video != null) {
                      if (video.paused || video.ended) {
                          video.play();
                      }

                  }
                  else {
                      video = document.getElementById(id);
                      if (video != null) {
                          if (video.paused || video.ended) {
                              video.play();
                          }

                      }

                  }
                  clearInterval(play_interval);

                  //}
                  //if(_new_time - _now_time >= 8000){
                  //clearInterval(play_interval);
                  //}
              }, 200);
          })
      }
  }
}

//5.1.2 新视频播放模板，滑动播放  结束

var myscroll = null;
var pressX = 0, pressY = 0;

$(document).ready(function () {
  if (GetQueryString("start") != "") {
      if (localStorage["indexChannel_Code"] != null) {
          //清空
          localStorage["indexChannel_Code"] = "";
          localStorage["indexChannel_Name"] = "";
      }
  }

  //来源于二维码的，直接跳转到相关叶签显示
  if (GetQueryString("tgcode") != "") {
      localStorage["indexChannel_Code"] = "NAV170703100001"//GetQueryString("tgcode");
      localStorage["indexChannel_Name"] = "惠头条"//unescape(GetQueryString("tgname"));
  }



  //禁止浏览器的下拉  --开始
  document.body.addEventListener('touchmove', function (e) {
      var touch = event.targetTouches[0];
      var spanX = touch.pageX - pressX;
      var spanY = touch.pageY - pressY;
      var direct = "none";
      if (Math.abs(spanX) > Math.abs(spanY)) {

      } else {
          //垂直方向

          //在顶部时，下拉禁止浏览器移动
          if (spanY > 0) {

              //direct = "down";//向下
              if ($(document).scrollTop() <= 0) {
                  //禁止操作
                  e.preventDefault();
              }
              //下拉层显示时，整体不能下滑
              if (!$("#div_scrolldown").is(":hidden")) {
                  //禁止操作
                  e.preventDefault();
              }
              //console.log($(document).scrollTop());
              //console.log($(window).scrollTop());
          } else {
              //alert($(window).scrollTop())
              //direct = "up";//向上
          }

          ////在可左右滑动模块中时，禁止垂直移动
          //if (typeof (Page_Index.IsCanLeftRight) != "undefined") {
          //    if (Page_Index.IsCanLeftRight == false) {
          //        //禁止操作
          //        // 判断默认行为是否可以被禁用
          //        if (e.cancelable) {
          //            // 判断默认行为是否已经被禁用
          //            if (!e.defaultPrevented) {
          //                e.preventDefault();
          //            }
          //        }
          //    }
          //}

      }

  }, { passive: false });
  document.body.addEventListener('touchstart', function (event) {
      var touch = event.targetTouches[0];
      pressX = touch.pageX;
      pressY = touch.pageY;
  }, { passive: false });
  //禁止浏览器的下拉  --结束


  //绑定页面关闭时，调用提交买点离开页面数据
  $(window).bind('beforeunload', function () {
      if (typeof (gas) != "undefined") {
          gas.submit_InOrOut('out');
      }
      $(window).unbind('beforeunload');//在不需要时解除绑定

  });

  //微信中显示“扫描”
  if (IsInWeiXin.check()) {

      if (IsInWxMiniProgram()) {
          $("#img_scan").hide();
          $("#img_scan").off("click");
      }
      else {
          $("#img_scan").show();
          $("#img_scan").on("click", function () {
              WeChat.ScanQRCode();
          });
      }
  }
  else {
      $("#img_scan").hide();
      $("#img_scan").off("click");
  }

  //下拉重新加载和左右横划
  //ScrollReload.Listen("bodycontent", "div_scrolldown", "IndexChannelContent", "6", Page_Index.ScollDownCallBack, "30", Page_Index.ScollDownCallBack_Left, Page_Index.ScollDownCallBack_Right);
  ScrollReload.Listen("pydiv", "div_scrolldown", "IndexChannelContent", "6", Page_Index.ScollDownCallBack, "70", Page_Index.ScollDownCallBack_Left, Page_Index.ScollDownCallBack_Right);

  ////判断来源（不是本站的，且明确表示显示第一个页签）
  //if (localStorage["showfirst"] != null && localStorage["showfirst"] != "0") {
  //    localStorage["showfirst"] = "0";
  //    var cType = GetClientType();
  //}



});


var iszhixing = true;
var Page_Index = {
    /*是否显示下一个页签的内容*/
    isShowNext: false,
    /*首页是否可以左右移动*/
    IsCanLeftRight: true,
    /*接口名称*/
    api_target: "../../../jsonapi/com_cmall_familyhas_api_ApiHomeColumn",
    /*输入参数*/
    api_input: { "sysDateTime": new Date().Format("yyyy-MM-dd hh:mm:ss"), "buyerType": "", "maxWidth": "", "version": 1.0, "viewType": g_const_viewType.WXSHOP },
    /*接口响应对象*/
    api_response: {},
    
    /*接收链接参数*/
    TP:"",
    time_point:"",
    column_name:"",
    column_type:"",
    nav_name:"",
    nav_name:"",
    count:"0",
    /*初始化*/
    "Init": function () {
        //清除浏览路径
        PageUrlConfig.Clear();
        PageUrlConfig.SetUrl();
        $("#search50_img").on("click", function (e) {
            PageUrlConfig.SetUrl();
            window.location.href = g_const_PageURL.Search + "?t=" + Math.random();;//"search.html";
        });
        $("#search50_txt").on("click", function (e) {
            PageUrlConfig.SetUrl();
            window.location.href = g_const_PageURL.Search + "?t=" + Math.random();;//"search.html";
        });
        $("#search50_back").on("click", function (e) {
            PageUrlConfig.SetUrl();
            window.location.href = g_const_PageURL.Search + "?t=" + Math.random();;//"search.html";
        });
        $(".search50_logo").on("click", function (e) {
            PageUrlConfig.SetUrl();
            window.location.href = g_const_PageURL.Search + "?t=" + Math.random();;//"search.html";
        });

        //$(".header .hd-classify").on("click", function (e) {
        //    PageUrlConfig.SetUrl();
        //    window.location.href = g_const_PageURL.Category + "?t=" + Math.random();
        //});
        //$(".header .user-content").on("click", function (e) {
        //    PageUrlConfig.SetUrl();
        //    window.location.href = g_const_PageURL.AccountIndex + "?t=" + Math.random();//"Account/index.html";
        //});
        $(".app-close").on("click", function (e) {
            $(e.target).parent().css("display", "none");
        });

        //Page_Index.GetCartCount();
       

      
        //396 H5弹层  开始
        //Action396H5.Show();

        $("#h5close").on("click", function (e) {
            Action396H5.CloseDIV();
        });
        //396 H5弹层  结束

    },
    
    up_urlparam: function (key,sUrl) {
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
    },
    
    /*获取购物车的中商品的数量【已转移到FooterMenu.js中】*/
    "GetCartCount": function () {
        $(".shop-cart").empty();
        var sCart = localStorage[g_const_localStorage.Cart];
        var objCart = null;
        if (typeof (sCart) != "undefined") {
            objCart = JSON.parse(sCart);
        }
        var icount = 0;
        var scount = ""
        if (objCart != null) {
            for (var i = 0; i < objCart.GoodsInfoForAdd.length; i++) {
                var GoodsInfo = objCart.GoodsInfoForAdd[i];
                icount += GoodsInfo.sku_num;
            }
        }
        if (icount > 99)
            scount = "99+";
        else
            scount = icount.toString();
        var html = "";
        if (icount != 0)
            html = "<em>" + scount + "</em>";
        $(".shop-cart").append(html);
    },

    /*获取数据，从接口获取*/
    "LoadData": function () {
        var s_api_input = JSON.stringify(Page_Index.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": Page_Index.api_target };
        var purl = g_APIUTL;
        //g_APIMethod = "get";
        //purl = "/JYH/index1.txt";
//        var request4 = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });

//        request4.done(function (msg) {
//            //$("#pageloading").css("display", "none");
//            Page_Index.api_response = msg;
//            if (msg.resultCode == g_const_Success_Code) {
//                $("#bodycontent").html("");
//                //Page_Index.LoadTop3();//作废，合并到LoadOther中
//                Page_Index.LoadOther();
//                //参你喜欢分类【4.0.6后增加，5.0后作废】
//                //Page_Index.GuessYourLikeFenLei();
//                //原有调用百分点接口
//                //Page_Index.LoadGuessYourLikeData();
//                //5.2.4 先获得MemberCode的AES加密串后调用百分点接口
//                Page_Index.Load_MemberCode();
//            }
//            else {
//                ShowMesaage(msg.resultMessage);
//            }
//        });
//
//        request4.fail(function (jqXHR, textStatus) {
//            //$("#pageloading").css("display", "none");
//            ShowMesaage(g_const_API_Message["7001"]);
//        });
    },

    GetLocationByShowmoreLinktype: function (t, u, dgRequestId) {
        //PageUrlConfig.SetUrl();
        return g_GetLocationByShowmoreLinktype(t, u, dgRequestId);
    },
	/*5.4.0 存放视频ID*/
	videoIDs:[],
    /*读取其它模板*/
    LoadOther: function () {

        var otherlist = Page_Index.api_response.columnList;
        var stpl = "";//页面模板内容
        var data = {};//渲染模板时的对象
        var html = "";
        var top3Div = []
		//5.4.0
		Page_Index.videoIDs=[];

        for (var i = 0; i < otherlist.length; i++) {
            var other = otherlist[i];
            var showBottomSpace = "";
            try {
                if (!(typeof (other.hasEdgeDistance) == "undefined")) {
                    if (other.hasEdgeDistance == g_const_Index_hasEdgeDistance.No) {
                        //隐藏导航栏上方间距
                        //$("#div_menubottomfenge").hide();
                        showBottomSpace = " style=\"display:none;\"";
                    }
                }
            }
            catch (e) {

            }

            //第一个模块时隐藏上部间隔
            if (i == 0) {
                showBottomSpace = " style=\"display:none;\"";
            }
            switch (other.columnType) {
                //新增URL类型模板
                case g_const_columnType.UrlMoBan:
                    //清除记录标签位置
                    localStorage["indexChannel_Code"] = "";
                    localStorage["indexChannel_Name"] = "";
                    ////判断来源（不是本站的，显示相邻的页签）

                    localStorage["showfirst"] = "1";

                    if (other.showmoreLinkvalue.indexOf('?') == -1)
                        window.location.href = other.showmoreLinkvalue + "?t=" + Math.random();
                    else
                        window.location.href = other.showmoreLinkvalue + "&t=" + Math.random();

                    break;
                    //4.1.2  top3内容合并到otherlist中，便于灵活配置 修改开始
                    //轮播
                case g_const_columnType.swipeSlide:
                    var swipeNum = "";
                    html = "";
                    stpl = $("#tpl_swipeSlide_li")[0].innerHTML;
                    if (other.contentList.length > 0) {
                        for (var j = 0; j < other.contentList.length; j++) {
                            var other_content = other.contentList[j];
                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                            if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                                /*396 h5弹窗类型*/
                                //temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                            }
                            else {
                                if (other_content.isShare == g_const_isShowmore.YES) {
                                    temp_asdfgghhh += "&wx_st=" + encodeURIComponent(other_content.shareTitle);
                                    temp_asdfgghhh += "&wx_sc=" + encodeURIComponent(other_content.shareContent);
                                    temp_asdfgghhh += "&wx_si=" + encodeURIComponent(other_content.sharePic);
                                }
                                //temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                            }

                            data = {
                                "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                                "caozuo": temp_asdfgghhh,
                                "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/index.png"),

                            };
                            html += renderTemplate(stpl, data);

                            swipeNum += '<a href="javascript:;"></a>';
                        }
                    }

                    stpl = $("#tpl_swipeSlide")[0].innerHTML;
                    data = {
                        "swipeSlideList": html,
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);

                    $("#bodycontent").append(html);

                    break;
                    //广告
                case g_const_columnType.TwoADs:
                    html = "";
                    stpl = $("#tpl_AD_li")[0].innerHTML;
                    if (other.contentList.length > 0) {
                        for (var j = 0; j < other.contentList.length; j++) {
                            var other_content = other.contentList[j];
                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                            //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                            //    /*396 h5弹窗类型*/
                            //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                            //}
                            //else {
                            //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                            //}

                            data = {
                                "id": (i + 1).toString(),
                                "xh": (j + 1).toString(),
                                "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                                "caozuo": temp_asdfgghhh,
                                "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/index.png"),

                            };
                            html += renderTemplate(stpl, data);
                        }
                    }

                    stpl = $("#tpl_AD")[0].innerHTML;
                    data = {
                        "id":(i+1).toString(),
                        "ADList": html,
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);

                    $("#bodycontent").append(html);
                    top3Div.push("div_TwoADs");

                    break;
                    //导航
                case g_const_columnType.Navigation:
                    /*4.1.5
                    html = "";
                    stpl = $("#tpl_Navigation_li")[0].innerHTML;
                    if (other.contentList.length > 0) {
                        for (var j = 0; j < other.contentList.length; j++) {
                            var other_content = other.contentList[j];
                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                            if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                                //396 h5弹窗类型
                                temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                            }
                            else {
                                temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                            }

                            data = {
                                "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType,other.columnName,Page_Index.indexChannel_Name,(j + 1).toString()),
                                "caozuo": temp_asdfgghhh,
                                "picture": g_GetPictrue(other_content.picture),
                                "title": other_content.title,
                                "stylestr": other_content.titleColor != undefined ? " style='color:" + other_content.titleColor + "'" : ""
                            };
                            html += renderTemplate(stpl, data);
                        }
                    }

                    stpl = $("#tpl_Navigation")[0].innerHTML;

                    //导航栏背景图--开始
                    var showStyle = "";
                    if (!(other.columnBgpic == undefined)) {
                        if (other.columnBgpic.picNewUrl != "") {
                            showStyle = " style=\"background:url('" + other.columnBgpic.picNewUrl + "') no-repeat;background-size:100% 100%;\"";
                        }
                    }

                    data = {
                        "showStyle": showStyle,
                        "NavigationList": html,
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);

                    $("#bodycontent").append(html);
                */

                    /*5.0 支持多个*/
                    html = "";
                    stpl = $("#tpl_Navigation_li_50")[0].innerHTML;
                    if (other.contentList.length > 0) {
                        //计算单个li的宽度(屏宽 除 一行按钮数目)
                        var ajnum = 4;
                        if (other.columnsPerRow == g_const_ColumnsPerRow.five) {
                            ajnum = 5;
                        }
                        var li_width = parseFloat(100) / parseFloat(ajnum);
                        for (var j = 0; j < other.contentList.length; j++) {
                            var other_content = other.contentList[j];
                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                            //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                            //    /*396 h5弹窗类型*/
                            //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                            //}
                            //else {
                            //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                            //}

                            data = {
                                "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name),
                                "caozuo": temp_asdfgghhh,
                                "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/comment.png"),

                                "title": other_content.title,
                                "stylestr": typeof (other_content.titleColor) != "undefined" ? " style='color:" + other_content.titleColor + "'" : "",
                                "li_width": " style=\"width:" + li_width + "%\""
                            };
                            html += renderTemplate(stpl, data);
                        }
                    }

                    stpl = $("#tpl_Navigation_50")[0].innerHTML;

                    //导航栏背景图--开始
                    var showStyle = "";
                    if (!(typeof (other.columnBgpic) == "undefined")) {
                        if (other.columnBgpic.picNewUrl != "") {
                            showStyle = " style=\"background:url('" + other.columnBgpic.picNewUrl + "') no-repeat;background-size:100% 100%;\"";
                        }
                    }

                    data = {
                        "showStyle": showStyle,
                        "NavigationList": html,
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);

                    $("#bodycontent").append(html);
                    break;
                    //============4.1.2 修改结束=================================================




                    //闪购(限时抢购)
                case g_const_columnType.FastBuy:
                    top3Div.push("article_fastbuy");

                    //4.15版闪购
                    Page_Index.FastBuy = other;
                    html = "";

                    stpl = $("#tpl_fastbuy_productList")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}
                        var allwidth = $(window).width() - other.contentList.length * 2 * 8
                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/comment.png"),
                            "discount": other_content.productInfo.discount,
                            "isShowDiscount": other_content.productInfo.discount == "" ? " style=\"display:none;\"" : "",
                            "productName": FormatText(other_content.productInfo.productName, 9),
                            "sellPrice": other_content.productInfo.sellPrice,
                            "imgSize": " style=\"width:" + 0.33 * allwidth + "px;height:" + 0.33 * allwidth + "px\""
                        };
                        html += renderTemplate(stpl, data);
                    }

                    stpl = $("#tpl_fastbuy")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}

                    data = {
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "showmoreTitle": other.showmoreTitle,
                        "CountDown": '<i>距离结束</i><em class=""></em><b>:</b><em class=""></em><b>:</b><em class=""></em>',
                        "productList": html,
                        "showBottomSpace": showBottomSpace,
                        "xh": i.toString()
                    };
                    html = renderTemplate(stpl, data);

                    $("#bodycontent").append(html);
                    Page_Index.flagCheapInterval = self.setInterval("Page_Index.ShowLeftTime('" + i.toString() + "');", g_const_seconds / 10);
                    //<i>距离结束</i><em class="">15</em><b>:</b><em class="">20</em><b>.</b><em class="">30</em>

                    break;
                    //通屏广告
                case g_const_columnType.CommonAD:
                    top3Div.push("div_commonAD");

                    var commonAD = other.contentList[0];
                    //var showBottomSpace = "";
                    //try{
                    //    if (!(other.hasEdgeDistance == undefined)) {
                    //        if (other.hasEdgeDistance == g_const_Index_hasEdgeDistance.No && i == 0) {
                    //            //隐藏导航栏上方间距
                    //            //$("#div_menubottomfenge").hide();
                    //            showBottomSpace=" style=\"display:none;\"";
                    //        }
                    //    }
                    //}
                    //catch(e){

                    //}
                    stpl = $("#tpl_commonAD")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(commonAD.showmoreLinktype, commonAD.showmoreLinkvalue);
                    //if (commonAD.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || commonAD.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}

                    data = {
                        "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), commonAD.showmoreLinktype, commonAD.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, "1"),
                        "showmoreLink": temp_asdfgghhh,
                        "picture": g_GetPictrue(commonAD.picture, "../../resources/cfamily/zzz_js/index.png"),
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    //一栏推荐
                case g_const_columnType.RecommendONE:
                    top3Div.push("article_recommendone");

                    stpl = $("#tpl_recommendone")[0].innerHTML;

                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}

                    var temp_asdfgghhh_1 = Page_Index.GetLocationByShowmoreLinktype(other.contentList[0].showmoreLinktype, other.contentList[0].showmoreLinkvalue);
                    //if (other.contentList[0].showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.contentList[0].showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh_1 = 'onclick="' + temp_asdfgghhh_1 + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh_1 = 'href="' + temp_asdfgghhh_1 + '"';
                    //}

                    data = {
                        "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other.showmoreLinktype, other.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, "1"),
                        "histyle": (Page_Index.showmoreTitle(other) == "" || Page_Index.showmoreTitle(other) == "&nbsp;") ? "display:none" : "",
                        "columnName": other.showName == g_const_isShowmore.YES ? other.columnName : "",
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "classmore": Page_Index.classmore(other),
                        "pshowmoreLink": temp_asdfgghhh_1,
                        "titleColor": "color:" + other.contentList[0].titleColor,
                        "title": FormatText(other.contentList[0].title, 6),
                        "descriptionColor": "color:" + other.contentList[0].descriptionColor,
                        "description": FormatText(other.contentList[0].description, 10),
                        "picture": g_GetPictrue(other.contentList[0].picture, "../../resources/cfamily/zzz_js/index.png"),
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    //左右两栏
                case g_const_columnType.RecommendRightTwo:
                case g_const_columnType.RecommendLeftTwo:
                    top3Div.push("article_recommend_leftorright_two");

                    html = "";
                    stpl = $("#tpl_recommend_leftorright_two_product")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        data = {
                            "id": (i + 1).toString(),
                            "xh": (j + 1).toString(),
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/comment.png"),
                            "titleColor": "color:" + other_content.titleColor,
                            "title": FormatText(other_content.title, 5),
                            "descriptionColor": "color:" + other_content.descriptionColor,
                            "description": FormatText(other_content.description, 9)
                        };
                        html += renderTemplate(stpl, data);
                    }

                    stpl = $("#tpl_recommend_leftorright_two")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}


                    data = {
                        "id": (i + 1).toString(),
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "classcolumn": g_const_columnType.RecommendLeftTwo == other.columnType ? "column-left" : "column-right",
                        "productList": html,
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    //商品推荐
                case g_const_columnType.RecommendProduct:
                    top3Div.push("div_recommend_product_5");

                    /*4.1.5
                    html = "";
                    stpl = $("#tpl_recommend_product_product")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                            //396 h5弹窗类型
                            temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        }
                        else {
                            temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        }

                        data = {
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType,other.columnName,Page_Index.indexChannel_Name,(j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.productInfo.mainpicUrl),
                            "productName": FormatText(other_content.productInfo.productName, 10),
                            "sellPrice": other_content.productInfo.sellPrice,
                            
                        };
                        html += renderTemplate(stpl, data);
                    }

                    stpl = $("#tpl_recommend_product")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5) {
                        //396 h5弹窗类型
                        temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    }
                    else {
                        temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    }

                    data = {
                        "columnName": other.columnName,
                        "showStyle": other.columnName == "" ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "touchwrapid": "touchwrapid_" + i.toString(),
                        "showBottomSpace": showBottomSpace
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    Page_Index.touchInit(data.touchwrapid);
                    */

                    /*5.0 支持横划*/
                    html = "";
                    stpl = $("#tpl_recommend_product_product_5")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" style=\"width: 28%;margin-top: 0.01rem;margin-right: 2px;\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.productInfo.mainpicUrl, "../../resources/cfamily/zzz_js/comment.png"),
                            "productName": FormatText(other_content.productInfo.productName, 10),
                            "sellPrice": other_content.productInfo.sellPrice,
                            "imgSize": " style=\"width:" + 0.28 * $(window).width() + "px;height:" + 0.28 * $(window).width() + "px\""
                        };
                        html += renderTemplate(stpl, data);
                    }

                    var toMoreUrl = "";
                    stpl = $("#tpl_recommend_product_5")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    toMoreUrl = temp_asdfgghhh;
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';

                    //}
                    //else {
                    //    toMoreUrl = temp_asdfgghhh;
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}
                    var divid = "recommend_product_" + (new Date()).valueOf() + "_" + Math.floor(9999999);
                    data = {
                        "divid": divid,
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "touchwrapid": "touchwrapid_" + i.toString(),
                        "showBottomSpace": showBottomSpace,
                        "showMoreLast": other.showmoreLinkvalue == "" ? ";display:none;" : (other.contentList.length < 6 ? ";display:none;" : ""),
                        "showMoreTop": other.showmoreLinkvalue == "" ? " style=\"display:none;\"" : ""
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);

                    //===========横划设定  开始=======================
                    //计算li/ul宽度
                    var oUL_width = 0;
                    $("#module-lookMore_" + divid + " li").width(0.28 * $(window).width());
                    $("#module-lookMore_" + divid + " li:last-child").css("width", "1rem");
                    $("#module-lookMore_" + divid + " li").each(function (i, n) {
                        oUL_width += $(this).outerWidth();
                    })
                    oUL_width += 2;
                    if (other.showmoreLinkvalue == "" || other.contentList.length < 6) {
                        $("#module-lookMore_" + divid + " ul").width(oUL_width - $("#module-lookMore_" + divid + " li:last-child").width());
                    }
                    else {
                        $("#module-lookMore_" + divid + " ul").width(oUL_width);
                    }
                    //横滑
                    var myscroll = new IScroll("#module-lookMore_" + divid, {
                        scrollX: true
                        , scrollY: false
                        , mouseWheel: true
                        , preventDefault: false
                    });
                    //
                    $("#module-lookMore_" + divid)[0].addEventListener('touchstart', function (ev) {
                        Page_Index.IsCanLeftRight = false;
                    }, { passive: false });
                    $("#module-lookMore_" + divid)[0].addEventListener('touchend', function (ev) {
                        Page_Index.IsCanLeftRight = true;

                    }, { passive: false });
                    //最右侧 拖动跳转[不实现了]
                    //var module_timer = null;
                    //var arr = [];
                    //var tmp = '';
                    //var offset_right = 0;
                    //clearInterval(module_timer);
                    //module_timer = setTimeout("Page_Index.ScollLastGoTo('module-lookMore_" + divid + "','" + toMoreUrl + "')", 20);
                    //===========横划设定  结束=======================
                    break;
                    //两栏多行推荐(热门市场)
                case g_const_columnType.RecommendHot:
                    top3Div.push("div_RecommendHot");

                    html = "";
                    stpl = $("#tpl_RecommendHot_product")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" style=\"width: 30%;margin-top: 0.01rem;margin-right: 2px;\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/index.png"),

                            "titleColor": "color:" + other_content.titleColor,
                            "title": FormatText(other_content.title, 5),
                            "descriptionColor": "color:" + other_content.descriptionColor,
                            "description": FormatText(other_content.description, 9),
                            "imgSize": ""
                        };
                        html += renderTemplate(stpl, data);
                    }
                    stpl = $("#tpl_RecommendHot")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}
                    data = {
                        "showBottomSpace": showBottomSpace,
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "productList": html
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    //TV直播
                case g_const_columnType.TVLive:
                    top3Div.push("index_tvzb");
                    //395单个TV
                    html = "";
                    var numhtml = "";
                    var numstpl = "";
                    //<i class="f18">{{sellPrice}}</i>.<i class="f14">10</i>
                    stpl = $("#tpl_tvlive_product")[0].innerHTML;
                    numstpl = $("#tpl_tvlive_num")[0].innerHTML;

                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}

                    for (var j = 0; j < 1; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh_1 = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" style=\"width: 10%;margin-top: 0.01rem;margin-right: 2px;\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh_1,
                            "picture": g_GetPictrue(other_content.picUrl, "/img/default/tvShow.png"),

                            "productName": FormatText(other_content.productInfo.productName, 25),
                            "sellPrice": function (sellPrice) {
                                var arrmoney = sellPrice.split(".");
                                if (arrmoney.length = 1)
                                    return '<i class="f18">' + sellPrice + '</i>';
                                else if (arrmoney.length = 2)
                                    return '<i class="f18">' + arrmoney[0] + '</i>.<i class="f14">' + arrmoney[1] + '</i>';
                                else
                                    return '<i class="f18">' + sellPrice + '</i>';

                            }(other_content.productInfo.sellPrice),
                            "startTime": Date.Parse(other_content.startTime).Format("hh:mm"),
                            "endTime": Date.Parse(other_content.endTime).Format("hh:mm")
                        };
                        html += renderTemplate(stpl, data);
                        data = {
                            "classcurr": "class=\"\""
                        };
                        numhtml += renderTemplate(numstpl, data);
                    }
                    if (other.contentList.length <= 1) {
                        numhtml = "&nbsp;"
                    }
                    stpl = $("#tpl_tvlive")[0].innerHTML;
                    data = {
                        "columnName": other.columnName,
                        "showmoreLink": "/tvlive.html?futureProgram=" + other.futureProgram,//Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue),
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "NumList": numhtml
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    /*重置li的高度
                   var sumHeight = 0;
                   $("#index_tvzb").find("li div a img").each(function () {
                       if ($(this).height() > sumHeight) {
                           sumHeight = $(this).height();
                       }
                   });
                   if (sumHeight > 0) {
                       $("#index_tvzb").height(sumHeight + 80);
                   }
                   */
                    //原有结束


                    /*
                                        //396 多个视频直播--改为横栏多商品形式--开始
                                        html = "";
                                        var numhtml = "";
                                        var numstpl = "";
                                        stpl = $("#tpl_tvlive_product")[0].innerHTML;//未开始模板
                                        stpl_1 = $("#tpl_tvlive_product_1")[0].innerHTML;//直播中模板
                                        numstpl = $("#tpl_tvlive_num")[0].innerHTML;
                                        for (var j = 0; j < other.contentList.length; j++) {
                                        //for (var j = 0; j < 1; j++) {
                                            var other_content = other.contentList[j];
                                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                                            if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                                                ///396 h5弹窗类型
                                                temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                                            }
                                            else {
                                                temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                                            }
                    
                                            data = {
                                                "showmoreLink": temp_asdfgghhh,
                                                "picture": g_GetPictrue(other_content.picUrl),
                                                "productName": FormatText(other_content.productInfo.productName, 25),
                                                "sellPrice": function (sellPrice) {
                                                    return sellPrice;
                    
                                                }(other_content.productInfo.sellPrice),
                                                "startTime": Date.Parse(other_content.startTime).Format("hh:mm"),
                                                "endTime": Date.Parse(other_content.endTime).Format("hh:mm")
                                            };
                                            if (My_DateCheck.CheckEX_1("<=", other.sysTime, other_content.startTime) && My_DateCheck.CheckEX_1(">", other.sysTime, other_content.endTime)) {
                                                //正在直播
                                                html += renderTemplate(stpl_1, data);
                                            }
                                            else if (My_DateCheck.CheckEX_1(">", other.sysTime, other_content.startTime)) {
                                                //未开始的
                                                html += renderTemplate(stpl, data);
                                            }
                                        }
                                        if (other.contentList.length <= 1) {
                                            numhtml = "&nbsp;"
                                        }
                                        stpl = $("#tpl_tvlive")[0].innerHTML;
                                        data = {
                                            "columnName": other.columnName,
                                            "showStyle": other.showName==g_const_isShowmore.NO ? 'style="display:none"' : '',
                                            "showmoreLink": "/tvlive.html",//Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue),
                                            "classmore": Page_Index.classmore(other),
                                            "showmoreTitle": Page_Index.showmoreTitle(other),
                                            "productList": html,
                                            "showBottomSpace": showBottomSpace
                                        };
                                        html = renderTemplate(stpl, data);
                                        $("#bodycontent").append(html);
                    
                    
                                        //重置li的高度
                                        var sumHeight = 0;
                                        $("#index_tvzb").find("li div a img").each(function () {
                                            if ($(this).height() > sumHeight) {
                                                sumHeight = $(this).height();
                                            }
                                        });
                                        if (sumHeight > 0) {
                                            $("#index_tvzb").height(sumHeight + 80);
                                        }
                                        //重置UL的宽度other.contentList.length
                                        var ul_width = 90 * other.contentList.length;
                                        $("#index_tvzb").attr("style", "width:" + ul_width + "px;");
                    
                                        //396 多个视频直播--改为横栏多商品形式--结束
                    */
                    break;
                    //通知模版
                case g_const_columnType.Notify:
                    top3Div.push("div_notify");

                    html = "";
                    stpl = $("#tpl_notify").html();
                    var listhtml = "";
                    var liststpl = $("#tpl_notify_list").html();
                    for (var j = 0; j < other.contentList.length; j++) {
                        var notify = other.contentList[j];

                        var temp_asdfgghhh = g_GetLocationByShowmoreLinktype(notify.showmoreLinktype, notify.showmoreLinkvalue, notify);
                        //if (notify.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || notify.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        var listdata = {
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), notify.showmoreLinktype, notify.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            notifypic: notify.picture,
                            notifytext: "<span style='color:" + notify.titleColor + "'>" + notify.title + "<span>",
                            notifylink: temp_asdfgghhh,
                            notifytype: notify.showmoreLinktype,
                            notifybody: "<span style='color:" + notify.descriptionColor + "'>" + notify.description + "<span>"
                        };
                        listhtml += renderTemplate(liststpl, listdata);
                    }
                    data = {
                        "showBottomSpace": showBottomSpace,
                        notifylist: listhtml,
                        notifyid: "marqueebox_" + other.columnID
                    }
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);

                    Page_Index.Startmarquee(50, 10, other.intervalSecond * g_const_seconds, data.notifyid);

                    $("div.headline li").off("click");
                    $("div.headline li").on("click", function () {
                        var notifytype = $(this).attr("notifytype");
                        var notifytitle = $(this).attr("notifytitle");
                        var notifybody = $(this).attr("notifybody");
                        if (notifytype == g_const_showmoreLinktype.ShowLayer) {
                            $("#div_notice").css("display", "");
                            $("#notice_title").html(notifytitle);
                            $("#notice_body").html(notifybody);
                        }
                    });
                    $("#div_notice").off("click");
                    $("#div_notice").on("click", function () {
                        $(this).css("display", "none");
                    });

                    break;
                    //2栏2行
                case g_const_columnType.Recommend2L2H:
                    top3Div.push("div_2L2H");
                    html = "";
                    stpl = $("#tpl_2L2H").html();
                    var listhtml = "";
                    var liststpl = $("#tpl_2L2H_productList").html();
                    var maxlength = other.contentList.length > 4 ? 4 : other.contentList.length;

                    for (var j = 0; j < maxlength; j++) {
                        var product = other.contentList[j];

                        var temp_asdfgghhh = g_GetLocationByShowmoreLinktype(product.showmoreLinktype, product.showmoreLinkvalue);
                        //if (product.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || product.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                            /*5.2.4 增加自营标签 proClassifyTag*/
                       var listdata = {
                           "proClassifyTag": (typeof (product.productInfo.proClassifyTag) !== "undefined" && product.productInfo.proClassifyTag != "") ? "<img src=\"" + product.productInfo.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), product.showmoreLinktype, product.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            ProductDetailURL: temp_asdfgghhh,
                            picture: g_GetPictrue(product.productInfo.mainpicUrl, "../../resources/cfamily/zzz_js/index.png"),

                            SoldOut: "",
                            discount: product.productInfo.discount,
                            productNameString: product.productInfo.productName,
                            productPrice: product.productInfo.sellPrice,
                            "imgSize": " style=\"width:" + 0.49 * $(window).width() + "px;height:" + 0.49 * $(window).width() + "px\""
                        };
                        listhtml += renderTemplate(liststpl, listdata);
                    }

                    var temp_asdfgghhh = g_GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}


                    data = {
                        "showBottomSpace": showBottomSpace,
                        columnName: other.columnName,
                        showStyle: other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        showmoreLink: temp_asdfgghhh,
                        classmore: Page_Index.classmore(other),
                        showmoreTitle: Page_Index.showmoreTitle(other),
                        productList: listhtml
                    }
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    //3栏2行
                case g_const_columnType.Recommend3L2H:
                    top3Div.push("div_3L2H");
                    html = "";
                    stpl = $("#tpl_3L2H").html();
                    var listhtml = "";
                    var liststpl = $("#tpl_3L2H_productList").html();
                    var maxlength = other.contentList.length > 6 ? 6 : other.contentList.length;
                    for (var j = 0; j < maxlength; j++) {
                        var product = other.contentList[j];

                        var temp_asdfgghhh = g_GetLocationByShowmoreLinktype(product.showmoreLinktype, product.showmoreLinkvalue);
                        //if (product.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || product.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        var listdata = {
                            "proClassifyTag":"",// (typeof (product.productInfo.proClassifyTag) !== "undefined" && product.productInfo.proClassifyTag != "") ? "<img src=\"" + product.productInfo.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), product.showmoreLinktype, product.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            ProductDetailURL: temp_asdfgghhh,
                            picture: g_GetPictrue(product.productInfo.mainpicUrl, "../../resources/cfamily/zzz_js/index.png"),

                            SoldOut: "",
                            discount: product.productInfo.discount,
                            productNameString: product.productInfo.productName,
                            productPrice: product.productInfo.sellPrice
                        };
                        listhtml += renderTemplate(liststpl, listdata);
                    }

                    var temp_asdfgghhh = g_GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}


                    data = {
                        "showBottomSpace": showBottomSpace,
                        columnName: other.columnName,
                        showStyle: other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        showmoreLink: temp_asdfgghhh,
                        classmore: Page_Index.classmore(other),
                        showmoreTitle: Page_Index.showmoreTitle(other),
                        productList: listhtml
                    }
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    /*==========5.0新增模板  开始====================================*/
                    //TV直播(新)   支持横划
                case g_const_columnType.TVLive_new:
                    top3Div.push("TV_module-ul");
                    var tvid_group_TVLive = [];
                    var tvid_starttime_TVLive = [];
                    var tvid_endtime_TVLive = [];
                    html = "";
                    stpl = $("#tpl_TVLive_new_li_50")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];

                        var isfinish = false;
                        var isnobegin = false;
                        var temp_asdfgghhh = "";
                        var date_now = new Date();
                        //if (Date.Parse(other_content.endTime) < date_now.getTime()){ 
                        //    isfinish = true;
                        //    temp_asdfgghhh = 'href="/tvlive.html"';
                        //}
                        //else 
                        if (Date.Parse(other_content.startTime) > date_now.getTime() && typeof (other_content.videoUrlTV) != "undefined" && other_content.videoUrlTV == "") {
                            isnobegin = true;
                            temp_asdfgghhh = 'href="/tvlive.html?futureProgram=' + other.futureProgram + '"';
                        }
                        else {
                            var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                            //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                            //    /*396 h5弹窗类型*/
                            //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                            //}
                            //else {
                            //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                            //}
                        }
                        var tvid = "TVLive_new_" + other_content.productInfo.productCode + "_" + j.toString() + "_" + (new Date()).valueOf() + "_" + Math.floor(9999999);

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "tvid": tvid,
                            "picture": g_GetPictrue(other_content.picUrl, "/img/default/TVdefault.png"),

                            "productName": FormatText(other_content.productInfo.productName, 25),
                            "sellPrice": other_content.productInfo.sellPrice == "" ? "" : "<i>￥</i>" + other_content.productInfo.sellPrice,
                            "title": other_content.productInfo.tvTips,
                            "isshowbtn": other_content.productInfo.tvTips == "" ? " style=\"display:none;\"" : ""
                        };
                        tvid_group_TVLive.push(tvid);
                        tvid_starttime_TVLive.push(other_content.startTime);
                        tvid_endtime_TVLive.push(other_content.endTime);

                        html += renderTemplate(stpl, data);

                    }
                    stpl = $("#tpl_TVLive_new_50")[0].innerHTML;
                    //var temp_asdfgghhh1 = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh1 = 'onclick="' + temp_asdfgghhh1 + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh1 = 'href="' + temp_asdfgghhh1 + '"';
                    //}
                    var ulid = (new Date()).valueOf() + "_" + Math.floor(9999999);
                    data = {
                        "ulid": ulid,
                        "showmoreLink": 'onclick="Page_Index.GoToTVList(\'' + other.futureProgram + '\');"',
                        "isshow": other.isShowmore == g_const_isShowmore.NO ? " style=\"display:none\"" : "",
                        "showmoreLinkTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "columnName": other.columnName
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);

                    //显示TV状态
                    for (var j = 0; j < tvid_group_TVLive.length; j++) {
                        self.setInterval("Page_Index.ShowTvLiveBeginTime('" + tvid_group_TVLive[j] + "','" + tvid_starttime_TVLive[j] + "','" + tvid_endtime_TVLive[j] + "');", g_const_seconds);
                    }
                    //样式调整
                    //计算li/ul宽度
                    var win_with = $(window).width();
                    var li_width = win_with * 0.5;
                    var li_height = li_width * 0.56;
                    $("#TV_module-ul_" + ulid + " li").width(li_width);
                    $("#TV_module-ul_" + ulid + " li").height(li_height);
                    //var ul_width = $("#TV_module-ul_" + ulid + " li").length * $("#TV_module-ul_" + ulid + " li").width() + $("#TV_module-ul_" + ulid + " li").length + 6;
                    var ul_width = $("#TV_module-ul_" + ulid + " li").length * ($("#TV_module-ul_" + ulid + " li").width() + 2) + $("#TV_module-ul_" + ulid + " li").length * 27;

                    var ul_width_out = ul_width + $(window).width() * 0.5;
                    $("#TV_module-ul_" + ulid).width(ul_width);
                    $("#TV_module-ul_" + ulid).css({
                        "paddingLeft": $(window).width() * 0.25 + "px"
                        , "paddingRight": $(window).width() * 0.25 + "px"
                        , "left": -($(window).width() * 0.5) + "px"
                    });
                    $("#TV_module-ul_wrap_" + ulid).height($("#TV_module-ul_" + ulid).outerHeight());
                    //调用拖拽函数
                    TVLive_new_drag(document.getElementById("TV_module-ul_" + ulid), ulid);

                    break;
                    //视频播放[待测试]
                case g_const_columnType.Video_new:
                    top3Div.push("TV_bigModule-ul");
                    var tvid_group = [];
                    var tvid_starttime = [];
                    var tvid_endtime = [];
                    html = "";
                    stpl = $("#tpl_Video_new_li_50")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //        temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        var tvid = "Video_new_" + other_content.productInfo.productCode + "_" + j.toString();

                        /*5.2.4 增加自营标签 proClassifyTag*/
                        data = {
                            "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "tvid": tvid,
                            "picture": g_GetPictrue(other_content.picture, "/img/default/like.png"),

                            "productName": FormatText(other_content.productInfo.productName, 25),
                            "sellPrice": other_content.productInfo.sellPrice
                            //"showStr": function (tvid, startTime, endTime) {
                            //        //显示已经开始时间
                            //        self.setInterval("Page_Index.ShowTvLiveBeginTime('" + tvid + "','" + startTime + "','" + endTime + "',);", g_const_seconds);
                            //}(other_content.productInfo.productCode+"_"+j.toString(),other_content.productInfo.startTime, other_content.productInfo.endTime)
                        };
                        tvid_group.push(tvid);
                        tvid_starttime.push(other_content.startTime);
                        tvid_endtime.push(other_content.endTime);

                        html += renderTemplate(stpl, data);

                    }
                    stpl = $("#tpl_Video_new_50")[0].innerHTML;
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}


                    data = {
                        "a100": maidian.GetMaiDianObj_5002("1", other.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, "1"),
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "productList": html
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);

                    //显示TV状态
                    //for (var j = 0; j < tvid_group.length; j++) {
                    //    self.setInterval("Page_Index.ShowTvLiveBeginTime('" + tvid_group[j] + "','" + tvid_starttime[j] + "','" + tvid_endtime[j] + "');", g_const_seconds);
                    //}
                    break;

                    //右两栏闪购[等待确认返回数据结构，待测试]
                case g_const_columnType.RecommendRightTwoFastBuy:
                    top3Div.push("div_MiaoShaRightTwo");
                    Page_Index.MiaoShaRightTwo_50_endTime = other.endTime;

                    html = "";
                    var leftCaozuo = "";
                    var leftPicture = "";
                    var leftTitle = "";
                    var rightTopdescription
                    var rightBottomdescription
                    var rightTopCaozuo = "";
                    var rightTopStyle = "";
                    var rightTopPicture = "";
                    var rightTopTitle = "";
                    var rightBottomCaozuo = "";
                    var rightBottomStyle = "";
                    var rightBottomPicture = "";
                    var rightBottomTitle = "";
                    var rightToptitleColor = "";
                    var rightBottomtitleColor = "";
                    var leftToptitleColor = "";
                    var leftToptitleColor1 = "";
                    var leftToptitle2 = "";
                    var rightTopTitleColor = "";
                    var rightBottomTitleColor = "";
                    var rightTopdDescriptionColor = "";
                    var rightBottomDescriptionColor = "";
                    var rightTopdescription = "";
                    var rightBottomdescription = "";

                    var a100_1 = "";
                    var a100_2 = "";
                    var a100_3 = "";
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        //if (other_content.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other_content.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                        //    /*396 h5弹窗类型*/
                        //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                        //}
                        //else {
                        //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                        //}

                        switch (j) {
                            case 0:
                                leftCaozuo = temp_asdfgghhh;
                                leftPicture = other_content.picture;
                                leftTitle = other_content.title;
                                a100_1 = maidian.GetMaiDianObj_5002((j + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString());
                                leftToptitleColor = other_content.titleColor;
                                leftToptitleColor1 = other_content.descriptionColor;
                                leftToptitle2 = other_content.description
                                break;
                            case 1:
                                rightTopdescription = other_content.description
                                rightTopCaozuo = temp_asdfgghhh;
                                rightTopPicture = other_content.picture;
                                rightTopTitle = other_content.title;
                                a100_2 = maidian.GetMaiDianObj_5002((j + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString());
                                rightTopTitleColor = other_content.titleColor;
                                rightTopdDescriptionColor = other_content.descriptionColor;
                                break;
                            case 2:
                                rightBottomdescription = other_content.description
                                rightBottomCaozuo = temp_asdfgghhh;
                                rightBottomPicture = other_content.picture;
                                rightBottomTitle = other_content.title;
                                a100_3 = maidian.GetMaiDianObj_5002((j + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString());
                                rightBottomTitleColor = other_content.titleColor;
                                rightBottomDescriptionColor = other_content.descriptionColor;

                                break;
                        }
                    }
                    stpl = $("#tpl_MiaoShaRightTwo")[0].innerHTML;

                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);
                    //if (other.showmoreLinktype == g_const_showmoreLinktype.IndexH5 || other.showmoreLinktype == g_const_showmoreLinktype.ProductDetail) {
                    //    /*396 h5弹窗类型*/
                    //    temp_asdfgghhh = 'onclick="' + temp_asdfgghhh + '"';
                    //}
                    //else {
                    //    temp_asdfgghhh = 'href="' + temp_asdfgghhh + '"';
                    //}
                    var img1_w = 0.5 * $(window).width();
                    var img1_h = 0.35 * $(window).width();

                    data = {
                        "a100": maidian.GetMaiDianObj_5002("1", other.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, "1"),
                        "columnName": other.columnName,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "showmoreLink": temp_asdfgghhh,
                        "classmore": Page_Index.classmore(other),
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "a100_1": a100_1,
                        "a100_2": a100_2,
                        "a100_3": a100_3,
                        "leftCaozuo": leftCaozuo,
                        "leftPicture": leftPicture,
                        "leftTitle": leftTitle,
                        "leftToptitleColor": "color:" + leftToptitleColor,
                        "leftToptitleColor1": "color:" + leftToptitleColor1,
                        "leftToptitle2": leftToptitle2,
                        "rightTopCaozuo": rightTopCaozuo,
                        "rightTopPicture": rightTopPicture,
                        "rightTopTitle": rightTopTitle,
                        "rightTopTitleColor": "color:" + rightToptitleColor,
                        "rightTopdescription": rightTopdescription,
                        "rightTopdDescriptionColor": "color:" + rightTopdDescriptionColor,

                        "rightBottomCaozuo": rightBottomCaozuo,
                        "rightBottomPicture": rightBottomPicture,
                        "rightBottomTitle": rightBottomTitle,
                        "rightBottomtitleColor": "color:" + rightBottomtitleColor,
                        "rightBottomDescriptionColor": "color:" + rightBottomDescriptionColor,
                        "rightBottomdescription": rightBottomdescription,
                        "showBottomSpace": showBottomSpace,
                        "xh": i.toString(),
                        "img1": " style=\"width:" + img1_w + "px;height:" + img1_h + "px\"",
                        "img2": " style=\"width:" + img1_w + "px;height:" + 0.175 * $(window).width() + "px\""
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    //倒计时
                    Page_Index.flagCheapInterval_50 = self.setInterval("Page_Index.ShowLeftTime_50('" + i.toString() + "');", g_const_seconds);

                    //CSS调整
                    ////获取图片高度
                    //var img = new Image();
                    //img.src = leftPicture+'?' + Date.parse(new Date());
                    //// 定时执行获取宽高
                    //var check = function () {
                    //    // 只要任何一方大于0
                    //    // 表示已经服务器已经返回宽高
                    //    if (img.width > 0 || img.height > 0) {
                    //        //$(".Seckill-01").height(img.height);
                    //        //$(".Seckill-02,.Seckill-03").height(img.height * 0.5);

                    //        clearInterval(check);
                    //    }
                    //};
                    //var set = setInterval(check, 30);
                    break;

                    /*==========5.0新增模板  结束========================================*/

                    //5.1.2 新视频播放模板
                case g_const_columnType.NewVideoPlay:
                    //top3Div.push("div_commonAD");
                    var commonNewVideoPlay = other.contentList[0];

                    Page_Index.newvideoMode.push(commonNewVideoPlay);
                    Page_Index.newvideoID.push(Page_Index.newvideoMode.length);

                    var tipstr_css = "hd";
                    var adstr_css = "hdy";
                    //if (isAndroid) {
                        stpl = $("#tpl_newvideoplay_az")[0].innerHTML;
                        tipstr_css = "hd_az_3";
                        adstr_css = "hdy_az";
                    //}
                    //else {
                            //放弃，因为定位问题，最后一个视频下部无其他内容时，无法滑动到设定位置，决定放弃
                    //    stpl = $("#tpl_newvideoplay")[0].innerHTML;
                    //}
                    var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(g_const_showmoreLinktype.ProductDetail, commonNewVideoPlay.productInfo.productCode);
                    var temp_asdfgghhh_title = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);

                    /*5.4.0视频播放  区域  开始*/
                    var productPic = g_GetPictrue(commonNewVideoPlay.productInfo.mainpicUrl, "../../resources/cfamily/zzz_js/index.png");
                    var videoLink_str = "<video id=\"videoID_" + commonNewVideoPlay.productInfo.productCode+"_"+i.toString()+ "\" width=\"100%\" style=\"object-fit:fill\" poster=\"" + productPic + "\" webkit-playsinline=\"true\" x-webkit-airplay=\"true\" playsinline=\"true\" x5-video-orientation=\"h5\" x5-video-player-type=\"h5\" x5-video-player-fullscreen=\"false\" preload=\"auto\" controls=\"controls\"  autobuffer><source src=\"" + commonNewVideoPlay.videoLink + "\" type=\"video/mp4\"/></video>";
                    if (isAndroid) {
                        videoLink_str = "<video id=\"videoID_" + commonNewVideoPlay.productInfo.productCode+"_"+i.toString()+ "\" src=\"" + commonNewVideoPlay.videoLink + "\" poster=\"" + productPic + "\" preload=\"auto\" x-webkit-airplay=\"true\" x5-playsinline=\"true\" webkit-playsinline=\"true\" playsinline=\"true\" style=\"width: 100%;\"></video>";
                    }
					
					Page_Index.videoIDs.push(commonNewVideoPlay.productInfo.productCode+"_"+i.toString());
                    /*5.4.0视频播放  区域  结束*/

                    /*5.2.4 增加自营标签 proClassifyTag*/
                    data = {
                        "proClassifyTag": (typeof (commonNewVideoPlay.productInfo.proClassifyTag) !== "undefined" && commonNewVideoPlay.productInfo.proClassifyTag != "") ? "<img src=\"" + commonNewVideoPlay.productInfo.proClassifyTag + "\" style=\"width: 13%;height: 75%;margin-top: 0.03rem;margin-right: 4px;\"/>" : "",
                        "columnName": other.columnName == "" ? "" : other.columnName,
                        "showmoreLink": temp_asdfgghhh_title,
                        "showStyle": other.showName == g_const_isShowmore.YES ? "" : " style=\"display:none;\"",
                        "showmoreTitle": Page_Index.showmoreTitle(other),
                        "classmore": Page_Index.classmore(other),

                        "a100": maidian.GetMaiDianObj_5002("1", g_const_showmoreLinktype.ProductDetail, commonNewVideoPlay.productInfo.productCode, other.columnType, other.columnName, Page_Index.indexChannel_Name, "1"),
                        "video_id": commonNewVideoPlay.productInfo.productCode+"_"+i.toString(),
                        "caozuo": temp_asdfgghhh,
                        "price": commonNewVideoPlay.productInfo.sellPrice,
                        "picture": productPic,
                        "tipstr": commonNewVideoPlay.productInfo.isActivity == "" ? "" : "<span class=\"" + tipstr_css + "\">" + commonNewVideoPlay.productInfo.isActivity + "</span>",
                        "title": commonNewVideoPlay.productInfo.productName,
                        "videoAd": commonNewVideoPlay.productInfo.videoAd == "" ? "" : "<p class=\"" + adstr_css + "\">" + commonNewVideoPlay.productInfo.videoAd + "</p>",
                        "zb_div": commonNewVideoPlay.productInfo.videoAd == "" ? "" : "<div class=\"v_m_bg\"></div>",
                        "showBottomSpace": showBottomSpace,
                        "videoLink_ios": videoLink_str,
						"title2": commonNewVideoPlay.productInfo.videoAd == "" ? commonNewVideoPlay.productInfo.productName:commonNewVideoPlay.productInfo.videoAd,
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                //5.1.4 多栏广告
                case g_const_columnType.MoreColumnAD:
                    html = "";
                    var numLan = 0;
                    switch (other.numLan) {
                        case "4497471600360001":
                            numLan = 1;
                            break;
                        case "4497471600360002":
                            numLan = 2;
                            break;
                        case "4497471600360003":
                            numLan = 3;
                            break;
                        case "4497471600360004":
                            numLan = 4;
                            break;
                    }
                    if (other.contentList.length == numLan) {
                        stpl = $("#tpl_AD_li_Duo")[0].innerHTML;
                        if (other.contentList.length > 0) {
                            //计算宽度
                            var li_width_style = 100 / other.contentList.length;
                            for (var j = 0; j < other.contentList.length; j++) {
                                var other_content = other.contentList[j];
                                var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                                data = {
                                    "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                                    "caozuo": temp_asdfgghhh,
                                    "picture": g_GetPictrue(other_content.picture, "../../resources/cfamily/zzz_js/index.png"),
                                    "xh": (j + 1),
                                    "id": (i + 1),
                                    //"widthStyle": "width:" + li_width_style + "%",
                                    //"picheight": other_content.picOriginHeight + "px",
                                };
                                html += renderTemplate(stpl, data);
                            }
                        }

                        stpl = $("#tpl_AD_Duo")[0].innerHTML;
                        data = {
                            "id":(i+1).toString(),
                            "columnName": other.columnName == "" ? "" : other.columnName,
                            "showmoreLink": temp_asdfgghhh_title,
                            "showStyle": other.showName == g_const_isShowmore.YES ? "" : " style=\"display:none;\"",
                            "showmoreTitle": Page_Index.showmoreTitle(other),
                            "classmore": Page_Index.classmore(other),

                            "ADList": html,
                            "showBottomSpace": showBottomSpace
                        };
                        html = renderTemplate(stpl, data);

                        $("#bodycontent").append(html);
                        //top3Div.push("div_TwoADs");

                        //判断图片都加载完毕，设定多栏广告下图片要高度为第一栏图片高度

                    }
                    break;
                    
                    
                //540:新加秒杀和拼团模板
                    
                    
                    //5.4.0 秒杀（支持横划）
                case g_const_columnType.SecKill:
                    //top3Div.push("TV_module-ul");
                    var SecKill_group = [];
                    var SecKill_endtime = other.endTime;
                    var SecKill_startTime = other.startTime;
                    html = "";
                    debugger;
                    stpl = $("#tpl_SecKill_one")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        /*5.4.0 新增
                        isLoot: 是否抢光(449746250001：是,449746250002：否)
                        manyCollage： 几人团(示例：2人团)
                        */
                        var temp_asdfgghhh = "";
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        data = {
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "showmoreLink": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.picUrl, g_goods_Pic),
                            "sellPrice": other_content.productInfo.sellPrice == "" ? "" : other_content.productInfo.sellPrice,
                            "markPrice": other_content.productInfo.markPrice == "" ? "" : other_content.productInfo.markPrice
                        };
                        //SecKill_group.push(tvid);
                        html += renderTemplate(stpl, data);

                    }
                    stpl = $("#tpl_SecKill")[0].innerHTML;
                    var skid = (i + 1).toString() + "_" + (new Date()).valueOf() + "_" + Math.floor(9999999);

                    var temp_asdfgghhh_1 = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);

                    data = {
                        "showBottomSpace": showBottomSpace,
                        "skid": skid,
                        "startTime": new Date(SecKill_startTime.replace(/-/g, "/")).getHours() + "点场",
                        "showmoreLink": other.contentList.length < 6 ?  '' :temp_asdfgghhh_1,
                        //"isshow": other.isShowmore == g_const_isShowmore.NO ? " style=\"display:none\"" : "",
                        "productList": html,
                        "columnName": other.columnName,
                        "showMoreLast": other.contentList.length < 6 ? "display:none;" : "",
                        "BeginOrEnd": new Date(SecKill_startTime.replace(/-/g, "/")).getTime() > new Date().getTime() ? "距开始" : "还剩",
                        "a100": maidian.GetMaiDianObj_5001('1267'),
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);

                    //显示倒计时
                    self.setInterval("Page_Index.SecKillDJS('" + skid + "','" + SecKill_endtime + "','" + SecKill_startTime + "');", 100);

                    //计算li/ul宽度
                    var win_with = $(window).width();
                    var li_width = win_with /4;
                    //var li_height = li_width * 0.28;
                    $("#SecKill_" + skid + " li").width(li_width);
                    var ul_width = 0;
                    if (other.contentList.length<6) {
                        ul_width = $("#SecKill_" + skid + " li").length * ($("#SecKill_" + skid + " li").width() + 2) + $("#SecKill_" + skid + " li").length * 27;
                    }
                    else {
                        ul_width = 7 * ($("#SecKill_" + skid + " li").width() + 2) + $("#SecKill_" + skid + " li").length * 27;
                    }
                    var ul_width_out = ul_width + $(window).width() * 0.5;
                    $("#SecKill_" + skid).width(ul_width);
                    //$("#SecKill_" + skid).css({
                    //    "paddingLeft": $(window).width() * 0.25 + "px"
                    //    , "paddingRight": $(window).width() * 0.25 + "px"
                    //    , "left": -($(window).width() * 0.5) + "px"
                    //});
                    //$("#SecKill_" + skid).height($("#SecKill_" + skid).outerHeight());
                    //横滑
                    var myscroll = new IScroll("#SecKill_" + skid, {
                        scrollX: true
                        , scrollY: false
                        , mouseWheel: true
                        , preventDefault: false
                    });
                    break;
                //5.4.0 拼团列表
                case g_const_columnType.GroupBuy:
                    html = "";
                    var GroupBuy_group = [];
                    html = "";
                    stpl = $("#tpl_GroupBuy_one")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                    	
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = "";
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);
                        /*5.4.0 新增
                        isLoot: 是否抢光(449746250001：是,449746250002：否)
                        manyCollage： 几人团(示例：2人团)
                        */
                       
                        data = {
                        		   "proClassifyTag": (typeof (other_content.proClassifyTag) !== "undefined" && other_content.proClassifyTag != "") ? "<img src=\"" + other_content.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                                   "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                                   "showmoreLink": temp_asdfgghhh,
                                   "picture": g_GetPictrue(other_content.productInfo.mainpicUrl, g_goods_Pic),
                                   "productName": FormatText(other_content.productInfo.productName, 25),
                                   "sellPrice": other_content.productInfo.sellPrice == "" ? "" : "<i>￥</i>" + other_content.productInfo.sellPrice,
                                   "markPrice": other_content.productInfo.markPrice == "" ? "" : "<i>￥</i>" + other_content.productInfo.markPrice,
                                   "manyCollage": other_content.manyCollage.toString(),
                                   "isLoot": other_content.isLoot == "449746250001" ? "" : "display:none;",//抢光了
                                   "description": FormatText(other_content.description, 10),
                        };
                        GroupBuy_group.push(tvid);
                        html += renderTemplate(stpl, data);

                    }
                    stpl = $("#tpl_GroupBuy")[0].innerHTML;
                    data = {
                        "showBottomSpace":showBottomSpace,
                        "isshow": other.isShowmore == g_const_isShowmore.NO ? " style=\"display:none\"" : "",
                        "showmoreLinkTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "columnName": other.columnName,
                        "showMoreTop": other.showmoreLinkvalue == "" ? " style=\"display:none;\"" : "",
                        "showmoreTitle": other.showmoreTitle
                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    
                    //5.5.6 1栏多行
                case g_const_columnType.Recommend1LDH:
                    html = "";
                    stpl = $("#tpl_Recommend1LDH_one")[0].innerHTML;
                    for (var j = 0; j < other.contentList.length; j++) {
                        var other_content = other.contentList[j];
                        var temp_asdfgghhh = "";
                        var temp_asdfgghhh = Page_Index.GetLocationByShowmoreLinktype(other_content.showmoreLinktype, other_content.showmoreLinkvalue);                        
                        var tagList = "";
                        for (var kk = 0; kk < other_content.productInfo.tagList.length; kk++) {
                            tagList += "<span class=\"tv_quan1\">" + other_content.productInfo.tagList[kk] + "</span>";
                        }

                        data = {
                            "proClassifyTag": (typeof (other_content.productInfo.proClassifyTag) !== "undefined" && other_content.productInfo.proClassifyTag != "") ? "<img src=\"" + other_content.productInfo.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                            "a100": maidian.GetMaiDianObj_5002((i + 1).toString(), other_content.showmoreLinktype, other_content.showmoreLinkvalue, other.columnType, other.columnName, Page_Index.indexChannel_Name, (j + 1).toString()),
                            "caozuo": temp_asdfgghhh,
                            "picture": g_GetPictrue(other_content.productInfo.mainpicUrl, g_goods_Pic),
                            "productName": FormatText(other_content.productInfo.productName, 25),
                            "sellPrice": other_content.productInfo.sellPrice == "" ? "" : other_content.productInfo.sellPrice,
                            "markPrice": other_content.productInfo.markPrice == "" ? "" : "￥" + other_content.productInfo.markPrice,
                            "nomarkPriceClass": other_content.productInfo.markPrice == "" ? " mt_h_556" : "",
                            "description": FormatText(other_content.description, 10),
                            "tagList": tagList,
                            "flagTheSea": other_content.productInfo.labelsPic===""?"none":"",
                            "labelsPic": other_content.productInfo.labelsPic
                        };
                        html += renderTemplate(stpl, data);

                    }
                    stpl = $("#tpl_Recommend1LDH")[0].innerHTML;
                    var temp_asdfgghhh_a = Page_Index.GetLocationByShowmoreLinktype(other.showmoreLinktype, other.showmoreLinkvalue);                        

                    data = {
                        "showBottomSpace": showBottomSpace,
                        "showmoreLinkTitle": Page_Index.showmoreTitle(other),
                        "productList": html,
                        "showStyle": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',
                        "columnName": other.columnName,
                        "showMoreTop": other.isShowmore == "449746250002" ? " display:none;" : "",
                        "showmoreTitle": other.showmoreTitle,
                        "showmoreLink": temp_asdfgghhh_a,
                        "isshow": other.showName == g_const_isShowmore.NO ? 'style="display:none"' : '',

                    };
                    html = renderTemplate(stpl, data);
                    $("#bodycontent").append(html);
                    break;
                    
                    
                    
                    
                    
                    
                    
                    
            }

        }
        /* @ 轮播图订高  兆安 */
        var swiper = new Swiper('.swiper-container', {
            loop: true,
            pagination: '.swiper-pagination',
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            paginationClickable: true,
            spaceBetween: 30,
            centeredSlides: true,
            autoplay: 2500,
            autoplayDisableOnInteraction: false
        });

        ////加载完全部模板后，初始化埋点信息
        //if (typeof(gas.pageInit)=="function") {
        //    gas.pageInit();
        //}

        for (var jj = 0; jj < top3Div.length; jj++) {
            if (jj < 3) {
                //前3个模板直接替换图片
                lazyDivListFirstPage(top3Div[jj]);
            }
            //lazyload_allimg(top3Div[jj], 100);
        }
        lazyload_allimg("", 100);

        Page_Index.indexChannel_check = 1;
        /*隐藏下拉刷新层*/
        try {
            ScollDown_hide("div_scrolldown", 500);
        } catch (e) { }

        //5.1.2 新视频播放滑动监听(产品决定放弃此功能，不在首页播放视频)
        //new_tv1();
		
	//5.4.0 监听视频播放完毕
        for (var i = 0; i < Page_Index.videoIDs.length; i++) {
            if (typeof ($("videoID_" + Page_Index.videoIDs[i])) == "object") {
                document.getElementById("videoID_" + Page_Index.videoIDs[i]).addEventListener("play", function () {
                    //alert("播放开始");
                    $("#videoImg_" + this.id).hide();
					$("#videoDiv_" + this.id).show();
					$("#" + this.id).show();
					$("#sptitle_540_" + this.id).css("margin-top","-25px");
					
                });
                document.getElementById("videoID_" + Page_Index.videoIDs[i]).addEventListener("ended", function () {
                    //alert("播放结束");
                    $("#videoImg_" + this.id).show();
					$("#" + this.id).hide();
                    $("#videoDiv_" + this.id).hide();
					$("#sptitle_540_" + this.id).css("margin-top","0");
					this.play();
                });
				
				document.getElementById("videoID_" + Page_Index.videoIDs[i]).addEventListener("pause", function () {
                    //alert("播放暂停");
                    $("#videoImg_" + this.id).show();
					$("#" + this.id).hide();
                    $("#videoDiv_" + this.id).hide();
					$("#sptitle_540_" + this.id).css("margin-top","0");
					
					//重新书写video
					//var videoLink_str = "<video id=\"" + this.id+ "\" width=\"100%\" style=\"object-fit:fill\" poster=\"" + this.poster + "\" webkit-playsinline=\"true\" x-webkit-airplay=\"true\" playsinline=\"true\" x5-video-orientation=\"h5\" x5-video-player-type=\"h5\" x5-video-player-fullscreen=\"false\" preload=\"auto\" controls=\"controls\"  autobuffer><source src=\"" + this.src + "\" type=\"video/mp4\"/></video>";
                    if (isAndroid) {
                        videoLink_str = "<video id=\"" + this.id+ "\" src=\"" + this.src + "\" poster=\"" + this.poster + "\" preload=\"auto\" x-webkit-airplay=\"true\" x5-playsinline=\"true\" webkit-playsinline=\"true\" playsinline=\"true\" style=\"width: 100%;\"></video>";
						$("#videoDiv_"+this.id).html()
					}
                });
            }
        };
    },
    /*5.4.0 点击图片播放视频*/
    VodeoPlay: function (objId) {
		for (var i = 0; i < Page_Index.videoIDs.length; i++) {
			//if(Page_Index.videoIDs[i]!=objId){
				
				document.getElementById("videoID_" + Page_Index.videoIDs[i]).pause();
				//从头开始播放
				document.getElementById("videoID_" + Page_Index.videoIDs[i]).currentTime=0
				
				$("#videoImg_videoID_" + Page_Index.videoIDs[i]).show();
				$("#videoDiv_videoID_" + Page_Index.videoIDs[i]).hide();
				$("#sptitle_540_videoID_" + Page_Index.videoIDs[i]).css("margin-top","0");
			//}
		}
		
        var obj = document.getElementById("videoID_" + objId);
        var objimg = $("#videoImg_videoID_" + objId);
        var objdiv = $("#videoDiv_videoID_" + objId);
		
        //if (obj.paused) {
			objdiv.show();
            obj.play();
            objimg.hide();
			$("#sptitle_540_" + objId).css("margin-top","-25px");
        //} else {
        //   obj.pause();
		//   $("#sptitle_540_" + objId).css("margin-top","0");
        //}
		
		/*canvas = document.getElementById('canvas_videoID_'+objId);
		if (isAndroid) {
			//隐藏video
			objdiv.hide();
			
			
			canvas.width=window.screen.width;
			context = canvas.getContext( '2d' );
			context.fillStyle = '#fff';
			
			context.fillRect( 0, 0, window.screen.width, canvas.height );//绘制1920*1080像素的已填充矩形。
			var img=new Image();//新建一个图片，模仿video里面的poster属性。
			img.src="video.png";
			context.drawImage(img,0, 0,window.screen.width,canvas.height);//将图片绘制进canvas。
			Page_Index.CurrVideoId=objId;
			Page_Index.animate();
		}
		else{
			canvas.hide();
			
		}
		*/
    },
	/*5.4.0当前播放video的ID，安卓机型中使用*/
	CurrVideoId:"",
	/*5.4.0渲染方法(安卓机型使用)*/
	animate:function(){
		var objId=Page_Index.CurrVideoId;
		var obj = document.getElementById("videoID_" + objId);
		var objimg = $("#videoImg_videoID_" + objId);
		var objdiv = $("#videoDiv_videoID_" + objId);
		var canvas = document.getElementById('canvas_videoID_'+objId);
		if(obj.paused){//判断视频是否暂停，如果暂停显示控件。
				objdiv.hide();
				objimg.show();
				$("#sptitle_540_" + objId).css("margin-top","0");
			}
			else{
			context.drawImage(obj,0, 0,window.screen.width,canvas.height);//将视频当中的一帧绘制到canvas当中。
			videoanimate = requestAnimationFrame( Page_Index.animate );//每秒60帧渲染页面。关于此方法具体解释请自行百度。	
			}			
	},
    /*findID:ul的id
    xh：li的序号，li的id=ul的id+‘_’+序号
    num：从第二张图片开始，高度的倍数，不传表示=第一张图片高度
    在图片上增加 onload="Page_Index.SetImgHeight('ul的id_{{id}}',{{xh}})"
    */
    SetImgHeight: function (findID,xh,num) {
        // 找到为0就将isLoad设为false，并退出each
        var isLoad = true;
        var t_img;
        var firstHeight = $("#" + findID + "_1").height();

        if (num === undefined) {

        }
        else {
            //左两栏特殊处理
            if ($("#" + findID).attr("class").indexOf('column-left') > -1) {
                firstHeight = $("#" + findID + "_3").height();
            }
        }
        if ( firstHeight=== 0) {
            isLoad = false;
        }
        // 为true，没有发现为0的。加载完毕
        if (isLoad) {
            clearTimeout(t_img); // 清除定时器
            //设定其他图片高度
            $("#" + findID + " img").each(function (i, n) {
                if ($("#" + findID).attr("class").indexOf('column-left') > -1) {
                    //左两栏特殊处理
                    if (i <2) {
                        //var t_style = this.attr("style") ;
                        if (num === undefined) {
                            this.style.height = firstHeight + "px";
                        }
                        else {
                            this.style.height = (firstHeight * num) + "px";
                        }
                    }
                }
                else {
                    if (i >= 1) {
                        //var t_style = this.attr("style") ;
                        if (num === undefined) {
                            this.style.height = firstHeight + "px";
                        }
                        else {
                            this.style.height = (firstHeight * num) + "px";
                        }
                    }
                }
            });
            
        } else {
            isLoad = true;
            t_img = setTimeout(function () {
                Page_Index.SetImgHeight(findID, xh); // 递归扫描
            }, 500); // 我这里设置的是500毫秒就扫描一次，可以自己调整
        }
    },
    /*新视频模板内容*/
    newvideoMode: [],
    /*新视频模板编号*/
    newvideoID: [],

    GoToTVList: function (futureProgram) {
        PageUrlConfig.SetUrl();
        window.location.href = "/tvlive.html?futureProgram=" + futureProgram + "&t=" + Math.random();

    },
    /*滑动到最后，跳转*/
    ScollLastGoTo: function (divid, toMoreUrl) {
        var arr = [];
        var tmp = '';
        var offset_right = 0;
        var re = /\d+/g;
        if (typeof ($("#" + divid + " ul")) != "undefined") {
            if (typeof ($("#" + divid + " ul").css('transform')) != "undefined") {
                var str = $("#" + divid + " ul").css('transform');//.match(re).split(',')
                arr = $("#" + divid + " ul").css('transform').match(re);//.split(',')
                for (var i = 0; i < str.length; i++) {
                    var c = str.charAt(i);
                    if (c == "-") {
                        tmp = c;
                    }
                }
                //只有当translete-x的值是负数的时候执行
                if (tmp) {
                    //console.log(tmp);
                    offset_right = ($(window).width() + parseInt(arr[4])) + -$("#" + divid + " ul").outerWidth();
                    //console.log($(window).width(), parseInt(arr[4]), $(".module-lookMore ul").outerWidth(), offset_right);
                    if (offset_right > 15) {
                        window.location.href = toMoreUrl;
                    }
                    else {
                        Page_Index.ScollLastGoTo(divid, toMoreUrl);
                    }
                }
                else {
                    Page_Index.ScollLastGoTo(divid, toMoreUrl);
                }
            }
        }
        else {
            Page_Index.ScollLastGoTo(divid, toMoreUrl);
        }

    },
    //通知轮播
    Startmarquee: function (lh, speed, delay, id) {
        var p = false;
        var t;
        var o = $("#" + id)[0];
        o.innerHTML += o.innerHTML;
        o.style.marginTop = 0;
        o.onmouseover = function () { p = true; }
        o.onmouseout = function () { p = false; }

        function start() {
            t = setInterval(scrolling, speed);
            if (!p) o.style.marginTop = parseInt(o.style.marginTop) - 1 + "px";
        }

        function scrolling() {
            if (parseInt(o.style.marginTop) % lh != 0) {
                o.style.marginTop = parseInt(o.style.marginTop) - 1 + "px";
                if (Math.abs(parseInt(o.style.marginTop)) >= o.scrollHeight / 2) o.style.marginTop = 0;
            } else {
                clearInterval(t);
                setTimeout(start, delay);
            }
        }
        setTimeout(start, delay);
    },
    /*横向滚动初始化*/
    touchInit: function (touchwrapid) {
        var iWidth = $(window).innerWidth();
        var wrap = $('.touch-wrap');
        var goodsWrap = $('.goods-recomd');
        goodsWrap.each(function () {
            var aLi = $(this).find('li');
            var len = aLi.length;
            aLi.css({
                'width': Math.ceil(iWidth / 3)
            });
            $(this).css({
                'width': Math.ceil(aLi.outerWidth()) * aLi.length
            });
        });

        wrap.css({
            'width': iWidth,
            'overflow': 'hidden'
        });

        var myscroll = new IScroll("#" + touchwrapid, {
            scrollX: true, scrollY: false, mouseWheel: true, preventDefault: false
        });
    },
    flagCheapInterval: 0,
    /*快速购买数据*/
    FastBuy: {},
    /*倒计时[4.1.5快速购买]*/
    ShowLeftTime: function (xh) {
        if (typeof ($("#fastbuy_js_" + xh)[0]) != "undefined") {

            var date_last = Date.Parse(Page_Index.FastBuy.endTime);
            date_now = new Date();
            var ts = date_last.getTime() - date_now.getTime();  //时间差的毫秒数              

        	if(Page_Index.TP!=""){
            	var ct = Number(Page_Index.count)+1;
            	Page_Index.count = ct+"";
            	var date_now = new Date(Page_Index.TP.replace(/-/g, "/"));
                var timestamp = date_now.getTime()+100*ct; 
                ts = date_last.getTime()-timestamp;
        	}
            var hours = Math.floor(ts / g_const_hours);
            var leftmillionseconds = ts % g_const_hours;
            var minutes = Math.floor(leftmillionseconds / g_const_minutes);
            leftmillionseconds = leftmillionseconds % g_const_minutes;
            var seconds = Math.floor(leftmillionseconds / g_const_seconds);

            var hourstring = "0" + hours.toString();
            hourstring = hourstring.substr(hourstring.length - 2, 2);
            var minutestring = "0" + minutes.toString();
            minutestring = minutestring.substr(minutestring.length - 2, 2);

            var secondstring = "0" + seconds.toString();
            secondstring = secondstring.substr(secondstring.length - 2, 2);

            //var Millisecond = (date_now.getMilliseconds() / 100).toString().substr(0, 1);        
            var mseconds = Math.floor(leftmillionseconds / 100);
            var msecondstring = mseconds.toString();
            Millisecond = msecondstring.substr(msecondstring.length - 1, 1);
            $("#fastbuy_js_" + xh)[0].innerHTML = '<em class="">' + hourstring + '</em><b>:</b><em class="">' + minutestring + '</em><b>:</b><em class="">' + secondstring + '</em><b>.</b><em class="">' + Millisecond + '</em>';
            if (hours == 0 && minutes == 0 && seconds == 0)
                self.clearInterval(Page_Index.flagCheapInterval);
        }
    },
    
    /*倒计时(5.0版本)*/
    flagCheapInterval_50: 0,
    /*快速购买数据*/
    MiaoShaRightTwo_50_endTime: {},
    /*倒计时[5.0秒杀]*/
    ShowLeftTime_50: function (xh) {
        if (typeof ($("#djs50_" + xh)[0]) != "undefined") {

            var date_last = Date.Parse(Page_Index.MiaoShaRightTwo_50_endTime);
            date_now = new Date();
            var ts = date_last.getTime() - date_now.getTime();  //时间差的毫秒数              
        	if(Page_Index.TP!=""){	
            	var ct = Number(Page_Index.count)+1;
            	Page_Index.count = ct+"";
                var date_now = new Date(Page_Index.TP.replace(/-/g, "/"));
                var timestamp = date_now.getTime()+100*ct; 
                //date_now = Page_Index.formatDate(new Date(timestamp));
                //date_now =  new Date(date_now.replace(/-/g, "/"));
                ts = date_last.getTime()-timestamp;
        	}
            
            var hours = Math.floor(ts / g_const_hours);
            var leftmillionseconds = ts % g_const_hours;
            var minutes = Math.floor(leftmillionseconds / g_const_minutes);
            leftmillionseconds = leftmillionseconds % g_const_minutes;
            var seconds = Math.floor(leftmillionseconds / g_const_seconds);

            var hourstring = "0" + hours.toString();
            hourstring = hourstring.substr(hourstring.length - 2, 2);
            var minutestring = "0" + minutes.toString();
            minutestring = minutestring.substr(minutestring.length - 2, 2);

            var secondstring = "0" + seconds.toString();
            secondstring = secondstring.substr(secondstring.length - 2, 2);
            $("#djs50_" + xh)[0].innerHTML = '<li class="num">' + hourstring + '</li><li class="space">:</li><li class="num">' + minutestring + '</li><li class="space">:</li><li class="num">' + secondstring + '</li>';
            if (hours == 0 && minutes == 0 && seconds == 0)
                self.clearInterval(Page_Index.flagCheapInterval_50);
        }
    },

    
    formatDate: function (now) {
    	
    	var year=now.getFullYear(); 
    	var month=now.getMonth()+1; 
    	var date=now.getDate(); 
    	var hour=now.getHours(); 
    	var minute=now.getMinutes(); 
    	var second=now.getSeconds(); 
    	return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
    },
    
    SecKillDJS: function (skid, endtime,begintime) {
        if (typeof ($("#SecKill_djs_" + skid)[0]) != "undefined") {
        	date_now =  new Date();
        	var date_last = new Date(endtime.replace(/-/g, "/"));
            var date_begin = new Date(begintime.replace(/-/g, "/"));
            //距离结束倒计时
            var ts = date_last.getTime() - date_now.getTime();  //时间差的毫秒数      
        
        	if(Page_Index.TP!=""){
        		
            	var ct = Number(Page_Index.count)+1;
            	Page_Index.count = ct+"";
                var date_now = new Date(Page_Index.TP.replace(/-/g, "/"));
                var timestamp = date_now.getTime()+100*ct; 
                //date_now = Page_Index.formatDate(new Date(timestamp));
                //date_now =  new Date(date_now.replace(/-/g, "/"));
                ts = date_last.getTime()-timestamp;
        	}
     
                if (date_begin.getTime() > date_now.getTime()) {
                    //距离开始倒计时
                    ts = date_begin.getTime() - date_now.getTime();  //时间差的毫秒数
                }
             
                var hours = Math.floor(ts / g_const_hours);
                var leftmillionseconds = ts % g_const_hours;
                var minutes = Math.floor(leftmillionseconds / g_const_minutes);
                leftmillionseconds = leftmillionseconds % g_const_minutes;
                var seconds = Math.floor(leftmillionseconds / g_const_seconds);

                var hourstring = "0" + hours.toString();
                hourstring = hourstring.substr(hourstring.length - 2, 2);
                var minutestring = "0" + minutes.toString();
                minutestring = minutestring.substr(minutestring.length - 2, 2);

                var secondstring = "0" + seconds.toString();
                secondstring = secondstring.substr(secondstring.length - 2, 2);

                var mseconds = Math.floor(leftmillionseconds / 100);
                var msecondstring = mseconds.toString();
               
                msecondstring = msecondstring.substr(msecondstring.length - 1, 1);

                $("#SecKill_djs_" + skid)[0].innerHTML = '<em class="">' + hourstring + '</em><b>:</b><em class="">' + minutestring + '</em><b>:</b><em class="">' + secondstring + '</em><b>.</b><em class="">' + msecondstring + '</em>';

                if (hours == 0 && minutes == 0 && seconds == 0 && mseconds == 0) {
                    //window.location.reload();
                    //刷新栏目内容
                    if (UserLogin.LoginStatus === g_const_YesOrNo.NO) {
                        //所有人使用同一个Redis
                        Page_Index.IndexChannel_sel(Page_Index.indexChannel_Code, Page_Index.indexChannel_Name, Page_Index.indexChannel_maybeLove);
                    }
                    else {
                        //5.0.6增加会员日后需传递token，不能共用redis数据
                        Page_Index.IndexChannel_sel_API(Page_Index.indexChannel_Code, Page_Index.indexChannel_Name, Page_Index.indexChannel_maybeLove);
                    }
                }
            }
    },

    
    /*Tv直播已开始时间[5.0]*/
    TvLive_50: {},
    flagCheapInterval_tvlive_50: 0,
    ShowTvLiveBeginTime: function (tvid, starttime, endtime) {
        if (typeof ($("#" + tvid)[0]) != "undefined") {
            var date_now = new Date();
            if (Date.Parse(endtime) < date_now.getTime()) {
                $("#" + tvid)[0].innerHTML = '<span class="over"><i>已结束</i></span>';
            }
            else if (Date.Parse(starttime) > date_now.getTime()) {
                $("#" + tvid)[0].innerHTML = '<span class="waiting"><i>即将开始</i></span>';
            }
            else {

                //var date_last = Date.Parse(starttime);
                var date_last = Date.Parse(endtime);
                var ts = date_last.getTime() - date_now.getTime();  //时间差的毫秒数              

                var hours = Math.floor(ts / g_const_hours);
                var leftmillionseconds = ts % g_const_hours;
                var minutes = Math.floor(leftmillionseconds / g_const_minutes);
                leftmillionseconds = leftmillionseconds % g_const_minutes;
                var seconds = Math.floor(leftmillionseconds / g_const_seconds);

                var hourstring = "0" + hours.toString();
                hourstring = hourstring.substr(hourstring.length - 2, 2);
                var minutestring = "0" + minutes.toString();
                minutestring = minutestring.substr(minutestring.length - 2, 2);

                var secondstring = "0" + seconds.toString();
                secondstring = secondstring.substr(secondstring.length - 2, 2);
                $("#" + tvid)[0].innerHTML = '<span class="living"><i>直播中' + hourstring + ':' + minutestring + ':' + secondstring + '</i></span>';

                //if (hours == 0 && minutes == 0 && seconds == 0)
                //    self.clearInterval(Page_Index.flagCheapInterval_tvlive_50);
            }
        }
    },

    /*获取更多的Class*/
    classmore: function (other) {
        if (other.isShowmore == g_const_isShowmore.YES) {
            return other.showmoreTitle == "" ? "" : "more";
        }
        else
            return "";
    },
    /*更多的文字标题*/
    showmoreTitle: function (other) {
        if (other.isShowmore == g_const_isShowmore.YES) {
            return other.showmoreTitle == "" ? "&nbsp;" : other.showmoreTitle;
        }
        else
            return "&nbsp;";

    },
    //分类标签距离顶部的距离
    h: 20,
    //头部搜索栏高度
    ohtH: 20,
    isfirst: true,
    GuessYourLikeTitle: "",
    /*猜你喜欢原有分类编号[默认从百分点商品接口读取]*/
    GuessYourLike_categoryCode_old: "baifendian",
    /*猜你喜欢当前分类编号[默认从百分点商品接口读取]*/
    GuessYourLike_categoryCode: "baifendian",
    /*猜你喜欢当前分类搜索文字*/
    GuessYourLike_keyword: "",
    /*猜你喜欢当前页数*/
    GuessYourLike_pageNum: 1,
    /*猜你喜欢总页数*/
    GuessYourLike_TotalPages: 1,
    /*猜你喜欢响应对象【百分点推荐或查询结果】*/
    GuessYourLike_api_response: 1,
    /*防止重复*/
    GuessYourLike_check: 1,
    /*首页推荐分类【4.0.6后增加，5.0后作废】*/
    GuessYourLikeFenLei: function () {
        //计算ul总长度
        var totallil = 86;

        var GuessYourLikeTitle = "";
        var selectClass_f = "";
        var fenlei = Page_Index.api_response.homeRecommendList;
        $.each(fenlei, function (i, n) {
            totallil += 86;
            //if (i == 0) {
            //    selectClass_f = "class=\"curr\"";
            //}
            //else {
            //    selectClass_f = "";
            //}
            GuessYourLikeTitle += "<li id=\"GuessYourLike_" + n.categoryCode + "\" onclick=\"Page_Index.LoadGuessYourLikeData_sel('" + n.columnName + "','" + n.categoryCode + "')\">" + n.categoryNote + "</li>";
        });
        GuessYourLikeTitle = "<ul style=\"width:" + totallil.toString() + "px\"><li id=\"GuessYourLike_baifendian\" class=\"curr\" onclick=\"Page_Index.LoadGuessYourLikeData_sel('','baifendian')\">猜你喜欢</li>" + GuessYourLikeTitle + "</ul>";
        Page_Index.GuessYourLikeTitle = GuessYourLikeTitle;
        //$("#guesslikefenlei").html(GuessYourLikeTitle);
    },
    /*首页推荐分类点击【4.0.6后增加，5.0后作废】*/
    LoadGuessYourLikeData_sel: function (columnName, categoryCode) {
        $("#guesslikefenlei ul").find("li").each(function () {
            if (!($(this).attr("id") == undefined)) {
                $(this).attr("class", "");
            }
        });
        $("#GuessYourLike_" + categoryCode).attr("class", "curr");

        Page_Index.GuessYourLike_categoryCode = categoryCode;
        Page_Index.GuessYourLike_keyword = columnName;

        if (Page_Index.GuessYourLike_categoryCode_old != categoryCode) {
            //初始化数据
            $("#ichsy_jyh_scroller").html("");
            Page_Index.GuessYourLike_categoryCode_old = categoryCode;
            Page_Index.GuessYourLike_categoryCode = categoryCode;
            Page_Index.GuessYourLike_pageNum = 1;
            Page_Index.GuessYourLike_TotalPages = 1;
            Page_Index.GuessYourLike_api_response = 1;
            Page_Index.GuessYourLike_check = 1;
            Page_Index.Pagination == -1;
        }
      
        //原有调用百分点接口
        //Page_Index.LoadGuessYourLikeData();
        //5.2.4 先获得MemberCode的AES加密串后调用百分点接口
        Page_Index.Load_MemberCode();
    },
    /*5.2.4 获取用户AES加密后的memberCode 开始*/
    AESMemberCode:"",
    Load_MemberCode: function () {
//        if (UserLogin.LoginStatus == g_const_YesOrNo.YES && Page_Index.AESMemberCode == "") {
//            var purl = g_INAPIUTL;
//            var request = $.ajax({
//                url: purl,
//                cache: false,
//                method: g_APIMethod,
//                data: "t=" + Math.random() + "&action=getaesmembercodebyphone&phone_no=" + UserLogin.LoginName,
//                dataType: "json"
//            });
//
//            request.done(function (msg) {
//                if (msg.resultcode == g_const_Success_Code_IN) {
//                    Page_Index.AESMemberCode = msg.resultmessage;
//                }
//                //Page_Index.LoadGuessYourLikeData();
//                /*5.3.0 获取推荐商品配置*/
//                GetRecommendConfig.LoadData(Page_Index.LoadGuessYourLikeData, Page_Index.LoadGuessYourLikeData_dg)
//            });
//
//            request.fail(function (jqXHR, textStatus) {
//                //Page_Index.LoadGuessYourLikeData();
//                /*5.3.0 获取推荐商品配置*/
//                GetRecommendConfig.LoadData(Page_Index.LoadGuessYourLikeData, Page_Index.LoadGuessYourLikeData_dg)
//            });
//        }
//        else {
//            //Page_Index.LoadGuessYourLikeData();
//            /*5.3.0 获取推荐商品配置*/
//            GetRecommendConfig.LoadData(Page_Index.LoadGuessYourLikeData, Page_Index.LoadGuessYourLikeData_dg)
//        }

    },
    /*5.2.4 获取用户AES加密后的memberCode 开始*/

    /*获取猜你喜欢数据[从com_cmall_familyhas_api_ApiRecProductInfo接口获取数据]*/
    "LoadGuessYourLikeData": function () {
        //if (Page_Index.GuessYourLike_categoryCode != "baifendian") {
        //    //其他分类查询结果
        //    Page_Index.LoadGuessYourLikeData_New();
        //}
        //else {
        if (Page_Index.GuessYourLike_check == 0) {
            return;
        }
        Page_Index.GuessYourLike_categoryCode = "baifendian"
        Page_Index.GuessYourLike_categoryCode_old = "baifendian"
        

        Page_Index.GuessYourLike_check = 0;
        /*5.1.8 支持内购，增加isPurchase*/
        Page_Index.api_input = { pageSize: "6", operFlag: "maybelove", version: 1, pageIndex: Page_Index.GuessYourLike_pageNum, navCode: Page_Index.indexChannel_Code, "isPurchase": UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0 };
        /*输入参数 5.1.8 支持内购，增加isPurchase */
        Page_Index.api_input.isPurchase = UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0;
        /*5.2.4 新增参数 开始*/
        Page_Index.api_input.uid = Page_Index.AESMemberCode;
        Page_Index.api_input.buyerType = "4497469400050002";
        Page_Index.api_input.channelNO = "appStore";//使用ios默认渠道编号
        /*5.2.4 新增参数 开始*/

        Page_Index.api_target = "../../../jsonapi/com_cmall_familyhas_api_ApiRecProductInfo";
        //  Page_Index.api_input.picWidth = 500;
        var s_api_input = JSON.stringify(Page_Index.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": Page_Index.api_target };
        var purl = g_APIUTL;
//        var request = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });
//
//        request.done(function (msg) {
//            //$("#pageloading").css("display", "none");
//            Page_Index.GuessYourLike_api_response = msg;
//            if (msg.resultCode == g_const_Success_Code) {
//                /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
//                if (msg.productMaybeLove.length == 0 && Page_Index.GuessYourLike_pageNum === 1) {
//                    $("#article_GuessYourLike").hide();
//                    $("#div_space_GuessYourLike").hide();
//                    return;
//                }
//                /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
//                if (Page_Index.Pagination == -1)
//                    Page_Index.Pagination = msg.pagination
//                if (Page_Index.GuessYourLike_pageNum <= Page_Index.Pagination || Page_Index.Pagination == -1) {
//                    Page_Index.LoadGuessYourLike();
//                    Page_Index.GuessYourLike_pageNum++;
//                    Page_Index.Pagination = msg.pagination;
//                }
//
//                //if (!$("#ichsy_jyh_wrapper").is(":hidden") && Page_Index.GuessYourLike_pageNum > Page_Index.Pagination) {
//                //    ShowMesaage(g_const_API_Message["100021"]);
//                //}
//
//            }
//            else {
//                if (!$("#ichsy_jyh_wrapper").is(":hidden")) {
//                    ShowMesaage(msg.resultMessage);
//                }
//            }
//            Page_Index.GuessYourLike_check = 1;
//        });
//
//        request.fail(function (jqXHR, textStatus) {
//            if (!$("#ichsy_jyh_wrapper").is(":hidden")) {
//                //$("#pageloading").css("display", "none");
//                ShowMesaage(g_const_API_Message["100022"]);
//            }
//        });
        //}

    },
    /*5.3.0 达观推荐接口*/
    LoadGuessYourLikeData_dg: function () {
        //if (Page_Index.GuessYourLike_categoryCode != "baifendian") {
        //    //其他分类查询结果
        //    Page_Index.LoadGuessYourLikeData_New();
        //}
        //else {
        if (Page_Index.GuessYourLike_check == 0) {
            return;
        }
        Page_Index.GuessYourLike_categoryCode = Page_Index.indexChannel_Code
        Page_Index.GuessYourLike_categoryCode_old = Page_Index.indexChannel_Code


        Page_Index.GuessYourLike_check = 0;
        Page_Index.api_input = { pageSize: 30, navCode: "", cId: "", isPurchase: 1, recommendType: 3, pageIndex: Page_Index.GuessYourLike_pageNum };
        Page_Index.api_input.isPurchase = UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0;
        Page_Index.api_input.recommendType = UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 3;
        Page_Index.api_input.navCode = Page_Index.indexChannel_Code;
        Page_Index.api_target = "../../../jsonapi/com_cmall_familyhas_api_ApiDgGetRecommend";

        if (UserLogin.LoginStatus == g_const_YesOrNo.YES) 
            Page_Index.api_input.recommendType = 1;
        else
            Page_Index.api_input.recommendType = 3;

        if (localStorage["dg_unique_id"] == null || localStorage["dg_unique_id"] == "") {
            localStorage["dg_unique_id"] = "dg-" + guid();
        }
        //唯一编号
        Page_Index.api_input.cId = localStorage["dg_unique_id"];

        //  Page_Index.api_input.picWidth = 500;
        var s_api_input = JSON.stringify(Page_Index.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": Page_Index.api_target };
        var purl = g_APIUTL;
//        var request = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });

//        request.done(function (msg) {
//            //$("#pageloading").css("display", "none");
//            Page_Index.GuessYourLike_api_response = msg;
//            if (msg.resultCode == g_const_Success_Code) {
//                if (msg.productMaybeLove.length == 0 && Page_Index.GuessYourLike_pageNum === 1) {
//                    $("#article_GuessYourLike").hide();
//                    $("#div_space_GuessYourLike").hide();
//                    return;
//                }
//
//                if (msg.pagination == 0 && msg.productMaybeLove.length > 0) {
//                    //计算总页数
//                    var pagination = parseInt(msg.productMaybeLove.length / 6);
//                    if (msg.productMaybeLove.length % 6 > 0) {
//                        pagination += 1;
//                    }
//                    msg.pagination = pagination;
//                }
//
//                if (Page_Index.Pagination == -1)
//                    Page_Index.Pagination = msg.pagination
//                if (Page_Index.GuessYourLike_pageNum <= Page_Index.Pagination || Page_Index.Pagination == -1) {
//                    Page_Index.LoadGuessYourLike_dg();
//                    Page_Index.GuessYourLike_pageNum++;
//                    Page_Index.Pagination = msg.pagination;
//                }
//
//                //if (!$("#ichsy_jyh_wrapper").is(":hidden") && Page_Index.GuessYourLike_pageNum > Page_Index.Pagination) {
//                //    ShowMesaage(g_const_API_Message["100021"]);
//                //}
//
//            }
//            else {
//                if (!$("#ichsy_jyh_wrapper").is(":hidden")) {
//                    ShowMesaage(msg.resultMessage);
//                }
//            }
//            Page_Index.GuessYourLike_check = 1;
//        });
//
//        request.fail(function (jqXHR, textStatus) {
//            if (!$("#ichsy_jyh_wrapper").is(":hidden")) {
//                //$("#pageloading").css("display", "none");
//                ShowMesaage(g_const_API_Message["100022"]);
//            }
//        });
        //}

    },

    /*获取猜你喜欢数据[4.0.6后从搜索接口获取数据，5.0后作废]*/
    "LoadGuessYourLikeData_New": function () {
        categoryCode = Page_Index.GuessYourLike_categoryCode;
        categoryName = Page_Index.GuessYourLike_keyword;

        if (Page_Index.GuessYourLike_categoryCode_old != categoryCode) {
            //初始化数据
            $("#ichsy_jyh_scroller").html("");
            Page_Index.GuessYourLike_categoryCode_old = categoryCode;
            Page_Index.GuessYourLike_categoryCode = categoryCode;
            Page_Index.GuessYourLike_pageNum = 1;
            Page_Index.GuessYourLike_TotalPages = 1;
            Page_Index.GuessYourLike_api_response = 1;
            Page_Index.GuessYourLike_check = 1;
            Page_Index.Pagination == -1;
        }

        if (Page_Index.GuessYourLike_check == 0) {
            return;
        }
        Page_Index.GuessYourLike_check = 0;
        Page_Index.api_input = { "categoryOrBrand": "category", "screenWidth": "", "pageNo": Page_Index.GuessYourLike_pageNum, "pageSize": 10, "sortType": 5, "baseValue": "", "keyWord": categoryName, "sortFlag": 2, "channelId": g_const_ChannelID, "isPurchase": UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0 };
        /*5.1.8 支持内购，增加isPurchase*/
        Page_Index.api_target = "com_cmall_productcenter_service_api_ApiSearchResults";
        //  Page_Index.api_input.picWidth = 500;
        var s_api_input = JSON.stringify(Page_Index.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": Page_Index.api_target };
        var purl = g_APIUTL;
//        var request5 = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });

//        request5.done(function (msg) {
//            //$("#pageloading").css("display", "none");
//            Page_Index.GuessYourLike_api_response = msg;
//            if (msg.resultCode == g_const_Success_Code) {
//                if (Page_Index.Pagination == -1)
//                    Page_Index.Pagination = msg.pager.pageNum
//                if (Page_Index.GuessYourLike_pageNum <= Page_Index.Pagination || Page_Index.Pagination == -1) {
//                    if (Page_Index.api_response.GuessYourLike_categoryCode == "baifendian") {
//                        //百分点推荐
//                        Page_Index.LoadGuessYourLike();
//                    }
//                    else {
//                        //其他分类查询结果
//                        Page_Index.LoadGuessYourLike_New();
//                    }
//
//                    Page_Index.GuessYourLike_pageNum++;
//                }
//                else
//                    ShowMesaage(g_const_API_Message["100021"]);
//            }
//            else {
//                ShowMesaage(msg.resultMessage);
//            }
//            Page_Index.GuessYourLike_check = 1;
//        });
//
//        request5.fail(function (jqXHR, textStatus) {
//            //$("#pageloading").css("display", "none");
//            ShowMesaage(g_const_API_Message["100022"]);
//        });


    },
    /*猜你喜欢的总页数*/
    Pagination: -1,
    /*渲染猜你喜欢【百分点推荐商品】*/
    LoadGuessYourLike: function () {
        ShowLoading("努力加载中", "divwait");
        var html = "";
        //var stpl = $("#tpl_GuessYourLike_product")[0].innerHTML;
        var stpl = $("#tpl_GuessYourLike_product_ca")[0].innerHTML;
        var guessyoulike = Page_Index.GuessYourLike_api_response;

        /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
        if (guessyoulike.productMaybeLove.length == 0 && Page_Index.GuessYourLike_pageNum == 1 && Page_Index.isfirst) {
            $("#article_GuessYourLike").hide();
            $("#div_space_GuessYourLike").hide();

        }
            /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
        else {

            for (var j = 0; j < guessyoulike.productMaybeLove.length; j++) {
                var productMaybeLove = guessyoulike.productMaybeLove[j];
                var markHtml = "";
                //3.9.4 从接口获取图片
                if (productMaybeLove.labelsPic != "" && !(typeof (productMaybeLove.labelsPic) == "undefined")) {
                    markHtml = '<img class="d_add_ys" src="' + productMaybeLove.labelsPic + '" alt="" />';
                }

                /*5.2.4 增加自营标签 proClassifyTag*/
                data = {
                    "proClassifyTag": (typeof (productMaybeLove.proClassifyTag) !== "undefined" && productMaybeLove.proClassifyTag != "") ? "<img src=\"" + productMaybeLove.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                    "a100": maidian.GetMaiDianObj_5011((Page_Index.GuessYourLike_pageNum - 1) * 6 + (j + 1).toString(), productMaybeLove.recommendId),
                    "mark": markHtml,
                    "ProductDetailURL": Page_Index.GetLocationByShowmoreLinktype(g_const_showmoreLinktype.ProductDetail, productMaybeLove.procuctCode),
                    "picture": g_GetPictrue(productMaybeLove.mainpic_url, "../../resources/cfamily/zzz_js/comment.png"),
                    "showpic": Page_Index.GuessYourLike_pageNum == 1 ? g_GetPictrue(productMaybeLove.mainpic_url, "../../resources/cfamily/zzz_js/comment.png") : "../../resources/cfamily/zzz_js/comment.png",
                    //"discount": function (a, b) {
                    //    var float_a = parseFloat(a);
                    //    var float_b = parseFloat(b);
                    //    var float_c = float_a / float_b * 10;
                    //    var s = float_c.toFixed(1).toString();
                    //    if (s == "10.0" || s == "Infinity")
                    //        s = "&nbsp;";
                    //    return s;
                    //}(productMaybeLove.productPrice, productMaybeLove.market_price),
                    "productNameString": FormatText(productMaybeLove.productNameString, 10),
                    "productPrice": productMaybeLove.productPrice,
                    "market_price": productMaybeLove.market_price,
                    "imgSize": " style=\"width:" + 0.49 * $(window).width() + "px;height:" + 0.49 * $(window).width() + "px\"",
                    "pid": productMaybeLove.procuctCode
                };
                html += renderTemplate(stpl, data);
            }
            if (Page_Index.GuessYourLike_pageNum == 1 && Page_Index.isfirst) {

                //stpl = $("#tpl_GuessYourLike")[0].innerHTML;
                //data = {
                //    "productList": html,
                //    "GuessYourLikeTitle": Page_Index.GuessYourLikeTitle
                //};

                //html = renderTemplate(stpl, data);

                var iheight;
                if (Page_Index.GuessYourLike_pageNum == 1) {
                    iheight = $(document).height();
                }
                //$("#ichsy_jyh_wrapper").css("top", iheight-90);

                //$("#bodycontent").append(html);
                $("#guesslikefenlei").html(Page_Index.GuessYourLikeTitle)
                $("#ichsy_jyh_scroller").append(html)

                Page_Index.isfirst = false;
                Page_Index.InitScroll();
                $("#divwait").hide();

                //获得分类标签距离顶部的高度
                var haha = $(".guesslikefenlei");
                var hehe = $(".guesslike_xidingtiao");

                try {
                    Page_Index.ohtH = $(".header").outerHeight();
                    Page_Index.h = haha.offset().top;
                }
                catch (e) {
                }
                //完
            }
            else {
                $("#ichsy_jyh_scroller").append(html);
                $("#divwait").hide();
            }
            //判断是否显示
            if (Page_Index.indexChannel_maybeLove == g_const_Index_ShowMaybeLove.Yes) {
                $("#article_GuessYourLike").show();
                $("#div_space_GuessYourLike").show();

                //加载完全部模板后，初始化埋点信息
                if (typeof (gas) != "undefined") {
                    if (typeof (gas.pageInit) != "undefined") {
                        gas.pageInit();
                    }
                }
            }



            //懒加载
            lazyload_allimg("ichsy_jyh_scroller", 100);

            //增加长按监听
            dqCaDivID = 'ichsy_jyh_scroller';
            AddMyCATC('ichsy_jyh_scroller');
        }
    },
    /*5.3.0 渲染猜你喜欢【达观推荐商品】*/
    LoadGuessYourLike_dg: function () {
        ShowLoading("努力加载中", "divwait");
        var html = "";
        //var stpl = $("#tpl_GuessYourLike_product")[0].innerHTML;
        var stpl = $("#tpl_GuessYourLike_product_ca")[0].innerHTML;
        var guessyoulike = Page_Index.GuessYourLike_api_response;

        /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
        if (guessyoulike.productMaybeLove.length == 0 && Page_Index.GuessYourLike_pageNum == 1 && Page_Index.isfirst) {
            $("#article_GuessYourLike").hide();
            $("#div_space_GuessYourLike").hide();

        }
            /*5.2.8 第一页且无返回商品则隐藏整个猜你喜欢  开始*/
        else {

            for (var j = 0; j < guessyoulike.productMaybeLove.length; j++) {
                var productMaybeLove = guessyoulike.productMaybeLove[j];
                var markHtml = "";
                //3.9.4 从接口获取图片
                if (productMaybeLove.labelsPic != "" && !(typeof (productMaybeLove.labelsPic) == "undefined")) {
                    markHtml = '<img class="d_add_ys" src="' + productMaybeLove.labelsPic + '" alt="" />';
                }

                /*5.2.4 增加自营标签 proClassifyTag*/
                data = {
                    "proClassifyTag": (typeof (productMaybeLove.proClassifyTag) !== "undefined" && productMaybeLove.proClassifyTag != "") ? "<img src=\"" + productMaybeLove.proClassifyTag + "\" class=\"proClassifyTag\"/>" : "",
                    "a100": maidian.GetMaiDianObj_5011((Page_Index.GuessYourLike_pageNum - 1) * 6 + (j + 1).toString(), productMaybeLove.recommendId),
                    "mark": markHtml,
                    "ProductDetailURL": Page_Index.GetLocationByShowmoreLinktype(g_const_showmoreLinktype.ProductDetail, productMaybeLove.procuctCode,'', productMaybeLove.recommendId),
                    "picture": g_GetPictrue(productMaybeLove.mainpic_url, "../../resources/cfamily/zzz_js/comment.png"),
                    "showpic": Page_Index.GuessYourLike_pageNum == 1 ? g_GetPictrue(productMaybeLove.mainpic_url, "../../resources/cfamily/zzz_js/comment.png") : "../../resources/cfamily/zzz_js/comment.png",
                    //"discount": function (a, b) {
                    //    var float_a = parseFloat(a);
                    //    var float_b = parseFloat(b);
                    //    var float_c = float_a / float_b * 10;
                    //    var s = float_c.toFixed(1).toString();
                    //    if (s == "10.0" || s == "Infinity")
                    //        s = "&nbsp;";
                    //    return s;
                    //}(productMaybeLove.productPrice, productMaybeLove.market_price),
                    "productNameString": FormatText(productMaybeLove.productNameString, 10),
                    "productPrice": productMaybeLove.productPrice,
                    "market_price": productMaybeLove.market_price,
                    "imgSize": " style=\"width:" + 0.49 * $(window).width() + "px;height:" + 0.49 * $(window).width() + "px\"",
                    "pid": productMaybeLove.procuctCode + "_" + productMaybeLove.recommendId
                };
                html += renderTemplate(stpl, data);
            }
            if (Page_Index.GuessYourLike_pageNum == 1 && Page_Index.isfirst) {

                //stpl = $("#tpl_GuessYourLike")[0].innerHTML;
                //data = {
                //    "productList": html,
                //    "GuessYourLikeTitle": Page_Index.GuessYourLikeTitle
                //};

                //html = renderTemplate(stpl, data);

                var iheight;
                if (Page_Index.GuessYourLike_pageNum == 1) {
                    iheight = $(document).height();
                }
                //$("#ichsy_jyh_wrapper").css("top", iheight-90);

                //$("#bodycontent").append(html);
                $("#guesslikefenlei").html(Page_Index.GuessYourLikeTitle)
                $("#ichsy_jyh_scroller").append(html)

                Page_Index.isfirst = false;
                Page_Index.InitScroll();
                $("#divwait").hide();

                //获得分类标签距离顶部的高度
                var haha = $(".guesslikefenlei");
                var hehe = $(".guesslike_xidingtiao");

                try {
                    Page_Index.ohtH = $(".header").outerHeight();
                    Page_Index.h = haha.offset().top;
                }
                catch (e) {
                }
                //完
            }
            else {
                $("#ichsy_jyh_scroller").append(html);
                $("#divwait").hide();
            }
            //判断是否显示
            if (Page_Index.indexChannel_maybeLove == g_const_Index_ShowMaybeLove.Yes) {
                $("#article_GuessYourLike").show();
                $("#div_space_GuessYourLike").show();

                //加载完全部模板后，初始化埋点信息
                if (typeof (gas) != "undefined") {
                    if (typeof (gas.pageInit) != "undefined") {
                        gas.pageInit();
                    }
                }
            }



            //懒加载
            lazyload_allimg("ichsy_jyh_scroller", 100);

            //增加长按监听
            dqCaDivID = 'ichsy_jyh_scroller';
            AddMyCATC('ichsy_jyh_scroller');
        }
    },

    /*猜你喜欢--分类吸顶，4.0.6后增加，5.0后作废*/
    InitScroll: function () {
        var iHeight = 0;
        var winHeight = $(window).height();
        $(window).on('scroll', function () {
            if (!$("#ichsy_jyh_wrapper").is(":hidden")) {
                //分类吸顶
                try {
                    var scrollT = $(document).scrollTop() + Page_Index.ohtH;
                    //console.log(scrollT + "," + Page_Index.h);
                    if (scrollT > Page_Index.h) {
                        $(".guesslikefenlei").remove("style");
                        //$(".guesslikefenlei").css({"position":"fixed","top":Page_Index.ohtH+"px"});			
                        $(".guesslikefenlei").attr("style", "position:fixed;top:" + Page_Index.ohtH + "px;");
                        //$(".guesslikefenlei").prop("style","background:red");
                        $(".guesslike_xidingtiao").show();
                    } else {
                        $(".guesslikefenlei").remove("style");
                        $(".guesslikefenlei").attr({ "style": "position:relative" });
                        $(".guesslike_xidingtiao").hide();
                        //获得分类标签距离顶部的高度
                        var haha = $(".guesslikefenlei");
                        var hehe = $(".guesslike_xidingtiao");

                        try {
                            Page_Index.ohtH = $(".header").outerHeight();
                            Page_Index.h = haha.offset().top;
                        }
                        catch (e) {
                        }
                        //完
                    }
                }
                catch (e) {
                    alert('错啦')
                }
                //加载数据
                var el = $(this);
                var iScrollTop = el.scrollTop();
                iHeight = $('body').height();
                if ((iScrollTop + winHeight) >= (iHeight - 10)) {
                    if (Page_Index.GuessYourLike_pageNum <= Page_Index.Pagination) {
                        //原有调用百分点接口
                        //Page_Index.LoadGuessYourLikeData();
                        //5.2.4 先获得MemberCode的AES加密串后调用百分点接口
                        Page_Index.Load_MemberCode();
                    } else {
                        $("#divwait").hide();

                        if (Page_Index.isshowTip && !$("#ichsy_jyh_wrapper").is(":hidden")) {
                            Page_Index.isshowTip = false;
                            ShowMesaage(g_const_API_Message["100021"]);

                            setTimeout(function () {
                                Page_Index.isshowTip = true;
                            }, 3000);
                        }
                    }
                }

            }
        });
    },
    isshowTip: true,
    //========首页分栏目显示  开始=============
    /*全部页签*/
    AllIndexChannelCode: [],
    //分类标签距离顶部的距离
    i_h: 20,
    indexChannelTitle: "",
    /*切换前选择的栏目编号*/
    indexChannel_Code_old: "",
    /*当前选择的栏目编号*/
    indexChannel_Code: "",
    /*当前选择的栏目名称*/
    indexChannel_Name: "",
    /*当前选择的栏目备注名称*/
    indexChannel_Name_bz: "",
    /*是否显示“猜你喜欢区域”【4497480100020002：否，4497480100020001：是】*/
    indexChannel_maybeLove: "",
    /*首页栏目返回数据*/
    indexChannel_api_response: [],
    /*防止重复*/
    indexChannel_check: 1,
    

   

    /*显示首页栏目分类【5.0后使用，待确认接口返回参数】*/
    ShowIndexChannelFenLei: function (fenlei) {
        if (localStorage["indexChannel_Code"] != null && localStorage["indexChannel_Code"] != "") {
            Page_Index.indexChannel_Code = localStorage["indexChannel_Code"];
            Page_Index.indexChannel_Name = localStorage["indexChannel_Name"];
        }

        var ChannelTitle = "";
        var selectClass_f = "";
        var html = "";
        //var fenlei = Page_Index.indexChannel_api_response;
        //判断之前选择的栏目是否还存在
        var ishaveoldchannel = false;
        if (typeof (fenlei._Description) != "undefined") {
            fenlei = JSON.parse(fenlei._Description);
        }
        $.each(fenlei.homeNavList, function (iik, nk) {
            if (Page_Index.indexChannel_Code == nk.navCode) {
                ishaveoldchannel = true;
            }
        });
        if (!ishaveoldchannel) {
            Page_Index.indexChannel_Code = "";
            Page_Index.indexChannel_Name = "";
            //Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.Yes;
        }

        stpl = $("#tpl_IndexChannel_li_50")[0].innerHTML;
        Page_Index.AllIndexChannelCode = [];
        $.each(fenlei.homeNavList, function (iik, nk) {

            //5.0.4 读取栏目名称，没有则保存备注名称
            var navNamee = nk.navName;
            //if (typeof (nk.navName) != "undefined" && nk.navName!="") {
            //    navNamee = nk.navName;
            //}
            //else {
            //    if (typeof (nk.navName_bz) != "undefined") {
            //        navNamee = nk.navName_bz;
            //    }
            //}

            if (Page_Index.indexChannel_Code == "") {
                //用户未选择栏目或栏目已失效，显示第一个栏目
                if (iik == 0) {
                    selectClass_f = "curr";
                    Page_Index.indexChannel_Code = nk.navCode;
                    Page_Index.indexChannel_Name = navNamee;
                    //Page_Index.indexChannel_maybeLove = nk.maybeLove;

                }
                else {
                    selectClass_f = "";
                }
            }
            else {
                //用户已选择栏目时
                if (Page_Index.indexChannel_Code == nk.navCode) {
                    selectClass_f = "curr";
                    Page_Index.indexChannel_Code = nk.navCode;
                    Page_Index.indexChannel_Name = navNamee;
                    //Page_Index.indexChannel_maybeLove = nk.maybeLove;

                }
                else {
                    selectClass_f = "";
                }
            }

            data = {
                "id": nk.navCode,
                "className": nk.icon == "" ? selectClass_f : "class-nav-tv " + selectClass_f,
                "ChannelCode": nk.navCode,
                "ChannelName": navNamee,
                "title": navNamee,
                "maybeLove": nk.maybeLove,
                "showpic": nk.icon == "" ? "" : "<img src=\"" + nk.icon + "\">",
                "a100": maidian.GetMaiDianObj_5013((iik + 1).toString(), navNamee),
                "num": "202997-" + (iik + 1).toString(),
                "liwidth": nk.icon == "" ? "" : " style=\"width:" + (40 / parseFloat(nk.iconHeight)) * parseFloat(nk.iconWith) + "px\"",

            };
            ChannelTitle += renderTemplate(stpl, data);

        });
        //保存栏目数组
        Page_Index.AllIndexChannelCode.push(fenlei.homeNavList);

        Page_Index.indexChannelTitle = ChannelTitle;

        stpl = $("#tpl_IndexChannel_50")[0].innerHTML;
        data = {
            "ChannelList": ChannelTitle
        };
        html = renderTemplate(stpl, data);
        $("#indexChannelArea").html(html);
        Page_Index.indexChannel_check = 1;

        //计算ul总宽度
        var aLi_width = 0;
        $(".class-nav ul li").each(function (ij, nj) {
            aLi_width += $(this).outerWidth();
        })
        aLi_width += 20;

        //横滑
        $(".class-nav ul").width(aLi_width);
        myscroll = new IScroll("#class-nav", {
            scrollX: true
            , scrollY: false
            , mouseWheel: true
            , preventDefault: false
        });
        //点击栏目事件

        $("#class-nav li a").click(function () {
            if (Page_Index.indexChannel_check == 0) {
                ShowMesaage(g_const_API_Message["106022"]);
            }
            else {

                //调用接口，加载栏目内容
                var _channelCode = $(this).attr("ChannelCode");
                var _channelName = $(this).attr("ChannelName");
                var _maybeLove = $(this).attr("maybeLove");

                if (UserLogin.LoginStatus === g_const_YesOrNo.NO) {
                    //所有人使用同一个Redis
                    Page_Index.IndexChannel_sel(_channelCode, _channelName, _maybeLove);
                }
                else {
                    //5.0.6 增加会员日折扣后，用户登录后需传递Token，不能使用redis，直接调用接口
                    Page_Index.IndexChannel_sel_API(_channelCode, _channelName, _maybeLove);
                }

                var liobj = $(this)[0].parentNode;
                $(liobj).addClass("curr").siblings().removeClass("curr");
                var win_width = $(window).width() * 0.5;
                var w_left = win_width - $(liobj).position().left - 30;
                //win_width<liobj.position().left<$("#class-nav").width()-win_width
                if ($(".class-nav ul").width() > $(".class-nav").width()) {
                    if ($(liobj).position().left < win_width) {
                        myscroll.scrollTo(0, 0, 1000, IScroll.utils.ease.elastic);
                    } else if ($(liobj).position().left > win_width && $(liobj).position().left < $("#class-nav ul").width() - win_width - 30) {
                        myscroll.scrollTo(w_left, 0, 1000, IScroll.utils.ease.elastic);
                    } else {
                        myscroll.scrollTo($(window).width() - $("#class-nav ul").width() + 20, 0, 1000, IScroll.utils.ease.elastic);
                    }
                }
            }
        });


        //显示第一个栏目内容
        if (UserLogin.LoginStatus === g_const_YesOrNo.NO) {
            //所有人使用同一个Redis
            Page_Index.IndexChannel_sel(Page_Index.indexChannel_Code, Page_Index.indexChannel_Name, Page_Index.indexChannel_maybeLove);
        }
        else {
            //5.0.6增加会员日后需传递token，不能共用redis数据
            Page_Index.IndexChannel_sel_API(Page_Index.indexChannel_Code, Page_Index.indexChannel_Name, Page_Index.indexChannel_maybeLove);
        }

        //默认加载百分点推荐商品
        //Page_Index.LoadGuessYourLikeData();

    },
    /*首页栏目分类点击【5.0后使用】*/
    IndexChannel_sel: function (ChannelCode, ChannelName, maybeLove) {
        if (Page_Index.indexChannel_check == 0) {

        }
        else {
            Page_Index.indexChannel_check = 0;

            try {
                ScollDown_Show("div_scrolldown");
            }
            catch (e) { }


            //回到顶部
            document.body.scrollTop = 0;
            //$("#indexChannel ul").find("li").each(function () {
            //	if (!($(this).attr("id") == undefined)) {
            //		$(this).attr("class", "");
            //	}
            //});
            //$("#indexChannel_" + navCode).attr("class", "curr");

            Page_Index.indexChannel_Code = ChannelCode;
            Page_Index.indexChannel_Name = ChannelName;
            //Page_Index.indexChannel_maybeLove = maybeLove;

            localStorage["indexChannel_Code"] = ChannelCode;
            localStorage["indexChannel_Name"] = ChannelName;

            //if (Page_Index.GuessYourLike_columnCode_old != columnCode) {
            //	//初始化数据
            //	$("#ichsy_jyh_scroller").html("");
            //	Page_Index.GuessYourLike_pageNum = 1;
            //	Page_Index.GuessYourLike_TotalPages = 1;
            //	Page_Index.GuessYourLike_api_response = 1;
            //	Page_Index.GuessYourLike_check = 1;
            //	Page_Index.Pagination == -1;
            //}
            //Page_Index.LoadGuessYourLikeData();

            //重新加载猜你喜欢
            Page_Index.GuessYourLike_pageNum = 1;
            Page_Index.GuessYourLike_TotalPages = 1;
            Page_Index.GuessYourLike_api_response = 1;
            Page_Index.GuessYourLike_check = 1;
            Page_Index.Pagination == -1;
            $("#ichsy_jyh_scroller").html("");



            var purl = g_INAPIUTL;
//            var request8 = $.ajax({
//                url: purl,
//                cache: false,
//                method: g_APIMethod,
//                data: "t=" + Math.random() + "&action=getindexchannelcontent&channelid=" + ChannelCode,
//                dataType: g_APIResponseDataType
//            });

//            request8.done(function (msg) {
//                if (msg._ResultCode == "0") {
//                    try {
//                        msg = JSON.parse(msg._Description);
//                    }
//                    catch (e) {
//                        Page_Index.IndexChannel_sel_API();
//                        return;
//                    }
//                    if (msg == null || typeof (msg.columnList) == "undefined") {
//                        Page_Index.IndexChannel_sel_API();
//                    }
//                    else {
//                        Page_Index.api_response = msg;
//                        $("#bodycontent").html("");
//
//                        //判断是否显示猜你喜欢
//                        if (msg.maybeLove == g_const_Index_ShowMaybeLove.No) {
//                            Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.No
//                            if (typeof ($("#article_GuessYourLike")) != "undefined") {
//                                $("#article_GuessYourLike").hide();
//                                $("#div_space_GuessYourLike").hide();
//                            }
//                        }
//                        else {
//                            Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.Yes
//
//                            if (typeof ($("#article_GuessYourLike")) != "undefined") {
//                                $("#article_GuessYourLike").show();
//                                $("#div_space_GuessYourLike").show();
//                            }
//                        }
//                        Page_Index.LoadOther();
//                    }
//                }
//                else {
//                    try {
//                        ScollDown_hide("div_scrolldown");
//                    } catch (e) { }
//                    Page_Index.indexChannel_check = 1;
//                    if (g_const_API_Message[msg.resultcode]) {
//                        ShowMesaage(g_const_API_Message[msg.resultcode]);
//                    }
//                    else {
//                        ShowMesaage(msg.resultmessage);
//                    }
//                }
//
//            });
//
//            request8.fail(function (jqXHR, textStatus) {
//                Page_Index.indexChannel_check = 1;
//                try {
//                    ScollDown_hide("div_scrolldown");
//                } catch (e) { }
//                Page_Index.IndexChannel_sel_API();
//                //ShowMesaage(g_const_API_Message["7001"]);
//            });
            //原有调用百分点接口
            //Page_Index.LoadGuessYourLikeData();
            //5.2.4 先获得MemberCode的AES加密串后调用百分点接口
            Page_Index.Load_MemberCode();
        }
    },
    IndexChannel_sel_API: function (ChannelCode, ChannelName, maybeLove) {

        Page_Index.indexChannel_check = 0;
        if (typeof (ChannelCode) != "undefined") {
            try {
                ScollDown_Show("div_scrolldown");
            }
            catch (e) { }

            //重新加载猜你喜欢
            Page_Index.GuessYourLike_pageNum = 1;
            Page_Index.GuessYourLike_TotalPages = 1;
            Page_Index.GuessYourLike_api_response = 1;
            Page_Index.GuessYourLike_check = 1;
            Page_Index.Pagination == -1;
            $("#ichsy_jyh_scroller").html("");

            //回到顶部
            document.body.scrollTop = 0;
            Page_Index.indexChannel_Code = ChannelCode;
            Page_Index.indexChannel_Name = ChannelName;

            localStorage["indexChannel_Code"] = ChannelCode;
            localStorage["indexChannel_Name"] = ChannelName;
        }
        /*5.1.8 支持内购，增加isPurchase*/
        var index_content_api_input = { "buyerType": "", "viewType": "4497471600100002", "navCode": "", "isPurchase":0 };
        index_content_api_input.navCode = Page_Index.indexChannel_Code;
        /*输入参数 5.1.8 支持内购，增加isPurchase */
        index_content_api_input.isPurchase =  0;

        var index_content_api_target = "/cfamily/jsonapi/com_cmall_familyhas_api_ApiHomeColumnNew";

        var s_api_input = JSON.stringify(index_content_api_input);
        var obj_data = { "api_input": s_api_input, "api_target": index_content_api_target };
        var purl = g_APIUTL;
        //g_APIMethod = "get";
        //purl = "/JYH/index1.txt";
//        var request88 = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });

//        request88.done(function (msg) {
//            //if (msg.resultcode) {
//            if (msg.resultCode == g_const_Success_Code) {
//                Page_Index.api_response = msg;
//                $("#bodycontent").html("");
//                //判断是否显示猜你喜欢
//                if (msg.maybeLove == g_const_Index_ShowMaybeLove.No) {
//                    Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.No
//                    if (typeof ($("#article_GuessYourLike")) != "undefined") {
//                        $("#article_GuessYourLike").hide();
//                        $("#div_space_GuessYourLike").hide();
//                    }
//                }
//                else {
//                    Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.Yes
//
//                    if (typeof ($("#article_GuessYourLike")) != "undefined") {
//                        $("#article_GuessYourLike").show();
//                        $("#div_space_GuessYourLike").show();
//                    }
//                }
//                Page_Index.LoadOther();
//            }
//            else {
//                try {
//                    ScollDown_hide("div_scrolldown");
//                } catch (e) { }
//                Page_Index.indexChannel_check = 1;
//                if (g_const_API_Message[msg.resultCode]) {
//                    ShowMesaage(g_const_API_Message[msg.resultcode]);
//                }
//                else {
//                    ShowMesaage(msg.resultMessage);
//                }
//            }
//            
//            Page_Index.Load_MemberCode();
//        });
//
//        request88.fail(function (jqXHR, textStatus) {
//            Page_Index.indexChannel_check = 1;
//            try {
//                ScollDown_hide("div_scrolldown");
//            } catch (e) { }
//            ShowMesaage(g_const_API_Message["7001"]);
//        });
    },
   
    decodeUTF8: function (str){  
    	return str.replace(/(\\u)(\w{4}|\w{2})/gi, function($0,$1,$2){  
    	    return String.fromCharCode(parseInt($2,16));  
    	});   
    	},  
    
    IndexChannel_yuLan_API:function(){

        time_point = Page_Index.up_urlparam("time_point",'');
        column_name = Page_Index.up_urlparam("column_name",'');
        column_type = Page_Index.up_urlparam("column_type",'');
        nav_code = Page_Index.up_urlparam("nav_code",'');
        nav_name= Page_Index.up_urlparam("nav_name",'');
        

        //转义
        time_point = Page_Index.decodeUTF8(decodeURIComponent(time_point));
        column_name = Page_Index.decodeUTF8(decodeURIComponent(column_name));
        column_type = Page_Index.decodeUTF8(decodeURIComponent(column_type));
        nav_code = Page_Index.decodeUTF8(decodeURIComponent(nav_code));
        nav_name = Page_Index.decodeUTF8(decodeURIComponent(nav_name));
       
        Page_Index.TP = time_point;
        
        $.ajax({
       	type:'post',
       	dataType:'json',
       	data:{},
       	url:'/cfamily/jsonapi/com_cmall_familyhas_api_ApiHomeColumnNewForYuLan?api_key=betafamilyhas&timePoint='+time_point+"&columnName="+column_name+"&columnType="+column_type+"&navCode="+nav_code,
       	success:function(msg){
      	  if (msg.resultCode == g_const_Success_Code) {
      		  
      		  console.log(JSON.stringify(msg));
              Page_Index.api_response = msg;
              $("#bodycontent").html("");
              
              var ylSubTitle =nav_name;
              var yLData =  {"homeNavList":[{"icon":"","navCode":nav_code,"navName":ylSubTitle,"position":"1"}],"resultCode":1,"resultMessage":""};
         	      Page_Index.indexChannel_api_response =yLData
                  Page_Index.ShowIndexChannelFenLei(yLData);
              //判断是否显示猜你喜欢
              if (msg.maybeLove == g_const_Index_ShowMaybeLove.No) {
                  Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.No
                  if (typeof ($("#article_GuessYourLike")) != "undefined") {
                      $("#article_GuessYourLike").hide();
                      $("#div_space_GuessYourLike").hide();
                  }
              }
              else {
                  Page_Index.indexChannel_maybeLove = g_const_Index_ShowMaybeLove.Yes

                  if (typeof ($("#article_GuessYourLike")) != "undefined") {
                      $("#article_GuessYourLike").show();
                      $("#div_space_GuessYourLike").show();
                  }
              }
              Page_Index.LoadOther();
          }
          else {
              try {
                  ScollDown_hide("div_scrolldown");
              } catch (e) { }
              Page_Index.indexChannel_check = 1;
              if (g_const_API_Message[msg.resultCode]) {
                  ShowMesaage(g_const_API_Message[msg.resultcode]);
              }
              else {
                  ShowMesaage(msg.resultMessage);
              }
          }
          
          Page_Index.Load_MemberCode();
       			
       		}})
    	
    	
    	
    },
    
    
    
    
    
    
    /*下拉刷新回调方法，不改变栏目，重新加载内容*/
    ScollDownCallBack: function () {
        if (Page_Index.indexChannel_check == 0) {

        }
        else {
            //回到顶部
            document.body.scrollTop = 0;
            //视频停止播放
            $("#myvideo_temp").attr("src", "");
            Page_Index.IndexChannel_sel_API();
        }
    },
    /*右划回调，重新加载内容*/
    ScollDownCallBack_Right: function () {
        Page_Index.indexChannel_check = 1
        var code = "";
        var name = "";
        $.each(Page_Index.AllIndexChannelCode[0], function (iik, nk) {
            if (Page_Index.indexChannel_Code == nk.navCode) {
                if (iik == 0) {
                    //第一个不能左划
                }
                else {
                    code = Page_Index.AllIndexChannelCode[0][iik - 1].navCode;
                    name = Page_Index.AllIndexChannelCode[0][iik - 1].navName;

                    var liobj = $("#" + code);
                    $(liobj).addClass("curr").siblings().removeClass("curr");
                    var win_width = $(window).width() * 0.5;
                    var w_left = win_width - $(liobj).position().left - 30;
                    //win_width<liobj.position().left<$("#class-nav").width()-win_width
                    if ($(liobj).position().left < win_width) {
                        myscroll.scrollTo(0, 0, 1000, IScroll.utils.ease.elastic);
                    } else if ($(liobj).position().left > win_width && $(liobj).position().left < $("#class-nav ul").width() - win_width - 30) {
                        myscroll.scrollTo(w_left, 0, 1000, IScroll.utils.ease.elastic);
                    } else {
                        myscroll.scrollTo($(window).width() - $("#class-nav ul").width() + 20, 0, 1000, IScroll.utils.ease.elastic);
                    }

                }

            }
        });
        Page_Index.IsCanLeftRight = true;
        if (code != "") {
            Page_Index.IndexChannel_sel_API(code, name, null)
        }

    },
    /*左划回调，重新加载内容*/
    ScollDownCallBack_Left: function () {
        Page_Index.indexChannel_check = 1
        var code = "";
        var name = "";
        $.each(Page_Index.AllIndexChannelCode[0], function (iik, nk) {
            if (Page_Index.indexChannel_Code == nk.navCode) {
                if (iik == (Page_Index.AllIndexChannelCode[0].length - 1)) {
                    //最后一个不能右划
                }
                else {
                    code = Page_Index.AllIndexChannelCode[0][iik + 1].navCode;
                    name = Page_Index.AllIndexChannelCode[0][iik + 1].navName;

                    var liobj = $("#" + code);
                    $(liobj).addClass("curr").siblings().removeClass("curr");
                    var win_width = $(window).width() * 0.5;
                    var w_left = win_width - $(liobj).position().left - 30;
                    //win_width<liobj.position().left<$("#class-nav").width()-win_width
                    if ($(liobj).position().left < win_width) {
                        myscroll.scrollTo(0, 0, 1000, IScroll.utils.ease.elastic);
                    } else if ($(liobj).position().left > win_width && $(liobj).position().left < $("#class-nav ul").width() - win_width - 30) {
                        myscroll.scrollTo(w_left, 0, 1000, IScroll.utils.ease.elastic);
                    } else {
                        myscroll.scrollTo($(window).width() - $("#class-nav ul").width() + 20, 0, 1000, IScroll.utils.ease.elastic);
                    }
                }

            }
        });
        Page_Index.IsCanLeftRight = true;
        if (code != "") {
            Page_Index.IndexChannel_sel_API(code, name, null)
        }
    },

    //======首页分栏目显示  结束============
};

function ShowLoading(message, divid) {
    // $("#" + divid).html('');
    //var body = "";
    //body += "<div id=\"pageloading\" class=\"wrap-wait\">";
    //body += "<div class=\"img\">";
    //body += "<img src=\"/img/waiting.gif\" alt=\"\" />";
    //body += "</div>";
    //body += "<p style=\"color:white\">" + message + "<br />...</p>";
    //body += "</div>";
    //body += "<div id=\"mask\" class=\"mark\" style=\"display:block;\">&nbsp;</div>";

    //$("#" + divid).html(body);
    $("#divwait").show();
}
/*更新用户地址*/
var UpdateMemberCurrentAddress = {
    api_target: "../../../jsonapi/com_cmall_familyhas_api_ApiUpdateMemberCurrentAddress",
    api_input: { "sqNum": "", "longitude": "", "latitude": "" },
    Update: function () {

        if (localStorage["mylocation_temp"] != "" && !(localStorage["mylocation_temp"] == undefined) && UserLogin.LoginStatus == g_const_YesOrNo.YES) {
            var tttm = localStorage["mylocation_temp"].split('_');
            if (tttm.length == 2) {
                //赋值
                UpdateMemberCurrentAddress.api_input.sqNum = "LSH" + Math.random();
                UpdateMemberCurrentAddress.api_input.longitude = tttm[0];
                UpdateMemberCurrentAddress.api_input.latitude = tttm[1];

                //组织提交参数
                var s_api_input = JSON.stringify(this.api_input);
                //提交接口[api_token不为空，公用方法会从sission中获取]
                var obj_data = { "api_input": s_api_input, "api_target": UpdateMemberCurrentAddress.api_target, "api_token": g_const_api_token.Wanted };
                var purl = g_APIUTL;
//                var request9 = $.ajax({
//                    url: purl,
//                    cache: false,
//                    method: g_APIMethod,
//                    data: obj_data,
//                    dataType: g_APIResponseDataType
//                });
                //正常返回
//                request9.done(function (msg) {
//                    if (msg.resultcode == g_const_Error_Code.UnLogin) {
//                        //PageUrlConfig.SetUrl();
//                        //UserRELogin.login(g_const_PageURL.Index + "?t=" + Math.random());
//                        return;
//                    }
//
//                    if (msg.resultCode == g_const_Success_Code) {
//                        UpdateMemberCurrentAddress.Load_Result(msg, UpdateMemberCurrentAddress.api_input.longitude, UpdateMemberCurrentAddress.api_input.latitude);
//                    }
//                    else {
//                        ShowMesaage(msg.resultMessage);
//                    }
//                });
//                //接口异常
//                request9.fail(function (jqXHR, textStatus) {
//                    ShowMesaage(g_const_API_Message["7001"]);
//                });
            }
        }
    },
    //接口返回成功后的处理
    Load_Result: function (msg, longitude, latitude) {
        //记录是否已获得过当前地址
        //localStorage["mylocation"] = longitude + "_" + latitude;
    },
    //接口返回失败后的处理
    Load_ResultErr: function (resultlist) {
    },
};

/*396 活动H5*/
var h5_eventUrl;
var Action396H5 = {
    api_target: "../../../jsonapi/com_cmall_familyhas_api_ApiFhAppHomeDialog",
    api_input: { "version": 1 },
    Show: function () {

        //组织提交参数
        //var s_api_input = JSON.stringify(this.api_input);
        //提交接口[api_token不为空，公用方法会从sission中获取]
        //var obj_data = { "api_input": s_api_input, "api_target": Action396H5.api_target, "api_token": g_const_api_token.Unwanted };
        var purl = g_INAPIUTL;
//        var request1 = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: "t=" + Math.random() + "&action=getindexhomedialog",
//            dataType: g_APIResponseDataType
//        });
        //正常返回
//        request1.done(function (msg) {
//            if (msg._ResultCode == g_const_Success_Code_IN.toString()) {
//                var ss = JSON.parse(msg._Description);
//                if (typeof (ss.eventUrl) == "undefined") {
//                    Action396H5.ShowAPI();
//                }
//                else {
//                    Action396H5.Load_Result(msg);
//                }
//            }
//            else {
//                ShowMesaage(msg.resultMessage);
//            }
//        });
//        //接口异常
//        request1.fail(function (jqXHR, textStatus) {
//            Action396H5.ShowAPI();
//        });
    },
    ShowAPI: function () {

        //组织提交参数
        var s_api_input = JSON.stringify(this.api_input);
        //提交接口[api_token不为空，公用方法会从sission中获取]
        var obj_data = { "api_input": s_api_input, "api_target": Action396H5.api_target, "api_token": g_const_api_token.Unwanted };
        var purl = g_APIUTL;
//        var request2 = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: obj_data,
//            dataType: g_APIResponseDataType
//        });
        //正常返回
//        request2.done(function (msg) {
//            if (msg.resultCode == g_const_Success_Code) {
//                Action396H5.Load_Result(msg);
//            }
//            else {
//                ShowMesaage(msg.resultMessage);
//            }
//        });
//        //接口异常
//        request2.fail(function (jqXHR, textStatus) {
//            ShowMesaage(g_const_API_Message["7001"]);
//        });
    },
    //接口返回成功后的处理
    Load_Result: function (msg) {
        var isshow = false;
        //判断有效性
        if (msg.eventUrl != "") {
            h5_eventUrl = msg.eventUrl;
            //时间判断
            if (My_DateCheck.CheckEX_1("<=", msg.curentTime, msg.startTime) && My_DateCheck.CheckEX_1(">", msg.curentTime, msg.endTime)) {
                //缓存判断
                if (localStorage["index_h5"] == undefined) {
                    //第一次加载时显示
                    isshow = true;
                }
                else if (localStorage["index_h5"] != (msg.startTime + msg.endTime + msg.eventUrl)) {
                    //活动有变化时重新显示
                    isshow = true;
                }
            }
        }
        if (isshow) {
            localStorage["index_h5"] = msg.startTime + msg.endTime + msg.eventUrl;
            //显示H5
            //判断url是否是图片
            var asdfff = msg.eventUrl.substr(msg.eventUrl.length - 4).toLocaleLowerCase();
            if (asdfff == ".jpg" || asdfff == ".png" || asdfff == ".gif" || asdfff == "jpeg") {
                $("#img_ifr").attr("src", msg.eventUrl);
                $("#img_ifr").show();
                $("#index_ifr").hide();
            }
            else {
                $("#index_ifr").attr("src", msg.eventUrl);
                $("#index_ifr").show();
                $("#img_ifr").hide();
            }
            $("#div_h5_396").show();

        }
    },
    //接口返回失败后的处理
    Load_ResultErr: function (resultlist) {
    },
    /*关闭弹层*/
    CloseDIV: function () {
        $("#div_h5_396").hide();
    },
    ShowH5: function () {
        if (!(typeof (h5_eventUrl) == "undefined")) {
            var asdfff = h5_eventUrl.substr(h5_eventUrl.length - 4).toLocaleLowerCase();
            if (asdfff == ".jpg" || asdfff == ".png" || asdfff == ".gif" || asdfff == "jpeg") {
                $("#img_ifr").attr("src", h5_eventUrl);
                $("#img_ifr").show();
                $("#index_ifr").hide();
            }
            else {
                $("#index_ifr").attr("src", h5_eventUrl);
                $("#index_ifr").show();
                $("#img_ifr").hide();
            }
            $("#div_h5_396").show();
        }
    },
};