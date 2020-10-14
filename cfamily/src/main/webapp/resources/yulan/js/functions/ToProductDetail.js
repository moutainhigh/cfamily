
/*可以跳转至微信小程序的页面*/
var g_const_CanToProductDetail = ["cart", "product_list", "mycollection", "myviewhistory", "mytksh_list", "myorder_list", "myorder_list_dpj", "myorder_detail", "orderconfirm", "ordersuccess", "preorderconfirm", "brandpreferencedetail", "eventproduct", "flashactive", "fullcut", "importproduct", "monthtop", "seckill", "group", "product_detail", "product_list", "search", "todaynew", "tvlive"];

$(document).ready(function () {
    if (IsInWeiXin.check()) {
        var temmp = document.location.href.toLowerCase();
        var aa = "http://" + window.location.host + "/";
        var bb = "https://" + window.location.host + "/";
        var wxready = false;
        if (temmp.indexOf(aa) > -1 || temmp.indexOf(bb) > -1) {
            //缺省首页
            wxready = true;
        }
        else {
            $.each(g_const_CanToProductDetail, function (k, o) {
                if (temmp.toLowerCase().indexOf(o) > -1) {
                    wxready = true;
                }
                if (wxready) {
                    return false;
                }
            });
        }

        if (wxready) {
            //准备微信对象
            if (typeof (wx) != "undefined") {
                WX_JSAPI.wx = wx;
                WX_JSAPI.wxparam.debug = false;
                WX_JSAPI.LoadParam(g_const_wx_AllShare);
            }
        }
    }
});

function IsInWxMiniProgram() {
    var isMiniProgram_t = false;
    if (IsInWeiXin.check()) {
        //判断是否在微信小程序中
        if (WX_JSAPI.IsInMiniProgram) {
            isMiniProgram_t = true;
        }
    }
    return isMiniProgram_t;
}
/*
5.3.0 保存达观requestId
统一跳转商品详情页方法
增加判断是否在微信小程序中，在则直接调用小程序跳转方法,5.3.0 增加达观用户行为收集
pid:商品编号
saveback:是否需要保存返回地址
other:其他Url传值得参数串【&a=1&b=2】
*/
function GoProductDetail(pid, saveback, other, requestId) {
    

    var isMiniProgram = IsInWxMiniProgram();
    
    if (isMiniProgram) {
        //跳转小程序页面
        wx.miniProgram.navigateTo({
            url: '/pages/product_detail/product_detail?pid=' + pid,
        })
    }
    else {
        //跳转商品详情
        if (saveback != undefined) {
            if (saveback == "1") {
                //保存后腿url
                PageUrlConfig.SetUrl();
            }
        }

        var url_t = "/Product_Detail.html?t=" + Math.random() + "&pid=" + pid;
        if (other != undefined) {
            if (other == true) {
                //保存后腿url
                url_t += other;
            }
        }
        //5.2.6 APP中跳转原生商品详情
        url_t += "&goods_num:" + pid;

        if (UserLogin.LoginStatus == g_const_YesOrNo.YES) {
            localStorage["dg_url_t"] = url_t;

            /*5.3.0 增加达观用户行为收集 开始*/
            if (localStorage["dg_up"] != null && localStorage["dg_up"] == "1") {
                DgUserBehaviourAPI.Clear();
                //SetList: function (actionNum, productCodes, userAction, searchTxt, dgRequestId)
                if (requestId != null && requestId != "" && requestId != undefined) {
                    DgUserBehaviourAPI.SetList(1, pid, "rec_click", "", requestId);
                }
                else {
                    DgUserBehaviourAPI.SetList(1, pid, "view", "", "");
                }
                DgUserBehaviourAPI.GetList(GoProductDetail_dg);
            }
            else {
                window.location.replace(url_t);
            }
            /*5.3.0 增加达观用户行为收集 结束*/
        }
        else {
            window.location.replace(url_t);
        }
    }
}

//5.3.0 达观用户行为提交后的回调方法
function GoProductDetail_dg() {
    window.location.replace(localStorage["dg_url_t"]);
}

