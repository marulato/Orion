var jobScheduel = {};

$(function () {
    $("#batchjob_li").addClass("active");
    jobScheduel.search(1);
});

jobScheduel.initAdd = function() {
    common.dismissErrorMsg();
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
    $.ajax({
        url:"/orion/web/System/JobScheduel/search",
        type:"post",
        data:searchParam + "&page=" + page + "&pageSize=" + pageSize,
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            var result = data.result;
            if (data.responseCode == "200") {
                $(".odd").remove();
                $(".even").remove();
                var tableValue = "";
                if (result.length == 0) {
                    tableValue = "<tr><td colspan='7' style='text-align: center'>没有数据</td></tr>";
                }
                $.each(result, function (index, obj) {
                    var status = null;
                    var method = null;
                    var desc = obj.description;
                    var cron = obj.cron;
                    var jobGroup = obj.jobGroup;
                    var key = CryptoJS.enc.Utf8.parse("OrionWebRequests");
                    var param = CryptoJS.AES.encrypt(obj.jobName + "," + jobGroup, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}).toString();
                    param = encodeURIComponent(encodeURIComponent(param));
                    if (obj.isRegistered == "Y") {
                        status = "已注册";
                    }  else {
                        status = "未注册";
                    }
                    if (obj.isQuartz == "Y") {
                        method = "自动执行";
                    } else {
                        method = "手动执行";
                    }
                    if (obj.description == "" || obj.description == null) {
                        desc = "-";
                    }
                    if (obj.cron == "" || obj.cron == null) {
                        cron = "-";
                    }
                    if (index % 2 == 0) {
                        tableValue += "<tr class='odd'>";
                        tableValue += "<td>"+ obj.jobName +"</td>";
                        tableValue += "<td>"+ status +"</td>";
                        tableValue += "<td>"+ desc +"</td>";
                        tableValue += "<td>"+ method +"</td>";
                        tableValue += "<td>"+ cron +"</td>";
                        tableValue += "<td>"+ obj.className +"</td>";
                        tableValue += "<td> <button type=\"button\" class=\"btn btn-info\"><i class=\"fa fa-credit-card\" onclick=\"jobScheduel.doDisplay('"+param+"')\">&nbsp;查看</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-secondary\"><i class=\"fa fa-pencil\">&nbsp;修改</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-primary\"><i class=\"fa fa-rocket\">&nbsp;运行</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-danger\"><i class=\"fa fa-trash\" onclick=\"\">&nbsp;删除</i></button></td>";
                    } else {
                        tableValue += "<tr class='even'>";
                        tableValue += "<td>"+ obj.jobName +"</td>";
                        tableValue += "<td>"+ status +"</td>";
                        tableValue += "<td>"+ desc +"</td>";
                        tableValue += "<td>"+ method +"</td>";
                        tableValue += "<td>"+ cron +"</td>";
                        tableValue += "<td>"+ obj.className +"</td>";info
                        tableValue += "<td> <button type=\"button\" class=\"btn btn-info\"><i class=\"fa fa-credit-card\" onclick=\"jobScheduel.doDisplay('"+param+"')\">&nbsp;查看</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-secondary\"><i class=\"fa fa-pencil\">&nbsp;修改</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-primary\"><i class=\"fa fa-rocket\">&nbsp;运行</i></button>";
                        tableValue += "<button type=\"button\" class=\"btn btn-danger\"><i class=\"fa fa-trash\">&nbsp;删除</i></button></td>";
                    }
                });
                $("#jobBody").append(tableValue);
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
            console.log(msg);
        }
    });
}

jobScheduel.doAdd = function () {
    var content = $("#addForm").serialize();
    var result = common.validate(content + "&action=" + "Add", "/orion/web/System/JobScheduel/validate");
    if (result.code == "300") {
        $("#addBox").modal("hide");
        common.loading("正在处理您的请求，请稍后...");
        var token = common.getToken();
        $.ajax({
            url: "/orion/web/System/JobScheduel/create",
            type: "post",
            data: content + "&token=" + token,
            dataType: "json",
            success: function (data) {
                common.loadingFinished();
                if (data.code == "200") {
                    $("#alertBox").css("display", "block");
                    jobScheduel.search(1);
                } else {
                    common.toErrorPage();
                }
            }
        });
    } else {
        common.showErrorMsg(result);
    }
}

jobScheduel.doDisplay = function(param) {
    $("#auto1").attr("checked", false);
    $("#auto2").attr("checked", false);
    common.loading("正在处理您的请求，请稍后...");
    $.ajax({
        url:"/orion/web/System/JobScheduel/display/" + param,
        type:"get",
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            if (data.code == "200") {
                var obj = data.object;
                $("#nameSpan").html(obj.jobName);
                $("#groupSpan").html(obj.jobGroup);
                $("#fireSpan").html(obj.lastFireDate);
                $("#classSpan").html(obj.className);
                $("#descArea").val(obj.description);
                if (obj.automatic == "Y") {
                    $("#auto1").attr("checked", "checked");
                } else {
                    $("#auto2").attr("checked", "checked");
                }
                $("#displayBox").modal("show", {backdrop: 'static'});
            } else {
                common.toErrorPage();
            }
        },
        error:function (data) {
            common.loadingFinished();
            console.log(data);
        }
    });
}

jobScheduel.doDelete = function () {
    
}