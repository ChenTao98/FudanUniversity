<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/19
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>The ArtStore</title>
    <%--引入样式表、js--%>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/navSearch.js"></script>
    <%--定义特殊样式--%>
    <style>
        .navLogo {
            color: white;
            font-size: 33px;
            text-shadow: 0 0 3px #fff, 0 0 6px #fff, 0 0 10px #fff, 0 0 12px #ff00de, 0 0 23px #ff00de,
            0 0 27px #ff00de, 0 0 33px #ff00de, 0 0 49px #ff00de;
        }

        .topNav {
            margin-top: 4em;
        }

        .divBox {
            width: 80%;
            margin: auto;
        }
    </style>
</head>
<c:choose>
<c:when test="${sessionScope.get('user')==null}">
<jsp:forward page="/Login.jsp"/>
<body>
</c:when>
<c:otherwise>
<body>
</c:otherwise>
</c:choose>
<%--引入导航栏--%>
<jsp:include page="WEB-INF/header.jsp"/>
<div class="divBox">
    <%--定义页面小导航--%>
    <ul class="nav nav-tabs topNav">
        <li class="active">
            <a>Profile</a>
        </li>
        <li><a href="Cart.jsp">Cart</a></li>
        <li><a href="Record.jsp">Record</a></li>
        <li><a href="Setting.jsp">Setting</a></li>
    </ul>
        <%--展示账户个人信息--%>
    <table class="table table-hover table-striped table-right">
        <tbody>
        <tr class="success">
            <td>
                Name
            </td>
            <td>
                ${sessionScope.get('user').getLastName()}
            </td>
        </tr>
        <tr class="error">
            <td>
                Address
            </td>
            <td>
                ${sessionScope.get('user').getAddress()}
            </td>
        </tr>
        <tr class="warning">
            <td>
                City
            </td>
            <td>
                ${sessionScope.get('user').getCity()}
            </td>
        </tr>
        <tr class="info">
            <td>
                Country
            </td>
            <td>
                ${sessionScope.get('user').getCountry()}
            </td>
        </tr>
        <tr class="success">
            <td>
                Postal
            </td>
            <td>
                ${sessionScope.get('user').getPostal()}
            </td>
        </tr>
        <tr class="error">
            <td>
                Phone
            </td>
            <td>
                ${sessionScope.get('user').getPhone()}
            </td>
        </tr>
        <tr class="warning">
            <td>
                Email
            </td>
            <td>
                ${sessionScope.get('user').getEmail()}
            </td>
        </tr>
        </tbody>
    </table>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
