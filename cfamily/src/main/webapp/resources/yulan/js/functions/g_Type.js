﻿/// <reference path="g_Const.js" />
/// <reference path="../g_header.js" />
/// <reference path="../jquery-2.1.4.js" />
/// <reference path="../ShortURL.js" />

/*类型定义*/

/*购物车*/
var g_type_cart = {
    //购物车版本
    Version: {
        V1: 1,
        V2: 2
    },
    //当前使用版本
    GetVersion: function () {
        return g_type_cart.Version.V2;
    },
    /*本地购物车精简版*/
    LocalCart: function () {
        var scart = localStorage[g_const_localStorage.Cart];
        if (typeof (scart) != "undefined") {
            return JSON.parse(scart);
        }
        else
            return null;
    },
    /*本地购物车完整版*/
    LocalCartFull: function () {
        if (g_type_cart.GetVersion() == g_type_cart.Version.V2)
            return null;
        var scart = localStorage[g_const_localStorage.CartFull];
        if (typeof (scart) != "undefined") {
            return JSON.parse(scart);
        }
        else
            return null;
    },
    /*云端购物车*/
    CloudCart: [],
    SalesAdv: function () {
        var scart = localStorage[g_const_localStorage.CartSalesAdv];
        if (typeof (scart) != "undefined") {
            return scart;
        }
        else
            return "所有商品全场包邮(西藏、新疆地区除外)";
    }(),
    /*同步购物车*/
    SyncCart: function () {
        if (g_type_cart.CloudCart.length != 0) {
            g_type_cart.Clear();
            for (var i = g_type_cart.CloudCart.length - 1; i >= 0 ; i--) {
                var objcartfull = g_type_cart.CloudCart[i];
                g_type_cart.ADD(objcartfull, false);
            }
        }
    },
    /*清空本地购物车*/
    Clear: function () {
        try {
            localStorage.removeItem(g_const_localStorage.Cart);
            localStorage.removeItem(g_const_localStorage.CartFull);
        }
        catch (e) {
            console.log("清空本地购物车出现错误：" + e);
        }
    },
    /*清空本地和云端购物车*/
    ClearWithCloud: function () {
        var objLocalCart = g_type_cart.LocalCart();
        if (objLocalCart != null) {
            for (var i = 0; i < objLocalCart.GoodsInfoForAdd.length; i++) {
                objLocalCart.GoodsInfoForAdd[i].sku_num = 0;
            }
            localStorage[g_const_localStorage.Cart] = JSON.stringify(objLocalCart);
            g_type_cart.Upload();
        }
        g_type_cart.Clear();
    },
    /*单个产品的购物车对象*/
    objCartFull: {
        /*购买数量*/
        sku_num: 0,
        /*地区编号,可不填写，添加购物车不再需要区域编号*/
        area_code: "",
        /*商品编号*/
        product_code: "",
        /*sku编号*/
        sku_code: "",
        /*商品属性,商品规格,商品款式*/
        sku_property: [],
        /*其他相关显示语,比如赠品*/
        otherShow: "",
        /*商品价格*/
        sku_price: 0.0,
        /*促销描述*/
        sales_info: "",
        /*库存*/
        sku_stock: 0,
        /*促销种类*/
        sales_type: "",
        /*商品图片链接*/
        pic_url: "",
        /*每单限购数量*/
        limit_order_num: 0,
        /*库存是否足够*/
        flag_stock: g_const_YesOrNo.NO,
        /*商品活动相关显示语*/
        activitys: [],
        /*是否有效商品*/
        flag_product: g_const_YesOrNo.NO,
        /*商品名称(包含选中的SKU)*/
        sku_name: "",
        /*是否是海外购*/
        flagTheSea: "0",
        //是否选择	
        chooseFlag: g_const_YesOrNo.NO.toString(),
    },
    /*加入购物车,isModify为true代表修改,否则为增加*/
    ADD: function (objcartfull,isModify,isUpdateCar) {
        /*精简版单个产品购物车对象*/
        var objcart = {};
        if (g_type_cart.GetVersion() == g_type_cart.Version.V1) {
            objcart = {
                /*商品数量*/
                "sku_num": objcartfull.sku_num,
                /*地区编号,可不填写，添加购物车不再需要区域编号*/
                "area_code": objcartfull.area_code,
                /*商品编号*/
                "product_code": objcartfull.product_code,
                /*sku编号*/
                "sku_code": objcartfull.sku_code,
                //是否选择
                chooseFlag: objcartfull.chooseFlag,
            };
        } else if (g_type_cart.GetVersion() == g_type_cart.Version.V2) {
            objcart = {
                /*商品数量*/
                "sku_num": objcartfull.sku_num,
                /*商品编号*/
                "product_code": objcartfull.product_code,
                /*sku编号*/
                "sku_code": objcartfull.sku_code,
                //是否选择
                chooseFlag: objcartfull.chooseFlag,

            };
        }

        var objcarts = {
            "GoodsInfoForAdd": []
        };
        var objcartsfull = {
            "GoodsInfoForQuery": []
        };

        var scarts = localStorage[g_const_localStorage.Cart];
        if (typeof (scarts) != "undefined" && scarts != "null") {
            objcarts = JSON.parse(scarts);
        }

        var scartsfull = localStorage[g_const_localStorage.CartFull];
        if (typeof (scartsfull) != "undefined" && scartsfull != "null") {
            objcartsfull = JSON.parse(scartsfull);
        }

        var productisexist = false;
        for (var i = 0; i < objcarts.GoodsInfoForAdd.length; i++) {
            var objexistcart = objcarts.GoodsInfoForAdd[i];
            if (typeof (objexistcart) != "undefined") {
                if (objexistcart.product_code == objcart.product_code && objexistcart.sku_code == objcart.sku_code) {
                    if (!isModify) {
                        objcarts.GoodsInfoForAdd[i].sku_num += objcart.sku_num;
                        objcarts.GoodsInfoForAdd[i].chooseFlag = "1";
                        if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                            objcartsfull.GoodsInfoForQuery[i].sku_num += objcart.sku_num;
                    }
                    else {
                        objcarts.GoodsInfoForAdd[i].sku_num = objcart.sku_num;
                        if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                            objcartsfull.GoodsInfoForQuery[i].sku_num = objcart.sku_num;
                    }
                    if (g_type_cart.GetVersion() == g_type_cart.Version.V1) {
                        objcarts.GoodsInfoForAdd[i].area_code = objcart.area_code;
                        objcartsfull.GoodsInfoForQuery[i].area_code = objcart.area_code;
                    }
                    productisexist = true;
                    break;
                }
            }
            else
                break;
        }
        if (!productisexist) {
            if (objcarts.GoodsInfoForAdd.length >= 99) {
                objcarts.GoodsInfoForAdd.splice(objcarts.GoodsInfoForAdd.length - 1, 1);
                objcartsfull.GoodsInfoForQuery.unshift(objcartfull);
            }
            else {
                objcarts.GoodsInfoForAdd.unshift(objcart);
                objcartsfull.GoodsInfoForQuery.unshift(objcartfull);
            }
        }

        try {
            if (g_type_cart.GetVersion() == g_type_cart.Version.V1) {
                localStorage[g_const_localStorage.CartFull] = JSON.stringify(objcartsfull);
            }
            localStorage[g_const_localStorage.Cart] = JSON.stringify(objcarts);
        } catch (oException) {
            if (oException.name == 'QuotaExceededError') {

            }
        }


        //同步服务器,向云端同步本地购物车*/
        if (!(isUpdateCar == undefined)) {
            g_type_cart.Upload();
        }
    },
    /*从本地购物车中移除*/
    Remove: function (product_code, sku_code) {
        var objLocalCart = g_type_cart.LocalCart();
        var objLocalCartFull = g_type_cart.LocalCartFull();
        if (objLocalCart != null) {

            for (var i = 0; i < objLocalCart.GoodsInfoForAdd.length; i++) {
                var objcart = objLocalCart.GoodsInfoForAdd[i];
                if (objcart.product_code == product_code && objcart.sku_code == sku_code) {
                    objLocalCart.GoodsInfoForAdd.splice(i, 1);
                    if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                        objLocalCartFull.GoodsInfoForQuery.splice(i, 1);
                    break;
                }
            }
            localStorage[g_const_localStorage.Cart] = JSON.stringify(objLocalCart);
            if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                localStorage[g_const_localStorage.CartFull] = JSON.stringify(objLocalCartFull);
        }
    },
    /*从本地购物车和云端中移除*/
    RemoveWithCloud: function (product_code, sku_code) {
        var objLocalCart = g_type_cart.LocalCart();
        var objLocalCartFull = g_type_cart.LocalCartFull();
        if (objLocalCart != null) {

            for (var i = 0; i < objLocalCart.GoodsInfoForAdd.length; i++) {
                var objcart = objLocalCart.GoodsInfoForAdd[i];
                if (objcart.product_code == product_code && objcart.sku_code == sku_code) {
                    objLocalCart.GoodsInfoForAdd[i].sku_num = 0;
                    if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                        objLocalCartFull.GoodsInfoForQuery[i].sku_num = 0;
                    break;
                }
            }
        }
        localStorage[g_const_localStorage.Cart] = JSON.stringify(objLocalCart);
        if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
            localStorage[g_const_localStorage.CartFull] = JSON.stringify(objLocalCartFull);
        g_type_cart.Upload();
        g_type_cart.Remove(product_code, sku_code);
    },
    /*从本地购物车和云端中批量移除,arrobjproduct格式：[[product_code, sku_code],[product_code, sku_code]]*/
    BatchRemoveWithCloud: function (arrobjproduct, callback) {
        var objLocalCart = g_type_cart.LocalCart();
        var objLocalCartFull = g_type_cart.LocalCartFull();
        if (objLocalCart != null) {

            for (var i = 0; i < objLocalCart.GoodsInfoForAdd.length; i++) {
                var objcart = objLocalCart.GoodsInfoForAdd[i];
                for (var j = 0; j < arrobjproduct.length; j++) {
                    var product_code = arrobjproduct[j][0];
                    var sku_code = arrobjproduct[j][1];
                    if (objcart.product_code == product_code && objcart.sku_code == sku_code) {
                        objLocalCart.GoodsInfoForAdd[i].sku_num = 0;
                        if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
                            objLocalCartFull.GoodsInfoForQuery[i].sku_num = 0;
                        break;
                    }
                }
            }
        }
        localStorage[g_const_localStorage.Cart] = JSON.stringify(objLocalCart);
        if (g_type_cart.GetVersion() == g_type_cart.Version.V1)
            localStorage[g_const_localStorage.CartFull] = JSON.stringify(objLocalCartFull);
        g_type_cart.Upload(callback);
        for (var k = 0; k < arrobjproduct.length; k++) {
            var product_code = arrobjproduct[k][0];
            var sku_code = arrobjproduct[k][1];
            g_type_cart.Remove(product_code, sku_code);
        }
    },
    api_target: "com_cmall_familyhas_api_APiAddSkuToShopCart",
    /*输入参数  5.1.8 支持内购，增加isPurchase */
    api_input: { "goodsList": [], "buyer_code": "", "version": 1.0, "isPurchase": 1 },
    /*接口响应对象*/
    api_response: {},
    /*从云端下载购物车*/
    DownLoad: function (callback) {
        g_type_cart.api_input.goodsList = [];
        g_type_cart.SendToCloud(callback);
    },
    /*向云端同步本地购物车*/
    Upload: function (callback, clearcar) {
        
        if (g_type_cart.LocalCart() == null) {
            g_type_cart.api_input.goodsList = [];
        }
        //else if (!(clearcar == undefined)) {
        //    /*登录后，清空本地购物车*/
        //    g_type_cart.api_input.goodsList = [];
        //}
        else {
            g_type_cart.api_input.goodsList = g_type_cart.LocalCart().GoodsInfoForAdd;
        }
        g_type_cart.SendToCloud(callback);
    },
    /*向云端提交数据*/
    SendToCloud: function (callback) {
        if (g_type_cart.GetVersion() == g_type_cart.Version.V2) {
            g_type_cart.api_target = "com_cmall_familyhas_api_APiShopCartForCache";
            /*5.1.8 支持内购，增加isPurchase*/
            g_type_cart.api_input = { goodsList: g_type_cart.api_input.goodsList, version: 1.0, "channelId": g_const_ChannelID, "isPurchase": 1 };
        }
        var s_api_input = JSON.stringify(g_type_cart.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": g_type_cart.api_target, "api_token": g_const_api_token.Wanted };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {

            g_type_cart.api_response = msg;
            if (msg.resultCode == g_const_Success_Code) {
                if (g_type_cart.GetVersion() == g_type_cart.Version.V2) {
                    g_type_cart.CloudCart = msg;
                    //var shoppingCartList = msg.shoppingCartList;
                    //for (var i = 0; i < shoppingCartList.length; i++) {
                    //    var goods = shoppingCartList[i].goods;
                    //    g_type_cart.CloudCart.unshift(goods);
                    //}

                    //g_type_cart.CloudCart.unshift(msg.disableGoods);
                    //   g_type_cart.SalesAdv = msg.salesAdv;
                    g_type_cart.Clear();
                    $.each(g_type_cart.CloudCart.shoppingCartList, function (j, m) {
                        $.each(m.goods, function (i, n) {
                            g_type_cart.ADD(n, false);
                        });
                    });
                    $.each(g_type_cart.CloudCart.disableGoods, function (j, m) {
                        g_type_cart.ADD(m, false);
                    });
                    for (var i = g_type_cart.CloudCart.length - 1; i >= 0; i--) {
                        var objcartfull = g_type_cart.CloudCart[i];
                        g_type_cart.ADD(objcartfull, false);
                    }
                } else if (g_type_cart.GetVersion() == g_type_cart.Version.V1) {
                    g_type_cart.CloudCart = msg.shoppingCartList;
                    g_type_cart.SalesAdv = msg.salesAdv;
                    g_type_cart.SyncCart();
                }

            }
            else {
                if (msg.resultcode == g_const_Error_Code.UnLogin) {
                    localStorage[g_const_localStorage.BackURL] = "cart.html";
                    //ShowMesaage(g_const_API_Message["100001"]);
                    setTimeout("window.location.replace(\"" + g_const_PageURL.Login + "?t=" + Math.random() + "\");", 2000);
                    return;
                }
                ShowMesaage(msg.resultMessage);
            }
            if (typeof (callback) == "function")
                callback(msg);
        });

        request.fail(function (jqXHR, textStatus) {

            ShowMesaage(g_const_API_Message["100002"]);
        });
    }
}

/*我的产品浏览历史*/
var g_type_history = {
    canload:true,
    /*本地产品浏览历史*/
    LocalHistory: function () {
        var s = localStorage[g_const_localStorage.History];
        if (typeof (s) != "undefined") {
            return JSON.parse(s);
        }
        else
            return null;
    }(),
    /*历史对象*/
    ObjHistory: {
        /*产品编号*/
        "product_code": "",
        ///*产品图形*/
        //"picture": "",
        ///*产品名称*/
        //"pname": "",
        ///*产品售价*/
        //"SalePrice": "",
        ///*产品市场价*/
        //"marketPrice": "",
        ///*产品月销售量*/
        //"saleNum": ""
    },
    /*添加历史记录*/
    ADD: function (objhistory) {
        var objhistorys = {
            "PDHistory": []
        };
        if (g_type_history.LocalHistory != null)
            objhistorys = g_type_history.LocalHistory;
        var productisexist = false;

        for (var i = 0; i < objhistorys.PDHistory.length; i++) {
            var objexistproduct = objhistorys.PDHistory[i];
            if (typeof (objexistproduct) != "undefined") {
                if (objexistproduct.product_code == objhistory.product_code) {
                    objhistorys.PDHistory.splice(i, 1);
                    objhistorys.PDHistory.unshift(objhistory);
                    productisexist = true;
                    break;
                }
            }
            else
                break;
        }

        if (!productisexist) {
            if (objhistorys.PDHistory.length >= g_const_MaxHistoryCount) {
                objhistorys.PDHistory.splice(objhistorys.PDHistory.length - 1, 1);
                objhistorys.PDHistory.unshift(objhistory);
            }
            else {
                objhistorys.PDHistory.unshift(objhistory);

            }
        }
        localStorage[g_const_localStorage.History] = JSON.stringify(objhistorys);
    },
    /*清空*/
    Clear: function () {
        localStorage.removeItem(g_const_localStorage.History);
    },
    AddToServer: function () {
        var api_target = "com_cmall_familyhas_api_ApiAddBrowseHistory";
        var arrProducts = [];
        var loc = g_type_history.LocalHistory;
        if (loc != null) {
            for (var i in loc.PDHistory) {
                var item = loc.PDHistory[i];
                arrProducts.push(item.product_code);
            }
        }
        if (arrProducts.length == 0) {
            return;
        }
        var api_input = { "productCodes": arrProducts, "version": "" };
        var s_api_input = JSON.stringify(api_input);
        var obj_data = { "api_input": s_api_input, "api_target": api_target, "api_token": g_const_api_token.Wanted };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            if (msg.resultCode == g_const_Success_Code) {
                g_type_history.Clear();
            }
            else {
                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    GetListFormServer: function (pageNum, callBack) {
        var api_target = "com_cmall_familyhas_api_ApiGetBrowesHistory";
        /*5.1.8 支持内购，增加isPurchase*/
        var api_input = { "buyerType": g_const_buyerType.REGMEMBER, "pageNum": pageNum, "version": "", "isPurchase": 1 };
        var s_api_input = JSON.stringify(api_input);
        var obj_data = { "api_input": s_api_input, "api_target": api_target, "api_token": g_const_api_token.Wanted };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {

            if (msg.resultcode == g_const_Error_Code.UnLogin) {
                UserRELogin.login(g_const_PageURL.MyViewHistory + "");
                return;
            }
            if (msg.resultCode == g_const_Success_Code) {
                if (typeof (callBack) == "function")
                    callBack(msg);
            }
            else {
                //ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    ClearServer: function (callBack) {
        var api_target = "com_cmall_familyhas_api_ApiForBrowesHistoryClear";
        var api_input = { "version": "" };
        var s_api_input = JSON.stringify(api_input);
        var obj_data = { "api_input": s_api_input, "api_target": api_target, "api_token": g_const_api_token.Wanted };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            if (msg.resultcode == g_const_Error_Code.UnLogin) {
                UserRELogin.login(g_const_PageURL.MyViewHistory + "");
                return;
            }
            if (msg.resultCode == g_const_Success_Code) {
                if (typeof (callBack) == "function")
                    callBack(msg);
            }
            else {
                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    }
}

var Collection = {
    api_target: "com_cmall_familyhas_api_APiCollectionProduct",
    api_input: { "operateType": "", "productCode": [] },    
    BackURL: function () {
        var backurl = "";
        if (Product_Detail) {
            backurl = g_const_PageURL.Product_Detail + "?pid=" + Product_Detail.api_input.productCode;
        }
        return backurl;
    },
    /*5.3.4 商品详情页收藏请求参数新增skuCode、skuPrice*/
    Add: function (pidlist, callback, str_callback,skuCode,skuPrice) {
        Collection.api_input.operateType = "1";
        Collection.api_input.productCode = pidlist;
        /*5.3.4 商品详情页收藏请求参数新增skuCode、skuPrice  开始*/
        if (typeof (skuCode) != "undefined" && typeof (skuPrice) != "undefined") {
            Collection.api_input.skuCode = skuCode;
            Collection.api_input.skuPrice = skuPrice;
        }
        /*5.3.4 商品详情页收藏请求参数新增skuCode、skuPrice  结束*/

        var s_api_input = JSON.stringify(this.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": this.api_target, "api_token": "1" };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            /*5.3.4 兼容接口返回“已收藏”*/
            if (msg.resultCode == g_const_Success_Code || msg.resultCode=="916423301") {
                if (typeof (callback) == "function")
                    callback();
                ShowMesaage(g_const_API_Message["100003"]);
            }
            else {
                if (msg.resultCode == "969905919") {
                    //保存回跳页面
                    var temp_backurl= Collection.BackURL();
                    if(temp_backurl!=""){
                        localStorage[g_const_localStorage.BackURL] = temp_backurl;
                        PageUrlConfig.SetUrl(localStorage[g_const_localStorage.BackURL]);
                    }
                    //Message.ShowToPage(g_const_API_Message["100001"], g_const_PageURL.Login, 2000, str_callback);
                    if (str_callback != "") {
                        Message.ShowToPage("", g_const_PageURL.Login + "?t=" + Math.random(), 500, str_callback);
                    }
                    else {
                        window.location.replace(g_const_PageURL.Login + "?t=" + Math.random());
                    }
                    return;
                }

                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    Delete: function (pidlist, callback, str_callback) {
        Collection.api_input.operateType = "0";
        Collection.api_input.productCode = pidlist;
        var s_api_input = JSON.stringify(this.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": this.api_target, "api_token": "1" };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            if (msg.resultCode == g_const_Success_Code) {
                if (typeof (callback) == "function")
                    callback();

                ShowMesaage(g_const_API_Message["100004"]);
            }
            else if (msg.resultCode == "969905919") {
                //保存回跳页面
                PageUrlConfig.SetUrl();
                if (str_callback != "" && str_callback != undefined) {
                    Message.ShowToPage("", g_const_PageURL.Login + "?t=" + Math.random(), 500, str_callback);
                }
                else {
                    window.location.replace(g_const_PageURL.Login + "?t=" + Math.random());
                }
                return;
            }
            else {
                if (msg.resultcode == g_const_Error_Code.UnLogin) {
                    //保存回跳页面
                    var temp_backurl= Collection.BackURL();
                    if (temp_backurl != "") {
                        localStorage[g_const_localStorage.BackURL] = temp_backurl;
                        PageUrlConfig.SetUrl(localStorage[g_const_localStorage.BackURL]);
                    }
                    //Message.ShowToPage("您还没有登陆或者已经超时.", g_const_PageURL.Login, 2000, str_callback);
                    Message.ShowToPage("", g_const_PageURL.Login, 2000, str_callback);
                    return;
                }
                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
};
var Message = {
    ShowToPage: function (message, pageurl, time, str_callback, setbackurl) {
        var backurl = window.location.href;
        if (str_callback != "") {
            if (backurl.indexOf("?") != -1) {
                backurl += "&callback=" + encodeURIComponent(str_callback);
            }
            else {
                backurl += "callback=" + encodeURIComponent(str_callback);
            }
        }

        if (setbackurl != "" && !(setbackurl==undefined)) {
            backurl = setbackurl;
            localStorage[g_const_localStorage.BackURL] = backurl;
            PageUrlConfig.SetUrl(backurl);
        }

        if (message != "") {
            ShowMesaage(message);
        }
        setTimeout("window.location.replace(\"" + pageurl + "\");", time);
    },
    //加载层（消息，显示控件） 
    ShowLoading: function (message, divid) {
        $("#" + divid).html('');
        var body = "";
        body += "<div id=\"pageloading\" class=\"wrap-wait\">";
        //body += "<div class=\"img\">";
        //body += "<img src=\"/img/waiting.gif\" alt=\"\" />";
        //body += "</div>";
        body += "<p>" + message + "<br />...</p>";
        body += "</div>";
        body += "<div id=\"mask\" style=\"display:block;\">&nbsp;</div>";

        $("#" + divid).html(body);
    },
    //加载层（消息，显示控件） 
    ShowLoading_V2: function (divid) {
        $("#" + divid).html('');
        var body = "";
        body += "<div class=\"customs-pop\" style=\"display:block;\" id=\"pageloading\">";
        body += "<i></i>";
        //body += "<div class=\"waiting_bf\">";
        body += "<span class=\"w_red\"></span>";
        body += "<span class=\"\"></span>";
        body += "</div>";
        body += "</div>";
        body += "<div id=\"mask\" style=\"display:block;\">&nbsp;</div>";

        $("#" + divid).html(body);
        var flag = true;
        var timer;
        clearInterval(timer);
        timer = setInterval(function () {
            //$('.waiting_bf span').toggleClass("w_red");
            if (flag) {
                $('.waiting_bf span:first').animate({ left: "19px" }, 600);
                $('.waiting_bf span:last').animate({ left: "48px" }, 600);
                flag = false;
            } else {
                $('.waiting_bf span:first').animate({ left: "48px" }, 600);
                $('.waiting_bf span:last').animate({ left: "19px" }, 600);
                flag = true;
            }
        }, 0)
    },
    //确认提示层（消息，换行消息，显示控件，确定文字，确定操作，取消文字，取消操作[不传默认关闭层]）
    ShowConfirm: function (message, messageother, divid, yesstr, operateYes, nostr, operateNo) {
        $("#" + divid).html('');
        var body = "";
        body += "<div class=\"fbox ftel\">";
        body += "<div class=\"sc jxtx\"><span>" + message + "</span>" + messageother + "</div>";
        body += "<div class=\"btns\">";
        body += "<a onclick=\"Message.Operate(" + operateNo + ",'" + divid + "')\">" + nostr + "</a><a class=\"ok\" onclick=\"Message.Operate(" + operateYes + ",'" + divid + "')\">" + yesstr + "</a>";
        body += "</div>";
        body += "</div>";
        body += "<div id=\"mask\" style=\"z-index:100;\">&nbsp;</div>";
        $("#" + divid).html(body);
        //显示遮蔽层
        if (typeof ($("#mask")) != "undefined") {
            $("#mask").show();
        }
    },
    //普通提示层（消息，换行消息，显示控件，确定文字，确定操作[不传默认关闭层]）
    ShowAlert: function (message, messageother, divid, yesstr, operateYes) {
        $("#" + divid).html('');
        var body = "";
        body += "<div class=\"fbox ftel\">";
        body += "<div class=\"sc jxtx\"><span>" + message + "</span>" + messageother + "</div>";
        body += "<div class=\"btns\">";
        body += "<a class=\"ok2\" onclick=\"Message.Operate(" + operateYes + ",'" + divid + "')\">" + yesstr + "</a>";
        body += "</div>";
        body += "</div>";
        body += "<div id=\"mask\" style=\"z-index:100;\">&nbsp;</div>";
        $("#" + divid).html(body);
        //显示遮蔽层
        if (typeof ($("#mask")) != "undefined") {
            $("#mask").show();
        }
    },
    Operate: function (callback, divid) {
        if (typeof (callback) == "function") {
            callback();
        }
        $("#" + divid).html('');
        //隐藏遮蔽层
        if (typeof ($("#mask")) != "undefined") {
            $("#mask").hide();
        }
    },
};

//判断是否为微信浏览器
var IsInWeiXin = {
    check: function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            return true;
        } else {
            return false;
        }
    },
};
//判断是否为安卓客户端APP浏览器
var IsInAndroidAPP = {
    check: function (window) {
        try {
            var ua = window.navigator.userAgent.toLowerCase();
            if (ua.match(/hjy-android/i) == 'hjy-android') {
                return true;
            } else {
                return false;
            }
        }
        catch (e) {
            return false;
        }
    },
};

//检测登录
var UserLogin = {
    Check: function (callback) {
        //判断来源，安卓和ios且不再微信中时，表示是在APP中
        //alert("进入type.js的UserLogin.Check")
        //if (GroupPhone_LinShi.AppUseOnly(false, callback)) {
        //    //alert("进入type.js的UserLogin.Check,符合条件，等待抓取手机号和登录")
        //}
        //else {
            //alert("进入type.js的UserLogin.Check,不是APP调用，正常操作");
            var purl = g_INAPIUTL;
            var request = $.ajax({
                url: purl,
                cache: false,
                method: g_APIMethod,
                data: "t=" + Math.random() + "&action=checklogin",
                dataType: "json"
            });
            request.done(function (msg) {

                //登录状态 0 未登录； 1 已登录
                if (msg.resultcode == g_const_Success_Code_IN) {
                    UserLogin.LoginStatus = g_const_YesOrNo.YES;
                    UserLogin.LoginName = msg.resultmessage;
                }
                else {
                    UserLogin.LoginStatus = g_const_YesOrNo.NO;
                }
                if (typeof (callback) == "function")
                    callback();
            });

            request.fail(function (jqXHR, textStatus) {
                //ShowMesaage(g_const_API_Message["7001"]);
            });
        //}
    },
    /*活动微信授权后操作,实现不登录投票*/
    CheckForActWxSQ: function (callback,unionid,openid) {
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin_actsq&unionid=" + unionid + "&openid=" + openid,
            dataType: "json"
        });
        request.done(function (msg) {

            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
            }
            if (typeof (callback) == "function")
                callback();
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
        //}
    },

    CheckForGetWxPayID: function (callback) {
        //微信中且未登录，执行回调方法
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin",
            dataType: "json"
        });
        request.done(function (msg) {

            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
                if (typeof (callback) == "function")
                    callback();
            }
            
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
        //}
    },
    CheckForIndexGetLocation: function (callback) {
        //判断来源，安卓和ios且不再微信中时，表示是在APP中
        //alert("进入type.js的UserLogin.Check")
        //if (GroupPhone_LinShi.AppUseOnly(false, callback)) {
        //    //alert("进入type.js的UserLogin.Check,符合条件，等待抓取手机号和登录")
        //}
        //else {
            var purl = g_INAPIUTL;
            var request = $.ajax({
                url: purl,
                cache: false,
                method: g_APIMethod,
                data: "t=" + Math.random() + "&action=checklogin",
                dataType: "json"
            });
            request.done(function (msg) {

                //登录状态 0 未登录； 1 已登录
                if (msg.resultcode == g_const_Success_Code_IN) {
                    UserLogin.LoginStatus = g_const_YesOrNo.YES;
                    UserLogin.LoginName = msg.resultmessage;
                    if (typeof (callback) == "function")
                        callback();
                }
                else {
                    UserLogin.LoginStatus = g_const_YesOrNo.NO;
                }
            });

            request.fail(function (jqXHR, textStatus) {
                //ShowMesaage(g_const_API_Message["7001"]);
            });
        //}
    },
    CheckForCoupon_KSLQ_Mobile: function (callback_yes) {
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin",
            dataType: "json"
        });
        request.done(function (msg) {

            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;
                if (typeof (callback_yes) == "function")
                    callback_yes(UserLogin.LoginName);
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    CheckForCoupon_KSLQ_Mobile_n: function (callback_yes, callback_no) {
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin",
            dataType: "json"
        });
        request.done(function (msg) {

            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;
                if (typeof (callback_yes) == "function")
                    callback_yes(UserLogin.LoginName);
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
                if (typeof (callback_yes) == "function")
                    callback_no();
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    /*APP自动登录后调用*/
    Check_AfterAppLogin: function (callback) {
        //alert("进入type.js的UserLogin.Check_AfterAppLogin，callback："+callback)
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin",
            dataType: "json"
        });
        request.done(function (msg) {
            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
            }

            //alert("进入type.js的UserLogin.Check_AfterAppLogin,返回resultcode："+msg.resultcode+",typeof (callback)："+typeof (callback));
            if (typeof (callback) == "function")
                callback();
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    LoginStatus: 0,
    LoginName: "",
};
//设定当前页面路径
//localStorage[g_const_localStorage.BackURL] = location.href;

/*暂时使用，397上线后，删除此方法  开始*/
/*抓取APP手机号后自动登录，解决397上线前IOS拦截跳转链接重开窗口，导致session丢失问题，397上线后删除此方法*/
var GroupPhone_LinShi = {
    /*是否需要跳转*/
    canlocationhref: true,
    /*登录回调*/
    callback: true,
    /*来源*/
    from: "",
    /*目的*/
    to: "",
    /*手机号*/
    mobile: "",
    /*请求时间*/
    t: "",
    /*验签串*/
    mac: "",
    /*关闭窗口[只适用于web请求]*/
    CloseWin: function () {
        window.opener = null;
        window.open('', '_self');
        window.close();
    },
    /*只在APP中调用一元购时使用*/
    AppUseOnly: function (canlocationhref, callback) {
        if (!(callback == undefined)) {
            GroupPhone_LinShi.callback = callback;
        }
        else {
            GroupPhone_LinShi.callback = "";
        }
        //alert("进入type.GroupPhone_LinShi.AppUseOnly，获取callback："+GroupPhone_LinShi.callback)

        GroupPhone_LinShi.t = "";
        GroupPhone_LinShi.mac = "";
        var canuse = false;
        if (CheckMachine.versions.android && !CheckMachine.versions.inWeiXin) {
            GroupPhone_LinShi.from = "android";

            try {
                //安卓
                //window.share.getDataToJs(0)是安卓提供的获取手机号的方法						
                var androidInfo = window.share.getDataToJs(0);
                var info = eval('(' + androidInfo + ')');
                //本地保存
                GroupPhone_LinShi.mobile = info.mobilephone;
                if (GroupPhone_LinShi.mobile != "") {
                    GroupPhone_LinShi.AutoLogin(callback);
                }
                return true;
            }
            catch (e) {
                return false;
            }
        }
        else if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {
            GroupPhone_LinShi.from = "ios";
            //IOS
            try {
                //OCModel.getDataToJs(0)是IOS提供的获取手机号的方法				
                var iosInfo = OCModel.getDataToJs(0);
                //本地保存
                GroupPhone_LinShi.mobile = iosInfo.mobilephone;
                if (GroupPhone_LinShi.mobile != "") {
                    GroupPhone_LinShi.AutoLogin(callback);
                }
                return true;
            }
            catch (e) {
                return false;
            }
        }
        return false;
    },

    /*自动登录*/
//    AutoLogin: function (callback) {
//        var purl = g_INAPIUTL;//"/Ajax/API.aspx";
//        var request = $.ajax({
//            url: purl,
//            cache: false,
//            method: g_APIMethod,
//            data: "t=" + Math.random() + "&action=phoneloginauto&type=" + GroupPhone_LinShi.from + "&phoneno=" + GroupPhone_LinShi.mobile + "&tt=" + GroupPhone_LinShi.t + "&mac=" + GroupPhone_LinShi.mac,
//            dataType: g_APIResponseDataType
//        });
//
//        request.done(function (msg) {
//            if (msg.resultcode) {
//                if (msg.resultcode == g_const_Success_Code) {
//                    //UserLogin.Load_Result(JSON.parse(msg.resultmessage));
//                    GroupPhone_LinShi.GoTo();
//                }
//                else {
//                    //不能自动登录（验签失败或来源页没有登录）
//                    //ShowMesaage(msg.resultmessage);
//                    //ShowMesaageCallback(msg.resultmessage, GroupPhone_LinShi.CloseWin(), 5000);
//                    GroupPhone_LinShi.GoTo(callback);
//                }
//            }
//        });
//
//        request.fail(function (jqXHR, textStatus) {
//            ShowMesaage(g_const_API_Message["7001"]);
//        });
//        //g_APIUTL_User
//    },
    /*不跳转，执行登录验证*/
    GoTo: function (callback) {

        //alert("进入type.js的GroupPhone_LinShi.GoTo,GroupPhone_LinShi.callback："+GroupPhone_LinShi.callback);
        if (typeof (GroupPhone_LinShi.callback) == "function") {
            UserLogin.Check_AfterAppLogin(GroupPhone_LinShi.callback);
        }
        else {
            UserLogin.Check_AfterAppLogin("");
        }

    },
};

/*APP登录后，调用的通知方法*/
function appBackInfoToFun(info) {
   
    //获取来源
    if (info == "-1") {
        //关闭窗口
        UseAppFangFa.CaoZuo('close');
    }
    else {
        try {
            if (CheckMachine.versions.android && !CheckMachine.versions.inWeiXin) {

                //正常登陆
                GroupPhone_LinShi.from = "android";
                GroupPhone_LinShi.mobile = info.mobilephone;


            }
            else if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {

                GroupPhone_LinShi.from = "ios";
                GroupPhone_LinShi.mobile = info.mobilephone;
            }
            //自动登录
            GroupPhone_LinShi.AutoLogin();
        }
        catch (e) {
            //关闭窗口
            UseAppFangFa.CaoZuo('close');

        }
    }
}
/*暂时使用，397上线后，删除此方法  开始*/

//Session失效，重新登录
var UserRELogin = {
    login: function (callbackURL) {
        localStorage[g_const_localStorage.BackURL] = callbackURL;

        //ShowMesaage(g_const_API_Message["100001"]);
        setTimeout("window.location.replace(\"" + g_const_PageURL.Login + "?t=" + Math.random() + "\");", 500);
    },
    loginCB: function (callbackURL, pid) {
        localStorage[g_const_localStorage.BackURL] = callbackURL;
        //if (typeof (callback) == "function")
        //    callback();
        setTimeout("window.location.replace(\"" + g_const_PageURL.Login + "?t=" + Math.random() + "\");", 500);
    },
    
};
/*促销系统:活动商品接口*/
var g_type_event_product_list = {
    /*接口地址*/
    api_url: g_Temp_APIUTL,
    /*接口名*/
    api_target: "com_cmall_eventcall_api_APiEventProductInfo",
    /*输入参数*/
    api_input: { "special_code": GetQueryString("pageCode"), "img_Width": window.screen.availWidth, "version": 1.0 },
    /*是否需要token*/
    api_token: g_const_api_token.Unwanted,
    /*接口响应包*/
    api_response: {
        /*专题名称,以ZT开头的编号*/
        specialName: "",
        /*专题头图href*/
        imgTopUrlHref: "",
        /*商品数据*/
        eventProduct: [this.EventProduct],
        /*返回消息*/
        resultMessage: "",
        /*专题尾图href*/
        imgTailUrlHref: "",
        /*关联模板*/
        selectTemplate: "",
        /*系统时间*/
        sysTime: "",
        /*专题编号*/
        specialCode: "",
        /*专题描述*/
        dpecialDescription: "",
        /*专题头图*/
        imgTopUrl: "",
        /*返回标记*/
        resultCode: g_const_Success_Code,
        /*专题尾图*/
        imgTailUrl: ""
    },
    /*商品数据*/
    EventProduct: {
        /*多长时间抢光了,如果可卖库存数为0的时间，才会显示多长时间抢完，返回的数字以：秒 为单位*/
        longProduct: 0,
        /*商品编号*/
        productCode: "",
        /*促销库存*/
        salesNum: 0,
        /*商品主图*/
        mainpicUrl: "",
        /*商品名称*/
        skuName: "",
        /*开始时间*/
        beginTime: "",
        /*后台维护商品自动带回的库存数*/
        storeNum: "",
        /*结束时间*/
        endTime: "",
        /*销量值,代表该商品剩余可以卖的库存数，该数字从缓存里面取*/
        salesVolume: 0,
        /*sku编号*/
        skuCode: "",
        /*折扣,这有特价活动的时间该字段不为0，别的活动都为0,*/
        discount: "",
        /*商品位置*/
        seat: 0,
        /*阶梯价类型:4497473400010001为时间(分钟)， 4497473400010002为销量，如果值为空代表是活动的商品，活动商品是没有阶梯价*/
        sortType: g_const_event_product_sortType.Other,
        /*是否为阶梯价:449746250001 是阶梯价， 449746250002不是阶梯价，如果值为空代表是活动的商品，活动商品是没有阶梯价*/
        priceIs: g_const_event_product_priceIs.Unknown,
        /*明细编号*/
        itemCode: ""
    },
    /*读取数据*/
    LoadData: function (callback) {
        var api = g_type_api;
        api.api_url = g_type_event_product_list.api_url;
        api.api_token = g_type_event_product_list.api_token;
        api.api_input = g_type_event_product_list.api_input;
        api.api_target = g_type_event_product_list.api_target;
        api.LoadData(function (msg) {
            g_type_event_product_list.api_response = msg;
            if (typeof (callback) == "function")
                callback(msg);
        }, "");
    }

}
/*促销系统:活动商品商品价格接口*/
var g_type_event_product_price = {
    /*接口地址*/
    api_url: g_Temp_APIUTL,
    /*接口名*/
    api_target: "com_cmall_eventcall_api_APiEventProductPriceInfo",
    /*输入参数*/
    api_input: { "itemCode": "", "version": 1.0 },
    /*是否需要token*/
    api_token: g_const_api_token.Unwanted,
    /*接口响应包*/
    api_response: {},
    /*读取数据*/
    LoadData: function (callback) {
        var api = g_type_api;
        api.api_url = g_type_event_product_price.api_url;
        api.api_token = g_type_event_product_price.api_token;
        api.api_input = g_type_event_product_price.api_input;
        api.api_target = g_type_event_product_price.api_target;
        api.LoadData(function (msg) {
            g_type_event_product_price.api_response = msg;
            if (typeof (callback) == "function")
                callback(msg);
        }, "");
    }
}
var g_type_api = {
    /*接口地址*/
    api_url: g_APIUTL,
    /*接口名*/
    api_target: "",
    /*输入参数*/
    api_input: {},
    /*是否需要token*/
    api_token: g_const_api_token.Unwanted,
    /*接口响应包*/
    api_response: {},
    /*调用接口*/
    LoadData: function (callback, str_callback,err_callback) {
        var s_api_input = JSON.stringify(g_type_api.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": g_type_api.api_target };
        if (g_type_api.api_token === g_const_api_token.Wanted)
            obj_data = { "api_input": s_api_input, "api_target": g_type_api.api_target, "api_token": g_const_api_token.Wanted };

        var request = $.ajax({
            url: g_type_api.api_url,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            g_type_api.api_response = msg;
            if (msg.resultCode === g_const_Success_Code) {
                if (typeof (callback) === "function")
                    callback(msg);
            }
            else {
                if (msg.resultcode === g_const_Error_Code.UnLogin) {
                    //Message.ShowToPage("您还没有登陆或者已经超时.", g_const_PageURL.Login, 2000, str_callback);
                    Message.ShowToPage("", g_const_PageURL.Login, 2000, str_callback);
                    return;
                }
                ShowMesaage(msg.resultMessage);
                if (typeof (err_callback) === "function")
                    err_callback(msg);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            if (g_type_api.api_input.code) {
                if (g_type_api.api_input.code.indexOf('IC_SMG_')>-1) {
                    ShowMesaage(g_const_API_Message["70011"]);
                }
                else {
                    ShowMesaage(g_const_API_Message["7001"]);
                }
            }
            else {
                ShowMesaage(g_const_API_Message["7001"]);
            }
            
            if (typeof(err_callback) === "function")
                err_callback(null);
        });

    }
}

var ScollDown_Interval = null;
var refreshCount = 0;

function changeimg() {
    $(".d_refresh_02 img").attr("src", "/img/wza_img/refurbish_" + ((refreshCount % 12) + 1) + ".png");

    if (refreshCount == 12) {
        //alert(refreshCount)
        //refreshCount = 0
        //window.clearInterval(ScollDown_Interval);
        //ScollDown_Interval=null;
        $(".d_refresh_02 img").attr("src", "/img/wza_img/refurbish_12.png");

    } else {
        setTimeout("changeimg()", 300);
        refreshCount++;
    }


}

function ScollDown_Show(divID) {
    $("#" + divID).html("<img src=\"../../resources/cfamily/zzz_js/right-arrow.png\">");
    refreshCount = 1;
    //window.clearInterval(ScollDown_Interval);
    //ScollDown_Interval = self.setInterval("changeimg()", 300)
    //changeimg();

    if (typeof ($("#bodycontent")) != "undefined") {
        $("#bodycontent").attr("style", "margin-top: 0px;")
    }

    $("#" + divID).show();

    
}

function ScollDown_hide(divID, timenum) {
    if (timenum == undefined) {
        timenum = 100;
    }
    //window.clearInterval(ScollDown_Interval);
    /*隐藏下拉刷新层*/
    var ssst = setTimeout(function () {
        if (typeof ($("#bodycontent")) != "undefined") {
            $("#bodycontent").attr("style", "margin-top: 40px;")
        }

        $("#" + divID).hide();
        //window.clearInterval(ssst);
    }, timenum);//延时3秒

}
/*下拉刷新*/
var ScrollReload = {
    stop: false,
    toRight: false,
    toLeft: false,
    //增加监听（监听滚动的DIV的ID，显示加载中文字DIV的ID，来源Key【用于更新不同来源页面上次刷新的时间】，滚动的距离[不需要判断距离传0]，下滑回调执行的方法,左右滑动距离，左划回调执行的方法，右划回调执行的方法,）
    Listen: function (ScrollInDivId, ShowMessDivId, FromKey, length_number, callback, length_number_lr,callbackLeft, callbackGight) {
        var tagId = ScrollInDivId;
        var pressX = 0, pressY = 0;
        var obj = document.getElementById(tagId);//$(tagId);
        obj.addEventListener('touchmove', function (event) {
            // 如果这个元素的位置内只有一个手指的话
            if (event.targetTouches.length == 1) {
                var touch = event.targetTouches[0];
                var spanX = touch.pageX - pressX;
                var spanY = touch.pageY - pressY;
                var direct = "none";
                if (Math.abs(spanX) > Math.abs(spanY)) {
                    //水平方向
                    if (spanX > 0) {
                        direct = "right";//向右
                        if (typeof (Page_Index)!="undefined") {
                            if (typeof (Page_Index.IsCanLeftRight) != "undefined") {
                                if (Page_Index.IsCanLeftRight) {
                                    //alert('右');
                                   

                                    //if (Math.abs(spanX) > length_number_lr && Math.abs(spanY) <= 3) {
                                    //    var isFirst = false;
                                    //    //第一个页签不起作用
                                    //    $.each(Page_Index.AllIndexChannelCode[0], function (iik, nk) {
                                    //        if (Page_Index.indexChannel_Code == nk.navCode) {
                                    //            if (iik == 0) {
                                    //                isFirst = true;
                                    //            }
                                    //        }
                                    //    });

                                    //    if (ScrollReload.stop) {
                                    //        if (isFirst == false) {
                                    //            $(document.getElementById("pydiv")).css({
                                    //                "WebkitTransform": "translate(40px)",
                                    //            });

                                    //            ScrollReload.toRight = true;
                                    //        }
                                    //        else {
                                    //            ScrollReload.toRight = false;
                                    //        }
                                    //        ScrollReload.stop = false;
                                    //    }
                                    //}
                                }
                            }
                        }
                        
                        //do right function
                    } else {
                        direct = "left";//向左
                        if (typeof (Page_Index) != "undefined") {
                            if (typeof (Page_Index.IsCanLeftRight) != "undefined") {
                                if (Page_Index.IsCanLeftRight) {
                                    //alert('左');
                                    
                                    //if (Math.abs(spanX) > length_number_lr && Math.abs(spanY) <= 3) {
                                    //    var isLast = false;
                                    //    //最后一个页签不起作用
                                    //    $.each(Page_Index.AllIndexChannelCode[0], function (iik, nk) {
                                    //        if (Page_Index.indexChannel_Code == nk.navCode) {
                                    //            if (iik == (Page_Index.AllIndexChannelCode[0].length - 1)) {
                                    //                isLast = true;
                                    //            }
                                    //        }
                                    //    });

                                    //    if (ScrollReload.stop) {
                                    //        if (isLast == false) {
                                    //            $(document.getElementById("pydiv")).css({
                                    //                "WebkitTransform": "translate(-40px)",
                                    //            });
                                    //            ScrollReload.toLeft = true;
                                    //            //ScrollReload.CallbackDown(callbackLeft);
                                    //        }
                                    //        else {
                                    //            ScrollReload.toLeft = false;
                                    //        }
                                    //        ScrollReload.stop = false;
                                    //    }
                                    //}
                                   
                                }
                            }
                        }
                        //do left function
                    }
                } else {
                    //垂直方向
                    if (spanY > 0 && $(window).scrollTop() == 0) {
                        direct = "down";//向下

                        if (spanY > parseInt(length_number) ) {
                            
                            $("#" + ShowMessDivId).show();

                            //获取上次刷新时间
                            var proTime = "";
                            FromKey = "LastReloadTime_" + FromKey;
                            if (localStorage[FromKey] == null) {
                                //localStorage.setItem(g_const_localStorage.OrderConfirm, JSON.stringify({ "GoodsInfoForAdd": [{ "sku_num": 2, "area_code": "", "product_code": "120903", "sku_code": "120903" }] }))
                                proTime = getNowFormatDate();
                                localStorage[FromKey] = proTime;
                            }
                            else {
                                proTime = localStorage[FromKey];
                            }

                            //localStorage[FromKey] = JSON.stringify(objhistorys);

                            //显示层内容
                            //var showStr = "<div class=\"d_refresh\">"
                            //                + "<div class=\"d_refresh_div\">"
                            //                    + "<div>下拉可以刷新</div>"
                            //                    + "<div  class=\"d_cfs9\">上次刷新时间：" + proTime + "</div>"
                            //                + "</div>"
                            //            + "</div>";
                            //var showStr = "<img src=\"/img/wza_img/refurbish_1.png\">";
                            //$("#" + ShowMessDivId).html(showStr);

                            //显示下拉层
                            ScollDown_Show()

                            //更新上次刷新时间
                            localStorage[FromKey] = getNowFormatDate();

                            if (ScrollReload.stop) {
                                ScrollReload.stop = false;
                                //执行回调
                                ScrollReload.CallbackDown(callback);
                            }
                        }
                        else {
                            //$("#" + ShowMessDivId).hide();
                            ScollDown_hide(ShowMessDivId,10);
                        }

                    } else {
                        direct = "up";//向上
                        //do up function

                        //隐藏下拉层
                        //$("#" + ShowMessDivId).hide();
                        ScollDown_hide(ShowMessDivId,10);
                    }
                }
                // 把元素放在手指所在的位置
                //touchMove.value = direct + "(" + spanX + ';' + spanY + ")";
            }
        }, { passive: true });
        obj.addEventListener('touchstart', function (event) {
            // 如果这个元素的位置内只有一个手指的话
            //if (event.targetTouches.length == 1) {
                ScrollReload.stop = true;
                var touch = event.targetTouches[0];
                // 把元素放在手指所在的位置
                pressX = touch.pageX;
                pressY = touch.pageY;
                //touchStart.value = pressX + ';' + pressY;
            //}
        }, { passive: true });
        obj.addEventListener('touchend', function (event) {
            // 如果这个元素的位置内只有一个手指的话
            //if (event.targetTouches.length == 1) {
                ScrollReload.stop = true;
                //var touch = event.targetTouches[0];
                //// 把元素放在手指所在的位置
            //touchEnd.value=touch.pageX + ';' + touch.pageY;
                if (ScrollReload.toLeft) {
                    ScrollReload.toLeft = false;
                    if (typeof (Page_Index) != "undefined") {
                        if (typeof (Page_Index.IsCanLeftRight) != "undefined") {
                            $(document.getElementById("pydiv")).css({
                                "WebkitTransform": "translate(0px)",
                            });
                        }
                    }

                    ScrollReload.CallbackDown(callbackLeft);
                }
                else if (ScrollReload.toRight) {
                    ScrollReload.toRight = false;
                    if (typeof (Page_Index) != "undefined") {
                        if (typeof (Page_Index.IsCanLeftRight) != "undefined") {
                            $(document.getElementById("pydiv")).css({
                                "WebkitTransform": "translate(0px)",
                            });
                        }
                    }
                    ScrollReload.CallbackDown(callbackGight);
                }
                //隐藏下拉层
                ScollDown_hide(ShowMessDivId, 10);
            //}
        }, { passive: true });
    },
    //回调下拉方法
    CallbackDown: function (callback) {
        if (typeof (callback) == "function") {
            callback();
        }
    },
};

/*5.2.4 横横划时禁止纵向滚动滚动*/
var NoScroll = {
    //是否可以滚动
    nos: false,
    stop: false,
    toRight: false,
    toLeft: false,
    //增加监听（监听滚动的DIV的ID，禁止纵向滚动，禁止横向滚动）
    Listen: function (ScrollInDivId, No_ZX_Scoll, No_HX_Scoll, callbackZX, callbackHX) {
        var tagId = ScrollInDivId;
        var pressX = 0, pressY = 0;
        var obj = document.getElementById(tagId);//$(tagId);
        obj.addEventListener('touchmove', function (event) {
            // 如果这个元素的位置内只有一个手指的话
            if (event.targetTouches.length == 1) {
                var touch = event.targetTouches[0];
                var spanX = touch.pageX - pressX;
                var spanY = touch.pageY - pressY;
                var direct = "none";

                if (Math.abs(spanX) > Math.abs(spanY) && Math.abs(spanY)) {
                    NoScroll.nos = false;
                }
                else if (Math.abs(spanX) == 0 && Math.abs(spanY) > 0) {
                    NoScroll.nos = true;

                } else {
                    NoScroll.nos = false;
                }
                NoScroll.CallbackDown(callbackZX);
            }
        }, { passive: true });
        obj.addEventListener('touchstart', function (event) {
            // 如果这个元素的位置内只有一个手指的话
            //if (event.targetTouches.length == 1) {
            NoScroll.stop = true;
            var touch = event.targetTouches[0];
            // 把元素放在手指所在的位置
            pressX = touch.pageX;
            pressY = touch.pageY;
            //touchStart.value = pressX + ';' + pressY;
            //}
        }, { passive: true });
        obj.addEventListener('touchend', function (event) {
            //恢复
            NoScroll.nos = false;

        }, { passive: true });
    },
    //回调下拉方法
    CallbackDown: function (callback) {
        if (typeof (callback) == "function") {
            callback();
        }
    },
};



/*置顶的显示与隐藏*/
var objTop = {
    Start: function (objtop) {
        $(window).on("touchstart", objTop.OnTouchStart);
        $(window).on("touchmove", objTop.OnTouchMove);
        //$(window).on("touchend", objTop.OnTouchEnd);
        objTop.oTop = objtop;
        objTop.oTop.css("display", "none");
        objTop.oTop.on("click", function (e) {
            //$(e.target).css("display", "none");
            objTop.oTop.css("display", "none");
            window.scrollTo(0, 0);
        });
    },
    oTop: {},
    StartY: 0,
    OnTouchStart: function (e) {
        var objthis = e.target;
        objTop.StartY = $(objthis).offset().top;


    },
    OnTouchMove: function (e) {
        var i_body_hegiht = $("body").height();
        var i_scroll_height = $(document).scrollTop();
        var i_window_height = window.screen.availHeight;
        if (i_scroll_height >= i_window_height * 3)
            objTop.oTop.css("display", "block");
        else {
            objTop.oTop.css("display", "none");
        }

    },
    OnTouchEnd: function (e) {
        var i_body_hegiht = $("body").height();
        var i_scroll_height = $(document).scrollTop();
        var i_window_height = window.screen.availHeight;
        if (i_scroll_height >= i_window_height * 3)
            objTop.oTop.css("display", "block");
        else {
            objTop.oTop.css("display", "none");
        }
    },
    End: function () {
        $(window).off("touchstart", objTop.OnTouchStart);
        $(window).off("touchmove", objTop.OnTouchMove);
        //$(window).off("touchend", objTop.OnTouchEnd);
    }
}

//检测登录
var WeiXinLogin = {
    Check: function (callback) {
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=getwxopenid",
            dataType: "json"
        });
        request.done(function (msg) {
            if (msg.resultcode == g_const_Success_Code_IN) {
                WeiXinLogin.WeiXinStatus = g_const_YesOrNo.YES;
                WeiXinLogin.WeiXinName = msg.resultmessage;
            }
            else {
                WeiXinLogin.WeiXinStatus = g_const_YesOrNo.NO;
                if (localStorage[g_const_localStorage.Member]) {
                    WeiXinLogin.WeiXinName = (JSON.parse(localStorage[g_const_localStorage.Member]).Member).uid;
                }
            }
            // if (WeiXinLogin.WeiXinName.length == 0) {
            //var pageurl="";
            //if (localStorage.getItem(g_const_localStorage.BackURL).length > 0) {
            //    pageurl = localStorage.getItem(g_const_localStorage.BackURL);
            //}
            //else {
            //    pageurl = "/";
            //}
            //    location = "/Account/OauthLogin.aspx?oauthtype=WeiXin";
            //    return;
            //}
            if (typeof (callback) == "function")
                callback();
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    WeiXinStatus: 0,
    WeiXinName: "",
};

/*Base64编码和解码*/
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);
var Base64 = {

    /** 
     * base64编码 
     * @param {Object} str 
     */
    base64encode: function (str) {
        var out, i, len;
        var c1, c2, c3;
        len = str.length;
        i = 0;
        out = "";
        while (i < len) {
            c1 = str.charCodeAt(i++) & 0xff;
            if (i == len) {
                out += base64EncodeChars.charAt(c1 >> 2);
                out += base64EncodeChars.charAt((c1 & 0x3) << 4);
                out += "==";
                break;
            }
            c2 = str.charCodeAt(i++);
            if (i == len) {
                out += base64EncodeChars.charAt(c1 >> 2);
                out += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                out += base64EncodeChars.charAt((c2 & 0xF) << 2);
                out += "=";
                break;
            }
            c3 = str.charCodeAt(i++);
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
            out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
            out += base64EncodeChars.charAt(c3 & 0x3F);
        }
        return out;
    },
    /** 
     * base64解码 
     * @param {Object} str 
     */
    base64decode: function (str) {
        var c1, c2, c3, c4;
        var i, len, out;
        len = str.length;
        i = 0;
        out = "";
        while (i < len) {
            /* c1 */
            do {
                c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
            }
            while (i < len && c1 == -1);
            if (c1 == -1)
                break;
            /* c2 */
            do {
                c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
            }
            while (i < len && c2 == -1);
            if (c2 == -1)
                break;
            out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
            /* c3 */
            do {
                c3 = str.charCodeAt(i++) & 0xff;
                if (c3 == 61)
                    return out;
                c3 = base64DecodeChars[c3];
            }
            while (i < len && c3 == -1);
            if (c3 == -1)
                break;
            out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
            /* c4 */
            do {
                c4 = str.charCodeAt(i++) & 0xff;
                if (c4 == 61)
                    return out;
                c4 = base64DecodeChars[c4];
            }
            while (i < len && c4 == -1);
            if (c4 == -1)
                break;
            out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
        }
        return out;
    },
    /** 
     * utf16转utf8 
     * @param {Object} str 
     */
    utf16to8: function (str) {
        var out, i, len, c;
        out = "";
        len = str.length;
        for (i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            }
            else
                if (c > 0x07FF) {
                    out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                    out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                }
                else {
                    out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                }
        }
        return out;
    },
    /** 
     * utf8转utf16 
     * @param {Object} str 
     */
    utf8to16: function (str) {
        var out, i, len, c;
        var char2, char3;
        out = "";
        len = str.length;
        i = 0;
        while (i < len) {
            c = str.charCodeAt(i++);
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    // 0xxxxxxx  
                    out += str.charAt(i - 1);
                    break;
                case 12:
                case 13:
                    // 110x xxxx 10xx xxxx  
                    char2 = str.charCodeAt(i++);
                    out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
                    break;
                case 14:
                    // 1110 xxxx10xx xxxx10xx xxxx  
                    char2 = str.charCodeAt(i++);
                    char3 = str.charCodeAt(i++);
                    out += String.fromCharCode(((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
                    break;
            }
        }
        return out;
    },
};

/*微信JSAPI*/
var WX_JSAPI = {
    /*分享标题*/
    title: "",
    /*分享链接*/
    link: "",
    /*分享图标*/
    imgUrl: "",
    /*分享描述*/
    desc: "",
    /*分享类型*/
    type: g_const_wx_share_type.link,
    /*如果type是music或video，则要提供数据链接，默认为空*/
    dataUrl: "",
    /*腾讯对象wx,请先赋值*/
    wx: null,
    IsTest: function () {
        if (GetQueryString("fromtest") == "1")
            return true;
        else
            return false;
    },
    /*JSAPI参数对象*/
    wxparam: {
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '', // 必填，公众号的唯一标识
        timestamp: 0, // 必填，生成签名的时间戳
        nonceStr: '', // 必填，生成签名的随机串
        signature: '',// 必填，签名，见附录1
        jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    },
    func_CallBack: function () { },
    /*从接口获取参数,参数jsApiList是要调用的接口名,多个逗号分隔*/
    LoadParam: function (jsApiList) {
        WX_JSAPI.jsApiList = jsApiList;
        var obj_data = { action: "wxshare", jsApiList: WX_JSAPI.jsApiList, surl: window.location.href, debug: WX_JSAPI.wxparam.debug };
        var request = $.ajax({
            url: g_INAPIUTL,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            if (msg.resultcode == "0") {
                WX_JSAPI.wxparam.appId = msg.appId;
                WX_JSAPI.wxparam.timestamp = msg.timestamp;
                WX_JSAPI.wxparam.nonceStr = msg.nonceStr;
                WX_JSAPI.wxparam.jsApiList = msg.jsApiList;
                WX_JSAPI.jsApiList = msg.jsApiList;
                WX_JSAPI.wxparam.signature = msg.signature;
                if (WX_JSAPI.IsTest()) {
                    $("#showlog").append("微信JSAPI基础配置接口发送的参数：<br>" + JSON.stringify(WX_JSAPI.wxparam) + "<br>");
                }
                WX_JSAPI.CallWeiXin();
            }
            else {
                if (WX_JSAPI.IsTest()) {
                    $("#showlog").append("微信JSAPI基础配置发生错误：<br>" + msg.resultmessage + "<br>");
                }
                //ShowMesaage(msg.resultmessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage("调用接口出错!")
        });
    },
    LoadCardParam: function (cardid, openid, callback) {

        var obj_data = { action: "wxaddcard", wxcardid: cardid, debug: WX_JSAPI.wxparam.debug };
        var request = $.ajax({
            url: g_INAPIUTL,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });

        request.done(function (msg) {
            if (msg.resultcode == "0") {
                WX_JSAPI.cardList = msg.cardList;
                if (typeof (callback) == "function")
                    callback(msg);
            }
            else {
                //ShowMesaage(msg.resultmessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage("调用接口出错!")
        });
    },
    //卡券包
    cardList: [{}],
    /*调用微信接口,参数wx为腾讯wx对象*/
    CallWeiXin: function () {

        WX_JSAPI.wx.config(WX_JSAPI.wxparam);
        WX_JSAPI.wx.ready(WX_JSAPI.WX_Ready);
        WX_JSAPI.wx.error(function (res) {
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            //ShowMesaage(JSON.stringify(res));
            //$("#showlog").append(JSON.stringify(res)+"<br>");
        });
    },
    /*jsApiList*/
    jsApiList: g_const_wx_AllShare,
    //微信卡券接口是否已经准备好
    IsCardReady: false,
    //页面是否在微信小程序中
    IsInMiniProgram: false,
    /*微信准备好后执行的函数*/
    WX_Ready: function () {

        /*判断是否在微信小程序中
        if (window.__wxjs_environment === 'miniprogram') {
            WX_JSAPI.IsInMiniProgram = true;
            //跳转小程序中页面
            //wx.miniProgram.redirectTo({
            //    url: '/pages/product_detail/product_detail?pid=' + pid,
            //})
        }
        */
        //$("#showlog").text(JSON.stringify(WX_JSAPI.jsApiList));
        if (typeof (WX_JSAPI.func_CallBack) == "function")
            WX_JSAPI.func_CallBack();
        /*微信分享*/
        if (WX_JSAPI.jsApiList[0].toLowerCase().indexOf("share") != -1) {
            WX_JSAPI.WX_ShareReady();
        }
    },
    WX_Card_ID: "pjaSfwzyh75fjPRp1oMWgDnswf5s",
    WX_CardReady: function () {
        //测试环境的p-voTt1p_VGTGdPt0YAoOh6MUiOU 
        WX_JSAPI.LoadCardParam(WX_JSAPI.WX_Card_ID, "", function (msg) {
            if (WX_JSAPI.IsTest()) {
                $("#showlog").append("领取卡券发送的数据：<br>" + JSON.stringify(msg) + "<br>");
            }
            var objcard = {
                cardList: WX_JSAPI.cardList,
                success: function (res) {
                    if (WX_JSAPI.IsTest()) {
                        $("#showlog").append("领取卡券返回的数据：<br>" + JSON.stringify(res.cardList) + "<br>");
                    }
                }

            };
            WX_JSAPI.wx.addCard(objcard);
        });
    },
    WX_ShareReady: function () {
        var objdata = {
            title: WX_JSAPI.desc, // 分享标题
            link: WX_JSAPI.link, // 分享链接
            imgUrl: WX_JSAPI.imgUrl, // 分享图标
            ///*分享描述*/
            //desc: WX_JSAPI.desc,
            ///*分享类型*/
            //type: WX_JSAPI.type,
            ///*如果type是music或video，则要提供数据链接，默认为空*/
            //dataUrl: WX_JSAPI.dataUrl,
            success: function () {
                // 用户确认分享后执行的回调函数
                WX_JSAPI.WX_Success(g_const_wx_jsapi.onMenuShareTimeline);
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                WX_JSAPI.WX_Cancel(g_const_wx_jsapi.onMenuShareTimeline);
            }
        };

        WX_JSAPI.wx.onMenuShareTimeline(objdata);
        objdata = {
            title: WX_JSAPI.title, // 分享标题
            link: WX_JSAPI.link, // 分享链接
            imgUrl: WX_JSAPI.imgUrl, // 分享图标
            /*分享描述*/
            desc: WX_JSAPI.desc,
            /*分享类型*/
            type: WX_JSAPI.type,
            /*如果type是music或video，则要提供数据链接，默认为空*/
            dataUrl: WX_JSAPI.dataUrl,
            success: function () {
                // 用户确认分享后执行的回调函数
                WX_JSAPI.WX_Success(g_const_wx_jsapi.onMenuShareAppMessage);
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                WX_JSAPI.WX_Cancel(g_const_wx_jsapi.onMenuShareAppMessage);
            }
        };
        WX_JSAPI.wx.onMenuShareAppMessage(objdata);
        objdata = {
            title: WX_JSAPI.title, // 分享标题
            link: WX_JSAPI.link, // 分享链接
            imgUrl: WX_JSAPI.imgUrl, // 分享图标
            /*分享描述*/
            desc: WX_JSAPI.desc,
            /*分享类型*/
            type: WX_JSAPI.type,
            /*如果type是music或video，则要提供数据链接，默认为空*/
            dataUrl: WX_JSAPI.dataUrl,
            success: function () {
                // 用户确认分享后执行的回调函数
                WX_JSAPI.WX_Success(g_const_wx_jsapi.onMenuShareQQ);
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                WX_JSAPI.WX_Cancel(g_const_wx_jsapi.onMenuShareQQ);
            }
        };
        WX_JSAPI.wx.onMenuShareQQ(objdata);
        objdata = {
            title: WX_JSAPI.title, // 分享标题
            link: WX_JSAPI.link, // 分享链接
            imgUrl: WX_JSAPI.imgUrl, // 分享图标
            /*分享描述*/
            desc: WX_JSAPI.desc,
            /*分享类型*/
            type: WX_JSAPI.type,
            /*如果type是music或video，则要提供数据链接，默认为空*/
            dataUrl: WX_JSAPI.dataUrl,
            success: function () {
                // 用户确认分享后执行的回调函数
                WX_JSAPI.WX_Success(g_const_wx_jsapi.onMenuShareWeibo);
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                WX_JSAPI.WX_Cancel(g_const_wx_jsapi.onMenuShareWeibo);
            }
        };
        WX_JSAPI.wx.onMenuShareWeibo(objdata);
        objdata = {
            title: WX_JSAPI.title, // 分享标题
            link: WX_JSAPI.link, // 分享链接
            imgUrl: WX_JSAPI.imgUrl, // 分享图标
            /*分享描述*/
            desc: WX_JSAPI.desc,
            /*分享类型*/
            type: WX_JSAPI.type,
            /*如果type是music或video，则要提供数据链接，默认为空*/
            dataUrl: WX_JSAPI.dataUrl,
            success: function () {
                // 用户确认分享后执行的回调函数
                WX_JSAPI.WX_Success(g_const_wx_jsapi.onMenuShareQZone);
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                WX_JSAPI.WX_Cancel(g_const_wx_jsapi.onMenuShareQZone);
            }
        };
        WX_JSAPI.wx.onMenuShareQZone(objdata);

    },

    /*操作完成时回调的函数*/
    WX_Success: function (wx_jsapi_type) {
        Merchant1.RecordPageAct("wxshare", wx_jsapi_type);
    },
    /*操作取消时回调的函数*/
    WX_Cancel: function (wx_jsapi_type) {
        //ShowMesaage(wx_jsapi_type+"操作取消");
    }
};

var g_type_loginjs = {
    /*登陆会员信息*/
    Member: {
        Member: {
            /*账号绑定类型*/
            accounttype: g_const_accounttype.ICHSY,
            /*来源*/
            from: "",
            /*手机号*/
            phone: "",
            /*三方绑定唯一编号*/
            uid: ""
        }
    },
    /*登陆成功后要调用的方法数组*/
    calls: [],
    /*登陆成功后要转向的地址*/
    returnurl: "",
    /*登陆会员绑定信息到前台缓存，5.2.2 提交本地购物车缓存数据至云端*/
    SetMemberInfo: function () {
        try {
            localStorage[g_const_localStorage.Member] = JSON.stringify(g_type_loginjs.Member);
            return true;
        }
        catch (e) {
            //alert('清除本地购物车缓存')
            wxshop_LocalStorage_Clear();//localStorage.clear();

            g_type_cart.Upload(g_type_loginjs.AfterCartUpload);
            return false;
        }
    },
    /*执行*/
    Execute: function (str_loginjs) {
        if (str_loginjs != '') {
           // $("body").css("display", "none");
            var loginjs;
            eval('loginjs=' + str_loginjs);
            g_type_loginjs.returnurl = loginjs.returnurl;
            g_type_loginjs.Member.Member = loginjs.Member;

            /*5.1.8 跳转地址包含jfshop=时，直接拼接token后跳转  开始*/
            if (g_type_loginjs.returnurl.indexOf("jfshop=") > -1) {
                var backurl = "";
                var returnurl = g_type_loginjs.returnurl.split('&');
                var k = 0;
                for (var i = 0; i < returnurl.length; i++) {
                    //去掉返回串中的token参数
                    if (returnurl[i].toLowerCase().indexOf('token') == -1) {
                        if (k == 0) {
                            k++;
                            backurl = returnurl[i];
                        }
                        else {
                            if (backurl.indexOf("?") > -1) {
                                backurl += "&" + returnurl[i];
                            }
                            else {
                                backurl += "?" + returnurl[i];
                            }
                        }
                    }
                }
                
                if (backurl.indexOf("?") > -1) {
                    backurl = backurl + "&token=" + loginjs.Member.token;
                }
                else {
                    backurl = backurl + "?token=" + loginjs.Member.token;
                }
                //window.location.replace(backurl);
                location.href = backurl;
                return;
            }
            /*5.1.8 跳转地址包含jfshop=时，直接拼接token后跳转  结束*/

            for (var k in loginjs.calls) {
                var call = loginjs.calls[k];
                eval(call);
            }
            //同步本地缓存浏览历史记录
            g_type_history.AddToServer();

            if (loginjs.Member.phone != "")
                //登录后，清除本地购物车缓存
                //g_type_cart.Upload(g_type_loginjs.AfterCartUpload,true);
                //登录后，同步本地购物车缓存至云端
                g_type_cart.Upload(g_type_loginjs.AfterCartUpload,false);
            else
                g_type_loginjs.AfterCartUpload();
        }
    },
    /*同步购物车完成执行的操作*/
    AfterCartUpload: function (msg) {
        if (g_type_loginjs.SetMemberInfo()) {
            if (g_type_loginjs.returnurl != "") {
                var rurl = g_type_loginjs.returnurl;
                var r = rurl.split("^");
                var shortkey = r[0];
                if (typeof (ShortURL) != "undefined") {
                    for (var k in ShortURL) {
                        var shorturl = ShortURL[k];
                        if (shortkey.Trim() == shorturl.key.Trim()) {
                            rurl = shorturl.value;
                            break;
                        }
                    }
                }
                window.location.replace(rurl);
            }
        }
    }
};
//内部接口调用
var g_type_self_api = {
    /*接口地址*/
    api_url: g_INAPIUTL,
    /*接口响应包*/
    api_response: {},
    //是否异步
    api_async: true,
    /*调用接口*/
    LoadData: function (obj_data, callback, str_callback) {

        var request = $.ajax({
            url: g_type_self_api.api_url,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType,
            async: g_type_self_api.api_async
        });

        request.done(function (msg) {
            g_type_self_api.api_response = msg;
            if (msg.resultcode.toString() == g_const_Success_Code_IN.toString()) {
                if (typeof (callback) == "function")
                    callback(msg);
            }
            else {
                if (msg.resultcode == g_const_Error_Code.UnLogin) {
                    Message.ShowToPage("", g_const_PageURL.Login, 2000, str_callback);
                    return;
                }
                ShowMesaage(msg.resultmessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });

    }
}

//是否评价本地缓存
var g_type_Evaluate = {
    add: function (orderNo, is_comment) {
        var string = localStorage.getItem("IsEvaluate");
        if (typeof (string) != "undefined") {
            var arr = JSON.parse(string);
            if (arr == null) {
                arr = [];
            }
            arr.push({ 'k': orderNo, 'v': is_comment });
            localStorage.setItem("IsEvaluate", JSON.stringify(arr));
        }
    },
    is_comment: function (orderno) {
        var string = localStorage.getItem("IsEvaluate");
        if (typeof (string) != "undefined") {
            var commentlist = JSON.parse(string);
            for (var item in commentlist) {
                if (commentlist[item].k == orderno) {
                    return commentlist[item].v;
                }
            }
        }
        return 0;
    },
    clear: function () {
        localStorage.removeItem("IsEvaluate");
    },
    changeCommented: function (orderNo) {
        var string = localStorage.getItem("IsEvaluate");
        if (typeof (string) != "undefined") {
            var commentlist = JSON.parse(string);
            for (var item in commentlist) {
                if (commentlist[item].k == orderNo) {
                    commentlist[item].v = 1;
                }
            }
            localStorage.setItem("IsEvaluate", JSON.stringify(commentlist));
        }
    }
};

/*上传图片*/
var Upload = {
    /*支持类型*/
    ImgType: [".jpg", ".jpeg", ".png"],
    /*支持类型文字*/
    ImgTypeStr: "JPG、PNG、JPEG",

    /*基于jquery.form.min.js的 上传图片
    FormID：FormID
    FileControlName:file控件ID
    ShowControlID:显示上传图片控件ID
    */
    UpLoadImg: function (imgfileControls, FormID, FileControlID, ShowControlID,fileObj) {
        if ($("#" + FileControlID).val() == "") {
            //点击取消后处理
            Upload.DelImg(imgfileControls, FileControlID);
            return false;
        }
        else {
            var $form = $("#" + FormID);
            var options = {
                dataType: "json",
                beforeSubmit: function () {
                    var file = $("#" + FileControlID).val();
                    if (file.length > 0) {
                        var exName = file.substring(file.lastIndexOf('.'), file.length).toLowerCase();
                        var isFind = Upload.ImgType.indexOf(exName);

                        if (isFind == -1) {
                            //隐藏上传提示
                            if (typeof (HidWait) == "function") {
                                HidWait();
                            }

                            ShowMesaage("上传图片类型错误，请重新选择。</br>仅支持" + Upload.ImgTypeStr + "格式。");
                            //不是图片后处理
                            Upload.DelImg(imgfileControls, FileControlID);


                            return false;
                        }

                        /*解决ios中照片旋转问题(前端处理，返回转换后的base64格式图片)   开始
                        try {
                            if (fileObj != null) {
                                //图片方向角 
                                var Orientation = null;
                                //获取照片方向角属性，用户旋转控制  
                                EXIF.getData(fileObj.files['0'], function () {
                                    // alert(EXIF.pretty(this));  
                                    EXIF.getAllTags(this);
                                    //alert(EXIF.getTag(this, 'Orientation'));   
                                    Orientation = EXIF.getTag(this, 'Orientation');
                                    //return;  
                                });

                                var oReader = new FileReader();
                                oReader.onload = function (e) {
                                    //var blob = URL.createObjectURL(file);  
                                    //_compress(blob, file, basePath);  
                                    var image = new Image();
                                    image.src = e.target.result;
                                    image.onload = function () {
                                        var expectWidth = this.naturalWidth;
                                        var expectHeight = this.naturalHeight;

                                        if (this.naturalWidth > this.naturalHeight && this.naturalWidth > 800) {
                                            expectWidth = 800;
                                            expectHeight = expectWidth * this.naturalHeight / this.naturalWidth;
                                        } else if (this.naturalHeight > this.naturalWidth && this.naturalHeight > 1200) {
                                            expectHeight = 1200;
                                            expectWidth = expectHeight * this.naturalWidth / this.naturalHeight;
                                        }
                                        var canvas = document.createElement("canvas");
                                        var ctx = canvas.getContext("2d");
                                        canvas.width = expectWidth;
                                        canvas.height = expectHeight;
                                        ctx.drawImage(this, 0, 0, expectWidth, expectHeight);
                                        var base64 = null;
                                        //修复ios  
                                        if (navigator.userAgent.match(/iphone/i)) {
                                            console.log('iphone');
                                            //alert(expectWidth + ',' + expectHeight);  
                                            //如果方向角不为1，都需要进行旋转 added by lzk  
                                            if (Orientation != "" && Orientation != 1) {
                                                //alert('旋转处理');
                                                switch (Orientation) {
                                                    case 6://需要顺时针（向左）90度旋转  
                                                        //alert('需要顺时针（向左）90度旋转');
                                                        rotateImg(this, 'left', canvas);
                                                        break;
                                                    case 8://需要逆时针（向右）90度旋转  
                                                        //alert('需要顺时针（向右）90度旋转');
                                                        rotateImg(this, 'right', canvas);
                                                        break;
                                                    case 3://需要180度旋转  
                                                        //alert('需要180度旋转');
                                                        rotateImg(this, 'right', canvas);//转两次  
                                                        rotateImg(this, 'right', canvas);
                                                        break;
                                                }
                                            }

                                        }
                                        //base64 = canvas.toDataURL("image/jpeg",0.8)
                                        //document.getElementById('myImage').src = base64
                                    };
                                };
                            }
                        } catch (e) { }
                        //解决ios中照片旋转问题   结束*/
                    }
                    else {
                        //点击取消后处理
                        Upload.DelImg(imgfileControls, FileControlID);
                        return false;
                    }
                },
                success: function (result) {
                    if (result.resultcode == g_const_Success_Code) {
                        $("#" + ShowControlID).attr("src", result.resultmessage).data("url", result.resultmessage);

                        var tt = imgfileControls.find(FileControlID);
                        if (tt != null) {
                            tt.picture = result.resultmessage;
                            $("#li_" + FileControlID).show();
                        }
                    }
                    else {
                        ShowMesaage(result.resultmessage);
                    }

                    //隐藏上传提示
                    if (typeof (HidWait) == "function") {
                        HidWait();
                    }
                },
                error: function (result) {
                    ShowMesaage(g_const_API_Message["7001"]);

                    //隐藏上传提示
                    if (typeof (HidWait) == "function") {
                        HidWait();
                    }
                }
            };
            $form.ajaxSubmit(options);
        }
    },
    /*基于jquery.form.min.js的 上传图片（同一页面中多个上传组）
    delffObj:包含find，del，clear方法的对象ID
    imgfileControls：包含图片对象数组ID
    FormID：FormID
    FileControlName:file控件ID
    ShowControlID:显示上传图片控件ID
    */
    UpLoadImgForMore: function (delffObj,imgfileControls, FormID, FileControlID, ShowControlID) {
        if ($("#" + FileControlID).val() == "") {
            //点击取消后处理
            Upload.DelImgForMore(delffObj, FileControlID);
            return false;
        }
        else {
            var $form = $("#" + FormID);
            var options = {
                dataType: "json",
                beforeSubmit: function () {
                    var file = $("#" + FileControlID).val();
                    if (file.length > 0) {
                        var exName = file.substring(file.lastIndexOf('.'), file.length).toLowerCase();
                        var isFind = Upload.ImgType.indexOf(exName);

                        if (isFind == -1) {
                            ShowMesaage("上传图片类型错误，请重新选择。</br>仅支持" + Upload.ImgTypeStr + "格式。");
                            //不是图片后处理
                            Upload.DelImgForMore(delffObj, FileControlID);
                            return false;
                        }
                    }
                    else {
                        //点击取消后处理
                        Upload.DelImgForMore(delffObj, FileControlID);
                        return false;
                    }
                },
                success: function (result) {
                    if (result.resultcode == g_const_Success_Code) {
                        $("#" + ShowControlID).attr("src", result.resultmessage).data("url", result.resultmessage);
                        var aai = ShowControlID.split('_')[0];
                        var tt = delffObj.find(eval("imgfileControls" + aai), FileControlID);
                        if (tt != null) {
                            tt.picture = result.resultmessage;
                            $("#li_" + FileControlID).show();
                        }
                    }
                    else {
                        ShowMesaage(result.resultmessage);
                    }
                },
                error: function (result) {
                    ShowMesaage(g_const_API_Message["7001"]);
                }
            };
            $form.ajaxSubmit(options);
        }
    },
    
    /*删除图片
    obj_imgfileControls:存储图片控件对象
    FileControlID：需要删除的控件ID
    */
    DelImg: function (obj_imgfileControls, FileControlID) {
        var fileimg = obj_imgfileControls.find(FileControlID);
        if (fileimg) {
            fileimg.picture = "";
            var aa = FileControlID.split('_')[0];

            $("#li_" + FileControlID).remove();
            $("#li_uploadimg").show();

        }
    },
    /*删除图片
obj_imgfileControls:存储图片控件对象
FileControlID：需要删除的控件ID
*/
    DelImgForMore: function (delffObj, obj_imgfileControls, FileControlID) {
        var fileimg = eval(delffObj).find(obj_imgfileControls,FileControlID);
        if (fileimg) {
            fileimg.picture = "";
            if (FileControlID != undefined) {
                var aa = FileControlID.split('_')[0];

                $("#li_" + FileControlID).remove();
                $("#li_uploadimg" + aa).show();
            }

        }
    },

};

//对图片旋转处理 added by lzk  
function rotateImg(img, direction,canvas) {    
    //alert(img);  
    //最小与最大旋转方向，图片旋转4次后回到原方向    
    var min_step = 0;    
    var max_step = 3;    
    //var img = document.getElementById(pid);    
    if (img == null)return;    
    //img的高度和宽度不能在img元素隐藏后获取，否则会出错    
    var height = img.height;    
    var width = img.width;    
    //var step = img.getAttribute('step');    
    var step = 2;    
    if (step == null) {    
        step = min_step;    
    }    
    if (direction == 'right') {    
        step++;    
        //旋转到原位置，即超过最大值    
        step > max_step && (step = min_step);    
    } else {    
        step--;    
        step < min_step && (step = max_step);    
    }    
            
    //旋转角度以弧度值为参数    
    var degree = step * 90 * Math.PI / 180;    
    var ctx = canvas.getContext('2d');    
    switch (step) {    
        case 0:    
            canvas.width = width;    
            canvas.height = height;    
            ctx.drawImage(img, 0, 0);    
            break;    
        case 1:    
            canvas.width = height;    
            canvas.height = width;    
            ctx.rotate(degree);    
            ctx.drawImage(img, 0, -height);    
            break;    
        case 2:    
            canvas.width = width;    
            canvas.height = height;    
            ctx.rotate(degree);    
            ctx.drawImage(img, -width, -height);    
            break;    
        case 3:    
            canvas.width = height;    
            canvas.height = width;    
            ctx.rotate(degree);    
            ctx.drawImage(img, -width, 0);    
            break;    
    }    
} 

//获取图片方向
function getPhotoOrientation(img) {
   var orient;
   EXIF.getData(img, function () {
      orient = EXIF.getTag(this, 'Orientation');
   });
   return orient;
} 

/*微信JSAPI，调用扫描二维码*/
var WX_JSAPI_T = {
    /*腾讯对象wx,请先赋值*/
    wx: null,
    /*JSAPI参数对象*/
    wxparam: {
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '', // 必填，公众号的唯一标识
        timestamp: 0, // 必填，生成签名的时间戳
        nonceStr: '', // 必填，生成签名的随机串
        signature: '',// 必填，签名，见附录1
        jsApiList: '' // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    },
    /*jsApiList*/
    jsApiList: g_const_wx_jsapi.scanQRCode,

    func_CallBack: function () { },
    /*从接口获取参数,参数jsApiList是要调用的接口名,多个逗号分隔*/
    LoadParam: function (jsApiList) {
        if (IsInWeiXin.check()) {
            WX_JSAPI_T.jsApiList = jsApiList;
            var obj_data = { action: "wxshare", jsApiList: jsApiList, surl: window.location.href, debug: WX_JSAPI_T.wxparam.debug };
            var request = $.ajax({
                url: g_INAPIUTL,
                cache: false,
                method: g_APIMethod,
                data: obj_data,
                dataType: g_APIResponseDataType
            });

            request.done(function (msg) {
                if (msg.resultcode == "0") {
                    WX_JSAPI_T.wxparam.appId = msg.appId;
                    WX_JSAPI_T.wxparam.timestamp = msg.timestamp;
                    WX_JSAPI_T.wxparam.nonceStr = msg.nonceStr;
                    WX_JSAPI_T.wxparam.jsApiList = msg.jsApiList;
                    WX_JSAPI_T.jsApiList = msg.jsApiList;
                    WX_JSAPI_T.wxparam.signature = msg.signature;
                    WX_JSAPI_T.CallWeiXin();
                }
                else {
                    //ShowMesaage('112233');
                    //ShowMesaage(msg.resultmessage);
                }
            });

            request.fail(function (jqXHR, textStatus) {
                //ShowMesaage("调用接口出错!")
            });
        }
    },
    /*调用微信接口,参数wx为腾讯wx对象*/
    CallWeiXin: function () {
        WX_JSAPI_T.wx.config(WX_JSAPI_T.wxparam);
        WX_JSAPI_T.wx.ready(WX_JSAPI_T.WX_Ready);
        WX_JSAPI_T.wx.error(function (res) {
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            //ShowMesaage(res);
        });
    },
    /*微信准备好后执行的函数*/
    WX_Ready: function () {
        //$("#showlog").text(JSON.stringify(WX_JSAPI_T.jsApiList));
        if (typeof (WX_JSAPI_T.func_CallBack) == "function")
            WX_JSAPI.func_CallBack();
        /*微信扫一扫*/
        if (WX_JSAPI_T.jsApiList[0].toLowerCase().indexOf("scanqrcode") != -1) {
            WX_JSAPI_T.WX_ScanQRCode();
        }
        /*获取地理位置接口*/
        if (WX_JSAPI_T.jsApiList[0].toLowerCase().indexOf("geolocation") != -1) {
            WX_JSAPI_T.WX_GetLocation();
        }

    },

    /*微信准备好后执行的函数*/
    WX_ScanQRCode: function () {
        //调用扫描二维码
        WX_JSAPI_T.wx.scanQRCode({
            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                if (res.errMsg.toLowerCase() == "scanqrcode:ok") {
                    var str = result.split(',');
                    if (str.length > 1) {
                        WX_JSAPI_T.WX_Success(str[1]);
                    }
                    else {
                        WX_JSAPI_T.WX_Success(result);
                    }
                }
            }
        });
    },
    /*获取地理位置接口*/
    WX_GetLocation: function () {
        WX_JSAPI_T.wx.getLocation({
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
    /*操作完成时回调的函数*/
    WX_Success: function (resultStr) {
        //ShowMesaage(wx_jsapi_type+"操作完成");
        MyTKSH_TXWL.ScanQRCodeResult(resultStr);
    },
    /*操作取消时回调的函数*/
    WX_Cancel: function (wx_jsapi_type) {
        //ShowMesaage(wx_jsapi_type+"操作取消");
    }
};

/*判断是否来源于APP的请求*/
var CheckFromApp = {
    param: {
        app: '', // app名称
        from: '', // android或ios
        to: '', // 登录：login，注册：register，重置密码：resetPassword
        wxopenid: '', // 微信授权ID，不需要微信登录时传空
        showphonelogin: '1', // 是否显示“实际快捷登录”，由于快捷登录接口无法获得mem_code,暂时隐藏
        backurl: "" //web回通知地址
    },
    Check: function () {
        //if (GetQueryString("app") != "" && GetQueryString("from") != "" && GetQueryString("to") != "") {
        if (GetUrlParam.up_urlparam("app") != "" && GetUrlParam.up_urlparam("from") != "" && GetUrlParam.up_urlparam("to") != "") {
            CheckFromApp.param.app = GetUrlParam.up_urlparam("app");
            CheckFromApp.param.from = GetUrlParam.up_urlparam("from");
            CheckFromApp.param.to = GetUrlParam.up_urlparam("to");
            if (GetUrlParam.up_urlparam("wxopenid") != "") {
                CheckFromApp.param.wxopenid = GetUrlParam.up_urlparam("wxopenid");
            }
            else {
                CheckFromApp.param.wxopenid = "";
            }
            if (CheckFromApp.param.from == "web") {
                CheckFromApp.param.backurl = GetUrlParam.up_urlparam("backurl");
            }
            return true;
        }
        else {
            CheckFromApp.param.app = "";
            CheckFromApp.param.from = "";
            CheckFromApp.param.to = "";
            CheckFromApp.param.wxopenid = "";
            CheckFromApp.param.backurl = "";
            return false;
        }
    },
    Clear: function () {
        CheckFromApp.param.app = "";
        CheckFromApp.param.from = "";
        CheckFromApp.param.to = "";
        CheckFromApp.param.wxopenid = "";
        CheckFromApp.param.backurl = "";
        return false;
    },
    UrlAddParam: function () {
        if (CheckFromApp.param.app != "") {
            var tt = "&app=" + CheckFromApp.param.app + "&from=" + CheckFromApp.param.from + "&to=" + CheckFromApp.param.to + "&wxopenid=" + CheckFromApp.param.wxopenid;
            if (CheckFromApp.param.from == "web") {
                tt += "&backurl=" + escape(CheckFromApp.param.backurl);
            }
            return tt;
        }
        else {
            return "";
        }
    },

};
/*接口外部地址*/var g_APIUTL_User = "/JYH/API_User.aspx";
/*App使用，直接调用微公社接口*/
var AppLogin = {
    api_target: "com_cmall_membercenter_api_UserLogin",
    api_input: { "login_name": "", client_source: "", password: "" },

    userlogin: function (login_name, password) {
        //赋值
        AppLogin.api_input.login_name = login_name;//String.Replace($("#txtLogin").val());
        AppLogin.api_input.client_source = "site";
        AppLogin.api_input.password = password;//String.Replace($("#txtPass").val());

        //组织提交参数
        var s_api_input = JSON.stringify(this.api_input);
        //提交接口[api_token不为空，公用方法会从sission中获取]
        var obj_data = { "api_input": s_api_input, "api_target": AppLogin.api_target, "api_token": g_const_api_token.Unwanted };
        var purl = g_APIUTL_User;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });
        //正常返回
        request.done(function (msg) {
            if (msg.resultCode == g_const_Success_Code) {
                AppLogin.Load_Result(msg);
            }
            else {
                try {
                    if ($('#btnToLogin')) {
                        UserLogin_aa.RegIng = 0;
                        $('#btnToLogin').attr("class", "d_btn_dl");
                    }
                } catch (e) {

                }
                if (g_const_API_Message[msg.resultCode]) {
                    ShowMesaage(g_const_API_Message[msg.resultCode]);
                }
                else {
                    ShowMesaage(msg.resultMessage);
                }
                return;
            }
        });
        //接口异常
        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    /*接口返回的处理*/
    Load_Result: function (o_login_info) {
        var istype = CheckFromApp.param.from;

        try {
            AppLogin.SaveLoginInfoForAppLogin(o_login_info.user_token, AppLogin.api_input.login_name, AppLogin.api_input.password);
        }
        catch (e) {
            //alert(e.message);
        }

        /*通知app关闭窗体*/
        //setTimeout(function () {
        //    AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }');
        //}, 1000);

        //通知登录结果
        AppLogin.jsInvokeClient('{'
            + '"type": "user_login",'
            + '"obj": {'
                + '"user_token": "' + o_login_info.user_token + '",'
                + '"user_phone": "' + AppLogin.api_input.login_name + '",'
                + '"mem_code": "' + o_login_info.user.member_code + '",'
                + '"hxUserLoginInfo": {'
                    + '"hxUserName": "",'
                    + '"hxPassWord": "",'
                    + '"hxWorkerId": "",'
                    + '"hxStatus": "0",'
                    + '"extendInfo": {'
                        + '"member_avatar": "' + o_login_info.user.avatar + '",'
                        + '"nickname": "' + o_login_info.user.nickname + '"'
                    + '}'
                + '}'
            + '}'
        + '}', 'true');

        //通知app关闭窗体*/

        if (istype == 'ios') {
            setTimeout(function () {
                AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }', 'true');
            }, 1000);
        }
        else if (istype == 'android') {
            AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }', 'true');
        }
        else if (istype == 'web') {
            var backurl = unescape(CheckFromApp.param.backurl);
            CheckFromApp.Clear();
            if (backurl.indexOf("?") > -1) {
                backurl += "&p=" + AppLogin.api_input.login_name;
            }
            else {
                backurl += "?p=" + AppLogin.api_input.login_name;
            }
            window.location.href = backurl;
        }
    },
    /*通知APP处理*/
    jsInvokeClient: function (option, onlyClose) {
        var istype = CheckFromApp.param.from;

        var trt = JSON.parse(option);
        //alert(trt.type);
        if (trt.type == "close_window") {
            CheckFromApp.Clear();
        }
        //alert(option);

        if (onlyClose == "true") {
            //alert(option);

            if (istype == 'android') {
                try {
                    //window.notify.notifyOnAndroid(JSON.parse(option));
                    window.notify.notifyOnAndroid(option);
                } catch (e) {
                    alert(e.message);
                }
            } else if (istype == 'ios') {
                try {
                    var baseEncodeStr = Base64.base64encode(Base64.utf16to8(option));
                    baseEncodeStr = baseEncodeStr.replace(/[\=]/g, "");
                    baseEncodeStr = baseEncodeStr.replace(/[\+]/g, "//");
                    window.location.href = "wgscoc://" + baseEncodeStr;
                } catch (e) {
                    alert(e.message);
                }
            }
        }
    },
    /*APP使用微信商城登录后，保存用户信息*/
//    SaveLoginInfoForAppLogin: function (user_token, user_phone, password) {
//        $.ajax({
//            type: "POST",//用POST方式传输
//            dataType: "json",//数据格式:JSON
//            url: '/Ajax/API.aspx',//目标地址
//            data: "t=" + Math.random() +
//                    "&action=SaveLoginInfoForAppLogin" +
//                    "&token=" + user_token +
//                    "&username=" + user_phone +
//                    "&password=" + password +
//                    "&wxopenid=" + CheckFromApp.param.wxopenid,
//            success: function (rejson) {
//                if (rejson.resultcode == "1") {
//                    //保存信息成功
//                }
//                else {
//                    //保存信息失败
//                }
//            }
//        });
//    },

};

/*通过Token获取用户信息*/
var GetMemberInfoByToken = {
    api_target: "com_cmall_membercenter_api_GetMemberInfo",
    api_input: { "version": 1},

    userlogin: function (Token, mobile, membercode) {
        //赋值
        //AppLogin.api_input.login_name = login_name;//String.Replace($("#txtLogin").val());
        //AppLogin.api_input.client_source = "site";
        //AppLogin.api_input.password = password;//String.Replace($("#txtPass").val());

        //组织提交参数
        var s_api_input = JSON.stringify(this.api_input);
        //提交接口[api_token不为空，公用方法会从sission中获取]
        var obj_data = { "api_input": s_api_input, "api_target": GetMemberInfoByToken.api_target, "api_token": Token };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });
        //正常返回
        request.done(function (msg) {
            if (msg.resultCode == g_const_Success_Code) {
                GetMemberInfoByToken.Load_Result(msg, Token, mobile, membercode);
            }
            else {
                ShowMesaage(msg.resultMessage);
                return;
            }
        });
        //接口异常
        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
    },
    /*接口返回的处理*/
    Load_Result: function (o_login_info, Token, mobile,membercode) {
        var istype = CheckFromApp.param.from;

        try {
            AppLogin.SaveLoginInfoForAppLogin(Token, mobile, "");
        }
        catch (e) {
            //alert(e.message);
        }

        /*通知app关闭窗体*/
        //setTimeout(function () {
        //    AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }');
        //}, 1000);

        //通知登录结果
        AppLogin.jsInvokeClient('{'
            + '"type": "user_login",'
            + '"obj": {'
                + '"user_token": "' + Token + '",'
                + '"user_phone": "' + mobile + '",'
                + '"mem_code": "' + (o_login_info.user.member_code == "" ? membercode : o_login_info.user.member_code) + '",'
                + '"hxUserLoginInfo": {'
                    + '"hxUserName": "",'
                    + '"hxPassWord": "",'
                    + '"hxWorkerId": "",'
                    + '"hxStatus": "0",'
                    + '"extendInfo": {'
                        + '"member_avatar": "' + o_login_info.user.avatar + '",'
                        + '"nickname": "' + o_login_info.user.nickname + '"'
                    + '}'
                + '}'
            + '}'
        + '}', 'true');

        //通知app关闭窗体*/

        if (istype == 'ios') {
            setTimeout(function () {
                AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }', 'true');
            }, 1000);
        }
        else if (istype == 'android') {
            AppLogin.jsInvokeClient('{ "type": "close_window", "obj": {} }', 'true');
        }
        else if (istype == 'web') {
            var backurl = unescape(CheckFromApp.param.backurl);
            CheckFromApp.Clear();
            if (backurl.indexOf("?") > -1) {
                backurl += "&p=" + mobile;
            }
            else {
                backurl += "?p=" + mobile;
            }
            window.location.href = backurl;
        }
    },
};

var GetUrlParam = {

    config: {
        cookie_base: 'cookie-zw-',
        // 是否调试模式
        debug: true
    },

    // -------------------- JSON相关操作

    // 格式化json 对象转成字符串
    json_stringify: function (o_obj) {
        return JSON.stringify(o_obj);
    },
    // 将字符串转换成对象
    json_parse: function (s_json) {
        return JSON.stringify(s_json);
    },
    // 获取请求链接中的参数
    up_urlparam: function (sKey, sUrl) {

        var sReturn = "";

        if (!sUrl) {
            sUrl = window.location.href;
            if (sUrl.indexOf('?') < 1) {
                sUrl = sUrl + "?";
            }
        }

        var sParams = (sUrl.indexOf("?") != -1) ? sUrl.split('?')[1].split('&') : '';

        for (var i = 0, j = sParams.length; i < j; i++) {

            var sKv = sParams[i].split("=");
            if (sKv[0] == sKey) {
                sReturn = sKv[1];
                break;
            }
        }

        return sReturn;

    },
    repalce_urlparam_val: function (sKey, nValue, sUrl) {

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
            if (sKv[0] == sKey) {
                sReturn = sKv[1];
                break;
            }
        }
        var repalceStr = sKey + "=" + sReturn;

        return (sUrl.substring(0, sUrl.indexOf(sKey)) + sKey + "=" + nValue + sUrl.substring(sUrl.indexOf(repalceStr) + repalceStr.length));
    },

    // -------------------- COOKIE相关操作
    // Cookie操作
    cookie: function (key, value, options) {

        // 判断如果是写操作 写到根目录
        if (value !== undefined) {
            if (!options) {
                options = {
                };
            }

            options.path = "/";
            if (!options.expires)
                options.expires = 30;
        }

        return $.cookie(zapbase.config.cookie_base + key, value, options);
    },

    // -------------------- DEBUG调试相关
    d: function (o_obj) {
        if (window.console && window.console.log) {
            console.log(zapbase.json_stringify(o_obj));
        }
    }
};

/*App主动调用隐藏头部方法，解决首页调用IOS隐藏头部方法异常问题*/
function apphidehead() {

    if (CheckMachine.versions.android && !CheckMachine.versions.inWeiXin) {
        try {
            //隐藏头部【方法报错也不显示】
            window.share.hidehead();
            //res = true;
        }
        catch (e) {

        }
    }
    else if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {
        try {
            //alert("ios_主动调用隐藏头部");
            //隐藏头部【方法报错也不显示】
            OCModel.hidehead();
            //res = true;

            //首页显示关闭窗口层
            //$("#div_appclosewindow").show();
        }
        catch (e) {
            //alert("ios_主动调用隐藏头部报错啦OCModel.hidehead()");
        }
    }

}

function isNeedPullRefresh() {

    if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {
        try {
            if (location.pathname.toLowerCase().indexOf("showorder_list") > -1) {
                return true;
            }
        }
        catch (e) {
            return false;
        }
    }
    else {
        return false;
    }
}

function copyToClipBoard(s) {
    //alert(s);
    if (window.clipboardData) {
        window.clipboardData.setData("Text", s);
        alert("已经复制到剪切板！" + "\n" + s);
    } else if (navigator.userAgent.indexOf("Opera") != -1) {
        window.location = s;
    } else if (window.netscape) {
        try {
            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
        } catch (e) {
            alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
        }
        var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
        if (!clip)
            return;
        var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
        if (!trans)
            return;
        trans.addDataFlavor('text/unicode');
        var str = new Object();
        var len = new Object();
        var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
        var copytext = s;
        str.data = copytext;
        trans.setTransferData("text/unicode", str, copytext.length * 2);
        var clipid = Components.interfaces.nsIClipboard;
        if (!clip)
            return false;
        clip.setData(trans, null, clipid.kGlobalClipboard);
        alert("已经复制到剪切板！" + "\n" + s)
    }
}


/*APP嵌入页面调用外部调用的关闭窗口、后退和隐藏头部*/
var UseAppFangFa = {
    num:5,
    /*来源*/
    from: "",
    /*目的*/
    to: "",
    /*手机号*/
    mobile: "",
    /*请求时间*/
    t: "",
    /*验签串*/
    mac: "",
    /*关闭窗口[只适用于web请求]*/
    WebCloseWin: function () {
        window.opener = null;
        window.open('', '_self');
        window.close();
    },
    /*是否显示关闭APP窗口按钮*/
    ShowCloseBtn: function (id) {
        var res = false;
        if (CheckMachine.versions.android && !CheckMachine.versions.inWeiXin) {
            try {
                //隐藏头部【方法报错也不显示】
                UseAppFangFa.CaoZuo("hidehead");
                res = true;
            }
            catch (e) {

            }
        }
        else if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {
            //ios因为框架原因，导致页面加载时主动调用方法失败，解决方法是提供独立的js方法【function apphidehead()】，ios中主动调用
            //try {
            //    //隐藏头部【方法报错也不显示】
            //    OCModel.hide_head();;
            //    res=true;
            //}
            //catch (e) {

            //}
        }
    },
    /*调用APP提供的方法*/
    CaoZuo: function (type, shareStr, isNeedRefresh,webUrl) {
        //shareJSON：{"title":"标题","imgUrl":"分享图片","shareContent":"内容","shareUrl":"分享链接"}
        if (!(shareStr == undefined) && type == "share") {
            try {
                shareJSON = JSON.parse(shareStr);
            }
            catch (e) {
                alert("分享内容不是JSON结构,shareStr:" + shareStr);
                return false;

            }
        }
        //获取来源
        if (CheckMachine.versions.android && !CheckMachine.versions.inWeiXin) {
            try {
                UseAppFangFa.from = "android";
                //安卓
                switch (type) {
                    /*5.2.6 打开和关闭APP原生等待效果方法 开始*/
                    case "showWebLoadingAnimation_open":
                        try {
                            return window.share.showWebLoadingAnimation(true);
                        }
                        catch (e) {
                            
                        }
                        break;
                    case "showWebLoadingAnimation_close":
                        try {
                            return window.share.showWebLoadingAnimation(false);
                        }
                        catch (e) {

                        }
                        break;
                    /*5.2.6 打开和关闭APP原生等待效果方法 结束*/
                    /*5.2.2 新增方法 开始*/
                    case "getDeviceInfomation":
                        try {
                            /*获取设备信息
                            返回
                            {"app_vision":"5.2.4","from":"1025a","mac":"94:0E:6B:34:7D:B3","model":"ALP-AL00","net_type":"cellular","op":"中国移动","os":"android","os_info":"8.0.0","product":"huijiayou_android","screen":"1080x1808","sqNum":"LSH180704100007","uniqid":"47d4c9d5-a83a-378d-8379-b23ce824e141","useID":"13211111126"}
                            */
                            return window.share.getDeviceInfomation();
                        }
                        catch (e) {
                            return JSON.parse("{"
	                            + "\"app_vision\": \"0\","
	                            + "\"from\": \"\","
	                            + "\"mac\": \"\","
	                            +"\"mac\": \"\","
	                            + "\"model\": \"\","
	                            + "\"net_type\": \"\","
	                            + "\"op\": \"\","
	                            +"\"os\": \"\","
	                            + "\"os_info\": \"\","
	                            + "\"product\": \"\","
	                            + "\"screen\": \"\","
	                            + "\"uniqid\": \"\","
	                            + "\"useID\": \"\","
	                            + "\"sqNum\": \"\"}");
                        }
                        break;
                    case "isNOtLoginPopWeb":
                        //登录页面中如果直接关闭，此方法实现页面转向到首页，解决APP中内嵌的H5页面无登录信息时空白问题					
                        window.share.isNOtLoginPopWeb(true);
                        break;
                    /*5.2.2 新增方法 结束*/
                    case "close":
                        //安卓提供的调用APP关闭窗口方法					
                        window.share.closewindow();
                        break;
                    case "back":
                        //安卓提供的调用APP后退方法					
                        window.share.backwindow();
                        break;
                    case "hidehead":
                        //安卓提供的调用APP隐藏头部方法					
                        window.share.hidehead();
                        break;
                    case "showhead":
                        //安卓提供的调用APP显示头部方法					
                        window.share.showhead();
                        break;
                    case "share":
                        //安卓提供的调用APP分享方法					
                        window.share.shareOnDialogAndroid(shareJSON.title, shareJSON.imgUrl, shareJSON.shareContent, shareJSON.shareUrl);
                        break;
                    case "refresh":
                        //安卓提供的调用APP显示头部方法					
                        window.share.isNeedPullRefresh(isNeedRefresh);
                        break;
                        //5.3.4 跳转web页
                    case "jumptoPage":
                        //调用APP打开h5页面方法,	
                        try {
                            toActivity.jumpPage(webUrl);
                        }
                        catch (fhhj) {
                            alert("toActivity.jumpPage("+webUrl+")报错了");

                        }
                        break;
                }
            }
            catch (e) {
                UseAppFangFa.num = UseAppFangFa.num - 1;
                if (UseAppFangFa.num > 0) {
                    setInterval(UseAppFangFa.CaoZuo(type, shareStr, isNeedRefresh), 1000);
                }
                else {
                    UseAppFangFa.num = 5
                }
            }
        }
        else if ((CheckMachine.versions.ios || CheckMachine.versions.iPhone || CheckMachine.versions.iPad) && !CheckMachine.versions.inWeiXin) {
            try{
                UseAppFangFa.from = "ios";
                /*
                try {
                    //5.1.4新提供方法，用于避免ios框架未记载完毕，导致的调用方法失败【已作废】
                    OCModel.webViewDontNewJsContext();
                }
                catch(e){
                }*/
                //IOS
                switch (type) {
                    /*5.2.6 打开和关闭APP原生等待效果方法 开始*/
                    case "showWebLoadingAnimation_open":
                        try {
                            return OCModel.showWebLoadingAnimation(true);
                        }
                        catch (e) {

                        }
                        break;
                    case "showWebLoadingAnimation_close":
                        try {
                            return OCModel.showWebLoadingAnimation(false);
                        }
                        catch (e) {

                        }
                        break;
                        /*5.2.6 打开和关闭APP原生等待效果方法 结束*/
                    /*5.2.2 新增方法 开始*/
                    case "getDeviceInfomation":
                        try{
                            /*获取设备信息
                            {
	                            "net_type": "wifi",
	                            "os_info": "iPhone 11.2.1",
	                            "uniqid": "75214f6075b0bf2784691f6e15f44d3b484ea846",
	                            "mac": "02:00:00:00:00:00",
	                            "idfa": "7ED1F433-DC3D-40E6-8BC4-6052075F1E22",
	                            "product": "HuiJiaYou_iPhone",
	                            "screen": "1242x2208",
	                            "from": "JailBreak",
	                            "app_vision": "5.2.2",
	                            "os": "iOS",
	                            "op": "op",
	                            "model": "iPhone 6s Plus"
                            }
                            */
                            return OCModel.getDeviceInfomation();
                        }
                        catch (e) {
                            return JSON.parse("{"
	                            + "\"net_type\": \"\","
	                            + "\"os_info\": \"\","
	                            + "\"uniqid\": \"\","
	                            + "\"mac\": \"\","
	                            + "\"idfa\": \"\","
	                            + "\"product\": \"\","
	                            + "\"screen\": \"\","
	                            + "\"from\": \"\","
	                            + "\"app_vision\": \"0\","
	                            + "\"os\": \"\","
	                            + "\"op\": \"\","
	                            + "\"model\": \"\"}");
                        }

                    break;

                    case "isNOtLoginPopWeb":
                        try {
                            //登录页面中如果直接关闭，此方法实现页面转向到首页，解决APP中内嵌的H5页面无登录信息时空白问题					
                            OCModel.isNOtLoginPopWeb(true);
                        }
                        catch (e) {
                        }
                        
                        break;
                    /*5.2.2 新增方法 结束*/
                    case "close":
                        //提供的调用APP关闭窗口方法					
                        OCModel.closewindow();
                        break;
                    case "back":
                        //调用APP后退方法					
                        OCModel.backwindow();
                        break;
                    case "hidehead":
                        //调用APP隐藏头部方法					
                        OCModel.hidehead();
                        break;
                    case "showhead":
                        //调用APP显示头部方法				
                        OCModel.showhead();
                        break;
                    case "share":
                        //调用APP分享方法,	
                        try {
                            OCModel.shareweb(shareStr);
                            //alert("ios:OCModel.shareweb完毕");
                        }
                        catch (fhhj) {
                            alert("ios:OCModel.shareweb(" + shareStr + ")报错了");

                        }
                        break;
                    case "jumptoPage":
                        //调用APP打开h5页面方法,	
                        try {
                            OCModel.jumptoPage("{"
                              +"\"jumpPage\" : \"OneDollarsnatchViewController\","
                              +"\"jumpValue\" : {"
                                  +"\"webURL\" : \""+webUrl+"\""
                             + " }"
                            +"}");
                        }
                        catch (fhhj) {
                            alert("ios:OCModel.jumptoPage({"
                              +"\"jumpPage\" : \"OneDollarsnatchViewController\","
                              +"\"jumpValue\" : {"
                                  +"\"webURL\" : \""+webUrl+"\""
                             + " }"
                            +"})报错了");

                        }
                        break;

                }
                UseAppFangFa.num = 5;
            }
            catch (e) {
                UseAppFangFa.num = UseAppFangFa.num - 1;
                if (UseAppFangFa.num>0) {
                    setInterval(UseAppFangFa.CaoZuo(type, shareStr, isNeedRefresh), 1000);
                }
                else{
                    UseAppFangFa.num =5
                }
            }
        }
        else {
            ////web【暂不支持】
            UseAppFangFa.from = "web";
            //UseAppFangFa.mobile = GetQueryString("p");
            //UseAppFangFa.t = GetQueryString("tt");
            //UseAppFangFa.mac = GetQueryString("mac");
            //UseAppFangFa.AutoLogin();
        }


    },
};

//功能：当div中的img出现在屏幕时，加载需要显示的图片。
//参数divid，需要进行懒加载的div的id，传空表示前部图片
//参数beginHeight，img距离显示在页面的提前量
function lazyload_allimg(divid, beginHeight) {
    if (!beginHeight) beginHeight = 0;
    if (divid != "") {
        lazyDivList = $("#" + divid + " img");

        lazyDivList.bind("lazyme", function (e, scrTop) {
            var url = $(this).attr("request-url");//从request-url属性中获得真正需要加载的内容
            if (url != "" && !(url == undefined)) {
                //图片距离顶部的距离
                var offset = $(this).offset().top;
                //滚动距离+体现加载高度+屏幕可视高度
                var temp = scrTop + beginHeight + $(window).height();

                if (temp >= offset && temp > 0) {
                    $(this).attr("src", url);
                    $(this).attr("request-url", "");
                }

                //var offset = $(this).offset().top;
                //var interval = $(window).height() - $(this).height();//当前页面可视高度
                //var sumB = offset;
                //var sumS = offset - interval;

                //if (scrTop >= sumS && scrTop <= sumB) {
                //    $(this).attr("src", url);
                //    $(this).attr("request-url", "");
                //}
            }
        });
    }
    else {
        lazyDivList_all = $("img");
        lazyDivList_all.bind("lazyme", function (e, scrTop) {
            var url = $(this).attr("request-url");//从request-url属性中获得真正需要加载的内容
            if (url != "" && !(url == undefined)) {
                //图片距离顶部的距离
                var offset = $(this).offset().top;
                //滚动距离+体现加载高度+屏幕可视高度
                var temp = scrTop + beginHeight + $(window).height();

                if (temp >= offset && temp > 0) {
                    $(this).attr("src", url);
                    $(this).attr("request-url", "");
                }

                //var offset = $(this).offset().top;
                //var interval = $(window).height() - $(this).height();//当前页面可视高度
                //var sumB = offset;
                //var sumS = offset - interval;

                //if (scrTop >= sumS && scrTop <= sumB) {
                //    $(this).attr("src", url);
                //    $(this).attr("request-url", "");
                //}
            }
        });
    }
    $(window).scroll(function () {
        srcTop = $(window).scrollTop();
        if (srcTop >= beginHeight) {
            if (typeof (lazyDivList) != "undefined") {
                lazyDivList.trigger("lazyme", $(window).scrollTop());
            }
            if (typeof (lazyDivList_all) != "undefined") {
                lazyDivList_all.trigger("lazyme", $(window).scrollTop());
            }

        }

        //头部效果
        if ($(document).scrollTop() > 30) {
            $(".search50_logo").css({
                "WebkitTransform": "scale(.4, .4)",
                "left": "110%"
            });
            $(".search50_img").attr("src", "../img/wza_img/search50_img02.png").css({
                "WebkitTransform": "scale(.7, .7)",
                "left": ".23rem"
            });
            $(".search50_back").css({
                "width": "81%"
            });
            $(".search50_txt").show();
        } else {
            $(".search50_logo").css({
                "WebkitTransform": "scale(1, 1)",
                "left": "50%"
            });
            $(".search50_img").attr("src", "../img/wza_img/search50_img01.png").css({
                "WebkitTransform": "scale(1, 1)",
                "left": ".1rem"
            });
            $(".search50_back").css({
                "width": "0"
            });
            $(".search50_txt").hide();
        }
    });


}

function lazyDivListFirstPage(objID) {
    $("#" + objID + " img").each(function (i, nth) {
        var url = $(this).attr("request-url");//从request-url属性中获得真正需要加载的内容
        if (url != "" && !(url == undefined)) {
            $(this).attr("src", url);
            $(this).attr("request-url", "");
        }
    });


}

/* 5.0.4 增加日期处理方法  开始*/
function renderTemplate(template, data) {
    var s = template.replace(g_const_regex_template, function (fullMatch, capture) {
        //if (data[capture])
        if (typeof (data[capture]) != "undefined")
            return data[capture];
        else
            return "";
    });
    return s;
}

/**
         * 获取上一个月
         *
         * @date 格式为yyyy-mm-dd的日期，如：2014-01-25
         */
function getPreMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中月的天数
    var year2 = year;
    var month2 = parseInt(month) - 1;
    if (month2 == 0) {//如果是1月份，则取上一年的12月份
        year2 = parseInt(year2) - 1;
        month2 = 12;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {//如果原来日期大于上一月的日期，则取当月的最大日期。比如3月的30日，在2月中没有30
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;//月份填补成2位。
    }
    //var t2 = year2 + '-' + month2 + '-' + day2;
    var t2 = year2 + '-' + month2;
    return t2;
}

function GetNowMonth() {
    //获取完整的日期
    var date = new Date;
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    month = (month < 10 ? "0" + month : month);
    var mydate = (year.toString() + month.toString());
}
/*月份显示文字转化*/
function FormatMonth(dateStr) {
    dateStr = dateStr.replace(/-/g, "/");
    var dated = new Date(dateStr);
    var now = new Date();

    var showStr = "";
    if (dated.getFullYear() == now.getFullYear() && dated.getMonth() == now.getMonth()) {
        showStr = "本月"
    }
    else {
        showStr = dated.getFullYear() + "年" + (dated.getMonth()+1) + "月"
    }
    return showStr;
}
/*日期显示文字转化*/
function ShowDate(dateStr,type) {
    dateStr = dateStr.replace(/-/g, "/");
    var dated = new Date(dateStr);
    var now = new Date();

    var showStr = "";
    switch(type){
        case "1": //当天或返回月日
            if (dated.getFullYear() == now.getFullYear() && dated.getMonth() == now.getMonth() && dated.getDay() == now.getDay()) {
                showStr = "今天"
            }
            else {
                if ((dated.getMonth() + 1) < 10) {
                    showStr = "0" + (dated.getMonth() + 1).toString();
                }
                else {
                    showStr = dated.getMonth()+1;
                }

                if (dated.getDay() < 10) {
                    showStr += "-0" + dated.getDay().toString();
                }
                else {
                    showStr += "-0" + dated.getDay().toString();;
                }
            }
            break;
    }
   
    return showStr;
}
/*日期转星期几*/
function GetWeek(dateStr) {
    dateStr = dateStr.replace(/-/g, "/");
    var dated = new Date(dateStr);

    var weekArray = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    var week = weekArray[dated.getDay()];
    return week;
}

/* 5.0.4 增加日期处理方法  结束*/

/*判断图片是否存在*/
function CheckImgExists(imgurl,defaultPic) {
    var ImgObj = new Image(); //判断图片是否存在  
    ImgObj.src = imgurl;
    //没有图片，则返回-1  
    if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
        return imgurl;
    } else {
        return defaultPic;
    }
}

/*获得时间差
type:返回时间格式（毫秒：ms，秒：s，分：m，小时：h，天：day）
*/
function GetLeftTim(endTime,type) {
    var date_last = Date.Parse(endTime);
    var date_now = new Date();
    var ts = date_now.getTime() - date_last.getTime();  //时间差的毫秒数      

    if (ts > 0) {
        switch (type) {
            case "day":
                //var days = Math.floor(ts / g_const_days);
                var days = Math.ceil(ts / g_const_days);
                return days;
                break;
            case "h":
                var hours = Math.ceil(ts / g_const_hours);
                return hours;
                break;
            case "m":
                var minutes = Math.ceil(ts / g_const_minutes);
                return minutes;
                break;
            case "s":
                var seconds = Math.ceil(ts / g_const_seconds);
                return seconds;
                break;
            case "ms":
                var mseconds = Math.ceil(ts / (g_const_seconds * 1000));
                return mseconds;
                break;
        }
    }
    else {
        return 0;
    }
}

/*divID1:显示的层，divID2:隐藏的层，beginnum：显示第几个（从0开始）*/
function ShowBigPic(divID1, divID2, beginnum) {
    if (divID2 != "") {
        $("#" + divID2).hide();
    }
    $("#" + divID1).show();

    var swiper_zp = new Swiper('.swiper-container', {
        loop: false,
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        paginationClickable: false,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 0,
        autoplayDisableOnInteraction: false,
        initialSlide: beginnum
    });
}

/*divID1:显示的层，divID2:隐藏的层，beginnum：显示第几个（从0开始）*/
function ShowBigPicList(divID1, divID2, beginnum, type, skuCode) {
    if (divID2 != "") {
        $("#" + divID2).hide();
    }
    var temp = "";
    if (type == "zp") {
        //temp = eval("bigpic_zp." + skuCode);
        for (var i = 0; i < bigpic_zp.length; i++) {
            if (bigpic_zp.skuCode == skuCode) {
                temp = bigpic_zp.imgStr;
                break;
            }
        }
    }
    else {
        for (var i = 0; i < bigpic_pj.length; i++) {
            if (bigpic_pj.skuCode == skuCode) {
                temp = bigpic_pj.imgStr;
                break;
            }
        }
    }

    var stpl_pj = $("#tpl_swipeSlide")[0].innerHTML;
    var data_pj = {
        "swipeSlideList": temp
    };
    var html_pj = renderTemplate(stpl_pj, data_pj);


    $("#bigpiclist").html(html_pj);

    if (temp != "") {
        $("#" + divID1).show();
    }

    swiper_zp = null;
    var swiper_zp = new Swiper('.swiper-container', {
        loop: false,
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        paginationClickable: false,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 0,
        autoplayDisableOnInteraction: false,
        initialSlide: beginnum
    });
} 

function HideBigPic(divID) {
    $("#" + divID).hide();
}


// 判断图片加载的函数
var t_img; // 定时器
var isLoad = true; // 控制变量
function isImgLoad(callback,findID) {
    // 注意我的图片类名都是cover，因为我只需要处理cover。其它图片可以不管。
    // 查找所有封面图，迭代处理
    $(findID).each(function () {
        // 找到为0就将isLoad设为false，并退出each
        if (this.height === 0) {
            isLoad = false;
            return false;
        }
    });
    // 为true，没有发现为0的。加载完毕
    if (isLoad) {
        clearTimeout(t_img); // 清除定时器
        // 回调函数
        callback();
        // 为false，因为找到了没有加载完成的图，将调用定时器递归
    } else {
        isLoad = true;
        t_img = setTimeout(function () {
            isImgLoad(callback); // 递归扫描
        }, 500); // 我这里设置的是500毫秒就扫描一次，可以自己调整
    }
}


/*5.3.0 获取推荐配置接口，并调用数据加载或提交数据*/
var GetRecommendConfig = {
    api_target: "com_cmall_familyhas_api_ApiGetRecommendConfig",
    api_input: {},
    LoadData: function (bfd,dg,updg) {
        //GetMaybeLoveType.api_input.modelList = DgUserBehaviourAPI.modelList_g;

        var s_api_input = JSON.stringify(this.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": this.api_target };
        var purl = g_APIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: obj_data,
            dataType: g_APIResponseDataType
        });
        request.done(function (msg) {
            if (msg.resultCode == g_const_Success_Code) {
                if (msg.getConfig == "bfd_get") {
                    //使用百分点
                    if(typeof(bfd)=="function")
                        bfd();
                }
                else if (msg.getConfig == "dg_get") {
                    //使用达观
                    if (typeof (bfd) == "function")
                        dg();
                }
				else{
					//隐藏“猜你喜欢标题”
					localStorage["dg_up"] = "0";
					if(typeof($("#article_GuessYourLike"))=="object"){
						$("#article_GuessYourLike").hide()
					}
					if(typeof($("#div_space_GuessYourLike"))=="object"){
						$("#div_space_GuessYourLike").hide()
					}
					if(typeof($("#h_recBarName"))=="object"){
						$("#h_recBarName").hide()
					}
					if(typeof($("#h_recBarName"))=="object"){
						$("#h_recBarName").hide()
					}
					if(typeof($("#picScroll"))=="object"){
						$("#picScroll").hide()
					}
					
				}

                if (msg.upConfig.indexOf("dg_up") > -1) {
                    //上报达观数据收集
                    localStorage["dg_up"] = "1";
                    if (typeof (updg) == "function")
                        updg();
                }
                else {
                    localStorage["dg_up"] = "0";
                }
            }
            else {
                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            ShowMesaage(g_const_API_Message["7001"]);
        });
		
    },
};

//5.3.0 达观-用户行为数据上报接口
var DgUserBehaviourAPI = {
    actionNum: 1,
    dgRequestId:"",
    productCodes:"",
    /*
    用户行为;
    view:点击进入物品详情页;
    play:播放视频;
    rec_click:点击达观推荐结果;
    rec_show:用户浏览过达观推荐结果;
    collect:收藏(针对商品);
    comment:评论;
    share:分享;
    cart:加入购物车;
    buy:购买;
    search:搜索(输入框搜索);
    */
    userAction: "view",
    searchTxt:"",
    api_target: "com_cmall_familyhas_api_ApiDgUserBehaviour",
    modelList_g:[],
    modelList: { "actionNum": "", "dgRequestId": "", "productCodes": "", "userAction": "", "searchTxt": "" },
    api_input: { "cId": "", "modelList": [] },
    GetList: function (callback) {
        if (localStorage["dg_up"] != null && localStorage["dg_up"] == "1") {
            if (localStorage["dg_unique_id"] == null || localStorage["dg_unique_id"] == "") {
                localStorage["dg_unique_id"] = "dg-" + guid();
            }
            //唯一编号
            DgUserBehaviourAPI.api_input.cId = localStorage["dg_unique_id"];
            DgUserBehaviourAPI.api_input.modelList = DgUserBehaviourAPI.modelList_g;

            var s_api_input = JSON.stringify(this.api_input);
            var obj_data = { "api_input": s_api_input, "api_target": this.api_target };
            var purl = g_APIUTL;
            var request = $.ajax({
                url: purl,
                cache: false,
                method: g_APIMethod,
                data: obj_data,
                dataType: g_APIResponseDataType
            });

            request.done(function (msg) {
                if (msg.resultCode == g_const_Success_Code) {
                    localStorage["dg_dgRequestId"] = "";
                    if (typeof (callback) == "function") {
                        callback();
                    }
                }
                else {
                    ShowMesaage(msg.resultMessage);
                }
            });

            request.fail(function (jqXHR, textStatus) {
                ShowMesaage(g_const_API_Message["7001"]);
            });
        }
    },
    //
    SetList: function (actionNum, productCodes, userAction, searchTxt, dgRequestId) {
        DgUserBehaviourAPI.modelList.actionNum = actionNum;
        DgUserBehaviourAPI.modelList.productCodes = productCodes;
        DgUserBehaviourAPI.modelList.userAction = userAction;
        DgUserBehaviourAPI.modelList.searchTxt = searchTxt;
        DgUserBehaviourAPI.modelList.dgRequestId = dgRequestId;
        DgUserBehaviourAPI.modelList_g.push(DgUserBehaviourAPI.modelList);
    },
    //
    Clear: function () {
        DgUserBehaviourAPI.modelList_g=[];
    },
};

//用于生成uuid
function S4() {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
}
function guid() {
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}

//评论接口
var CommentAPI={
    //删除评论
    Delete:function(uid,succ_back,fail_back){
        g_type_api.api_input = {
            version: 1.0,
            uid:uid
        };
        g_type_api.api_target = "com_cmall_familyhas_api_ApiProductCommentDel";
        g_type_api.api_url = g_APIUTL;
        g_type_api.LoadData(succ_back,"", fail_back);
    }  

};