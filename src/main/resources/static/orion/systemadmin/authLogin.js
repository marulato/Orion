var auth = {};

auth.doLogin = function () {
    var name = $("#username").val();
    var pwd = $("#password").val();
    if (name == '' || pwd == '') {
        $("#loginFailedMsg").html("请输入账号或密码");
        return;
    }
    var key = CryptoJS.enc.Utf8.parse("OrionWebPassword");
    var encrypted = CryptoJS.AES.encrypt(pwd, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}).toString();
    $("#password").val(encrypted);
    common.loading("正在登陆, 请稍后...");
    $("#browser").val(common.getBrowser());
    $.ajax({
        url: "/orion/web/AuthLogin/signin",
        method: "post",
        data: $("#loginForm").serialize(),
        dataType: "text",
        success: function (data) {
            common.loadingFinished();
            $("#password").val(pwd);
            if (data == 'changePwd') {
                location.href="/orion/web/AuthLogin/ChangePassword";
            } else if(data == '1') {
                location.href="/orion/web/Home";
            } else if(data == '2') {
                $("#loginFailedMsg").html("此账号已过期");
            } else if(data == '3') {
                $("#loginFailedMsg").html("此账号未激活，请等待激活日期或请联系管理员激活");
            } else if(data == '4') {
                $("#loginFailedMsg").html("此账号已被冻结");
            } else if(data == '-2') {
                $("#loginFailedMsg").html("密码错误次数过多，账号已被锁定");
            } else {
                $("#loginFailedMsg").html("账号或密码错误");
            }
        },
        error: function (data) {
            common.loadingFinished();
            console.log(data);
            $("#loginFailedMsg").html("系统故障, 请稍后再试");
        }
    });
}

auth.doChangePwd = function () {
    var pwd1 = $("#pwd1").val();
    var pwd2 = $("#pwd2").val();
    if (pwd1 == '' || pwd2 == '') {
        $("#loginFailedMsg").html("请输入新密码");
        return;
    }
    var key = CryptoJS.enc.Utf8.parse("OrionWebPassword");
    var enpwd1 = CryptoJS.AES.encrypt(pwd1, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}).toString();
    var enpwd2 = CryptoJS.AES.encrypt(pwd2, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}).toString();
    $("#pwd1").val(enpwd1);
    $("#pwd2").val(enpwd2);
    common.loading("Processing your request, please wait...");
    $.ajax({
        url: "/orion/web/AuthLogin/ChangePassword/confirm",
        method: "post",
        data: $("#loginForm").serialize(),
        dataType: "text",
        success: function (msg) {
            common.loadingFinished();
            if (msg == 'notsame') {
                $("#loginFailedMsg").html("密码不一致");
            }
            if (msg == 'invalid') {
                $("#loginFailedMsg").html("密码格式不正确");
            }
            if (msg == 'pass') {
                location.href = "/orion/web/Home";
            }
        },
        error: function () {
            common.loadingFinished();
        }
    });
}
