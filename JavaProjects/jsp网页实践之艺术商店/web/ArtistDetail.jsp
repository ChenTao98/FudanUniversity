<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/19
  Time: 13:47
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
        h1 {
            margin-top: 2em;
        }

        .artworkBox {
            height: 100px;
            overflow: auto;
        }
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
<%--展示作家具体信息--%>
<div class="container">
    <div class="page-header">
        <h1>
            ${requestScope.get("artist").get(0).getFirstName()} ${requestScope.get("artist").get(0).getLastName()}
        </h1>
    </div>
    <div class="row clearfix">
        <div class="col-md-8 column DetailImage">
            <img class="img-thumbnail"
                 src="art-images/artists/medium/${requestScope.get("artist").get(0).getArtistId()}.jpg"/>
        </div>
        <div class="col-md-3 column ">
            <table class="table table-hover table-striped table-right">
                <tbody>
                <tr>
                    <td>
                        Artist Detail
                    </td>
                    <td>

                    </td>
                </tr>
                <tr class="success">
                    <td>
                        Year Of Birth
                    </td>
                    <td>
                        ${requestScope.get("artist").get(0).getYearOfBirth()}
                    </td>
                </tr>
                <tr class="error">
                    <td>
                        Year Of Death
                    </td>
                    <td>
                        ${requestScope.get("artist").get(0).getYearOfDeath()}
                    </td>
                </tr>
                <tr class="warning">
                    <td>
                        Artist Link
                    </td>
                    <td>
                        <a href="${requestScope.get("artist").get(0).getArtistLink()}">${requestScope.get("artist").get(0).getArtistLink()}</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="artworkBox">
                <%--判断，当没有作品时，显示无作品--%>
                <h4>Artworks</h4>
                <c:choose>
                    <c:when test="${requestScope.get('artwork')==null}">
                        <h4>No Artworks</h4>
                    </c:when>
                    <c:otherwise>
                        <ul>
                            <c:forEach var="artwork" items="${requestScope.get('artwork')}" begin="0" step="1">
                                <li><a title="View Details"
                                       href="http://localhost:8080/detail?id=${artwork.getArtWorkId()}">${artwork.getTitle()}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-12 column description">
            <h4>
                Detail:
            </h4>
            <p>
                ${requestScope.get("artist").get(0).getDetails()}
            </p>
        </div>
    </div>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
