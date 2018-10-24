<%--
  Created by IntelliJ IDEA.
  User: Bing Chen
  Date: 2017/7/18
  Time: 9:07
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
    <script src="js/DetailAdd.js"></script>
    <script src="js/navSearch.js"></script>
    <%--定义特殊样式--%>
    <style>
        .addTo {
            text-align: center;
            margin-bottom: 2em;
        }

        h1 {
            margin-top: 2em;
        }

        h3 {
            margin-top: 1.5em;
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
<div class="container">
    <div class="page-header">
        <h1>
            <%--判断是否有Title--%>
            <c:choose>
                <c:when test="${requestScope.get('imageDetail').get(0).getTitle()==null}">
                    No Title
                </c:when>
                <c:otherwise>
                    ${requestScope.get('imageDetail').get(0).getTitle()}
                </c:otherwise>
            </c:choose>
            <%--${requestScope.get('imageDetail').get(0).getTitle()}--%>
        </h1>
    </div>
    <div class="row clearfix">
        <div class="col-md-8 column DetailImage">
            <img class="img-thumbnail"
                 src="art-images/works/large/${requestScope.get('imageDetail').get(0).getImageFileName()}.jpg"/>
        </div>
        <div class="col-md-3 column ">
            <div class="row clearfix">
                <div class="col-md-12 column addTo">
                    <%--判断是否登录，展示不同的按钮--%>
                    <c:choose>
                        <c:when test="${sessionScope.get('user')==null}">
                            <button class="btn btn-lg btn-primary btn-block" id="button1"
                                    type="button">add to cart
                            </button>
                        </c:when>
                        <c:otherwise>
                            <form role="form" id="detailForm">
                                <input type="hidden" id="customerId" name="customerId"
                                       value="${sessionScope.get('user').getCustomerId()}">
                                <input type="hidden" id="artworkId" name="artworkId"
                                       value="${requestScope.get('imageDetail').get(0).getArtWorkId()}">
                                <input type="hidden" id="artworkName" name="artworkName"
                                       value="${requestScope.get('imageDetail').get(0).getTitle()}">
                                <button class="btn btn-lg btn-primary btn-block" id="button2" type="button">add to cart
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <%--展示详细信息--%>
            <table class="table table-hover table-striped table-right">
                <tbody>
                <tr>
                    <td>
                        Product Details
                    </td>
                    <td>

                    </td>
                </tr>
                <tr class="success">
                    <td>
                        Date
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getYearOfWork()}
                    </td>
                </tr>
                <tr class="error">
                    <td>
                        Medium
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getMedium()}
                    </td>
                </tr>
                <tr class="warning">
                    <td>
                        Width
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getWidth()}
                    </td>
                </tr>
                <tr class="info">
                    <td>
                        Height
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getHeight()}
                    </td>
                </tr>
                <tr class="success">
                    <td>
                        Home
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getOriginalHome()}
                    </td>
                </tr>
                <tr class="error">
                    <td>
                        Genres
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${requestScope.get('imageGenres')==null}">
                            </c:when>
                            <c:otherwise>
                                ${requestScope.get('imageGenres').get(0).getGenreName()}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="warning">
                    <td>
                        Subject
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${requestScope.get('imageSubject')==null}">
                            </c:when>
                            <c:otherwise>
                                ${requestScope.get('imageSubject').get(0).getSubjectName()}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="info">
                    <td>
                        Costs
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getCost()}
                    </td>
                </tr>
                <tr class="success">
                    <td>
                        MSRP
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getMsrp()}
                    </td>
                </tr>
                <tr class="error">
                    <td>
                        Artist
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getArtistName()}
                    </td>
                </tr>
                <tr class="warning">
                    <td>
                        Heat
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getAmount()}
                    </td>
                </tr>
                <tr class="info">
                    <td>
                        Link
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getArtWorkLink()}
                    </td>
                </tr>
                <tr class="success">
                    <td>
                        Google Link
                    </td>
                    <td>
                        ${requestScope.get('imageDetail').get(0).getGoogleLink()}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-12 column description">
            <h4>
                Description:
            </h4>
            <p>
                <c:choose>
                    <c:when test="${requestScope.get('imageDetail').get(0).getDescription()==null}">
                        No Description
                    </c:when>
                    <c:otherwise>
                        ${requestScope.get('imageDetail').get(0).getDescription()}
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>
    <%--社交分享--%>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <!-- JiaThis Button BEGIN -->
            <h3>Share By</h3>
            <div class="jiathis_style_24x24">
                <a class="jiathis_button_qzone"></a>
                <a class="jiathis_button_tsina"></a>
                <a class="jiathis_button_tqq"></a>
                <a class="jiathis_button_weixin"></a>
                <a class="jiathis_button_renren"></a>
                <a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis"
                   target="_blank"></a>
                <a class="jiathis_counter_style"></a>
            </div>
            <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js" charset="utf-8"></script>
            <!-- JiaThis Button END -->
        </div>
    </div>
</div>
<%--引入页脚--%>
<jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
