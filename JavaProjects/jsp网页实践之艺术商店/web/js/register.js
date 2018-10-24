$(document).ready(function () {
    //判断邮箱是否符合格式，给出提示
    var emailElement = document.getElementById('username');
    var emailHelp = document.getElementById('emailHelp');
    emailElement.addEventListener('input', function (event) {
        var email = emailElement.value;
        if (email.match(/^[a-z0-9]+[\.\_]{0,1}[a-z0-9]+@([a-z0-9]+\.)+[a-z]{2,}$/i)) {
            emailHelp.innerHTML = "";
        } else {
            emailHelp.innerHTML = '错误的邮箱格式，正确的邮箱格式为例如 12@163.com 或者 1630201000@fudan.edu.cn等';
        }
    });
    //判断用户名是否符合格式
    var lastName = document.getElementById('lastName');
    var lastNameHelp = document.getElementById('lastNameHelp');
    lastName.addEventListener('input', function (event) {
        var last = lastName.value;
        var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
        if (!reg.test(last)) {
            lastNameHelp.innerHTML = "";
        } else {
            lastNameHelp.innerHTML = '错误的用户名，用户名只能为英文';
        }
    });
    //判断密码是否符合格式
    var password = document.getElementById('password');
    var passwordHelp = document.getElementById('passwordHelp');
    password.addEventListener('input', function (event) {
        var pass = password.value;
        if (pass.length < 6) {
            passwordHelp.innerHTML = '密码至少还有6个字符，必须且只能包含数字与字母'
        } else if (pass.match(/^[a-z]{6,}$/i)) {
            passwordHelp.innerHTML = '密码至少还有6个字符，必须且只能包含数字与字母'
        } else if (pass.match(/^[0-9]{6,}$/i)) {
            passwordHelp.innerHTML = '密码至少还有6个字符，必须且只能包含数字与字母'
        } else if (!pass.match(/^[a-z0-9]{6,}$/i)) {
            passwordHelp.innerHTML = '密码至少还有6个字符，必须且只能包含数字与字母'
        } else {
            passwordHelp.innerHTML = ""
        }
    });
    //判断第二次密码输入是否相同
    var passwordAgain = document.getElementById("passwordAgain");
    var passwordAgainHelp = document.getElementById("passwordAgainHelp");
    passwordAgain.addEventListener('input', function () {
        var pass = password.value;
        var passAgain = passwordAgain.value;
        if (pass === passAgain) {
            passwordAgainHelp.innerHTML = "";
        } else {
            passwordAgainHelp.innerHTML = '两次输入密码不同'
        }
    });
    //对输入信息进一步判断
    $("#registerBtn").click(function () {
        //发送 post 请求
        var username = document.getElementById("username").value;
        var lastName = document.getElementById("lastName").value;
        var password = document.getElementById("password").value;
        var passwordAgain = document.getElementById("passwordAgain").value;
        if (username === "" || password === "" || lastName === "" || passwordAgain === "") {
            alert("请补全注册信息");
        } else if ((emailHelp.innerHTML !== "") || (lastNameHelp.innerHTML !== "") || (passwordHelp.innerHTML !== "") || (passwordAgainHelp.innerHTML !== "")) {
            alert("输入信息有误，请重新输入");
        } else if ((password) !== (passwordAgain)) {
            alert("输入信息有误，请重新输入");
        } else {
            //加密密码，并传送数据
            var hiddenPassword=document.getElementById("hiddenPassword");
            hiddenPassword.value=hex_md5(password);
            document.getElementById("password").value="";
            document.getElementById("passwordAgain").value="";
            $.post("./RegisterServlet", // path
                $("#registerForm").serialize(), //data
                function (data) { // callback function
                    data = JSON.parse(data); // get js object
                    if (data.result === "OK") {
                        alert("注册成功");
                        window.location.href = "./"; //回到首页
                    } else {
                        alert("注册失败，该用户名或该邮箱已存在");
                    }
                }
            );
        }
        // $.get, $.ajax
    });
});