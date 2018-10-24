package artStore.servlet;

import artStore.entity.ArtworksEntity;
import artStore.service.GetArtworkService;
import artStore.service.impl.GetArtworkServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Bing Chen on 2017/7/22.
 */
@WebServlet(name = "SearchServlet",value = "/SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        搜索页面排序搜索，获取搜索类型、排序类型、搜索内容
        GetArtworkService getArtworkService=new GetArtworkServiceImpl();
        String searchType=request.getParameter("searchType");
        String inputString=request.getParameter("inputString");
        String orderType=request.getParameter("orderType");
//        通过获取的数据搜索
        List<ArtworksEntity> artworksEntityList=getArtworkService.getArtworkSearch(searchType,orderType,inputString);
//        设置request属性
        request.setAttribute("SearchResult",artworksEntityList);
        request.setAttribute("SearchType",searchType);
        request.setAttribute("inputString",inputString);
//        重定向到搜索页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Search.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
