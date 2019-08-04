var jobScheduel = {};

$(function () {
    $("#batchjob_li").addClass("active");
});

jobScheduel.initAdd = function() {
    $("#addBox").modal('show', {backdrop: 'static'});
}

jobScheduel.showCronInput = function () {
    $("#cronDiv").css("display", "block");
}

jobScheduel.hideCronInput = function () {
    $("#cronDiv").css("display", "none");
}

jobScheduel.initSearch = function() {
    $("#searchBox").modal('show', {backdrop: 'static'});
}

jobScheduel.search = function (page) {
    common.loading("正在加载，请稍后...");
    var pageSize = $("#pageSizeDropDown").val();
    var searchParam = $("#searchForm").serialize();
}