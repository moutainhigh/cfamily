<!DOCTYPE html>
<html lang="en">


<head>
    <meta charset="UTF-8">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="telephone=no" name="format-detection">
    <meta content="email=no" name="format-detection" />
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <script src="../../resources/zapjs/zapjs.js"></script>
    <script src="../../resources/zapjs/zapjs.zw.js"></script>
    <script src="../../resources/yulan/js/cdn.js"></script>
    <link rel="shortcut icon" href="https://image-mall.huijiayou.cn/hjyweb/img/jyh.ico" type="image/vnd.microsoft.icon" />
    
    
   <script type="text/javascript">
    //var staticlist = [["../../resources/yulan/css/base.css", "../../resources/yulan/css/index.css", "../../resources/yulan/css/swiper.min.css", "../../resources/yulan/css/bottNav.css", "../../resources/yulan/css/index_new_video.css", "../../resources/yulan/css/proClassifyTag.css"], ["../../resources/yulan/js/jquery-2.1.4.js", "../../resources/yulan/js/g_header.js", "../../resources/yulan/js/functions/g_Const.js", "../../resources/yulan/js/functions/g_Type.js", "../../resources/yulan/js/jquery.easing.1.3.js"], []];
    //var staticlist = [["../../resources/yulan/css/base.css", "../../resources/yulan/css/index.css", "../../resources/yulan/css/swiper.min.css", "../../resources/yulan/css/bottNav.css", "../../resources/yulan/css/index_new_video.css", "../../resources/yulan/css/proClassifyTag.css"], ["../../resources/yulan/js/jquery-2.1.4.js", "../../resources/yulan/js/g_header.js", "../../resources/yulan/js/functions/g_Const.js", "../../resources/yulan/js/functions/g_Type.js", "../../resources/yulan/js/jquery.easing.1.3.js"], []];
        //WriteStatic(staticlist);
    </script>
    
    
    
    <link rel="stylesheet" type="text/css" href="../../resources/yulan/css/base.css?v=1552563555282">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/index.css?v=1552563555283">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/pt_style.css?v=1552563555283">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/swiper.min.css?v=1552563555283">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/bottNav.css?v=1552563555283">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/index_new_video.css?v=1552563555284">
<link rel="stylesheet" type="text/css" href="../../resources/yulan/css/proClassifyTag.css?v=1552563555284">
<script type="text/javascript" src="../../resources/yulan/js/jquery-2.1.4.js?v=xb1552563555284"></script>
<script type="text/javascript" src="../../resources/yulan/js/g_header.js?v=xb1552563555284"></script>
<script type="text/javascript" src="../../resources/yulan/js/functions/g_Const.js?v=xb1552563555284"></script>
<script type="text/javascript" src="../../resources/yulan/js/functions/g_Type.js?v=xb1552563555284"></script>
<script type="text/javascript" src="../../resources/yulan/js/tost.js?v=xb1552563555748?v=xb1552563555284"></script>
    

    
        <style>
        
  
        .shipinios {
            width: 100%;
            height: 2.3rem;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 98;
        }

        .fa-play-circle-o {
            display: block;
            width: 100%;
            height: 2.3rem;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 99;
            background-color: rgba(0,0,0,0.5);
            background: url(/img/bf_03.png) no-repeat center;
        }
        
        .sptitle_540 {
            font-size: 0.14rem;
            padding: 5px;
            height: 30px;
            line-height: 30px;
            overflow: hidden;
        }

        .sptitle_540_1 {
            display: inline-block;
            width: 75%;
            overflow: hidden;
            float: left;
        }

        .sptitle_540_2 {
            float: right;
            color: #f37121;
            font-weight: bold
        }

        .video_img_540 {
            width: 100%;
            z-index: 999
        }

            .video_img_540 img {
                width: 100%;
            }

        .common-ad-new {
            position: relative;
        }

        .video_540 {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 998
        }
      
    </style>
    </head>
    
    
    <body id="ddfd" style="background:#fff">






    <!--ios临时存放video-->
    <div id="ssdd" style="width:0px;height:0px;display:none;">
        <video id="myvideo_temp" width="100%" style="object-fit:fill" webkit-playsinline="true" x-webkit-airplay="true" playsinline="true" x5-video-orientation="h5" x5-video-player-type="h5" x5-video-player-fullscreen="false" preload="auto" controls autobuffer="" src=""></video>
    </div>

    <!--起始坐标-->
    <input id="touchStart" type="hidden" />
    <!--结果计算-->
    <input id="touchMove" type="hidden" />
    <!-- header srarch -->
    <!--<header class="header" module="202018">
        <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"1"}' class="hd-classify"></a>
        <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"2"}' class="hd-search">搜索商品</a>
        <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"3"}' class="user-content"></a>
    </header>-->
    <!--老版头部
    <div class="search50Wrap">
        <div class="search50">
            <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"1"}' id="search50_img"><img class="search50_img" src="/img/wza_img/search50_img01.png" style="transform: scale(1, 1); left: 0.1rem;"></a>
            <img class="search50_logo" src="/img/wza_img/search50_logo.png" style="transform: scale(1, 1); left: 50%;">
            <img class="search50_code" src="/img/wza_img/search50_code.png" id="img_scan">
            <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"2"}' id="search50_txt"><span class="search50_txt" style="display: none;">搜索商品</span></a>
            <a a100='{"20":"","21":"","22":"","23":"","60":"搜索商品","61":"","62":"3"}' id="search50_back"><span class="search50_back" style="width: 0px;"></span></a>
        </div>
    </div>-->
    <!--5.3.8 头部搜索 开始-->
    <style>
        .new_538_search {
            width: 100%;
            height: .5rem;
            background-color: #fff;
			position:fixed; top:0; left:0; z-index:9999;
        }

        .sao-sao_538 {
            width: 9%;
            float: left;
            padding: 0 2%;
        }

            .sao-sao_538 img {
                width: 0.37rem;
                margin-top: 0.08rem
            }

         .sou_ad_538 {
            width: 8%;
            float: left;
			margin-left:2%;
		
        }

            .sou_ad_538 img {
                width: 0.35rem;
				height: 0.35rem;
                margin-top: 0.08rem
            }

        
         /* 都显示  */
        .sou_538 {
            width: 74%;
            float: left;
            height: .5rem;
            position: relative
        }
		.sou_538 .input_538 {
            float: left;
            width: 86%;
            padding-left: 0.4rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		
		.sou_538 .sou_538_img {
            position: absolute;
           width: 0.2rem;
            top: 0.15rem;
            left: 0.15rem
        }
		/* 扫一扫隐藏，活动入口显示  */
		.sou_538_hidScan{
			width: 84%;
            float: left;
            height: .5rem;
            position: relative;
			margin-left:3%;
		}
		.sou_538_hidScan  .input_538 {
            float: left;
            width: 87%;
            padding-left: 0.45rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		
		.sou_538_hidScan .sou_538_img {
            position: absolute;
            width: 0.2rem;
            top: 0.15rem;
            left: 0.15rem
        }
		/* 扫一扫显示，活动入口隐藏  */
		.sou_538_hidAd{
			width: 84%;
            float: left;
            height: .5rem;
            position: relative;
		}
		.sou_538_hidAd .input_538 {
            float: left;
            width: 87%;
            padding-left: 0.45rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		
		.sou_538_hidAd .sou_538_img {
            position: absolute;
            width: 0.2rem;
            top: 0.15rem;
            left: 0.15rem
        }
		
		
		/*  扫一扫，活动入口都隐藏*/
		.sou_538_hidAll{
			width: 94%;
            float: left;
            height: .5rem;
            position: relative;
			padding:0 3%
		}
		.sou_538_hidAll .input_538 {
            float: left;
            width: 86%;
            padding-left: 0.5rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		
		.sou_538_hidAll .sou_538_img {
            position: absolute;
            width: 0.2rem;
            top: 0.15rem;
            left: 0.3rem
        }
		@media screen and (max-width: 320px) {
			.sou_538_hidScan{
			width: 80%;
            float: left;
            height: .5rem;
            position: relative;
			margin-left:3%;
		}
		  .sou_538_hidScan .input_538 {
            float: left;
            width: 82%;
            padding-left: 0.45rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		 
			 .sou_538 {
            width: 72%;
            float: left;
            height: .5rem;
            position: relative
        }
		.sou_538 .input_538 {
            float: left;
            width: 80%;
            padding-left: 0.4rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		.sou_538_hidAll .input_538 {
            float: left;
            width: 83.5%;
            padding-left: 0.5rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		.sou_538_hidAd .input_538 {
            float: left;
            width: 81%;
            padding-left: 0.45rem;
            height: 0.38rem;
            background-color: #f5f5f5;
            border-radius: 0.19rem;
            margin-top: 0.06rem
        }
		
		.sou_538_hidAd{
			width: 80%;
            float: left;
            height: .5rem;
            position: relative;
			margin-left:3%
		}
		}

       </style>
    <div class="new_538_search">
        <div class="sao-sao_538" id="img_scan" style="display:none;"> <img class="sao-sao_538_img" src="../../resources/cfamily/zzz_js/saoyisao.png" /></div>
        <div class="sou_538" id="search50_back">
            <input type="text" placeholder="搜索商品" class="input_538" />
            <img class="sou_538_img" src="../../resources/cfamily/zzz_js/saoyisao.png" />
        </div>
        <div class="sou_ad_538" id="btnsearchRiAd" style="display:none;"> <img id="PicsearchRiAd" class="sao-sao_538_img" /></div>
    </div>
    <!--5.3.8 头部搜索 结束-->
    <!-- end -->
    <!--首页栏目区域-->
    <div id="indexChannelArea" class="class-nav-wrap" module="202997">
    </div>
    <!--下拉显示的层-->
    <div id="div_scrolldown" class="d_refresh_02" style="display:none;margin-top: 45px;">
    </div>

    <div id="pydiv">
        <!--单个栏目内容区域 egin-->
        <div id="bodycontent"  style="margin-top: 40px;"></div>
        <!--单个栏目内容区域 end-->
        <!--猜你喜欢  开始-->
        <div class="space" id="div_space_GuessYourLike" style="display:none;"></div>
        <article class="module love-goods" module="202017" id="article_GuessYourLike" style="display:none;">
            <h1 class="module-hd-5">猜你喜欢</h1>
            <div id="ichsy_jyh_wrapper" class="cols-two-wrap">
                <ul class="cols-two clearfix" id="ichsy_jyh_scroller"></ul>
            </div>
        </article>
        <!--猜你喜欢  结束-->
    </div>
    <!-- 轮播swipeSlide 开始 -->
    <script type="text/template" id="tpl_swipeSlide">
        <div class="space" {{showBottomSpace}}></div>
        <div class="swiper-container" module="202001" id="div_swipeSlide">
            <div class="swiper-wrapper">
                {{swipeSlideList}}
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
    </script>
    <!--轮播swipeSlide 单个li-->
    <script type="text/template" id="tpl_swipeSlide_li">
        <div class="swiper-slide">
            <a a100='{{a100}}' {{caozuo}} title="">
                <img src="{{picture}}" alt="">
            </a>
        </div>
    </script>
    <!--轮播swipeSlide 结束-->
    <!--AD 开始-->
    <script type="text/template" id="tpl_AD">
        <div class="space" {{showBottomSpace}}></div>
        <ul class="index-ad clearfix" module="202020" id="div_TwoADs_{{id}}">
            {{ADList}}
        </ul>
    </script>
    <!--单个AD-->
    <script type="text/template" id="tpl_AD_li">
        <li>
            <a a100='{{a100}}' {{caozuo}} title="">
                <img src="{{picture}}" alt="" onload="Page_Index.SetImgHeight('div_TwoADs_{{id}}',{{xh}})" id="div_TwoADs_{{id}}_{{xh}}">
            </a>
        </li>
    </script>
    <!--5.1.4多栏广告 开始-->
    <script type="text/template" id="tpl_AD_Duo">
        <div class="space" {{showBottomSpace}}></div>
        <!--<h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}" num="202222-6">{{showmoreTitle}}</a></h1>-->
        <ul class="index-ad clearfix" module="202222" id="duo_img_{{id}}">
            {{ADList}}
        </ul>
    </script>
    <!--5.1.4多栏广告 单个AD-->
    <script type="text/template" id="tpl_AD_li_Duo">
        <li>
            <a a100='{{a100}}' {{caozuo}} title="" num="202222-{{xh}}">
                <img src="{{picture}}" alt="" onload="Page_Index.SetImgHeight('duo_img_{{id}}',{{xh}})" id="duo_img_{{id}}_{{xh}}">
            </a>
        </li>
    </script>
    <!--导航 开始-->
    <script type="text/template" id="tpl_Navigation">
        <div class="space" {{showBottomSpace}}></div>
        <article class="menu">
            <ul class="menu-list clearfix" module="202019" {{showStyle}}>
                {{NavigationList}}
            </ul>
        </article>
    </script>
    <!--单个导航按钮-->
    <script type="text/template" id="tpl_Navigation_li">
        <li class="clearfix">
            <a a100='{{a100}}' {{caozuo}} {{stylestr}} title="">
                <img src="{{picture}}" alt=""><em>{{title}}</em>
            </a>
        </li>
    </script>
    <!--导航 结束-->
    <!--=======================5.0新增模板  开始==========================-->
    <!--导航_50 开始-->
    <script type="text/template" id="tpl_Navigation_50">
        <div class="space" {{showBottomSpace}}></div>
        <div class="menu">
            <ul class="menu-list-02 clearfix" module="202019" {{showStyle}}>
                {{NavigationList}}
            </ul>
        </div>
    </script>
    <!--单个导航_50按钮-->
    <script type="text/template" id="tpl_Navigation_li_50">
        <li class="clearfix" {{li_width}}>
            <a a100='{{a100}}' {{caozuo}} {{stylestr}} title="">
                <img src="{{picture}}" alt=""><em>{{title}}</em>
            </a>
        </li>
    </script>
    <!--导航_50 结束-->
    <!--通知_50 开始-->
    <script type="text/template" id="tpl_Notice_50">
        <div class="space" {{showBottomSpace}}></div>
        <div class="menu">
            <div class="menu-msg clearfix">
                <img src="{{picture}}">
                <p class="menu-txt">{{title}}</p>
            </div>
        </div>
    </script>
    <!--通知_50 结束-->
    <!--首页栏目_50 开始-->
    <script type="text/template" id="tpl_IndexChannel_50">

        <div class="class-nav-kong"></div>
        <div id="class-nav" class="class-nav">
            <ul style="width: 822px; transition-timing-function: cubic-bezier(0.1, 0.57, 0.1, 1); transition-duration: 0ms; transform: translate(0px, 0px) translateZ(0px);">
                {{ChannelList}}
            </ul>
            <div class="mask-left"></div>
            <div class="mask-right"></div>
        </div>
    </script>
    <!--单个栏目按钮-->
    <script type="text/template" id="tpl_IndexChannel_li_50">
        <li id="{{id}}" class="{{className}}" {{liwidth}} ChannelCode="{{ChannelCode}}" ChannelName="{{ChannelName}}" maybeLove="{{maybeLove}}">
            <a a100='{{a100}}' ChannelCode="{{ChannelCode}}" ChannelName="{{ChannelName}}" maybeLove="{{maybeLove}}" num="{{num}}">
                {{showpic}}
                <span>{{title}}</span>
            </a>
        </li>
    </script>
    <!--首页栏目_50 结束-->
    <!--限时秒杀_50 -->
    <script type="text/template" id="tpl_MiaoShaRightTwo">
        <div class="space" {{showBottomSpace}}></div>
        <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
        <div class="Seckill clearfix" module="202998" id="div_MiaoShaRightTwo">
            <a a100='{{a100_1}}' class="Seckill-01" {{leftCaozuo}} title="">
                <img src="../../resources/cfamily/zzz_js/comment.png" request-url="{{leftPicture}}" alt="" {{img1}}>
                <div class="timeLimit">
                    <p class="limit" style="{{leftToptitleColor}}">{{leftTitle}}</p>
                    <ul class="time clearfix" id="djs50_{{xh}}">
                        <!--<li class="num">02</li>
                        <li class="space">:</li>
                        <li class="num">46</li>
                        <li class="space">:</li>
                        <li class="num">59</li>-->
                    </ul>
                    <p class="gameOver" style="{{leftToptitleColor1}}">{{leftToptitle2}}</p>
                </div>
            </a>
            <a a100='{{a100_2}}' class="Seckill-02" {{rightTopCaozuo}} title="">
                <img src="/img/default/tworight.png" request-url="{{rightTopPicture}}" alt="" {{img2}}>
                <h3 class="Seckill-rt-header" style="{{rightTopTitleColor}}">{{rightTopTitle}}</h3>
                <p class="Seckill-rt-txt" style="{{rightTopdDescriptionColor}}">{{rightTopdescription}}</p>
            </a>
            <a a100='{{a100_3}}' class="Seckill-03" {{rightBottomCaozuo}} title="">
                <img src="/img/default/tworight.png" request-url="{{rightBottomPicture}}" alt="" {{img2}}>
                <h3 class="Seckill-rt-header" style="{{rightBottomTitleColor}}">{{rightBottomTitle}}</h3>
                <p class="Seckill-rt-txt" style="{{rightBottomDescriptionColor}}">{{rightBottomdescription}}</p>
            </a>
        </div>
    </script>


    <!--5.4.0 秒杀模板 -->
    <script type="text/template" id="tpl_SecKill">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module ms_module" module="202011">
            <h1 class="hd-time" {{showmoreLink}}><span class="txt-ts">{{startTime}}<c>|</c>{{BeginOrEnd}}</span><span class="time-down" id="SecKill_djs_{{skid}}"><!--<em class="">21</em><b>:</b><em class="">03</em><b>:</b><em class="">12</em><b>.</b><em class="">8</em>--></span></h1>
        </article>
        <article class="module ms_module nobj" module="202011">
            <div id="SecKill_{{skid}}" style="transition-timing-function: cubic-bezier(0.1, 0.57, 0.1, 1);transition-duration: 0ms;transform: translate(0px, 0px) translateZ(0px);">
                <ul class="ms_module_ul clearfix" style="width: 740px;">
                    {{productList}}
                    <li style="{{showMoreLast}}">
                        <a a100='{{a100}}' {{showmoreLink}} num="203993-3">
                            <div class="listmore">
                                <p class="more1"> 查看更多 </p>
                                <p class="more2"> SEE ALL </p>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
        </article>
    </script>
    
    
    <!--5.4.0 秒杀模板（单个商品）-->
    <script type="text/template" id="tpl_SecKill_one">
        <li  style="width: 95px;">
            <a a100='{{a100}}' {{showmoreLink}}>
                <img 
                <#if "{{picture}}"??>
                src="{{picture}}"
                <#else>
                src="../../resources/cfamily/zzz_js/comment.png"
                </#if>
                request-url="{{picture}}">
                <p class="num1"><span>秒杀价</span>￥{{sellPrice}}</p>
                <p class="num2">￥{{markPrice}}</p>
            </a>
        </li>
    </script>


<script type="text/template" id="tpl_GroupBuy">
        <div class="space" {{showBottomSpace}}></div>
        <div class="module-lookMore-wrap" style="background:#f5f5f5">
            <!--<h1 class="module-hd-5" {{isshow}}>{{columnName}} <a a100='{{a100}}' href="/Group/GroupList.html" class="more" {{showMoreTop}}>{{showmoreTitle}}</a></h1>-->
            <div class="mrbp_ping">
                <span><img src="../../resources/cfamily/zzz_js/cnxh_03.png"></span>
                <a a100='{{a100}}' href="/GroupList.html" class=""  num="203994-2">
                 {{columnName}}
                </a>
                <span><img src="../../resources/cfamily/zzz_js/cnxh_05.png"></span>
                <!--<a a100='{{a100}}' href="/GroupList.html" class="more" {{showMoreTop}}>更多</a>-->
            </div>
            <div class="ping_list" id="MyOrder_list_article">
                {{productList}}
            </div>
        </div>
 </script>

    <!--5.4.0 拼团列表模板（单个商品）-->
    <script type="text/template" id="tpl_GroupBuy_one">
        <div class="ping_item">
            <a {{caozuo}}>
                <div class="ping_end" style="{{isLoot}}"><img alt="" src="../../resources/cfamily/zzz_js/sxk.png"></div>
                <div class="ping_img"><img src="{{picture}}" alt="" /></div>
                <div class="ping_content">
                    <p class="ping_name">{{proClassifyTag}}{{productName}}</p>
                    <p class="ping_info">{{description}}</p>
                    <p class="ping_ren"><span>{{manyCollage}}</span></p>
                    <p class="ping_price"><span></span>{{sellPrice}}</p>
                    <p class="ping_price_x"><span>{{markPrice}}</span></p>

                </div>
                <span class="ping_start"><img src="../../resources/cfamily/zzz_js/pinlist_10.jpg" alt="" /></span>
            </a>
        </div>
    </script>

    <!--新视频_50 开始-->
    <script type="text/template" id="tpl_Video_new_50">
        <div class="space" {{showBottomSpace}}></div>
        <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
        <div class="TV_bigModule_wrap" module="202995">
            <ul id="TV_bigModule-ul" class="TV_bigModule-ul clearfix">
                {{productList}}
            </ul>
        </div>
    </script>
    <!--新视频_50 单个Li 开始-->
    <script type="text/template" id="tpl_Video_new_li_50">
        <li>
            <a a100='{{a100}}' {{showmoreLink}}>

                <img src="/img/default/like.png" request-url="{{picture}}">
                <div class="TV_bigModule-cont">
                    <!--<div class="TV_bigModule-state" id="{{tvid}}">
                        <span class="living"><i>直播中00:30:23</i></span>
                        <span class="over"><i>已结束</i></span>
                        <span class="waiting"><i>即将开始</i></span>
                    </div>-->
                    <b class="TV_bigModule-play"></b>
                    <h3>{{proClassifyTag}}{{productName}}<span><i>￥</i>{{sellPrice}}</span></h3>
                </div>
            </a>
        </li>
    </script>

    <!--TV直播(新)  支持横划_50 开始-->
    <!--TV直播(新)  支持横划_50 开始-->
    <script type="text/template" id="tpl_TVLive_new_50">
        <div class="space" {{showBottomSpace}}></div>
        <div class="TV_module clearfix" module="202993">
            <h1 class="module-hd-5">{{columnName}}<a a100='{{a100}}' {{showmoreLink}} class="more" {{isshow}}>{{showmoreLinkTitle}}</a></h1>
            <div id="TV_module-ul_wrap_{{ulid}}" class="TV_module-ul_wrap" style="height: 153px;">
                <ul id="TV_module-ul_{{ulid}}" class="TV_module-ul clearfix" style="width: 752px; padding-left: 94px; padding-right: 94px; left: 1.52588e-05px;">
                    {{productList}}
                </ul>
            </div>
        </div>
    </script>
    <!--TV直播(新)  支持横划_50  单个Li开始-->
    <script type="text/template" id="tpl_TVLive_new_li_50">
        <li style="width: 188px; transform: scale(1.36363); z-index: 999; border-top: none; border-right: 1px solid rgb(255, 255, 255); border-bottom: none; border-left: 1px solid rgb(255, 255, 255); border-image: initial;">
            <a a100='{{a100}}' {{showmoreLink}}>
                <img src="{{picture}}" request-url="{{picture}}">
                <div class="TV_module-cont" style="background: rgba(0, 0, 0, 0.498039);">
                    <div class="TV_module-state" id="{{tvid}}">
                        <!--<span class="living"><i>直播中00:30:23</i></span>
                        <span class="over"><i>已结束</i></span>
                        <span class="waiting"><i>即将开始</i></span>-->
                    </div>
                    <h3>{{proClassifyTag}}<span class="txt">{{productName}}</span><span>{{sellPrice}}</span></h3>
                    <div class="TV_module-btn" {{isshowbtn}}><b>{{title}}</b></div>
                </div>
            </a>
        </li>
    </script>

    <!--=======================5.0新增模板  结束==========================-->
    <!-- @ 限时抢购 -->
    <script type="text/template" id="tpl_fastbuy">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202011">
            <h1 class="hd-time" {{showStyle}}><span class="txt">{{columnName}}</span><span class="time-down" id="fastbuy_js_{{xh}}">{{CountDown}}</span><a a100='{{a100}}' {{showmoreLink}} class="more">{{showmoreTitle}}</a></h1>
            <ul id="article_fastbuy" class="cols-three clearfix">
                {{productList}}
            </ul>
        </article>
    </script>

    <script type="text/template" id="tpl_fastbuy_productList">
        <li>
            <a a100='{{a100}}' {{showmoreLink}} title="">
                <div class="goods-item">
                    <img src="{{picture}}" request-url="{{picture}}" title="" {{imgSize}}>
                    <!-- 抢光了
                    <span class="sold-out"><em>抢光了</em></span>
                    -->
                </div>
                <em class="goods-item-sale" {{isShowDiscount}}>{{discount}}折</em>
                <h1 class="goods-item-tit">{{proClassifyTag}}{{productName}}</h1>
                <span class="goods-item-price"><i>¥</i>{{sellPrice}}</span>
            </a>
        </li>
    </script>
    <!--396 TV直播 多个 -->
    <script type="text/template" id="tpl_tvlive_396">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202010">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <section class="module-live-02 clearfix">
                <ul id="index_tvzb" style="width:450px;">
                    {{productList}}
                </ul>
            </section>
        </article>
    </script>
    <!--396 TV直播 多个_单一li-->
    <script type="text/template" id="tpl_tvlive_product_396">
        <li>
            <a a100='{{a100}}' {{showmoreLink}}>
                <div class="live-02-img">
                    <img src="/img/default/like.png" request-url="{{picture}}">
                    <span class="live-02-state">直播中</span>
                </div>
                <div class="live-02-con">
                    <p class="live-02-name">{{proClassifyTag}}{{productName}}</p>
                    <span class="live-02-price"><i>￥</i>{{sellPrice}}</span>
                </div>
            </a>
        </li>
    </script>
    <!--395 TV直播 1个-->
    <script type="text/template" id="tpl_tvlive">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202010" id="div_tvlive_one">
            <h1 class="module-hd-5">{{columnName}} <a a100='{{a100}}' href="{{showmoreLink}}" class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <section class="module-live">
                <ul id="index_tvzb" class="live-list clearfix">
                    {{productList}}
                </ul>
                <div class="live-goods-num" style="display:none;">{{NumList}}</div>
            </section>
        </article>
        <!-- end -->

    </script>
    <!--395 TV直播 1个_li版-->
    <script type="text/template" id="tpl_tvlive_product">
        <!--395 1个li版-->
        <li>
            <div class="live-goods"><a a100='{{a100}}' {{showmoreLink}}><img src="../../resources/cfamily/zzz_js/index.png" request-url="{{picture}}"></a></div>
            <div class="live-goods-info">
                <h1>{{proClassifyTag}}<a a100='{{a100}}' {{showmoreLink}}>{{productName}}</a></h1>
                <p><i class="f12">¥</i>{{sellPrice}}</p>
                <em class="live-status"><i>正在直播</i>{{startTime}}－{{endTime}}</em>
            </div>
        </li>
    </script>
    <!--396 TV直播 多个 视频数量-->
    <script type="text/template" id="tpl_tvlive_num">
        <a a100='{{a100}}' {{classcurr}}></a>
    </script>
    <!-- @ 单品模块一 -->
    <script type="text/template" id="tpl_single_one">

        <div class="space" {{showBottomSpace}}></div>
        <article class="module">
            <h1 class="module-hd-5">单品模块一 <a a100='{{a100}}' href="javascript:;" class="switch">换一换</a></h1>
            <ul class="cols-two clearfix">
                <li>
                    <a a100='{{a100}}' href="" title="">
                        <div class="goods-item">
                            <img src="./img/w-demo/demo-11.jpg" title="">
                            <!-- 抢光了 -->
                            <span class="sold-out"><em>抢光了</em></span>
                        </div>
                        <em class="goods-item-sale">5.2折</em>
                        <h1 class="goods-item-tit">摩登情人 拼接雪纺长款</h1>
                        <span class="goods-item-price"><i>¥</i>238</span>
                    </a>
                </li>
                <li>
                    <a a100='{{a100}}' href="" title="">
                        <div class="goods-item"><img src="./img/w-demo/demo-11.jpg" title=""></div>
                        <em class="goods-item-sale">5.2折</em>
                        <h1 class="goods-item-tit">摩登情人 拼接雪纺长款</h1>
                        <span class="goods-item-price"><i>¥</i>238</span>
                    </a>
                </li>
            </ul>
        </article>

        <!-- end -->
    </script>
    <!-- @ 单品模块二 -->
    <script type="text/template" id="tpl_single_two">

        <div class="space" {{showBottomSpace}}></div>
        <article class="module">
            <h1 class="module-hd-5">单品模块二 <a a100='{{a100}}' href="javascript:;" class="switch">换一换</a></h1>
            <ul class="cols-three clearfix">
                <li>
                    <a a100='{{a100}}' href="" title="">
                        <div class="goods-item">
                            <img src="./img/w-demo/demo-11.jpg" title="">
                            <!-- 抢光了 -->
                            <span class="sold-out"><em>抢光了</em></span>
                        </div>
                        <em class="goods-item-sale">5.2折</em>
                        <h1 class="goods-item-tit">摩登情人 拼接雪纺长款</h1>
                        <span class="goods-item-price"><i>¥</i>238</span>
                    </a>
                </li>
                <li>
                    <a a100='{{a100}}' href="" title="">
                        <div class="goods-item"><img src="./img/w-demo/demo-12.jpg" title=""></div>
                        <em class="goods-item-sale">5.2折</em>
                        <h1 class="goods-item-tit">摩登情人 拼接雪纺长款</h1>
                        <span class="goods-item-price"><i>¥</i>238</span>
                    </a>
                </li>
                <li>
                    <a a100='{{a100}}' href="" title="">
                        <div class="goods-item"><img src="./img/w-demo/demo-11.jpg" title=""></div>
                        <em class="goods-item-sale">5.2折</em>
                        <h1 class="goods-item-tit">摩登情人 拼接雪纺长款</h1>
                        <span class="goods-item-price"><i>¥</i>238</span>
                    </a>
                </li>
            </ul>
        </article>

        <!-- end -->
    </script>
    <!-- @ 一栏推荐 -->
    <script type="text/template" id="tpl_recommendone">

        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202005">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}}<a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <ul id="article_recommendone" class="column-one clearfix">
                <li>
                    <a a100='{{a100}}' {{pshowmoreLink}} title="">
                        <h1 style="{{titleColor}}">{{title}}</h1>
                        <p style="{{descriptionColor}}">{{description}}</p>
                        <img src="../../resources/cfamily/zzz_js/index.png" request-url="{{picture}}" alt="">
                    </a>
                </li>
            </ul>
        </article>

        <!-- end -->
    </script>
    <!-- @ 通屏广告 -->
    <script type="text/template" id="tpl_commonAD">

        <div class="space" {{showBottomSpace}}></div>
        <article class="common-ad" module="202002" id="div_commonAD">
            <a a100='{{a100}}' {{showmoreLink}} title="" style="display:block; position:relative;">
                <!--遮蔽层-->
                <span style="position:absolute;display:block; top:0; left:0; z-index:9; width:100%; height:100%;"></span>
                <img src="../../resources/cfamily/zzz_js/index.png" request-url="{{picture}}" alt="">
            </a>
        </article>

        <!-- end -->
    </script>
    <!-- @ 左或者右两栏推荐 -->
    <script type="text/template" id="tpl_recommend_leftorright_two">
        <!-- @ 左或者右两栏推荐 -->
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202006" id="article_recommend_leftorright_two">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <div class="{{classcolumn}} clearfix" id="article_recommend_leftorright_two_{{id}}">
                {{productList}}
            </div>
        </article>

        <!-- end -->
    </script>
    <script type="text/template" id="tpl_recommend_leftorright_two_product">
        <a a100='{{a100}}' {{showmoreLink}} title="">
            <!--<img src="{{picture}}" alt="" onload="Page_Index.SetImgHeight('article_recommend_leftorright_two_{{id}}',{{xh}},0.5)" id="article_recommend_leftorright_two_{{id}}_{{xh}}">-->
            <img src="{{picture}}" alt="" id="article_recommend_leftorright_two_{{id}}_{{xh}}">
            <aside class="column-dec">
                <h1 style="{{titleColor}}">{{title}}</h1>
                <p style="{{descriptionColor}}">{{description}}</p>
            </aside>
        </a>
    </script>
    <!-- @ 商品推荐 -->
    <script type="text/template" id="tpl_recommend_product">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" module="202008">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <div class="touch-wrap" id="{{touchwrapid}}">
                <ul class="goods-recomd clearfix">
                    {{productList}}
                </ul>
            </div>
        </article>
    </script>
    <!-- @ 商品推荐 单个商品-->
    <script type="text/template" id="tpl_recommend_product_product">
        <li>
            <a a100='{{a100}}' {{showmoreLink}}>
                <img src="../../resources/cfamily/zzz_js/comment.png" request-url="{{picture}}">
                <h1>{{proClassifyTag}}{{productName}}</h1>
                <span><i>¥</i>{{sellPrice}}</span>
            </a>
        </li>
    </script>

    <!-- @ 商品推荐(新) 横划到最后有查看更多链连 -->
    <script type="text/template" id="tpl_recommend_product_5">
        <div class="space" {{showBottomSpace}}></div>
        <div class="module-lookMore-wrap">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}}<a a100='{{a100}}' {{showmoreLink}} class="more" {{showMoreTop}}>{{showmoreTitle}}</a></h1>
            <div id="module-lookMore_{{divid}}" class="module-lookMore">
                <ul id="div_recommend_product_5" class="clearfix" style="width: 488px;transition-timing-function: cubic-bezier(0.1, 0.57, 0.1, 1);transition-duration: 0ms;transform: translate(0px, 0px) translateZ(0px);">
                    {{productList}}
                    <li class="lookMoreBtn" style="width: 0.24rem;{{showMoreLast}}"><a a100='{{a100}}' {{showmoreLink}}>查看全部<i>SEE ALL</i></a></li>
                </ul>
            </div>
        </div>
    </script>
    <!-- @ 商品推荐(新) 单个商品 5.2.4 增加自营标签-->
    <script type="text/template" id="tpl_recommend_product_product_5">
        <li>
            <a a100='{{a100}}' {{showmoreLink}}>
                <img src="../../resources/cfamily/zzz_js/comment.png" request-url="{{picture}}" {{imgSize}}>
                <h1>{{proClassifyTag}}{{productName}}</h1>
                <span><i>¥</i>{{sellPrice}}</span>
            </a>
        </li>
    </script>

    <!-- @ 两栏多行推荐 热门市场 -->
    <script type="text/template" id="tpl_RecommendHot">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module clearfix" module="202009">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <ul id="div_RecommendHot" class="hot-market clearfix">
                {{productList}}
            </ul>
        </article>
    </script>
    <!-- @ 两栏多行推荐 热门市场 单个商品-->
    <script type="text/template" id="tpl_RecommendHot_product">
        <li>
            <a a100='{{a100}}' {{showmoreLink}}>
                <img src="../../resources/cfamily/zzz_js/comment.png" request-url="{{picture}}" alt="" {{imgSize}}>
                <aside class="market-dec">
                    <h1 style="{{titleColor}}">{{proClassifyTag}}{{title}}</h1>
                    <p style="{{descriptionColor}}">{{description}}</p>
                </aside>
            </a>
        </li>
    </script>
    <!-- @ 猜您喜欢【分类显示】 -->
    <script type="text/template" id="tpl_GuessYourLike_415">
        <div class="space"></div>
        <article class="module love-goods" module="202017">
            <div id="guesslikefenlei" class="guesslikefenlei">{{GuessYourLikeTitle}}</div>
            <div id="ichsy_jyh_wrapper">
                <ul class="cols-two clearfix" id="ichsy_jyh_scroller">
                    {{productList}}
                </ul>
            </div>
        </article>
        <!-- end -->
    </script>
    <!-- @ 猜您喜欢【只有一个】 -->
    <script type="text/template" id="tpl_GuessYourLike">
        <div class="space" id="div_space_GuessYourLike"></div>
        <article class="module love-goods" module="202017" id="article_GuessYourLike" style="display:none;">
            <h1 class="module-hd-5">猜你喜欢</h1>
            <div id="ichsy_jyh_wrapper">
                <ul class="cols-two clearfix" id="ichsy_jyh_scroller">
                    {{productList}}
                </ul>
            </div>
        </article>
        <!-- end -->
    </script>
    <!-- @ 猜您喜欢 单个商品 -->
    <script type="text/template" id="tpl_GuessYourLike_product">
        <li>
            <a a100='{{a100}}' onclick="{{ProductDetailURL}}">
                <div class="goods-item">
                    {{mark}}
                    <img src="../../resources/cfamily/zzz_js/comment.png" request-url="{{picture}}" {{imgSize}} />
                </div>
                <h1 class="goods-item-tit-02">{{productNameString}}</h1>
                <!--<span class="goods-item-price"><i>¥</i>{{productPrice}}<b>¥{{market_price}}</b></span>-->
                <!--5.2.7-->
                <span class="goods-item-price"><i>¥</i>{{productPrice}}</span>
            </a>
        </li>
    </script>
    <!-- @ 猜您喜欢 单个商品【增加长按弹层】 -->
    <script type="text/template" id="tpl_GuessYourLike_product_ca">
        <li id="li_{{pid}}">
            <a a100='{{a100}}' id="mylike_{{pid}}" myurl="{{ProductDetailURL}}">
                <div class="goods-item" style="position: relative;">
                    {{mark}}
                    <img id="img_{{pid}}" src="{{showpic}}" request-url="{{picture}}" {{imgSize}} />
                    <!--遮蔽层-->
                    <span id="span_{{pid}}" style="position: absolute; bottom: 0;left: 0; width: 100%; height: 100%;  background: #000;opacity: 0.0;"></span>
                </div>
                <h1 class="goods-item-tit-02">
                    <!--5.2.4 增加自营标签 开始-->
                    {{proClassifyTag}}
                    <!--5.2.4 增加自营标签 结束-->
                    {{productNameString}}
                </h1>
                <!--<span class="goods-item-price"><i>¥</i>{{productPrice}}<b>¥{{market_price}}</b></span>-->
                <!--5.2.7 去除原价-->
                <span class="goods-item-price"><i>¥</i>{{productPrice}}</span>
            </a>
            <!--长按弹层，id命名规则：ca_操作类型_商品ID-->
            <div id="div_{{pid}}" style="display:none;" class="sc-bg">
                <a num="202919-1" href="javascript:void(0);" id="ca_collection_{{pid}}" class="show-sc"><img id="ca_collection_{{pid}}" src="img/ssc_03.png" /></a>
            </div>
        </li>
    </script>
    <!-- 通知模版 -->
    <script type="text/template" id="tpl_notify">
        <div class="space" {{showBottomSpace}}></div>
        <div class="headline" id="div_notify">
            <ul id="{{notifyid}}">
                {{notifylist}}
            </ul>
        </div>
    </script>
    <!-- 通知模版 单个通知内容-->
    <script type="text/template" id="tpl_notify_list">
        <li notifytype="{{notifytype}}" notifytitle="{{notifytext}}" notifybody="{{notifybody}}">
            <img src="{{notifypic}}" alt="">
            <a a100='{{a100}}' {{notifylink}}>{{notifytext}}</a>
        </li>
    </script>
    <!-- @ 两栏两行推荐 -->
    <script type="text/template" id="tpl_2L2H">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" id="div_2L2H">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <ul class="cols-two clearfix">
                {{productList}}
            </ul>
        </article>
    </script>
    <!-- @ 两栏两行推荐 单个商品-->
    <script type="text/template" id="tpl_2L2H_productList">
        <li>
            <a a100='{{a100}}' {{ProductDetailURL}}>
                <div class="goods-item">
                    <img src="../../resources/cfamily/zzz_js/index.png" request-url="{{picture}}" {{imgSize}}>
                    {{SoldOut}}
                </div>
                <em class="goods-item-sale">{{discount}}折</em>
                <h1 class="goods-item-tit">{{proClassifyTag}}{{productNameString}}</h1>
                <span class="goods-item-price"><i>¥</i>{{productPrice}}</span>
            </a>
        </li>
    </script>
    <!-- @ 三栏两行推荐 -->
    <script type="text/template" id="tpl_3L2H">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module" id="div_3L2H">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
            <ul class="cols-three two-three clearfix">
                {{productList}}
            </ul>
        </article>
    </script>
    <!-- @ 三栏两行推荐 单个商品-->
    <script type="text/template" id="tpl_3L2H_productList">
        <li>
            <a a100='{{a100}}' {{ProductDetailURL}}>
                <div class="goods-item">
                    <img src="../../resources/cfamily/zzz_js/index.png" request-url="{{picture}}">
                    {{SoldOut}}
                </div>
                <em class="goods-item-sale">{{discount}}折</em>
                <h1 class="goods-item-tit">{{proClassifyTag}}{{productNameString}}</h1>
                <span class="goods-item-price"><i>¥</i>{{productPrice}}</span>
            </a>
        </li>
    </script>
    <!-- @ 5.1.2新视频播放模板 IOS-->
    <script type="text/template" id="tpl_newvideoplay">
        <div class="space" {{showBottomSpace}}></div>
        <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}">{{showmoreTitle}}</a></h1>
        <article class="common-ad-new" id="div_commonAD{{video_id}}">
            <a title="{{title}}" class="atv" id="atvimg{{video_id}}">
                {{zb_div}}
                <img src="{{picture}}" request-url="" alt="">
                {{videoAd}}
            </a>
        </article>
        <div class="spmc" module="202991">
            <a a100='{{a100}}' {{caozuo}} num="202991-1">
                <p>{{proClassifyTag}}{{title}}</p>
                <p><span class="jage">￥{{price}}</span> {{tipstr}}</p>
            </a>
        </div>
    </script>
    <!-- @ 5.1.2新视频播放模板 安卓-->
    <script type="text/template" id="tpl_newvideoplay_az">
        <div class="space" {{showBottomSpace}}></div>
        <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}" num="202991-2">{{showmoreTitle}}</a></h1>
        <article class="common-ad-new" id="div_commonAD{{video_id}}" module="202991">
            <!--<a title="{{title}}" class="atv" id="atvimg{{video_id}}" a100='{{a100}}' {{caozuo}} num="202991-1">
                {{zb_div}}
                <img src="{{picture}}" request-url="" alt="">
                {{videoAd}}
                <p class="hdy_az_2">{{title}}</p>
                <p class="hdy_az_3"><span>￥{{price}}</span> {{proClassifyTag}}{{tipstr}}</p>
            </a>-->
            <!--5.4.0视频播放  区域  开始-->
            <div id="vvis">
                <div id="videoImg_videoID_{{video_id}}" class="video_img_540" onclick="Page_Index.VodeoPlay('{{video_id}}')">
                    <img src="{{picture}}">
                </div>
                <div id="videoDiv_videoID_{{video_id}}" class="video_540" style="display:none">
                    <!--安卓中<video id=\"videoID_"+Page_Index.newvideoMode.length+"\" controls src=\""+commonNewVideoPlay.videoLink+"\" poster=\"\" preload=\"auto\" x-webkit-airplay=\"true\" x5-playsinline=\"true\" webkit-playsinline=\"true\" playsinline=\"true\" style=\"width: 100%;\"></video>-->
                    <!--IOS中<video id=\"myvideo_"+Page_Index.newvideoMode.length+"\" width=\"100%\" style=\"object-fit:fill\" webkit-playsinline=\"true\" x-webkit-airplay=\"true\" playsinline=\"true\" x5-video-orientation=\"h5\" x5-video-player-type=\"h5\" x5-video-player-fullscreen=\"false\" preload=\"auto\" controls=\"controls\"  autobuffer><source src=\"" + commonNewVideoPlay.videoLink + "\" type=\"video/mp4\"/></video>-->
                    {{videoLink_ios}}
                </div>
                <p id="sptitle_540_videoID_{{video_id}}" class="sptitle_540" {{caozuo}}>
                    <span class="sptitle_540_1">{{title2}}</span>
                    <span class="sptitle_540_2">￥{{price}}</span>
                </p>
            </div>
            <!--视频播放  区域  结束-->
        </article>

    </script>
    <!-- @ 5.1.2新视频播放模板【无视频连接时】 -->
    <script type="text/template" id="tpl_newvideoplay_novidio">

        <a title="{{title}}" {{caozuo}} class="atv" id="atvimg{{video_id}}">
            {{zb_div}}
            <img src="{{picture}}" request-url="" alt="">

            {{videoAd}}
        </a>

    </script>

    <!--5.5.6 一行多栏模板 开始-->
    <style>
         .danpin_556 {
            padding: 0 3%;
            height: auto;
            overflow: hidden;
            background-color: #f4f4f4;
        }

        .danpin_556_div {
            padding: 10px;
            margin-top: 0.1rem;
            border-radius: 0.1rem;
            background-color: #fff;
            padding-left: 1.35rem;
            height: 1.2rem;
            position: relative;
            overflow: hidden
        }

        .danpin_556_sp_img {
            width: 1.2rem;
            height: 1.2rem;
            position: absolute;
            top: 10px;
            left: 10px;
        }

        .danpin_556_m {
            font-size: 0.16rem;
            line-height: 0.2rem;
            height: 0.4rem;
            overflow: hidden;
			overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
        }

        .danpin_556_bq {
            height: 0.2rem;
            margin-left: -0.03rem;
            margin-top: 0.02rem;
            overflow: hidden;
        }

            .danpin_556_bq span {
                border: 1px solid #f36f21;
                float: left;
                padding: 0 3px;
                line-height: 0.18rem;
                height: 0.18rem;
                display: inline-block;
                color: #f36f21;
                font-size: 0.12rem;
                border-radius: 0.04rem;
                margin-left: 0.03rem;
            }

        .danpin_556_jg {
            font-size: 0.2rem;
            font-weight: bold;
            color: #f36f21;
            margin-top: 0.2rem;
        }

            .danpin_556_jg span {
                font-size: 0.13rem;
            }

        .danpin_556_jg1 {
            font-size: 0.14rem;
            color: #ccc;
            text-decoration: line-through
        }

        .danpin_556_gm {
            display: block;
            position: absolute;
            bottom: 0.12rem;
            right: 0.1rem;
            background-color: #f36f21;
            color: #fff;
            text-align: center;
            line-height: 0.34rem;
            border-radius: 0.17rem;
            height: 0.34rem;
            width: 1.2rem;
            text-align: center;
            font-size: 0.14rem;
        }

        .danpin_556_cuxiao {
            width: 0.35rem;
            z-index: 3;
            height: 0.35rem;
            position: absolute;
            top: 0;
            left: 0;
        }

        .mt_h_556 {
            margin-top: 0.25rem !important;
        }
		
		.proClassifyTag{
			margin-top:0;}

        @media screen and (max-width: 400px) {
            .danpin_556_m {
                font-size: 0.14rem;
                line-height: 0.16rem;
                height: 0.32rem;
                overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
            }

            .danpin_556_div {
                padding: 10px;
                background-color: #fff;
                padding-left: 1.15rem;
                height: 1rem;
                position: relative;
                overflow: hidden;
			
            }

            .danpin_556_sp_img {
                width: 1rem;
                height: 1rem;
                position: absolute;
                top: 10px;
                left: 10px;
            }

            .danpin_556_jg {
                font-size: 0.16rem;
                font-weight: bold;
                color: #f36f21;
                margin-top: 0.1rem;
            }

            .mt_h_556 {
                margin-top: 0.2rem !important;
            }

            .danpin_556_gm {
                display: block;
                position: absolute;
                bottom: 0.12rem;
                right: 0.05rem;
                background-color: #f36f21;
                color: #fff;
                text-align: center;
                line-height: 0.34rem;
                border-radius: 0.17rem;
                height: 0.34rem;
                width: 0.9rem;
                text-align: center;
                font-size: 0.14rem;
            }
        }
    </style>
    <!--5.5.6 一行多栏模板（单个商品）-->
    <script type="text/template" id="tpl_Recommend1LDH_one">
        <div class="danpin_556_div" {{caozuo}}>
            <img class="danpin_556_cuxiao" src="{{labelsPic}}" alt="" style="display:{{flagTheSea}}">
            <img class="danpin_556_sp_img" src="{{picture}}" />
            <p class="danpin_556_m">{{proClassifyTag}}{{productName}}</p>
            <p class="danpin_556_bq">{{tagList}}</p>
            <p class="danpin_556_jg {{nomarkPriceClass}}" ><span>￥</span>{{sellPrice}}</p>
            <p class="danpin_556_jg1">{{markPrice}}</p>
            <a {{caozuo}} num=""  class="danpin_556_gm">立即购买 ></a>
        </div>
    </script>
    <script type="text/template" id="tpl_Recommend1LDH">
        <div class="space" {{showBottomSpace}}></div>
        <article class="module clearfix" module="">
            <h1 class="module-hd-5" {{showStyle}}>{{columnName}} <a a100='{{a100}}' {{showmoreLink}} class="{{classmore}}" style="float:right; margin-right:2%;color:#333;{{showMoreTop}}">{{showmoreTitle}} ></a></h1>
            <div class="danpin_556">
                {{productList}}
            </div>
        </article>
    </script>
    <!--5.5.6 一行多栏模板 结束-->

    <!--打开App 开始 /*5.2.8 控制APP浮层*/-->
    <div class="openAPP" module="202994" style="display:none;"></div>
    <div class="upAPP"></div>
    <!--打开App 结束-->
    <!--底部导航 开始-->
    <div class="footer-menu-wrap" module="202996">
        <ul class="footer-menu" id="ul_footerMenu"></ul>
    </div>
    <!--底部导航 结束-->
    <!--加载等待-->
    <div id="" style="display:none;">正在加载请稍后</div>
    <a a100='{"20":"","21":"","22":"","23":"","60":"返回头部","61":"","62":"1"}' class="scroll-top" num="202101-1"></a>
    <!--<a a100='{{a100}}'  href="cart.html" num="202101-2" class="shop-cart"><em></em></a>-->
    <footer class="copyright">
        <a a100='{{a100}}' onclick="window.location.reload(true);" num="202101-3"></a>
    </footer>
    <!-- 引导页 -->
    <aside a100='{{a100}}' side class="guide-app" module="202102" style="display:none">
        <img id="img_guide" src="" alt="">
        <a a100='{{a100}}' href="javascript:;" onclick="openApp();"></a>
        <a a100='{{a100}}' href="javascript:;" class="guide-close"></a>
    </aside>
    <aside a100='{{a100}}' side class="mask" style="display:none"></aside>
    <!-- end -->
    <script type="text/javascript">
        //var staticlist = [[], ["../../resources/yulan/js/tost.js", "../../resources/yulan/js/swipeSlide.min.js", "../../resources/yulan/js/swiper.min.js", "../../resources/yulan/js/iscroll.js", "../../resources/yulan/js/jquery.w.js", "../../resources/yulan/js/functions/catc.js", "../../resources/yulan/js/pages/index5_0.js", "../../resources/yulan/js/pages/FooterMenu.js", "../../resources/yulan/js/functions/g_WeChat.js", "../../resources/yulan/js/pages/OpenApp50.js", "../../resources/yulan/js/pages/guide.js", "../../resources/yulan/js/shareGoodsDetail.js", "../../resources/yulan/js/g_footer.js", "../../resources/yulan/js/Waiting.js", "../../resources/yulan/js/shareGoodsDetail.js"]];
        var staticlist = [[], [ "../../resources/yulan/js/swipeSlide.min.js", "../../resources/yulan/js/swiper.min.js", "../../resources/yulan/js/iscroll.js", "../../resources/yulan/js/jquery.w.js", "../../resources/yulan/js/pages/index5_0.js"]];
        WriteStatic(staticlist);
    </script>
    <!--<script type="text/javascript" src="https://cps.huijiayou.cn/HJY/jyhshop/merchant_act.js"></script>-->
    <script>
        Page_Index.Init();
        Page_Index.IndexChannel_yuLan_API();
    </script>
    
    <!--=通知详情 star=-->
    <div class="notice" style="display:none;" id="div_notice">
        <i></i>
        <div class="cont">
            <h2 id="notice_title">通知标题</h2>
            <p id="notice_body">通知内容</p>
        </div>
    </div>
    <!--=end =-->
    <!--396 首页H5动画及遮罩 开始-->
    <div class="h5_mask" style="display:none;" id="div_h5_396" module="202999">
        <iframe src="" frameborder="0" id="index_ifr" name="ifr" style="overflow:hidden;overflow-x:hidden;overflow-y:hidden;height:100%;width:100%;position:absolute;top:43px;left:0px;right:0px;bottom:0px"></iframe>
        <img src="" id="img_ifr" style="display:none;" />
        <a id="h5close" onclick="Action396H5.CloseDIV()"><b class="dia_h5_close"></b></a>
    </div>
    <!--396 首页H5动画及遮罩 结束-->
    <!--双球切换 等待效果-->
    <div class="customs-pop" style="display:none;" id="divwait">
        <i></i>
        <div class="waiting_bf">
            <span class="w_red"></span>
            <span class=""></span>
        </div>
    </div>
    <!--end(双球切换 等待效果)-->
</body>


<#-- 获取屏幕宽度  -->

</html>

