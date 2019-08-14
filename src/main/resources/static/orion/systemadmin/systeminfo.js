var sysinfo = {};

$(function () {
   $("#sysinfo_li").addClass("active");
   sysinfo.initSystemInfo();
});

sysinfo.initSystemInfo = function () {
   common.loading("正在载入，请稍后...");
   $.ajax({
      url:"/orion/web/System/Info/display",
      type:"get",
      dataType:"json",
      success:function (data) {
         common.loadingFinished();
         var map = data.object;
         var tableValue = "";
         $("#info_body").empty();
         $.each(map, function (key, value) {
            tableValue += "<tr id='infotr'>";
            tableValue += "<td>"+key+"</td>"
            tableValue += "<td>"+value+"</td>"
            tableValue += "</tr>";
         });
         $("#info_body").append(tableValue);
      }
   });
}

sysinfo.refreshMem = function () {
   sysinfo.initSystemInfo();
}