var jobSchedule = {};

$(function () {
    $("#batchjob_li").addClass("active");
    jobSchedule.search(1);
});

jobSchedule.initAdd = function() {
    common.dismissErrorMsg();
    $("#addBox").modal('show', {backdrop: 'static'});
}

jobSchedule.showCronInput = function () {
    $("#cronDiv").css("display", "block");
}

jobSchedule.hideCronInput = function () {
    $("#cronDiv").css("display", "none");
}

jobSchedule.showModifyCronInput = function () {
    $("#modifyCron").css("display", "block");
}

jobSchedule.hideModifyCronInput = function () {
    $("#modifyCron").css("display", "none");
}

jobSchedule.initSearch = function() {
    $("#searchBox").modal('show', {backdrop: 'static'});
}

jobSchedule.search = function (page) {
    common.loading("正在加载，请稍后...");
    var pageSize = $("#pageSizeDropDown").val();
    var searchParam = $("#searchForm").serialize();
    $.ajax({
        url:"/orion/web/System/JobSchedule/search",
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
                    tableValue = "<tr class='odd'><td colspan='7' style='text-align: center'>没有数据</td></tr>";
                }
                $.each(result, function (index, obj) {
                    var status = null;
                    var method = null;
                    var desc = obj.description;
                    var cron = obj.cron;
                    var jobGroup = obj.jobGroup;
                    var key = CryptoJS.enc.Utf8.parse(common.requestKey());
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
                        tableValue += "<td> <button type=\"button\" title=\"查看\" class=\"btn btn-info\" onclick=\"jobSchedule.doDisplay('"+ param +"', false)\"><i class=\"fa fa-credit-card\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"修改\" class=\"btn btn-secondary\" onclick=\"jobSchedule.initModify('"+ param +"')\"><i class=\"fa fa-pencil\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"执行\" class=\"btn btn-orange\" onclick=\"jobSchedule.launch('"+ param +"')\"><i class=\"fa fa-rocket\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"配置参数\" class=\"btn btn-blue\" onclick=\"jobSchedule.initAddParam()\"><i class=\"fa fa-gear\"></i></button>"
                        tableValue += "<button type=\"button\" title=\"删除\" class=\"btn btn-danger\" onclick=\"jobSchedule.initDelete('"+ param +"', true)\"><i class=\"fa fa-trash\"></i></button></td>";
                    } else {
                        tableValue += "<tr class='even'>";
                        tableValue += "<td>"+ obj.jobName +"</td>";
                        tableValue += "<td>"+ status +"</td>";
                        tableValue += "<td>"+ desc +"</td>";
                        tableValue += "<td>"+ method +"</td>";
                        tableValue += "<td>"+ cron +"</td>";
                        tableValue += "<td>"+ obj.className +"</td>"
                        tableValue += "<td> <button type=\"button\" title=\"查看\" class=\"btn btn-info\" onclick=\"jobSchedule.doDisplay('"+ param +"', false)\"><i class=\"fa fa-credit-card\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"修改\" title=\"查看\" class=\"btn btn-secondary\" onclick=\"jobSchedule.initModify('"+ param +"')\"><i class=\"fa fa-pencil\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"执行\" class=\"btn btn-orange\" onclick=\"jobSchedule.launch('"+ param +"')\"><i class=\"fa fa-rocket\"></i></button>";
                        tableValue += "<button type=\"button\" title=\"配置参数\" class=\"btn btn-blue\" onclick=\"jobSchedule.initAddParam()\"><i class=\"fa fa-gear\"></i></button>"
                        tableValue += "<button type=\"button\" title=\"删除\" class=\"btn btn-danger\" onclick=\"jobSchedule.initDelete('"+ param +"', true)\"><i class=\"fa fa-trash\"></i></button></td>";
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

jobSchedule.doAdd = function () {
    var content = $("#addForm").serialize();
    var result = common.validate(content + "&action=" + "Add", "/orion/web/System/JobSchedule/validate");
    if (result.code == "300") {
        $("#addBox").modal("hide");
        common.loading("正在处理您的请求，请稍后...");
        var token = common.getToken();
        $.ajax({
            url: "/orion/web/System/JobSchedule/create",
            type: "post",
            data: content + "&token=" + token,
            dataType: "json",
            success: function (data) {
                common.loadingFinished();
                if (data.code == "200") {
                    $("#alertBox").css("display", "block");
                    window.setTimeout(jobSchedule.hideSuccessAlert, 2500);
                    jobSchedule.search(1);
                } else {
                    common.toErrorPage();
                }
            }
        });
    } else {
        common.showErrorMsg(result, "a");
    }
}

jobSchedule.doDisplay = function(param, isDelete) {
    common.loading("正在处理您的请求，请稍后...");
    var url = null;
    if (isDelete) {
        url = "/orion/web/System/JobSchedule/delete/" + param;
    } else {
        url = "/orion/web/System/JobSchedule/display/" + param;
    }
    $.ajax({
        url:url,
        type:"get",
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            if (data.code == "200") {
                var obj = data.object;
                var cron = obj.cron;
                var fireDate = obj.lastFireDate;
                if (cron == null || cron == "") {
                    cron = "-";
                }
                if (fireDate == null || cron == "") {
                    fireDate = "-";
                }
                if (isDelete) {
                    $("#displayTitle").html("确认删除");
                    $("#confirmDeleteBtn").css("display","inline");
                } else {
                    $("#displayTitle").html("详细信息");
                    $("#confirmDeleteBtn").css("display","none");
                }
                $("#nameSpan").html(common.unescape(obj.jobName));
                $("#groupSpan").html(common.unescape(obj.jobGroup));
                $("#fireSpan").html(fireDate);
                $("#classSpan").html(obj.className);
                $("#descArea").val(common.unescape(obj.jobDesc));
                $("#cronSpan").html(cron);
                $("#createdAtSpan").html(obj.createdAtStr);
                $("#createdBySpan").html(obj.createdBy);
                $("#updatedAtSpan").html(obj.updatedAtStr);
                $("#updatedBySpan").html(obj.updatedBy);
                if (obj.automatic == "Y") {
                    $("#autoSpan").html("自动托管");
                } else {
                    $("#autoSpan").html("手动托管");
                }
                if (obj.register == "Y") {
                    $("#regSpan").html("已注册");
                } else {
                    $("#regSpan").html("未注册");
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

jobSchedule.initModify = function(param) {
    common.loading("正在处理您的请求，请稍后...");
    common.dismissErrorMsg();
    $.ajax({
        url:"/orion/web/System/JobSchedule/modify/" + param,
        type:"get",
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            var obj = data.object;
            if (data.code == "200") {
                $("#modifyName").val(common.unescape(obj.jobName));
                $("#modifyGroup").val(common.unescape(obj.jobGroup));
                $("#modifyClass").val(obj.className);
                $("#modifyCron").val(obj.cron);
                $("#modifyDesc").val(common.unescape(obj.jobDesc));
                $("#modifyJobId").val(param);
            } else {
                common.toErrorPage();
            }
        },
        error:function () {
            common.loadingFinished();
        }
    });
    $("#modifyBox").modal('show', {backdrop: 'static'});
}

jobSchedule.doModify = function () {
    var content = $("#modifyForm").serialize();
    var result = common.validate(content + "&action=" + "Modify", "/orion/web/System/JobSchedule/validate");
    if (result.code == "300") {
        $("#modifyBox").modal('hide');
        common.loading("正在处理您的请求，请稍后...");
        var token = common.getToken();
        $.ajax({
            url:"/orion/web/System/JobSchedule/doModify",
            type:"post",
            data:content + "&token=" + token,
            dataType:"json",
            success:function (data) {
                common.loadingFinished();
                if (data.code == "200") {
                    jobSchedule.search(1);
                    $("#alertBox").css("display", "block");
                    window.setTimeout(jobSchedule.hideSuccessAlert, 2500);
                } else {
                    common.toErrorPage();
                }
            },
            error:function () {
                common.loadingFinished();
            }
        });
    } else {
        common.showErrorMsg(result, "m");
    }
}

jobSchedule.initDelete = function (param) {
    jobSchedule.doDisplay(param, true);
}

jobSchedule.doDelete = function () {
    $("#displayBox").modal("hide");
    common.loading("正在处理您的请求，请稍后...");
    var token = common.getToken();
    $.ajax({
        url: "/orion/web/System/JobSchedule/doDelete",
        data: {"token":token},
        type:"post",
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            if (data.code == "200") {
                $("#alertBox").css("display", "block");
                window.setTimeout(jobSchedule.hideSuccessAlert, 2500);
                jobSchedule.search(1);
            } else {
                common.toErrorPage();
            }
        },
        error:function () {
            common.loadingFinished();
        }
    });

}

jobSchedule.launch = function (param) {
    common.loading("正在执行目标任务，请稍后...");
    var token = common.getToken();
    $.ajax({
        url:"/orion/web/System/JobSchedule/launch/" + param,
        data:{"token":token},
        type:"get",
        dataType:"json",
        success:function (data) {
            common.loadingFinished();
            if (data.code == "200") {
                $("#jobAlert").css("display", "block");
                var t1 = window.setTimeout(jobSchedule.hideJobAlert, 2500);
            } else if (data.code == "201") {
                $("#jobAlert").css("display", "block");
                var t1 = window.setTimeout(jobSchedule.hideJobAlert, 2500);
            } else {
                common.toErrorPage();
            }
        }
    });
}

jobSchedule.hideJobAlert = function () {
    $("#jobAlert").css("display", "none");
}

jobSchedule.hideSuccessAlert = function () {
    $("#alertBox").css("display", "none");
}

jobSchedule.doSearchParam = function () {
    $("#searchBox").modal("hide");
    jobSchedule.search(1);
}

jobSchedule.initAddParam = function () {
    $("#paramBox").modal('show', {backdrop: 'static'});
}

jobSchedule.addParamRow = function () {
    var rowNum = $("div[id^='paramRow']").length;
    var sequence = rowNum + 1;
    var rowId= "paramRow" + sequence;
    var paramName = "params[" + rowNum + "]." + "paramName" ;
    var paramValue = "params[" + rowNum + "]." + "paramValue" ;
    var str = "<div class=\"row\" id=\""+rowId+"\"> <div class=\"col-md-6\"><div class=\"form-group\"><label class=\"control-label\">参数名</label><input type=\"text\" class=\"form-control\" name=\""+paramName+"\"></div></div><div class=\"col-md-6\"><div class=\"form-group\"><label class=\"control-label\">值</label><span style='float: right'><a href='javascript:void(0);' onclick=\"jobSchedule.removeParamRow('"+rowId+"')\">x</a></span><input type=\"text\" class=\"form-control\" name=\""+paramValue+"\"></div></div></div>"
    if (rowNum <= 0) {
        $("#paramBody").append(str);
    } else {
        $("#paramRow" + rowNum).after(str);
    }
}

jobSchedule.removeParamRow = function (rowId) {
    var id = "#" + rowId;
    $(id).remove();
    $("div[id^='paramRow']").each(function (index, div) {
        var idStr = $(div).attr("id");
        var seq = index + 1;
        var newId = "paramRow" + seq;
        var paramName = "params[" + index + "]." + "paramName" ;
        var paramValue = "params[" + index + "]." + "paramValue" ;
        $("#" + idStr).find("a").attr("onclick", "jobSchedule.removeParamRow('"+newId+"')");
        $("#" + idStr).find("input[name$='paramName']").attr("name", paramName);
        $("#" + idStr).find("input[name$='paramValue']").attr("name", paramValue);
        $("#" + idStr).attr("id", newId);
    });
}