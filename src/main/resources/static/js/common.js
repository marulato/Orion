var common = {};

common.loading = function (message) {
    $.blockUI({
        message:"<img src='/star/images/loading.gif' />" + "&nbsp;" + message,
        css:{border:'1px solid #aaa'}
    });
}

common.loadingFinished = function () {
    $.unblockUI();
}
