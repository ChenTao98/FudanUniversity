package artStore.servlet;

import artStore.entity.ArtworksEntity;
import artStore.service.GetArtworkService;
import artStore.service.impl.GetArtworkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@WebServlet(name = "indexLoadServlet",value = "/indexLoadServlet")
public class indexLoadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        首页加载时，获取所有作品
        GetArtworkService GetArtworkServiceImpl =new GetArtworkServiceImpl();
        List<ArtworksEntity> artworksEntityListAll= GetArtworkServiceImpl.getAll();
        List<ArtworksEntity> artworksEntityListByBrowse= GetArtworkServiceImpl.getByBrowse();
//        设置request属性
        request.setAttribute("artworkAll", artworksEntityListAll);
        request.setAttribute("artworkByBrowse",artworksEntityListByBrowse);
//        重定向到首页
        RequestDispatcher dispatcher = request.getRequestDispatcher("./index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
