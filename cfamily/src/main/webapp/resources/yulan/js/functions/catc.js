$(document).ready(function () {
    CheckUserTS.Check();
    

});

//长按弹层JS

var djsss;
var caid;
var showDivID;
var tempid
var caulid
var pressX
var pressY
var dqCaDivID=""


//5.3.0 增加达观推荐商品的支持
function AddMyCATC(ulid) {
    //绑定点击事件
    $("#" + ulid + " li").each(function (ii, nn) {
        var ttt = nn.id.split('_')
        var iid = ttt[1] +"_"+ ttt[2];
        $("#mylike_" + iid).click(function () {
            if ($("#div_" + iid).is(':hidden') == true) {
                //原有
                //PageUrlConfig.SetUrl();
                //window.location.href = g_const_PageURL.Product_Detail + "?pid=" + iid + "&t=" + Math.random();

                //2018-01-19 贾楠 增加小程序兼容
                
                if (ttt.length > 1) {
                    GoProductDetail(ttt[1], true, '', ttt[2]);
                }
                else {
                    GoProductDetail(ttt[1], true);
                }

            }
            return false;
        });
    });


    caulid = ulid;
    $(document).on("touchmove", function (e) {
        //隐藏全部收藏弹层
        HideAllTC();
    });
    //猜你喜欢中每个li增加长按监听
    $("#" + ulid + " li").each(function (i, n) {
        var obj = n;

        obj.addEventListener('touchstart', function (event) {
            var touch = event.targetTouches[0];
            // 获得手指位置
            pressX = touch.pageX;
            pressY = touch.pageY;

            //通过ID的命名规则判断，符合ca_操作类型_商品ID的，执行具体操作
            var pd = event.target.id.split('_');
            
            if (pd[0] == "ca" && pd.length == 4) {
                //浮层弹出后的操作
                chuli(pd[1], pd[2]);
            }
            else {
                //执行长按操作
                var caid = pd[1]+"_"+pd[2];

                if ($("#div_" + caid).is(':hidden')) {

                    ShowDivCA(caulid);

                    //已收藏过的直接提示
                    if ($("#collection_" + caid).attr("class") == "gr curr") {
                        ShowMesaage("您已经收藏过啦");
                    }
                    else {

                        djsss = setTimeout(function () {
                            ShowDivCA(caulid, caid);
                            //return false;
                        }, 1200);
                    }
                }
            }

        }, false);

        obj.addEventListener('touchmove', function (event) {
            //计算移动距离
            var touch = event.targetTouches[0];
            var spanX = touch.pageX - pressX;
            var spanY = touch.pageY - pressY;

            //移动距离小于5忽略
            //if (Math.abs(spanX) > 5 || Math.abs(spanY) > 5) {
                clearInterval(djsss);
                ShowDivCA(caulid);
            //隐藏全部收藏弹层
                $(".sc-bg").each(function (ii, nn) {
                    if (typeof (nn) != "undefined") {
                        //nn.hide();
                        nn.style.display = "none";
                    }
                });
            //}
        }, false);
        obj.addEventListener('touchend', function (event) {
            clearInterval(djsss);
            
        }, false);
        //安卓中禁用长按弹出菜单
        obj.addEventListener('contextmenu', function (e) {
            e.preventDefault();
        });        
    });
}

/*隐藏全部收藏弹层*/
function HideAllTC() {
    
    $(".sc-bg").each(function (ii, nn) {
        if (typeof (nn) != "undefined") {
            //nn.hide();
            nn.style.display = "none";
        }
    });
}
/**/
function ShowDivCA(ulid, id) {
    //用于确认是否触发了事件
    //$("#ssss").append("<li>"+id+"</li>")

    if (showDivID == undefined) {
        tempid = "";
    }
    else {
        tempid = showDivID;
    }
    //重新绑定点击事件
    $("#" + ulid + " li").each(function (ii, nn) {
        var iid = nn.id.split('_')[1];
        $("#mylike_" + iid).click(function () {
            if ($("#div_" + iid).is(':hidden') == true && tempid == "") {

                //原有
                //PageUrlConfig.SetUrl();
                //window.location.href = g_const_PageURL.Product_Detail + "?pid=" + iid + "&t=" + Math.random();

                //2018-01-19 贾楠 增加小程序兼容
                GoProductDetail(iid, true)
            }
            return false;
        });
    });

    //隐藏以显示的弹层
    if (showDivID != undefined && showDivID != "") {

        $("#div_" + showDivID).hide();


        //延后1秒清空遮蔽层，防止马上出发单击事件
        setTimeout(function () {
            tempid = "";

        }, 500);



    }
    //显示选中的弹层
    if (id) {			//判断商品是否被收藏
        //GetEventSkuInfoNew.GetInfo(id);

        ////直接显示收藏弹层
        showDivID = id;
        $("#div_" + showDivID).show();
        //屏蔽点击事件
        $("#mylike_" + showDivID).off("click");


    }


}
function chuli(type, id) {
    ShowDivCA(caulid);
    switch (type) {
        case 'search':
            //alert('找相似' + id);
            console.log('找相似' + id);
            break;
        case 'buxi':
            //alert('不喜欢' + id);
            console.log('不喜欢' + id);
            break;
        case 'collection':
            if (UserLogin.LoginStatus == g_const_YesOrNo.YES) {
                
                    CollectionD.Add(id, function () {
                        /*5.3.0 提交达观用户行为 开始*/
                        if (localStorage["dg_up"] != null && localStorage["dg_up"] != undefined && localStorage["dg_up"] == "1") {
                            if (UserLogin.LoginStatus == g_const_YesOrNo.YES) {
                                DgUserBehaviourAPI.Clear();
                                DgUserBehaviourAPI.SetList(1, id, "collect", "", "");
                                DgUserBehaviourAPI.GetList();
                            }
                        }
                        /*5.3.0 提交达观用户行为 结束*/
                    }, "");
            }
            else {
                //保存商品编号
                localStorage["jj_collectionid"] = id;

                if (window.location.href.indexOf("/Index.html") > -1) {
                    //首页
                    PageUrlConfig.SetUrl();
                    UserRELogin.loginCB(g_const_PageURL.Index, CollectionD.Add);
                    return ;
                }
                else if (window.location.href.indexOf("/cart.html") > -1) {
                    //购物车
                    PageUrlConfig.SetUrl();
                    UserRELogin.loginCB(g_const_PageURL.Cart, CollectionD.Add);
                    return ;
                }
                else if (window.location.href.indexOf("/Product_List.html") > -1) {
                    //搜索结果列表页
                    PageUrlConfig.SetUrl();
                    UserRELogin.loginCB(g_const_PageURL.Product_List, CollectionD.Add);
                    return ;
                }
                else {
                    //首页
                    PageUrlConfig.SetUrl();
                    UserRELogin.loginCB(g_const_PageURL.Index, CollectionD.Add);
                    return ;
                }
            }

            break;
            
    }
}

/*判断登录状态*/
var CheckUserTS = {
    //判断等登录状态，未登录长按时不能收藏
    Check: function () {
        var purl = g_INAPIUTL;
        var request = $.ajax({
            url: purl,
            cache: false,
            method: g_APIMethod,
            data: "t=" + Math.random() + "&action=checklogin",
            dataType: g_APIResponseDataType
        });
        request.done(function (msg) {
            //登录状态 0 未登录； 1 已登录
            if (msg.resultcode == g_const_Success_Code_IN) {
                UserLogin.LoginStatus = g_const_YesOrNo.YES;
                UserLogin.LoginName = msg.resultmessage;

                //登录后自动完成未完成的收藏
                if (typeof (localStorage["jj_collectionid"]) !=="undefined"&&localStorage["jj_collectionid"] !== "") {
                    var str_callback = "";//encodeURIComponent("Product_Detail.OperateCollection('" + operate + "')");
                    //添加收藏
                    CollectionD.Add(localStorage["jj_collectionid"], function () {
                        //$("#collection_" + localStorage["jj_collectionid"]).attr("class", "gr curr");
                        /*5.3.0 提交达观用户行为 开始*/
                        if (localStorage["dg_up"] != null && localStorage["dg_up"] != undefined && localStorage["dg_up"] == "1") {
                            if (UserLogin.LoginStatus == g_const_YesOrNo.YES) {
                                DgUserBehaviourAPI.Clear();
                                DgUserBehaviourAPI.SetList(1, localStorage["jj_collectionid"], "collect", "", "");
                                DgUserBehaviourAPI.GetList();
                            }
                        }
                        /*5.3.0 提交达观用户行为 结束*/
                    }, str_callback);

                    localStorage["jj_collectionid"] = "";

                    //隐藏弹层
                    
                    if (window.location.href.indexOf("/Index.html") > -1) {
                        //首页
                        dqCaDivID = "ichsy_jyh_scroller";
                    }
                    else if (window.location.href.indexOf("/cart.html") > -1) {
                        //购物车
                        dqCaDivID = "ichsy_jyh_scroller";
                    }
                    else if (window.location.href.indexOf("/Product_List.html") > -1) {
                        //搜索结果列表页
                        if ($("#divResultList").is(':hidden') == true) {
                            dqCaDivID = "divResultRecom";
                        }
                        else {
                            dqCaDivID = "divResultList";
                        }
                    }
                    else {
                        //首页
                        dqCaDivID = "ichsy_jyh_scroller";
                    }
                    ShowDivCA(dqCaDivID);
                }
            }
            else {
                UserLogin.LoginStatus = g_const_YesOrNo.NO;
            }

        });
        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
}

/*收藏*/
var CollectionD = {
    api_target: "com_cmall_familyhas_api_APiCollectionProduct",
    api_input: { "operateType": "1", "productCode": [] },
    /*增加收藏*/
    Add: function (pidlist, callback, str_callback) {
        CollectionD.api_input.operateType = "1";
        CollectionD.api_input.productCode = pidlist;
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

                //已收藏
                //$("#collection_" + pidlist).attr("class", "gr curr");

                ShowMesaage(g_const_API_Message["100003"]);

            }
            else {
                if (msg.resultCode == "969905919") {
                    //保存回跳页面
                    localStorage[g_const_localStorage.BackURL] = g_const_PageURL.Index + "?pid=" + pidlist;
                    PageUrlConfig.SetUrl(localStorage[g_const_localStorage.BackURL]);
                    if (str_callback != "") {
                        Message.ShowToPage("", g_const_PageURL.Login + "?t=" + Math.random(), 500, str_callback);
                    }
                    else {
                        window.location.replace(g_const_PageURL.Login + "?t=" + Math.random());
                    }
                    return;
                }

                //显示接口返回提示语
                ShowMesaage(msg.resultMessage);
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
        
    },
    /*取消收藏*/
    Delete: function (pidlist, callback, str_callback) {
        CollectionD.api_input.operateType = "0";
        CollectionD.api_input.productCode = pidlist;
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
            else {
                if (msg.resultcode == g_const_Error_Code.UnLogin) {
                    //保存回跳页面
                    localStorage[g_const_localStorage.BackURL] = g_const_PageURL.Product_Detail + "?pid=" + Product_Detail.api_input.productCode;
                    PageUrlConfig.SetUrl(localStorage[g_const_localStorage.BackURL]);
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

/*获取商品详情，判断是否已被收藏【作废啦，不需要提前知道是否已收藏】*/
var GetEventSkuInfoNew = {
    api_target: "com_cmall_familyhas_api_ApiGetEventSkuInfoNew",
    /*5.1.8 支持内购，增加isPurchase*/
    api_input: { "productCode": "", "buyerType": "4497469400050002", "version": 1, "channelId": "", "isPurchase": UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0 },
    /*增加收藏*/
    GetInfo: function (productCode) {
        GetEventSkuInfoNew.api_input.productCode = productCode;
        GetEventSkuInfoNew.api_input.channelId = g_const_ChannelID;
        /*输入参数 5.1.8 支持内购，增加isPurchase */
        GetEventSkuInfoNew.api_input.isPurchase = UserLogin.LoginStatus === g_const_YesOrNo.YES ? 1 : 0;
        var s_api_input = JSON.stringify(this.api_input);
        var obj_data = { "api_input": s_api_input, "api_target": GetEventSkuInfoNew.api_target, "api_token": "1" };
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
                if (msg.collectionProduct == g_const_collectionProduct.NO) {
                    //未收藏
                    $("#collection_" + productCode).attr("class", "gr");

                    //显示收藏弹层
                    showDivID = id;
                    $("#div_" + showDivID).show();

                }
                else {
                    //已收藏
                    $("#collection_" + productCode).attr("class", "gr curr");
                    ShowMesaage("您已经收藏过啦");
                }
            }
            else {
                if (msg.resultCode == "969905919") {
                    //保存商品编号
                    localStorage["jj_collectionid"] = id;
                    UserRELogin.loginCB(g_const_PageURL.Index, CollectionD.Add)
                    return;
                }
            }
        });

        request.fail(function (jqXHR, textStatus) {
            //ShowMesaage(g_const_API_Message["7001"]);
        });
    },
};


