var sysConfig = {};

$(function () {
    $("#sysconfig_li").addClass("active");
    sysConfig.search(1);
});

sysConfig.search = function (page) {
    common.loading("正在加载，请稍后...");
    var pageSize = $("#pageSizeDropDown").val();
    var searchParam = $("#searchForm").serialize();
        $.ajax({
        url:'/orion/web/System/Configuration/search',
        method:'post',
        data:searchParam + "&page=" + page + "&pageSize=" + pageSize,
        dataType:'json',
        success:function (data) {
            common.loadingFinished();
            var result = data.result;
            if (data.responseCode == "200") {
                var tableValue = "";
                if (result.length == 0) {
                    tableValue = "<tr><td colspan='4' style='text-align: center'>没有数据</td></tr>";
                }
                $(".odd").remove();
                $(".even").remove();
                $.each(result, function (index, obj) {
                    if (index % 2 == 0) {
                        tableValue += "<tr class='odd'>";
                        tableValue += "<td>"+obj.description+"</td>";
                        tableValue += "<td>"+obj.configKey+"</td>";
                        tableValue += "<td>"+obj.configValue+"</td>";
                        tableValue += "<td><button type=\"button\" class=\"btn btn-secondary\" onclick=\"sysConfig.initModify('"+obj.configKey+"')\"><i class=\"fa-pencil\"></i></button></td>"
                        tableValue += "</tr>"
                    } else {
                        tableValue += "<tr class='even'>"
                        tableValue += "<td>"+obj.description+"</td>";
                        tableValue += "<td>"+obj.configKey+"</td>";
                        tableValue += "<td>"+obj.configValue+"</td>";
                        tableValue += "<td><button type=\"button\" class=\"btn btn-secondary\" onclick=\"sysConfig.initModify('"+obj.configKey+"')\"><i class=\"fa-pencil\"></i></button></td>"
                        tableValue += "</tr>";
                    }
                });
                $("#sysConfigbody").append(tableValue);
                $(".tcdPageCode").createPage({
                    pageCount:data.totalPages,
                    current:page,
                    backFn:function(p){
                        sysConfig.search(p, pageSize);
                    }
                });
            } else {
                location.href = "/orion/web/error";
            }
        },
        error:function (msg) {
            common.loadingFinished();
            console.log(msg);
        }
    });
}

sysConfig.initModify = function (key) {
    common.dismissErrorMsg();
    $("#alertBox").css("display", "none");
    $.ajax({
        url:'/orion/web/System/Configuration/modify',
        type:"post",
        data:{"configKey":key},
        success:function (data) {
            $("#descInput").val(data.description);
            $("#keyInput").val(data.configKey);
            $("#valueInput").val(data.configValue);
            $("#modifyBox").modal('show', {backdrop: 'static'});
        },
        error:function (msg) {

        }
    });
}

sysConfig.doModify = function () {
    var content = $("#modifyForm").serialize();
    var result = common.validate(content, "/orion/web/System/Configuration/validate");
    if (result.code == "300") {
        $("#modifyBox").modal('hide');
        common.loading("正在处理您的请求，请稍后...");
        var token = common.getToken();
        $.ajax({
            url:"/orion/web/System/Configuration/doModify",
            type:"post",
            data:content + "&token=" + token,
            dataType:"json",
            success:function (data) {
                common.loadingFinished();
                if (data.code == "200") {
                    $("#alertBox").css("display", "block");
                    sysConfig.search(1);
                } else {
                    location.href = "/orion/web/error";
                }
            }

        });
    } else {
        common.showErrorMsg(result, "m");
    }
}

sysConfig.changePageSize = function() {
    sysConfig.search(1);
}

sysConfig.initSearchParam = function () {
    $("#searchBox").modal('show', {backdrop: 'static'});
}

sysConfig.doSearchParam = function () {
    $("#searchBox").modal('hide');
    sysConfig.search(1);
}

sysConfig.resetSearch = function () {
    $("#condesc").val('');
    $("#conkey").val('');
    $("#condesc").val('');
}