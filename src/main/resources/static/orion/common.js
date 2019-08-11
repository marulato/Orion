var common = {};

common.loading = function (message) {
    $.blockUI({
        message:"<img src='/orion/images/loading.gif' />" + "&nbsp;" + message,
        css:{border:'1px solid #aaa'}
    });
}

common.toErrorPage = function() {
    location.href = "/orion/web/error";
}

common.loadingFinished = function () {
    $.unblockUI();
}

common.getBrowser = function () {
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Opera") > -1) {
        return "Opera";
    } else if(userAgent.indexOf("Edge") > -1) {
        return "Edge";
    } else if(userAgent.indexOf("Firefox") > -1) {
        return "Firefox";
    } else if(userAgent.indexOf(".NET") > -1) {
        return "IE";
    } else if(userAgent.indexOf("Chrome") > -1) {
        return "Chrome";
    } else if(userAgent.indexOf("360") > -1) {
        return "360";
    } else {
        return "Others";
    }


}

common.getToken = function () {
    var token = "";
    $.ajax({
        url:"/orion/web/Security/RequestValidation/token",
        type:"post",
        dataType:"text",
        async:false,
        success:function (data) {
            token = encodeURIComponent(data);
        },
        error:function () {
            token = "error_token";
        }
    });
    return token;
}

common.validate = function (form, url) {
    var response = null;
    $.ajax({
        url:url,
        type:"post",
        data:form,
        async:false,
        dataType:"json",
        success:function (data) {
            response = data;
        }
    });
    return response;
}

common.showErrorMsg = function (result, suffix) {
    common.dismissErrorMsg();
    var errors = result.errors;
    $.each(errors, function (idx, err) {
        var errMsg ="*" + err.errorDesc;
        $("div[id^='err_']").each(function (index, div) {
            var idStr = $(div).attr("id");
            console.log(idStr);
            var id = "#" + idStr;
            if (idStr.indexOf(err.errorCode) != -1 && idStr.indexOf(suffix) != -1) {
                if ($(id).hasClass("form-group")) {
                    $(id).removeClass("form-group");
                }
                if (!$(id).hasClass("form-group has-error")) {
                    $(id).addClass("form-group has-error");
                }
                $(id).after("<span id='err_span_" + idx + "' style='color: #cc3f44'>"+errMsg+"</span>");
            }
        });
    });
}

common.dismissErrorMsg = function () {
    $("div[id^='err_']").removeClass("form-group has-error");
    $("div[id^='err_']").addClass("form-group");
    $("span[id^='err_span_']").remove();
}

common.requestKey = function () {
    return "OrionWebRequests";
}

common.unescape = function (str) {
    return eval("'" + str + "'");
}
