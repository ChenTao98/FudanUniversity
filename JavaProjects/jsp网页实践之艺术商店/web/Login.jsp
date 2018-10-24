<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/18
  Time: 9:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>The ArtStore</title>
    <%--引入样式表、js--%>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/Login.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/md5.js" type="text/javascript"></script>
    <script src="js/Login.js"></script>
    <script src="js/navSearch.js"></script>
    <%--定义特殊样式--%>
    <style>
        .navLogo {
            color: white;
            font-size: 33px;
            text-shadow: 0 0 3px #fff, 0 0 6px #fff, 0 0 10px #fff, 0 0 12px #ff00de, 0 0 23px #ff00de,
            0 0 27px #ff00de, 0 0 33px #ff00de, 0 0 49px #ff00de;
        }

    </style>
</head>
<body>
<%--引入导航栏--%>
<jsp:include page="WEB-INF/header.jsp"/>
<%--定义登录页主体部分--%>
<div class="container">
    <form id="LoginForm" class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="username" class="sr-only">Email address</label>
        <input type="email" id="username" name="username" class="form-control" placeholder="Email address" required
               autofocus>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        <input type="hidden" id="hiddenPassword" name="hiddenPassword">
        <button class="btn btn-lg btn-primary btn-block signBtn" id="loginBtn" type="button">Sign in</button>
    </form>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
