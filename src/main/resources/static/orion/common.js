var common = {};

common.loading = function (message) {
    $.blockUI({
        message:"<img src='/orion/images/loading.gif' />" + "&nbsp;" + message,
        css:{border:'1px solid #aaa'}
    });
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
