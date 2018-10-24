/**
 * Created by Bing Chen on 2017/7/20.
 */
$(document).ready(function () {
    //未登录时，跳到首页
    $("#button1").click(function () {
        window.location.href = "./Login.jsp";
    });
    //登录时，添加到购物车
    $("#button2").click(function () {
        $.post("./DetailAddServlet", // path
            $("#detailForm").serialize(), //data
            function (data) { // callback function
                data = JSON.parse(data); // get js object
                if (data.result === "OK") {
                    alert("加入购物车成功");
                }
                else {
                    alert("加入购物车失败");
                }
            }
        );
    });
});