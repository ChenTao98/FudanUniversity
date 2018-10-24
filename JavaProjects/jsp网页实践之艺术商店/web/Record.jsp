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
<%--判断是否登录，没有则去登录页面--%>
<c:choose>
<c:when test="${sessionScope.get('user')==null}">
<jsp:forward page="/Login.jsp"/>
<body>
</c:when>
<c:otherwise>
<c:choose>
<%--判断是否获取订单，没有则连接servlet--%>
<c:when test="${requestScope.get('RecordArtwork')==null}">
<jsp:forward page="/RecordServlet">
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
        <li><a href="Cart.jsp">Cart</a></li>
        <li class="active"><a>Record</a></li>
        <li><a href="Setting.jsp">Setting</a></li>
    </ul>
        <%--判断是否有完成订单--%>
    <c:choose>
        <c:when test="${requestScope.get('RecordArtwork').size()==0}">
            <h2>No Record</h2>
        </c:when>
        <%--展示所有订单--%>
        <c:otherwise>
            <table class="table table-hover table-striped table-right">
                <tbody>
                <tr>
                    <td>RecordId</td>
                    <td>DataCreate</td>
                    <td>Artwork Name</td>
                    <td>DataComplete</td>
                </tr>
                <c:forEach var="record" items="${requestScope.get('RecordArtwork')}" begin="0" step="1" varStatus="i">
                    <tr>
                        <td>${(i.index)+1}</td>
                        <td>${record.getDateCreated()}</td>
                        <td>${record.getArtworkName()}</td>
                        <td>${record.getDateComplete()}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
