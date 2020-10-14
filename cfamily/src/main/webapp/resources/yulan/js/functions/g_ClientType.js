//客户端类型
var ClientType = {
    //微信
    WeiXin: 7,
    //苹果客户端
    JYH_iOS: 3,
    //安卓客户端
    JYH_Android: 4,
    //浏览器
    Other: 1,
    //取客户端信息
    GetClientType: function () {
    var ua = navigator.userAgent;
    if (ua.match(/MicroMessenger/i) !== null)
        return ClientType.WeiXin;
    else if (ua.match(/hjy-android/i) !== null)
        return ClientType.JYH_Android;
    else if (ua.match(/hjy-ios/i) !== null)
        return ClientType.JYH_iOS;
    else
        return ClientType.Other;
}
}