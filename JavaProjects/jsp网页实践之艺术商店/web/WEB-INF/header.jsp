<%--Start nav--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-default navbar-fixed-top navbar-inverse ">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand navLogo">ArtStore</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="/index.jsp">Artworks <span class="sr-only">(current)</span></a></li>
                <li><a href="/Artist.jsp">Artists</a></li>
                <c:choose>
                <c:when test="${sessionScope.get('user')==null}">
                <li><a href="/Login.jsp">Sign in</a></li>
                <li><a href="/Register.jsp">Sing up</a></li>
                </c:when>
                <c:otherwise>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">${sessionScope.get('user').getLastName()}<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/Profile.jsp">Your Profile</a></li>
                        <li><a href="/Cart.jsp">Your Cart</a></li>
                        <li><a href="/Record.jsp">Your Records</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="/Setting.jsp">Settings</a></li>
                        <li><a href="http://localhost:8080/SignOutServlet">Sign Out</a></li>
                    </ul>
                </li>
                </c:otherwise>
                </c:choose>
            </ul>
            <form id="navSearchForm" action="/navSearchServlet" name="navSearchForm" class="navbar-form navbar-right">
                <div class="form-group">
                    <input type="text" id="searchString" name="searchString" class="form-control" placeholder="Search Art Store">
                </div>
                <button type="button" id="navSearchBtn" class="btn btn-success">Search</button>
                <select id="navSearchSelect"  class="selectpicker" name="navSearchSelect">
                    <option value="artist">Search By Artist</option>
                    <option value="title">Search By Title</option>
                    <option value="description">Search By Description</option>
                </select>
            </form>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<%--end nav--%>
