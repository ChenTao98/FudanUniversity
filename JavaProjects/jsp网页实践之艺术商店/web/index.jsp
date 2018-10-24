<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/17
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>The ArtStore</title>
    <!-- Bootstrap -->
    <%--引入样式表、js--%>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/Home.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/navSearch.js"></script>
</head>
<c:choose>
<c:when test="${requestScope.get('artworkAll')==null}">
<jsp:forward page="/indexLoadServlet"/>
<body>
</c:when>
<c:otherwise>
<body>
</c:otherwise>
</c:choose>
<%--引入导航栏--%>
<jsp:include page="WEB-INF/header.jsp"/>
<%--start most hot artwork--%>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="carousel slide" id="carousel-114332">
                <ol class="carousel-indicators">
                    <li class="active" data-target="#carousel-114332" data-slide-to="0">
                    </li>
                    <li data-target="#carousel-114332" data-slide-to="1">
                    </li>
                    <li data-target="#carousel-114332" data-slide-to="2">
                    </li>
                </ol>
                <div class="carousel-inner">
                    <div class="item active">
                        <a title="View Details"
                           href="http://localhost:8080/detail?id=${requestScope.get('artworkByBrowse').get(0).getArtWorkId()}"><img
                                alt="No image" class="img"
                                src="art-images/works/large/${requestScope.get('artworkByBrowse').get(0).getImageFileName()}.jpg"/></a>
                        <div class="carousel-caption">
                            <h4>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(0).getTitle()==null}">
                                        No Title
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(0).getTitle()}
                                    </c:otherwise>
                                </c:choose>
                            </h4>
                            <p>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(0).getDescription()==null}">
                                        No Description
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(0).getDescription()}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                    <div class="item">
                        <a title="View Details"
                           href="http://localhost:8080/detail?id=${requestScope.get('artworkByBrowse').get(1).getArtWorkId()}">
                            <img alt="" class="img"
                                 src="art-images/works/large/${requestScope.get('artworkByBrowse').get(1).getImageFileName()}.jpg"/></a>
                        <div class="carousel-caption">
                            <h4>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(1).getTitle()==null}">
                                        No Title
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(1).getTitle()}
                                    </c:otherwise>
                                </c:choose>
                            </h4>
                            <p>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(1).getDescription()==null}">
                                        No Description
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(1).getDescription()}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                    <div class="item">
                        <a title="View Details"
                           href="http://localhost:8080/detail?id=${requestScope.get('artworkByBrowse').get(2).getArtWorkId()}">
                            <img alt="" class="img"
                                 src="art-images/works/large/${requestScope.get('artworkByBrowse').get(2).getImageFileName()}.jpg"/></a>
                        <div class="carousel-caption">
                            <h4>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(2).getTitle()==null}">
                                        No Title
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(2).getTitle()}
                                    </c:otherwise>
                                </c:choose>
                            </h4>
                            <p>
                                <c:choose>
                                    <c:when test="${requestScope.get('artworkByBrowse').get(2).getDescription()==null}">
                                        No Description
                                    </c:when>
                                    <c:otherwise>
                                        ${requestScope.get('artworkByBrowse').get(2).getDescription()}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>
                <a class="left carousel-control" href="#carousel-114332" data-slide="prev"><span
                        class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control"
                                                                                href="#carousel-114332"
                                                                                data-slide="next"><span
                    class="glyphicon glyphicon-chevron-right"></span></a>
            </div>
        </div>
    </div>
</div>
<%--end most hot artwork--%>
<%--start new upload--%>
<div class="container">
    <div class="row clearfix">
        <hr>
        <h3>New Upload</h3>
        <hr>
        <div class="col-md-12 column">
            <div class="row clearfix">
                <c:forEach var="artwork" items="${requestScope.get('artworkAll')}" begin="0" end="2" step="1">
                    <div class="col-md-4 column imgDivBox">
                        <a title="View Details"
                           href="http://localhost:8080/detail?id=${artwork.getArtWorkId()}">
                            <img class="img-circle" alt="140x140"
                                 src="art-images/works/medium/${artwork.getImageFileName()}.jpg"/></a>
                        <h2>
                            <c:choose>
                                <c:when test="${artwork.getTitle()==null}">
                                    No Title
                                </c:when>
                                <c:otherwise>
                                    ${artwork.getTitle()}
                                </c:otherwise>
                            </c:choose>
                        </h2>
                        <p>
                            <c:choose>
                                <c:when test="${artwork.getDescription()==null}">
                                    No Description
                                </c:when>
                                <c:otherwise>
                                    ${artwork.getDescription()}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p>
                            <a title="View Details"
                               href="http://localhost:8080/detail?id=${artwork.getArtWorkId()}"
                               class="btn"><button class="btn btn-primary">View Details</button></a>
                        </p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<%--end new upload--%>
<%--start all work--%>
<div class="container">
    <div class="row clearfix">
        <hr>
        <h3>All works</h3>
        <hr>
        <div class="col-md-12 column">
            <div class="row clearfix">
                <c:forEach var="artwork" items="${requestScope.get('artworkAll')}" begin="3" step="1">
                    <div class="col-md-3 column imgDivBox">
                        <a title="View Details"
                           href="http://localhost:8080/detail?id=${artwork.getArtWorkId()}">
                            <img class="img-thumbnail" alt="140x140"
                                 src="art-images/works/medium/${artwork.getImageFileName()}.jpg"/></a>
                        <h2>
                            <c:choose>
                                <c:when test="${artwork.getTitle()==null}">
                                    No Title
                                </c:when>
                                <c:otherwise>
                                    ${artwork.getTitle()}
                                </c:otherwise>
                            </c:choose>
                        </h2>
                        <p>
                            <c:choose>
                                <c:when test="${artwork.getDescription()==null}">
                                    No Description
                                </c:when>
                                <c:otherwise>
                                    ${artwork.getDescription()}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p>
                            <a title="View Details"
                               href="http://localhost:8080/detail?id=${artwork.getArtWorkId()}"
                               class="btn"><button class="btn btn-primary">View Details</button></a>
                        </p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<%--end all work--%>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
