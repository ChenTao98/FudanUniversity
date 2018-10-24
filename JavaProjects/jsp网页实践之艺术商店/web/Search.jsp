<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/22
  Time: 10:55
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
    <script src="js/search.js"></script>
    <%--定义特殊样式--%>
    <style>
        .navLogo {
            color: white;
            font-size: 33px;
            text-shadow: 0 0 3px #fff, 0 0 6px #fff, 0 0 10px #fff, 0 0 12px #ff00de, 0 0 23px #ff00de,
            0 0 27px #ff00de, 0 0 33px #ff00de, 0 0 49px #ff00de;
        }

        .topHeader {
            margin-top: 1.5em;
        }

        .imgDivBox {
            height: 450px;
            margin-bottom: 3em;
            overflow: auto;
        }

        .searchRadio {
            width: 20px;
            height: 20px;
        }

        span {
            font-size: 30px;
        }
        .searchSpan{
            color: blue;
        }
        label {
            margin-right: 1em;
        }
    </style>
</head>
<c:choose>
<c:when test="${requestScope.get('SearchResult')==null}">
<jsp:forward page="/index.jsp"/>
<body>
</c:when>
<c:otherwise>
<body>
</c:otherwise>
</c:choose>
<%--引入导航栏--%>
<jsp:include page="WEB-INF/header.jsp"/>
<h1 class="topHeader">Search Result</h1>
<c:choose>
    <%--判断是否有搜索结果--%>
    <c:when test="${requestScope.get('SearchResult').size()==0}">
        <br/>
        <h2>Sorry,no picture you want to find</h2>
    </c:when>
    <c:otherwise>
        <div class="col-md-12 column">
            <%--展示搜索内容--%>
            <h3>Your are Search by <span class="searchSpan">${requestScope.get("SearchType")}</span> with the key
                words <span class="searchSpan">"${requestScope.get("inputString")}"</span></h3>
        </div>
        <%--定义表单，排序选择框--%>
        <div class="col-md-12 column">
            <form role="form" action="/SearchServlet" id="SearchForm">
                <input type="hidden" name="searchType" value="${requestScope.get("SearchType")}">
                <input type="hidden" name="inputString" value="${requestScope.get("inputString")}">
                <label><input class="searchRadio" type="radio" name="orderType" value="cost"><span>Order By Cost</span></label>
                <label><input class="searchRadio" type="radio" name="orderType" value="data"><span>Order By Data</span></label>
                <label><input class="searchRadio" type="radio" name="orderType"
                              value="amount"><span>Order By amount</span></label>
                <br/>
                <button class="btn btn-primary" id="SearchButton" type="button">Search</button>
            </form>
        </div>
        <%--展示搜索结果--%>
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="row clearfix">
                        <c:forEach var="artwork" items="${requestScope.get('SearchResult')}" begin="0" step="1">
                            <div class="col-md-4 column imgDivBox">
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
                                       class="btn">
                                        <button class="btn btn-primary">View Details</button>
                                    </a>
                                </p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
