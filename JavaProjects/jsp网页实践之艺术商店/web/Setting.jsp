<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/19
  Time: 19:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>The Artstore</title>
    <%--引入样式表、js--%>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/setting.js"></script>
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
        <li>
            <a href="Profile.jsp">Profile</a>
        </li>
        <li><a href="Cart.jsp">Cart</a></li>
        <li><a href="Record.jsp">Record</a></li>
        <li class="active"><a>Setting</a></li>
    </ul>
        <%--定义表单，展示已有信息--%>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <form role="form" id="settingForm">
                    <input type="hidden" id="customerId" name="customerId" value="${sessionScope.get('user').getCustomerId()}">
                    <div class="form-group">
                        <label for="nameSetting">Name</label>
                        <input class="form-control" id="nameSetting" name="nameSetting"
                               value="${sessionScope.get('user').getLastName()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="addressSetting">Address</label>
                        <input class="form-control" id="addressSetting" name="addressSetting"
                               value=" ${sessionScope.get('user').getAddress()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="citySetting">City</label>
                        <input class="form-control" id="citySetting" name="citySetting"
                               value="${sessionScope.get('user').getCity()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="countrySetting">Country</label>
                        <input class="form-control" id="countrySetting" name="countrySetting"
                               value="${sessionScope.get('user').getCountry()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="postalSetting">Postal</label>
                        <input class="form-control" id="postalSetting" name="postalSetting"
                               value=" ${sessionScope.get('user').getPostal()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="phoneSetting">Phone</label>
                        <input class="form-control" id="phoneSetting" name="phoneSetting"
                               value="${sessionScope.get('user').getPhone()}" type="text"/>
                    </div>
                    <div class="form-group">
                        <label for="emailSetting">Email</label>
                        <input class="form-control" id="emailSetting" name="emailSetting"
                               value="${sessionScope.get('user').getEmail()}" type="email"/>
                    </div>
                    <button class="btn btn-default" id="btnSetting" type="button">Modify</button>
                </form>
            </div>
        </div>
    </div>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
