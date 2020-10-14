$(document).ready(function () {
    if (window.navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger') {
        WeChat.LoadParam();
    }
    $("#Button1").click(function () {
        WeChat.ScanQRCode();
    });
});
$(window).unload(function () {
    //wx.closeWindow();
});
var WeChat = {
    wx: null,
    wxparam: {
        debug: false, 
        appId: '', 
        timestamp: 0,
        nonceStr: '',
        signature: '',
        jsApiList: '' 
    },
    /*jsApiList*/
    jsApiList: [
        'checkJsApi',
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
        'onMenuShareQZone',
        'hideMenuItems',
        'showMenuItems',
        'hideAllNonBaseMenuItem',
        'showAllNonBaseMenuItem',
        'translateVoice',
        'startRecord',
        'stopRecord',
        'onVoiceRecordEnd',
        'playVoice',
        'onVoicePlayEnd',
        'pauseVoice',
        'stopVoice',
        'uploadVoice',
        'downloadVoice',
        'chooseImage',
        'previewImage',
        'uploadImage',
        'downloadImage',
        'getNetworkType',
        'openLocation',
        'getLocation',
        'hideOptionMenu',
        'showOptionMenu',
        'closeWindow',
        'scanQRCode',
        'chooseWXPay',
        'openProductSpecificView',
        'addCard',
        'chooseCard',
        'openCard'
    ],
    func_CallBack: function () { },
    /*从接口获取参数,参数jsApiList是要调用的接口名,多个逗号分隔*/
    LoadParam: function () {
        var obj_data = { action: "wxshare", jsApiList: WeChat.jsApiList.join(','), surl: window.location.href, debug: WeChat.wxparam.debug };
        var request9999 = $.ajax({
            url: g_INAPIUTL,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request9999.done(function (msg) {
            if (msg.resultcode == g_const_Success_Code_IN) {
                WeChat.wxparam.appId = msg.appId;
                WeChat.wxparam.timestamp = msg.timestamp;
                WeChat.wxparam.nonceStr = msg.nonceStr;
                WeChat.wxparam.jsApiList = msg.jsApiList;
                WeChat.wxparam.signature = msg.signature;
                wx.config(WeChat.wxparam);
            }
            else {
            }
        });

        request9999.fail(function (jqXHR, textStatus) {
        });
    },
    /*扫描二维码*/
    ScanQRCode: function (callback) {
        wx.scanQRCode({
            needResult: 1,// 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"],
            success: function (res) {
                //alert(result);
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                if (res.errMsg.toLowerCase() == "scanqrcode:ok") {
                    var str = result.split(',');
                    if (str.length > 1) {
                        window.location.replace(str[1] + "?t=" + Math.random());
                    }
                    else {
                        window.location.replace(str + "?t=" + Math.random());
                    }
                }
            }
        });
    },
    /*获取地理位置接口*/
    WX_GetLocation: function () {
        wx.getLocation({
            type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
            success: function (res) {
                if (res.errMsg.toLowerCase() == "getlocation:ok") {
                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                    var speed = res.speed; // 速度，以米/每秒计
                    var accuracy = res.accuracy; // 位置精度

                    //更新用户位置[惠家有接口]
                    localStorage["mylocation_temp"] = longitude + "_" + latitude;
                }
            }
        });
    },
    /*隐藏微信右上角按钮*/
    WX_HideOptionMenu: function () {
        wx.hideOptionMenu();
    },
    /*显示微信右上角按钮*/
    WX_ShowOptionMenu: function () {
        wx.showOptionMenu();
    },
    /*获取网络状态*/
    WX_GetNetworkType: function () {
        wx.getNetworkType({
            success: function (res) {
                var oldNet = res.networkType; // 返回网络类型2g，3g，4g，wifi
            }
        });
    },
    /*关闭当前网页窗口接口*/
    WX_CloseWindow: function () {
        wx.closeWindow();
    },
};
