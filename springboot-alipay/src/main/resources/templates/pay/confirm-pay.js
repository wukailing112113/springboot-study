$(function () {
    init();
})
var orderPay = (function () {
    var subsId = common.getQueryStr("subsId");
    var clickPay = function () {
        var url = "/app/busi/subs/pay";
        var params = {};
        var reqHeader = {};
        var reqBody = {};
        reqBody.subsIds = [subsId];
        reqBody.openId = common.getSessionStorage("openId");
        if (App.isApp()) { //app

        } else {
            if (common.isWeixin()) { //微信
                $("#zfbpay").hide();
                $("#unionpay").hide();
                reqHeader.channel = 4;
                params.reqHeader = reqHeader;
                $("#ok-fry").click(function () {
                    reqBody.payType = $(".weixinPay .check").attr("payType");
                    params.reqBody = reqBody;
                    common.ajax("POST", url, params, showWxPayDialog, null, null,null,null,"toast");
                })
            } else {//h5
                $("#wxpay").hide();
                $("#unionpay").hide();//暂时没接入
                reqHeader.channel = 5;
                params.reqHeader = reqHeader;
                $("#ok-fry").click(function () {
                    reqBody.payType = $(".webPay .check").attr("payType");
                    params.reqBody = reqBody;
                    common.ajax("POST", url, params, function (data) {
                        showAlipayDialog(data);
                    },null,null,null,null,"toast");
                })
            }
        }
    }
    return {
        clickPay: clickPay
    }
})()
function showWxPayDialog(data) {//微信支付
    console.log(data);
    if (data && data.paymsg) {
        var msg = JSON.parse(data.paymsg);
       // console.log(msg.timeStamp)
        //document.getElementById("ok-fry").classList.remove("disabled");
        wx.chooseWXPay({
            "timestamp": msg["timeStamp"], // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
            "nonceStr": msg["nonceStr"], // 支付签名随机串，不长于 32 位
            "package": msg["package"], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
            "signType": msg["signType"], // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
            "paySign": msg["paySign"], // 支付签名
            success: function (res) {
                // 支付成功后的回调函数
                // alert(res);
                var typeId = common.getQueryStr("actType");
                var subsId = common.getQueryStr("subsId");
                document.getElementById("ok-fry").classList.remove("disabled");
                if(typeId == "13"){
                    window.location.href = "/pages/prod/activity/grpbuy/grpbuy-process.html?subsId="+subsId;
                }else if(typeId == "12") {
                    window.location.href = "/pages/busi/success-pay.html";
                }else {
                    window.location.href = "/pages/busi/success-pay.html";
                }
            },
            cancel: function () {
                document.getElementById("ok-fry").classList.remove("disabled");
            }
        });
    } else {
        document.getElementById("ok-fry").classList.remove("disabled");
    }
}
function showAlipayDialog(data) {//支付宝支付
    if (data && data.paymsg) {
        document.getElementById("ok-fry").classList.remove("disabled");
        $("#submitFormDiv").html(data.paymsg);
        $("#pay_form").submit();
    }
}
var init = function () {
    var subsId = common.getQueryStr("subsId");
    common.ajaxAllResp("GET","/app/busi/subs/order/confirm/page/" + subsId,null,function (data) {
        if (data.respBody.subs) {
            var subs = data.respBody.subs;
            var totalPrice = subs.totalPrice;
            if(subs.taxPrice){
                var actualTotalPrice = totalPrice + subs.freightPrice + subs.taxPrice;
            } else{
                var actualTotalPrice = totalPrice + subs.freightPrice;
            }
            $("#totalMoney").html("¥" + parseFloat(actualTotalPrice).toFixed(2));
            if (subs.status != 1) {
                $("#ok-fry").addClass("disabled");
                if (subs.status == 2 || subs.status == 3 || subs.status == 4 || subs.status == 5) {
                    $("#ok-fry").html('订单已经支付');
                } else if (subs.status == 6) {
                    $("#ok-fry").html('订单已关闭');
                }
            }
        }
    }) ;
    var urlDis = window.location.href;
    if(urlDis.indexOf("pages") > -1){
        var processId = "0";
        $("#deffBack").attr("href",'/pages/prod/activity/grpbuy/grpbuy-my.html?processId='+processId);
    }else{
        $("#deffBack").attr("href","/user/index.html");
    }
    $(".bot").click(function () {
        $(this).find(".btn-sel1").addClass("check").parent().siblings().find(".btn-sel1").removeClass("check");
    })
    orderPay.clickPay();
}
