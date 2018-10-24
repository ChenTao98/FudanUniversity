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
@WebServlet(name = "navSearchServlet",value = "/navSearchServlet")
public class navSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        导航栏搜索处理，获取搜索类型、搜索字符串、搜索排序类型
        GetArtworkService getArtworkService=new GetArtworkServiceImpl();
        String type=request.getParameter("navSearchSelect");
        String inputString=request.getParameter("searchString");
        String orderType="defaultType";
//        获取搜索的结果
        List<ArtworksEntity> artworksEntityList=getArtworkService.getArtworkSearch(type,orderType,inputString);
        request.setAttribute("SearchResult",artworksEntityList);
        request.setAttribute("SearchType",type);
        request.setAttribute("inputString",inputString);
//        重定向到搜索结果页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Search.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
