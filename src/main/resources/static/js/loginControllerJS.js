var loginCtrl = {};

loginCtrl.doLogin = function () {
    common.loading("正在登陆, 请稍后...");
    $.ajax({
        url:"/star/web/doLogin",
        method:"post",
        data:$("#loginForm").serialize(),
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            if (data.code == "OK") {
                location.href = "/star/web/dashboard";
            } else if(data.code == "I") {
                $("#loginFailedMsg").html("账号未激活，请等待激活时间或联系管理员");
            } else if (data.code == "L") {
                $("#loginFailedMsg").html("账号已锁定");
            } else if (data.code == "E") {
                $("#loginFailedMsg").html("账号已过期");
            } else if (data.code == "B") {
                $("#loginFailedMsg").html("账号已冻结");
            } else if (data.code == "NONE") {
                $("#loginFailedMsg").html("账号或密码错误");
            }
        }, 
        error:function () {
            common.loadingFinished();
            $("#loginFailedMsg").html("系统故障, 请稍后再试");
        }
    });
}
