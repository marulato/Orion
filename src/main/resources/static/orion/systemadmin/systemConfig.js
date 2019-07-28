var sysConfig = {};

$(function () {
    $("#sysconfig_li").addClass("active");
    sysConfig.search(1);
});

sysConfig.search = function (page) {
    common.loading("正在加载，请稍后...");
    $.ajax({
        url:'/orion/web/System/Configuration/search',
        method:'post',
        data:{"page":page},
        dataType:'json',
        success:function (data) {
            common.loadingFinished();
            var result = data.result;
            var tableValue = "";
            $(".odd").remove();
            $(".even").remove();
            $.each(result, function (index, obj) {
                if (index % 2 == 0) {
                    tableValue += "<tr class='odd'>";
                    tableValue += "<td>"+obj.description+"</td>";
                    tableValue += "<td>"+obj.configKey+"</td>";
                    tableValue += "<td>"+obj.configValue+"</td>";
                    tableValue += "<td><a href=\"javascript:void(0);\" class=\"btn btn-info btn-sm btn-icon icon-left\" onclick=\"sysConfig.initModify('"+obj.configKey+"')\">修改</a>"
                    tableValue += "<a href='javascript:void(0);' class='btn btn-danger btn-sm btn-icon icon-left'>删除</a></td>"
                    tableValue += "</tr>"
                } else {
                    tableValue += "<tr class='even'>"
                    tableValue += "<td>"+obj.description+"</td>";
                    tableValue += "<td>"+obj.configKey+"</td>";
                    tableValue += "<td>"+obj.configValue+"</td>";
                    tableValue += "<td><a href=\"javascript:void(0);\" class=\"btn btn-info btn-sm btn-icon icon-left\" onclick=\"sysConfig.initModify('"+obj.configKey+"')\">修改</a>"
                    tableValue += "<a href='javascript:void(0);' class='btn btn-danger btn-sm btn-icon icon-left'>删除</a></td>"
                    tableValue += "</tr>";
                }
            });
            $("#sysConfigbody").append(tableValue);
            $(".tcdPageCode").createPage({
                pageCount:data.totalPages,
                current:page,
                backFn:function(p){
                    sysConfig.search(p);
                }
            });
        },
        error:function (msg) {
            common.loadingFinished();
            console.log(msg);
        }
    });
}

sysConfig.initModify = function (key) {
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

sysConfig.showEntry = function () {
    $("#sysconfigTable").dataTable({
        "bPaginate": true,
        "bInfo": true,
        "searching": true,
        "bLengthChange":true,
        "ajax":{
            url:'/orion/web/System/Configuration/search',
            type:'get',
            dataType:'json',
            success:function (data) {

            }
        },
    });
}
