$(function () {
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
});