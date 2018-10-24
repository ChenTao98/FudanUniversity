/**
 * Created by Bing Chen on 2017/7/20.
 */
$(document).ready(function () {
    $("#btnSetting").click(function () {
        //获取输入信息
        var last = document.getElementById("nameSetting").value;
        var address = document.getElementById("addressSetting").value;
        var city = document.getElementById("citySetting").value;
        var country = document.getElementById("countrySetting").value;
        var postal = document.getElementById("postalSetting").value;
        var phone = document.getElementById("phoneSetting").value;
        var email = document.getElementById("emailSetting").value;
        var string = last + address + city + country + postal + phone + email;
        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        //判断是否符合要求
        if (reg.test(string)) {
            alert("输入信息不能含有中文");
        } else if (last===""||email==="") {
            alert("邮箱或者用户名不能为空")
        } else {
            $.post("./SettingServlet", // path
                $("#settingForm").serialize(), //data
                function (data) { // callback function
                    data = JSON.parse(data); // get js object
                    if (data.result === "OK") {
                        alert("修改成功")
                        window.location.href = "./Profile.jsp"; //回到首页
                    }
                    else {
                        alert("修改失败，该用户名或者邮箱已存在");
                    }
                }
            );
        }
    });
});