/**
* 客户端类型
* */
var ClientType = {
    /**
     * 微信
     * */
    WeiXin: 1,
    /**
     * 安卓客户端
     * */
    JYH_Android: 2,
    /**
     * 苹果客户端
     * */
    JYH_iOS: 3,
    /**
     * 其它浏览器
     * */
    Other: 4
}


/**
 * 获取客户端类型
 * */
var GetClientType = function () {
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
/**
 * APP事件处理
 * */
var JYHBridgeEvent = {

    listeners: {},

    addEvent: function (type, fn) {
        if (typeof this.listeners[type] === "undefined") {
            this.listeners[type] = [];
        }
        if (typeof fn === "function") {
            this.listeners[type].push(fn);
        }

        return this;
    },

    fireEvent: function (type, param) {
        var arrayEvent = this.listeners[type];
        if (arrayEvent instanceof Array) {
            for (var i = 0, length = arrayEvent.length; i < length; i += 1) {
                if (typeof arrayEvent[i] === "function") {
                    arrayEvent[i](param);
                }
            }
        }

        return this;
    },

    removeEvent: function (type, fn) {
        var arrayEvent = this.listeners[type];
        if (typeof type === "string" && arrayEvent instanceof Array) {
            if (typeof fn === "function") {
                for (var i = 0, length = arrayEvent.length; i < length; i += 1) {
                    if (arrayEvent[i] === fn) {
                        this.listeners[type].splice(i, 1);
                        break;
                    }
                }
            } else {
                delete this.listeners[type];
            }
        }

        return this;
    }
};
/**
 * APP客户端
 * */
var appClient = {

    /**
     * 客户端操作对象
     */
    get client() {
        var ctype = GetClientType();
        switch (ctype) {
            case ClientType.JYH_Android:
                return window.share;
            case ClientType.JYH_iOS:
                return OCModel;
            default:
                return null;
        }
    },
    /**
     * 新客户端操作对象
     */
    get newClient() {
        var ctype = GetClientType();
        switch (ctype) {
            case ClientType.JYH_Android:
                //兼容低版本和接口返回不使用新版本
                if (window.JYHEventHandler === undefined) {
                    //兼容低版本
                    return null
                }
                else {
                    //接口返回是否使用新版本
                    var obj = JSON.parse(window.JYHEventHandler.AppInfo());
                    if (obj.newView === "0") {
                        return null
                    }
                    else {
                        return window.JYHEventHandler;
                    }
                }
            //return window.JYHEventHandler ? window.JYHEventHandler : null;
            case ClientType.JYH_iOS:
                if (window.AppInfo === null || window.AppInfo === undefined) {
                    return null;
                }
                else {
                    return window.webkit.messageHandlers.JYHEventHandler;
                }
                //return window.AppInfo ? window.webkit.messageHandlers.JYHEventHandler : null;
            default:
                return null;
        }
    },
    /**
     * 获取APP对象
     * */
    getAppInfo: function () {
        var ctype = GetClientType();
        switch (ctype) {
            case ClientType.JYH_Android:
                //兼容低版本和接口返回不使用新版本
                if (window.JYHEventHandler === undefined) {
                    //兼容低版本
                    return null
                }
                else {
                    //接口返回是否使用新版本
                    var obj = JSON.parse(window.JYHEventHandler.AppInfo());
                    if (obj.newView === "0") {
                        return null
                    }
                    else {
                        return obj;
                    }
                }
            case ClientType.JYH_iOS:
                if (window.AppInfo === null || window.AppInfo === undefined) {
                    return null;
                }
                else {
                    return window.AppInfo;
                }
            default:
                return null;
        }
    },
    /**
     * 调用APP原生方法
     * @param {string} nativeMethodName 原生方法名
     * @param {any} params 客户端需要的参数
     * @param {string} callBackID 回调的事件ID
     * @param {function} successCallBack 成功的回调
     * @param {function} failureCallBack 失败的回调
     */
    callNativeFunction: function (nativeMethodName, params, callBackID, successCallBack, failureCallBack) {
        var successCallBackID = callBackID;
        successCallBackID += 'successCallBack';
        var failureCallBackID = callBackID;
        failureCallBackID += 'failureCallBack';
        var message = { 'methodName': nativeMethodName, 'params': params, 'successCallBackID': successCallBackID, 'failureCallBackID': failureCallBackID };
        var sMessage = JSON.stringify(message);

        if (successCallBack && !JYHBridgeEvent.listeners[successCallBackID]) {
            JYHBridgeEvent.addEvent(successCallBackID, successCallBack);
        }
        if (failureCallBack && !JYHBridgeEvent.listeners[failureCallBackID]) {
            JYHBridgeEvent.addEvent(failureCallBackID, failureCallBack);
        }
        var ctype = GetClientType();
        switch (ctype) {
            case ClientType.JYH_Android:
                this.newClient.postMessage(sMessage);
                break;
            case ClientType.JYH_iOS:
                this.newClient.postMessage(message);
                break;
            default:
                return null;
        }
    },
    /**
     * 基础交互方法
     * @param {string} nativeMethodName 原生方法名
     * @param {any} params  客户端需要的参数   
     * @param {function} successCallBack 成功的回调
     * @param {function} failureCallBack 失败的回调
     */
    callFunction: function (nativeMethodName, params, successCallBack, failureCallBack) {
        var callBackID = nativeMethodName + new Date().getTime().toString();
        if (!params)
            params = {};
        this.callNativeFunction(nativeMethodName, params, callBackID, successCallBack, failureCallBack);
    },
    /**
     * 设定APP分享按钮是否显示
     * @param {string} shareTitle 分享标题
     * @param {string} shareImgUrl 分享图形链接
     * @param {string} shareDesc 分享描述
     * @param {Function} succ_callback 分享成功时的回调
     * @param {Function} fail_callback 分享失败时的回调
     */
    setShare: function (shareTitle, shareImgUrl, shareDesc, shareLink, succ_callback, fail_callback) {
        var keyWord = "";
        var ctype = GetClientType();
        var param = {
            shareTitle: shareTitle,
            shareImgUrl: shareImgUrl,
            shareDesc: shareDesc,
            shareLink: shareLink
        }
        if (this.newClient !== null) {
            switch (ctype) {
                case ClientType.JYH_Android:
                    keyWord = "shareOnAndroid";
                    break;
                case ClientType.JYH_iOS:
                    keyWord = "getjsshare";
                    break;

            }
            var param = {
                shareTitle: shareTitle,
                shareImgUrl: shareImgUrl,
                shareDesc: shareDesc,
                shareLink: shareLink
            }
            this.callFunction(keyWord, param, succ_callback, fail_callback);
        }
        else {
            switch (ctype) {
                case ClientType.JYH_Android:
                    this.client.shareOnAndroid(shareTitle, shareImgUrl, shareDesc, shareLink, true);
                    break;
                case ClientType.JYH_iOS:
                    this.client.getjsshare("ios", shareTitle, shareImgUrl, shareDesc, shareLink);
                    break;
            }
        }
    },
    /**
     * 公共回调
     * @param {any} callBackID
     * @param {any} resData
     */
    publicCallback: function (callBackID, resData) {
        JYHBridgeEvent.fireEvent(callBackID, resData);
    },
    /**
     * 销毁所有回调事件
     * */
    removeAllCallBacks: function () {
        JYHBridgeEvent.listeners = {};
    },
    /**
     * 返回nav跟控制器
     * @param {string} index 0：首页 1:分类 2：购物车 3：我的    
     */
    popNavRootVCWithIndex: function (index) {
        if (this.client !== null)
            this.client.popNavRootVCWithIndex(index);
    },
    /**
     * pop上几个页面（仅苹果有，安卓未加）
     * @param {string} vcCount 返回几个页面(数字字符串)
     * @param {string} hasAnimation 是否含有动画(1为含有其它为不含有)
     */
    popVCWithVCCount: function (vcCount, hasAnimation) {
        if (this.client !== null)
            this.client.popVCWithVCCount(vcCount, hasAnimation);
    },

    /**
     * -跳转商品详情页
     * @param {string} pid 商品编号
     */
    jumpProductVC: function (pid, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                "productCode": pid
            }
            this.callFunction("jumpProductVC", param, succ_callback, fail_callback);
        }
        else {
            //无旧版方法,连接最后需要含“&goods_num:商品编号”
            var url = "/Product_Detail.html?pid=" + pid + "&goods_num:" + pid
            location.href=url; 
        }
    },
    /**
     * 在新窗口中打开链接
     * @param {string} weburl 要打开的链接
     * @param {bool} useOld 针对商品详情页的特殊处理
     */
    jumpNewWeb: function (weburl, useOld, succ_callback, fail_callback) {
        //if (this.client !== null)
        //    this.client.jumpNewWeb(weburl);

        var ctype = GetClientType();
        if (useOld) {
            switch (ctype) {
                case ClientType.JYH_Android:
                    try {
                        toActivity.jumpPage(weburl);
                    }
                    catch (e) {
                        alert("toActivity.jumpPage(" + weburl + ")报错了");
                    }
                    break;
                case ClientType.JYH_iOS:
                    //调用APP打开h5页面方法,	
                    try {
                        OCModel.jumptoPage("{"
                            + "\"jumpPage\" : \"OneDollarsnatchViewController\","
                            + "\"jumpValue\" : {"
                            + "\"webURL\" : \"" + weburl + "\""
                            + " }"
                            + "}");
                    }
                    catch (e) {
                        alert("ios:OCModel.jumptoPage({"
                            + "\"jumpPage\" : \"OneDollarsnatchViewController\","
                            + "\"jumpValue\" : {"
                            + "\"webURL\" : \"" + weburl + "\""
                            + " }"
                            + "})报错了");

                    }
                    break;
            }
        }
        else {
            if (this.newClient !== null) {
                var param = {
                    "webURL": weburl
                }
                this.callFunction("jumpNewWeb", param, succ_callback, fail_callback);

            }
            else {
                if (this.client !== null) {
                    this.client.jumpNewWeb(weburl);
                }
            }
        }
    },
    /**
     * 兑换优惠券
     * @param {string} CouponCode 优惠券编号
     */
    obtainCouponCode: function (couponCode) {
        /*if (this.client !== null)
        	 this.client.obtainCouponCode(CouponCode);*/
        var param = {
        		couponCode: couponCode
              }
        if (this.newClient !== null) {
            this.callFunction("obtainCouponCode", param, "", "");
        }
        else {
        	 this.client.obtainCouponCode(CouponCode);
        }
    },
    /**
     * 分类搜索
     * @param {string} categoryName 分类名
     * @param {string} categoryCode 分类编号
     */
    searchByCategory: function (categoryName, categoryCode, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                "categoryName": categoryName,
                "categoryCode":categoryCode
            }
            this.callFunction("searchByCategory", param, succ_callback, fail_callback);
        }
        else{
        	if (this.client !== null)
             this.client.searchByCategory(categoryName, categoryCode);
        }
    },
    /**
     * 精确搜索
     * @param {string} searchName 关键字
     */
    searchByKeyword: function (searchName, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                "keyword": searchName
            }
            this.callFunction("searchByKeyword", param, succ_callback, fail_callback);
        }
        else{
        	if (this.client !== null)
             this.client.searchByKeyword(searchName);
        }
    },
    /**
     * 橙意卡订单确认
     * @param {string} product_code 橙意卡商品编号
     * @param {string} sku_code 橙意卡商品sku编号
     */
    jumpOrangeCardOrderConfirm: function (product_code, sku_code, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                "productCode": product_code,
                "skuCode": sku_code
            }
            this.callFunction("jumpOrangeCardOrderConfirm", param, succ_callback, fail_callback);
        }
        else{
        	if (this.client !== null)
             this.client.jumpOrangeCardOrderConfirm(product_code, sku_code);
        }
    },
    /**
     * 设定下拉刷新
     * @param {string} isAdd 固定值true
     * @param {function} succ_callback
     * @param {function} fail_callback
     */
    isAddRefresh: function (isAdd, succ_callback, fail_callback) {
        var ctype = GetClientType();
        if (this.newClient !== null) {
            var param = {}
            switch (ctype) {
                case ClientType.JYH_Android:
                    this.callFunction("isNeedPullRefresh", param, succ_callback, fail_callback);                    
                    break;
                case ClientType.JYH_iOS:
                    this.callFunction("isAddRefresh", param, succ_callback, fail_callback);                    
                    break;
            }
            
        }
        else {
            if (this.client !== null) {
               
                switch (ctype) {
                    case ClientType.JYH_Android:
                        this.client.isNeedPullRefresh(isAdd);
                        break;
                    case ClientType.JYH_iOS:
                        this.client.isAddRefresh(isAdd);
                        break;
                }
                
            }
        }
    },
    /**
     * 5.4.8 -跳转订单确认页
     * @param {string} productCode 商品编号
     * @param {string} skuCode sku编号
     * @param {string} productNum 商品数量
     * @param {string} totalMoney 总价
     * @param {string} changeCode 商品兑换码
     * @param {string} activityCode 活动编号
     */
    jumpOrderConfirm: function (productCode, skuCode, productNum, totalMoney, changeCode, activityCode, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                productCode: productCode,
                skuCode: skuCode,
                productNum: productNum,
                totalMoney: totalMoney,
                changeCode: changeCode,
                activityCode: activityCode
            }
            this.callFunction("jumpOrderConfirm", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null)
                this.client.jumpOrderConfirm(productCode, skuCode, productNum, totalMoney, changeCode, activityCode);
        }
    },
    /**
     * 5.4.8 -保存图片至APP本地
     * @param {string} picUrl 图片完整地址
     */
    saveImageToGallery: function (picUrl) {
        if (this.newClient !== null) {
            var param = {
                url: picUrl
            }
            this.callFunction("saveImageToGallery", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null)
                this.client.saveImageToGallery(picUrl);
        }
    },
    /**
     * 5.5.0 -跳转APP优惠券页面
     */
    jumptoCoupon: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {

            }
            this.callFunction("jumptoCoupon", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null)
                this.client.jumptoCoupon();
        }
    },
    /**
     * 5.5.0 -跳转APP积分页面
     */
    jumpJiFenActivity: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {

            }
            this.callFunction("jumpJiFenActivity", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null)
                this.client.jumpJiFenActivity();
        }
    },
    /**
     * 5.5.0 -登录页面中如果直接关闭，此方法实现页面转向到来源页，解决APP中内嵌的H5页面无登录信息时空白问题,
     */
    isNOtLoginPopWeb: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
            }
            this.callFunction("isNOtLoginPopWeb", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.isNOtLoginPopWeb("true");
            }
        }
    },
    /*5.5.2 预作废，请准备删除 判断app登录状态，未登录唤起原生登录窗口*/
    GetPhone: function () {
        if (this.client !== null) {
            this.client.isNOtLoginPopWeb();
            var ctype = GetClientType();
            switch (ctype) {
                case ClientType.JYH_Android:
                    return this.client.getDataToJs(0);
                case ClientType.JYH_iOS:
                    //5.5.2 针对新版WebView的调用方法对象，iosAppObj中保存有页面需要的信息（版本号，手机号等等）
                    if (AppInfo) {
                        var objInfo = { "mobilephone": AppInfo.userInfo.mobilephone }
                        return objInfo;
                    }
                    else {
                        return this.client.getDataToJs(0);
                    }
            }

        }
    },
    /**
     * 5.5.4 获取app登录账号，未登录唤醒app登录,登录完成app调用成功回调，用户点关闭，app调用失败回调
     * @param {function} succ_callback 成功回调
     * @param {function} fail_callback 失败回调
     * @returns {any} 旧webview时同步返回登陆用户的手机号
     */
    Login: function (succ_callback, fail_callback) {

        if (this.newClient !== null) {
            var param = {}
            this.callFunction("getDataToJs", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.isNOtLoginPopWeb("true");
                var ctype = GetClientType();
                switch (ctype) {
                    case ClientType.JYH_Android:
                        return this.client.getDataToJs(0);//返回的是字符串
                    case ClientType.JYH_iOS:
                        return this.client.getDataToJs(0);//返回的是对象
                }
            }
            else {
                //$("#errdesp").append(" <br> client无效" + App.mobile);
            }
        }
    },
    /*5.5.4 打开和关闭APP原生等待效果方法
     flag:true：打开，false：关闭*/
    showWebLoadingAnimation: function (flag, succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {
                flag: isOpen
            }
            this.callFunction("showWebLoadingAnimation", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.showWebLoadingAnimation(flag);
            }
        }
    },
    /*5.5.4 关闭app窗口*/
    closewindow: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {

            }
            this.callFunction("closewindow", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.closewindow();
            }
        }
    },
    /*5.5.4 隐藏APP头部*/
    hidehead: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {

            }
            this.callFunction("hidehead", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.hidehead();
            }
        }
    },
    /*5.5.4 显示APP头部*/
    showhead: function (succ_callback, fail_callback) {
        if (this.newClient !== null) {
            var param = {

            }
            this.callFunction("showhead", param, succ_callback, fail_callback);
        }
        else {
            if (this.client !== null) {
                this.client.showhead();
            }
        }
    },
    /**
     * 唤醒APP分享
     * @param {string} shareTitle 分享标题
     * @param {string} shareImgUrl 分享图形链接
     * @param {string} shareDesc 分享描述
     * @param {Function} succ_callback 分享成功时的回调
     * @param {Function} fail_callback 分享失败时的回调
     */
    OpenAppShare: function (shareTitle, shareImgUrl, shareDesc, shareLink, succ_callback, fail_callback) {
        var keyWord = "";
        var ctype = GetClientType();
        var param = {
            shareTitle: shareTitle,
            shareImgUrl: shareImgUrl,
            shareDesc: shareDesc,
            shareLink: shareLink
        }
        if (this.newClient !== null) {
            switch (ctype) {
                case ClientType.JYH_Android:
                    keyWord = "shareOnDialogAndroid";
                    break;
                case ClientType.JYH_iOS:
                    keyWord = "shareweb";
                    break;

            }
            var param = {
                shareTitle: shareTitle,
                shareImgUrl: shareImgUrl,
                shareDesc: shareDesc,
                shareLink: shareLink
            }
            this.callFunction(keyWord, param, succ_callback, fail_callback);
        }
        else {
            switch (ctype) {
                case ClientType.JYH_Android:
                    this.client.shareOnDialogAndroid(shareTitle, shareImgUrl, shareDesc, shareLink);
                    break;
                case ClientType.JYH_iOS:
                    var param1 = {
                        title: shareTitle,
                        imgUrl: shareImgUrl,
                        shareContent: shareDesc,
                        shareUrl: shareLink
                    }
                    this.client.shareweb(JSON.stringify(param1));
                    break;

            }
        }
    },

};
