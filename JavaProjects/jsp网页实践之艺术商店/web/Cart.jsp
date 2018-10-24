<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/19
  Time: 19:51
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
    <script src="js/Cart.js"></script>
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
<%--判断是否登录，没有跳转至登录页面--%>
<c:choose>
<c:when test="${sessionScope.get('user')==null}">
<jsp:forward page="/Login.jsp"/>
<body>
</c:when>
<c:otherwise>
<c:choose>
<%--判断是否获得购物信息，没有连接servlet获取--%>
<c:when test="${requestScope.get('CartArtwork')==null}">
<jsp:forward page="/CartGetServlet">
    <jsp:param name="customerId" value="${sessionScope.get('user').getCustomerId()}"/>
</jsp:forward>
<body>
</c:when>
<c:otherwise>
<body>
</c:otherwise>
</c:choose>
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
        <li class="active"><a>Cart</a></li>
        <li><a href="Record.jsp">Record</a></li>
        <li><a href="Setting.jsp">Setting</a></li>
    </ul>
        <%--判断购物车是否为空--%>
    <c:choose>
        <c:when test="${requestScope.get('CartArtwork').size()==0}">
            <%--为空则显示无订单--%>
            <h2>No Order</h2>
        </c:when>
        <c:otherwise>
            <%--不为空则展示订单--%>
            <form id="CartBuyForm">
                <input type="hidden" name="customerID" value="${sessionScope.get('user').getCustomerId()}">
            <table class="table table-hover table-striped table-right">
                <tbody>
                    <tr>
                        <td>Select</td>
                        <td>DataCreate</td>
                        <td>Artwork Name</td>
                        <td>Delete</td>
                    </tr>
                    <c:forEach var="order" items="${requestScope.get('CartArtwork')}" begin="0" step="1">
                        <tr>
                            <td><input type="checkbox" name="buyArtworkCheckbox" value="${order.getArtworkId()}"></td>
                            <td>${order.getDateCreated()}</td>
                            <td>
                                <a href="http://localhost:8080/detail?id=${order.getArtworkId()}">${order.getArtworkName()}</a>
                            </td>
                            <td>
                                <a href="http://localhost:8080/CartDeleteServlet?customerID=${sessionScope.get('user').getCustomerId()}&orderId=${order.getOrderId()}">
                                    <button type="button">Delete</button>
                                </a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
                <button class="btn-primary btn-block btn" id="CartBuyBtn" type="button">Buy</button>
            </form>
        </c:otherwise>
    </c:choose>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
