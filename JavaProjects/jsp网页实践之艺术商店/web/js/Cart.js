/**
 * Created by Bing Chen on 2017/7/21.
 */
$(document).ready(function () {
    $("#CartBuyBtn").click(function () {
        //获取选择的商品
        var check = document.getElementsByName("buyArtworkCheckbox");
        var string = "";
        for (var i = 0; i < check.length; i++) {
            if (check[i].checked) {
                string += check[i].value;
            }
        }
        //判断是否有选择商品
        if (string === "") {
            //未选择商品则给出警告
            alert("请选择商品");
        } else {
            //选择商品，则购买，并给出提示
            $.post("./CartBuyServlet", // path
                $("#CartBuyForm").serialize(), //data
                function (data) { // callback function
                    data = JSON.parse(data); // get js object
                    if (data.result === "OK") {
                        alert("购买成功");
                        window.location.href = "./Cart.jsp"; //回到首页
                    }
                    else {
                        alert("购买成功");
                    }
                }
            );
        }
    });
});
