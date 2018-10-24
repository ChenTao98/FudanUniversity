$(document).ready(function(){
    $("#loginBtn").click(function(){
        //获取用户名与密码
        var username=document.getElementById("username").value;
        var pass=document.getElementById("password");
        var hiddenPassword=document.getElementById("hiddenPassword");
        var password=pass.value;
        //判断密码与用户名是否为空
        if(username===""||password===""){
            alert("用户名与密码不能为空")
        }else{
            // 加密密码
            hiddenPassword.value=hex_md5(password);
            pass.value="";
            //传送数据
            $.post("./LoginServlet", // path
                $("#LoginForm").serialize(), //data
                function (data) { // callback function
                    data = JSON.parse(data); // get js object
                    if (data.result === "OK") {
                        alert("登入成功");
                        window.location.href = "./"; //回到首页
                    }
                    else {
                        alert("用户名或密码错误");
                    }
                }
            );
        }
        // $.get, $.ajax
    });
});