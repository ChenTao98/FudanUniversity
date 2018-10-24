<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/19
  Time: 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>The Art Store</title>
    <%--引入样式表、js--%>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/Home.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/navSearch.js"></script>
    <script src="js/artistPage.js"></script>
    <%--定义特殊样式--%>
    <style>
        .column {
            text-align: center;
        }

        .buttonPage {
            width: 30px;
            height: 30px;
        }
    </style>
</head>
<%--判断是否有取得作家信息，没有则跳转servlet--%>
<c:choose>
<c:when test="${requestScope.get('artistAll')==null}">
<jsp:forward page="/ArtistServlet"/>
<body>
</c:when>
<c:otherwise>
<body>
</c:otherwise>
</c:choose>
<%--引入导航栏--%>
<jsp:include page="WEB-INF/header.jsp"/>
<h1 class="ArtistTitle">Artist</h1>
<hr>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column ">
            <div class="row clearfix">
                <%--循环展示作家--%>
                <c:forEach var="artist" items="${requestScope.get('artistAll')}" begin="0" end="11" step="1"
                           varStatus="i">
                    <div class="col-md-4 column imgDivBox">
                        <a id="artistToDetailLink${i.index}" title="View Detail"
                           href="http://localhost:8080/artistDetail?id=${artist.getArtistId()}">
                            <img id="artistImg${i.index}" class="img-circle"
                                 alt="No Artist Photos"
                                 src="art-images/artists/medium/${artist.getArtistId()}.jpg"/></a>
                        <h2 id="artistName${i.index}">
                                ${artist.getFirstName()} ${artist.getLastName()}
                        </h2>
                        <h4>Link <a id="artistLink${i.index}"
                                    href="${artist.getArtistLink()}">${artist.getArtistLink()}</a></h4>
                        <p>
                            <a id="SecondArtistToDetailLink${i.index}" class="btn"
                               href="http://localhost:8080/artistDetail?id=${artist.getArtistId()}">
                                <button class="btn btn-primary">View Details</button>
                            </a>
                        </p>
                    </div>
                </c:forEach>
            </div>
        </div>
        <%--判断总页数，定义页面切换按钮--%>
        <div class="row clearfix">
            <div class="col-md-12 column">
                <ul class="pagination pagination-lg">
                    <c:forEach begin="0" step="1" varStatus="i"
                               end="${(requestScope.get('artistAll').size()%12)==0?(requestScope.get('artistAll').size()/12-1):((requestScope.get('artistAll').size()/12))}">
                        <li>
                            <button class="buttonPage" id="artistChangePage${i.index}">${i.index+1}</button>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
