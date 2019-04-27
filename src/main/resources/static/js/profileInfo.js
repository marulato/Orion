var profile = {};

profile.showProfile = function () {
    var id = $("#userId").val();
    location.href = "/star/web/user/profile/" + id ;
}

profile.modifyProfile = function () {
    $("#formArea").css('display', 'block');
    $("#dataArea").css('display', 'none');
    $("#saveBtn").show();
    $("#cancelBtn").show();

}
profile.initButton = function () {
    $("#saveBtn").hide();
    $("#cancelBtn").hide();
}

profile.save = function () {
    common.loading("正在保存, 请稍后...");
    $.ajax({
        url:"/star/web/user/profile/doModify",
        type:"post",
        data:$("#profileForm").serialize(),
        dataType:"json",
        success:function (data) {
            var errMsg = data.resultData;
            if (errMsg != null) {
                common.loadingFinished();
                $.each(errMsg, function(index, msg){
                    if ("displayName" == msg) {
                        $("#dn").css('border-color', '#FF0000');
                        $("#displayNameErr").html("昵称长度不能超过25个字符");
                    }
                });
            } else {
                common.loadingFinished();
                profile.showProfile();
            }
        },
        error:function () {
            common.loadingFinished();
            console.log("failed");
        }
    });
}

profile.cancel = function () {
    profile.showProfile();
}