/**
 * Created by Bing Chen on 2017/7/22.
 */
$(document).ready(function () {
    $("#SearchButton").click(function () {
        //获取并判断是否选择排序方式
        var obj = document.getElementsByName("orderType");
        var typeString = "";
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                typeString += obj[i].value
            }
        }
        if (typeString === "") {
            alert("请选择排序方式");
        } else {
            //如果选择了排序方式，提交表单
            document.getElementById("SearchForm").submit();
        }
    });
});